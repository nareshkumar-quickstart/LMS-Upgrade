package com.softech.vu360.lms.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;


@Transactional
public class JPAEntityLearnerSCOStatisticsTestService  extends TestBaseDAO<Object>{

	
	@Test
	public void testLearnerSCOStatisticsEntityUpdate() throws Exception {
		
		LearnerSCOStatistics b = new LearnerSCOStatistics();
		b.setCompletionStatus("d");
		b.setCreatedDate(new Date(System.currentTimeMillis()));
		b.setCredit("d");
		b.setEntry("dss");
		b.setExit("44");
		b.setLastUpdatedDate(new Date(System.currentTimeMillis()));
		b.setLearnerEnrollmentId(4742L);
		b.setLearnerId(472L);
		b.setPostAssessmentTaken(false);
		b.setProgressMeasure(84D);
		b.setScoLocation("434");
		b.setSessionTimeInSeconds(883L);
		b.setSupsendData("dpd");
		b.setTotalTimeInSeconds(622);
		b = (LearnerSCOStatistics) crudSave(LearnerSCOStatistics.class, b);

		
		LearnerSCOStatistics c = (LearnerSCOStatistics) crudFindById(LearnerSCOStatistics.class, b.getId());
		c.setCompletionStatus(LearnerSCOStatistics.COMPLETION_STATUS_INCOMPLETE);
		update(c);

		LearnerSCOStatistics lsco = (LearnerSCOStatistics) crudFindById(LearnerSCOStatistics.class, new Long(14852));
		 List<LearnerSCOAssessment> ls = lsco.getAssessments();
		 for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			LearnerSCOAssessment learnerSCOAssessment = (LearnerSCOAssessment) iterator.next();
			System.out.println("ddddddddddd"+learnerSCOAssessment);			
		}
		
	}
}
