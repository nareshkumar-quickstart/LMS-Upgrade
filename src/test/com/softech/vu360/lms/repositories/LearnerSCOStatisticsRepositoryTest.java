package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.StatisticsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearnerSCOStatisticsRepositoryTest {

	@Inject
	private LearnerSCOStatisticsRepository learnerSCOStatsRepository;
	
	
	
	@Test
	public void LearnerSCOStatistics_should_find_By_LearnerID_And_LearnerEnrollmentID(){
		List<LearnerSCOStatistics> result=learnerSCOStatsRepository.findByLearnerIdAndLearnerEnrollmentId(1L, 1L);
		for(LearnerSCOStatistics stats:result){
			System.out.println("LearnerSCOStatisticsId is " +stats.getId());
		}
		
	}

	
}
