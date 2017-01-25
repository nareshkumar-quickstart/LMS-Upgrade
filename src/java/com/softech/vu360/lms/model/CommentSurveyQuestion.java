package com.softech.vu360.lms.model;

/**
 * @author Ashis
 * 
 */
public class CommentSurveyQuestion extends SurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = 1L;
	private  Boolean isAnswerLimited = Boolean.FALSE;

	public CommentSurveyQuestion() {
		super();
	}

	public CommentSurveyQuestion(Boolean isAnswerLimited) {
		super();
		this.isAnswerLimited = isAnswerLimited;
	}

	public  Boolean isAnswerLimited() {
		return isAnswerLimited;
	}

	public void setAnswerLimited(Boolean isAnswerLimited) {
		if(isAnswerLimited==null){
			this.isAnswerLimited=Boolean.FALSE;
		}
		else{
			this.isAnswerLimited = isAnswerLimited;
		}
	}

	public SurveyQuestion getClone(){		
		SurveyQuestion question = new CommentSurveyQuestion();
		question.setAnsFlag(super.getAnsFlag());
		question.setDisplayOrder(super.getDisplayOrder());
		question.setRequired(super.getRequired());
		question.setSurvey(super.getSurvey());
		question.setSurveyAnswerLines(super.getSurveyAnswerLines());
		question.setText(super.getText());
		((CommentSurveyQuestion)question).setAnswerLimited(this.isAnswerLimited);
		return question;
	}
}
