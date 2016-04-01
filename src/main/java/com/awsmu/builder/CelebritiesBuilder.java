package com.awsmu.builder;



import com.awsmu.config.Properties;
import com.awsmu.entity.Celebrities;
import com.awsmu.model.CelebritiesModel;

public class CelebritiesBuilder {
	
	/**
	 * Convert model to entity 
	 */
	public Celebrities fromModelToEntity(CelebritiesModel celebritiesModel){
		Celebrities celebrities = new Celebrities(); 
		
		celebrities.setGender(celebritiesModel.getGender());
		celebrities.set_id(celebritiesModel.get_id());
		celebrities.setCreatedDate(celebritiesModel.getCreatedDate());
		
		celebrities.setGender(celebritiesModel.getGender());
		celebrities.setImages(celebritiesModel.getImages());
		celebrities.setIsActive(celebritiesModel.getIsActive());
		celebrities.setMainImage(celebritiesModel.getMainImage());
		celebrities.setName(celebritiesModel.getName());
		celebrities.setNationality(celebritiesModel.getNationality());
		celebrities.setProfession(celebritiesModel.getProfession());
		celebrities.setUpdatedDate(celebritiesModel.getUpdatedDate());
		celebrities.setIsDeleted(celebritiesModel.getIsDeleted());
		return celebrities;
	}
	
	/**
	 * Convert entity to model 
	 */
	public CelebritiesModel fromEntityToModel(Celebrities celebrities){
		CelebritiesModel celebritiesModel = new  CelebritiesModel();
		celebritiesModel.set_id(celebrities.get_id());
		celebritiesModel.setCreatedDate(celebrities.getCreatedDate());
		
		celebritiesModel.setGender(celebrities.getGender());
		celebritiesModel.setImages(celebrities.getImages());
		celebritiesModel.setIsActive(celebrities.getIsActive());
		celebritiesModel.setMainImage(celebrities.getMainImage());
		celebritiesModel.setName(celebrities.getName());
		celebritiesModel.setNationality(celebrities.getNationality());
		celebritiesModel.setProfession(celebrities.getProfession());
		celebritiesModel.setUpdatedDate(celebrities.getUpdatedDate());
		celebritiesModel.setImageUrl(Properties.AMAZON_SITE_UPLOADS_URL);
		celebritiesModel.setIsDeleted(celebrities.getIsDeleted());
		return celebritiesModel;
	}
}
