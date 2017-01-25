package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CourseApprovalRepositoryCustom {

	public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(Long courseId, Long regulatorCategoryId, Date startDate, Date endDate, Long excludeCourseApprovalId);

	boolean isCourseAlreadyAssociatedWithRegulatorAuthority(Long courseId, Long regulatorCategoryId, Date startDate, Date endDate);
	
	public int getNumberOfUnusedPurchaseCertificateNumbers(Long courseApprovalId);
	
	public boolean isCertificateLinkedWithCourseApproval(List<Long> certificateIds);
	
	public boolean isAffidavitLinkedWithCourseApproval(List<Long> certificateIds);
	
	public List<Map<Object, Object>> getCourseApprovalByCourse(String varCourseId, String varLearnerEnrollmentId);
	
}
