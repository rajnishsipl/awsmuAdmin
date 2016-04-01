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

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PostsModel;
import com.awsmu.service.PostService;
import com.google.gson.Gson;

/**
 * Post controller 
 */

@Controller
public class PostController {
	/**
	 * Injecting PostServiceImple 
	 */
	@Autowired
	@Qualifier(value="PostService")
	public PostService postService;
	
	
	/*@Autowired
	@Qualifier(value="PostService1")
	public static PlannerGroupService plannerGroupService;*/
	/*
	 *Created Date: 18-Sep-2015
     *Purpose: return post list, post sorting and searching as a json response
     */ 
	@RequestMapping(value = "/getPosts", method = RequestMethod.POST)
	public @ResponseBody String getPosts(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
		//System.out.println(plannerGroupService);
		System.out.println(postService);
		
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
		return new Gson().toJson(postService.getPosts((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	/*
	 *Created Date: 22-Sep-2015
     *Purpose: get post details
     */ 	 
	@RequestMapping(value = "/getPostDetail/{postId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getPostDetail(@PathVariable String postId, HttpServletRequest request, HttpServletResponse response) {				 
		//get post service response from service 
		return postService.getPostDetail(postId);
	}
	
	/*
	 *Created Date: 22-Sep-2015
     *Purpose: get post details
     */ 	 
	@RequestMapping(value = "/submitPostEdit/{postId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse submitPostEdit(@PathVariable String postId, HttpSession session,HttpServletRequest request, HttpServletResponse response, @RequestParam("postDetail") String postDetail) {				 
		//get post service response from service 
		if(session.getAttribute("userId")==null) {
			AjaxResponse gr = new AjaxResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return gr;
		}
		PostsModel postsModel = new Gson().fromJson(postDetail, PostsModel.class);
		return postService.submitPostEdit(postId,postsModel);
		//return postService.getPostDetail(postId);
	}
	
	/*
	 *Created Date: 17-Nov-2015
     *Purpose: active / inactive post 
     */ 	 
	@RequestMapping(value = "/activeInactivePost", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse activeInactivePost(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();		
		ajaxResponse = postService.activeInactivePost(request.getParameter("postId"),Integer.parseInt(request.getParameter("active")));    	
    	return ajaxResponse;
	}
}
