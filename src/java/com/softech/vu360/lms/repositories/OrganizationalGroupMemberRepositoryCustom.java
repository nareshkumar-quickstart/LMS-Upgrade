package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.OrganizationalGroupMember;

public interface OrganizationalGroupMemberRepositoryCustom {
	//void deleteOGM(OrganizationalGroupMember ogm);
	OrganizationalGroupMember saveOGM(OrganizationalGroupMember ogm);/**/
	//void updateLearner(Long learnerId, Long orgId, Long oldlearnerId);
}
