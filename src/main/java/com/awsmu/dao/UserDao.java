package com.awsmu.dao;


import java.util.List;

import com.awsmu.entity.User;
import com.awsmu.exception.AwsmuException;
import com.awsmu.entity.UserAttributes;
import com.awsmu.entity.UserValues;

/**
 * User dao interface
 */
public interface UserDao {
	
	
	/**
	 * check admin login from database
	 */
	public User checkAdminLogin(String email, String password) throws AwsmuException;
	
	/**
	 * get user based on the pagination criteria
	 */
	List<User> getUsers(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList)  throws AwsmuException;
	
	/**
	 * get user detail by id
	 */
	public User getUserById(String userId)  throws AwsmuException;
	
	/**
	 * get user attribute by getUserAttrByUserId
	 */
	public List<UserAttributes> getUserAttrByUserId(String userId)  throws AwsmuException;
	
	/**
	 * function return the total number of records in user collection
	 */
	public  int getUserCount(List<Object> searchList) throws AwsmuException;
	
	/**
	 * function update the user profile pic based on userId
	 */
	public  void updateProfilePic(String fileName, String userId) throws AwsmuException;
	
	
	/**
	 * function check email id already exists
	 */
	public  User checkEmailExists(String email, String userId) throws AwsmuException;
	
	/**
	 * function check username already exists
	 */
	public  User checkUsernameExists(String username, String userId) throws AwsmuException;
	
	
	/**
	 * function check username equal to id of any other user
	 */
	public  User checkUsernameEqualToId(String username, String userId) throws AwsmuException;
	
	
	/**
	 * function save user detail
	 */
	public  void saveUser(User user) throws AwsmuException;
	
	/**
	 * function save user attributes
	 */
	public  void saveAttributes(String id, String optionName) throws AwsmuException;
	
	/**
	 * function change user password
	 */
	public  void changeUserPassword(String userId, String password) throws AwsmuException;
	
	
	/**
	 * function to delete user
	 */
	public  void deleteUser(String userId) throws AwsmuException;
	
	/**
	 * function to fetch user values (used in thread)
	 */
	public User getUserValues(String userId);
	
	/**
	 * get user value info text (used in thread)
	 */
	public String getUserValuesInfoText(String userId);
	
	/**
	 * update friend user values (used in thread)
	 */
	public void updateFriendUserValues(String userId, UserValues userValues);	
	
	/**
	 * update post user values (used in thread)
	 */
	public void updatePostUserValues(String userId, UserValues userValues );	
	
	/**
	 * update post comment user values (used in thread)
	 */
	public void updatePostCommentUserValues(String userId, UserValues userValues );
	
	/**
	 * update post likes user values (used in thread)
	 */
	public void updatePostLikeUserValues(String userId, UserValues userValues );
	
	/**
	 * update message user values (used in thread)
	 */
	public void updateMessageUserValues(String userId, UserValues userValues );
	
	/**
	 * update planner group user values (used in thread)
	 */
	public void updatePlannerGroupUserValues(String userId, UserValues userValues );
	
	/**
	 * update social action user values (used in thread)
	 */
	public void updateSocialActionsTrailUserValues(String userId, UserValues userValues );
}
