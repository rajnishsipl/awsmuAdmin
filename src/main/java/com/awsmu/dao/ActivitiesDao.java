package com.awsmu.dao;

import java.util.List;






import com.awsmu.entity.Activities;
import com.awsmu.exception.AwsmuException;


public interface ActivitiesDao {
	/**
	 * save activities 
	 */
	public void saveActivities(Activities activity) throws AwsmuException;
	
	/**
	 * get activities list  
	 */
	public List<Activities> getActivities(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;

	/**
	 * function return the total number of records in activities collection
	 */
	public  int getActivitiesCount(List<Object> searchList) throws AwsmuException;

	/**
	 * get activity details by id  
	 */
	
	public Activities getActivityById(String id) throws AwsmuException;
	
	/**
	 * update activities 
	 */
	public void updateActivities(Activities activity) throws AwsmuException;
	
	/**
	 * delete activity 
	 */
	public  void deleteActivityById(String id) throws AwsmuException;
	
}
