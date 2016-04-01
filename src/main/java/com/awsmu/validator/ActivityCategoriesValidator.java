package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.ActivityCategoriesModel;
import com.awsmu.util.Utils;

public class ActivityCategoriesValidator {
	// Validate activities 
		public Map<String,String> validateActivityCategory(ActivityCategoriesModel activityCategoriesModel){
			Map<String,String> errorMap = new HashMap<String,String>();  
			
						
			// Validate category
			if(Utils.checkEmpty(activityCategoriesModel.getCategory())){
				errorMap.put("category", "Category name is required");
			}else if(Utils.checkStringLength(activityCategoriesModel.getCategory(), 50)){
				errorMap.put("category", "Category name should not exceed than 50 characters");
			}
			
			
			return errorMap;
		}
}
