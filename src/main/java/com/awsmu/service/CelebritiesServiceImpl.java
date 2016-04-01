package com.awsmu.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.awsmu.builder.CelebritiesBuilder;

import com.awsmu.config.Properties;
import com.awsmu.dao.CelebritiesDao;
import com.awsmu.dao.ProblemsDao;
import com.awsmu.entity.Celebrities;

import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.CelebritiesModel;
import com.awsmu.model.GridResponse;

import com.awsmu.util.Utils;
import com.awsmu.validator.CelebritiesValidator;

import com.google.gson.Gson;


@Service(value = "CelebritiesService")
public class CelebritiesServiceImpl implements CelebritiesService {
	
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	
	
	/**
	 * Injecting celebrities dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "CelebritiesDao")
	CelebritiesDao celebritiesDao; 
	
	
	
	/**
	 * Injecting problem dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ProblemsDao")
	ProblemsDao problemsDao; 
	
	
	
	/**
	 * get celebrities based on the pagination criteria
	 */
	@Override
	public  GridResponse getCelabritiesGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
		
		
		CelebritiesBuilder celebritiesBuilder = new CelebritiesBuilder();
		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		try{
			// function return the array list of collection columns and its value after filtering the search parameters					
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = celebritiesDao.getCelebritiesCount(searchList);
			//returns users list after searching and sorting
			List<Celebrities> celebritiesList =  celebritiesDao.getCelebrities(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
			// setting total records of grid response 
			gridResponse.setRecords(totalRecords);
			
			if(totalRecords!=0) {
				int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
				gridResponse.setTotal(total);
			}	
			// set current page status of pagination for grid
			gridResponse.setPage(page);
			
			List<Object> rows  = new ArrayList<Object>();
			if(celebritiesList.isEmpty()){
				
				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			}else{						
				// iterating user to convert User Entity to UserModel and add into list for grid response
				for (Celebrities row : celebritiesList) {														
					rows.add(celebritiesBuilder.fromEntityToModel(row));
				}
				//add list of user to gridResponse rows												
				gridResponse.setRows(rows);
			}	
		}
		catch(AwsmuException e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(e.getCode());
			gridResponse.setMessage(e.getDisplayMsg());
			gridResponse.setRows(null);
			//ajaxResponse.setMessage(e.getDisplayMsg());		
		}
		catch(Exception e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e.getStackTrace().toString());					
			gridResponse.setStatus(false);
			gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			gridResponse.setMessage(e.getMessage());
			gridResponse.setRows(null);									
		}		
		return gridResponse;
		
		
	}
	
	

	/** 
	 * get celebrity details by id
	 */
	@Override
	public AjaxResponse getCelebrityById(String celebrityId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			/* Get question details by id*/
			Celebrities celebrity = celebritiesDao.getCelebrityById(celebrityId);
			if(celebrity !=null){
				CelebritiesBuilder celebritiesBuilder = new CelebritiesBuilder();
				
				/*Convert entity to model*/
				CelebritiesModel celebrityModel = celebritiesBuilder.fromEntityToModel(celebrity);
				/* set ajax response*/
				Gson gson =new Gson();
				
				String jsonCelebrityDetails =  gson.toJson(celebrityModel);
				String jsonProblemList = gson.toJson(problemsDao.getParentProblems());
				
				String jsonContent = "{\"problemList\":"+jsonProblemList+",\"celebrityDetails\":"+jsonCelebrityDetails+"}";
					     		
				ajaxResponse.setStatus(true);
				ajaxResponse.setContent(jsonContent);
			}else{
				
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);
			}
			
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e.getStackTrace().toString());
			ajaxResponse.setMessage(Properties.EXCEPTION_IN_LOGIC+"-"+e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
		
	}
	
	
	/**
	 * Add new celebrity 
	 */
	@Override
	public AjaxResponse saveCelebrity(CelebritiesModel celebritiesModel,MultipartFile mainImage){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			CelebritiesValidator celebritiesValidator = new CelebritiesValidator();
			
			// Validate celebrity data
			Map<String,String> trendError = celebritiesValidator.validateCelebrity(celebritiesModel);
			
			if(trendError.isEmpty()){
				
				CelebritiesBuilder celebritiesBuilder = new CelebritiesBuilder();
				
				/*Convert entity to model*/
				Celebrities celebrity = celebritiesBuilder.fromModelToEntity(celebritiesModel);
				
				
				String extIcon = FilenameUtils.getExtension(mainImage.getOriginalFilename());
				
				String iconFileFileName = "celebs-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
				celebrity.setMainImage(iconFileFileName);   
				celebrity.setIsDeleted(0);
	        	
	        	//upload file on amazon
	      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
	      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
	  	        try {
	  	        	//upload icon  directly into amazon
	  	        	FileInputStream stream =(FileInputStream) mainImage.getInputStream();
	  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
	  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, iconFileFileName, stream, objectMetadata);
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
				
				/* Insert new celebrity*/ 
	  	        celebritiesDao.saveCelebrity(celebrity);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Celebrity has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(trendError);		
				ajaxResponse.setMessage("There is some problem with inserted data.");
			}
		}
		catch(AwsmuException e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e);
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(e.getCode());
			ajaxResponse.setMessage(e.getDisplayMsg());
			
		}
		catch(Exception e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);					
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			ajaxResponse.setMessage(Properties.EXCEPTION_DATABASE+e);
										
		}		
		
		return ajaxResponse;
		
	}
	
	
	/**
	 * update celebrities images
	 */
	@Override
	public AjaxResponse updateCelebrity(CelebritiesModel celebritiesModel,String isImage,MultipartFile mainImage){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		
		try{
			CelebritiesValidator celebritiesValidator = new CelebritiesValidator();
			
			// Validate celebrity data
			Map<String,String> celebrityError = celebritiesValidator.validateCelebrity(celebritiesModel);
			
			if(celebrityError.isEmpty()){
				
				CelebritiesBuilder celebritiesBuilder = new CelebritiesBuilder();
				
				/*Convert entity to model*/
				Celebrities celebrity = celebritiesBuilder.fromModelToEntity(celebritiesModel);
				
				// If image file uploaded 
				if(isImage.equals("1")){
	        	    	//upload file on amazon
			      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
			      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);

						String extIcon = FilenameUtils.getExtension(mainImage.getOriginalFilename());
						String iconFileFileName = "celebs-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
						celebrity.setMainImage(iconFileFileName);   
						
			      	  	try {
			  	        	//upload icon  directly into amazon
			  	        	FileInputStream stream =(FileInputStream) mainImage.getInputStream();
			  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
			  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, iconFileFileName, stream, objectMetadata);
			  	        	putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			  	        	s3client.putObject(putObjectRequest);
			  	        	
			  	        	//remove icon from amazon
			  	        	Celebrities oldCelebrity = celebritiesDao.getCelebrityById(celebrity.get_id());
			  	        	if(!oldCelebrity.getMainImage().equals("") || !oldCelebrity.getMainImage().equals(null))
				  	        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldCelebrity.getMainImage()));
			  	        	
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
				}
				
				
				/* Update new celebrity*/ 
	  	        celebritiesDao.updateCelebrity(celebrity);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Celebrity has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(celebrityError);		
				ajaxResponse.setMessage("There is some problem with inserted data.");
			}
		}
		catch(AwsmuException e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e);
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(e.getCode());
			ajaxResponse.setMessage(e.getDisplayMsg());
			
		}
		catch(Exception e){
			//print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);					
			ajaxResponse.setStatus(false);
			ajaxResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			ajaxResponse.setMessage(Properties.EXCEPTION_DATABASE+e);
										
		}		
		return ajaxResponse;
	} 
	

	/**
	 * Get tip form data
	 */
	@Override
	public AjaxResponse getFormData(){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
				//set ajax response
				Gson gson = new Gson();
				String jsonProblemList = gson.toJson(problemsDao.getParentProblems());
				String jsonContent = "{\"problemList\":"+jsonProblemList+"}";
				ajaxResponse.setStatus(true);
				ajaxResponse.setContent(jsonContent);
			}
			catch(AwsmuException e){
				logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
				ajaxResponse.setMessage(e.getDisplayMsg());
				ajaxResponse.setCode(e.getCode());
			}
			catch(Exception e){
				logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e.getStackTrace().toString());
				ajaxResponse.setMessage(e.getMessage());
				ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
			}
	
	return ajaxResponse;
	}
	
	/** 
	 * Delete celebrity  by id
	 */
	@Override
	public AjaxResponse deleteCelebrityById(String celebrityId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
			
			/*delete question by question id*/
			celebritiesDao.deleteCelebrityById(celebrityId);
			
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Celebrity deleted successfully.");
		   
			
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
			ajaxResponse.setMessage(e.getDisplayMsg());
			ajaxResponse.setCode(e.getCode());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e.getStackTrace().toString());
			ajaxResponse.setMessage(Properties.EXCEPTION_IN_LOGIC+"-"+e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
		
	}
	
	
	
}
