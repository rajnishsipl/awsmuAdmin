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
import org.springframework.web.multipart.MultipartFile;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.TrendsModel;
import com.awsmu.service.TrendsService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/trends")
public class TrendsController {
	/**
	 * Injecting trend Service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "TrendsService")
	private TrendsService trendsService;
	
	/*  
	*Created Date: 8-Oct-2015
	*Purpose:Function to get trends grid  
	*/ 	 
	@RequestMapping(value = "/getGrid", method = RequestMethod.POST)
	public @ResponseBody String getTrendsGrid(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		
		// Get all trends from database 	
		
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
		
		return new Gson().toJson(trendsService.getTrendsGrid((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	
	/*  
	*Created Date: 30-Sep-2015
	*Purpose:Function to save trend  
	*/ 	 
	@RequestMapping(value = "/addTrend ", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse addTrend(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("trend") String trend,
			 @RequestParam(value = "trendIcon", required=true) MultipartFile trendIcon) {
		
		// Convert trend json to trend model	
		TrendsModel  trendsModel = new Gson().fromJson(trend, TrendsModel.class);
		Date date =  new Date();
		trendsModel.setCreatedDate(date);
		trendsModel.setUpdatedDate(date);
		
		
		// Save trend
		ajaxResponse = trendsService.saveTrend(trendsModel,trendIcon);
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 7-Oct-2015
	*Purpose:Function to get trend detail by ID  
	*/ 
	@RequestMapping(value = "/getDetail/{trendId}", method = RequestMethod.POST)

	public @ResponseBody AjaxResponse getTrendDetail(@PathVariable String trendId,HttpSession session,AjaxResponse ajaxResponse) {
		// Get trend from database 	
		ajaxResponse = trendsService.getTrendById(trendId); 
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 08- Oct-2015
	*Purpose:Function to update trend  
	*/ 	 
	@RequestMapping(value = "/editTrendImage", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse editTrendImage(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("trend") String trend, 
			@RequestParam(value = "trendIcon", required=true) MultipartFile trendIcon) {
		
		// Convert trend json to trend model	
		TrendsModel  trendsModel = new Gson().fromJson(trend, TrendsModel.class);
		Date date =  new Date();
		trendsModel.setCreatedDate(date);
		trendsModel.setUpdatedDate(date);
		
		
		// update trend
		ajaxResponse = trendsService.updateTrend(trendsModel,"1",trendIcon);
		
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 08- Oct-2015
	*Purpose:Function to update trend  
	*/ 	 
	@RequestMapping(value = "/editTrend", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse editTrend(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("trend") String trend ) {
		
		// Convert trend json to trend model	
		TrendsModel  trendsModel = new Gson().fromJson(trend, TrendsModel.class);
		Date date =  new Date();
		trendsModel.setCreatedDate(date);
		trendsModel.setUpdatedDate(date);
		
		
		// update trend
		ajaxResponse = trendsService.updateTrend(trendsModel,"0",null);
		
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 09-Nov-2015
	*Purpose:Function to delete celebrity
	*/ 
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse deleteQuestion(@RequestParam("trendId") String trendId,HttpSession session,AjaxResponse ajaxResponse) {
		/*delete data from database*/ 	
		ajaxResponse = trendsService.deleteTrendById(trendId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
}
