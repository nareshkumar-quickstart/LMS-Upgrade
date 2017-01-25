/**
 * 
 */
package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.InstructorCourseRepository;
import com.softech.vu360.lms.repositories.InstructorRepository;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.InstructorService;
import com.softech.vu360.lms.web.controller.model.instructor.InstructorItemForm;
import com.softech.vu360.lms.webservice.client.impl.VcsDiscussionForumClientWSImpl;

/**
 * @author sana.majeed
 *
 */
public class InstructorServiceImpl implements InstructorService {
	
	private static final Logger log = Logger.getLogger(InstructorServiceImpl.class.getName());
	private EnrollmentService enrollmentService = null;
	
	@Inject 
	private CourseRepository courseRepository = null;
	
	@Inject 
	private InstructorCourseRepository instructorCourseRepository = null;
	
	@Inject 
	private InstructorRepository instructorRepository = null;
	

	/**
	 * Get Instructor by Content Owner ID which are not yet associated with given Course ID.
	 */
	@Override
	public Map<Object, Object> getInstructorsForCourseAssociation(Long courseId, String firstName, String lastName, Long contentOwnerId, int pageIndex, int pageSize, int sortDirection, String sortColumn) {
		
		String instType=null;
		List<InstructorCourse> objs = new ArrayList();
		Vector<Long> vctrInstructorId = new Vector();
		if ( StringUtils.isNotBlank(instType)) {
			objs = instructorCourseRepository.findByCourseIdAndInstructorType(courseId,instType);
		}else{
			objs = instructorCourseRepository.findByCourseId(courseId);
		}
		
		
		for(InstructorCourse obj : objs){
			vctrInstructorId.add(obj.getInstructor().getId());
			
		}
		
		
		//Vector<Long> vctrInstructorId = this.instructorDAO.getInstructorsIdsByCourseId(courseId, null);
		List<Instructor> instructorList = this.instructorRepository.findInstructorByContentIdfirstNamelastName(vctrInstructorId, firstName, lastName, contentOwnerId, pageIndex, pageSize, sortDirection, sortColumn);
		Map<Object, Object> serachResult = new HashMap<Object, Object>();
		serachResult.put("totalRecords", instructorList.size());
		serachResult.put("instructorList", instructorList);
		return serachResult;//this.instructorDAO.getInstructorsExceptInstructorIds(vctrInstructorId, firstName, lastName, contentOwnerId, pageIndex, pageSize, sortDirection, sortColumn);
	}
	
	/**
	 * Create InstructorItemForm list from given Instructor list 
	 */
	@Override
	public List<InstructorItemForm> getInstructorItemListFromInstructors ( List<Instructor> instructorList ) {
		List<InstructorItemForm> result = new ArrayList<InstructorItemForm>();
		
		for ( Instructor curInstructor : instructorList ) {
			InstructorItemForm instructorItem = new InstructorItemForm();
			instructorItem.setInstructor(curInstructor);
			
			result.add(instructorItem);
		}
		
		return result;
	}
	
	/**
	 * Create InstructorItemForm list from given InstructorCourse list 
	 */
	@Override
	public List<InstructorItemForm> getInstructorItemListFromCourseInstructors(List<InstructorCourse> courseInstructorList) {
		
		List<InstructorItemForm> result = new ArrayList<InstructorItemForm>();
		
		for( InstructorCourse courseInstructor : courseInstructorList ) {
			InstructorItemForm instructorItem = new InstructorItemForm();
			instructorItem.setInstructor(courseInstructor.getInstructor());
			instructorItem.setInstructorType(courseInstructor.getInstructorType());
			instructorItem.setId(courseInstructor.getId());
			
			result.add(instructorItem);
		}
		
		return result;
	}
	
