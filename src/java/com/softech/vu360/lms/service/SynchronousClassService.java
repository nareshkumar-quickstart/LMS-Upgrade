package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.MyWebinarPlace;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.vo.SynchrounousSessionVO;
import com.softech.vu360.lms.web.controller.model.AssignInstructorForm;
import com.softech.vu360.lms.web.controller.model.ClassForm;
import com.softech.vu360.lms.web.controller.model.SessionForm;
import com.softech.vu360.util.Brander;

/**
 * 
 * @author noman
 * 
 */
public interface SynchronousClassService {

	/**
	 * addSynchronousSession is used for adding the single or multiple session
	 * in existing synchronousclass or brand new synchronousclass (within a
	 * single screen, instructor or admin wants to create the single or multiple
	 * session of a class, all session belongs to only one instructor, therefore
	 * we have a list of synchronousSession and an object of
	 * instructorSynchronousSession for creating a single or bulk session.
	 * 
	 * @return
	 */
	// public String addSynchronousSession(List<SynchronousSession>
	// lstSynchronousSession, InstructorSynchronousClass instSynchClass);

	/**
	 * for dropping/deleting single or multiple sessions from existing class
	 * 
	 * @return
	 */
	// public String dropSynchronousSession(List<Long> id);

	/**
	 * it returns the particular
	 * 
	 * @return
	 */
	// public SynchronousClass getSynchronousClassByID(Long id);

	/**
	 * it returns all the active Synchronousclasses
	 * 
	 * @return
	 */
	public List<SynchronousClass> getAllSynchronousClasses(Long instructorId);
	
	
	public Course getInstructorCourse(Long courseId);

	public List<SynchronousSession> getSynchronousSessionsByClassId(
			Long synchClassId);

	/**
	 * it returns all the active Synchronoussessions in a particular class
	 * 
	 * @return
	 */
	// public List<SynchronousSession>
	// getAllSynchronousSessionBySychronousClassID(Long id);

	public SynchronousClass getSynchronousClassById(Long id);

	public SynchronousClass getSynchronousClassByGUID(String guid);

	public Map<Object, Object> findClassesByCourseId(Long courseId,
			String className, String startDate, String endDate, int pageIndex,
			int pageSize, String sortBy, int sortDirection, int maxLimit);

	
	public Map<Object, Object> findAllClassesByCourseId(Long courseId,
			String className, String startDate, String endDate, String sortBy,
			int sortDirection, int maxLimit);

	public void addSchedule(ClassForm classForm);

	public void deleteSynchronousClass(Long classIdArray[]);

	/* this is actually having SynchronousSession PK ids */
	public void deleteSynchronousClassSession(Long[] sessionIdArray);

	/* this is actually having Instructor_SynchronousClass PK ids */
	public void deleteSynchronousClassInstructor(
			long synchronousClassInstructorIdArray[]);

	public void saveSynchronousClass(SynchronousClass synchClass);

	public void saveSynchronousClassInstructorRole(
			InstructorSynchronousClass instructorSynchClass);

	public List<SynchronousSession> findSynchronousSessionByClassId(
			Long synchClassId, SessionForm sessionForm);

	public void addSynchronousSession(SessionForm sessionForm);

	public void saveSynchronousSession(SynchronousSession synchronousSession);

	public List<InstructorSynchronousClass> findInstructorsByClassId(
			Long synchClassId, String firstName, String lastName,
			String instType);

	public List<Instructor> getInstructors2AssignSynchClass(Long classId,
			String firstName, String lastName, com.softech.vu360.lms.vo.ContentOwner contentOwner);

	public InstructorSynchronousClass assignInstructors2SynchClass(
			AssignInstructorForm assignInstructorForm);

	public InstructorSynchronousClass findInstructorSynchClassByClassId(
			Long classId, Long instructorId);

	public InstructorSynchronousClass loadForUpdateInstructorSynchClassById(
			Long classId);

	public InstructorSynchronousClass findInstructorSynchClassById(Long id);

	public SynchronousSession findSynchClassSessionBySessionId(Long id);

	public List<SynchronousClass> getSynchronousClassByCourseId(Long courseId);

	public boolean checkIfSessionInProgress(
			List<SynchronousSession> syncSessions, TimeZone classTimeZone,
			TimeZone learnerTimeZone, Brander brand);// Sana Majeed | LMS-4132

	public List<Course> getCourseByGUID(Vector<String> guid);

	public List<InstructorSynchronousClass> getLeadInstructorsOfSynchClass(
			Long classId);

	public List<InstructorSynchronousClass> getAllInstructorsOfSynchClass(
			Long classId);

	public List<SynchronousSession> getAllClassSessionsOfSynchClass(Long classId);

	public List<SynchronousClass> getAllSynchClassesOfCourse(Long courseId);

	public List<SynchronousClass> getSynchronousClassOfCourses(
			Vector<Long> courseId);

	public Map<Object, Object> getSynchronousSessionsByClassId(
			Long synchClassId, int pageIndex, int pageSize, String sortBy,
			int sortDirection); // LMS-4189

	public SynchrounousSessionVO getMinMaxScheduleDateForSynchronousClass(
			Long classId);

	// Not found in 4.7.1 during merging
	// public List<LearnerEnrollment>
	// getAllLearnerEnrollmentBySynchronousClass(Long synchronousClassId);

	public SynchronousSession loadForUpdateSynchronousSession(long id);

	public SynchronousClass loadForUpdateSynchronousClass(long id);

	// [8/10/2010] LMS-6657 :: Use of aggregate function to get the Enrollment
	// count By Class ID instead of loading all objects
	public Long getEnrollmentCountBySynchronousClassId(Long synchronousClassId);

	// [8/24/2010] LMS-6857 :: Get Synchronous Class for Enrollment. Set
	// newEnrollmentCount to -1 if want to by pass it
	public List<SynchronousClass> getSynchronousClassesForEnrollment(
			Long courseId, int newEnrollmentCount);

	// [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses
	// irrespective of contract and enrollments availability
	public List<SynchronousClass> getSynchronousClassesForEnrollment(
			Long courseId, int newEnrollmentCount, boolean includeSessions);

	public boolean isSyncCourseAssignable(Long syncCourseId);

	public boolean isSyncClassAssignable(SynchronousClass synchronousClass,
			int newEnrollmentCount);
	
	public String getMeetingInfoForSessions(MyWebinarPlace myWebinarPlace);
	public boolean deleteMyWebinarPlaceMeeting(String meetingID);
}