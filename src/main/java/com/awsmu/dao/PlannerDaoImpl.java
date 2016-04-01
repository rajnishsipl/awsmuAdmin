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
import com.awsmu.entity.Planners;
import com.awsmu.entity.Problem;
import com.awsmu.entity.UserActivitiesCount;
import com.awsmu.entity.UserPlannerActions;
import com.awsmu.entity.UserPlanners;
import com.awsmu.exception.AwsmuException;

/**
 * Planner dao implementation
 */
@Repository(value = "PlannerDao")
public class PlannerDaoImpl implements PlannerDao {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	
	/**
	 * total user's planners count
	 */
	
	public  int getUserPlannersCount(List<Object> searchList, String userId) throws AwsmuException{
		int totalUserPlanners=0;
		try{			
			Query query = new Query();
			
			//select planners of specific user
			query.addCriteria(Criteria.where("userId").is(userId));
			

			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));
			
			totalUserPlanners =  mongoTemplate.find(query, UserPlanners.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalUserPlanners;
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
	 * get user's planners based on the pagination criteria
	 */
	@Override
	public  List<UserPlanners> getUserPlannersList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList, String userId) throws AwsmuException{
		List<UserPlanners> userPlannersList;
		try{		
			Query query = new Query();		 
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
			

			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));
			
			userPlannersList =  mongoTemplate.find(query, UserPlanners.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userPlannersList;
	}
	
	/**
	 * total all user's planners count
	 */
	
	public  int getAllUserPlannersCount(List<Object> searchList) throws AwsmuException{
		int totalUserPlanners=0;
		try{			
			Query query = new Query();
			

			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));
			
			totalUserPlanners =  mongoTemplate.find(query, UserPlanners.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalUserPlanners;
	}
	
		
	/**
	 * get all user's planners based on the pagination criteria
	 */
	@Override
	public  List<UserPlanners> getAllUserPlannersList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException{
		List<UserPlanners> userPlannersList;
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
			
			userPlannersList =  mongoTemplate.find(query, UserPlanners.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userPlannersList;
	}
	
	/**
	 * total planners count
	 */
	
	public  int getPlannersCount(List<Object> searchList) throws AwsmuException{
		int totalPlanners = 0;
		try{			
			Query query = new Query();

			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").ne(1));	
						
			totalPlanners =  mongoTemplate.find(query, Planners.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalPlanners;
	}
	
		
	/**
	 * get planners based on the pagination criteria
	 */
	@Override
	public  List<Planners> getPlannersList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<Planners> plannersList;
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
			
			plannersList =  mongoTemplate.find(query, Planners.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return plannersList;
	}
	

	/**
	 * get planner detail
	 */
	@Override
	public  Planners getPlannerDetailById(String plannerId) throws AwsmuException{
		Planners planner;
		try{
			Query query = new Query(Criteria.where("_id").is(plannerId));		 
			planner =  mongoTemplate.findOne(query, Planners.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return planner;
	}
	
	/**
	 * get user planner detail
	 */
	@Override
	public  UserPlanners getUserPlannerDetailById(String userPlannerId) throws AwsmuException{
		UserPlanners userPlanner;
		try{
			Query query = new Query(Criteria.where("_id").is(userPlannerId));		 
			userPlanner =  mongoTemplate.findOne(query, UserPlanners.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return userPlanner;
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
	 * get user planner actions 
	 */
	@Override
	public   List<UserPlannerActions> getPlannerActions(String userPlannerId, String actionDate) throws AwsmuException{
		List<UserPlannerActions> userPlannerActions;
		try{
			
	 		Query query = new Query(Criteria.where("actionDate").is(actionDate).and("plannerId").is(userPlannerId));
	 		userPlannerActions = mongoTemplate.find(query, UserPlannerActions.class);

		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return userPlannerActions;
	}
		
	/**
	 * get user planner action count
	 */
	@Override
	public  UserActivitiesCount getDailyActionsCount(String userPlannerId, String actionDate) throws AwsmuException{
		UserActivitiesCount userActivitiesCount;
		try{
			
			// query to search id
	 		Query query = new Query(Criteria.where("userPlannerId").is(userPlannerId).and("actionDate").is(actionDate));
	 		userActivitiesCount =  mongoTemplate.findOne(query, UserActivitiesCount.class);

		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return userActivitiesCount;
	}
	
	/**
	 * get user planner action 
	 */
	@Override
	public UserPlannerActions getPlannerAction(String userPlannerId, String actionDate, String activityId) throws AwsmuException{
		UserPlannerActions userPlannerAction;
		try{
			

	 		Query query = new Query(Criteria.where("actionDate").is(actionDate).and("plannerId").is(userPlannerId).and("ActivityId").is(activityId));

	 		userPlannerAction = mongoTemplate.findOne(query, UserPlannerActions.class);

		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return userPlannerAction;
	}
	
	/**
	 * store action
	 */
	@Override
	public  String storeAction(UserPlannerActions actions){		
		mongoTemplate.save(actions, "userPlannerActions");
		return actions.get_id();
	} 
	
	/**
	 * store user planner daily actions count 
	 */
	@Override
	public  String storeDailyActionsCount(UserActivitiesCount actionsCount){		
		mongoTemplate.save(actionsCount, "userActivitiesCount");
		return actionsCount.get_id();
	} 
	
	/**
	 * edit user planner
	 */
	@Override
	public  String userPlannerEdit(UserPlanners userPlanner){		
		mongoTemplate.save(userPlanner, "userPlanners");
		return userPlanner.get_id();
	} 
	
	
	
	/**
	 *  update user profile pic
	 */
	@Override
	public  void updatePlannerInfo(Planners planner, String plannerId) throws AwsmuException{
	
		try{
			Query query = new Query();
			 query.addCriteria(Criteria.where("_id").is(plannerId));
			
			 Update update = new Update();
			 update.set("area", planner.getArea());
			 update.set("gender", planner.getGender());
			 update.set("isActive", planner.getIsActive());
			 update.set("maxAge", planner.getMaxAge());
			 update.set("minAge", planner.getMinAge());
			 update.set("maxFollowTime", planner.getMaxFollowTime());
			 update.set("minFollowTime", planner.getMinFollowTime());
			 update.set("nationality", planner.getNationality());
			 update.set("problemId", planner.getProblemId());
			 
			 mongoTemplate.updateFirst(query, update, Planners.class);
		 		
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	
	
	
	/**
	 * update planner 
	 */
	@Override
	public void updatePlanner(Planners planners) throws AwsmuException{
		try{
		   mongoTemplate.save(planners, "planners");
		}
		catch(Exception e){
		   	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
		}
		
	}
}
