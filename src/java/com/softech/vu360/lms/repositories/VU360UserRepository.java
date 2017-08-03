/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.VU360User;

/**
 * @author marium.saud
 *
 */
public interface VU360UserRepository extends CrudRepository<VU360User, Long>, VU360UserRepositoryCustom , JpaRepository<VU360User, Long>, JpaSpecificationExecutor<VU360User>{

	List<VU360User> findByUsernameIn(@Param("lstUserName") Collection<String> lstUserName);

	public VU360User getUserById(Long id);
	
	public List<VU360User> findByLearnerIdIn(Long[] learnerId);
	
	public VU360User findFirstByUserGUID(@Param("userGUID") String userGUID );
	
	public int countByEmailAddress(@Param("emailAddress") String emailAddress);
	
	@Query(value = "if exists (select top 1 rf.id from vu360user u, vu360user_role ur, LMSROLELMSFEATURE rf, LMSFEATURE f, lmsrole r where u.id = ?1 and ur.user_id = u.id and ur.role_id = ?2 and rf.LMSROLE_ID = ur.ROLE_ID and rf.ENABLEDTF = 1 and f.id = rf.LMSFEATURE_ID and r.id = ur.ROLE_ID)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)\n" + 
			"", nativeQuery = true)
	public boolean hasAtLeastOnePermssionOfRoleEnabled(Long userId, Long roleId);
	
	@Query(value = "if exists (select top 1 rf.id from vu360user u, vu360user_role ur, LMSROLELMSFEATURE rf, LMSFEATURE f, lmsrole r where u.id = ?1 and ur.user_id = u.id and rf.LMSROLE_ID = ur.ROLE_ID and rf.ENABLEDTF = 1 and f.id = rf.LMSFEATURE_ID and r.id = ur.ROLE_ID)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)\n" + 
			"", nativeQuery = true)
	public boolean hasAtLeastOnePermssionOfAnyRoleEnabled(Long userId);
	
	@Query(value = "if exists (select top 1 r.id from vu360user u, vu360user_role ur, lmsrole r where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasAnyRoleAssigned(Long userId);
	
	@Query(value = "if exists(select top 1 l.id from vu360user u, learner l where u.id = ?1 and l.VU360USER_ID = u.ID) and exists(select top 1 rf.id from vu360user u, vu360user_role ur, lmsrole r, LMSROLELMSFEATURE rf where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_LEARNER +"' and rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasLearnerRole(Long userId);
	
	@Query(value = "if exists(select top 1 p.id from vu360user u, proctor p where u.id = ?1 and p.VU360USER_ID = u.ID) and exists(select top 1 rf.id from vu360user u, vu360user_role ur, lmsrole r, LMSROLELMSFEATURE rf where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_PROCTOR +"' and rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasProctorRole(Long userId);
	
	@Query(value = "if exists(select top 1 a.id from vu360user u, lmsadministrator a where u.id = ?1 and a.VU360USER_ID = u.ID) and exists(select top 1 rf.id from vu360user u, vu360user_role ur, lmsrole r, LMSROLELMSFEATURE rf where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_LMSADMINISTRATOR +"' and rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasAdministratorRole(Long userId);
	
	@Query(value = "if exists(select top 1 ta.id from vu360user u, TRAININGADMINISTRATOR ta where u.id = ?1 and ta.VU360USER_ID = u.ID) and exists(select top 1 rf.id from vu360user u, vu360user_role ur, lmsrole r, LMSROLELMSFEATURE rf where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_TRAININGMANAGER +"' and rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasTrainingAdministratorRole(Long userId);
	
	@Query(value = "if exists(select top 1 ra.id from vu360user u, REGULATORYANALYST ra where u.id = ?1 and ra.VU360USER_ID = u.ID) and exists(select top 1 rf.id from vu360user u, vu360user_role ur, lmsrole r, LMSROLELMSFEATURE rf where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_REGULATORYANALYST +"' and rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasRegulatoryAnalystRole(Long userId);
	
	@Query(value = "if exists(select top 1 i.id from vu360user u, INSTRUCTOR i where u.id = ?1 and i.VU360USER_ID = u.ID) and exists(select top 1 rf.id from vu360user u, vu360user_role ur, lmsrole r, LMSROLELMSFEATURE rf where u.id = ?1 and ur.user_id = u.id and r.id = ur.ROLE_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_INSTRUCTOR +"' and rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)\n" + 
			"	select cast(1 as bit)\n" + 
			"else\n" + 
			"	select cast(0 as bit)", nativeQuery = true)
	public boolean hasInstructorRole(Long userId);
	
	@Query(value = "select distinct u.*\n" + 
			"from vu360user u, learner l, LEARNER_ORGANIZATIONALGROUP lorg,\n" + 
			"TRAININGADMINISTRATOR tadmin, LMSROLE r, VU360USER_ROLE ur\n" + 
			"where lorg.ORGANIZATIONALGROUP_ID in (\n" + 
			"  select org.ID\n" + 
			"  from vu360user u, learner l, \n" + 
			"  LEARNER_ORGANIZATIONALGROUP lgg, \n" + 
			"  ORGANIZATIONALGROUP org\n" + 
			"  where u.ID = ?1 \n" + 
			"  and l.VU360USER_ID = u.ID\n" + 
			"  and lgg.LEARNER_ID = l.ID\n" + 
			"  and org.ID = lgg.ORGANIZATIONALGROUP_ID\n" + 
			")\n" + 
			"and l.ID = lorg.LEARNER_ID\n" + 
			"and (\n" + 
			"      tadmin.VU360USER_ID = l.VU360USER_ID\n" + 
			"      or (tadmin.CUSTOMER_ID = l.CUSTOMER_ID and tadmin.MANAGEALLORGANIZATIONALGROUPTF = 1)\n" + 
			"    )\n" + 
			"and (r.CUSTOMER_ID = l.CUSTOMER_ID and r.ROLE_TYPE = '"+ LMSRole.ROLE_TRAININGMANAGER +"')\n" + 
			"and ur.[USER_ID] = tadmin.VU360USER_ID\n" + 
			"and ur.ROLE_ID = r.ID\n" + 
			"and (u.ID = ur.[USER_ID] and u.ACCOUNTNONEXPIREDTF = 1 and u.ACCOUNTNONLOCKEDTF = 1 and u.ENABLEDTF = 1 )\n" + 
			"", nativeQuery = true)
	public List<VU360User> findTrainingAdministratorsOfUser(Long userId);
	
}
