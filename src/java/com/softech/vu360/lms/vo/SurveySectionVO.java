package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion;

public class SurveySectionVO implements Serializable {
	
	private static final long serialVersionUID = 221035764397349895L;
	
	private Long id;
	private String name;
	private String description;
	private List<SurveyQuestion> surveyQuestions;
	private List<SurveyQuestionBankVO> surveyQuestionBanks = new ArrayList<SurveyQuestionBankVO>();
	private List<SurveySectionVO> children = new ArrayList<SurveySectionVO>();

	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}

	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SurveyQuestionBankVO> getSurveyQuestionBanks() {
		return surveyQuestionBanks;
	}

	public void setSurveyQuestionBanks(List<SurveyQuestionBankVO> surveyQuestionBanks) {
		this.surveyQuestionBanks = surveyQuestionBanks;
	}

	public List<SurveySectionVO> getChildren() {
		return children;
	}

	public void setChildren(List<SurveySectionVO> children) {
		this.children = children;
	}
	
}
