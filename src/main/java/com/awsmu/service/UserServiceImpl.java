package com.awsmu.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.awsmu.builder.UserBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.QuestionsDao;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.UserDao;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.entity.UserValues;
import com.awsmu.entity.Questions;
import com.awsmu.model.UserModel;
import com.awsmu.util.Utils;
import com.awsmu.entity.User;
import com.awsmu.entity.UserAttributes;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;
/**
 * User service implementation
 */
@Service(value = "UserService")
public class UserServiceImpl implements UserService {
	
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Injecting userDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "UserDao")
	private UserDao userDao;
	
	/**
	 * Injecting QuestionsDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "QuestionsDao")
	private QuestionsDao questionsDao;
	
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;
	
	
	/**
	 * check Admin Login
	 */
	@Override
	public  AjaxResponse checkAdminLogin(String email, String password){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		UserBuilder userBuilder =  new UserBuilder();
		
		
		// Convert password in md5
		String md5Password = Utils.getMD5(password);
		try{
			 // Check admin authentication 
			User user = userDao.checkAdminLogin(email, md5Password);
			
			if(user == null){
				ajaxResponse.setMessage(Properties.INVALID_LOGIN);								
			}else{
				
				UserModel userModel = userBuilder.fromEntityToModel(user);
				ajaxResponse.setStatus(true);	
				ajaxResponse.setContent(new Gson().toJson(userModel));					
			}
		}
		catch(AwsmuException e){
			e.printStackTrace();
			//System.out.println(e.printStackTrace());
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
	 * get user based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getUsers(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				UserBuilder userBuilder =  new UserBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = userDao.getUserCount(searchList);
					//returns users list after searching and sorting
					List<User> userList =  userDao.getUsers(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(userList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (User userRow : userList) {														
							rows.add(userBuilder.fromEntityToModel(userRow));
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
	 * get user detail by id
	 */
	@Override
	public  AjaxResponse getUserDetail(String userId){

		AjaxResponse ajaxResponse = new AjaxResponse();
		UserBuilder userBuilder =  new UserBuilder();
		try{
			// get user detail 
			User user =  userDao.getUserById(userId);
			
			//get user attributes list
			List<UserAttributes> userAttributesList = userDao.getUserAttrByUserId(userId);
			
			//get attributes questions list
			List<Questions> attrQuestionsList =  new ArrayList<Questions>();
			       		
			for(UserAttributes attributes : userAttributesList){
				attrQuestionsList.add(questionsDao.getQuestionByTag(attributes.getQuestionTag()));	
			}
						
			//check if found any records
			if(user == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model				
				UserModel userDetail = userBuilder.fromEntityToModel(user);
				
				//set ajax response
				String jsonUserDetail = new Gson().toJson(userDetail);
				String jsonUserAttributesList = new Gson().toJson(userAttributesList);
				String jsonAttrQuestionsList = new Gson().toJson(attrQuestionsList);
				String jsonNationalityList = new Gson().toJson(attributesDao.getNatioanlityList());
				String jsonProfessionList = new Gson().toJson(attributesDao.getProfessionList());
				String jsonDegreeCoursesList = new Gson().toJson(attributesDao.getDegreeCoursesList());
				String jsonSpecialtyList = new Gson().toJson(attributesDao.getSpecialtiesList());
				
				String jsonContent = "{\"userDetail\":"+jsonUserDetail+", \"userAttributesList\":"+jsonUserAttributesList+", \"attrQuestionsList\":"+jsonAttrQuestionsList+", \"nationalityList\":"+jsonNationalityList+", \"professionList\":"+jsonProfessionList+", \"degreeCoursesList\":"+jsonDegreeCoursesList+", \"specialtyList\":"+jsonSpecialtyList+"}";
         		
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
	 * edit user
	 */
	@Override
	public  AjaxResponse editUserById(UserModel userModel){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
				
			//check if username already exists
			User checkUsername = userDao.checkUsernameExists(userModel.getEmail(), userModel.get_id());
			
			User checkUsernameEqualToId = userDao.checkUsernameEqualToId(userModel.getEmail(), userModel.get_id());
			
			User checkEmailExists = userDao.checkEmailExists(userModel.getEmail(), userModel.get_id());
			
			if(checkUsername != null && checkUsernameEqualToId != null){
		   		
				ajaxResponse.setMessage("Username already exists.");
				return ajaxResponse;
			}
			
			if( ! userModel.getEmail().isEmpty() && checkEmailExists != null){
				ajaxResponse.setMessage("Email already exists.");
				return ajaxResponse;
			}
		
			//convert from entity to model		
			UserBuilder userBuilder =  new UserBuilder();
			User user = userBuilder.fromModelToEntity(userModel);
			
			//save user
			userDao.saveUser(user);
			
			// initiate user thread to update all collection with user values
	   		initiateUserThread(userModel.get_id());
	   		
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Profile updated successfully");
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
	 * upload profile pic
	 */
	@Override
	public  AjaxResponse uploadProfilePic(MultipartFile file, String userId){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
		
			InputStream inputStream = null;  
	    	OutputStream outputStream = null;  
	    	      	  
	    	Date now = new Date();
	    	
	    	String fileName = userId+(now.getTime() / 1000);  //unique file name    
	    	
	    	try {  
	    		
	    		 // get user detail to delete previous image file on amazon
  				User userOld =  userDao.getUserById(userId);
  				
	    		inputStream = file.getInputStream();  
		  
	    		File newFile = new File(Properties.PIC_UPLOAD_PATH + fileName);  
	    		
	    		if ( ! newFile.exists()) {  
	    			newFile.createNewFile();  
	    		}  
	    		outputStream = new FileOutputStream(newFile);  
	    		int read = 0;  
	    		byte[] bytes = new byte[1024];  
		  
	    		while ((read = inputStream.read(bytes)) != -1) {  
	    			outputStream.write(bytes, 0, read);  
	    		}
	    		outputStream.close();
	    		     	  	
	      	  	//upload file on amazon
	        	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
	        	//AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	        	AmazonS3 s3client = new AmazonS3Client(awsCreds);
	  	        try {
	  	        	//copy image after uploading on current instance
	  	            File amazonFile = new File(Properties.PIC_UPLOAD_PATH + fileName);
	  	            PutObjectRequest por = new PutObjectRequest(Properties.AMAZON_PROFILE_PIC_PATH, fileName, amazonFile);
	  	            por.setCannedAcl(CannedAccessControlList.PublicRead);
	  	            s3client.putObject(por); 
	  	           
	  	            //update image
	  		    	userDao.updateProfilePic(fileName, userId);
	  		    	
	  		    	// initiate user thread to update all collection with user values
	  		   		initiateUserThread(userId);
	  		    	
	  		    	String userImageUrl =  Properties.AMAZON_PROFILE_PIC_URL+ fileName;
	  		    	String jsonContent = "{\"userImage\":\""+fileName+"\", \"userImageUrl\":\""+userImageUrl+"\"}";
	  		    		
					ajaxResponse.setStatus(true);
					ajaxResponse.setContent(jsonContent);
					ajaxResponse.setMessage("Profile pic uploaded successfully.");
															  	  	         
	  	         } catch (AmazonServiceException ase) {
	  	      
	  	        	ajaxResponse.setMessage(ase.getMessage());
	  	        	return ajaxResponse;
	  	        	
	  	        } catch (AmazonClientException ace) {
	  	        	
	  	        	ajaxResponse.setMessage(ace.getMessage());
	             	return ajaxResponse;             	
	  	        }
	  	        
	  	        //delete old file
	  	        try {
	  	        	s3client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_PROFILE_PIC_PATH, userOld.getImage()));
	  	        } catch (AmazonServiceException ase) {
		           
	  	        	ajaxResponse.setMessage(ase.getMessage());
		  	        return ajaxResponse;
		  	        
	  	        } catch (AmazonClientException ace) {
		        
	  	        	ajaxResponse.setMessage(ace.getMessage());
		            return ajaxResponse;  
		            
	  	        }
	    	} catch (IOException e) {  
	    		// TODO Auto-generated catch block  
	    		ajaxResponse.setMessage(e.toString());
	           	return ajaxResponse;
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
	 * remove profile pic
	 */
	@Override
	public  AjaxResponse removeProfilePic(String userId){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			
			// get user detail 
			User user =  userDao.getUserById(userId);
				    	
	    	//delete image from Amazon s3    	
	    	BasicAWSCredentials awsCreds = new BasicAWSCredentials(Properties.AMAZON_ACCESS_KEY, Properties.AMAZON_SECRET_KEY);
	   	  	//AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	   	  	AmazonS3 s3Client = new AmazonS3Client(awsCreds);
	    	
	        try {
	            s3Client.deleteObject(new DeleteObjectRequest(Properties.AMAZON_PROFILE_PIC_PATH, user.getImage()));
	        } catch (AmazonServiceException ase) {
	        
	        	ajaxResponse.setMessage(ase.getMessage());
		        return ajaxResponse;
		        
	        } catch (AmazonClientException ace) {
	         
	        	ajaxResponse.setMessage(ace.getMessage());
	         	return ajaxResponse;  
	        }
	        
	        //update image
		    userDao.updateProfilePic("", userId);
		    
		    String userImageUrl =  Properties.AMAZON_PROFILE_PIC_URL+ "profile_no_pic.png";
		    String jsonContent = "{\"userImage\":\"profile_no_pic.png\", \"userImageUrl\":\""+userImageUrl+"\"}";
		    	
		    // initiate user thread to update all collection with user values
		   	initiateUserThread(userId);
		     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setContent(jsonContent);
		    ajaxResponse.setMessage("Profile pic removed successfully.");
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
	 * save user attributes
	 */
	@Override
	public  AjaxResponse saveUserAttributes(String id, String optionName){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			
			// save attributes 
			userDao.saveAttributes(id, optionName);
				    			     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Attributes updated successfully.");
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
	 * change user password
	 */
	@Override
	public  AjaxResponse changeUserPassword(String userId, String password){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			
			// change password
			userDao.changeUserPassword(userId, Utils.getMD5(password));
						    			     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Password updated successfully.");
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
	 * delete user
	 */
	@Override
	public  AjaxResponse deleteUser(String userId){

		AjaxResponse ajaxResponse = new AjaxResponse();
	
		try{
			
			// delete user
			userDao.deleteUser(userId);
						    			     		        
		    ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("User deleted successfully.");
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
	
	/*
	*Method name: initiateUserThread
    *Created Date: 26-June-2015
    *Purpose: Function to initiate User Thread to update user sub document    
	*/ 		
	public void initiateUserThread (String UserId){
		final String id = UserId;
		User userValue = userDao.getUserValues(id);
   		final UserValues userValues = new UserValues();
    	userValues.setDisplayName(userValue.getDisplayName());
    	userValues.setImage(userValue.getImage());
    	userValues.setUsername(userValue.getUsername());
    	userValues.setUserId(id);
    	userValues.setUserRole(userValue.getUserRole());
    	userValues.setUserInfo(userDao.getUserValuesInfoText(userValue.get_id()));
    	try{
    		ExecutorService executor = Executors.newFixedThreadPool(10);
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updateFriendUserValues(id, userValues);
    		    }
    		});
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updatePostUserValues(id, userValues);
    		    }
    		});
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updatePostCommentUserValues(id, userValues);
    		    }
    		});
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updatePostLikeUserValues(id, userValues);
    		    }
    		});
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updateMessageUserValues(id, userValues);
    		    }
    		});
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updatePlannerGroupUserValues(id, userValues);
    		    }
    		});
    		executor.execute(new Runnable() {
    		    public void run() {
    		    	userDao.updateSocialActionsTrailUserValues(id, userValues);
    		    }
    		});
    		executor.shutdown();
    	}
    	catch (Exception e) {
    		
		}
   		
	}
}
