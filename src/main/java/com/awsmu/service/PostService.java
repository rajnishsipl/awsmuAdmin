package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PostsModel;

public interface PostService {

	/**
	 * get post list
	 */
	public  GridResponse getPosts(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	
	/**
	 * get post details
	 */
	public AjaxResponse getPostDetail(String postId);
	
	/**
	 * save post details after validation
	 */
	public AjaxResponse submitPostEdit(String postId,PostsModel postsModel);
	
	/**
	 * change status of post
	 */
	public AjaxResponse activeInactivePost(String postId,int status);
	
	
}
