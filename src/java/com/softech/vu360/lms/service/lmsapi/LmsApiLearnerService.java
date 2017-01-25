package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360User;

public interface LmsApiLearnerService {

	void sendEmailToLearners(Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap, VU360User manager);
	
}
