package com.awsmu.model;
import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Component;
@Component("userMessageModel")
public class UserMessageModel {
	private String _id;
	private String chainId;
	private UserValuesModel fromUser;
	private UserValuesModel toUser;
	private String message;
	private Integer isRead;
	
	private List<String> deletedBy;
	private Integer isDeleted;	
	private Date createdDate;
	private Date updatedDate;
	private String preetyTime;
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

	public UserValuesModel getFromUser() {
		return fromUser;
	}
	public void setFromUser(UserValuesModel fromUser) {
		this.fromUser = fromUser;
	}
	public UserValuesModel getToUser() {
		return toUser;
	}
	public void setToUser(UserValuesModel toUser) {
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
	
	public String getPreetyTime() {
		return preetyTime;
	}
	public void setPreetyTime(String preetyTime) {
		this.preetyTime = preetyTime;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "UserMessageModel [_id=" + _id + ", chainId=" + chainId
				+ ", fromUser=" + fromUser + ", toUser=" + toUser
				+ ", message=" + message + ", isRead=" + isRead
				+ ", deletedBy=" + deletedBy + ", isDeleted=" + isDeleted
				+ ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", isActive=" + isActive + "]";
	}
	
}
