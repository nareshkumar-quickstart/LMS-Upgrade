package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

	@Override
	@Transactional
	public boolean savePurchaseNumberAsBatch(Set<PurchaseCertificateNumber> purchaseCertificateNumbers) {
		int counter=0;
		boolean result = false;
		Iterator<PurchaseCertificateNumber> purchaseCertificateNumberIterator = purchaseCertificateNumbers.iterator();
		PurchaseCertificateNumber purchaseCertificateNumber = null;
		while(purchaseCertificateNumberIterator.hasNext()){
			purchaseCertificateNumber = purchaseCertificateNumberIterator.next();
			this.entityManager.persist(purchaseCertificateNumber);
			if(counter > 0 && counter % 100 == 0){
				entityManager.flush();
				entityManager.clear();
			}
			counter++;
		}
		entityManager.flush();
		entityManager.clear();
		result = true;
		return result;
	}
}
