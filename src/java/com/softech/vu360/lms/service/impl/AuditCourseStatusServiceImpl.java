package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.AuditCourseStatus;
import com.softech.vu360.lms.repositories.AuditCourseStatusRepository;
import com.softech.vu360.lms.service.AuditCourseStatusService;

public class AuditCourseStatusServiceImpl implements AuditCourseStatusService{
	
	@Inject
	private AuditCourseStatusRepository auditCourseStatusRepository;
	
	@Transactional
	public void logCourseStatusChangeForAudit (AuditCourseStatus auditCourseStatus)	{
		auditCourseStatus=auditCourseStatusRepository.save(auditCourseStatus);		
	}
}
