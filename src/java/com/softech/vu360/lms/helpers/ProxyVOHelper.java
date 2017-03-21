package com.softech.vu360.lms.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.RegulatoryAnalyst;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.MyCustomer;

public class ProxyVOHelper {
	
	private static final Logger log = Logger.getLogger(ProxyVOHelper.class.getName());
	
	public static com.softech.vu360.lms.vo.Learner setLearnerProxy(Learner learnerModel) {

		if (learnerModel == null)
			return null;

		com.softech.vu360.lms.vo.Learner learnerVO = new com.softech.vu360.lms.vo.Learner();

		learnerVO.setId(learnerModel.getId());
		learnerVO.setCustomer(createCustomerVO(learnerModel.getCustomer()));
		learnerVO.setLearnerProfile(createLearnerProfileVO(learnerModel.getLearnerProfile()));
		learnerVO.setPreference(createLearnerPreferencesVO(learnerModel.getPreference()));
		com.softech.vu360.lms.vo.VU360User vu360UserVo = createVU360UserVOForLearner(learnerModel.getVu360User());
		vu360UserVo.setLearner(learnerVO);
		learnerVO.setVu360User(vu360UserVo);

		return learnerVO;

	}

	private static com.softech.vu360.lms.vo.LearnerProfile createLearnerProfileVO(LearnerProfile learnerProfileModel) {

		if (learnerProfileModel == null)
			return null;

		com.softech.vu360.lms.vo.LearnerProfile learnerProfileVO = new com.softech.vu360.lms.vo.LearnerProfile();

		learnerProfileVO.setId(learnerProfileModel.getId());
		learnerProfileVO.setLearnerAddress(createAddressVO(learnerProfileModel.getLearnerAddress()));
		learnerProfileVO.setLearnerAddress2(createAddressVO(learnerProfileModel.getLearnerAddress2()));
		learnerProfileVO.setMobilePhone(learnerProfileModel.getMobilePhone());
		learnerProfileVO.setOfficePhone(learnerProfileModel.getOfficePhone());
		learnerProfileVO.setOfficePhoneExtn(learnerProfileModel.getOfficePhoneExtn());
		learnerProfileVO.setTimeZone(createTimeZoneVO(learnerProfileModel.getTimeZone()));

		return learnerProfileVO;
	}

	public static com.softech.vu360.lms.vo.TimeZone createTimeZoneVO(TimeZone timeZoneModel) {

		if (timeZoneModel == null)
			return null;

		try {
			timeZoneModel.getId();
		} catch (LazyInitializationException lazyExc) {
			log.info("LazyInitializationException ::: ignore this ----> TimeZone could not initialize");
			return null;
		}
		com.softech.vu360.lms.vo.TimeZone timeZoneVO = new com.softech.vu360.lms.vo.TimeZone();

		timeZoneVO.setCode(timeZoneModel.getCode());
		timeZoneVO.setHours(timeZoneModel.getHours());
		timeZoneVO.setId(timeZoneModel.getId());
		timeZoneVO.setMinutes(timeZoneModel.getMinutes());
		timeZoneVO.setZone(timeZoneModel.getZone());

		return timeZoneVO;
	}

	private static com.softech.vu360.lms.vo.Customer createCustomerVO(Customer customerModel) {

		if (customerModel == null)
			return null;

		com.softech.vu360.lms.vo.Customer customerVO = new com.softech.vu360.lms.vo.Customer();

		customerVO.setActive(customerModel.isActive());
		customerVO.setAiccInterfaceEnabled(customerModel.getAiccInterfaceEnabled());
		customerVO.setBrandName(customerModel.getBrandName());
		customerVO.setContentOwner(createContentOwnerVO(customerModel.getContentOwner()));
		customerVO.setCustomerCode(customerModel.getCustomerCode());
		customerVO.setCustomerGUID(customerModel.getCustomerGUID());
		customerVO.setCustomerPreferences(createCustomerPreferencesVO(customerModel.getCustomerPreferences()));
		customerVO.setCustomerType(customerModel.getCustomerType());
		customerVO.setCustomFields(createCustomFieldVOList(customerModel.getCustomFields()));
		customerVO.setDistributor(createDistributorVO(customerModel.getDistributor()));
		customerVO.setEmail(customerModel.getEmail());
		customerVO.setFirstName(customerModel.getFirstName());
		customerVO.setId(customerModel.getId());
		customerVO.setLastName(customerModel.getLastName());
		customerVO.setLmsApiEnabledTF(customerModel.getLmsApiEnabledTF());
		customerVO.setLmsProvider(customerModel.getLmsProvider());
		customerVO.setName(customerModel.getName());
		customerVO.setOrderId(customerModel.getOrderId());
		customerVO.setPhoneExtn(customerModel.getPhoneExtn());
		customerVO.setPhoneNumber(customerModel.getPhoneNumber());
		customerVO.setWebsiteUrl(customerModel.getWebsiteUrl());

		return customerVO;
	}

