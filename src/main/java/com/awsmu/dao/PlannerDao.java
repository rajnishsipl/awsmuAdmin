package com.awsmu.dao;

import java.util.List;

import com.awsmu.exception.AwsmuException;
import com.awsmu.entity.Planners;
import com.awsmu.entity.Problem;
import com.awsmu.entity.UserActivitiesCount;
import com.awsmu.entity.UserPlannerActions;
import com.awsmu.entity.UserPlanners;
import com.awsmu.model.GridResponse;

/**
 * Planner dao interface
 */
public interface PlannerDao {
	
	/**
	 * function return the total number of records in user's planner collection
	 */
	public  int getUserPlannersCount(List<Object> searchList, String userId) throws AwsmuException;
	
	/**
	 * function return list of user's planner
	 */
	public  List<UserPlanners> getUserPlannersList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList, String userId) throws AwsmuException;
	
	/**
	 * function return the total number of records in all user's planner collection
	 */
	public  int getAllUserPlannersCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * function return list of all user's planner
	 */
	public  List<UserPlanners> getAllUserPlannersList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException;

	/**
	 * function return total number planner count
	 */
	public  int getPlannersCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * function return list of planners
	 */
	public  List<Planners> getPlannersList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException;
	
	/**
	 * get planner detail
	 */
	public  Planners getPlannerDetailById(String plannerId) throws AwsmuException;
	
	/**
	 * get user planner detail
	 */
	public  UserPlanners getUserPlannerDetailById(String userPlannerId) throws AwsmuException;
	
	/**
	 * get problem detail by id
	 */
	public  Problem getProblemDetailById(String problemId) throws AwsmuException;
	
	/**
	 * get user planner actions 
	 */
	public  List<UserPlannerActions> getPlannerActions(String userPlannerId, String actionDate) throws AwsmuException;
	
	/**
	 * get user planner action count
	 */
	public  UserActivitiesCount getDailyActionsCount(String userPlannerId, String actionDate) throws AwsmuException;
	
	/**
	 * get user planner action
	 */
	public UserPlannerActions getPlannerAction(String userPlannerId, String actionDate, String activityId) throws AwsmuException;
	
	/**
	 * store action
	 */
	public String storeAction(UserPlannerActions actions) throws AwsmuException;
	
	/**
	 * store user planner daily actions count 
	 */
	public String storeDailyActionsCount(UserActivitiesCount actionsCount) throws AwsmuException;
	
	/**
	 * edit user planner
	 */
	public String userPlannerEdit(UserPlanners userPlanner) throws AwsmuException;
	
	/**
	 * edit planner info
	 */
	public  void updatePlannerInfo(Planners planner, String plannerId) throws AwsmuException;
	
	
	/**
	 * update planner 
	 */
	public void updatePlanner(Planners planner) throws AwsmuException;
	
}
