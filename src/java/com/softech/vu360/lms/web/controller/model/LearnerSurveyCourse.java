package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class LearnerSurveyCourse  implements ILMSBaseInterface, Comparable<LearnerSurveyCourse> {
	private static final long serialVersionUID = 1L;
	private String surveyName = null;
	private String status = null;
	
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the surveyName
	 */
	public String getSurveyName() {
		return surveyName;
	}
	/**
	 * @param surveyName the surveyName to set
	 */
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	
	public int compareTo(LearnerSurveyCourse arg0) {
		return surveyName.compareToIgnoreCase(arg0.getSurveyName());
	}
	
}