	private static com.softech.vu360.lms.vo.Distributor createDistributorVO(Distributor distributorModel) {

		if (distributorModel == null)
			return null;

		com.softech.vu360.lms.vo.Distributor distributorVO = new com.softech.vu360.lms.vo.Distributor();

		distributorVO.setActive(distributorModel.getActive());
		distributorVO.setBrandName(distributorModel.getBrandName());
		distributorVO.setCallLoggingEnabled(distributorModel.getCallLoggingEnabled());
		distributorVO.setContentOwner(createContentOwnerVO(distributorModel.getContentOwner()));
		distributorVO.setCorporateAuthorVar(distributorModel.isCorporateAuthorVar());
		distributorVO.setCustomFields(createCustomFieldVOList(distributorModel.getCustomFields()));
		distributorVO.setDistributorCode(distributorModel.getDistributorCode());
		distributorVO.setDistributorEmail(distributorModel.getDistributorEmail());
		distributorVO.setDistributorPreferences(
				createDistributorPreferencesVO(distributorModel.getDistributorPreferences()));
		distributorVO.setFirstName(distributorModel.getFirstName());
		distributorVO.setId(distributorModel.getId());
		distributorVO.setLastName(distributorModel.getLastName());
		distributorVO.setLmsApiEnabledTF(distributorModel.getLmsApiEnabledTF());
		distributorVO.setMarkedPrivate(distributorModel.isMarkedPrivate());
		distributorVO.setMobilePhone(distributorModel.getMobilePhone());
		distributorVO.setMyCustomer(createMyCustomerVO(distributorModel.getMyCustomer()));
		distributorVO.setName(distributorModel.getName());
		distributorVO.setOfficePhone(distributorModel.getOfficePhone());
		distributorVO.setOfficePhoneExtn(distributorModel.getOfficePhoneExtn());
		distributorVO.setSelfReporting(distributorModel.isSelfReporting());
		distributorVO.setType(distributorModel.getType());
		distributorVO.setWebsiteUrl(distributorModel.getWebsiteUrl());
		return distributorVO;
	}

	private static MyCustomer createMyCustomerVO(Customer customerModel) {
		if (customerModel == null)
			return null;
		
		try {
			customerModel.getId();
		} catch (LazyInitializationException lazyExc) {
			// could not initialize proxy - no Session
			log.info("LazyInitializationException ::: ignore this ----> CustomerModel could not initialize");
			return null;
		}

		com.softech.vu360.lms.vo.MyCustomer myCustomerVO = new com.softech.vu360.lms.vo.MyCustomer();
		myCustomerVO.setId(customerModel.getId());
		myCustomerVO.setName(customerModel.getName());
		
		return myCustomerVO;
	}

	private static com.softech.vu360.lms.vo.Address createAddressVO(Address addressModel) {

		if (addressModel == null)
			return null;

		com.softech.vu360.lms.vo.Address addressVO = new com.softech.vu360.lms.vo.Address();

		addressVO.setCity(addressModel.getCity());
		addressVO.setCountry(addressModel.getCountry());
		addressVO.setId(addressModel.getId());
		addressVO.setProvince(addressModel.getProvince());
		addressVO.setState(addressModel.getState());
		addressVO.setStreetAddress(addressModel.getStreetAddress());
		addressVO.setStreetAddress2(addressModel.getStreetAddress2());
		addressVO.setStreetAddress3(addressModel.getStreetAddress3());
		addressVO.setZipcode(addressModel.getZipcode());

		return addressVO;
	}

