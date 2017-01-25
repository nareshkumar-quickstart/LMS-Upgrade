package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CourseApprovalAudit;

public interface CourseApprovalAuditRepository extends CrudRepository<CourseApprovalAudit, Long> {
	
}
