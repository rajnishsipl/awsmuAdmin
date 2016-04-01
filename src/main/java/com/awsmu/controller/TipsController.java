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
import com.awsmu.model.GridResponse;
import com.awsmu.model.TipsModel;
import com.awsmu.service.TipsService;
import com.google.gson.Gson;


@Controller
@RequestMapping("/tips")
public class TipsController {
	/**
	 * Injecting tips Service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "TipsService")
	private TipsService tipsService;
	
	
	
	
	
	/*  
	*Created Date: 8-Oct-2015
	*Purpose:Function to get tips grid  
	*/ 	 
	@RequestMapping(value = "/getGrid", method = RequestMethod.POST)
	public @ResponseBody String getTipsGrid(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		
		// Get all tips from database 	
		
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
		
		return new Gson().toJson(tipsService.getTipsGrid((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	
	/*  
	*Created Date: 8-Oct-2015
	*Purpose:Function to save tip  
	*/ 	 
	@RequestMapping(value = "/addTip", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse addTip(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("tip") String tip ) {
		
		// Convert tip json to tip model	
		TipsModel  tipsModel = new Gson().fromJson(tip, TipsModel.class);
		Date date =  new Date();
		tipsModel.setCreatedDate(date);
		tipsModel.setUpdatedDate(date);
		
		
		// Save tip
		ajaxResponse = tipsService.saveTip(tipsModel);
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 8-Oct-2015
	*Purpose:Function to save tip  
	*/ 	 
	@RequestMapping(value = "/editTip", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse editTip(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("tip") String tip ) {
		
		// Convert tip json to tip model	
		TipsModel  tipsModel = new Gson().fromJson(tip, TipsModel.class);
		Date date =  new Date();
		tipsModel.setCreatedDate(date);
		tipsModel.setUpdatedDate(date);
		
		
		// update tip
		ajaxResponse = tipsService.updateTip(tipsModel);
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 7-Oct-2015
	*Purpose:Function to get tips detail by ID  
	*/ 
	@RequestMapping(value = "/getDetail/{tipId}", method = RequestMethod.POST)

	public @ResponseBody AjaxResponse getTipDetail(@PathVariable String tipId,HttpSession session,AjaxResponse ajaxResponse) {
		// Get tips from database 	
		ajaxResponse = tipsService.getTipsById(tipId); 
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 16-Sep-2015
	*Purpose:Function to get activities form data, problems and activity categories  
	*/ 	 
	@RequestMapping(value = "/FormData", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getTipsFormData(HttpSession session,AjaxResponse ajaxResponse) {
		// Get tips form data from database 	
		ajaxResponse = tipsService.getTipsFormData(); 
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 09-Nov-2015
	*Purpose:Function to delete tip
	*/ 
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse deleteTip(@RequestParam("tipId") String tipId,HttpSession session,AjaxResponse ajaxResponse) {
		/*delete data from database*/ 	
		ajaxResponse = tipsService.deleteTipById(tipId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
}
