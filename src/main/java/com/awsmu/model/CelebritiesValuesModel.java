package com.awsmu.model;

import org.springframework.stereotype.Component;

/**
 * Handles celebrities images and tag attribute.
 *
*/

@Component("celebritiesValuesModel")
public class CelebritiesValuesModel {
	private String name;
	private String attribute; // problem name
	private String attributeTitle; // problem title
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getAttributeTitle() {
		return attributeTitle;
	}
	public void setAttributeTitle(String attributeTitle) {
		this.attributeTitle = attributeTitle;
	}
		
	
}
