package com.softech.vu360.lms.web.controller.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.vo.SurveySectionVO;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class Survey implements ILMSBaseInterface, Serializable {

	private static final long serialVersionUID = 6241265806480730731L;
	
	private com.softech.vu360.lms.model.Survey surveyRef;
	private Survey aggrSurveyRef;
	private List<SurveySectionVO> surveySections = new ArrayList<SurveySectionVO>();

	public Survey(com.softech.vu360.lms.model.Survey surveyRef) {
		super();
		this.surveyRef = surveyRef;
		//this.aggrSurveyRef = new Survey(surveyRef);
	}
	
	private List<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionList = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>();

	/**
	 * @return the questionList
	 */
	public List<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> getQuestionList() {
		return questionList;
	}

	/**
	 * @param questionList the questionList to set
	 */
	public void setQuestionList(
			List<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionList) {
		this.questionList = questionList;
	}

	public void addQuestion(com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question){
		if(this.questionList==null)
			questionList = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>();
		this.questionList.add(question);
	}
	
	/**
	 * @return the surveyRef
	 */
	public com.softech.vu360.lms.model.Survey getSurveyRef() {
		return surveyRef;
	}

	/**
	 * @param surveyRef the surveyRef to set
	 */
	public void setSurveyRef(com.softech.vu360.lms.model.Survey surveyRef) {
		this.surveyRef = surveyRef;
	}

	/**
	 * @return the aggrSurveyRef
	 */
	public Survey getAggrSurveyRef() {
		return aggrSurveyRef;
	}

	/**
	 * @param aggrSurveyRef the aggrSurveyRef to set
	 */
	public void setAggrSurveyRef(Survey aggrSurveyRef) {
		this.aggrSurveyRef = aggrSurveyRef;
	}

	public List<SurveySectionVO> getSurveySections() {
		return surveySections;
	}

	public void setSurveySections(List<SurveySectionVO> surveySections) {
		this.surveySections = surveySections;
	}
	
}
