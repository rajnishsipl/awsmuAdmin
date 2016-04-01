package com.awsmu.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "socialNotifications")
public class SocialNotifications {
	private String _id;
	private UserValues fromUser;
	private String postId;
	private String actionType;
	private String toUserId;
	private String text;
	private String url;
	private int isRead;
	private Date createdDate;
	private Date updatedDate;
	
	
	public String get_id() {
		return _id;
	}
	public UserValues getFromUser() {
		return fromUser;
	}
	public String getPostId() {
		return postId;
	}
	public String getActionType() {
		return actionType;
	}
	public String getTouserId() {
		return toUserId;
	}
	public String getText() {
		return text;
	}
	public String getUrl() {
		return url;
	}
	public int getIsRead() {
		return isRead;
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
	public void setFromUser(UserValues fromUser) {
		this.fromUser = fromUser;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public void setTouserId(String touserId) {
		this.toUserId = touserId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
