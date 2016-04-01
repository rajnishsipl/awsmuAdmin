package com.awsmu.builder;


import com.awsmu.entity.ExpertRequest;
import com.awsmu.model.ExpertRequestModel;

/**
 * Handles conversion between expert model and expert entity.
 *
 */
public class ExpertRequestBuilder {
	/**
	 * convert planner model to planner entity before sending it to database
	 */
	public ExpertRequest fromModelToEntity(ExpertRequestModel expertRequestModel){
		ExpertRequest expertRequest = new ExpertRequest();
	
		expertRequest.set_id(expertRequestModel.get_id());
		expertRequest.setFirstName(expertRequestModel.getFirstName());
		expertRequest.setLastName(expertRequestModel.getLastName());
		expertRequest.setEmail(expertRequestModel.getEmail());
		expertRequest.setAddress(expertRequestModel.getAddress());
		expertRequest.setPhone(expertRequestModel.getPhone());
		expertRequest.setRequestMessage(expertRequestModel.getRequestMessage());
		expertRequest.setSpecialization(expertRequestModel.getSpecialization());
		expertRequest.setCountry(expertRequestModel.getCountry());
		expertRequest.setCity(expertRequestModel.getCity());
		expertRequest.setLatitude(expertRequestModel.getLatitude());
		expertRequest.setIsActive(expertRequestModel.getIsActive());
		expertRequest.setCreatedDate(expertRequestModel.getCreatedDate());
		expertRequest.setUpdatedDate(expertRequestModel.getUpdatedDate());
		 
		 return expertRequest;
	}
	
	/**
	 * convert expert entity to expert model before sending it to view
	 */
	public ExpertRequestModel fromEntityToModel(ExpertRequest expertRequest){
		
		ExpertRequestModel expertRequestModel = new ExpertRequestModel();
		
		expertRequestModel.set_id(expertRequest.get_id());
		expertRequestModel.setFirstName(expertRequest.getFirstName());
		expertRequestModel.setLastName(expertRequest.getLastName());
		expertRequestModel.setEmail(expertRequest.getEmail());
		expertRequestModel.setAddress(expertRequest.getAddress());
		expertRequestModel.setPhone(expertRequest.getPhone());
		expertRequestModel.setRequestMessage(expertRequest.getRequestMessage());
		expertRequestModel.setSpecialization(expertRequest.getSpecialization());
		expertRequestModel.setCountry(expertRequest.getCountry());
		expertRequestModel.setCity(expertRequest.getCity());
		expertRequestModel.setLatitude(expertRequest.getLatitude());
		expertRequestModel.setIsActive(expertRequest.getIsActive());
		expertRequestModel.setCreatedDate(expertRequest.getCreatedDate());
		expertRequestModel.setUpdatedDate(expertRequest.getUpdatedDate());
				
		return expertRequestModel;
	}
}
