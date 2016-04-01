package com.awsmu.builder;


import com.awsmu.entity.Attributes;

import com.awsmu.model.AttributesModel;

public class AttributesBuilder {
	/**
	 * Convert model to entity 
	 */
	public Attributes fromModelToEntity(AttributesModel attributeModel){
		Attributes attribute = new Attributes();
		attribute.set_id(attributeModel.get_id());
		attribute.setAttr(attributeModel.getAttr());
		attribute.setCreated_date(attributeModel.getCreated_date());
		return attribute;
	}
	
	
	/**
	 * Convert entity to model 
	 */
	
	public AttributesModel fromEntityToModel(Attributes  attribute){
		AttributesModel attributeModel = new AttributesModel();
		attributeModel.set_id(attribute.get_id());
		attributeModel.setAttr(attribute.getAttr());
		attributeModel.setCreated_date(attribute.getCreated_date());
		return attributeModel;		
	}
}
