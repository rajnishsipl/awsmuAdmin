package com.awsmu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.awsmu.builder.MessageBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.MessageDao;
import com.awsmu.entity.UserMessage;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.UserMessageAggregation;
import com.awsmu.model.UserMessageModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.MessageValidator;
import com.google.gson.Gson;
@Service(value="MessageService")
public class MessageServiceImpl implements MessageService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	MessageDao messageDao;
	
	@Override
	public GridResponse getRecentConversaction(int skipRecord,
			int skipRecordFreq, int page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {				
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();	
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total posts count after applying search filters.
					int totalRecords = messageDao.getRecentConversactionCount(searchList);
					//returns posts list after searching and sorting
					List<UserMessageAggregation> messageList =  messageDao.getRecentConversaction(skipRecord, skipRecordFreq, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(messageList.isEmpty()){						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);
						gridResponse.setRows(rows);
					}else{						
						/*// iterating posts to convert User Entity to PostsModel and add into list for grid response
						for (UserMessageAggregation messageRow : messageList) {
							System.out.println(messageRow.toString());
							rows.add(messageBuilder.fromEntityToModel(messageRow));
						}*/
						for (UserMessageAggregation messageRow : messageList) {
							//System.out.println(messageRow.toString());
							rows.add(messageRow);
						}						
						//add list of post to gridResponse rows						
						gridResponse.setRows(rows);
					}	
				}
				catch(AwsmuException e){
					//print error in debug file
					logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
					gridResponse.setStatus(false);
					gridResponse.setCode(e.getCode());
					gridResponse.setMessage(e.getDisplayMsg());
					gridResponse.setRows(null);		
				}
				catch(Exception e){
					//print error in debug file
					logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e.getStackTrace().toString());					
					gridResponse.setStatus(false);
					gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
					gridResponse.setMessage(e.getMessage());
					gridResponse.setRows(null);									
				}		
				return gridResponse;	
	}

	@Override
	public AjaxResponse getMessageThread(String chainId, int skipRecord, int skipFreq) {
		AjaxResponse ajaxResponse = new AjaxResponse();		
		try{
			MessageBuilder messageBuilder =  new MessageBuilder();		
			List<UserMessage> messageList =  messageDao.getMessageThread(chainId,skipRecord, skipFreq);					
			List<Object> rows  = new ArrayList<Object>();
			if(messageList.isEmpty()){				
				// set total number of rows as null
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);
				
			}else{						
				// iterating posts to convert User Entity to PostsModel and add into list for grid response
				Collections.reverse(messageList);
				/* process to check if message conversation with admin */
				UserMessage row=  messageList.get(0);
				ajaxResponse.setContent("{\"fromUser\":"+ new Gson().toJson(row.getFromUser())+",\"toUser\":"+new Gson().toJson(row.getToUser())+"}");
				for (UserMessage messageRow : messageList) {
					rows.add(messageBuilder.fromEntityToModel(messageRow));
				}				
				ajaxResponse.setContentList(rows);				
			}	
			ajaxResponse.setStatus(true);
		}
		catch(AwsmuException e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(e.getCode());
			ajaxResponse.setMessage(e.getDisplayMsg());			
		}
		catch(Exception e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e.getStackTrace().toString());					
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			ajaxResponse.setMessage(e.getMessage());								
		}		
		return ajaxResponse;	
	}
	/* function save user message after validating it*/
	@Override
	public AjaxResponse sendMessage(UserMessageModel userMessageModel) {
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			MessageValidator  mv = new MessageValidator();
			
			// Validate message data
			Map<String,String> messageError = mv.validateMessage(userMessageModel);
			
			if(messageError.isEmpty()){
				
				MessageBuilder messageBuilder = new MessageBuilder();
				/*Convert tip model to entity*/
				UserMessage userMessage = messageBuilder.fromModelToEntityTo(userMessageModel);
				
				/* Insert new message*/ 
				messageDao.saveMessage(userMessage);
				ajaxResponse.setContent(new Gson().toJson(messageBuilder.fromEntityToModel(userMessage)));
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Message has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(messageError);		
				ajaxResponse.setMessage("Problem while sending message.");
			}
		}
		catch(AwsmuException e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e);
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(e.getCode());
			ajaxResponse.setMessage(e.getDisplayMsg());
			
		}
		catch(Exception e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);					
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			ajaxResponse.setMessage(Properties.EXCEPTION_DATABASE+e);
										
		}		
		
		return ajaxResponse;
		
	}

}
