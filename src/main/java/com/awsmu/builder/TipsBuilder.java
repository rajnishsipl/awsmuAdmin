package com.awsmu.builder;

import com.awsmu.entity.Tips;

import com.awsmu.model.TipsModel;


public class TipsBuilder {
	/**
	 * Convert model to entity 
	 */
	public Tips fromModelToEntity(TipsModel tipsModel){
		Tips tips =new Tips();
		tips.set_id(tipsModel.get_id());
		tips.setCreatedDate(tipsModel.getCreatedDate());
		tips.setIsActive(tipsModel.getIsActive());
		tips.setTipText(tipsModel.getTipText());
		tips.setTipTitle(tipsModel.getTipTitle());
		tips.setUpdatedDate(tipsModel.getUpdatedDate());
		
		// Set blank fields
		if(tipsModel.getAge() != 0)
			tips.setAge(tipsModel.getAge());
		else
			tips.setAge(0);
		
				
		if(tipsModel.getGender() !=null)
			tips.setGender(tipsModel.getGender());
		else
			tips.setGender("Any");
		
		
		if(tipsModel.getNationality() !=null)
			tips.setNationality(tipsModel.getNationality());
		else
			tips.setNationality("");
			
		
		if(tipsModel.getProblemNames() !=null)
			tips.setProblemNames(tipsModel.getProblemNames());
		else
			tips.setProblemNames(null);
		
		if(tipsModel.getProfession() !=null)
			tips.setProfession(tipsModel.getProfession());
		else
			tips.setProfession("");
		return tips;
	}
	
	

	/**
	 * Convert entity to model 
	 */
	
	public TipsModel fromEntityToModel(Tips tips){
		TipsModel tipsModel =new TipsModel();
		tipsModel.set_id(tips.get_id());
		tipsModel.setAge(tips.getAge());
		tipsModel.setCreatedDate(tips.getCreatedDate());
		tipsModel.setGender(tips.getGender());
		tipsModel.setIsActive(tips.getIsActive());
		tipsModel.setNationality(tips.getNationality());
		tipsModel.setProblemNames(tips.getProblemNames());
		tipsModel.setProfession(tips.getProfession());
		tipsModel.setTipText(tips.getTipText());
		tipsModel.setTipTitle(tips.getTipTitle());
		tipsModel.setUpdatedDate(tips.getUpdatedDate());
		
		return tipsModel;
	}
	
}
