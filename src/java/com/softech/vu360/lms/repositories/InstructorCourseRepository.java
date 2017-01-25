package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorCourse;

public interface InstructorCourseRepository extends CrudRepository<InstructorCourse, Long>,InstructorCourseRepositoryCustom{
	
	
	public List<InstructorCourse> findByIdIn(Long[] id);
	public List<InstructorCourse> findByCourseId(Long courseId);
	public List<InstructorCourse> findByCourseIdAndInstructorType(Long courseId,String instType);
	public List<InstructorCourse> findByInstructorIdEqualsAndCourseCourseMediaTypeEqualsIgnoreCaseAndCourseRetiredFalseOrderByCourseCourseTitleAsc(Long instructorId,String courseMediaType);
	
	
	
	

}
