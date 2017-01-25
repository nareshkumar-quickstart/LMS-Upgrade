package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SurveySuggestedCourse  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Survey survey = null;
	private List<LearnerCourse> learnerCourses = new ArrayList<LearnerCourse>();
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public List<LearnerCourse> getLearnerCourses() {
		return learnerCourses;
	}
	public void setLearnerCourses(List<LearnerCourse> learnerCourses) {
		this.learnerCourses = learnerCourses;
	}
	
}
