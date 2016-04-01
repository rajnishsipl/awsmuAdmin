package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.awsmu.builder.PostBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.PostDao;
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PostsModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.PostValidator;
import com.google.gson.Gson;
@Service(value = "PostService")
public class PostServiceImpl implements PostService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	/**
	 * Injecting postDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "PostDao")
	private PostDao postDao;
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;
	
	@Override
	public GridResponse getPosts(Integer skipRecord, Integer skipRecordFreq,
			Integer page, String sortBy, String sortOrder,
			Map<Object, Object> filters) {
		
		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		PostBuilder postBuilder =  new PostBuilder();			
		try{
			// function return the array list of collection columns and its value after filtering the search parameters					
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total posts count after applying search filters.
			int totalRecords = postDao.getPostCount(searchList);
			//returns posts list after searching and sorting
			List<Posts> userList =  postDao.getPosts(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
			// setting total records of grid response 
			gridResponse.setRecords(totalRecords);
			
			if(totalRecords!=0) {
				int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
				gridResponse.setTotal(total);
			}	
			// set current page status of pagination for grid
			gridResponse.setPage(page);
			
			List<Object> rows  = new ArrayList<Object>();
			if(userList.isEmpty()){
				
				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			}else{						
				// iterating posts to convert User Entity to PostsModel and add into list for grid response
				for (Posts postRow : userList) {																			
					rows.add(postBuilder.fromEntityToModel(postRow));
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

	/* function return post details*/
	@Override
	public AjaxResponse getPostDetail(String postId) {		
		AjaxResponse ajaxResponse = new AjaxResponse();
		PostBuilder postBuilder =  new PostBuilder();	
		try{
			 // get post obj 
			Posts posts =  postDao.getPostById(postId);
			//check if found any records
			if(posts == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model				
				PostsModel postDetail = postBuilder.fromEntityToModel(posts);
				
				//set ajax response
				String jsonPostDetail = new Gson().toJson(postDetail);
				String jsonTrendsList = new Gson().toJson(attributesDao.getTrendsList());				
				//String jsonContent = "{\"postDetail\":"+jsonPostDetail+", \"userAttributesList\":"+jsonTrendsList+", \"attrQuestionsList\":"+jsonAttrQuestionsList+", \"nationalityList\":"+jsonNationalityList+", \"professionList\":"+jsonProfessionList+", \"degreeCoursesList\":"+jsonDegreeCoursesList+", \"specialtyList\":"+jsonSpecialtyList+"}";
				String jsonContent = "{\"postDetail\":"+jsonPostDetail+", \"trendsList\":"+jsonTrendsList+"}";
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
	public AjaxResponse submitPostEdit(String postId, PostsModel postsModel) {
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			PostValidator  pv = new PostValidator();
			
			// Validate post data
			Map<String,String> postError = pv.validatePost(postsModel);
			
			if(postError.isEmpty()){
				
				PostBuilder postBuilder = new PostBuilder();
				/*Convert tip model to entity*/
				Posts posts = postBuilder.fromModelToEntity(postsModel);
				
				/* update post data*/ 
				postDao.submitPostEdit(postId,posts);
				//ajaxResponse.setContent(new Gson().toJson(messageBuilder.fromEntityToModel(userMessage)));
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Post has been saved successfully.");
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

	@Override
	public AjaxResponse activeInactivePost(String postId, int status) {
		AjaxResponse ajaxResponse = new AjaxResponse();		
		try{			
			// active / inactive post
			postDao.activeInactivePost(postId,status);
						    			     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Post status changed successfully.");
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
