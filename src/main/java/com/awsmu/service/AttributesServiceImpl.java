package com.awsmu.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import com.awsmu.builder.AttributesBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.util.Utils;
import com.awsmu.entity.Attributes;
import com.awsmu.entity.City;
import com.awsmu.entity.DegreeCourses;
import com.awsmu.entity.Nationality;
import com.awsmu.entity.Profession;
import com.awsmu.entity.Specialties;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;

/**
 * Attribute service implementation
 */
@Service(value = "AttributesService")
public class AttributesServiceImpl implements AttributesService {
	
	private static Logger logger = Logger.getLogger(AttributesServiceImpl.class);
		
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;
	
	
	/**
	 * get cities list
	 */
	@Override
	public  AjaxResponse getCitiesList(String country){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
				
		try{
			 // Check admin authentication 
			City city = attributesDao.getCitiesList(country);
			
			if(city == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);					
			}else{
				
				String jsonCitiesList = new Gson().toJson(city);
				
				String jsonContent = "{\"citiesList\":"+jsonCitiesList+"}";
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
	 * get attributes based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getAttributesGrid(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				AttributesBuilder attributeBuilder =  new AttributesBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = attributesDao.getAttributesCount(searchList);
					//returns users list after searching and sorting
					List<Attributes> attributesList =  attributesDao.getAttributesGrid(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(attributesList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Attributes row : attributesList) {														
							rows.add(attributeBuilder.fromEntityToModel(row));
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
	 * get country list of Attributes 
	 */
	@Override
	public  AjaxResponse getCountryList(){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			 // Check admin authentication 
			Nationality nationality = attributesDao.getNatioanlityList();
			
			if(nationality == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);					
			}else{
				
				String jsonNationalityList = new Gson().toJson(nationality);
				
				String jsonContent = "{\"countryList\":"+jsonNationalityList+"}";
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
	 * update or add country attributes 
	 */
	@Override
	public AjaxResponse updateCountry(Nationality nationality){
		
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			attributesDao.updateCountry(nationality);
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Country has been updated successfully.");
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
	 * get Courses list of Attributes 
	 */
	
	@Override
	public  AjaxResponse getDegreeCoursesList(){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			 // get list of degree courses 
			DegreeCourses degreeCourses = attributesDao.getDegreeCoursesList();
			
			if(degreeCourses == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);					
			}else{
				
				String jsondegreeCoursesList = new Gson().toJson(degreeCourses);
				
				String jsonContent = "{\"degreeCoursesList\":"+jsondegreeCoursesList+"}";
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
	 * update or add degree courses attributes 
	 */
	@Override
	public AjaxResponse updateDegreeCourses(DegreeCourses degreeCourses){
		
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			attributesDao.updateDegreeCourses(degreeCourses);
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Degree Courses has been updated successfully.");
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
	 * get profession list of Attributes 
	 */
	
	@Override
	public  AjaxResponse getProfessionsList(){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			 // get profession list
			Profession professions = attributesDao.getProfessionList();
			
			if(professions == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);					
			}else{
				
				String jsonProfessionsList = new Gson().toJson(professions);
				
				String jsonContent = "{\"professionList\":"+jsonProfessionsList+"}";
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
	 * update or add profession attributes 
	 */
	@Override
	public AjaxResponse updateProfessions(Profession profession){
		
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			attributesDao.updateProfessions(profession);
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("profession has been updated successfully.");
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
	 * get specialities list of Attributes 
	 */
	
	@Override
	public  AjaxResponse getSpecialitiesList(){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			 // get profession list
			Specialties specialties = attributesDao.getSpecialtiesList();
			
			if(specialties == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);					
			}else{
				
				String jsonSpecialtiesList = new Gson().toJson(specialties);
				
				String jsonContent = "{\"specialtyList\":"+jsonSpecialtiesList+"}";
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
	 * update or add Specialities attributes 
	 */
	@Override
	public AjaxResponse updateSpecialities(Specialties specialties){
		
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			attributesDao.updateSpecialties(specialties);
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Specialty has been updated successfully.");
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
	
	
}
