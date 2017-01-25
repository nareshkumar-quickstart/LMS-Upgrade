/**
 * 
 */
package com.softech.vu360.lms.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.repositories.SurveySectionSurveyQuestionBankCustom;

/**
 * @author muhammad.junaid
 *
 */
public class SurveySectionSurveyQuestionBankRepositoryImpl implements SurveySectionSurveyQuestionBankCustom {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public SurveySectionSurveyQuestionBank saveSurveySectionSurveyQuestionBank(SurveySectionSurveyQuestionBank surveySectionSurveyQuestionBank) {
		return entityManager.merge(surveySectionSurveyQuestionBank);
	}

}
