package com.awsmu.service;

import java.util.Map;

import com.awsmu.entity.Planners;
import com.awsmu.entity.UserPlanners;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PlannersModel;

/**
 * PlannerService service interface
 */
public interface PlannerService {

	/**
	 * get user's planner list
	 */
	public  GridResponse getUserPlannersList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters, String userId);
	
	/**
	 * get all user's planner list
	 */
	public  GridResponse getAllUserPlannersList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters);

	
	/**
	 * get planners list
	 */
	public  GridResponse getPlannersList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters);
	
	/** 
	 * get planner detail
	 */
	public  AjaxResponse getPlannerDetailById(String plannerId);
	
	/**
	 * get user planner detail
	 */
	public  AjaxResponse getUserPlannerDetailById(String userPlannerId);
	
	/**
	 * get user planner actions
	 */
	public  AjaxResponse getUserPlannerActions(String userPlannerId, String actionDate);
	
	/**
	 * submit planner actions
	 */
	public  AjaxResponse actionsSubmit(String userPlannerId, String userId, String actionDate, String activityId, String isPerformed, Integer yesCount, Integer noCount);
		
	/**
	 * edit planner
	 */
	public  AjaxResponse userPlannerEdit(UserPlanners userPlanner);
	
	/**
	 * return user planner 
	 */
	public  UserPlanners userPlanner(String userPlannerId);
	
	
	/**
	 * return planner 
	 */
	public  Planners getPlanner(String plannerId);
	
	/**
	 * update planner information 
	 */
	public  AjaxResponse updatePlannerInfo(Planners planner, String plannerId);
	
	
	/**
	 * update planner  
	 */
	public  AjaxResponse updatePlanner(PlannersModel planner);
}
