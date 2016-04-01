package com.awsmu.builder;


import com.awsmu.config.Properties;
import com.awsmu.entity.UserDoctors;
import com.awsmu.model.UserDoctorsModel;

/**
 * Handles conversion between Doctor model and Doctor entity.
 *
 */
public class UserDoctorBuilder {
	/**
	 * convert user's doctor model to user's doctor entity before sending it to database
	 */
	public UserDoctors fromModelToEntity(UserDoctorsModel userDoctorModel){
		UserDoctors userDoctor = new UserDoctors();
		
		if(userDoctorModel.get_id() != null)
			userDoctor.set_id(userDoctorModel.get_id());
				
		userDoctor.setUserId(userDoctorModel.getUserId());
		userDoctor.setName(userDoctorModel.getName());
		userDoctor.setImage(userDoctorModel.getImage());
		userDoctor.setPersonalPhone(userDoctorModel.getPersonalPhone());
		userDoctor.setOfficePhone(userDoctorModel.getOfficePhone());
		userDoctor.setAddress(userDoctorModel.getAddress());
		userDoctor.setLatitude(userDoctorModel.getLatitude());
		userDoctor.setLongitude(userDoctorModel.getLongitude());
		userDoctor.setSpecialties(userDoctorModel.getSpecialties());
		userDoctor.setIsActive(userDoctorModel.getIsActive());
		userDoctor.setIsDeleted(userDoctorModel.getIsDeleted());
		userDoctor.setCreatedDate(userDoctorModel.getCreatedDate());
		userDoctor.setUpdatedDate(userDoctorModel.getUpdatedDate());
				 
		 return userDoctor;
	}
	
	/**
	 * convert User's Doctor entity to User's Doctor model before sending it to view
	 */
	public UserDoctorsModel fromEntityToModel(UserDoctors userDoctor){
		
		UserDoctorsModel userDoctorModel = new UserDoctorsModel();
		
		
		userDoctorModel.set_id(userDoctor.get_id());
		userDoctorModel.setUserId(userDoctor.getUserId());
		userDoctorModel.setName(userDoctor.getName());
		
		String profilePic = userDoctor.getImage() == "" ? "doctorpic.jpg": userDoctor.getImage();
		profilePic = Properties.AMAZON_PROFILE_PIC_URL+ profilePic;
		userDoctorModel.setImageUrl(profilePic);
		
		userDoctorModel.setImage(userDoctor.getImage());
		userDoctorModel.setPersonalPhone(userDoctor.getPersonalPhone());
		userDoctorModel.setOfficePhone(userDoctor.getOfficePhone());
		
		userDoctorModel.setAddress(userDoctor.getAddress());
		userDoctorModel.setLatitude(userDoctor.getLatitude());
		userDoctorModel.setLongitude(userDoctor.getLongitude());
		
		userDoctorModel.setSpecialties(userDoctor.getSpecialties());
		userDoctorModel.setIsActive(userDoctor.getIsActive());
		userDoctorModel.setIsDeleted(userDoctor.getIsDeleted());
		userDoctorModel.setCreatedDate(userDoctor.getCreatedDate());
		userDoctorModel.setUpdatedDate(userDoctor.getUpdatedDate());
						
		return userDoctorModel;
	}
	
}
