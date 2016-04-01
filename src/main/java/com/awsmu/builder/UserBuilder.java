package com.awsmu.builder;

import com.awsmu.config.Properties;
import com.awsmu.entity.User;
import com.awsmu.model.UserModel;

/**
 * Handles conversion between user model and user entity.
 *
 */
public class UserBuilder {
	/**
	 * convert user model to user entity before sending it to database
	 */
	public User fromModelToEntity(UserModel userModel){
		 User user = new User();
		 user.set_id(userModel.get_id());
		 user.setAboutMe(userModel.getAboutMe());
		 user.setAddress(userModel.getAddress());
		 user.setArea(userModel.getArea());
		 user.setCity(userModel.getCity());
		 user.setCountry(userModel.getCountry());
		 user.setCreatedDate(userModel.getCreatedDate());
		 user.setDateOfBirth(userModel.getDateOfBirth());
		 user.setDegreeCourses(userModel.getDegreeCourses());
		 user.setDisplayName(userModel.getDisplayName());
		 user.setEmail(userModel.getEmail());
		 user.setFacebookId(userModel.getFacebookId());
		 user.setFacebookUrl(userModel.getFacebookUrl());
		 user.setGender(userModel.getGender());
		 user.setGoogleId(userModel.getGoogleId());
		 user.setImage(userModel.getImage());
		 user.setIsActive(userModel.getIsActive());
		 user.setIsDeleted(userModel.getIsDeleted());
		 user.setIsVerified(userModel.getIsVerified());
		 user.setLastLogin(userModel.getLastLogin());
		 user.setLatitude(userModel.getLatitude());
		 user.setLinkedinId(userModel.getLinkedinId());
		 user.setName(userModel.getName());
		 user.setLongitude(userModel.getLongitude());
		 user.setNationality(userModel.getNationality());
		 user.setPassword(userModel.getPassword());
		 user.setPhone(userModel.getPhone());
		 user.setProfession(userModel.getProfession());
		 user.setShowAttributes(userModel.getShowAttributes());
		 user.setShowPersonal(userModel.getShowPersonal());
		 user.setShowPlanner(userModel.getShowPlanner());
		 user.setSpecialties(userModel.getSpecialties());
		 user.setTwitterId(userModel.getTwitterId());
		 user.setTwitterUrl(userModel.getTwitterUrl());
		 user.setUpdatedDate(userModel.getUpdatedDate());
		 user.setUsername(userModel.getUsername());
		 user.setUserRole(userModel.getUserRole());
		 user.setVerificationCode(userModel.getVerificationCode());
		 user.setForgotPassword(userModel.getForgotPassword());
		 return user;
	}
	
	/**
	 * convert user entity to user model before sending it to view
	 */
	public UserModel fromEntityToModel(User user){
		
		 UserModel userModel = new UserModel();
		 
		 userModel.set_id(user.get_id());
		 userModel.setName(user.getName());
		 userModel.setAboutMe(user.getAboutMe());
		 userModel.setAddress(user.getAddress());
		 userModel.setArea(user.getArea());
		 userModel.setCity(user.getCity());
		 userModel.setCountry(user.getCountry());
		 userModel.setCreatedDate(user.getCreatedDate());
		 userModel.setDateOfBirth(user.getDateOfBirth());
		 userModel.setDegreeCourses(user.getDegreeCourses());
		 userModel.setDisplayName(user.getDisplayName());
		 userModel.setEmail(user.getEmail());
		 userModel.setFacebookId(user.getFacebookId());
		 userModel.setFacebookUrl(user.getFacebookUrl());
		 userModel.setGender(user.getGender());
		 userModel.setGoogleId(user.getGoogleId());
		 
		 String profilePic = user.getImage() == "" ? "profile_no_pic.png": user.getImage();

		 int intIndex = profilePic.indexOf("http");
		 if (intIndex == -1) {
			profilePic = Properties.AMAZON_PROFILE_PIC_URL+ profilePic;
		 }
		 userModel.setImage(profilePic);
		 
		 userModel.setIsActive(user.getIsActive());
		 userModel.setIsDeleted(user.getIsDeleted());
		 userModel.setIsVerified(user.getIsVerified());
		 userModel.setLastLogin(user.getLastLogin());
		 userModel.setLatitude(user.getLatitude());
		 userModel.setLinkedinId(user.getLinkedinId());
		 userModel.setName(user.getName());
		 userModel.setLongitude(user.getLongitude());
		 userModel.setNationality(user.getNationality());
		 userModel.setPassword("");
		 userModel.setPhone(user.getPhone());
		 userModel.setProfession(user.getProfession());
		 userModel.setShowAttributes(user.getShowAttributes());
		 userModel.setShowPersonal(user.getShowPersonal());
		 userModel.setShowPlanner(user.getShowPlanner());
		 userModel.setSpecialties(user.getSpecialties());
		 userModel.setTwitterId(user.getTwitterId());
		 userModel.setTwitterUrl(user.getTwitterUrl());
		 userModel.setUpdatedDate(user.getUpdatedDate());
		 userModel.setUsername(user.getUsername());
		 userModel.setUserRole(user.getUserRole());
		 userModel.setVerificationCode(user.getVerificationCode());
		 userModel.setForgotPassword(user.getForgotPassword());
		 return userModel;
	}
}
