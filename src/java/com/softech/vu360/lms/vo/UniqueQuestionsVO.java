package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class UniqueQuestionsVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String question;
	private String questionType;
	private String id;
	private int questionId;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
}
