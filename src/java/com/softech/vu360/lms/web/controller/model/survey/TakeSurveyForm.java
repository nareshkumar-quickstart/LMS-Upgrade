package com.softech.vu360.lms.web.controller.model.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class TakeSurveyForm  implements ILMSBaseInterface {
	
	private static final long serialVersionUID = -8871311023727351091L;
	
	private Long courseId;
	private Learner learner;
	private VU360User surveyee;
	private Map<String, List<com.softech.vu360.lms.model.SurveyResultAnswerFile>> answerFilesMap = new HashMap<String, List<com.softech.vu360.lms.model.SurveyResultAnswerFile>>();

	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	private Long surveyId;

	/**
	 * @return the surveyId
	 */
	public Long getSurveyId() {
		return surveyId;
	}

	/**
	 * @param surveyId the surveyId to set
	 */
	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
	
	private Survey survey;

	/**
	 * @return the survey
	 */
	public Survey getSurvey() {
		return survey;
	}

	/**
	 * @param survey the survey to set
	 */
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	
	
	private String learningSessionId; //For Survey Response View

	/**
	 * @return the learningSessionId
	 */
	public String getLearningSessionId() {
		return learningSessionId;
	}

	/**
	 * @param learningSessionId the learningSessionId to set
	 */
	public void setLearningSessionId(String learningSessionId) {
		this.learningSessionId = learningSessionId;
	}
	
	private List<com.softech.vu360.lms.model.Survey> surveys = new ArrayList<com.softech.vu360.lms.model.Survey>(); //For Survey Response View

	/**
	 * @return the surveys
	 */
	public List<com.softech.vu360.lms.model.Survey> getSurveys() {
		return surveys;
	}

	/**
	 * @param surveys the surveys to set
	 */
	public void setSurveys(List<com.softech.vu360.lms.model.Survey> surveys) {
		this.surveys = surveys;
	}

	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}

	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public VU360User getSurveyee() {
		return surveyee;
	}

	public void setSurveyee(VU360User surveyee) {
		this.surveyee = surveyee;
	}

	public Map<String, List<com.softech.vu360.lms.model.SurveyResultAnswerFile>> getAnswerFilesMap() {
		return answerFilesMap;
	}

	public void setAnswerFilesMap(
			Map<String, List<com.softech.vu360.lms.model.SurveyResultAnswerFile>> answerFilesMap) {
		this.answerFilesMap = answerFilesMap;
	}

}
