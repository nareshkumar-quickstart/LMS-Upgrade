/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerAssessmentResultStatistic;


/**
 * @author marium.saud
 *
 */
public interface LearnerAssessmentResultStatisticRepository extends CrudRepository<LearnerAssessmentResultStatistic, Long> {
	
	List<LearnerAssessmentResultStatistic> findByLearningSessionLearningSessionID(String learningSessionID);

}
