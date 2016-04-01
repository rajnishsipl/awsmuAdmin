package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.UserMessageModel;

public interface MessageService {
	/**
	 * get message conversation of all users with the last conversation message
	 */
	public  GridResponse getRecentConversaction(int skipRecord, int skipRecordFreq,int page,String sortBy,String sortOrder,Map<Object, Object> filters);

	/**
	 * get message thread list between two users
	 */
	public  AjaxResponse getMessageThread(String chainId, int skipRecord, int skipFreq);
	
	/**
	 * send message
	 */
	public AjaxResponse sendMessage(UserMessageModel userMessageModel); 
}
