package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerSCOStatistics;

/**
 * 
 * @author haider
 *
 */
public interface LearnerSCOStatisticsRepository extends CrudRepository<LearnerSCOStatistics, Long>{

	public List<LearnerSCOStatistics> findByLearnerEnrollmentIdIn(Collection<Long> learnerEnrollmentsIds);
	public List<LearnerSCOStatistics> findByLearnerIdAndLearnerEnrollmentId(Long learnerid, Long learnerEnrollmentId);
}
