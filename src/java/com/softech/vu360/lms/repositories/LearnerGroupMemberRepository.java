package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.LearnerGroupMember;
import com.softech.vu360.lms.model.LearnerGroupMemberPK;

public interface LearnerGroupMemberRepository extends CrudRepository<LearnerGroupMember, LearnerGroupMemberPK>, LearnerGroupMemberRepositoryCustom {
	
	//public List<LearnerGroupMember> getLearnersByLearnerGroupIds(Long learnerGroupIdArray[]);
	//@Query("select p from LearnerGroupMember p where p.learnerGroup.id in (:ids)")
	public List<LearnerGroupMember> findByLearnerGroupIdIn(@Param("ids") Long[] learnerGroupId);
	
	
	// public List<LearnerGroupMember> getExistingMemberships(Long[] learnersArray,LearnerGroup learnerGroup)
	public List<LearnerGroupMember> findByLearnerGroupIdAndLearnerIdIn(Long learnerGroupId , Long[] learnerId);
	
	public List<LearnerGroupMember> findByLearnerIdAndLearnerGroupIdIn(Long learnerId , Long[] learnerGroupIds);
	
	@Modifying
	@Transactional
	public void deleteByLearnerId(Long id);
}
