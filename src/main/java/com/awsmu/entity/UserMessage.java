package com.awsmu.entity;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "userMessage")
public class UserMessage {
	private String _id;
	private String chainId;
	private UserValues fromUser;
	private UserValues toUser;
	private String message;
	private Integer isRead;
	private List<String> deletedBy;
	private Integer isDeleted;	
	private Date createdDate;
	private Date updatedDate;
	private Integer isActive;
	
	
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

	public UserValues getFromUser() {
		return fromUser;
	}
	public void setFromUser(UserValues fromUser) {
		this.fromUser = fromUser;
	}
	public UserValues getToUser() {
		return toUser;
	}
	public void setToUser(UserValues toUser) {
		this.toUser = toUser;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isread) {
		this.isRead = isread;
	}
	public List<String> getDeletedBy() {
		return deletedBy;
	}
	public void setDeletedBy(List<String> deletedBy) {
		this.deletedBy = deletedBy;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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
	
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "UserMessage [_id=" + _id + ", chainId=" + chainId
				+ ", fromUser=" + fromUser + ", toUser=" + toUser
				+ ", message=" + message + ", isRead=" + isRead
				+ ", deletedBy=" + deletedBy + ", isDeleted=" + isDeleted
				+ ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", isActive=" + isActive + "]";
	}
	
}
