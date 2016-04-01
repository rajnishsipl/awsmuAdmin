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
import com.awsmu.entity.Problem;
import com.awsmu.exception.AwsmuException;
@Repository("ProblemsDao")
public class ProblemsDaoImpl implements ProblemsDao {
		
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	/**
	 * Get problems 
	 */
	@Override
	public List<Problem> getProblems() throws AwsmuException{
		
		 List<Problem> problemsList;
		 Query query = new Query(Criteria.where("parent_id").ne(0).and("isActive").is(1));
		 query.fields().include("name");
		 query.fields().include("title");
		 problemsList =  mongoTemplate.find(query, Problem.class);
		 return problemsList;
	}
	
	/**
	 * Get problems 
	 */
	@Override
	public List<Problem> getParentProblems() throws AwsmuException{
		
		 List<Problem> problemsList;
		 Query query = new Query(Criteria.where("parent_id").is(0).and("isActive").is(1));
		 query.fields().include("name");
		 query.fields().include("title");
		 problemsList =  mongoTemplate.find(query, Problem.class);
		 return problemsList;
	}
	/**
	 * Get problems 
	 */
	@Override
	public List<Problem> getAllProblems() throws AwsmuException{
		
		 List<Problem> problemsList;
		 Query query = new Query(Criteria.where("isActive").is(1));
		 query.fields().include("name");
		 query.fields().include("title");
		 problemsList =  mongoTemplate.find(query, Problem.class);
		 return problemsList;
	}
	
	/**
	 * get problem list
	 */	
	@Override
	public List<Problem> getProblemsList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Problem> problemList;
		
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
			problemList =  mongoTemplate.find(query, Problem.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return problemList;
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
	 * get problem count based on the pagination criteria
	 */
	@Override
	public  int getProblemsCount(List<Object> searchList) throws AwsmuException{
		int totalProblems=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));
			totalProblems =  mongoTemplate.find(query, Problem.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalProblems;
	}
	
	
	/**
	 * save problem 
	 */
	@Override
	public void saveProblem(Problem problem) throws AwsmuException{
		try{
		   mongoTemplate.insert(problem, "problems");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	/**
	 * update problem 
	 */
	@Override
	public void updateProblem(Problem problem) throws AwsmuException{
		try{
		   mongoTemplate.save(problem, "problems");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	
	/**
	 * get problem detail by id
	 */
	@Override
	public  Problem getProblemDetailById(String problemId) throws AwsmuException{
		Problem problem;
		try{
			Query query = new Query(Criteria.where("_id").is(problemId));		 
			problem =  mongoTemplate.findOne(query, Problem.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return problem;
	}
	
	
	

	/**
	 * check problem name exist or not 
	 */
	@Override
	public  boolean checkProblemNameExists(String problemId, String problemName) throws AwsmuException{
		Problem problem;
		try{
			Query query = new Query();
			if(problemId == null || problemId == ""  || problemId.isEmpty())
				query.addCriteria(Criteria.where("name").is(problemName));	 
			else
				query.addCriteria(Criteria.where("name").is(problemName).and("_id").ne(problemId));
			
			problem =  mongoTemplate.findOne(query, Problem.class);
			
			if(problem!=null)
				return true;
			else
				return false;
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
	}
	
	/**
	 * delete problem
	 */
	@Override
	public  void deleteProblemById(String id) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Problem.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
}
