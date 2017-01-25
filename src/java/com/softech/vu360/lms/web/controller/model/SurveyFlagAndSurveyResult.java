package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.SurveyFlag;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SurveyFlagAndSurveyResult  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private SurveyFlag surveyFlag = null;
	private SurveyResult surveyResult=null;
	public SurveyFlag getSurveyFlag() {
		return surveyFlag;
	}
	public void setSurveyFlag(SurveyFlag surveyFlag) {
		this.surveyFlag = surveyFlag;
	}
	public SurveyResult getSurveyResult() {
		return surveyResult;
	}
	public void setSurveyResult(SurveyResult surveyResult) {
		this.surveyResult = surveyResult;
	}

}