	private static List<com.softech.vu360.lms.vo.CustomField> createCustomFieldVOList(
			List<CustomField> customFieldModelList) {

		try{
			if(customFieldModelList == null || customFieldModelList.size()==0){
				return null;
			}
		} catch (LazyInitializationException lazyExc) {
			log.info("LazyInitializationException ::: ignore this ----> customFieldList could not initialize");
			return null;
		}
		
		List<com.softech.vu360.lms.vo.CustomField> customFieldVOList = new ArrayList<com.softech.vu360.lms.vo.CustomField>();

		customFieldModelList.forEach((customFieldModel) -> {

			customFieldVOList.add(createCustomFieldVO(customFieldModel));

		});

		return customFieldVOList;

	}

	private static com.softech.vu360.lms.vo.CustomerPreferences createCustomerPreferencesVO(
			CustomerPreferences customerPreferencesModel) {

		if (customerPreferencesModel == null)
			return null;

		com.softech.vu360.lms.vo.CustomerPreferences customerPreferencesVO = new com.softech.vu360.lms.vo.CustomerPreferences();

		customerPreferencesVO.setAudioEnabled(customerPreferencesModel.isAudioEnabled());
		customerPreferencesVO.setAudioLocked(customerPreferencesModel.isAudioLocked());
		customerPreferencesVO.setBandwidth(customerPreferencesModel.getBandwidth());
		customerPreferencesVO.setBandwidthLocked(customerPreferencesModel.isBandwidthLocked());
		customerPreferencesVO.setBlankSearchEnabled(customerPreferencesModel.isBlankSearchEnabled());
		customerPreferencesVO.setCaptioningEnabled(customerPreferencesModel.isCaptioningEnabled());
		customerPreferencesVO.setCaptioningLocked(customerPreferencesModel.isCaptioningLocked());
		customerPreferencesVO.setCourseCompletionCertificateEmailEnabled(
				customerPreferencesModel.isCourseCompletionCertificateEmailEnabled());
		customerPreferencesVO.setCourseCompletionCertificateEmailLocked(
				customerPreferencesModel.isCourseCompletionCertificateEmailLocked());
		// customerPreferencesVO.setCustomer(customerVO);
		customerPreferencesVO.setEnableEnrollmentEmailsForNewCustomers(
				customerPreferencesModel.isEnableEnrollmentEmailsForNewCustomers());
		customerPreferencesVO.setEnableRegistrationEmailsForNewCustomers(
				customerPreferencesModel.isEnableRegistrationEmailsForNewCustomers());
		customerPreferencesVO.setEnableSelfEnrollmentEmailsForNewCustomers(
				customerPreferencesModel.isEnableSelfEnrollmentEmailsForNewCustomers());
		customerPreferencesVO.setEnrollmentEmailLocked(customerPreferencesModel.isEnrollmentEmailLocked());
		customerPreferencesVO.setId(customerPreferencesModel.getId());
		customerPreferencesVO.setRegistrationEmailLocked(customerPreferencesModel.isRegistrationEmailLocked());
		customerPreferencesVO.setVedioEnabled(customerPreferencesModel.isVedioEnabled());
		customerPreferencesVO.setVideoLocked(customerPreferencesModel.isVideoLocked());
		customerPreferencesVO.setVolume(customerPreferencesModel.getVolume());
		customerPreferencesVO.setVolumeLocked(customerPreferencesModel.isVolumeLocked());

		return customerPreferencesVO;
	}

