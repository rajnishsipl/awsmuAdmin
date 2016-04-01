package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import com.awsmu.entity.PlannerActivitiesValues;

@Component("plannersModel")
public class PlannersModel {
	private String _id;
	private String problemId;
	private Integer minFollowTime;
	private Integer maxFollowTime;
	private Integer minAge;
	private Integer maxAge;
	private String nationality;
    private String gender;
    private List<PlannerActivitiesValues> plannerActivities;       
    private Integer isActive;
    private List<String> generalInstructions;
    private List<String> tips;
    private Date createdDate;
	private Date updatedDate;
	private String area;
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
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getProblemId() {
		return problemId;
	}
	public void setProblemId(String problemId) {
		this.problemId = problemId;
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
	public Integer getMinAge() {
		return minAge;
	}
	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}
	public Integer getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
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
	public List<PlannerActivitiesValues> getPlannerActivities() {
		return plannerActivities;
	}
	public void setPlannerActivities(List<PlannerActivitiesValues> plannerActivities) {
		this.plannerActivities = plannerActivities;
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
