package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.LearnerAttendanceSummaryStatistic;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;

public class LearnerAttendanceSummaryStatisticRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private LearnerAttendanceSummaryStatisticRepository learnerAttendanceSummaryStatisticRepository;



	
	@Test
	public void LearnerAttendanceSummaryStatistic_should_getByLearningSessionId() {
		List<LearnerAttendanceSummaryStatistic> result=learnerAttendanceSummaryStatisticRepository.findByLearningSessionLearningSessionID("D73C51B6-91AB-BCA0-9719C1BE9F4B1614");
		System.out.println("LearnerAttendanceSummaryStatistic LearningSession LearningSessionID  = "+result.size());
	}
	
}
