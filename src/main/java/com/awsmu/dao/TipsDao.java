package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.Tips;
import com.awsmu.exception.AwsmuException;

public interface TipsDao {
	/*get tips details by Id*/	
	public Tips getTipsDetailsById(String id) throws AwsmuException;
	
	/**
	 * get tips list for grid 
	 */	
	
	public List<Tips> getTipList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;

	/**
	 * get tips count based on the pagination criteria
	 */
	public  int getTipsCount(List<Object> searchList) throws AwsmuException;

	
	/**
	 * save tips 
	 */
	public void saveTip(Tips tips) throws AwsmuException;
	
	/**
	 * update tips 
	 */
	
	public void updateTip(Tips tips) throws AwsmuException;
	
	/**
	 * delete tip by tip id 
	 */
	
	public  void deleteTipById(String id) throws AwsmuException;
}
