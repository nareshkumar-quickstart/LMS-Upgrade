package com.softech.vu360.lms.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

public class RegistrationInvitationRepositoryImpl implements
		RegistrationInvitationRepositoryCustom {

	 @PersistenceContext
	 protected EntityManager entityManager;

	@Override
	public Object[] addRegistrationInvitation(Long registrationInvitationID, Integer registrationUtilized) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("RegistrationInvitation.addNewRegistrationInvitation");
		query.setParameter("REG_ID", registrationInvitationID);
		query.setParameter("REG_UTILIZED", registrationUtilized);
		query.execute();
		return  (Object[])query.getSingleResult();
	}
}
