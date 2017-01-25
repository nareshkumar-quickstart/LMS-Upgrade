package com.softech.vu360.lms.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LmsApi;
import com.softech.vu360.lms.repositories.LmsApiRepository;

public class LmsApiRepositoryTest extends SpringJUnitConfigAbstractTest {
	
	@Autowired
	private LmsApiRepository lmsApiRepository;
	
	
	@Test
	public void findByLmsEnrollmentId() {
		LmsApi lc = lmsApiRepository.findByCustomerId(1L);
		System.out.println(lc.getApiKey());
	}

}
