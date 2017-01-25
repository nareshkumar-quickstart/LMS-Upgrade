package com.softech.vu360.lms.web.controller.administrator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.util.ORMUtils;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerController;
import com.softech.vu360.lms.web.controller.model.EditDistributorForm;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
//import com.softech.vu360.lms.web.filter.VU360AdminAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.RedirectUtils;
import com.softech.vu360.util.VU360Branding;

/**
 * The controller for edit Distributor Profile, edit Distributor Preferences .
 *
 * @author Bishwajit Maitra
 * @date 06-02-09
 *
 */
public class EditDistributorProfileController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private DistributorService distributorService = null;

	//private static final String EDIT_DISTRIBUTOR_PROFILE_EDITPREFERENCES_ACTION = "editPreferences";
	private static final String EDIT_DISTRIBUTOR_PROFILE_SAVE_ACTION = "save";
	private static final String EDIT_DISTRIBUTOR_EDITPREFERENCES_SAVE_ACTION = "save";
	private static final String EDIT_DISTRIBUTOR_PROFILE_CANCEL_ACTION = "cancel";
	private static final String ENABLE_LCMS_ACTION = "enableLCMS";
	private static final String DISTRIBUTOR_GROUP_DEFAULT_ACTION = "Default";
	private static final String DISTRIBUTOR_GROUPS_SEARCH_ACTION = "Search";
	private static final String DISTRIBUTOR_GROUPS_ALLDISTRIBUTORS_ACTION = "ShowAll";
	
	private String editDistributorProfileTemplate = null;
	private String displayDistributorTemplate = null;
	private String displayDistributorEditPreferencesTemplate = null;
	private String displayDistributorGroupTemplate = null;
	private String editDistributorGroupTemplate = null;
	private String redirectTemplate = null;
	private String enableLCMSTemplate = null;
	private BrandService brandService = null;
	//private String errorMessage = null;


	/**
	 * added by Bishwajit Maitra
	 * this method display distributor profile from database
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			
	public ModelAndView displayEditDistributorProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0019");
			
		Map<Object, Object> context = new HashMap<Object, Object>();
		Object authDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		
		if( authDetails != null && authDetails instanceof VU360UserAuthenticationDetails ) {
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)authDetails;
			Distributor distributor = vu360AuthDetails.getCurrentDistributor();// If change this to Proxy, please verify profile saving
			if( distributor != null ) {
				try {
					/*String distributorID = distributor.getId().toString();
					distributor = distributorService.loadForUpdateDistributor(Long.valueOf(distributorID));*/
					
					if(distributor.getDistributorAddress()!=null){
						Address address1 = ORMUtils.initializeAndUnproxy(distributor.getDistributorAddress());
						context.put("Address1", address1);
						distributor.setDistributorAddress(address1);
					}
					
					if(distributor.getDistributorAddress2()!=null){
						Address address2 = ORMUtils.initializeAndUnproxy(distributor.getDistributorAddress2());
						context.put("Address2", address2);
						distributor.setDistributorAddress2(address2);
					}
					
					if(distributor.getBrand()!=null){
						Brand brand = ORMUtils.initializeAndUnproxy(distributor.getBrand());
						context.put("Brand", brand);
					}
					
					String actionType=request.getParameter("action");			
					if(StringUtils.isNotBlank(actionType)){
						if(actionType.equalsIgnoreCase(EDIT_DISTRIBUTOR_PROFILE_SAVE_ACTION)){
							
							if( this.validateData(request,context,distributor) ) {
	 							context.put("distributor", distributor);
								return new ModelAndView(editDistributorProfileTemplate, "context", context);
							} else {
								this.saveDistributorProfile(request, response,distributor);
								return new ModelAndView(displayDistributorTemplate, "context", context);
							}			
						}
						else if(actionType.equalsIgnoreCase(EDIT_DISTRIBUTOR_PROFILE_CANCEL_ACTION)) {
							context.put("distributorId", request.getParameter("distributorId"));
							return new ModelAndView(redirectTemplate);
						}
						else if(actionType.equalsIgnoreCase(ENABLE_LCMS_ACTION)) {
							context.put("distributorId", request.getParameter("distributorId"));
							return new ModelAndView(enableLCMSTemplate, "context", context);
						}
					}
					
					context.put("distributor", distributor);

					ArrayList<Brand>brandList= brandService.getAllBrands();
					context.put("brandList",brandList);			
					return new ModelAndView(editDistributorProfileTemplate, "context", context);

				} catch (Exception e) {
					log.debug("exception", e);
					throw new IllegalArgumentException("Distributor not found" , e);
				}		
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
	}	

	/**
	 * added by Bishwajit Maitra
	 * this method validate distributor profile field 
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */
	private boolean validateData(HttpServletRequest request, Map<Object, Object> context, Distributor distributor){
		
		if(distributor.getDistributorAddress() == null){
			distributor.setDistributorAddress(new Address());
		}
		if(distributor.getDistributorAddress2() == null){
			distributor.setDistributorAddress2(new Address());
		}
		String eSource = request.getParameter("EventSource");
		if ( eSource != null ){
			
			if(eSource.equalsIgnoreCase("donotValidate")) {

				distributor.setName(request.getParameter("DistributorName"));
				distributor.setFirstName(request.getParameter("FirstName"));
				distributor.setLastName(request.getParameter("LastName"));
				distributor.setDistributorEmail(request.getParameter("EmailAddress"));
				distributor.setOfficePhone(request.getParameter("OfficePhone"));
				distributor.setOfficePhoneExtn(request.getParameter("OfficePhoneExt"));
				distributor.getDistributorAddress().setStreetAddress(request.getParameter("StreetAddress"));
				distributor.getDistributorAddress().setStreetAddress2(request.getParameter("StreetAddress2"));
				distributor.getDistributorAddress2().setStreetAddress(request.getParameter("StreetAddress3"));
				distributor.getDistributorAddress2().setStreetAddress2(request.getParameter("StreetAddress4"));

				distributor.getDistributorAddress().setState(request.getParameter("State"));
				distributor.getDistributorAddress2().setState(request.getParameter("State2"));

				distributor.getDistributorAddress().setCountry(request.getParameter("admCountry"));
				distributor.getDistributorAddress2().setCountry(request.getParameter("admCountry2"));				

				distributor.getDistributorAddress().setCity(request.getParameter("City"));
				distributor.getDistributorAddress2().setCity(request.getParameter("City2"));
				distributor.getDistributorAddress().setZipcode(request.getParameter("ZipCode"));
				distributor.getDistributorAddress2().setZipcode(request.getParameter("ZipCode2"));
				
				// Sana Majeed | 12/30/2009 | LMS-955 | for Website URL validation
				distributor.setWebsiteUrl(request.getParameter("WebsiteURL"));
				return true;
			}
		}
			
		// for Distributor Name
		boolean check = false;
		if (StringUtils.isBlank(request.getParameter("DistributorName"))){
			context.put("validateDistributorName", "error.addDistributor.distributorName.required");
			distributor.setName(null);
			check = true;
		}
		else if (FieldsValidation.isInValidGlobalName(request.getParameter("DistributorName"))){
			context.put("validateDistributorName", "error.editDistributor.distributorName.invalid");
			distributor.setName(null);
			check = true;
		}
		else{
			distributor.setName(request.getParameter("DistributorName"));
		}
		
		// for First Name
		if (StringUtils.isBlank(request.getParameter("FirstName"))){
			context.put("validateFirstName", "error.editDistributorFirstName.required");
			distributor.setFirstName(null);
			check = true;
		}
		else if (FieldsValidation.isInValidGlobalName(request.getParameter("FirstName"))){
			context.put("validateFirstName", "error.editDistributorFirstName.invalid");
			distributor.setFirstName(null);
			check = true;
		}
		else{
			distributor.setFirstName(request.getParameter("FirstName"));
		}		

		// for Last Name
		if (StringUtils.isBlank(request.getParameter("LastName"))){
			context.put("validateLastName", "error.addDistributor.lastName.required");
			distributor.setLastName(null);
			check = true;
		}
		else if (FieldsValidation.isInValidGlobalName(request.getParameter("LastName"))){
			context.put("validateLastName", "error.addDistributor.lastName.invalid");
			distributor.setLastName(null);
			check = true;
		}
		else{
			distributor.setLastName(request.getParameter("LastName"));
		}
		
		// for Email Address
		if (!StringUtils.isBlank(request.getParameter("EmailAddress"))){
			if (!FieldsValidation.isEmailValid(request.getParameter("EmailAddress"))){
				context.put("validateEmailAddress", "error.addDistributor.email.invalid");
				distributor.setDistributorEmail(null);
				check = true;
			}
			else{
				distributor.setDistributorEmail(request.getParameter("EmailAddress"));
			}
		}
	
		// for Office Phone
		if (!StringUtils.isBlank(request.getParameter("OfficePhone").trim())){
			if (FieldsValidation.isInValidOffPhone(request.getParameter("OfficePhone").trim())){
				//errors.rejectValue("officePhone", "error.all.invalidText");
				context.put("validateOfficePhone", "error.editDistributor.Phone.invalidText");

				distributor.setOfficePhone(null);
				check = true;
			}else{
				distributor.setOfficePhone(request.getParameter("OfficePhone"));
			}
		}
		
		// for Office Phone Extension
		if (!StringUtils.isBlank(request.getParameter("OfficePhoneExt").trim())){
			if (FieldsValidation.isInValidMobPhone((request.getParameter("OfficePhoneExt").trim()))){
				context.put("validateOfficePhoneExtn", "error.editDistributor.ext.invalidText");
				distributor.setOfficePhoneExtn(null);
				check = true;
			}
			else
				distributor.setOfficePhoneExtn(request.getParameter("OfficePhoneExt"));
		}

		// for Distributor Address-street Address1
		if (!StringUtils.isBlank(request.getParameter("StreetAddress"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("StreetAddress"))){
				context.put("validateStreetAddress", "error.addDistributor.address1.invalid");
				distributor.getDistributorAddress().setStreetAddress(null);
				check = true;
			}
			else
				distributor.getDistributorAddress().setStreetAddress(request.getParameter("StreetAddress"));

		}
		
		// for Distributor Address-street Address2
		if (!StringUtils.isBlank(request.getParameter("StreetAddress2"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("StreetAddress2"))){
				context.put("validateStreetAddress2", "error.addDistributor.address1.invalid");
				distributor.getDistributorAddress().setStreetAddress2(null);
				check = true;
			}
			else
				distributor.getDistributorAddress().setStreetAddress2(request.getParameter("StreetAddress2"));
		}
		
		// for Distributor Address2-street Address1
		if (!StringUtils.isBlank(request.getParameter("StreetAddress3"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("StreetAddress3"))){
				context.put("validateStreetAddress3", "error.addDistributor.address2.invalid");
				distributor.getDistributorAddress2().setStreetAddress(null);
				check = true;
			}
			else
				distributor.getDistributorAddress2().setStreetAddress(request.getParameter("StreetAddress3"));

		}
		
		// for Distributor Address2-street Address2
		if (!StringUtils.isBlank(request.getParameter("StreetAddress4"))){
			if (FieldsValidation.isInValidAddress(request.getParameter("StreetAddress4"))){
				context.put("validateStreetAddress4", "error.addDistributor.address2.invalid");
				distributor.getDistributorAddress2().setStreetAddress2(null);
				check = true;
			}
			else
				distributor.getDistributorAddress2().setStreetAddress2(request.getParameter("StreetAddress4"));
		}
		
		// for Distributor Address-City
		if (!StringUtils.isBlank(request.getParameter("City"))){
			if (FieldsValidation.isInValidGlobalName(request.getParameter("City"))){
				context.put("validateCity", "error.addDistributor.city1.invalid");
				distributor.getDistributorAddress().setCity(null);
				check = true;
			}
			else{
				distributor.getDistributorAddress().setCity(request.getParameter("City"));
			}
		}
		
		// for Distributor Address2-City
		if (!StringUtils.isBlank(request.getParameter("City2"))){
			if (FieldsValidation.isInValidGlobalName(request.getParameter("City2"))){
				context.put("validateCity2", "error.addDistributor.city2.invalid");
				distributor.getDistributorAddress2().setCity(null);
				check = true;
			}
			else{
				distributor.getDistributorAddress2().setCity(request.getParameter("City2"));
			}
		}
		
		// Sana Majeed | 12/30/2009 | LMS-955 | for Website URL validation
		if (!StringUtils.isBlank(request.getParameter("WebsiteURL").trim())){
			if (!FieldsValidation.isValidWebURL(request.getParameter("WebsiteURL").trim())) {
				context.put("validatewesiteURL", "error.addDistributor.wesiteURL.invalid");	
				distributor.setWebsiteUrl(null);
				check = true;
			}
			else {
				distributor.setWebsiteUrl(request.getParameter("WebsiteURL").trim());
			}
		}	
		
		/* for Zip Code
		if (!StringUtils.isBlank(request.getParameter("ZipCode"))){
			if (StringUtils.length(request.getParameter("ZipCode"))>5){
				context.put("validateZipcode", "error.addDistributor.zip1.invalidlength");
				distributor.getDistributorAddress().setZipcode(null);
				check = true;
			}
			else if (!StringUtils.isNumeric(request.getParameter("ZipCode"))){
				context.put("validateZipcode", "error.addDistributor.zip1.invalidText");
				distributor.getDistributorAddress().setZipcode(null);
				check = true;
			}else{
				distributor.getDistributorAddress().setZipcode(request.getParameter("ZipCode"));
			}
		}

		// for Zip Code2
		if (!StringUtils.isBlank(request.getParameter("ZipCode2"))){
			if (StringUtils.length(request.getParameter("ZipCode2"))>5){
				context.put("validateZipcode2", "error.addDistributor.zip2.invalidlength");
				distributor.getDistributorAddress2().setZipcode(null);
				check = true;
			}
			else if (!StringUtils.isNumeric(request.getParameter("ZipCode2"))){
				context.put("validateZipcode2", "error.addDistributor.zip2.invalidText");
				distributor.getDistributorAddress2().setZipcode(null);
				check = true;
			}else{
				distributor.getDistributorAddress2().setZipcode(request.getParameter("ZipCode2"));
			}
		}
		*/
		
		// to ensure that null pointer exception will not occur
		String zip1 = request.getParameter("ZipCode");
		String zip2 = request.getParameter("ZipCode2");
		String country1 = request.getParameter("admCountry");
		String country2 = request.getParameter("admCountry2");

		if(StringUtils.isNotBlank(country1)) 
			distributor.getDistributorAddress().setCountry(country1.trim());
		
		if(StringUtils.isNotBlank(country2))
			distributor.getDistributorAddress2().setCountry(country2.trim());
		
		if(StringUtils.isNotBlank(zip1))
			distributor.getDistributorAddress().setZipcode(zip1.trim());
		
		if(StringUtils.isNotBlank(zip2))
			distributor.getDistributorAddress2().setZipcode(zip2.trim());
		
		Brander brand =  VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
		if( brand != null)	{
			String country = null ;
			String zipCode = null ;
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			
			country = distributor.getDistributorAddress().getCountry();
			zipCode = distributor.getDistributorAddress().getZipcode();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brand, log) ) {
            	log.debug("ZIP CODE FAILED" );
            	//errors.rejectValue("ZipCode2", ZipCodeValidator.getCountryZipCodeError(form.getCountry1(), form.getBrander()),"");
            	context.put("validateZipcode", ZipCodeValidator.getCountryZipCodeError(country, brand));
            	check = true;
            }				
		
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = distributor.getDistributorAddress2().getCountry();
			zipCode = distributor.getDistributorAddress2().getZipcode();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brand, log) ) {
            	log.debug("ZIP CODE FAILED" );
            	//errors.rejectValue("zip2", ZipCodeValidator.getCountryZipCodeError(form.getCountry2(), form.getBrander()),"");
            	context.put("validateZipcode2", ZipCodeValidator.getCountryZipCodeError(country, brand));
            	check = true;
            }	
			
		}
		
		if (!StringUtils.isBlank(request.getParameter("DistributorCode").trim())){
			if (distributorService.isDistributCodeMappedToMoreThenOneDistributor(request.getParameter("DistributorCode").trim(),  distributor.getId() )) {
				context.put("validateDistributorCode", "error.addDistributor.distributorCode.invalidText");	
				distributor.setDistributorCode(null);
				check = true;
			}
			else {
				distributor.setDistributorCode(request.getParameter("DistributorCode").trim());
			}
		}
		//LMS-20804
		String distributorCode = StringUtils.trim(request.getParameter("DistributorCode"));
		if (StringUtils.isBlank(distributorCode) || StringUtils.isEmpty(distributorCode)){
			distributor.setDistributorCode(null);
		}
		 

		
		// need to add them, as they are missed previously
		distributor.getDistributorAddress().setState(request.getParameter("State"));
		distributor.getDistributorAddress2().setState(request.getParameter("State2"));


		return check;
	}
	
	/*private boolean validate(HttpServletRequest request,Map<Object, Object> context){
		boolean check = false;
		if(StringUtils.isBlank(request.getParameter("DistributorName"))){
			context.put("validateDistributorName","Distributor Name can not be blank");
			check = true;
		}
		if(StringUtils.isBlank(request.getParameter("FirstName"))){
			context.put("validateFirstName","First Name can not be blank");
		}
		if(StringUtils.isBlank(request.getParameter("LastName"))){
			context.put("validateLastName","Last Name can not be blank");
			check = true;
		}
		if (FieldsValidation.isEmailValid(request.getParameter("EmailAddress"))){
			//errors.rejectValue("firstName", "error.all.invalidText");
			context.put("validateEmailAddress","Email Address is not valid");
		}
		if (FieldsValidation.isNumeric(request.getParameter("OfficePhoneExt"))){
			context.put("validateOfficePhoneExt","Office Phone Ext is not valid");
		}
		return check;
	}	*/

	/**
	 * added by Bishwajit Maitra
	 * this method update distributor profile
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */	
	public ModelAndView saveDistributorProfile(HttpServletRequest request, HttpServletResponse response, Distributor authDistributor) {			

		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0019");
				
			EditDistributorForm editDistributorForm = new EditDistributorForm();
			
			editDistributorForm.setName(request.getParameter("DistributorName"));
			
			
			String disCode = StringUtils.trim(request.getParameter("DistributorCode"));
			if(StringUtils.isEmpty(disCode))	
				disCode = null;
						
			editDistributorForm.setDistributorCode(disCode);
			
			editDistributorForm.setFirstName(request.getParameter("FirstName"));
			editDistributorForm.setLastName(request.getParameter("LastName"));
			editDistributorForm.setWebsiteURL(request.getParameter("WebsiteURL"));
			editDistributorForm.setEmailAddress(request.getParameter("EmailAddress"));
			editDistributorForm.setOfficePhone(request.getParameter("OfficePhone"));
			editDistributorForm.setOfficePhoneExt(request.getParameter("OfficePhoneExt"));
			editDistributorForm.setBrandId(Long.parseLong(request.getParameter("BrandId")));			
			editDistributorForm.setType(request.getParameter("type"));
			
			if(request.getParameter("activeRadio").equalsIgnoreCase("active")) {
				editDistributorForm.setActive(Boolean.TRUE);
			}else {
				editDistributorForm.setActive(Boolean.FALSE);
			}			
			if(request.getParameter("selfReportingRadio").equalsIgnoreCase("true")) {
				editDistributorForm.setSelfReporting(Boolean.TRUE);
			} else {
				editDistributorForm.setSelfReporting(Boolean.FALSE);
			}			
			if(request.getParameter("markedPrivateRadio").equalsIgnoreCase("true")) {
				editDistributorForm.setMarkedPrivate(Boolean.TRUE);
			}else {
				editDistributorForm.setMarkedPrivate(Boolean.FALSE);
			}			
			if(request.getParameter("selfAuthor")!=null && request.getParameter("selfAuthor").equalsIgnoreCase("true")) {
				editDistributorForm.setSelfAuthor(Boolean.TRUE);
			}else {
				editDistributorForm.setSelfAuthor(Boolean.FALSE);
			}	
			
			editDistributorForm.setCorporateAuthorVar(Boolean.valueOf(request.getParameter("isCorporateAuthorVar")));

			/**
			 * Call Logging | LMS-8108 | S M Humayun | 27 April 2011
			 */
			if(request.getParameter("callLoggingEnabled")!=null && request.getParameter("callLoggingEnabled").equalsIgnoreCase("true")) {
				editDistributorForm.setCallLoggingEnabled(Boolean.TRUE);
			}else {
				editDistributorForm.setCallLoggingEnabled(Boolean.FALSE);
			}
			
			Address modifiedAddress1 = new Address();
			modifiedAddress1.setStreetAddress(request.getParameter("StreetAddress"));
			modifiedAddress1.setStreetAddress2(request.getParameter("StreetAddress2"));
			modifiedAddress1.setCity(request.getParameter("City"));
			modifiedAddress1.setState(request.getParameter("State"));
			modifiedAddress1.setZipcode(request.getParameter("ZipCode"));
			modifiedAddress1.setCountry(request.getParameter("admCountry"));
			editDistributorForm.setDistributorAddress(modifiedAddress1);

			Address modifiedAddress2 = new Address();
			modifiedAddress2.setStreetAddress(request.getParameter("StreetAddress3"));
			modifiedAddress2.setStreetAddress2(request.getParameter("StreetAddress4"));
			modifiedAddress2.setCity(request.getParameter("City2"));
			modifiedAddress2.setState(request.getParameter("State2"));
			modifiedAddress2.setZipcode(request.getParameter("ZipCode2"));
			modifiedAddress2.setCountry(request.getParameter("admCountry2"));
			editDistributorForm.setDistributorAddress2(modifiedAddress2);
			
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			Distributor distributor = distributorService.updateDistributor(editDistributorForm, authDistributor, loggedInUser);
			//distributorService.saveDistributor(distributor);

			if (distributor != null) {
				authDistributor.setName(distributor.getName());
				authDistributor.setFirstName(distributor.getFirstName());
				authDistributor.setLastName(distributor.getLastName()); // adding for brandName
				authDistributor.setBrandName(distributor.getBrandName());
				authDistributor.setWebsiteUrl(distributor.getWebsiteUrl());
				authDistributor.setDistributorEmail(distributor.getDistributorEmail());
				authDistributor.setOfficePhone(distributor.getOfficePhone());
				authDistributor.setOfficePhoneExtn(distributor.getOfficePhoneExtn());
				authDistributor.setActive(distributor.getActive());
				authDistributor.setSelfReporting(distributor.isSelfReporting());
				
				/**
				 * Call Logging | LMS-8108 | S M Humayun | 27 April 2011
				 */
				authDistributor.setCallLoggingEnabled(distributor.getCallLoggingEnabled());
				
				authDistributor.setMarkedPrivate(distributor.isMarkedPrivate());
				authDistributor.setType(distributor.getType());
				authDistributor.setContentOwner(distributor.getContentOwner());
				if (distributor.getDistributorAddress() != null) {
					authDistributor.getDistributorAddress().setStreetAddress(distributor.getDistributorAddress().getStreetAddress());
					authDistributor.getDistributorAddress().setStreetAddress2(distributor.getDistributorAddress().getStreetAddress2());
					authDistributor.getDistributorAddress().setCity(distributor.getDistributorAddress().getCity());
					authDistributor.getDistributorAddress().setState(distributor.getDistributorAddress().getState());
					authDistributor.getDistributorAddress().setZipcode(distributor.getDistributorAddress().getZipcode());
					authDistributor.getDistributorAddress().setCountry(distributor.getDistributorAddress().getCountry());	
				}
				
				if (distributor.getDistributorAddress2() != null) {
					authDistributor.getDistributorAddress2().setStreetAddress(editDistributorForm.getDistributorAddress2().getStreetAddress());
					authDistributor.getDistributorAddress2().setStreetAddress2(editDistributorForm.getDistributorAddress2().getStreetAddress2());
					authDistributor.getDistributorAddress2().setCity(editDistributorForm.getDistributorAddress2().getCity());
					authDistributor.getDistributorAddress2().setState(editDistributorForm.getDistributorAddress2().getState());
					authDistributor.getDistributorAddress2().setZipcode(editDistributorForm.getDistributorAddress2().getZipcode());
					authDistributor.getDistributorAddress2().setCountry(editDistributorForm.getDistributorAddress2().getCountry());	
				}
			}			
			return new ModelAndView(editDistributorProfileTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editDistributorProfileTemplate);
	}

		
	/**
	 * added by Bishwajit Maitra
	 * this method display distributor Preferences from database
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */
	public ModelAndView displayDistributorEditPreferences(HttpServletRequest request, HttpServletResponse response) throws Exception {			
		
		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0020");
			
		Object authDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if( authDetails != null && authDetails instanceof VU360UserAuthenticationDetails ) {
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)authDetails;
			Distributor distributor = vu360AuthDetails.getCurrentDistributor();// If change this to Proxy, please verify profile saving
			if( distributor != null ) {
				try {			
					Map<Object, Object> context = new HashMap<Object, Object>();

					String actionType=request.getParameter("action");			
					if(StringUtils.isNotBlank(actionType)){
						if(actionType.equalsIgnoreCase(EDIT_DISTRIBUTOR_EDITPREFERENCES_SAVE_ACTION)){
							this.saveDistributorPreferences(request, response, distributor);
							//Map<Object, Object> context = new HashMap<Object, Object>();
						//	context.put("distributor", distributor);
							return new ModelAndView(displayDistributorTemplate);
							//return new ModelAndView(displayDistributorTemplate);
						}else if(actionType.equalsIgnoreCase(EDIT_DISTRIBUTOR_PROFILE_CANCEL_ACTION)) {
							return new ModelAndView(redirectTemplate);
						}
					}	

					String distributorID = distributor.getId().toString();
					distributor = distributorService.getDistributorById(Long.valueOf(distributorID));
					context.put("distributorID", distributorID);
					context.put("distributor", distributor);
					return new ModelAndView(displayDistributorEditPreferencesTemplate, "context", context);

				} catch (Exception e) {
					log.debug("exception", e);
					throw new IllegalArgumentException("Distributor not found" , e);
				}
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
	}
	
	/**
	 * added by Bishwajit Maitra
	 * this method update distributor Prefetrences
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */
	public ModelAndView saveDistributorPreferences(HttpServletRequest request, HttpServletResponse response, Distributor authDistributor) {			

		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0020");
				
			Distributor distributor = null;
			distributor = distributorService.getDistributorById(authDistributor.getId());			
			DistributorPreferences distributorPreferences = null;
			distributorPreferences = distributorService.loadForUpdateDistributorPreferences(distributor.getDistributorPreferences().getId());
			
			if(request.getParameter("Audio").equalsIgnoreCase("true")) {
				distributorPreferences.setAudioEnabled(true);
			}else {
				distributorPreferences.setAudioEnabled(false);
			}
			if(request.getParameter("AudioLocked").equalsIgnoreCase("true")) {
				distributorPreferences.setAudioLocked(new Boolean("true"));
			}else {
				distributorPreferences.setAudioLocked(new Boolean("false"));
			}
			distributorPreferences.setVolume(Integer.parseInt(request.getParameter("volume")));
			if(request.getParameter("VolumeLocked").equalsIgnoreCase("true")) {
				distributorPreferences.setVolumeLocked(true);
			}else {
				distributorPreferences.setVolumeLocked(false);
			}
			if(request.getParameter("Captioning").equalsIgnoreCase("true")) {
				distributorPreferences.setCaptioningEnabled(true);
			}else {
				distributorPreferences.setCaptioningEnabled(false);
			}
			if(request.getParameter("CaptioningLocked").equalsIgnoreCase("true")) {
				distributorPreferences.setCaptioningLocked(true);
			}else {
				distributorPreferences.setCaptioningLocked(false);
			}
			if(request.getParameter("Bandwidth").equalsIgnoreCase("high")) {
				distributorPreferences.setBandwidth("high");
			}else {
				distributorPreferences.setBandwidth("low");
			}
			if(request.getParameter("BandwidthLocked").equalsIgnoreCase("true")) {
				distributorPreferences.setBandwidthLocked(true);
			}else {
				distributorPreferences.setBandwidthLocked(false);
			}
			if(request.getParameter("Video").equalsIgnoreCase("true")) {
				distributorPreferences.setVedioEnabled(true);
			}else {
				distributorPreferences.setVedioEnabled(false);
			}
			if(request.getParameter("VideoLocked").equalsIgnoreCase("true")) {
				distributorPreferences.setVideoLocked(true);
			}else {
				distributorPreferences.setVideoLocked(false);
			}
			if(request.getParameter("RagistrationEmails").equalsIgnoreCase("true")) {
				distributorPreferences.setEnableRegistrationEmailsForNewCustomers(true);
			}else {
				distributorPreferences.setEnableRegistrationEmailsForNewCustomers(false);
			}
			if(request.getParameter("RagistrationEmailsLock").equalsIgnoreCase("true")) {
				distributorPreferences.setRegistrationEmailLocked(true);
			}else {
				distributorPreferences.setRegistrationEmailLocked(false);
			}
			if(request.getParameter("EnrollmentEmails").equalsIgnoreCase("true")) {
				distributorPreferences.setEnableEnrollmentEmailsForNewCustomers(true);
			}else {
				distributorPreferences.setEnableEnrollmentEmailsForNewCustomers(false);
			}	
			if(request.getParameter("EnrollmentEmailsLock").equalsIgnoreCase("true")) {
				distributorPreferences.setEnrollmentEmailLocked(true);
			}else {
				distributorPreferences.setEnrollmentEmailLocked(false);
			}
			if(request.getParameter("CertificateEmails").equalsIgnoreCase("true")) {
				distributorPreferences.setCourseCompletionCertificateEmailEnabled(true);
			}else {
				distributorPreferences.setCourseCompletionCertificateEmailEnabled(false);
			}	
			if(request.getParameter("CertificateEmailsLock").equalsIgnoreCase("true")) {
				distributorPreferences.setCourseCompletionCertificateEmailLocked(true);
			}else {
				distributorPreferences.setCourseCompletionCertificateEmailLocked(false);
			}	
			distributorPreferences=distributorService.saveDistributorPreferences(distributorPreferences);
			
			response.sendRedirect("adm_editDistributorPreferences.do");

		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		
		return null;
	}
	
	public ModelAndView displayDistributorGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{		
		
		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0027");
			

		Object authDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if(authDetails !=null && authDetails instanceof VU360UserAuthenticationDetails){
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)authDetails;
			Distributor distributor = vu360AuthDetails.getCurrentDistributor();// If change this to Proxy, please verify profile saving
			if(distributor!=null){
				try {			
					com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Map<Object, Object> context = new HashMap<Object, Object>();
					//String distributorID=request.getParameter("distributorId");
					String distributorID = distributor.getId().toString();
					String action = request.getParameter("action");
					List<DistributorGroup> distributorGroup=null;
					List<DistributorGroup> distributorGroups=null;
					action = (action == null)?DISTRIBUTOR_GROUP_DEFAULT_ACTION:action;

					if (action.equalsIgnoreCase(DISTRIBUTOR_GROUPS_SEARCH_ACTION)){
						String distributorGroupName = request.getParameter("distributorName");
						distributorGroups=distributorService.findDistributorGroupsByName(distributorGroupName, loggedInUser, false);
						context.put("distributorGroups", distributorGroups);
					}
					if (action.equalsIgnoreCase(DISTRIBUTOR_GROUPS_ALLDISTRIBUTORS_ACTION)){
						//String distributorGroupName = request.getParameter("distributorName");
						distributorGroups=distributorService.findDistributorGroupsByName("", loggedInUser, false);
						context.put("distributorGroups", distributorGroups);
					}

					if(StringUtils.isNotBlank(distributorID)){
						distributorGroup=distributorService.getDistributorGroupsBydistributorId(Long.valueOf(distributorID));
						context.put("distributorID", distributorID);
						context.put("distributorGroup", distributorGroup);
					}			
					return new ModelAndView(displayDistributorGroupTemplate, "context", context);
				} catch (Exception e) {
					log.debug("exception", e);
					throw new IllegalArgumentException("Distributor not found" , e);
				}
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
	}	
	
	/**
	 * exception handler method for this multiaction
	 * to be used if the distributor could not be resolved
	 */
	public void handleDistributorNotFoundException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException ex ) throws IOException{
		String url = "/adm_searchMember.do?method=displaySearchMemberView&isRedirect=d";
		RedirectUtils.sendRedirect(request, response, url, false);
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public String getEditDistributorProfileTemplate() {
		return editDistributorProfileTemplate;
	}

	public void setEditDistributorProfileTemplate(
			String editDistributorProfileTemplate) {
		this.editDistributorProfileTemplate = editDistributorProfileTemplate;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public String getDisplayDistributorTemplate() {
		return displayDistributorTemplate;
	}

	public void setDisplayDistributorTemplate(String displayDistributorTemplate) {
		this.displayDistributorTemplate = displayDistributorTemplate;
	}

	public String getDisplayDistributorGroupTemplate() {
		return displayDistributorGroupTemplate;
	}

	public void setDisplayDistributorGroupTemplate(
			String displayDistributorGroupTemplate) {
		this.displayDistributorGroupTemplate = displayDistributorGroupTemplate;
	}

	public static String getDISTRIBUTOR_GROUP_DEFAULT_ACTION() {
		return DISTRIBUTOR_GROUP_DEFAULT_ACTION;
	}

	public String getEditDistributorGroupTemplate() {
		return editDistributorGroupTemplate;
	}

	public void setEditDistributorGroupTemplate(String editDistributorGroupTemplate) {
		this.editDistributorGroupTemplate = editDistributorGroupTemplate;
	}

	public static String getEDIT_DISTRIBUTOR_EDITPREFERENCES_SAVE_ACTION() {
		return EDIT_DISTRIBUTOR_EDITPREFERENCES_SAVE_ACTION;
	}

	public String getDisplayDistributorEditPreferencesTemplate() {
		return displayDistributorEditPreferencesTemplate;
	}

	public void setDisplayDistributorEditPreferencesTemplate(
			String displayDistributorEditPreferencesTemplate) {
		this.displayDistributorEditPreferencesTemplate = displayDistributorEditPreferencesTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
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