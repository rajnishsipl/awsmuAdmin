package com.awsmu.model;

import java.util.List;

import org.springframework.stereotype.Component;
@Component("userPlannerActivitiesValuesModel")
public class UserPlannerActivitiesValuesModel {
	private String plannerActivityId;
	private String category;
	private String description;
	private Float minTime;
	private Float userPreferTime;
	private String  showUserPreferTime;

	private String showMinTime;
   	private Float maxTime;
   	private String showMaxTime;
   	private List<UserActivitiesValuesModel> userActivities;
	
   	
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPlannerActivityId() {
		return plannerActivityId;
	}
	public void setPlannerActivityId(String plannerActivityId) {
		this.plannerActivityId = plannerActivityId;
	}

	public Float getUserPreferTime() {
		return userPreferTime;
	}
	public void setUserPreferTime(Float userPreferTime) {
		this.userPreferTime = userPreferTime;
	}
	public String getShowUserPreferTime() {
		return showUserPreferTime;
	}
	public void setShowUserPreferTime(String showUserPreferTime) {
		this.showUserPreferTime = showUserPreferTime;
	}
    
    public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Float getMinTime() {
		return minTime;
	}
	public void setMinTime(Float minTime) {
		this.minTime = minTime;
	}
	public String getShowMinTime() {
		return showMinTime;
	}
	public void setShowMinTime(String showMinTime) {
		this.showMinTime = showMinTime;
	}
	public Float getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Float maxTime) {
		this.maxTime = maxTime;
	}
	public String getShowMaxTime() {
		return showMaxTime;
	}
	public void setShowMaxTime(String showMaxTime) {
		this.showMaxTime = showMaxTime;
	}
	public List<UserActivitiesValuesModel> getUserActivities() {
		return userActivities;
	}
	public void setUserActivities(List<UserActivitiesValuesModel> userActivities) {
		this.userActivities = userActivities;
	} 
}
