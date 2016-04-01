package com.awsmu.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.BookletModel;
import com.awsmu.model.GridResponse;

/**
 * BookletService service interface
 */
public interface BookletService {

	/**
	 * get user's booklet list
	 */
	public  GridResponse getUserBookletsList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters, String userId);
	
	/**
	 * get booklets list
	 */
	public  GridResponse getBookletsList(Integer skipRecord, Integer skipRecordFreq, Integer page, String sortBy,  String sortOrder, Map<Object, Object> filters);
	
	/** 
	 * get booklet detail
	 */
	public  AjaxResponse getBookletById(String plannerId);
	
	/**
	 * edit booklet by id
	 */
	public AjaxResponse saveBooklet(BookletModel bookletModel, String fileUpload, MultipartFile file);
	
	/**
	 * delete booklet
	 */
	public AjaxResponse deleteBookletById(String bookletId); 

}
