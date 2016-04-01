package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("postTypeValuesModel")
public class PostTypeValuesModel {
	private String type;
	private String typeId;
	private String typeTitle;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeTitle() {
		return typeTitle;
	}
	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}
	
	
}
