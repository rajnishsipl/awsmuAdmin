package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.Booklet;
import com.awsmu.exception.AwsmuException;

public interface BookletDao {
	
	/**
	 * function return the total number of records in user's booklet collection
	 */
	public  int getUserBookletsCount(List<Object> searchList, String userId) throws AwsmuException;
	
	/**
	 * get user's booklet list
	 */
	public List<Booklet> getUserBookletsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList, String userId) throws AwsmuException;
		
	/**
	 * function return total number booklets count
	 */
	public  int getBookletsCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * function return list of booklets
	 */
	public  List<Booklet> getBookletsList(Integer skipPostRecord, Integer skipPostFreq, Integer page, String sortBy, String sortOrder, List<Object> searchList) throws AwsmuException;
		
	/**
	 * get booklet detail
	 */
	public  Booklet getBookletById(String bookletId) throws AwsmuException;
	
	/**
	 * function save booklet detail
	 */
	public  void saveBooklet(Booklet booklet) throws AwsmuException;
	
	/**
	 * function to delete booklet
	 */
	public  void deleteBookletById(String bookletId) throws AwsmuException;
	
}
