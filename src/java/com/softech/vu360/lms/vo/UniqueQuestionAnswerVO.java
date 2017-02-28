package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class UniqueQuestionAnswerVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String question;
	private String questionType;
	private String id;
	private long questionId;
	private long answerId;
	private long learnerId;
	private String answer = null;
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
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public long getAnswerId() {
		return answerId;
	}
	public void setAnswerId(long answerId) {
		this.answerId = answerId;
	}
	public long getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	
}
