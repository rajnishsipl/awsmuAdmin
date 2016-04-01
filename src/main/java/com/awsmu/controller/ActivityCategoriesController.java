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
import com.awsmu.model.ActivityCategoriesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.service.ActivityCategoriesService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/activityCategories")
public class ActivityCategoriesController {
	
	
	
	/**
	 * Injecting activities Categories service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "activityCategoriesService")
	private ActivityCategoriesService activityCategoriesService;
	
	
	
	/*  
	*Created Date: 6-Oct-2015
	*Purpose:Function to get activity category list  
*/ 	 
@RequestMapping(value = "/getGrid", method = RequestMethod.POST)
public @ResponseBody String getActivityCategories(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
	
	// Get all activity category from database 	
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
	
	return new Gson().toJson(activityCategoriesService.getActivityCategoriesList((page-1)*displayRows,displayRows,page,sidx,sord,filters));
}




/*  
*Created Date: 07-Oct-2015
*Purpose:Function to save activity category 
*/ 	 
@RequestMapping(value = "/add", method = RequestMethod.POST)
public @ResponseBody AjaxResponse addActivityCategory(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("activityCategory") String activityCategory ) {
	
	// Convert activity json to activity model	
	ActivityCategoriesModel activityCategoryModel = new Gson().fromJson(activityCategory, ActivityCategoriesModel.class);
	Date date =  new Date();
	activityCategoryModel.setCreatedDate(date);
	activityCategoryModel.setUpdatedDate(date);
	
	// Save activity
	ajaxResponse = activityCategoriesService.saveActivityCategory(activityCategoryModel);
	
	return ajaxResponse;
}

/*  
*Created Date: 07-Oct-2015
*Purpose:Function to save activities  
*/ 
@RequestMapping(value = "/getDetail/{activityCategoryId}", method = RequestMethod.POST)

public @ResponseBody AjaxResponse getActivityDetail(@PathVariable String activityCategoryId,HttpSession session,AjaxResponse ajaxResponse) {
	// Get activity form data from database 	
	ajaxResponse = activityCategoriesService.getActivityCategoryById(activityCategoryId); 
	return ajaxResponse;
}


/*  
*Created Date: 25-Sep-2015
*Purpose:Function to update activities  
*/ 
@RequestMapping(value = "/update", method = RequestMethod.POST)

public @ResponseBody AjaxResponse updateActivityCategory(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("activityCategory") String activityCategory) {
	
	// Convert activity json to activity model	
	ActivityCategoriesModel activityCategoryModel = new Gson().fromJson(activityCategory, ActivityCategoriesModel.class);
	activityCategoryModel.setUpdatedDate(new Date());
			// update activity
			ajaxResponse = activityCategoriesService.updateActivityCategory(activityCategoryModel);
			return ajaxResponse;
}


/*  
*Created Date: 17-Nov-2015
*Purpose:Function to delete activity Category
*/ 
@RequestMapping(value = "/delete", method = RequestMethod.POST)

public @ResponseBody AjaxResponse deleteQuestion(@RequestParam("activityCategoryId") String activityCategoryId,HttpSession session,AjaxResponse ajaxResponse) {
	/*delete data from database*/ 	
	ajaxResponse = activityCategoriesService.deleteActivityCategoryById(activityCategoryId);
	/*Return ajax response object with response status*/
	return ajaxResponse;
}
}
