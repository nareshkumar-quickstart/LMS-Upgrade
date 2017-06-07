package com.softech.vu360.lms.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.OrganizationalGroupMember;
import com.softech.vu360.lms.repositories.OrganizationalGroupMemberRepositoryCustom;

public class OrganizationalGroupMemberRepositoryImpl implements OrganizationalGroupMemberRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public OrganizationalGroupMember saveOGM(OrganizationalGroupMember ogm){
		ogm = entityManager.merge(ogm);
		return ogm;
	}

	public void deleteByLearnerIdSql(Long learnerId) {
		entityManager.createNativeQuery("Delete from LEARNER_ORGANIZATIONALGROUP where learner_id = " + learnerId);
	}
}
