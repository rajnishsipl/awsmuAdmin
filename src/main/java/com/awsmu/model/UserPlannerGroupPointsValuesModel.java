package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("userPlannerGroupPointsValuesModel")
public class UserPlannerGroupPointsValuesModel {
	private String activity;
	private String problemId;
	private String plannerGroupId;
	private String activityId; /* id indicates the id of activity like like activity, comment activity, post activity */
	
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
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

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	

}
