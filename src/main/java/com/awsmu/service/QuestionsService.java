package com.awsmu.service;



import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.ActivitiesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.QuestionsModel;


public interface QuestionsService {
	
	
	
	/** 
	 * get questions based on the pagination criteria
	 */
	public GridResponse getQuestionsGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters); 
	
	/** 
	 * get question details by id
	 */
	public AjaxResponse getQuestionById(String questionId);
	
	
	/** 
	 * get question form data
	 */
	public AjaxResponse getFormData();
	
	
	/**
	 * add question 
	 */
	public AjaxResponse addQuestion(QuestionsModel questionmodel,String imageUpload,MultipartFile[] optionsIcons);
	
	
	/**
	 * update question 
	 */
	public AjaxResponse updateQuestion(QuestionsModel questionmodel,String imageUpload,MultipartFile[] optionsIcons);
	
	
	/** 
	 * get question details by id
	 */
	public AjaxResponse deleteQuestionById(String questionId);
	
}


