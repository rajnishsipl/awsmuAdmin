package com.awsmu.dao;

import java.util.List;
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;

public interface PostDao {
	/**
	 * get posts based on the pagination and search criteria
	 */
	List<Posts> getPosts(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;	
	/**
	 * get post detail by id
	 */
	public Posts getPostById(String postId)  throws AwsmuException;	
	/**
	 * function return the total number of records in post collection
	 */
	public  int getPostCount(List<Object> searchList) throws AwsmuException;	
	/**
	 * update post
	 */
	public void submitPostEdit(String postId,Posts posts) throws AwsmuException;
	
	/**
	 * active inactive post
	 */
	public void activeInactivePost(String postId, int status) throws AwsmuException;
	
	/**
	 * increment and decrement post comment count
	 */
	public void inDecPostCommentCount(String postId, int status) throws AwsmuException;
	
	/**
	 * increment and decrement post like count
	 */
	public void inDecPostLikeCount(String postId, int status) throws AwsmuException;

	
}