package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CustomerOrder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CustomerOrderRepositoryTest {
	@Inject
	private CustomerOrderRepository customerOrderRepository;
	
	//@Test
	public void findByOrderGUID() {
		CustomerOrder entity = customerOrderRepository.findByOrderGUID("23003");
		System.out.println(entity);
	}

	@Test
	public void findByCustomerId() {
		List<CustomerOrder> aList = customerOrderRepository.findByCustomerId(1454L);
		System.out.println(aList);
	}
}
