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
import org.springframework.web.bind.annotation.ResponseBody;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.service.HelloService;
import com.awsmu.service.PlannerGroupService;
import com.awsmu.service.PostService;
import com.google.gson.Gson;

@Controller
public class PlannerGroupController {
	/**
	 * Injecting planner Group service 
	 */
	@Autowired
	@Qualifier(value="HelloServiceImpl")
	public static HelloService helloService;
	
	/**
	 * Injecting PostServiceImple 
	 */
	@Autowired
	@Qualifier(value="PostService")
	public PostService postService;
	/*@RequestMapping(value = "/getplannerGroupView/{problemId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getPostDetail(@PathVariable String problemId, HttpServletRequest request, HttpServletResponse response,HttpSession session) {				 
		// get admin id from session if logged in  
		if(session.getAttribute("userId")==null) {
			AjaxResponse gr = new AjaxResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return gr;
		}		
		return plannerGroupService.getPlannerGroupDetail(problemId);
	}*/
	/*
	 *Created Date: 24-Nov-2015
     *Purpose: return planner group member list, planner group member sorting and searching as a json response
     */ 
	/*@RequestMapping(value = "/getPlannerGroupMember/{problemId}", method = RequestMethod.POST)
	public @ResponseBody String getPosts(@PathVariable String problemId,HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
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
		return new Gson().toJson(plannerGroupService.getPlannerGroupMembers(problemId,(page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}*/
	
	/*
	 *Created Date: 26-Nov-2015
     *Purpose: return planner group post list,planner group post sorting and searching as a json response
     */ 
	//@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getPlannerGroupPosts/{problemId}", method = RequestMethod.POST)
	public @ResponseBody String getPlannerGroupPosts(@PathVariable String problemId,HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		System.out.println("Object is"+helloService);
		
		System.out.println("Object is postService "+postService);
		
		/*Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"),HashMap.class);
		// get admin id from session if logged in  
		if(session.getAttribute("userId")==null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(plannerGroupService.getPlannerGroupPosts(problemId,(page-1)*displayRows,displayRows,page,sidx,sord,filters));*/
		return null;
	}
}
