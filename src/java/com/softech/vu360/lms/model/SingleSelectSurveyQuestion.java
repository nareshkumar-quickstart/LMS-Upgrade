package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author raja.ali
 * 2015/11/19
 * 
 */
@Entity
@DiscriminatorValue("SSSQ")
public class SingleSelectSurveyQuestion extends MutlipleChoiceSurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = -1866793440447173948L;
	
	@Column(name = "DROPDOWNTF")
	private  Boolean isDropdown = Boolean.FALSE;

	public SingleSelectSurveyQuestion() {
		super();
	}

	public SingleSelectSurveyQuestion(Boolean isDropdown) {
		super();
		this.isDropdown = isDropdown;
	}

	public  Boolean isDropdown() {
		return isDropdown;
	}

	public void setDropdown(Boolean isDropdown) {
		this.isDropdown = isDropdown;
	}
	
	public SurveyQuestion getClone(){
		SurveyQuestion question = new SingleSelectSurveyQuestion();
		question.setAnsFlag(super.getAnsFlag());
		question.setDisplayOrder(super.getDisplayOrder());
		question.setRequired(super.getRequired());
		question.setSurvey(super.getSurvey());
		question.setSurveyAnswerLines(super.getSurveyAnswerLines());
		question.setText(super.getText());
		
		((SingleSelectSurveyQuestion)question).isDropdown=this.isDropdown;
		return question;
	}
}
