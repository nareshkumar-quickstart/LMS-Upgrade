/**
 * 
 */
package com.softech.vu360.lms.web.controller.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.impl.lmsapi.UpdateMySqlUser;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.learner.LearnerProfileController;
import com.softech.vu360.lms.web.controller.model.UserForm;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.UserValidator;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.LearnerOfLicenseSort;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Arijit
 *
 */
public class EditUserController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(LearnerProfileController.class.getName());

	private LearnerService learnerService;
	private VU360UserService vu360UserService;
	private String editUserTemplate = null;
	private VelocityEngine velocityEngine;
	private CustomFieldService customFieldService;
	private ActiveDirectoryService activeDirectoryService;
    private LearnerLicenseService learnerLicenseServices;
   
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}
	

	public EditUserController() {
		super();
	}

	public EditUserController(Object delegate) {
		super(delegate);
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		UserForm form = (UserForm)command;
		Long userId= ServletRequestUtils.getLongParameter(request, "userId");

		//if(userId==null){
		//	return new ModelAndView("redirect:mgr_manageLearners.do");
		//}else {
		if(methodName.equals("displayUser")){
			VU360User vu360User = this.getVu360UserService().loadForUpdateVU360User(userId);

			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();

			/*
			 * If the user belong to the same customer
			 * */
			if(vu360User !=null && vu360User.getLearner().getCustomer().getId().compareTo(customer.getId()) == 0){

				form.setVu360User(vu360User);
				
				if (vu360User.getNotifyOnLicenseExpire()) {
					form.setNotifyOnLicenseExpire(true);
				} else {
					form.setNotifyOnLicenseExpire(false);
				}

				CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
				List<CreditReportingField> customFieldList=learnerService.getCreditReportingFieldsByLearnerCourseApproval(vu360User.getLearner());
				List<CreditReportingFieldValue> customFieldValueList = learnerService.getCreditReportingFieldValues(vu360User.getLearner());

				List<LicenseOfLearner> list = learnerLicenseServices.getAllLicensesOfLearner(vu360User.getLearner().getId());
				form.setLearnerOfLicense(list);
				
				Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();

				for(CreditReportingField creditReportingField : customFieldList){
				  if(!(creditReportingField instanceof StaticCreditReportingField)){
					if (creditReportingField instanceof SingleSelectCreditReportingField || 
							creditReportingField instanceof MultiSelectCreditReportingField) {

						List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = learnerService.getChoicesByCreditReportingField(creditReportingField);

						if(creditReportingField instanceof MultiSelectCreditReportingField){
							CreditReportingFieldValue creditReportingFieldValue=this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, customFieldValueList);
							existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(), creditReportingFieldValue.getCreditReportingValueChoices());
							
							//Sort the list selected values on top of list
							List<CreditReportingFieldValueChoice> tempCreditReportingFieldValueOptionList = new ArrayList<CreditReportingFieldValueChoice>();
							
							for(CreditReportingFieldValueChoice fieldChoice: creditReportingFieldValue.getCreditReportingValueChoices()){
								
								for (Iterator iterator = creditReportingFieldValueOptionList.iterator(); iterator.hasNext();) {
									CreditReportingFieldValueChoice fieldList = (CreditReportingFieldValueChoice) iterator.next();
									
									if(fieldChoice.getId() == fieldList.getId()){
										iterator.remove();
										tempCreditReportingFieldValueOptionList.add(0,fieldList);
										
									}
								}
							}
							
							//Collections.sort(tempCreditReportingFieldValueOptionList, new ReportingFieldComparable());
							//Collections.sort(creditReportingFieldValueOptionList, new ReportingFieldComparable());
							/*
							for(CreditReportingFieldValueChoice tempChoiceToAdd:tempCreditReportingFieldValueOptionList){
								creditReportingFieldValueOptionList.add(0, tempChoiceToAdd);
							}
							*/
							for(int iCount = tempCreditReportingFieldValueOptionList.size(); iCount>0;iCount--)
							{
								int sortedItemsize = iCount - 1;
								CreditReportingFieldValueChoice tempChoiceToAdd = tempCreditReportingFieldValueOptionList.get(sortedItemsize); 
								creditReportingFieldValueOptionList.add(0, tempChoiceToAdd);
							}
							
						}
						
						fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList,creditReportingFieldValueOptionList);

					} else {
						fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList);
					}
				  }
				}

				List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = fieldBuilder.getCreditReportingFieldList();

				for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : creditReportingFields){
					if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){
						List<CreditReportingFieldValueChoice> existingChoices = existingCreditReportingFieldValueChoiceMap.get(field.getCreditReportingFieldRef().getId());
						Map<Long,CreditReportingFieldValueChoice> existingChoicesMap = new HashMap<Long,CreditReportingFieldValueChoice>();

						for (CreditReportingFieldValueChoice choice : existingChoices){
							existingChoicesMap.put(choice.getId(), choice);
						}

						for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice tempChoice : field.getCreditReportingFieldValueChoices()){
							if(existingChoicesMap.containsKey(tempChoice.getCreditReportingFieldValueChoiceRef().getId())){
								tempChoice.setSelected(true);
							}
						}
					}
				}
				form.setCreditReportingFields(creditReportingFields);
				customFieldService.createValueRecordForStaticReportingField(vu360User, customFieldList, customFieldValueList);				
				//for Custom Fields
				
				
