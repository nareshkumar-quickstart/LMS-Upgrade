/**
 * 
 */
package com.softech.vu360.lms.repositories.impl;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyOwner;

/**
 * @author muhammad.junaid
 *
 */
public class SurveyRepositoryImpl implements SurveyRepositoryCustom {
	private final static String  SURVEY_PUBLISHED ="PUBLISHED" ;
	private final static String  SURVEY_UNPUBLISHED ="UNPUBLISHED" ;

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public List<Survey> getCurrentSurveyListByCoursesForUser(Long learnerId,Long ownerId) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Survey.getCurrentSurveyListByCoursesForUser");
		query.setParameter("LEARNERID", learnerId);
		query.setParameter("OWNERID", ownerId);
		query.execute();
		List<Survey> out = query.getResultList();
		return out;
	}
	
	@Override
	public Boolean isAlertQueueRequiredProctorApproval(Long tableNameId, String businessKeys) {
		StoredProcedureQuery query;
		int out = 0;
		Object resultSet;
		
		query = entityManager.createNamedStoredProcedureQuery("Survey.isAlertQueueRequiredProctorApproval");
		query.setParameter("tableNameId", tableNameId);
		query.setParameter("businessKeys", businessKeys);
		query.execute();
		
		resultSet = query.getSingleResult();
		
		if(resultSet != null) {
			out = ((Integer)resultSet).intValue();
		}
		return out > 0 ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> getSurveysByOwnerAndSurveyName(SurveyOwner surveyowner,String surveyName, String surveyStatus, String isLocked,String readonly, String event) {
		List<Survey> surveys = null;
		
		StringBuilder jpq = new StringBuilder("SELECT s FROM Survey s WHERE s.owner.id=:surveyownerId AND s.owner.ownerType=:surveyownerType AND s.name LIKE :surveyName");
		//Customer customer=(Customer)surveyowner;
		//whereClause = builder.getField("OWNER_ID").equal(surveyowner.getId()).and(builder.getField("OWNER_TYPE").equal(surveyowner.getOwnerType()));
		//whereClause = builder.getField("OWNER_ID").equal(customer.getId()).and(builder.getField("OWNER_TYPE").equal(customer.getOwnerType()));
		if(event!=null){
			//whereClause=whereClause.and(builder.get("event").equal(Survey.SURVEY_EVENT_MANUAL_CODE));
			jpq.append(" AND s.event=:event");
		}
		if (!surveyStatus.isEmpty()) {
			jpq.append(" AND s.status=:surveyStatus");
			//whereClause=whereClause.and(builder.get("status").equalsIgnoreCase(surveyStatus));
		}

		if(!isLocked.equalsIgnoreCase(Survey.RETIRE_SURVEY.All.toString())){
			if(isLocked.equalsIgnoreCase(Survey.RETIRE_SURVEY.Yes.toString())){
				jpq.append(" AND s.isLocked=1");
				//whereClause=whereClause.and(builder.get("isLocked").equal(true));
			}else if(isLocked.equalsIgnoreCase(Survey.RETIRE_SURVEY.No.toString())){
				jpq.append(" AND s.isLocked=0");
				//whereClause=whereClause.and(builder.get("isLocked").equal(false));
			}
		}
		if(!surveyowner.getOwnerType().equalsIgnoreCase("CUSTOMER") && surveyStatus.isEmpty()){
			if(!readonly.equalsIgnoreCase(Survey.Editable.All.toString())){
				if(readonly.equalsIgnoreCase(Survey.Editable.Yes.toString())){
					jpq.append(" AND s.readonly=1");
					//whereClause=whereClause.and(builder.get("readonly").equal(true));
				}else if(readonly.equalsIgnoreCase(Survey.Editable.No.toString())){
					jpq.append(" AND s.readonly=0");
					//whereClause=whereClause.and(builder.get("readonly").equal(false));
				}
			}
		}else{
			if(!readonly.equalsIgnoreCase(Survey.Editable.All.toString())){
				if(readonly.equalsIgnoreCase(Survey.Editable.Yes.toString())){
					jpq.append(" AND s.readonly=0");
					//whereClause=whereClause.and(builder.get("readonly").equal(false));
				}else if(readonly.equalsIgnoreCase(Survey.Editable.No.toString())){
					jpq.append(" AND s.readonly=1");
					//whereClause=whereClause.and(builder.get("readonly").equal(true));
				}
			}
		}
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("surveyownerId", surveyowner.getId());
		query.setParameter("surveyownerType", surveyowner.getOwnerType());
		query.setParameter("surveyName", "%" + surveyName + "%");
		if(event!=null){
			//whereClause=whereClause.and(builder.get("event").equal(Survey.SURVEY_EVENT_MANUAL_CODE));
			query.setParameter("event", event);
		}
		if (!surveyStatus.isEmpty()){
			query.setParameter("surveyStatus", surveyStatus);
		}
		surveys=query.getResultList();
		return surveys;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> findManualSurveys(String surveyName,String surveyStatus, String retiredSurvey, Long[] ownerIds,int intLimit) {
		List<Survey> surveys = null;
		
		StringBuilder jpq = new StringBuilder("SELECT s FROM Survey s WHERE s.owner.id IN (:ownerIds) AND s.event = :event");
		if ( ! StringUtils.isBlank( surveyStatus ) ) {
			if( surveyStatus.equalsIgnoreCase(SURVEY_PUBLISHED) ||  surveyStatus.equalsIgnoreCase(SURVEY_UNPUBLISHED)){
				//mainClause =  mainClause.and(builder.get("status").equalsIgnoreCase(surveyStatus));
				jpq.append(" AND s.status=:surveyStatus");
			}
		}		 
		
		if ( ! StringUtils.isBlank(surveyName) ) {
			//mainClause = mainClause.and( builder.get("name").likeIgnoreCase("%"+surveyName+"%") );
			jpq.append(" AND s.name LIKE :surveyName");
		}		
		
		if ( ! StringUtils.isBlank(retiredSurvey) ) {
			if( retiredSurvey.equalsIgnoreCase("1")){
				//mainClause = mainClause.and( builder.get("isLocked").equal(RETIRED_YES) );	 
				jpq.append(" AND s.isLocked=1");
			}
			else if( retiredSurvey.equalsIgnoreCase("0")){
				//mainClause = mainClause.and( builder.get("isLocked").equal(RETIRED_NO) );	 
				jpq.append(" AND s.isLocked=0");
			}
		}

		//raq.addAscendingOrdering("name");
		jpq.append(" ORDER BY s.name ASC");
		
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("ownerIds", Arrays.asList(ownerIds));
		//mainClause = mainClause.and(builder.get("event").equal(Survey.SURVEY_EVENT_MANUAL_CODE));
		query.setParameter("event", Survey.SURVEY_EVENT_MANUAL_CODE);

		if ( ! StringUtils.isBlank( surveyStatus ) ) {
			if( surveyStatus.equalsIgnoreCase(SURVEY_PUBLISHED) ||  surveyStatus.equalsIgnoreCase(SURVEY_UNPUBLISHED)){
				//mainClause =  mainClause.and(builder.get("status").equalsIgnoreCase(surveyStatus));
				query.setParameter("surveyStatus", surveyStatus);
			}
		}
		if ( ! StringUtils.isBlank(surveyName) ) {
			query.setParameter("surveyName", "%" + surveyName + "%");
		}
		query.setMaxResults(intLimit);
		surveys=query.getResultList();
		return surveys;
	}
	
	@Override
	public List<Survey> getNonFinishedManualSurveyAssignmentOfLearners(Long learnerId, Long customerId, Long vu360UserId) {
		StoredProcedureQuery proc = entityManager.createNamedStoredProcedureQuery("Survey.SP.GetNonFinishedManualSurveyByLearner");
		proc.setParameter("LEARNERID", learnerId);
		proc.setParameter("OWNERID", customerId);
		proc.setParameter("VU360USERID", vu360UserId);
		
		List<Survey> surveyList = (List<Survey>)proc.getResultList();
		return surveyList;
	}

}
