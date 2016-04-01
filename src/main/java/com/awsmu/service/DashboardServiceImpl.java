package com.awsmu.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import com.awsmu.builder.ExpertRequestBuilder;
import com.awsmu.builder.PlannerBuilder;
import com.awsmu.builder.UserBuilder;
import com.awsmu.builder.UserPlannerBuilder;
import com.awsmu.config.Properties;

import com.awsmu.dao.DashboardDao;

import com.awsmu.model.AjaxResponse;
import com.awsmu.model.ExpertRequestModel;
import com.awsmu.model.PlannersModel;
import com.awsmu.model.UserMessageAggregation;
import com.awsmu.model.UserModel;
import com.awsmu.model.UserPlannersModel;
import com.awsmu.entity.ExpertRequest;
import com.awsmu.entity.Planners;
import com.awsmu.entity.User;
import com.awsmu.entity.UserPlanners;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;

/**
 * Dashboard service implementation
 */
@Service(value = "DashboardService")
public class DashboardServiceImpl implements DashboardService {

	private static Logger logger = Logger.getLogger(DashboardServiceImpl.class);

	/**
	 * Injecting DashboardDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "DashboardDao")
	private DashboardDao dashboardDao;
	
	
	/**
	 * get dashboard detail
	 */
	@Override
	public  AjaxResponse getDashboardData(){

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			
			 //date time format
	       	PrettyTime preetyObj = new PrettyTime();
	       	
			// get total user count 
			int totalUsersCount =  dashboardDao.getAllUsersCount();
			
			// get latest user
			List<User> usersList =  dashboardDao.getLatestUsers();
			UserBuilder userBuilder =  new UserBuilder();	
			List<UserModel> userModelList =  new ArrayList<UserModel>();
			if( ! usersList.isEmpty()){
				// iterating user to convert User Entity to UserModel and add into list 
				for (User userRow : usersList) {														
					userModelList.add(userBuilder.fromEntityToModel(userRow));
				}
			}
			
			
			//get user messages
			List<UserMessageAggregation> unreadMessages = dashboardDao.getUnreadMessages();
			List<UserMessageAggregation> messages = dashboardDao.getRecentMessages();
			//create message list
			List<Object> messagesList = new ArrayList<Object>();
			if (! messages.isEmpty()) {
				for (UserMessageAggregation messagesRow : messages) {
					Map<String, String> messageRow = new HashMap<String, String>();

					if (messagesRow.getToUserId().equalsIgnoreCase(Properties.ADMIN_ID)) {

						String profilePic = messagesRow.getFromUserImage() == "" ? "profile_no_pic.png": messagesRow.getFromUserImage();

						int intIndex = profilePic.indexOf("http");
						if (intIndex == -1) {
							profilePic = Properties.AMAZON_PROFILE_PIC_URL+ profilePic;
						}

						messageRow.put("image", profilePic);
						messageRow.put("displayName", messagesRow.getFromUserDisplayName());
						messageRow.put("isRead", String.valueOf(messagesRow.getIsRead()));
						messageRow.put("userName", messagesRow.getFromUserUserName());
						messageRow.put("userRole", messagesRow.getFromUserRole());

					} else {
						String profilePic = messagesRow.getToUserImage() == "" ? "profile_no_pic.png"
								: messagesRow.getToUserImage();

						int intIndex = profilePic.indexOf("http");
						if (intIndex == -1) {
							profilePic = Properties.AMAZON_PROFILE_PIC_URL+ profilePic;
						}
						messageRow.put("image", profilePic);
						messageRow.put("displayName", messagesRow.getToUserDisplayName());
						messageRow.put("isRead", "1");
						messageRow.put("userName", messagesRow.getToUserUserName());
					}
					messageRow.put("message", messagesRow.getMessage());
					messageRow.put("chainId", messagesRow.getChainId());
					messageRow.put("dateTime", preetyObj.format(messagesRow.getCreatedDate()).toString());

					messagesList.add(messageRow);
				}
				
				// get total planners count
				int totalPlannersCount =  dashboardDao.getAllPlannersCount();
				
				// get latest planners
				List<Planners> plannersList =  dashboardDao.getLatestPlanners();
				PlannerBuilder plannerBuilder =  new PlannerBuilder();	
				List<PlannersModel> plannersModelList =  new ArrayList<PlannersModel>();
				if( ! plannersList.isEmpty()){
					// iterating user to convert Planners Entity to PlannersModel and add into list 
					for (Planners plannerRow : plannersList) {														
						plannersModelList.add(plannerBuilder.fromEntityToModel(plannerRow));
					}
				}
				
				// get total user's  planners count
				int totalUserPlannersCount =  dashboardDao.getAllUserPlannersCount();
				
				// get latest user's planners
				List<UserPlanners> userPlannersList =  dashboardDao.getLatestUserPlanners();
				UserPlannerBuilder userPlannerBuilder =  new UserPlannerBuilder();	
				List<UserPlannersModel> userPlannersModelList =  new ArrayList<UserPlannersModel>();
				if( ! userPlannersList.isEmpty()){
					// iterating user to convert UserPlanners Entity to UserPlannersModel and add into list
					for (UserPlanners userPlannerRow : userPlannersList) {														
						userPlannersModelList.add(userPlannerBuilder.fromEntityToModel(userPlannerRow));
					}
				}
				
				// get total experts count
				int totalExpertRequestsCount =  dashboardDao.getAllExpertRequestCount();
				
				// get latest user's planners
				List<ExpertRequest> expertRequestsList =  dashboardDao.getLatestExpertRequest();
				ExpertRequestBuilder expertRequestBuilder =  new ExpertRequestBuilder();	
				List<ExpertRequestModel>  expertRequestsModelList =  new ArrayList<ExpertRequestModel>();
				if( ! userPlannersList.isEmpty()){
					// iterating user to convert UserPlanners Entity to UserPlannersModel and add into list
					for (ExpertRequest userPlannerRow : expertRequestsList) {														
						expertRequestsModelList.add(expertRequestBuilder.fromEntityToModel(userPlannerRow));
					}
				}
				
				//set ajax response
				String jsonTotalUsersCount = new Gson().toJson(totalUsersCount);
				String jsonLatestUserList = new Gson().toJson(userModelList);
				
				String jsonUnreadMessagesCount = new Gson().toJson(unreadMessages.size());
				
				String jsonMessagesList = new Gson().toJson(messagesList);
				String jsonTotalPlannersCount = new Gson().toJson(totalPlannersCount);
				String jsonPlannersList = new Gson().toJson(plannersModelList);
				String jsonTotalUserPlannersCount = new Gson().toJson(totalUserPlannersCount);
				String jsonUserPlannersList = new Gson().toJson(userPlannersModelList);
				String jsonTotalExpertRequestsCount = new Gson().toJson(totalExpertRequestsCount);
				String jsonExpertRequestsList = new Gson().toJson(expertRequestsModelList);
				
				String jsonContent = "{\"totalUsersCount\":"+jsonTotalUsersCount+", "
										+ "\"latestUserList\":"+jsonLatestUserList+", "
										+ "\"unreadMessagesCount\":"+jsonUnreadMessagesCount+", "
										+ "\"messagesList\":"+jsonMessagesList+", "
										+ "\"totalPlannersCount\":"+jsonTotalPlannersCount+", "
										+ "\"plannersList\":"+jsonPlannersList+", "
										+ "\"totalUserPlannersCount\":"+jsonTotalUserPlannersCount+", "
										+ "\"userPlannersList\":"+jsonUserPlannersList+", "
										+ "\"totalExpertRequestsCount\":"+jsonTotalExpertRequestsCount+", "
										+ "\"expertRequestsList\":"+jsonExpertRequestsList+"}";
	
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
	
}
