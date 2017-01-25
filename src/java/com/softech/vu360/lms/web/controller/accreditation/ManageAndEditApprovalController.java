package com.softech.vu360.lms.web.controller.accreditation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.RegulatoryApproval;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulatorCategory;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRequirement;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageApproval;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditApprovalValidator;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.lms.webservice.client.LMSCourseApprovalPublishWSClient;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.DocumentSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ManageApprovalSort;
import com.softech.vu360.util.PurchaseCertificateNumberSort;
import com.softech.vu360.util.RegulatorSort;
import com.softech.vu360.util.VU360Properties;

/**
 * 
 * @author Saptarshi
 *
 */
@SuppressWarnings("all")
public class ManageAndEditApprovalController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditApprovalController.class.getName());

	public static final String COURSE_APPROVAL = "Course Approval";
	public static final String PROVIDER_APPROVAL = "Provider Approval";
	public static final String INSTRUCTOR_APPROVAL = "Instructor Approval";
	public static final String ALL = "All";

	public static final String ACTIVE_YES = "Yes";
	public static final String ACTIVE_NO = "No";
	
	
	public static final String CUSTOMFIELD_ENTITY_COURSEAPPROVAL = "CUSTOMFIELD_COURSEAPPROVAL";
	public static final String CUSTOMFIELD_ENTITY_PROVIDERAPPROVAL = "CUSTOMFIELD_PROVIDERAPPROVAL";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTORAPPROVAL = "CUSTOMFIELD_INSTRUCTORAPPROVAL";

	private AccreditationService accreditationService;
	private LCMSClientWS lcmsClientWS = null;
	private LMSCourseApprovalPublishWSClient lmsCourseApprovalClientToSF = null;
	
