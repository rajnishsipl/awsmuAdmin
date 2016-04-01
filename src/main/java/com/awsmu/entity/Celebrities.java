package com.awsmu.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Handles celebrities list.
 *
 */
@Document(collection = "celebrities")
public class Celebrities {
	private String _id;
	private String name;
	private String gender;
	
	private String nationality;
	private Integer isActive;
	private Date createdDate;
	private Date updatedDate;
	private List<CelebritiesValues> images;
	private String profession;
	private String mainImage;
	private int isDeleted;
	
	
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
	public String getMainImage() {
		return mainImage;
	}
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	public List<CelebritiesValues> getImages() {
		return images;
	}
	public void setImages(List<CelebritiesValues> images) {
		this.images = images;
	}
	
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
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
	@Override
	public String toString() {
		return "Celebrities [_id=" + _id + ", name=" + name + ", gender="
				+ gender + ", nationality=" + nationality + ", isActive="
				+ isActive + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", images=" + images + ", profession="
				+ profession + ", mainImage=" + mainImage + "]";
	}
	
	
	
}
