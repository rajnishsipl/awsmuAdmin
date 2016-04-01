package com.awsmu.entity;

import java.util.Date;


import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles answers attribute
 */
@Document(collection = "answers")
public class Answers {

	private String _id;
	private String userId;
	private String problemId;
	private String questionId;
	private String questionTag;
	private String answer;
	private String plannerId;
	private Integer isActive;
	private Date updatedDate;
	private Date createdDate;
		
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
	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	
	public String getQuestionTag() {
		return questionTag;
	}
	
	public void setQuestionTag(String questionTag) {
		this.questionTag = questionTag;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getPlannerId() {
		return plannerId;
	}
	public void setPlannerId(String plannerId) {
		this.plannerId = plannerId;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
}
