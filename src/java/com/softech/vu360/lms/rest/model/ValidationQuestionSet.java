package com.softech.vu360.lms.rest.model;

import java.util.List;

public class ValidationQuestionSet {
	private List<Question> questions;
	private String answer = "";
	
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
