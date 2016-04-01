package com.awsmu.model;

import java.util.Date;


import org.springframework.stereotype.Component;
@Component("userPlannerGroupPointsModel")
public class UserPlannerGroupPointsModel {
	private String _id;
	private String userId;
	private String fromUserId;
	private UserPlannerGroupPointsValuesModel performanceValues;
	private int points;
	private Date createdDate;
	private Date editedDate;
	private int isActive;

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

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public UserPlannerGroupPointsValuesModel getPerformanceValues() {
		return performanceValues;
	}

	public void setPerformanceValues(UserPlannerGroupPointsValuesModel performanceValues) {
		this.performanceValues = performanceValues;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
