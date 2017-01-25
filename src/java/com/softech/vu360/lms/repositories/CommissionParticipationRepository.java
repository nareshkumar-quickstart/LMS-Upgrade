package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CommissionParticipation;

public interface CommissionParticipationRepository extends CrudRepository<CommissionParticipation, Long> {
	List<CommissionParticipation> findByCommissionId(Long commissionId);
	List<CommissionParticipation> findByIdIn(Long[] pkIds);
	Long countByCommissionablePartyIdIn(Long[] commPartyIds);
}
