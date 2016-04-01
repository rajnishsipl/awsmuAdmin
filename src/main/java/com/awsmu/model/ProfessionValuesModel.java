package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("professionValuesModel")
public class ProfessionValuesModel {
	
	private String name;
	private Integer isActive;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

}
