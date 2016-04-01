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
import com.awsmu.entity.Trends;
import com.awsmu.exception.AwsmuException;

@Repository(value = "TrendsDao")
public class TrendsDaoImpl implements TrendsDao {
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	/*get trends details*/	
	@Override
	public List <Trends> getTrends() throws AwsmuException{
		
		try{
			List<Trends> trendList;
			Query query = new Query(Criteria.where("isActive").is(1));		 
			trendList =  mongoTemplate.find(query, Trends.class);
			return trendList;
		}
		catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
	}
	
	
	/*get trends details by Id*/	
	@Override
	public Trends getTrendDetailsById(String id) throws AwsmuException{
		
		try{
			Trends trend;
			Query query = new Query(Criteria.where("_id").is(id));		 
			trend =  mongoTemplate.findOne(query, Trends.class);
			return trend;
		}
		catch(Exception e){
			   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
	}
	
	
	/**
	 * get trends list for grid 
	 */	
	@Override
	public List<Trends> getTrendsList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Trends> trendsList;
		
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
			trendsList =  mongoTemplate.find(query, Trends.class);
		}
	    catch(Exception e){
	   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
		return trendsList;
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
	 * get trends count based on the pagination criteria
	 */
	@Override
	public  int getTrendsCount(List<Object> searchList) throws AwsmuException{
		int totalTrends=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);	
			query.addCriteria(Criteria.where("isDeleted").ne(1));	
			
			totalTrends =  mongoTemplate.find(query, Trends.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalTrends;
	}
	
	
	/**
	 * save trend 
	 */
	@Override
	public void saveTrend(Trends trends) throws AwsmuException{
		try{
		   mongoTemplate.insert(trends, "trends");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	
	/**
	 * update trend 
	 */
	@Override
	public void updateTrend(Trends trend) throws AwsmuException{
		try{
		   mongoTemplate.save(trend, "trends");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
	
	
	/**
	 * check problem name exist or not 
	 */
	@Override
	public  boolean checkTrendTitleExists(String trendId, String trendTitle) throws AwsmuException{
		Trends trends;
		try{
			Query query = new Query();
			if(trendId == null || trendId == ""  || trendId.isEmpty())
				query.addCriteria(Criteria.where("title").is(trendTitle));	 
			else
				query.addCriteria(Criteria.where("title").is(trendTitle).and("_id").ne(trendId));
			
			trends =  mongoTemplate.findOne(query, Trends.class);
			
			if(trends!=null)
				return true;
			else
				return false;
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		
	}
	
	/**
	 * delete trend
	 */
	@Override
	public  void deleteTrendById(String id) throws AwsmuException{		
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, Trends.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
}
