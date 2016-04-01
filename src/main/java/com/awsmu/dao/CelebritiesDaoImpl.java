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
import com.awsmu.entity.Celebrities;
import com.awsmu.exception.AwsmuException;



@Repository(value = "CelebritiesDao")
public class CelebritiesDaoImpl implements CelebritiesDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	
	
	
	/**
	 * get celebrities list
	 */	
	@Override
	public List<Celebrities> getCelebrities(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Celebrities> celebritiesList;
		
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
			celebritiesList =  mongoTemplate.find(query, Celebrities.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return celebritiesList;
	}
	
	
	
	
	/**
	 * get celebrities count based on the pagination criteria
	 */
	@Override
	public  int getCelebritiesCount(List<Object> searchList) throws AwsmuException{
		int total=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			
			query.addCriteria(Criteria.where("isDeleted").ne(1));	
			total =  mongoTemplate.find(query, Celebrities.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return total;
	}
	
	
	/**
	 * get user based on the pagination criteria
	 */
	
	/*function add search parameters as per user request*/
	public Query addSearchFilter(Query query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
			boolField.add("isDeleted");
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
	 * get question details by id  
	 */
	@Override
	public Celebrities getCelebrityById(String questionsId) throws AwsmuException{
		try{
				// Search celebrities id 
				Query query = new Query(Criteria.where("_id").is(questionsId));
				return   mongoTemplate.findOne(query,Celebrities.class);
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
		
	}
	
	/**
	 * Save new Celebrity
	 */
	@Override
	public void saveCelebrity(Celebrities celebrity) throws AwsmuException{
		
		try{
			   mongoTemplate.insert(celebrity, "celebrities");
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
	}
	
	/**
	 * update  Celebrity
	 */
	@Override
	public void updateCelebrity(Celebrities celebrity) throws AwsmuException{
		
		try{
			   mongoTemplate.save(celebrity, "celebrities");
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
	}
	
	
	
	/**
	 * delete celebrity
	 */
	@Override
	public  void deleteCelebrityById(String id) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Celebrities.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	
}
