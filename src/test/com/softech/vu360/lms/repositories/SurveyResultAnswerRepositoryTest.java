package com.softech.vu360.lms.repositories;

import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.SurveyResultAnswer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class SurveyResultAnswerRepositoryTest {

	@Inject
	private SurveyResultAnswerRepository surveyResultAnswerRepository;
	
	@Test
	public void SurveyResultAnswer_should_findByQuestionId() {
		SurveyResultAnswer answer = surveyResultAnswerRepository.findFirstByQuestionId(1L);
	}
	
	@Test
	public void SurveyResultAnswer_should_findByQuestionIdAndSurveyQuestionBankIdAndSurveySectionIdAndSurveyResultSurveyeeId() {
		SurveyResultAnswer answer = surveyResultAnswerRepository.findByQuestionIdAndSurveyQuestionBankIdAndSurveySectionIdAndSurveyResultSurveyeeId(1L, 1L, 1L, 1L);
	}
	
	@Test
	public void SurveyResultAnswer_should_findByQuestionIdAndSurveyResultSurveyeeId() {
		surveyResultAnswerRepository.findByQuestionIdAndSurveyResultSurveyeeId(1L, 1L);
	}
}
