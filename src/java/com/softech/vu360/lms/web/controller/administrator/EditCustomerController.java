/**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerController;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;
import com.softech.vu360.lms.web.controller.model.EditDistributor;
import com.softech.vu360.lms.web.controller.validator.EditCustomerValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 *
 */
public class EditCustomerController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());

	private String editCustomerProfileTemplate = null;
	private String editCustomerPreferencesTemplate = null;
	private String editCustomerDistributorTemplate = null;
	private String redirectTemplate = null;
	private String failureTemplate = null;
	private String cancelTemplate = null;
	private String enableLCMSTemplate = null;
	
	private DistributorService distributorService = null;
	private CustomerService customerService = null;
	private LearnerService learnerService = null;
	private BrandService brandService = null;

	public EditCustomerController() {
		super();
	}

	public EditCustomerController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		log.debug("IN handleNoSuchRequestHandlingMethod");
		return new ModelAndView(redirectTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof EditCustomerForm ){
			EditCustomerForm form = (EditCustomerForm)command;
			form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
			if( methodName.equals("editCustomerProfile")
					|| methodName.equals("editCustomerPreferences")
					|| methodName.equals("editCustomerDistributor") ) {
				form.reset();
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				//VU360User loggedInUser =  VU360UserAuthenticationDetails.getCurrentUser();
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				form.setMultiLearner(false);
				
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
					List<OrganizationalGroup> tempManagedGroups = learnerService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
					Customer customer = details.getCurrentCustomer();
					if(customer!=null)					
					//*
					// * the below check can be more optimized, 
					// * By implementing COUNT query, desired result can be achieve because "user_list" is not fetching for necessary purpose here. 
					// TODO
					if(methodName.equals("editCustomerProfile") || methodName.equals("editCustomerDistributor")){
//						LMS:3462, If customer has more than one learner then it cannot be changed into B2C type.
						Map<Object,Object> results = new HashMap<Object,Object>();
						results =learnerService.findAllLearners("", 
								loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								1, 1, "firstName",5);
						List<VU360User>userList = (List<VU360User>)results.get("list");
						if(userList.size()>1){
							form.setMultiLearner(true);
						}
					}
					if( customer != null ){
						form.setId(customer.getId());
						form.setCustomer(customer);
						form.setName(customer.getName());
						form.setFirstName(customer.getFirstName());
						form.setLastName(customer.getLastName());
						form.setSelfAuthor(customer.isSelfAuthor());
						form.setDistributorRepresentative(customer.isDistributorRepresentative());
						form.setBrandName(customer.getBrandName()); // setting the brand name
						form.setBrandId(customer.getBrand()!=null?customer.getBrand().getId():0);
						form.setEmailAddress(customer.getEmail());
						form.setWebsiteUrl(customer.getWebsiteUrl());
						form.setType(customer.getCustomerType().toUpperCase());
						form.setPhone(customer.getPhoneNumber());
						form.setExtension(customer.getPhoneExtn());
						
						// If address is null then set default address with country =US
						customer=customerService.setDefaultAddressIfNull(customer);

						if( customer.getBillingAddress() != null ) {
							form.setBillingAddress1(customer.getBillingAddress().getStreetAddress());
							form.setBillingAddress2(customer.getBillingAddress().getStreetAddress2());
							form.setBillingCity(customer.getBillingAddress().getCity());
							form.setBillingCountry(customer.getBillingAddress().getCountry());
							form.setBillingState(customer.getBillingAddress().getState());
							form.setBillingZip(customer.getBillingAddress().getZipcode());
						}
						if( customer.getShippingAddress() != null ) {
							form.setShippingAddress1(customer.getShippingAddress().getStreetAddress());
							form.setShippingAddress2(customer.getShippingAddress().getStreetAddress2());
							form.setShippingCity(customer.getShippingAddress().getCity());
							form.setShippingCountry(customer.getShippingAddress().getCountry());
							form.setShippingState(customer.getShippingAddress().getState());
							form.setShippingZip(customer.getShippingAddress().getZipcode());
						}
						form.setStatus(customer.isActive());
						if( customer.getCustomerPreferences() != null ) {
                            form.setBlanksearch(customer.getCustomerPreferences().isBlankSearchEnabled());
							form.setAudio(customer.getCustomerPreferences().isAudioEnabled());
							form.setAudioLocked(customer.getCustomerPreferences().isAudioLocked());
							String bandwidth = customer.getCustomerPreferences().getBandwidth();
							if( bandwidth.equalsIgnoreCase("high") ) 
								form.setBandwidth(true);
							else
								form.setBandwidth(false);
							form.setBandwidthLocked(customer.getCustomerPreferences().isBandwidthLocked());
							form.setCaptioning(customer.getCustomerPreferences().isCaptioningEnabled());
							form.setCaptioningLocked(customer.getCustomerPreferences().isCaptioningLocked());
							form.setEnrollEmail(customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers());
							form.setRegEmail(customer.getCustomerPreferences().isEnableRegistrationEmailsForNewCustomers());
							form.setVedio(customer.getCustomerPreferences().isVedioEnabled());
							form.setVedioLocked(customer.getCustomerPreferences().isVideoLocked());
							form.setVolume(customer.getCustomerPreferences().getVolume());
							form.setVolumeLocked(customer.getCustomerPreferences().isVolumeLocked());
							form.setCourseCompCertificateEmails(customer.getCustomerPreferences().isCourseCompletionCertificateEmailEnabled());
							form.setCourseCompCertificateEmailsLocked(customer.getCustomerPreferences().isCourseCompletionCertificateEmailLocked());
						}
					}
				}
			} else if(methodName.equals("saveCustomerDistributor")) {
				String distId = "0";
				String[] selectedDists = request.getParameterValues("dists");
				if( selectedDists != null ) {
					for( int distLength = 0; distLength < selectedDists.length; distLength ++) {
						distId = selectedDists[distLength];
						break;
					}
				}
				long selectedDistId = Long.valueOf(distId);
				for(EditDistributor editDist : form.getDistributors()) {
					editDist.setSelected(false);
					if( editDist.getDistributor().getId() == selectedDistId ) {
						editDist.setSelected(true);
					}
				}
			}
		}
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		EditCustomerForm form = (EditCustomerForm)command; 
		EditCustomerValidator validator = (EditCustomerValidator)this.getValidator();

		if( methodName.equals("saveCustomerProfile")) {
			validator.validateProfilePage(form, errors);
		}
	}

	public ModelAndView enableLCMS(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("customerId", request.getParameter("customerId"));
		return new ModelAndView(enableLCMSTemplate, "context", context);
	}
	
	public ModelAndView editCustomerProfile(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0013");
				
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null && details.getCurrentSearchType()==AdminSearchType.CUSTOMER){
					customer = details.getCurrentCustomer();
				}else{
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			} else {
				// admin has not selected any customer
				return new ModelAndView(failureTemplate,"isRedirect","c");
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			ArrayList<Brand>brandList= brandService.getAllBrands();
			context.put("brandList",brandList);
			return new ModelAndView(editCustomerProfileTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerProfileTemplate);
	}

	public ModelAndView saveCustomerProfile(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try  {

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0013");
				
			EditCustomerForm form = (EditCustomerForm)command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			if(errors.hasErrors() || form.getEventSource().equalsIgnoreCase("donotValidate")){
				// matching EventSource - if country is changed , then no need to validate
				context.put("surveyEvents", Survey.SURVEY_EVENTS);
				return new ModelAndView(editCustomerProfileTemplate, "context", context);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				customer = details.getCurrentCustomer();
				if( customer == null ){
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			}			
			
			//customerService.saveCustomer(customer);
			customer = customerService.updateCustomer(form, customer);
			form.setDistributorRepresentative(customer.isDistributorRepresentative());
			form.setSelfAuthor(customer.isSelfAuthor());
			form.setCustomer(customer); 
			
			VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			request.setAttribute("SwitchType", AdminSearchType.CUSTOMER);
			request.setAttribute("SwitchCustomer", ProxyVOHelper.setCustomerProxy(customer));
			adminDetails.doPopulateAdminSearchInformation(request);
			ArrayList<Brand>brandList= brandService.getAllBrands();
			context.put("brandList",brandList);			
			return new ModelAndView(editCustomerProfileTemplate, "context", context);

		} catch (Exception e) {
			e.printStackTrace();
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerProfileTemplate);
	}

	public ModelAndView editCustomerPreferences(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0014");
				
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null && details.getCurrentSearchType()==AdminSearchType.CUSTOMER){
					//customer = details.getCurrentCustomer();
				}else{
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			} else {
				// admin has not selected any customer
				return new ModelAndView(failureTemplate,"isRedirect","c");
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			return new ModelAndView(editCustomerPreferencesTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerPreferencesTemplate);
	}

	public ModelAndView saveCustomerPreferences(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0014");
				
			EditCustomerForm form = (EditCustomerForm)command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			if(errors.hasErrors()){

				context.put("surveyEvents", Survey.SURVEY_EVENTS);
				return new ModelAndView(editCustomerPreferencesTemplate, "context", context);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;
			long preferenceId = 0;

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null ){
					customer = details.getCurrentCustomer();
					if( customer.getCustomerPreferences() != null ) {
						preferenceId = customer.getCustomerPreferences().getId();
					}
				}else{
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			}
			CustomerPreferences preferences = customer.getCustomerPreferences();
			if( preferenceId != 0 ) {
				preferences.setId(preferenceId);
			}	
			
			preferences=customerService.loadForUpdateCustomerPreferences(preferences.getId());
            preferences.setBlankSearchEnabled(form.getBlanksearch());
            preferences.setAudioEnabled(form.getAudio());
			preferences.setAudioLocked(form.getAudioLocked());
			preferences.setVedioEnabled(form.getVedio());
			preferences.setVideoLocked(form.getVedioLocked());
			preferences.setVolume(form.getVolume());
			preferences.setVolumeLocked(form.getVolumeLocked());
			preferences.setCaptioningEnabled(form.getCaptioning());
			preferences.setCaptioningLocked(form.getCaptioningLocked());						
			preferences.setBandwidth(form.getBandwidth()?preferences.BANDWIDTH_HIGH:preferences.BANDWIDTH_LOW);						
			preferences.setEnableRegistrationEmailsForNewCustomers(form.getRegEmail());
			preferences.setBandwidthLocked(form.getBandwidthLocked());
			preferences.setEnableEnrollmentEmailsForNewCustomers(form.getEnrollEmail());
			customer.setCustomerPreferences(preferences);
			preferences.setCustomer(customer);
			preferences.setCourseCompletionCertificateEmailEnabled(form.isCourseCompCertificateEmails());
			preferences.setCourseCompletionCertificateEmailLocked(form.isCourseCompCertificateEmailsLocked()); 
			customerService.saveCustomerPreferences(preferences);
			return new ModelAndView(editCustomerPreferencesTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerPreferencesTemplate);
	}

	public ModelAndView editCustomerDistributor(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() == null ){
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			} else {
				// admin has not selected any customer
				return new ModelAndView(failureTemplate,"isRedirect","c");
			}
			Map<Object, Object> context = new HashMap<Object, Object>();

			return new ModelAndView(editCustomerDistributorTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerDistributorTemplate);
	}

	public ModelAndView searchCustomerDistributor(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {
			EditCustomerForm form = (EditCustomerForm)command;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null ){
					customer = details.getCurrentCustomer();
				}else{
					return new ModelAndView(failureTemplate,"isRedirect","c");
				}
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			String distributorName = request.getParameter("distributorName");
			
			List<Distributor> dists = distributorService.findDistributorsByName(distributorName, loggedInUser, false,0,-1,new ResultSet(),null,0);
			List<EditDistributor> formDists = new ArrayList<EditDistributor>();
            assert customer != null;
            Distributor dist = customer.getDistributor();
			for( Distributor distributor : dists ) {
				EditDistributor editDistributor = new EditDistributor();
				editDistributor.setDistributor(distributor);
				editDistributor.setSelected(false);
				if(distributor.getId().equals(dist.getId())) {
					editDistributor.setSelected(true);
				}
				formDists.add(editDistributor);
			}
			form.setDistributors(formDists);
			
			return new ModelAndView(editCustomerDistributorTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerDistributorTemplate);
	}

	public ModelAndView saveCustomerDistributor(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {
			EditCustomerForm form = (EditCustomerForm)command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			if(errors.hasErrors()){

				context.put("surveyEvents", Survey.SURVEY_EVENTS);
				return new ModelAndView(editCustomerDistributorTemplate, "context", context);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;

			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null ){
					customer = details.getCurrentCustomer();
				}else{
					return new ModelAndView(failureTemplate,"isRedirect",1);
				}
			}
			List<EditDistributor> formDists = form.getDistributors();
			for( EditDistributor distributor : formDists ) {
				if( distributor.isSelected() == true ) {
					log.debug("D"+distributor.getDistributor().getName());
					customer.setDistributor(distributor.getDistributor());
					break;
				}
			}
			customerService.saveCustomer(customer,true);
			return new ModelAndView(editCustomerDistributorTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerDistributorTemplate);
	}
	
	public ModelAndView cancelEditCustomer(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		return new ModelAndView(cancelTemplate);
	}

	/*
	 *  getter & setters
	 */

	public String getEditCustomerProfileTemplate() {
		return editCustomerProfileTemplate;
	}

	public void setEditCustomerProfileTemplate(String editCustomerProfileTemplate) {
		this.editCustomerProfileTemplate = editCustomerProfileTemplate;
	}

	public String getEditCustomerPreferencesTemplate() {
		return editCustomerPreferencesTemplate;
	}

	public void setEditCustomerPreferencesTemplate(String editCustomerPreferencesTemplate) {
		this.editCustomerPreferencesTemplate = editCustomerPreferencesTemplate;
	}

	public String getEditCustomerDistributorTemplate() {
		return editCustomerDistributorTemplate;
	}

	public void setEditCustomerDistributorTemplate(String editCustomerDistributorTemplate) {
		this.editCustomerDistributorTemplate = editCustomerDistributorTemplate;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public String getFailureTemplate() {
		return failureTemplate;
	}

	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getEnableLCMSTemplate() {
		return enableLCMSTemplate;
	}

	public void setEnableLCMSTemplate(String enableLCMSTemplate) {
		this.enableLCMSTemplate = enableLCMSTemplate;
	}

	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}

	
	

}