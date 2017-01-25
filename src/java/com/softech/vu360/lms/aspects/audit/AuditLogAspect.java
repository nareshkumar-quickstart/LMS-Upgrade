package com.softech.vu360.lms.aspects.audit;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseApprovalAudit;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldAudit;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderAudit;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.RegulatorCategoryAudit;
import com.softech.vu360.lms.model.RegulatorCategoryCreditReportingFieldAudit;
import com.softech.vu360.lms.model.RegulatoryApprovalAudit;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.CourseApprovalAuditRepository;
import com.softech.vu360.lms.repositories.CourseApprovalRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldAuditRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldRepository;
import com.softech.vu360.lms.repositories.ProviderAuditRepository;
import com.softech.vu360.lms.repositories.ProviderRepository;
import com.softech.vu360.lms.repositories.RegulatorCategoryAuditRepository;
import com.softech.vu360.lms.repositories.RegulatorCategoryCreditReportingFieldAuditRepository;
import com.softech.vu360.lms.repositories.RegulatorCategoryRepository;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Properties;

/**
 * 
 * @author haider.ali, abdullah.mashood
 * 
 */

@Aspect
public class AuditLogAspect implements IAuditLogAspect {

	@Inject
	RegulatorCategoryCreditReportingFieldAuditRepository regulatorCategoryCreditReportingFieldAuditRepository;
	
	@Inject
	RegulatorCategoryRepository regulatorCategoryRepository;
	@Inject
	RegulatorCategoryAuditRepository regulatorCategoryAuditRepository;
	@Inject
	CourseApprovalRepository courseApprovalRepository;
	@Inject
	CreditReportingFieldRepository creditReportingFieldRepository;
	@Inject
	CreditReportingFieldAuditRepository creditReportingFieldAuditRepository;
	@Inject
	CourseApprovalAuditRepository courseApprovalAuditRepository;
	@Inject
	ProviderRepository providerRepository;
	@Inject
	ProviderAuditRepository providerAuditRepository;
	
	private static final Logger log = Logger.getLogger(AuditLogAspect.class);
	private static final String DELETE_OPERATION = "Delete";
	private static final String ADD_OPERATION = "Add";

	@Around(PointCutProvider)
	public void logProviderAround(ProceedingJoinPoint joinPoint) throws Throwable {

		Boolean isAddOpertion = new Boolean(false);
		try {
			
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Long logginUserID = loggedInUser.getId();
				Object[] argArray = joinPoint.getArgs();
				
				if (argArray != null && argArray.length > 0) {
					Provider provider = (Provider) argArray[0];
	
					if (provider.getId() == null) {
						isAddOpertion = true;
						Provider pro = (Provider) joinPoint.proceed();
						this.saveProviderAudit(pro, "Add", logginUserID);
					} else {
						//Provider pro = audit_accreditationDAO.getProviderById(provider.getId());
						Provider pro = providerRepository.findOne(provider.getId());
						
						this.saveProviderAudit(pro, "update", logginUserID);
					}
				}
			}else{
				log.error(" ** SecurityContextHolder.getContext().getAuthentication().getPrincipal() unable to retrive VU360User Object **");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e,"provider");
		}finally{
			if(!isAddOpertion)	
			  joinPoint.proceed();
		}
		
	}

