package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.util.FormUtil;

public class EnrollmentCourseViewRepositoryImpl implements EnrollmentCourseViewRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;


	@Override
	public List<EnrollmentCourseView> getCoursesForEnrollment(Long customerId, String courseName, String courseCode, String contractName, int startIndex, int endIndex) {
		
		StringBuilder query = new StringBuilder();
				
		query.append(" SELECT * FROM ( " +
				" SELECT *, ROW_NUMBER() OVER (ORDER BY COURSENAME) AS ROWNUM " +
				" , COUNT(1) OVER () AS TOTALRECORDS " +
				" FROM COURSEVIEWFORENROLLLEARNER WHERE CUSTOMER_ID = " + customerId);
		
		if(! StringUtils.isBlank(courseName))
			query.append(" AND COURSENAME like :courseName");
		
		if(! StringUtils.isBlank(courseCode))
			query.append(" AND COURSECODE like :courseCode");
		
		if(! StringUtils.isBlank(contractName))
			query.append(" AND ENTITLEMENT_NAME like :contractName");
		
		query.append(" ) ENTITLEMENTS ");
		
		if (startIndex > 0 && endIndex > 0) {
			query.append(" WHERE ROWNUM BETWEEN " + startIndex + " AND " + endIndex );
		}
		
		query.append(" ORDER BY COURSENAME ");
		
		Query q = entityManager.createNativeQuery(query.toString(), "getCoursesForEnrollmentWithPagingMapping");

		if(StringUtils.isNotBlank(courseName)) {
			q.setParameter("courseName", "%" + courseName + "%");
		}
		if(StringUtils.isNotBlank(courseCode)) {
			q.setParameter("courseCode", "%" + courseCode + "%");	
		}
		
		if(StringUtils.isNotBlank(contractName)) {
			q.setParameter("entitlementName", "%" + contractName + "%");
		}

		return (List<EnrollmentCourseView>)q.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnrollmentCourseView> getCoursesForEnrollment(Long customerId, String courseName, String courseCode, String contractName, Date expirationDate) {
		
		StringBuilder query = new StringBuilder();
		query.append("select * ");
		query.append("from CourseViewForEnrollLearner ");
		query.append("WHERE ");
		List<String> criteria = new ArrayList<>();
		criteria.add("customer_id = :customerId");
		if(StringUtils.isNotBlank(courseName)) {
			criteria.add("COURSENAME like :courseName");
		}
		
		if(StringUtils.isNotBlank(courseCode)) {
			criteria.add("COURSECODE like :courseCode");	
		}
		
		if(StringUtils.isNotBlank(contractName)) {
			criteria.add("ENTITLEMENT_NAME like :entitlementName");
		}
			
		if(expirationDate != null) {
			criteria.add("EXPIRATION_DATE <= :expirationDate");
		}
		
		if (criteria.size() == 0) {
			throw new RuntimeException("no criteria");
		}
		
		for (int i = 0; i < criteria.size(); i++) {
			if (i > 0) { 
				query.append(" AND "); 
			}
			query.append(criteria.get(i));
		}
		
		Query q = entityManager.createNativeQuery(query.toString(), "getCoursesForEnrollmentMapping");
		q.setParameter("customerId", customerId);
		
		if(StringUtils.isNotBlank(courseName)) {
			q.setParameter("courseName", "%" + courseName + "%");
		}
		
		if(StringUtils.isNotBlank(courseCode)) {
			q.setParameter("courseCode", "%" + courseCode + "%");	
		}
		
		if(StringUtils.isNotBlank(contractName)) {
			q.setParameter("entitlementName", "%" + contractName + "%");
		}
			
		if(expirationDate != null) {
			q.setParameter("expirationDate", FormUtil.getInstance().formatDate(expirationDate, "yyyy-MM-dd"));
		}
		
		return (List<EnrollmentCourseView>)q.getResultList();
		
	}
	
}
