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
import com.awsmu.service.PostLikeService;
import com.google.gson.Gson;

@Controller
public class PostLikeController {
	/* Injecting postlike service
	 * */
	@Autowired
	@Qualifier(value="PostLikeService")
	PostLikeService postLikeService; 
	/*
	 *Created Date: 25-Sep-2015
     *Purpose: return like user list, like sorting and searching as a json response
     *if we do not pass post id as a parameter then it return all post likes.
     *
     */ 
	@RequestMapping(value = "/getPostLikes/{postId}", method = RequestMethod.POST)	
	public @ResponseBody String getPostLikes(@PathVariable("postId") String postId,HttpSession session, HttpServletRequest request, HttpServletResponse response) {
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
		return new Gson().toJson(postLikeService.getPostLikes(postId,(page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	/*
	 *Created Date: 17-Nov-2015
     *Purpose: active / inactive comment 
     */ 	 
	@RequestMapping(value = "/activeInactiveLike", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse activeInactiveLike(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();		 
		ajaxResponse = postLikeService.activeInactiveLike(request.getParameter("likeId"),Integer.parseInt(request.getParameter("active")),request.getParameter("postId"));
    	return ajaxResponse;
	}
}
