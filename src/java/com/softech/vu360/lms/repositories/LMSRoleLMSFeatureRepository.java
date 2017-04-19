package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.softech.vu360.lms.model.LMSRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;

/**
 * @author marium.saud
 *
 */
public interface LMSRoleLMSFeatureRepository extends
		CrudRepository<LMSRoleLMSFeature, Long>, LMSRoleLMSFeatureRepositoryCustom {

	Collection<LMSRoleLMSFeature> findByLmsRoleId(@Param("roleID") long roleID);

	/*
	 * getAvailablePermissionsByRoleType() public List<LMSRoleLMSFeature>
	 * findByLmsRole_OwnerAndLmsRole_RoleTypeAndLmsFeature_RoleType( Customer
	 * customer, String roleType, String roleType2);
	 */
	@Query("SELECT p from LMSRoleLMSFeature p WHERE p.lmsRole.owner = :customer AND p.lmsRole.roleType = :roleType AND p.lmsFeature.roleType = :roleType AND p.enabled = true")
	public List<LMSRoleLMSFeature> getAvailablePermissionsByRoleType(
			@Param(value = "customer") Customer customer,
			@Param(value = "roleType") String roleType);

	@Query("SELECT p from LMSRoleLMSFeature p WHERE p.lmsRole.owner = :customer AND p.lmsRole.roleType = :roleType AND p.lmsFeature.roleType = :roleType")
	public List<LMSRoleLMSFeature> getAllPermissionsByRoleType(
			@Param(value = "customer") Customer customer,
			@Param(value = "roleType") String roleType);
	

	@Query("SELECT LRLF FROM LMSRoleLMSFeature LRLF " +
			"	JOIN FETCH LRLF.lmsFeature " +
			"	WHERE LRLF.lmsRole IN (:lmsRoles)")
	public List<LMSRoleLMSFeature> getLMSRoleLMSFeature(@Param(value = "lmsRoles")Set<LMSRole> lmsRoles);

}
