package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;

public interface DistributorEntitlementRepository extends CrudRepository<DistributorEntitlement, Long> {
	List<DistributorEntitlement> findByDistributor(Distributor distributor);
	List<DistributorEntitlement> findByDistributorId(Long id);
	List<DistributorEntitlement> findByNameAndDistributorId(String name, Long id);
	
	
}
