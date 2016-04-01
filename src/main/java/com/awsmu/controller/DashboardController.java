package com.awsmu.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.awsmu.model.AjaxResponse;
import com.awsmu.service.DashboardService;

/**
 * Dashboard controller to handles requests for dashboard section.
 */
@Controller
public class DashboardController {
	
	/**
	 * Injecting DashboardServiceImpl
	 */
	@Autowired(required = true)
    @Qualifier(value = "DashboardService")
	private DashboardService dashboardService;
		
	/*
	 *Created Date: 20-Oct-2015
     *Purpose: get dashbaord 
     */ 	 
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public @ResponseBody AjaxResponse getUsers(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
		//get data 
		return dashboardService.getDashboardData();
	}
}