	private static com.softech.vu360.lms.vo.DistributorPreferences createDistributorPreferencesVO(
			DistributorPreferences distributorPreferencesModel) {

		if (distributorPreferencesModel == null)
			return null;

		com.softech.vu360.lms.vo.DistributorPreferences distributorPreferencesVO = new com.softech.vu360.lms.vo.DistributorPreferences();

		distributorPreferencesVO.setAudioEnabled(distributorPreferencesModel.isAudioEnabled());
		distributorPreferencesVO.setAudioLocked(distributorPreferencesModel.isAudioLocked());
		distributorPreferencesVO.setBandwidth(distributorPreferencesModel.getBandwidth());
		distributorPreferencesVO.setBandwidthLocked(distributorPreferencesModel.isBandwidthLocked());
		distributorPreferencesVO.setCaptioningEnabled(distributorPreferencesModel.isCaptioningEnabled());
		distributorPreferencesVO.setCaptioningLocked(distributorPreferencesModel.isCaptioningLocked());
		distributorPreferencesVO.setCourseCompletionCertificateEmailEnabled(
				distributorPreferencesModel.isCourseCompletionCertificateEmailEnabled());
		distributorPreferencesVO.setCourseCompletionCertificateEmailLocked(
				distributorPreferencesModel.isCourseCompletionCertificateEmailLocked());
		distributorPreferencesVO.setEnableEnrollmentEmailsForNewCustomers(
				distributorPreferencesModel.isEnableEnrollmentEmailsForNewCustomers());
		distributorPreferencesVO.setEnableRegistrationEmailsForNewCustomers(
				distributorPreferencesModel.isEnableRegistrationEmailsForNewCustomers());
		distributorPreferencesVO.setEnableSelfEnrollmentEmailsForNewCustomers(
				distributorPreferencesModel.isEnableSelfEnrollmentEmailsForNewCustomers());
		distributorPreferencesVO.setEnrollmentEmailLocked(distributorPreferencesModel.isEnrollmentEmailLocked());
		distributorPreferencesVO.setId(distributorPreferencesModel.getId());
		distributorPreferencesVO.setRegistrationEmailLocked(distributorPreferencesModel.isRegistrationEmailLocked());
		distributorPreferencesVO.setVedioEnabled(distributorPreferencesModel.isVedioEnabled());
		distributorPreferencesVO.setVideoLocked(distributorPreferencesModel.isVideoLocked());
		distributorPreferencesVO.setVolume(distributorPreferencesModel.getVolume());
		distributorPreferencesVO.setVolumeLocked(distributorPreferencesModel.isVolumeLocked());

		return distributorPreferencesVO;
	}

	private static com.softech.vu360.lms.vo.ContentOwner createContentOwnerVO(ContentOwner contentOwnerModel) {

		if (contentOwnerModel == null)
			return null;
		
		try {
			contentOwnerModel.getId();
		} catch (LazyInitializationException lazyExc) {
			log.info("LazyInitializationException ::: ignore this ----> ContentOwner could not initialize");
			return null;
		}

		com.softech.vu360.lms.vo.ContentOwner contentOwnerVO = new com.softech.vu360.lms.vo.ContentOwner();

		contentOwnerVO.setCurrentAuthorCount(contentOwnerModel.getCurrentAuthorCount());
		contentOwnerVO.setCurrentCourseCount(contentOwnerModel.getCurrentCourseCount());
		contentOwnerVO.setGuid(contentOwnerModel.getGuid());
		contentOwnerVO.setId(contentOwnerModel.getId());
		contentOwnerVO.setMaxAuthorCount(contentOwnerModel.getMaxAuthorCount());
		contentOwnerVO.setMaxCourseCount(contentOwnerModel.getMaxCourseCount());
		contentOwnerVO.setName(contentOwnerModel.getName());
		contentOwnerVO.setPlanTypeId(contentOwnerModel.getPlanTypeId());

		return contentOwnerVO;
	}

	private static com.softech.vu360.lms.vo.CustomField createCustomFieldVO(CustomField customFieldModel) {

		if (customFieldModel == null)
			return null;

		com.softech.vu360.lms.vo.CustomField customFieldVO = new com.softech.vu360.lms.vo.CustomField();

		customFieldVO.setId(customFieldModel.getId());
		customFieldVO.setActive(customFieldModel.isActive());
		customFieldVO.setAlignment(customFieldModel.getAlignment());
		customFieldVO.setCustomFieldDescription(customFieldModel.getCustomFieldDescription());
		customFieldVO.setFieldEncrypted(customFieldModel.getFieldEncrypted());
		customFieldVO.setFieldLabel(customFieldModel.getFieldLabel());
		customFieldVO.setFieldRequired(customFieldModel.getFieldRequired());
		customFieldVO.setFieldType(customFieldModel.getFieldType());
		customFieldVO.setGlobal(customFieldModel.isGlobal());

		return customFieldVO;
	}

