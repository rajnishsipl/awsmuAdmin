package com.awsmu.builder;

import com.awsmu.config.Properties;
import com.awsmu.entity.Trends;
import com.awsmu.model.TrendsModel;

public class TrendsBuilder {
	
	
	/**
	 * Convert model to entity 
	 */
	public Trends fromModelToEntity(TrendsModel trendsModel){
		Trends trends =new Trends();
		trends.set_id(trendsModel.get_id());
		trends.setCreatedDate(trendsModel.getCreatedDate());
		trends.setIcon(trendsModel.getIcon());
		trends.setIsActive(trendsModel.getIsActive());
		trends.setIsDeleted(trendsModel.getIsDeleted());
		trends.setTitle(trendsModel.getTitle());
		trends.setUpdatedDate(trendsModel.getUpdatedDate());
		return trends;
		
	}
	
	
	/**
	 * Convert entity to model 
	 */
	public TrendsModel fromEntityToModel(Trends trends){
		TrendsModel trendsModel = new TrendsModel();
		
		trendsModel.set_id(trends.get_id());
		trendsModel.setCreatedDate(trends.getCreatedDate());
		trendsModel.setIcon(trends.getIcon());
		trendsModel.setIconUrl(Properties.AMAZON_SITE_UPLOADS_URL+trends.getIcon());
		trendsModel.setIsDeleted(trends.getIsDeleted());
		trendsModel.setIsActive(trends.getIsActive());
		trendsModel.setTitle(trends.getTitle());
		trendsModel.setUpdatedDate(trends.getUpdatedDate());
		
		return trendsModel;
		
	}
	
}
