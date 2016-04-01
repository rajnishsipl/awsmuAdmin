package com.awsmu.dao;

import java.util.List;







import com.awsmu.entity.Activities;
import com.awsmu.entity.Friends;
import com.awsmu.exception.AwsmuException;


public interface ConnectionsDao {
	
	/**
	 * get connections list  
	 */
	public List<Friends> getConnections(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;

	/**
	 * function return the total number of records in friends collection
	 */
	public  int getConnectionsCount(List<Object> searchList) throws AwsmuException;

	/**
	 * get connection details by id  
	 */
	
	public Friends getConnectionDetailById(String id) throws AwsmuException;
	
	/**
	 * update connections 
	 */
	public void updateConnection(Friends friends) throws AwsmuException;
	
	/**
	 * delete activity 
	 */
	//public  void deleteActivityById(String id) throws AwsmuException;
	
}
