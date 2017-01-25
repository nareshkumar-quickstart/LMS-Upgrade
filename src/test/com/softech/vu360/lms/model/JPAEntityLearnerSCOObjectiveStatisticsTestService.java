package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;


@Transactional
public class JPAEntityLearnerSCOObjectiveStatisticsTestService  extends TestBaseDAO<Object>{

	
	@Test
	public void testLearnerSCOObjectiveStatisticsEntityUpdate() throws Exception {
		
		LearnerSCOObjectiveStatistics b = new LearnerSCOObjectiveStatistics();
		b.setCompletionStatus("dd");
		b.setMaxScore(43D);
		b.setMinScore(4D);
		b.setProgressMeasure(4D);
		b.setRawScore(4D);
		b.setScaledScore(5D);
		b.setSuccessStatus("1");
		b.setLrnSCOStatisticID(15004L);
		b = (LearnerSCOObjectiveStatistics) crudSave(LearnerSCOObjectiveStatistics.class, b);

		
		LearnerSCOObjectiveStatistics c = (LearnerSCOObjectiveStatistics) crudFindById(LearnerSCOObjectiveStatistics.class, b.getId());
		c.setCompletionStatus("e");
		update(c);

	}
}
