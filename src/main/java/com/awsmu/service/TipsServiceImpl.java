package com.awsmu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.TipsBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.ProblemsDao;
import com.awsmu.dao.TipsDao;
import com.awsmu.entity.Tips;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.TipsModel;
import com.awsmu.util.Utils;
import com.awsmu.validator.TipsValidator;
import com.google.gson.Gson;


@Service(value = "TipsService")
public class TipsServiceImpl implements TipsService {
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Injecting tips dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "TipsDao")
	TipsDao tipsDao; 
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;
	
	
	
	/**
	 * Injecting problem dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ProblemsDao")
	ProblemsDao problemsDao; 
	
	
	/**
	 * get tips based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getTipsGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				TipsBuilder tipsBuilder =  new TipsBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = tipsDao.getTipsCount(searchList);
					//returns users list after searching and sorting
					List<Tips> tipsList =  tipsDao.getTipList(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(tipsList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Tips row : tipsList) {														
							rows.add(tipsBuilder.fromEntityToModel(row));
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
	 * save tip 
	 */
	@Override
	public AjaxResponse saveTip(TipsModel tipsModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			TipsValidator tipsValidator = new TipsValidator();
			
			// Validate tips data
			Map<String,String> tipsError = tipsValidator.validateTips(tipsModel);
			
			if(tipsError.isEmpty()){
				
				TipsBuilder tipsBuilder = new TipsBuilder();
				/*Convert tip model to entity*/
				Tips tips = tipsBuilder.fromModelToEntity(tipsModel);
				
				/* Insert new tip*/ 
				tips.setIsDeleted(0);
				tipsDao.saveTip(tips);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Tip has been added successfully.");
			}else{
				ajaxResponse.setErrorMap(tipsError);		
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
	 * update tip 
	 */
	@Override
	public AjaxResponse updateTip(TipsModel tipsModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			TipsValidator tipsValidator = new TipsValidator();
			
			// Validate tip data
			Map<String,String> tipsError = tipsValidator.validateTips(tipsModel);
			
			if(tipsError.isEmpty()){
				
				TipsBuilder tipsBuilder = new TipsBuilder();
				/*Convert tip model to entity*/
				Tips tips = tipsBuilder.fromModelToEntity(tipsModel);
				
				/* Update new tip*/ 
				tipsDao.updateTip(tips);
				ajaxResponse.setStatus(true);
				ajaxResponse.setMessage("Tip has been updated successfully.");
			}else{
				ajaxResponse.setErrorMap(tipsError);		
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
	 * Get tip by tip id  
	 */
	@Override
	public AjaxResponse getTipsById(String tipId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
				/*Get problem details*/ 
				Tips tipDetail = tipsDao.getTipsDetailsById(tipId);
				
				if(tipDetail !=null){
					TipsBuilder tipsBuilder = new TipsBuilder();
					TipsModel tipsModel =   tipsBuilder.fromEntityToModel(tipDetail);
					
					//set ajax response
					Gson gson = new Gson();
					String jsonNationalityList = gson.toJson(attributesDao.getNatioanlityList());
					String jsonProfessionList = gson.toJson(attributesDao.getProfessionList());
					String jsonProblemList = gson.toJson(problemsDao.getProblems());
					String jsonTipDetails = new Gson().toJson(tipsModel);				
					
				    String jsonContent = "{\"nationalityList\":"+jsonNationalityList+", \"professionList\":"+jsonProfessionList+", \"problemList\":"+jsonProblemList+", \"tipDetails\":"+jsonTipDetails+"}";
					
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
	 * Get tip form data
	 */
	@Override
	public AjaxResponse getTipsFormData(){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
				//set ajax response
				Gson gson = new Gson();
				String jsonNationalityList = gson.toJson(attributesDao.getNatioanlityList());
				String jsonProfessionList = gson.toJson(attributesDao.getProfessionList());
				String jsonProblemList = gson.toJson(problemsDao.getProblems());
							
				
			    String jsonContent = "{\"nationalityList\":"+jsonNationalityList+", \"professionList\":"+jsonProfessionList+", \"problemList\":"+jsonProblemList+"}";
				
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
	 * Delete tips  by id
	 */
	@Override
	public AjaxResponse deleteTipById(String tipId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			
			
			/*delete tip by tip id*/
			tipsDao.deleteTipById(tipId);
			
			ajaxResponse.setStatus(true);
		    ajaxResponse.setMessage("Tip deleted successfully.");
		   
			
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
