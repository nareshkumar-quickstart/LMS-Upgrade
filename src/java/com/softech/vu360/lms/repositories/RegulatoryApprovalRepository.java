package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.RegulatoryApproval;
public interface RegulatoryApprovalRepository extends CrudRepository<RegulatoryApproval, Long>, RegulatoryApprovalRepositoryCustom {
	
}
