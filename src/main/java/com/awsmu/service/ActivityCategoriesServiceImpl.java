package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.ActivityCategoriesBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.ActivityCategoriesDao;
import com.awsmu.entity.ActivityCategories;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.ActivityCategoriesModel;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.util.Utils;
import com.awsmu.validator.ActivityCategoriesValidator;
import com.google.gson.Gson;

@Service(value = "activityCategoriesService")
public class ActivityCategoriesServiceImpl implements ActivityCategoriesService {
	
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	/**
	 * Injecting activity dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ActivityCategoriesDao")
	ActivityCategoriesDao activityCategoriesDao; 
	
	/**
	 * get activities based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getActivityCategoriesList(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				ActivityCategoriesBuilder activityBuilder =  new ActivityCategoriesBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = activityCategoriesDao.getActivityCategoryCount(searchList);
					//returns users list after searching and sorting
					List<ActivityCategories> activityCategoryList =  activityCategoriesDao.getActivityCategoriesList(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(activityCategoryList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (ActivityCategories row : activityCategoryList) {														
							rows.add(activityBuilder.fromEntityToModel(row));
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
	 * save activities 
	 */
	@Override
	public AjaxResponse saveActivityCategory(ActivityCategoriesModel activitiesModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			ActivityCategoriesBuilder activityCategoriesBuilder =  new ActivityCategoriesBuilder();			
			
			//Check validation on activity object
			ActivityCategoriesValidator activitiesValidator = new ActivityCategoriesValidator();
				
			Map<String,String> activitiesCategoryError = activitiesValidator.validateActivityCategory(activitiesModel);
			
			/* Validate uniqueness of category name */ 
			if(activityCategoriesDao.checkCategoryNameExists(activitiesModel.get_id(),activitiesModel.getCategory())){
				activitiesCategoryError.put("category", "Category name is already exist");
			}
			
			if(activitiesCategoryError.isEmpty()){
				// Convert model to entity
				ActivityCategories activityCategory = activityCategoriesBuilder.fromModelToEntity(activitiesModel);
				activityCategory.setIsDeleted(0);
				// insert new activity
				activityCategoriesDao.saveActivityCategory(activityCategory);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Activity category has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(activitiesCategoryError);		
				ajaxResponse.setMessage("Error in inserted data");
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
	 * get activity category details by id
	 */
	@Override
	public AjaxResponse getActivityCategoryById(String activityId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		
		try{
			
			/* Get activity details by id*/
			ActivityCategories activityCategory = activityCategoriesDao.getActivityCategoryById(activityId);
			if(activityCategory !=null){
				ActivityCategoriesBuilder activityCategoriesBuilder =  new ActivityCategoriesBuilder();			
				
				/*Convert entity to model*/
				
				ActivityCategoriesModel activityCategoryModel = activityCategoriesBuilder.fromEntityToModel(activityCategory);
				
				
				/* set ajax response*/
				Gson gson =new Gson();
				String jsonActivityCategoryDetails =  gson.toJson(activityCategoryModel);
				String jsonContent = "{\"activityCategoryDetails\":"+jsonActivityCategoryDetails+"}";
	     		
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
	public AjaxResponse updateActivityCategory(ActivityCategoriesModel activitiesModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			ActivityCategoriesBuilder activityCategoriesBuilder =  new ActivityCategoriesBuilder();			
			
			//Check validation on activity object
			ActivityCategoriesValidator activitiesValidator = new ActivityCategoriesValidator();
				
			Map<String,String> activitiesCategoryError = activitiesValidator.validateActivityCategory(activitiesModel);
			
			/* Validate uniqueness of category name */ 
			if(activityCategoriesDao.checkCategoryNameExists(activitiesModel.get_id(),activitiesModel.getCategory())){
				activitiesCategoryError.put("category", "Category name is already exist");
			}
			
			if(activitiesCategoryError.isEmpty()){
				// Convert model to entity
				ActivityCategories activityCategory = activityCategoriesBuilder.fromModelToEntity(activitiesModel);
				// update new activity
				activityCategoriesDao.updateCategories(activityCategory);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Activity category has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(activitiesCategoryError);		
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
	 * Delete activity category  by id
	 */
	@Override
	public AjaxResponse deleteActivityCategoryById(String activityCategoryId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			/*delete activity category by id*/			
			activityCategoriesDao.deleteActivityCategoryById(activityCategoryId);
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Activity category deleted successfully.");
		   
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
