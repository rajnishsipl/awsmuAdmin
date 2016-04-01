package com.awsmu.builder;

import com.awsmu.entity.UserPlannerActions;
import com.awsmu.model.UserPlannerActionsModel;

/**
 * Handles conversion between UserPlannerActions model and UserPlannerActions entity.
 *
 */
public class UserPlannerActionBuilder {
	/**
	 * convert UserPlannerActions model to UserPlannerActions entity before sending it to database
	 */
	public UserPlannerActions fromModelToEntity(UserPlannerActionsModel userPlannerActionsModel){
		UserPlannerActions userPlannerActions = new UserPlannerActions();
				
		userPlannerActions.set_id(userPlannerActionsModel.get_id());		
		userPlannerActions.setUserId(userPlannerActionsModel.getUserId());
		userPlannerActions.setPlannerId(userPlannerActionsModel.getPlannerId());
		userPlannerActions.setActivityId(userPlannerActionsModel.getActivityId());
		userPlannerActions.setIsPerformed(userPlannerActionsModel.getIsPerformed());
		userPlannerActions.setActionDescription(userPlannerActionsModel.getActionDescription());
		userPlannerActions.setActionDate(userPlannerActionsModel.getActionDate());
		userPlannerActions.setIsActive(userPlannerActionsModel.getIsActive());
		userPlannerActions.setCreatedDate(userPlannerActionsModel.getCreatedDate());
		userPlannerActions.setUpdatedDate(userPlannerActionsModel.getUpdatedDate());
				 
		 return userPlannerActions;
	}
	
	/**
	 * convert UserPlannerActions entity to UserPlannerActions model before sending it to view
	 */
	public UserPlannerActionsModel fromEntityToModel(UserPlannerActions userPlannerActions){
		
		UserPlannerActionsModel userPlannerActionsModel = new UserPlannerActionsModel();
		
		userPlannerActionsModel.set_id(userPlannerActions.get_id());		
		userPlannerActionsModel.setUserId(userPlannerActions.getUserId());
		userPlannerActionsModel.setPlannerId(userPlannerActions.getPlannerId());
		userPlannerActionsModel.setActivityId(userPlannerActions.getActivityId());
		userPlannerActionsModel.setIsPerformed(userPlannerActions.getIsPerformed());
		userPlannerActionsModel.setActionDescription(userPlannerActions.getActionDescription());
		userPlannerActionsModel.setActionDate(userPlannerActions.getActionDate());
		userPlannerActionsModel.setIsActive(userPlannerActions.getIsActive());
		userPlannerActionsModel.setCreatedDate(userPlannerActions.getCreatedDate());
		userPlannerActionsModel.setUpdatedDate(userPlannerActions.getUpdatedDate());
				 
		return userPlannerActionsModel;

	}
	
}
