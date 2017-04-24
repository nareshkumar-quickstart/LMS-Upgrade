package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;

public interface LearnerEnrollmentRepositoryCustom {

	public Long getEnrollmentCountBySynchronousClassId(Long synchronousClassId);
	public List<LearnerEnrollment> getEnrollmentsByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,Date startDate,Date endDate, String courseTitle, String status[]);
	public List<LearnerEnrollment> findByLearnerIdAndEnrollmentStatusNotIn(Long learnerId,List<String> status);
	
}
