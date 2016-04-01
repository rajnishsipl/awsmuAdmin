package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("UserValuesModel")
public class UserValuesModel {
	
	private String userId;
	private String displayName;
	private String username;
	private String image;
	private String userRole;
	private String userInfo;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	@Override
	public String toString() {
		return "UserValuesModel [userId=" + userId + ", displayName="
				+ displayName + ", username=" + username + ", image=" + image
				+ ", userRole=" + userRole + ", userInfo=" + userInfo + "]";
	}
	
}
