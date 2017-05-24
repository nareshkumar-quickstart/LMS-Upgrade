package com.softech.vu360.lms.repositories.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.LearnerProfile;
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
		builder.append("SELECT new Learner(ogm.learner.id,ogm.learner.vu360User.id,ogm.learner.vu360User.accountNonExpired,learner.vu360User.accountNonLocked,learner.vu360User.enabled,learner.vu360User.expirationDate,learner.vu360User.firstName,learner.vu360User.lastName,learner.vu360User.emailAddress,learner.customer.id,learner.customer.name,learner.customer.customerType,learner.customer.distributor.id,learner.customer.distributor.name) from OrganizationalGroupMember ogm where ogm.organizationalGroup.id IN (:orgGroupIdArray)");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("orgGroupIdArray", Arrays.asList(orgGroupIdArray));
		return query.getResultList();
	}

	public List<Learner> findLearnersByVU360UserIn(List<VU360User> users) {

		//TODO - Remove extra entity graph once Learner entity is configured for lazyInitialization.
		/////////////////////////////////////////////////////////////////////////////
		//By Sajjad - Following code is not needed here but since Learner entity is not lazily configured, we have to do that to avoid extra queries.
		// With/Without the following code, 4/34 queries were generated for 10 users respectively.
		EntityGraph<Learner> learnerEntityGraph = this.entityManager.createEntityGraph(Learner.class);
		Subgraph<LearnerProfile> learnerProfileSubgraph =  learnerEntityGraph.addSubgraph("learnerProfile");
		learnerProfileSubgraph.addAttributeNodes("learnerAddress");
		learnerProfileSubgraph.addAttributeNodes("learnerAddress2");
		Subgraph<LearnerPreferences> learnerPreferencesSubgraph =  learnerEntityGraph.addSubgraph("preference");
		///////////////////////////////////////////////////////////////////////////////

		Query query = this.entityManager.createQuery("SELECT L FROM Learner L join fetch L.vu360User U WHERE U in (:users)", Learner.class)
														.setHint("javax.persistence.loadgraph", learnerEntityGraph);
		query.setParameter("users", users);
		return query.getResultList();
	}
}