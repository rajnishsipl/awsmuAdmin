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
import com.awsmu.entity.Friends;
import com.awsmu.exception.AwsmuException;

@Repository(value = "ConnectionsDao")
public class ConnectionsDaoImpl implements ConnectionsDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;

	
	
	/**
	 * get activities list
	 */	
	@Override
	public List<Friends> getConnections(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Friends> friendsList;
		
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
			friendsList =  mongoTemplate.find(query, Friends.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return friendsList;
	}
	
	
	/*function add search parameters as per user request*/
	public Query addSearchFilter(Query query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			//boolField.add("isActive");
			
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
	 * get Friends count based on the pagination criteria
	 */
	@Override
	public  int getConnectionsCount(List<Object> searchList) throws AwsmuException{
		int totalFriends=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			//query.addCriteria(Criteria.where("isDeleted").ne(1));	
			totalFriends =  mongoTemplate.find(query, Friends.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalFriends;
	}
	
	
	
	/**
	 * get problem detail by id
	 */
	@Override
	public  Friends getConnectionDetailById(String connectionId) throws AwsmuException{
		Friends connection;
		try{
			Query query = new Query(Criteria.where("_id").is(connectionId));		 
			connection =  mongoTemplate.findOne(query, Friends.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return connection;
	}
	
	
	
	/**
	 * update connection 
	 */
	@Override
	public void updateConnection(Friends friends) throws AwsmuException{
		
		    try{
			   mongoTemplate.save(friends, "friends");
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
	}
}
