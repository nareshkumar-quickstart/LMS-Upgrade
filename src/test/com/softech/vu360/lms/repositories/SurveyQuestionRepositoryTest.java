package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import com.softech.vu360.lms.model.SurveyQuestion;

public class SurveyQuestionRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private SurveyQuestionRepository surveyQuestionRepository;

	// @Test
	public void findBySurveyId() {
		Long surveyId = 1L;
		List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findBySurveyId(surveyId);
		System.out.println(surveyQuestions);
	}

}
