
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
import org.springframework.web.multipart.MultipartFile;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.BookletModel;
import com.awsmu.service.BookletService;
import com.google.gson.Gson;

/**
 * Booklet controller to handles booklet section.
 */
@Controller
public class BookletController {
	
	/**
	 * Injecting BookletServiceImpl 
	 */
	@Autowired(required = true)
    @Qualifier(value = "BookletService")
	private BookletService bookletService;
	
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: get user's booklets based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/userBookletsList/{userId}", method = RequestMethod.POST)
	public @ResponseBody String userBookletsList(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String userId) {				
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
		
		
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"), HashMap.class);

		// get admin id from session if logged in  
		if(session.getAttribute("userId") == null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(bookletService.getUserBookletsList((page-1)*displayRows, displayRows, page, sidx, sord, filters, userId));
	}
	
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: get booklets based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/bookletsList", method = RequestMethod.POST)
	public @ResponseBody String bookletsList(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
		
		
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"), HashMap.class);

		// get admin id from session if logged in  
		if(session.getAttribute("userId")==null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(bookletService.getBookletsList((page-1)*displayRows, displayRows, page, sidx, sord, filters));
	}
	
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: get booklet detail
     */ 	 
	@RequestMapping(value = "/getBookletDetail/{bookletId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getBookletDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String bookletId) {				
		//get planner detail 
		return bookletService.getBookletById(bookletId);
			
	}
		
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: edit booklet
     */ 	 
	@RequestMapping(value = "/saveUploadBooklet", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveUploadBooklet(HttpServletRequest request, HttpServletResponse response, @RequestParam("bookletDetail") String bookletDetail, @RequestParam(value = "file", required=false) MultipartFile file) {		
		BookletModel bookletModel = new Gson().fromJson(bookletDetail, BookletModel.class);
		return bookletService.saveBooklet(bookletModel, request.getParameter("fileUpload"), file);
	}
	
	/*
	 *Created Date: 9-Oct-2015
     *Purpose: edit booklet
     */ 	 
	@RequestMapping(value = "/saveBooklet", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse saveBooklet(HttpServletRequest request, HttpServletResponse response, @RequestParam("bookletDetail") String bookletDetail) {		
		MultipartFile file = null;
		BookletModel bookletModel = new Gson().fromJson(bookletDetail, BookletModel.class);
		return bookletService.saveBooklet(bookletModel, request.getParameter("fileUpload"), file);
	}
	
	/*
	 *Created Date: 7-Oct-2015
     *Purpose: delete booklet 
     */ 	 
	@RequestMapping(value = "/deleteBooklet", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteBooklet(HttpServletRequest request, HttpServletResponse response) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
	
		ajaxResponse = bookletService.deleteBookletById(request.getParameter("bookletId"));
    	
    	return ajaxResponse;
	}
}
