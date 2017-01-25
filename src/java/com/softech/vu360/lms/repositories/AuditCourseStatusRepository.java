/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AuditCourseStatus;

/**
 * @author muhammad.junaid
 *
 */
public interface AuditCourseStatusRepository extends CrudRepository<AuditCourseStatus, Long> {
	//public void logCourseStatusChangeForAudit (AuditCourseStatus auditCourseStatus);
}