	private static com.softech.vu360.lms.vo.TrainingAdministrator createTrainingAdministratorVO(
			TrainingAdministrator trainingAdminModel) {

		if (trainingAdminModel == null)
			return null;

		com.softech.vu360.lms.vo.TrainingAdministrator trainingAdminVO = new com.softech.vu360.lms.vo.TrainingAdministrator();

		trainingAdminVO.setId(trainingAdminModel.getId());
		trainingAdminVO.setManagesAllOrganizationalGroups(trainingAdminModel.isManagesAllOrganizationalGroups());

		return trainingAdminVO;
	}

	private static com.softech.vu360.lms.vo.LMSAdministrator createLMSAdministratorVO(LMSAdministrator lmsAdminModel) {

		if (lmsAdminModel == null)
			return null;

		com.softech.vu360.lms.vo.LMSAdministrator lmsAdminVO = new com.softech.vu360.lms.vo.LMSAdministrator();

		lmsAdminVO.setId(lmsAdminModel.getId());
		lmsAdminVO.setGlobalAdministrator(lmsAdminModel.isGlobalAdministrator());
		// lmsAdminVO.setVu360User(createVU360UserVO(lmsAdminModel.getVu360User()));

		return lmsAdminVO;
	}

	// Change
	public static com.softech.vu360.lms.vo.Proctor createProctorVO(Proctor proctorModel) {

		if (proctorModel == null)
			return null;

		com.softech.vu360.lms.vo.Proctor proctorVO = new com.softech.vu360.lms.vo.Proctor();

		proctorVO.setFirstLogin(proctorModel.isFirstLogin());
		proctorVO.setId(proctorModel.getId());
		proctorVO.setPassword(proctorModel.getPassword());
		proctorVO.setPasswordResetDateTime(proctorModel.getPasswordResetDateTime());
		proctorVO.setProctorStatusTimeStamp(proctorModel.getProctorStatusTimeStamp());
		proctorVO.setStatus(proctorModel.getStatus());
		// proctorVO.setUser(user);
		proctorVO.setUsername(proctorModel.getUsername());

		return proctorVO;
	}

	public static com.softech.vu360.lms.vo.VU360User setUserProxy(VU360User vu360UserModel) {

		if (vu360UserModel == null)
			return null;

		return createVU360UserVO(vu360UserModel);
	}

	public static com.softech.vu360.lms.vo.Customer setCustomerProxy(Customer customerModel) {

		if (customerModel == null)
			return null;

		return createCustomerVO(customerModel);
	}

	public static com.softech.vu360.lms.vo.Distributor setDistributorProxy(Distributor distributorModel) {

		if (distributorModel == null)
			return null;

		return createDistributorVO(distributorModel);
	}

	private static Set<com.softech.vu360.lms.vo.LMSRole> createLMSRoleVOList(Set<LMSRole> lmsRoleModelList) {

		try{
			if(lmsRoleModelList == null || lmsRoleModelList.size()==0){
				return null;
			}
		} catch (LazyInitializationException lazyExc) {
			log.info("LazyInitializationException ::: ignore this ----> lmsRoleList could not initialize");
			return null;
		}
		
		Set<com.softech.vu360.lms.vo.LMSRole> lmsRoleVOList = new HashSet<com.softech.vu360.lms.vo.LMSRole>();

		lmsRoleModelList.forEach((lmsRoleModel) -> {
			lmsRoleVOList.add(createLMSRoleVO(lmsRoleModel));
		});

		return lmsRoleVOList;
	}

