package com.softech.vu360.lms.web.controller.learner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
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
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
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
import com.softech.vu360.lms.model.CustomerOrder;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.LearnerValidationAnswers;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrderService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.impl.lmsapi.UpdateMySqlUser;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.UniqueQuestionAnswerVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.LearnerProfileForm;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.LearnerProfileValidator;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.LearnerOfLicenseSort;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * The controller for the learner profile display and modify .
 *
 * @author Arijit
 * @date 20-07-09
 *
 */
public class LearnerProfileController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(LearnerProfileController.class.getName());
	private VelocityEngine velocityEngine;
	public LearnerProfileController() {
		super();
		
	}

	public LearnerProfileController(Object delegate) {
		super(delegate);
	}

	private String learnerProfileTemplate = null;
	private LearnerService learnerService = null;
	private CustomFieldService customFieldService = null;
	private VU360UserService vu360UserService=null;
	private OrderService orderService=null;
    private LearnerLicenseService learnerLicenseServices =null;

	@Override
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		
		HttpSession session = request.getSession();
		VU360User loggedInUser = null;	
		if( command instanceof LearnerProfileForm ){
			LearnerProfileForm form = (LearnerProfileForm)command;
			if( methodName.equals("displayLearnerProfile") || 
					methodName.equals("displayProfileAfterSave") ) {
				
				loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
				if( session.getAttribute("customerOrderCount") == null ){
					List<CustomerOrder> customerOrderList = orderService.getOrderByCustomerId(loggedInUser.getLearner().getCustomer().getId());
					int countOfCoustomerOrders = 0;
					if( customerOrderList != null )
						countOfCoustomerOrders = customerOrderList.size();
					session.setAttribute("customerOrderCount", countOfCoustomerOrders);				
					
				}
				
				/**
				 * Checks if the user information is updated then user info will not be loaded  from DAO
				 * if IsCountrySelected is empty or null means request is not from Country Combo
				 * if the user is set on country combo then user will again see its country which
				 * is saved in DB not the one which he is changing
				**/  
				if( StringUtils.isEmpty(form.getIsCountrySelected()))
					form.setVu360User(loggedInUser);
				
				form.setTimeZoneList((learnerService.getTimeZoneList()) );
				if(loggedInUser.getLearner().getLearnerProfile().getTimeZone() != null){
					form.setTimeZoneId(loggedInUser.getLearner().getLearnerProfile().getTimeZone().getId());
				}else
					form.setTimeZoneId(0);
				
				form.setHasAnyInProgressEnrollmentOfStandardValidationQuestions(learnerService.hasAnyInProgressEnrollmentOfStandardValidationQuestions(form.getVu360User().getLearner().getId()));
				if(form.isHasAnyInProgressEnrollmentOfStandardValidationQuestions()) {
				  form.setLearnerValidationQuestions(learnerService.getLearnerValidationQuestions(form.getVu360User().getLearner().getId()));
				}
				Map<Object,Object> mpValidationQuestion = learnerService.getLearnerUniqueQuestions(form.getVu360User().getLearner().getId());
				
				if(mpValidationQuestion!=null  && !mpValidationQuestion.isEmpty()){
					form.setMpValidationQuestion(mpValidationQuestion);
				}
				
				form.setLearnerValidationQuestions(learnerService.getLearnerValidationQuestions(form.getVu360User().getLearner().getId()));
				
				CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
				List<CreditReportingField> customFieldList=this.getLearnerService().getCreditReportingFieldsByLearnerCourseApproval(loggedInUser.getLearner());
				List<CreditReportingFieldValue> customFieldValueList = this.getLearnerService().getCreditReportingFieldValues(loggedInUser.getLearner());
				
				Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();
				
				for(CreditReportingField creditReportingField : customFieldList){
					if(!(creditReportingField instanceof StaticCreditReportingField)){
					 if (creditReportingField instanceof SingleSelectCreditReportingField || 
						creditReportingField instanceof MultiSelectCreditReportingField) {
						
						List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = this.getLearnerService().getChoicesByCreditReportingField(creditReportingField);
						
						if(creditReportingField instanceof MultiSelectCreditReportingField){
							CreditReportingFieldValue creditReportingFieldValue=this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, customFieldValueList);
							
							/*
							 * To Put the selected options on top of the list
							 * LMS-15519
							 */
							List<CreditReportingFieldValueChoice> tempCreditReportingFieldValueOptionList = new ArrayList<CreditReportingFieldValueChoice>();
							for(CreditReportingFieldValueChoice fieldChoice: creditReportingFieldValue.getCreditReportingValueChoices()){
								for (Iterator<CreditReportingFieldValueChoice> iterator = creditReportingFieldValueOptionList.iterator(); iterator.hasNext();) {
									CreditReportingFieldValueChoice fieldList = iterator.next();
									if(fieldChoice.getId() == fieldList.getId()){
										iterator.remove();
										tempCreditReportingFieldValueOptionList.add(0,fieldList);
									}
								}
							}
							for(int iCount = tempCreditReportingFieldValueOptionList.size(); iCount>0;iCount--)
							{
								int sortedItemsize = iCount - 1;
								CreditReportingFieldValueChoice tempChoiceToAdd = tempCreditReportingFieldValueOptionList.get(sortedItemsize); 
								creditReportingFieldValueOptionList.add(0, tempChoiceToAdd);
							}
							/*
							 * End
							 */
							existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(), creditReportingFieldValue.getCreditReportingValueChoices());
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
				customFieldService.createValueRecordForStaticReportingField(loggedInUser, customFieldList, customFieldValueList);
				/* ================================================================================ */
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
					Customer customer = loggedInUser.getLearner().getCustomer();
					Distributor reseller = loggedInUser.getLearner().getCustomer().getDistributor();
					CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
					customFieldValues = loggedInUser.getLearner().getLearnerProfile().getCustomFieldValues() ;
					totalCustomFieldList.addAll(customer.getCustomFields());
					totalCustomFieldList.addAll(reseller.getCustomFields());
					
					if( customFieldValues.size() == 0 && form.getCustomFieldValueList().size()  > 0 ) {
						customFieldValues.addAll(form.getCustomFieldValueList()) ;
					}
					
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
				/* ================================================================================ */
					
					List<LicenseOfLearner> list = learnerLicenseServices.getAllLicensesOfLearner(loggedInUser.getLearner().getId());
					form.setLearnerOfLicense(list);

			} 
			if( methodName.equals("displayLearnerProfile") ) {
				form.setSaved("false");
				if (loggedInUser != null) {
					if (loggedInUser.getNotifyOnLicenseExpire()) {
						form.setNotifyOnLicenseExpire(true);
					} else {
						form.setNotifyOnLicenseExpire(false);
					}
				}
			} else if( methodName.equals("displayProfileAfterSave") ) {
				form.setSaved("true");
			}
		}
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

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		LearnerProfileForm form = (LearnerProfileForm)command;
		LearnerProfileValidator validator = (LearnerProfileValidator)this.getValidator();
		if(methodName.equals("updateProfile")){

			validator.validate(form, errors);
			if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "password"))){
				if(!FieldsValidation.isPasswordCorrect(ServletRequestUtils.getStringParameter(request, "password"))){
					errors.rejectValue("vu360User.password", "error.editLearnerPassword.invalidlength","");
				}else if(!StringUtils.equals(ServletRequestUtils.getStringParameter(request, "password"), ServletRequestUtils.getStringParameter(request, "confirmpassword"))){
					errors.rejectValue("vu360User.password", "error.password.matchPassword","");
				}
			}
			if(form.getMpValidationQuestion()!=null && !form.getMpValidationQuestion().isEmpty()){
				Enumeration enumeration = request.getParameterNames();
			   	while (enumeration.hasMoreElements()) {
			   		String parameterName = (String) enumeration.nextElement();
			   		if(parameterName.contains("answerTxt_")){
			   			String strAnswer = request.getParameter(parameterName);
			   			if(StringUtils.isEmpty(strAnswer)){
			   				errors.rejectValue("vu360User.password", "error.courseConfiguration.uniqueQuestions.blank","");
			   			}
			   		}
			   	}
			}
		}
		
		if( form.getEventSource().equalsIgnoreCase("donotValidate"))
			return ;		
		
		String country = null ;
		String zipCode = null ;
		Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		if(brander != null)	{
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getCountry();
			zipCode = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getZipcode();
			
			if(country!=null)
			{
	            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brander, log) ) {
	            	log.debug("ZIP CODE FAILED" );
	            	errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress.zipcode", ZipCodeValidator.getCountryZipCodeError(form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getCountry(),brander),"");
	            }			
			}
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
            if (form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2() != null) {
				country = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getCountry();
				zipCode = form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getZipcode();
            }
            
            if(country!=null){
	            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, brander, log) ) {
	            	log.debug("ZIP CODE FAILED" );
	            	errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress2.zipcode", ZipCodeValidator.getCountryZipCodeError(form.getVu360User().getLearner().getLearnerProfile().getLearnerAddress2().getCountry(),brander),"");
	            }	
            }
		}
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayLearnerProfile( request,  response);
		//return new ModelAndView(learnerProfileTemplate);
	}

	public ModelAndView displayLearnerProfile(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		//LearnerProfileForm form = (LearnerProfileForm)command;
		return new ModelAndView("redirect:lrn_learnerProfile.do?method=displayLearnerProfile");
	}

	/**
	 * this method show learner's profile data
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			
	public ModelAndView displayLearnerProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		com.softech.vu360.lms.vo.VU360User  loggedInUser = VU360UserAuthenticationDetails.getProxyUser();
		LearnerProfileForm form = (LearnerProfileForm) command;
		HttpSession session = request.getSession();
		Map<Object,Object> context = new HashMap<Object,Object>();
		List<LicenseOfLearner> list = learnerLicenseServices.getAllLicensesOfLearner(loggedInUser.getLearner().getId());

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

		return new ModelAndView(learnerProfileTemplate, "context", context);
	}
	
	/**
	 * this method is called only if learner profile is saved, works same as displayLearnerProfile
	 * needed to set the 'save' field of bean class
	 */
	public ModelAndView displayProfileAfterSave(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(learnerProfileTemplate);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView  updateProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception{
		
		LearnerProfileForm form = (LearnerProfileForm)command;
		VU360User frmUser = form.getVu360User();
		
		if(errors.hasErrors()){
			CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> customFieldList=form.getCreditReportingFields();
			
			Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();
			
			for(com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : customFieldList){
				
				List<CreditReportingFieldValue> customFieldValueList = new ArrayList<CreditReportingFieldValue>();
				
				if (field.getCreditReportingFieldRef() instanceof SingleSelectCreditReportingField || 
						field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField) {
					
					customFieldValueList.add(field.getCreditReportingFieldValueRef());
					List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = this.getLearnerService().getChoicesByCreditReportingField(field.getCreditReportingFieldRef());
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
			
			return new ModelAndView(learnerProfileTemplate);
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
		TimeZone tz = learnerService.getTimeZoneById(form.getTimeZoneId());
		frmUser.getLearner().getLearnerProfile().setTimeZone(tz);
		
		if ( vu360UserService.hasAccessToFeatureCode(frmUser.getId(), "LMS-LRN-0013")) {
			
			boolean notifyOnLicenseExpire;
			boolean licenseExpireChkBoxCurrentState;
			boolean licenseExpireChkBoxPreviousState;
			
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
				String url;
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
		
		String learnerPassWord=null;
		if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "password"))){
			frmUser.setPassWordChanged(true);
			frmUser.setPassword(ServletRequestUtils.getStringParameter(request, "password"));
			learnerPassWord=frmUser.getPassword();
		}
		VU360User usr = this.getLearnerService().saveUser(frmUser);
		//LMS-2114 Mail Send to User
		if(learnerPassWord != null){
			
			try {
				Map model = new HashMap();
				 com.softech.vu360.lms.vo.VU360User loggedInUser = VU360UserAuthenticationDetails.getProxyUser();
	
				model.put("loggedInUser", loggedInUser);
				model.put("customerName", usr.getLearner().getCustomer().getName());
				model.put("user", usr);
				model.put("learnerPassword", learnerPassWord);
				
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
				/*START- BRANDNG EMAIL WORK*/
				String templateText =  brander.getBrandElement("lms.branding.email.passwordUpdated.templateText");						
				String loginurl= lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());				
				templateText= templateText.replaceAll("&lt;firstname&gt;", usr.getFirstName());
				templateText= templateText.replaceAll("&lt;lastname&gt;", usr.getLastName());						 						
				templateText= templateText.replaceAll("&lt;loginurl&gt;", loginurl);
				templateText= templateText.replaceAll("&lt;phone&gt;", phone);
				templateText= templateText.replaceAll("&lt;support&gt;", support);
				templateText= templateText.replaceAll("&lt;customername&gt;", usr.getLearner().getCustomer().getName());
				model.put("templateText", templateText);			
				/*END-BRANDING EMAIL WORK*/
				model.put("brander", brander);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, batchImportTemplatePath, model);
				SendMailService.sendSMTPMessage(usr.getEmailAddress(), 
						fromAddress, 
						fromCommonName, 
						subject, 
						text);					 
	
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		
		try {
			Customer customer=usr.getLearner().getCustomer();
			if(customer!=null && customer.getCustomerType().equals(Customer.B2C) && CustomerUtil.isUserProfileUpdateOnSF(customer)){
				transformAndUpdateProfile(customer, usr, request.getParameter("password"));
			}
		} catch( Exception ex ) {
			log.error("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
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
						
					} else {
						
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
					creditReportingFieldValue.setLearnerprofile(usr.getLearner().getLearnerProfile());
					this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
					
				} else {
					CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
					creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
					creditReportingFieldValue.setLearnerprofile(usr.getLearner().getLearnerProfile());
					this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
				}
			}
		}
		// ***************************************************************************************
		
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

							Map<Long,CustomFieldValueChoice> totalChoiceMap2 = new HashMap<Long,CustomFieldValueChoice>();

							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : field.getCustomFieldValueChoices()){
								totalChoiceMap2.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
							}

							for(String selectedChoiceIdString : field.getSelectedChoices()){
								if(totalChoiceMap2.containsKey(new Long(selectedChoiceIdString))){
									selectedChoiceList.add(totalChoiceMap2.get(new Long(selectedChoiceIdString)));
								}
							}

						}
					}

					CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
					customFieldValue.setCustomField(field.getCustomFieldRef());
					customFieldValue.setValueItems(selectedChoiceList);
					myCustomFieldValues.add(customFieldValue);

				}else{
					CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
					customFieldValue.setCustomField(field.getCustomFieldRef());
					myCustomFieldValues.add(customFieldValue);
				}
			}
		}
		
		//assign it to learner profile & save
		form.getCustomFieldValueList().addAll(myCustomFieldValues);
		usr.getLearner().getLearnerProfile().setCustomFieldValues(myCustomFieldValues);
		learnerService.updateLearnerProfile(usr.getLearner().getLearnerProfile());
		learnerService.saveLearnerValidationAnswers(form.getLearnerValidationQASet(), usr.getLearner());
		
		if(form.getMpValidationQuestion() != null && !form.getMpValidationQuestion().isEmpty()){
			for (Map.Entry<Object, Object> entry : form.getMpValidationQuestion().entrySet()) {
			    Object value = entry.getValue();
			    if(value instanceof List)
			    {
			    List<UniqueQuestionAnswerVO> lstUniqueQuestionAnswerVO = (List<UniqueQuestionAnswerVO>)value;
			    
			    if(lstUniqueQuestionAnswerVO!=null && !lstUniqueQuestionAnswerVO.isEmpty()){
			    	for(UniqueQuestionAnswerVO uQAVO :lstUniqueQuestionAnswerVO){
			    		LearnerValidationAnswers lrnValidationAnswers =learnerService.getLearnerUniqueQuestionsAnswersByQuestion(uQAVO.getQuestionId());
                        if(lrnValidationAnswers!=null){
                        	//LearnerValidationAnswers learnerValidationAnswers = new LearnerValidationAnswers();
                        	LearnerValidationAnswers learnerValidationAnswers = learnerService.loadForUpdateLearnerValidationAnswers(lrnValidationAnswers.getId());
			    			learnerValidationAnswers.setLearner(usr.getLearner());
			    			learnerValidationAnswers.setQuestionId(uQAVO.getQuestionId());
			    			if(uQAVO.getQuestionType().equals("True False")){
			    				String chkAnwser = request.getParameter("answerTF_"+ uQAVO.getQuestionId());
				    			learnerValidationAnswers.setAnswer(chkAnwser);
				    			learnerService.updateLearnerValidationAnswers(learnerValidationAnswers);
			    			}
			    			else if(uQAVO.getQuestionType().equals("Text Entry")){
			    				String txtAnwser = request.getParameter("answerTxt_"+ uQAVO.getQuestionId());
				    			learnerValidationAnswers.setAnswer(txtAnwser);
				    			learnerService.updateLearnerValidationAnswers(learnerValidationAnswers);
			    			}
			    		}else{
			    			LearnerValidationAnswers learnerValidationAnswers = new LearnerValidationAnswers();
			    			learnerValidationAnswers.setLearner(usr.getLearner());
			    			learnerValidationAnswers.setQuestionId(uQAVO.getQuestionId());
			    			if(uQAVO.getQuestionType().equals("True False")){
			    				String chkAnwser = request.getParameter("answerTF_"+ uQAVO.getQuestionId());
				    			learnerValidationAnswers.setAnswer(chkAnwser);
				    			learnerService.saveLearnerUniquesValidationQuestions(learnerValidationAnswers);
			    			}
			    			else if(uQAVO.getQuestionType().equals("Text Entry")){
			    				String txtAnwser = request.getParameter("answerTxt_"+ uQAVO.getQuestionId());
				    			learnerValidationAnswers.setAnswer(txtAnwser);
				    			learnerService.saveLearnerUniquesValidationQuestions(learnerValidationAnswers);
			    			}
			    			
			    		}
			    	}
			    }
			}
		  }
		}
		
		//End Save Custom Fields
		// ***************************************************************************************
		
		return new ModelAndView("redirect:lrn_learnerProfile.do?method=displayProfileAfterSave");
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
    public com.softech.vu360.lms.webservice.message.storefront.client.Address getBillingAddress( VU360User user ) {
    	
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
    	msgAddress.setPhone( user.getLearner().getLearnerProfile().getMobilePhone() );
    	return msgAddress;
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
    	msgAddress.setPhone( user.getLearner().getLearnerProfile().getMobilePhone());
    	return msgAddress;
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
	
	public class ReportingFieldComparable implements Comparator<CreditReportingFieldValueChoice>{
			 
	    @Override
	    public int compare(CreditReportingFieldValueChoice o1, CreditReportingFieldValueChoice o2) {
	    	 String s1 = o1.getValue().trim().toUpperCase();
	    	    String s2 = o2.getValue().trim().toUpperCase();
	    	    return s1.compareTo(s2);
	    }
	}
	
	public String getLearnerProfileTemplate() {
		return learnerProfileTemplate;
	}

	public void setLearnerProfileTemplate(String learnerProfileTemplate) {
		this.learnerProfileTemplate = learnerProfileTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

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

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public LearnerLicenseService getLearnerLicenseServices() {
		return learnerLicenseServices;
	}

	public void setLearnerLicenseServices(LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}
}