/**
 * 
 */
package com.softech.vu360.lms.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.repositories.LearnerGroupRepositoryCustom;

/**
 * @author marium.saud
 *
 */
public class LearnerGroupRepositoryImpl implements LearnerGroupRepositoryCustom{
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<LearnerGroup> findByLearnerIdOrderByLearnerGroupNameAsc(Long learnerId) {
		
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT lg from Learner l JOIN l.learnerGroup lg WHERE l.id = :learnerId ORDER BY lg.name ASC");
			Query query = entityManager.createQuery(builder.toString());
			query.setParameter("learnerId", learnerId);
			List<LearnerGroup> learnerGroup = query.getResultList();
			return learnerGroup;
		}
	}


