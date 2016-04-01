package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.awsmu.entity.TrendValues;

/**
 * Handles user attribute
 */
@Component("problemModel")
public class ProblemModel {
	private String _id;
	private String name;
	private String parent_id;
	private String title;
	private String description;
	private String icon;
	
	private String banner;	
	private List<TrendValues> trends;
	private List<String> trendsIdList;
	
	
	private String iconUrl;
	private String bannerUrl;	
	private int isDeleted;
	
	
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public List<String> getTrendsIdList() {
		return trendsIdList;
	}
	public void setTrendsIdList(List<String> trendsIdList) {
		this.trendsIdList = trendsIdList;
	}
	
	
	private Date updatedDate;
	private Integer isActive;
	private Date createdDate;
		
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
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}	
	
	public List<TrendValues> getTrends() {
		return trends;
	}
	
	public void setTrends(List<TrendValues> trends) {
		this.trends = trends;
	}
	
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	@Override
	public String toString() {
		return "ProblemModel [_id=" + _id + ", name=" + name + ", parent_id="
				+ parent_id + ", title=" + title + ", description="
				+ description + ", icon=" + icon + ", banner=" + banner
				+ ", trends=" + trends + ", trendsIdList=" + trendsIdList
				+ ", updatedDate=" + updatedDate + ", isActive=" + isActive
				+ ", createdDate=" + createdDate + "]";
	}

	
	
	
}
