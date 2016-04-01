package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.PostLikeBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.PostDao;
import com.awsmu.dao.PostLikeDao;
import com.awsmu.entity.PostLikes;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.util.Utils;
@Service(value="PostLikeService")
public class PostLikeServiceImpl implements PostLikeService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	@Qualifier(value="PostLikeDao")
	PostLikeDao postLikeDao;
	
	@Autowired
	@Qualifier(value="PostDao")
	PostDao postDao;
	
	
	@Override
	public GridResponse getPostLikes(String postId, Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {
		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		PostLikeBuilder postLikeBuilder =  new PostLikeBuilder();				 
		try{
			// function return the array list of collection columns and its value after filtering the search parameters					
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total posts count after applying search filters.
			int totalRecords = postLikeDao.getPostLikeCount(postId,searchList);
			//returns posts list after searching and sorting
			List<PostLikes> postLikeList =  postLikeDao.getPostLikes(postId,skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
			// setting total records of grid response 
			gridResponse.setRecords(totalRecords);
			
			if(totalRecords!=0) {
				int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
				gridResponse.setTotal(total);
			}	
			// set current page status of pagination for grid
			gridResponse.setPage(page);					
			List<Object> rows  = new ArrayList<Object>();
			if(postLikeList.isEmpty()){						
				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);
				gridResponse.setRows(rows);
			}else{						
				// iterating posts to convert User Entity to PostsModel and add into list for grid response
				for (PostLikes postRow : postLikeList) {																			
					rows.add(postLikeBuilder.fromEntityToModel(postRow));
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
	public AjaxResponse activeInactiveLike(String likeId, int status,String postId) {
		AjaxResponse ajaxResponse = new AjaxResponse();		
		try{			
			// active / inactive post comment
			postLikeDao.activeInactiveLike(likeId,status);
			postDao.inDecPostLikeCount(postId, status);
			//inDecPostCommentCount
			
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Post like status changed successfully.");
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

}
