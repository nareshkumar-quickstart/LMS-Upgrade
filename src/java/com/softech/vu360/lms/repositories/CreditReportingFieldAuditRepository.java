package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CreditReportingFieldAudit;

public interface CreditReportingFieldAuditRepository extends CrudRepository<CreditReportingFieldAudit, Long> {
	List<CreditReportingFieldAudit> findByCreditReportingFieldIdOrderByIdDesc(Long creditReportingFieldId);
	
}