	public static com.softech.vu360.lms.vo.LMSRole createLMSRoleVO(LMSRole lmsRoleModel) {

		if (lmsRoleModel == null)
			return null;

		com.softech.vu360.lms.vo.LMSRole lmsRoleVO = new com.softech.vu360.lms.vo.LMSRole();

		lmsRoleVO.setRoleName(lmsRoleModel.getRoleName());
		lmsRoleVO.setId(lmsRoleModel.getId());
		lmsRoleVO.setRoleType(lmsRoleModel.getRoleType());

		List<LMSRoleLMSFeature> lmsPermissions = lmsRoleModel.getLmsPermissions();
		
		List<com.softech.vu360.lms.vo.LMSRoleLMSFeature> lmsPermissionsVO = new ArrayList<>();
		
		lmsPermissions.forEach(permission -> {
			
			com.softech.vu360.lms.vo.LMSRoleLMSFeature lmsRoleLMSFeatureVO = new com.softech.vu360.lms.vo.LMSRoleLMSFeature();
			
			lmsRoleLMSFeatureVO.setId(permission.getId());
			lmsRoleLMSFeatureVO.setEnabled(permission.getEnabled());
			
			LMSFeature lmsFeature = permission.getLmsFeature();
			
			com.softech.vu360.lms.vo.LMSFeature lmsFeatureVO = new com.softech.vu360.lms.vo.LMSFeature();
			lmsFeatureVO.setDisplayOrder(lmsFeature.getDisplayOrder());
			lmsFeatureVO.setFeatureCode(lmsFeature.getFeatureCode());
			lmsFeatureVO.setFeatureDescription(lmsFeature.getFeatureDescription());
			lmsFeatureVO.setFeatureGroup(lmsFeature.getFeatureGroup());
			lmsFeatureVO.setFeatureName(lmsFeature.getFeatureName());
			lmsFeatureVO.setId(lmsFeature.getId());
			lmsFeatureVO.setLmsMode(lmsFeature.getLmsMode());
			lmsFeatureVO.setRoleType(lmsFeature.getRoleType());
			
			lmsRoleLMSFeatureVO.setLmsFeature(lmsFeatureVO);
			lmsRoleLMSFeatureVO.setLocked(permission.isLocked());
			
			
			lmsRoleLMSFeatureVO.setLmsRole(lmsRoleVO);
			
			lmsPermissionsVO.add(lmsRoleLMSFeatureVO);
			
		});
		
		lmsRoleVO.setLmsPermissions(lmsPermissionsVO);
		
		return lmsRoleVO;
	}

	private static com.softech.vu360.lms.vo.VU360User createVU360UserVO(VU360User vu360UserModel) {

		if (vu360UserModel == null)
			return null;

		com.softech.vu360.lms.vo.VU360User usrVO = new com.softech.vu360.lms.vo.VU360User();

		usrVO.setAcceptedEULA(vu360UserModel.isAcceptedEULA());
		usrVO.setAccountNonExpired(vu360UserModel.getAccountNonExpired());
		usrVO.setAccountNonLocked(vu360UserModel.getAccountNonLocked());
		usrVO.setAccountNonLockedInt(vu360UserModel.getAccountNonLockedInt());
		usrVO.setChangePasswordOnLogin(vu360UserModel.getChangePasswordOnLogin());
		usrVO.setCreatedDate(vu360UserModel.getCreatedDate());
		usrVO.setCredentialsNonExpired(vu360UserModel.isCredentialsNonExpired());
		usrVO.setDomain(vu360UserModel.getDomain());
		usrVO.setEmailAddress(vu360UserModel.getEmailAddress());
		usrVO.setEnabled(vu360UserModel.getEnabled());
		usrVO.setExpirationDate(vu360UserModel.getExpirationDate());
		usrVO.setFirstName(vu360UserModel.getFirstName());
		usrVO.setId(vu360UserModel.getId());
		usrVO.setInstructor(createInstructorVO(vu360UserModel.getInstructor()));
		usrVO.setLastLogonDate(vu360UserModel.getLastLogonDate());
		usrVO.setLastName(vu360UserModel.getLastName());
		usrVO.setLastUpdatedDate(vu360UserModel.getLastUpdatedDate());
		usrVO.setLearner(setLearnerProxy(vu360UserModel.getLearner()));
		usrVO.setLmsAdministrator(createLMSAdministratorVO(vu360UserModel.getLmsAdministrator()));
		usrVO.setLogInAsManagerRole(createLMSRoleVO(vu360UserModel.getLogInAsManagerRole()));
		usrVO.setMiddleName(vu360UserModel.getMiddleName());
		usrVO.setNewUser(vu360UserModel.isNewUser());
		usrVO.setNotifyOnLicenseExpire(vu360UserModel.getNotifyOnLicenseExpire());
		usrVO.setNumLogons(vu360UserModel.getNumLogons());
		usrVO.setPassword(vu360UserModel.getPassword());
		usrVO.setPassWordChanged(vu360UserModel.isPassWordChanged());
		usrVO.setProctor(createProctorVO(vu360UserModel.getProctor()));
		usrVO.setRegulatoryAnalyst(createRegulatoryAnalystVO(vu360UserModel.getRegulatoryAnalyst()));
		usrVO.setRoleID(vu360UserModel.getRoleID());
		usrVO.setRoleName(vu360UserModel.getRoleName());
		usrVO.setShowGuidedTourScreenOnLogin(vu360UserModel.getShowGuidedTourScreenOnLogin());
		usrVO.setTrainingAdministrator(createTrainingAdministratorVO(vu360UserModel.getTrainingAdministrator()));
		usrVO.setUserGUID(vu360UserModel.getUserGUID());
		usrVO.setUsername(vu360UserModel.getUsername());
		usrVO.setVissibleOnReport(vu360UserModel.getVissibleOnReport());
		usrVO.setLanguage(createLanguageVO(vu360UserModel.getLanguage()));
		
		usrVO.setLmsRoles(createLMSRoleVOList(vu360UserModel.getLmsRoles()));
		
		return usrVO;
	}

