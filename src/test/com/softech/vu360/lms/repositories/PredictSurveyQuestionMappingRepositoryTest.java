package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class PredictSurveyQuestionMappingRepositoryTest {
	@Inject
	private PredictSurveyQuestionMappingRepository predictSurveyQuestionMappingRepository;
	
	//@Test
	public void findByLmsSurveyIdAndLmsQuestionId() {
		Long surveyId=26634L;
		Long questionId=32234L;
		PredictSurveyQuestionMapping entity = predictSurveyQuestionMappingRepository.findByLmsSurveyIdAndLmsQuestionId(surveyId, questionId);
		System.out.println(entity);
	}

	//@Test
	public void findBySurveySectionIdAndQuestionIdAndLmsSurveyIdOrderByIdDesc() {
		Long surveyId=26834L;
		Long questionId=698L;
		String sectionId="128";
		List<PredictSurveyQuestionMapping> entity = predictSurveyQuestionMappingRepository.findBySurveySectionIdAndQuestionIdAndLmsSurveyIdOrderByIdDesc(sectionId, questionId, surveyId);
		System.out.println(entity);
	}
	
	@Test
	public void findBySurveyIdAndLmsSurveyIdAndSurveySectionId(){
		Long predictSurveyId=48L;
		Long lmsSurveyId=26834L;
		String predictSectionId="128";
		PredictSurveyQuestionMapping entity = predictSurveyQuestionMappingRepository.
				findFirstBySurveyIdAndLmsSurveyIdAndSurveySectionIdOrderByClonesAsc(predictSurveyId, lmsSurveyId, predictSectionId);
		System.out.println(entity);
	}
	
	@Test
	public void PredictSurveyQuestionMapping_should_findByLmsSectionId() {
		predictSurveyQuestionMappingRepository.findByLmsSectionId(1L);
	}
	
	@Test
	public void PredictSurveyQuestionMapping_should_findByLmsFrameworkId() {
		predictSurveyQuestionMappingRepository.findByLmsFrameworkId(1L);
	}
	
	@Test
	public void PredictSurveyQuestionMapping_should_findBySurveySectionIdLikeAndQuestionIdAndLmsSurveyId() {
		predictSurveyQuestionMappingRepository.findBySurveySectionIdLikeAndQuestionIdAndLmsSurveyId("C_%_" + 1L, 1L, 1L);
	}
}
