package com.awsmu.dao;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.awsmu.config.Properties;
import com.awsmu.entity.Friends;
import com.awsmu.entity.Posts;
import com.awsmu.entity.User;
import com.awsmu.entity.UserAttributes;
import com.awsmu.entity.UserValues;
import com.awsmu.exception.AwsmuException;
import com.awsmu.entity.PlannerGroupMember;
import com.awsmu.entity.PostComments;
import com.awsmu.entity.PostLikes;
import com.awsmu.entity.SocialActionsTrail;
import com.awsmu.entity.UserMessage;

/**
 * User dao implementation
 */
@Repository(value = "UserDao")
public class UserDaoImpl implements UserDao {
	
	/**
	 * Injecting mongoTemplate 
	 */
	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	
	
	
	/**
	 * get all user from database
	 */
	@Override
	public  User checkAdminLogin(String email, String password)throws AwsmuException{		
		System.out.println(mongoTemplate);		
		User user;
		try{
			// query to search admin	
			Query searchUserQuery = new Query(Criteria.where("email").is(email).and("password").is(password).and("userRole").is(Properties.ROLE_ADMIN));
			
			user = mongoTemplate.findOne(searchUserQuery,User.class);
		}
	     catch(Exception e){
	    	 
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return user;
	}
	
	
	/**
	 * get user based on the pagination criteria
	 */
	@Override
	public  List<User> getUsers(Integer skipPostRecord, Integer skipPostFreq,Integer page,String sortBy,String sortOrder,List<Object> searchList) throws AwsmuException{
		List<User> userList;
		try{		
			Query query = new Query();		 
			query.limit(skipPostFreq);		
			query.skip(skipPostRecord);
			
			if(sortOrder.equalsIgnoreCase("asc"))
				query.with(new Sort(Sort.Direction.ASC, sortBy));
			else if(sortOrder.equalsIgnoreCase("desc"))
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			else 
				query.with(new Sort(Sort.Direction.DESC, sortBy));
			
			query = addSearchFilter(query,searchList);
			query.addCriteria(Criteria.where("isDeleted").is(0));
			 
			userList =  mongoTemplate.find(query, User.class);
		}
	     catch(Exception e){
	    	 throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);	    	 
	     }
		return userList;
	}	
	/**
	 * get user based on the pagination criteria
	 */
	
	/*function add search parameters as per user request*/
	public Query addSearchFilter(Query query,List<Object> searchList) {
		if(searchList!=null) {
			List<String> boolField = new ArrayList<String>();
			boolField.add("isActive");
			boolField.add("isDeleted");
		   List<Object> filterList =  searchList;			   
		   for (Object rows : filterList) {						   
			   Map<String, String> row = (Map) rows;					   
			   if(boolField.indexOf(row.get("field").toString())!=-1) {
				   query.addCriteria(Criteria.where(row.get("field").toString()).is(Integer.parseInt(row.get("data"))));					  
			   } 
			   else if(row.get("field").toString().equals("_id")){
				   query.addCriteria(Criteria.where(row.get("field").toString()).is(row.get("data")));
			   }
			   else {
				   query.addCriteria(Criteria.where(row.get("field").toString()).regex(WordUtils.capitalize(row.get("data").toString())));
			   }				   
		   }						   
		}	
		return query;
	}
	
