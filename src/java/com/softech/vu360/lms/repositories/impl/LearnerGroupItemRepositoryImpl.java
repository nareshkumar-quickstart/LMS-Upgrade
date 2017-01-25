package com.softech.vu360.lms.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.repositories.LearnerGroupItemRepositoryCustom;

public class LearnerGroupItemRepositoryImpl implements LearnerGroupItemRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	@Transactional
	public LearnerGroupItem saveLGI(LearnerGroupItem ogm){
		entityManager.merge(ogm);
		return ogm;
	}
}
