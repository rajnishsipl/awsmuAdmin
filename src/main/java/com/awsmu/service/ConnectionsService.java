package com.awsmu.service;



import java.util.Map;


import com.awsmu.model.AjaxResponse;
import com.awsmu.model.FriendsModel;
import com.awsmu.model.GridResponse;


public interface ConnectionsService {
	
	/**
	 * save activity 
	 */
	//public AjaxResponse saveActivities(ActivitiesModel activity);
	
	/** 
	 * get connections based on the pagination criteria
	 */
	public GridResponse getConnections(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters); 
	
	
	/** 
	 * get user connections based on the pagination criteria
	 */
	public GridResponse getUserConnections(String userId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters); 
	
	
	/**
	 * Get connection by  id 	 
	 * */
	public AjaxResponse getConnectionDetailById(String connectionId);
	
	/**
	 * update connection  	 
	 * */
	public AjaxResponse updateConnection(FriendsModel friendsModel);
	
}


