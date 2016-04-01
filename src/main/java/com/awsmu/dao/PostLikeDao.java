package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.PostLikes;
import com.awsmu.exception.AwsmuException;

public interface PostLikeDao {
	/**
	 * get post likes based on the pagination and search criteria
	 */
	List<PostLikes> getPostLikes(String postId,Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;
	
	/**
	 * function return the total number of records in user collection
	 */
	public  int getPostLikeCount(String postId,List<Object> searchList) throws AwsmuException;
	
	/**
	 * active inactive post like
	 */
	public void activeInactiveLike(String likeId, int status) throws AwsmuException;
}
