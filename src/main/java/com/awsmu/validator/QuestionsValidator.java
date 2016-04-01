package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.QuestionsModel;
import com.awsmu.util.Utils;

public class QuestionsValidator {

	
	public Map<String,String> validateQuestion(QuestionsModel questionsModel){
		
		Map<String,String> errorMap = new HashMap<String,String>();  
		
		// Validate question text
		if(Utils.checkEmpty(questionsModel.getQuestion())){
			errorMap.put("question", "Question text is required");
		}else if(Utils.checkStringLength(questionsModel.getQuestion(), 500)){
			errorMap.put("question", "Question text should not exceed than 500 characters");
		}
		
		
		// Validate question tag
		if(Utils.checkEmpty(questionsModel.getQuestionTag())){
			errorMap.put("questionTag", "Question tag is required");
		}else if(Utils.checkStringLength(questionsModel.getQuestionTag(), 50)){
			errorMap.put("questionTag", "Question tag should not exceed than 50 characters");
		}else if(!Utils.checkRegx(questionsModel.getQuestionTag(), "^[a-zA-Z0-9_-]{2,50}$"))
			errorMap.put("questionTag", "Question tag is incorrect");
		
		
		// Validate problems list
		if(Utils.checkEmptyStringList(questionsModel.getProblemIds()))
			errorMap.put("problemIds", "Problems name is required");
		
		// Validate user attribute text
		if(questionsModel.getIsUserAttribute() == 1){
			if(Utils.checkEmpty(questionsModel.getAttributeText())){
				errorMap.put("attributeText", "Attribute Text is required");
			}else if(Utils.checkStringLength(questionsModel.getAttributeText(), 50)){
				errorMap.put("attributeText", "Attribute Text should not exceed than 50 characters");
			}else if(!Utils.checkRegx(questionsModel.getAttributeText(), "^[a-zA-Z0-9_-]{2,50}$"))
				errorMap.put("attributeText", "Attribute Text is incorrect");
		}
		
		return errorMap;
	} 
}
