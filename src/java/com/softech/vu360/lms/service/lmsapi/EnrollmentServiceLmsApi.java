package com.softech.vu360.lms.service.lmsapi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;

public interface EnrollmentServiceLmsApi {

	public Map<String, Map<String, ?>> doEnrollment(Customer customer, Learner learner, 
			Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception;
	
	public List<LearnerEnrollment> getLearnerSuccessfullCoursesEnrolledList(Learner learner, Map<String, Map<String, ?>> courseMap) throws Exception;
	public Map<com.softech.vu360.lms.model.Course, String> getLearnerUnsuccessfullCoursesErrorMap(Learner learner, Map<String, Map<String, ?>> courseMap) throws Exception;
	
}
