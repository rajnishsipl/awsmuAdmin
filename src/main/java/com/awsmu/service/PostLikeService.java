package com.awsmu.service;

import java.util.Map;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;

public interface PostLikeService {
	/**
	 * get post like  list
	 */
	public  GridResponse getPostLikes(String postId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	
	/**
	 * post like active/inactive action
	 */
	public AjaxResponse activeInactiveLike(String likeId, int status,String postId);

}
