package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CreditReportingField;

public interface CreditReportingFieldRepository extends CrudRepository<CreditReportingField, Long> {
	List<CreditReportingField> findByFieldLabelLikeIgnoreCaseAndFieldTypeLikeIgnoreCaseAndActiveTrue(String fieldLabel, String fieldType);
	List<CreditReportingField> findByFieldLabelAndContentOwnerIdAndActiveTrue(String fieldLabel, Long co);
}
