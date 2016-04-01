package com.awsmu.model;

import org.springframework.stereotype.Component;

@Component("trendValuesModel")
public class TrendValuesModel {
	private String trendId;
	private String title;
	private String icon;
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
		return "TrendValuesModel [trendId=" + trendId + ", title=" + title
				+ ", icon=" + icon + "]";
	}
	
}
