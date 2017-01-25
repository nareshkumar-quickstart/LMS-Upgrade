package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.AuditCourseStatus;

public interface AuditCourseStatusService {
	
	public void logCourseStatusChangeForAudit (AuditCourseStatus auditCourseStatus);

}
