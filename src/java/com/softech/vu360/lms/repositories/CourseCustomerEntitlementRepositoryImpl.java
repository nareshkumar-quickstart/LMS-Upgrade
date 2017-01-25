package com.softech.vu360.lms.repositories;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;

public class CourseCustomerEntitlementRepositoryImpl implements
		CourseCustomerEntitlementRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<CourseCustomerEntitlement> getCourseCustomerEntitlementsByCourse(
			Long customerID, String searchCriteria){
		boolean searchCriteriaFlag = false;
		StringBuilder builder = new StringBuilder(
				"Select p from CourseCustomerEntitlement p");
		builder.append(" join p.entitlementItems c where p.customer.id=:customerId");
		if (!StringUtils.isBlank(searchCriteria)) {
			builder.append(" and c.course.courseTitle Like :criteria");
			builder.append(" or c.course.courseGUID like :criteria or");
			builder.append(" c.course.keywords like :criteria");
			searchCriteriaFlag = true;
		}
		builder.append(" and p.maxNumberSeats > p.numberSeatsUsed or");
		builder.append(" p.allowUnlimitedEnrollments=true");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("customerId", customerID);
		if (searchCriteriaFlag) {
			query.setParameter("criteria", "%"+searchCriteria+"%");
		}
		@SuppressWarnings("unchecked")
		List<CourseCustomerEntitlement> cceList = query.getResultList();
		return cceList;
	}

	@Override
	public List<CourseCustomerEntitlement> getActiveCourseCustomerEntitlement(Customer customer, Long courseId) {
		String queryString = "select ccei.customerEntitlement from CourseCustomerEntitlementItem ccei where ccei.customerEntitlement.customer.id=:customerId "
			+ " and (ccei.customerEntitlement.endDate is null or ccei.customerEntitlement.endDate>=:endDate) "
			+ " and ccei.customerEntitlement.startDate<=:startDate "
			+ " and (ccei.customerEntitlement.allowUnlimitedEnrollments=:allowUnlimitedEnrollments "
			+ " or ccei.customerEntitlement.maxNumberSeats > ccei.customerEntitlement.numberSeatsUsed) "
			+ " and ccei.course.id=:courseId";
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("customerId", customer.getId());
		query.setParameter("allowUnlimitedEnrollments", Boolean.TRUE);
		query.setParameter("endDate", new Date(System.currentTimeMillis()));
		query.setParameter("startDate", new Date(System.currentTimeMillis()));
		query.setParameter("courseId", courseId);
		
		List<CourseCustomerEntitlement> listCourseCustomerEntitlement = (List<CourseCustomerEntitlement>)query.getResultList();
		
		return listCourseCustomerEntitlement;
	}

}
