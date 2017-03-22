package com.softech.vu360.lms.repositories.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.repositories.OrganizationalGroupRepositoryCustom;

public class OrganizationalGroupRepositoryImpl implements OrganizationalGroupRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public List<OrganizationalGroup> findByLearnerIdOrderByOrgGrpNameAsc(Long learnerId) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT new OrganizationalGroup(ogm.organizationalGroup.id) from OrganizationalGroupMember ogm where ogm.learner.id = :learnerId order by ogm.organizationalGroup.name ASC");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("learnerId", learnerId);
		List<OrganizationalGroup> orgGrps = query.getResultList();
		return orgGrps;
	}

	@Override
	public List<OrganizationalGroup> findOrganizationGroupById(String[] orgGroupId) {
		
		//***************** This below Code is temporary Code, the idea is to fetch all records to avoid n+1 issue ***************
		List<Long> orgGroupIdList = new ArrayList<>();
		for (int i = 0; i < orgGroupId.length; i++) {
			orgGroupIdList.add(new Long(orgGroupId[i]));
			
		}
		String jpaQuery = " select p from OrganizationalGroup p "
				+ "LEFT JOIN p.parentOrgGroup pg "
				+ "LEFT JOIN p.childrenOrgGroups cg "
				+ "WHERE p.id in (:orgGroupId)";
		Query query = entityManager.createQuery(jpaQuery);
		query.setParameter("orgGroupId", orgGroupIdList);
		List<OrganizationalGroup> orgGrps = query.getResultList();
		return orgGrps;
	}

	@Override
	public List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId) {
		String jpaQuery = " select p from TrainingAdministrator p "
				+ "LEFT JOIN FETCH p.managedGroups mg "
				+ "WHERE p.id in (:trainingAdminstratorId)";
		
		Query query = entityManager.createQuery(jpaQuery, TrainingAdministrator.class);
		query.setParameter("trainingAdminstratorId", trainingAdminstratorId);
		List<TrainingAdministrator> taz = query.getResultList();
		
		List<OrganizationalGroup> orgGrps = new ArrayList<>();
		
		if(taz!=null){
			orgGrps = taz.get(0).getManagedGroups();
		}
		
		return orgGrps;
	}
	
}
