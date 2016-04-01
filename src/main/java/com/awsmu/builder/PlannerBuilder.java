package com.awsmu.builder;

import com.awsmu.entity.Planners;
import com.awsmu.model.PlannersModel;

/**
 * Handles conversion between planner model and planner entity.
 *
 */
public class PlannerBuilder {
	/**
	 * convert planner model to planner entity before sending it to database
	 */
	public Planners fromModelToEntity(PlannersModel plannerModel){
		Planners planners = new Planners();
				
		planners.set_id(plannerModel.get_id());
		planners.setProblemId(plannerModel.getProblemId());
		planners.setMinFollowTime(plannerModel.getMinFollowTime());
		planners.setMaxFollowTime(plannerModel.getMaxFollowTime());
		planners.setMinAge(plannerModel.getMinAge());
		planners.setMaxAge(plannerModel.getMaxAge());
		planners.setNationality(plannerModel.getNationality());
		planners.setGender(plannerModel.getGender());
		planners.setPlannerActivities(plannerModel.getPlannerActivities());
		planners.setIsActive(plannerModel.getIsActive());
		planners.setGeneralInstructions(plannerModel.getGeneralInstructions());
		planners.setTips(plannerModel.getTips());
		planners.setCreatedDate(plannerModel.getCreatedDate());
		planners.setUpdatedDate(plannerModel.getUpdatedDate());
		planners.setArea(plannerModel.getArea());
		planners.setTotalActivity(plannerModel.getTotalActivity());
				 
		 return planners;
	}
	
	/**
	 * convert planner entity to planners model before sending it to view
	 */
	public PlannersModel fromEntityToModel(Planners planners){
		
		PlannersModel plannersModel = new PlannersModel();
		
		plannersModel.set_id(planners.get_id());
		plannersModel.setProblemId(planners.getProblemId());
		plannersModel.setMinFollowTime(planners.getMinFollowTime());
		plannersModel.setMaxFollowTime(planners.getMaxFollowTime());
		plannersModel.setMinAge(planners.getMinAge());
		plannersModel.setMaxAge(planners.getMaxAge());
		plannersModel.setNationality(planners.getNationality());
		plannersModel.setGender(planners.getGender());
		plannersModel.setPlannerActivities(planners.getPlannerActivities());
		plannersModel.setIsActive(planners.getIsActive());
		plannersModel.setGeneralInstructions(planners.getGeneralInstructions());
		plannersModel.setTips(planners.getTips());
		plannersModel.setCreatedDate(planners.getCreatedDate());
		plannersModel.setUpdatedDate(planners.getUpdatedDate());
		plannersModel.setArea(planners.getArea());
		plannersModel.setTotalActivity(planners.getTotalActivity());
		
		return plannersModel;
	}
}
