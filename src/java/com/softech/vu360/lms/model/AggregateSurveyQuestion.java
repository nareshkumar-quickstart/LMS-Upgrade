/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * @author raja.ali
 * 2015/11/19
 * 
 */
@Entity
@PrimaryKeyJoinColumn(name="ID") 
@DiscriminatorValue("ASQ")
public class AggregateSurveyQuestion extends SurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = -3231858975501300829L;

	public SurveyQuestion getClone(){
		
		SurveyQuestion question = new AggregateSurveyQuestion();
		question.setAnsFlag(super.getAnsFlag());
		question.setDisplayOrder(super.getDisplayOrder());
		question.setRequired(super.getRequired());
		question.setSurvey(super.getSurvey());
		question.setSurveyAnswerLines(super.getSurveyAnswerLines());
		question.setText(super.getText());
		
		return question;
	}

}
