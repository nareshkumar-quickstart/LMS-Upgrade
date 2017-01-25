package com.softech.vu360.lms.model.predict;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;

public class PredictSurveyResultAnswer {
	private PredictSurveyQuestionMapping quesMap;
	private List<PredictSurveyAnswerItem> surveyAnswerItems;
	private String surveyAnswerText;
	private List<PredictSurveyResultAnswerFile> answerFiles = new ArrayList<PredictSurveyResultAnswerFile>();
	
	public PredictSurveyQuestionMapping getQuesMap() {
		return quesMap;
	}
	public void setQuesMap(PredictSurveyQuestionMapping quesMap) {
		this.quesMap = quesMap;
	}
	public List<PredictSurveyAnswerItem> getSurveyAnswerItems() {
		return surveyAnswerItems;
	}
	public void setSurveyAnswerItems(List<PredictSurveyAnswerItem> surveyAnswerItems) {
		this.surveyAnswerItems = surveyAnswerItems;
	}
	public String getSurveyAnswerText() {
		return surveyAnswerText;
	}
	public void setSurveyAnswerText(String surveyAnswerText) {
		this.surveyAnswerText = surveyAnswerText;
	}
	public List<PredictSurveyResultAnswerFile> getAnswerFiles() {
		return answerFiles;
	}
	public void setAnswerFiles(List<PredictSurveyResultAnswerFile> answerFiles) {
		this.answerFiles = answerFiles;
	}
}
