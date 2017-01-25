package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;




public interface CourseDetailsEventService {

	public List<Instructor> getAllInstructorsOfSynchClass(Long classId);
	public List<SynchronousSession> getAllClassSessionsOfSynchClass(Long classId);
	public List<SynchronousClass> getAllSynchClassesOfCourse(Long courseId);
	//public boolean implementRefund(RefundRequest refund) throws Exception;
	
}
