/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LMSFeature;

/**
 * @author marium.saud
 * @Kaunain.Wajeeh
 *
 */
public interface LMSFeatureRepository extends CrudRepository<LMSFeature, Long>, LMSFeatureRepositoryCustom {
	
	public List<LMSFeature> findByRoleType(String roleType);
	public List<LMSFeature> findByRoleTypeAndIdNotIn(String roleType, List<Long> ids);
	@Query(value = "if exists(select f.ID from vu360user u, vu360user_role ur, LMSROLELMSFEATURE rf, LMSFEATURE f where u.id = ?1 and ur.USER_ID = u.id and (rf.LMSROLE_ID = ur.role_id and rf.ENABLEDTF = 1) and (f.id = rf.LMSFEATURE_ID and f.FEATUREGROUP = ?2))\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasAccessToFeatureGroup(Long userId, String featureGroup);
	@Query(value = "if exists(select f.ID from vu360user u, vu360user_role ur, LMSROLELMSFEATURE rf, LMSFEATURE f where u.id = ?1 and ur.USER_ID = u.id and (rf.LMSROLE_ID = ur.role_id and rf.ENABLEDTF = 1) and (f.id = rf.LMSFEATURE_ID and f.FEATURECODE = ?2))\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasAccessToFeatureCode(Long userId, String featureCode);
	@Query(value = "if exists(select f.ID from vu360user u, vu360user_role ur, LMSROLELMSFEATURE rf, LMSFEATURE f where u.id = ?1 and ur.USER_ID = u.id and (rf.LMSROLE_ID = ur.role_id and rf.LMSROLE_ID = ?2 and rf.ENABLEDTF = 1) and (f.id = rf.LMSFEATURE_ID and f.FEATUREGROUP = ?3))\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasAccessToFeatureGroup(Long userId, Long roleId, String featureGroup);
	@Query(value = "if exists(select f.ID from vu360user u, vu360user_role ur, LMSROLELMSFEATURE rf, LMSFEATURE f where u.id = ?1 and ur.USER_ID = u.id and (rf.LMSROLE_ID = ur.role_id and rf.LMSROLE_ID = ?2 and rf.ENABLEDTF = 1) and (f.id = rf.LMSFEATURE_ID and f.FEATURECODE = ?3))\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasAccessToFeatureCode(Long userId, Long roleId, String featureCode);
	@Query(value = "select f.featuregroup from vu360user u, learner l, vu360user_role ur, lmsrole r, lmsrolelmsfeature rf, lmsfeature f where u.id = ?1 and l.VU360USER_ID = u.id and ur.USER_ID = u.ID and (r.id = ur.role_id and r.customer_id = l.customer_id) and (rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1) and (f.id = rf.LMSFEATURE_ID) order by DISPLAYORDER", nativeQuery = true)
	public List<String> getEnabledFeatureGroups(Long userId);
	@Query(value = "select f.featuregroup from vu360user u, learner l, vu360user_role ur, lmsrole r, lmsrolelmsfeature rf, lmsfeature f where u.id = ?1 and l.VU360USER_ID = u.id and (ur.USER_ID = u.ID  and ur.ROLE_ID = ?2) and (r.id = ur.role_id and r.customer_id = l.customer_id) and (rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1) and (f.id = rf.LMSFEATURE_ID) order by DISPLAYORDER", nativeQuery = true)
	public List<String> getEnabledFeatureGroups(Long userId, Long roleId);
}
