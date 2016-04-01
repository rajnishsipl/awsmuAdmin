package com.awsmu.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.TrendsModel;




public interface TrendsService {
	/**
	 * get trends based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	
	public  GridResponse getTrendsGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);

	/**
	 * save trend 
	 */
	
	public AjaxResponse saveTrend(TrendsModel trendsModel,MultipartFile trendIcon);
	
	/**
	 * update trend 
	 */
	
	public AjaxResponse updateTrend(TrendsModel trendsModel,String isFile,MultipartFile trendIcon);
	
	/**
	 * Get trend by trend id  
	 */
	
	public AjaxResponse getTrendById(String trendId);
	
	/**
	 * Delete trend by trend id  
	 */
	
	public AjaxResponse deleteTrendById(String trendId);
}
