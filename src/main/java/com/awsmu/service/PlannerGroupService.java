package com.awsmu.service;

import java.util.Map;

import com.awsmu.entity.PlannerGroupMember;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;

public interface PlannerGroupService {
	/**
	 * get planner group list
	 */
	public  GridResponse getPlannerGroupMembers(String problemId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	
	/**
	 * get planner group post list
	 */
	public  GridResponse getPlannerGroupPosts(String problemId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	
	
	/**
	 * get planner group details
	 */
	public AjaxResponse getPlannerGroupDetail(String problemId);
	
	
	/**
	 * get planner group top members list
	 */
	public AjaxResponse getPlannerGroupTopMember(String problemId);
	
	/**
	 * save planner group details after validation
	 */
	public AjaxResponse submitPlannerGroupEdit(String plannerGroupId,PlannerGroupMember plannerGroupModel);
	
	/**
	 * change status of planner group
	 */
	public AjaxResponse activeInactivePlannerGroup(String plannerGroupId,int status);
	/**
	 * get planner group details
	 */
	public AjaxResponse getPlannerGroupDetail(String problemId, String problemId2);
	
	/**
	 * make database changes for one time
	 */
	
	public String makeDatabaseChanges();
}
