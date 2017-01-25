package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;

public class PurchaseCertificateNumberRepositoryImpl implements PurchaseCertificateNumberRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public PurchaseCertificateNumber getUnusedPurchaseCertificateNumber(Long courseApprovalId) {
		
		try {
			
			Query query = entityManager.createNativeQuery("{call getUnusedPurchaseCertificateNumber(?)}", PurchaseCertificateNumber.class);
			query.setParameter(1, courseApprovalId);
			
			List results = query.getResultList();          
                    
			if(results.size() > 0) {
				PurchaseCertificateNumber result = (PurchaseCertificateNumber) results.get(0);
				
				return result;
				
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	


}
