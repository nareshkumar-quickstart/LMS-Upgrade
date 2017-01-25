package com.softech.vu360.lms.service;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;

public class EntitlementServiceTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private EntitlementService entitlementService;
	@Inject
	private CustomerService customerService;
	@Inject
	private CourseAndCourseGroupService courseAndCourseGroupService;
	
	@Test
	public void customerHasActiveEntitlement() {
		Customer customer = customerService.getCustomerById(63235);
		Course course = courseAndCourseGroupService.getCourseById(217910L);
		CustomerEntitlement custEntitlement = entitlementService.customerHasActiveEntitlementFor(customer, course);
	}
	
}
