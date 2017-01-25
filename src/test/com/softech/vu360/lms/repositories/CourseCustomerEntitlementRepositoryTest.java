package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;

public class CourseCustomerEntitlementRepositoryTest extends
		SpringJUnitConfigAbstractTest {

	@Inject
	private CourseCustomerEntitlementRepository courseCustomerEntitlementRepository;
	@Inject
	private CustomerRepository customerRepository;

	//@Test
	public void getCourseCustomerEntitlementsByCourse() {

		Customer customer = customerRepository.findOne(1l);

		List<CourseCustomerEntitlement> cceList = courseCustomerEntitlementRepository
				.getCourseCustomerEntitlementsByCourse(customer.getId(), "Course");
		if (cceList != null && cceList.size() > 0) {
			for (CourseCustomerEntitlement cce : cceList) {
				System.out.println(cce.getEnrollmentType());
			}
		}
	}

}
