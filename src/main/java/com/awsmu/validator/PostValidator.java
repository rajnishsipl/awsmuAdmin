package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;

import com.awsmu.model.PostsModel;
import com.awsmu.util.Utils;


public class PostValidator {
	int MAX_POST_CHAR_LEN=5000;
	public Map<String,String> validatePost(PostsModel postsModel){
		Map<String,String> errorMap = new HashMap<String,String>();
		// Validate message text
		if(Utils.checkEmpty(postsModel.getPostContent())){
			errorMap.put("postContent", "Post is required");
		}else if(Utils.checkStringLength(postsModel.getPostContent(), this.MAX_POST_CHAR_LEN))
			errorMap.put("postContent", "Post character lenght should not exceed "+this.MAX_POST_CHAR_LEN+" characters");
				
		// Validate trend
		if(postsModel.getTrends().isEmpty()) {
			errorMap.put("trends", "Select post trend");
		}
		else if(Utils.checkEmpty(postsModel.getTrends().get(0).getTitle())){
			errorMap.put("trends", "Select post trend");
		}
		return errorMap;
	}
}
