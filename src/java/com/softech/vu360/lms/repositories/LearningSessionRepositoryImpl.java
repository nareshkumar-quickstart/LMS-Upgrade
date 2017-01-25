package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class LearningSessionRepositoryImpl implements LearningSessionRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * SELECT COUNT(DISTINCT(ENROLLMENT_ID)) FROM LEARNINGSESSION WHERE (COURSEAPPROVALID IN (?, ?))
	 */
	@Override
	public boolean isCourseApprovalLinkedWithLearnerEnrollment(List<Long> courseApprovalIds) {
		StringBuilder queryString = new StringBuilder("SELECT count(Distinct c.enrollmentId) FROM LearningSession c WHERE c.courseApprovalId In :courseApprovalId ");
	
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("courseApprovalId", courseApprovalIds);
		
		Long count = (Long) query.getSingleResult();
		return count.longValue()>0;
	}

	@Override
	public boolean isCourseApprovalLinkedWithLearnerEnrollment(Long courseApprovalId) {
		StringBuilder queryString = new StringBuilder("SELECT count(Distinct c.enrollmentId) FROM LearningSession c WHERE c.courseApprovalId =:courseApprovalId ");
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("courseApprovalId", courseApprovalId);
		
		Long count = (Long) query.getSingleResult();
		return count.longValue()>0;
	}

}