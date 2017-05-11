package com.softech.vu360.lms.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.ManageUserStatus;
import com.softech.vu360.lms.repositories.AuthorRepositoryCustom;

public class AuthorRepositoryImpl implements AuthorRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public boolean isThisAuthor(String userName) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Author.isThisAuthor");
		query.setParameter("USERNAME", userName);
		query.execute();
		List<Object> out = query.getResultList();
		return out!=null && out.size()>0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isAuthor(Long userId) {		
/*		ExpressionBuilder exprBuilder = new ExpressionBuilder();
		Expression whereClauseExpression = exprBuilder.get("vu360User").get("id").equal(userId);
		return !getTopLinkTemplate().readAll(Author.class, whereClauseExpression).isEmpty();
*/
		Query query = entityManager.createNamedQuery("Author.isAuthor");
		query.setParameter("userId", userId);
		
		List<Author> authorList = query.getResultList();
		if(authorList!=null && authorList.size()>0)
			return true;
		else
			return false;
	}
	
	public List<Object[]> getUserStatus(ManageUserStatus criteria) {
		StringBuffer strBuffSQL= new StringBuffer();
		
		if(criteria==null){
			return null;
		}
		strBuffSQL.append("EXEC [LMS_SELECT_MANAGE_USER_STATUS] ");
		
		//First Name 
		String fname = removeBrace(criteria.getFirstName());
		if(StringUtils.isNotEmpty(fname)) { 
			strBuffSQL.append("[").append(fname).append("]");
		}else{
		    strBuffSQL.append("''");
		}

		
		//Last Name
		strBuffSQL.append(", ");
		String lname = removeBrace(criteria.getLastName());
		if(StringUtils.isNotEmpty(lname)) { 
			strBuffSQL.append("[").append(lname).append("] ");
		}else{
		    strBuffSQL.append("''");
		}

		//Email Address
		strBuffSQL.append(", ");
		String email = removeBrace(criteria.getEmailAddress());
		if(StringUtils.isNotEmpty(email)) { 
			strBuffSQL.append("[").append(email).append("] ");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}

		//Course ID
		strBuffSQL.append(", ");
		String cid = removeBrace(criteria.getCourseId());
		if(StringUtils.isNotEmpty(cid)) { 
			strBuffSQL.append("[").append(cid).append("] ");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}

		//Holding Regulator
		strBuffSQL.append(", ");
		if(criteria.getHoldingRegulatorId()!=null && criteria.getHoldingRegulatorId()>0) { 
			strBuffSQL.append("").append(criteria.getHoldingRegulatorId()).append("");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}

		//Holding Regulator Category
		strBuffSQL.append(", ");
		if(criteria.getRegulatoryCategoryId()!=null && criteria.getRegulatoryCategoryId()>0) { 
			strBuffSQL.append("").append(criteria.getRegulatoryCategoryId()).append("");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}
		
		//CourseType
		strBuffSQL.append(", ");
		if(criteria.getCourseTypeId()!=null && criteria.getCourseTypeId().intValue()>0) {
			
			if(criteria.getCourseTypeId()==1){ //use constant
				strBuffSQL.append("").append("1").append(", ");
				strBuffSQL.append("").append("null").append(",");
				strBuffSQL.append("").append("null").append("");
			}
			
			if(criteria.getCourseTypeId()==2){
				strBuffSQL.append("").append("null").append(", ");
				strBuffSQL.append("").append("2").append(", ");
				strBuffSQL.append("").append("null").append("");
			}
			if(criteria.getCourseTypeId()==3){
				strBuffSQL.append("").append("null").append(", ");
				strBuffSQL.append("").append("NULL").append(", ");
				strBuffSQL.append("").append("3").append("");
			}

		}else{
		    strBuffSQL.append("").append("null").append(", ");
		    strBuffSQL.append("").append("null").append(",");
		    strBuffSQL.append("").append("null").append("");

		}

		//CourseStatus
		strBuffSQL.append(", ");
		if( StringUtils.isNotEmpty(criteria.getCourseStatus()) && !criteria.getCourseStatus().equals("0")) { 
			strBuffSQL.append("'").append(criteria.getCourseStatus()).append("'");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}
		
		//Affidavit Type FILE
		strBuffSQL.append(", ");
		if( StringUtils.isNotEmpty(criteria.getAffidavitType()) && !criteria.getAffidavitType().equals("0")) {
			strBuffSQL.append("'").append(criteria.getAffidavitType()).append("'");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}

		
		//Start Date
		strBuffSQL.append(", ");
		if(StringUtils.isNotEmpty(criteria.getStartDate())) { 
			strBuffSQL.append("'").append(criteria.getStartDate()).append("'");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}

		//End Date
		strBuffSQL.append(", ");
		if(StringUtils.isNotEmpty(criteria.getEndDate())) { 
			strBuffSQL.append("'").append(criteria.getEndDate()).append("'");
		}else{
		    strBuffSQL.append("").append("null").append("");
		}

		Query qry=entityManager.createNativeQuery(strBuffSQL.toString());
		return qry.getResultList();
	
	}
	
	private static String removeBrace(String val){
		if(StringUtils.isNotEmpty(val)){
			val = val.replaceAll("\\[", "");
			return val = StringUtils.trim(val.replaceAll("\\]", ""));
		}
		return val;
	}
	
	public Author getAuthorByUsername(String userName) {
		Query query = entityManager.createNamedQuery("Author.isAuthor");
		query.setParameter("userId", userName);
		
		List<Author> authorList = query.getResultList();
		if(authorList!=null && authorList.size()>0)
			return authorList.get(0);
		else
			return null;
	}


}
