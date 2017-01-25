package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.SurveyReviewComment;
import com.softech.vu360.lms.repositories.SurveyReviewCommentRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class SurveyReviewCommentRepositoryTest {

	private static final long REVIEW_ID = 2L;
	private static final long ANSWER_ID = 12373L;
	
	@Inject
	private SurveyReviewCommentRepository surveyReviewCommentRepository;
	
	@Test
	public void SurveyReviewComment_should_findOne() {
		surveyReviewCommentRepository.findOne(REVIEW_ID);
	}
	
	@Test
	public void SurveyReviewComment_should_findByAnswerId() {
		SurveyReviewComment review = surveyReviewCommentRepository.findByAnswerId(ANSWER_ID);
		System.out.print(review);
	}
	
}
