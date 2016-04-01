package com.awsmu.model;

import java.util.Date;


import org.springframework.stereotype.Component;
@Component("postCommentsModel")
public class PostCommentsModel {
	private String _id;
	private UserValuesModel user;
	private String postId;
	private String commentContent;
	private int isActive;
	private Date createdDate;
	private Date updatedDate;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public UserValuesModel getUser() {
		return user;
	}
	public void setUser(UserValuesModel user) {
		this.user = user;
	}
	
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
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
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
