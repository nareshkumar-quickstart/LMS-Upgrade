package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion;

public class SurveyQuestionBankVO implements Serializable {

	private static final long serialVersionUID = -1645413927503568013L;

	private Long id;
	private String name;
	private String description;
	private List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
	
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
	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}
	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}
}
