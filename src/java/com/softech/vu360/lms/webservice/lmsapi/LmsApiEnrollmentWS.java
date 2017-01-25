package com.softech.vu360.lms.webservice.lmsapi;

import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.BulkEnrollmentRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.BulkEnrollmentResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.enrollment.LearnerCoursesEnrollResponse;

public interface LmsApiEnrollmentWS {

	/**
	 * This is the namespace we defined in our EnrollmentServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String ENROLLMENT_TARGET_NAMESPACE = "http://enrollment.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String LEARNER_COURSES_ENROLL_REQUEST = "LearnerCoursesEnrollRequest";
	String BULK_ENROLLMENT_REQUEST = "BulkEnrollmentRequest";
	
	LearnerCoursesEnrollResponse learnerCoursesEnroll(LearnerCoursesEnrollRequest learnerCoursesEnrollRequest);
	BulkEnrollmentResponse bulkEnrollment(BulkEnrollmentRequest bulkEnrollmentRequest);
	
}
