package com.softech.vu360.lms.repositories;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;

public class CourseGroupCustomerEntitlementRepositoryImpl implements
		CourseGroupCustomerEntitlementRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Collection<CourseGroupCustomerEntitlement> getActiveCourseGroupCustomerEntitlement(Customer customer, Long courseId) {
		String queryString = "SELECT s.customerEntitlement FROM CourseGroupCustomerEntitlementItem s inner join s.courseGroup.courses c "
				+ " WHERE s.customerEntitlement.customer.id=:customerId "
				+ " AND (s.customerEntitlement.endDate is null OR s.customerEntitlement.endDate>= :curDate) "
				+ " AND s.customerEntitlement.startDate<= :curDate AND "
				+ "(s.customerEntitlement.allowUnlimitedEnrollments=1 OR s.customerEntitlement.maxNumberSeats > s.customerEntitlement.numberSeatsUsed) "
				+ " AND c.id=:courseId ";
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("customerId", customer.getId());
		query.setParameter("curDate", new Date(System.currentTimeMillis()));
		query.setParameter("courseId", courseId);
		
		List<CourseGroupCustomerEntitlement> listCourseGroupCustomerEntitlement = (List<CourseGroupCustomerEntitlement>)query.getResultList();
		
		return listCourseGroupCustomerEntitlement;
	}

	

}
