package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
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
			
			Query query = entityManager.createNativeQuery("{call getUnusedPurchaseCertificateNumber(?)}");
			query.setParameter(1, courseApprovalId);
			
			List results = query.getResultList();
                    
			if(results.size() > 0) {
//				PurchaseCertificateNumber result = (PurchaseCertificateNumber) results.get(0);
				PurchaseCertificateNumber result = new PurchaseCertificateNumber();
				Object[] dataArray = (Object[]) results.get(0);
				result.setId(Long.parseLong(dataArray[0].toString()));
				result.setCertificateNumber(dataArray[1].toString());
				result.setUsed(Boolean.valueOf(dataArray[2].toString()));
				result.setNumericCertificateNumber(Long.parseLong(dataArray[3].toString()));
                CourseApproval c = new CourseApproval();
                c.setId(courseApprovalId);
				result.setCourseApproval(c);
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
		int batchSize = Integer.valueOf(this.entityManager.getEntityManagerFactory().getProperties().get("hibernate.jdbc.batch_size").toString());
		Iterator<PurchaseCertificateNumber> purchaseCertificateNumberIterator = purchaseCertificateNumbers.iterator();
		PurchaseCertificateNumber purchaseCertificateNumber = null;
		this.entityManager.setFlushMode(FlushModeType.COMMIT);
		while(purchaseCertificateNumberIterator.hasNext()){
			purchaseCertificateNumber = purchaseCertificateNumberIterator.next();
			this.entityManager.persist(purchaseCertificateNumber);
			if(counter > 0 && counter % batchSize == 0){
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
