package com.awsmu.entity;

import java.util.List;

public class UserActivitiesValues {
	
	private String activityId;
	private String description;
	private String action;
	private String amount;
	private String frequency;
	private Integer frequencyValue;
	private List<String> frequencyValues;
	
	
	
	public Integer getFrequencyValue() {
		return frequencyValue;
	}
	public void setFrequencyValue(Integer frequencyValue) {
		this.frequencyValue = frequencyValue;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	public List<String> getFrequencyValues() {
		return frequencyValues;
	}
	public void setFrequencyValues(List<String> frequencyValues) {
		this.frequencyValues = frequencyValues;
	}
	
}
