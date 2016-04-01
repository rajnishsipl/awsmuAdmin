package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.CelebritiesModel;

import com.awsmu.util.Utils;

public class CelebritiesValidator {
	
	public Map<String,String> validateCelebrity(CelebritiesModel celebritiesModel){
		Map<String,String> errorMap = new HashMap<String,String>();  
		
			
		
		// Validate gender
		if(Utils.checkEmpty(celebritiesModel.getGender())){
			errorMap.put("gender", "Gender is required");
		}
		
		
		// Validate name
		if(Utils.checkEmpty(celebritiesModel.getName())){
			errorMap.put("name", "Name is required");
		}else if(Utils.checkStringLength(celebritiesModel.getName(), 50))
			errorMap.put("name", "Name should not exceed than 50 characters");
		
		// Validate nationality
		if(Utils.checkEmpty(celebritiesModel.getNationality())){
			errorMap.put("nationality", "Nationality is required");
		}else if(Utils.checkStringLength(celebritiesModel.getNationality(), 50))
			errorMap.put("nationality", "Nationality should not exceed than 50 characters");
		
		
		// Validate profession
		if(Utils.checkEmpty(celebritiesModel.getProfession())){
			errorMap.put("profession", "Profession is required");
		}else if(Utils.checkStringLength(celebritiesModel.getProfession(), 50))
			errorMap.put("profession", "Profession should not exceed than 50 characters");

		
		return errorMap;
	}
}
