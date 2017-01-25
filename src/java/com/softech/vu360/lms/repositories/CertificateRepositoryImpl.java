/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.Certificate;


public class CertificateRepositoryImpl implements CertificateRepositoryCustom {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<Certificate> getCertificatesWhereAssetVersionIsEmpty(Long fromCertificateId, Long toCertificateId) {
		try{
			StringBuilder queryString = new StringBuilder("SELECT p FROM Certificate p Where (p.currentAssetVersionId is null or p.currentAssetVersionId <0) ");
			
			if(fromCertificateId != null){
				queryString.append(" AND p.id>=:fromCertificateId ");	
			}
			
			if(toCertificateId != null){
				queryString.append(" AND p.id<=:toCertificateId ");	
			}
			
			Query query = entityManager.createQuery(queryString.toString());
			
			if(fromCertificateId != null){
				query.setParameter("fromCertificateId", fromCertificateId);
			}
			
			if(toCertificateId != null){
				query.setParameter("toCertificateId", toCertificateId);	
			}
			
			List<Certificate> listCertificate = query.getResultList(); 
			
			return listCertificate;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
