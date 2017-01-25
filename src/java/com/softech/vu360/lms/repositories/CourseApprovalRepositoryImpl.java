package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

public class CourseApprovalRepositoryImpl implements CourseApprovalRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * SELECT COUNT(t0.ID) FROM REGULATORYAPPROVAL t0, COURSE t2, COURSEAPPROVAL t1 
	 * WHERE (((((t2.ID = ?) 
	 * AND (((? BETWEEN t1.COURSEAPPROVALEFFECTIVELYSTARTDATE AND t1.COURSEAPPROVALEFFECTIVELYENDSDATE) 
	 * OR (? BETWEEN t1.COURSEAPPROVALEFFECTIVELYSTARTDATE AND t1.COURSEAPPROVALEFFECTIVELYENDSDATE)) 
	 * OR ((t1.COURSEAPPROVALEFFECTIVELYSTARTDATE >= ?) AND (t1.COURSEAPPROVALEFFECTIVELYENDSDATE <= ?)))) 
	 * AND (t0.ID <> ?)) AND ((t1.ID = t0.ID) AND (t0.APPROVALTYPE = ?))) AND (t2.ID = t1.COURSE_ID))
	 */
	@Override
	public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(Long courseId, Long regulatorCategoryId,
			Date startDate, Date endDate, Long excludeCourseApprovalId) {

		/*StringBuilder queryString = new StringBuilder("SELECT count(c.id) FROM CourseApproval c WHERE c.Course.Id=:courseId ");
	
		queryString.append(" AND :startDate  Between courseApprovalEffectivelyStartDate And courseApprovalEffectivelyEndsDate ");
		
		if(excludeCategoryId > 0){
			queryString.append(" AND c.id <>:excludeCategoryId ");
		}
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("regulatorId", regulatorId);
		
		if (excludeCategoryId > 0){
			query.setParameter("excludeCategoryId", excludeCategoryId);
		}
		
		Long count = (Long)query.getSingleResult();*/

		
		return false;
	}

	@Override
	public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(Long courseId, Long regulatorCategoryId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	@Override
	public int getNumberOfUnusedPurchaseCertificateNumbers(Long courseApprovalId) {
		/*select count(*) from courseapproval ca inner join courseapproval_purchasecertificate capc on ca.id = capc.courseapprovalid 
		inner join purchasecertificate pc on pc.id = capc.purchasecertificatenumberid where pc.isused != 1 and ca.id = 13805*/

		StringBuilder queryString = new StringBuilder("SELECT count(c) FROM CourseApproval c join c.purchaseCertificateNumbers pcn WHERE c.id =:courseApprovalId  ");
		queryString.append(" and pcn.isUsed = False ");
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("courseApprovalId", courseApprovalId);
		
		Long count = (Long)query.getSingleResult();
		
		return count.intValue();
	}

	@Override
	public boolean isCertificateLinkedWithCourseApproval(List<Long> certificateIds) {

		/*
		SELECT COUNT(t0.ID) FROM REGULATORYAPPROVAL t0, ASSET t2, COURSEAPPROVAL t1 
		WHERE (((t2.ID IN (?)) AND ((t1.ID = t0.ID) AND (t0.APPROVALTYPE = ?))) AND ((t2.ID = t1.ASSET_ID) AND (t2.ASSETTYPE = ?)))
		*/
		
		StringBuilder queryString = new StringBuilder("SELECT count(c) FROM CourseApproval c WHERE c.certificate.id In :certificateIds  ");
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("certificateIds", certificateIds);
		
		Long count = (Long)query.getSingleResult();
		
		return count.longValue()>0;
		
		
	}

	@Override
	public boolean isAffidavitLinkedWithCourseApproval(List<Long> certificateIds) {
		
		/*
		SELECT COUNT(t0.ID) FROM REGULATORYAPPROVAL t0, ASSET t2, COURSEAPPROVAL t1 
		WHERE (((t2.ID IN (?)) AND ((t1.ID = t0.ID) AND (t0.APPROVALTYPE = ?))) AND ((t2.ID = t1.AFFIDAVIT_ID) AND (t2.ASSETTYPE = ?)))
		*/
		
		StringBuilder queryString = new StringBuilder("SELECT count(c) FROM CourseApproval c WHERE c.affidavit.id In :certificateIds  ");
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("certificateIds", certificateIds);
		
		Long count = (Long)query.getSingleResult();
		
		return count.longValue()>0;
	}

	@Override
	public List<Map<Object, Object>> getCourseApprovalByCourse(String varCourseId, String varLearnerEnrollmentId) {
		try {
			
			//varCourseId = "8db46b2b3ff84fd1adde051fe0df2798";
			//varLearnerEnrollmentId = "910442";
			
			StringBuilder queryString = new StringBuilder("EXEC [SELECT_COURSE_COURSEAPPROVAL] '" + varCourseId + "','"+ varLearnerEnrollmentId + "'"); 
			Query query = entityManager.createNativeQuery(queryString.toString());
			
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList(); 
			List<Map<Object, Object>> results =new   ArrayList<Map<Object,Object>>();
			results.addAll(rows);
			
			return results;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}


}
