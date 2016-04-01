package com.awsmu.builder;


import com.awsmu.entity.ActivityCategories;

import com.awsmu.model.ActivityCategoriesModel;

public class ActivityCategoriesBuilder {
	/**
	 * Convert model to entity 
	 */
	public ActivityCategories fromModelToEntity(ActivityCategoriesModel categoryModel){
		ActivityCategories category = new ActivityCategories();
		category.set_id(categoryModel.get_id());
		category.setCategory(categoryModel.getCategory());
		category.setCreatedDate(categoryModel.getCreatedDate());
		category.setIsActive(categoryModel.getIsActive());
		category.setIsDeleted(categoryModel.getIsDeleted());
		category.setUpdatedDate(categoryModel.getUpdatedDate());
		return category;
	}
	
	
	/**
	 * Convert entity to model 
	 */
	
	public ActivityCategoriesModel fromEntityToModel(ActivityCategories  category){
		ActivityCategoriesModel categoryModel = new ActivityCategoriesModel();
		categoryModel.set_id(category.get_id());
		categoryModel.setCategory(category.getCategory());
		categoryModel.setCreatedDate(category.getCreatedDate());
		categoryModel.setIsActive(category.getIsActive());
		categoryModel.setUpdatedDate(category.getUpdatedDate());
		categoryModel.setIsDeleted(category.getIsDeleted());
		return categoryModel;		
	}
}
