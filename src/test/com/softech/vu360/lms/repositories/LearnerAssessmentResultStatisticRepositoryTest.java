package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.LearnerAssessmentResultStatistic;
import com.softech.vu360.lms.model.LearnerAttendanceSummaryStatistic;
import com.softech.vu360.lms.model.LearnerTimeSpentStatistic;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;

public class LearnerAssessmentResultStatisticRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private LearnerAssessmentResultStatisticRepository learnerAssesmentResultStatisticRepository;



	
	@Test
	public void LearnerAssessmentResultStatistic_should_getByLearningSessionId() {
		List<LearnerAssessmentResultStatistic> result=learnerAssesmentResultStatisticRepository.findByLearningSessionLearningSessionID("D73C51B6-91AB-BCA0-9719C1BE9F4B1614");
		System.out.println("LearnerAssessmentResultStatistic LearningSession LearningSessionID  = "+result.size());
	}
	
}
