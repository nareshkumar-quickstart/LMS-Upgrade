package com.softech.vu360.lms.repositories.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.LearnerRepositoryCustom;

public class LearnerRepositoryImpl implements LearnerRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Learner> getFilteredRecipientsByAlert(Long alertId) {
		StoredProcedureQuery query;
		List<Learner> learners = new ArrayList<Learner>();
		List<VU360User> users;

		query = entityManager
				.createNamedStoredProcedureQuery("Learner.getFilteredRecipientsByAlert");
		query.setParameter("alert_id", alertId);
		query.execute();
		users = (List<VU360User>) query.getResultList();

		for (VU360User user : users) {
			learners.add(user.getLearner());
		}

		return learners;
	}

	@Override
	public List<Learner> findLearnerByLearnerGroupID(Long learnerGroupId) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT new Learner(l.id,vu.id,vu.accountNonLocked,vu.username,vu.firstName,vu.lastName,vu.emailAddress,la.id,ta.id) from LearnerGroupMember lgm LEFT JOIN lgm.learner l LEFT JOIN l.vu360User vu LEFT JOIN vu.lmsAdministrator la LEFT JOIN vu.trainingAdministrator ta WHERE lgm.learnerGroup.id = :learnerGroupId");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("learnerGroupId", learnerGroupId);
		return query.getResultList();
	}

	@Override
	public List<Learner> getLearnerByOrganizationalGroups(Long[] orgGroupIdArray) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT new Learner(ogm.learner.id,ogm.learner.vu360User.id,ogm.learner.vu360User.accountNonExpired,learner.vu360User.accountNonLocked,learner.vu360User.enabled,learner.vu360User.expirationDate,learner.vu360User.firstName,learner.vu360User.lastName,learner.vu360User.emailAddress,learner.customer.name,learner.customer.customerType,learner.customer.distributor.name) from OrganizationalGroupMember ogm where ogm.organizationalGroup.id IN (:orgGroupIdArray)");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("orgGroupIdArray", Arrays.asList(orgGroupIdArray));
		return query.getResultList();
	}

}
