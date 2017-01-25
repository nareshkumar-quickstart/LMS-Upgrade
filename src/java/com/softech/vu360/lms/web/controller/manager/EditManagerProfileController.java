/**
 * 
 */
package com.softech.vu360.lms.web.controller.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;
import com.softech.vu360.lms.web.controller.validator.EditCustomerValidator;
//import com.softech.vu360.lms.web.filter.VU360AdminAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 *
 */
public class EditManagerProfileController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());

	private String editCustomerProfileTemplate = null;
	private String editCustomerPreferencesTemplate = null;
	private String editCustomerDistributorTemplate = null;
	private String redirectTemplate = null;
	private String redirectToCustProfileTemplate = null;
	//private String failureTemplate = null;
	private String cancelTemplate = null;

	//private DistributorService distributorService = null;
	private CustomerService customerService = null;

	public EditManagerProfileController() {
		super();
	}

	public EditManagerProfileController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		//return editCustomerProfile( request, response );
		//return null;
		log.debug("IN handleNoSuchRequestHandlingMethod");
		return new ModelAndView(redirectTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {

		if( command instanceof EditCustomerForm ){
			EditCustomerForm form = (EditCustomerForm)command;
			if( methodName.equals("editCustomerProfile")
					|| methodName.equals("editCustomerPreferences")) {
					 
				form.reset();
				/*@Kaunain - This solution was given by junaid results in null pointer exception, Please do not attempt 
				 * to get id from form as its always null*/
				//long id=form.getCustomer().getId();
				
				/*@Kaunain get current user works in most cases. Although anomolies are not analyzed*/
				//Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getProxyCustomer();
				//@Wajahat: Changing to getCurrentCustomer() because irrelavant insert queries is being executed due to setting NULL for Address in getProxyCustomer() 
				Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
				
				if( customer != null ){
					form.setId(customer.getId());
					form.setCustomer(customer);
					form.setName(customer.getName());
					form.setFirstName(customer.getFirstName());
					form.setLastName(customer.getLastName());
					form.setEmailAddress(customer.getEmail());
					form.setWebsiteUrl(customer.getWebsiteUrl());
					form.setType(customer.getCustomerType());
					form.setPhone(customer.getPhoneNumber());
					form.setExtension(customer.getPhoneExtn());
					form.setBrandId(customer.getBrand()!=null?customer.getBrand().getId():0);

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
						form.setRegEmailLocked(customer.getCustomerPreferences().isRegistrationEmailLocked());
						form.setEnrollEmailLocked(customer.getCustomerPreferences().isEnrollmentEmailLocked());
						form.setCourseCompCertificateEmails(customer.getCustomerPreferences().isCourseCompletionCertificateEmailEnabled());
						form.setCourseCompCertificateEmailsLocked(customer.getCustomerPreferences().isCourseCompletionCertificateEmailLocked());

					}
				}else{
					// do nothing
				}
			}	
		}
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		EditCustomerForm form = (EditCustomerForm)command; 
		EditCustomerValidator validator = (EditCustomerValidator)this.getValidator();

		if( methodName.equals("saveCustomerProfile") && form.getEventSource().equals("validate")) {
			form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
			validator.validateProfilePage(form, errors);
		}
	}

	public ModelAndView editCustomerProfile(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			return new ModelAndView(editCustomerProfileTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerProfileTemplate);
	}

	public ModelAndView saveCustomerProfile(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		Customer customer=null;
		try  {
			EditCustomerForm form = (EditCustomerForm)command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			if(errors.hasErrors()|| ! form.getEventSource().equalsIgnoreCase("validate")){

				//context.put("surveyEvents", Survey.SURVEY_EVENTS);
				return new ModelAndView(editCustomerProfileTemplate, "context", context);
			}
			
			if(form.getCustomer()!=null && form.getCustomer().getId()!=null){
				customer = customerService.loadForUpdateCustomer(form.getCustomer().getId());
			}
			else{
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.
					getContext().getAuthentication().getDetails()).getCurrentCustomer();
			}
            //try to refresh it.Not sure why . this works now 
			
			customer.setName(form.getName());
			customer.setFirstName(form.getFirstName());
			customer.setLastName(form.getLastName());
			customer.setWebsiteUrl(form.getWebsiteUrl());
			customer.setEmail(form.getEmailAddress());
			customer.setPhoneNumber(form.getPhone());
			customer.setPhoneExtn(form.getExtension());

			Address customerBillingAddress = customer.getBillingAddress();
			Address customerShippingAddress = customer.getShippingAddress();
			
			/*@Kaunain - below code snippet was added to cater null pointer exception while getting billing address
			 * and setting it to null street address*/
			
			if(customerBillingAddress==null){
				customerBillingAddress=new Address();
			}
			if(customerShippingAddress==null){
				customerShippingAddress=new Address();
			}
			customerBillingAddress.setStreetAddress(form.getBillingAddress1());
			customerBillingAddress.setStreetAddress2(form.getBillingAddress2());
			customerBillingAddress.setCity(form.getBillingCity());
			customerBillingAddress.setState(form.getBillingState());
			customerBillingAddress.setZipcode(form.getBillingZip());
			customerBillingAddress.setCountry(form.getBillingCountry());
			customerShippingAddress.setStreetAddress(form.getShippingAddress1());
			customerShippingAddress.setStreetAddress2(form.getShippingAddress2());
			customerShippingAddress.setCity(form.getShippingCity());
			customerShippingAddress.setState(form.getShippingState());
			customerShippingAddress.setZipcode(form.getShippingZip());
			customerShippingAddress.setCountry(form.getShippingCountry());
			customer.setActive(form.getStatus());
			customer.setBillingAddress(customerBillingAddress);
			customer.setShippingAddress(customerShippingAddress);
			
			customerService.saveCustomer(customer,CustomerUtil.isUserProfileUpdateOnSF(customer));
			
			return new ModelAndView(redirectToCustProfileTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
			if(customer!=null){
			customerService.saveCustomer(customer,CustomerUtil.isUserProfileUpdateOnSF(customer));
			}
		}
		return new ModelAndView(redirectToCustProfileTemplate);
	}

	public ModelAndView editCustomerPreferences(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		try {
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
			EditCustomerForm form = (EditCustomerForm)command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			if(errors.hasErrors()){

				//context.put("surveyEvents", Survey.SURVEY_EVENTS);
				return new ModelAndView(editCustomerPreferencesTemplate, "context", context);
			}
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.
					getContext().getAuthentication().getDetails()).getCurrentCustomer();
			// sorry to refresh, but it works
			customer = customerService.getCustomerById(customer.getId());	
			CustomerPreferences preferences = customer.getCustomerPreferences();
			preferences = customerService.loadForUpdateCustomerPreferences(preferences.getId());
			if( preferences ==null ) {
				preferences = new CustomerPreferences();
			}	
			preferences.setAudioEnabled(form.getAudio());
			preferences.setAudioLocked(form.getAudioLocked());
			preferences.setVedioEnabled(form.getVedio());
			preferences.setVideoLocked(form.getVedioLocked());
			preferences.setVolume(form.getVolume());
			preferences.setVolumeLocked(form.getVolumeLocked());
			preferences.setCaptioningEnabled(form.getCaptioning());
			preferences.setCaptioningLocked(form.getCaptioningLocked());
			boolean bandwidth = form.getBandwidth();
			if( bandwidth == true )
				preferences.setBandwidth(preferences.BANDWIDTH_HIGH);
			else
				preferences.setBandwidth(preferences.BANDWIDTH_LOW);

			preferences.setEnableRegistrationEmailsForNewCustomers(form.getRegEmail());
			preferences.setBandwidthLocked(form.getBandwidthLocked());
			preferences.setEnableEnrollmentEmailsForNewCustomers(form.getEnrollEmail());
			preferences.setRegistrationEmailLocked(form.getRegEmailLocked());
			preferences.setEnrollmentEmailLocked(form.getEnrollEmailLocked());
			preferences.setCourseCompletionCertificateEmailEnabled(form.isCourseCompCertificateEmails());
			preferences.setCourseCompletionCertificateEmailLocked(form.isCourseCompCertificateEmailsLocked());
			customer.setCustomerPreferences(preferences);
			preferences.setCustomer(customer);
			customerService.saveCustomerPreferences(preferences);
			return new ModelAndView(editCustomerPreferencesTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomerPreferencesTemplate);
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

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	/**
	 * @return the redirectToCustProfileTemplate
	 */
	public String getRedirectToCustProfileTemplate() {
		return redirectToCustProfileTemplate;
	}

	/**
	 * @param redirectToCustProfileTemplate the redirectToCustProfileTemplate to set
	 */
	public void setRedirectToCustProfileTemplate(
			String redirectToCustProfileTemplate) {
		this.redirectToCustProfileTemplate = redirectToCustProfileTemplate;
	}

}