/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerTimeSpentStatistic;

/**
 * @author marium.saud
 *
 */
public interface LearnerTimeSpentStatisticRepository extends CrudRepository<LearnerTimeSpentStatistic, Long> {
	
	List<LearnerTimeSpentStatistic> findByLearningSessionLearningSessionID(String learningSessionID);

}
