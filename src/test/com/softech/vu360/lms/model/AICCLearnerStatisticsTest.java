package com.softech.vu360.lms.model;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class AICCLearnerStatisticsTest extends
		TestBaseDAO<AICCLearnerStatistics> {


	@Test
	public void AICCLearnerStatistics_should_save() throws Exception {

		AICCLearnerStatistics aiccStats = new AICCLearnerStatistics();
		
		AICCLearnerStatistics updateRecord=getById(new Long(2), AICCLearnerStatistics.class);

		aiccStats.setLearnerId(updateRecord.getLearnerId());
		aiccStats.setLearnerEnrollmentId(updateRecord.getLearnerEnrollmentId());
		aiccStats.setCreateDate(new Date());
		aiccStats.setCredit("");
		aiccStats.setLastUpdateDate(new Date());
		aiccStats.setLessonLocation("");
		aiccStats.setStatus("I");
		aiccStats.setTotalTimeInSeconds(0);
		try {

			aiccStats = save(aiccStats);
		} catch (Exception ex) {
			System.out.println(aiccStats.getId());
		}

	}


}
