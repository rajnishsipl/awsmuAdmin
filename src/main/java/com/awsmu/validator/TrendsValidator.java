package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;


import com.awsmu.model.TrendsModel;
import com.awsmu.util.Utils;

public class TrendsValidator {
	public Map<String,String> validateTrend(TrendsModel trendModel){
		Map<String,String> errorMap = new HashMap<String,String>();  
		
			
		
		// Validate title
		if(Utils.checkEmpty(trendModel.getTitle())){
			errorMap.put("title", "Trend title is required");
		}else if(Utils.checkStringLength(trendModel.getTitle(), 50))
			errorMap.put("title", "Trend title should not exceed than 50 characters");
		

		// Validate Icon
		if(Utils.checkEmpty(trendModel.getIcon())){
			//errorMap.put("icon", "icon is required");
		}
		
		

		return errorMap;
	}
}
