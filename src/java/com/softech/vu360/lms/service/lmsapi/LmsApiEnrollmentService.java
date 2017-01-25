package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.DuplicatesEnrollment;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrolledCourses;

public interface LmsApiEnrollmentService {

	List<LearnerEnrolledCourses> enrollLearnerCourses(Map<Boolean, List<LearnerCourses>> learnerCoursesMap, Customer customer, VU360User manager, boolean notifyLearnersByEmail, DuplicatesEnrollment duplicatesEnrollment);
	
}
