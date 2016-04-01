package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.ActivityCategories;
import com.awsmu.exception.AwsmuException;

public interface ActivityCategoriesDao {
	/**
	 * Get category list 
	 */
	public List<ActivityCategories> getActivityCategories() throws AwsmuException;
	
	
	
	
	/**
	 * get activities category list for grid
	 */	
	public List<ActivityCategories> getActivityCategoriesList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;
	


	/**
	 * get activities category count based on the pagination criteria
	 */
	
	public  int getActivityCategoryCount(List<Object> searchList) throws AwsmuException;
	
	
	
	/**
	 * save category list 
	 */
	public void saveActivityCategory(ActivityCategories category) throws AwsmuException;
	
	/**
	 * gate category by id 
	 */
	
	public ActivityCategories getActivityCategoryById(String id) throws AwsmuException;
	
	
	/**
	 * update activities 
	 */
	public void updateCategories(ActivityCategories category) throws AwsmuException;

	
	/**
	 * check uniqueness of category
	 */
	public  boolean checkCategoryNameExists(String categoryId,String categoryName ) throws AwsmuException;
	
	/**
	 * delete Activity Category
	 */
	public  void deleteActivityCategoryById(String id) throws AwsmuException;
}
