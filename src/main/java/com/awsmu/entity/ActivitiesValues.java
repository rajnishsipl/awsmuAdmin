package com.awsmu.entity;

public class ActivitiesValues {
	private String activityId;
	private String description;
	private String action;
	private String amount;
	private String frequency;
	private Integer frequencyValue;

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
}