//				List<CustomField> distCustomFieldList=vu360User.getLearner().getCustomer().getDistributor().getCustomFields();
//				List<CustomField> myCustomFieldList=vu360User.getLearner().getCustomer().getCustomFields();
//				if(distCustomFieldList!=null)
//					myCustomFieldList.addAll(vu360User.getLearner().getCustomer().getDistributor().getCustomFields());
//				
//				List<CustomFieldValue> myCustomFieldValueList = vu360User.getLearner().getLearnerProfile().getCustomFieldValues();
//
//				Map<Long,List<CustomFieldValueChoice>> existingCustomerFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

				
				List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
				List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
				//customFieldValues = ;
				//Customer customer = vu360User.getLearner().getCustomer();
				Distributor reseller = vu360User.getLearner().getCustomer().getDistributor();
				CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
				customFieldValues = vu360User.getLearner().getLearnerProfile().getCustomFieldValues() ;
				totalCustomFieldList.addAll(customer.getCustomFields());
				totalCustomFieldList.addAll(reseller.getCustomFields());
				
				Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

				for(CustomField customField:totalCustomFieldList){
					if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
						List<CustomFieldValueChoice> customFieldValueChoices=this.getCustomFieldService().getOptionsByCustomField(customField);
						fieldBuilder2.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

						if (customField instanceof MultiSelectCustomField){
							CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
							existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
						}

					}else {
						fieldBuilder2.buildCustomField(customField,0,customFieldValues);
					}
				}

				List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList2 =fieldBuilder2.getCustomFieldList();

				for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList2){
					if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){
						List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
						Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

						for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
							tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
						}

						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
							if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
								customFieldValueChoice.setSelected(true);
							}
						}
					}
				}
				
				form.setCustomFields(customFieldList2);
				
				//End Custom Fields
				
				
			}

		}

		//}

		//return new ModelAndView("redirect:mgr_manageLearners.do");
		// TODO Auto-generated method stub

	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		UserForm form = (UserForm)command;
		UserValidator validator = (UserValidator)this.getValidator();
		
		if(methodName.equals("updateUser")){
			if( form.getEventSource().equalsIgnoreCase("donotValidate"))
				return ;
			validator.validate(form, errors);
			
			VU360User usr=vu360UserService.findUserByUserName(form.getVu360User().getUsername()); 
			if(!form.getVu360User().getUsername().trim().isEmpty()&& usr!=null && usr.getId().longValue()!=form.getVu360User().getId().longValue())
			{
				errors.rejectValue("vu360User.username", "error.addNewLearner.username.all.existUsername","");
				
			}
			else if (!form.getVu360User().getUsername().trim().isEmpty()&& usr!=null && !usr.getUsername().equalsIgnoreCase(form.getVu360User().getUsername()) && activeDirectoryService.findADUser(form.getVu360User().getUsername())){
				errors.rejectValue("vu360User.username", "error.addNewUser.AD.existUsername","");
			}
			if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "password"))){
				if(!FieldsValidation.isPasswordCorrect(ServletRequestUtils.getStringParameter(request, "password"))){
					errors.rejectValue("vu360User.password", "error.editLearnerPassword.invalidlength","");
				}else if(!StringUtils.equals(ServletRequestUtils.getStringParameter(request, "password"), ServletRequestUtils.getStringParameter(request, "confirmpassword"))){
					errors.rejectValue("vu360User.password", "error.password.matchPassword","");
				}
							}

			String dateString = request.getParameter("expirationDate").trim();
			if (dateString==null)dateString="";
			if (!StringUtils.isBlank(dateString)){

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date expirationDate = null;
				Date date =null;

				try {
					String TodaysDate=formatter.format(new Date());
					date=formatter.parse(TodaysDate);
					expirationDate = formatter.parse(dateString);
					if (!formatter.format(expirationDate).equals(dateString)) {
						errors.rejectValue("vu360User.expirationDate", "error.addNewLearner.expDate.invalidDate","");
					}else {
						if( expirationDate.before(date)  ) {
							errors.rejectValue("vu360User.expirationDate", "error.addNewLearner.expDate.invalidDate","");
						}
					}

				} catch (ParseException e) {
					errors.rejectValue("vu360User.expirationDate", "error.addNewLearner.expDate.invalidDate","");
				}

			}
		
			
			//Validate Custom Fields
			
			if(form.getCustomFields().size()>0){
				this.validateMainCustomFields(form.getCustomFields(), errors);
			}
			//End Custom Fields Validation
			
			form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().setZipcode(form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getZipcode().trim());
			form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getZipcode().trim());
			Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());

			if(brander != null)	{
				String country = null ;
				String zipCode = null ;
				// -----------------------------------------------------------------------------
				// 			for learner address 1 Zip Code   									//
				// -----------------------------------------------------------------------------
				
				country = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getCountry();
				zipCode = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getZipcode();

	            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brander, log) ) {
	            	log.debug("ZIP CODE FAILED" );
                	errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress.zipcode", ZipCodeValidator.getCountryZipCodeError(form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getCountry(),brander),"");
	            }				
							
				// -----------------------------------------------------------------------------
				// 			for learner address 2 Zip Code   									//
				// -----------------------------------------------------------------------------
				country = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getCountry();
				zipCode = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getZipcode();

	            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brander, log) ) {
	            	log.debug("ZIP CODE FAILED" );
                	errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress2.zipcode", ZipCodeValidator.getCountryZipCodeError(form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getCountry(),brander),"");
	            }	
			}
		}	
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:mgr_manageLearners.do");
	}

	/**
	 * this method show User's profile data
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			
	public ModelAndView displayUser(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(editUserTemplate);

	}

	private CreditReportingFieldValue getCreditReportingFieldValueByCreditReportingField(com.softech.vu360.lms.model.CreditReportingField creditReportingField,
			List<CreditReportingFieldValue> creditReportingFieldValues){
		if (creditReportingFieldValues != null){
			for (CreditReportingFieldValue creditReportingFieldValue : creditReportingFieldValues){
				if (creditReportingFieldValue.getReportingCustomField()!=null){
					if (creditReportingFieldValue.getReportingCustomField().getId().compareTo(creditReportingField.getId())==0){
						return creditReportingFieldValue;
					}
				}
			}
		}
		return new CreditReportingFieldValue();
	}

	/**
	 * this method updates User's data
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			
	public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		UserForm form = (UserForm)command;
		VU360User frmUser = form.getVu360User();

		if(errors.hasErrors()|| form.getEventSource().equalsIgnoreCase("donotValidate")){

			CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> customFieldList=form.getCreditReportingFields();

			Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();

			for(com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : customFieldList){

				List<CreditReportingFieldValue> customFieldValueList = new ArrayList<CreditReportingFieldValue>();

				if (field.getCreditReportingFieldRef() instanceof SingleSelectCreditReportingField || 
						field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField) {

					customFieldValueList.add(field.getCreditReportingFieldValueRef());
					List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = learnerService.getChoicesByCreditReportingField(field.getCreditReportingFieldRef());
					fieldBuilder.buildCreditReportingField(field.getCreditReportingFieldRef(), field.getStatus(), customFieldValueList,creditReportingFieldValueOptionList);

					if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){
						CreditReportingFieldValue creditReportingFieldValue=field.getCreditReportingFieldValueRef();
						existingCreditReportingFieldValueChoiceMap.put(field.getCreditReportingFieldRef().getId(), creditReportingFieldValue.getCreditReportingValueChoices());
					}

				} else {
					customFieldValueList.add(field.getCreditReportingFieldValueRef());
					fieldBuilder.buildCreditReportingField(field.getCreditReportingFieldRef(), field.getStatus()	, customFieldValueList);
				}
			}

			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = fieldBuilder.getCreditReportingFieldList();

			for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : creditReportingFields){
				if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){
					List<CreditReportingFieldValueChoice> existingChoices = existingCreditReportingFieldValueChoiceMap.get(field.getCreditReportingFieldRef().getId());
					Map<Long,CreditReportingFieldValueChoice> existingChoicesMap = new HashMap<Long,CreditReportingFieldValueChoice>();

					for (CreditReportingFieldValueChoice choice : existingChoices){
						existingChoicesMap.put(choice.getId(), choice);
					}

					for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice tempChoice : field.getCreditReportingFieldValueChoices()){
						if(existingChoicesMap.containsKey(tempChoice.getCreditReportingFieldValueChoiceRef().getId())){
							tempChoice.setSelected(true);
						}
					}
				}
			}
			form.setCreditReportingFields(creditReportingFields);

			return new ModelAndView(editUserTemplate);
		}
		
		/**
		 * In learnerProfile.vm, there is a condition that 
		 * 
		 * 		#if($userPermissionChecker.hasAccessToFeature("LMS-LRN-0013", $userData, $ssn))    
            	#parse("learner/learnerProfileLicense.vm")
				#end
		 * 
		 * means if condition is true then include "Notify me on License Expire check box" which is in 
		 * learnerProfileLicense.vm. Here we have no way to determine whether check box is present on the page or not
		 * because if check box is not present then we get null or if checkbox is present and unselected then we also get
		 * null. So here we are putting this condition which means check box is not present on page.
		 * 
		 */
		// set LmsRoles into Form bean instance vu360User
		if(frmUser.getId()!=null){
			VU360User dbUsesr= vu360UserService.getUserById(frmUser.getId());
			frmUser.setTrainingAdministrator(dbUsesr.getTrainingAdministrator());
			frmUser.setLmsAdministrator(dbUsesr.getLmsAdministrator());
			frmUser.setLmsRoles(dbUsesr.getLmsRoles());
			frmUser.setInstructor(dbUsesr.getInstructor());
			frmUser.setProctor(dbUsesr.getProctor());
			frmUser.setRegulatoryAnalyst(dbUsesr.getRegulatoryAnalyst());
		}
		
		com.softech.vu360.lms.vo.VU360User voUser = ProxyVOHelper.setUserProxy(frmUser);
		if ( UserPermissionChecker.hasAccessToFeature("LMS-LRN-0013", voUser, request.getSession())) {
			
			boolean notifyOnLicenseExpire;
			boolean licenseExpireChkBoxPreviousState;
			boolean licenseExpireChkBoxCurrentState;
			
			
			/**
			 * <input type="checkbox" name="notifyOnLicenseExpire" value=$status.value #if($status.value) checked #end >
			 * 
			 * if checkbox with name notifyOnLicenseExpire is checked then you get this condition true otherwise not.
			 * if no value attribute is present and checkbox is checked then you get "on" as String value
			 * 
			 * <input type="checkbox" name="notifyOnLicenseExpire" #if($status.value) checked #end >
			 */
			notifyOnLicenseExpire = (request.getParameter("notifyOnLicenseExpire") == null) ? false : true ;
		
			licenseExpireChkBoxPreviousState = frmUser.getNotifyOnLicenseExpire();
			licenseExpireChkBoxCurrentState = notifyOnLicenseExpire;
			
			if (licenseExpireChkBoxPreviousState != licenseExpireChkBoxCurrentState) {
				
				form.setNotifyOnLicenseExpire(licenseExpireChkBoxCurrentState);
				frmUser.setNotifyOnLicenseExpire(licenseExpireChkBoxCurrentState);
				
				Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				
				licenseExpireChkBoxPreviousState = licenseExpireChkBoxCurrentState;
				String url = null;
				String mySqlUrl = brander.getBrandElement("autoalerts.user.subscribe.mysql.url");
				String userGuid = frmUser.getUserGUID();
				if (notifyOnLicenseExpire) {
					String queryString = "?command=MySQL&opt=subscribe&usr=" + userGuid;
					url = mySqlUrl + queryString;
				} else {
					String queryString = "?command=MySQL&opt=unsubscribe&usr=" + userGuid;
					url = mySqlUrl + queryString;
				}
				
				UpdateMySqlUser mySqlUser = new UpdateMySqlUser(url);
				Thread UpdateMySqlUserThread = new Thread(mySqlUser);
				UpdateMySqlUserThread.start();
				
			}
		}
		
		if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "password"))){
			frmUser.setPassWordChanged(Boolean.TRUE);
			frmUser.setPassword(ServletRequestUtils.getStringParameter(request, "password"));
		}
		frmUser.setAccountNonExpired(new Boolean(request.getParameter("accountNonExpired")));
		//vu360User.setAcceptedEULA(new Boolean(request.getParameter("accountNonExpired")));
		frmUser.setAccountNonLocked(new Boolean(request.getParameter("accountNonLocked")));
		frmUser.setChangePasswordOnLogin(new Boolean(request.getParameter("exsisting")));
		frmUser.setCredentialsNonExpired(new Boolean(request.getParameter("accountNonExpired")));
		frmUser.setEnabled(new Boolean(request.getParameter("enabled")));
		frmUser.setVissibleOnReport(new Boolean(request.getParameter("vissibleOnReport")));

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateString = request.getParameter("expirationDate").trim();
		if (!dateString.isEmpty()){
			Date myDate = formatter.parse(dateString);
			frmUser.setExpirationDate(myDate);
		}else{
			frmUser.setExpirationDate(null);
		}

		//Update it on StoreFront
		try {
			VU360User savedUser = learnerService.saveUser(frmUser);
			LearnerProfile dbProfile=savedUser.getLearner().getLearnerProfile();
			//Send new password mail 
			String newPassword=ServletRequestUtils.getStringParameter(request, "password");
			if(StringUtils.isNotBlank(newPassword)){
				try {
					Map model = new HashMap();
					VU360User user=frmUser;
					user.setPassword(newPassword);
					com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					model.put("loggedInUser", loggedInUser);
					model.put("customerName", frmUser.getLearner().getCustomer().getName());
					model.put("user", user);
					
					StringBuilder loginURL=new StringBuilder();
					loginURL.append(request.getScheme());
					loginURL.append("://");
					loginURL.append(request.getServerName());
					if(request.getServerPort()!=80){
						loginURL.append(":");
						loginURL.append(request.getServerPort());
					}
					loginURL.append(request.getContextPath());
					model.put("url",loginURL.toString());
					
					Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
					String batchImportTemplatePath =  brander.getBrandElement("lms.email.resetPassWord.body");
					String fromAddress =  brander.getBrandElement("lms.email.resetPassWord.fromAddress");
					String fromCommonName =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
					String subject =  brander.getBrandElement("lms.emil.resetPassWord.subject");//+ customer.getName();
					String support =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
					String phone =  brander.getBrandElement("lms.footerLinks.contactus.contactPhone");
					String lmsDomain=VU360Properties.getVU360Property("lms.domain");
					model.put("lmsDomain",lmsDomain);
					model.put("support", support);
					model.put("phone", phone);
					model.put("brander", brander);
					model.put("learnerPassword", user.getPassword());
					
					/*START- BRANDNG EMAIL WORK*/
					String templateText =  brander.getBrandElement("lms.branding.email.passwordUpdated.templateText");						
					String loginurl= lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());				
					templateText= templateText.replaceAll("&lt;firstname&gt;", frmUser.getFirstName());
					templateText= templateText.replaceAll("&lt;lastname&gt;", frmUser.getLastName());						 						
					templateText= templateText.replaceAll("&lt;loginurl&gt;", loginurl);
					templateText= templateText.replaceAll("&lt;phone&gt;", phone);
					templateText= templateText.replaceAll("&lt;support&gt;", support);	
					templateText= templateText.replaceAll("&lt;customername&gt;", frmUser.getLearner().getCustomer().getName());
					model.put("templateText", templateText);			
					/*END-BRANDING EMAIL WORK*/
					
					String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);
					SendMailService.sendSMTPMessage(user.getEmailAddress(),fromAddress,fromCommonName,subject,text);					 
		
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
	
			Customer cust=frmUser.getLearner().getCustomer();
			if(cust!=null && cust.getCustomerType().equals(Customer.B2C)){
				transformAndUpdateProfile(cust, frmUser, request.getParameter("password"));
			}

			if(form.getCreditReportingFields().size()>0){
				for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : form.getCreditReportingFields()){
					if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){
						List<CreditReportingFieldValueChoice> selectedChoiceList = new ArrayList<CreditReportingFieldValueChoice>();
						if (((MultiSelectCreditReportingField) field.getCreditReportingFieldRef()).isCheckBox()){
							for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice  customFieldValueChoice : field.getCreditReportingFieldValueChoices()){
								if(customFieldValueChoice.isSelected()){
									selectedChoiceList.add(customFieldValueChoice.getCreditReportingFieldValueChoiceRef());
								}
							}
						}else{
							if(field.getSelectedChoices()!=null){
								Map<Long,CreditReportingFieldValueChoice> totalChoiceMap = new HashMap<Long,CreditReportingFieldValueChoice>();
								for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice  customFieldValueChoice : field.getCreditReportingFieldValueChoices()){
									totalChoiceMap.put(customFieldValueChoice.getCreditReportingFieldValueChoiceRef().getId(), customFieldValueChoice.getCreditReportingFieldValueChoiceRef());
								}
								for(String selectedChoiceIdString : field.getSelectedChoices()){
									if(totalChoiceMap.containsKey(new Long(selectedChoiceIdString))){
										selectedChoiceList.add(totalChoiceMap.get(new Long(selectedChoiceIdString)));
									}
								}
							}
						}
						CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
						creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
						creditReportingFieldValue.setCreditReportingValueChoices(selectedChoiceList);
						creditReportingFieldValue.setLearnerprofile(dbProfile);

						learnerService.saveCreditReportingfieldValue(creditReportingFieldValue);
					}else{
						CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
						creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
						creditReportingFieldValue.setLearnerprofile(dbProfile);
						learnerService.saveCreditReportingfieldValue(creditReportingFieldValue);
					}
				}
			}
			//Save Custom Fields
			List<CustomFieldValue> myCustomFieldValues=new ArrayList<CustomFieldValue>();
			if(form.getCustomFields().size()>0){
				for (com.softech.vu360.lms.web.controller.model.customfield.CustomField field : form.getCustomFields()){
					if(field.getCustomFieldRef() instanceof MultiSelectCustomField){
						List<CustomFieldValueChoice> selectedChoiceList = new ArrayList<CustomFieldValueChoice>();
						if (((MultiSelectCustomField) field.getCustomFieldRef()).getCheckBox()){
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : field.getCustomFieldValueChoices()){
								if(customFieldValueChoice.isSelected()){
									selectedChoiceList.add(customFieldValueChoice.getCustomFieldValueChoiceRef());
								}
							}
						}else{
							if(field.getSelectedChoices()!=null){
								Map<Long,CustomFieldValueChoice> totalChoiceMap = new HashMap<Long,CustomFieldValueChoice>();
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : field.getCustomFieldValueChoices()){
									totalChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
								}
								for(String selectedChoiceIdString : field.getSelectedChoices()){
									if(totalChoiceMap.containsKey(new Long(selectedChoiceIdString))){
										selectedChoiceList.add(totalChoiceMap.get(new Long(selectedChoiceIdString)));
									}
								}
							}
						}
						CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
						customFieldValue.setCustomField(field.getCustomFieldRef());
						customFieldValue.setValueItems(selectedChoiceList);
						//customFieldValue.setLearnerprofile(usr.getLearner().getLearnerProfile());
						//usr.getLearner().getLearnerProfile().setCustomFieldValues(customFieldValues);
						//this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
						myCustomFieldValues.add(customFieldValue);
					}else{
						CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
						customFieldValue.setCustomField(field.getCustomFieldRef());
						//customFieldValue.setLearnerprofile(usr.getLearner().getLearnerProfile());
						//this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
						myCustomFieldValues.add(customFieldValue);
					}
				}
			}
			//assign it to learner profile & save
			dbProfile.setCustomFieldValues(myCustomFieldValues);
			learnerService.updateLearnerProfile(dbProfile);
			Customer customer=((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			//TODO TEMP logic should be removed after R&D why Reseller Custom Fields being inserted on customer_customfield table
			customFieldService.deleteCustomerCustomFieldsRelation(customer, this.getDistributorCustomFieldIds(customer));
			//End Save Custom Fields
		}catch(Exception ex){
			log.error("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
			ex.printStackTrace();
		}

		return new ModelAndView("redirect:mgr_editlearner.do?method=displayUser&userId="+frmUser.getId());
	}

	private Long[] getDistributorCustomFieldIds(Customer customer)	{
		List<CustomField> list=customer.getDistributor().getCustomFields();
		Long[] ids=new Long[list.size()];
		int counter=0;
		for(CustomField field:list)
		{
			ids[counter]=field.getId().longValue();
			counter++;
		}
		return ids;
	}

	private void transformAndUpdateProfile(Customer customer, VU360User user, String strPwd){
		CustomerData customerData=new CustomerData();
		customerData.setCustomerID(customer.getCustomerGUID());
		customerData.setBillingAddress(getBillingAddress(user));
		customerData.setShippingAddress(getShippingAddress(user));
		UpdateUserAuthenticationCredential auth=new UpdateUserAuthenticationCredential();
		auth.setUserLogonID(user.getUsername());
		auth.setUserPassword(strPwd);

		new StorefrontClientWSImpl().updateProfileOnStorefront(customerData, auth);
	}

	/**
	 * Setting the Address info
	 * @param user
	 * @param address
	 * @return
	 */
	public com.softech.vu360.lms.webservice.message.storefront.client.Address getBillingAddress( VU360User user) {
		com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
		Address learnerAddress = user.getLearner().getLearnerProfile().getLearnerAddress();

		msgAddress.setStreetAddress1(learnerAddress.getStreetAddress() );
		msgAddress.setStreetAddress2( learnerAddress.getStreetAddress2() );
		msgAddress.setCity( learnerAddress.getCity() );
		msgAddress.setCountry( learnerAddress.getCountry());
		msgAddress.setState( learnerAddress.getState() );
		msgAddress.setZipCode(learnerAddress.getZipcode() );
		msgAddress.setFirstName( user.getFirstName() );
		msgAddress.setLastName( user.getLastName() );
		msgAddress.setPhone( user.getLearner().getLearnerProfile().getOfficePhone() );

		return msgAddress;
	}  
	
	/**
	 * this method show learner's profile data
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			
	public ModelAndView displayLearnerProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserForm form = (UserForm)command;
		Long userId= ServletRequestUtils.getLongParameter(request, "userId");
		//LearnerProfileForm form = (LearnerProfileForm) command;
		HttpSession session = request.getSession();
		Map<Object,Object> context = new HashMap<Object,Object>();
		
		VU360User vu360SelectedUser = this.getVu360UserService().loadForUpdateVU360User(userId);
		
		List<LicenseOfLearner> list = learnerLicenseServices.getAllLicensesOfLearner(vu360SelectedUser.getLearner().getId());

		//============================For Sorting============================
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}

		Map<String,Object> pagerAttributeMap = new HashMap<String,Object>();
		pagerAttributeMap.put("pageIndex",pageIndex);

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			}
			if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseExpiration");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseExpiration");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			} 

		}	
		request.setAttribute("myPageSize", 5);
		form.setLearnerOfLicense(list);

		return new ModelAndView(editUserTemplate, "context", context);
	}

	
	public class ReportingFieldComparable implements Comparator<CreditReportingFieldValueChoice>{
		 
	    @Override
	    public int compare(CreditReportingFieldValueChoice o1, CreditReportingFieldValueChoice o2) {
	    	 String s1 = o1.getValue().trim().toUpperCase();
	    	    String s2 = o2.getValue().trim().toUpperCase();
	    	    return s1.compareTo(s2);
	    }
	}
	
	/**
	 * Setting the Address info
	 * @param user
	 * @param address
	 * @return
	 */
	public com.softech.vu360.lms.webservice.message.storefront.client.Address getShippingAddress( VU360User user ) {
		com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
		Address learnerAddress = user.getLearner().getLearnerProfile().getLearnerAddress2();

		msgAddress.setStreetAddress1( learnerAddress.getStreetAddress() );
		msgAddress.setStreetAddress2(learnerAddress.getStreetAddress2() );
		msgAddress.setCity( learnerAddress.getCity() );
		msgAddress.setCountry( learnerAddress.getCountry());
		msgAddress.setState( learnerAddress.getState() );
		msgAddress.setZipCode( learnerAddress.getZipcode() );
		msgAddress.setFirstName( user.getFirstName() );
		msgAddress.setLastName( user.getLastName() );
		msgAddress.setPhone( user.getLearner().getLearnerProfile().getOfficePhone() );
		return msgAddress;
	}

	public ModelAndView cancelEditUser(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView("redirect:mgr_manageLearners.do");
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the editUserTemplate
	 */
	public String getEditUserTemplate() {
		return editUserTemplate;
	}

	/**
	 * @param editUserTemplate the editUserTemplate to set
	 */
	public void setEditUserTemplate(String editUserTemplate) {
		this.editUserTemplate = editUserTemplate;
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

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	} 
	
	private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField,List<CustomFieldValue> customFieldValues){
		if (customFieldValues != null){
			for (CustomFieldValue customFieldValue : customFieldValues){
				if (customFieldValue.getCustomField()!=null){
					if (customFieldValue.getCustomField().getId().compareTo(customField.getId())==0){
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}
	
	
	public void validateMainCustomFields( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields, Errors errors ) {
		int fieldindex = 0;
		if ( customFields.size() > 0 ) {

			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField : customFields){

				CustomField customField = tempCustomField.getCustomFieldRef();
				CustomFieldValue customFieldValue = tempCustomField.getCustomFieldValueRef();

				if (customField.getFieldRequired()){
					if(customField instanceof MultiSelectCustomField){

						if (((MultiSelectCustomField) customField).getCheckBox()){
							int count=0;
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : tempCustomField.getCustomFieldValueChoices()){
								if (customFieldValueChoice.isSelected()){count=count+1;}
							}
							if(count==0){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						} else {
							if(tempCustomField.getSelectedChoices() == null){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else if (tempCustomField.getSelectedChoices().length==0){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					} else {
						/*Object[] errorArgs = new Object[1];
						errorArgs[0] = customField.getFieldLabel();
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customFields["+fieldindex+"].customFieldValueRef.value" , "custom.field.required", errorArgs,customField.getFieldLabel()+" is required");*/
						if (customFieldValue.getValue()==null){
							errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else if(StringUtils.isBlank(customFieldValue.getValue().toString())){
							errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else{
							tempCustomField.setStatus(1);
						}
					}
				}
				if(customField instanceof NumericCusomField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isNumeric(customFieldValue.getValue().toString())){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid number.");
								tempCustomField.setStatus(2);
							}else {
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if(customField instanceof DateTimeCustomField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isValidDate(customFieldValue.getValue().toString(),true,"MM/dd/yyyy")){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invaid date.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if (customField instanceof SSNCustomFiled){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isSSNValid(customFieldValue.getValue().toString())){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invaid SSN Number.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}
				fieldindex = fieldindex+1;
			}
		}
	}

	public void setLearnerLicenseServices(
			LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}
}
