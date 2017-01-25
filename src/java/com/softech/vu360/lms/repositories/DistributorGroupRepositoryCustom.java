package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.DistributorGroup;

/**
 * 
 * @author haider.ali
 *
 */
public interface DistributorGroupRepositoryCustom {
	void deleteDistributorGroups(Long distributorGroupIdArray[]);
	void assignDistributorsInDistributorGroup(Long distributorGroupId,Long distributorIdArray[]);
	List<DistributorGroup> findDistributorGroupsByLMSAdministratorId(Long lmsAdministratorId);
}
