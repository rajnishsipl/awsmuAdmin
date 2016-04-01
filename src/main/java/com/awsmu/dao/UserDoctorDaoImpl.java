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
import com.awsmu.entity.UserDoctors;
import com.awsmu.exception.AwsmuException;

/**
 * User's Doctor dao implementation
 */
@Repository(value = "UserDoctorDao")
public class UserDoctorDaoImpl implements UserDoctorDao {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
			
	/**
	 * total user's doctors count
	 */
	public  int getUserDoctorsCount(List<Object> searchList) throws AwsmuException{
		int totalDoctors = 0;
		try{			
			Query query = new Query();
			
			query = addSearchFilter(query, searchList);	
				
			query.addCriteria(Criteria.where("isDeleted").is(0));
			
			totalDoctors =  mongoTemplate.find(query, UserDoctors.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalDoctors;
	}
	
		
	/**
	 * get User's Doctors based on the pagination criteria
	 */
	@Override
	public  List<UserDoctors> getUserDoctorsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException{
		List<UserDoctors> userDoctorsList;
		try{		
			Query query = new Query();		
			
			query = addSearchFilter(query, searchList);	
			
			query.limit(skipPostFreq);		
			query.skip(skipPostRecord);
			
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
					
			query.addCriteria(Criteria.where("isDeleted").is(0));
			userDoctorsList =  mongoTemplate.find(query, UserDoctors.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userDoctorsList;
	}
	
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
	 * get User's dpctor detail
	 */
	@Override
	public  UserDoctors getUserDoctorById(String doctorId) throws AwsmuException{
		UserDoctors userDoctor;
		try{
			Query query = new Query(Criteria.where("_id").is(doctorId));		 
			userDoctor =  mongoTemplate.findOne(query, UserDoctors.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return userDoctor;
	}
	
	/**
	 * save User's doctor detail
	 */
	@Override
	public  void saveUserDoctor(UserDoctors userDoctor) throws AwsmuException{
		try{	
			mongoTemplate.save(userDoctor, "userDoctors");
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * delete user's doctor
	 */
	@Override
	public  void deleteUserDoctorById(String doctorId) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(doctorId));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, UserDoctors.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
}
