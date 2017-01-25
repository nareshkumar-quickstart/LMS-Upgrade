package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;




import com.softech.vu360.lms.model.TrainingPlanCourse;

public interface TrainingPlanCourseRepository extends CrudRepository<TrainingPlanCourse, Long> {
	public void deleteByIdIn(List<Long> trainingPlanCourses);
	
	public List<TrainingPlanCourse> findByTrainingPlanIdIn(long[] trainingPlanIds);
	public List<TrainingPlanCourse> findByIdIn(long[] trainingPlanCourseIds);
	public List<TrainingPlanCourse> findByTrainingPlanId(long trainingPlanId);
	public List<TrainingPlanCourse> findByIdNotInAndTrainingPlanId(long[] trainingPlanCourseIds,long trainingPlanId);
}
