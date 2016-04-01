package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.TipsModel;
import com.awsmu.util.Utils;


public class TipsValidator {
	public Map<String,String> validateTips(TipsModel tipsModel){
		Map<String,String> errorMap = new HashMap<String,String>();  

		// Validate title
		if(Utils.checkEmpty(tipsModel.getTipTitle())){
			errorMap.put("tipTitle", "Trend title is required");
		}else if(Utils.checkStringLength(tipsModel.getTipTitle(), 50))
			errorMap.put("tipTitle", "Trend title should not exceed than 50 characters");
				
		// Validate title
		if(Utils.checkEmpty(tipsModel.getTipText())){
			errorMap.put("tipText", "Tip text is required");
		}else if(Utils.checkStringLength(tipsModel.getTipText(), 500))
			errorMap.put("tipText", "Tip text should not exceed than 500 characters");
		
		return errorMap;
	}
}