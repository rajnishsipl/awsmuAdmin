package com.awsmu.model;

import java.util.Date;


import org.springframework.stereotype.Component;

@Component("userActivitiesCountModel")
public class UserActivitiesCountModel {
	 
	private String _id;
	private String userId;
	private String userPlannerId;
	private Integer totalActivity;
	private Integer totalYesActivityCount;
	private Integer totalNoActivityCount;
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
	public String getUserPlannerId() {
		return userPlannerId;
	}
	public void setUserPlannerId(String userPlannerId) {
		this.userPlannerId = userPlannerId;
	}
	public Integer getTotalActivity() {
		return totalActivity;
	}
	public void setTotalActivity(Integer totalActivity) {
		this.totalActivity = totalActivity;
	}
	public Integer getTotalYesActivityCount() {
		return totalYesActivityCount;
	}
	public void setTotalYesActivityCount(Integer totalYesActivityCount) {
		this.totalYesActivityCount = totalYesActivityCount;
	}
	public Integer getTotalNoActivityCount() {
		return totalNoActivityCount;
	}
	public void setTotalNoActivityCount(Integer totalNoActivityCount) {
		this.totalNoActivityCount = totalNoActivityCount;
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
