package com.awsmu.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.awsmu.config.Properties;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PlannersModel;
import com.awsmu.entity.Planners;
import com.awsmu.entity.UserActivitiesValues;
import com.awsmu.entity.UserPlannerActivitiesValues;
import com.awsmu.entity.UserPlanners;
import com.awsmu.service.PlannerService;
import com.google.gson.Gson;

/**
 * Planner controller to handles planner section.
 */
@Controller
public class PlannerController {
	
	/**
	 * Injecting PlannerServiceImpl 
	 */
	@Autowired(required = true)
    @Qualifier(value = "PlannerService")
	private PlannerService plannerService;
	
	/*
	 *Created Date: 24-Sep-2015
     *Purpose: get user's planners based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/usersPlannersList/{userId}", method = RequestMethod.POST)
	public @ResponseBody String usersPlannersList(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String userId) {				
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
		
		
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"), HashMap.class);

		// get admin id from session if logged in  
		if(session.getAttribute("userId") == null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(plannerService.getUserPlannersList((page-1)*displayRows, displayRows, page, sidx, sord, filters, userId));
	}
	
	/*
	 *Created Date: 07-Nov-2015
     *Purpose: get all user's planners based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/allUsersPlannersList", method = RequestMethod.POST)
	public @ResponseBody String allUsersPlannersList(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
		
		
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"), HashMap.class);

		// get admin id from session if logged in  
		if(session.getAttribute("userId") == null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return new Gson().toJson(plannerService.getAllUserPlannersList((page-1)*displayRows, displayRows, page, sidx, sord, filters));
	}
		
	/*
	 *Created Date: 24-Sep-2015
     *Purpose: get planners based on the pagination criteria
     */ 	 
	@RequestMapping(value = "/plannersList", method = RequestMethod.POST)
	public @ResponseBody String plannersList(HttpSession session, HttpServletRequest request, HttpServletResponse response) {				
		int page = Integer.parseInt(request.getParameter("page"));
		int displayRows =  Integer.parseInt(request.getParameter("rows"));
		
		
		String sidx = request.getParameter("sidx");
		String sord =  request.getParameter("sord");		
		// creating map object of the search filter, Null will be assigned if not filter sent from jqgrid
		Map<Object, Object> filters= new Gson().fromJson(request.getParameter("filters"), HashMap.class);

		// get admin id from session if logged in  
		if(session.getAttribute("userId")==null) {
			GridResponse gr = new GridResponse();
			gr.setCode(Properties.USER_AUTHENTICATION_FAILED_CODE);
			gr.setStatus(false);
			gr.setIsLoggedIn(false);
			gr.setMessage(Properties.USER_AUTHENTICATION_FAILED_MESSAGE);
			return new Gson().toJson(gr);
		}		
		return  new Gson().toJson(plannerService.getPlannersList((page-1)*displayRows, displayRows, page, sidx, sord, filters));
	}
	
	/*
	 *Created Date: 30-Sep-2015
     *Purpose: get planner detail
     */ 	 
	@RequestMapping(value = "/getPlannerDetail/{plannerId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getPlannerDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String plannerId) {				
		//get planner detail 
		return plannerService.getPlannerDetailById(plannerId);
			
	}
	
	/*
	 *Created Date: 06-Oct-2015
     *Purpose: get user planner detail
     */ 	 
	@RequestMapping(value = "/getUserPlannerDetail/{userPlannerId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getUserPlannerDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String userPlannerId) {				
		//get planner detail 
		return plannerService.getUserPlannerDetailById(userPlannerId);
			
	}
	
