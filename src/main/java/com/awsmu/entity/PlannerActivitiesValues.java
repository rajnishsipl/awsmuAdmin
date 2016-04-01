package com.awsmu.entity;

import java.util.List;

public class PlannerActivitiesValues {
	private String plannerActivityId;
	private String category;
	private String description;
	private Float minTime;
	private String showMinTime;
    private Float maxTime;
    private String showMaxTime;
    private List<ActivitiesValues> activities;
	
    
    
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
	public String getShowMinTime() {
		return showMinTime;
	}
	public void setShowMinTime(String showMinTime) {
		this.showMinTime = showMinTime;
	}
	public String getShowMaxTime() {
		return showMaxTime;
	}
	public void setShowMaxTime(String showMaxTime) {
		this.showMaxTime = showMaxTime;
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
	public Float getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Float maxTime) {
		this.maxTime = maxTime;
	}
	public List<ActivitiesValues> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivitiesValues> activities) {
		this.activities = activities;
	} 
    
    
}
