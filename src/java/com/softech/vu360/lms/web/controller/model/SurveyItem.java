package com.softech.vu360.lms.web.controller.model;



import java.util.Date;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SurveyItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Survey survey = null ;
	private boolean selected = false;
	private boolean openSurvey = false;
	private String surveyStartDate =null;
	private String surveyEndDate =null;
	
	private Date surveyStartDateObject =null;
	private Date surveyEndDateObject =null;
	
	
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	/**
	 * @return the openSurvey
	 */
	public boolean isOpenSurvey() {
		return openSurvey;
	}
	/**
	 * @param openSurvey the openSurvey to set
	 */
	public void setOpenSurvey(boolean openSurvey) {
		this.openSurvey = openSurvey;
	}
	/**
	 * @return the enrollmentStartDate
	 */
 
	/**
	 * @return the surveyStartDate
	 */
	public String getSurveyStartDate() {
		return surveyStartDate;
	}
	/**
	 * @param surveyStartDate the surveyStartDate to set
	 */
	public void setSurveyStartDate(String surveyStartDate) {
		this.surveyStartDate = surveyStartDate;
	}
	/**
	 * @return the surveyEndDate
	 */
	public String getSurveyEndDate() {
		return surveyEndDate;
	}
	/**
	 * @param surveyEndDate the surveyEndDate to set
	 */
	public void setSurveyEndDate(String surveyEndDate) {
		this.surveyEndDate = surveyEndDate;
	}
	/**
	 * @param surveyStartDateObject the surveyStartDateObject to set
	 */
	public void setSurveyStartDateObject(Date surveyStartDateObject) {
		this.surveyStartDateObject = surveyStartDateObject;
	}
	/**
	 * @return the surveyStartDateObject
	 */
	public Date getSurveyStartDateObject() {
		return surveyStartDateObject;
	}
	/**
	 * @param surveyEndDateObject the surveyEndDateObject to set
	 */
	public void setSurveyEndDateObject(Date surveyEndDateObject) {
		this.surveyEndDateObject = surveyEndDateObject;
	}
	/**
	 * @return the surveyEndDateObject
	 */
	public Date getSurveyEndDateObject() {
		return surveyEndDateObject;
	}
  
}