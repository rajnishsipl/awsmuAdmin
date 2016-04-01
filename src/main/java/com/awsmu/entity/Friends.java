package com.awsmu.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friends")
public class Friends {
	
	private String _id;
	private UserValues toUser;
	private UserValues fromUser;
	private String status;
	private Date requestDate;
	private Date acceptDate;
	private Date declineDate;
	private Date blockDate;
	private Date createdDate;
	private Date updatedDate;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public UserValues getToUser() {
		return toUser;
	}
	public void setToUser(UserValues toUser) {
		this.toUser = toUser;
	}
	public UserValues getFromUser() {
		return fromUser;
	}
	public void setFromUser(UserValues fromUser) {
		this.fromUser = fromUser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
	public Date getDeclineDate() {
		return declineDate;
	}
	public void setDeclineDate(Date declineDate) {
		this.declineDate = declineDate;
	}
	public Date getBlockDate() {
		return blockDate;
	}
	public void setBlockDate(Date blockDate) {
		this.blockDate = blockDate;
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
