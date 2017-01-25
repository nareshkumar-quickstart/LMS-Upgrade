package com.softech.vu360.lms.model.predict;

import java.util.List;

import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;

public class ExportToPredict {
	private Long surveyId;
	private String surveyStatus;
	private List<PredictSurveyResult> surveyResults;
	private List<PredictSurveyQuestionMapping> predictSurveyQuestionMappings;
	
	public Long getSurveyId() {
		return surveyId;
	}
	
	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
	
	public String getSurveyStatus() {
		return surveyStatus;
	}
	
	public void setSurveyStatus(String surveyStatus) {
		this.surveyStatus = surveyStatus;
	}
	
	public List<PredictSurveyResult> getSurveyResults() {
		return surveyResults;
	}
	
	public void setSurveyResults(List<PredictSurveyResult> surveyResults) {
		this.surveyResults = surveyResults;
	}
	
	public List<PredictSurveyQuestionMapping> getPredictSurveyQuestionMappings() {
		return predictSurveyQuestionMappings;
	}
	
	public void setPredictSurveyQuestionMappings(List<PredictSurveyQuestionMapping> predictSurveyQuestionMappings) {
		this.predictSurveyQuestionMappings = predictSurveyQuestionMappings;
	}
}
