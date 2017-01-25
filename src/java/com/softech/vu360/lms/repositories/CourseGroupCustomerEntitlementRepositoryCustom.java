package com.softech.vu360.lms.repositories;

import java.util.Collection;

import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;

public interface CourseGroupCustomerEntitlementRepositoryCustom {

	public Collection<CourseGroupCustomerEntitlement> getActiveCourseGroupCustomerEntitlement(Customer customer, Long courseId);
	
}
