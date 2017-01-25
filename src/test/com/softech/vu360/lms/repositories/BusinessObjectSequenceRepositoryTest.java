package com.softech.vu360.lms.repositories;


import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.repositories.BusinessObjectSequenceRepository;

public class BusinessObjectSequenceRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private BusinessObjectSequenceRepository businessObjectSequenceRepository;
	
	//@PersistenceContext
	//protected EntityManager entityManager;
	
	
	@Test
	public void BusinessObjectSequence_should_getNextSequence() {
		String businessObjSeq = businessObjectSequenceRepository.getNextBusinessObjectSequence("OSHACertificate");
		System.out.println("BusinessObjectSequence = "+businessObjSeq);
	}
	
	
}
