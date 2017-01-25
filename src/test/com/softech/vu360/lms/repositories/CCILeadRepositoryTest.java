package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CCILead;
import com.softech.vu360.lms.model.CustomerOrder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CCILeadRepositoryTest {
	@Inject
	private CCILeadRepository cCILeadRepository;
	@Inject
	private CustomerOrderRepository orderRepository;
	
	@Test
	public void save() {
		String cciLeadId="420";
		CustomerOrder customerOrder=orderRepository.findByOrderGUID("23003");
		CCILead entity=new CCILead();
		entity.setCciLeadId(cciLeadId);
		entity.setCustomerOrder(customerOrder);
		entity = cCILeadRepository.save(entity);
		System.out.println(entity);
	}

}
