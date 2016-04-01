package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.Problem;
import com.awsmu.exception.AwsmuException;

public interface ProblemsDao {
	/**
	 * get Problem list  
	 */
	public List<Problem> getProblems() throws AwsmuException;
	
	
	/**
	 * get Parent Problem list  
	 */
	public List<Problem> getParentProblems() throws AwsmuException;
	
	
	/**
	 * get all Problem list  
	 */
	public List<Problem> getAllProblems() throws AwsmuException;
	
	/**
	 * get problem list  
	 */
	public List<Problem> getProblemsList(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;

	/**
	 * function return the total number of records in problems collection
	 */
	public  int getProblemsCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * save problem 
	 */
	public void saveProblem(Problem problem) throws AwsmuException;
	
	
	/**
	 * update problem 
	 */
	public void updateProblem(Problem problem) throws AwsmuException;
	
	/**
	 * get problem detail by id
	 */
	public  Problem getProblemDetailById(String problemId) throws AwsmuException;
	
	
	
	/**
	 * check uniqueness of problem name
	 */
	public  boolean checkProblemNameExists(String problemId,String problemName ) throws AwsmuException;
	
	/**
	 * delete problem
	 */
	public  void deleteProblemById(String id) throws AwsmuException;
	
}
