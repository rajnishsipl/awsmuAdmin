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
import com.awsmu.model.UserModel;
import com.awsmu.service.UserService;
import com.google.gson.Gson;

/**
 * User controller to handles requests for user section.
 */
@Controller
public class UserController {
	
	/**
	 * Injecting UserServiceImple 
	 */
	@Autowired(required = true)
    @Qualifier(value = "UserService")
	private UserService userService;
		
	/*
	 *Created Date: 11-Sep-2015
     *Purpose: get user based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/getUsers", method = RequestMethod.POST)
	public @ResponseBody String getUsers(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
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
		return new Gson().toJson(userService.getUsers((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	/*
	 *Created Date: 16-Sep-2015
     *Purpose: get user detail
     */ 	 
	@RequestMapping(value = "/getUserDetail/{userId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getUserDetail(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response) {				 
		//get user 
		return userService.getUserDetail(userId);
	}
	
	/*
	 *Created Date: 21-Sep-2015
     *Purpose: edit user
     */ 	 
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse editUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("userDetail") String userDetail) {		
		UserModel userModel = new Gson().fromJson(userDetail, UserModel.class);
		return userService.editUserById(userModel);
		
	}
	
	/*
	 *Created Date: 22-Sep-2015
     *Purpose: upload profile pic
     */ 	 
	@RequestMapping(value = "/uploadProfilePic", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse uploadProfilePic(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile file, @RequestParam("userId") String userId) {		
		
		return userService.uploadProfilePic(file, userId);
	}
	
	/*
	 *Created Date: 22-Sep-2015
     *Purpose: remove profile pic
     */ 	 
	@RequestMapping(value = "/removeProfilePic", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse removeProfilePic(HttpServletRequest request, HttpServletResponse response, @RequestParam("userId") String userId) {		
		return userService.removeProfilePic(userId);
	}
	
	/*
	 *Created Date: 22-Sep-2015
     *Purpose: save user attribtues
     */ 	 
	@RequestMapping(value = "/saveUserAttributes", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveUserAttributes(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
		//get post parameter to save attributes
    	int attrCount  = Integer.parseInt(request.getParameter("attrCount"));
    	for(int i=1; i<= attrCount; i++){
    		   		
    		ajaxResponse = userService.saveUserAttributes(request.getParameter("attrId"+i), request.getParameter("optionsName"+i));
    			
    	}
    	
    	return ajaxResponse;
	}
	
	
	/*
	 *Created Date: 23-Sep-2015
     *Purpose: change user password
     */ 	 
	@RequestMapping(value = "/changeUserPassword", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse changeUserPassword(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = userService.changeUserPassword(request.getParameter("userId"), request.getParameter("password"));
    	
    	return ajaxResponse;
	}
	
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: delete user 
     */ 	 
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteUser(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = userService.deleteUser(request.getParameter("userId"));
    	
    	return ajaxResponse;
	}
	
}
