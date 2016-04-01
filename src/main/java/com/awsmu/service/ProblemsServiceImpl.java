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
import com.awsmu.builder.ProblemBuilder;
import com.awsmu.builder.ProblemsBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.ProblemsDao;
import com.awsmu.dao.TrendsDao;
import com.awsmu.entity.Problem;
import com.awsmu.entity.TrendValues;
import com.awsmu.entity.Trends;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.ProblemModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.ProblemsValidator;
import com.google.gson.Gson;



@Service("ProblemsService")
public class ProblemsServiceImpl implements ProblemsService {
	
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	/**
	 * Injecting problem dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ProblemsDao")
	ProblemsDao problemsDao; 
	
	/**
	 * Injecting trends dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "TrendsDao")
	TrendsDao trendsDao; 
	

	/**
	 * get problems based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getProblems(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				ProblemsBuilder problemsBuilder =  new ProblemsBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = problemsDao.getProblemsCount(searchList);
					//returns users list after searching and sorting
					List<Problem> problemList =  problemsDao.getProblemsList(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(problemList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Problem userRow : problemList) {														
							rows.add(problemsBuilder.fromEntityToModel(userRow));
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
	 * get Problems Form Data 
	 */
	@Override
	public AjaxResponse getProblemsFormData(){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			//Get trend list
			List<Trends>  trendsList = trendsDao.getTrends();
			//Get problem list
			List<Problem>  problemList = problemsDao.getParentProblems();
			
			//set ajax response
			String jsonTrendList = new Gson().toJson(trendsList);
			String jsonProblemList = new Gson().toJson(problemList);
			// Get trend details
			
			
			String jsonContent = "{\"problemList\":"+jsonProblemList+", \"trendList\":"+jsonTrendList+"}";
			
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
	 * save problem 
	 */
	@Override
	public AjaxResponse saveProblem(ProblemModel problemModel,String imageUpload, MultipartFile problemIcon, MultipartFile problemBanner){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			ProblemsValidator problemsValidator = new ProblemsValidator();
			
			// Validate problem data
			Map<String,String> problemError = problemsValidator.validateProblem(problemModel);
			
			/* Validate uniqueness of problem name */ 
			if(problemsDao.checkProblemNameExists(problemModel.get_id(),problemModel.getName())){
				problemError.put("name", "Problem name is already exist");
			}
			
			
			if(problemError.isEmpty()){
				
				ProblemBuilder problemBuilder = new ProblemBuilder();
				
				List <TrendValues> trends = new ArrayList<TrendValues>();
				
				/* prepare trendvalues list*/ 
				for(TrendValues trendValues : problemModel.getTrends()){
					trendValues.setTrendId(trendValues.get_id());
					trends.add(trendValues);
				}
				problemModel.setTrends(trends);
				
				/*Convert problem model to entity*/
				Problem problem = problemBuilder.fromModelToEntity(problemModel);
				
				
				/*upload problem Icon */
				if(imageUpload.equals("1")){
					
					String extIcon = FilenameUtils.getExtension(problemIcon.getOriginalFilename());
					
					String extBenner = FilenameUtils.getExtension(problemBanner.getOriginalFilename());
					
		        	
					String iconFileFileName = "icon-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
		        	problem.setIcon(iconFileFileName);   
				
		        	String bennerFileName = "banner-"+new Date().getTime() / 1000+"."+extBenner;  //unique file name  
		        	problem.setBanner(bennerFileName);   
				
		        	
		        	//upload file on amazon
		      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
		      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
		  	        try {
		  	        	//upload icon  directly into amazon
		  	        	FileInputStream stream =(FileInputStream) problemIcon.getInputStream();
		  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
		  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, iconFileFileName, stream, objectMetadata);
		  	        	putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		  	        	s3client.putObject(putObjectRequest);
		  	        	
		  	        	
		  	        	//upload banner directly into amazon
		  	        	FileInputStream streamBanner =(FileInputStream) problemBanner.getInputStream();
		  	                    
		  	        	PutObjectRequest putObjectBannerRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, bennerFileName, streamBanner, objectMetadata);
		  	        	putObjectBannerRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		  	        	s3client.putObject(putObjectBannerRequest);
		  	         
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
				
				
				
				
				
				/* Insert new problem*/ 
				problemsDao.saveProblem(problem);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Problem has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(problemError);		
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
	 * update Problem data 
	 */
	@Override
	public AjaxResponse updateProblem(ProblemModel problemModel, String isBanner,String isIcon,MultipartFile problemIcon,MultipartFile problemBanner){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			ProblemsValidator problemsValidator = new ProblemsValidator();
			
			/* Validate problem model data */
			Map<String,String> problemError = problemsValidator.validateProblem(problemModel);
			
			/* Validate uniqueness of problem name */ 
			if(problemsDao.checkProblemNameExists(problemModel.get_id(),problemModel.getName()))
				problemError.put("name", "Problem name is already exist");
			
			
			if(problemError.isEmpty()){
				ProblemBuilder problemBuilder = new ProblemBuilder();
				
				/*Create trends values list for problem data using trends string list*/
				
				List <TrendValues> trends = new ArrayList<TrendValues>();
				for(String trendId : problemModel.getTrendsIdList()){
					TrendValues trendValues = new TrendValues();
					Trends trend = trendsDao.getTrendDetailsById(trendId);
					
					trendValues.setTrendId(trend.get_id());
					trendValues.setIcon(trend.getIcon());
					trendValues.setTitle(trend.getTitle());
					trendValues.set_id(trend.get_id());
					
					trends.add(trendValues);
				}
				problemModel.setTrends(trends);
				
				/*Convert problem model to problem entity*/
				Problem problem = problemBuilder.fromModelToEntity(problemModel);
				
				
				/*upload problem Icon */
				if(isIcon.equals("1")){
					
					String extIcon = FilenameUtils.getExtension(problemIcon.getOriginalFilename());
					
					String iconFileFileName = "icon-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
		        	problem.setIcon(iconFileFileName);   
				
		        	
		        	//upload file on amazon
		      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
		      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
		  	        try {
		  	        	//upload icon  directly into amazon
		  	        	FileInputStream stream =(FileInputStream) problemIcon.getInputStream();
		  	        	ObjectMetadata objectMetadata = new ObjectMetadata(); 	            
		  	        	PutObjectRequest putObjectRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, iconFileFileName, stream, objectMetadata);
		  	        	putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		  	        	s3client.putObject(putObjectRequest);
		  	        	
		  	        	
		  	        	// Remove old icon
		  	        	/*Problem oldproblem = problemsDao.getProblemDetailById(problem.get_id());	
			    	    if(!oldproblem.getIcon().equals("") || !oldproblem.getIcon().equals(null))
		  	        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldproblem.getIcon()));*/
				        
		  	        	
		  	        	
		  	        	
		  	        	
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
				
				
				/*upload problem Icon */
				if(isBanner.equals("1")){
					
					String extBenner = FilenameUtils.getExtension(problemBanner.getOriginalFilename());
					
		        	String bennerFileName = "banner-"+new Date().getTime() / 1000+"."+extBenner;  //unique file name  
		        	problem.setBanner(bennerFileName);   
				
		        	
		        	//upload file on amazon
		      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
		      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
		  	        try {
		  	        	 	      
		  	        	//upload banner directly into amazon
		  	        	FileInputStream streamBanner =(FileInputStream) problemBanner.getInputStream();
		  	        	ObjectMetadata objectMetadata = new ObjectMetadata();        
		  	        	PutObjectRequest putObjectBannerRequest = new PutObjectRequest(Properties.AMAZON_SITE_UPLOADS_PATH, bennerFileName, streamBanner, objectMetadata);
		  	        	putObjectBannerRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		  	        	s3client.putObject(putObjectBannerRequest);
		  	         
		  	       // Remove old icon
		  	        	/*Problem oldproblem = problemsDao.getProblemDetailById(problem.get_id());	
		  	        	if(!oldproblem.getBanner().equals("") || !oldproblem.getBanner().equals(null))
		  	        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_BOOKLET_PATH, oldproblem.getBanner()));*/
		  	        	
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
				
				
				
				
				
				
				/*update problem data */
				problem.setIsDeleted(0);
				problemsDao.updateProblem(problem);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Problem has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(problemError);		
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
	 * Get problem by problem id and get default field for problem 
	 */
	@Override
	public AjaxResponse getProblemById(String problemId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			/*Get problem details*/ 
			Problem problemDetail = problemsDao.getProblemDetailById(problemId);
			
			if(problemDetail !=null){
				ProblemBuilder problemBuilder = new ProblemBuilder();
				ProblemModel problemModel =   problemBuilder.fromEntityToModel(problemDetail);
				
				/*Get trend list*/
				List<Trends>  trendsList = trendsDao.getTrends();
				
				/*Get problem list*/
				List<Problem>  problemList = problemsDao.getParentProblems();
				
				/*set ajax response*/
				Gson gson =new Gson();
				
				//set ajax response
				String jsonTrendList = gson.toJson(trendsList);
				String jsonProblemList = gson.toJson(problemList);
				String jsonProblemDetails = gson.toJson(problemModel);
				
				String jsonContent = "{\"problemList\":"+jsonProblemList+", \"trendList\":"+jsonTrendList+", \"problemDetails\":"+jsonProblemDetails+"}";
				
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
	 * Delete problem  by id
	 */
	@Override
	public AjaxResponse deleteProblemById(String problemId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
			
			/*delete question by question id*/
			problemsDao.deleteProblemById(problemId);
			
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Problem deleted successfully.");
		   
			
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
