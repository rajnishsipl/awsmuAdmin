package com.awsmu.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.QuestionsModel;
import com.awsmu.service.QuestionsService;
import com.google.gson.Gson;


@Controller
@RequestMapping("/question")
public class QuestionsController {
	/**
	 * Injecting questions service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "QuestionsService")
	QuestionsService questionsService;
	
	/*  
	*Created Date: 16-Sep-2015
	*Purpose:Function to get questions grid  
	*/ 	 
	@RequestMapping(value = "/getGrid", method = RequestMethod.POST)
	public @ResponseBody String getQuestionGrid(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		// Get all question from database 	
		
		
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
		
		return  new Gson().toJson(questionsService.getQuestionsGrid((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}

	
	
	/*  
	*Created Date: 20-Sep-2015
	*Purpose:Function to get question detail
	*/ 
	@RequestMapping(value = "/getDetail/{questionId}", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse getQuestionDetail(@PathVariable String questionId,HttpSession session,AjaxResponse ajaxResponse) {
		/*Get question details from database*/ 	
		ajaxResponse = questionsService.getQuestionById(questionId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
	
	
	/*  
	*Created Date: 09-Nov-2015
	*Purpose:Function to delete question
	*/ 
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse deleteQuestion(@RequestParam("questionId") String questionId,HttpSession session,AjaxResponse ajaxResponse) {
		/*Get question details from database*/ 	
		ajaxResponse = questionsService.deleteQuestionById(questionId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 16-Sep-2015
	*Purpose:Function to get question detail
	*/ 
	@RequestMapping(value = "/getFormData", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse getFormData(HttpSession session,AjaxResponse ajaxResponse) {
		// Get question form database 	
		ajaxResponse = questionsService.getFormData(); 
		return ajaxResponse;
	}
	
	
	
	/*  
	*Created Date: 16-Sep-2015
	*Purpose:Function to add new question
	*/ 
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse addQuestion(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("question") String question) {
		// save new question	
		
		QuestionsModel questionsModel =  new Gson().fromJson(question,QuestionsModel.class);
		
		System.out.println(questionsModel);
		// Set date for new question
		
		Date date =  new Date();
		questionsModel.setCreatedDate(date);
		questionsModel.setUpdatedDate(date);
		// Add question details
		ajaxResponse = questionsService.addQuestion(questionsModel,"0",null); 
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 16-Sep-2015
	*Purpose:Function to add new question with images
	*/ 
	@RequestMapping(value = "/addQuestionImage", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse addQuestionImage(MultipartHttpServletRequest request, HttpSession session,AjaxResponse ajaxResponse,@RequestParam("question") String question
			,@RequestParam("noOfimage") int noOfimage,@RequestParam(value = "optionsIcon", required=true) MultipartFile[] optionsIcons) {
		// save new question	
		QuestionsModel questionsModel =  new Gson().fromJson(question,QuestionsModel.class);
		// Set date for new question
		
		Date date =  new Date();
		questionsModel.setCreatedDate(date);
		questionsModel.setUpdatedDate(date);
		// Add question details
		ajaxResponse = questionsService.addQuestion(questionsModel,"1",optionsIcons); 
		return ajaxResponse;
	}
	
	
	
	/*  
	*Created Date: 16-Sep-2015
	*Purpose:Function to add new question with images
	*/ 
	@RequestMapping(value = "/updateQuestionImage", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateQuestionImage(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("question") String question
			,@RequestParam(value = "optionsIcon", required=true) MultipartFile[] optionsIcons) {
		// convert Gson to question model	
		QuestionsModel questionsModel =  new Gson().fromJson(question,QuestionsModel.class);
		// Set date for new question
		
		Date date =  new Date();
		questionsModel.setUpdatedDate(date);
		
		// Add question details
		ajaxResponse = questionsService.updateQuestion(questionsModel,"1",optionsIcons); 
		return ajaxResponse;
	}
	
	
	
	
	
	
	
	
	
	
	
	/*  
	*Created Date: 20-Sep-2015
	*Purpose:Function to update new question
	*/ 
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse updateQuestion(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("question") String question) {
		
		// Update new question	
		QuestionsModel questionsModel =  new Gson().fromJson(question,QuestionsModel.class);
		
		// Set date for question
		Date date =  new Date();
		questionsModel.setUpdatedDate(date);
		// Update question details
		ajaxResponse = questionsService.updateQuestion(questionsModel,"0",null); 
		return ajaxResponse;
	}
}
