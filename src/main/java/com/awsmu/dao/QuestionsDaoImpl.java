package com.awsmu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.exception.AwsmuException;
import com.awsmu.entity.Activities;
import com.awsmu.entity.Questions;
import com.awsmu.entity.UserDoctors;

/**
 * Questrions dao implementation
 */

@Repository(value = "QuestionsDao")
public class QuestionsDaoImpl implements QuestionsDao {
	
	private static Logger logger = Logger.getLogger(QuestionsDaoImpl.class);
	
	/**    
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	
	/**
	 *  get question by tag
	 */
	@Override
	public  Questions getQuestionByTag(String questionTag) throws AwsmuException{
		
		Questions question;
		
		try{
			
			Query query = new Query(Criteria.where("questionTag").is(questionTag));	 
		
			question =  mongoTemplate.findOne(query, Questions.class);
		
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return question;
	}
	
	
	/**
	 * get questions list
	 */	
	@Override
	public List<Questions> getQuestions(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Questions> questionsList;
		
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
			questionsList =  mongoTemplate.find(query, Questions.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return questionsList;
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
	 * get questions count based on the pagination criteria
	 */
	@Override
	public  int getQuestionsCount(List<Object> searchList) throws AwsmuException{
		int totalQuestions=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));		
			totalQuestions =  mongoTemplate.find(query, Questions.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalQuestions;
	}
	
	
	
	/**
	 * get question details by id  
	 */
	@Override
	public Questions getQuestionById(String questionsId) throws AwsmuException{
		try{
				// Search activities id 
				Query query = new Query(Criteria.where("_id").is(questionsId));
				return   mongoTemplate.findOne(query,Questions.class);
			}
			catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
			}
		
	}
	
	/**
	 * update Question 
	 */
	@Override
	public void updateQuestion(Questions question) throws AwsmuException{
		try{
		   mongoTemplate.save(question, "questions");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	
	/**
	 * add Question 
	 */
	@Override
	public void addQuestion(Questions question) throws AwsmuException{
		try{
		   mongoTemplate.insert(question, "questions");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	/**
	 * delete question
	 */
	@Override
	public  void deleteQuestionById(String id) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Questions.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
}