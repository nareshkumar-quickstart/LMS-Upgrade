package com.softech.vu360.lms.web.controller.learner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.UserForm;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.UserValidator;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.VU360Branding;

public class LearnerSelfRegistrationController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(LearnerSelfRegistrationController.class.getName());

	private String selfRegistrationTemplate = null;
	private LearnerService learnerService = null;
	private VU360UserService vu360UserService = null;
	private String registrationRedirectTemplate = null;
	private String learnerRegistrationTemplate = null;
	private String learnerThankyouTemplate = null;
	private String defaultTemplate = null;
	private CustomFieldService customFieldService = null;
	private EnrollmentService enrollmentService = null;
	private ActiveDirectoryService activeDirectoryService;
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	private VelocityEngine velocityEngine;
	public LearnerSelfRegistrationController() {
		super();
	}

	public LearnerSelfRegistrationController(Object delegate) {
		super(delegate);
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub

	}
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		String registrationId=request.getParameter("registrationId");
		if(StringUtils.isNotBlank(registrationId) )
		{
			RegistrationInvitation regInv = learnerService.getRegistrationInvitationByID(Long.valueOf(registrationId));
			if(regInv ==null){
				context.put("registrationId", registrationId);
				context.put("error","lms.learner.selfReg.error.invalidRegistration");
				return new ModelAndView(selfRegistrationTemplate,"context", context);
			}
			String passcode = "";
			passcode=request.getParameter("passcode");
			if(passcode==null ){
				//it means user clicked a link given in self registration email
				context.put("registrationId", registrationId);
				return new ModelAndView(selfRegistrationTemplate,"context", context);
			}else{
				// it means user submitted Self Registration form
				if(passcode.trim().isEmpty()){
					// it means Self Registration form is submitted with blank passcode
					context.put("registrationId", registrationId);
					context.put("error","lms.learner.selfReg.error.blankPasscode");
					return new ModelAndView(selfRegistrationTemplate,"context", context);
				}else{
					//it means user enter a passcode, need to validate
					if(!(regInv.getPasscode().equalsIgnoreCase(passcode))){
						//pass code doesn't match with the one generated/stored at the time of invitatin in db
						context.put("registrationId", registrationId);
						context.put("error","lms.learner.selfReg.error.invalidPasscode");
						return new ModelAndView(selfRegistrationTemplate,"context", context);
					}else if(!(regInv.hasUnutilizedInvitation())){
						// there is no untilized invitation left 
						context.put("registrationId", registrationId);
						context.put("error","lms.learner.selfReg.error.maxInvReached");
						return new ModelAndView(selfRegistrationTemplate,"context", context);
					}else if(regInv.getPasscode().equalsIgnoreCase(passcode)){
						// phew.. at last done with all validation
						context.put("regInv", regInv);
						///
					
						return new ModelAndView(registrationRedirectTemplate,"context", context);
					}
				}
			}
			
		}else{
		
			context.put("error","lms.learner.selfReg.error.invalidRegistration");
			return new ModelAndView(selfRegistrationTemplate,"context", context);
		}
		return new ModelAndView(selfRegistrationTemplate,"context", context);
	}
	
	public ModelAndView displayProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		UserForm form = (UserForm)command;
		
		String regInvitationId= request.getParameter("registrationId");
		Map<Object, Object> context = new HashMap<Object, Object>();
		if(regInvitationId==null){
			return new ModelAndView(selfRegistrationTemplate);
		}else {
			 context.put("regInvitationId", regInvitationId) ;
			RegistrationInvitation regInv = learnerService.getRegistrationInvitationByID(Long.valueOf(regInvitationId));
			VU360User vu360User=new VU360User();
			Learner learner=new Learner();
			Address address1=new Address();
			Address address2=new Address();
			LearnerProfile learnerProfile=new LearnerProfile();
			learnerProfile.setLearnerAddress(address1);
			learnerProfile.setLearnerAddress2(address2);
			learner.setLearnerProfile(learnerProfile);
			vu360User.setLearner(learner);
			learner.setVu360User(vu360User);
			form.setVu360User(vu360User);
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
			List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
			Customer customer = regInv.getCustomer();
			
			Distributor reseller = customer.getDistributor();
			CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
		//	customFieldValues = vu360User.getLearner().getLearnerProfile().getCustomFieldValues() ;
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
			
		}
		
		return new ModelAndView(learnerRegistrationTemplate,"context",context);
	}
	
	public ModelAndView saveUser(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		UserForm form = (UserForm)command;
		VU360User vu360User = form.getVu360User();
		String regInvitationId= request.getParameter("registrationId");
		Map<Object, Object> context = new HashMap<Object, Object>();
		 context.put("regInvitationId", regInvitationId) ;
			try{
				
		if(errors.hasErrors()|| form.getEventSource().equalsIgnoreCase("donotValidate")){

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

			return new ModelAndView(learnerRegistrationTemplate,"context",context);
		}
		RegistrationInvitation regInv = learnerService.getRegistrationInvitationByID(Long.valueOf(regInvitationId));
		
		Set<LMSRole> userRoles= new HashSet<LMSRole>();
		userRoles.add(vu360UserService.getDefaultRole(regInv.getCustomer()));
		if(userRoles.isEmpty()){
			List<LMSRole> systemOwnedRole = vu360UserService.getSystemRolesByCustomer(regInv.getCustomer());
			for(LMSRole myRole : systemOwnedRole){
				//Hope this will be not null
				if(myRole.getRoleType().equals(LMSRole.ROLE_LEARNER)){
					userRoles.add(myRole);
				}
			}
		}
		vu360User.setLmsRoles(userRoles);
		regInv.setRegistrationUtilized(regInv.getRegistrationUtilized()+1);
		vu360User.getLearner().setCustomer(regInv.getCustomer());
		vu360User.setPassword(ServletRequestUtils.getStringParameter(request, "password"));

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
						creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());

						this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);

					}else{
						CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
						creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
						creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());

						this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);

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
			vu360User.setUserGUID(GUIDGeneratorUtil.generateGUID());
			
			//assign it to learner profile & save
			vu360User.getLearner().getLearnerProfile().setLearner(vu360User.getLearner()); 
			vu360User.getLearner().getLearnerProfile().setCustomFieldValues(myCustomFieldValues);
			vu360User.getLearner().setVu360User(vu360User);
			learnerService.addLearner(null, regInv.getOrgGroups(), regInv.getLearnerGroups(), vu360User.getLearner());
			
			// [1/27/2011] LMS-7183 :: Retired Course Functionality II (to enroll learner in Learner Group from Self Registration Invitations
			if (regInv.getLearnerGroups() != null && regInv.getLearnerGroups().size() > 0) {
				for (LearnerGroup learnerGroup : regInv.getLearnerGroups()) {
					this.enrollmentService.enrollLearnerToLearnerGroupItems(vu360User, learnerGroup);				
				}
			}
			learnerService.addRegistrationInvitation(regInv);
			
			context.put("learner", vu360User.getLearner());					
			return new ModelAndView(learnerThankyouTemplate, "context", context);	

			
		}catch(Exception ex){
			log.debug("Exception Occured while transofrming & updating learner profile: " + ex.getMessage());
			errors.rejectValue("learner", "lms.learner.selfReg.error.incompleteRegistration");
		}

		return new ModelAndView(learnerRegistrationTemplate,"context",context);
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
public ModelAndView cancelEditUser(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws IOException
{
	String regInvitationId= request.getParameter("registrationId");
	
	response.sendRedirect("/lms/learnerRegistration.do?registrationId="+regInvitationId);
	return new ModelAndView(selfRegistrationTemplate);
	
}
	private long[] getDistributorCustomFieldIds(Customer customer)
	{
		List<CustomField> list=customer.getDistributor().getCustomFields();
		long[] ids=new long[list.size()];
		int counter=0;
		for(CustomField field:list)
		{
			ids[counter]=field.getId().longValue();
			counter++;
		}
		return ids;
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
		UserForm form = (UserForm)command;
		UserValidator validator = (UserValidator)this.getValidator();
		
		if(methodName.equals("saveUser")){
			if( form.getEventSource().equalsIgnoreCase("donotValidate"))
				return ;
			validator.validate(form, errors);
			
			VU360User usr=vu360UserService.findUserByUserName(form.getVu360User().getUsername().trim());
			
			// LMS-6353 :: No need to check ID here as ID doesn't exist yet.
			if(!form.getVu360User().getUsername().trim().isEmpty()&& usr!=null)	{
				errors.rejectValue("vu360User.username", "error.addNewLearner.username.all.existUsername","");
				
			}
			else if (activeDirectoryService.findADUser(form.getVu360User().getUsername())){
				errors.rejectValue("vu360User.username", "error.addNewUser.AD.existUsername","");
			} 
				
			if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "password"))){
				if(!FieldsValidation.isPasswordCorrect(ServletRequestUtils.getStringParameter(request, "password"))){
					errors.rejectValue("vu360User.password", "error.editLearnerPassword.invalidlength","");
				}else if(!StringUtils.equals(ServletRequestUtils.getStringParameter(request, "password"), ServletRequestUtils.getStringParameter(request, "confirmpassword"))){
					errors.rejectValue("vu360User.password", "error.password.matchPassword","");
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

	public String getSelfRegistrationTemplate() {
		return selfRegistrationTemplate;
	}

	public void setSelfRegistrationTemplate(String selfRegistrationTemplate) {
		this.selfRegistrationTemplate = selfRegistrationTemplate;
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

	public String getRegistrationRedirectTemplate() {
		return registrationRedirectTemplate;
	}

	public void setRegistrationRedirectTemplate(String registrationRedirectTemplate) {
		this.registrationRedirectTemplate = registrationRedirectTemplate;
	}

	public String getLearnerRegistrationTemplate() {
		return learnerRegistrationTemplate;
	}

	public void setLearnerRegistrationTemplate(String learnerRegistrationTemplate) {
		this.learnerRegistrationTemplate = learnerRegistrationTemplate;
	}

	public String getLearnerThankyouTemplate() {
		return learnerThankyouTemplate;
	}

	public void setLearnerThankyouTemplate(String learnerThankyouTemplate) {
		this.learnerThankyouTemplate = learnerThankyouTemplate;
	}

	public String getDefaultTemplate() {
		return defaultTemplate;
	}

	public void setDefaultTemplate(String defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}

	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	/**
	 * // [1/27/2011] LMS-7183 :: Retired Course Functionality II
	 * @return the enrollmentService
	 */
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	/**
	 * // [1/27/2011] LMS-7183 :: Retired Course Functionality II
	 * @param enrollmentService the enrollmentService to set
	 */
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
}
