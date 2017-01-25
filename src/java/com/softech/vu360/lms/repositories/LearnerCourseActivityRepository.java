package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.Location;

public interface LearnerCourseActivityRepository extends CrudRepository<LearnerCourseActivity, Long>{

	public List<LearnerCourseActivity> findByCourseActivityGradeBookSynchronousClassId(Long synchronousClassId);
	public List<LearnerCourseActivity> findByCourseActivityId(Long courseActivityId);
	public List<LearnerCourseActivity> findByLearnerCourseStatisticsId(Long learnerCourseStatisticsId);
}
