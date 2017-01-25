package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.BrandDomain;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.repositories.BrandDomainRepository;
import com.softech.vu360.lms.repositories.BrandRepository;
import com.softech.vu360.lms.repositories.LearnerRepository;

public class BrandDomainRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private BrandDomainRepository brandDomainRepository;
	
	//@PersistenceContext
	//protected EntityManager entityManager;
	
	
	@Test
	public void brandDomain_should_getByDomain() {
		BrandDomain brandDomain = brandDomainRepository.findBrandByDomain("yyy");
		System.out.println("BrandDomain : ID = "+brandDomain.getId()+" Name = "+brandDomain.getDomain());
	}
	
	
}
