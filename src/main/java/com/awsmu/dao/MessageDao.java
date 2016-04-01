package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.UserMessage;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.UserMessageAggregation;

public interface MessageDao {
	/**
	 * get message conversation based on the pagination and search criteria and get last message of conversation
	 */
	List<UserMessageAggregation> getRecentConversaction(Integer skipRecord, Integer skipFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;
	
	/**
	 * function return the total number of records in user collection
	 */
	public  int getRecentConversactionCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * get message list id
	 */
	public List<UserMessage> getMessageThread(String chainId, int skipRecord, int skipFreq)  throws AwsmuException;
	
	/**
	 * save message to database
	 */
	
	public String saveMessage(UserMessage userMessage) throws AwsmuException;
}
