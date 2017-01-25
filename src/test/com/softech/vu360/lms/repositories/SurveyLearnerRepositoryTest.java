package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.model.VU360User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@Transactional
public class SurveyLearnerRepositoryTest {

	@Inject
	private SurveyLearnerRepository surveyLearnerRepository;
	
	@Inject
	private SurveyRepository surveyRepository;
	
	@Inject
	private LearnerRepository learnerRepository;

//	Caused by: java.lang.IllegalArgumentException: This class [class com.softech.vu360.lms.model.SurveyLearner] does not define an IdClass
/*	@Test
	public void SurveyLearner_should_findByLearnerIdInAndSurveyIsLockedIsFalse() {
		List<Learner> learners = new ArrayList<Learner>();
		learners.add(learnerRepository.findByCustomerId(1L));
		learners.add(learnerRepository.findByCustomerId(12L));
		List<SurveyLearner> surveyLearners = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalse(learners);
		Assert.assertNotNull(surveyLearners);
	}
*/	
//	@Test
	public void getManagerOfSurveyLearner() {
		Learner learner = learnerRepository.findOne(2L);
		Survey survey = surveyRepository.findOne(10561L);

		List<SurveyLearner> surveyLearnerList = surveyLearnerRepository.findByLearnerAndSurvey(learner, survey);

		VU360User manager = null;
		if( surveyLearnerList != null && surveyLearnerList.size() > 0 ){
			manager = surveyLearnerList.get(0).getUserToNotify();
		}
		System.out.println(manager.getEmailAddress());
	}
	
//	@Test
	public void getSurveyAssignmentOfLearners() {
		Learner learner1 = learnerRepository.findOne(2L);
		Learner learner2 = learnerRepository.findOne(3L);
		List<Learner> learners=new ArrayList<>(2);
		learners.add(learner1);
		learners.add(learner2);
		String search="survey";
		List<SurveyLearner> surveyLearnerList = null;
		if(StringUtils.isBlank(search))
		{
			surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalse(learners);
		}else{
			surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIsLockedIsFalseAndSurveyNameLike( learners, "%"+search+"%" );
		}
		if(surveyLearnerList!=null)
			System.out.println(surveyLearnerList.size());
	}

	@Test
	public void SurveyLearner_should_findBySurveyOwnerAndSurveyIsInspectionIsTrue() {
		Survey survey = surveyRepository.findOne(1L);
		surveyLearnerRepository.findBySurveyOwnerAndSurveyIsInspectionIsTrue(survey.getOwner());
	}
	
	//Added By Marium Saud
	@Test
	public void SurveyLearner_should_findByLearnersInAndSurveyIn() {
		Learner learner1 = learnerRepository.findOne(2L);
		Learner learner2 = learnerRepository.findOne(3L);
		List<Learner> learners=new ArrayList<>(2);
		learners.add(learner1);
		learners.add(learner2);
		Survey survey=surveyRepository.findOne(10561L);
		List<Survey> surveyList= new ArrayList<Survey>();
		surveyList.add(survey);
		
		List<SurveyLearner> surveyLearnerList = null;
		surveyLearnerList = surveyLearnerRepository.findByLearnerInAndSurveyIn(learners,surveyList);
		if(surveyLearnerList!=null)
			System.out.println(surveyLearnerList.size());
			for (SurveyLearner sl : surveyLearnerList){
				System.out.println(sl.getLearner());
				System.out.println(sl.getSurvey());
			}
	}
}