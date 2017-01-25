package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.CourseRestrictedIP;

public class CourseRestrictedIPRepositoryImpl implements CourseRestrictedIPRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;


	@Override
	public Boolean isValidOSHACourseJurisdication(Long courseId) {
		
		StringBuilder queryString = new StringBuilder("SELECT c FROM CourseRestrictedIP c WHERE c.courseId =:courseId ");
		
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("courseId", courseId);
		
		List<CourseRestrictedIP> listCourseRestrictedIP = query.getResultList(); 
		
		if(listCourseRestrictedIP!=null && listCourseRestrictedIP.size()>0)
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}

}
