package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;

public interface CourseCustomerEntitlementRepositoryCustom {

	public List<CourseCustomerEntitlement> getCourseCustomerEntitlementsByCourse(
			Long customerID, String searchCriteria);

	public List<CourseCustomerEntitlement> getActiveCourseCustomerEntitlement(Customer customer, Long courseId);
}
