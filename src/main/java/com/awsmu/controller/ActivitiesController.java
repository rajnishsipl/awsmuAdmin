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

import com.awsmu.config.Properties;
import com.awsmu.model.ActivitiesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.service.ActivitiesService;
import com.google.gson.Gson;

@Controller
public class ActivitiesController {

	/**
	 * Injecting activitiesService Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "ActivitiesService")
	private ActivitiesService activitiesService;
	
	
	/*  
		*Created Date: 15-Sep-2015
		*Purpose:Function to get activities  
	*/ 	 
	@RequestMapping(value = "/getActivities", method = RequestMethod.POST)
	public @ResponseBody String getActivities(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		// Get all activities from database 	
		//ajaxResponse = activitiesService.getActivities(skipPostRecord, skipPostFreq); 
		
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
		
		return new Gson().toJson(activitiesService.getActivities((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	

	/*  
		*Created Date: 16-Sep-2015
		*Purpose:Function to get activities form data, problems and activity categories  
	*/ 	 
	@RequestMapping(value = "/getActivitiesFormData", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getActivitiesFormData(HttpSession session,AjaxResponse ajaxResponse) {
		// Get activity form data from database 	
		ajaxResponse = activitiesService.getActivitiesFormData(); 
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 15-Sep-2015
	*Purpose:Function to save activities  
	*/ 	 
	@RequestMapping(value = "/addActivities", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse addActivities(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("activity") String activity ) {
		
		// Convert activity json to activity model	
		ActivitiesModel activityModel = new Gson().fromJson(activity, ActivitiesModel.class);
		Date date =  new Date();
		activityModel.setCreatedDate(date);
		activityModel.setUpdatedDate(date);
		
		// Save activity
		ajaxResponse = activitiesService.saveActivities(activityModel);
		
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 15-Sep-2015
	*Purpose:Function to save activities  
	*/ 
	@RequestMapping(value = "/getActivityDetail/{activityId}", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse getActivityDetail(@PathVariable String activityId,HttpSession session,AjaxResponse ajaxResponse) {
		// Get activity form data from database 	
		ajaxResponse = activitiesService.getActivityById(activityId); 
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 25-Sep-2015
	*Purpose:Function to update activities  
	*/ 
	@RequestMapping(value = "/updateActivities", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateActivities(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("activity") String activity) {
		
		// Convert activity json to activity model	
				ActivitiesModel activityModel = new Gson().fromJson(activity, ActivitiesModel.class);
				activityModel.setUpdatedDate(new Date());
				// update activity
				ajaxResponse = activitiesService.updateActivities(activityModel);
				return ajaxResponse;
	}
	
	/*  
	*Created Date: 09-Nov-2015
	*Purpose:Function to delete activity
	*/ 
	@RequestMapping(value = "/activityDelete", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse deleteQuestion(@RequestParam("activityId") String activityId,HttpSession session,AjaxResponse ajaxResponse) {
		/*delete data from database*/ 	
		ajaxResponse = activitiesService.deleteActivityById(activityId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
	
}
