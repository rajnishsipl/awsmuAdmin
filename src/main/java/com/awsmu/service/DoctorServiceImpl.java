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
import com.awsmu.builder.DoctorBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.DoctorDao;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.DoctorsModel;
import com.awsmu.entity.Doctors;
import com.awsmu.util.Utils;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;

/**
 * Doctor service implementation
 */
@Service(value = "DoctorService")
public class DoctorServiceImpl implements DoctorService {

	private static Logger logger = Logger.getLogger(DoctorServiceImpl.class);

	/**
	 * Injecting doctorDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "DoctorDao")
	private DoctorDao doctorDao;
	
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
	public GridResponse getDoctorsList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {

		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		DoctorBuilder doctorBuilder = new DoctorBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = doctorDao.getDoctorsCount(searchList);
			// returns users list after searching and sorting
			List<Doctors> doctorList = doctorDao.getDoctorsList(skipRecord, skipRecord, page, sortBy, sortOrder, searchList);
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
				for (Doctors doctorRow : doctorList) {
					rows.add(doctorBuilder.fromEntityToModel(doctorRow));
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
	 * get Doctor detail by id
	 */
	@Override
	public  AjaxResponse getDoctorById(String doctorId){

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			// get planner detail 
			Doctors doctor =  doctorDao.getDoctorById(doctorId);
					
			//check if found any records
			if(doctor == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model	
				DoctorBuilder doctorBuilder = new DoctorBuilder();
				DoctorsModel doctorDetail = doctorBuilder.fromEntityToModel(doctor);
								
				//set ajax response
				String jsonDoctorDetail = new Gson().toJson(doctorDetail);
				String jsonNationalityList = new Gson().toJson(attributesDao.getNatioanlityList());
				String jsonDegreeCoursesList = new Gson().toJson(attributesDao.getDegreeCoursesList());
				String jsonSpecialtyList = new Gson().toJson(attributesDao.getSpecialtiesList());
				
				String jsonContent = "{\"doctorDetail\":"+jsonDoctorDetail+", \"nationalityList\":"+jsonNationalityList+", \"degreeCoursesList\":"+jsonDegreeCoursesList+", \"specialtyList\":"+jsonSpecialtyList+"}";
	
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
	 * save doctor
	 */
	@Override
	public  AjaxResponse saveDoctor(DoctorsModel doctorModel, String imageUpload, MultipartFile file){
AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			
			//get date
			Date now = new Date();
			
			//convert to entity		
			DoctorBuilder doctorBuilder =  new DoctorBuilder();
			Doctors doctor = doctorBuilder.fromModelToEntity(doctorModel);
			
			//check if adding booklet
			if(doctor.get_id()== null){
				
				doctor.setImage("");
				doctor.setIsDeleted(0);
				doctor.setIsActive(1);
				doctor.setCreatedDate(now);
			}
			
			//check if file uploaded
			if(imageUpload.equals("1")){
				
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				
	        	String newFileName = "doc-"+now.getTime() / 1000+"."+ext;  //unique file name  
	        	doctor.setImage(newFileName);   
	        	
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
	  	        	logger.debug(Properties.AMAZON_BUCKET_UPLOAD_EXCEPTION_ERROR +":"+ ase.getStackTrace().toString());  
	  	        	ajaxResponse.setMessage(ase.getMessage());
		  	        return ajaxResponse;
		  	        	
		  	     } catch (AmazonClientException ace) {
		  	    	 logger.debug(Properties.AMAZON_BUCKET_UPLOAD_EXCEPTION_ERROR +":"+ ace.getStackTrace().toString());	
		  	    	 ajaxResponse.setMessage(ace.getMessage());
		             return ajaxResponse;             	
		  	     }catch (Exception e) {
			        logger.debug(Properties.AMAZON_BUCKET_REMOVE_EXCEPTION_ERROR +":"+ e.getStackTrace().toString());
			        ajaxResponse.setMessage(e.getMessage());
			        return ajaxResponse;  
			     }  
	  	        
	  	        
	  	        //remove previous image if updating 
		    	if(doctor.get_id() != null){
		    		
		    		Doctors oldDoctor = doctorDao.getDoctorById(doctor.get_id());	
		    	    try {
			        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldDoctor.getImage()));
			        } catch (AmazonServiceException ase) {
			        	logger.debug(Properties.AMAZON_BUCKET_REMOVE_EXCEPTION_ERROR +":"+ ase.getStackTrace().toString());
			        	ajaxResponse.setMessage(ase.getMessage());
			    	    return ajaxResponse;
			        } catch (AmazonClientException ace) {
			        	logger.debug(Properties.AMAZON_BUCKET_REMOVE_EXCEPTION_ERROR +":"+ ace.getStackTrace().toString());
			        	ajaxResponse.setMessage(ace.getMessage());
			             return ajaxResponse;  
			        }  catch (Exception e) {
			        	logger.debug(Properties.AMAZON_BUCKET_REMOVE_EXCEPTION_ERROR +":"+ e.getStackTrace().toString());
			        	ajaxResponse.setMessage(e.getMessage());
			             return ajaxResponse;  
			        }  
		    	}
			}
			doctor.setUpdatedDate(now);
			
			//save user
			doctorDao.saveDoctor(doctor);
			
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
	 * delete doctor
	 */
	@Override
	public  AjaxResponse deleteDoctorById(String doctorId){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			// delete doctor
			doctorDao.deleteDoctorById(doctorId);
						    			     		        
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
			String jsonNationalityList = new Gson().toJson(attributesDao.getNatioanlityList());
			String jsonDegreeCoursesList = new Gson().toJson(attributesDao.getDegreeCoursesList());
			String jsonSpecialtyList = new Gson().toJson(attributesDao.getSpecialtiesList());
			
			String jsonContent = "{\"nationalityList\":"+jsonNationalityList+", \"degreeCoursesList\":"+jsonDegreeCoursesList+", \"specialtyList\":"+jsonSpecialtyList+"}";
     		
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
