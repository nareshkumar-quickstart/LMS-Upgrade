/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.util.CustomFieldEntityType;

/**
 * @author marium.saud
 *
 */
public class CustomFieldRepositoryImpl implements CustomFieldRepositoryCustom {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	CustomFieldRepository customFieldRepository;
	
	@Inject
	CustomFieldValueChoiceRepository customFieldValueChoiceRepository;

	@Override
	public List<CustomField> findCustomFieldByCustomFieldEntityTypeAndFieldTypeAndName(
			CustomFieldEntityType customFieldEntityType, String fieldType, String name) {
		
		StringBuilder queryString = new StringBuilder("SELECT cf FROM CustomField cf WHERE cf.active=TRUE ");
				//+ "AND c.shortLicenseName LIKE :shortLicenseName AND c.active=:active ");
		
		switch (customFieldEntityType) {
			case CUSTOMFIELD_CONTACT:
				//whereClause = builder.get("customFieldContext").get("globalContact").equal(true);
				queryString.append(" And cf.customFieldContext.globalContact=TRUE ");
				break;
			case CUSTOMFIELD_REGULATOR:
				//whereClause = builder.get("customFieldContext").get("globalRegulator").equal(true);
				queryString.append(" And cf.customFieldContext.globalRegulator=TRUE ");
				break;
			case CUSTOMFIELD_CREDENTIAL:
				//whereClause = builder.get("customFieldContext").get("globalCredential").equal(true);
				queryString.append(" And cf.customFieldContext.globalCredential=TRUE ");
				break;
			case CUSTOMFIELD_CREDENTIALREQUIREMENT:
				//whereClause = builder.get("customFieldContext").get("globalCredentialRequirement").equal(true);
				queryString.append(" And cf.customFieldContext.globalCredentialRequirement=TRUE ");
				break;
			case CUSTOMFIELD_PROVIDER:
				//whereClause = builder.get("customFieldContext").get("globalProvider").equal(true);
				queryString.append(" And cf.customFieldContext.globalProvider=TRUE ");
				break;
			case CUSTOMFIELD_INSTRUCTOR:
				//whereClause = builder.get("customFieldContext").get("globalInstructor").equal(true);
				queryString.append(" And cf.customFieldContext.globalInstructor=TRUE ");
				break;
			case CUSTOMFIELD_COURSEAPPROVAL:
				//whereClause = builder.get("customFieldContext").get("globalCourseApproval").equal(true);
				queryString.append(" And cf.customFieldContext.globalCourseApproval=TRUE ");
				break;
			case CUSTOMFIELD_PROVIDERAPPROVAL:
				//whereClause = builder.get("customFieldContext").get("globalProviderApproval").equal(true);
				queryString.append(" And cf.customFieldContext.globalProviderApproval=TRUE ");
				break;
			case CUSTOMFIELD_INSTRUCTORAPPROVAL:
				//whereClause = builder.get("customFieldContext").get("globalInstructorApproval").equal(true);
				queryString.append(" And cf.customFieldContext.globalInstructorApproval=TRUE ");
				break;
			case CUSTOMFIELD_CREDENTIALCATEGORY: // LMS-8314
				//whereClause = builder.get("customFieldContext").get("globalCredentialCategory").equal(true);
				queryString.append(" And cf.customFieldContext.globalCredentialCategory=TRUE ");
				break;
			case CUSTOMFIELD_ALL:
				/*whereClause = builder.get("customFieldContext").get("globalContact").equal(true)
						.or(builder.get("customFieldContext").get("globalRegulator").equal(true))
						.or(builder.get("customFieldContext").get("globalCredential").equal(true))
						.or(builder.get("customFieldContext").get("globalCredentialRequirement").equal(true))
						.or(builder.get("customFieldContext").get("globalProvider").equal(true))
						.or(builder.get("customFieldContext").get("globalInstructor").equal(true))
						.or(builder.get("customFieldContext").get("globalCourseApproval").equal(true))
						.or(builder.get("customFieldContext").get("globalProviderApproval").equal(true))
						.or(builder.get("customFieldContext").get("globalCredentialCategory").equal(true)). // LMS-8314
						or(builder.get("customFieldContext").get("globalInstructorApproval").equal(true));*/
				
				queryString.append(" And (cf.customFieldContext.globalContact=TRUE OR "
						+ " cf.customFieldContext.globalRegulator=TRUE OR "
						+ " cf.customFieldContext.globalCredential=TRUE OR "
						+ " cf.customFieldContext.globalCredentialRequirement=TRUE OR "
						+ " cf.customFieldContext.globalProvider=TRUE OR "
						+ " cf.customFieldContext.globalInstructor=TRUE OR "
						+ " cf.customFieldContext.globalCourseApproval=TRUE OR "
						+ " cf.customFieldContext.globalProviderApproval=TRUE OR "
						+ " cf.customFieldContext.globalCredentialCategory=TRUE OR "
						+ " cf.customFieldContext.globalInstructorApproval=TRUE) ");
				
				break;
	
			default:
				System.out.println("Invalid customFieldEntityType ====>>>>>>>");
				break;
		}
		
		if (StringUtils.isNotBlank(name)) {
			queryString.append(" AND cf.fieldLabel LIKE :fieldLabel ");
		}
		
		if (fieldType!=null && !fieldType.isEmpty() && !(fieldType.equalsIgnoreCase("CUSTOMFIELD_ALL") || fieldType.equalsIgnoreCase("ALL"))) {
			queryString.append(" AND cf.fieldType =:fieldType ");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
//		query.setParameter("activeOption", true);
//		query.setParameter("shortLicenseName", "%"+shortLicenseName+"%");

		if (StringUtils.isNotBlank(name)) {
			query.setParameter("fieldLabel", "%" + name + "%");
		}

		if (fieldType!=null && !fieldType.isEmpty() && !(fieldType.equalsIgnoreCase("CUSTOMFIELD_ALL") || fieldType.equalsIgnoreCase("ALL"))) {
			query.setParameter("fieldType", fieldType);
		}
		
		List<CustomField> listCustomField = query.getResultList(); 
		
		
		return listCustomField;
	}

	
	@Override
	public void deleteCustomFields(Long[] customFieldIds) {

		List<Long> lls = Arrays.asList(customFieldIds);
		List<CustomField> ls = (List<CustomField>) customFieldRepository.findAll(lls);
		for (Iterator<CustomField> iterator = ls.iterator(); iterator.hasNext();) {
			CustomField customField = (CustomField) iterator.next();
			List<CustomFieldValueChoice> cvc = customFieldValueChoiceRepository
					.findByCustomFieldOrderByDisplayOrderAsc(customField);
			customFieldValueChoiceRepository.delete(cvc);
		}
		customFieldRepository.delete(ls);

	}
	
	@Override
	public boolean isUniqueCustomFieldName(String entityType, String name) {
		
		StringBuilder queryString = new StringBuilder("SELECT cf FROM CustomField cf WHERE cf.active=TRUE ");
		
		if(entityType.equalsIgnoreCase("REGULATOR")){
			queryString.append("And cf.customFieldContext.globalRegulator=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("CREDENTIALs")){
			queryString.append("And cf.customFieldContext.globalCredential=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("Credential Category")){
			queryString.append("And cf.customFieldContext.globalCredentialCategory=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("PROVIDERS")){
			queryString.append("And cf.customFieldContext.globalCredentialCategory=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("INSTRUCTORS")){
			queryString.append("And cf.customFieldContext.globalCredentialCategory=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("COURSE APPROVALS")){
			queryString.append("And cf.customFieldContext.globalCourseApproval=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("PROVIDER APPROVALS")){
			queryString.append("And cf.customFieldContext.globalProviderApproval=False AND cf.fieldLabel =:fieldLabel ");
		}else if(entityType.equalsIgnoreCase("INSTRUCTOR APPROVALS")){
			queryString.append("And cf.customFieldContext.globalInstructorApproval=False AND cf.fieldLabel =:fieldLabel ");
		}else{
			
			queryString.append("And (cf.fieldLabel Like :fieldLabel And (cf.customFieldContext.globalContact=True or "
					+ "cf.customFieldContext.globalRegulator=True or "
					+ "cf.customFieldContext.globalCredential=True or "
					+ "cf.customFieldContext.globalProvider=True or "
					+ "cf.customFieldContext.globalInstructor=True or "
					+ "cf.customFieldContext.globalCourseApproval=True or "
					+ "cf.customFieldContext.globalProviderApproval=True or "
					+ "cf.customFieldContext.globalInstructorApproval=True ))");
			
			
		}


		Query query = entityManager.createQuery(queryString.toString());

		if(entityType.equalsIgnoreCase("REGULATOR") || entityType.equalsIgnoreCase("CREDENTIALs") ||entityType.equalsIgnoreCase("Credential Category") || 
				entityType.equalsIgnoreCase("PROVIDERS") || entityType.equalsIgnoreCase("INSTRUCTORS") || entityType.equalsIgnoreCase("COURSE APPROVALS") || 
				entityType.equalsIgnoreCase("PROVIDER APPROVALS") || entityType.equalsIgnoreCase("INSTRUCTOR APPROVALS") ){
			query.setParameter("fieldLabel", name );
		}
		else{
			query.setParameter("fieldLabel", "%" + name + "%");
		}
		
		
		List<CustomField> listCustomField = query.getResultList(); 

		boolean retValue = (listCustomField==null || (listCustomField!=null && listCustomField.isEmpty())) ? true: false;

		return retValue;
	}
}
