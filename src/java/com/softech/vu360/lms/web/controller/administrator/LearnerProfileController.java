package com.softech.vu360.lms.web.controller.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

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
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrderService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.LearnerProfileForm;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.model.instructor.CourseCourseStatisticsPair;
import com.softech.vu360.lms.web.controller.validator.LearnerProfileValidator;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
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
	private String learnerTranscriptTemplate=null;
	private LearnerService learnerService = null;
	private CustomFieldService customFieldService = null;
	private VU360UserService vu360UserService=null;
	private OrderService orderService=null;
	private EntitlementService entitlementService=null;
	
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		
		HttpSession session = request.getSession();
		String learnerId=request.getParameter("id");
		if( command instanceof LearnerProfileForm ){
			LearnerProfileForm form = (LearnerProfileForm)command;
			if( methodName.equals("displayLearnerProfile") || 
					methodName.equals("displayProfileAfterSave") ) {
				
				//VU360User vu360User = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						  //vu360User = vu360UserService.getUpdatedUserById(vu360User.getId());
				//VU360User vu360User = vu360UserService.loadForUpdateVU360User(Long.parseLong(learnerId));
				
				Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth
				.getDetails();

                VU360User vu360User;

                vu360User = getLearnerVu360User(details, learnerId);

				if( session.getAttribute("customerOrderCount") == null ){
					List<CustomerOrder> customerOrderList = orderService.getOrderByCustomerId(vu360User.getLearner().getCustomer().getId());
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
				form.setVu360User(vu360User);
				
				form.setTimeZoneList((learnerService.getTimeZoneList()) );
				if(vu360User.getLearner().getLearnerProfile().getTimeZone() != null){
					form.setTimeZoneId(vu360User.getLearner().getLearnerProfile().getTimeZone().getId());
				}else
					form.setTimeZoneId(0);
				
				CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
				List<CreditReportingField> customFieldList=this.getLearnerService().getCreditReportingFieldsByLearnerCourseApproval(vu360User.getLearner());
				List<CreditReportingFieldValue> customFieldValueList = this.getLearnerService().getCreditReportingFieldValues(vu360User.getLearner());
				
				Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();
				
				for(CreditReportingField creditReportingField : customFieldList){
					if(!(creditReportingField instanceof StaticCreditReportingField)){
					 if (creditReportingField instanceof SingleSelectCreditReportingField || 
						creditReportingField instanceof MultiSelectCreditReportingField) {
						
						List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = this.getLearnerService().getChoicesByCreditReportingField(creditReportingField);
						fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList,creditReportingFieldValueOptionList);
						
						if(creditReportingField instanceof MultiSelectCreditReportingField){
							CreditReportingFieldValue creditReportingFieldValue=this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, customFieldValueList);
							existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(), creditReportingFieldValue.getCreditReportingValueChoices());
						}
						
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
				/* ================================================================================ */
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
					//customFieldValues = ;
					Customer customer = vu360User.getLearner().getCustomer();
					Distributor reseller = vu360User.getLearner().getCustomer().getDistributor();
					CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
					customFieldValues = vu360User.getLearner().getLearnerProfile().getCustomFieldValues() ;
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
			} 
			if( methodName.equals("displayLearnerProfile") ) {
				form.setSaved("false");
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
			
			
			form.getVu360User().setAccountNonExpired(new Boolean(request.getParameter("accountNonExpired")));
			//vu360User.setAcceptedEULA(new Boolean(request.getParameter("accountNonExpired")));
			form.getVu360User().setAccountNonLocked(new Boolean(request.getParameter("accountNonLocked")));
			form.getVu360User().setChangePasswordOnLogin(new Boolean(request.getParameter("exsisting")));
			form.getVu360User().setCredentialsNonExpired(new Boolean(request.getParameter("accountNonExpired")));
			form.getVu360User().setEnabled(new Boolean(request.getParameter("enabled")));
			form.getVu360User().setVissibleOnReport(new Boolean(request.getParameter("vissibleOnReport")));

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateString = request.getParameter("expirationDate").trim();
			if (!dateString.isEmpty()){
				Date myDate = formatter.parse(dateString);
				form.getVu360User().setExpirationDate(myDate);
			}else{
				form.getVu360User().setExpirationDate(null);
			}
		}
		
		if( form.getEventSource().equalsIgnoreCase("donotValidate"))
			return ;		
		
		String country = null ;
		String zipCode = null ;
		Brander brander = VU360Branding.getInstance()
				.getBrander(request.getSession().getAttribute(VU360Branding.BRAND).toString(), new Language());
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
            
            if(country!=null)
            {
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

        String url = "redirect:adm_learnerProfile.do?method=displayLearnerProfile";

        return getViewTemplate(request, url);

	}
	public ModelAndView displayLearnerTranscript(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		//LearnerProfileForm form = (LearnerProfileForm)command;
		
		Authentication auth = SecurityContextHolder.getContext()
		.getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth
		.getDetails();

        VU360User vu360User = null;

        String learnerId=request.getParameter("id");

        vu360User = getLearnerVu360User(details, learnerId);

		//VU360User vu360User = details.getCurrentLearner().getVu360User();
		List<LearnerEnrollment> enrolls = entitlementService.getLearnerEnrollmentsForLearner(vu360User.getLearner());
		List<CourseCourseStatisticsPair> results = new ArrayList<CourseCourseStatisticsPair>();

		for( LearnerEnrollment le : enrolls ) {
			CourseCourseStatisticsPair pair = new CourseCourseStatisticsPair();
			LearnerCourseStatistics stat = le.getCourseStatistics();
			if( stat != null ) {
				pair.setCourseName(le.getCourse().getCourseTitle());
				pair.setCourseComplete(stat.getCompleted());
				pair.setComplitionDate(stat.getCompletionDate());
				pair.setPercentComplete(stat.getPercentComplete());
				pair.setPreTestScore(stat.getPretestScore());
				//pair.setPreTestPassed(stat.getPreTestPassed());
				pair.setHigestPostScore(stat.getHighestPostTestScore());
				//pair.setPostTestPassed(stat.getPostTestPassed());
				pair.setQuizes(stat.getNumberQuizesTaken());
				pair.setCourseStatus(stat.getStatus());
				pair.setFirstAccessDate(stat.getFirstAccessDate());
				pair.setLastAccessDate(stat.getLastAccessDate());
				pair.setAverageQuizScore(stat.getAverageQuizScore());
				pair.setNumberPostTestsTaken(stat.getNumberPostTestsTaken());
				pair.setTotalTimeInSeconds(stat.getTotalTimeInSeconds());
				pair.setNumberQuizesTaken(stat.getNumberQuizesTaken());
				
				results.add(pair);
			}
		}
		
		
		Map context = new HashMap();
		context.put("results", results);
		
		return getViewTemplate(request, learnerTranscriptTemplate, context);
		//return new ModelAndView("redirect:adm_learnerTranscript.do?method=displayLearnerTranscript");
	}

	/**
	 * this method show learner's profile data
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			
	public ModelAndView displayLearnerProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return getViewTemplate(request, learnerProfileTemplate);
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
		VU360User vu360User = form.getVu360User();
		TimeZone tz = learnerService.getTimeZoneById(Long.valueOf(form.getTimeZoneId()));
		vu360User.getLearner().getLearnerProfile().setTimeZone(tz);
		
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
		String learnerPassWord=null;
		if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "password"))){
			vu360User.setPassWordChanged(true);
			vu360User.setPassword(ServletRequestUtils.getStringParameter(request, "password"));
			learnerPassWord=vu360User.getPassword();
		}
		
		
		
		vu360User.setAccountNonExpired(new Boolean(request.getParameter("accountNonExpired")));
		//vu360User.setAcceptedEULA(new Boolean(request.getParameter("accountNonExpired")));
		vu360User.setAccountNonLocked(new Boolean(request.getParameter("accountNonLocked")));
		vu360User.setChangePasswordOnLogin(new Boolean(request.getParameter("exsisting")));
		vu360User.setCredentialsNonExpired(new Boolean(request.getParameter("accountNonExpired")));
		vu360User.setEnabled(new Boolean(request.getParameter("enabled")));
		vu360User.setVissibleOnReport(new Boolean(request.getParameter("vissibleOnReport")));

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateString = request.getParameter("expirationDate").trim();
		if (!dateString.isEmpty()){
			Date myDate = formatter.parse(dateString);
			vu360User.setExpirationDate(myDate);
		}else{
			vu360User.setExpirationDate(null);
		}
		
		VU360User usr = this.getLearnerService().saveUser(vu360User);
		
		
		//LMS-2114 Mail Send to User
		if(learnerPassWord != null){
			
			try {
				Map<String, Object> model = new HashMap<>();
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
	
				model.put("loggedInUser", loggedInUser);
				model.put("customerName", vu360User.getLearner().getCustomer().getName());
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
	
	
				//	model.put("url", request.getScheme()+"://"+request.getServerName()
				//			+":"+request.getServerPort()+request.getContextPath()+"/login.do");
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
				/*START- BRANDNG EMAIL WORK*/
				String templateText =  brander.getBrandElement("lms.branding.email.passwordUpdated.templateText");						
				String loginurl= lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());				
				templateText= templateText.replaceAll("&lt;firstname&gt;", vu360User.getFirstName());
				templateText= templateText.replaceAll("&lt;lastname&gt;", vu360User.getLastName());						 						
				templateText= templateText.replaceAll("&lt;loginurl&gt;", loginurl);
				templateText= templateText.replaceAll("&lt;phone&gt;", phone);
				templateText= templateText.replaceAll("&lt;support&gt;", support);
				templateText= templateText.replaceAll("&lt;customername&gt;", vu360User.getLearner().getCustomer().getName());
				model.put("templateText", templateText);			
				/*END-BRANDING EMAIL WORK*/
				
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
			if(customer!=null && customer.getCustomerType().equals(Customer.B2C)){
				transformAndUpdateProfile(customer, vu360User, request.getParameter("password"));
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
		form.getCustomFieldValueList().addAll(myCustomFieldValues);
		usr.getLearner().getLearnerProfile().setCustomFieldValues(myCustomFieldValues);
		learnerService.updateLearnerProfile(usr.getLearner().getLearnerProfile());
		
		//End Save Custom Fields
		// ***************************************************************************************
		
		//return new ModelAndView(learnerProfileTemplate);
		return new ModelAndView("redirect:adm_learnerProfile.do?method=displayProfileAfterSave");
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
	
	/**
	 * @return the learnerProfileTemplate
	 */
	public String getLearnerProfileTemplate() {
		return learnerProfileTemplate;
	}

	/**
	 * @param learnerProfileTemplate the learnerProfileTemplate to set
	 */
	public void setLearnerProfileTemplate(String learnerProfileTemplate) {
		this.learnerProfileTemplate = learnerProfileTemplate;
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

	/**
	 * @return the customFieldService
	 */
	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	/**
	 * @param customFieldService the customFieldService to set
	 */
	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}

	/**
	 * @param orderService the orderService to set
	 */
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * @return the orderService
	 */
	public OrderService getOrderService() {
		return orderService;
	}

	public String getLearnerTranscriptTemplate() {
		return learnerTranscriptTemplate;
	}

	public void setLearnerTranscriptTemplate(String learnerTranscriptTemplate) {
		this.learnerTranscriptTemplate = learnerTranscriptTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

    private VU360User getLearnerVu360User(VU360UserAuthenticationDetails details, String learnerId) {
		// does this user has a learner mode or select any learner
		com.softech.vu360.lms.vo.Learner currentLearner = details.getProxyLearner();
		Learner learner = null;
		VU360User vu360User = null;

		// no learner yet selected
		if (currentLearner == null) {
			if (learnerId != null && learnerId.trim().length() > 0) {
				// load the learner from query string
				learner = learnerService.getLearnerByID(Long.parseLong(learnerId));
				vu360User = learner.getVu360User();
				if (details.getOriginalPrincipal().isAdminMode()) {
					details.setClusteredCurrentMode(VU360UserMode.ROLE_LMSADMINISTRATOR);
				}
			}
		} else {
			learner = learnerService.getLearnerByID(currentLearner.getId());
			vu360User = learner.getVu360User();
		}

		return vu360User;
    }

    private ModelAndView getViewTemplate(HttpServletRequest request, String url, Map context) {

        // if request instance is provided
        if(request != null && request.getQueryString().length() > 0) {

            // concatenate query string in the url
            if(url.contains(".do"))
                url += (url.contains("?") ? "&" : "?").concat(request.getQueryString());

            // if there is no context provided, create one
            if(context ==  null)
                context = new HashMap();

            // get all parameters from request instance
            Map params = request.getParameterMap();
            // prepare a parameter iterator
            Iterator iterator = params.keySet().iterator();

            // iterate through each parameter and push
            // it into the context (map)
            while(iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = ((String[]) params.get( key ))[ 0 ];
                context.put(key, value);
            }

        }

        // pass context to velocity template
        return new ModelAndView(url, "context", context);

    }

    private ModelAndView getViewTemplate(HttpServletRequest request, String url) {
        // passing empty context
        return getViewTemplate(request, url, null);
    }

}