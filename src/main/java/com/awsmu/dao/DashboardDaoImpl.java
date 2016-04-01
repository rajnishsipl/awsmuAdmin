package com.awsmu.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.entity.ExpertRequest;
import com.awsmu.entity.Planners;
import com.awsmu.entity.User;
import com.awsmu.entity.UserPlanners;
import com.awsmu.exception.AwsmuException;
import com.awsmu.entity.UserMessage;
import com.awsmu.model.UserMessageAggregation;

@Repository(value = "DashboardDao")
public class DashboardDaoImpl implements DashboardDao  {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	@Override
	public int getAllUsersCount() throws AwsmuException {
		
		int totalUsers=0;
		try{			
			Query query = new Query();
			
			query.addCriteria(Criteria.where("isDeleted").is(0).and("userRole").is(Properties.ROLE_USER));
			
			totalUsers =  mongoTemplate.find(query, User.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalUsers;
		
	}
	
	@Override
	public List<User> getLatestUsers() throws AwsmuException {
		
		List<User> userList;
		try{		
			Query query = new Query();		 
			query.limit(10);		
			query.skip(0);
			
			query.with(new Sort(Sort.Direction.DESC, "createdDate"));
			query.addCriteria(Criteria.where("isDeleted").is(0));
			 
			userList =  mongoTemplate.find(query, User.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userList;
		
	}
	
	@Override
	public List<UserMessageAggregation> getRecentMessages() throws AwsmuException {
		
		AggregationResults<UserMessageAggregation> groupResults;
		try{	
			
			Aggregation aggregation = newAggregation(
					// match(Criteria.where("toUser.userId").is("").orOperator(Criteria.where("toUser.userId")))
					match(Criteria
							.where("isActive")
							.is(1)
							.orOperator(Criteria.where("toUser.userId").is(Properties.ADMIN_ID),
									Criteria.where("fromUser.userId").is(Properties.ADMIN_ID))),
					group("chainId").last("message").as("message").last("isRead")
							.as("isRead").last("createdDate").as("createdDate")
							.last("chainId").as("chainId").last("deletedBy")
							.as("deletedBy").last("toUser.userId").as("toUserId")
							.last("toUser.username").as("toUserUserName")
							.last("toUser.userRole").as("toUserRole")
							.last("toUser.displayName").as("toUserDisplayName")
							.last("toUser.image").as("toUserImage")
							.last("fromUser.userId").as("fromUserUserId")
							.last("fromUser.username").as("fromUserUserName")
							.last("fromUser.userRole").as("fromUserRole")
							.last("fromUser.displayName").as("fromUserDisplayName")
							.last("fromUser.image").as("fromUserImage"),
					sort(Sort.Direction.DESC, "createdDate"), limit(6)
			);

			groupResults = mongoTemplate.aggregate(aggregation, UserMessage.class, UserMessageAggregation.class);
			
		
		}
		catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	    } 
		
		return groupResults.getMappedResults();
		
	}
	
	@Override
	public List<UserMessageAggregation> getUnreadMessages() throws AwsmuException {
		
		AggregationResults<UserMessageAggregation> groupResults ;
		try{		
			Aggregation aggregation = newAggregation(
										match(Criteria.where("isActive").is(1).and("toUser.userId").is(Properties.ADMIN_ID).and("isRead").is(0)), group("chainId"));

			groupResults = mongoTemplate.aggregate(aggregation, UserMessage.class, UserMessageAggregation.class);
					
		}
		catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	    } 
		return groupResults.getMappedResults();
		
	}
	
	
	@Override
	public int getAllPlannersCount() throws AwsmuException {
		
		int totalPlanners = 0;
		try{			
			Query query = new Query();		
			totalPlanners =  mongoTemplate.find(query, Planners.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalPlanners;
		
	}
	
	@Override
	public List<Planners> getLatestPlanners() throws AwsmuException {
		
		List<Planners>plannersList;
		try{		
			Query query = new Query();
			
			//select fields
			query.fields().include("_id");
	 		query.fields().include("problemId");
	 		query.fields().include("totalActivity");
	 		query.fields().include("maxAge");
	 		query.fields().include("minAge");
	 		query.fields().include("maxFollowTime");
	 		query.fields().include("minFollowTime");
	 		query.fields().include("area");
	 		query.fields().include("nationality");
	 		query.fields().include("createdDate");
	 		query.fields().include("gender");
	 		query.fields().include("isActive");
	 		
	 		//set limit
			query.limit(10);		
			query.skip(0);
			
			query.with(new Sort(Sort.Direction.DESC, "createdDate"));
			//query.addCriteria(Criteria.where("isActive").is(1));
			 
			plannersList =  mongoTemplate.find(query, Planners.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return plannersList;
		
	}
	
	
	@Override
	public int getAllUserPlannersCount() throws AwsmuException {
		
		int totalUserPlanners=0;
		try{			
			Query query = new Query();
			
			query.addCriteria(Criteria.where("deletedByUser").is(0));
			
			totalUserPlanners =  mongoTemplate.find(query, UserPlanners.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalUserPlanners;
		
	}
				
	@Override
	public List<UserPlanners> getLatestUserPlanners() throws AwsmuException {
		
		List<UserPlanners> userPlannersList;
		try{		
			Query query = new Query();	
						
			//select fields
			query.fields().include("_id");
	 		query.fields().include("problemId");
	 		query.fields().include("totalActivity");
	 		query.fields().include("plannerId");
	 		query.fields().include("plannerName");
	 		query.fields().include("plannerDescription");
	 		query.fields().include("userId");
	 		query.fields().include("endDate");
	 		query.fields().include("maxFollowTime");
	 		query.fields().include("minFollowTime");
	 		query.fields().include("area");
	 		query.fields().include("nationality");
	 		query.fields().include("age");
	 		query.fields().include("createdDate");
	 		query.fields().include("gender");
	 		query.fields().include("isActive");
	 		
	 		//set limit
			query.limit(10);		
			query.skip(0);
			
			query.with(new Sort(Sort.Direction.DESC, "createdDate"));
			query.addCriteria(Criteria.where("deletedByUser").is(0));
			 
			userPlannersList =  mongoTemplate.find(query, UserPlanners.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userPlannersList;
		
	}
	
	@Override
	public int getAllExpertRequestCount() throws AwsmuException {
		
		int totalExpertsReqCount=0;
		try{			
			Query query = new Query();
			
			query.addCriteria(Criteria.where("isActive").is(1));
			
			totalExpertsReqCount =  mongoTemplate.find(query, ExpertRequest.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalExpertsReqCount;
		
	}
	
	@Override
	public List<ExpertRequest> getLatestExpertRequest() throws AwsmuException {
		
		List<ExpertRequest> expertRequestList;
		try{		
			Query query = new Query();		 
			query.limit(10);		
			query.skip(0);
			
			query.with(new Sort(Sort.Direction.DESC, "createdDate"));
			query.addCriteria(Criteria.where("isActive").is(1));
			 
			expertRequestList =  mongoTemplate.find(query, ExpertRequest.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return expertRequestList;
				
	}
	
}