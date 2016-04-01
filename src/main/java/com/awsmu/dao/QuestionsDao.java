package com.awsmu.dao;

import java.util.List;

import com.awsmu.entity.Questions;
import com.awsmu.exception.AwsmuException;

public interface QuestionsDao { 

	/** 
	 * save celebrities 
	 */
	public Questions getQuestionByTag(String questionTag) throws AwsmuException;
	
	/**
	 * get questions list
	 */	
	public List<Questions> getQuestions(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException;
	
	/**
	 * get questions count based on the pagination criteria
	 */
	public  int getQuestionsCount(List<Object> searchList) throws AwsmuException;

	/**
	 * get question details by id  
	 */
	public Questions getQuestionById(String questionsId) throws AwsmuException;
	
	/**
	 * update Question 
	 */
	public void updateQuestion(Questions question) throws AwsmuException;
	
	/**
	 * add new Question 
	 */
	public void addQuestion(Questions question) throws AwsmuException;
	
	/**
	 * Delete question by id
	 */
	public  void deleteQuestionById(String doctorId) throws AwsmuException;
}
