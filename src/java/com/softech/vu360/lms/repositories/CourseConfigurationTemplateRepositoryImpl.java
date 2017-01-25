package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.softech.vu360.lms.model.CourseConfigurationTemplate;

public class CourseConfigurationTemplateRepositoryImpl implements CourseConfigurationTemplateRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<CourseConfigurationTemplate> findByNameAndLastUpdateDate(String name, Date lastUpdatedDate) {
		try{
			StringBuilder queryString = new StringBuilder("SELECT c FROM CourseConfigurationTemplate c "
					+ "WHERE c.active =TRUE AND c.name LIKE :name ");
		
			if (lastUpdatedDate != null && !lastUpdatedDate.toString().isEmpty()) {
				queryString.append(" AND c.lastUpdatedDate =:lastUpdatedDate");
			}
			
			Query query = entityManager.createQuery(queryString.toString());
			query.setParameter("name", "%" + name + "%");
			
	
			if (lastUpdatedDate != null && !lastUpdatedDate.toString().isEmpty()) {
				query.setParameter("lastUpdatedDate", lastUpdatedDate);
			}
			
			List<CourseConfigurationTemplate> listCourseConfigurationTemplate = query.getResultList(); 
			
			return listCourseConfigurationTemplate;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<CourseConfigurationTemplate> findByNameAndLastUpdateDateAndContentOwnerId(String name,
			Date lastUpdatedDate, Long contentOwnerId) {
		try{
			StringBuilder queryString = new StringBuilder("SELECT c FROM CourseConfigurationTemplate c "
					+ "WHERE c.active =TRUE AND c.name LIKE :name AND c.contentOwner.id =:contentOwnerId ");
		
			if (lastUpdatedDate != null && !lastUpdatedDate.toString().isEmpty()) {
				queryString.append(" AND c.lastUpdatedDate =:lastUpdatedDate");
			}
			
			Query query = entityManager.createQuery(queryString.toString());
			query.setParameter("name", "%" + name + "%");
			query.setParameter("contentOwnerId", contentOwnerId);
			
			
	
			if (lastUpdatedDate != null && !lastUpdatedDate.toString().isEmpty()) {
				query.setParameter("lastUpdatedDate", lastUpdatedDate);
			}
			
			List<CourseConfigurationTemplate> listCourseConfigurationTemplate = query.getResultList(); 
			
			return listCourseConfigurationTemplate;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Boolean findviewAssessmentResults(Long learnerEnrollmentId){
			Query query = entityManager.createNativeQuery("EXEC ICP_SELECT_ASSESSMENTITEM_RESULTS :learnerEnrollmentId, :POSTASSESSMENT");
			query.setParameter("learnerEnrollmentId", learnerEnrollmentId);
			query.setParameter("POSTASSESSMENT", "POSTASSESSMENT");
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList();  
			
			if(rows != null){
				if(rows.size() > 0){
					return true;
				}
				else
					return false;
			}
			else
				return false;
	}
}
