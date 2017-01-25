/**
 * 
 */
package com.softech.vu360.lms.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.repositories.ProctorRepository;
import com.softech.vu360.lms.repositories.RepositorySpecificationsBuilder;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.BrandingService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialProctors;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
/**
 * @author syed.mahmood
 *
 */
public class ProctorServiceImpl implements ProctorService {

	private static final Logger log = Logger.getLogger(ProctorServiceImpl.class.getName());
//	private ProctorDAO proctorDAO;
	//private VU360UserDAO userDAO = null;
	private AccreditationService accreditationService;
	private VU360UserService vu360UserService = null;	
	private CustomerService customerService = null;
	private LearnerService learnerService = null;	
	private BrandingService brandingService = null;
	
	@Inject 
	private ProctorRepository proctorRepository;
	
	@Inject LearnerEnrollmentRepository learnerEnrollmentRepository;
	
	public List<LearnerEnrollment> getLearnersByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,Date startDate,Date endDate, String courseTitle) {
		return getLearnersByProctor(proctor, firstName, lastName, emailAddress, startDate, endDate, courseTitle, null);
	}

	public List<LearnerEnrollment> getLearnersByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,Date startDate,Date endDate, String courseTitle, String status[]) {
		
		List<LearnerEnrollment> learnerEnrollments = null;
		try
		{
			if(proctor != null)
			{
				learnerEnrollments = this.getEnrollmentsByProctor(proctor, firstName, lastName, emailAddress, startDate, endDate, courseTitle, status);
			}
		}
		catch (Exception e) 
		{
			log.debug("learnerEnrollments not found "+e);
			e.printStackTrace();
		}
		return learnerEnrollments;
	}
	
	//Moved from TopLinkProctorDAO
	public List<LearnerEnrollment> getEnrollmentsByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,Date startDate,Date endDate, String courseTitle, String status[]) {
		java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("CST"));
		List<LearnerEnrollment> enrollmentList = null;
	
		//change 00:00 to 23:50
		if(endDate !=null){
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.MINUTE, 1430);
			endDate = endCal.getTime(); 
		}
		
		enrollmentList = learnerEnrollmentRepository.getEnrollmentsByProctor(proctor,firstName,lastName,emailAddress, startDate, endDate, courseTitle, status);
		return enrollmentList;
	}
		
	public ByteArrayOutputStream generateCompletionCertificates(long[] proctorEnrollmentIDs) {
		return null;
	}


	public Proctor loadForUpdate(long proctorId) {
		//return proctorDAO.loadForUpdateProctor(proctorId);
		return this.proctorRepository.findOne(new Long(proctorId));
	}
	@Transactional
	public Proctor saveProctor(Proctor proctor) {
		//return proctorDAO.save(proctor);
		return proctorRepository.save(proctor);
		
	}

	public List<LearnerEnrollment> getProctorEnrollmentsByIds(Long[] proctorEnrollmentIds) {
		//return proctorDAO.getProctorEnrollments(proctorEnrollmentIds);
		return learnerEnrollmentRepository.findByIdIn(proctorEnrollmentIds);
	}
	
	public Proctor getProctorByUserId(long userId) {
		
		Proctor proctor =  this.proctorRepository.findByUserId(new Long(userId)); //proctorDAO.getProctorByUserId(userId);
		if(proctor == null)
		{
			return null;
		}
		return  loadForUpdate(proctor.getId());
	}

	
	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the VU360UserService
	 */
	 public VU360UserService getVu360UserService() {
		return vu360UserService;
	 }



	 public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	 public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	 }

	 
	 public List<LearnerEnrollment> getLearnersByCredentialTrainingCourse(Credential credential, String firstName, String lastName, String email, String companyName, Date startDate, Date endDate, Long[] arrProctorAlreadyExist){
		
		
		if(credential.getTrainingCourses() != null && credential.getTrainingCourses().size() > 0){
			
			Long[] courseIdArray = new Long[credential.getTrainingCourses().size()];
			List<Course> lstCourse = credential.getTrainingCourses();
			
			for(int count=0; count<lstCourse.size();count++){
				courseIdArray[count] = lstCourse.get(count).getId();
			}
		
			return this.getLearnersByCredentialTrainingCourse(courseIdArray,  firstName,   lastName,  email,  companyName,  startDate,  endDate, arrProctorAlreadyExist);
		}else
			return null;
		
	}
	
	 public List<LearnerEnrollment> getLearnersByCredentialTrainingCourse(Long[] credentialCourseIds, String firstName, String lastName, String email, String companyName, Date startDate, Date endDate, Long[] arrProctorAlreadyExist){
			java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("CST"));
			//change 00:00 to 23:50
			if(endDate !=null){
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(endDate);
				endCal.set(Calendar.MINUTE, 1430);
				endDate = endCal.getTime(); 
			}
			
			List<LearnerEnrollment> enrollmentList = null;
			RepositorySpecificationsBuilder<LearnerEnrollment> sb_LearnerEnrollment=new RepositorySpecificationsBuilder<LearnerEnrollment>();
			
			sb_LearnerEnrollment.with("courseStatistics_completionDate", sb_LearnerEnrollment.JOIN_NOT_NULL, "Null", "AND");
			sb_LearnerEnrollment.with("courseStatistics_completed", sb_LearnerEnrollment.JOIN_EQUALS, true, "AND");
			sb_LearnerEnrollment.with("course_id", sb_LearnerEnrollment.JOIN_IN, credentialCourseIds, "AND");
			sb_LearnerEnrollment.with("learner_vu360User_firstName", sb_LearnerEnrollment.JOIN_LIKE_IGNORE_CASE, firstName, "AND");
			sb_LearnerEnrollment.with("learner_vu360User_lastName", sb_LearnerEnrollment.JOIN_LIKE_IGNORE_CASE, lastName, "AND");
			sb_LearnerEnrollment.with("learner_vu360User_emailAddress", sb_LearnerEnrollment.JOIN_LIKE_IGNORE_CASE, email, "AND");
			sb_LearnerEnrollment.with("learner_customer_name", sb_LearnerEnrollment.JOIN_LIKE_IGNORE_CASE, companyName, "AND");
			sb_LearnerEnrollment.with("courseStatistics_completionDate", sb_LearnerEnrollment.JOIN_BETWEEN, startDate, endDate, "AND");
			
			if(startDate != null && endDate == null){
				sb_LearnerEnrollment.with("courseStatistics_completionDate", sb_LearnerEnrollment.JOIN_GREATER_THAN_EQUALS_TO, startDate, "AND");
			}
			
			if(startDate == null && endDate != null ){
				sb_LearnerEnrollment.with("courseStatistics_completionDate", sb_LearnerEnrollment.JOIN_LESS_THAN_EQUALS_TO, endDate, "AND");
			}
			
			sb_LearnerEnrollment.with("learner_vu360User_id", sb_LearnerEnrollment.JOIN_NOT_IN, arrProctorAlreadyExist, "AND");
			sb_LearnerEnrollment.with("learner_vu360User_accountNonExpired", sb_LearnerEnrollment.JOIN_EQUALS, true, "AND");
			sb_LearnerEnrollment.with("learner_vu360User_accountNonLocked", sb_LearnerEnrollment.JOIN_EQUALS, true, "AND");
			sb_LearnerEnrollment.with("learner_vu360User_enabled", sb_LearnerEnrollment.JOIN_EQUALS, true, "AND");
			sb_LearnerEnrollment.with("DISTINCT", sb_LearnerEnrollment.DISTINCT, "DISTINCT", "AND");
			enrollmentList=learnerEnrollmentRepository.findAll(sb_LearnerEnrollment.build());
			return enrollmentList;
		}
	
	public boolean saveProctorWithCredential(List<CredentialProctors> lstCredentialProctors, Credential credential){
		final String PROCTOR_STATUS_ACTIVE = "Active";
		final String PROCTOR_USERNAME_PREFIX = "PROC-";
		final String PROCTOR_USERNAME_SUFFIX = "-CODE";
		
		boolean allowtoSaveUpdate = true;
		Proctor proctor = null;
		VU360User usr;
		List<Credential> crd = null;
	for(CredentialProctors ctc : lstCredentialProctors){
			
		if(ctc.isSelected()){
				proctor = new Proctor();
				crd = new ArrayList<Credential>();
				
				//verify that learner is already Poctor
				Proctor isAlreadyProctor = this.proctorRepository.findByUserId(ctc.getVu360UserId()); //proctorDAO.getProctorByUserId(ctc.getVu360UserId());
				if(isAlreadyProctor!=null){
					//proctor = proctorDAO.loadForUpdateProctor(isAlreadyProctor.getId());
					proctor =  this.proctorRepository.findOne(isAlreadyProctor.getId());
					crd = proctor.getCredentials();
				}
				
				// restriction to add duplicate entry into CREDENTIAL_PROCTOR table
				if(crd!=null)
				{
					for(Credential cred : proctor.getCredentials()){
						if(cred.getId()==credential.getId())
							allowtoSaveUpdate=false;
					}
				}
				
			if(allowtoSaveUpdate){
					usr = vu360UserService.loadForUpdateVU360User(ctc.getVu360UserId());
					proctor.setUser(usr);
					
					if (proctor.getCredentials().size()==0 || isAlreadyProctor==null){
						proctor.setFirstLogin(true);
						proctor.setProctorStatusTimeStamp(new Date());
						proctor.setUsername(PROCTOR_USERNAME_PREFIX + GUIDGeneratorUtil.generateProctorPassword() + PROCTOR_USERNAME_SUFFIX);
						proctor.setPassword(GUIDGeneratorUtil.generateProctorPassword());
	
						// in 2 conditions we will send email to proctor
						// 1- on first time proctor creation. 2- when removed from all credential[become expired] and then added back to any credential(s). 
						Brander brands = VU360Branding.getInstance().getBrander(VU360Branding.DEFAULT_BRAND, new com.softech.vu360.lms.vo.Language());
						String emailSubject = brands.getBrandElement("lms.proctor.email.subject");
						String emailBody = MessageFormat.format(brands.getBrandElement("lms.proctor.email.body"), 
								proctor.getUser().getFirstName(), 
								proctor.getUser().getLastName(), 
								proctor.getUsername(), 
								proctor.getPassword());
						
						sendEmailToProctor(proctor.getUser().getEmailAddress(), brands.getBrandElement("lms.proctor.from.email.address"), brands.getBrandElement("lms.proctor.from.address.personal.name"), emailSubject, emailBody);
						
					}
					
					crd.add(credential);
					
					proctor.setStatus(PROCTOR_STATUS_ACTIVE);
					proctor.setCredentials(crd);
				
				
					if(isAlreadyProctor==null){
						// creating new Proctor with selected Credential
						proctor = saveProctor(proctor);
						
						// Adding proctor in VU360USER_ROLE because of update fresh proctor object in VU360USER_ROLE
						usr.setProctor(proctor);
						this.assignProctorRole(usr.getLearner().getCustomer(), usr);
					}else{
						// update Proctor with selected Credential[adding into CREDENTIAL_PROCTOR only]
						updateProctor(proctor);
						
						if(proctor.getCredentials().size()==1)
							this.assignProctorRole(usr.getLearner().getCustomer(), usr);
					}
			  	}
				isAlreadyProctor=null;	
			}
		}
		usr=null;crd=null;proctor=null;
		return true;
	}

	private void sendEmailToProctor(String toAddr, String fromAddr, String fromAddrPersonalName, String subject, String body){
		SendMailService.sendSMTPMessage(toAddr, fromAddr, fromAddrPersonalName, subject, body);
	}
	private void assignProctorRole (Customer customerToUpdate, VU360User user)
	{
		log.info("Assigning Proctor role...");		
		LMSRole LMSRoleProctor = learnerService.getRoleForProctorByCustomer(user.getLearner().getCustomer());
		
		if(LMSRoleProctor==null){
			LMSRoleProctor = new LMSRole();
			LMSRoleProctor.setOwner(customerToUpdate);
			LMSRoleProctor.setRoleName("PROCTOR");
			LMSRoleProctor.setSystemCreated(true);
			LMSRoleProctor.setRoleType(LMSRole.ROLE_PROCTOR);
			List<LMSRoleLMSFeature>	lmsPermissions = customerService.getLMSPermissions(
					customerToUpdate.getDistributor(), LMSRole.ROLE_PROCTOR, LMSRoleProctor);
			LMSRoleProctor.setLmsPermissions(lmsPermissions);
			LMSRoleProctor = learnerService.addRole(LMSRoleProctor, customerToUpdate);
		}
		
		user.addLmsRole(LMSRoleProctor);
		learnerService.assignUserToRole(user, LMSRoleProctor);
		//vu360UserService.updateUser(user);
		vu360UserService.updateUser(user.getId(), user);
		log.debug("Proctor role ADDED!");
	}
	@Transactional
	@Override
	public Proctor updateProctor(Proctor proctor) {
		//return proctorDAO.updateProctor(proctor);
		return this.proctorRepository.save(proctor);
	}

	public BrandingService getBrandingService() {
		return brandingService;
	}

	public void setBrandingService(BrandingService brandingService) {
		this.brandingService = brandingService;
	}
		
}
