package com.awsmu.validator;

import java.util.HashMap;

import java.util.Map;

import com.awsmu.model.ProblemModel;
import com.awsmu.util.Utils;

public class ProblemsValidator {
	public Map<String,String> validateProblem(ProblemModel problemModel){
		Map<String,String> errorMap = new HashMap<String,String>();  
		
		// Validate name
		if(Utils.checkEmpty(problemModel.getName())){
			errorMap.put("name", "Problem name is required");
		}else if(Utils.checkStringLength(problemModel.getName(), 50)){
			errorMap.put("name", "Problem name should not exceed than 50 characters");
		}else if(!Utils.checkRegx(problemModel.getName(), "^[a-zA-Z0-9_-]{2,50}$"))
			errorMap.put("name", "Problem name is incorrect");
		
		
		// Validate title
		if(Utils.checkEmpty(problemModel.getTitle())){
			errorMap.put("title", "Problem title is required");
		}else if(Utils.checkStringLength(problemModel.getTitle(), 50))
			errorMap.put("title", "Problem title should not exceed than 50 characters");
		
		
		// Validate Description
		if(Utils.checkEmpty(problemModel.getDescription())){
			errorMap.put("description", "description is required");
		}	

		// Validate Icon
		if(Utils.checkEmpty(problemModel.getIcon())){
			//errorMap.put("icon", "icon is required");
		}
		
		// Validate Banner
		if(Utils.checkEmpty(problemModel.getBanner())){
			//errorMap.put("banner", "Banner is required");
		}

		return errorMap;
	}
}
