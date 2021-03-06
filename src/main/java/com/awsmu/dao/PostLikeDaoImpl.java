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
import com.awsmu.entity.PostComments;
import com.awsmu.entity.PostLikes;
import com.awsmu.exception.AwsmuException;
@Repository(value="PostLikeDao")
public class PostLikeDaoImpl implements PostLikeDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	@Override
	public List<PostLikes> getPostLikes(String postId,
			Integer skipRecord, Integer skipFreq, Integer page,
			String sortBy, String sortOrder, List<Object> searchList)
			throws AwsmuException {
		List<PostLikes> postLikeList;
		try{		
			Query query = new Query();
			query.addCriteria(Criteria.where("postId").is(postId));
			query.limit(skipFreq);		
			query.skip(skipRecord);
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);
			System.out.println(query.toString());
			postLikeList =  mongoTemplate.find(query, PostLikes.class);
		}
		catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	    } 
		return postLikeList;
	}

	@Override
	public int getPostLikeCount(String postId, List<Object> searchList)
			throws AwsmuException {
		int totalPosts=0;
		try{			
			Query query = new Query();
			query.addCriteria(Criteria.where("postId").is(postId));
			query = addSearchFilter(query,searchList);
			System.out.println(query.toString());
			totalPosts =  mongoTemplate.find(query, PostLikes.class).size();
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

	@Override
	public void activeInactiveLike(String likeId, int status)
			throws AwsmuException {
		try{			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(likeId));	
			 Update update = new Update();
			 update.set("isActive", status);
			 mongoTemplate.updateFirst(query, update, PostLikes.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
	}

}
