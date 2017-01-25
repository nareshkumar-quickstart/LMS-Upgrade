package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Distributor;

public interface CourseGroupCustomerEntitlementItemRepositoryCustom {
	
	List<Map<Object,Object>> getCourseGroupIdsForDistributor(Distributor distributor);
	List<CourseGroupCustomerEntitlementItem> bulkSave(List<CourseGroupCustomerEntitlementItem> entities);
	CourseGroupCustomerEntitlementItem saveCGCEI(CourseGroupCustomerEntitlementItem CGCEI);
}
