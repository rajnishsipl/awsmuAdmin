package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.Trends;
import com.awsmu.exception.AwsmuException;

public interface TrendsDao {

	/**
	 * Get trends 
	 */
	public List <Trends> getTrends() throws AwsmuException;
	
	
	/**
	 * Get trend details by id
	 */
	public Trends getTrendDetailsById(String id) throws AwsmuException;
	
	/**
	 * get trends list for grid 
	 */	
	public List<Trends> getTrendsList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;
	
	
	/**
	 * get trends count based on the pagination criteria
	 */
	
	public  int getTrendsCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * save trend 
	 */
	public void saveTrend(Trends trends) throws AwsmuException;
	
	/**
	 * update trend 
	 */
	public void updateTrend(Trends trend) throws AwsmuException;
	
	/**
	 * check trend title exist or not 
	 */
	public  boolean checkTrendTitleExists(String trendId, String trendTitle) throws AwsmuException;
	
	/**
	 * delete trend by trend id
	 */
	public  void deleteTrendById(String id) throws AwsmuException;
}
