package com.awsmu.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles activities list.
 *
 */
@Document(collection = "socialActionsTrail")
public class SocialActionsTrail {
	
	private String _id;
	private String postId;
	private UserValues user;
	private PostTypeValues postFor; // {type:problem,id:123}
	private String actionType;
	private Integer isActive;
    private Date createdDate;
	private Date updatedDate;
	
	public String get_id() {
		return _id;
	}
	public String getPostId() {
		return postId;
	}
	public UserValues getUser() {
		return user;
	}
	public void setUser(UserValues user) {
		this.user = user;
	}
	public PostTypeValues getPostFor() {
		return postFor;
	}
	public String getActionType() {
		return actionType;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	public void setPostFor(PostTypeValues postFor) {
		this.postFor = postFor;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}