/*	@Around(PointCutVU360User)
	public void logVU360User(ProceedingJoinPoint joinPoint) throws Throwable {
		try {			
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof VU360User) {
				VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Long loginUserID = loggedInUser.getId();
				Object[] argArray = joinPoint.getArgs();
				
				if (argArray != null && argArray.length > 0) {
					VU360User user = (VU360User) argArray[0];
	
					if (user.getId() != null) {//only edit case we need to log
						VU360User userToAudit = audit_VU360UserDAO.getUserById(user.getId());
						audit_VU360UserDAO.saveVU360UserAudit(userToAudit, "update", loginUserID);
					}
				}
			}else{
				log.error(" ** SecurityContextHolder.getContext().getAuthentication().getPrincipal() unable to retrive VU360User Object **");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e,"vu360User");
		}finally{	
			  joinPoint.proceed();
		}
		
	}
*/
	
	@Around(PointCutRegulatorCategory)
	public void logRegulatorCategory(ProceedingJoinPoint joinPoint)
			throws Throwable {

		Boolean isAddOpration = new Boolean (false);

		try {

			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
				Object[] argArray = joinPoint.getArgs();
				if (argArray != null && argArray.length > 0) {
	
					RegulatorCategory regulatorCategory1 = (RegulatorCategory) argArray[0];
	
					if (regulatorCategory1.getId() == null) {
						isAddOpration = true;
						RegulatorCategory regulatorCategory = (RegulatorCategory) joinPoint.proceed();
						//audit_accreditationDAO.saveRegulatorCategoryAudit(regulatorCategory, "Add", loggedInUser.getId());
						this.saveRegulatorCategoryAudit(regulatorCategory, "Add", loggedInUser.getId());
	
					} else {
						//RegulatorCategory regulatorCategory = audit_accreditationDAO.getRegulatorCategory(regulatorCategory1.getId());
						RegulatorCategory regulatorCategory = regulatorCategoryRepository.findOne(regulatorCategory1.getId());
						//audit_accreditationDAO.saveRegulatorCategoryAudit( regulatorCategory, "update", loggedInUser.getId());
						this.saveRegulatorCategoryAudit( regulatorCategory, "update", loggedInUser.getId());
						
						joinPoint.proceed();
					}
				}
			}else{
				log.error(" ** SecurityContextHolder.getContext().getAuthentication().getPrincipal() unable to retrive VU360User Object **");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e,"RegulatorCategory");
		}
		//do not run in case of add operation.
		if(!isAddOpration){
			joinPoint.proceed();
		}

	}

	@Around(PointCutCourseApproval)
	public void CourseApprovallogAround(ProceedingJoinPoint joinPoint) throws Throwable {

			Boolean isAddOpration = new Boolean (false);
		try {
			
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
				
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Long loginUserID = loggedInUser.getId();
				Object[] argArray = joinPoint.getArgs();
				if (argArray != null && argArray.length > 0) {
					CourseApproval courseapproval = (CourseApproval) argArray[0];
					Long cid = courseapproval.getId();
					
					if (cid == null) {
						isAddOpration = true;
						CourseApproval ca = (CourseApproval) joinPoint.proceed();
						this.saveCourseApprovalAudit(ca,"Add", loginUserID);
					} else {
						//CourseApproval ca = audit_accreditationDAO.getCourseApprovalById(cid);
						CourseApproval ca = courseApprovalRepository.findByIdAndDeletedFalse(cid);
						
						this.saveCourseApprovalAudit(ca,"update", loginUserID);
					}
				}
			}else{
				log.error(" ** SecurityContextHolder.getContext().getAuthentication().getPrincipal() unable to retrive VU360User Object **");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e,"CourseApproval");
		}finally{
			//do not run in case of add operation.
			if(!isAddOpration){
				joinPoint.proceed();
			}
		}
	}

	@Around(PointCutAssignCreditReportingFields)
	public void logCreditReportingField(ProceedingJoinPoint joinPoint)
			throws Throwable {

		try {
			
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
			
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
				Object[] argArray = joinPoint.getArgs();
				if (argArray != null && argArray.length > 0) {
	
					RegulatorCategory regulatorCategory = (RegulatorCategory) argArray[0];
					//audit_accreditationDAO.assignUnAssignCreditReportingFieldAudit(category, "Add", loggedInUser.getId());
					
					List<CreditReportingField> reportingFieldList = regulatorCategory.getReportingFields();
					
					if (reportingFieldList!=null && !reportingFieldList.isEmpty()){
						List<RegulatorCategoryCreditReportingFieldAudit> crfList = new ArrayList<RegulatorCategoryCreditReportingFieldAudit>();
						RegulatorCategoryCreditReportingFieldAudit crfa = null;
						
						for (int i = 0; i < reportingFieldList.size(); i++) {
							CreditReportingField crf = reportingFieldList.get(i);
							crfa = new RegulatorCategoryCreditReportingFieldAudit();
							crfa.setCreditReportingFieldId(crf.getId());
							crfa.setRegulatorCategoryId(regulatorCategory.getId());
							crfa.setOperation(ADD_OPERATION);
							crfa.setCreatedBy(loggedInUser.getId());
							crfa.setCreatedOn(new Date(System.currentTimeMillis()));
							crfa.setUpdatedOn(new Date(System.currentTimeMillis()));
							crfa.setUpdatedBy(loggedInUser.getId());
							crfList.add(crfa);
						}

						log.debug("saving regulator Category Creadit Reporting fields Audit in from dao..");
						
						regulatorCategoryCreditReportingFieldAuditRepository.save(crfList);
					}
					else{ // if all CRF are deleted.
						

						//RegulatorCategory dbRegulatorCategory = getRegulatorCategory(regulatorCategory.getId());
						RegulatorCategory dbRegulatorCategory = regulatorCategoryRepository.findOne(regulatorCategory.getId());
						List<CreditReportingField> db_reportingFieldList = dbRegulatorCategory.getReportingFields();
						if(dbRegulatorCategory.getReportingFields().size()>0){
							List<RegulatorCategoryCreditReportingFieldAudit> crfList = new ArrayList<RegulatorCategoryCreditReportingFieldAudit>();
							RegulatorCategoryCreditReportingFieldAudit crfa = null;
							
							for (int i = 0; i < db_reportingFieldList.size(); i++) {
								CreditReportingField crf = db_reportingFieldList.get(i);
								crfa = new RegulatorCategoryCreditReportingFieldAudit();
								crfa.setCreditReportingFieldId(crf.getId());
								crfa.setRegulatorCategoryId(regulatorCategory.getId());
								crfa.setOperation(ADD_OPERATION);
								crfa.setCreatedBy(loggedInUser.getId());
								crfa.setCreatedOn(new Date(System.currentTimeMillis()));
								crfa.setUpdatedOn(new Date(System.currentTimeMillis()));
								crfa.setUpdatedBy(loggedInUser.getId());
								crfList.add(crfa);
							}
							
							log.debug("saving regulator Category Creadit Reporting fields Audit in from dao..");
										
							regulatorCategoryCreditReportingFieldAuditRepository.save(crfList);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e,"CreditReportingAdd");

		}finally{
			joinPoint.proceed();
		}
	}

	
	/**
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around(PointCutUnAssignCreditReportingFields)
	public void logDropCreditReportingField(ProceedingJoinPoint joinPoint)
			throws Throwable {

		try {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
			
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Object[] argArray = joinPoint.getArgs();
				if (argArray != null && argArray.length > 0) {
	
					RegulatorCategory regulatorCategory = (RegulatorCategory) argArray[0];
					//audit_accreditationDAO.assignUnAssignCreditReportingFieldAudit(category, "Delete", loggedInUser.getId());
					
					List<CreditReportingField> reportingFieldList = regulatorCategory.getReportingFields();
					
					if (reportingFieldList!=null && !reportingFieldList.isEmpty()){
						List<RegulatorCategoryCreditReportingFieldAudit> crfList = new ArrayList<RegulatorCategoryCreditReportingFieldAudit>();
						RegulatorCategoryCreditReportingFieldAudit crfa = null;
						
						for (int i = 0; i < reportingFieldList.size(); i++) {
							CreditReportingField crf = reportingFieldList.get(i);
							crfa = new RegulatorCategoryCreditReportingFieldAudit();
							crfa.setCreditReportingFieldId(crf.getId());
							crfa.setRegulatorCategoryId(regulatorCategory.getId());
							crfa.setOperation(DELETE_OPERATION);
							crfa.setCreatedBy(loggedInUser.getId());
							crfa.setCreatedOn(new Date(System.currentTimeMillis()));
							crfa.setUpdatedOn(new Date(System.currentTimeMillis()));
							crfa.setUpdatedBy(loggedInUser.getId());
							crfList.add(crfa);
						}

						log.debug("saving regulator Category Creadit Reporting fields Audit in from dao..");
						
						regulatorCategoryCreditReportingFieldAuditRepository.save(crfList);
					}
					else{ // if all CRF are deleted.
						

						//RegulatorCategory dbRegulatorCategory = getRegulatorCategory(regulatorCategory.getId());
						RegulatorCategory dbRegulatorCategory = regulatorCategoryRepository.findOne(regulatorCategory.getId());
						List<CreditReportingField> db_reportingFieldList = dbRegulatorCategory.getReportingFields();
						if(dbRegulatorCategory.getReportingFields().size()>0){
							List<RegulatorCategoryCreditReportingFieldAudit> crfList = new ArrayList<RegulatorCategoryCreditReportingFieldAudit>();
							RegulatorCategoryCreditReportingFieldAudit crfa = null;
							
							for (int i = 0; i < db_reportingFieldList.size(); i++) {
								CreditReportingField crf = db_reportingFieldList.get(i);
								crfa = new RegulatorCategoryCreditReportingFieldAudit();
								crfa.setCreditReportingFieldId(crf.getId());
								crfa.setRegulatorCategoryId(regulatorCategory.getId());
								crfa.setOperation(DELETE_OPERATION);
								crfa.setCreatedBy(loggedInUser.getId());
								crfa.setCreatedOn(new Date(System.currentTimeMillis()));
								crfa.setUpdatedOn(new Date(System.currentTimeMillis()));
								crfa.setUpdatedBy(loggedInUser.getId());
								crfList.add(crfa);
							}
							
							log.debug("saving regulator Category Creadit Reporting fields Audit in from dao..");
										
							regulatorCategoryCreditReportingFieldAuditRepository.save(crfList);
						}
					}
	
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e,"CreditReportingDrop");

		}finally{
			joinPoint.proceed();
		}

	}

	/**
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around(PointCutDeleteRegulatorCategories)
	public void logRegulatorCategoriest(ProceedingJoinPoint joinPoint)
			throws Throwable {

		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Object[] argArray = joinPoint.getArgs();
			if (argArray != null && argArray.length > 0) {

				List<RegulatorCategory> category = (List<RegulatorCategory>) argArray[0];
				
				if(category != null && category.size() > 0) {
					List<RegulatorCategoryAudit> listRegulatorCategoryAudit = new ArrayList<RegulatorCategoryAudit>();
					RegulatorCategoryAudit regulatorCategoryAudit = null; 
							
					for (Iterator<RegulatorCategory> iterator = category.iterator(); iterator.hasNext();) {
						
						RegulatorCategory rc = iterator.next();

						regulatorCategoryAudit = new RegulatorCategoryAudit();
						BeanUtils.copyProperties(rc, regulatorCategoryAudit );
						regulatorCategoryAudit.setRegulatorCategoryAuditId(rc.getId());
						regulatorCategoryAudit.setRegulatorId(rc.getRegulator().getId());
						regulatorCategoryAudit.setId(null);// FOR INSERTING NEW RECORD EVERYTIME.
						regulatorCategoryAudit.setOperation("Delete");
						regulatorCategoryAudit.setUpdatedOn(new Date(System.currentTimeMillis()));
						regulatorCategoryAudit.setUpdatedBy(loggedInUser.getId());
						listRegulatorCategoryAudit.add(regulatorCategoryAudit);
						

					}
					
					regulatorCategoryAuditRepository.save(listRegulatorCategoryAudit);
				}
				

			}
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.toString());
			sendmail(e, "RegulatorCategories");

		}finally{
			joinPoint.proceed();
		}

	}	
	private static void sendmail(Exception errors,String method) {

		try {
			SendMailService
					.sendSMTPMessage(
							new String[] { VU360Properties
									.getVU360Property("lms.email.globalException.toAddress") },
							null,
							VU360Properties
									.getVU360Property("lms.email.globalException.fromAddress"),
							VU360Properties
									.getVU360Property("lms.email.globalException.title"+"Audit Log ("+method+")"),
							VU360Properties
									.getVU360Property("lms.email.globalException.subject"),
							"<br>" + ExceptionUtils.getFullStackTrace(errors) + "</div>");

		} catch (Exception e) {
			log.info("Audid Logging Failed");
			e.printStackTrace();
		}

	}
	
	@Around(PointCutReportingFields)
	public void auditLogApprovalsReportingField(ProceedingJoinPoint joinPoint) throws Throwable{
		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Object[] args = joinPoint.getArgs();
			if(args != null && args.length > 0){
				com.softech.vu360.lms.model.CreditReportingField updatedCRF = (com.softech.vu360.lms.model.CreditReportingField)args[0];
				if(updatedCRF.getId() == null){
					updatedCRF = (com.softech.vu360.lms.model.CreditReportingField)joinPoint.proceed();
					this.saveAprovalsReportingFieldAudit(null,updatedCRF,  "ADD", loggedInUser.getId());
				}else{
					//CreditReportingField oldCRF = (com.softech.vu360.lms.model.CreditReportingField)audit_accreditationDAO.getCreditReportingFieldById(updatedCRF.getId());
					CreditReportingField oldCRF = creditReportingFieldRepository.findOne(updatedCRF.getId());
					
					this.saveAprovalsReportingFieldAudit(oldCRF,updatedCRF,  "UPDATE", loggedInUser.getId());
					joinPoint.proceed();
				}
			}			
			System.out.println("Testing Statement");	
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}finally{
			
		}		
	}

	private RegulatorCategoryAudit saveRegulatorCategoryAudit(RegulatorCategory regulatorCategory, String operation, Long createdORmodifiedByUser ){
		
		RegulatorCategoryAudit regulatorCategoryAudit= null; 
		
		if(regulatorCategory != null) {
			
			regulatorCategoryAudit = new RegulatorCategoryAudit();
			BeanUtils.copyProperties(regulatorCategory, regulatorCategoryAudit );
			regulatorCategoryAudit.setRegulatorCategoryAuditId(regulatorCategory.getId());
			regulatorCategoryAudit.setRegulatorId(regulatorCategory.getRegulator().getId());
			regulatorCategoryAudit.setOperation(operation);
			regulatorCategoryAudit.setId(null);// FOR INSERTING NEW RECORD EVERYTIME.
			
			//preserve Regulator Categories.
			if(operation.equalsIgnoreCase("Add")){
				regulatorCategoryAudit.setCreatedBy(createdORmodifiedByUser);
				regulatorCategoryAudit.setCreatedOn(new Date(System.currentTimeMillis()));
				regulatorCategoryAudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				regulatorCategoryAudit.setUpdatedBy(createdORmodifiedByUser);
				
			}else{
				regulatorCategoryAudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				regulatorCategoryAudit.setUpdatedBy(createdORmodifiedByUser);
			}
			
			regulatorCategoryAudit = regulatorCategoryAuditRepository.save(regulatorCategoryAudit);
			
		}
		
		
		return regulatorCategoryAudit;
		
		
	}

	
	private CreditReportingFieldAudit saveAprovalsReportingFieldAudit(CreditReportingField oldObject, CreditReportingField newObject, String operation, Long modifingUser){
		
		CreditReportingFieldAudit crfAudit = null;
		
		if(newObject!=null){
			List<CreditReportingFieldAudit> listCreditReportingFieldAudit = creditReportingFieldAuditRepository.findByCreditReportingFieldIdOrderByIdDesc(newObject.getId());
			if(listCreditReportingFieldAudit!=null && listCreditReportingFieldAudit.size()>0){
				crfAudit = listCreditReportingFieldAudit.get(0);
			}
		}
		
		
		boolean isRecordUpdated = false;
		try{			
			if (operation.equals("ADD") || crfAudit == null) {
				if(crfAudit == null){
					crfAudit = new CreditReportingFieldAudit();					
				}
				BeanUtils.copyProperties(newObject, crfAudit);
				crfAudit.setCreditReportingFieldId(newObject.getId());
				crfAudit.setCreatedBy(modifingUser);
				crfAudit.setCreatedOn(Calendar.getInstance().getTime());
				crfAudit.setFieldType(newObject.getClass().getSimpleName().toUpperCase());	
				isRecordUpdated = true;
			}else{
				isRecordUpdated = saveAuditObject(crfAudit, oldObject, newObject);
			}
			
			crfAudit.setId(null);
			crfAudit.setCreditReportingFieldId(newObject.getId());			
			crfAudit.setOperation(operation);
			crfAudit.setUpdatedBy(modifingUser);
			crfAudit.setUpdatedOn(Calendar.getInstance().getTime());
			
			if(isRecordUpdated){
				/*getSession().getIdentityMapAccessor().invalidateObject(crfAudit);
				crfAudit = (CreditReportingFieldAudit) getTopLinkTemplate().deepMerge(crfAudit);*/
				creditReportingFieldAuditRepository.save(crfAudit);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return crfAudit;
	}
	
	private boolean saveAuditObject(Object auditObject, Object oldBusinessObject, Object newBusinessObject) throws Exception {
		Class newBusinessObjectClass = newBusinessObject.getClass();
		PropertyDescriptor propDesc = null;
		boolean isRecordUpdated = false;
		String fieldName = null;
		for(Field field : auditObject.getClass().getDeclaredFields()){
			 fieldName = field.getName();
			propDesc = BeanUtils.getPropertyDescriptor(newBusinessObjectClass, fieldName);

			if(propDesc == null || propDesc.getReadMethod() == null){
				continue;
			}
			if(auditObject.getClass().getDeclaredField(fieldName) != null){
				Object newObjPropValue = PropertyUtils.getProperty(newBusinessObject, fieldName);
				Object oldObjPropValue = PropertyUtils.getProperty(oldBusinessObject, fieldName);
				
				if( (newObjPropValue != null && !newObjPropValue.equals(oldObjPropValue)) ||
				    (oldObjPropValue != null && !oldObjPropValue.equals(newObjPropValue))  || 
				    auditObject == null){
					PropertyUtils.setProperty(auditObject, fieldName, newObjPropValue);
					isRecordUpdated = true;
				}
			}
		}
		return isRecordUpdated;
	}
	

	private CourseApprovalAudit saveCourseApprovalAudit(CourseApproval courseapproval, String operation, Long createdORmodifiedByUser ) {
		
		CourseApprovalAudit courseapprovalaudit = null; 
		
		if(courseapproval != null)
		{
			courseapprovalaudit = new CourseApprovalAudit();
            
			courseapprovalaudit.setCourseApprovalId(courseapproval.getId());
			courseapprovalaudit.setActive(courseapproval.getActive());
			if(courseapproval.getAffidavit() != null)
			courseapprovalaudit.setAffidavitid(courseapproval.getAffidavit().getId());
			courseapprovalaudit.setApprovedCourseName(courseapproval.getApprovedCourseName());
			courseapprovalaudit.setApprovedCreditHours(courseapproval.getApprovedCreditHours());
			courseapprovalaudit.setCertificateNumberGeneratorNextNumber(courseapproval.getCertificateNumberGeneratorNextNumber());
			courseapprovalaudit.setCertificateNumberGeneratorNumberFormat(courseapproval.getCertificateNumberGeneratorNumberFormat());
			courseapprovalaudit.setCertificateNumberGeneratorPrefix(courseapproval.getCertificateNumberGeneratorPrefix());
			
			courseapprovalaudit.setCourseApprovalEffectivelyEndsDate(courseapproval.getCourseApprovalEffectivelyEndsDate());
			courseapprovalaudit.setCourseApprovalEffectivelyStartDate(courseapproval.getCourseApprovalEffectivelyStartDate());
			courseapprovalaudit.setCourseApprovalInformation(courseapproval.getCourseApprovalInformation());
			courseapprovalaudit.setCourseApprovalNumber(courseapproval.getCourseApprovalNumber());
			courseapprovalaudit.setCourseApprovalRenewalFee(courseapproval.getCourseApprovalRenewalFee());
			courseapprovalaudit.setCourseApprovalStatus(courseapproval.getCourseApprovalStatus());
			courseapprovalaudit.setCourseApprovalSubmissionFee(courseapproval.getCourseApprovalSubmissionFee());
			courseapprovalaudit.setCourseApprovaltype(courseapproval.getCourseApprovaltype());
			if(courseapproval.getCertificate() != null)
			courseapprovalaudit.setAssetid(courseapproval.getCertificate().getId());
			if(courseapproval.getTemplate() != null)
			courseapprovalaudit.setCourseconfigurationtemplateid(courseapproval.getTemplate().getId());
			if(courseapproval.getCourse() != null)
			courseapprovalaudit.setCourseId(courseapproval.getCourse().getId());
			courseapprovalaudit.setCourseSubmissionReminderDate(courseapproval.getCourseSubmissionReminderDate());
			courseapprovalaudit.setMostRecentlySubmittedForApprovalDate(courseapproval.getMostRecentlySubmittedForApprovalDate());
			courseapprovalaudit.setOriginalCourseApprovalFee(courseapproval.getOriginalCourseApprovalFee());
			if(courseapproval.getProvider() != null)
			courseapprovalaudit.setProviderid(courseapproval.getProvider().getId());
			if(courseapproval.getRenewedFrom() != null)
			courseapprovalaudit.setRenewedfromcourseapprovalid(courseapproval.getRenewedFrom().getId());
			courseapprovalaudit.setSelfReported(courseapproval.getSelfReported());
			courseapprovalaudit.setTag(courseapproval.getTag());
			courseapprovalaudit.setUseCertificateNumberGenerator(courseapproval.getUseCertificateNumberGenerator());
			courseapprovalaudit.setUsePurchasedCertificateNumbers(courseapproval.getUsePurchasedCertificateNumbers());			
	
			//Setting RegulatorCategories
			RegulatoryApprovalAudit raa = new RegulatoryApprovalAudit();
			raa.setRegulatorApprovaId(courseapproval.getId());
			if(courseapproval.getContentOwner()!=null)
			raa.setContainOwnerId(courseapproval.getContentOwner().getId());
			raa.setApprovalType(courseapproval.getCourseApprovaltype());
			raa.setDelete(false);
			if(courseapproval.getRegulatorCategory()!=null)
			raa.setRegulatoryCategoryId(courseapproval.getRegulatorCategory().getId());
			raa.setOperation(operation);
			raa.setCreatedBy(createdORmodifiedByUser);
			raa.setCreatedOn(new Date(System.currentTimeMillis()));
			raa.setUpdatedOn(new Date(System.currentTimeMillis()));
			raa.setUpdatedBy(createdORmodifiedByUser);
			
			
			if(operation.equalsIgnoreCase("Add")){
				courseapprovalaudit.setOperation(operation);
				courseapprovalaudit.setCreatedBy(createdORmodifiedByUser);
				courseapprovalaudit.setCreatedOn(new Date(System.currentTimeMillis()));
                courseapprovalaudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				courseapprovalaudit.setUpdatedBy(createdORmodifiedByUser);
			}else{
				courseapprovalaudit.setOperation(operation);
				courseapprovalaudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				courseapprovalaudit.setUpdatedBy(createdORmodifiedByUser);
			}
            
		courseapprovalaudit = courseApprovalAuditRepository.save(courseapprovalaudit);

		}
		
		return courseapprovalaudit;
	}


	private ProviderAudit saveProviderAudit(Provider provider, String operation, Long modifingUser ){
		
		ProviderAudit provideraudit = null; 
		
		if(provider != null)
		{
			provideraudit = new ProviderAudit();
            
			provideraudit.setAddressId(provider.getAddress().getId());
			provideraudit.setRegulatorId(null);
			provideraudit.setContentownerId(provider.getContentOwner().getId());
			provideraudit.setContactName(provider.getContactName());
			provideraudit.setName(provider.getName());
			provideraudit.setPhone(provider.getPhone());
			provideraudit.setWebsite(provider.getWebsite());
			provideraudit.setEmailAddress(provider.getEmailAddress());
			provideraudit.setFax(provider.getFax());
			provideraudit.setActive(provider.isActive());
			provideraudit.setProviderId(provider.getId());
			
			if(operation.equalsIgnoreCase("Add")){
				provideraudit.setOperation(operation);
				provideraudit.setCreatedBy(modifingUser);
				provideraudit.setCreatedOn(new Date(System.currentTimeMillis()));
				provideraudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				provideraudit.setUpdatedBy(modifingUser);
			}else{
				provideraudit.setOperation(operation);
				provideraudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				provideraudit.setUpdatedBy(modifingUser);
			}

			
			provideraudit = providerAuditRepository.save(provideraudit);

		}
		
		return provideraudit;
	}

}