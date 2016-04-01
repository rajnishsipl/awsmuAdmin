package com.awsmu.service;

import java.util.Map;

import com.awsmu.entity.DegreeCourses;
import com.awsmu.entity.Nationality;
import com.awsmu.entity.Profession;
import com.awsmu.entity.Specialties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;

public interface AttributesService {
		
	/**
	 * get cities list based on country name
	 */
	public AjaxResponse getCitiesList(String country);
	
	/**
	 * get grid of attributes 
	 */
	public  GridResponse getAttributesGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);


	/**
	 * get country list of Attributes 
	 */
	public AjaxResponse getCountryList();
	
	/**
	 * update country
	 */
	
	public AjaxResponse updateCountry(Nationality nationality);
	
	
	/**
	 * get  list of degree courses 
	 */
	public AjaxResponse getDegreeCoursesList();
	
	
	/**
	 * update degree courses
	 */
	
	public AjaxResponse updateDegreeCourses(DegreeCourses degreeCourses);
	
	
	/**
	 * get  list of professions  
	 */
	public AjaxResponse getProfessionsList();
	
	
	/**
	 * update degree courses
	 */
	
	public AjaxResponse updateProfessions(Profession profession);
	
	/**
	 * get  list of Speciality  
	 */
	public AjaxResponse getSpecialitiesList();
	
	/**
	 * update degree courses
	 */
	
	public AjaxResponse updateSpecialities(Specialties specialties);
}
