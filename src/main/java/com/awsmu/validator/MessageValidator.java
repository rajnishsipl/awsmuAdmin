package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.UserMessageModel;
import com.awsmu.util.Utils;

public class MessageValidator {
	int MAX_MESSAGE_CHAR_LEN=5000;
	public Map<String,String> validateMessage(UserMessageModel userMessageModel){
		System.out.println(userMessageModel.toString());
		Map<String,String> errorMap = new HashMap<String,String>();  
		
		// Validate message text
		if(Utils.checkEmpty(userMessageModel.getMessage())){
			errorMap.put("message", "Message is required");
		}else if(Utils.checkStringLength(userMessageModel.getMessage(), this.MAX_MESSAGE_CHAR_LEN))
			errorMap.put("message", "Message character lenght should not exceed "+this.MAX_MESSAGE_CHAR_LEN+" characters");
				
		// Validate from user
		if(Utils.checkEmpty(userMessageModel.getFromUser().getUserId())){
			errorMap.put("fromUser", "Sender id is missing");
		}
		
		// Validate from user
		if(Utils.checkEmpty(userMessageModel.getToUser().getUserId())){
			errorMap.put("toUser", "Receiver id is missing");
		}
		return errorMap;
	}
}
