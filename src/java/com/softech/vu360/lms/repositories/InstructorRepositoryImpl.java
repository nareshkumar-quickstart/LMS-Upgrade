package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.vo.VU360User;

public class InstructorRepositoryImpl implements InstructorRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<Instructor> findInstructorByfirstNamelastNameemailAddress(String firstName, String lastName,String emailAddress,String sortBy, String sortDirection) {
		StringBuilder queryString = new StringBuilder("SELECT i FROM  Instructor i WHERE i.active=TRUE AND i.user.firstName LIKE :firstName AND i.user.lastName LIKE :lastName ");
		if (emailAddress!=null && !emailAddress.isEmpty()) {
			queryString.append(" AND i.user.emailAddress LIKE :emailAddress ");
		}
		
		if(!StringUtils.isBlank(sortBy)){
			if(sortDirection.equalsIgnoreCase("0"))
				queryString.append(" order by i.user.").append(sortBy).append(" ASC");
			else
				queryString.append(" order by i.user.").append(sortBy).append(" DESC");
		}
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("firstName", "%" + firstName + "%");
		query.setParameter("lastName", "%" + lastName + "%");
		
		if (emailAddress!=null && !emailAddress.isEmpty()) {
			query.setParameter("emailAddress", "%" + emailAddress + "%");
		}
		
		List<Instructor> listInstructor = query.getResultList(); 
		return listInstructor;
	}

	@Override
	public List<Instructor> findInstructor(String firstName, String lastName, String emailAddress, VU360User user,String sortBy, String sortDirection) {
		StringBuilder queryString = new StringBuilder("SELECT i FROM  Instructor i ");
		
		if (!user.getRegulatoryAnalyst().isForAllContentOwner()) {
			Long customerId = user.getLearner().getCustomer().getId();
			queryString.append(" WHERE i.user.learner.customer.id = "+ customerId +" And i.active = TRUE ");
		}else{
			queryString.append(" WHERE i.active = TRUE ");
		}
		
		if(!StringUtils.isBlank(firstName)){
			queryString.append(" AND i.user.firstName LIKE :firstName ");
		}
		
		if(!StringUtils.isBlank(lastName)){
			queryString.append(" AND i.user.lastName LIKE :lastName ");
		}
		
		if (emailAddress!=null && !emailAddress.isEmpty()) {
			queryString.append(" AND i.user.emailAddress LIKE :emailAddress ");
		}
		
		if(!StringUtils.isBlank(sortBy)){
			if(sortDirection.equalsIgnoreCase("0"))
				queryString.append(" order by i.user.").append(sortBy).append(" ASC");
			else
				queryString.append(" order by i.user.").append(sortBy).append(" DESC");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		
		if(!StringUtils.isBlank(firstName)){
			query.setParameter("firstName", "%" + firstName + "%");
		}
		
		if(!StringUtils.isBlank(lastName)){
			query.setParameter("lastName", "%" + lastName + "%");
		}
		
		if (emailAddress!=null && !emailAddress.isEmpty()) {
			query.setParameter("emailAddress", "%" + emailAddress + "%");
		}
		
		List<Instructor> listInstructor = query.getResultList(); 
		return listInstructor;
	}

	@Override
	public List<Instructor> findInstructorByContentIdfirstNamelastName(Vector<Long> vctrInstructorIds, String firstName, String lastName,Long contentOwnerId, int pageIndex, int pageSize,int sortDirection, String sortColumn) {
		StringBuilder queryString = new StringBuilder("SELECT i FROM  Instructor i ");
		queryString.append(" where contentOwner.id =:contentOwnerId ");
		
		if( (vctrInstructorIds != null) && (vctrInstructorIds.size() > 0) ) {
			queryString.append(" and id not in (:vctrInstructorIds)");
		}
		
		if ( StringUtils.isNotBlank(firstName) ) {
			queryString.append(" and firstName  like '%"+firstName+"%'");
		}
		
		if ( StringUtils.isNotBlank(lastName) ) {
			queryString.append(" and lastName  like '%"+lastName+"%'");
		}
		
		if (sortDirection == 0) // ascending
			queryString.append(" order by "+sortColumn+"  asc");	
		else
			queryString.append(" order by "+sortColumn+"  desc");
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setFirstResult(pageSize*pageIndex);
		query.setMaxResults(pageSize);
		query.setParameter("contentOwnerId",contentOwnerId);
		if( (vctrInstructorIds != null) && (vctrInstructorIds.size() > 0) )
			query.setParameter("vctrInstructorIds",vctrInstructorIds);

		List<Instructor> instructorList = query.getResultList();
		return instructorList;
	}

	@Override
	public List<Instructor> searchInstructorByFirstName(String firstName) {
		StringBuilder queryString = new StringBuilder("SELECT new Instructor(i.id,i.user.firstName) FROM  Instructor i WHERE i.active=TRUE AND i.user.firstName LIKE :firstName ");
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("firstName", "%" + firstName + "%");
		
		List<Instructor> listInstructor = query.getResultList(); 
		return listInstructor;
	}

}