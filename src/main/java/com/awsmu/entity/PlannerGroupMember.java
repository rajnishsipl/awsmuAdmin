package com.awsmu.entity;

import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles planner groups.
 *
 */
@Document(collection = "plannerGroupMember")
public class PlannerGroupMember {

	private String _id;
	private UserValues user;
	private String problemId;
	private String plannerGroupId;
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

	public UserValues getUser() {
		return user;
	}

	public void setUser(UserValues user) {
		this.user = user;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public String getPlannerGroupId() {
		return plannerGroupId;
	}

	public void setPlannerGroupId(String plannerGroupId) {
		this.plannerGroupId = plannerGroupId;
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
