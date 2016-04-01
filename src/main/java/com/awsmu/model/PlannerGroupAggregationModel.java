package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("plannerGroupAggregationModel")
public class PlannerGroupAggregationModel {
	private String problemId;
	private String total;
	
	public String getProblemId() {
		return problemId;
	}
	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
}
