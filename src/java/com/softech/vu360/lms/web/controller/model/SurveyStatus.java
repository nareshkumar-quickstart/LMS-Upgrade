package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class SurveyStatus  implements ILMSBaseInterface, Comparable<SurveyStatus>{
	private static final long serialVersionUID = 1L;
	private String courseName = null;
	private List<LearnerSurveyCourse> surveyItem = new ArrayList<LearnerSurveyCourse>();
	
	
	

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	/**
	 * @return the surveyItem
	 */
	public List<LearnerSurveyCourse> getSurveyItem() {
		return surveyItem;
	}
	/**
	 * @param surveyItem the surveyItem to set
	 */
	public void setSurveyItem(List<LearnerSurveyCourse> surveyItem) {
		this.surveyItem = surveyItem;
	}
	
	public int compareTo(SurveyStatus arg0) {
		return courseName.compareToIgnoreCase(arg0.getCourseName());
	}
}
