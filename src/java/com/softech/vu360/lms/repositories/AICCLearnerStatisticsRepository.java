package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AICCLearnerStatistics;

public interface AICCLearnerStatisticsRepository extends CrudRepository<AICCLearnerStatistics, Long> {
	AICCLearnerStatistics findTop1ByLearnerIdAndLearnerEnrollmentId(Long learnerId,Long learnerEnrollmentId);
}
