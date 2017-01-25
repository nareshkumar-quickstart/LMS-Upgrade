/**
 * 
 */
package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.web.controller.model.instructor.InstructorItemForm;

/**
 * @author sana.majeed
 *
 * InstructorService defines a set of interfaces
 * to control the business logic for interactions with the
 * Instructor within LMS. Specially for Instructor Association with Courses
 */
public interface InstructorService {
		
	public Map<Object, Object> getInstructorsForCourseAssociation ( Long courseId, String firstName, String lastName, Long contentOwnerId, int pageIndex, int pageSize, int sortDirection, String sortColumn );
	public List<InstructorItemForm> getInstructorItemListFromInstructors ( List<Instructor> instructorList );
	public List<InstructorCourse> associateCourseInstructors ( Long courseId, List<InstructorItemForm> instructorList );
	public Map<Object, Object> getAssociatedCourseInstructors ( Long courseId, String firstName, String lastName, String instructorType, int pageIndex, int pageSize, int sortDirection, String sortColumn );
	public List<InstructorItemForm> getInstructorItemListFromCourseInstructors ( List<InstructorCourse> courseInstructorList );
	public InstructorCourse getCourseInstructorInfoById (Long Id);
	public boolean updateCourseInstructorRole (Long courseInstructorId, String instructorType);	
	public boolean deleteCourseInstructors(Long[] courseInstructorIds);
}
