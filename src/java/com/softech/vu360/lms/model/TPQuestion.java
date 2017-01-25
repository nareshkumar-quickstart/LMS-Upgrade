package com.softech.vu360.lms.model;
import java.util.ArrayList;
import java.util.List;

public class TPQuestion {

	private long id;
	private String question;
	private Integer displayOrder=0;
	private  Boolean hasAnswer=false;
	private List<TPAnswer> answers = new ArrayList<TPAnswer>();
	
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public  Boolean isHasAnswer() {
		return hasAnswer;
	}
	public void setHasAnswer(Boolean hasAnswer) {
		this.hasAnswer = hasAnswer;
	}
	public List<TPAnswer> getAnswers() {
		if(answers.size()==0)
			answers.add(new TPAnswer());
		return answers;
	}
	public void setAnswers(List<TPAnswer> answers) {
		this.answers = answers;
	}
	
	
	public  Boolean getAnswerText(String answerText){
		
		 Boolean found=false;
		for(TPAnswer answer : this.answers){
			if(answer.getAnswerText()!=null && answer.getAnswerText().contains(answerText)){
				found = true;
				break;
			}
				
		}
		return found;
	}
	
	

	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	
	
	
	
	
}
