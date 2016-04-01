package com.awsmu.builder;
import com.awsmu.entity.PostLikes;
import com.awsmu.model.PostLikesModel;
import com.awsmu.model.UserValuesModel;
public class PostLikeBuilder {
	/**
	 * convert user model to user entity before sending it to database
	 */
	public PostLikes fromModelToEntity(PostLikesModel userModel){
		 
		PostLikes postLike = new PostLikes();
		
		return postLike; 
	}
	
	/**
	 * convert post comment entity to user model before sending it to view
	*/
	public PostLikesModel fromEntityToModel(PostLikes postLike){		
		PostLikesModel posLikeModel = new PostLikesModel();
		posLikeModel.set_id(postLike.get_id());
		posLikeModel.setPostId(postLike.getPostId());
		
		UserValuesModel uvm = new UserValuesModel(); 
		//postComment.getUser()
		uvm.setUserId(postLike.getUser().getUserId());
		uvm.setDisplayName(postLike.getUser().getDisplayName());
		uvm.setImage(postLike.getUser().getImage());
		uvm.setUserInfo(postLike.getUser().getUserInfo());
		uvm.setUsername(postLike.getUser().getUsername());
		posLikeModel.setUser(uvm);		
		posLikeModel.setIsActive(postLike.getIsActive());
		posLikeModel.setCreatedDate(postLike.getCreatedDate());
		posLikeModel.setUpdatedDate(postLike.getUpdatedDate());
		return posLikeModel;
	}
}
