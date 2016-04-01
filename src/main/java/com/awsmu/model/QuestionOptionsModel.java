package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("questionOptionsModel")
public class QuestionOptionsModel {
	private String text;
	private String image;
	private String imageUrl;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	
}
