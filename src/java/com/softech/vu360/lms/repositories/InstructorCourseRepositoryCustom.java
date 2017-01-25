package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorCourse;

public interface InstructorCourseRepositoryCustom {

	List<InstructorCourse> findCourseInstructorByCoursefirstNamelastNameIntructorType(Long courseId, String firstName, String lastName, String instructorType, int pageIndex, int pageSize, int sortDirection, String sortColumn);
}
