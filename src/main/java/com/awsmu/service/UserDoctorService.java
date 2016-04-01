package com.awsmu.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.UserDoctorsModel;
import com.awsmu.model.GridResponse;

/**
 * DoctorService service interface
 */
public interface UserDoctorService {

	/**
	 * get doctors list
	 */
	public  GridResponse getUserDoctorsList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters);
	
	/** 
	 * get doctor detail
	 */
	public  AjaxResponse getUserDoctorById(String doctorId);
	
	/**
	 * edit doctor by id
	 */
	public AjaxResponse saveUserDoctor(UserDoctorsModel userDoctorModel, String imageUpload, MultipartFile file);
	
	/**
	 * delete doctor
	 */
	public AjaxResponse deleteUserDoctorById(String doctorId); 
	
	/**
	 * delete doctor
	 */
	public AjaxResponse attributesList(); 

}
