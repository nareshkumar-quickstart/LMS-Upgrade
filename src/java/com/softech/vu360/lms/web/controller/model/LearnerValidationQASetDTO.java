package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.LearnerValidationAnswers;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class LearnerValidationQASetDTO  implements ILMSBaseInterface{

	private long learnerId;
	private long questionInSet1;
	private long questionInSet2;
	private long questionInSet3;
	private long questionInSet4;
	private long questionInSet5;

	private String answerForSet1;
	private String answerForSet2;
	private String answerForSet3;
	private String answerForSet4;
	private String answerForSet5;

	public long getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(long learnerId) {
		this.learnerId = learnerId;
	}
	public long getQuestionInSet1() {
		return questionInSet1;
	}
	public void setQuestionInSet1(long questionInSet1) {
		this.questionInSet1 = questionInSet1;
	}
	public long getQuestionInSet2() {
		return questionInSet2;
	}
	public void setQuestionInSet2(long questionInSet2) {
		this.questionInSet2 = questionInSet2;
	}
	public long getQuestionInSet3() {
		return questionInSet3;
	}
	public void setQuestionInSet3(long questionInSet3) {
		this.questionInSet3 = questionInSet3;
	}
	public long getQuestionInSet4() {
		return questionInSet4;
	}
	public void setQuestionInSet4(long questionInSet4) {
		this.questionInSet4 = questionInSet4;
	}
	public long getQuestionInSet5() {
		return questionInSet5;
	}
	public void setQuestionInSet5(long questionInSet5) {
		this.questionInSet5 = questionInSet5;
	}
	public String getAnswerForSet1() {
		return answerForSet1;
	}
	public void setAnswerForSet1(String answerForSet1) {
		this.answerForSet1 = answerForSet1;
	}
	public String getAnswerForSet2() {
		return answerForSet2;
	}
	public void setAnswerForSet2(String answerForSet2) {
		this.answerForSet2 = answerForSet2;
	}
	public String getAnswerForSet3() {
		return answerForSet3;
	}
	public void setAnswerForSet3(String answerForSet3) {
		this.answerForSet3 = answerForSet3;
	}
	public String getAnswerForSet4() {
		return answerForSet4;
	}
	public void setAnswerForSet4(String answerForSet4) {
		this.answerForSet4 = answerForSet4;
	}
	public String getAnswerForSet5() {
		return answerForSet5;
	}
	public void setAnswerForSet5(String answerForSet5) {
		this.answerForSet5 = answerForSet5;
	}

	public LearnerValidationQASetDTO getLearnerValidationQASetDTO(List<LearnerValidationAnswers> answers){
		LearnerValidationQASetDTO dto = new LearnerValidationQASetDTO();
		for(LearnerValidationAnswers answer:answers){
			
			switch ((int)answer.getQuestionId()){
			case 101:
			case 102:
			case 103:
				dto.setQuestionInSet1(answer.getQuestionId());
				dto.setAnswerForSet1(answer.getAnswer());

			case 104:
			case 105:
			case 106:
				dto.setQuestionInSet2(answer.getQuestionId());
				dto.setAnswerForSet2(answer.getAnswer());

			case 107:
			case 108:
			case 109:
				dto.setQuestionInSet3(answer.getQuestionId());	
				dto.setAnswerForSet3(answer.getAnswer());

			case 110:
			case 111:
			case 112:
				dto.setQuestionInSet4(answer.getQuestionId());
				dto.setAnswerForSet4(answer.getAnswer());

			case 113:
			case 114:
			case 115:
				dto.setQuestionInSet5(answer.getQuestionId());
				dto.setAnswerForSet5(answer.getAnswer());
			}
		}
		return dto;	
	}
	
	public List<LearnerValidationAnswers> getLearnerValidationAnswersList(){
		List<LearnerValidationAnswers> answers = new ArrayList<LearnerValidationAnswers>();
		LearnerValidationAnswers answer1, answer2, answer3, answer4, answer5;
		
		answer1 = new LearnerValidationAnswers();
		answer1.setAnswer(this.getAnswerForSet1());
		answer1.setSetId(1);
		answer1.setQuestionId(this.getQuestionInSet1());
		answers.add(answer1);

		answer2 = new LearnerValidationAnswers();
		answer2.setAnswer(this.getAnswerForSet2());
		answer2.setSetId(2);
		answer2.setQuestionId(this.getQuestionInSet2());
		answers.add(answer2);
		
		answer3 = new LearnerValidationAnswers();
		answer3.setAnswer(this.getAnswerForSet3());
		answer3.setSetId(3);
		answer3.setQuestionId(this.getQuestionInSet3());
		answers.add(answer3);
		
		answer4 = new LearnerValidationAnswers();
		answer4.setAnswer(this.getAnswerForSet4());
		answer4.setSetId(4);
		answer4.setQuestionId(this.getQuestionInSet4());
		answers.add(answer4);
		
		answer5 = new LearnerValidationAnswers();
		answer5.setAnswer(this.getAnswerForSet5());
		answer5.setSetId(5);
		answer5.setQuestionId(this.getQuestionInSet5());
		answers.add(answer5);
				
		return answers;
	}
}
