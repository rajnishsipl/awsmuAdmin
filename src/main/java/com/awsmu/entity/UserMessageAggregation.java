package com.awsmu.entity;

import java.util.Date;
import java.util.List;

public class UserMessageAggregation {
	private String _id;

	private String chainId;
	private String message;
	private int isRead;
	private List<String> deletedBy;
	private Date createdDate;

	private String toUserId;
	private String toUserUserName;

	private String toUserDisplayName;
	private String toUserImage;
	private String toUserRole;

	private String fromUserUserId;
	private String fromUserUserName;

	private String fromUserDisplayName;
	private String fromUserImage;
	private String fromUserRole;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public List<String> getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(List<String> deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserUserName() {
		return toUserUserName;
	}

	public void setToUserUserName(String toUserUserName) {
		this.toUserUserName = toUserUserName;
	}

	public String getToUserDisplayName() {
		return toUserDisplayName;
	}

	public void setToUserDisplayName(String toUserDisplayName) {
		this.toUserDisplayName = toUserDisplayName;
	}

	public String getToUserImage() {
		return toUserImage;
	}

	public void setToUserImage(String toUserImage) {
		this.toUserImage = toUserImage;
	}
	

	public String getToUserRole() {
		return toUserRole;
	}

	public void setToUserRole(String toUserRole) {
		this.toUserRole = toUserRole;
	}

	public String getFromUserUserId() {
		return fromUserUserId;
	}

	public void setFromUserUserId(String fromUserUserId) {
		this.fromUserUserId = fromUserUserId;
	}

	public String getFromUserUserName() {
		return fromUserUserName;
	}

	public void setFromUserUserName(String fromUserUserName) {
		this.fromUserUserName = fromUserUserName;
	}

	public String getFromUserDisplayName() {
		return fromUserDisplayName;
	}

	public void setFromUserDisplayName(String fromUserDisplayName) {
		this.fromUserDisplayName = fromUserDisplayName;
	}

	public String getFromUserImage() {
		return fromUserImage;
	}

	public void setFromUserImage(String fromUserImage) {
		this.fromUserImage = fromUserImage;
	}
	
	public String getFromUserRole() {
		return fromUserRole;
	}

	public void setFromUserRole(String fromUserRole) {
		this.fromUserRole = fromUserRole;
	}
}
