package com.awsmu.builder;

import com.awsmu.config.Properties;
import com.awsmu.entity.Questions;
import com.awsmu.model.QuestionsModel;


public class QuestionsBuilder {

	
	/**
	 * Convert model to entity 
	 */
	public Questions fromModelToEntity(QuestionsModel questionsModel){
		
		Questions questions = new Questions();
		
		questions.set_id(questionsModel.get_id());
		questions.setAttributeText(questionsModel.getAttributeText());
		questions.setCreatedDate(questionsModel.getCreatedDate());
		questions.setIsActive(questionsModel.getIsActive());
		questions.setIsImage(questionsModel.getIsImage());
		questions.setIsUserAttribute(questionsModel.getIsUserAttribute());
		questions.setOptions(questionsModel.getOptions());
		questions.setProblemIds(questionsModel.getProblemIds());
		questions.setQuestion(questionsModel.getQuestion());
		questions.setQuestionTag(questionsModel.getQuestionTag());
		questions.setUpdatedDate(questionsModel.getUpdatedDate());
		questions.setIsDeleted(questionsModel.getIsDeleted());
		return questions;
		
	}
	
	
	/**
	 * Convert entity to model 
	 */
	public QuestionsModel fromEntityToModel(Questions questions){
		
		QuestionsModel questionsModel = new QuestionsModel();
		
		questionsModel.set_id(questions.get_id());
		questionsModel.setAttributeText(questions.getAttributeText());
		questionsModel.setCreatedDate(questions.getCreatedDate());
		questionsModel.setIsActive(questions.getIsActive());
		questionsModel.setIsImage(questions.getIsImage());
		questionsModel.setIsUserAttribute(questions.getIsUserAttribute());
		questionsModel.setOptions(questions.getOptions());
		questionsModel.setProblemIds(questions.getProblemIds());
		questionsModel.setQuestion(questions.getQuestion());
		questionsModel.setQuestionTag(questions.getQuestionTag());
		questionsModel.setUpdatedDate(questions.getUpdatedDate());
		questionsModel.setImageUrl(Properties.AMAZON_SITE_UPLOADS_URL);
		questionsModel.setIsDeleted(questions.getIsDeleted());
		return questionsModel;
		
	}
}
