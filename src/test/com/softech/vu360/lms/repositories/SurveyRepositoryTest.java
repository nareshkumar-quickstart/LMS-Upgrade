package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyOwner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(defaultRollback=true, transactionManager="transactionManager")
@Transactional
public class SurveyRepositoryTest {
	
	@Inject
	private SurveyRepository surveyRepository;
	@Inject
	private CustomerRepository customerRepository;
	
	private static final String MY_SURVEY = "MySurvey";

	@Test
	public void Survey_should_findByName() {
		surveyRepository.findFirstByName(MY_SURVEY);
	}
	
//	@Test
//	public void Survey_should_deleteByQuestionsSurveyAnswers() {
//				
//	}
	
	@Test
	public void Survey_should_findBySurveyOwner() {
		Survey survey = surveyRepository.findOne(1L);
		SurveyOwner owner = survey.getOwner();
		List<Survey> lists = surveyRepository.findByOwnerAndIsLockedFalse(owner);
		Survey someSurvey = surveyRepository.findByOwnerId(54156L);
		if(lists.size() > 0) {
			lists.get(0).setOwner(someSurvey.getOwner());
			surveyRepository.save(lists.get(0));
		}
	}

	@Test
	public void Survey_should_findByCoursesInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerIdAndNameLike() {
		surveyRepository.findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerIdAndNameLike(new Long[] {1L, 2L, 3L}, Survey.PUBLISHED, 1L, 1L, "Some survey");
	}
	
	@Test
	public void Survey_should_findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerId() {
		surveyRepository.findByCoursesIdInAndIsLockedIsFalseAndStatusAndOwnerIdOrOwnerId(new Long[] {1L, 2L, 3L}, Survey.PUBLISHED, 1L, 1L);
	}
	
	@Test
	public void Survey_should_GetCurrentSurveyListByCoursesForUser() {
		surveyRepository.getCurrentSurveyListByCoursesForUser(8055L, 28405L);
	}
	
	@Test
	public void Survey_should_isAlertQueueRequiredProctorApproval() {
		surveyRepository.isAlertQueueRequiredProctorApproval(1L, "A");
	}

	@Test
	public void Survey_should_getSurveysByOwnerAndSurveyName() {
		Survey survey = surveyRepository.findOne(901L);
		String surveyName = survey.getName();
		String surveyStatus = survey.getStatus();
		String isLocked = "All";
		if(survey.getIsLocked()!=null){
			if(survey.getIsLocked()==Boolean.TRUE){
				isLocked = "Yes";
			}else{
				isLocked = "No";
			}
		}
		String readonly = "All";
		if(survey.isReadonly()!=null){
			if(survey.isReadonly()==Boolean.TRUE){
				isLocked = "Yes";
			}else{
				isLocked = "No";
			}
		}
		SurveyOwner surveyowner = survey.getOwner();
		String event=Survey.SURVEY_EVENT_MANUAL_CODE;
		List<Survey> aList = surveyRepository.getSurveysByOwnerAndSurveyName(surveyowner, surveyName, surveyStatus, isLocked, readonly,event);
		System.out.println(aList);
	}

	@Test
	public void Survey_should_findManualSurveys(){
		Survey survey = surveyRepository.findOne(901L);
		String surveyName = survey.getName();
		String surveyStatus = survey.getStatus();
		String retiredSurvey = "0";
		if(survey.getIsLocked()!=null){
			if(survey.getIsLocked()==Boolean.TRUE){
				retiredSurvey = "1";
			}else{
				retiredSurvey = "0";
			}
		}
		
		Customer customer=customerRepository.findOne(1403L);
		Long[] ids=new Long[2];
		ids[0]=customer.getId();
		ids[1]=customer.getDistributor().getId();
		int intLimit=10;
		List<Survey> allSurveys = surveyRepository.findManualSurveys(surveyName , surveyStatus ,  retiredSurvey, ids, intLimit );
		System.out.println(allSurveys);
	}
	
	//Added By Marium Saud
	@Test
	public void Survey_should_findSurvey_IsLocked_IsFalse(){
		List<Survey> surveyList=surveyRepository.findByIsLockedIsFalse();
		System.out.println(surveyList.size());
		
	}
	
}