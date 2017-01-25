package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.RegulatoryApproval;

public class RegulatoryApprovalRepositoryImpl implements RegulatoryApprovalRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<RegulatoryApproval> findByRequirements(Long[] selectedRequirementIds) {
		
		List<RegulatoryApproval> regulatoryApprovalList =null;
		
		try{
			Query query = entityManager.createNamedQuery("RegulatoryApproval.getByRequirements");
			query.setParameter(1, Arrays.asList(selectedRequirementIds));
			regulatoryApprovalList = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return regulatoryApprovalList;
	}
	
	
	
}
