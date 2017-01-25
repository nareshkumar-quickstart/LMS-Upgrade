package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.RegulatoryApproval;

public interface RegulatoryApprovalRepositoryCustom {
	List<RegulatoryApproval> findByRequirements(Long[] selectedRequirementIds);
}
