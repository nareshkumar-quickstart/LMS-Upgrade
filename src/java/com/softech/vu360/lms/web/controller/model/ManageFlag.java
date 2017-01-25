package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.SurveyFlagTemplate;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class ManageFlag  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private SurveyFlagTemplate surveyFlagTemplate;
	private String questionText = null;
	
	
	/**
	 * @return the surveyFlagTemplate
	 */
	public SurveyFlagTemplate getFlag() {
		return surveyFlagTemplate;
	}
	/**
	 * @param surveyFlagTemplate the surveyFlagTemplate to set
	 */
	public void setFlag(SurveyFlagTemplate surveyFlagTemplate) {
		this.surveyFlagTemplate = surveyFlagTemplate;
	}
	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}
	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

}