	private static com.softech.vu360.lms.vo.VU360User createVU360UserVOForLearner(VU360User vu360UserModel) {

		if (vu360UserModel == null)
			return null;

		com.softech.vu360.lms.vo.VU360User usrVO = new com.softech.vu360.lms.vo.VU360User();

		usrVO.setAcceptedEULA(vu360UserModel.isAcceptedEULA());
		usrVO.setAccountNonExpired(vu360UserModel.getAccountNonExpired());
		usrVO.setAccountNonLocked(vu360UserModel.getAccountNonLocked());
		usrVO.setAccountNonLockedInt(vu360UserModel.getAccountNonLockedInt());
		usrVO.setChangePasswordOnLogin(vu360UserModel.getChangePasswordOnLogin());
		usrVO.setCreatedDate(vu360UserModel.getCreatedDate());
		usrVO.setCredentialsNonExpired(vu360UserModel.isCredentialsNonExpired());
		usrVO.setDomain(vu360UserModel.getDomain());
		usrVO.setEmailAddress(vu360UserModel.getEmailAddress());
		usrVO.setEnabled(vu360UserModel.getEnabled());
		usrVO.setExpirationDate(vu360UserModel.getExpirationDate());
		usrVO.setFirstName(vu360UserModel.getFirstName());
		usrVO.setId(vu360UserModel.getId());
		usrVO.setInstructor(createInstructorVO(vu360UserModel.getInstructor()));
		usrVO.setLastLogonDate(vu360UserModel.getLastLogonDate());
		usrVO.setLastName(vu360UserModel.getLastName());
		usrVO.setLastUpdatedDate(vu360UserModel.getLastUpdatedDate());
		usrVO.setLmsAdministrator(createLMSAdministratorVO(vu360UserModel.getLmsAdministrator()));
		usrVO.setLogInAsManagerRole(createLMSRoleVO(vu360UserModel.getLogInAsManagerRole()));
		usrVO.setMiddleName(vu360UserModel.getMiddleName());
		usrVO.setNewUser(vu360UserModel.isNewUser());
		usrVO.setNotifyOnLicenseExpire(vu360UserModel.getNotifyOnLicenseExpire());
		usrVO.setNumLogons(vu360UserModel.getNumLogons());
		usrVO.setPassword(vu360UserModel.getPassword());
		usrVO.setPassWordChanged(vu360UserModel.isPassWordChanged());
		usrVO.setProctor(createProctorVO(vu360UserModel.getProctor()));
		usrVO.setRegulatoryAnalyst(createRegulatoryAnalystVO(vu360UserModel.getRegulatoryAnalyst()));
		usrVO.setRoleID(vu360UserModel.getRoleID());
		usrVO.setRoleName(vu360UserModel.getRoleName());
		usrVO.setShowGuidedTourScreenOnLogin(vu360UserModel.getShowGuidedTourScreenOnLogin());
		usrVO.setTrainingAdministrator(createTrainingAdministratorVO(vu360UserModel.getTrainingAdministrator()));
		usrVO.setUserGUID(vu360UserModel.getUserGUID());
		usrVO.setUsername(vu360UserModel.getUsername());
		usrVO.setVissibleOnReport(vu360UserModel.getVissibleOnReport());
		usrVO.setLanguage(createLanguageVO(vu360UserModel.getLanguage()));
		
		usrVO.setLmsRoles(createLMSRoleVOList(vu360UserModel.getLmsRoles()));
		
		return usrVO;
	}

