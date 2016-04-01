package com.awsmu.builder;

import com.awsmu.config.Properties;
import com.awsmu.entity.Problem;
import com.awsmu.model.ProblemModel;

public class ProblemBuilder {
	/**
	 * Convert model to entity 
	 */
	public Problem fromModelToEntity(ProblemModel problemModel){
		Problem problem = new Problem();
		problem.set_id(problemModel.get_id());
		problem.setBanner(problemModel.getBanner());
		problem.setCreatedDate(problemModel.getCreatedDate());
		problem.setDescription(problemModel.getDescription());
		problem.setIcon(problemModel.getIcon());
		problem.setIsActive(problemModel.getIsActive());
		problem.setIsDeleted(problemModel.getIsDeleted());
		problem.setName(problemModel.getName());
		problem.setParent_id(problemModel.getParent_id());
		problem.setTitle(problemModel.getTitle());
		problem.setTrends(problemModel.getTrends());
		problem.setUpdatedDate(problemModel.getUpdatedDate());	
		return problem;
	}
	
	/**
	 * Convert entity to model 
	 */
	
	public ProblemModel fromEntityToModel(Problem problem){
		ProblemModel problemModel = new ProblemModel();
		
		problemModel.set_id(problem.get_id());
		problemModel.setBanner(problem.getBanner());
		problemModel.setBannerUrl(Properties.AMAZON_SITE_UPLOADS_URL+problem.getBanner());
		problemModel.setCreatedDate(problem.getCreatedDate());
		problemModel.setDescription(problem.getDescription());
		problemModel.setIconUrl(Properties.AMAZON_SITE_UPLOADS_URL+problem.getIcon());
		problemModel.setIcon(problem.getIcon());
		problemModel.setIsActive(problem.getIsActive());
		problemModel.setIsDeleted(problem.getIsDeleted());
		problemModel.setName(problem.getName());
		problemModel.setParent_id(problem.getParent_id());
		problemModel.setTitle(problem.getTitle());
		problemModel.setTrends(problem.getTrends());
		problemModel.setUpdatedDate(problem.getUpdatedDate());
		return problemModel;
		
	}
}
