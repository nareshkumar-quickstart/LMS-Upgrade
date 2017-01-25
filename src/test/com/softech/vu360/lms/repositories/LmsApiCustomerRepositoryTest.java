package com.softech.vu360.lms.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.repositories.LmsApiCustomerRepository;

public class LmsApiCustomerRepositoryTest extends SpringJUnitConfigAbstractTest {
	
	@Autowired
	private LmsApiCustomerRepository lmsApiCustomerRepository;
	
	//@Test
	public void findByLmsEnrollmentId() {
		LmsApiCustomer lc = lmsApiCustomerRepository.findOne(1L);
		System.out.println(lc.getApiKey());
	}
	
	@Test
	public void findByApiKey() {
		try {
			LmsApiCustomer lc = lmsApiCustomerRepository.findByApiKey("0f9ef1ea-c634-4797-a490-de6cc88d48a3");
			System.out.println(lc.getApiKey());
		} catch (Exception e) {
		}
	}
	
}
