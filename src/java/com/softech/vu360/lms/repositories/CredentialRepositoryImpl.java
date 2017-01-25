package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.Credential;

public class CredentialRepositoryImpl implements CredentialRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<Credential> findCredentialByContentOwner(Collection<Long> contentOwnerIds, String officialLicenseName, String shortLicenseName, Boolean active) {
		
		StringBuilder queryString = new StringBuilder("SELECT c FROM Credential c WHERE c.officialLicenseName LIKE :officialLicenseName "
				+ "AND c.shortLicenseName LIKE :shortLicenseName AND c.active=:active ");
		
		if (contentOwnerIds!=null && contentOwnerIds.size() > 0){
			queryString.append(" AND c.contentOwner IN :cos");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("officialLicenseName", "%"+officialLicenseName+"%");
		query.setParameter("shortLicenseName", "%"+shortLicenseName+"%");
		query.setParameter("active", active);

		if (contentOwnerIds!=null && contentOwnerIds.size() > 0){
			query.setParameter("cos", contentOwnerIds);
		}
		
		
		List<Credential> credentialList = query.getResultList(); 
		
		return credentialList;
	}

	

	

}
