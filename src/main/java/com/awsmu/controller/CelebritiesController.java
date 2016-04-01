package com.awsmu.controller;

import java.util.Date;
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
import com.awsmu.model.CelebritiesModel;
import com.awsmu.model.GridResponse;

import com.awsmu.service.CelebritiesService;
import com.google.gson.Gson;

@RequestMapping("/celebrities")

@Controller
public class CelebritiesController {

	
	/**
	 * Injecting celebrities Service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "CelebritiesService")
	private CelebritiesService celebritiesService;
	
	/*  
 		*Created Date: 03-Nov-2015
 		*Purpose:Function to get grid of celebrities  
     */ 	 
 @RequestMapping(value = "/getGrid", method = RequestMethod.POST)
 public @ResponseBody String getCelabritiesGrid(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		// Get all celebrities from database 	
		
		
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
		
		return new Gson().toJson(celebritiesService.getCelabritiesGrid((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}

 
 /*  
	*Created Date: 09-Nov-2015
	*Purpose:Function to delete celebrity
	*/ 
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse deleteQuestion(@RequestParam("celebrityId") String celebrityId,HttpSession session,AjaxResponse ajaxResponse) {
		/*delete data from database*/ 	
		ajaxResponse = celebritiesService.deleteCelebrityById(celebrityId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
 
 
 /*  
	*Created Date: 03-Nov-2015
	*Purpose:Function to get celebrity detail
	*/ 
	@RequestMapping(value = "/getDetail/{celebId}", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse getCelebrityDetail(@PathVariable String celebId,HttpSession session,AjaxResponse ajaxResponse) {
		/*Get celebrity details from database*/ 	
		ajaxResponse = celebritiesService.getCelebrityById(celebId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 30-Sep-2015
	*Purpose:Function to add new celebrity  
	*/ 	 
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse addCelebrity(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("celebrity") String celebrity,
			 @RequestParam(value = "mainImage", required=true) MultipartFile mainImage) {
				
		
				// Convert celebrity json to celebrity model	
				CelebritiesModel  celebritiesModel = new Gson().fromJson(celebrity, CelebritiesModel.class);
				Date date =  new Date();
				celebritiesModel.setCreatedDate(date);
				celebritiesModel.setUpdatedDate(date);
				
				
				// Save trend
				ajaxResponse = celebritiesService.saveCelebrity(celebritiesModel,mainImage);
				
				return ajaxResponse;
	
	}
	
	/*  
	*Created Date: 30-Sep-2015
	*Purpose:Function to update celebrity with images  
	*/ 	 
	@RequestMapping(value = "/editCelebrityImage", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse editCelebrityImage(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("celebrity") String celebrity,
			 @RequestParam(value = "mainImage", required=true) MultipartFile mainImage) {
				
		
				// Convert celebrity json to celebrity model	
				CelebritiesModel  celebritiesModel = new Gson().fromJson(celebrity, CelebritiesModel.class);
				Date date =  new Date();
				celebritiesModel.setUpdatedDate(date);
				
				// Save celebrity
				ajaxResponse = celebritiesService.updateCelebrity(celebritiesModel,"1",mainImage);
				
				return ajaxResponse;
	
	}
	
	/*  
	*Created Date: 30-Sep-2015
	*Purpose:Function to add new celebrity  
	*/ 	 
	@RequestMapping(value = "/editCelebrity", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse editCelebrity(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("celebrity") String celebrity) {
				
		
				// Convert celebrity json to celebrity model	
				CelebritiesModel  celebritiesModel = new Gson().fromJson(celebrity, CelebritiesModel.class);
				Date date =  new Date();
				celebritiesModel.setUpdatedDate(date);
				
				// Update trend
				ajaxResponse = celebritiesService.updateCelebrity(celebritiesModel,"0",null);
				
				return ajaxResponse;
	
	}
	
	/*  
	*Created Date: 05-Nov-2015
	*Purpose:Function to get celebrities form data, problems   
	*/ 	 
	@RequestMapping(value = "/FormData", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getTipsFormData(HttpSession session,AjaxResponse ajaxResponse) {
		// Get tips form data from database 	
		ajaxResponse = celebritiesService.getFormData(); 
		return ajaxResponse;
	}
	
	
}
