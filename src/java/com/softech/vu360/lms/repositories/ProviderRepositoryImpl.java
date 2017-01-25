/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Provider;


public class ProviderRepositoryImpl implements ProviderRepositoryCustom {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<Provider> findProviders(String name, List<ContentOwner> cos) {
		StringBuilder queryString = new StringBuilder("SELECT p FROM  Provider p WHERE p.active=TRUE AND p.name LIKE :name ");
		
		if (cos!=null && cos.size()>0) {
			queryString.append(" AND p.contentOwner in :coids ");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("name", "%" + name + "%");
		
		if (cos!=null && cos.size()>0) {
			query.setParameter("coids", cos);
		}
		
		List<Provider> listProvider = query.getResultList(); 
		return listProvider;
	}

	@Override
	public List<Provider> searchProviders(String name, List<ContentOwner> cos) {
		StringBuilder queryString = new StringBuilder("SELECT new Provider(p.id,p.name) FROM  Provider p WHERE p.active=TRUE AND p.name LIKE :name ");
		
		if (cos!=null && cos.size()>0) {
			queryString.append(" AND p.contentOwner in :coids ");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("name", "%" + name + "%");
		
		if (cos!=null && cos.size()>0) {
			query.setParameter("coids", cos);
		}
		
		List<Provider> listProvider = query.getResultList(); 
		return listProvider;
	}

}
