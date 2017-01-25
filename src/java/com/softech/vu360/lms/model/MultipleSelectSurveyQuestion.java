/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author raja.ali
 * 2015/11/19
 * 
 */
@Entity
@DiscriminatorValue("MSSQ")
public class MultipleSelectSurveyQuestion extends MutlipleChoiceSurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = 5029300431559762875L;

	public MultipleSelectSurveyQuestion() {
		super();
	}
	
	public SurveyQuestion getClone(){
		SurveyQuestion question = new MultipleSelectSurveyQuestion();
		question.setAnsFlag(super.getAnsFlag());
		question.setDisplayOrder(super.getDisplayOrder());
		question.setRequired(super.getRequired());
		question.setSurvey(super.getSurvey());
		question.setSurveyAnswerLines(super.getSurveyAnswerLines());
		question.setText(super.getText());
		
		((MutlipleChoiceSurveyQuestion)question).setAlignment(super.getAlignment());
		return question;
	}
}
