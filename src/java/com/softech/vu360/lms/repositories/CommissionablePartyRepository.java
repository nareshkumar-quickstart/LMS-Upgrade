package com.softech.vu360.lms.repositories;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CommissionableParty;

public interface CommissionablePartyRepository extends CrudRepository<CommissionableParty, Long> {

	HashSet<CommissionableParty> findByDistributorId(Long distId);
	List<CommissionableParty> findByIdIn(Long[] pkIds);
}
