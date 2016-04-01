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
import com.awsmu.entity.Booklet;
import com.awsmu.exception.AwsmuException;

/**
 * Booklet dao implementation
 */
@Repository(value = "BookletDao")
public class BookletDaoImpl implements BookletDao {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	
	/**
	 * total user's booklets count
	 */
	
	public  int getUserBookletsCount(List<Object> searchList, String userId) throws AwsmuException{
		int totalUserBooklets = 0;
		try{			
			Query query = new Query();
			
			//select planners of specific user
			query.addCriteria(Criteria.where("userId").is(userId));
			
			query = addSearchFilter(query,searchList);	
						
			query.addCriteria(Criteria.where("isDeleted").is(0));
			
			totalUserBooklets =  mongoTemplate.find(query, Booklet.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalUserBooklets;
	}
	
	/**
	 * get user's booklets based on the pagination criteria
	 */
	@Override
	public  List<Booklet> getUserBookletsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList, String userId) throws AwsmuException{
		List<Booklet> userBookletsList;
		try{		
			Query query = new Query();	
			
			query = addSearchFilter(query, searchList);	
			
			query.limit(skipPostFreq);		
			query.skip(skipPostRecord);
			
			//select planners of specific user
			query.addCriteria(Criteria.where("userId").is(userId));
			
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
		
			query.addCriteria(Criteria.where("isDeleted").is(0));
			
			userBookletsList =  mongoTemplate.find(query, Booklet.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userBookletsList;
	}
		
	/**
	 * total booklets count
	 */
	
	public  int getBookletsCount(List<Object> searchList) throws AwsmuException{
		int totalBooklets = 0;
		try{			
			Query query = new Query();
			
			query = addSearchFilter(query,searchList);	
			
			query.addCriteria(Criteria.where("isDeleted").is(0));	
			
			totalBooklets =  mongoTemplate.find(query, Booklet.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalBooklets;
	}
	
		
	/**
	 * get Booklets based on the pagination criteria
	 */
	@Override
	public  List<Booklet> getBookletsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException{
		List<Booklet> bookletsList;
		try{		
			Query query = new Query();	
			
			query = addSearchFilter(query,searchList);	
			
			query.limit(skipPostFreq);		
			query.skip(skipPostRecord);
			
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			
			query.addCriteria(Criteria.where("isDeleted").is(0));
			
			bookletsList =  mongoTemplate.find(query, Booklet.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return bookletsList;
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
	 * get Booklet detail
	 */
	@Override
	public  Booklet getBookletById(String bookletId) throws AwsmuException{
		Booklet booklet;
		try{
			Query query = new Query(Criteria.where("_id").is(bookletId));		 
			booklet =  mongoTemplate.findOne(query, Booklet.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return booklet;
	}
	
	/**
	 * save booklet detail
	 */
	@Override
	public  void saveBooklet(Booklet booklet) throws AwsmuException{
		try{	
			mongoTemplate.save(booklet, "booklet");
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * delete booklet
	 */
	@Override
	public  void deleteBookletById(String bookletId) throws AwsmuException{
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(bookletId));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Booklet.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
}
