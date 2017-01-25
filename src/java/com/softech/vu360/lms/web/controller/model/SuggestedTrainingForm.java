package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * created on 28th May 2010.
 *
 */
public class SuggestedTrainingForm implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private long sid = 0;
	private SuggestedTraining suggTraining ;
	private com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = null;
	//private List<SuggestedTraining> suggTrainings=new ArrayList<SuggestedTraining>();
	private List<Course> courses=new ArrayList<Course>();

	/**
	 * @return the sid
	 */
	public long getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(long sid) {
		this.sid = sid;
	}

	/**
	 * @return the surveyView
	 */
	public com.softech.vu360.lms.web.controller.model.survey.Survey getSurveyView() {
		return surveyView;
	}

	/**
	 * @param surveyView the surveyView to set
	 */
	public void setSurveyView(
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView) {
		this.surveyView = surveyView;
	}

	/**
	 * @return the suggTraining
	 */
	public SuggestedTraining getSuggTraining() {
		return suggTraining;
	}

	/**
	 * @param suggTraining the suggTraining to set
	 */
	public void setSuggTraining(SuggestedTraining suggTraining) {
		this.suggTraining = suggTraining;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	/*public List<SuggestedTraining> getSuggTrainings() {
		return suggTrainings;
	}

	public void setSuggTrainings(List<SuggestedTraining> suggTrainings) {
		this.suggTrainings = suggTrainings;
	}*/
	
	

}