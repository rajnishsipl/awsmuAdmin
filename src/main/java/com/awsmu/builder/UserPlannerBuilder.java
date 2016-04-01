package com.awsmu.builder;

import com.awsmu.entity.UserPlanners;
import com.awsmu.model.UserPlannersModel;

/**
 * Handles conversion between user planner model and user planner entity.
 *
 */
public class UserPlannerBuilder {
	/**
	 * convert planner model to planner entity before sending it to database
	 */
	public UserPlanners fromModelToEntity(UserPlannersModel userPlannersModel){
		UserPlanners userPlanners = new UserPlanners();
				
		userPlanners.set_id(userPlannersModel.get_id());
		userPlanners.setUserId(userPlannersModel.getUserId());
		userPlanners.setProblemId(userPlannersModel.getProblemId());
		userPlanners.setPlannerName(userPlannersModel.getPlannerName());
		userPlanners.setPlannerDescription(userPlannersModel.getPlannerDescription());
		
		userPlanners.setPlannerId(userPlannersModel.getPlannerId());
		userPlanners.setMinFollowTime(userPlannersModel.getMinFollowTime());
		userPlanners.setMaxFollowTime(userPlannersModel.getMaxFollowTime());
		userPlanners.setAge(userPlannersModel.getAge());
		userPlanners.setGender(userPlannersModel.getGender());
		userPlanners.setNationality(userPlannersModel.getNationality());
		userPlanners.setGender(userPlannersModel.getGender());
		userPlanners.setUserPlannerActivities(userPlannersModel.getUserPlannerActivities());
		userPlanners.setIsActive(userPlannersModel.getIsActive());
		userPlanners.setGeneralInstructions(userPlannersModel.getGeneralInstructions());
		userPlanners.setTips(userPlannersModel.getTips());
		userPlanners.setCreatedDate(userPlannersModel.getCreatedDate());
		userPlanners.setUpdatedDate(userPlannersModel.getUpdatedDate());
		userPlanners.setEndDate(userPlannersModel.getEndDate());
		
		userPlanners.setArea(userPlannersModel.getArea());
		userPlanners.setTotalActivity(userPlannersModel.getTotalActivity());
		userPlanners.setDeletedByUser(userPlannersModel.getDeletedByUser());
		userPlanners.setIsActive(userPlannersModel.getIsActive());
		
		 return userPlanners;
	}
	
	/**
	 * convert user planner entity to user planners model before sending it to view
	 */
	public UserPlannersModel fromEntityToModel(UserPlanners userPlanners){
		
		UserPlannersModel userPlannersModel = new UserPlannersModel();
				
		userPlannersModel.set_id(userPlanners.get_id());
		userPlannersModel.setUserId(userPlanners.getUserId());
		userPlannersModel.setProblemId(userPlanners.getProblemId());
		userPlannersModel.setPlannerName(userPlanners.getPlannerName());
		userPlannersModel.setPlannerDescription(userPlanners.getPlannerDescription());
		
		userPlannersModel.setPlannerId(userPlanners.getPlannerId());
		userPlannersModel.setMinFollowTime(userPlanners.getMinFollowTime());
		userPlannersModel.setMaxFollowTime(userPlanners.getMaxFollowTime());
		userPlannersModel.setAge(userPlanners.getAge());
		userPlannersModel.setGender(userPlanners.getGender());
		userPlannersModel.setNationality(userPlanners.getNationality());
		userPlannersModel.setGender(userPlanners.getGender());
		userPlannersModel.setUserPlannerActivities(userPlanners.getUserPlannerActivities());
		userPlannersModel.setIsActive(userPlanners.getIsActive());
		userPlannersModel.setGeneralInstructions(userPlanners.getGeneralInstructions());
		userPlannersModel.setTips(userPlanners.getTips());
		userPlannersModel.setCreatedDate(userPlanners.getCreatedDate());
		userPlannersModel.setUpdatedDate(userPlanners.getUpdatedDate());
		userPlannersModel.setEndDate(userPlanners.getEndDate());
		
		userPlannersModel.setArea(userPlanners.getArea());
		userPlannersModel.setTotalActivity(userPlanners.getTotalActivity());
		userPlannersModel.setDeletedByUser(userPlanners.getDeletedByUser());
		userPlannersModel.setIsActive(userPlanners.getIsActive());
		
		
		
		return userPlannersModel;
	}
}
