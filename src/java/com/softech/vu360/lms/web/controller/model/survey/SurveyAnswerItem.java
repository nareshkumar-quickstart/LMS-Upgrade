package com.softech.vu360.lms.web.controller.model.survey;

import java.io.Serializable;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class SurveyAnswerItem implements ILMSBaseInterface, Serializable {
	
	private static final long serialVersionUID = -592792784452863261L;
	
	private com.softech.vu360.lms.model.SurveyAnswerItem surveyAnswerItemRef;
	private Integer responseCount = 0;
	private Float responsePercent = 0F;

	public SurveyAnswerItem(com.softech.vu360.lms.model.SurveyAnswerItem surveyAnswerItemRef) {
		super();
		this.surveyAnswerItemRef = surveyAnswerItemRef;
	}

	private boolean selected = false;
	
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the surveyAnswerItemRef
	 */
	public com.softech.vu360.lms.model.SurveyAnswerItem getSurveyAnswerItemRef() {
		return surveyAnswerItemRef;
	}

	/**
	 * @param surveyAnswerItemRef the surveyAnswerItemRef to set
	 */
	public void setSurveyAnswerItemRef(
			com.softech.vu360.lms.model.SurveyAnswerItem surveyAnswerItemRef) {
		this.surveyAnswerItemRef = surveyAnswerItemRef;
	}

	public Integer getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(Integer responseCount) {
		this.responseCount = responseCount;
	}

	public Float getResponsePercent() {
		return responsePercent;
	}

	public void setResponsePercent(Float responsePercent) {
		this.responsePercent = responsePercent;
	}
	
}