	/*
	 *Created Date: 10-Nov-2015
     *Purpose: get user planner action 
     */ 	 
	@RequestMapping(value = "/getUserPlannerActions/{userPlannerId}", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getUserPlannerActions(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String userPlannerId, @RequestParam(value = "actionDate", required = false) String actionDate) {				
		//get planner detail 
		return plannerService.getUserPlannerActions(userPlannerId, actionDate);
			
	}
	
	/*
	 *Created Date: 10-Nov-2015
     *Purpose: submit user planner actions
     */ 	 
	@RequestMapping(value = "/actionsSubmit", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse actionsSubmit(HttpSession session, 
															HttpServletRequest request, 
															HttpServletResponse response, 
															@RequestParam(value = "userPlannerId", required = false) String userPlannerId,
															@RequestParam(value = "userId", required = false) String userId,
															@RequestParam(value = "actionDate", required = false) String actionDate,
															@RequestParam(value = "activityId", required = false) String activityId,
															@RequestParam(value = "isPerformed", required = false) String isPerformed,
															@RequestParam(value = "yesCount", required = false) Integer yesCount,
															@RequestParam(value = "noCount", required = false) Integer noCount) {				
		//get planner detail 
		return plannerService.actionsSubmit(userPlannerId, userId, actionDate, activityId, isPerformed, yesCount, noCount);
			
	}
	
	
	
	/*  
	*Created Date: 25-Nov-2015
	*Purpose:Function to edit planner activities  
	*/ 	 
	@RequestMapping(value = "/editPlanner", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse updatePlanner(HttpSession session,AjaxResponse ajaxResponse,@RequestParam("planner") String planner ) {
		PlannersModel   plannersModel = new Gson().fromJson(planner,PlannersModel.class);
		return plannerService.updatePlanner(plannersModel);
	}
	
	
	
	/*
	 *Created Date: 21-Nov-2015
     *Purpose: edit planner information
     */ 
	@RequestMapping(value = "/plannerInfoEdit", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse plannerEdit(HttpServletRequest request, 
														HttpServletResponse response) {				
		AjaxResponse ajaxResponse = new AjaxResponse();
			
		Planners mainPlanner = plannerService.getPlanner(request.getParameter("plannerId"));

		if (mainPlanner == null){
			ajaxResponse.setMessage("No planner found.");
			
		}else{
			
				Planners planner = new Planners();
				//planner.set_id(mainPlanner.get_id());
				planner.setArea(request.getParameter("area"));
				planner.setGender(request.getParameter("gender"));
				planner.setIsActive(Integer.parseInt(request.getParameter("isActive")));
				planner.setMaxAge(Integer.parseInt(request.getParameter("maxAge")));
				planner.setMinAge(Integer.parseInt(request.getParameter("minAge")));
				planner.setMaxFollowTime(Integer.parseInt(request.getParameter("maxFollowTime")));
				planner.setMinFollowTime(Integer.parseInt(request.getParameter("minFollowTime")));
				planner.setNationality(request.getParameter("nationality"));
				planner.setProblemId(request.getParameter("problemId"));
				
				ajaxResponse = plannerService.updatePlannerInfo(planner,mainPlanner.get_id());
			
		}
		
		
		return ajaxResponse;		
		
	}
	
	
	
	
	
	/*
	 *Created Date: 18-Nov-2015
     *Purpose: edit user planner
     */ 
	@RequestMapping(value = "/userPlannerEdit", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse userPlannerEdit(HttpServletRequest request, 
														HttpServletResponse response) {				
		AjaxResponse ajaxResponse = new AjaxResponse();
			
		UserPlanners mainPlanner = plannerService.userPlanner(request.getParameter("userPlannerId"));

		if (mainPlanner == null){
			ajaxResponse.setMessage("No planner found.");
			
		}else{
			try {
			
				// Convert date picker date to java date
				Date myDate = null;
				DateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
				myDate = dformat.parse(request.getParameter("curEndDate"));
				SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
				String ymd = ymdFormat.format(myDate);
				Date curEndDate = ymdFormat.parse(ymd);
		
				UserPlanners userPlanner = new UserPlanners();
				userPlanner.set_id(mainPlanner.get_id());
				userPlanner.setAge(mainPlanner.getAge());
				userPlanner.setGender(mainPlanner.getGender());
				userPlanner.setIsActive(Integer.parseInt(request.getParameter("isActive")));
				userPlanner.setNationality(mainPlanner.getNationality());
				userPlanner.setPlannerId(mainPlanner.getPlannerId());
				userPlanner.setPlannerName(request.getParameter("plannerName"));
				userPlanner.setPlannerDescription(request.getParameter("plannerDescription"));
				userPlanner.setUserId(mainPlanner.getUserId());
				userPlanner.setProblemId(mainPlanner.getProblemId());
				userPlanner.setMaxFollowTime(mainPlanner.getMaxFollowTime());
				userPlanner.setMinFollowTime(mainPlanner.getMinFollowTime());
				userPlanner.setCreatedDate(mainPlanner.getCreatedDate());
				userPlanner.setUpdatedDate(mainPlanner.getUpdatedDate());
				userPlanner.setDeletedByUser(0);
				userPlanner.setDeletedDate(null);
				userPlanner.setEndDate(curEndDate);
				userPlanner.setTotalActivity(mainPlanner.getTotalActivity());
				userPlanner.setArea(mainPlanner.getArea());
				userPlanner.setGeneralInstructions(mainPlanner.getGeneralInstructions());
				userPlanner.setTips(mainPlanner.getTips());
		
				// Set userPlannerActivity
				List<UserPlannerActivitiesValues> userPlannerActivitiesList = new ArrayList<UserPlannerActivitiesValues>();
				List<UserPlannerActivitiesValues> plannerActivitiesList = mainPlanner
						.getUserPlannerActivities();
		
				for (UserPlannerActivitiesValues planerActivity : plannerActivitiesList) {
					UserPlannerActivitiesValues userPlannerActivities = new UserPlannerActivitiesValues();
					// Set according user input
					if (!planerActivity.getCategory().equals("General")
							&& !planerActivity.getCategory().equals("Tips")) {
		
						userPlannerActivities
								.setShowUserPreferTime(request
										.getParameter(planerActivity
												.getPlannerActivityId()));
						String timeStr = request.getParameter(
								planerActivity.getPlannerActivityId()).replace(':',
								'.');
						Float timeFloat = Float.parseFloat(timeStr);
		
						DecimalFormat df = new DecimalFormat("0.00");
						df.setMaximumFractionDigits(2);
						timeStr = df.format(timeFloat);
						timeFloat = Float.parseFloat(timeStr);
						userPlannerActivities.setUserPreferTime(timeFloat);
		
					} else {
						userPlannerActivities.setShowUserPreferTime("");
						userPlannerActivities.setUserPreferTime(null);
					}
		
					userPlannerActivities.setPlannerActivityId(planerActivity.getPlannerActivityId());
					userPlannerActivities.setCategory(planerActivity.getCategory());
					userPlannerActivities.setDescription(planerActivity.getDescription());
					userPlannerActivities.setMaxTime(planerActivity.getMaxTime());
					userPlannerActivities.setMinTime(planerActivity.getMinTime());
					userPlannerActivities.setShowMaxTime(planerActivity.getShowMaxTime());
					userPlannerActivities.setShowMinTime(planerActivity.getShowMinTime());
		
					List<UserActivitiesValues> activityList = planerActivity.getUserActivities();
					List<UserActivitiesValues> userActivityList = new ArrayList<UserActivitiesValues>();
		
					for (UserActivitiesValues activity : activityList) {
						UserActivitiesValues userActivity = new UserActivitiesValues();
						if (request.getParameterMap().containsKey(
								activity.getActivityId())) {
							List<String> frequencyValuesList = new ArrayList<String>();
							String[] frequencyValuesArr = request
									.getParameterValues(activity.getActivityId());
							if (frequencyValuesArr.length > 0) {
								for (String frequencyValuestr : frequencyValuesArr) {
									frequencyValuesList.add(frequencyValuestr);
								}
							}
							userActivity.setFrequencyValues(frequencyValuesList);
						} else
							userActivity.setFrequencyValues(null);
		
						userActivity.setActivityId(activity.getActivityId());
						userActivity.setAction(activity.getAction());
						userActivity.setAmount(activity.getAmount());
						userActivity.setDescription(activity.getDescription());
						userActivity.setFrequency(activity.getFrequency());
						userActivity.setFrequencyValue(activity.getFrequencyValue());
						userActivityList.add(userActivity);
					}
					userPlannerActivities.setUserActivities(userActivityList);
					userPlannerActivitiesList.add(userPlannerActivities);
		
				}
				userPlanner.setUserPlannerActivities(userPlannerActivitiesList);
		
				ajaxResponse = plannerService.userPlannerEdit(userPlanner);
				
			}catch (Exception e) {
				ajaxResponse.setMessage("Exception occured while updating.");
			}
		}
		
		return ajaxResponse;			
	}
}
