/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;

/**
 * @author muhammad.junaid
 *
 */
public class SurveyLearnerPK implements Serializable{

	private static final long serialVersionUID = -6645970144056793007L;

	private Learner learner;
	private Survey survey;
	
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
}
