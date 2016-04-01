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
import com.awsmu.model.ProblemModel;
import com.awsmu.service.ProblemsService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/problem")
public class ProblemsController {

	/**
	 * Injecting problem Service Implementation 
	 */
	@Autowired(required = true)
    @Qualifier(value = "ProblemsService")
	private ProblemsService  problemsService;
	
	

	/*  
		*Created Date: 27-Sep-2015
		*Purpose:Function to get problems  
	*/ 	 
	@RequestMapping(value = "/getProblems", method = RequestMethod.POST)
	public @ResponseBody String getProblems(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		// Get all problems from database 	
		
		
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
		
		return new Gson().toJson(problemsService.getProblems((page-1)*displayRows,displayRows,page,sidx,sord,filters));
	}
	
	
	
	/*  
	*Created Date: 28-Sep-2015
	*Purpose:Function to get problem form data, trends  
	*/ 	 
	@RequestMapping(value = "/getFormData", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getFormData(HttpSession session,AjaxResponse ajaxResponse) {
		// Get problem form data from database 	
		ajaxResponse = problemsService.getProblemsFormData(); 
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 30-Sep-2015
	*Purpose:Function to save problem  
	*/ 	 
	@RequestMapping(value = "/addProblem", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse addProblem(HttpServletRequest request,HttpSession session,AjaxResponse ajaxResponse,@RequestParam("problem") String problem , @RequestParam(value = "problemIcon", required=true) MultipartFile problemIcon
			, @RequestParam(value = "problemBanner", required=true) MultipartFile problemBanner) {
		
		// Convert problem json to problem model	
		ProblemModel  problemModel = new Gson().fromJson(problem, ProblemModel.class);
		Date date =  new Date();
		problemModel.setCreatedDate(date);
		problemModel.setUpdatedDate(date);
		
		
		// Save problem
		ajaxResponse = problemsService.saveProblem(problemModel, request.getParameter("imageUpload"), problemIcon,problemBanner);
		
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 1-Oct-2015
	*Purpose:Function to get problem detail by ID  
	*/ 
	@RequestMapping(value = "/getDetail/{problemId}", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse getProblemDetail(@PathVariable String problemId,HttpSession session,AjaxResponse ajaxResponse) {
		// Get problem form data from database 	
		ajaxResponse = problemsService.getProblemById(problemId); 
		return ajaxResponse;
	}
	
	/*  
	*Created Date: 6-Oct-2015
	*Purpose:Function to edit problem   
	*/ 
	@RequestMapping(value = "/editProblem", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse editProblem(HttpServletRequest request,HttpSession session,
			AjaxResponse ajaxResponse,@RequestParam("problem") String problem) {
		
		    // Convert problem json to problem model	
			ProblemModel  problemModel = new Gson().fromJson(problem, ProblemModel.class);
			problemModel.setUpdatedDate(new Date());
			
			// update problem
			
			ajaxResponse = problemsService.updateProblem(problemModel,"0","0",null,null);
		return ajaxResponse;
	}

	
	/*  
	*Created Date: 6-Oct-2015
	*Purpose:Function to edit problem   
	*/ 
	@RequestMapping(value = "/editProblemImage", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse editProblemImage(HttpServletRequest request,HttpSession session,
			AjaxResponse ajaxResponse,@RequestParam("problem") String problem , 
			@RequestParam(value = "problemIcon", required=false) MultipartFile problemIcon,
			@RequestParam(value = "problemBanner", required=false) MultipartFile problemBanner,
			@RequestParam("isBanner") String isBanner,
			@RequestParam("isIcon") String isIcon) {
		
		    // Convert problem json to problem model	
			ProblemModel  problemModel = new Gson().fromJson(problem, ProblemModel.class);
			problemModel.setUpdatedDate(new Date());
			
			// update problem
			//ajaxResponse = problemsService.saveProblem(problemModel, request.getParameter("imageUpload"), problemIcon,problemBanner);
			ajaxResponse = problemsService.updateProblem(problemModel,isBanner,isIcon,problemIcon,problemBanner);
		return ajaxResponse;
	}

	
	/*  
	*Created Date: 09-Nov-2015
	*Purpose:Function to delete problem
	*/ 
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	
	public @ResponseBody AjaxResponse deleteQuestion(@RequestParam("problemId") String problemId,HttpSession session,AjaxResponse ajaxResponse) {
		/*delete data from database*/ 	
		ajaxResponse = problemsService.deleteProblemById(problemId); 
		/*Return ajax response object with response status*/
		return ajaxResponse;
	}
}
