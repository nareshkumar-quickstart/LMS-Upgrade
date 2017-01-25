package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;

public interface CourseCustomerEntitlementRepository extends
		CrudRepository<CourseCustomerEntitlement, Long>, CourseCustomerEntitlementRepositoryCustom {


}
