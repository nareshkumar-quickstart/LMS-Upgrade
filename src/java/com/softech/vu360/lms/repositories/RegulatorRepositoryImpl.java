package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Regulator;

public class RegulatorRepositoryImpl implements RegulatorRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<Regulator> findRegulator(String name, String alias, String emailAddress, List<ContentOwner> cos) {
		StringBuilder queryString = new StringBuilder("SELECT r FROM  Regulator r WHERE r.active=TRUE ");
		
		queryString.append(" AND r.name LIKE :name And r.alias LIKE :alias AND r.emailAddress LIKE :emailAddress ");
		
		if (cos!=null && cos.size() > 0){
			queryString.append(" AND r.contentOwner IN :cos ");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("name", "%" + name + "%");
		query.setParameter("alias", "%" + alias + "%");
		query.setParameter("emailAddress", "%" + emailAddress + "%");
		
		if (cos!=null && cos.size() > 0){
			query.setParameter("cos", cos);
		}
		
		List<Regulator> listRegulator = query.getResultList(); 
		return listRegulator;
	}

	@Override
	public List<Regulator> searchRegulator(String name, List<ContentOwner> cos) {
		StringBuilder queryString = new StringBuilder("SELECT new Regulator(r.id,r.name) FROM  Regulator r WHERE r.active=TRUE AND r.name LIKE :name");
		
		if (cos!=null && cos.size() > 0){
			queryString.append(" AND r.contentOwner IN :cos ");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("name", "%" + name + "%");
		
		if (cos!=null && cos.size() > 0){
			query.setParameter("cos", cos);
		}
		
		List<Regulator> listRegulator = query.getResultList(); 
		return listRegulator;
	}

}