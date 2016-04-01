package com.awsmu.service;



import java.util.Map;

import com.awsmu.model.ActivitiesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;


public interface ActivitiesService {
	
	/**
	 * save activity 
	 */
	public AjaxResponse saveActivities(ActivitiesModel activity);
	
	/** 
	 * get activities based on the pagination criteria
	 */
	public GridResponse getActivities(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters); 
	
	
	/** 
	 * get activities form data
	 */
	public AjaxResponse getActivitiesFormData(); 
	
	
	/** 
	 * get activities form data
	 */
	public AjaxResponse getActivityById(String acvityId); 
	
	/**
	 * update activity 
	 */
	public AjaxResponse updateActivities(ActivitiesModel activity);
	/**
	 * delete activity by id
	 */
	public AjaxResponse deleteActivityById(String activityId);
}


