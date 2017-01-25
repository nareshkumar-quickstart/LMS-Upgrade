package com.softech.vu360.lms.repositories;

import java.util.List;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;

public interface CourseCustomerEntitlementItemRepositoryCustom {

	public List<CourseCustomerEntitlementItem> getCoursesByNameAndCourseType(
			Customer customer, String courseName, String courseType);

}
