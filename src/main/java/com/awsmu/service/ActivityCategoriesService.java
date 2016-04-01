package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.ActivityCategoriesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;

public interface ActivityCategoriesService {
	
	/** 
	 * get activity category based on the pagination criteria
	 */
	GridResponse getActivityCategoriesList(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);


	/** 
	 * save activity category 
	 */
	public AjaxResponse saveActivityCategory(ActivityCategoriesModel activitiesModel);
	

	/** 
	 * get activity category by id 
	 */
	public AjaxResponse getActivityCategoryById(String id);
	
	
	/** 
	 * save activity category 
	 */
	public AjaxResponse updateActivityCategory(ActivityCategoriesModel activitiesModel);
	
	/** 
	 * delete activity category 
	 */
	public AjaxResponse deleteActivityCategoryById(String activityCategoryId);
}