//	HttpSession session = null;

	private String searchApprovalTemplate = null;
	private String editApprovalDocumentsTemplate = null;

	private String courseApprovalSummaryTemplate = null;
	private String courseApprovalRegulatorTemplate = null;
	private String courseApprovalRegulatorCategoryTemplate = null;
	private String courserApprovalReportingFieldTemplate = null;
	private String courserApprovalRequirementTemplate = null;
	private String showCourseApprovalDocumentsTemplate = null;
	private String courseApprovalCustomFieldTemplate = null;

	private String providerApprovalSummaryTemplate = null;
	private String providerApprovalRegulatorTemplate = null;
	private String providerApprovalProviderTemplate = null;
	private String showProviderApprovalDocumentsTemplate = null;
	private String providerApprovalCustomFieldTemplate = null;
	private String courseApprovalPurchasedCertificateTemplate = null;

	private String instructorApprovalSummaryTemplate = null;
	private String instructorApprovalRegulatorTemplate = null;
	private String instructorApprovalProviderTemplate = null;
	private String showInstructorApprovalDocumentsTemplate = null;
	private String instructorApprovalCustomFieldTemplate = null;
	private String instructorApprovalInstructorTemplate = null;
	private String instructorApprovalCourseTemplate = null;
	private String redirectTemplate = null;
    private String listCreditReportingFieldTemplate= null;
    
    private String redirectChangeCourse = null;
    private String redirectChangeRegulatorCategory = null;
    
	public ManageAndEditApprovalController() {
		super();
	}

	public ManageAndEditApprovalController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
			request.setAttribute("newPage","true");
		return displayApprovals( request,  response);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof ApprovalForm ){
			ApprovalForm form = (ApprovalForm)command;
			if( methodName.equals("showProviderApprovalSummary")
					|| methodName.equals("showProviderApprovalRegulator")
					||(methodName.equals("showApprovalRegulatorCategories") && form.getEntity().equalsIgnoreCase(ApprovalForm.PROVIDER_APPROVAL))
					|| methodName.equals("showProviderForProviderApproval")
					|| methodName.equals("showProviderApprovalDocuments")
					|| methodName.equals("searchProviderApprovalDocuments")
					|| methodName.equals("showProviderApprovalCustomField")){

				ProviderApproval providerApproval = accreditationService.loadForUpdateProviderApproval(form.getAppId());
				form.setProviderApproval(providerApproval);
//				form.setRegulatorCategories(this.convertToUiObject(providerApproval.getRegulatorCategories()));//CONVERT
				form.setEntity(ApprovalForm.PROVIDER_APPROVAL);
			} else if(methodName.equals("showInstructorApprovalSummary")
					|| methodName.equals("showInstructorApprovalRegulator")
					||(methodName.equals("showApprovalRegulatorCategories") && form.getEntity().equalsIgnoreCase(ApprovalForm.INSTRUCTOR_APPROVAL))
					|| methodName.equals("showInstructorApprovalInstructor")
					|| methodName.equals("showProviderForInstructorApproval")
					|| methodName.equals("showInstructorApprovalCourse")
					|| methodName.equals("showInstructorApprovalDocuments")
					|| methodName.equals("showInstructorApprovalCustomField")){					

				InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(form.getAppId());
				form.setInstructorApproval(instructorApproval);
//				form.setRegulatorCategories(this.convertToUiObject(instructorApproval.getRegulatorCategories()));
				form.setEntity(ApprovalForm.INSTRUCTOR_APPROVAL);
			} else if(methodName.equals("showCourseApprovalSummary")
					|| methodName.equals("showCourseApprovalRegulator")					
					||(methodName.equals("showApprovalRegulatorCategories") && form.getEntity().equalsIgnoreCase(ApprovalForm.COURSE_APPROVAL)) 
					|| methodName.equals("showCourseApprovalReportingFields")
					|| methodName.equals("showCourseApprovalRequirement")
					|| methodName.equals("showCourseApprovalDocuments")
					|| methodName.equals("showCourseApprovalPurchasedCertificate")
					|| methodName.equals("showCourseApprovalCustomField")){

				CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(form.getAppId());
				form.setCourseApproval(courseApproval);
//				form.setRegulatorCategories(this.convertToUiObject(courseApproval.getRegulatorCategories()));
				form.setPurchaseCertificateNumbers(courseApproval.getPurchaseCertificateNumbers());
				form.setCertificateNumberGeneratorNextNumberString(courseApproval.getCertificateNumberGeneratorNextNumber() > 0 ? courseApproval.getCertificateNumberGeneratorNextNumber() + "" : "");
				form.setEntity(ApprovalForm.COURSE_APPROVAL);
				Integer certificateExpirationPeriod = courseApproval.getCertificateExpirationPeriod();
				if (certificateExpirationPeriod != null && certificateExpirationPeriod != 0) {
					form.setCertificateExpirationPeriod(String.valueOf(certificateExpirationPeriod));
				} else {
					form.setCertificateExpirationPeriod("");
				}	
			}

			if(methodName.equals("showProviderApprovalDocuments")) {
				form.setDocuments(form.getProviderApproval().getDocuments());
			} else if (methodName.equals("showInstructorApprovalDocuments")) {
				form.setDocuments(form.getInstructorApproval().getDocuments());
			} else if (methodName.equals("showCourseApprovalDocuments")) {
				form.setDocuments(form.getCourseApproval().getDocuments());
			}

			if (methodName.equals("showCourseApprovalSummary")) {
				CourseApproval courseApproval = form.getCourseApproval();
				if (courseApproval != null){
					List<CustomFieldValue> customFieldValues = courseApproval.getCustomFieldValues();
					List<CustomField> courseApprovalCustomFieldList = courseApproval.getCustomFields();
					List<CustomField> globalCustomFieldList  = accreditationService.findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_COURSEAPPROVAL), "", "");
					form.setCustomFields(this.arrangeCustomField(courseApprovalCustomFieldList, customFieldValues, globalCustomFieldList));
				}
			} else if (methodName.equals("showProviderApprovalSummary")) {
				ProviderApproval providerApproval = form.getProviderApproval();
				if (providerApproval != null){
					List<CustomFieldValue> customFieldValues = providerApproval.getCustomFieldValues();
					List<CustomField> courseApprovalCustomFieldList = providerApproval.getCustomFields();
					List<CustomField> globalCustomFieldList  = accreditationService.findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_PROVIDERAPPROVAL), "", "");
					form.setCustomFields(this.arrangeCustomField(courseApprovalCustomFieldList, customFieldValues, globalCustomFieldList));
				}
			} else if (methodName.equals("showInstructorApprovalSummary")) {
				InstructorApproval instructorApproval = form.getInstructorApproval();
				if (instructorApproval != null){
					List<CustomFieldValue> customFieldValues = instructorApproval.getCustomFieldValues();
					List<CustomField> courseApprovalCustomFieldList = instructorApproval.getCustomFields();
					List<CustomField> globalCustomFieldList  = accreditationService.findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_INSTRUCTORAPPROVAL), "", "");
					form.setCustomFields(this.arrangeCustomField(courseApprovalCustomFieldList, customFieldValues, globalCustomFieldList));
				}
			}

			if( methodName.equals("downloadDocument") || methodName.equals("saveDocument") ) {
				if(form.getDocEntity().equalsIgnoreCase(form.PROVIDER_DOCUMENT)){
					ProviderApproval providerApproval = accreditationService.getProviderApprovalById(form.getAppId());
					form.setProviderApproval(providerApproval);
				}else if(form.getDocEntity().equalsIgnoreCase(form.COURSE_DOCUMENT)){
					CourseApproval courseApproval = accreditationService.getCourseApprovalById(form.getAppId());
					form.setCourseApproval(courseApproval);
				}else if(form.getDocEntity().equalsIgnoreCase(form.INSTRUCTOR_DOCUMENT)){
					InstructorApproval instructorApproval = accreditationService.getInstructorApprovalById(form.getAppId());
					form.setInstructorApproval(instructorApproval);
				}
			}						
		}	
	}

	private List<ApprovalRegulatorCategory> convertToUiObject(List <RegulatorCategory> categories){
		List <ApprovalRegulatorCategory> uiCategories=new ArrayList<ApprovalRegulatorCategory>();
		for(RegulatorCategory regulatorCategory:categories){
			ApprovalRegulatorCategory appRegulator = new ApprovalRegulatorCategory();
			appRegulator.setCategory(regulatorCategory);
			appRegulator.setRegulator(regulatorCategory.getRegulator());
			appRegulator.setSelected(false);
			uiCategories.add(appRegulator);			
		}
		return  uiCategories;
	}
	/**
	 * This method arrange the custom field for approvals summary page.
	 * @param courseApprovalCustomFieldList
	 * @param customFieldValues
	 * @return List<com.softech.vu360.lms.web.controller.model.customfield.CustomField>
	 */
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

	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		ApprovalForm form = (ApprovalForm)command;
		if( methodName.equals("saveProviderApprovalSummary")) {
			validator.validateProviderApprovalSummary(form, errors);
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
		} else if(methodName.equals("saveCourseApprovalSummary")) {
			validator.validateCourseApprovalSummary(form, errors);
			
			long regulatorCategoryId = 0;
			long courseId = 0;
			if( form.getCourseApproval() != null ) {
				regulatorCategoryId = form.getCourseApproval().getRegulatorCategory().getId();
				courseId = form.getCourseApproval().getCourse().getId();
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
		} else if(methodName.equals("saveInstructorApprovalSummary")) {
			validator.validateInstructorApprovalSummary(form, errors);
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
		}
	}

	/**
	 * For display manage approval page
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView displayApprovals(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String appType = "All";
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("appType", appType);
		return new ModelAndView(searchApprovalTemplate, "context", context);
	}


	/**
	 * For approval search
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView searchApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			
			List<ManageApproval> approvalList = new ArrayList<ManageApproval>();
			String appName = (request.getParameter("appName") == null) ? "" : request.getParameter("appName");
			String appNumber = (request.getParameter("appNumber") == null) ? "" : request.getParameter("appNumber");
			String appType = (request.getParameter("appType") == null) ? "All" : request.getParameter("appType");
			String appStatus = request.getParameter("appStatus");
			String regulator = (request.getParameter("regulator") == null) ? "" : request.getParameter("regulator");
			appName = HtmlEncoder.escapeHtmlFull(appName).toString();
			context.put("appName", appName);
			appNumber = HtmlEncoder.escapeHtmlFull(appNumber).toString();
			context.put("appNumber", appNumber);
			context.put("appType", appType);
			context.put("appStatus", appStatus);
			regulator = HtmlEncoder.escapeHtmlFull(regulator).toString();
			context.put("regulator", regulator);
			
				
			if (appType.equalsIgnoreCase(PROVIDER_APPROVAL)) {
				List<ProviderApproval> approvals = accreditationService.findProviderApproval(appName, appNumber, appType, regulator,Integer.parseInt(appStatus));
				for (ProviderApproval providerApproval : approvals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(providerApproval.getId());
					manageApproval.setApprovalName(providerApproval.getApprovedProviderName());
					manageApproval.setApprovalNumber(providerApproval.getProviderApprovalNumber());
					manageApproval.setApprovalType(PROVIDER_APPROVAL);
//					if(providerApproval.getRegulatorCategories() != null && providerApproval.getRegulatorCategories().size() >0)
//						manageApproval.setRegulatorName(providerApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + providerApproval.getRegulatorCategories().size());
					if(providerApproval.getRegulatorCategory() != null){
						manageApproval.setRegulatorName(providerApproval.getRegulatorCategory().getRegulator().getName());
					}
					if (providerApproval.isActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);

					approvalList.add(manageApproval);
				}

			} else if (appType.equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
				List<InstructorApproval> approvals = accreditationService.findInstructorApproval(appName, appNumber, appType, regulator,Integer.parseInt(appStatus));
				for (InstructorApproval instructorApproval : approvals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(instructorApproval.getId());
					manageApproval.setApprovalName(instructorApproval.getApprovedInstructorName());
					manageApproval.setApprovalNumber(instructorApproval.getInstructorApprovalNumber());
					manageApproval.setApprovalType(INSTRUCTOR_APPROVAL);
//					if(instructorApproval.getRegulatorCategories() != null && instructorApproval.getRegulatorCategories().size() >0)
//						manageApproval.setRegulatorName(instructorApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + instructorApproval.getRegulatorCategories().size());
					if(instructorApproval.getRegulatorCategory() != null){
						manageApproval.setRegulatorName(instructorApproval.getRegulatorCategory().getRegulator().getName());
					}
					if (instructorApproval.isActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);
					approvalList.add(manageApproval);
				}

			} else if (appType.equalsIgnoreCase(COURSE_APPROVAL)) {
				List<CourseApproval> approvals = accreditationService.findCourseApproval(appName, appNumber, appType, regulator,Integer.parseInt(appStatus));
				for (CourseApproval cApproval : approvals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(cApproval.getId());
					manageApproval.setApprovalName(cApproval.getApprovedCourseName());
					manageApproval.setApprovalNumber(cApproval.getCourseApprovalNumber());
					manageApproval.setApprovalType(COURSE_APPROVAL);
//					if(cApproval.getRegulatorCategories() != null && cApproval.getRegulatorCategories().size() >0)
//						manageApproval.setRegulatorName(cApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + cApproval.getRegulatorCategories().size());
					if(cApproval.getRegulatorCategory() != null){
						manageApproval.setRegulatorName(cApproval.getRegulatorCategory().getRegulator().getName());
					}
					if (cApproval.getActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);
					approvalList.add(manageApproval);
				}

			} else if (appType.equalsIgnoreCase(ALL)) {
				
				
				List<CourseApproval> courseApprovals = accreditationService.findCourseApproval(appName, appNumber, appType, regulator,Integer.parseInt(appStatus));
				for (CourseApproval cApproval : courseApprovals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(cApproval.getId());
					manageApproval.setApprovalName(cApproval.getApprovedCourseName());
					manageApproval.setApprovalNumber(cApproval.getCourseApprovalNumber());
					manageApproval.setApprovalType(COURSE_APPROVAL);
//					if(cApproval.getRegulatorCategories() != null && cApproval.getRegulatorCategories().size() >0)
//						manageApproval.setRegulatorName(cApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + cApproval.getRegulatorCategories().size());
					if(cApproval.getRegulatorCategory() != null){
						manageApproval.setRegulatorName(cApproval.getRegulatorCategory().getRegulator().getName());
					}
					if (cApproval.getActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);
					approvalList.add(manageApproval);
				}
				List<ProviderApproval> providerApprovals = accreditationService.findProviderApproval(appName, appNumber, appType, regulator,Integer.parseInt(appStatus));
				for (ProviderApproval providerApproval : providerApprovals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(providerApproval.getId());
					manageApproval.setApprovalName(providerApproval.getApprovedProviderName());
					manageApproval.setApprovalNumber(providerApproval.getProviderApprovalNumber());
					manageApproval.setApprovalType(PROVIDER_APPROVAL);
//					if(providerApproval.getRegulatorCategories() != null && providerApproval.getRegulatorCategories().size() >0)
//						manageApproval.setRegulatorName(providerApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + providerApproval.getRegulatorCategories().size());
					if(providerApproval.getRegulatorCategory() != null){
						manageApproval.setRegulatorName(providerApproval.getRegulatorCategory().getDisplayName());
					}
					if (providerApproval.isActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);
					approvalList.add(manageApproval);
				}
				List<InstructorApproval> instructorApprovals = accreditationService.findInstructorApproval(appName, appNumber, appType, regulator,Integer.parseInt(appStatus));
				for (InstructorApproval instructorApproval : instructorApprovals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(instructorApproval.getId());
					manageApproval.setApprovalName(instructorApproval.getApprovedInstructorName());
					manageApproval.setApprovalNumber(instructorApproval.getInstructorApprovalNumber());
					manageApproval.setApprovalType(INSTRUCTOR_APPROVAL);
//					if(instructorApproval.getRegulatorCategories() != null && instructorApproval.getRegulatorCategories().size() >0)
//						manageApproval.setRegulatorName(instructorApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + instructorApproval.getRegulatorCategories().size());
					if(instructorApproval.getRegulatorCategory() != null){
						manageApproval.setRegulatorName(instructorApproval.getRegulatorCategory().getDisplayName());
					}
					if (instructorApproval.isActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);
					approvalList.add(manageApproval);
				}
			}
			
			//============================For Sorting============================
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
//			if( pageIndex == null ) pageIndex = session.getAttribute("pageCurrIndex").toString();

			if( sortColumnIndex != null && sortDirection != null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("regulatorName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 3);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("regulatorName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 3);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("4") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("active");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 4);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("active");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 4);
					}
				}
			}	
			context.put("approvals", approvalList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll", request.getParameter("showAll"));
//			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			
			//========================================================================
			
			return new ModelAndView(searchApprovalTemplate, "context",context);

		} catch (Exception e) {
			log.debug("COUGHT EXCEPTION  "+e);
		}
		return new ModelAndView(searchApprovalTemplate);
	}

	/**
	 * This method delete the approval.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if  ( request.getParameterValues("approvalIds") != null ) {
			
			String []checkID = request.getParameterValues("approvalIds");
			
			int success = 0, failure = 0;
			
			for (int i=0;i<checkID.length;i++) {
		
				RegulatoryApproval regulatoryApproval = accreditationService.loadForUpdateRegulatoryApproval(Long.valueOf(checkID[i]));
				
				if(regulatoryApproval instanceof CourseApproval){
					
					if(!getAccreditationService().isCourseApprovalLinkedWithLearnerEnrollment(regulatoryApproval.getId())){						
						accreditationService.deleteRegulatoryApproval(regulatoryApproval);
						success++;
					}else{
						failure++;
					}
				}else{
					
					regulatoryApproval.setDeleted(true);
					accreditationService.saveApproval(regulatoryApproval);
					
				}
				
			}
			
			
			if(failure > 0 && success <=0 ){
				errors.reject("error.approval.delete.failure", "");
			}else if(failure > 0){
				errors.reject("error.approval.delete.failureAndSuccess", new Object[]{success, failure}, "");
			}else if(success > 0){
				errors.reject("error.approval.delete.success", new Object[]{success}, "");
			}else{
				errors.reject("error.approval.delete.success", new Object[]{success}, "");
			}
		}
		
		context.put("target", "searchApproval");
		
		String appName = (request.getParameter("appName") == null) ? "" : request.getParameter("appName");
		String appNumber = (request.getParameter("appNumber") == null) ? "" : request.getParameter("appNumber");
		String appType = (request.getParameter("appType") == null) ? "All" : request.getParameter("appType");
		String appStatus = request.getParameter("appStatus");

		context.put("appName", HtmlEncoder.escapeHtmlFull(appNumber).toString());
		context.put("appNumber", HtmlEncoder.escapeHtmlFull(appNumber).toString());
		context.put("appType", appType);
		context.put("appStatus", appStatus);
		
		return new ModelAndView(searchApprovalTemplate, "context", context);
		
	}
	/**
	 * for displaying course approval summary page...
	 */
	public ModelAndView showCourseApprovalSummary( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		 

		if ( form != null ) {
			if ( form.getCourseApproval() != null ) {
				form.setApprovalEffectivelyStartDate(formatter.format(form.getCourseApproval().getCourseApprovalEffectivelyStartDate()));
				form.setApprovalEffectivelyEndDate(formatter.format(form.getCourseApproval().getCourseApprovalEffectivelyEndsDate()));
				form.setMostRecentlySubmittedForApprovalDate(formatter.format(form.getCourseApproval().getMostRecentlySubmittedForApprovalDate()));
				form.setSubmissionReminderDate(formatter.format(form.getCourseApproval().getCourseSubmissionReminderDate()));
				/*
				if(form.getCourseApproval().getCertificateExpirationPeriod() != 0)
				form.setCertificateExpirationPeriod(form.getCourseApproval().getCertificateExpirationPeriod());
				*/
				if(form.getCourseApproval().getCertificate() == null) {
					log.debug("Certificate is not available");
				}
			}
		}
		return new ModelAndView(courseApprovalSummaryTemplate);
	}

	/**
	 * For display provider approval summary page
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showProviderApprovalSummary( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		if (form!=null){
			if (form.getProviderApproval() != null){
				form.setApprovalEffectivelyStartDate(formatter.format(form.getProviderApproval().getProviderApprovalEffectivelyStartDate()));
				form.setApprovalEffectivelyEndDate(formatter.format(form.getProviderApproval().getCourseApprovalEffectivelyEndDate()));
				form.setMostRecentlySubmittedForApprovalDate(formatter.format(form.getProviderApproval().getProviderMostRecentlySubmittedForApprovalDate()));
				form.setOriginallyApprovedDate(formatter.format(form.getProviderApproval().getProviderOriginallyApprovedDate()));
				form.setSubmissionReminderDate(formatter.format(form.getProviderApproval().getProviderSubmissionReminderDate()));
			}
		}
		return new ModelAndView(providerApprovalSummaryTemplate);
	}

	/**
	 * This method display instructor approval summary page
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showInstructorApprovalSummary( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		if (form!=null){
			if (form.getInstructorApproval() != null){
				form.setApprovalEffectivelyStartDate(formatter.format(form.getInstructorApproval().getInstructorApprovalEffectivelyStartDate()));
				form.setApprovalEffectivelyEndDate(formatter.format(form.getInstructorApproval().getInstructorApprovalEffectivelyEndsDate()));
				form.setMostRecentlySubmittedForApprovalDate(formatter.format(form.getInstructorApproval().getMostRecentlySubmittedForApprovalDate()));
				form.setOriginallyApprovedDate(formatter.format(form.getInstructorApproval().getInspectorOriginallyApprovalDate()));
				form.setSubmissionReminderDate(formatter.format(form.getInstructorApproval().getInstructorSubmissionReminderDate()));
			}
		}
		return new ModelAndView(instructorApprovalSummaryTemplate);
	}

	private void saveCourseApproval(Object command)throws Exception{
		
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		CourseApproval courseApproval = form.getCourseApproval();
		
		if (courseApproval != null) {
			
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ){
				courseApproval.setCourseApprovalEffectivelyStartDate(formatter.parse(form.getApprovalEffectivelyStartDate()));
			}
			
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ){
				Date date=this.setTimeToDayend(formatter.parse(form.getApprovalEffectivelyEndDate()));				
				courseApproval.setCourseApprovalEffectivelyEndsDate(date);
			}
			
			if ( !StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ){
				courseApproval.setMostRecentlySubmittedForApprovalDate(formatter.parse(form.getMostRecentlySubmittedForApprovalDate()));
			}
			
			if ( !StringUtils.isBlank(form.getSubmissionReminderDate()) ){
				courseApproval.setCourseSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));
			}

			if(StringUtils.isNotBlank(form.getCertificateNumberGeneratorNextNumberString())){
				courseApproval.setCertificateNumberGeneratorNextNumber(Long.parseLong(form.getCertificateNumberGeneratorNextNumberString()));
			}
			
			if(form.getCertificateExpirationPeriod().length() > 0)
			{
				log.debug("Found Value in CertificateExpirationPeriod");
				courseApproval.setCertificateExpirationPeriod(Integer.parseInt(form.getCertificateExpirationPeriod()));
				log.debug("Value in SetCertificationExpirationPeriod : " + Integer.parseInt(form.getCertificateExpirationPeriod()));
			}
			else
			{
				log.debug("No Value found in CertificateExpirationPeriod");	
				courseApproval.setCertificateExpirationPeriod(0);
				log.debug("Value in SetCertificationExpirationPeriod : 0");
			}
			
			
			courseApproval.setCustomFieldValues(this.saveCustomField(form.getCustomFields()));
			accreditationService.saveCourseApproval(courseApproval);
			
		}
		
	}
	
	/**
	 * This method saves course approval summary
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveCourseApprovalSummary( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = null;
		try{
			
			if( errors.hasErrors() ) {
				return new ModelAndView(courseApprovalSummaryTemplate);
			}else{
				context = new HashMap<Object, Object>();
				saveCourseApproval(command);
				context.put("courseApprovalPublishSuccess", "successMessage.courseApproval.saveCourseApproval.success.label");
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return new ModelAndView(courseApprovalSummaryTemplate, "context", context);
	}

	public ModelAndView publishAprpoval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		ApprovalForm form = (ApprovalForm)command;
		if(form.getEntity().equals(ApprovalForm.COURSE_APPROVAL)){
			if(!getLcmsClientWS().invokeCourseApprovalUpdated(getAccreditationService().getCourseApprovalById(form.getCourseApproval().getId()))){
				errors.reject("error.courseApproval.publish.failure", "");
			}
		}
		
		return new ModelAndView(courseApprovalSummaryTemplate);
	}
	
	public ModelAndView saveAndPublish( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		
		try{
			ApprovalForm form = (ApprovalForm)command;
			EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
			validator.validateCourseApprovalSummary(form, errors);

			if( errors.hasErrors() ) {
				return new ModelAndView(courseApprovalSummaryTemplate);
			}
			
			saveCourseApproval(command);
			
			if(form.getEntity().equals(ApprovalForm.COURSE_APPROVAL)){
				if(!getLcmsClientWS().invokeCourseApprovalUpdated(getAccreditationService().getCourseApprovalById(form.getCourseApproval().getId()))){
					errors.reject("error.courseApproval.publish.failure", "");
				}
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return new ModelAndView(courseApprovalSummaryTemplate);
	}
	
	
	public ModelAndView saveAndPublishCourseApprovalToSF( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		try{
			EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
			validator.validateCourseApprovalSummary(form, errors);

			if(form.getCourseApproval().getCourseGroups().size()<=0)
				errors.rejectValue("courseApproval.courseGroups", "error.custEntitlement.selectedCourseGroups.required","");
			
			if( errors.hasErrors() ) {
				return new ModelAndView(courseApprovalSummaryTemplate);
			}
			
			// SF will send -1 in ResponseCode & 'error' in ResponseMessage in case of issue in publish approval to SF
			// SF will send 1 in ResponseCode & 'success' in ResponseMessage in case of success in publish approval to SF
			
			if(form.getEntity().equals(ApprovalForm.COURSE_APPROVAL)){
				String strResponseMessage = lmsCourseApprovalClientToSF.publishCourseApproval(getAccreditationService().getCourseApprovalById(form.getCourseApproval().getId()));
				
				 if(strResponseMessage.equalsIgnoreCase("success"))
					 context.put("courseApprovalPublishSuccess", "successMessage.courseApproval.publishCourseApprovalToSF.label");
				 else 
					errors.reject("error.courseApproval.publish.failure", "");
				 
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			errors.reject("error.courseApproval.publish.failure", "");
		}
		
		return new ModelAndView(courseApprovalSummaryTemplate, "context", context);
	}

	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView unAssignCourseConfiguration( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		ApprovalForm form = (ApprovalForm)command;
		CourseApproval courseApproval = form.getCourseApproval();
		if (courseApproval != null) {
			courseApproval= accreditationService.loadForUpdateCourseApproval(courseApproval.getId());
			courseApproval.setTemplate(null);
			accreditationService.saveCourseApproval(courseApproval);
			form.setCourseApproval(courseApproval);
		}
		return new ModelAndView(courseApprovalSummaryTemplate);
	}
	private Date setTimeToDayend(Date date){		
		GregorianCalendar cal= new GregorianCalendar();
		cal.setTime(date);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
		cal.set(GregorianCalendar.MINUTE, 59);
		cal.set(GregorianCalendar.SECOND, 59);
		return cal.getTime();
	}
	
	/**
	 * This method save the custom field when the summary page is saved.
	 * @param customFields
	 * @return List<CustomFieldValue>
	 */
	public List<CustomFieldValue> saveCustomField ( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		if ( customFields.size() > 0 ) {
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFields){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (multiSelectCustomField.getCheckBox()){

						List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){

							if (customFieldValueChoice.isSelected()){
								CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
								customFieldValueChoices.add(customFieldValueChoiceRef);
							}
						}
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						customFieldValues.add(customFieldValue);
					} else {
						List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
						if( customField.getSelectedChoices() != null ) {
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
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						customFieldValues.add(customFieldValue);
					}
				} else {
					CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
					customFieldValue.setCustomField(customField.getCustomFieldRef());
					if( customFieldValue.getValue() != null ) {
						customFieldValue.setValue(HtmlEncoder.escapeHtmlFull(customFieldValue.getValue().toString()).toString());
						customFieldValues.add(customFieldValue);
					}
				}
			}
			if (customFieldValues.size()>0){
				return customFieldValues;
			}
		}
		return null;
	}

	/**
	 * For save provider approval summary
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveProviderApprovalSummary( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		if( errors.hasErrors() ) {
			return new ModelAndView(providerApprovalSummaryTemplate);
		}
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		ProviderApproval providerApproval = form.getProviderApproval();
		if (providerApproval != null) {
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) )
				providerApproval.setProviderApprovalEffectivelyStartDate(formatter.parse(form.getApprovalEffectivelyStartDate()));
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) )
				providerApproval.setCourseApprovalEffectivelyEndDate(this.setTimeToDayend(formatter.parse(form.getApprovalEffectivelyEndDate())));
			if ( !StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) )
				providerApproval.setProviderMostRecentlySubmittedForApprovalDate(formatter.parse(form.getMostRecentlySubmittedForApprovalDate()));
			if ( !StringUtils.isBlank(form.getOriginallyApprovedDate()) )
				providerApproval.setProviderOriginallyApprovedDate(formatter.parse(form.getOriginallyApprovedDate()));
			if ( !StringUtils.isBlank(form.getSubmissionReminderDate()) )
				providerApproval.setProviderSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));

			providerApproval.setCustomFieldValues(this.saveCustomField(form.getCustomFields()));
			accreditationService.saveProviderApproval(providerApproval);
		}

		return new ModelAndView(searchApprovalTemplate);
	}

	/**
	 * This method save instructor approval summary
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveInstructorApprovalSummary( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		if( errors.hasErrors() ) {
			return new ModelAndView(instructorApprovalSummaryTemplate);
		}
		ApprovalForm form = (ApprovalForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		InstructorApproval instructorApproval = form.getInstructorApproval();
		if (instructorApproval != null) {
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) )
				instructorApproval.setInstructorApprovalEffectivelyStartDate(formatter.parse(form.getApprovalEffectivelyStartDate()));
			if ( !StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) )
				instructorApproval.setInstructorApprovalEffectivelyEndsDate(this.setTimeToDayend(formatter.parse(form.getApprovalEffectivelyEndDate())));
			if ( !StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) )
				instructorApproval.setMostRecentlySubmittedForApprovalDate(formatter.parse(form.getMostRecentlySubmittedForApprovalDate()));
			if ( !StringUtils.isBlank(form.getOriginallyApprovedDate()) )
				instructorApproval.setInspectorOriginallyApprovalDate(formatter.parse(form.getOriginallyApprovedDate()));
			if ( !StringUtils.isBlank(form.getSubmissionReminderDate()) )
				instructorApproval.setInstructorSubmissionReminderDate(formatter.parse(form.getSubmissionReminderDate()));

			instructorApproval.setCustomFieldValues(this.saveCustomField(form.getCustomFields()));
			accreditationService.saveInstructorApproval(instructorApproval);
		}

		return new ModelAndView(searchApprovalTemplate);
	}
	

	public ModelAndView showApprovalRegulatorCategories( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		ApprovalForm form = (ApprovalForm)command;
		
		List<ApprovalRegulatorCategory> regulatorCategories = form.getRegulatorCategories();
		Map<Object, Object> context = new HashMap<Object, Object>();		
		//regulators = this.sortRegulators(request, regulators, context);	
		
		form.setRegulatorCategories(regulatorCategories);
		
		return new ModelAndView(courseApprovalRegulatorCategoryTemplate, "context", context);
	}

	
	/**
	 * This method is for sorting the regulator
	 * @param request
	 * @param regulators
	 * @param context
	 * @return List<Regulator>
	 * @throws Exception
	 */
	private List<Regulator> sortRegulators( HttpServletRequest request, List<Regulator> regulators, Map<Object, Object> context ) throws Exception {
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
//		session = request.getSession();
		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( regulators.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}
		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null ) {
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortColumnIndex") == null ) {
//			context.put("sortColumnIndex", "");
//		}
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null ) {
//			sortDirection = session.getAttribute("sortDirection").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortDirection") == null ) {
//			context.put("sortDirection", "");
//		}
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);

		if( sortColumnIndex != null && sortDirection != null ) {

			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			// sorting against regulator name
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					RegulatorSort RegulatorSort = new RegulatorSort();
					RegulatorSort.setSortBy("name");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					RegulatorSort RegulatorSort = new RegulatorSort();
					RegulatorSort.setSortBy("name");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against alias
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					RegulatorSort RegulatorSort = new RegulatorSort();
					RegulatorSort.setSortBy("alias");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					RegulatorSort RegulatorSort=new RegulatorSort();
					RegulatorSort.setSortBy("alias");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
				// sorting against email - address
			} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					RegulatorSort RegulatorSort=new RegulatorSort();
					RegulatorSort.setSortBy("emailAddress");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 2);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 2);
				} else {
					RegulatorSort RegulatorSort=new RegulatorSort();
					RegulatorSort.setSortBy("emailAddress");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 2);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 2);
				}
				// sorting against jurisdiction...
			} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					RegulatorSort RegulatorSort=new RegulatorSort();
					RegulatorSort.setSortBy("jurisdiction");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 3);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 2);
				} else {
					RegulatorSort RegulatorSort=new RegulatorSort();
					RegulatorSort.setSortBy("jurisdiction");
					RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(regulators,RegulatorSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 3);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 3);
				}
			}
		}
		return regulators;
	}
	

	/**
	 * Display the add provider page for Provider Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showProviderForProviderApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(providerApprovalProviderTemplate);
	}

	/**
	 * This method displays the add provider page for Instructor Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showProviderForInstructorApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(instructorApprovalProviderTemplate);
	}

	/**
	 * This method shows the document page for course approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showCourseApprovalDocuments( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		List<Document> documents = form.getDocuments();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		documents = this.sortDocument(request, documents, context);
		
		form.setDocuments(documents);
		
		return new ModelAndView(showCourseApprovalDocumentsTemplate, "context", context);
	}
	
	/**
	 * This method use for Archive CourseApproval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView archiveCourseApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(form.getAppId());
		courseApproval.setActive(false);
		form.setCourseApproval(courseApproval);
		accreditationService.saveCourseApproval(courseApproval);
		return new ModelAndView(courseApprovalSummaryTemplate);
	}
	
	
	/**
	 * This method use for unlink the Affidavit From CourseApproval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView unlinkAffidavitFromCourseApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(form.getAppId());
		courseApproval.setAffidavit(null);
		accreditationService.saveCourseApproval(courseApproval);
		form.getCourseApproval().setAffidavit(null);
		return new ModelAndView(courseApprovalSummaryTemplate);
	}
	
	
	public ModelAndView unlinkCertificateFromCourseApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(form.getAppId());
		courseApproval.setCertificate(null);
		accreditationService.saveCourseApproval(courseApproval);
		form.getCourseApproval().setCertificate(null);
		return new ModelAndView(courseApprovalSummaryTemplate);
	}

	public ModelAndView unlinkTemplateFromCourseApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(form.getAppId());
		courseApproval.setTemplate(null);
		accreditationService.saveCourseApproval(courseApproval);
		form.getCourseApproval().setTemplate(null);
		return new ModelAndView(courseApprovalSummaryTemplate);
	}

	/**
	 * This method shows the purchased certificate page for course approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showCourseApprovalPurchasedCertificate( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		List<PurchaseCertificateNumber> purchaseCertificateNumbers = form.getPurchaseCertificateNumbers();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		purchaseCertificateNumbers = this.sortPurchaseCertificateNumbers(request, purchaseCertificateNumbers, context);
		
		form.setPurchaseCertificateNumbers(purchaseCertificateNumbers);
		
		form.setTotalNumberOfUnusedPurchaseCertificateNumbers(this.getAccreditationService().getNumberOfUnusedPurchaseCertificateNumbers(form.getAppId()));
		context.put("certificates", purchaseCertificateNumbers);
		
		return new ModelAndView(courseApprovalPurchasedCertificateTemplate, "context", context);
	}
	

	/**
	 * Display the add document page for Provider Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showProviderApprovalDocuments( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		List<Document> documents = form.getDocuments();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		documents = this.sortDocument(request, documents, context);
		
		form.setDocuments(documents);
		
		return new ModelAndView(showProviderApprovalDocumentsTemplate, "context", context);
	}

	/**
	 * Display the add document page for Instructor Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showInstructorApprovalDocuments( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		List<Document> documents = form.getDocuments();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		documents = this.sortDocument(request, documents, context);
		
		form.setDocuments(documents);
		
		return new ModelAndView(showInstructorApprovalDocumentsTemplate, "context", context);
	}

	/**
	 * This method search document for course approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView searchCourseApprovalDocuments( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(showCourseApprovalDocumentsTemplate);
	}

	/**
	 * This method search document for provider approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView searchProviderApprovalDocuments( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(showProviderApprovalDocumentsTemplate);
	}
	
	private List<Document> sortDocument( HttpServletRequest request, List<Document> documents, Map<Object, Object> context ) throws Exception {
		
//		session = request.getSession();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";
		
		PagerAttributeMap.put("pageIndex",pageIndex);
		if( documents.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}
		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null ) {
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortColumnIndex") == null ) {
//			context.put("sortColumnIndex", "");
//		}
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null ) {
//			sortDirection = session.getAttribute("sortDirection").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortDirection") == null ) {
//			context.put("sortDirection", "");
//		}
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);
		
		if( sortColumnIndex != null && sortDirection != null ) {

			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			// sorting against fileName
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					DocumentSort documentSort = new DocumentSort();
					documentSort.setSortBy("name");
					documentSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(documents,documentSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					DocumentSort documentSort = new DocumentSort();
					documentSort.setSortBy("name");
					documentSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(documents,documentSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against description
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					DocumentSort documentSort = new DocumentSort();
					documentSort.setSortBy("description");
					documentSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(documents,documentSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					DocumentSort documentSort = new DocumentSort();
					documentSort.setSortBy("description");
					documentSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(documents,documentSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		return documents;
	}
	
	private List<PurchaseCertificateNumber> sortPurchaseCertificateNumbers( HttpServletRequest request, List<PurchaseCertificateNumber> purchaseCertificateNumbers, Map<Object, Object> context ) throws Exception {
		
//		session = request.getSession();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";
		
		PagerAttributeMap.put("pageIndex",pageIndex);
		if( purchaseCertificateNumbers.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}
		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		String sortDirection = request.getParameter("sortDirection");
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);
		
		if( sortColumnIndex != null && sortDirection != null ) {

			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			// sorting against fileName
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					PurchaseCertificateNumberSort purchaseCertificateNumberSort = new PurchaseCertificateNumberSort();
					purchaseCertificateNumberSort.setSortBy("name");
					purchaseCertificateNumberSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(purchaseCertificateNumbers,purchaseCertificateNumberSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					PurchaseCertificateNumberSort purchaseCertificateNumberSort = new PurchaseCertificateNumberSort();
					purchaseCertificateNumberSort.setSortBy("name");
					purchaseCertificateNumberSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(purchaseCertificateNumbers,purchaseCertificateNumberSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
				// sorting against description
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					PurchaseCertificateNumberSort purchaseCertificateNumberSort = new PurchaseCertificateNumberSort();
					purchaseCertificateNumberSort.setSortBy("name");
					purchaseCertificateNumberSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(purchaseCertificateNumbers,purchaseCertificateNumberSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					PurchaseCertificateNumberSort purchaseCertificateNumberSort = new PurchaseCertificateNumberSort();
					purchaseCertificateNumberSort.setSortBy("name");
					purchaseCertificateNumberSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(purchaseCertificateNumbers,purchaseCertificateNumberSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			}
			else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					PurchaseCertificateNumberSort purchaseCertificateNumberSort = new PurchaseCertificateNumberSort();
					purchaseCertificateNumberSort.setSortBy("used");
					purchaseCertificateNumberSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(purchaseCertificateNumbers,purchaseCertificateNumberSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 2);
				} else {
					PurchaseCertificateNumberSort purchaseCertificateNumberSort = new PurchaseCertificateNumberSort();
					purchaseCertificateNumberSort.setSortBy("used");
					purchaseCertificateNumberSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(purchaseCertificateNumbers,purchaseCertificateNumberSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 2);
				}
			}
		}
		return purchaseCertificateNumbers;
	}

	/**
	 * For edit Approval documents.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView editApprovalDocuments( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		ApprovalForm form = (ApprovalForm)command;
		if(request.getParameter("docId") != null) {
			String docId = request.getParameter("docId");
			Document doc = accreditationService.loadForUpdateDocument(Long.parseLong(docId)); // LMS-11548 change 
			form.setDocument(doc);
		}
		return new ModelAndView(editApprovalDocumentsTemplate);
	}

	/**
	 * Added by Dyutiman
	 * method to download the document & save in local computer
	 */
	public ModelAndView downloadDocument( HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors ) throws Exception {

		log.debug("IN DOWNLOAD");
		String filepath = "";
		ApprovalForm form = (ApprovalForm)command;
		String documentPath = VU360Properties.getVU360Property("document.saveLocation");
		if (form.getDocEntity().equalsIgnoreCase(form.COURSE_DOCUMENT)) {
			filepath = documentPath+"/Course/"+form.getCourseApproval().getId().toString()+"/"+
			form.getDocument().getId().toString();
		} else if(form.getDocEntity().equalsIgnoreCase(form.PROVIDER_DOCUMENT)) {
			filepath = documentPath+"/Provider/"+form.getProviderApproval().getId().toString()+"/"+
			form.getDocument().getId().toString();
		} else if(form.getDocEntity().equalsIgnoreCase(form.INSTRUCTOR_DOCUMENT)) {
			filepath = documentPath+"/Instructor/"+form.getInstructorApproval().getId().toString()+"/"+
			form.getDocument().getId().toString();
		}

		String path = filepath+"_"+form.getDocument().getFileName();
		try {
			File f = new File(path);
			FileInputStream fis = new FileInputStream(f);
			byte[] filedata = null;
			filedata = new byte[(int)f.length()];
			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < filedata.length
					&& (numRead = fis.read(filedata, offset, filedata.length-offset)) >= 0) {
				offset += numRead;
			}
			fis.read();
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength((int)f.length());
			response.setHeader("Content-Disposition", "inline; filename="+form.getDocument().getFileName());
			ServletOutputStream outputStream = response.getOutputStream();

			outputStream.write(filedata);
			outputStream.flush();
			outputStream.close();

		} catch( FileNotFoundException e ) {
			log.debug("FILE NOT FOUND");
		}
		return new ModelAndView(editApprovalDocumentsTemplate);
	}


	/**
	 * This method saves the Approval document after edit.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveDocument( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<Document> modifiedDocs = new ArrayList <Document>();

		Document doc = form.getDocument();
		accreditationService.saveDocument(doc);
		if(form.getDocEntity().equalsIgnoreCase(form.COURSE_DOCUMENT)) {
			CourseApproval cApproval = form.getCourseApproval();
			List<Document> presentDocs = cApproval.getDocuments();
			for( Document presentDoc : presentDocs ) {
				if( presentDoc.getId().equals(doc.getId())) {
					modifiedDocs.add(doc);
				} else {
					modifiedDocs.add(presentDoc);
				}
			}
			cApproval.setDocuments(modifiedDocs);
			accreditationService.saveCourseApproval(cApproval);

			context.put("target", "showCourseApprovalDocuments");
		} else if(form.getDocEntity().equalsIgnoreCase(form.PROVIDER_DOCUMENT)) {
			ProviderApproval pApproval = form.getProviderApproval();
			List<Document> presentDocs = pApproval.getDocuments();
			for( Document presentDoc : presentDocs ) {
				if( presentDoc.getId().equals(doc.getId())) {
					modifiedDocs.add(doc);
				} else {
					modifiedDocs.add(presentDoc);
				}
			}
			pApproval.setDocuments(modifiedDocs);
			accreditationService.saveProviderApproval(pApproval);

			context.put("target", "showProviderApprovalDocuments");
		} else if(form.getDocEntity().equalsIgnoreCase(form.INSTRUCTOR_DOCUMENT)) {
			InstructorApproval iApproval = form.getInstructorApproval();
			List<Document> presentDocs = iApproval.getDocuments();
			for( Document presentDoc : presentDocs ) {
				if( presentDoc.getId().equals(doc.getId())) {
					modifiedDocs.add(doc);
				} else {
					modifiedDocs.add(presentDoc);
				}
			}
			iApproval.setDocuments(modifiedDocs);
			accreditationService.saveInstructorApproval(iApproval);

			context.put("target", "showInstructorApprovalDocuments");
		}

		return new ModelAndView(redirectTemplate, "context", context);
	}

	public ModelAndView cancelEditDocument( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();

		if(form.getDocEntity().equalsIgnoreCase(form.COURSE_DOCUMENT)) {
			context.put("target", "showCourseApprovalDocuments");
		} else if(form.getDocEntity().equalsIgnoreCase(form.PROVIDER_DOCUMENT)) {
			context.put("target", "showProviderApprovalDocuments");
		} else if(form.getDocEntity().equalsIgnoreCase(form.INSTRUCTOR_DOCUMENT)) {
			context.put("target", "showInstructorApprovalDocuments");
		}
		return new ModelAndView(redirectTemplate, "context", context);
	}
	/**
	 * Display the add custom field page for Course Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showCourseApprovalCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getCourseApproval().getCustomFields() != null && form.getCourseApproval().getCustomFields().size() > 0) {
			for (CustomField customField : form.getCourseApproval().getCustomFields()) {
				ManageCustomField manageCustomField = new ManageCustomField();
				manageCustomField.setFieldName(customField.getFieldLabel());
				manageCustomField.setId(customField.getId());
				if (customField instanceof SingleLineTextCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
				} else if (customField instanceof DateTimeCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_DATE);
				} else if (customField instanceof MultipleLineTextCustomfield) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
				} else if (customField instanceof NumericCusomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_NUMBER);
				} else if (customField instanceof SSNCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
				} else if (customField instanceof SingleSelectCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
				} else if (customField instanceof MultiSelectCustomField) {
					if (((MultiSelectCustomField) customField).getCheckBox())
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
					else 
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHOOSE);
				}
				manageCustomFieldList.add(manageCustomField);
			}
			
			manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);
			
			form.setManageCustomField(manageCustomFieldList);
		} else {
			form.setManageCustomField(manageCustomFieldList);
		}

		return new ModelAndView(courseApprovalCustomFieldTemplate, "context", context);
	}

	/**
	 * Display the add custom field page for Provider Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showProviderApprovalCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getProviderApproval().getCustomFields() != null && form.getProviderApproval().getCustomFields().size() > 0) {
			for (CustomField customField : form.getProviderApproval().getCustomFields()) {
				ManageCustomField manageCustomField = new ManageCustomField();
				manageCustomField.setFieldName(customField.getFieldLabel());
				manageCustomField.setId(customField.getId());
				if (customField instanceof SingleLineTextCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
				} else if (customField instanceof DateTimeCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_DATE);
				} else if (customField instanceof MultipleLineTextCustomfield) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
				} else if (customField instanceof NumericCusomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_NUMBER);
				} else if (customField instanceof SSNCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
				} else if (customField instanceof SingleSelectCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
				} else if (customField instanceof MultiSelectCustomField) {
					if (((MultiSelectCustomField) customField).getCheckBox())
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
					else 
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHOOSE);
				}
				manageCustomFieldList.add(manageCustomField);
			}
			
			manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);
			
			form.setManageCustomField(manageCustomFieldList);
		} else {
			form.setManageCustomField(manageCustomFieldList);
		}

		return new ModelAndView(providerApprovalCustomFieldTemplate, "context", context);
	}

	/**
	 * Display the add custom field page for Instructor Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showInstructorApprovalCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getInstructorApproval().getCustomFields() != null && form.getInstructorApproval().getCustomFields().size() > 0) {
			for (CustomField customField : form.getInstructorApproval().getCustomFields()) {
				ManageCustomField manageCustomField = new ManageCustomField();
				manageCustomField.setFieldName(customField.getFieldLabel());
				manageCustomField.setId(customField.getId());
				if (customField instanceof SingleLineTextCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
				} else if (customField instanceof DateTimeCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_DATE);
				} else if (customField instanceof MultipleLineTextCustomfield) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
				} else if (customField instanceof NumericCusomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_NUMBER);
				} else if (customField instanceof SSNCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
				} else if (customField instanceof SingleSelectCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
				} else if (customField instanceof MultiSelectCustomField) {
					if (((MultiSelectCustomField) customField).getCheckBox())
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
					else 
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHOOSE);
				}
				manageCustomFieldList.add(manageCustomField);
			}
			
			manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);
			
			form.setManageCustomField(manageCustomFieldList);
		} else {
			form.setManageCustomField(manageCustomFieldList);
		}

		return new ModelAndView(instructorApprovalCustomFieldTemplate, "context", context);
	}
	
	/**
	 * This method is for sorting the custom field.
	 * @param request
	 * @param manageCustomFieldList
	 * @param context
	 * @return List<ManageCustomField>
	 * @throws Exception
	 */
	private List<ManageCustomField> sortCustomField( HttpServletRequest request, List<ManageCustomField> manageCustomFieldList, Map<Object, Object> context ) throws Exception {
		
//		session = request.getSession();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();

		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( manageCustomFieldList.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}

		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null ) {
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortColumnIndex") == null ) {
//			context.put("sortColumnIndex", "");
//		}
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null ) {
//			sortDirection = session.getAttribute("sortDirection").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortDirection") == null ) {
//			context.put("sortDirection", "");
//		}
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);

		// sorting against officialLicenseName name
		if( sortColumnIndex != null && sortDirection != null ) {
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldLabel");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldLabel");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against jurisdiction
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldType");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldType");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		return manageCustomFieldList;
	}

	/**
	 * This method shows the instructor for Instructor Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showInstructorApprovalInstructor( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(instructorApprovalInstructorTemplate);
	}

	/**
	 * This method shows the Course for Instructor Approval
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showInstructorApprovalCourse( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(instructorApprovalCourseTemplate);
	}

	/**
	 * This method shows the Requirements for Course Approval 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showCourseApprovalRequirement( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		ApprovalForm form = (ApprovalForm)command;
		List <CredentialCategoryRequirement> requirements = form.getCourseApproval().getRequirements();
		Set<Credential> credentialSet = new HashSet<Credential>();
		for (CredentialCategoryRequirement cReq : requirements) {
			credentialSet.add(cReq.getCredentialCategory().getCredential()); // LMS-8314 : Need to rework on this
		}
		List<ApprovalCredential> appCreds = new ArrayList<ApprovalCredential>();
		List<ApprovalRequirement> approvalRequirement;

		for( Credential cred : credentialSet ) {
			ApprovalCredential appCred = new ApprovalCredential();
			List <CredentialCategoryRequirement> rqurmnts = accreditationService.getCredentialCategoryRequirementsByCredential(cred.getId());
			if( rqurmnts != null ) {
				approvalRequirement = new ArrayList <ApprovalRequirement>();
				for( CredentialCategoryRequirement cr : rqurmnts ) {
					for ( CredentialCategoryRequirement credReq : requirements )
						if (cr.getId().compareTo(credReq.getId()) == 0) {
							ApprovalRequirement ar = new ApprovalRequirement();
							ar.setRequirement(cr);
							ar.setSelected(false);
							approvalRequirement.add(ar);
						}
				}
				appCred.setRequirements(approvalRequirement);
			}
			appCred.setCredential(cred);
			appCred.setSelected(false);
			appCreds.add(appCred);
		}
		form.setApprovalCredential(appCreds);

		return new ModelAndView(courserApprovalRequirementTemplate);
	}
		
	public ModelAndView deleteRegulatorCategories( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedRegulatorCategoryId = request.getParameterValues("regulatorCategory");
		CourseApproval courseApproval= null;
		ProviderApproval providerApproval= null;
		InstructorApproval instructorApproval= null;
		if(form.getEntity().equalsIgnoreCase(ApprovalForm.COURSE_APPROVAL)){
			courseApproval= form.getCourseApproval();
		}else if(form.getEntity().equalsIgnoreCase(ApprovalForm.PROVIDER_APPROVAL)){
			providerApproval= form.getProviderApproval();
		}else{ 
			instructorApproval= form.getInstructorApproval();				
		}
		List<ApprovalRegulatorCategory > regulatorCategories = form.getRegulatorCategories();
		List<ApprovalRegulatorCategory > deleteCandidates = new ArrayList<ApprovalRegulatorCategory>();
		for (int i=0 ; i < selectedRegulatorCategoryId.length ;i++) {
			for (int j=0 ; j < regulatorCategories.size(); j++) {
				if (Long.valueOf(regulatorCategories.get(j).getCategory().getId()).compareTo(Long.parseLong(selectedRegulatorCategoryId[i])) == 0) {
					//regulatorCategories.remove(j);
					deleteCandidates.add(regulatorCategories.get(j));
					break;
				}
			}
		}
		regulatorCategories.removeAll(deleteCandidates);
		List<RegulatorCategory > categories= new ArrayList<RegulatorCategory>();
		for(ApprovalRegulatorCategory cat:regulatorCategories){
			categories.add(cat.getCategory());	
		}
		form.setRegulatorCategories(regulatorCategories);
		
		if(form.getEntity().equalsIgnoreCase(ApprovalForm.COURSE_APPROVAL)){
			courseApproval.setRegulatorCategories(categories);
			accreditationService.saveCourseApproval(courseApproval);
		}else if(form.getEntity().equalsIgnoreCase(ApprovalForm.PROVIDER_APPROVAL)){
			providerApproval.setRegulatorCategories(categories);
			accreditationService.saveProviderApproval(providerApproval);
		}else{ 
			instructorApproval.setRegulatorCategories(categories);
		    accreditationService.saveInstructorApproval(instructorApproval);
		}
		
		return showApprovalRegulatorCategories(request, response, command, errors);
		
	}
	/**
	 * This method delete the Provider Approval document.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteProviderDocument( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedDocumentId = request.getParameterValues("document");
		ProviderApproval providerApproval = form.getProviderApproval();
		List<Document> documents = providerApproval.getDocuments();
		for (int i=0 ; i < selectedDocumentId.length ;i++) {
			for (int j=0 ; j < documents.size(); j++) {
				if (documents.get(j).getId().compareTo(Long.parseLong(selectedDocumentId[i])) == 0) {
					documents.remove(j);
					break;
				}
			}
		}
		providerApproval.setDocuments(documents);
		accreditationService.saveProviderApproval(providerApproval);

		context.put("target", "showProviderApprovalDocuments");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	/**
	 * This method delete the Provider Approval custom field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteProviderCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		ProviderApproval providerApproval = form.getProviderApproval();
		List<CustomField> customFields = providerApproval.getCustomFields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		providerApproval.setCustomFields(customFields);
		accreditationService.saveProviderApproval(providerApproval);

		context.put("target", "showProviderApprovalCustomField");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	/**
	 * This method delete the Course Approval document.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteCourseDocument( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedDocumentId = request.getParameterValues("document");
		CourseApproval courseApproval = form.getCourseApproval();
		List<Document> documents = courseApproval.getDocuments();
		for (int i=0 ; i < selectedDocumentId.length ;i++) {
			for (int j=0 ; j < documents.size(); j++) {
				if (documents.get(j).getId().compareTo(Long.parseLong(selectedDocumentId[i])) == 0) {
					documents.remove(j);
					break;
				}
			}
		}
		courseApproval.setDocuments(documents);
		accreditationService.saveCourseApproval(courseApproval);

		context.put("target", "showCourseApprovalDocuments");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	/**
	 * This method delete the Course Approval custom field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteCouseCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		CourseApproval courseApproval = form.getCourseApproval();
		List<CustomField> customFields = courseApproval.getCustomFields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		courseApproval.setCustomFields(customFields);
		accreditationService.saveCourseApproval(courseApproval);

		context.put("target", "showCourseApprovalCustomField");
		return new ModelAndView(redirectTemplate, "context", context);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteCertificateNumbers( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		List<PurchaseCertificateNumber> certificateNumbersToBeDeleted = null;
		String[] selectedCertificateNumberId = request.getParameterValues("certificateNumbers");
		CourseApproval courseApproval = form.getCourseApproval();
		List<PurchaseCertificateNumber> certificateNumbers = courseApproval.getPurchaseCertificateNumbers();
		
		if(certificateNumbers != null && selectedCertificateNumberId != null && selectedCertificateNumberId.length > 0)
		{
			certificateNumbersToBeDeleted = new ArrayList<PurchaseCertificateNumber>();
			for (int i=0 ; i < selectedCertificateNumberId.length ;i++) {
				for (int j=0 ; j < certificateNumbers.size(); j++) {
					if (certificateNumbers.get(j).getId().compareTo(Long.parseLong(selectedCertificateNumberId[i])) == 0 && !certificateNumbers.get(j).isUsed()) {
						certificateNumbersToBeDeleted.add(certificateNumbers.get(j));
						certificateNumbers.remove(j);
						break;
					}
				}
			}
		}
		
		courseApproval.setPurchaseCertificateNumbers(certificateNumbers);
		accreditationService.saveCourseApproval(courseApproval);
		//accreditationService.deletePurchaseCertificateNumbers(certificateNumbersToBeDeleted);

		context.put("target", "showCourseApprovalPurchasedCertificate");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	public ModelAndView deleteCourseRequirement( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedRequirementId = request.getParameterValues("requirement");
		CourseApproval courseApproval = form.getCourseApproval();
		List<CredentialCategoryRequirement> requirements = courseApproval.getRequirements();
		for (int i=0 ; i < selectedRequirementId.length ;i++) {
			for (int j=0 ; j < requirements.size(); j++) {
				if (requirements.get(j).getId().compareTo(Long.parseLong(selectedRequirementId[i])) == 0) {
					requirements.remove(j);
					break;
				}
			}
		}
		courseApproval.setRequirements(requirements);
		accreditationService.saveCourseApproval(courseApproval);

		context.put("target", "showCourseApprovalRequirement");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	/**
	 * This method delete the Instructor Approval document.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteInstructorDocument( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedDocumentId = request.getParameterValues("document");
		InstructorApproval instructorApproval = form.getInstructorApproval();
		List<Document> documents = instructorApproval.getDocuments();
		for (int i=0 ; i < selectedDocumentId.length ;i++) {
			for (int j=0 ; j < documents.size(); j++) {
				if (documents.get(j).getId().compareTo(Long.parseLong(selectedDocumentId[i])) == 0) {
					documents.remove(j);
					break;
				}
			}
		}
		instructorApproval.setDocuments(documents);
		accreditationService.saveInstructorApproval(instructorApproval);

		context.put("target", "showInstructorApprovalDocuments");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	/**
	 * This method delete the Instructor Approval custom field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteInstructorCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ApprovalForm form = (ApprovalForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		InstructorApproval instructorApproval = form.getInstructorApproval();
		List<CustomField> customFields = instructorApproval.getCustomFields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		instructorApproval.setCustomFields(customFields);
		accreditationService.saveInstructorApproval(instructorApproval);

		context.put("target", "showInstructorApprovalCustomField");
		return new ModelAndView(redirectTemplate, "context", context);
	}



	public ModelAndView changeCourse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try{
			ApprovalForm form = (ApprovalForm)command;
			
			if(getAccreditationService().isCourseApprovalLinkedWithLearnerEnrollment(form.getAppId())){
				errors.rejectValue("courseApproval.course", "error.approval.course.change.enrollments.exists","");
			}else{			
				return new ModelAndView(getRedirectChangeCourse() +"?entity=Course&approvalId="+ form.getAppId());
			}
		
		}catch(Exception e){
			log.error(e.getMessage(), e);
			errors.rejectValue("courseApproval.course", "error.approval.course.change.requestProcessingError", "");
		}
		
		return new ModelAndView(getCourseApprovalSummaryTemplate());

	}
		
	public ModelAndView changeRegulatorCategory(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try{
			ApprovalForm form = (ApprovalForm)command;
			
			if(getAccreditationService().isCourseApprovalLinkedWithLearnerEnrollment(form.getAppId())){
				errors.rejectValue("regulatorCategoryName", "error.approval.regulatorCategory.change.enrollments.exists","");
			}else{
				return new ModelAndView(getRedirectChangeRegulatorCategory() + "?entity=Course&approvalId=" + form.getAppId());
			}
		
		}catch(Exception e){
			log.error(e.getMessage(), e);
			errors.rejectValue("regulatorCategoryName", "error.approval.regulatorCategory.change.requestProcessingError");
		}
		
		return new ModelAndView(getCourseApprovalSummaryTemplate());

	}


	public String getSearchApprovalTemplate() {
		return searchApprovalTemplate;
	}
	public void setSearchApprovalTemplate(String searchApprovalTemplate) {
		this.searchApprovalTemplate = searchApprovalTemplate;
	}
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the providerApprovalSummaryTemplate
	 */
	public String getProviderApprovalSummaryTemplate() {
		return providerApprovalSummaryTemplate;
	}

	/**
	 * @param providerApprovalSummaryTemplate the providerApprovalSummaryTemplate to set
	 */
	public void setProviderApprovalSummaryTemplate(
			String providerApprovalSummaryTemplate) {
		this.providerApprovalSummaryTemplate = providerApprovalSummaryTemplate;
	}

	/**
	 * @return the providerApprovalRegulatorTemplate
	 */
	public String getProviderApprovalRegulatorTemplate() {
		return providerApprovalRegulatorTemplate;
	}

	/**
	 * @param providerApprovalRegulatorTemplate the providerApprovalRegulatorTemplate to set
	 */
	public void setProviderApprovalRegulatorTemplate(
			String providerApprovalRegulatorTemplate) {
		this.providerApprovalRegulatorTemplate = providerApprovalRegulatorTemplate;
	}

	/**
	 * @return the providerApprovalCustomFieldTemplate
	 */
	public String getProviderApprovalCustomFieldTemplate() {
		return providerApprovalCustomFieldTemplate;
	}

	/**
	 * @param providerApprovalCustomFieldTemplate the providerApprovalCustomFieldTemplate to set
	 */
	public void setProviderApprovalCustomFieldTemplate(
			String providerApprovalCustomFieldTemplate) {
		this.providerApprovalCustomFieldTemplate = providerApprovalCustomFieldTemplate;
	}

	/**
	 * @return the showProviderApprovalDocumentsTemplate
	 */
	public String getShowProviderApprovalDocumentsTemplate() {
		return showProviderApprovalDocumentsTemplate;
	}

	/**
	 * @param showProviderApprovalDocumentsTemplate the showProviderApprovalDocumentsTemplate to set
	 */
	public void setShowProviderApprovalDocumentsTemplate(
			String showProviderApprovalDocumentsTemplate) {
		this.showProviderApprovalDocumentsTemplate = showProviderApprovalDocumentsTemplate;
	}

	/**
	 * @return the instructorApprovalSummaryTemplate
	 */
	public String getInstructorApprovalSummaryTemplate() {
		return instructorApprovalSummaryTemplate;
	}

	/**
	 * @param instructorApprovalSummaryTemplate the instructorApprovalSummaryTemplate to set
	 */
	public void setInstructorApprovalSummaryTemplate(
			String instructorApprovalSummaryTemplate) {
		this.instructorApprovalSummaryTemplate = instructorApprovalSummaryTemplate;
	}


	/**
	 * @return the instructorApprovalRegulatorTemplate
	 */
	public String getInstructorApprovalRegulatorTemplate() {
		return instructorApprovalRegulatorTemplate;
	}

	/**
	 * @param instructorApprovalRegulatorTemplate the instructorApprovalRegulatorTemplate to set
	 */
	public void setInstructorApprovalRegulatorTemplate(
			String instructorApprovalRegulatorTemplate) {
		this.instructorApprovalRegulatorTemplate = instructorApprovalRegulatorTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	/**
	 * @return the showInstructorApprovalDocumentsTemplate
	 */
	public String getShowInstructorApprovalDocumentsTemplate() {
		return showInstructorApprovalDocumentsTemplate;
	}

	/**
	 * @param showInstructorApprovalDocumentsTemplate the showInstructorApprovalDocumentsTemplate to set
	 */
	public void setShowInstructorApprovalDocumentsTemplate(
			String showInstructorApprovalDocumentsTemplate) {
		this.showInstructorApprovalDocumentsTemplate = showInstructorApprovalDocumentsTemplate;
	}

	/**
	 * @return the instructorApprovalCustomFieldTemplate
	 */
	public String getInstructorApprovalCustomFieldTemplate() {
		return instructorApprovalCustomFieldTemplate;
	}

	/**
	 * @param instructorApprovalCustomFieldTemplate the instructorApprovalCustomFieldTemplate to set
	 */
	public void setInstructorApprovalCustomFieldTemplate(
			String instructorApprovalCustomFieldTemplate) {
		this.instructorApprovalCustomFieldTemplate = instructorApprovalCustomFieldTemplate;
	}

	/**
	 * @return the providerApprovalProviderTemplate
	 */
	public String getProviderApprovalProviderTemplate() {
		return providerApprovalProviderTemplate;
	}

	/**
	 * @param providerApprovalProviderTemplate the providerApprovalProviderTemplate to set
	 */
	public void setProviderApprovalProviderTemplate(
			String providerApprovalProviderTemplate) {
		this.providerApprovalProviderTemplate = providerApprovalProviderTemplate;
	}

	/**
	 * @return the instructorApprovalInstructorTemplate
	 */
	public String getInstructorApprovalInstructorTemplate() {
		return instructorApprovalInstructorTemplate;
	}

	/**
	 * @param instructorApprovalInstructorTemplate the instructorApprovalInstructorTemplate to set
	 */
	public void setInstructorApprovalInstructorTemplate(
			String instructorApprovalInstructorTemplate) {
		this.instructorApprovalInstructorTemplate = instructorApprovalInstructorTemplate;
	}

	/**
	 * @return the instructorApprovalProviderTemplate
	 */
	public String getInstructorApprovalProviderTemplate() {
		return instructorApprovalProviderTemplate;
	}

	/**
	 * @param instructorApprovalProviderTemplate the instructorApprovalProviderTemplate to set
	 */
	public void setInstructorApprovalProviderTemplate(
			String instructorApprovalProviderTemplate) {
		this.instructorApprovalProviderTemplate = instructorApprovalProviderTemplate;
	}

	/**
	 * @return the instructorApprovalCourseTemplate
	 */
	public String getInstructorApprovalCourseTemplate() {
		return instructorApprovalCourseTemplate;
	}

	/**
	 * @param instructorApprovalCourseTemplate the instructorApprovalCourseTemplate to set
	 */
	public void setInstructorApprovalCourseTemplate(
			String instructorApprovalCourseTemplate) {
		this.instructorApprovalCourseTemplate = instructorApprovalCourseTemplate;
	}

	/**
	 * @return the courseApprovalSummaryTemplate
	 */
	public String getCourseApprovalSummaryTemplate() {
		return courseApprovalSummaryTemplate;
	}

	/**
	 * @param courseApprovalSummaryTemplate the courseApprovalSummaryTemplate to set
	 */
	public void setCourseApprovalSummaryTemplate(
			String courseApprovalSummaryTemplate) {
		this.courseApprovalSummaryTemplate = courseApprovalSummaryTemplate;
	}

	/**
	 * @return the courseApprovalRegulatorTemplate
	 */
	public String getCourseApprovalRegulatorTemplate() {
		return courseApprovalRegulatorTemplate;
	}

	/**
	 * @param courseApprovalRegulatorTemplate the courseApprovalRegulatorTemplate to set
	 */
	public void setCourseApprovalRegulatorTemplate(
			String courseApprovalRegulatorTemplate) {
		this.courseApprovalRegulatorTemplate = courseApprovalRegulatorTemplate;
	}

	/**
	 * @return the showCourseApprovalDocumentsTemplate
	 */
	public String getShowCourseApprovalDocumentsTemplate() {
		return showCourseApprovalDocumentsTemplate;
	}

	/**
	 * @param showCourseApprovalDocumentsTemplate the showCourseApprovalDocumentsTemplate to set
	 */
	public void setShowCourseApprovalDocumentsTemplate(
			String showCourseApprovalDocumentsTemplate) {
		this.showCourseApprovalDocumentsTemplate = showCourseApprovalDocumentsTemplate;
	}

	/**
	 * @return the courseApprovalCustomFieldTemplate
	 */
	public String getCourseApprovalCustomFieldTemplate() {
		return courseApprovalCustomFieldTemplate;
	}

	/**
	 * @param courseApprovalCustomFieldTemplate the courseApprovalCustomFieldTemplate to set
	 */
	public void setCourseApprovalCustomFieldTemplate(
			String courseApprovalCustomFieldTemplate) {
		this.courseApprovalCustomFieldTemplate = courseApprovalCustomFieldTemplate;
	}

	/**
	 * @return the courserApprovalRequirementTemplate
	 */
	public String getCourserApprovalRequirementTemplate() {
		return courserApprovalRequirementTemplate;
	}

	/**
	 * @param courserApprovalRequirementTemplate the courserApprovalRequirementTemplate to set
	 */
	public void setCourserApprovalRequirementTemplate(
			String courserApprovalRequirementTemplate) {
		this.courserApprovalRequirementTemplate = courserApprovalRequirementTemplate;
	}

	/**
	 * @return the courserApprovalReportingFieldTemplate
	 */
	public String getCourserApprovalReportingFieldTemplate() {
		return courserApprovalReportingFieldTemplate;
	}

	/**
	 * @param courserApprovalReportingFieldTemplate the courserApprovalReportingFieldTemplate to set
	 */
	public void setCourserApprovalReportingFieldTemplate(
			String courserApprovalReportingFieldTemplate) {
		this.courserApprovalReportingFieldTemplate = courserApprovalReportingFieldTemplate;
	}

	/**
	 * @return the editApprovalDocumentsTemplate
	 */
	public String getEditApprovalDocumentsTemplate() {
		return editApprovalDocumentsTemplate;
	}

	/**
	 * @param editApprovalDocumentsTemplate the editApprovalDocumentsTemplate to set
	 */
	public void setEditApprovalDocumentsTemplate(
			String editApprovalDocumentsTemplate) {
		this.editApprovalDocumentsTemplate = editApprovalDocumentsTemplate;
	}

	public String getListCreditReportingFieldTemplate() {
		return listCreditReportingFieldTemplate;
	}

	public void setListCreditReportingFieldTemplate(
			String listCreditReportingFieldTemplate) {
		this.listCreditReportingFieldTemplate = listCreditReportingFieldTemplate;
	}

	public String getCourseApprovalRegulatorCategoryTemplate() {
		return courseApprovalRegulatorCategoryTemplate;
	}

	public void setCourseApprovalRegulatorCategoryTemplate(
			String courseApprovalRegulatorCategoryTemplate) {
		this.courseApprovalRegulatorCategoryTemplate = courseApprovalRegulatorCategoryTemplate;
	}

	/**
	 * @return the courseApprovalPurchasedCertificateTemplate
	 */
	public String getCourseApprovalPurchasedCertificateTemplate() {
		return courseApprovalPurchasedCertificateTemplate;
	}

	/**
	 * @param courseApprovalPurchasedCertificateTemplate the courseApprovalPurchasedCertificateTemplate to set
	 */
	public void setCourseApprovalPurchasedCertificateTemplate(
			String courseApprovalPurchasedCertificateTemplate) {
		this.courseApprovalPurchasedCertificateTemplate = courseApprovalPurchasedCertificateTemplate;
	}

	public LCMSClientWS getLcmsClientWS() {
		return lcmsClientWS;
	}

	public void setLcmsClientWS(LCMSClientWS lcmsClientWS) {
		this.lcmsClientWS = lcmsClientWS;
	}

	public String getRedirectChangeCourse() {
		return redirectChangeCourse;
	}

	public void setRedirectChangeCourse(String redirectChangeCourse) {
		this.redirectChangeCourse = redirectChangeCourse;
	}

	public String getRedirectChangeRegulatorCategory() {
		return redirectChangeRegulatorCategory;
	}

	public void setRedirectChangeRegulatorCategory(
			String redirectChangeRegulatorCategory) {
		this.redirectChangeRegulatorCategory = redirectChangeRegulatorCategory;
	}

	public LMSCourseApprovalPublishWSClient getLmsCourseApprovalClientToSF() {
		return lmsCourseApprovalClientToSF;
	}

	public void setLmsCourseApprovalClientToSF(
			LMSCourseApprovalPublishWSClient lmsCourseApprovalClientToSF) {
		this.lmsCourseApprovalClientToSF = lmsCourseApprovalClientToSF;
	}
	
	
	
	
	
}