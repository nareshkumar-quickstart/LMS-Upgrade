/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerAttendanceSummaryStatistic;

/**
 * @author marium.saud
 *
 */
public interface LearnerAttendanceSummaryStatisticRepository extends CrudRepository<LearnerAttendanceSummaryStatistic, Long> {
	
	List<LearnerAttendanceSummaryStatistic> findByLearningSessionLearningSessionID(String learningSessionID);

}
