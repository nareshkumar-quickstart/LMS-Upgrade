package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.AuditEvent;
import com.softech.vu360.lms.model.Auditable;

/**
 * AuditService defines the set of interfaces 
 * to control the interactions and business logic
 * of all auditing within the LMS.
 * 
 * @author jason
 *
 */
public interface AuditService {
	
	public void auditEvent(AuditEvent evt, Auditable aud);

}
