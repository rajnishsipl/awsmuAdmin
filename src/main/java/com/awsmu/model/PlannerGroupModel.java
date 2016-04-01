package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.awsmu.entity.TrendValues;

/**
 * Handles user attribute
 */
@Component("plannerGroupModel")
public class PlannerGroupModel {
	private String _id;
	private String name;
	private String problemId;
	private String plannerGroupId;
	private String title;
	private String description;
	private String icon;	
	private String banner;	
	private List<TrendValues> trends;
	private List<String> trendsIdList;		
	private String iconUrl;
	private String bannerUrl;	
	private Date isDeleted;
	
	
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
	public String getProblemId() {
		return problemId;
	}
	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
	
	public String getPlannerGroupId() {
		return plannerGroupId;
	}
	public void setPlannerGroupId(String plannerGroupId) {
		this.plannerGroupId = plannerGroupId;
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
	public List<String> getTrendsIdList() {
		return trendsIdList;
	}
	public void setTrendsIdList(List<String> trendsIdList) {
		this.trendsIdList = trendsIdList;
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
	
	public Date getIsDeleted() {
		return isDeleted;
	}
	
	public void setIsDeleted(Date isDeleted) {
		this.isDeleted = isDeleted;
	}
}