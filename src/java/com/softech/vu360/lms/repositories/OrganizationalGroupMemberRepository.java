package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.OrganizationalGroupMember;
import com.softech.vu360.lms.model.OrganizationalGroupMemberPK;

public interface OrganizationalGroupMemberRepository extends CrudRepository<OrganizationalGroupMember, OrganizationalGroupMemberPK>, OrganizationalGroupMemberRepositoryCustom {

	@Query("SELECT o FROM #{#entityName} o WHERE o.learner.id = :Id")
	List<OrganizationalGroupMember> findByLearnerId(@Param("Id") Long orgGroupIdArray); 
	
	
	//public List<OrganizationalGroupMember> getLearnersByOrgGroups(Long orgGroupIdArray[]);
	//@Query("SELECT o FROM #{#entityName} o WHERE o.organizationalGroup.id in (:Id)")
	List<OrganizationalGroupMember> findByOrganizationalGroupIdIn(@Param("Id") Long [] orgGroupIdArray);
	
	
	//	public List<OrganizationalGroupMember> getExistingMemberships(Long[] learnersArray,OrganizationalGroup orgGroup){
	@Query("SELECT o FROM #{#entityName} o WHERE o.organizationalGroup.id = :Id and o.learner.id in (:learnerId)")
	List<OrganizationalGroupMember> findByOrganizationalGroupIdLearnerIdIn(@Param("Id") Long orgGroupId, @Param("learnerId") Long [] orgLearnerIdArray);
	
	
	//public List<OrganizationalGroupMember> getOrgGroupMembers(Long orgGroupId)
	//@Query("SELECT o FROM #{#entityName} o WHERE o.organizationalGroup.id = :Id")
	List<OrganizationalGroupMember> findByOrganizationalGroupId(@Param("Id") Long orgGroupId);
	
	//public List<OrganizationalGroup>  getOrgGroupsByLearner(Learner learner)
	List<OrganizationalGroupMember> findByLearnerIdOrderByOrganizationalGroupNameAsc(@Param("Id") Long learnerId);
	
	public void deleteByLearnerId(Long learnerId);
	
	List<OrganizationalGroupMember> findDistinctByLearnerIdIn(@Param("Id") Long [] learnerIdArray);
}
