package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.RegulatorCategory;

public interface LearningSessionRepositoryCustom {

	public boolean isCourseApprovalLinkedWithLearnerEnrollment(List<Long> courseApprovalIds);
	public boolean isCourseApprovalLinkedWithLearnerEnrollment(Long courseApprovalId);
	
}
