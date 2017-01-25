package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialProctors;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CredentialValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.CredentialProctorSort;
import com.softech.vu360.util.CredentialSort;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ProctorStatusEnum;
import com.softech.vu360.util.TreeNode;

public class ManageCredentialController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(ManageCredentialController.class.getName());
	
	private AccreditationService accreditationService = null;
	private ProctorService proctorService = null;
	private LearnerService learnerService = null;
	
	private static final String MANAGE_CREDENTIAL_PAGING_ACTION = "paging";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALS = "CUSTOMFIELD_CREDENTIAL";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT = "CUSTOMFIELD_CREDENTIALREQUIREMENT";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY = "CUSTOMFIELD_CREDENTIALCATEGORY";
	private final String ACTION_SHOW_ALL = "showAll";
	private final String ACTION_SHOW_NEXT = "showNext";
	private final String ACTION_SHOW_PREV = "showPrev";
	private final String ACTION_SORT = "sort";
	private final String ACTION_DELETE = "delete";
	private final int DEFAULT_PAGE_SIZE = 10;

	private String defaultManageCredentialTemplate;
	private String editCredentialSummaryTemplate;
	private String editCredentialRequirementTemplate;
	private String editRequirementTemplate;
	private String showCredentialCustomFieldTemplate;
	private String showCredentialRequirementCustomFieldTemplate;
	private String closeTemplate;
	private String editCredentialCategoryTemplate;
	private String showCategoryCustomFieldTemplate;
	private String categoryReportingFieldTemplate;
	private String listCreditReportingFieldTemplate;
	private String showallProctorsTemplate;
	
	
	public ManageCredentialController() {
		super();
	}

	public ManageCredentialController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("newPage","true");
		return displayCredentials( request,  response);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String)
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

		if(command instanceof CredentialForm){
			CredentialForm form = (CredentialForm)command;
			if(methodName.equals("editCredentialSummary")
					|| methodName.equals("showCredentialRequirement")
					|| methodName.equals("deleteCredentialRequirement")
					|| methodName.equals("showCustomField")){
				//load the credential data from the command.getCid() into the command object using service
				Credential credential = accreditationService.loadForUpdateCredential(form.getCid());
				form.setCredential(credential);
				form.setTotalNumberOfLicense(((Integer)credential.getTotalNumberOfLicense()).toString());
			}
			
			if(methodName.equals("editCredentialSummary")){
				if(form.getCredential()!=null){
					Credential credential = form.getCredential();
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALS), "", "");
					form.setCustomFields(this.getCustomFieldListForForm(globalCustomFieldList,credential.getCustomfields(),credential.getCustomfieldValues()));
				}
			}
			else if(methodName.equals("showCredentialRequirementCustomField") || methodName.equals("editCredentialRequirement")){
				if(form.getRequirement()!=null){
					CredentialCategoryRequirement credentialCategoryRequirement = accreditationService.loadForUpdateCredentialCategoryRequirement(form.getRequirement().getId());
					form.setRequirement(credentialCategoryRequirement);
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT ), "", "");					
					form.setCredentialRequirementCustomFields(this.getCustomFieldListForForm(globalCustomFieldList,credentialCategoryRequirement.getCustomfields(),credentialCategoryRequirement.getCustomfieldValues()));
				}
			}
		}
	}

	/**
	 * @param form
	 */
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldListForForm(List <CustomField>globalCustomFieldList,List<CustomField> sourceCustomFieldList,List<CustomFieldValue> sourceCustomFieldValues ) {
			
			List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
			for (CustomField globalCustomField : globalCustomFieldList){
				totalCustomFieldList.add(globalCustomField);
			}
			for (CustomField customField : sourceCustomFieldList){
				totalCustomFieldList.add(customField);
			}
			
			return this.accreditationService.getCustomFieldsAndValues(totalCustomFieldList, sourceCustomFieldValues);
	}


	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#validate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, java.lang.String)
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		CredentialForm form = (CredentialForm)command;
		CredentialValidator validator = (CredentialValidator)this.getValidator();	
		
		//validation before saving the question just entered
		if(methodName.equals("saveEditedCredential")){
			validator.validateAddCredential(form, errors);

			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}

		}
		
		else if(methodName.equals("saveEditedCredentialRequirement")){
			validator.validateAddRequirement(form, errors);
			if (form.getCredentialRequirementCustomFields().size()>0){
				validator.validateCredentialRequirementCustomFields(form.getCredentialRequirementCustomFields(), errors);
			}			
		}
		
		// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
		else if(methodName.equals("editCredentialCategory")) {
			
			String action = request.getParameter("action");			
			if (StringUtils.isNotBlank(action) && action.equalsIgnoreCase("save")) {
				errors.setNestedPath("");
				validator.validateAddCategory(form, errors);
			}
		}
	}

	//handler methods
	public ModelAndView displayCredentials(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView(defaultManageCredentialTemplate);
	}

	public ModelAndView searchCredential(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		/* @Wajahat
		 * Principal: 03-09-2016
		 * Please don't change the below code of getting VU360User object because it is being used to check contentowners set in this object
		 */
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		List<Credential> credentialList = new ArrayList<Credential>();
		HttpSession session = request.getSession();
		String credentialName = request.getParameter("credentialName");
		String credentialShortName = request.getParameter("credentialShortName");

		credentialName = (credentialName == null)? "" : credentialName.trim();
		credentialShortName = (credentialShortName == null)? "" : credentialShortName.trim();
		
		credentialName = HtmlEncoder.escapeHtmlFull(credentialName).toString();
		context.put("credentialName", credentialName);
		credentialShortName = HtmlEncoder.escapeHtmlFull(credentialShortName).toString();
		context.put("credentialShortName", credentialShortName);
		
		String paging = request.getParameter("paging");

		if (StringUtils.isNotBlank(paging) && paging.equalsIgnoreCase(MANAGE_CREDENTIAL_PAGING_ACTION)) {
			credentialName = (String)session.getAttribute("credentialName");
			credentialShortName = (String)session.getAttribute("credentialShortName");

			session.setAttribute("credentialName", credentialName);
			session.setAttribute("credentialShortName", credentialShortName);

		} else {
			session.setAttribute("credentialName", credentialName);
			session.setAttribute("credentialShortName", credentialShortName);

		}
		String pageIndex = request.getParameter("pageIndex");

		credentialList = accreditationService.findCredential(credentialName,credentialShortName, loggedInUser.getRegulatoryAnalyst());
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( credentialList.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}
		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null )
