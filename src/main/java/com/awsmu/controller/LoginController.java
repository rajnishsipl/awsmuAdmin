 package com.awsmu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.UserModel;
import com.awsmu.service.UserService;
import com.google.gson.Gson;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {
	
	/**
	 * Injecting User Service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "UserService")
	private UserService userService;
	
	
	
		
	/*  
	 	*Created Date: 11-Sep-2015
        *Purpose:Function to handle admin login request  
         
	*/ 	 
	 @RequestMapping(value = "/loginAction", method = RequestMethod.POST)
	 public @ResponseBody AjaxResponse login(HttpSession session,AjaxResponse ajaxResponse,
			 												@RequestParam("email") String email, 
			 												@RequestParam("password") String password) {
		 
		
		 // Check admin authentication
		 ajaxResponse = userService.checkAdminLogin(email, password);
		 
		 // If status is true than set admin session
		 if(ajaxResponse.getStatus()){
			 // Convert string to userModel
			 UserModel userModel = new Gson().fromJson(ajaxResponse.getContent(),UserModel.class);		
			 session.setAttribute("userId" , userModel.get_id());
			 session.setAttribute("userEmail" , userModel.getEmail() );
			 session.setAttribute("userDisplayName" , userModel.getDisplayName() );
			 session.setAttribute("userFullName" , userModel.getName() );
			 session.setAttribute("userRole" , userModel.getUserRole() );
		 }
		 return ajaxResponse;
	}	
	 
	 /*
	  *Created Date: 11-Sep-2015
      *Purpose:Function to handle admin logout request  
    */ 	 
	 
	 
	 @RequestMapping(value = "/logout", method = RequestMethod.POST)
	 public @ResponseBody AjaxResponse logout(AjaxResponse ajaxResponse,HttpSession session,HttpServletRequest request) {
		 session.invalidate();
	     request.getSession().invalidate();
	     ajaxResponse.setIsLoggedIn(false);
	     return ajaxResponse;
	 }
}