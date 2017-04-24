package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;

public class LearnerEnrollmentRepositoryImpl implements LearnerEnrollmentRepositoryCustom{

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public Long getEnrollmentCountBySynchronousClassId(Long synchronousClassId) {
		Long count = 0l;
		StringBuilder builder = new StringBuilder();

		builder.append("SELECT count(p) from LearnerEnrollment p where p.synchronousClass.id = :synchronousClassId and p.enrollmentStatus = 'ACTIVE'");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("synchronousClassId", synchronousClassId);
		count=(Long)query.getSingleResult();
		return count;
	}

	@Override
	public List<LearnerEnrollment> getEnrollmentsByProctor(Proctor proctor,
			String firstName, String lastName, String emailAddress,
			Date startDate, Date endDate, String courseTitle, String[] status) {
		List<LearnerEnrollment> learnerEnrollmentList=null;
		
		StringBuilder builder = new StringBuilder("SELECT DISTINCT p , p.courseStatistics.completionDate, p.learner.vu360User.lastName FROM LearnerEnrollment p");
		builder.append(" JOIN p.proctorEnrollment d Where ");
		
		builder.append("p.courseStatistics.completionDate IS NOT NULL ");
		builder.append("AND p.courseStatistics.completed = true ");
		
		if(firstName != null && !StringUtils.isEmpty(firstName))
			builder.append("AND UPPER(p.learner.vu360User.firstName) LIKE UPPER(:firstName) ");
		
		if(lastName != null && !StringUtils.isEmpty(lastName))
			builder.append("AND UPPER(p.learner.vu360User.lastName) LIKE UPPER(:lastName) ");
			
		if(emailAddress != null && !StringUtils.isEmpty(emailAddress))
			builder.append("AND UPPER(p.learner.vu360User.emailAddress) LIKE UPPER(:emailAddress) ");
		
		if(startDate != null && endDate != null)
			builder.append("AND p.courseStatistics.completionDate BETWEEN :startDate AND :endDate");
		
		if(startDate != null && endDate == null)
			builder.append(" AND p.courseStatistics.completionDate >= :startDate");
		
		if(startDate == null && endDate != null )
			builder.append(" AND p.courseStatistics.completionDate <= :endDate");
		
		if(courseTitle != null && !StringUtils.isEmpty(courseTitle))
			builder.append(" AND UPPER(p.course.courseTitle) LIKE UPPER(:courseTitle) ");
		
		builder.append("AND d.proctor.id = :proctorId ");
	
		if(status != null){
			builder.append(" AND p.courseStatistics.status IN (:status) ");
		}
		
		builder.append("order by p.courseStatistics.completionDate desc, p.learner.vu360User.lastName asc");
		System.out.println(builder.toString());
		Query query = entityManager.createQuery(builder.toString());
		
		if(firstName != null && !StringUtils.isEmpty(firstName)){
			query.setParameter("firstName", firstName);
		}
		if(lastName != null && !StringUtils.isEmpty(lastName)){
			query.setParameter("lastName", lastName);
		}
		
		if(emailAddress != null && !StringUtils.isEmpty(emailAddress)){
			query.setParameter("emailAddress", emailAddress);
		}
		if(startDate != null && endDate != null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		if(startDate != null && endDate == null){
			query.setParameter("startDate", startDate);
		}
		if(startDate == null && endDate != null ){
			query.setParameter("endDate", endDate);
		}
		if(courseTitle != null && !StringUtils.isEmpty(courseTitle)){
			query.setParameter("courseTitle", courseTitle);
		}
		if(status != null){
			query.setParameter("status", status);
		}
		query.setParameter("proctorId", proctor.getId());
		learnerEnrollmentList=query.getResultList();
		return learnerEnrollmentList;
	}

	@Override
	public List<LearnerEnrollment> findByLearnerIdAndEnrollmentStatusNotIn(Long learnerId,List<String> status) {
		StringBuilder builder = new StringBuilder();
		builder.append("select new LearnerEnrollment(le.enrollmentStartDate,le.course.id,le.course.courseGUID) from LearnerEnrollment le JOIN le.courseStatistics cs where le.learner.id= :learnerId and le.enrollmentStatus not IN (:status)");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("learnerId", learnerId);
		query.setParameter("status", status);
		List<LearnerEnrollment> LearnerEnrollment = query.getResultList();
		return LearnerEnrollment;
	}
}
