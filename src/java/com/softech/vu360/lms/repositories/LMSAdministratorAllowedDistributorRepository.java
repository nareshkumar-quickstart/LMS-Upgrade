package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;

/**
 * 
 * @author haider.ali
 *
 */
public interface LMSAdministratorAllowedDistributorRepository extends CrudRepository<LMSAdministratorAllowedDistributor, Long> {

	public List<LMSAdministratorAllowedDistributor> findByDistributorGroupIdAndLmsAdministratorId(Long distributorGroupId, Long lmsAdministratorId);
	public List<LMSAdministratorAllowedDistributor> findByDistributorGroupIdInAndLmsAdministratorId(Collection<Long> distributorGroupIds, Long lmsAdministratorId);
	public void deleteByDistributorGroupIdAndLmsAdministratorId(Long distributorGroupId, Long lmsAdministratorId);
	public LMSAdministratorAllowedDistributor findByDistributorGroupIdAndLmsAdministratorIdAndAllowedDistributorId(Long distributorGroupId, Long lmsAdministratorId, Long allowedDistributorId);
}