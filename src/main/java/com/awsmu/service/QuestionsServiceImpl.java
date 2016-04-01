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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.awsmu.builder.ActivitiesBuilder;
import com.awsmu.builder.QuestionsBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.ActivitiesDao;
import com.awsmu.dao.ActivityCategoriesDao;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.ProblemsDao;
import com.awsmu.dao.QuestionsDao;
import com.awsmu.entity.Activities;
import com.awsmu.entity.ActivityCategories;
import com.awsmu.entity.Problem;
import com.awsmu.entity.QuestionOptions;
import com.awsmu.entity.Questions;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.ActivitiesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.QuestionsModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.ActivitiesValidator;
import com.awsmu.validator.QuestionsValidator;
import com.google.gson.Gson;


@Service(value = "QuestionsService")
public class QuestionsServiceImpl implements QuestionsService {
	
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Injecting question dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "QuestionsDao")
	QuestionsDao questionsDao; 
	
	/**
	 * Injecting problem dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ProblemsDao")
	ProblemsDao problemsDao; 
	
	
	
	
	
	/**
	 * get activities based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getQuestionsGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				QuestionsBuilder questionsBuilder =  new QuestionsBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = questionsDao.getQuestionsCount(searchList);
					//returns users list after searching and sorting
					List<Questions> questionsList =  questionsDao.getQuestions(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(questionsList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Questions row : questionsList) {														
							rows.add(questionsBuilder.fromEntityToModel(row));
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
	 * get question details by id
	 */
	@Override
	public AjaxResponse getQuestionById(String questionId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			/* Get question details by id*/
			Questions question = questionsDao.getQuestionById(questionId);
			if(question !=null){
				QuestionsBuilder questionsBuilder =  new QuestionsBuilder();			
				
				/*Convert entity to model*/
				
				QuestionsModel questionsModel = questionsBuilder.fromEntityToModel(question);
				
				/*Get problem list and activity category*/
				List<Problem>  problemList = problemsDao.getAllProblems();
				
				
				/* set ajax response*/
				Gson gson =new Gson();
				
				String jsonProblemList = gson.toJson(problemList);
				String jsonQuestionDetails =  gson.toJson(questionsModel);
				
				
				String jsonContent = "{\"problemList\":"+jsonProblemList+", \"questionDetails\":"+jsonQuestionDetails+"}";
	     		
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
	 * get question form data
	 * 
	 * 	 */
	@Override
	public AjaxResponse getFormData(){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
		
				/*Get problem list and activity category*/
				List<Problem>  problemList = problemsDao.getAllProblems();
				/* set ajax response*/
				Gson gson =new Gson();
				
				String jsonProblemList = gson.toJson(problemList);
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
			ajaxResponse.setMessage(Properties.EXCEPTION_IN_LOGIC+"-"+e.getMessage());
			ajaxResponse.setCode(Properties.INTERNAL_SERVER_ERROR);	
		}
		
		return ajaxResponse;
		
	}
	
	/**
	 * add question 
	 */
	@Override
	public AjaxResponse addQuestion(QuestionsModel questionModel, String imageUpload,MultipartFile[] optionsIcons){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			QuestionsValidator questionsValidator = new QuestionsValidator();
			// Validate question data
			Map<String,String> questionError = questionsValidator.validateQuestion(questionModel);
			
			if(questionError.isEmpty()){
				QuestionsBuilder questionsBuilder = new QuestionsBuilder(); 
				
				/*Convert question model to entity*/
				Questions question = questionsBuilder.fromModelToEntity(questionModel);
				question.setIsDeleted(0);
				List<QuestionOptions> questionOptions = new ArrayList<QuestionOptions>();
				/*upload question Icon */
				
				if(imageUpload.equals("1")){
					int index = 0;
					for(MultipartFile optionsIcon : optionsIcons){
						QuestionOptions questionOption = new QuestionOptions();
						String extIcon = FilenameUtils.getExtension(optionsIcon.getOriginalFilename());
						String iconFileFileName = "icon-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
						questionOption.setImage(iconFileFileName);
						
						questionOption.setText(question.getOptions().get(index).getText());
					
			        	//upload file on amazon
			      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
			      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
			  	        try {
			  	        	//upload icon  directly into amazon
			  	        	FileInputStream stream =(FileInputStream) optionsIcon.getInputStream();
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
			  	      questionOptions.add(questionOption);
			  	     index++ ;
					 }
				}
				question.setOptions(questionOptions);   
				
				/*Add question to db*/
				questionsDao.addQuestion(question);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Question has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(questionError);		
				ajaxResponse.setMessage("There is some problem with inserted data.");
			}
		
		}catch(AwsmuException e){
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
	 * update question 
	 */
	@Override
	public AjaxResponse updateQuestion(QuestionsModel questionModel,String imageUpload,MultipartFile[] optionsIcons){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			QuestionsValidator questionsValidator = new QuestionsValidator();
			// Validate question data
			Map<String,String> questionError = questionsValidator.validateQuestion(questionModel);
			
			if(questionError.isEmpty()){
				QuestionsBuilder questionsBuilder = new QuestionsBuilder(); 
				
				/*Convert question model to entity*/
				Questions question = questionsBuilder.fromModelToEntity(questionModel);
				question.setIsDeleted(0);
				List<QuestionOptions> questionOptions = new ArrayList<QuestionOptions>();
				/*upload question Icon */
				
				if(imageUpload.equals("1")){
					int imageCount = 0;
					
					for(QuestionOptions questionOption : question.getOptions()){
					
						if(questionOption.getIsUpdate() == 1){
							String extIcon = FilenameUtils.getExtension(optionsIcons[imageCount].getOriginalFilename());
							String iconFileFileName = "icon-"+new Date().getTime() / 1000+"."+extIcon;  //unique file name  
							questionOption.setImage(iconFileFileName);
							questionOption.setIsUpdate(0);
							
				        	//upload file on amazon
				      	  	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
				      	  	AmazonS3 s3client = new AmazonS3Client(awsCreds);
				  	        try {
				  	        	//upload icon  directly into amazon
				  	        	FileInputStream stream =(FileInputStream) optionsIcons[imageCount].getInputStream();
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
				  	      
				  	    imageCount++ ;
						}
						questionOptions.add(questionOption);
					}
					question.setOptions(questionOptions);   
				}
				/*update  question to db*/
				System.out.println(question);
				questionsDao.updateQuestion(question);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Question has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(questionError);		
				ajaxResponse.setMessage("There is some problem with inserted data.");
			}
		
		}catch(AwsmuException e){
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
	 * Delete question  by id
	 */
	@Override
	public AjaxResponse deleteQuestionById(String questionId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
			
			/*delete question by question id*/
			questionsDao.deleteQuestionById(questionId);
			
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Doctor deleted successfully.");
		   
			
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