package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearnerCourseStatisticsRepositoryTest {
	

	@Inject
	private LearnerCourseStatisticsRepository learnerCourseStatisticsRepository;
	@Inject
	private LearnerEnrollmentRepository learnerEnrollmentRepository;
	
	//@Test
	public void findByLearnerEnrollmentLearnerIdAndLearnerEnrollmentEnrollmentStatusNotIn(){
		
		List<String> lst = new ArrayList();
		
		lst.add(LearnerEnrollment.DROPPED);
		lst.add(LearnerEnrollment.SWAPPED);
		
		
		
		List<LearnerCourseStatistics> ls = learnerCourseStatisticsRepository.findByLearnerEnrollmentLearnerIdAndLearnerEnrollmentEnrollmentStatusNotIn(105L, lst);
		
		for(LearnerCourseStatistics obj : ls){
			System.out.println("**************** ID = "+obj.getId());
		}
		
		
		
	}
	
	//@Test
	public void findByLearnerEnrollmentCourseIdInAndCompletedTrueAndCompletionDateBetweenAndLearnerEnrollmentLearnerCustomerDistributorSelfReportingFalse(){
		
		List<Long> courseIds = new ArrayList();
		courseIds.add(2962L);
		courseIds.add(5713L);
		courseIds.add(6004L);
		
		/*5084
		2962
		5226
		5713
		2494
		5861
		5997
		5996
		6004
		5861*/
		
		Date startDate = new Date();
		startDate.setYear(85);
		Date endDate = new Date();
		
		
		
		List<LearnerCourseStatistics> ls = learnerCourseStatisticsRepository.findByLearnerEnrollmentCourseIdInAndCompletedTrueAndCompletionDateBetweenAndLearnerEnrollmentLearnerCustomerDistributorSelfReportingFalse(courseIds, startDate, endDate);
		
		for(LearnerCourseStatistics obj : ls){
			System.out.println("**************** ID = "+obj.getId());
		}
		
		
		
	}
	
	//Added B y Marium Saud
//	@Test
	public void LearnerCourseStatistics_should_getLearerCoursebyEnrolmentId(){
		
		List<LearnerCourseStatistics> ls = learnerCourseStatisticsRepository.findByLearnerEnrollmentIdEqualsAndStatusEqualsOrStatusEquals(334L, LearnerCourseStatistics.NOT_STARTED, LearnerCourseStatistics.IN_PROGRESS);
		for(LearnerCourseStatistics obj : ls){
			System.out.println("**************** ID = "+obj.getId());
		}
	}
	
	//@Test
	public void LearnerCourseStatistics_should_getLearnerEnrollment_By_LearnerId_And_CourseID(){
		LearnerCourseStatistics ls = learnerCourseStatisticsRepository.findFirstByLearnerEnrollmentLearnerIdAndLearnerEnrollmentCourseId(106L, 2L);
			System.out.println("**************** ID = "+ls.getId());
			
	}
	
//	@Test
	public void LearnerCourseStatistics_should_getLearnerEnrollmentID(){
		LearnerCourseStatistics ls = learnerCourseStatisticsRepository.findFirstByLearnerEnrollmentId(1L);
			System.out.println("**************** ID = "+ls.getId());
			
	}
	
	//Added By Marium Saud
	//@Test
	public void LearnerCourseStatistics_should_findByLearnerEnrollment(){
		LearnerEnrollment le=learnerEnrollmentRepository.findOne(16206L);
		LearnerCourseStatistics lcs=learnerCourseStatisticsRepository.findByLearnerEnrollment(le);
		System.out.println("**************** ID = "+lcs.getId());
	}
	@Test
	public void LearnerCourseStatistics_should_findByLearnerEnrollment1(){
		LearnerEnrollment le=learnerEnrollmentRepository.findOne(16206L);
		LearnerCourseStatistics lcs=learnerCourseStatisticsRepository.findByLearnerEnrollment(le);
		System.out.println("**************** ID = "+lcs.getId());
	}
}
