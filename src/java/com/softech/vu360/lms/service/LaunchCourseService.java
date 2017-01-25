package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;

/**
 * LaunchCourseService defines the set of interfaces 
 * to control the interactions and business logic
 * of launching a course within the LMS.
 * 
 * @author jason
 *
 */
public interface LaunchCourseService {
	
	public String launchCourse(Learner l, LearnerEnrollment learnerEnrollment, String brandName, Language language, String source, String externalLMSSessionID, String externalLMSUrl, int lmsProvider, int courseApprovalId);
	public String launchCourse(String userGUID, String courseID, String brandName, Language language, String source, String externalLMSSessionID, String externalLMSUrl, int lmsProvider);
	public boolean isValidOSHACourseJurisdication(Long courseId);

}