package com.softech.vu360.lms.model;

import java.sql.Date;

import javax.transaction.Transactional;

import org.junit.Test;


@Transactional
public class JPAEntityLearnerStatisticsTestServices  extends TestBaseDAO<Object>{

	
	//@Test
	public void testLearnerStatisticEntityUpdate() throws Exception {
		
		LearnerStatistic ls = new LearnerAssessmentResultStatistic();
			LearningSession lgs = new LearningSession();
			lgs.setBrandName("d");
			lgs.setCourseApprovalId(1L);
			lgs.setCourseId("1");
			lgs.setEnrollmentId(4343L);
			lgs.setExternalLMSSessionID("vv");
			lgs.setExternalLMSUrl("vv");
			lgs.setLearnerId(434L);
			lgs.setLearningSessionID("bvb");
			lgs.setLmsProvider(34);
			lgs.setRedirectURI("v");
			lgs.setSessionEndDateTime(new Date(System.currentTimeMillis()));
			lgs.setSessionStartDateTime(new Date(System.currentTimeMillis()));
			lgs.setSource("ffffff");
			lgs.setUniqueUserGUID("WQWQWQWQWQW");
		ls.setLearningSession(lgs);
		
		ls = (LearnerStatistic) crudSave(LearnerStatistic.class, ls);
		
		
		LearnerStatistic c = (LearnerStatistic) crudFindById(LearnerStatistic.class, ls.getId());
		update(c);
		
	}
	
	@Test
	public void testLearnerAttendanceSummaryStatisticEntity() throws Exception {
		
		LearnerAttendanceSummaryStatistic ls = new LearnerAttendanceSummaryStatistic();
			LearningSession lgs = new LearningSession();
			lgs.setBrandName("aaaaaaad");
			lgs.setCourseApprovalId(1L);
			lgs.setCourseId("1");
			lgs.setEnrollmentId(4343L);
			lgs.setExternalLMSSessionID("vv");
			lgs.setExternalLMSUrl("vv");
			lgs.setLearnerId(434L);
			lgs.setLearningSessionID("bvb");
			lgs.setLmsProvider(34);
			lgs.setRedirectURI("v");
			lgs.setSessionEndDateTime(new Date(System.currentTimeMillis()));
			lgs.setSessionStartDateTime(new Date(System.currentTimeMillis()));
			lgs.setSource("ffffff");
			lgs.setUniqueUserGUID("vvvv");
			
			
		ls.setLearningSession(lgs);
		ls.setMaxPercentCourseAttended( 2d );
		
		ls = (LearnerAttendanceSummaryStatistic) crudSave(LearnerAttendanceSummaryStatistic.class, ls);
		
		
		LearnerStatistic c = (LearnerStatistic) crudFindById(LearnerStatistic.class, ls.getId());
		update(c);
		
	}
}
