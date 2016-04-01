package com.awsmu.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles friend request log.
 *
 */
@Document(collection = "friendRequestLog")
public class FriendRequestLog {
	private String _id;
	private String fromUserId;
	private String toUserId;
	private String status;
	private Integer isActive;
	private Integer isDeleted;
	private Date createdDate;
	private Date updatedDate;
	
	public String get_id() {
		return _id;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public String getStatus() {
		return status;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public Integer getIsDeleted() {
		return isDeleted;
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
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}