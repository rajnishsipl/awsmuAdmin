package com.awsmu.validator;

import java.util.HashMap;
import java.util.Map;
import com.awsmu.model.PostCommentsModel;
import com.awsmu.util.Utils;

public class PostCommentValidator {
	int MAX_COMMENT_CHAR_LEN = 5000;
	public Map<String, String> validatePostComment(PostCommentsModel postCommentsModel) {
		Map<String, String> errorMap = new HashMap<String, String>();
		// Validate comment text
		if (Utils.checkEmpty(postCommentsModel.getCommentContent())) {
			errorMap.put("commentContent", "Comment is required");
		} else if (Utils.checkStringLength(
				postCommentsModel.getCommentContent(),
				this.MAX_COMMENT_CHAR_LEN))
			errorMap.put("commentContent",
					"Comment character lenght should not exceed "
							+ this.MAX_COMMENT_CHAR_LEN + " characters");

		return errorMap;
	}
}
