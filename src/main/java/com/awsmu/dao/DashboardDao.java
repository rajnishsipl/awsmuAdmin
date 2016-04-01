package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.ExpertRequest;
import com.awsmu.entity.Planners;
import com.awsmu.entity.UserPlanners;
import com.awsmu.entity.User;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.UserMessageAggregation;

/**
 * Dashboard dao interface
 */
public interface DashboardDao {
	
	
	/**
	 * get all users count
	 */
	public int getAllUsersCount() throws AwsmuException;
	
	/**
	 * get latest joined user
	 */
	List<User> getLatestUsers()  throws AwsmuException;
		
	/**
	 * get all messages count
	 */
	public List<UserMessageAggregation> getRecentMessages() throws AwsmuException;
	
	/**
	 * get unread message count
	 */
	public List<UserMessageAggregation> getUnreadMessages()  throws AwsmuException;
	
	/**
	 * get all planners count
	 */
	public int getAllPlannersCount() throws AwsmuException;
	
	/**
	 * get latest planners
	 */
	List<Planners> getLatestPlanners()  throws AwsmuException;

	/**
	 * get all user's planners count
	 */
	public int getAllUserPlannersCount() throws AwsmuException;
	
	/**
	 * get all user's planners
	 */
	public List<UserPlanners> getLatestUserPlanners() throws AwsmuException;
	
	/**
	 * get all experts request count
	 */
	public int getAllExpertRequestCount() throws AwsmuException;
			
	/**
	 * get experts request
	 */
	List<ExpertRequest> getLatestExpertRequest()  throws AwsmuException;
	
}
