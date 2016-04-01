package com.awsmu.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import com.awsmu.builder.PlannerBuilder;
import com.awsmu.builder.ProblemsBuilder;
import com.awsmu.builder.UserPlannerActionBuilder;
import com.awsmu.builder.UserPlannerBuilder;
import com.awsmu.config.Properties;
import com.awsmu.dao.AttributesDao;
import com.awsmu.dao.PlannerDao;
import com.awsmu.dao.ProblemsDao;
import com.awsmu.entity.PlannerActivitiesValues;
import com.awsmu.entity.Planners;
import com.awsmu.entity.Problem;
import com.awsmu.entity.UserPlannerActivitiesValues;
import com.awsmu.entity.UserPlanners;
import com.awsmu.model.AjaxResponse;
import com.awsmu.model.GridResponse;
import com.awsmu.model.PlannersModel;
import com.awsmu.model.ProblemModel;
import com.awsmu.model.UserPlannerActionsModel;
import com.awsmu.entity.UserActivitiesCount;
import com.awsmu.entity.UserPlannerActions;
import com.awsmu.model.UserPlannersModel;
import com.awsmu.util.Utils;
import com.awsmu.exception.AwsmuException;
import com.google.gson.Gson;

/**
 * Planner service implementation
 */
@Service(value = "PlannerService")
public class PlannerServiceImpl implements PlannerService {

	private static Logger logger = Logger.getLogger(PlannerServiceImpl.class);

	/**
	 * Injecting plannerDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "ProblemsDao")
	private ProblemsDao problemsDao;

	/**
	 * Injecting plannerDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "PlannerDao")
	private PlannerDao plannerDao;
	
	
	
	/**
	 * Injecting AttributesDao
	 */
	@Autowired(required = true)
	@Qualifier(value = "AttributesDao")
	private AttributesDao attributesDao;

