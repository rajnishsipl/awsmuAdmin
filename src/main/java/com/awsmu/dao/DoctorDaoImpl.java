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
import com.awsmu.entity.Doctors;
import com.awsmu.exception.AwsmuException;

/**
 * Doctor dao implementation
 */
@Repository(value = "DoctorDao")
public class DoctorDaoImpl implements DoctorDao {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
			
	/**
	 * total doctors count
	 */
	
	public  int getDoctorsCount(List<Object> searchList) throws AwsmuException{
		int totalDoctors = 0;
		try{			
			Query query = new Query();
			
			query = addSearchFilter(query, searchList);	
			
			query.addCriteria(Criteria.where("isDeleted").is(0));
			
			totalDoctors =  mongoTemplate.find(query, Doctors.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalDoctors;
	}
	
		
	/**
	 * get Doctors based on the pagination criteria
	 */
	@Override
	public  List<Doctors> getDoctorsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException{
		List<Doctors> doctorsList;
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
			doctorsList =  mongoTemplate.find(query, Doctors.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return doctorsList;
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
	 * get Doctor detail
	 */
	@Override
	public  Doctors getDoctorById(String bookletId) throws AwsmuException{
		Doctors doctor;
		try{
			Query query = new Query(Criteria.where("_id").is(bookletId));		 
			doctor =  mongoTemplate.findOne(query, Doctors.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return doctor;
	}
	
	/**
	 * save doctor detail
	 */
	@Override
	public  void saveDoctor(Doctors doctor) throws AwsmuException{
		try{	
			mongoTemplate.save(doctor, "doctors");
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * delete doctor
	 */
	@Override
	public  void deleteDoctorById(String doctorId) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(doctorId));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Doctors.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
}
