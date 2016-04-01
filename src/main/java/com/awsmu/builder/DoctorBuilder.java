package com.awsmu.builder;


import com.awsmu.config.Properties;
import com.awsmu.entity.Doctors;
import com.awsmu.model.DoctorsModel;

/**
 * Handles conversion between Doctor model and Doctor entity.
 *
 */
public class DoctorBuilder {
	/**
	 * convert doctor model to doctor entity before sending it to database
	 */
	public Doctors fromModelToEntity(DoctorsModel doctorModel){
		Doctors doctor = new Doctors();
		
		if(doctorModel.get_id() != null)
			doctor.set_id(doctorModel.get_id());
				
		doctor.setName(doctorModel.getName());
		doctor.setImage(doctorModel.getImage());
		doctor.setPersonalContactNo(doctorModel.getPersonalContactNo());
		doctor.setOfficeContactNo(doctorModel.getOfficeContactNo());
		doctor.setCountry(doctorModel.getCountry());
		doctor.setCity(doctorModel.getCity());
		doctor.setArea(doctorModel.getArea());
		doctor.setAddress(doctorModel.getAddress());
		doctor.setLatitude(doctorModel.getLatitude());
		doctor.setLongitude(doctorModel.getLongitude());
		doctor.setQualifications(doctorModel.getQualifications());
		doctor.setSpecialities(doctorModel.getSpecialities());
		doctor.setIsActive(doctorModel.getIsActive());
		doctor.setIsDeleted(doctorModel.getIsDeleted());
		doctor.setCreatedDate(doctorModel.getCreatedDate());
		doctor.setUpdatedDate(doctorModel.getUpdatedDate());
				 
		 return doctor;
	}
	
	/**
	 * convert Doctor entity to Doctor model before sending it to view
	 */
	public DoctorsModel fromEntityToModel(Doctors doctor){
		
		DoctorsModel doctorModel = new DoctorsModel();
		
		doctorModel.set_id(doctor.get_id());
		doctorModel.setName(doctor.getName());
		
		String profilePic = doctor.getImage() == "" ? "doctorpic.jpg": doctor.getImage();
		profilePic = Properties.AMAZON_PROFILE_PIC_URL+ profilePic;
		doctorModel.setImageUrl(profilePic);
		
		doctorModel.setImage(doctor.getImage());
		doctorModel.setPersonalContactNo(doctor.getPersonalContactNo());
		doctorModel.setOfficeContactNo(doctor.getOfficeContactNo());
		doctorModel.setCountry(doctor.getCountry());
		doctorModel.setCity(doctor.getCity());
		doctorModel.setArea(doctor.getArea());
		doctorModel.setAddress(doctor.getAddress());
		doctorModel.setLatitude(doctor.getLatitude());
		doctorModel.setLongitude(doctor.getLongitude());
		doctorModel.setQualifications(doctor.getQualifications());
		doctorModel.setSpecialities(doctor.getSpecialities());
		doctorModel.setIsActive(doctor.getIsActive());
		doctorModel.setIsDeleted(doctor.getIsDeleted());
		doctorModel.setCreatedDate(doctor.getCreatedDate());
		doctorModel.setUpdatedDate(doctor.getUpdatedDate());
						
		return doctorModel;
	}
	
}
