package com.softech.vu360.lms.repositories;


import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LearnerValidationAnswers;


public class LearnerValidationAnswersRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private LearnerValidationAnswersRepository validationAnswerRepository;
	
	//@PersistenceContext
	//protected EntityManager entityManager;
	
	
//	@Test
	public void LearnerValidationAnswers_should_getLearnerById() {
		List<LearnerValidationAnswers> validationAnswerList = validationAnswerRepository.findByLearnerId(1034444L);
		System.out.println("LearnerValidationAnswersList size() = "+validationAnswerList.size());
		for (LearnerValidationAnswers answer: validationAnswerList){
			 
            System.out.println(answer.getId());

         }
	}
	
//	@Test
	public void LearnerValidationAnswers_should_save(){
		
		List<LearnerValidationAnswers> answers = validationAnswerRepository.findByLearnerId(1034444L);
		System.out.println("LearnerValidationAnswersList size() = "+answers.size());
		
		for (LearnerValidationAnswers answer: answers){
			 
			LearnerValidationAnswers answer1 = new LearnerValidationAnswers();
			answer1.setAnswer(answer.getAnswer());
			answer1.setSetId(answer.getSetId());
			answer1.setQuestionId(answer.getQuestionId());
			answer1.setLearner(answer.getLearner());
			answer1=validationAnswerRepository.save(answer1);
			System.out.println("LearnerValidationAnswer id is: "+answer1.getId());
         }
	}
	
	@Test
	public void LearnerValidationAnswers_should_getById(){
		
		LearnerValidationAnswers answer=validationAnswerRepository.findOne(1111L);
		System.out.println("LearnerValidationAnswer Question Id is:" +answer.getQuestionId());
		
	}
}