	/**
	 * [5/10/2010] VCS-266 :: Associate Instructor(s) with Course specially for Discussion Forum type courses
	 */
	@Override
	public List<InstructorCourse> associateCourseInstructors(Long courseId, List<InstructorItemForm> instructorList) {
		
		List<InstructorCourse> courseInstructorList = new ArrayList<InstructorCourse>();
		
		//Course course = this.courseAndCourseGroupDAO.getCourseById(courseId);
		Course course =this.courseRepository.findOne(courseId);
		
		List<InstructorItemForm> finalInstructorList = new ArrayList<InstructorItemForm>();
		
		String instType=null;
		List<InstructorCourse> objs = new ArrayList();
		Vector<Long> instructorIdList = new Vector();
		if ( StringUtils.isNotBlank(instType)) {
			objs = instructorCourseRepository.findByCourseIdAndInstructorType(courseId,instType);
		}else{
			objs = instructorCourseRepository.findByCourseId(courseId);
		}
		
		
		for(InstructorCourse obj : objs){
			instructorIdList.add(obj.getInstructor().getId());
			
		}
		
		//Vector<Long> instructorIdList = this.instructorDAO.getInstructorsIdsByCourseId(courseId, null);
		
		if (course.getCourseType().equals("DFC") || (course.getCourseMediaType()!= null && course.getCourseMediaType().equals("DFC"))) {
			for ( InstructorItemForm instructorItem : instructorList ) {
				
				// Call web service to sync data with phpBB
				boolean wsSuccess = new VcsDiscussionForumClientWSImpl().registerInstructorEvent(instructorItem.getInstructor(), course.getCourseGUID() );
				if ( wsSuccess ) {
					
					// Filter out existing associations in case of any mistake
					if ( !instructorIdList.contains(instructorItem.getInstructor().getId()) ){
						// Save data to DB only if data successfully transfered to phpBB
						finalInstructorList.add(instructorItem);						
					}
				}
			}			
		}
		else {
			finalInstructorList = instructorList;
		}
				
		if (finalInstructorList.size() > 0){
			// Save data to LMS DB
			//courseInstructorList = this.instructorDAO.associateCourseInstructors(course, finalInstructorList);
			List<InstructorCourse> instructorCourses = new ArrayList<InstructorCourse>();
			for ( InstructorItemForm instructorItem : instructorList ) {
				InstructorCourse courseInstructor = new InstructorCourse();
				courseInstructor.setCourse(course);
				courseInstructor.setInstructor(instructorItem.getInstructor());
				courseInstructor.setInstructorType(instructorItem.getInstructorType());
				
				instructorCourses.add( courseInstructor );			
			}
			
			instructorCourseRepository.save(instructorCourses);
			
		}
						
		return courseInstructorList;
	}
	
	/**
	 * Get already associated course instructor list
	 */
	@Override
	public Map<Object, Object> getAssociatedCourseInstructors(Long courseId, String firstName, String lastName, String instructorType, int pageIndex, int pageSize, int sortDirection, String sortColumn) {
				
		//this.instructorDAO.getAssociatedCourseInstructors(courseId, firstName, lastName, instructorType, pageIndex, pageSize, sortDirection, sortColumn);
		List<InstructorCourse> instructorCourses = instructorCourseRepository.findCourseInstructorByCoursefirstNamelastNameIntructorType(courseId, firstName, lastName, instructorType, pageIndex, pageSize, sortDirection, sortColumn);
		Map<Object, Object> serachResult = new HashMap<Object, Object>();
		serachResult.put("totalRecords", instructorCourses.size());
		serachResult.put("courseInstructorList", instructorCourses);
		
		return serachResult;
	}	
	
	/**
	 * Get Course-Instructor information by relationship Id
	 */
	@Override
	public InstructorCourse getCourseInstructorInfoById(Long id) {
		
		return this.instructorCourseRepository.findOne(id);		
	}
		
	/**
	 * Update the instructors role for given Course-Instructor Relationship ID
	 */

	@Override
	public boolean updateCourseInstructorRole(Long courseInstructorId, String instructorType) {
		
		
		InstructorCourse courseInstructor = this.instructorCourseRepository.findOne(courseInstructorId);
		courseInstructor.setInstructorType(instructorType);
		return this.instructorCourseRepository.save(courseInstructor).getInstructorType().equals(instructorType);
				
	}
	
	/**
	 * Delete Course Instructor Association for given CourseInstructor IDs
	 */
	@Override
	public boolean deleteCourseInstructors(Long[] courseInstructorIds) {
		
		List<InstructorCourse> courseInstructorVector = this.instructorCourseRepository.findByIdIn(courseInstructorIds);
		List<Long> finalList = new ArrayList<Long>();
		
		if (courseInstructorIds.length > 0) {
			// Save data to LMS DB
			try{
				this.instructorCourseRepository.delete(courseInstructorVector);
			}catch(Exception ex){
				return false;
			}
		}
		return true;			
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	
}
