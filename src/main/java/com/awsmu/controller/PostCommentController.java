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
import com.awsmu.model.PostCommentsModel;
import com.awsmu.service.PostCommentService;
import com.google.gson.Gson;

@Controller
public class PostCommentController {
	/**
	 * Injecting PostCommentService 
	 */
	@Autowired
	@Qualifier(value="PostCommentService")
	public PostCommentService postCommentService;
	
	/*
	 *Created Date: 23-Sep-2015
     *Purpose: return comment list, comment sorting and searching as a json response
     *if we do not pass post id as a parameter then it return all post comments.
     *
     */ 
	@RequestMapping(value = "/getPostComments/{postId}", method = RequestMethod.POST)	
	public @ResponseBody String getPostComments(@PathVariable("postId") String postId,HttpSession session, HttpServletRequest request, HttpServletResponse response) {
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
		return new Gson().toJson(postCommentService.getPostComments(postId,(page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	
	
	/*
	 *Created Date: 17-Nov-2015
     *Purpose: get comment details
     */ 	 
	@RequestMapping(value = "/getCommentDetail/{commentId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getCommentDetail(@PathVariable String commentId, HttpServletRequest request, HttpServletResponse response) {
		return postCommentService.getCommentDetail(commentId);
	}
	/*
	 *Created Date: 17-Nov-2015
     *Purpose: active / inactive comment 
     */ 	 
	@RequestMapping(value = "/activeInactiveComment", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse activeInactiveComment(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();		 
		ajaxResponse = postCommentService.activeInactiveComment(request.getParameter("commentId"),Integer.parseInt(request.getParameter("active")),request.getParameter("postId"));
    	return ajaxResponse;
	}
	
	//
	
	/*
	 *Created Date: 22-Sep-2015
     *Purpose: get post details
     */ 	 
	@RequestMapping(value = "/submitPostCommentEdit/{commentId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse submitPostCommentEdit(@PathVariable String commentId, HttpSession session,HttpServletRequest request, HttpServletResponse response, @RequestParam("postCommentDetail") String postCommentDetail) {				 
		//get post service response from service 
		if(session.getAttribute("userId")==null) {
			AjaxResponse gr = new AjaxResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return gr;
		}
		PostCommentsModel postsModel = new Gson().fromJson(postCommentDetail, PostCommentsModel.class);
		return postCommentService.submitPostCommentEdit(commentId,postsModel);
		//return postService.getPostDetail(postId);
	}
	
}
