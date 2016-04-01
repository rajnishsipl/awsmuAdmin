package com.awsmu.dao;

import java.util.List;







import com.awsmu.entity.Celebrities;
import com.awsmu.exception.AwsmuException;




public interface CelebritiesDao {

	
	/**
	 * get celebrities list
	 */
	public List<Celebrities> getCelebrities(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;
	
	/**
	 * get celebrities count based on the pagination criteria
	 */
	public  int getCelebritiesCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * get celebrity  details by id
	 */
	public Celebrities getCelebrityById(String questionsId) throws AwsmuException;
	
	/**
	 * save new celebrity
	 */
	public void saveCelebrity(Celebrities celebrity) throws AwsmuException;
	
	/**
	 * update celebrity 
	 */
	public void updateCelebrity(Celebrities celebrity) throws AwsmuException;
	
	/**
	 * delete celebrity 
	 */
	public  void deleteCelebrityById(String id) throws AwsmuException;
	
}
