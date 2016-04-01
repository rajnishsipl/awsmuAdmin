package com.awsmu.validator;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.ActivitiesModel;
import com.awsmu.util.Utils;

public class ActivitiesValidator {

	// Validate activities 
	public Map<String,String> validateActivities(ActivitiesModel activitiesModel){
		Map<String,String> errorMap = new HashMap<String,String>();  
		
		// Validate category
		if(Utils.checkEmpty(activitiesModel.getCategory()))
			errorMap.put("category", "Category is required");
		
		// Validate problems name
		if(Utils.checkEmptyStringList(activitiesModel.getProblems()))
			errorMap.put("problems", "Problems name is required");
		
		// Validate action
		if(Utils.checkEmpty(activitiesModel.getAction()))
			errorMap.put("action", "Action is required");
		else if(Utils.checkStringLength(activitiesModel.getAction(), 500))
			errorMap.put("action", "Action should not exceed than 500 characters");
		
		// Validate description 
		if(Utils.checkStringLength(activitiesModel.getDescription(), 500))
			errorMap.put("description", "Description should not exceed than 500 characters");
		
			
		
		// Validate amount
		if(Utils.checkStringLength(activitiesModel.getAmount(), 200))
			errorMap.put("amount", "Amount should not exceed than 200 characters");
		
		// Validate frequency
		if(Utils.checkStringLength(activitiesModel.getFrequency(), 200))
			errorMap.put("frequency", "Frequency should not exceed than 200 characters");
		
		// Validate Minimum and max Time
		String minTimeStr = activitiesModel.getMinTime();
		String maxTimeStr = activitiesModel.getMaxTime();
        
		if(minTimeStr !=null && maxTimeStr !=null ){
			// Convert string to float	minTime	
			Float minTimeFloat = Float.parseFloat(minTimeStr.replace(':','.'));
			DecimalFormat df = new DecimalFormat("0.00");
			df.setMaximumFractionDigits(2);
			minTimeStr = df.format(minTimeFloat);
			minTimeFloat = Float.parseFloat(minTimeStr);
			
			// Convert string to float	maxtime	
			Float maxTimeFloat = Float.parseFloat(maxTimeStr.replace(':','.'));
			DecimalFormat dff = new DecimalFormat("0.00");
			dff.setMaximumFractionDigits(2);
			maxTimeStr = dff.format(maxTimeFloat);
			maxTimeFloat = Float.parseFloat(maxTimeStr);
			
			if(minTimeFloat > maxTimeFloat){
				errorMap.put("maxTime", "Please provide right time");
			}
			
		}
		
		// Validate age 
		String minAge = activitiesModel.getMinAge();
		String maxAge = activitiesModel.getMaxAge();
		if(minAge != null && maxAge !=null ){
			int minAgeInt = Integer.parseInt(minAge);
			int maxAgeInt = Integer.parseInt(maxAge);
			if(minAgeInt > maxAgeInt)
				errorMap.put("maxAge", "Please provide right age");
		}
		
		
		return errorMap;
	}
	
	
	
}