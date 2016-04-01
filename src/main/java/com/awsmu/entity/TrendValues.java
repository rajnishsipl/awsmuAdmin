package com.awsmu.entity;

public class TrendValues {
	private String _id;
	private String trendId;
	private String title;
	private String icon;
	// Added for problem 
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getTrendId() {
		return trendId;
	}
	public void setTrendId(String trendId) {
		this.trendId = trendId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Override
	public String toString() {
		return "TrendValues [_id=" + _id + ", trendId=" + trendId + ", title="
				+ title + ", icon=" + icon + "]";
	}
	
	
}
