package com.awsmu.model;


import java.util.List;
import java.util.Map;


/**
 * Handles Ajax response
 */
public class AjaxResponse {
	
	private boolean status = false;
	private String message;
	
	private Map<String,String> errorMap;
	private boolean isLoggedIn = true;
	private String content;
	private List<Object> contentList;
	private String backUrl;
	private int code=200;
	
	

	public boolean getStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
		
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<Object> getContentList() {
		return contentList;
	}
	public void setContentList(List<Object> contentList) {
		this.contentList = contentList;
	}
	
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public boolean getIsLoggedIn() {
		return this.isLoggedIn;
	}
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	

	
	public Map<String, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
	}

	@Override
	public String toString() {
		return "AjaxResponse [status=" + status + ", message=" + message
				+ ", errorMap=" + errorMap + ", isLoggedIn=" + isLoggedIn
				+ ", content=" + content + ", backUrl=" + backUrl + ", code="
				+ code + "]";
	}

	
	
	
	
	
}