package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.awsmu.entity.UserPlannerActivitiesValues;

/**
 * Handles user planner.
 *
 */
@Component("userPlannersModel")
public class UserPlannersModel {
	private String _id;
	private String userId;
	private String plannerId;
	private String plannerName;
	private String plannerDescription;
	private String problemId;
	private Date endDate;
	private Integer minFollowTime;
	private Integer maxFollowTime;
	private Integer age;
	private String nationality;
	private String gender;
	private List<UserPlannerActivitiesValues> userPlannerActivities;    
	private Integer deletedByUser;
	private Integer deletedDate;
	  
    private Integer isActive;
    private Date createdDate;
	private Date updatedDate;
	
	private String area; 
	private List<String> generalInstructions;
    private List<String> tips;
    private Integer totalActivity;
	
	public Integer getTotalActivity() {
		return totalActivity;
	}
	public void setTotalActivity(Integer totalActivity) {
		this.totalActivity = totalActivity;
	}
    
    
    public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public List<String> getGeneralInstructions() {
		return generalInstructions;
	}
	public void setGeneralInstructions(List<String> generalInstructions) {
		this.generalInstructions = generalInstructions;
	}
	public List<String> getTips() {
		return tips;
	}
	public void setTips(List<String> tips) {
		this.tips = tips;
	}
	
	public Integer getMinFollowTime() {
		return minFollowTime;
	}
	public void setMinFollowTime(Integer minFollowTime) {
		this.minFollowTime = minFollowTime;
	}
	public Integer getMaxFollowTime() {
		return maxFollowTime;
	}
	public void setMaxFollowTime(Integer maxFollowTime) {
		this.maxFollowTime = maxFollowTime;
	}
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
	
	public String getPlannerName() {
		return plannerName;
	}
	public void setPlannerName(String plannerName) {
		this.plannerName = plannerName;
	}
	public String getPlannerDescription() {
		return plannerDescription;
	}
	public void setPlannerDescription(String plannerDescription) {
		this.plannerDescription = plannerDescription;
	}
	public void setPlannerId(String plannerId) {
		this.plannerId = plannerId;
	}
	public String getProblemId() {
		return problemId;
	}
	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<UserPlannerActivitiesValues> getUserPlannerActivities() {
		return userPlannerActivities;
	}
	public void setUserPlannerActivities(List<UserPlannerActivitiesValues> userPlannerActivities) {
		this.userPlannerActivities = userPlannerActivities;
	}
	public Integer getDeletedByUser() {
		return deletedByUser;
	}
	public void setDeletedByUser(Integer deletedByUser) {
		this.deletedByUser = deletedByUser;
	}
	public Integer getDeletedDate() {
		return deletedDate;
	}
	public void setDeletedDate(Integer deletedDate) {
		this.deletedDate = deletedDate;
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