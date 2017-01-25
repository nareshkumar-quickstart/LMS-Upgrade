package com.softech.vu360.lms.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.LearnerGroupMember;
import com.softech.vu360.lms.repositories.LearnerGroupMemberRepositoryCustom;

public class LearnerGroupMemberRepositoryImpl implements LearnerGroupMemberRepositoryCustom {


	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public LearnerGroupMember saveLGM(LearnerGroupMember ogm){
		entityManager.merge(ogm);
		return ogm;
	}
}