//			sortDirection = session.getAttribute("sortDirection").toString();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);
		if( sortColumnIndex != null && sortDirection != null ) {

			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			// sorting against regulator name
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CredentialSort credentialSort=new CredentialSort();
					credentialSort.setSortBy("officialLicenseName");
					credentialSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(credentialList,credentialSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					CredentialSort credentialSort=new CredentialSort();
					credentialSort.setSortBy("officialLicenseName");
					credentialSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(credentialList,credentialSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against alias
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CredentialSort credentialSort=new CredentialSort();
					credentialSort.setSortBy("shortLicenseName");
					credentialSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(credentialList,credentialSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					CredentialSort credentialSort=new CredentialSort();
					credentialSort.setSortBy("shortLicenseName");
					credentialSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(credentialList,credentialSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}

		context.put("credentialList", credentialList);
		return new ModelAndView(defaultManageCredentialTemplate, "context",context);
	}

	public ModelAndView deleteCredential(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		if  ( request.getParameterValues("credential") != null ) {
			String []checkID = request.getParameterValues("credential");
			for (int i=0;i<checkID.length;i++) {
				Credential credential = accreditationService.loadForUpdateCredential(Long.valueOf(checkID[i]));
				credential.setActive(false);
				accreditationService.saveCredential(credential);
			}
		}
		
		context.put("target", "searchCredential");
		return new ModelAndView(closeTemplate, "context",context);
	}

	public ModelAndView editCredentialSummary(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		request.getSession(true).setAttribute("feature", "LMS-ACC-0003");
		
		CredentialForm form = (CredentialForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		if (form!=null){
			if (form.getCredential()!=null){
				form.setInformationLastVerifiedDate(formatter.format(form.getCredential().getInformationLastVerifiedDate()));
			}
		}
		return new ModelAndView(editCredentialSummaryTemplate);
	}

	public ModelAndView saveEditedCredential(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CredentialForm form = (CredentialForm)command;
		if(errors.hasErrors()){

			// Refill Custom Fields and Values for Rendering
			form.setCustomFields( this.accreditationService.getCustomFieldAndValuesForRendering(form.getCustomFields()) );
			return new ModelAndView(editCredentialSummaryTemplate);
		}

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Credential credential = form.getCredential();
		credential.setInformationLastVerifiedDate(formatter.parse(form.getInformationLastVerifiedDate()));
		
		if(!StringUtils.isBlank(form.getTotalNumberOfLicense()))
			credential.setTotalNumberOfLicense(Integer.parseInt(form.getTotalNumberOfLicense()));

		try {
			
			credential.setCustomfieldValues( this.accreditationService.getCustomFieldValues(form.getCustomFields(), true) );
			
			if(form.getCredential().getJurisdiction().equalsIgnoreCase("other")){
				credential.setJurisdiction(form.getCredential().getOtherJurisdiction());
			}
			
			if(credential.getRenewalDeadlineType().equalsIgnoreCase(credential.HARD)){
				credential.setStaggeredBy(null);
				credential.setStaggeredTo(null);
			}else if(credential.getRenewalDeadlineType().equalsIgnoreCase(credential.STAGGERED)){
				credential.setHardDeadlineMonth(null);
				credential.setHardDeadlineDay(null);
				credential.setHardDeadlineYear(null);
			}else{
				credential.setStaggeredBy(null);
				credential.setStaggeredTo(null);
				credential.setHardDeadlineMonth(null);
				credential.setHardDeadlineDay(null);
				credential.setHardDeadlineYear(null);
			}		
			accreditationService.saveCredential(credential);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(closeTemplate);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	public ModelAndView showCredentialRequirement(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		CredentialForm form = (CredentialForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		// Clear Form Fields
		form.setManageCredentialRequirementCustomField(null);
		
		List<List<TreeNode>> requirementTree = this.accreditationService.getCategroyRequirementTreeByCredential(form.getCid(), true);
		int totalRecords = 0;
		
		if (requirementTree != null) {
			totalRecords = requirementTree.size();
		}
		
		context.put("requirementTree", requirementTree);
		context.put("totalRecords", totalRecords);		
		return new ModelAndView(editCredentialRequirementTemplate, "context", context);
	}


	public ModelAndView editCredentialRequirement(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CredentialForm form = (CredentialForm)command;
		CredentialCategoryRequirement requirement = accreditationService.loadForUpdateCredentialCategoryRequirement(form.getReqId());
		form.setRequirement(requirement);
		List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT ), "", "");							
		form.setCredentialRequirementCustomFields(this.getCustomFieldListForForm(globalCustomFieldList,requirement.getCustomfields(),requirement.getCustomfieldValues()));
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if(requirement.getCERequirementDeadline()!=null){
			form.setCERequirementDeadline(formatter.format(requirement.getCERequirementDeadline()));
		}
		if(requirement.getLicenseRenewalDate() !=null){
			form.setLicenseRenewalDate(formatter.format(requirement.getLicenseRenewalDate()));
		}
		if(requirement.getReportingPeriod() !=null){
			form.setReportingPeriod(formatter.format(requirement.getReportingPeriod()));
		}
		form.setCreditHours(String.valueOf(requirement.getNumberOfHours()));
		return new ModelAndView(editRequirementTemplate);
	}

	public ModelAndView saveEditedCredentialRequirement(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CredentialForm form = (CredentialForm)command;
		if(errors.hasErrors()){
			
			// Refill Custom Fields and Values for Rendering
			form.setCredentialRequirementCustomFields( this.accreditationService.getCustomFieldAndValuesForRendering(form.getCredentialRequirementCustomFields()) );
			return new ModelAndView(editRequirementTemplate);
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		//CredentialForm form = (CredentialForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		CredentialCategoryRequirement requirement = form.getRequirement();
                requirement.setNumberOfHours( Double.valueOf(form.getCreditHours()));
                
		if ( !StringUtils.isBlank(form.getLicenseRenewalDate()) )
			requirement.setLicenseRenewalDate(formatter.parse(form.getLicenseRenewalDate()));
		if ( !StringUtils.isBlank(form.getCERequirementDeadline()) )
			requirement.setCERequirementDeadline(formatter.parse(form.getCERequirementDeadline()));
		if ( !StringUtils.isBlank(form.getReportingPeriod()) )
			requirement.setReportingPeriod(formatter.parse(form.getReportingPeriod()));
		
		requirement.setCustomfieldValues( this.accreditationService.getCustomFieldValues(form.getCredentialRequirementCustomFields(), true) );
		
		accreditationService.saveCredentialCategoryRequirement(requirement);
		context.put("target", "showCredentialRequirement");
		return new ModelAndView(closeTemplate, "context", context);
	}

	@SuppressWarnings("static-access")
	public ModelAndView showCredentialRequirementCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CredentialForm form = (CredentialForm)command;
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>();

		HttpSession session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();

		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( form.getRequirement().getCustomfields().size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}

		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);
		
		if (form.getRequirement().getCustomfields() != null && form.getRequirement().getCustomfields().size() > 0) {
			for (CustomField customField : form.getRequirement().getCustomfields()) {
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
		}

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
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldLabel");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
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
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldType");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		form.setManageCredentialRequirementCustomField(manageCustomFieldList);

		return new ModelAndView(showCredentialRequirementCustomFieldTemplate, "context", context);
	}
	
	public ModelAndView deleteCredentialRequirementCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		CredentialForm form = (CredentialForm)command;				
		if(form.getManageCredentialRequirementCustomField() != null && form.getManageCredentialRequirementCustomField().size() > 0 ) {
			for(ManageCustomField manageCustomField : form.getManageCredentialRequirementCustomField()) {
				if (manageCustomField.isSelected()) {
					for(int i=0;i<form.getRequirement().getCustomfields().size();i++) {						
						if(manageCustomField.getId() == form.getRequirement().getCustomfields().get(i).getId()) {				
					       form.getRequirement().getCustomfields().remove(i);					       
						}
					}
				}
			}
		}		
		accreditationService.saveCredentialCategoryRequirement(form.getRequirement());
		context.put("target", "showCredentialRequirementCustomField");
		return new ModelAndView(closeTemplate, "context", context);
	}

	
	@SuppressWarnings("static-access")
	public ModelAndView showCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CredentialForm form = (CredentialForm)command;
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>();

		HttpSession session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();

		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( form.getCredential().getCustomfields().size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}

		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);

		if (form.getCredential().getCustomfields() != null && form.getCredential().getCustomfields().size() > 0) {
			for (CustomField customField : form.getCredential().getCustomfields()) {
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
		}

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
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldLabel");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
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
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldType");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		form.setManageCustomField(manageCustomFieldList);

		return new ModelAndView(showCredentialCustomFieldTemplate, "context", context);
	}
	
	

		
	public ModelAndView showProctors( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		HttpSession session = request.getSession(true);
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		
		
		CredentialForm form = (CredentialForm)command;
		
		List<CredentialProctors> lstCredProctors = new ArrayList<CredentialProctors>();
		List<Proctor> lstProctors = accreditationService.getAllProctorsByCredential(form.getCid());
		
		
		try{	
				CredentialProctors cProctor = null;
				
				for(Proctor proctor : lstProctors)
				{
					cProctor = new CredentialProctors();

					cProctor.setUserName(proctor.getUser().getUsername());
					
					if(proctor.getUser()!=null){
						cProctor.setId(proctor.getId());
						cProctor.setFirstName(proctor.getUser().getFirstName());
						cProctor.setLastName(proctor.getUser().getLastName());
						//cProctor.setVu360UserId(proctor.getUser().getId());
					}
					
					if(proctor.getUser().getAccountNonLocked())
						cProctor.setStatus("No");
					else
						cProctor.setStatus("Yes");
					
					lstCredProctors.add(cProctor);
					
				}
			
				if(lstProctors != null && lstProctors.size() > 0){
					//============================For Sorting============================//
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
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
		
					if( sortColumnIndex != null && sortDirection != null ) {
		
						 if( sortColumnIndex.equalsIgnoreCase("0") ) {
							 CredentialProctorSort sort = new CredentialProctorSort();
								Integer sortDirInt = 0; 
								try {
									sortDirInt = Integer.parseInt(sortDirection);// .parseInt();
								} catch (Exception e) {
									log.error("Error parsing sort direction ");
								}
								sort.setSortBy("firstName");
								sort.setSortDirection(sortDirInt);
								Collections.sort(lstCredProctors,sort);
								session.setAttribute("sortDirection", sortDirInt);
								session.setAttribute("sortColumnIndex", 0);
								context.put("sortDirection", sortDirInt);
								context.put("sortColumnIndex", 3);
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							 CredentialProctorSort sort = new CredentialProctorSort();
								Integer sortDirInt = 0; 
								try {
									sortDirInt = Integer.parseInt(sortDirection);// .parseInt();
								} catch (Exception e) {
									log.error("Error parsing sort direction ");
								}
								sort.setSortBy("lastName");
								sort.setSortDirection(sortDirInt);
								Collections.sort(lstCredProctors,sort);
								session.setAttribute("sortDirection", sortDirInt);
								session.setAttribute("sortColumnIndex", 1);
								context.put("sortDirection", sortDirInt);
								context.put("sortColumnIndex", 3);
							} 
						else if( sortColumnIndex.equalsIgnoreCase("2") ) {
							 CredentialProctorSort sort = new CredentialProctorSort();
								Integer sortDirInt = 0; 
								try {
									sortDirInt = Integer.parseInt(sortDirection);// .parseInt();
								} catch (Exception e) {
									log.error("Error parsing sort direction ");
								}
								sort.setSortBy("userName");
								sort.setSortDirection(sortDirInt);
								Collections.sort(lstCredProctors,sort);
								session.setAttribute("sortDirection", sortDirInt);
								session.setAttribute("sortColumnIndex", 2);
								context.put("sortDirection", sortDirInt);
								context.put("sortColumnIndex", 3);
							} 
					}
					
					
					context.put("proctorEnrollmentList", lstCredProctors);
					context.put("sortDirection", sortDirection);
					context.put("sortColumnIndex", sortColumnIndex);
					context.put("showAll", request.getParameter("showAll"));
					pagerAttributeMap.put("showAll", request.getParameter("showAll"));
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					form.setCredentialProctors(lstCredProctors);
					return new ModelAndView(showallProctorsTemplate , "context" , context);
				}
			
		}
		catch(Exception ex){
			log.debug(" EXCEPTION  "+ex.getMessage());
		}
		form.setCredentialProctors(new ArrayList<CredentialProctors>());
		return new ModelAndView(showallProctorsTemplate);
	}
	
	
	
	public ModelAndView retireProctors(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		CredentialForm form = (CredentialForm)command;
		
		if(request.getParameterValues("selectedProctor")!=null){
			Long[] fieldsToDelete=FormUtil.getStringAsLong(request.getParameterValues("selectedProctor"));
			
			
			
			if (fieldsToDelete.length > 0) {
				for (int index = 0; index < fieldsToDelete.length; index++) {
					if(fieldsToDelete[index]!=null && fieldsToDelete[index]>0){
						Proctor proctor = proctorService.loadForUpdate(fieldsToDelete[index]);
						
						if(proctor.getCredentials().size()==1)
							proctor.setStatus(ProctorStatusEnum.Expired.toString());
							
						// removing Credential from Proctor
						List<Credential> lstCred = proctor.getCredentials();
						for (Iterator<Credential> it = lstCred.iterator(); it.hasNext(); ) {
							Credential credential = it.next();  
							if(credential.getId().compareTo(form.getCredential().getId())==0)
								it.remove();
						}
						
						proctor.setCredentials(lstCred);
						proctorService.updateProctor(proctor);
						
						if(lstCred==null || lstCred.size()<=0)
						{
							//CODE USE TO DELETE PROCTOR ROLE OF USER
							String userIdArray[] = new String[1];
							userIdArray[0] = proctor.getUser().getId().toString();
							learnerService.unAssignUsersFromAllRolesOfType(userIdArray, LMSRole.ROLE_PROCTOR);
						}
						
						
						lstCred=null;
					}
				}
			}
			

		}	
		return new ModelAndView("redirect:acc_manageCredential.do?method=showProctors");
	}
	
	
	public ModelAndView deleteCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		CredentialForm form = (CredentialForm)command;
		if(form.getManageCustomField() != null && form.getManageCustomField().size() > 0 ) {
			for(ManageCustomField manageCustomField : form.getManageCustomField()) {
				if (manageCustomField.isSelected()) {
					for(int i=0;i<form.getCredential().getCustomfields().size();i++) {
						if(manageCustomField.getId() == form.getCredential().getCustomfields().get(i).getId()) {
							form.getCredential().getCustomfields().remove(i);
						}
					}
				}
			}
		}
		accreditationService.saveCredential(form.getCredential());
		context.put("target", "showCustomField");
		return new ModelAndView(closeTemplate, "context", context);
	}
	
	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	public ModelAndView editCredentialCategory (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		CredentialForm form = (CredentialForm)command;
		String resultingView = "redirect:acc_manageCredential.do?method=showCredentialRequirement";
		Map<String, Object> context = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(request.getParameter("categoryId"))) {
			
			if(errors.hasErrors()){
				
				// Refill Custom Fields and Values for Rendering
				form.setCredentialRequirementCustomFields( this.accreditationService.getCustomFieldAndValuesForRendering(form.getCredentialRequirementCustomFields()) );
				resultingView = this.editCredentialCategoryTemplate;
			}
			else {
				String action = request.getParameter("action");
				
				if (StringUtils.isNotBlank(action) && action.equalsIgnoreCase("save")) {
					
					// Save the edited information
					form.getCategory().setCustomFieldValues( this.accreditationService.getCustomFieldValues( form.getCredentialRequirementCustomFields(), true ) );					
					CredentialCategory category = this.accreditationService.saveCredentialCategory(form.getCategory());
					form.setCategory(category);
				}
				else {
					// Display Category Information for Editing
					Long catId = Long.valueOf( request.getParameter("categoryId") );					
					CredentialCategory category = this.accreditationService.loadForUpdateCredentialCategory(catId); // Used loadForUpdate method call because having issues in saving Custom Field Values
					
					form.setCredentialRequirementCustomField(null);
					form.setCategory(null);
					if (category != null) {
						category.setStrHours(String.valueOf(category.getHours()));
						category.setStrCompletionDeadlineMonths(String.valueOf(category.getCompletionDeadlineMonths()));
						form.setCategory(category);
						
						// Fetch associated Custom Fields
						List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY ), "", "");
						form.setCredentialRequirementCustomFields( this.getCustomFieldListForForm( globalCustomFieldList, category.getCustomFields(), category.getCustomFieldValues()) );
						
						resultingView = this.editCredentialCategoryTemplate;
					}
				}
			}			
		}
		context.put("categoryTypes", CredentialCategory.CATEGORY_TYPES);
		return new ModelAndView(resultingView,context);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	public ModelAndView deleteCategoryRequirement (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		String[] selCatIds = request.getParameterValues("category");
		String[] selReqIds = request.getParameterValues("requirement");
				
		boolean isSucceed = this.accreditationService.deleteCredentialCategoryRequirements(selCatIds, selReqIds);
		if (!isSucceed) {
			errors.rejectValue("cateory", "error.deleteCredentialCategoryRequirement.failure");
		}
				
		return new ModelAndView("redirect:acc_manageCredential.do?method=showCredentialRequirement");
	}
	
	// [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	@SuppressWarnings("unchecked")
	public ModelAndView showCredentialCategoryCustomField (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		log.debug("IN showCredentialCategoryCustomField");
		
		CredentialForm form = (CredentialForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		int totalRecords = -1, recordShowing = 0, pageIndex = 0, sortDirection = 0, pageSize = DEFAULT_PAGE_SIZE;
		String sortColumn = "fieldLabel";
		
		// Get request parameters
		String action = StringUtils.isNotBlank(request.getParameter("action")) ? request.getParameter("action") : ACTION_SORT;
		sortColumn = StringUtils.isNotBlank(request.getParameter("sortColumn")) ? request.getParameter("sortColumn") : sortColumn;
		sortDirection = StringUtils.isNotBlank(request.getParameter("sortDirection")) ? Integer.valueOf(request.getParameter("sortDirection")) : sortDirection;
		pageIndex = StringUtils.isNotBlank(request.getParameter("pageIndex")) ? Integer.valueOf(request.getParameter("pageIndex")) : pageIndex;
		pageIndex = (pageIndex <= 0) ? 0 : pageIndex;
		
		if ( action.equalsIgnoreCase(ACTION_DELETE) ) {
			String[] selCustomFieldIds = request.getParameterValues("selectedCustomFields");
			this.accreditationService.deleteCategoryCustomFields(form.getCategory().getId(), selCustomFieldIds);
		}
		else if ( action.equalsIgnoreCase(ACTION_SHOW_ALL) ) {					
			pageSize = -1;
			pageIndex = 0;
		}
		else if ( action.equalsIgnoreCase(ACTION_SHOW_PREV) ) {
			pageIndex = (pageIndex <= 0) ? 0 : (pageIndex - 1);				
		}
		else if ( action.equals(ACTION_SHOW_NEXT) ) {
			pageIndex = pageIndex < 0 ? 0 : (pageIndex + 1);			
		}
		
		Map<Object, Object> resultSet = this.accreditationService.getCustomFieldsByCategory(form.getCategory().getId(), sortColumn, sortDirection, pageIndex, pageSize);
		List<ManageCustomField> customFieldList = (List<ManageCustomField>) resultSet.get("customFieldList");
		totalRecords = Integer.valueOf( resultSet.get("totalRecords").toString() );
		pageSize = (pageSize == -1) ? totalRecords : pageSize;
		
		if ( action.equalsIgnoreCase(ACTION_SHOW_ALL) ) {
			recordShowing = customFieldList.size();
		}
		else {
			recordShowing = customFieldList.size() < DEFAULT_PAGE_SIZE ? totalRecords : (pageIndex + 1) * DEFAULT_PAGE_SIZE;
		}
		
		//form.setManageCredentialRequirementCustomField(customFieldList);
		context.put("customFieldList", customFieldList);
		context.put("pageIndex", pageIndex);
		context.put("recordShowing", recordShowing);
		context.put("totalRecords", totalRecords);
		context.put("sortDirection", sortDirection);
		context.put("sortColumn", sortColumn);
		
		return new ModelAndView(this.showCategoryCustomFieldTemplate, "context", context);
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
	 * @return the defaultManageCredentialTemplate
	 */
	public String getDefaultManageCredentialTemplate() {
		return defaultManageCredentialTemplate;
	}

	/**
	 * @param defaultManageCredentialTemplate the defaultManageCredentialTemplate to set
	 */
	public void setDefaultManageCredentialTemplate(
			String defaultManageCredentialTemplate) {
		this.defaultManageCredentialTemplate = defaultManageCredentialTemplate;
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
	 * @return the editCredentialSummaryTemplate
	 */
	public String getEditCredentialSummaryTemplate() {
		return editCredentialSummaryTemplate;
	}

	/**
	 * @param editCredentialSummaryTemplate the editCredentialSummaryTemplate to set
	 */
	public void setEditCredentialSummaryTemplate(
			String editCredentialSummaryTemplate) {
		this.editCredentialSummaryTemplate = editCredentialSummaryTemplate;
	}

	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	/**
	 * @return the editCredentialRequirementTemplate
	 */
	public String getEditCredentialRequirementTemplate() {
		return editCredentialRequirementTemplate;
	}

	/**
	 * @param editCredentialRequirementTemplate the editCredentialRequirementTemplate to set
	 */
	public void setEditCredentialRequirementTemplate(
			String editCredentialRequirementTemplate) {
		this.editCredentialRequirementTemplate = editCredentialRequirementTemplate;
	}

	/**
	 * @return the showCredentialCustomFieldTemplate
	 */
	public String getShowCredentialCustomFieldTemplate() {
		return showCredentialCustomFieldTemplate;
	}

	/**
	 * @param showCredentialCustomFieldTemplate the showCredentialCustomFieldTemplate to set
	 */
	public void setShowCredentialCustomFieldTemplate(
			String showCredentialCustomFieldTemplate) {
		this.showCredentialCustomFieldTemplate = showCredentialCustomFieldTemplate;
	}

	/**
	 * @return the editRequirementTemplate
	 */
	public String getEditRequirementTemplate() {
		return editRequirementTemplate;
	}

	/**
	 * @param editRequirementTemplate the editRequirementTemplate to set
	 */
	public void setEditRequirementTemplate(String editRequirementTemplate) {
		this.editRequirementTemplate = editRequirementTemplate;
	}

	public String getShowCredentialRequirementCustomFieldTemplate() {
		return showCredentialRequirementCustomFieldTemplate;
	}

	public void setShowCredentialRequirementCustomFieldTemplate(
			String showCredentialRequirementCustomFieldTemplate) {
		this.showCredentialRequirementCustomFieldTemplate = showCredentialRequirementCustomFieldTemplate;
	}

	/**
	 * // [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	 * @param editCredentialCategoryTemplate the editCredentialCategoryTemplate to set
	 */
	public void setEditCredentialCategoryTemplate(
			String editCredentialCategoryTemplate) {
		this.editCredentialCategoryTemplate = editCredentialCategoryTemplate;
	}

	/**
	 * // [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	 * @return the editCredentialCategoryTemplate
	 */
	public String getEditCredentialCategoryTemplate() {
		return editCredentialCategoryTemplate;
	}

	/**
	 *  [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	 * @param showCategoryCustomFieldTemplate the showCategoryCustomFieldTemplate to set
	 */
	public void setShowCategoryCustomFieldTemplate(
			String showCategoryCustomFieldTemplate) {
		this.showCategoryCustomFieldTemplate = showCategoryCustomFieldTemplate;
	}

	/**
	 *  [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	 * @return the showCategoryCustomFieldTemplate
	 */
	public String getShowCategoryCustomFieldTemplate() {
		return showCategoryCustomFieldTemplate;
	}

	public String getCategoryReportingFieldTemplate() {
		return categoryReportingFieldTemplate;
	}

	public void setCategoryReportingFieldTemplate(
			String categoryReportingFieldTemplate) {
		this.categoryReportingFieldTemplate = categoryReportingFieldTemplate;
	}

	public String getListCreditReportingFieldTemplate() {
		return listCreditReportingFieldTemplate;
	}

	public void setListCreditReportingFieldTemplate(
			String listCreditReportingFieldTemplate) {
		this.listCreditReportingFieldTemplate = listCreditReportingFieldTemplate;
	}

	public String getShowallProctorsTemplate() {
		return showallProctorsTemplate;
	}

	public void setShowallProctorsTemplate(String showallProctorsTemplate) {
		this.showallProctorsTemplate = showallProctorsTemplate;
	}

	public ProctorService getProctorService() {
		return proctorService;
	}

	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	
	
}