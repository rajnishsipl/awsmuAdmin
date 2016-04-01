package com.awsmu.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.UserModel;

/**
 * User service interface
 */
public interface UserService {

	
	/**
	 * check admin login from database
	 */
	public AjaxResponse checkAdminLogin(String email, String password); 
	
	/**
	 * get user based on the pagination criteria
	 */
	public  GridResponse getUsers(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters);
	
	/**
	 * get user detail by id
	 */
	public AjaxResponse getUserDetail(String userId); 
	
	/**
	 * edit user by id
	 */
	public AjaxResponse editUserById(UserModel userModel); 
	
	/**
	 * upload profile pic
	 */
	public AjaxResponse uploadProfilePic(MultipartFile file, String userId); 
	
	/**
	 * remove profile pic
	 */
	public AjaxResponse removeProfilePic(String userId); 
	
	/**
	 * save user attributes
	 */
	public AjaxResponse saveUserAttributes(String id, String optionName); 
	
	/**
	 * change user password
	 */
	public AjaxResponse changeUserPassword(String userId, String password); 
	
	/**
	 * delete user
	 */
	public AjaxResponse deleteUser(String userId); 
	
	/**
	 * thread to update other user values 
	 */
	public void initiateUserThread(String userId);
	
}
