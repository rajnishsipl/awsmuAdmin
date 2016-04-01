package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.PostBuilder;
import com.awsmu.builder.PostCommemtBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.PostCommentDao;
import com.awsmu.dao.PostDao;
import com.awsmu.entity.PostComments;
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PostCommentsModel;
import com.awsmu.model.PostsModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.PostCommentValidator;
import com.awsmu.validator.PostValidator;
import com.google.gson.Gson;
@Service(value = "PostCommentService")
public class PostCommentServiceImpl implements PostCommentService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	/*Injecting post comment*/
	/*@Autowired
	@Qualifier("PostCommentDao")
	PostCommentDao postCommentDao;*/

	
	/**
	 * Injecting postDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "PostCommentDao")
	private PostCommentDao postCommentDao;
	
	@Autowired(required = true)
	@Qualifier(value = "PostDao")
	private PostDao postDao;
	
	@Override
	public GridResponse getPostComments(String postId, Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {
		// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				PostCommemtBuilder postCommentBuilder =  new PostCommemtBuilder();				 
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total posts count after applying search filters.
					int totalRecords = postCommentDao.getPostCommentCount(postId,searchList);
					//returns posts list after searching and sorting
					List<PostComments> postCommentList =  postCommentDao.getPostCommennts(postId,skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);					
					List<Object> rows  = new ArrayList<Object>();
					if(postCommentList.isEmpty()){						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);
						gridResponse.setRows(rows);
					}else{						
						// iterating posts to convert User Entity to PostsModel and add into list for grid response
						for (PostComments postRow : postCommentList) {																			
							rows.add(postCommentBuilder.fromEntityToModel(postRow));
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
	public AjaxResponse getCommentDetail(String commentId) {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		PostCommemtBuilder postCommentBuilder =  new PostCommemtBuilder();	
		try{
			 // get post obj 
			PostComments postComment =  postCommentDao.getPostCommentById(commentId);
			//check if found any records
			if(postComment == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				//convert from entity to model				
				PostCommentsModel postCommentDetail = postCommentBuilder.fromEntityToModel(postComment);
				//set ajax response
				String jsonPostCommentDetail = new Gson().toJson(postCommentDetail);
				String jsonContent = "{\"postCommentDetail\":"+jsonPostCommentDetail+"}";
				ajaxResponse.setStatus(true);
				ajaxResponse.setContent(jsonContent);
			}
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
			ajaxResponse.setMessage(e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
	}

	@Override
	public AjaxResponse activeInactiveComment(String commentId, int status,String postId) {
		AjaxResponse ajaxResponse = new AjaxResponse();		
		try{			
			// active / inactive post comment
			postCommentDao.activeInactiveComment(commentId,status);
			postDao.inDecPostCommentCount(postId, status);
			//inDecPostCommentCount
			
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Post comment status changed successfully.");
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
			ajaxResponse.setMessage(e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}		
		return ajaxResponse;
	}

	@Override
	public AjaxResponse submitPostCommentEdit(String commentId,
			PostCommentsModel postCommentModel) {
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			PostCommentValidator  pcv = new PostCommentValidator();
			
			// Validate post data
			Map<String,String> postError = pcv.validatePostComment(postCommentModel);
			
			if(postError.isEmpty()){
				
				PostCommemtBuilder postCommentBuilder = new PostCommemtBuilder();
				/*Convert tip model to entity*/
				PostComments postComments = postCommentBuilder.fromModelToEntity(postCommentModel);
				
				/* update post data*/ 
				postCommentDao.submitPostCommentEdit(commentId,postComments);
				//ajaxResponse.setContent(new Gson().toJson(messageBuilder.fromEntityToModel(userMessage)));
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Comment has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(postError);		
				ajaxResponse.setMessage("Problem while editing post.");
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
