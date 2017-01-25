package com.softech.vu360.lms.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;

public class CourseCustomerEntitlementItemRepositoryImpl implements
		CourseCustomerEntitlementItemRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<CourseCustomerEntitlementItem> getCoursesByNameAndCourseType(
			Customer customer, String courseName, String courseType) {

		StringBuilder builder = new StringBuilder(
				"Select p from CourseCustomerEntitlementItem p WHERE p.customerEntitlement.customer.id=:id and p.course.courseStatus = :courseStatus");
		if (!StringUtils.isBlank(courseName)) {

			builder.append(" and p.course.courseTitle like :courseName");

		}
		if (!StringUtils.isBlank(courseType)) {

			builder.append(" and p.course.courseType like :courseType");

		}
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("id", customer.getId());
		query.setParameter("courseStatus", Course.PUBLISHED);
		query.setParameter("courseName", "%" + courseName + "%");
		query.setParameter("courseType", "%" + courseType + "%");
		@SuppressWarnings("unchecked")
		List<CourseCustomerEntitlementItem> cList = query.getResultList();
		return cList;
	}

}