	public  int getUserCount(List<Object> searchList) throws AwsmuException{
		int totalUsers=0;
		try{			
			Query query = new Query();
			query = addSearchFilter(query,searchList);			
			query.addCriteria(Criteria.where("isDeleted").is(0));
			
			totalUsers =  mongoTemplate.find(query, User.class).size();			
		}
	     catch(Exception e){	    	
	    	 throw new AwsmuException(500, e.getMessage(), Properties.EXCEPTION_DATABASE);
	     }
		return totalUsers;
	}
	/**
	 *  get user detail by id
	 */
	@Override
	public  User getUserById(String userId) throws AwsmuException{
		User user;
		try{
			Query query = new Query(Criteria.where("_id").is(userId));		 
			user =  mongoTemplate.findOne(query, User.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return user;
	}
	
	/**
	 *  get user attribute by id
	 */
	@Override
	public  List<UserAttributes> getUserAttrByUserId(String userId) throws AwsmuException{
		List<UserAttributes> userAttrList;		
		try{
				// query to search id
		 		Query query = new Query(Criteria.where("userId").is(userId));
		 		userAttrList = mongoTemplate.find(query, UserAttributes.class);
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
		return userAttrList;
	}
	
	/**
	 *  update user profile pic
	 */
	@Override
	public  void updateProfilePic(String fileName, String userId) throws AwsmuException{
	
		try{
			Query query = new Query();
			 query.addCriteria(Criteria.where("_id").is(userId));
			
			 Update update = new Update();
			 update.set("image", fileName);
			 mongoTemplate.updateFirst(query, update, User.class);
		 		
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * check email id already exists
	 */
	@Override
	public  User checkEmailExists(String email, String userId) throws AwsmuException{
		try{	
			 Query query = new Query(Criteria.where("email").is(email).and("_id").ne(userId));
		 	 return mongoTemplate.findOne(query, User.class);		 		
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * check username already exists
	 */
	@Override
	public  User checkUsernameExists(String username, String userId) throws AwsmuException{
		try{	
			 Query query = new Query(Criteria.where("username").is(username).and("_id").ne(userId));
		 	 return mongoTemplate.findOne(query, User.class);		 		
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * check username equal to id of any other user
	 */
	@Override
	public  User checkUsernameEqualToId(String username, String userId) throws AwsmuException{
		try{	
			Query query = new Query(Criteria.where("_id").is(username));
		 	return mongoTemplate.findOne(query, User.class); 		
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * save user detail
	 */
	@Override
	public  void saveUser(User user) throws AwsmuException{
		try{	
			mongoTemplate.save(user, "users");
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	
	/**
	 * save user attributes
	 */
	@Override
	public  void saveAttributes(String id, String optionsName) throws AwsmuException{
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(id));	
			 Update update = new Update();
			 update.set("answer", optionsName);
			 mongoTemplate.updateFirst(query, update, UserAttributes.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	
	/**
	 * change user password
	 */
	@Override
	public  void changeUserPassword(String userId, String password) throws AwsmuException{
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(userId));	
			 Update update = new Update();
			 update.set("password", password);
			 mongoTemplate.updateFirst(query, update, User.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	
	/**
	 * delete user
	 */
	@Override
	public  void deleteUser(String userId) throws AwsmuException{
		try{
			
			 Query query = new Query();			
			 query.addCriteria(Criteria.where("_id").is(userId));	
			 Update update = new Update();
			 update.set("isDeleted", 1);
			 mongoTemplate.updateFirst(query, update, User.class); 
		}
	    catch(Exception e){
	    	throw new AwsmuException(Properties.INTERNAL_SERVER_ERROR, e.getMessage(), Properties.EXCEPTION_DATABASE);
	    }
	}
	
	/**
	 * function to fetch user values
	 */
	public User getUserValues(String userId){
		// query to search id
		Query query = new Query(Criteria.where("_id").is(userId));
		query.fields().include("_id");
		query.fields().include("image");
		query.fields().include("displayName");
		query.fields().include("username");
		query.fields().include("userRole");
		return mongoTemplate.findOne(query, User.class);
	}
	
	/**
	 * get user value info text (used in thread)
	 */
	public String getUserValuesInfoText(String userId) {
		// query to search id
		Query query = new Query(Criteria.where("_id").is(userId));
		User user = mongoTemplate.findOne(query, User.class);
		
		user.getCountry();
		user.getCity();
		user.getProfession();
		
		String userInfoText = "";
		
		if(!user.getProfession().equals("")){
			userInfoText = user.getProfession();
		}
		
		if(!user.getCity().equals("")){
			
			if(!userInfoText.equals(""))
				userInfoText += ", "+user.getCity();
			else
				userInfoText += user.getCity();
		}
		
		if(!user.getCountry().equals("")){
			
			if(!userInfoText.equals(""))
				userInfoText += ", "+user.getCountry();
			else
				userInfoText += user.getCountry();
		}
		
		return userInfoText;
	}
	
	/**
	 * update friend user values (used in thread)
	 */
	public void updateFriendUserValues(String userId, UserValues userValues) {

		Query query = new Query();
		query.addCriteria(Criteria.where("fromUser.userId").is(userId));
		Update update = new Update();
		update.set("fromUser", userValues);
		update.set("updatedDate", new Date());
		mongoTemplate.updateMulti(query, update, Friends.class);

		Query queryTouser = new Query();
		queryTouser.addCriteria(Criteria.where("toUser.userId").is(userId));
		Update updateTouser = new Update();
		updateTouser.set("toUser", userValues);
		updateTouser.set("updatedDate", new Date());
		mongoTemplate.updateMulti(queryTouser, updateTouser, Friends.class);
	}
	
	/**
	 * update post user user values (used in thread)
	 */
	public void updatePostUserValues(String userId, UserValues userValues ){	
		
		 Query query = new Query();
		 query.addCriteria(Criteria.where("user.userId").is(userId));
		 Update update = new Update();
		 update.set("updatedDate", new Date());
		 update.set("user", userValues);
		 mongoTemplate.updateMulti(query, update, Posts.class);		 
	}
	
	/**
	 * update post comment user values (used in thread)
	 */
	public void updatePostCommentUserValues(String userId, UserValues userValues ){
		 Query query = new Query();
		 query.addCriteria(Criteria.where("user.userId").is(userId));
		 Update update = new Update();
		 update.set("updatedDate", new Date());
		 update.set("user", userValues);
		 mongoTemplate.updateMulti(query, update, PostComments.class);	
	}
	
	/**
	 * update post likes user values (used in thread)
	 */
	public void updatePostLikeUserValues(String userId, UserValues userValues ){
		 Query query = new Query();
		 query.addCriteria(Criteria.where("user.userId").is(userId));
		 Update update = new Update();
		 update.set("updatedDate", new Date());
		 update.set("user", userValues);
		 mongoTemplate.updateMulti(query, update, PostLikes.class);	
	}
	
	/**
	 * update message user values (used in thread)
	 */
	public void updateMessageUserValues(String userId, UserValues userValues ){
		Query query = new Query();
		query.addCriteria(Criteria.where("fromUser.userId").is(userId));
		Update update = new Update();
		update.set("updatedDate", new Date());
		update.set("fromUser", userValues);
		mongoTemplate.updateMulti(query, update, UserMessage.class);

		Query queryTouser = new Query();
		queryTouser.addCriteria(Criteria.where("toUser.userId").is(userId));
		Update updateTouser = new Update();
		updateTouser.set("updatedDate", new Date());
		updateTouser.set("toUser", userValues);
		mongoTemplate.updateMulti(queryTouser, updateTouser, UserMessage.class);
	}
	
	/**
	 * update planner group user values (used in thread)
	 */
	public void updatePlannerGroupUserValues(String userId, UserValues userValues ){
		Query query = new Query();
		query.addCriteria(Criteria.where("user.userId").is(userId));
		Update update = new Update();
		update.set("user", userValues);
		mongoTemplate.updateMulti(query, update, PlannerGroupMember.class);
	}
	
	/**
	 * update social action user values (used in thread)
	 */
	public void updateSocialActionsTrailUserValues(String userId, UserValues userValues ){
		 Query query = new Query();
		 query.addCriteria(Criteria.where("user.userId").is(userId));
		 Update update = new Update();
		 update.set("updatedDate", new Date());
		 update.set("user", userValues);
		 mongoTemplate.updateMulti(query, update, SocialActionsTrail.class);	
	}
	
}
