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
import com.awsmu.builder.TrendsBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.TrendsDao;
import com.awsmu.entity.Trends;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.TrendsModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.TrendsValidator;
import com.google.gson.Gson;


@Service(value = "TrendsService")
public class TrendsServiceImpl implements TrendsService {

	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Injecting trends dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "TrendsDao")
	TrendsDao trendsDao; 
	
	
	
	/**
	 * get trends based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getTrendsGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				TrendsBuilder trendsBuilder =  new TrendsBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = trendsDao.getTrendsCount(searchList);
					//returns users list after searching and sorting
					List<Trends> trendsList =  trendsDao.getTrendsList(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(trendsList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Trends row : trendsList) {														
							rows.add(trendsBuilder.fromEntityToModel(row));
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
	 * save trend 
	 */
	@Override
	public AjaxResponse saveTrend(TrendsModel trendsModel,MultipartFile trendIcon){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			TrendsValidator trendsValidator = new TrendsValidator();
			
			// Validate trend data
			Map<String,String> trendError = trendsValidator.validateTrend(trendsModel);
			
			/* Validate uniqueness of trend name */ 
			if(trendsDao.checkTrendTitleExists(trendsModel.get_id(),trendsModel.getTitle())){
				trendError.put("title", "Trend title is already exist");
			}
			
			
			if(trendError.isEmpty()){
				
				TrendsBuilder trendsBuilder = new TrendsBuilder();
				/*Convert trend model to entity*/
				Trends trends = trendsBuilder.fromModelToEntity(trendsModel);
				
				
				
				
				String extIcon = FilenameUtils.getExtension(trendIcon.getOriginalFilename());
				
				String iconFileFileName = "icon-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
				trends.setIcon(iconFileFileName);   
			
	        	
	        	//upload file on amazon
	      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
	      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
	  	        try {
	  	        	//upload icon  directly into amazon
	  	        	FileInputStream stream =(FileInputStream) trendIcon.getInputStream();
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
				
				/* Insert new trend*/ 
	  	        trends.setIsDeleted(0);
				trendsDao.saveTrend(trends);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Trend has been added successfully.");
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
	 * update trend 
	 */
	@Override
	public AjaxResponse updateTrend(TrendsModel trendsModel,String isIcon,MultipartFile trendIcon){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			TrendsValidator trendsValidator = new TrendsValidator();
			
			// Validate trend data
			Map<String,String> trendError = trendsValidator.validateTrend(trendsModel);
			
			/* Validate uniqueness of trend name */ 
			if(trendsDao.checkTrendTitleExists(trendsModel.get_id(),trendsModel.getTitle())){
				trendError.put("title", "Trend title is already exist");
			}
			
			
			if(trendError.isEmpty()){
				
				TrendsBuilder trendsBuilder = new TrendsBuilder();
				/*Convert trend model to entity*/
				Trends trends = trendsBuilder.fromModelToEntity(trendsModel);
				
				/*if trend icon updated */
				if(isIcon.equals("1")){
					String extIcon = FilenameUtils.getExtension(trendIcon.getOriginalFilename());
					
					String iconFileFileName = "icon-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
					trends.setIcon(iconFileFileName);   
				
		        	
		        	//upload file on amazon
		      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
		      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
		  	        try {
		  	        	//upload icon  directly into amazon
		  	        	FileInputStream stream =(FileInputStream) trendIcon.getInputStream();
		  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
		  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, iconFileFileName, stream, objectMetadata);
		  	        	putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		  	        	s3client.putObject(putObjectRequest);
		  	        	//remove icon from amazon
		  	        	Trends oldtrend = trendsDao.getTrendDetailsById(trends.get_id());	
			    	    if(!oldtrend.getIcon().equals("") || !oldtrend.getIcon().equals(null))
		  	        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldtrend.getIcon()));
		  	        	
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
				
				
				
				/* update trend*/ 
				trendsDao.updateTrend(trends);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Trend has been updated successfully.");
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
	 * Get trend by trend id  
	 */
	@Override
	public AjaxResponse getTrendById(String trendId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			/*Get problem details*/ 
			Trends trendDetail = trendsDao.getTrendDetailsById(trendId);
			
			if(trendDetail !=null){
				TrendsBuilder trendBuilder = new TrendsBuilder();
				TrendsModel trendModel =   trendBuilder.fromEntityToModel(trendDetail);
				
				//set ajax response
				String jsonTrendDetails = new Gson().toJson(trendModel);
				String jsonContent = "{\"trendDetails\":"+jsonTrendDetails+"}";
				
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
				ajaxResponse.setMessage(e.getMessage());
				ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
			}
	
		return ajaxResponse;
	}
	
	
	
	
	/** 
	 * Delete trend  by id
	 */
	@Override
	public AjaxResponse deleteTrendById(String trendId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
			
			/*delete question by question id*/
			trendsDao.deleteTrendById(trendId);
			
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Trend deleted successfully.");
		   
			
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
