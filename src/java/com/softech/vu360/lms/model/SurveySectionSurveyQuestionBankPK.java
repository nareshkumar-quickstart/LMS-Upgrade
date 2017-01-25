package com.softech.vu360.lms.model;

import java.io.Serializable;

public class SurveySectionSurveyQuestionBankPK implements Serializable {
	private static final long serialVersionUID = 3521454855352102554L;
	private SurveySection surveySection ;
	private SurveyQuestionBank surveyQuestionBank ;
	
	public SurveySection getSurveySection() {
		return surveySection;
	}
	public void setSurveySection(SurveySection surveySection) {
		this.surveySection = surveySection;
	}
	public SurveyQuestionBank getSurveyQuestionBank() {
		return surveyQuestionBank;
	}
	public void setSurveyQuestionBank(SurveyQuestionBank surveyQuestionBank) {
		this.surveyQuestionBank = surveyQuestionBank;
	}
}
