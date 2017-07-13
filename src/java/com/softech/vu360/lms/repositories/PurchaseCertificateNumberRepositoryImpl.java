package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
		int i=0;

//		EntityTransaction transaction = Persistence.createEntityManagerFactory("lmsPersistenceUnit").createEntityManager().getTransaction();
////        transaction.begin();
		Iterator<PurchaseCertificateNumber> it = purchaseCertificateNumbers.iterator();
		PurchaseCertificateNumber p = null;
		while(it.hasNext()){
			p = it.next();
			this.entityManager.persist(p);
			if(i > 0 && i % 100 == 0){
				entityManager.flush();
				entityManager.clear();
			}
			i++;
		}
		return false;
	}
}
