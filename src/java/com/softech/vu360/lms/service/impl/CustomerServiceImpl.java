package com.softech.vu360.lms.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.LcmsResource;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.CustomerEntitlementRepository;
import com.softech.vu360.lms.repositories.CustomerLMSFeatureRepository;
import com.softech.vu360.lms.repositories.CustomerPreferencesRepository;
import com.softech.vu360.lms.repositories.CustomerRepository;
import com.softech.vu360.lms.repositories.LcmsResourceRepository;
import com.softech.vu360.lms.repositories.RepositorySpecificationsBuilder;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.model.AddDistributors;
import com.softech.vu360.lms.web.controller.model.CEPlannerForm;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.StorefrontClientWS;
import com.softech.vu360.util.GUIDGeneratorUtil;


/**
 * @author Haider.ali
 *
 */
class CustomerServiceImpl implements CustomerService {
	
	private static final Logger log = Logger.getLogger(CustomerServiceImpl.class.getName());
	
	@Inject
	private CustomerRepository customerRespository;
	@Inject
	private CustomerEntitlementRepository customerEntitlementRepository;
	@Inject
	private CustomerPreferencesRepository customerPreferencesRepository;
	@Inject
	private LcmsResourceRepository lcmsResourceRepository;
	@Inject
	private CustomerLMSFeatureRepository customerLMSFeatureRepository;

	private VU360UserService vu360UserService = null;
	private LearnerService learnerService = null;
	private OrgGroupLearnerGroupService groupService = null;
	private DistributorService distributorService = null;
	private StorefrontClientWS storefrontClientWS=null;
	private AuthorService authorService = null;
	private BrandService brandService = null;
	private SecurityAndRolesService securityAndRolesService;
	private static final int LOCALEID= 1;
	private static final Long LANGUAGEID = 1L;
	private static final String RESOURCEKEY = "ImageComanyLogo";
	
	@Override
	public List<Customer> findCustomersByName(String name,String orderId,VU360User loggedInUser,boolean isExact){
		 List<Customer> ls = customerRespository.findCustomersByName(name,orderId, loggedInUser, isExact);
		 return ls;
		 
		
	}
	
//	@Override
//	public List<Map<Object, Object>> findCustomersSimpleSearch(String name,String orderId, String orderDate, String emailAddress, VU360User loggedInUser,boolean isExact){
//		List<Map<Object, Object>> objCol = customerRespository.findCustomersSimpleSearch(name,orderId, orderDate, emailAddress, loggedInUser);
//		return objCol;
//	}
	
	public List<Map<Object, Object>> findCustomersAdvanceSearch(String name, String operator1 ,String orderId , String operator2,
			String orderDate, String operator3 ,String emailAddress ,VU360User loggedInUser,boolean isExact, int startRowIndex,
			int noOfRecords, ResultSet resultSet,String sortBy,int sortDirection){
		return customerRespository.findCustomersAdvanceSearch(name,  operator1 ,orderId , operator2,
				orderDate, operator3 ,emailAddress  ,loggedInUser, isExact, startRowIndex, noOfRecords, resultSet,sortBy,sortDirection);
	}
	
	@Override
	public List<Customer> findCustomersWithCustomFields(VU360User loggedInUser){
		return customerRespository.findCustomersWithCustomFields(loggedInUser);		
	}
	@Override
	public Customer getCustomerByCustomerCode(String customerCode) {
		return customerRespository.findByCustomerCode(customerCode);
	}

	@Deprecated
	public List<Customer> findCustomersByEmailAddess(String emailAddress){
		return customerRespository.findByEmail(emailAddress);
	}
	@Override
	public Customer findCustomerByCustomerGUID(String custGUID) {
		return customerRespository.findByCustomerGUID(custGUID);
	}
	@Override
	public List<Customer> findCustomersByDistributor(Distributor distributor){
		return customerRespository.findByDistributorId(distributor.getId());
	}
	
	@Override
	@Deprecated
	public List<CustomerEntitlement> findCustomerEntitlementByCustomerId(Long customerId){
		return customerEntitlementRepository.findByCustomerId(customerId);
	}
	
