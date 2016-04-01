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
import org.springframework.web.multipart.MultipartFile;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.UserDoctorsModel;
import com.awsmu.service.UserDoctorService;
import com.google.gson.Gson;

/**
 * User's Doctor controller to handles doctor section.
 */
@Controller
public class UserDoctorController {
	
	/**
	 * Injecting UserDoctorServiceImpl 
	 */
	@Autowired(required = true)
    @Qualifier(value = "UserDoctorService")
	private UserDoctorService userDoctorService;
		
	/*
	 *Created Date: 14-Oct-2015
     *Purpose: get user's doctors based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/userDoctorsList", method = RequestMethod.POST)
	public @ResponseBody String userDoctorsList(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
				
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"), HashMap.class);

		// get admin id from session if logged in  
		if(session.getAttribute("userId")==null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(userDoctorService.getUserDoctorsList((page-1)*displayRows, displayRows, page, sidx, sord, filters));
	}
	
	/*
	 *Created Date: 14-Oct-2015
     *Purpose: get user's doctor detail
     */ 	 
	@RequestMapping(value = "/getUserDoctorDetail/{doctorId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getUserDoctorDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String doctorId) {				
		//get user's doctor detail 
		return userDoctorService.getUserDoctorById(doctorId);
			
	}
	
	/*
	 *Created Date: 14-Oct-2015
     *Purpose: add doctor with image
     */ 	 
	@RequestMapping(value = "/saveImageUserDoctor", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveImageUserDoctor(HttpServletRequest request, HttpServletResponse response, @RequestParam("doctorDetail") String doctorDetail, @RequestParam(value = "file", required=false) MultipartFile file) {		
		UserDoctorsModel userDoctorModel = new Gson().fromJson(doctorDetail, UserDoctorsModel.class);
		return userDoctorService.saveUserDoctor(userDoctorModel, request.getParameter("imageUpload"), file);
	}
	
	/*
	 *Created Date: 14-Oct-2015
     *Purpose: add doctor without image
     */ 	 
	@RequestMapping(value = "/saveUserDoctor", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveUserDoctor(HttpServletRequest request, HttpServletResponse response, @RequestParam("doctorDetail") String doctorDetail) {		
		MultipartFile file = null;
		UserDoctorsModel userDoctorModel = new Gson().fromJson(doctorDetail, UserDoctorsModel.class);
		return userDoctorService.saveUserDoctor(userDoctorModel, request.getParameter("imageUpload"), file);
	}
		
	/*
	 *Created Date: 14-Oct-2015
     *Purpose: delete doctor 
     */ 	 
	@RequestMapping(value = "/deleteUserDoctor", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteUserDoctor(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = userDoctorService.deleteUserDoctorById(request.getParameter("doctorId"));
    	
    	return ajaxResponse;
	}
	
	/*
	 *Created Date: 14-Oct-2015
     *Purpose: get attributes list 
     */ 	 
	@RequestMapping(value = "/userDoctorsAttributesList", method = RequestMethod.GET)
	public @ResponseBody AjaxResponse userDoctorsAttributesList(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = userDoctorService.attributesList();
    	
    	return ajaxResponse;
	}
}
