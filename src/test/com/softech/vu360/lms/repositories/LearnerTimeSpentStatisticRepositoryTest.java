package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.LearnerAttendanceSummaryStatistic;
import com.softech.vu360.lms.model.LearnerTimeSpentStatistic;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;

public class LearnerTimeSpentStatisticRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private LearnerTimeSpentStatisticRepository learnerTimeSpentStatisticRepository;



	
	@Test
	public void LearnerTimeSpentStatistic_should_getByLearningSessionId() {
		List<LearnerTimeSpentStatistic> result=learnerTimeSpentStatisticRepository.findByLearningSessionLearningSessionID("D7414C46-0A97-24E5-23E09C88618B9E66");
		System.out.println("LearnerTimeSpentStatistic LearningSession LearningSessionID  = "+result.size());
	}
	
}
