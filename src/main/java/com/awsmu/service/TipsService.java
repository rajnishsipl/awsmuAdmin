package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.TipsModel;

public interface TipsService {
	/**
	 * get tips based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	
	public  GridResponse getTipsGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);

	
	/**
	 * save tip 
	 */
	public AjaxResponse saveTip(TipsModel tipsModel);
	
	
	/**
	 * update tip 
	 */
	public AjaxResponse updateTip(TipsModel tipsModel);
	
	/**
	 * Get tip by tip id  
	 */
	public AjaxResponse getTipsById(String tipId);
	
	/**
	 * Get tip form data  
	 */
	public AjaxResponse getTipsFormData();
	
	/**
	 * Delete tip by id  
	 */
	
	public AjaxResponse deleteTipById(String tipId);
}