	/**
	 * get user's planner based on the pagination criteria, also perform
	 * searching sorting This function specially works to return grid response
	 * for jqgrid
	 */
	@Override
	public GridResponse getUserPlannersList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters, String userId) {

		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		UserPlannerBuilder userPlannerBuilder = new UserPlannerBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = plannerDao.getUserPlannersCount(searchList,
					userId);
			// returns users list after searching and sorting
			List<UserPlanners> userPlannersList = plannerDao
					.getUserPlannersList(skipRecord, skipRecord, page, sortBy,
							sortOrder, searchList, userId);
			// setting total records of grid response
			gridResponse.setRecords(totalRecords);

			if (totalRecords != 0) {
				int total = (int) Math.ceil((float) totalRecords
						/ (float) skipRecordFreq);
				gridResponse.setTotal(total);
			}
			// set current page status of pagination for grid
			gridResponse.setPage(page);

			List<Object> rows = new ArrayList<Object>();
			if (userPlannersList.isEmpty()) {

				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			} else {
				// iterating user to convert User Entity to UserModel and add
				// into list for grid response
				for (UserPlanners userPlannerRow : userPlannersList) {
					rows.add(userPlannerBuilder
							.fromEntityToModel(userPlannerRow));
				}
				// add list of user to gridResponse rows
				gridResponse.setRows(rows);
			}
		} catch (AwsmuException e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(e.getCode());
			gridResponse.setMessage(e.getDisplayMsg());
			gridResponse.setRows(null);
			// ajaxResponse.setMessage(e.getDisplayMsg());
		} catch (Exception e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			gridResponse.setMessage(e.getMessage());
			gridResponse.setRows(null);
		}
		return gridResponse;
	}
	
	/**
	 * get all user's planner based on the pagination criteria, also perform
	 * searching sorting This function specially works to return grid response
	 * for jqgrid
	 */
	@Override
	public GridResponse getAllUserPlannersList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {

		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		UserPlannerBuilder userPlannerBuilder = new UserPlannerBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = plannerDao.getAllUserPlannersCount(searchList);
			// returns users list after searching and sorting
			List<UserPlanners> allUserPlannersList = plannerDao
					.getAllUserPlannersList(skipRecord, skipRecord, page, sortBy,
							sortOrder, searchList);
			// setting total records of grid response
			gridResponse.setRecords(totalRecords);

			if (totalRecords != 0) {
				int total = (int) Math.ceil((float) totalRecords
						/ (float) skipRecordFreq);
				gridResponse.setTotal(total);
			}
			// set current page status of pagination for grid
			gridResponse.setPage(page);

			List<Object> rows = new ArrayList<Object>();
			if (allUserPlannersList.isEmpty()) {

				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			} else {
				// iterating user to convert User Entity to UserModel and add
				// into list for grid response
				for (UserPlanners userPlannerRow : allUserPlannersList) {
					rows.add(userPlannerBuilder.fromEntityToModel(userPlannerRow));
				}
				// add list of user to gridResponse rows
				gridResponse.setRows(rows);
			}
		} catch (AwsmuException e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(e.getCode());
			gridResponse.setMessage(e.getDisplayMsg());
			gridResponse.setRows(null);
			
		} catch (Exception e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			gridResponse.setMessage(e.getMessage());
			gridResponse.setRows(null);
		}
		return gridResponse;
	}

	/**
	 * get planners based on the pagination criteria, also perform searching
	 * sorting This function specially works to return grid response for jqgrid
	 */
	@Override
	public GridResponse getPlannersList(Integer skipRecord,
			Integer skipRecordFreq, Integer page, String sortBy,
			String sortOrder, Map<Object, Object> filters) {

		// creating object of grid response class
		GridResponse gridResponse = new GridResponse();
		PlannerBuilder plannerBuilder = new PlannerBuilder();

		try {
			// function return the array list of collection columns and its
			// value after filtering the search parameters
			List<Object> searchList = Utils.getSearchParameter(filters);
			// returns total user count after applying search filters.
			int totalRecords = plannerDao.getPlannersCount(searchList);
			// returns users list after searching and sorting
			List<Planners> plannerList = plannerDao.getPlannersList(skipRecord,
					skipRecord, page, sortBy, sortOrder, searchList);
			// setting total records of grid response
			gridResponse.setRecords(totalRecords);

			if (totalRecords != 0) {
				int total = (int) Math.ceil((float) totalRecords
						/ (float) skipRecordFreq);
				gridResponse.setTotal(total);
			}
			// set current page status of pagination for grid
			gridResponse.setPage(page);

			List<Object> rows = new ArrayList<Object>();
			if (plannerList.isEmpty()) {

				// set total number of rows as null
				gridResponse.setMessage(Properties.NO_RECORD_FOUND);

				gridResponse.setRows(rows);
			} else {
				// iterating user to convert User Entity to UserModel and add
				// into list for grid response
				for (Planners plannerRow : plannerList) {
					rows.add(plannerBuilder.fromEntityToModel(plannerRow));
				}
				// add list of user to gridResponse rows
				gridResponse.setRows(rows);
			}
		} catch (AwsmuException e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_DAO_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(e.getCode());
			gridResponse.setMessage(e.getDisplayMsg());
			gridResponse.setRows(null);
			// ajaxResponse.setMessage(e.getDisplayMsg());
		} catch (Exception e) {
			// print error in debug file
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR + ":"
					+ e.getStackTrace().toString());
			gridResponse.setStatus(false);
			gridResponse.setCode(Properties.DEFAULT_EXCEPTION_ERROR_CODE);
			gridResponse.setMessage(e.getMessage());
			gridResponse.setRows(null);
		}
		return gridResponse;
	}

	/**
	 * get planner detail by planner id
	 */
	@Override
	public  AjaxResponse getPlannerDetailById(String plannerId){

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			// get planner detail 
			Planners planner =  plannerDao.getPlannerDetailById(plannerId);
					
			//check if found any records
			if(planner == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model	
				PlannerBuilder plannerBuilder = new PlannerBuilder();
				PlannersModel plannerDetail = plannerBuilder.fromEntityToModel(planner);
				
				// get problem detail 
				Problem problem =  problemsDao.getProblemDetailById(plannerDetail.getProblemId());
				
				//convert from entity to model	
				ProblemsBuilder problemsBuilder = new ProblemsBuilder();
				ProblemModel problemDetail = problemsBuilder.fromEntityToModel(problem);
				
				
				//create planner detail hash map
				Map<String, String> planerInfo = new HashMap<String, String>();
				planerInfo.put("_id", plannerDetail.get_id());
				planerInfo.put("problemId", plannerDetail.getProblemId());
				planerInfo.put("minFollowTime", String.valueOf(plannerDetail.getMinFollowTime()));
				planerInfo.put("maxFollowTime", String.valueOf(plannerDetail.getMaxFollowTime()));
				planerInfo.put("minAge", String.valueOf(plannerDetail.getMinAge()));
				planerInfo.put("maxAge", String.valueOf(plannerDetail.getMaxAge()));
				planerInfo.put("nationality", plannerDetail.getNationality());
				planerInfo.put("gender", plannerDetail.getGender());
				planerInfo.put("isActive", String.valueOf(plannerDetail.getIsActive()));
				planerInfo.put("createdDate", String.valueOf(plannerDetail.getCreatedDate()));
				planerInfo.put("updatedDate", String.valueOf(plannerDetail.getUpdatedDate()));
				planerInfo.put("area", plannerDetail.getArea());
				planerInfo.put("totalActivity", String.valueOf(plannerDetail.getTotalActivity()));
								
				List<PlannerActivitiesValues> plannerActivities = plannerDetail.getPlannerActivities();
				List<PlannerActivitiesValues> planerGeneralActivity = new ArrayList<PlannerActivitiesValues>();
				List<PlannerActivitiesValues> planerTipActivity = new ArrayList<PlannerActivitiesValues>();
				List<PlannerActivitiesValues> planerTimelineActivity = new ArrayList<PlannerActivitiesValues>();

				for (PlannerActivitiesValues planerActivity : plannerActivities) {
					if (planerActivity.getCategory().equals("General")) {
						planerGeneralActivity.add(planerActivity);
					} else if (planerActivity.getCategory().equals("Tips")) {
						planerTipActivity.add(planerActivity);
					} else {
						planerTimelineActivity.add(planerActivity);
					}
				}
				
				// Sort timeline activity
				if (planerTimelineActivity.size() > 0) {
					Collections.sort(planerTimelineActivity,
							new Comparator<PlannerActivitiesValues>() {
								public int compare(PlannerActivitiesValues o1,
										PlannerActivitiesValues o2) {
									return o1.getMinTime() < o2.getMinTime() ? -1
											: o1.getMinTime() > o2.getMinTime() ? 1
													: 0;

								}
							});
				} 
				
				//set ajax response
				String jsonPlannerDetail = new Gson().toJson(plannerDetail);
				String jsonPlannerInfoList = new Gson().toJson(planerInfo);
				String jsonProblemDetail = new Gson().toJson(problemDetail);
				String jsonPlannerGeneralList = new Gson().toJson(planerGeneralActivity);
				String jsonPlannerTipsList = new Gson().toJson(planerTipActivity);
				String jsonPlannerTimelineList = new Gson().toJson(planerTimelineActivity);
				String jsonGeneralInstructions = new Gson().toJson(plannerDetail.getGeneralInstructions());
				String jsontips = new Gson().toJson(plannerDetail.getTips());
				String jsonNatioanlityList = new Gson().toJson(attributesDao.getNatioanlityList());
				String jsonProblemList = new Gson().toJson(problemsDao.getProblems());
							
				//
				String jsonContent = "{ \"plannerInfoList\":"+jsonPlannerInfoList+", "
										+ "\"problemDetail\":"+jsonProblemDetail+", "
										+ "\"plannerGeneralList\":"+jsonPlannerGeneralList+", "
										+ "\"plannerTipsList\":"+jsonPlannerTipsList+", "
										+ "\"plannerTimelineList\":"+jsonPlannerTimelineList+", "
										+ "\"generalInstructions\":"+jsonGeneralInstructions+", "
										+ "\"natioanlityList\":"+jsonNatioanlityList+", "
										+ "\"problemList\":"+jsonProblemList+", "
										+ "\"plannerDetail\":"+jsonPlannerDetail+", "
										+ "\"tips\":"+jsontips
									 +"}";
         		
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
	 * get planner detail by user planner id
	 */
	@Override
	public AjaxResponse getUserPlannerDetailById(String userPlannerId) {

		
		AjaxResponse ajaxResponse = new AjaxResponse();
			
		try{
			// get planner detail 
			UserPlanners userPlanner =  plannerDao.getUserPlannerDetailById(userPlannerId);
					
			//check if found any records
			if(userPlanner == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				//convert from entity to model	
				UserPlannerBuilder userPlannerBuilder = new UserPlannerBuilder();
				UserPlannersModel userPlannerDetail = userPlannerBuilder.fromEntityToModel(userPlanner);
				
				// get problem detail 
				Problem problem =  problemsDao.getProblemDetailById(userPlannerDetail.getProblemId());
				
				//convert from entity to model	
				ProblemsBuilder problemsBuilder = new ProblemsBuilder();
				ProblemModel problemDetail = problemsBuilder.fromEntityToModel(problem);
				
				//create planner detail hash map
				Map<String, String> planerInfo = new HashMap<String, String>();
				planerInfo.put("_id", userPlannerDetail.get_id());
				planerInfo.put("userId", userPlannerDetail.getUserId());
				
				planerInfo.put("problemId", userPlannerDetail.getProblemId());
				
				planerInfo.put("plannerId", userPlannerDetail.getPlannerId());
				planerInfo.put("plannerName", userPlannerDetail.getPlannerName());
				planerInfo.put("plannerDescription", userPlannerDetail.getPlannerDescription());
				planerInfo.put("minFollowTime", String.valueOf(userPlannerDetail.getMinFollowTime()));
				planerInfo.put("maxFollowTime", String.valueOf(userPlannerDetail.getMaxFollowTime()));
				planerInfo.put("nationality", userPlannerDetail.getNationality());
				planerInfo.put("gender", userPlannerDetail.getGender());
				planerInfo.put("isActive", String.valueOf(userPlannerDetail.getIsActive()));
				planerInfo.put("createdDate", String.valueOf(Utils.getDateFormat(userPlannerDetail.getCreatedDate())));
				planerInfo.put("endDate", String.valueOf(Utils.getDateFormat(userPlannerDetail.getEndDate())));
				planerInfo.put("updatedDate", String.valueOf(userPlannerDetail.getUpdatedDate()));
				planerInfo.put("deletedByUser", String.valueOf(userPlannerDetail.getDeletedByUser()));
				planerInfo.put("deletedDate", String.valueOf(userPlannerDetail.getDeletedDate()));
				planerInfo.put("area", userPlannerDetail.getArea());
				planerInfo.put("totalActivity", String.valueOf(userPlannerDetail.getTotalActivity()));
				
				// To show end date picker
				Date plannerMaxDate = userPlanner.getCreatedDate();
				Calendar c = Calendar.getInstance();
				c.setTime(plannerMaxDate);
				c.add(Calendar.DATE, userPlanner.getMaxFollowTime());
				plannerMaxDate = c.getTime();
				planerInfo.put("plannerMaxDate", String.valueOf(Utils.getDateFormat(plannerMaxDate)));

				Date plannerMinDate = userPlanner.getCreatedDate();
				Calendar c2 = Calendar.getInstance();
				c2.setTime(plannerMinDate);
				c2.add(Calendar.DATE, userPlanner.getMinFollowTime());
				plannerMinDate = c2.getTime();
				planerInfo.put("plannerMinDate", String.valueOf(Utils.getDateFormat(plannerMinDate)));
				
				List<UserPlannerActivitiesValues> userPlannerActivities = userPlannerDetail.getUserPlannerActivities();
				List<UserPlannerActivitiesValues> userPlanerGeneralActivity = new ArrayList<UserPlannerActivitiesValues>();
				List<UserPlannerActivitiesValues> userPlanerTipActivity = new ArrayList<UserPlannerActivitiesValues>();
				List<UserPlannerActivitiesValues> userPlanerTimelineActivity = new ArrayList<UserPlannerActivitiesValues>();

				for (UserPlannerActivitiesValues planerActivity : userPlannerActivities) {
					if (planerActivity.getCategory().equals("General")) {
						userPlanerGeneralActivity.add(planerActivity);
					} else if (planerActivity.getCategory().equals("Tips")) {
						userPlanerTipActivity.add(planerActivity);
					} else {
						userPlanerTimelineActivity.add(planerActivity);
					}
				}
				
				// Sort timeline activities
				if (userPlanerTimelineActivity.size() > 0) {
					Collections.sort(userPlanerTimelineActivity,
							new Comparator<UserPlannerActivitiesValues>() {
								public int compare(UserPlannerActivitiesValues o1,
										UserPlannerActivitiesValues o2) {
									return o1.getMinTime() < o2.getMinTime() ? -1
											: o1.getMinTime() > o2.getMinTime() ? 1
													: 0;

								}
							});
				} 
				
				//set ajax response
				String jsonPlannerInfoList = new Gson().toJson(planerInfo);
				String jsonProblemDetail = new Gson().toJson(problemDetail);
				String jsonPlannerGeneralList = new Gson().toJson(userPlanerGeneralActivity);
				String jsonPlannerTipsList = new Gson().toJson(userPlanerTipActivity);
				String jsonPlannerTimelineList = new Gson().toJson(userPlanerTimelineActivity);
				String jsonGeneralInstructions = new Gson().toJson(userPlannerDetail.getGeneralInstructions());
				String jsontips = new Gson().toJson(userPlannerDetail.getTips());
				
								
				String jsonContent = "{ \"plannerInfoList\":"+jsonPlannerInfoList+", "
										+ "\"problemDetail\":"+jsonProblemDetail+", "
										+ "\"plannerGeneralList\":"+jsonPlannerGeneralList+", "
										+ "\"plannerTipsList\":"+jsonPlannerTipsList+", "
										+ "\"plannerTimelineList\":"+jsonPlannerTimelineList+", "
										+ "\"generalInstructions\":"+jsonGeneralInstructions+", "
										+ "\"tips\":"+jsontips
									 +"}";
         		
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
	 * get planner action by user planner id
	 */
	@Override
	public AjaxResponse getUserPlannerActions(String userPlannerId, String actionDate) {

		AjaxResponse ajaxResponse = new AjaxResponse();
			
		try{
			// get planner detail 
			UserPlanners userPlanner =  plannerDao.getUserPlannerDetailById(userPlannerId);
					
			//check if found any records
			if(userPlanner == null){
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);								
			}else{
				
				// get previous stored actions and actions count
				List<UserPlannerActions> actionsList = new ArrayList<UserPlannerActions>();
				UserActivitiesCount actionsCount = new UserActivitiesCount();
				if (actionDate != null) {
					actionsList = plannerDao.getPlannerActions(userPlannerId, actionDate);
					actionsCount = plannerDao.getDailyActionsCount(userPlannerId, actionDate);
					
				} else {

					actionDate = Utils.getDateFormat(userPlanner.getCreatedDate());
					actionsList = plannerDao.getPlannerActions(userPlannerId, actionDate);
					actionsCount = plannerDao.getDailyActionsCount(userPlannerId, actionDate);
				}
				
				//convert actions from entity to model	
				UserPlannerActionBuilder userPlannerActionBuilder = new UserPlannerActionBuilder();
				List<UserPlannerActionsModel> plannerActionList = new ArrayList<UserPlannerActionsModel>();
				if (actionsList.isEmpty()) {

				} else {
					// iterating user to convert User Entity to UserModel and add
					// into list for grid response
					for (UserPlannerActions actionRow : actionsList) {
						plannerActionList.add(userPlannerActionBuilder.fromEntityToModel(actionRow));
					}
				}
				
				//convert planner from entity to model	
				UserPlannerBuilder userPlannerBuilder = new UserPlannerBuilder();
				UserPlannersModel userPlannerDetail = userPlannerBuilder.fromEntityToModel(userPlanner);
				
				// get problem detail 
				Problem problem =  problemsDao.getProblemDetailById(userPlannerDetail.getProblemId());
				
				//convert problem from entity to model	
				ProblemsBuilder problemsBuilder = new ProblemsBuilder();
				ProblemModel problemDetail = problemsBuilder.fromEntityToModel(problem);
				
				//create planner detail hash map
				Map<String, String> planerInfo = new HashMap<String, String>();
				planerInfo.put("_id", userPlannerDetail.get_id());
				planerInfo.put("userId", userPlannerDetail.get_id());
				
				planerInfo.put("problemId", userPlannerDetail.getProblemId());
				
				planerInfo.put("plannerId", userPlannerDetail.getPlannerId());
				planerInfo.put("plannerName", String.valueOf(userPlannerDetail.getMinFollowTime()));
				planerInfo.put("plannerDescription", String.valueOf(userPlannerDetail.getMaxFollowTime()));
				planerInfo.put("minFollowTime", String.valueOf(userPlannerDetail.getMinFollowTime()));
				planerInfo.put("maxFollowTime", String.valueOf(userPlannerDetail.getMaxFollowTime()));
				planerInfo.put("nationality", userPlannerDetail.getNationality());
				planerInfo.put("gender", userPlannerDetail.getGender());
				planerInfo.put("isActive", String.valueOf(userPlannerDetail.getIsActive()));
				planerInfo.put("createdDate", Utils.getDateFormat(userPlannerDetail.getCreatedDate()));
				planerInfo.put("endDate", Utils.getDateFormat(userPlannerDetail.getEndDate()));
				planerInfo.put("updatedDate", String.valueOf(userPlannerDetail.getUpdatedDate()));
				planerInfo.put("deletedByUser", String.valueOf(userPlannerDetail.getDeletedByUser()));
				planerInfo.put("deletedDate", String.valueOf(userPlannerDetail.getDeletedDate()));
				planerInfo.put("area", userPlannerDetail.getArea());
				planerInfo.put("totalActivity", String.valueOf(userPlannerDetail.getTotalActivity()));
				planerInfo.put("actionDate",  String.valueOf(actionDate));
				if(actionsCount != null){
					planerInfo.put("totalYesCount",  String.valueOf(actionsCount.getTotalYesActivityCount()));
					planerInfo.put("totalNoCount",  String.valueOf(actionsCount.getTotalNoActivityCount()));
				}else{
					planerInfo.put("totalYesCount",  String.valueOf(0));
					planerInfo.put("totalNoCount",  String.valueOf(0));
				}				
				List<UserPlannerActivitiesValues> userPlannerActivities = userPlannerDetail.getUserPlannerActivities();
				List<UserPlannerActivitiesValues> userPlanerGeneralActivity = new ArrayList<UserPlannerActivitiesValues>();
				List<UserPlannerActivitiesValues> userPlanerTipActivity = new ArrayList<UserPlannerActivitiesValues>();
				List<UserPlannerActivitiesValues> userPlanerTimelineActivity = new ArrayList<UserPlannerActivitiesValues>();

				for (UserPlannerActivitiesValues planerActivity : userPlannerActivities) {
					if (planerActivity.getCategory().equals("General")) {
						userPlanerGeneralActivity.add(planerActivity);
					} else if (planerActivity.getCategory().equals("Tips")) {
						userPlanerTipActivity.add(planerActivity);
					} else {
						userPlanerTimelineActivity.add(planerActivity);
					}
				}
				
				// Sort timeline activities
				if (userPlanerTimelineActivity.size() > 0) {
					Collections.sort(userPlanerTimelineActivity,
							new Comparator<UserPlannerActivitiesValues>() {
								public int compare(UserPlannerActivitiesValues o1,
										UserPlannerActivitiesValues o2) {
									return o1.getMinTime() < o2.getMinTime() ? -1
											: o1.getMinTime() > o2.getMinTime() ? 1
													: 0;

								}
							});
				} 
				
				//set ajax response
				String jsonPlannerInfoList = new Gson().toJson(planerInfo);
				String jsonProblemDetail = new Gson().toJson(problemDetail);
				String jsonPlannerGeneralList = new Gson().toJson(userPlanerGeneralActivity);
				String jsonPlannerTipsList = new Gson().toJson(userPlanerTipActivity);
				String jsonPlannerTimelineList = new Gson().toJson(userPlanerTimelineActivity);
				String jsonGeneralInstructions = new Gson().toJson(userPlannerDetail.getGeneralInstructions());
				String jsontips = new Gson().toJson(userPlannerDetail.getTips());
				String jsonPlannerActionList = new Gson().toJson(plannerActionList);
				
								
				String jsonContent = "{ \"plannerInfoList\":"+jsonPlannerInfoList+", "
										+ "\"problemDetail\":"+jsonProblemDetail+", "
										+ "\"plannerGeneralList\":"+jsonPlannerGeneralList+", "
										+ "\"plannerTipsList\":"+jsonPlannerTipsList+", "
										+ "\"plannerTimelineList\":"+jsonPlannerTimelineList+", "
										+ "\"generalInstructions\":"+jsonGeneralInstructions+", "
										+ "\"tips\":"+jsontips+", "
										+ "\"plannerActionList\":"+jsonPlannerActionList
									 +"}";
         		
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
	 * submit planner actions
	 */
	@Override
	public  AjaxResponse actionsSubmit(String userPlannerId, String userId, String actionDate, String activityId, String isPerformed, Integer yesCount, Integer noCount) {

		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			// get planner detail 
			UserPlanners userPlanner =  plannerDao.getUserPlannerDetailById(userPlannerId);

			//check if found any records
			if(userPlanner == null){
				
				ajaxResponse.setMessage(Properties.NO_RECORD_FOUND);	
				
			}else{

				// set default value
				Date now = new Date();

				// get previous stored actions
				UserPlannerActions oldAction = new UserPlannerActions();
				
				oldAction = plannerDao.getPlannerAction(userPlannerId, actionDate, activityId);
				if (oldAction != null) {
					
					oldAction.setIsPerformed(isPerformed);
					oldAction.setUpdatedDate(now);
					String actionId = plannerDao.storeAction(oldAction);

					ajaxResponse.setMessage("Action has been updated successfully.");
	
				} else {
					
					UserPlannerActions newActions = new UserPlannerActions();
					newActions.setUserId(userId);
					newActions.setPlannerId(userPlannerId);
					newActions.setActivityId(activityId);
					newActions.setIsPerformed(isPerformed);
					newActions.setActionDescription("");
					newActions.setActionDate(actionDate);
					newActions.setIsActive(1);
					newActions.setCreatedDate(now);
					newActions.setUpdatedDate(now);
					String actionId = plannerDao.storeAction(newActions);
	
					ajaxResponse.setMessage("Action has been added successfully.");
					
				}
				
				//save count of performed activities day wise
				UserActivitiesCount prevActionsCount = plannerDao.getDailyActionsCount(userPlannerId, actionDate);
				if (prevActionsCount != null) {
					
					prevActionsCount.setTotalYesActivityCount(yesCount);
					prevActionsCount.setTotalNoActivityCount(noCount);	
					prevActionsCount.setUpdatedDate(now);
					plannerDao.storeDailyActionsCount(prevActionsCount);
					
				}else{
					UserActivitiesCount actionsCount = new UserActivitiesCount();
					actionsCount.setUserId(userId);
					actionsCount.setUserPlannerId(userPlannerId);
					actionsCount.setTotalActivity(userPlanner.getTotalActivity());
					actionsCount.setTotalYesActivityCount(yesCount);
					actionsCount.setTotalNoActivityCount(noCount);
					actionsCount.setActionDate(actionDate);
					actionsCount.setIsActive(1);
					actionsCount.setCreatedDate(now);
					actionsCount.setUpdatedDate(now);
					plannerDao.storeDailyActionsCount(actionsCount);
				}
         		
				ajaxResponse.setStatus(true);
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
	 * submit planner actions
	 */
	@Override
	public  AjaxResponse userPlannerEdit(UserPlanners userPlanner) {

		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			//edit detail
			plannerDao.userPlannerEdit(userPlanner);
			
			ajaxResponse.setMessage("User planner has been updated successfully.");
			ajaxResponse.setStatus(true);
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
	 * get user planner 
	 */
	@Override
	public  UserPlanners userPlanner(String userPlannerId) {

		UserPlanners userPlanner = null;
		try{
			// get planner detail 
			userPlanner =  plannerDao.getUserPlannerDetailById(userPlannerId);
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
		}
		return userPlanner;
	
	}
	
	
	/**
	 * get planner 
	 */
	@Override
	public  Planners getPlanner(String plannerId) {

		Planners planner = null;
		try{
			// get planner detail 
			planner =  plannerDao.getPlannerDetailById(plannerId);
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
		}
		return planner;
	
	}
	
	/**
	 * update planner 
	 */
	@Override
	public  AjaxResponse updatePlannerInfo(Planners planner, String plannerId) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		try{
			// update planner detail 
			plannerDao.updatePlannerInfo(planner,plannerId);
			ajaxResponse.setMessage("Planner has been updated successfully.");
			ajaxResponse.setStatus(true);
		}
		catch(AwsmuException e){
			logger.debug(Properties.EXCEPTION_DAO_ERROR +":"+ e.getStackTrace().toString());
		}
		catch(Exception e){
			logger.debug(Properties.EXCEPTION_SERVICE_ERROR+":"+e);
		}
		return ajaxResponse;
	}
	
	
	
	/**
	 * update planner 
	 */
	@Override
	public  AjaxResponse updatePlanner(PlannersModel plannerModel) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		PlannerBuilder plannerBuilder = new PlannerBuilder();
		try{
		
		Planners planner = plannerBuilder.fromModelToEntity(plannerModel);
		List<PlannerActivitiesValues> plannerActivitiesList = new ArrayList<PlannerActivitiesValues>();
		
		for(PlannerActivitiesValues plannerActivity:  planner.getPlannerActivities()){
			
			//set Min time 
			String minTimeStr = plannerActivity.getShowMinTime().replace(':','.');
			Float minTimeFloat = Float.parseFloat(minTimeStr);
			DecimalFormat mindf = new DecimalFormat("0.00");
			mindf.setMaximumFractionDigits(2);
			minTimeStr = mindf.format(minTimeFloat);
			minTimeFloat = Float.parseFloat(minTimeStr);
			plannerActivity.setMinTime(minTimeFloat);
			
			// set Max time
			
			String maxTimeStr = plannerActivity.getShowMaxTime().replace(':','.');
			Float maxTimeFloat = Float.parseFloat(maxTimeStr);
			DecimalFormat maxdf = new DecimalFormat("0.00");
			maxdf.setMaximumFractionDigits(2);
			maxTimeStr = maxdf.format(maxTimeFloat);
			maxTimeFloat = Float.parseFloat(maxTimeStr);
			plannerActivity.setMaxTime(maxTimeFloat);
			
			
			plannerActivitiesList.add(plannerActivity);
			
		}
		planner.setPlannerActivities(plannerActivitiesList);
		
		plannerDao.updatePlanner(planner);
		ajaxResponse.setStatus(true);
		ajaxResponse.setMessage("Planner has been updated successfully.");
		
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