	public static com.softech.vu360.lms.vo.Language createLanguageVO(Language language) {

		if (language == null)
			return null;

		com.softech.vu360.lms.vo.Language languageVO = new com.softech.vu360.lms.vo.Language();
		languageVO.setCountry(language.getCountry());
		languageVO.setDisplayName(language.getDisplayName());
		languageVO.setId(language.getId());
		languageVO.setLanguage(language.getLanguage());
		languageVO.setVariant(language.getVariant());

		return languageVO;
	}
	
	public static com.softech.vu360.lms.vo.Instructor createInstructorVO(Instructor instructor) {

		if (instructor == null)
			return null;

		com.softech.vu360.lms.vo.Instructor instructorVO = new com.softech.vu360.lms.vo.Instructor();
		instructorVO.setActive(instructor.isActive());
		instructorVO.setAreaOfExpertise(instructor.getAreaOfExpertise());
		instructorVO.setContentOwner(createContentOwnerVO(instructor.getContentOwner()));
		instructorVO.setCustomfields(createCustomFieldVOList(instructor.getCustomfields()));
		instructorVO.setFirstName(instructor.getFirstName());
		instructorVO.setId(instructor.getId());
		instructorVO.setLastName(instructor.getLastName());

		return instructorVO;
	}
	
	private static com.softech.vu360.lms.vo.RegulatoryAnalyst createRegulatoryAnalystVO(
			RegulatoryAnalyst regAnalystModel) {

		if (regAnalystModel == null)
			return null;

		com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalystVO = new com.softech.vu360.lms.vo.RegulatoryAnalyst();

		regulatoryAnalystVO.setForAllContentOwner(regAnalystModel.isForAllContentOwner());
		regulatoryAnalystVO.setId(regAnalystModel.getId());
		regulatoryAnalystVO.setContentOwners(createContentOwnerVOList(regAnalystModel.getContentOwners()));

		return regulatoryAnalystVO;

	}

	private static com.softech.vu360.lms.vo.LearnerPreferences createLearnerPreferencesVO(LearnerPreferences learnerpreferencesModel){
		
		if(learnerpreferencesModel == null)
			return null;
		
		com.softech.vu360.lms.vo.LearnerPreferences learnerPreferencesVO = new com.softech.vu360.lms.vo.LearnerPreferences();
		
		learnerPreferencesVO.setIsAudioEnabled(learnerpreferencesModel.isAudioEnabled());
		learnerPreferencesVO.setIsCaptioningEnabled(learnerpreferencesModel.isCaptioningEnabled());
		learnerPreferencesVO.setId(learnerpreferencesModel.getId());
		
		return learnerPreferencesVO;
		
	}
	
	private static List<com.softech.vu360.lms.vo.ContentOwner> createContentOwnerVOList(List<ContentOwner> contentOwnerModelList) {
		try{
			if(contentOwnerModelList == null || contentOwnerModelList.size()==0){
				return null;
			}
		} catch (LazyInitializationException lazyExc) {
			log.info("LazyInitializationException ::: ignore this ----> contentOwnerList could not initialize");
			return null;
		}

		List<com.softech.vu360.lms.vo.ContentOwner> contentOwnerVOList = new ArrayList<com.softech.vu360.lms.vo.ContentOwner>();

		contentOwnerModelList.forEach((contentOwnerModel) -> {
			contentOwnerVOList.add(createContentOwnerVO(contentOwnerModel));
		});

		return contentOwnerVOList;
	}
	
}