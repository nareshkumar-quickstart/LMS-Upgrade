/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.OrganizationalGroup;

/**
 * @author marium.saud
 *
 */
public interface OrganizationalGroupRepositoryCustom {
	List<OrganizationalGroup> findByLearnerIdOrderByOrgGrpNameAsc(Long learnerId);
	List<OrganizationalGroup> findOrganizationGroupById(String[] orgGroupId);
	List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId);
}
