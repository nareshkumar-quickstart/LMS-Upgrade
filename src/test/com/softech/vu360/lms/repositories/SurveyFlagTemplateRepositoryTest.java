package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyFlagTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(defaultRollback=true, transactionManager="transactionManager")
@Transactional
public class SurveyFlagTemplateRepositoryTest {

	@Inject
	private SurveyRepository surveyRepository;
	@Inject
	private SurveyFlagTemplateRepository surveyFlagTemplateRepository;
	
	@Test
	public void SurveyFlagTemplate_should_findBySurveyId() {
		Survey firstSurvey = surveyRepository.findOne(7574L);
		List<SurveyFlagTemplate> surveyFlagTemplateList = surveyFlagTemplateRepository.findBySurveyId(firstSurvey.getId());
		Assert.assertNotNull(surveyFlagTemplateList);
	}
	
	@Test
	public void SurveyFlagTemplate_should_findByFlagNameLikeAndSurveyId() {
		List<SurveyFlagTemplate> surveyFlagTemplates = surveyFlagTemplateRepository.findByFlagNameIgnoreCaseLikeAndSurveyId("%aaaa%", 7574L);
		Assert.assertNotNull(surveyFlagTemplates);
	}
	
	@Test
	public void SurveyFlagTemplate_should_deleteByIdIn() {
		surveyFlagTemplateRepository.deleteByIdIn(new Long[] {2L, 3L});
	}
}
