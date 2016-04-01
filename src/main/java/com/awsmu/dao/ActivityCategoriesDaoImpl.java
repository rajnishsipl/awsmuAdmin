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
import com.awsmu.entity.ActivityCategories;
import com.awsmu.exception.AwsmuException;

@Repository("ActivityCategoriesDao")
public class ActivityCategoriesDaoImpl implements ActivityCategoriesDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	/**
	 * Get category list 
	 */
	@Override
	public List<ActivityCategories> getActivityCategories() throws AwsmuException{
		try{
			List<ActivityCategories> categoryList;
			Query query = new Query(Criteria.where("isActive").is(1));	
			query.addCriteria(Criteria.where("isDeleted").is(0));
			categoryList =  mongoTemplate.find(query, ActivityCategories.class);
			return categoryList;
		}
		catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
	}
	
	
	/**
	 * get activities Category list
	 */	
	@Override
	public List<ActivityCategories> getActivityCategoriesList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<ActivityCategories> categoryList;
		
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
			categoryList =  mongoTemplate.find(query, ActivityCategories.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return categoryList;
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
	 * get activities Category count based on the pagination criteria
	 */
	@Override
	public  int getActivityCategoryCount(List<Object> searchList) throws AwsmuException{
		int totalCategory=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);		
			query.addCriteria(Criteria.where("isDeleted").ne(1));	
			totalCategory =  mongoTemplate.find(query, ActivityCategories.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalCategory;
	}
	
	
	/**
	 * save activities category
	 */
	@Override
	public void saveActivityCategory(ActivityCategories category) throws AwsmuException{
		try{
		   mongoTemplate.insert(category, "activityCategories");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	/**
	 * get activity category details by id  
	 */
	@Override
	public ActivityCategories getActivityCategoryById(String id) throws AwsmuException{
		try{
				// Search category id 
				Query query = new Query(Criteria.where("_id").is(id));
				return   mongoTemplate.findOne(query,ActivityCategories.class);
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
		
	}
	
	
	/**
	 * update activities category
	 */
	@Override
	public void updateCategories(ActivityCategories category) throws AwsmuException{
		try{
		   mongoTemplate.save(category, "activityCategories");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	/**
	 * check problem name exist or not 
	 */
	@Override
	public  boolean checkCategoryNameExists(String activityCategoryId, String categoryName) throws AwsmuException{
		ActivityCategories activityCategory;
		try{
			Query query = new Query();
			if(activityCategoryId == null || activityCategoryId == ""  || activityCategoryId.isEmpty())
				query.addCriteria(Criteria.where("category").is(categoryName));	 
			else
				query.addCriteria(Criteria.where("category").is(categoryName).and("_id").ne(activityCategoryId));
			
			activityCategory =  mongoTemplate.findOne(query, ActivityCategories.class);
			
			if(activityCategory!=null)
				return true;
			else
				return false;
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
	}
	
	
	
	/**
	 * delete activity category
	 */
	@Override
	public  void deleteActivityCategoryById(String id) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, ActivityCategories.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
}
