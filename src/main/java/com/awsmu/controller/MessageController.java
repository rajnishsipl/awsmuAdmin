package com.awsmu.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.UserMessageModel;
import com.awsmu.service.MessageService;
import com.google.gson.Gson;

/**
 * message controller to handles requests for user message section.
 */
@Controller
public class MessageController {
	@Autowired
	MessageService messageService;
	/*
	 *Created Date: 28-Nov-2015
     *Purpose: get message conversation user wise
     */ 	 
	@RequestMapping(value = "/getMessageConversaction", method = RequestMethod.POST)
	public @ResponseBody String getMessageConversaction(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
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
		GridResponse gr = messageService.getRecentConversaction((page-1)*displayRows,displayRows,page,sidx,sord,filters);
		/*converting object to json string */
		return new Gson().toJson(gr);
	}
	
	
	/*
	 *Created Date: 02-Nov-2015
     *Purpose: get message thread
     */ 	 
	@RequestMapping(value = "/getMessageThread/{chainId}", method = RequestMethod.POST)
	public @ResponseBody String getMessageThread(HttpSession session,@PathVariable String chainId, HttpServletRequest request, HttpServletResponse response) {				 
		int skipRecord = Integer.parseInt(request.getParameter("skipRecord"));
		int skipFreq = Integer.parseInt(request.getParameter("skipFreq"));
		if(session.getAttribute("userId")==null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(messageService.getMessageThread(chainId, skipRecord, skipFreq));
	}
	
	/*
	 *Created Date: 06-Nov-2015
     *Purpose: edit user
     */

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse sendMessage(HttpSession session,HttpServletRequest request, HttpServletResponse response, @RequestParam("messageDetail") String messageDetail) {		
		if(session.getAttribute("userId")==null) {
			AjaxResponse gr = new AjaxResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return gr;
		}
		UserMessageModel useMessagerModel = new Gson().fromJson(messageDetail, UserMessageModel.class);
		return messageService.sendMessage(useMessagerModel);
		//return null;
	}
}
