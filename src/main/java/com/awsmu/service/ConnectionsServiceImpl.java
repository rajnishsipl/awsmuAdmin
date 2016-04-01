package com.awsmu.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.awsmu.builder.FriendsBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.ConnectionsDao;
import com.awsmu.entity.Friends;
import com.awsmu.exception.AwsmuException;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.FriendsModel;
import com.awsmu.model.GridResponse;
import com.awsmu.util.Utils;
import com.google.gson.Gson;



@Service(value = "ConnectionsService")
public class ConnectionsServiceImpl implements ConnectionsService {
	
	/*create instance of loger*/
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Injecting activity dao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ConnectionsDao")
	ConnectionsDao connectionDao; 
	
	
	/**
	 * get connections  based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getConnections(Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				FriendsBuilder connectionBuilder =  new FriendsBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = connectionDao.getConnectionsCount(searchList);
					//returns users list after searching and sorting
					List<Friends> connectionsList =  connectionDao.getConnections(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(connectionsList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Friends userRow : connectionsList) {														
							rows.add(connectionBuilder.fromEntityToModel(userRow));
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
	 * get user connections  based on the pagination criteria, also perform searching sorting
	 * This function specially works to return grid response for jqgrid 
	 */
	@Override
	public  GridResponse getUserConnections(String userId,Integer skipRecord, Integer skipRecordFreq,Integer page,String sortBy,String sortOrder,Map<Object, Object> filters){
				// creating object of grid response class
				GridResponse gridResponse = new GridResponse();
				FriendsBuilder connectionBuilder =  new FriendsBuilder();			
				try{
					// function return the array list of collection columns and its value after filtering the search parameters					
					List<Object> searchList = Utils.getSearchParameter(filters);
					// returns total user count after applying search filters.
					int totalRecords = connectionDao.getConnectionsCount(searchList);
					//returns users list after searching and sorting
					List<Friends> connectionsList =  connectionDao.getConnections(skipRecord, skipRecord, page, sortBy, sortOrder,searchList);					
					// setting total records of grid response 
					gridResponse.setRecords(totalRecords);
					
					if(totalRecords!=0) {
						int total = (int)Math.ceil((float)totalRecords/(float)skipRecordFreq);
						gridResponse.setTotal(total);
					}	
					// set current page status of pagination for grid
					gridResponse.setPage(page);
					
					List<Object> rows  = new ArrayList<Object>();
					if(connectionsList.isEmpty()){
						
						// set total number of rows as null
						gridResponse.setMessage(Properties.NO_RECORD_FOUND);

						gridResponse.setRows(rows);
					}else{						
						// iterating user to convert User Entity to UserModel and add into list for grid response
						for (Friends userRow : connectionsList) {														
							rows.add(connectionBuilder.fromEntityToModel(userRow));
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
	 * Get connection by  id 	 */
	@Override
	public AjaxResponse getConnectionDetailById(String connectionId){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			/*Get problem details*/ 
			Friends connectionDetail = connectionDao.getConnectionDetailById(connectionId);
			
			if(connectionDetail !=null){
				FriendsBuilder friendsBuilder = new FriendsBuilder();
				FriendsModel friendsModel =   friendsBuilder.fromEntityToModel(connectionDetail );
				
				
				Gson gson =new Gson();
				String jsonFriendsDetails = gson.toJson(friendsModel);
				
				String jsonContent = "{\"connectionDetails\":"+jsonFriendsDetails+"}";
				
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
	 * update connection 
	 */
	@Override
	public AjaxResponse updateConnection(FriendsModel friendsModel){
		AjaxResponse ajaxResponse =  new AjaxResponse();
		try{
			FriendsBuilder friendsBuilder =  new FriendsBuilder();			
			
				
			/// Convert model to entity
			Friends connection = friendsBuilder.fromModelToEntity(friendsModel);
			// insert new activity
			if(connection.getStatus().equals(Properties.FRIEND_ACCEPT)){
				connection.setAcceptDate(new Date());
			}else if(connection.getStatus().equals(Properties.FRIEND_DECLINE)){
				connection.setDeclineDate(new Date());
			}
			
			connectionDao.updateConnection(connection);
			ajaxResponse.setStatus(true);
			ajaxResponse.setMessage("Connection has been updated successfully.");
			
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