package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.PlannerGroupMember;
import com.awsmu.entity.Posts;
import com.awsmu.exception.AwsmuException;

public interface PlannerGroupDao {
	/**
	 * get planner group based on the pagination and search criteria
	 */
	List<PlannerGroupMember> getPlannerGroupMember(String problemId,Integer skipRecord, Integer skipFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;
	
	/**
	 * get planner group detail by id
	 */
	public PlannerGroupMember getPlannerGroupById(String plannerGroupId)  throws AwsmuException;
	
	/**
	 * get planner group top members based on their activity earning point
	 */
	public List<PlannerGroupMember> getPlannerGroupTopMembers(String problemId)  throws AwsmuException;
	/**
	 * function return the total number of records in planner group collection
	 */
	public  int getPlannerGroupMemberCount(String problemId,List<Object> searchList) throws AwsmuException;	
	
	
	/**
	 * get planner group post based on the pagination and search criteria
	 */
	List<Posts> getPlannerGroupPosts(String problemId,Integer skipRecord, Integer skipFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;
	
	
	/**
	 * function return the total number of planner group posts records in posts collection
	 */
	public  int getPlannerGroupPostCount(String problemId,List<Object> searchList) throws AwsmuException;	
	/**
	 * update planner group
	 */
	public void submitPlannerGroupEdit(String plannerGroupId,PlannerGroupMember plannerGroup) throws AwsmuException;
	
	/**
	 * active inactive planner group
	 */
	public void activeInactivePlannerGroup(String plannerGroupId, int status) throws AwsmuException;
	
	/**
	 * increment and decrement post comment count
	 */
	public void inDecPostCommentCount(String postId, int status) throws AwsmuException;
	
	/**
	 * increment and decrement post like count
	 */
	public void inDecPostLikeCount(String postId, int status) throws AwsmuException;
	
	public void makeDatabaseChanges() throws AwsmuException;
}
