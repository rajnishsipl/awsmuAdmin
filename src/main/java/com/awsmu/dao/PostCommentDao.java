package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.PostComments;
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;
/*Post comment dao
 * Date: 23-Sep-2013
 * */
public interface PostCommentDao {
	/**
	 * get post comments based on the pagination and search criteria
	 */
	List<PostComments> getPostCommennts(String postId,Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;
	
	/**
	 * get post comment detail by  comment id
	 */
	public PostComments getPostCommentById(String id)  throws AwsmuException;
	
	/**
	 * function return the total number of records in user collection
	 */
	public  int getPostCommentCount(String postId,List<Object> searchList) throws AwsmuException;
	
	/**
	 * active inactive post comment
	 */
	public void activeInactiveComment(String commentId, int status) throws AwsmuException;
	
	
	
	/**
	 * update post comment
	 */
	public void submitPostCommentEdit(String commentId,PostComments postComments) throws AwsmuException;
}
