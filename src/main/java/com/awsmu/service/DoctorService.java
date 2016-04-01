package com.awsmu.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.DoctorsModel;
import com.awsmu.model.GridResponse;

/**
 * DoctorService service interface
 */
public interface DoctorService {

	/**
	 * get doctors list
	 */
	public  GridResponse getDoctorsList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters);
	
	/** 
	 * get doctor detail
	 */
	public  AjaxResponse getDoctorById(String doctorId);
	
	/**
	 * edit doctor by id
	 */
	public AjaxResponse saveDoctor(DoctorsModel doctorModel, String imageUpload, MultipartFile file);
	
	/**
	 * delete doctor
	 */
	public AjaxResponse deleteDoctorById(String doctorId); 
	
	/**
	 * delete doctor
	 */
	public AjaxResponse attributesList(); 

}
