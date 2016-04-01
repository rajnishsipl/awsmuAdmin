package com.awsmu.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.awsmu.config.Properties;
import com.awsmu.entity.DegreeCourses;
import com.awsmu.entity.Nationality;
import com.awsmu.entity.Profession;
import com.awsmu.entity.Specialties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.service.AttributesService;
import com.google.gson.Gson;

/**
 * User controller to handles requests for user section.
 */
@Controller
public class AttributesController {
	
	/**
	 * Injecting AttributesService 
	 */
	@Autowired(required = true)
    @Qualifier(value = "AttributesService")
	private AttributesService attributesService;
		
	/*
	 *Created Date: 1-Oct-2015
     *Purpose: get cities list
     */ 	 
	@RequestMapping(value = "/getCitiesList", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getCitiesList(@RequestParam("country") String country) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		ajaxResponse = attributesService.getCitiesList(country);
    	
    	return ajaxResponse;
	}
	
	
	
	/*  
	*Created Date: 9-Oct-2015
	*Purpose:Function to get attributes grid 
*/ 	 
@RequestMapping(value = "/getAttributesGrid", method = RequestMethod.POST)
public @ResponseBody String getAttributesGrid(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
	// Get all attributes from database 	
	
	int page = Integer.parseInt(request.getParameter("page"));
	int displayRows =  Integer.parseInt(request.getParameter("rows"));
	
	
	String sidx = request.getParameter("sidx");
	String sord =  request.getParameter("sord");		
	// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
	Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"),HashMap.class);

	// get admin id from session if logged in  
	if(session.getAttribute("userId")==null) {
		GridResponse gr = new GridResponse();
		gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
		gr.setStatus(false);
		gr.setIsLoggedIn(false);
		gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
		return new Gson().toJson(gr);
	}	
	
	return new Gson().toJson(attributesService.getAttributesGrid((page-1)*displayRows,displayRows,page,sidx,sord,filters));
}


/*
 *Created Date: 13-Oct-2015
 *Purpose: get country list
 */ 	 
@RequestMapping(value = "/getCounntryList/{attributeId}", method = RequestMethod.POST)
public @ResponseBody AjaxResponse getCounntryList(@PathVariable("attributeId") String attributeId) {		
	AjaxResponse ajaxResponse = new AjaxResponse();
	
	ajaxResponse = attributesService.getCountryList();
	
	return ajaxResponse;
}

	/*  
	*Created Date: 13-Oct-2015
	*Purpose:Function to update Country  
	*/ 
	@RequestMapping(value = "/updateCountry", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateCountry(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("countries") String countries) {
		Nationality nationality  =  new Gson().fromJson(countries,Nationality.class);
		return attributesService.updateCountry(nationality) ;
	}

	
	
	/*
	 *Created Date: 14-Oct-2015
	 *Purpose: get degree courses list
	 */ 	 
	@RequestMapping(value = "/getDegreeCoursesList/{attributeId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getDegreeCoursesList(@PathVariable("attributeId") String attributeId) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		ajaxResponse = attributesService.getDegreeCoursesList();
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 14-Oct-2015
	*Purpose:Function to update DegreeCourses  
	*/ 
	@RequestMapping(value = "/updateDegreeCourses", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateDegreeCourses(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("degreeCourses") String degreeCoursesStr) {
		DegreeCourses degreeCourses  =  new Gson().fromJson(degreeCoursesStr,DegreeCourses.class);
		return attributesService.updateDegreeCourses(degreeCourses) ;
	}
	
	
	/*
	 *Created Date: 15-Oct-2015
	 *Purpose: get profession list
	 */ 	 
	@RequestMapping(value = "/getProfessionList/{attributeId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getProfessionList(@PathVariable("attributeId") String attributeId) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		ajaxResponse = attributesService.getProfessionsList();
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 14-Oct-2015
	*Purpose:Function to update profession  
	*/ 
	@RequestMapping(value = "/updateProfessions", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateProfessions(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("professions") String professionsStr) {
		Profession profession  =  new Gson().fromJson(professionsStr,Profession.class);
		return attributesService.updateProfessions(profession) ;
	}
	
	
	/*
	 *Created Date: 15-Oct-2015
	 *Purpose: get speciality list
	 */ 	 
	@RequestMapping(value = "/getSpecialityList/{attributeId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getSpecialityList(@PathVariable("attributeId") String attributeId) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		ajaxResponse = attributesService.getSpecialitiesList();
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 14-Oct-2015
	*Purpose:Function to update Speciality  
	*/ 
	@RequestMapping(value = "/updateSpecialities", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateSpecialities(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("specialities") String specialitiesStr) {
		Specialties specialities  =  new Gson().fromJson(specialitiesStr,Specialties.class);
		return  attributesService.updateSpecialities(specialities) ;
	}
	
}
