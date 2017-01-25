package com.softech.vu360.lms.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.AlertRecipient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@Transactional
public class SurveyServiceTest extends SpringJUnitConfigAbstractTest {

	@Inject
	SurveyService surveyService;
	
//	@Test
//	public void SurveyFlagAndSurveyResult_getNotReviewedFlaggedSurveyResult2() {
//		//TODO: Until, SurveyOwner issue resolved!
//	}
	
	@Test
	public void deleteAlerts() {
		surveyService.deleteAlerts(new long[] {14303L, 14353L, 14403L, 14453L, 14503L});
	}
	
	@Test
	public void findAlertRecipient() {
		surveyService.findAlertRecipient("saaliz", AlertRecipient.Type.LEARNER.toString(), 14303L);
	}
	
}
