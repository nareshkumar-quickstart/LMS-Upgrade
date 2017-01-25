/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.BrandDomain;

/**
 * @author marium.saud
 *
 */
public class BrandDomainRepositoryImpl implements BrandDomainRepositoryCustom {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public BrandDomain findBrandByDomain(String domain) {
		Query query = entityManager.createNamedQuery("BrandDomain.findBrandByDomain");
		query.setParameter("domain", domain+"%");
		List<BrandDomain> brands = query.getResultList();
		BrandDomain brand=null;
		if(brands!=null && brands.size()>0)
			brand=(BrandDomain)brands.get(0);
		return brand;
	}
	
}
