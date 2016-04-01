package com.awsmu.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles user's planner action.
 *
 */
@Document(collection = "userPlannerActions")
public class UserPlannerActions {
	private String _id;
	private String userId;
	private String plannerId;
	private String activityId;
	private String isPerformed;
	private String actionDescription;
	private String actionDate;
    private Integer isActive;
    private Date createdDate;
	private Date updatedDate;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPlannerId() {
		return plannerId;
	}
	public void setPlannerId(String plannerId) {
		this.plannerId = plannerId;
	}
	
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getIsPerformed() {
		return isPerformed;
	}
	public void setIsPerformed(String isPerformed) {
		this.isPerformed = isPerformed;
	}
	public String getActionDescription() {
		return actionDescription;
	}
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
