package com.softech.vu360.lms.repositories;

import java.util.List;
import javax.inject.Inject;
import org.junit.Test;
import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;

public class CustomerLMSFeatureRepositoryTest extends
		SpringJUnitConfigAbstractTest {

	@Inject
	CustomerLMSFeatureRepository clfRepository;
	@Inject
	CustomerRepository customerRepository;

	@Test
	public void findByCustomerAndLmsFeature_RoleType() {
		//String roleType = "ROLE_LEARNER";
		String roleType = "ROLE_TRAININGADMINISTRATOR";
		Customer customer = customerRepository.findOne(234871L);
		List<CustomerLMSFeature> clfList = clfRepository
				.findByCustomerAndLmsFeature_RoleTypeOrderByLmsFeature_FeatureCode(customer, roleType);
		if (clfList != null && clfList.size() > 0) {
			for (CustomerLMSFeature clf : clfList) {
				System.out.println(clf.getKey());
			}
		}
	}

	//@Test
	public void findByCustomerAndEnabledTrueOrNull() {
		Customer customer = customerRepository.findOne(53l);
		List<CustomerLMSFeature> clfList = clfRepository
				.findByCustomerAndEnabledFalse(customer.getId());
		if (clfList != null && clfList.size() > 0) {
			for (CustomerLMSFeature clf : clfList) {
				System.out.println(clf.getKey());
			}
		}
	}

}
