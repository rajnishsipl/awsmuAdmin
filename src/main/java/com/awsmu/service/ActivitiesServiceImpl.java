package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.ActivitiesBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.ActivitiesDao;
import com.awsmu.dao.ActivityCategoriesDao;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.ProblemsDao;
import com.awsmu.entity.Activities;
import com.awsmu.entity.ActivityCategories;
import com.awsmu.entity.Problem;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.ActivitiesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.util.Utils;
import com.awsmu.validator.ActivitiesValidator;
import com.google.gson.Gson;


@Service(value = "ActivitiesService")
public class ActivitiesServiceImpl implements ActivitiesService {
	
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Injecting activity dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ActivitiesDao")
	ActivitiesDao activitiesDao; 
	
	/**
	 * Injecting problem dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ProblemsDao")
	ProblemsDao problemsDao; 
	
	/**
	 * Injecting activity dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ActivityCategoriesDao")
	ActivityCategoriesDao activityCategoriesDao; 
	
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;
	
	
	/**
	 * save activities 
	 */
	@Override
	public AjaxResponse saveActivities(ActivitiesModel activitiesModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			ActivitiesBuilder activityBuilder =  new ActivitiesBuilder();			
			
			//Check validation on activity object
			ActivitiesValidator activitiesValidator = new ActivitiesValidator();
				
			Map<String,String> activitiesError = activitiesValidator.validateActivities(activitiesModel);
				
			if(activitiesError.isEmpty()){
				// Convert model to entity
				Activities activity = activityBuilder.fromModelToEntity(activitiesModel);
				// insert new activity
				activitiesDao.saveActivities(activity);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Activity has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(activitiesError);		
				ajaxResponse.setMessage("errors");
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
	 * get activities based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getActivities(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				ActivitiesBuilder activityBuilder =  new ActivitiesBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = activitiesDao.getActivitiesCount(searchList);
					//returns users list after searching and sorting
					List<Activities> activityList =  activitiesDao.getActivities(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(activityList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Activities userRow : activityList) {														
							rows.add(activityBuilder.fromEntityToModel(userRow));
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
	 * get activity 
	 */
	@Override
	public AjaxResponse getActivitiesFormData(){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			//Get problem list
			List<Problem>  problemList = problemsDao.getProblems();
			List<ActivityCategories>  activityCategoryList = activityCategoriesDao.getActivityCategories();
			
			//set ajax response
			String jsonProblemList = new Gson().toJson(problemList);
			String jsonActivityCategoryList = new Gson().toJson(activityCategoryList);
			String jsonNationalityList = new Gson().toJson(attributesDao.getNatioanlityList());
			
			// Get activity details
			
			
			String jsonContent = "{\"problemList\":"+jsonProblemList+", \"activityCategoriesList\":"+jsonActivityCategoryList+", \"nationalityList\":"+jsonNationalityList+"}";
     		
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
	 * get activity details by id
	 */
	@Override
	public AjaxResponse getActivityById(String activityId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		
		
		try{
			
			
			/* Get activity details by id*/
			Activities activity = activitiesDao.getActivityById(activityId);
			if(activity !=null){
				ActivitiesBuilder activityBuilder =  new ActivitiesBuilder();			
				
				/*Convert entity to model*/
				
				ActivitiesModel activityModel = activityBuilder.fromEntityToModel(activity);
				
				/*Get problem list and activity category*/
				List<Problem>  problemList = problemsDao.getProblems();
				List<ActivityCategories>  activityCategoryList = activityCategoriesDao.getActivityCategories();
				
				/* set ajax response*/
				Gson gson =new Gson();
				
				String jsonProblemList = gson.toJson(problemList);
				String jsonActivityCategoryList = gson.toJson(activityCategoryList);
				String jsonNationalityList = gson.toJson(attributesDao.getNatioanlityList());
				String jsonActivityDetails =  gson.toJson(activityModel);
				
				
				String jsonContent = "{\"problemList\":"+jsonProblemList+", \"activityCategoriesList\":"+jsonActivityCategoryList+", \"nationalityList\":"+jsonNationalityList+", \"activityDetails\":"+jsonActivityDetails+"}";
	     		
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
	 * update activities 
	 */
	@Override
	public AjaxResponse updateActivities(ActivitiesModel activitiesModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			ActivitiesBuilder activityBuilder =  new ActivitiesBuilder();			
			
			//Check validation on activity object
			ActivitiesValidator activitiesValidator = new ActivitiesValidator();
				
			Map<String,String> activitiesError = activitiesValidator.validateActivities(activitiesModel);
				
			if(activitiesError.isEmpty()){
				// Convert model to entity
				Activities activity = activityBuilder.fromModelToEntity(activitiesModel);
				// insert new activity
				activitiesDao.updateActivities(activity);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Activity has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(activitiesError);		
				ajaxResponse.setMessage("errors");
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
	 * Delete activity  by id
	 */
	@Override
	public AjaxResponse deleteActivityById(String activityId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
			
			/*delete activity by question id*/
			activitiesDao.deleteActivityById(activityId);
			
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Activity deleted successfully.");
		   
			
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