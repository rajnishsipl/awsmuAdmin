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
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.FriendsModel;
import com.awsmu.model.GridResponse;
import com.awsmu.service.ConnectionsService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/connections")
public class ConnectionsController {

	/**
	 * Injecting connectionsService Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "ConnectionsService")
	private ConnectionsService connectionsService;
	
	
	/*  
		*Created Date: 1-Dec-2015
		*Purpose:Function to get connections  
	*/ 	 
	@RequestMapping(value = "/getGrid", method = RequestMethod.POST)
	public @ResponseBody String getGrid(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		// Get all connections from database 	
		
		
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
		
		return new Gson().toJson(connectionsService.getConnections((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	
	
	
	
	/*  
	*Created Date: 7-Dec-2015
	*Purpose:Function to get user connections  
*/ 	 
@RequestMapping(value = "/getUserConnectionGrid{userId}", method = RequestMethod.POST)
public @ResponseBody String getUserConnectionGrid(@PathVariable("userId") String userId,HttpSession session,HttpServletRequest request, HttpServletResponse response) {
	// Get all connections from database 	
	
	
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
	
	return new Gson().toJson(connectionsService.getUserConnections(userId,(page-1)*displayRows,displayRows,page,sidx,sord,filters));
}
	
	//getUserConnectionGrid
	
	/*  
	*Created Date: 2-Dec-2015
	*Purpose:Function to get connection detail by ID  
	*/ 
	@RequestMapping(value = "/getDetail/{connectionId}", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse getConnectionDetailById(@PathVariable String connectionId,HttpSession session,AjaxResponse ajaxResponse) {
		// Get problem form data from database 	
		ajaxResponse = connectionsService.getConnectionDetailById(connectionId); 
		return ajaxResponse;
	}
	
	
	
	/*  
	*Created Date: 2-Dec-2015
	*Purpose:Function to edit friends   
	*/ 
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse editConnection(HttpServletRequest request,HttpSession session,
			AjaxResponse ajaxResponse,@RequestParam("connection") String problem) {
		
		    // Convert friend json to friend model	
			FriendsModel  friendsModel = new Gson().fromJson(problem, FriendsModel.class);
			friendsModel.setUpdatedDate(new Date());
			
			// update friend
			
			ajaxResponse = connectionsService.updateConnection(friendsModel);
		return ajaxResponse;
	}

	
}
