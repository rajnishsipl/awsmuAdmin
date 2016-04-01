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
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;
@Repository(value = "PostDao")
public class PostDaoImpl implements PostDao  {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<Posts> getPosts(Integer skipRecord, Integer skipFreq,
			Integer page, String sortBy, String sortOrder,
			List<Object> searchList) throws AwsmuException {
		List<Posts> postList;
		try{		
			Query query = new Query();		 
			query.limit(skipFreq);		
			query.skip(skipRecord);
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);			
			postList =  mongoTemplate.find(query, Posts.class);
		}
		catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	    } 
		return postList;
	}

	/*function return all post details based on post id*/
	@Override
	public Posts getPostById(String postId) throws AwsmuException {
		
		Posts posts;
		try{
			Query query = new Query(Criteria.where("_id").is(postId));		 
			posts =  mongoTemplate.findOne(query, Posts.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return posts;
	}

	@Override
	public int getPostCount(List<Object> searchList) throws AwsmuException {
		int totalPosts=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			totalPosts =  mongoTemplate.find(query, Posts.class).size();
		}
		catch(Exception e){
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }		
		return totalPosts;
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
	/*function add search parameters as per user request*/
	@Override
	public void submitPostEdit(String postId, Posts posts) throws AwsmuException {
		try{
			 mongoTemplate.save(posts);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}

	@Override
	public void activeInactivePost(String postId, int status)
			throws AwsmuException {
		try{			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(postId));	
			 Update update = new Update();
			 update.set("isActive", status);
			 mongoTemplate.updateFirst(query, update, Posts.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
	}

	@Override
	public void inDecPostCommentCount(String postId, int status)
			throws AwsmuException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		
		Update update = new Update();
		
		if(status==1)
		update.inc("commentCount", 1);
		else 
		update.inc("commentCount", -1);	
		mongoTemplate.updateFirst(query, update, Posts.class);		
	}

	@Override
	public void inDecPostLikeCount(String postId, int status)
			throws AwsmuException {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(postId));
		Update update = new Update();
		if(status==1)
			update.inc("likeCount", 1);
		else 
			update.inc("likeCount", -1);
		mongoTemplate.updateFirst(query, update, Posts.class);		
	}
	
}
