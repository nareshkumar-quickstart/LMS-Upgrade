package com.softech.vu360.lms.vo;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLearner;

/**
 * Value object for enrollment controller
 * @author Adeel
 * @since LMS-4383
 */
public class SurveyVO {
 
	   private List<SurveyLearner> learnerSurveyList  = new ArrayList<SurveyLearner>();
	   private List<Learner> learnerList  = new ArrayList<Learner>();
	   private List<Survey> surveyList  = new ArrayList<Survey>();
	      

	/**
	 * @param learnerSurveyList the learnerSurveyList to set
	 */
	public void setLearnerSurveyList(List<SurveyLearner> learnerSurveyList) {
		this.learnerSurveyList = learnerSurveyList;
	}

	/**
	 * @return the learnerSurveyList
	 */
	public List<SurveyLearner> getLearnerSurveyList() {
		return learnerSurveyList;
	}

	/**
	 * @return the learnerList
	 */
	public List<Learner> getLearnerList() {
		return learnerList;
	}

	/**
	 * @param learnerList the learnerList to set
	 */
	public void setLearnerList(List<Learner> learnerList) {
		this.learnerList = learnerList;
	}

	/**
	 * @return the surveyList
	 */
	public List<Survey> getSurveyList() {
		return surveyList;
	}

	/**
	 * @param surveyList the surveyList to set
	 */
	public void setSurveyList(List<Survey> surveyList) {
		this.surveyList = surveyList;
	}
 
    
        

}