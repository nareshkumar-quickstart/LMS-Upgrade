package com.softech.vu360.lms.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LegacyCredential;
import com.softech.vu360.lms.repositories.LegacyCredentialRepository;

public class LegacyCredentialRepositoryTest extends SpringJUnitConfigAbstractTest {
	
	@Autowired
	private LegacyCredentialRepository lcRepository;
	
	
	@Test
	public void findByLmsEnrollmentId() {
		LegacyCredential lc = lcRepository.findTop1ByLmsEnrollmentId(5337453L);
		System.out.println(lc.getAddress());
	}
	
}
