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
import com.awsmu.builder.UserDoctorBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.UserDoctorDao;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.UserDoctorsModel;
import com.awsmu.entity.UserDoctors;
import com.awsmu.util.Utils;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;

/**
 * Doctor service implementation
 */
@Service(value = "UserDoctorService")
public class UserDoctorServiceImpl implements UserDoctorService {

	private static Logger logger = Logger.getLogger(UserDoctorServiceImpl.class);

	/**
	 * Injecting userDoctorDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "UserDoctorDao")
	private UserDoctorDao userDoctorDao;
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;
	
	/**
	 * get doctors based on the pagination criteria, also perform searching
	 * sorting This function specially works to return grid response for jqgrid
	 */
	@Override
	public GridResponse getUserDoctorsList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {

		// creating object of grid response classDoctorDaoImpl.java
		GridResponse gridResponse = new GridResponse();
		UserDoctorBuilder userDoctorBuilder = new UserDoctorBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = userDoctorDao.getUserDoctorsCount(searchList);
			// returns users list after searching and sorting
			List<UserDoctors> doctorList = userDoctorDao.getUserDoctorsList(skipRecord, skipRecord, page, sortBy, sortOrder, searchList);
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
			if (doctorList.isEmpty()) {

				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			} else {
				// iterating user to convert User Entity to UserModel and add
				// into list for grid response
				for (UserDoctors doctorRow : doctorList) {
					rows.add(userDoctorBuilder.fromEntityToModel(doctorRow));
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
	 * get User's Doctor detail by id
	 */
	@Override
	public  AjaxResponse getUserDoctorById(String doctorId){

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			// get planner detail 
			UserDoctors userDoctor =  userDoctorDao.getUserDoctorById(doctorId);
					
			//check if found any records
			if(userDoctor == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model	
				UserDoctorBuilder userDoctorBuilder = new UserDoctorBuilder();
				UserDoctorsModel doctorDetail = userDoctorBuilder.fromEntityToModel(userDoctor);
								
				//set ajax response
				String jsonDoctorDetail = new Gson().toJson(doctorDetail);
				String jsonSpecialtyList = new Gson().toJson(attributesDao.getSpecialtiesList());
				
				String jsonContent = "{\"doctorDetail\":"+jsonDoctorDetail+", \"specialtyList\":"+jsonSpecialtyList+"}";
	
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
	 * save user's doctor
	 */
	@Override
	public  AjaxResponse saveUserDoctor(UserDoctorsModel userDoctorModel, String imageUpload, MultipartFile file){
AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			
			//get date
			Date now = new Date();
			
			//convert to entity		
			UserDoctorBuilder userDoctorBuilder =  new UserDoctorBuilder();
			UserDoctors userDoctor = userDoctorBuilder.fromModelToEntity(userDoctorModel);
			
			//check if adding booklet
			if(userDoctor.get_id()== null){
				
				userDoctor.setImage("");
				userDoctor.setIsDeleted(0);
				userDoctor.setIsActive(1);
				userDoctor.setCreatedDate(now);
			}
			
			//check if file uploaded
			if(imageUpload.equals("1")){
				
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				
	        	String newFileName = "userDoc-"+now.getTime() / 1000+"."+ext;  //unique file name  
	        	userDoctor.setImage(newFileName);   
	        	
	        	//upload file on amazon
	      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
	      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
	  	        try {
	  	        	//upload file directly into amazon
	  	        	FileInputStream stream =(FileInputStream) file.getInputStream();
	  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
	  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_PROFILE_PIC_PATH, newFileName, stream, objectMetadata);
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
		    	if(userDoctor.get_id() != null){
		    		
		    		UserDoctors oldDoctor = userDoctorDao.getUserDoctorById(userDoctor.get_id());	
		    	    try {
			        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldDoctor.getImage()));
			        } catch (AmazonServiceException ase) {
			        	ajaxResponse.setMessage(ase.getMessage());
			    	    return ajaxResponse;
			        } catch (AmazonClientException ace) {
			        	ajaxResponse.setMessage(ace.getMessage());
			             return ajaxResponse;  
			        }        
		    	}
			}
			userDoctor.setUpdatedDate(now);
			
			//save user
			userDoctorDao.saveUserDoctor(userDoctor);
			
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Doctor saved successfully");
			
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
	 * delete user's doctor
	 */
	@Override
	public  AjaxResponse deleteUserDoctorById(String doctorId){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			// delete doctor
			userDoctorDao.deleteUserDoctorById(doctorId);
						    			     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Doctor deleted successfully.");
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
	 * get doctors attributes
	 */
	@Override
	public  AjaxResponse attributesList(){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			String jsonSpecialtyList = new Gson().toJson(attributesDao.getSpecialtiesList());
			
			String jsonContent = "{\"specialtyList\":"+jsonSpecialtyList+"}";
     		
			ajaxResponse.setStatus(true);
			ajaxResponse.setContent(jsonContent);
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
