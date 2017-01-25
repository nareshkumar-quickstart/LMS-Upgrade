package com.softech.vu360.lms.repositories;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.InstructorCourse;

public class InstructorCourseRepositoryImpl implements InstructorCourseRepositoryCustom{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public List<InstructorCourse> findCourseInstructorByCoursefirstNamelastNameIntructorType(Long courseId, String firstName, String lastName, String instructorType, int pageIndex, int pageSize, int sortDirection, String sortColumn){
		StringBuilder queryString = new StringBuilder("SELECT i FROM  InstructorCourse i ");
		queryString.append(" where course.id =:courseId ");
		
		if ( StringUtils.isNotBlank(instructorType) ) {
			queryString.append(" and instructorType  like '%"+instructorType+"%'");
		}
		
		if ( StringUtils.isNotBlank(firstName) ) {
			queryString.append(" and instructor.user.firstName  like '%"+firstName+"%'");
		}
		
		if ( StringUtils.isNotBlank(lastName) ) {
			queryString.append(" and instructor.user.lastName  like '%"+lastName+"%'");
		}
		
		if ( sortDirection == 0 ) {
			if ( sortColumn.equalsIgnoreCase("firstName") ) {
				queryString.append(" order by instructor.user.firstName  asc");	
			}else if ( sortColumn.equalsIgnoreCase("lastName") ) {
				queryString.append(" order by instructor.user.lastName  asc");	
			}else if ( sortColumn.equalsIgnoreCase("instructorType") ) {
				queryString.append(" order by iinstructorType  asc");
			}				
		}else {
			if ( sortColumn.equalsIgnoreCase("firstName") ) {
				queryString.append(" order by instructor.user.firstName  desc");	
			}else if ( sortColumn.equalsIgnoreCase("lastName") ) {
				queryString.append(" order by instructor.user.lastName  desc");
			}else if ( sortColumn.equalsIgnoreCase("instructorType") ) {
				queryString.append(" order by iinstructorType  desc");
			}
		}

		Query query = entityManager.createQuery(queryString.toString());
		query.setFirstResult(pageSize*pageIndex);
		query.setMaxResults(pageSize);
		query.setParameter("courseId",courseId);
		List<InstructorCourse> instructorList = query.getResultList();
		return instructorList;
	}

}
