package com.awsmu.model;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * Handles planner groups.
 *
 */

@Component("plannerGroupMemberModel")
public class PlannerGroupMemberModel {

	private String _id;
	private UserValuesModel user;
	private String problemId;
	private int pointsEarned;

	private Integer isActive;
	private Date createdDate;
	private Date updatedDate;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public UserValuesModel getUser() {
		return user;
	}

	public void setUser(UserValuesModel user) {
		this.user = user;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public int getPointsEarned() {
		return pointsEarned;
	}

	public void setPointsEarned(int pointsEarned) {
		this.pointsEarned = pointsEarned;
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
