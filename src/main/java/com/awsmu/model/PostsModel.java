package com.awsmu.model;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
@Component("postsModel")
public class PostsModel {
	private String _id;
	private String postContent;
	private UserValuesModel user;
	private PostTypeValuesModel postFor; // {type:problem,id:123}
	private List<TrendValuesModel> trends;
	private String contentRedirect;
	private int commentCount;
	private int likeCount;
	private int isActive;
	private Date createdDate;
	private Date updatedDate;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	
	public UserValuesModel getUser() {
		return user;
	}
	public void setUser(UserValuesModel user) {
		this.user = user;
	}
	
	
	
	public PostTypeValuesModel getPostFor() {
		return postFor;
	}
	public void setPostFor(PostTypeValuesModel postFor) {
		this.postFor = postFor;
	}
	public List<TrendValuesModel> getTrends() {
		return trends;
	}
	public void setTrends(List<TrendValuesModel> trends) {
		this.trends = trends;
	}
	public String getContentRedirect() {
		return contentRedirect;
	}
	public void setContentRedirect(String contentRedirect) {
		this.contentRedirect = contentRedirect;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
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
		return "PostsModel [_id=" + _id + ", postContent=" + postContent
				+ ", user=" + user + ", postFor=" + postFor + ", trends="
				+ trends + ", contentRedirect=" + contentRedirect
				+ ", commentCount=" + commentCount + ", likeCount=" + likeCount
				+ ", isActive=" + isActive + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + "]";
	}
	
}
