
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
import com.awsmu.model.DoctorsModel;
import com.awsmu.service.DoctorService;
import com.google.gson.Gson;

/**
 * Doctor controller to handles doctor section.
 */
@Controller
public class DoctorController {
	
	/**
	 * Injecting DoctorServiceImpl 
	 */
	@Autowired(required = true)
    @Qualifier(value = "DoctorService")
	private DoctorService doctorService;
		
	/*
	 *Created Date: 12-Oct-2015
     *Purpose: get doctors based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/doctorsList", method = RequestMethod.POST)
	public @ResponseBody String doctorsList(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
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
		return new Gson().toJson(doctorService.getDoctorsList((page-1)*displayRows, displayRows, page, sidx, sord, filters));
	}
	
	/*
	 *Created Date: 12-Oct-2015
     *Purpose: get doctor detail
     */ 	 
	@RequestMapping(value = "/getDoctorDetail/{doctorId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getDoctorDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String doctorId) {				
		//get planner detail 
		return doctorService.getDoctorById(doctorId);
			
	}
	
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: add doctor with image
     */ 	 
	@RequestMapping(value = "/saveImageDoctor", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveImageDoctor(HttpServletRequest request, @RequestParam("doctorDetail") String doctorDetail, @RequestParam(value = "file", required=false) MultipartFile file) {		
		DoctorsModel doctorModel = new Gson().fromJson(doctorDetail, DoctorsModel.class);
		return doctorService.saveDoctor(doctorModel, request.getParameter("imageUpload"), file);
	}
	
	/*
	 *Created Date: 9-Oct-2015
     *Purpose: add doctor without image
     */ 	 
	@RequestMapping(value = "/saveDoctor", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveDoctor(HttpServletRequest request, @RequestParam("doctorDetail") String doctorDetail) {		
		MultipartFile file = null;
		DoctorsModel doctorModel = new Gson().fromJson(doctorDetail, DoctorsModel.class);
		return doctorService.saveDoctor(doctorModel, request.getParameter("imageUpload"), file);
	}
		
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: delete doctor 
     */ 	 
	@RequestMapping(value = "/deleteDoctor", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteDoctor(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = doctorService.deleteDoctorById(request.getParameter("doctorId"));
    	
    	return ajaxResponse;
	}
	
	/*
	 *Created Date: 13-Oct-2015
     *Purpose: get attributes list 
     */ 	 
	@RequestMapping(value = "/doctorsAttributesList", method = RequestMethod.GET)
	public @ResponseBody AjaxResponse doctorsAttributesList(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = doctorService.attributesList();
    	
    	return ajaxResponse;
	}
}
