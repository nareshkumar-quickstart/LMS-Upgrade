package com.softech.vu360.lms.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.repositories.SurveyLearnerRepositoryCustom;

public class SurveyLearnerRepositoryImpl implements SurveyLearnerRepositoryCustom {


	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public SurveyLearner saveSL(SurveyLearner sl) {
		try{
			return entityManager.merge(sl);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
