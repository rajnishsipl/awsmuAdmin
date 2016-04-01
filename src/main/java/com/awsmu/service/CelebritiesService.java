package com.awsmu.service;


import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.CelebritiesModel;
import com.awsmu.model.GridResponse;

public interface CelebritiesService {
		
	
	/**
	 * update celebrities 
	 */
	public AjaxResponse updateCelebrity(CelebritiesModel celebsModel,String isImage,MultipartFile mainImage);
	
	
	/** 
	 * get celebrities based on the pagination criteria
	 */
	public  GridResponse getCelabritiesGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	/** 
	 * get celebrity details by id
	 */
	public AjaxResponse getCelebrityById(String celebrityId);
	
	/** 
	 * Add new celebrities
	 */
	public AjaxResponse saveCelebrity(CelebritiesModel celebritiesModel,MultipartFile trendIcon);
	
	/** 
	 * Get form data
	 */
	public AjaxResponse getFormData();
	
	/** 
	 * Delete celebrity by id
	 */
	public AjaxResponse deleteCelebrityById(String celebrityId);
}
