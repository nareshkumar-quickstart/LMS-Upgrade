package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.RegulatoryApproval;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditApprovalValidator;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.HtmlEncoder;

/**
 * 
 * @author Saptarshi
 *
 */
public class RenewCourseApproval  extends AbstractWizardFormController {

	public static final String CUSTOMFIELD_ENTITY_COURSEAPPROVAL = "CUSTOMFIELD_COURSEAPPROVAL";

	private AccreditationService accreditationService = null;

	private	String closeApprovalTemplate=null;

	public RenewCourseApproval() {
		super();
		setCommandName("approvalForm");
		setCommandClass(ApprovalForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);		
		setPages(new String[] {
				"accreditation/approvals/editApproval/renewCourseApproval/renew_approval"
				, "accreditation/approvals/editApproval/renewCourseApproval/renew_approval_confirm"
		});
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if (request.getParameter("appID") != null) {
			long courseApprovalId = Long.parseLong(request.getParameter("appID"));
			CourseApproval courseApproval = accreditationService.getCourseApprovalById(courseApprovalId);
			form.setCourseApproval(courseApproval);
			
			CourseApproval renewCourseApproval = new CourseApproval();
			renewCourseApproval.setApprovedCourseName(courseApproval.getApprovedCourseName());
			renewCourseApproval.setApprovedCreditHours(courseApproval.getApprovedCreditHours());
			renewCourseApproval.setCertificate(courseApproval.getCertificate());
			renewCourseApproval.setContentOwner(courseApproval.getContentOwner());
			renewCourseApproval.setCourse(courseApproval.getCourse());
			renewCourseApproval.setCourseGroups(courseApproval.getCourseGroups());
			renewCourseApproval.setCourseApprovalEffectivelyEndsDate(courseApproval.getCourseApprovalEffectivelyEndsDate());
			renewCourseApproval.setCourseApprovalEffectivelyStartDate(courseApproval.getCourseApprovalEffectivelyStartDate());
			renewCourseApproval.setCourseApprovalInformation(courseApproval.getCourseApprovalInformation());
			renewCourseApproval.setCourseApprovalNumber(courseApproval.getCourseApprovalNumber());
			renewCourseApproval.setCourseApprovalRenewalFee(courseApproval.getCourseApprovalRenewalFee());
			renewCourseApproval.setCourseApprovalStatus(courseApproval.getCourseApprovalStatus());
			renewCourseApproval.setCourseApprovalSubmissionFee(courseApproval.getCourseApprovalSubmissionFee());
			renewCourseApproval.setCourseApprovaltype(courseApproval.getCourseApprovaltype());
			renewCourseApproval.setCourseSubmissionReminderDate(courseApproval.getCourseSubmissionReminderDate());
			renewCourseApproval.setCustomFields(courseApproval.getCustomFields());
			renewCourseApproval.setCustomFieldValues(courseApproval.getCustomFieldValues());
			renewCourseApproval.setDocuments(courseApproval.getDocuments());
			renewCourseApproval.setMostRecentlySubmittedForApprovalDate(courseApproval.getMostRecentlySubmittedForApprovalDate());
			renewCourseApproval.setOriginalCourseApprovalFee(courseApproval.getOriginalCourseApprovalFee());
			renewCourseApproval.setProvider(courseApproval.getProvider());
			renewCourseApproval.setRegulatorCategories(courseApproval.getRegulatorCategories());
			renewCourseApproval.setRequirements(courseApproval.getRequirements());
			renewCourseApproval.setTag(courseApproval.getTag());
			if(courseApproval.getTemplate()!=null){
				renewCourseApproval.setTemplate(accreditationService.getTemplateById(courseApproval.getTemplate().getId()));
			}
			renewCourseApproval.setCertificate(courseApproval.getCertificate());
			renewCourseApproval.setAffidavit(courseApproval.getAffidavit());
			renewCourseApproval.setUsePurchasedCertificateNumbers(courseApproval.getUsePurchasedCertificateNumbers());
			renewCourseApproval.setUseCertificateNumberGenerator(courseApproval.getUseCertificateNumberGenerator());
			renewCourseApproval.setCertificateNumberGeneratorNextNumber(courseApproval.getCertificateNumberGeneratorNextNumber());
			renewCourseApproval.setCertificateNumberGeneratorNumberFormat(courseApproval.getCertificateNumberGeneratorNumberFormat());
			renewCourseApproval.setCertificateNumberGeneratorPrefix(courseApproval.getCertificateNumberGeneratorPrefix());
			renewCourseApproval.setPurchaseCertificateNumbers(courseApproval.getPurchaseCertificateNumbers());
			renewCourseApproval.setSelfReported(courseApproval.getSelfReported());
			//renewCourseApproval.setSelfReported(courseApproval.get);
			
			form.setCertificateNumberGeneratorNextNumberString(courseApproval.getCertificateNumberGeneratorNextNumber() > 0 ? courseApproval.getCertificateNumberGeneratorNextNumber() + "" : "");
			form.setRenewCourseApproval(renewCourseApproval);
			
			form.setApprovalEffectivelyStartDate(formatter.format(courseApproval.getCourseApprovalEffectivelyStartDate()));
			form.setApprovalEffectivelyEndDate(formatter.format(courseApproval.getCourseApprovalEffectivelyEndsDate()));
			form.setMostRecentlySubmittedForApprovalDate(formatter.format(courseApproval.getMostRecentlySubmittedForApprovalDate()));
			form.setSubmissionReminderDate(formatter.format(courseApproval.getCourseSubmissionReminderDate()));
			
			List<CustomFieldValue> customFieldValues = courseApproval.getCustomFieldValues();
			List<CustomField> courseApprovalCustomFieldList = courseApproval.getCustomFields();
			List<CustomField> globalCustomFieldList  = accreditationService.findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_COURSEAPPROVAL), "", "");
			form.setCustomFields(this.arrangeCustomField(courseApprovalCustomFieldList, customFieldValues, globalCustomFieldList));
		}
		return command;
	}
	
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> arrangeCustomField(List<CustomField> courseApprovalCustomFieldList, List<CustomFieldValue> customFieldValues, List<CustomField> globalCustomFieldList){

		CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
		List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
		Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();
		List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();

		for (CustomField globalCustomField : globalCustomFieldList){
			totalCustomFieldList.add(globalCustomField);
		}
		for (CustomField customField : courseApprovalCustomFieldList){
			totalCustomFieldList.add(customField);
		}

		for(CustomField customField:totalCustomFieldList){
			if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
				List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
				fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

				if (customField instanceof MultiSelectCustomField){
					CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
					existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
				}

			}else {
				fieldBuilder.buildCustomField(customField,0,customFieldValues);
			}
		}

		for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList){
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
		return customFieldList;
	}

	
	/**
	 * For getting custom field values by custom field.
	 * @param customField
	 * @param customFieldValues
	 * @return CustomFieldValue
	 */
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

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		super.postProcessPage(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();

		context.put("target", "showCourseApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {

		ApprovalForm form = (ApprovalForm)command;
		
		CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval( form.getCourseApproval().getId());
		CourseApproval renewCourseApproval = form.getRenewCourseApproval();
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if (renewCourseApproval != null) {
				
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ){
				renewCourseApproval.setCourseApprovalEffectivelyStartDate(formatter.parse(form.getApprovalEffectivelyStartDate()));
			}
			
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ){
				Date date=this.setTimeToDayend(formatter.parse(form.getApprovalEffectivelyEndDate()));				
				renewCourseApproval.setCourseApprovalEffectivelyEndsDate(date);
			}
			
			if ( !StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ){
				renewCourseApproval.setMostRecentlySubmittedForApprovalDate(formatter.parse(form.getMostRecentlySubmittedForApprovalDate()));
			}
			
			if ( !StringUtils.isBlank(form.getSubmissionReminderDate()) ){
				renewCourseApproval.setCourseSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));
			}

			if(StringUtils.isNotBlank(form.getCertificateNumberGeneratorNextNumberString())){
				renewCourseApproval.setCertificateNumberGeneratorNextNumber(Long.parseLong(form.getCertificateNumberGeneratorNextNumberString()));
			}
				
			renewCourseApproval.setRenewedFrom(form.getCourseApproval());
			renewCourseApproval.setCustomFieldValues(this.saveCustomField(form.getCustomFields()));	

			accreditationService.saveCourseApproval(renewCourseApproval);
			
			
			courseApproval.setActive(false);
			accreditationService.saveCourseApproval(courseApproval);
		} 
		
		
		Map<Object, Object> context = new HashMap<Object, Object>();

		context.put("target", "showCourseApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
	}
	
	public List<CustomFieldValue> saveCustomField ( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		if (customFields.size()>0){
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFields){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (multiSelectCustomField.getCheckBox()){

						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){

							if (customFieldValueChoice.isSelected()){
								CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
								customFieldValueChoices.add(customFieldValueChoiceRef);
							}

						}
						CustomFieldValue customFieldValue = new CustomFieldValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);
					}else {
						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
						if(customField.getSelectedChoices()!=null){
							Map<Long,CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long,CustomFieldValueChoice>();
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
								customFieldValueChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
							}

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								if(customFieldValueChoiceMap.containsKey(new Long(selectedChoiceIdString))){
									CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap.get(new Long(selectedChoiceIdString));
									customFieldValueChoices.add(customFieldValueChoiceRef);
								}
							}
						}

						CustomFieldValue customFieldValue = new CustomFieldValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);

					}

				}else {
					CustomFieldValue customFieldValue = new CustomFieldValue();
					CustomFieldValue customFieldValueRef= customField.getCustomFieldValueRef();
					
					if(customFieldValueRef.getValue() != null){
						customFieldValue.setValue(HtmlEncoder.escapeHtmlFull(customFieldValueRef.getValue().toString()).toString());
					}else{
						customFieldValue.setValue("");
					}
					customFieldValue.setCustomField(customField.getCustomFieldRef());
					customFieldValues.add(customFieldValue);
				}
			}
			if (customFieldValues.size()>0){
				return customFieldValues;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map<Object,Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		switch(page){
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		ApprovalForm form = (ApprovalForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:

			validator.validateCourseApprovalRenew(form, errors);
			long regulatorCategoryId = 0;
			long courseId = 0;
			if(form.getRenewCourseApproval()!=null){
				if(form.getRenewCourseApproval().getRegulatorCategory()!=null){
					regulatorCategoryId = form.getRenewCourseApproval().getRegulatorCategory().getId();
				}
				if(form.getRenewCourseApproval().getCourse()!=null){
					courseId = form.getRenewCourseApproval().getCourse().getId();
				}
			}

			Date startDate = null, endDate = null;

			if(StringUtils.isNotBlank(form.getApprovalEffectivelyStartDate())){
				startDate = DateUtil.getDateObject(form.getApprovalEffectivelyStartDate());
			}

			if(StringUtils.isNotBlank(form.getApprovalEffectivelyEndDate())){
				endDate = DateUtil.getDateObject(form.getApprovalEffectivelyEndDate());
			}
			
			if(regulatorCategoryId > 0 && courseId > 0 && startDate != null && endDate != null){
				if(getAccreditationService().isCourseAlreadyAssociatedWithRegulatorAuthority(courseId, regulatorCategoryId, startDate, endDate, form.getCourseApproval().getId())){
					errors.rejectValue("approvalEffectivelyStartDate", "error.approval.summary.alreadyAssociated","");
				}
			}
			
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
			break;
		case 1:
			break;
		case 2:
			break;
		}
		super.validatePage(command, errors, page, finish);
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
	 * @return the closeApprovalTemplate
	 */
	public String getCloseApprovalTemplate() {
		return closeApprovalTemplate;
	}

	/**
	 * @param closeApprovalTemplate the closeApprovalTemplate to set
	 */
	public void setCloseApprovalTemplate(String closeApprovalTemplate) {
		this.closeApprovalTemplate = closeApprovalTemplate;
	}

	private Date setTimeToDayend(Date date){		
		GregorianCalendar cal= new GregorianCalendar();
		cal.setTime(date);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
		cal.set(GregorianCalendar.MINUTE, 59);
		cal.set(GregorianCalendar.SECOND, 59);
		return cal.getTime();
	}

}