	public List<Customer> findCustomersWithEntitlementByDistributor(Distributor dist, int pageIndex, int retrieveRowCount, ResultSet resultSet, String sortBy,int sortDirection){
		List<Customer> lstCustomer=new ArrayList<Customer>();
		Distributor currentDistributor=((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentDistributor();
		if(currentDistributor==null){
			return lstCustomer;
		}
		Long distributorId = currentDistributor.getId();
		RepositorySpecificationsBuilder<Customer> sb_customer=new RepositorySpecificationsBuilder<Customer>();
		sb_customer.with("distributor_id", sb_customer.JOIN_EQUALS, distributorId, "AND");

		PageRequest pageRequest = null;
		if (retrieveRowCount != -1 && pageIndex >= 0) {
			pageRequest = new PageRequest(pageIndex/retrieveRowCount, retrieveRowCount);
			Page<Customer> page = customerRespository.findAll(sb_customer.build(), pageRequest);
			resultSet.total=((int) (long) page.getTotalElements());
			lstCustomer = page.getContent();
		}else{
			lstCustomer = customerRespository.findAll(sb_customer.build());
			resultSet.total = lstCustomer.size();
		}
		
//		for(Customer customer : lstCustomer){
//			AdminSearchMember adminSearchMember = new AdminSearchMember();
//            adminSearchMember.setId(customer.getId());
//            adminSearchMember.setName(customer.getName());
//            adminSearchMember.setEMail(customer.getEmail());
//            adminSearchMember.setStatus(customer.isActive());
//            adminSearchMember.setCurrentSearchType(AdminSearchType.CUSTOMER);
//            adminSearchMember.setCustomerCode(customer.getCustomerCode()); 
//            adminSearchMember.setResellerCode(customer.getDistributor().getDistributorCodeUI());
//            
//			List<CustomerEntitlement> lstCustomerEnt  = customerEntitlementRepository.findByCustomerId(customer.getId());
//			if(lstCustomerEnt!=null && lstCustomerEnt.size()>0){
//				adminSearchMember.setOriginalContractCreationDate(FormUtil.getInstance().formatDate( lstCustomerEnt.get(0).getContractCreationDate(), "MM/dd/yyyy"));
//				adminSearchMember.setRecentContractCreationDate(FormUtil.getInstance().formatDate( lstCustomerEnt.get(lstCustomerEnt.size()-1).getContractCreationDate(), "MM/dd/yyyy"));
//				adminSearchMember.originalContractCreationDatetime = lstCustomerEnt.get(0).getContractCreationDate();
//				adminSearchMember.recentContractCreationDatetime = lstCustomerEnt.get(lstCustomerEnt.size()-1).getContractCreationDate();
//			}
//			lstPopulatedCustomer.add(adminSearchMember);	
			/*	
				for(CustomerEntitlement ce : lstCustomerEnt){
					AdminSearchMember adminSearchMember = new AdminSearchMember();
	                adminSearchMember.setId(customer.getId());
	                adminSearchMember.setName(customer.getName());
	                adminSearchMember.setEMail(customer.getEmail());
	                adminSearchMember.setStatus(customer.isActive());
	                adminSearchMember.setCurrentSearchType(AdminSearchType.CUSTOMER);
	                adminSearchMember.setCustomerCode(customer.getCustomerCode()); 
	                adminSearchMember.setResellerCode(customer.getDistributor().getDistributorCodeUI()); 
	                
	                if(ce.getContractCreationDate()!=null)
	                      adminSearchMember.setContractCreationDate(FormUtil.getInstance().formatDate( ce.getContractCreationDate(), "MM/dd/yyyy"));
	                lstPopulatedCustomer.add(adminSearchMember);
				}
			}else{
					AdminSearchMember adminSearchMember = new AdminSearchMember();
	                adminSearchMember.setId(customer.getId());
	                adminSearchMember.setName(customer.getName());
	                adminSearchMember.setEMail(customer.getEmail());
	                adminSearchMember.setStatus(customer.isActive());
	                adminSearchMember.setCurrentSearchType(AdminSearchType.CUSTOMER);
	                adminSearchMember.setCustomerCode(customer.getCustomerCode()); 
	                adminSearchMember.setResellerCode(customer.getDistributor().getDistributorCodeUI()); 
	                
	                lstPopulatedCustomer.add(adminSearchMember);   
            }
            */
		//}
		return lstCustomer;
	}
	
	@Override
	@Transactional
	public Customer updateCustomer(EditCustomerForm editCustomerForm, Customer customer){
		Customer origCustomer = this.loadForUpdateCustomer(customer.getId());
		String origCustomerName=origCustomer.getName();
		origCustomer.setActive(editCustomerForm.getStatus());		
		Brand brand= brandService.getBrandById(editCustomerForm.getBrandId());
		origCustomer.setBrand(brand);
		origCustomer.setBrandName(brand==null?"":brand.getBrandKey());
		origCustomer.setCustomerType(editCustomerForm.getType());
		// do not yet support changing of distributor
		//origCustomer.setDistributor(distributor)
		origCustomer.setEmail(editCustomerForm.getEmailAddress());
		origCustomer.setFirstName(editCustomerForm.getFirstName());
		origCustomer.setLastName(editCustomerForm.getLastName());
		origCustomer.setName(editCustomerForm.getName());
		origCustomer.setPhoneExtn(editCustomerForm.getExtension());
		origCustomer.setPhoneNumber(editCustomerForm.getPhone());
		origCustomer.setWebsiteUrl(editCustomerForm.getWebsiteUrl());
		origCustomer.getBillingAddress().setCity(editCustomerForm.getBillingCity());
		origCustomer.getBillingAddress().setCountry(editCustomerForm.getBillingCountry());		
		origCustomer.getBillingAddress().setState(editCustomerForm.getBillingState());
		origCustomer.getBillingAddress().setStreetAddress(editCustomerForm.getBillingAddress1());
		origCustomer.getBillingAddress().setStreetAddress2(editCustomerForm.getBillingAddress2());
		origCustomer.getBillingAddress().setZipcode(editCustomerForm.getBillingZip());
		
		origCustomer.getShippingAddress().setCity(editCustomerForm.getShippingCity());
		origCustomer.getShippingAddress().setState(editCustomerForm.getShippingState());
		origCustomer.getShippingAddress().setZipcode(editCustomerForm.getShippingZip());		
		origCustomer.getShippingAddress().setCountry(editCustomerForm.getShippingCountry());
		
		origCustomer.getShippingAddress().setStreetAddress(editCustomerForm.getShippingAddress1());
		origCustomer.getShippingAddress().setStreetAddress2(editCustomerForm.getShippingAddress2());
		
		// ---------- code written for B2B Authoring ---------------
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(editCustomerForm.isSelfAuthor() && !origCustomer.isSelfAuthor()){
			ContentOwner contentOwner = authorService.addContentOwnerIfNotExist(origCustomer, loggedInUser.getId());
			origCustomer.setContentOwner(contentOwner);
		}
		else if ( origCustomer.isSelfAuthor() ) { // [12/9/2010] LMS-7512 :: Update Content Owner for Self-Author Customer/Reseller
			ContentOwner contentOwner = this.authorService.updateContentOwner(origCustomer);
			origCustomer.setContentOwner(contentOwner);			
		}
			
		// ---------- END of code written for B2B Authoring ---------------

		Customer newlySavedCustomer = customerRespository.save(origCustomer);

		if(!newlySavedCustomer.getName().equals(origCustomerName)){
			// [11/01/2010] LMS-7675 :: Update Root Org. group Name on changing Customer Name
			//OrganizationalGroup orgGroup = learnerService.loadForUpdateOrganizationalGroup(groupService.getRootOrgGroupForCustomer(newlySavedCustomer).getId());
			OrganizationalGroup orgGroup = groupService.getRootOrgGroupForCustomer(newlySavedCustomer.getId());
			orgGroup.setName(newlySavedCustomer.getName());
			groupService.saveOrganizationalGroup( orgGroup);
		}
		
		// Now update it on StoreFront
		try {
			this.updateProfile(newlySavedCustomer);
			log.debug("PROFILE Updated on SF!");
		} catch( Exception ex ) {
			log.error("Error while calling SF: " + ex.getMessage());
		}
		return newlySavedCustomer;
	}
	
	@Transactional
	public Customer saveCustomer(Customer customer) {
		return customerRespository.save(customer);
	}
	
	@Override
	public Customer saveCustomer(Customer customer,boolean isSendProfileUpdateOnSF) {
		// make no assumptions about where this object came from, refresh it 
		// from the databse and then update the one from this session/server
		
		//Customer origCustomer = this.getCustomerById(customer.getId());
		Customer origCustomer = this.loadForUpdateCustomer(customer.getId());
		origCustomer.setActive(customer.isActive());
		origCustomer.setBrand(customer.getBrand());
		origCustomer.setBrandName(customer.getBrandName());
		origCustomer.setCustomerType(customer.getCustomerType());
		// do not yet support changing of distributor
		//origCustomer.setDistributor(distributor)
		origCustomer.setEmail(customer.getEmail());
		origCustomer.setFirstName(customer.getFirstName());
		origCustomer.setLastName(customer.getLastName());
		origCustomer.setName(customer.getName());
		origCustomer.setPhoneExtn(customer.getPhoneExtn());
		origCustomer.setPhoneNumber(customer.getPhoneNumber());
		origCustomer.setWebsiteUrl(customer.getWebsiteUrl());
		origCustomer.getBillingAddress().setCity(customer.getBillingAddress().getCity());
		origCustomer.getBillingAddress().setCountry(customer.getBillingAddress().getCountry());
		origCustomer.getBillingAddress().setProvince(customer.getBillingAddress().getProvince());
		origCustomer.getBillingAddress().setState(customer.getBillingAddress().getState());
		origCustomer.getBillingAddress().setStreetAddress(customer.getBillingAddress().getStreetAddress());
		origCustomer.getBillingAddress().setStreetAddress2(customer.getBillingAddress().getStreetAddress2());
		origCustomer.getBillingAddress().setStreetAddress3(customer.getBillingAddress().getStreetAddress3());
		origCustomer.getBillingAddress().setZipcode(customer.getBillingAddress().getZipcode());
		
		origCustomer.getShippingAddress().setCity(customer.getShippingAddress().getCity());
		origCustomer.getShippingAddress().setCountry(customer.getShippingAddress().getCountry());
		origCustomer.getShippingAddress().setProvince(customer.getShippingAddress().getProvince());
		origCustomer.getShippingAddress().setState(customer.getShippingAddress().getState());
		origCustomer.getShippingAddress().setStreetAddress(customer.getShippingAddress().getStreetAddress());
		origCustomer.getShippingAddress().setStreetAddress2(customer.getShippingAddress().getStreetAddress2());
		origCustomer.getShippingAddress().setStreetAddress3(customer.getShippingAddress().getStreetAddress3());
		origCustomer.getShippingAddress().setZipcode(customer.getShippingAddress().getZipcode());
		origCustomer.getCustomerPreferences().setAudioEnabled(customer.getCustomerPreferences().isAudioEnabled());
		origCustomer.getCustomerPreferences().setAudioLocked(customer.getCustomerPreferences().isAudioLocked());
		origCustomer.getCustomerPreferences().setBandwidth(customer.getCustomerPreferences().getBandwidth());
		origCustomer.getCustomerPreferences().setBandwidthLocked(customer.getCustomerPreferences().isBandwidthLocked());
		origCustomer.getCustomerPreferences().setCaptioningEnabled(customer.getCustomerPreferences().isCaptioningEnabled());
		origCustomer.getCustomerPreferences().setCaptioningLocked(customer.getCustomerPreferences().isCaptioningLocked());
		origCustomer.getCustomerPreferences().setEnableEnrollmentEmailsForNewCustomers(customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers());
		origCustomer.getCustomerPreferences().setEnableRegistrationEmailsForNewCustomers(customer.getCustomerPreferences().isEnableRegistrationEmailsForNewCustomers());
		origCustomer.getCustomerPreferences().setEnableSelfEnrollmentEmailsForNewCustomers(customer.getCustomerPreferences().isEnableSelfEnrollmentEmailsForNewCustomers());
		origCustomer.getCustomerPreferences().setVedioEnabled(customer.getCustomerPreferences().isVedioEnabled());
		origCustomer.getCustomerPreferences().setVideoLocked(customer.getCustomerPreferences().isVideoLocked());
		origCustomer.getCustomerPreferences().setVolume(customer.getCustomerPreferences().getVolume());
		origCustomer.getCustomerPreferences().setVolumeLocked(customer.getCustomerPreferences().isVolumeLocked());
		
		Customer newlySavedCustomer= customerRespository.save(origCustomer);
 
		OrganizationalGroup orgGroup = learnerService.loadForUpdateOrganizationalGroup(groupService.getRootOrgGroupForCustomer(newlySavedCustomer.getId()).getId());
		orgGroup.setName(newlySavedCustomer.getName());

		orgGroup.setCustomer(newlySavedCustomer);
		groupService.saveOrganizationalGroup( orgGroup);
		
		
		
		if(isSendProfileUpdateOnSF){
			//Now update it on StoreFront
			try{
				this.updateProfile(origCustomer);
				log.debug("PROFILE Updated on SF!");
			}
			catch(Exception ex){
				log.error("Error while calling SF: " + ex.getMessage());
			}
		}
		
		return origCustomer;
	}

	@Override
	public Customer addCustomer(Long userId, AddCustomerForm addCustomerForm) {

		if ( addCustomerForm.getDistributors() == null ) {
			throw new IllegalArgumentException("a customer must have a distributor.");
		}
		Distributor dist = null;
		for(AddDistributors addDistributors : addCustomerForm.getDistributors()){
			if(addDistributors.isSelected() ){
				dist = addDistributors.getDistributor();
                break;
			}
		}
		if ( dist == null ) {
			throw new IllegalArgumentException("a customer must have a distributor.");
		}
		return addCustomer(userId, dist, addCustomerForm);
	}
	public Customer addCustomer(Long userId, Distributor dist, AddCustomerForm addCustomerForm) {
		Customer customer = addCustomer(true, userId,  dist,  addCustomerForm);
		return customer;
	}
	
	@Override
	@Transactional
	public Customer addCustomer(boolean shouldAddInAD, Long userId, Distributor dist, AddCustomerForm addCustomerForm) {
		try {
			// first add a new customer object
			Customer customer = new Customer();
			Brand brand = null;
			String guid=GUIDGeneratorUtil.generateGUID();
			customer.setCustomerGUID(guid);
			customer.setName(addCustomerForm.getCustomerName());
			customer.setFirstName(addCustomerForm.getFirstName());
			customer.setLastName(addCustomerForm.getLastName());
			customer.setWebsiteUrl(addCustomerForm.getWesiteURL());
			customer.setEmail(addCustomerForm.getEmailAdd());
			customer.setPhoneNumber(addCustomerForm.getPhone());
			customer.setPhoneExtn(addCustomerForm.getExt());
			customer.setCustomerType(addCustomerForm.getCustomerType());
			customer.setActive(addCustomerForm.isStatus());
			customer.setLmsApiEnabledTF(addCustomerForm.isLmsApiEnabledTF());
			
			//Removing default brand setting ENGSUP-31747
			if(addCustomerForm.getBrandId()>0)
			{			
				brand= brandService.getBrandById(addCustomerForm.getBrandId());
				customer.setBrand(brand);
				customer.setBrandName(brand==null?"":brand.getBrandKey());				
			}
			
			
			
			Address address1 = new Address();
			address1.setStreetAddress(addCustomerForm.getAddress1());
			address1.setStreetAddress2(addCustomerForm.getAddress1a());
			address1.setCity(addCustomerForm.getCity1());
			address1.setCountry(addCustomerForm.getCountry1());
			address1.setState(addCustomerForm.getState1());
			address1.setZipcode(addCustomerForm.getZip1());
			customer.setBillingAddress(address1);

			Address address2 = new Address();
			address2.setStreetAddress(addCustomerForm.getAddress2());
			address2.setStreetAddress2(addCustomerForm.getAddress2a());
			address2.setCity(addCustomerForm.getCity2());
			address2.setCountry(addCustomerForm.getCountry2());
			address2.setState(addCustomerForm.getState2());
			address2.setZipcode(addCustomerForm.getZip2());

			customer.setShippingAddress(address2);

			CustomerPreferences customerPreferences = new CustomerPreferences();
			customerPreferences.setAudioEnabled(addCustomerForm.isAudio());
			customerPreferences.setAudioLocked(addCustomerForm.isAudioLocked());
			customerPreferences.setVolumeLocked(addCustomerForm.isVolumeLocked());
			customerPreferences.setCaptioningEnabled(addCustomerForm.isCaptioning());
			customerPreferences.setCaptioningLocked(addCustomerForm.isCaptioningLocked());
			customerPreferences.setVolume(addCustomerForm.getVolume());
			if(addCustomerForm.isBandwidth())
				customerPreferences.setBandwidth(CustomerPreferences.BANDWIDTH_HIGH);
			else
				customerPreferences.setBandwidth(CustomerPreferences.BANDWIDTH_LOW);

			customerPreferences.setBandwidthLocked(addCustomerForm.isBandwidthLocked());
			customerPreferences.setVedioEnabled(addCustomerForm.isVideo());
			customerPreferences.setVideoLocked(addCustomerForm.isVideoLocked());
			customerPreferences.setEnableRegistrationEmailsForNewCustomers(addCustomerForm.isRegistrationEmails());
			customerPreferences.setRegistrationEmailLocked(addCustomerForm.isRegistrationEmailsLocked());
			customerPreferences.setEnableEnrollmentEmailsForNewCustomers(addCustomerForm.isEnrollmentEmails());
			customerPreferences.setEnrollmentEmailLocked(addCustomerForm.isEnrollmentEmailsLocked());
			customerPreferences.setCourseCompletionCertificateEmailEnabled(addCustomerForm.isCourseCompCertificateEmails());
			customerPreferences.setCourseCompletionCertificateEmailLocked(addCustomerForm.isCourseCompCertificateEmailsLocked());

			customer.setCustomerPreferences(customerPreferences);
			customerPreferences.setCustomer(customer);

			// make no assumptions on how we got this one
			dist = distributorService.getDistributorById(dist.getId());
            if ( dist == null ) {
					throw new IllegalArgumentException("could not find the distributor in the db!  DistributorCode:");
			}
		    //dist = distributorService.findDistibutorByDistributorCode(dist.getDistributorCode());
			customer.setDistributor(dist);
			
			if(addCustomerForm.getCustomerType().equalsIgnoreCase("B2B")){
				customer.setCustomerType("b2b");
			} else {
				customer.setCustomerType("b2c");
			}
			
			//------ Self Authoring Code starts here --------
			try {
				
				if(addCustomerForm.isSelfAuthor()){
					ContentOwner co = authorService.addContentOwnerIfNotExist(customer, userId);
					customer.setContentOwner(co);
				}

			} catch (Exception e) {

				log.error(e.getMessage());
				e.printStackTrace();
			}
		//------ Self Authoring Code ends here --------
			
			// save customer to DB
			log.debug("customer code=>"+customer.getCustomerCode());
			customer.setDefault(addCustomerForm.isDefault());
			customer = customerRespository.save(customer);
			customer.setCustomerCode(Customer.CODE_PREFIX+"-"+customer.getId());
			log.debug("customer code=>"+customer.getCustomerCode());
			customerRespository.save(customer);
			// now setup the default roles
			LMSRole LMSRoleLearner = new LMSRole();
			LMSRoleLearner.setOwner(customer);
			LMSRoleLearner.setRoleName("LEARNER");
			LMSRoleLearner.setDefaultForRegistration(Boolean.TRUE);
			LMSRoleLearner.setSystemCreated(Boolean.TRUE);
			LMSRoleLearner.setRoleType(LMSRole.ROLE_LEARNER);
			LMSRoleLearner.setDefaultForRegistration(Boolean.TRUE);
			List<LMSRoleLMSFeature>lmsPermissions =getLMSPermissions(dist,LMSRole.ROLE_LEARNER,LMSRoleLearner);
			LMSRoleLearner.setLmsPermissions(lmsPermissions);

			// add the Learner Role
			LMSRoleLearner = learnerService.addRole(LMSRoleLearner,customer);
			
			/*
			 * Only b2b customer may have manager role.
			 */
			LMSRole LMSRoleManager = null;
			if(addCustomerForm.getCustomerType().equalsIgnoreCase("B2B")){
				LMSRoleManager = new LMSRole();
				LMSRoleManager.setOwner(customer);
				LMSRoleManager.setRoleName("MANAGER");
				LMSRoleManager.setSystemCreated(true);
				LMSRoleManager.setRoleType(LMSRole.ROLE_TRAININGMANAGER);
				lmsPermissions = getLMSPermissions(dist,LMSRole.ROLE_TRAININGMANAGER,LMSRoleManager);
				LMSRoleManager.setLmsPermissions(lmsPermissions);
				LMSRoleManager = learnerService.addRole(LMSRoleManager,customer);
			}
			
			// now add an org group
			OrganizationalGroup orgGroup = new OrganizationalGroup();
			orgGroup.setName(customer.getName());
			orgGroup.setCustomer(customer);
			orgGroup.setRootOrgGroup(null);
			orgGroup.setParentOrgGroup(null);
			List<OrganizationalGroup> organizationalGroup = new ArrayList <OrganizationalGroup>();
			organizationalGroup.add(orgGroup);
			//Old toplink method only insert in (ORGANIZATIONALGROUP) table not update the column "rootOrgGroup_Id"
			//and keep blank while insert new ORGANIZATIONALGROUP. (LMS-18372)
			groupService.saveOrganizationalGroup(orgGroup);
			
			/*
			 * Now I am to create learner
			 * 
			 */
			VU360User newUser = new VU360User();
			newUser.setFirstName(addCustomerForm.getFirstName());
			//UI has not provided me the middlename
			newUser.setLastName(addCustomerForm.getLastName());
			newUser.setEmailAddress(addCustomerForm.getEmailAdd());
			newUser.setPassword(addCustomerForm.getPassword());
			newUser.setUsername(addCustomerForm.getLoginEmailID());
			newUser.setAccountNonExpired(!new Boolean(addCustomerForm.isExpired()));
			newUser.setAcceptedEULA(Boolean.FALSE);
			newUser.setNewUser(Boolean.TRUE);
			newUser.setAccountNonLocked(addCustomerForm.isLocked()==true?Boolean.FALSE:Boolean.TRUE);
			newUser.setChangePasswordOnLogin(addCustomerForm.isChangePassword()==false?Boolean.FALSE:Boolean.TRUE);
			newUser.setCredentialsNonExpired(addCustomerForm.isExpired()==true?Boolean.FALSE:Boolean.TRUE);
			newUser.setEnabled(addCustomerForm.isDisabled()==true?Boolean.FALSE:Boolean.TRUE);
			newUser.setVissibleOnReport(addCustomerForm.isReport()==false?Boolean.FALSE:Boolean.TRUE);
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if(StringUtils.isBlank(addCustomerForm.getExpirationDate())){
				newUser.setExpirationDate(null);
			}else{
				String dateString = addCustomerForm.getExpirationDate().trim();
				// Parse the string into a Date.
				if (!dateString.isEmpty()){
					Date myDate = formatter.parse(dateString);
					newUser.setExpirationDate(myDate);
				}
			}
			newUser.setUserGUID(guid);
			Calendar calender=Calendar.getInstance();
			Date createdDate=calender.getTime();
			newUser.setCreatedDate(createdDate);
			
			// everyone is a learner
			newUser.addLmsRole(LMSRoleLearner);
			
			if(addCustomerForm.getCustomerType().equalsIgnoreCase("B2B")){
				newUser.addLmsRole(LMSRoleManager);
				TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
				trainingAdministrator.setManagesAllOrganizationalGroups(true);
				trainingAdministrator.setCustomer(customer);
				trainingAdministrator.setVu360User(newUser);
				newUser.setTrainingAdministrator(trainingAdministrator);
				customer.setCustomerType("b2b");
			} else {
				customer.setCustomerType("b2c");
			}
			LearnerProfile newLearnerProfile = new LearnerProfile();
			Address newAddress = new Address();
			newAddress.setStreetAddress(addCustomerForm.getAddress1().trim());
			newAddress.setStreetAddress2(addCustomerForm.getAddress1a().trim());
			newAddress.setCity(addCustomerForm.getCity1().trim());
			newAddress.setState(addCustomerForm.getState1().trim());
			newAddress.setZipcode(addCustomerForm.getZip1().trim());
			newAddress.setCountry(addCustomerForm.getCountry1().trim());
			newLearnerProfile.setLearnerAddress(newAddress);
			
			Address newAddress2 = new Address();
			newAddress2.setStreetAddress(addCustomerForm.getAddress2().trim());
			newAddress2.setStreetAddress2(addCustomerForm.getAddress2a().trim());
			newAddress2.setCity(addCustomerForm.getCity2().trim());
			newAddress2.setState(addCustomerForm.getState2().trim());
			newAddress2.setZipcode(addCustomerForm.getZip2().trim());
			newAddress2.setCountry(addCustomerForm.getCountry2().trim());
			newLearnerProfile.setLearnerAddress2(newAddress2);
			newLearnerProfile.setMobilePhone(customer.getPhoneNumber());

			Learner newLearner = new Learner();
			newLearner.setVu360User(newUser);
			newLearnerProfile.setLearner(newLearner);
			newLearner.setLearnerProfile(newLearnerProfile);
			newLearner.setCustomer(customer);
			
			List<OrganizationalGroup> groups = new ArrayList<OrganizationalGroup> ();
			groups.add(orgGroup);
			newLearner=learnerService.addLearner(false, newLearner.getCustomer(), groups, null, newLearner);
			
			/**
			 * LMS-10055 | S M Humayun | 25 April 2011
			 * Moved from AddNewCustomerWizardController to here - 5 August 2011
			 */
			this.enableAllResellerPermissionsForThisCustomer(customer);

			return customer;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	 }

	/**
	 * Enable all reseller permissions for this <code>customer</code>
	 * @param customer
	 * @since 4.13 {LMS-10055}
	 * @author sm.humayun
	 */
	private void enableAllResellerPermissionsForThisCustomer (Customer customer)
	{
		try
		{
			log.info("\t ---------- START - enableAllResellerPermissionsForThisCustomer : " + this.getClass().getName() + " ---------- ");
			log.info("fetching all distributor lms feaures...");
			List<DistributorLMSFeature> distributorLMSFeatures = securityAndRolesService.getAllDistributorLMSFeatures(customer.getDistributor());
			log.info("distributorLMSFeatures " + (distributorLMSFeatures == null ? "is null" : "size = " + distributorLMSFeatures.size()));
			if(distributorLMSFeatures != null && distributorLMSFeatures.size() > 0)
			{
				List<CustomerLMSFeature> customerLMSFeatures = new ArrayList<CustomerLMSFeature>(distributorLMSFeatures.size());
				CustomerLMSFeature customerLMSFeature;
				for(DistributorLMSFeature distributorLMSFeature : distributorLMSFeatures)
				{
					customerLMSFeature = new CustomerLMSFeature();
					customerLMSFeature.setCustomer(customer);
					if(distributorLMSFeature.getLmsFeature().getFeatureCode().equals("LMS-MGR-0033")){
						 if(distributorLMSFeature.getEnabled() || !distributorLMSFeature.getEnabled()){
						 	customerLMSFeature.setEnabled(false);
						 }
					}
					else{
						customerLMSFeature.setEnabled(distributorLMSFeature.getEnabled());	
					}
					customerLMSFeature.setLmsFeature(distributorLMSFeature.getLmsFeature());
					customerLMSFeatures.add(customerLMSFeature);
				}
				log.info("adding customerLMSFeatures...");
				this.addLMSFeatures(customerLMSFeatures);
				log.info("added.");
			}
		}
		finally
		{
			log.info("\t ---------- END - enableAllResellerPermissionsForThisCustomer : " + this.getClass().getName() + " ---------- ");
		}
	}

	public List<LMSRoleLMSFeature> getLMSPermissions(String roleType,LMSRole lmsRole){
		List<LMSRoleLMSFeature> lmsPermissions= new ArrayList<LMSRoleLMSFeature>();	
		List<LMSFeature> 	lmsFeatures=  vu360UserService.getFeaturesByRoleType(roleType);
		for (LMSFeature lmsFeature: lmsFeatures){
		LMSRoleLMSFeature lmsPermission= new LMSRoleLMSFeature();
		lmsPermission.setLmsFeature(lmsFeature);
		lmsPermission.setEnabled(true);
		lmsPermission.setLmsRole(lmsRole);
		lmsPermissions.add(lmsPermission);
		}
		return lmsPermissions;
	}
	
	@Override
	public List<LMSRoleLMSFeature> getLMSPermissions(Distributor distributor,String roleType,LMSRole lmsRole){

		List<LMSRoleLMSFeature> lmsPermissions= new ArrayList<LMSRoleLMSFeature>();
		List<DistributorLMSFeature> defaultEnabledPermissions=distributorService.getPermissions(distributor, roleType);
		if(defaultEnabledPermissions!=null && defaultEnabledPermissions.size()>0){
			for (DistributorLMSFeature permission: defaultEnabledPermissions){
				LMSRoleLMSFeature lmspermission=new LMSRoleLMSFeature();
				lmspermission.setLmsFeature(permission.getLmsFeature());
				if(permission.getLmsFeature().getFeatureCode().equals("LMS-MGR-0033")){
					 if(permission.getEnabled()){
					 	lmspermission.setEnabled(false);
					 }
				}
				else{
				     lmspermission.setEnabled(permission.getEnabled());
				}
				//lmspermission.setEnabled(permission.getEnabled());
				lmspermission.setLmsRole(lmsRole);
				lmsPermissions.add(lmspermission);
			}
		}
		else
			lmsPermissions=this.getLMSPermissions(roleType, lmsRole);
		return lmsPermissions;
		
	}
	
	@Transactional
	public Customer updateCustomer(Customer customer){
		return customerRespository.save(customer);
	}

	public Customer findByContentOwner(ContentOwner owner ){
		 return customerRespository.findByContentOwner(owner);
	 }

	public Customer getCustomerById(long id){
		return  customerRespository.findOne(id);
		
	}
	
	public Customer loadForUpdateCustomer(long customerId){
    	return customerRespository.findOne(customerId);
    }
	
	public CustomerPreferences saveCustomerPreferences(CustomerPreferences customerPreferences) {
		return customerPreferencesRepository.save(customerPreferences);
	}
	
	public void deleteCustomers(long customerIdArray[]){
		for (long l : customerIdArray) {
			Customer c = customerRespository.findOne(new Long(l));
			c.setActive(false);
			customerRespository.save(c);
		}
	}
	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}
	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	/**
	 * @return the groupService
	 */
	public OrgGroupLearnerGroupService getGroupService() {
		return groupService;
	}
	/**
	 * @param groupService the groupService to set
	 */
	public void setGroupService(OrgGroupLearnerGroupService groupService) {
		this.groupService = groupService;
	}

	/*
	 * This method will return Customer w.r.t email and if more than 1 customers found then it will return first Customer
	 */
	public Customer getCustomerByEmail(String email)
	{
		List<Customer> customers=customerRespository.findByEmail(email);
		if(customers!=null && customers.size()>0)
			return customers.get(0);
		else
			return null;	
		
	}
	/**
	 * @return the distributorService
	 */
	public DistributorService getDistributorService() {
		return distributorService;
	}
	/**
	 * @param distributorService the distributorService to set
	 */
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}
	
	/**
	 * updateProfile
	 * @return boolean flag indicating if it was successful in updating the profile
	 * @exception CustomerExceptin In case if something went wrong 
	 */
	public Customer updateProfile( Customer customer ) {
		if ( customer == null )
			return null;
		//We need to call save customer passing entire customer object
		//Commenting the saving as it is being called from saveCustomer method
		//Customer cust = this.saveCustomer( customer );
		//Now we need to pass the customer object to the SF via it's client
		//StorefrontClientWSImpl storefrontClientWS = new StorefrontClientWSImpl();
		storefrontClientWS.updateProfileEvent( customer );
		return customer;
	}

	
	public StorefrontClientWS getStorefrontClientWS() {
		return storefrontClientWS;
	}
	public void setStorefrontClientWS(StorefrontClientWS storefrontClientWS) {
		this.storefrontClientWS = storefrontClientWS;
	}
	
	public Map<Object,Object> getCustomersByCurrentDistributor(String firstName, String lastName,
			String email, int pageIndex, int pageSize,
			String sortBy, int sortDirection) {
		
		List<Customer> ls = customerRespository.getCustomersByCurrentDistributor(firstName, lastName, email, pageIndex, pageSize, sortBy, sortDirection);
		Long l = customerRespository.getCustomersByCurrentDistributorRecordCount(firstName, lastName, email, pageIndex, pageSize, sortBy, sortDirection);
		
		Map<Object,Object> results = new HashMap<Object, Object>();
		results.put("recordSize", l.toString());
		results.put("list", ls);

		return results;
	}
	
	

	/**
	 * @return the authorService
	 */
	public AuthorService getAuthorService() {
		return authorService;
	}

	/**
	 * @param authorService the authorService to set
	 */
	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	
	public Customer addCustomer(Long userId, Distributor dist, CEPlannerForm addCustomerForm) {
		try {
			// first add a new customer object
			Customer customer = new Customer();
			//customer.setCustomerGUID(GUIDGeneratorUtil.generateGUID(guid));
			String guid=GUIDGeneratorUtil.generateGUID();
			customer.setCustomerGUID(guid);
			customer.setName(addCustomerForm.getCompanyName());
			customer.setFirstName(addCustomerForm.getFirstName());
			customer.setLastName(addCustomerForm.getLastName());
			customer.setWebsiteUrl(addCustomerForm.getWesiteURL());
			customer.setEmail(addCustomerForm.getEmailAdd());
			customer.setPhoneNumber(addCustomerForm.getPhone());
			customer.setPhoneExtn(addCustomerForm.getExt());
			customer.setCustomerType(Customer.B2B);
			customer.setActive(true);
			//customer.setBrandName(addCustomerForm.getBrandName());
			
			Address address1 = new Address();
			address1.setStreetAddress(addCustomerForm.getAddress1());
			address1.setStreetAddress2(addCustomerForm.getAddress1a());
			address1.setCity(addCustomerForm.getCity1());
			address1.setCountry(addCustomerForm.getCountry1());
			address1.setState(addCustomerForm.getState1());
			address1.setZipcode(addCustomerForm.getZip1());
			customer.setBillingAddress(address1);

			Address address2 = new Address();
			address2.setStreetAddress(addCustomerForm.getAddress2());
			address2.setStreetAddress2(addCustomerForm.getAddress2a());
			address2.setCity(addCustomerForm.getCity2());
			address2.setCountry(addCustomerForm.getCountry2());
			address2.setState(addCustomerForm.getState2());
			address2.setZipcode(addCustomerForm.getZip2());

			customer.setShippingAddress(address2);

			CustomerPreferences customerPreferences = new CustomerPreferences();
			customerPreferences.setAudioEnabled(true);
			customerPreferences.setAudioLocked(false);
			customerPreferences.setVolumeLocked(false);
			customerPreferences.setCaptioningEnabled(true);
			customerPreferences.setCaptioningLocked(false);
			customerPreferences.setVolume(10);
			
			customerPreferences.setBandwidth(CustomerPreferences.BANDWIDTH_LOW);
			

			customerPreferences.setBandwidthLocked(false);
			customerPreferences.setVedioEnabled(true);
			customerPreferences.setVideoLocked(false);
			customerPreferences.setEnableRegistrationEmailsForNewCustomers(false);
			customerPreferences.setRegistrationEmailLocked(false);
			customerPreferences.setEnableEnrollmentEmailsForNewCustomers(true);
			customerPreferences.setEnrollmentEmailLocked(false);

			customer.setCustomerPreferences(customerPreferences);
			customerPreferences.setCustomer(customer);

			// make no assumptions on how we got this one
			dist = distributorService.getDistributorById(dist.getId());
            if ( dist == null ) {
					throw new IllegalArgumentException("could not find the distributor in the db!  DistributorCode:");
			}
		    //dist = distributorService.findDistibutorByDistributorCode(dist.getDistributorCode());
			customer.setDistributor(dist);
			
			
			customer.setCustomerType("b2b");
			
			
			// save customer to DB
			log.debug("customer code=>"+customer.getCustomerCode());
			customer = customerRespository.save(customer);
			customer.setCustomerCode(Customer.CODE_PREFIX+"-"+customer.getId());
			log.debug("customer code=>"+customer.getCustomerCode());
			customerRespository.save(customer);
			// now setup the default roles
			LMSRole LMSRoleLearner = new LMSRole();
			LMSRoleLearner.setOwner(customer);
			LMSRoleLearner.setRoleName("LEARNER");
			LMSRoleLearner.setDefaultForRegistration(true);
			LMSRoleLearner.setSystemCreated(true);
			LMSRoleLearner.setRoleType(LMSRole.ROLE_LEARNER);
			LMSRoleLearner.setDefaultForRegistration(true);
			List<LMSRoleLMSFeature>lmsPermissions =getLMSPermissions(dist,LMSRole.ROLE_LEARNER,LMSRoleLearner);
			LMSRoleLearner.setLmsPermissions(lmsPermissions);

			// add the Learner Role
			LMSRoleLearner = learnerService.addRole(LMSRoleLearner,customer);
			
			/*
			 * Only b2b customer may have manager role.
			 */
			LMSRole LMSRoleManager = null;
			
			LMSRoleManager = new LMSRole();
			LMSRoleManager.setOwner(customer);
			LMSRoleManager.setRoleName("MANAGER");
			LMSRoleManager.setSystemCreated(true);
			LMSRoleManager.setRoleType(LMSRole.ROLE_TRAININGMANAGER);
			lmsPermissions = getLMSPermissions(dist,LMSRole.ROLE_TRAININGMANAGER,LMSRoleManager);
			LMSRoleManager.setLmsPermissions(lmsPermissions);
			LMSRoleManager = learnerService.addRole(LMSRoleManager,customer);
			
			
			// now add an org group
			OrganizationalGroup orgGroup = new OrganizationalGroup();
			orgGroup.setName(customer.getName());
			orgGroup.setCustomer(customer);
			orgGroup.setRootOrgGroup(null);
			orgGroup.setParentOrgGroup(null);
			List<OrganizationalGroup> organizationalGroup = new ArrayList <OrganizationalGroup>();
			organizationalGroup.add(orgGroup);
			groupService.saveOrgGroup(orgGroup);
			
			/*
			 * Now I am to create learner
			 * 
			 */
			Learner newLearner = new Learner();
			VU360User newUser = new VU360User();
			
			newUser.setFirstName(addCustomerForm.getFirstName());
			//UI has not provided me the middlename
			newUser.setLastName(addCustomerForm.getLastName());
			newUser.setEmailAddress(addCustomerForm.getEmailAdd());
			newUser.setPassword(addCustomerForm.getPassword());
			newUser.setUsername(addCustomerForm.getUsername());

			newUser.setAccountNonExpired(true);
			newUser.setAcceptedEULA(false);
			newUser.setNewUser(true);
			newUser.setAccountNonLocked(true);
			newUser.setChangePasswordOnLogin(false);
			newUser.setCredentialsNonExpired(true);
			newUser.setEnabled(true);
			newUser.setVissibleOnReport(true);
			newUser.setExpirationDate(null);
			newUser.setUserGUID(guid);
			Calendar calender=Calendar.getInstance();
			Date createdDate=calender.getTime();
			newUser.setCreatedDate(createdDate);
			// everyone is a learner
			newUser.addLmsRole(LMSRoleLearner);
			newUser.addLmsRole(LMSRoleManager);
				
			TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
			trainingAdministrator.setManagesAllOrganizationalGroups(true);
			trainingAdministrator.setCustomer(customer);
			trainingAdministrator.setVu360User(newUser);
			newUser.setTrainingAdministrator(trainingAdministrator);
			customer.setCustomerType("b2b");
			
			LearnerProfile newLearnerProfile = new LearnerProfile();
			
			Address newAddress = new Address();
			newAddress.setStreetAddress(addCustomerForm.getAddress1().trim());
			newAddress.setStreetAddress2(addCustomerForm.getAddress1a().trim());
			newAddress.setCity(addCustomerForm.getCity1().trim());
			newAddress.setState(addCustomerForm.getState1().trim());
			newAddress.setZipcode(addCustomerForm.getZip1().trim());
			newAddress.setCountry(addCustomerForm.getCountry1().trim());
			newLearnerProfile.setLearnerAddress(newAddress);
			
			Address newAddress2 = new Address();
			newAddress2.setStreetAddress(addCustomerForm.getAddress2().trim());
			newAddress2.setStreetAddress2(addCustomerForm.getAddress2a().trim());
			newAddress2.setCity(addCustomerForm.getCity2().trim());
			newAddress2.setState(addCustomerForm.getState2().trim());
			newAddress2.setZipcode(addCustomerForm.getZip2().trim());
			newAddress2.setCountry(addCustomerForm.getCountry2().trim());
			newLearnerProfile.setLearnerAddress2(newAddress2);
			
			newLearnerProfile.setMobilePhone(customer.getPhoneNumber());
			
			newLearner.setVu360User(newUser);
			newLearnerProfile.setLearner(newLearner);
			newLearner.setLearnerProfile(newLearnerProfile);
			newLearner.setCustomer(customer);
			
			newUser.setLearner(newLearner);
			List<OrganizationalGroup> groups = new ArrayList<OrganizationalGroup> ();
			groups.add(orgGroup);
			newLearner=learnerService.addLearner(newLearner.getCustomer(), groups, null, newLearner);
			
			createLearnersForCustomer(customer, addCustomerForm, groups, LMSRoleLearner);
			
			return customer;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	 }
	
	private void createLearnersForCustomer(Customer customer, CEPlannerForm addCustomerForm, List<OrganizationalGroup> orgGroups, LMSRole LMSRoleLearner){
		for(int i=0;i<Integer.parseInt(addCustomerForm.getNumberOfReps());i++){
			if(StringUtils.isBlank(addCustomerForm.getFirstNames().get(i))
						|| StringUtils.isBlank(addCustomerForm.getLastNames().get(i))
						||StringUtils.isBlank(addCustomerForm.getEmailAddresses().get(i))
						||StringUtils.isBlank(addCustomerForm.getPasswords().get(i))
			){
				continue;
						
			}
						
			/*
			 * Now I am to create learner
			 * 
			 */
			Learner newLearner = new Learner();
			VU360User newUser = new VU360User();
			
			newUser.setFirstName(addCustomerForm.getFirstNames().get(i));
			//UI has not provided me the middlename
			newUser.setLastName(addCustomerForm.getLastNames().get(i));
			newUser.setEmailAddress(addCustomerForm.getEmailAddresses().get(i));
			newUser.setPassword(addCustomerForm.getPasswords().get(i));
			newUser.setUsername(addCustomerForm.getEmailAddresses().get(i));

			newUser.setAccountNonExpired(true);
			newUser.setAcceptedEULA(false);
			newUser.setNewUser(true);
			newUser.setAccountNonLocked(true);
			newUser.setChangePasswordOnLogin(false);
			newUser.setCredentialsNonExpired(true);
			newUser.setEnabled(true);
			newUser.setVissibleOnReport(true);
			newUser.setExpirationDate(null);
			newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
			Calendar calender=Calendar.getInstance();
			Date createdDate=calender.getTime();
			newUser.setCreatedDate(createdDate);
			// everyone is a learner
			newUser.addLmsRole(LMSRoleLearner);
			
			LearnerProfile newLearnerProfile = new LearnerProfile();
			
			Address newAddress = new Address();
			newAddress.setStreetAddress(addCustomerForm.getAddress1().trim());
			newAddress.setStreetAddress2(addCustomerForm.getAddress1a().trim());
			newAddress.setCity(addCustomerForm.getCity1().trim());
			newAddress.setState(addCustomerForm.getState1().trim());
			newAddress.setZipcode(addCustomerForm.getZip1().trim());
			newAddress.setCountry(addCustomerForm.getCountry1().trim());
			newLearnerProfile.setLearnerAddress(newAddress);
			
			Address newAddress2 = new Address();
			newAddress2.setStreetAddress(addCustomerForm.getAddress2().trim());
			newAddress2.setStreetAddress2(addCustomerForm.getAddress2a().trim());
			newAddress2.setCity(addCustomerForm.getCity2().trim());
			newAddress2.setState(addCustomerForm.getState2().trim());
			newAddress2.setZipcode(addCustomerForm.getZip2().trim());
			newAddress2.setCountry(addCustomerForm.getCountry2().trim());
			newLearnerProfile.setLearnerAddress2(newAddress2);
			newLearnerProfile.setMobilePhone(customer.getPhoneNumber());
			newLearner.setVu360User(newUser);
			newLearnerProfile.setLearner(newLearner);
			newLearner.setLearnerProfile(newLearnerProfile);
			newLearner.setCustomer(customer);
			newUser.setLearner(newLearner);
			
			newLearner=learnerService.addLearner(newLearner.getCustomer(), orgGroups, null, newLearner);
		}
	}

	public CustomerPreferences loadForUpdateCustomerPreferences(long customerPreferecnesId){
		CustomerPreferences customerPreferences =  customerPreferencesRepository.findOne(customerPreferecnesId);
		return customerPreferences;
	}
	
	@Override
	@Transactional
	public void updateLMSFeatures (List<CustomerLMSFeature> customerLMSFeatures)
	{
		CustomerLMSFeature updatableCustomerLMSFeature;
		for(CustomerLMSFeature customerLMSFeature : customerLMSFeatures)
		{
			if(customerLMSFeature.getId()!=null){
				
				updatableCustomerLMSFeature = this.loadCustomerLMSFeatureForUpdate(customerLMSFeature.getId());
				updatableCustomerLMSFeature.setEnabled(customerLMSFeature.getEnabled());
			
			}
			else
			{
				updatableCustomerLMSFeature = new CustomerLMSFeature();
				updatableCustomerLMSFeature.setEnabled(
						customerLMSFeature.getEnabled());
				updatableCustomerLMSFeature.setCustomer(
						customerLMSFeature.getCustomer());
				updatableCustomerLMSFeature.setLmsFeature(
						customerLMSFeature.getLmsFeature());
			}
			customerLMSFeatureRepository.save(updatableCustomerLMSFeature);
		}
	}

	@Override
	@Transactional
	public void addLMSFeatures (List<CustomerLMSFeature> customerLMSFeatures)
	{
		CustomerLMSFeature updatableCustomerLMSFeature;
		for(CustomerLMSFeature customerLMSFeature : customerLMSFeatures)
		{
			updatableCustomerLMSFeature = new CustomerLMSFeature();
			updatableCustomerLMSFeature.setEnabled(
					customerLMSFeature.getEnabled());
			updatableCustomerLMSFeature.setCustomer(
					customerLMSFeature.getCustomer());
			updatableCustomerLMSFeature.setLmsFeature(
					customerLMSFeature.getLmsFeature());
			customerLMSFeatureRepository.save(updatableCustomerLMSFeature);
		}
	}

	@Override
    public CustomerLMSFeature loadCustomerLMSFeatureForUpdate (long customerLMSFeatureId) 
    {
    	return customerLMSFeatureRepository.findById(customerLMSFeatureId);
    }

	public void deleteCustomerLMSFeatures (List<CustomerLMSFeature> customerLMSFeatures)
	{
		customerLMSFeatureRepository.delete(customerLMSFeatures);
	}

	@Override
	public LcmsResource loadForUpdateResource(long brandingId) {
		return lcmsResourceRepository.findByBrandIdAndLocaleIdEqualsAndLanguageIdEqualsAndResourceKeyEquals(new Long(brandingId), LOCALEID , LANGUAGEID, RESOURCEKEY);
	}
	
	@Override
	public void savelcmsResource(LcmsResource lcmsResource) {
		lcmsResourceRepository.save(lcmsResource);		
	}
	public BrandService getBrandService() {
		return brandService;
	}
	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}
     
	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}
	
	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}
	
	@Override
	@Transactional
	public boolean updateFeaturesForCustomer(Customer customer, List<LMSRoleLMSFeature> lmsPermissions ) {
		if ( customer == null ) {
			return false;
		}		
		CustomerLMSFeature defalutPermisson;

		for( LMSRoleLMSFeature lmsPermission :  lmsPermissions ) {
			defalutPermisson = this.loadCustomerLMSFeatureForUpdate(lmsPermission.getId());
			if(defalutPermisson!=null){
			
				defalutPermisson.setEnabled( lmsPermission.getEnabled());
			
			}
			else{
				defalutPermisson=new CustomerLMSFeature();
				defalutPermisson.setEnabled( lmsPermission.getEnabled() );
				defalutPermisson.setCustomer(customer);
				defalutPermisson.setLmsFeature( lmsPermission.getLmsFeature());
			}
				
			//Now update
			customerLMSFeatureRepository.save(defalutPermisson);
			
		}
		return true;
	}
	
	public Customer findDefaultCustomerByDistributor(Distributor dist){
		return (Customer) customerRespository.findDefaultCustomerByDistributor(dist.getId());
	}
	
	@Override
	@Transactional
	public boolean updateDefaultCustomer(Distributor distributor) {
		
		boolean isUpdateSuccessful = false;
		Long s = customerRespository.getAssociatedCustomerCount(distributor.getId());
		if(s!=null && s==1){
			List<Customer> ls = customerRespository.findByDistributorId(distributor.getId());
			Customer c = ls.get(0);
			c.setDefault(true);
			customerRespository.save(c);
			isUpdateSuccessful = true;
		}
		return isUpdateSuccessful;
	}

	public List<Map<Object, Object>> findCustomersSimpleSearch(String name,
			String orderId, String orderDate, String emailAddress,
			VU360User loggedInUser, boolean isExact, int startRowIndex,
			int noOfRecords, ResultSet resultSet, String sortBy, int sortDirection) {
		List<Map<Object, Object>> objCol = customerRespository.findCustomersSimpleSearch(name,orderId, orderDate, emailAddress, loggedInUser, isExact, startRowIndex, noOfRecords, resultSet, sortBy, sortDirection);
		return objCol;
	}

	@Override
	public Customer setDefaultAddressIfNull(Customer customer) {
		boolean updateCustomerRequired=false;
		if(customer.getBillingAddress() == null){
			Address newAdd=new Address();
			newAdd.setCountry("US");
			customer.setBillingAddress(newAdd);
			updateCustomerRequired=true;
		}
		if(customer.getShippingAddress() == null){
			Address newAdd=new Address();
			newAdd.setCountry("US");
			customer.setShippingAddress(newAdd);
			updateCustomerRequired=true;
		}
		if(updateCustomerRequired){
			customer=updateCustomer(customer);
		}

		return customer;
	}

}