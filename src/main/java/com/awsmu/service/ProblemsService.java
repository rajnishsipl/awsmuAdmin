package com.awsmu.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.ProblemModel;



public interface ProblemsService {
	
	
	
	/** 
	 * get problems based on the pagination criteria
	 */
	public GridResponse getProblems(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters); 
	
	
	/** 
	 * get problems form data
	 */
	public AjaxResponse getProblemsFormData();
	
	
	/**
	 * save problem 
	 */
	public AjaxResponse saveProblem(ProblemModel problemModel,String imageUpload, MultipartFile problemIcon, MultipartFile problemBanner);
	
	
	/**
	 * update problem 
	 */
	public AjaxResponse updateProblem(ProblemModel problemModel, String isBanner,String isIcon,MultipartFile problemIcon,MultipartFile problemBanner);
	
	/**
	 * get problem detail by problem id
	 */
	public AjaxResponse getProblemById(String problemId);
	
	/**
	 * delete problem by problem id
	 */
	public AjaxResponse deleteProblemById(String problemId);
}
