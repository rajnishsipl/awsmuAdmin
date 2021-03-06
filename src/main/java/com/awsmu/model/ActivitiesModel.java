package com.awsmu.model;

import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Component;
/**
 * Handles activities list.
 *
 */
@Component("activitiesModel")
public class ActivitiesModel {
	
	private String _id;
	private String category;
    private List<String> problems;
    private String action;
    private String description;
    private String amount;
    private String frequency;
    private String minTime;
    private String maxTime;
    private String minAge;
    private String maxAge;
    private String nationality;
    private String gender;
    private Integer isActive;
    private Date createdDate;
	private Date updatedDate;   	
	private int isDeleted;
	
	
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<String> getProblems() {
		return problems;
	}
	public void setProblems(List<String> problems) {
		this.problems = problems;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public String getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}
	public String getMinAge() {
		return minAge;
	}
	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}
	public String getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	public String getMinTime() {
		return minTime;
	}
	public void setMinTime(String minTime) {
		this.minTime = minTime;
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
	@Override
	public String toString() {
		return "ActivitiesModel [_id=" + _id + ", category=" + category
				+ ", problems=" + problems + ", action=" + action
				+ ", description=" + description + ", amount=" + amount
				+ ", frequency=" + frequency + ", minTime=" + minTime
				+ ", maxTime=" + maxTime + ", minAge=" + minAge + ", maxAge="
				+ maxAge + ", nationality=" + nationality + ", gender="
				+ gender + ", isActive=" + isActive + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + "]";
	}	
	
}
