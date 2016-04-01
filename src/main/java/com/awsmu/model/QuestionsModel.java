package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.awsmu.entity.QuestionOptions;

/**
 * Handles questions attribute
 */
@Component("questionsModel")
public class QuestionsModel {

	private String _id;
	private List <String> problemIds;
	private String question;
	private List<QuestionOptions> options;
	private String questionTag;
	private int isActive;
	private int isUserAttribute;
	private Date updatedDate;
	private Date createdDate;
	private String attributeText;
	private int isImage;
	private String imageUrl;
	private int isDeleted;
	
	
	
	
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getIsUserAttribute() {
		return isUserAttribute;
	}
	public void setIsUserAttribute(int isUserAttribute) {
		this.isUserAttribute = isUserAttribute;
	}
	public String getAttributeText() {
		return attributeText;
	}
	public void setAttributeText(String attributeText) {
		this.attributeText = attributeText;
	}
	public int getIsImage() {
		return isImage;
	}
	public void setIsImage(int isImage) {
		this.isImage = isImage;
	}

	public List<QuestionOptions> getOptions() {
		return options;
	}
	public void setOptions(List<QuestionOptions> options) {
		this.options = options;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public List<String> getProblemIds() {
		return problemIds;
	}
	public void setProblemIds(List<String> problemIds) {
		this.problemIds = problemIds;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
		

	public String getQuestionTag() {
		return questionTag;
	}
	
	public void setQuestionTag(String questionTag) {
		this.questionTag = questionTag;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "QuestionsModel [_id=" + _id + ", problemIds=" + problemIds
				+ ", question=" + question + ", options=" + options
				+ ", questionTag=" + questionTag + ", isActive=" + isActive
				+ ", isUserAttribute=" + isUserAttribute + ", updatedDate="
				+ updatedDate + ", createdDate=" + createdDate
				+ ", attributeText=" + attributeText + ", isImage=" + isImage
				+ "]";
	}	
	
	
}
