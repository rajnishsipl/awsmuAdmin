package com.awsmu.builder;

import com.awsmu.entity.Activities;

import com.awsmu.model.ActivitiesModel;



public class ActivitiesBuilder {

	/**
	 * Convert model to entity 
	 */
	public Activities fromModelToEntity(ActivitiesModel activitiesModel){
		Activities activities = new Activities();
		
		activities.set_id(activitiesModel.get_id());
		
		if(activitiesModel.getAction() != null)
			activities.setAction(activitiesModel.getAction());
		else
			activities.setAction("");
		
		if(activitiesModel.getAmount() != null)
			activities.setAmount(activitiesModel.getAmount());
		else
			activities.setAmount(null);
		
		if(activitiesModel.getCategory() !=null)
			activities.setCategory(activitiesModel.getCategory());
		else
			activities.setCategory("");
		
		if(activitiesModel.getDescription() != null)
			activities.setDescription(activitiesModel.getDescription());
		else
			activities.setDescription("");
		
		if(activitiesModel.getFrequency() != null)
			activities.setFrequency(activitiesModel.getFrequency());
		else
			activities.setFrequency("");
		
		if(activitiesModel.getGender() != null)
			activities.setGender(activitiesModel.getGender());
		else
			activities.setGender("Any"); //Set default to any
		
		if(activitiesModel.getMaxAge() != null)
			activities.setMaxAge(activitiesModel.getMaxAge());
		else
			activities.setMaxAge("");
		
		if(activitiesModel.getMinAge() !=null)
			activities.setMinAge(activitiesModel.getMinAge());
		else
			activities.setMinAge("");
		
		
		if(activitiesModel.getMinTime() !=null)
			activities.setMinTime(activitiesModel.getMinTime());
		else
			activitiesModel.setMinTime("");
		
		
		
		if(activitiesModel.getMaxTime() !=null)
			activities.setMaxTime(activitiesModel.getMaxTime());
		else
			activities.setMaxTime("");
		
		if(activitiesModel.getNationality() !=null)
			activities.setNationality(activitiesModel.getNationality());
		else
			activities.setNationality("Any"); //Set default to any
		
		activities.setProblems(activitiesModel.getProblems());
		activities.setIsActive(activitiesModel.getIsActive());
		activities.setIsDeleted(activitiesModel.getIsDeleted());
		
		activities.setUpdatedDate(activitiesModel.getUpdatedDate());
		activities.setCreatedDate(activitiesModel.getCreatedDate());
		return activities;
	}
	
	
	/**
	 * Convert entity to model 
	 */
	public ActivitiesModel fromEntityToModel(Activities activities){
		ActivitiesModel activitiesModel = new ActivitiesModel();
		activitiesModel.set_id(activities.get_id());
		
		activitiesModel.setAction(activities.getAction());
		activitiesModel.setAmount(activities.getAmount());
		activitiesModel.setCategory(activities.getCategory());
		activitiesModel.setCreatedDate(activities.getCreatedDate());
		activitiesModel.setDescription(activities.getDescription());
		activitiesModel.setFrequency(activities.getFrequency());
		activitiesModel.setGender(activities.getGender());
		activitiesModel.setIsActive(activities.getIsActive());
		activitiesModel.setMaxAge(activities.getMaxAge());
		activitiesModel.setMinTime(activities.getMinTime());
		activitiesModel.setMinAge(activities.getMinAge());
		activitiesModel.setMaxTime(activities.getMaxTime());
		activitiesModel.setNationality(activities.getNationality());
		activitiesModel.setProblems(activities.getProblems());
		activitiesModel.setUpdatedDate(activities.getUpdatedDate());
		activitiesModel.setIsDeleted(activities.getIsDeleted());
		return activitiesModel;
	}
	
	
	

}
