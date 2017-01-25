package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.SurveyLink;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class SurveyLinkRepositoryTest {

	@Inject
	SurveyLinkRepository surveyLinkRepository;
	
	@Test
	public void SurveyLink_should_getSurveyLinksById() {
		List<SurveyLink> links = surveyLinkRepository.findBySurveyId(1L);
	}
	
}
