package com.awsmu.builder;


import com.awsmu.entity.Problem;
import com.awsmu.model.ProblemModel;

public class ProblemsBuilder {
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
	public ProblemModel fromEntityToModel( Problem  problem){
		ProblemModel problemModel = new ProblemModel();
		
		problemModel.set_id(problem.get_id());
		problemModel.setBanner(problem.getBanner());
		problemModel.setCreatedDate(problem.getCreatedDate());
		problemModel.setDescription(problem.getDescription());
		problemModel.setIcon(problem.getIcon());
		problemModel.setIsActive(problem.getIsActive());
		problemModel.setName(problem.getName());
		problemModel.setParent_id(problem.getParent_id());
		problemModel.setTitle(problem.getTitle());
		problemModel.setTrends(problem.getTrends());
		problemModel.setUpdatedDate(problem.getUpdatedDate());
		
		return problemModel;
	}
}
