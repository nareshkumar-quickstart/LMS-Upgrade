package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;

public interface CourseApprovalRepository extends CrudRepository<CourseApproval, Long>, CourseApprovalRepositoryCustom {
	
	@Query("SELECT ca FROM  CourseApproval ca WHERE UPPER(ca.approvedCourseName) LIKE %:approvedCourseName% "
			+ "AND UPPER(ca.courseApprovalNumber) LIKE %:courseApprovalNumber% AND ca.deleted=:isDeleted "
			+ "and ca.active=:isActive  ")
	List<CourseApproval> findCourseApprovals(@Param("approvedCourseName") String approvalName, @Param("courseApprovalNumber") String approvalNumber, 
			@Param("isDeleted") Boolean isDeleted, @Param("isActive") Boolean isActive);
	
	List<CourseApproval> findByApprovedCourseNameLikeIgnoreCaseAndCourseApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(@Param("approvedCourseName") String approvalName, @Param("courseApprovalNumber") String approvalNumber);
	
	
	List<CourseApproval> findByTemplateIdAndDeletedFalse(Long templateId);
	
	CourseApproval findByIdAndDeletedFalse(Long id);
	
	List<CourseApproval> findByCourseIdAndDeletedFalseAndActiveTrue(Long courseId);
	List<CourseApproval> findByCourseAndDeletedFalseAndActiveTrue(Course course);
	
	List<CourseApproval> findByCourseApprovalEffectivelyEndsDateBetweenAndDeletedFalseAndActiveTrue(Date startDate,Date endDate);
	
	@Query(value = "select ca.* from courseapproval ca, learningsession ls where ls.enrollment_id = ?1 and ca.id = ls.courseapprovalid", nativeQuery = true)
	CourseApproval getCourseApprovalByEnrollment(long learnerEnrollmentId);
	
}
