package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;

/**
 * 
 * @author haider
 *
 */
public interface LearnerSelfStudyCourseActivityRepository extends CrudRepository<LearnerSelfStudyCourseActivity, Long>{

	public List<LearnerSelfStudyCourseActivity> findByLearnerEnrollmentIdIn(Collection<Long> learnerEnrollmentsIds);
}
