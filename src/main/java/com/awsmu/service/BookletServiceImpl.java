package com.awsmu.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Qualifier;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.awsmu.builder.BookletBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.BookletDao;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.BookletModel;
import com.awsmu.entity.Booklet;
import com.awsmu.util.Utils;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;

/**
 * Booklet service implementation
 */
@Service(value = "BookletService")
public class BookletServiceImpl implements BookletService {

	private static Logger logger = Logger.getLogger(BookletServiceImpl.class);

	/**
	 * Injecting bookletDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "BookletDao")
	private BookletDao bookletDao;

	
	/**
	 * get user's booklets based on the pagination criteria, also perform
	 * searching sorting This function specially works to return grid response
	 * for jqgrid
	 */
	@Override
	public GridResponse getUserBookletsList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters, String userId) {

		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		BookletBuilder userBookletBuilder = new BookletBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = bookletDao.getUserBookletsCount(searchList, userId);
			// returns users list after searching and sorting
			List<Booklet> userBookletsList = bookletDao.getUserBookletsList(skipRecord, skipRecord, page, sortBy, sortOrder, searchList, userId);
			// setting total records of grid response
			gridResponse.setRecords(totalRecords);

			if (totalRecords != 0) {
				int total = (int) Math.ceil((float) totalRecords
						/ (float) skipRecordFreq);
				gridResponse.setTotal(total);
			}
			// set current page status of pagination for grid
			gridResponse.setPage(page);

			List<Object> rows = new ArrayList<Object>();
			if (userBookletsList.isEmpty()) {

				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			} else {
				// iterating user to convert User Entity to UserModel and add
				// into list for grid response
				for (Booklet userBookletRow : userBookletsList) {
					rows.add(userBookletBuilder.fromEntityToModel(userBookletRow));
				}
				// add list of user to gridResponse rows
				gridResponse.setRows(rows);
			}
		} catch (AwsmuException e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(e.getCode());
			gridResponse.setMessage(e.getDisplayMsg());
			gridResponse.setRows(null);
			// ajaxResponse.setMessage(e.getDisplayMsg());
		} catch (Exception e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			gridResponse.setMessage(e.getMessage());
			gridResponse.setRows(null);
		}
		return gridResponse;
	}

	/**
	 * get booklets based on the pagination criteria, also perform searching
	 * sorting This function specially works to return grid response for jqgrid
	 */
	@Override
	public GridResponse getBookletsList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {

		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		BookletBuilder bookletBuilder = new BookletBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = bookletDao.getBookletsCount(searchList);
			// returns users list after searching and sorting
			List<Booklet> bookletList = bookletDao.getBookletsList(skipRecord, skipRecord, page, sortBy, sortOrder, searchList);
			// setting total records of grid response
			gridResponse.setRecords(totalRecords);

			if (totalRecords != 0) {
				int total = (int) Math.ceil((float) totalRecords
						/ (float) skipRecordFreq);
				gridResponse.setTotal(total);
			}
			// set current page status of pagination for grid
			gridResponse.setPage(page);

			List<Object> rows = new ArrayList<Object>();
			if (bookletList.isEmpty()) {

				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			} else {
				// iterating user to convert User Entity to UserModel and add
				// into list for grid response
				for (Booklet bookletRow : bookletList) {
					rows.add(bookletBuilder.fromEntityToModel(bookletRow));
				}
				// add list of user to gridResponse rows
				gridResponse.setRows(rows);
			}
		} catch (AwsmuException e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(e.getCode());
			gridResponse.setMessage(e.getDisplayMsg());
			gridResponse.setRows(null);
			// ajaxResponse.setMessage(e.getDisplayMsg());
		} catch (Exception e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			gridResponse.setMessage(e.getMessage());
			gridResponse.setRows(null);
		}
		return gridResponse;
	}

	/**
	 * get booklet detail by id
	 */
	@Override
	public  AjaxResponse getBookletById(String bookletId){

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			// get planner detail 
			Booklet booklet =  bookletDao.getBookletById(bookletId);
					
			//check if found any records
			if(booklet == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model	
				BookletBuilder bookletBuilder = new BookletBuilder();
				BookletModel bookletDetail = bookletBuilder.fromEntityToModel(booklet);
								
				//set ajax response
				String jsonBookletDetail = new Gson().toJson(bookletDetail);
			
								
				String jsonContent = "{ \"bookletDetail\":"+jsonBookletDetail
									
									 +"}";
         		
				ajaxResponse.setStatus(true);
				ajaxResponse.setContent(jsonContent);
			}
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
			ajaxResponse.setMessage(e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
	}
	
	/**
	 * save booklet
	 */
	@Override
	public  AjaxResponse saveBooklet(BookletModel bookletModel, String fileUpload, MultipartFile file){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			
			//get date
			Date now = new Date();
			
			//convert to entity		
			BookletBuilder bookletBuilder =  new BookletBuilder();
			Booklet booklet = bookletBuilder.fromModelToEntity(bookletModel);
			
			//check if adding booklet
			if(booklet.get_id()== null){
				
				booklet.setFile("");
				booklet.setFileType("");
				booklet.setIsDeleted(0);
				booklet.setIsActive(1);
				booklet.setCreatedDate(now);
			}
			
			//check if file uploaded
			if(fileUpload.equals("1")){
				
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
	        	booklet.setFileType(ext);      
	        	
	        	String newFileName = booklet.get_id()+(now.getTime() / 1000)+"."+ext;  //unique file name  
	        	booklet.setFile(newFileName);   
	        	
	        	//upload file on amazon
	      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
	      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
	  	        try {
	  	        	//upload file directly into amazon
	  	        	FileInputStream stream =(FileInputStream) file.getInputStream();
	  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
	  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_BOOKLET_PATH, newFileName, stream, objectMetadata);
	  	        	putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
	  	        	s3client.putObject(putObjectRequest);
	  	         
	  	         } catch (AmazonServiceException ase) {
	  		  	      
	  	        	ajaxResponse.setMessage(ase.getMessage());
		  	        return ajaxResponse;
		  	        	
		  	     } catch (AmazonClientException ace) {
		  	        	
		  	    	 ajaxResponse.setMessage(ace.getMessage());
		             return ajaxResponse;             	
		  	     }
	  	        
	  	        
	  	        //remove previous image if updating 
		    	if(booklet.get_id() != null){
		    		
		    		Booklet oldBooklet = bookletDao.getBookletById(booklet.get_id());	
		    	    try {
			        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldBooklet.getFile()));
			        } catch (AmazonServiceException ase) {
			        	ajaxResponse.setMessage(ase.getMessage());
			    	    return ajaxResponse;
			        } catch (AmazonClientException ace) {
			        	ajaxResponse.setMessage(ace.getMessage());
			             return ajaxResponse;  
			        }        
		    	}
			}
			booklet.setUpdatedDate(now);
			
			//save user
			bookletDao.saveBooklet(booklet);
			
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Booklet saved successfully");
			
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
			ajaxResponse.setMessage(e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
	}
	
	/**
	 * delete booklet
	 */
	@Override
	public  AjaxResponse deleteBookletById(String bookletId){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			// delete booklet
			bookletDao.deleteBookletById(bookletId);
						    			     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Booklet deleted successfully.");
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
			ajaxResponse.setMessage(e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
	}
}
