package com.awsmu.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.awsmu.config.Properties;
import com.awsmu.entity.PostTypeValues;
import com.awsmu.entity.Posts;
import com.awsmu.entity.TrendValues;
import com.awsmu.entity.User;
import com.awsmu.entity.UserValues;
import com.awsmu.model.PostTypeValuesModel;
import com.awsmu.model.PostsModel;
import com.awsmu.model.TrendValuesModel;
import com.awsmu.model.UserValuesModel;
import com.awsmu.util.Utils;

public class PostBuilder {
	/**
	 * convert user model to user entity before sending it to database
	 */
	public Posts fromModelToEntity(PostsModel postsModel){		 
		Posts posts = new Posts();
		posts.set_id(postsModel.get_id());
		posts.setCommentCount(postsModel.getCommentCount());
		posts.setContentRedirect(postsModel.getContentRedirect());
		posts.setCreatedDate(postsModel.getCreatedDate());
		posts.setIsActive(postsModel.getIsActive());
		posts.setLikeCount(postsModel.getLikeCount());
		
		String postContent = postsModel.getPostContent().replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");		
		postContent=postContent.replaceAll("Â","");		
		postContent = HtmlUtils.htmlEscape(postContent);		
		postContent = Utils.replaceURLs(postContent);		
		posts.setPostContent(postContent);
		
		
		PostTypeValues ptv= new PostTypeValues();
		ptv.setType(postsModel.getPostFor().getType());
		ptv.setTypeId(postsModel.getPostFor().getTypeId());
		ptv.setTypeTitle(postsModel.getPostFor().getTypeTitle());
		posts.setPostFor(ptv);
		
		List<TrendValues>  tvl= new ArrayList<TrendValues>();
		
		TrendValues tv = new TrendValues(); 
		tv.setIcon(postsModel.getTrends().get(0).getIcon());
		tv.setTrendId(postsModel.getTrends().get(0).getTrendId());
		tv.setTitle(postsModel.getTrends().get(0).getTitle());
		tvl.add(tv);			
		posts.setTrends(tvl);
		posts.setUpdatedDate(postsModel.getUpdatedDate());
		
		UserValues userValues = new UserValues();
		userValues.setDisplayName(postsModel.getUser().getDisplayName());
		userValues.setImage(postsModel.getUser().getImage());
		userValues.setUserId(postsModel.getUser().getUserId());
		userValues.setUserInfo(postsModel.getUser().getUserInfo());
		userValues.setUsername(postsModel.getUser().getUsername());
		userValues.setUserRole(postsModel.getUser().getUserRole());
		posts.setUser(userValues);
		
		return posts; 
	}
	
	/**
	 * convert user entity to user model before sending it to view
	*/
	public PostsModel fromEntityToModel(Posts post){		
		 PostsModel postModel = new PostsModel();
		 postModel.set_id(post.get_id());		 
		 String postContent = post.getPostContent();
		 if(postContent!=null) {
			 postContent = postContent.length()>Properties.POST_CONTENT_CHAR_LENGTH ? postContent.substring(0, Properties.POST_CONTENT_CHAR_LENGTH).concat("..."):postContent;
		 }		 
		 postContent = HtmlUtils.htmlUnescape(postContent);
		 postModel.setPostContent(postContent);		 
		 /*Creating user values model from post data */
		 UserValuesModel uvm = new UserValuesModel();
		 uvm.setUserId(post.getUser().getUserId());
		 uvm.setDisplayName(post.getUser().getDisplayName());
		 uvm.setImage(post.getUser().getImage());
		 uvm.setUsername(post.getUser().getUsername());
		 uvm.setUserInfo(post.getUser().getUserInfo());
		 uvm.setUserRole(post.getUser().getUserRole());
		 postModel.setUser(uvm);
		 
		 /*creating postFor object*/
		 PostTypeValuesModel postFor = new PostTypeValuesModel();
		 postFor.setType(post.getPostFor().getType());
		 postFor.setTypeId(post.getPostFor().getTypeId());
		 postFor.setTypeTitle(post.getPostFor().getTypeTitle());
		 postModel.setPostFor(postFor);		 
		 /*creating trend list*/
		 List<TrendValuesModel> trends = new ArrayList<TrendValuesModel>();		 
		 if(!post.getTrends().isEmpty()) {
			 for(TrendValues tv : post.getTrends()) {
				 TrendValuesModel tvm = new TrendValuesModel();
				 tvm.setIcon(tv.getIcon());
				 tvm.setTitle(tv.getTitle());
				 tvm.setTrendId(tv.getTrendId());
				 trends.add(tvm); 
			 }
		 }		 
		 postModel.setTrends(trends);		 
		 postModel.setContentRedirect(post.getContentRedirect());
		 postModel.setCommentCount(post.getCommentCount());
		 postModel.setLikeCount(post.getLikeCount());
		 postModel.setIsActive(post.getIsActive());
		 postModel.setCreatedDate(post.getCreatedDate());
		 postModel.setUpdatedDate(post.getUpdatedDate());		 	
		 return postModel;
	}
}
