package com.awsmu.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.awsmu.config.Properties;
import com.awsmu.entity.PostComments;
import com.awsmu.entity.Posts;
import com.awsmu.entity.TrendValues;
import com.awsmu.entity.UserValues;
import com.awsmu.model.PostCommentsModel;
import com.awsmu.model.PostTypeValuesModel;
import com.awsmu.model.PostsModel;
import com.awsmu.model.TrendValuesModel;
import com.awsmu.model.UserValuesModel;
import com.awsmu.util.Utils;

public class PostCommemtBuilder {
	/**
	 * convert user model to user entity before sending it to database
	 */
	public PostComments fromModelToEntity(PostCommentsModel postCommentsModel){
		 
		PostComments postComments = new PostComments();
		postComments.set_id(postCommentsModel.get_id());

		String postCommentContent = postCommentsModel.getCommentContent().replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");		
		postCommentContent=postCommentContent.replaceAll("Â","");		
		postCommentContent = HtmlUtils.htmlEscape(postCommentContent);		
		postCommentContent = Utils.replaceURLs(postCommentContent);		
		postComments.setCommentContent(postCommentContent);		
		postComments.setCreatedDate(postCommentsModel.getCreatedDate());		
		postComments.setIsActive(postCommentsModel.getIsActive());
		postComments.setPostId(postCommentsModel.getPostId());
		postComments.setUpdatedDate(postCommentsModel.getUpdatedDate());
				
		UserValues userValues = new UserValues();
		userValues.setDisplayName(postCommentsModel.getUser().getDisplayName());
		userValues.setImage(postCommentsModel.getUser().getImage());
		userValues.setUserId(postCommentsModel.getUser().getUserId());
		userValues.setUserInfo(postCommentsModel.getUser().getUserInfo());
		userValues.setUsername(postCommentsModel.getUser().getUsername());
		userValues.setUserRole(postCommentsModel.getUser().getUserRole());		
		postComments.setUser(userValues);
		
		return postComments; 
	}
	
	/**
	 * convert post comment entity to user model before sending it to view
	*/
	public PostCommentsModel fromEntityToModel(PostComments postComment){		
		PostCommentsModel posCommenttModel = new PostCommentsModel();
		posCommenttModel.set_id(postComment.get_id());
		posCommenttModel.setPostId(postComment.getPostId());
		posCommenttModel.setCommentContent(postComment.getCommentContent());
		
		UserValuesModel uvm = new UserValuesModel(); 
		//postComment.getUser()
		uvm.setUserId(postComment.getUser().getUserId());
		uvm.setDisplayName(postComment.getUser().getDisplayName());
		uvm.setImage(postComment.getUser().getImage());
		uvm.setUserInfo(postComment.getUser().getUserInfo());
		uvm.setUsername(postComment.getUser().getUsername());
		posCommenttModel.setUser(uvm);
		
		posCommenttModel.setIsActive(postComment.getIsActive());
		posCommenttModel.setCreatedDate(postComment.getCreatedDate());
		posCommenttModel.setUpdatedDate(postComment.getUpdatedDate());
		return posCommenttModel;
	}
}