package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.VU360User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class SurveyResultRepositoryTest {

	private static final String SURVEY_NAME_LIKE = "Survey 4.2";

	private static final long SURVEY_ID = 901L;

	private static final long COURE_ID = 1245L;

	private static final long USER_ID = 1725L;

	private static final long SURVEYRESULT_ID = 451L;
	
	@Inject
	private SurveyResultRepository surveyResultRepository;
	@Inject
	private VU360UserRepository vu360Repository;
	@Inject
	private CourseRepository courseRepository;
	@Inject
	private SurveyRepository surveyRepository;
	
	
	@Test
	public void SurveyResult_should_findOne() {
		surveyResultRepository.findOne(SURVEYRESULT_ID);
	}
	
	@Test
	public void SurveyResult_should_findOneBySurveyeeAndCourseAndSurvey() {
		VU360User surveyee = vu360Repository.findOne(USER_ID);
		Course course = courseRepository.findOne(COURE_ID);
		Survey survey = surveyRepository.findOne(SURVEY_ID);
		SurveyResult result = surveyResultRepository.findOneBySurveyeeAndCourseAndSurvey(surveyee, course, survey);
	}
	
	@Test
	public void SurveyResult_should_findOneBySurveyeeAndSurvey() {
		VU360User surveyee = vu360Repository.findOne(USER_ID);
		Survey survey = surveyRepository.findOne(SURVEY_ID);
		SurveyResult result = surveyResultRepository.findOneBySurveyeeAndSurvey(surveyee, survey);
	}
	
	@Test
	public void SurveyResult_should_findOneBySurveyeeAndCourseIsNullAndSurvey() {
		VU360User surveyee = vu360Repository.findOne(USER_ID);
		Survey survey = surveyRepository.findOne(SURVEY_ID);
		SurveyResult result = surveyResultRepository.findOneBySurveyeeAndCourseIsNullAndSurvey(surveyee, survey);
	}
	
	@Test
	public void SurveyResult_should_findOneBySurveyeeAndCourseIsNullAndSurveyAndSurveyNameIsLike() {
		VU360User surveyee = vu360Repository.findOne(USER_ID);
		Survey survey = surveyRepository.findOne(SURVEY_ID);
		String surveyNameLike = SURVEY_NAME_LIKE;
		SurveyResult result = surveyResultRepository.findOneBySurveyeeAndCourseIsNullAndSurveyAndSurveyNameIsLike(surveyee, survey, surveyNameLike);
	}

	@Test
	public void SurveyResult_should_countBySurveyId() {
		Integer count = surveyResultRepository.countBySurveyId(1L);
		Assert.assertNotNull(count);
	}

	@Test
	public void SurveyResult_should_countByAnsweredQuestion() {
		Integer count = surveyResultRepository.countByAnsweredQuestion(1L, 1L, 1L);
		Assert.assertNotNull(count);
	}
	
	@Test
	public void SurveyResult_should_countByQuestionsAnswered() {
		Integer count = surveyResultRepository.countByQuestionsAnswered(1L, 1L, 1L);
		Assert.assertNotNull(count);
	}
	
	@Test
	public void SurveyResult_should_countByAnswer() {
		Integer count = surveyResultRepository.countByAnswer(1L, 1L, 1L, 1L);
		Assert.assertNotNull(count);
	}

	@Test
	public void SurveyResult_should_findOneBySurveyeeAndCourseAndSurveyAndSurveyNameLike() {
		VU360User surveyee = vu360Repository.findOne(USER_ID);
		Course course = courseRepository.findOne(COURE_ID);
		Survey survey = surveyRepository.findOne(SURVEY_ID);
		SurveyResult result = surveyResultRepository.findOneBySurveyeeAndCourseAndSurveyAndSurveyNameLike(surveyee, course, survey,"%survey%");
		System.out.println(result.getId());
	}
}
