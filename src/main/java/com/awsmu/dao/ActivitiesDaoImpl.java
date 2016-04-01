package com.awsmu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.entity.Activities;
import com.awsmu.exception.AwsmuException;

@Repository(value = "ActivitiesDao")
public class ActivitiesDaoImpl implements ActivitiesDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;

	/**
	 * save activities 
	 */
	@Override
	public void saveActivities(Activities activity) throws AwsmuException{
		try{
		   mongoTemplate.insert(activity, "activities");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	
	/**
	 * get activities list
	 */	
	@Override
	public List<Activities> getActivities(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Activities> activitiesList;
		
		try{
			Query query = new Query();		 
			query.limit(skipPostFreq);		
			query.skip(skipPostRecord);
			
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));
			activitiesList =  mongoTemplate.find(query, Activities.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return activitiesList;
	}
	
	
	/*function add search parameters as per user request*/
	public Query addSearchFilter(Query query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
			
		   List<Object> filterList =  searchList;			   
		   for (Object rows : filterList) {						   
			   Map<String, String> row = (Map) rows;					   
			   if(boolField.indexOf(row.get("field").toString())!=-1) {
				   query.addCriteria(Criteria.where(row.get("field").toString()).is(Integer.parseInt(row.get("data"))));					  
			   } 
			   else if(row.get("field").toString().equals("_id")){
				   query.addCriteria(Criteria.where(row.get("field").toString()).is(row.get("data")));
			   }
			   else {
				   query.addCriteria(Criteria.where(row.get("field").toString()).regex(WordUtils.capitalize(row.get("data").toString())));
			   }				   
		   }						   
		}	
		return query;
	}
	
	/**
	 * get activities count based on the pagination criteria
	 */
	@Override
	public  int getActivitiesCount(List<Object> searchList) throws AwsmuException{
		int totalActivities=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));	
			totalActivities =  mongoTemplate.find(query, Activities.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalActivities;
	}
	
	
	
	
	/**
	 * get activity details by id  
	 */
	@Override
	public Activities getActivityById(String activityId) throws AwsmuException{
		try{
				// Search activities id 
				Query query = new Query(Criteria.where("_id").is(activityId));
				return   mongoTemplate.findOne(query,Activities.class);
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
		
	}
	
	
	
	/**
	 * update activities 
	 */
	@Override
	public void updateActivities(Activities activity) throws AwsmuException{
		try{
		   mongoTemplate.save(activity, "activities");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	/**
	 * delete activity
	 */
	@Override
	public  void deleteActivityById(String id) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Activities.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
}
