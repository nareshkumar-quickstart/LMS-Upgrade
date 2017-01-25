/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author raja.ali
 * 2015/11/19
 * 
 */
@Entity
@DiscriminatorValue("CSQ")
public class TextBoxSurveyQuestion extends SurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = 518041103177039226L;
	
	@Transient
	private  Boolean singleLineResponse = Boolean.TRUE;
	

	public TextBoxSurveyQuestion() {
		super();
	}

	public TextBoxSurveyQuestion(Boolean isAnswerLimited) {
		super();
		this.singleLineResponse = isAnswerLimited;
	}

	public  Boolean isSingleLineResponse() {
		return singleLineResponse;
	}

	public void setSingleLineResponse(Boolean singleLineResponse) {
		this.singleLineResponse = singleLineResponse;
	}

	public SurveyQuestion getClone(){
		SurveyQuestion question = new TextBoxSurveyQuestion();
		question.setAnsFlag(super.getAnsFlag());
		question.setDisplayOrder(super.getDisplayOrder());
		question.setRequired(super.getRequired());
		question.setSurvey(super.getSurvey());
		question.setSurveyAnswerLines(super.getSurveyAnswerLines());
		question.setText(super.getText());
		((TextBoxSurveyQuestion)question).setSingleLineResponse(this.singleLineResponse);

		return question;
	}
}
