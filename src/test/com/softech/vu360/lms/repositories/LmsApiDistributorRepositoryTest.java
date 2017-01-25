package com.softech.vu360.lms.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.repositories.LmsApiDistributorRepository;

public class LmsApiDistributorRepositoryTest extends SpringJUnitConfigAbstractTest {
	
	@Autowired
	private LmsApiDistributorRepository lmsApiDistributorRepository;
	
	//@Test
	public void findByLmsEnrollmentId() {
		LmsApiDistributor lc = lmsApiDistributorRepository.findOne(1L);
		System.out.println(lc.getEnvironment());
	}
	
	@Test
	public void findByApiKey() {
		try {
			LmsApiDistributor lc = lmsApiDistributorRepository.findByApiKey("dcda5167-8af6-48d3-99cf-4337d19d378a");
			System.out.println(lc.getEnvironment());
		} catch (Exception e) {
			
		}
	}
}
