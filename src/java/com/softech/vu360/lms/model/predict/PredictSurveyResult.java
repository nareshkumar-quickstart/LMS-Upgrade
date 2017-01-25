package com.softech.vu360.lms.model.predict;

import java.util.List;

public class PredictSurveyResult {
	private String surveyee;
	private String responseDate;
	private List<PredictSurveyResultAnswer> answers;
	
	public String getSurveyee() {
		return surveyee;
	}
	public void setSurveyee(String surveyee) {
		this.surveyee = surveyee;
	}
	public String getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}
	public List<PredictSurveyResultAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<PredictSurveyResultAnswer> answers) {
		this.answers = answers;
	}
}
