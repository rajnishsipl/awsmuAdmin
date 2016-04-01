package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PostCommentsModel;
import com.awsmu.model.PostsModel;

public interface PostCommentService {
	/**
	 * get post list
	 */
	public  GridResponse getPostComments(String postId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	
	/**
	 * get post details
	 */
	public AjaxResponse getCommentDetail(String commentId);
	
	/**
	 * change status of comment
	 */
	public AjaxResponse activeInactiveComment(String commentId,int status,String postId);
	
	
	/**
	 * save post comment details after validation
	 */
	public AjaxResponse submitPostCommentEdit(String postId,PostCommentsModel postCommentModel);
}
