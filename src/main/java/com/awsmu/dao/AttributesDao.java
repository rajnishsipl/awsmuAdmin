package com.awsmu.dao;


import java.util.List;

import com.awsmu.entity.Attributes;
import com.awsmu.entity.DegreeCourses;
import com.awsmu.entity.Nationality;
import com.awsmu.entity.Profession;
import com.awsmu.entity.Specialties;
import com.awsmu.entity.Trends;
import com.awsmu.exception.AwsmuException;
import com.awsmu.entity.City;

public interface AttributesDao {
	
	/**
	 * get nationality list  
	 */
	public Nationality getNatioanlityList() throws AwsmuException;
	
	/**
	 * get profession list  
	 */
	public Profession getProfessionList() throws AwsmuException;
	
	/**
	 * get specialities list  
	 */
	public Specialties getSpecialtiesList() throws AwsmuException;
	
	/**
	 * get Degree list  
	 */
	public DegreeCourses getDegreeCoursesList() throws AwsmuException;
	
	
	/**
	 * get City list  
	 */
	public City getCitiesList(String country) throws AwsmuException;
	
	
	/**
	 * get Attributes count  
	 */
	public  int getAttributesCount(List<Object> searchList) throws AwsmuException;

	/**
	 * get Attributes list  grid
	 */
	public List<Attributes> getAttributesGrid(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;
	
	/**
	 * get trends list  
	 */
	public List<Trends> getTrendsList() throws AwsmuException;
	
	/**
	 * update country  
	 */
	public void updateCountry( Nationality nationality) throws AwsmuException;
	
	/**
	 * update DegreeCourses  
	 */
	public void updateDegreeCourses( DegreeCourses degreeCourses) throws AwsmuException;
	
	/**
	 * update profession 
	 */
	public void updateProfessions( Profession professions) throws AwsmuException;
	
	/**
	 * update Specialty 
	 */
	
	public void updateSpecialties( Specialties specialties) throws AwsmuException;
}
