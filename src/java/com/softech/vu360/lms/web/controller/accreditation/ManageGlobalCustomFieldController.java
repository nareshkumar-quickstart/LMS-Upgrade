package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldContext;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.GlobalCustomField;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.GlobalCustomFieldShort;
import com.softech.vu360.util.HtmlEncoder;

public class ManageGlobalCustomFieldController extends VU360BaseMultiActionController{
	
	private AccreditationService accreditationService = null;
	private String defaultGlobalCustomFieldTemplate;
	
	public static final String CUSTOMFIELD_ALL = "All";
	
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
		
	public static final String CUSTOMFIELD_ENTITY_REGULATOR = "Regulator";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALS = "Credentials";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT = "Credential Requirement";
	public static final String CUSTOMFIELD_ENTITY_PROVIDERS = "Providers";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTORS = "Instructors";
	public static final String CUSTOMFIELD_ENTITY_COURSE_APPROVALS = "Course Approvals";
	public static final String CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS = "Provider Approvals";
	public static final String CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS = "Instructor Approvals";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY = "Credential Category";
	
	private static final String[] CUSTOMFIELD_TYPES = {
		  CUSTOMFIELD_ALL
		, CUSTOMFIELD_SINGLE_LINE_OF_TEXT
		, CUSTOMFIELD_DATE
		, CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT
		, CUSTOMFIELD_NUMBER
		, CUSTOMFIELD_RADIO_BUTTON
		, CUSTOMFIELD_CHOOSE
		, CUSTOMFIELD_CHECK_BOX
		, CUSTOMFIELD_SOCIAL_SECURITY_NUMBER
	};
	
	private static final String[] CUSTOMFIELD_ENTITIES = {
		  CUSTOMFIELD_ALL
		, CUSTOMFIELD_ENTITY_REGULATOR
		, CUSTOMFIELD_ENTITY_CREDENTIALS
		, CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY
		, CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT
		, CUSTOMFIELD_ENTITY_PROVIDERS
		, CUSTOMFIELD_ENTITY_INSTRUCTORS
		, CUSTOMFIELD_ENTITY_COURSE_APPROVALS
		, CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS
		, CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS
	};
	
	
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// do nothing
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// do nothing
	}
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("newPage","true");
		return displayGlobalCustomFields( request,  response);
	}
	
	public ModelAndView displayGlobalCustomFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<Object,Object> context = new HashMap<Object,Object>();
		context.put("customFieldEntities", CUSTOMFIELD_ENTITIES);
		context.put("customFieldTypes", CUSTOMFIELD_TYPES);
		
		return new ModelAndView(defaultGlobalCustomFieldTemplate, "context", context);
	}
	
	public ModelAndView searchGlobalCustomFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		HttpSession session = request.getSession();
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		 
		String fieldLabel = ServletRequestUtils.getStringParameter(request, "fieldLabel");
		String fieldType = ServletRequestUtils.getStringParameter(request, "fieldType");
		String fieldEntity = ServletRequestUtils.getStringParameter(request, "fieldEntity");
		
		fieldLabel = (fieldLabel == null)? "" : fieldLabel.trim();
		fieldType = (fieldType == null)? "" : fieldType.trim();
		fieldEntity = (fieldEntity == null)? "" : fieldEntity.trim();
		
		fieldLabel = HtmlEncoder.escapeHtmlFull(fieldLabel).toString();
		
		context.put("fieldLabel", fieldLabel);
		context.put("fieldType", fieldType);
		context.put("fieldEntity", fieldEntity);
		
		fieldEntity = this.getDBEntityType(fieldEntity);
		fieldType = this.getDBFieldType(fieldType);
		
		List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, fieldEntity), fieldType, fieldLabel);
		List<GlobalCustomField> globalCustomFieldWrapperList = new ArrayList<GlobalCustomField>();
		for(CustomField customField: globalCustomFieldList){
			GlobalCustomField globalCustomField = new GlobalCustomField();
			globalCustomField.setId(customField.getId());
			globalCustomField.setFieldLabel(customField.getFieldLabel());
			globalCustomField.setFieldType(this.getFieldType(customField));
			globalCustomField.setEntity(this.getEntity(customField));
			
			globalCustomFieldWrapperList.add(globalCustomField);
		}
		
		String pageIndex = ServletRequestUtils.getStringParameter(request, "pageIndex");
		if(StringUtils.isBlank(pageIndex))pageIndex="0";
		PagerAttributeMap.put("pageIndex",pageIndex);
		if( globalCustomFieldList.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}
		
		String sortColumnIndex = ServletRequestUtils.getStringParameter(request, "sortColumnIndex");
		String sortDirection = ServletRequestUtils.getStringParameter(request, "sortDirection");
		
		if (StringUtils.isBlank(sortColumnIndex) && session.getAttribute("sortColumnIndex") != null ){
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		}
		if (StringUtils.isBlank(sortDirection) && session.getAttribute("sortDirection") != null ){
			sortDirection = session.getAttribute("sortDirection").toString();
		}
		
		String showAll = ServletRequestUtils.getStringParameter(request, "showAll");
		if(StringUtils.isBlank(showAll))showAll="false";
		context.put("showAll", showAll);
		
		if( sortColumnIndex != null && sortDirection != null ) {
			
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				
				GlobalCustomFieldShort customFieldSort = new GlobalCustomFieldShort();
				customFieldSort.setSortBy("fieldLabel");
				customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
				Collections.sort(globalCustomFieldWrapperList,customFieldSort);
				
				context.put("sortDirection", Integer.parseInt(sortDirection));
				context.put("sortColumnIndex", 0);
				
			}else if(sortColumnIndex.equalsIgnoreCase("1")){
				
				GlobalCustomFieldShort customFieldSort = new GlobalCustomFieldShort();
				customFieldSort.setSortBy("fieldType");
				customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
				Collections.sort(globalCustomFieldWrapperList,customFieldSort);
				
				context.put("sortDirection", Integer.parseInt(sortDirection));
				context.put("sortColumnIndex", 1);
				
			}else if(sortColumnIndex.equalsIgnoreCase("2")){
				
				GlobalCustomFieldShort customFieldSort = new GlobalCustomFieldShort();
				customFieldSort.setSortBy("entity");
				customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
				Collections.sort(globalCustomFieldWrapperList,customFieldSort);
				
				context.put("sortDirection", Integer.parseInt(sortDirection));
				context.put("sortColumnIndex", 2);					
			}
		}
		context.put("customFieldList", globalCustomFieldWrapperList);
		context.put("customFieldEntities", CUSTOMFIELD_ENTITIES);
		context.put("customFieldTypes", CUSTOMFIELD_TYPES);
		
		return new ModelAndView(defaultGlobalCustomFieldTemplate,"context",context);
		
	}
	
	private String getFieldType(CustomField customField){
		if(customField instanceof SingleLineTextCustomFiled){
			return CUSTOMFIELD_SINGLE_LINE_OF_TEXT;
		}else if(customField instanceof MultipleLineTextCustomfield){
			return CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT;
		}else if(customField instanceof NumericCusomField){
			return CUSTOMFIELD_NUMBER;
		}else if(customField instanceof DateTimeCustomField){
			return CUSTOMFIELD_DATE;
		}else if (customField instanceof SingleSelectCustomField){
			return CUSTOMFIELD_RADIO_BUTTON;
		}else if (customField instanceof MultiSelectCustomField){
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			if (multiSelectCustomField.getCheckBox()){
				return CUSTOMFIELD_CHECK_BOX;
			}else {
				return CUSTOMFIELD_CHOOSE;
			}
		}else if (customField instanceof SSNCustomFiled){
			return CUSTOMFIELD_SOCIAL_SECURITY_NUMBER;
		}
		return "";
	}
	
	private String getEntity(CustomField customField){
		
		CustomFieldContext customFieldContext = customField.getCustomFieldContext();
		if(customFieldContext !=null){
			if(customFieldContext.isGlobalRegulator() ){//Regulator
				return CUSTOMFIELD_ENTITY_REGULATOR;
			}else if(customFieldContext.isGlobalCredential()){//Credentials
				return CUSTOMFIELD_ENTITY_CREDENTIALS;
			} else if(customFieldContext.isGlobalCredentialRequirement()){//Credentials
				return CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT;
			}else if(customFieldContext.isGlobalProvider()){//Providers
				return CUSTOMFIELD_ENTITY_PROVIDERS;
			}else if(customFieldContext.isGlobalInstructor()){//Instructors
				return CUSTOMFIELD_ENTITY_INSTRUCTORS;
			}else if(customFieldContext.isGlobalCourseApproval()){//Course Approvals
				return CUSTOMFIELD_ENTITY_COURSE_APPROVALS;
			}else if(customFieldContext.isGlobalProviderApproval()){//Provider Approvals
				return CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS;
			}else if(customFieldContext.isGlobalInstructorApproval()){//Instructor Approvals
				return CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS;
			}else if(customFieldContext.isGlobalCredentialCategory()){//Credential Category
				return CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY;
			}
		}
		return "";
	}
	
	// [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	private String getDBEntityType (String entity) {
		
		if (entity.equalsIgnoreCase(CUSTOMFIELD_ALL)) {
			return "CUSTOMFIELD_ALL";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_REGULATOR)) {
			return "CUSTOMFIELD_REGULATOR";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_CREDENTIALS)) {
			return "CUSTOMFIELD_CREDENTIAL";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY)) {
			return "CUSTOMFIELD_CREDENTIALCATEGORY";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT)) {
			return "CUSTOMFIELD_CREDENTIALREQUIREMENT";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_PROVIDERS)) {
			return "CUSTOMFIELD_PROVIDER";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_INSTRUCTORS)) {
			return "CUSTOMFIELD_INSTRUCTOR";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_COURSE_APPROVALS)) {
			return "CUSTOMFIELD_COURSEAPPROVAL";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_PROVIDER_APPROVALS)) {
			return "CUSTOMFIELD_PROVIDERAPPROVAL";
		} else if (entity.equalsIgnoreCase(CUSTOMFIELD_ENTITY_INSTRUCTOR_APPROVALS)) {
			return "CUSTOMFIELD_INSTRUCTORAPPROVAL";
		} 
		
		return null;
	}
	
	// [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
	private String getDBFieldType (String fieldType) {
		
		if (fieldType.equalsIgnoreCase(CUSTOMFIELD_ALL)) {
			return "CUSTOMFIELD_ALL";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			return "SINGLELINETEXTCUSTOMFIELD";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_DATE)) {
			return "DATETIMECUSTOMFIELD";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			return "MULTIPLELINETEXTCUSTOMFIELD";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_NUMBER)) {
			return "NUMERICCUSTOMFIELD";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)) {
			return "SINGLESELECTCUSTOMFIELD";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_CHOOSE)) {
			return "MULTISELECTCUSTOMFIELD";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
			return "MULTISELECTCUSTOMFIELD_CHECK_BOX";
		} else if (fieldType.equalsIgnoreCase(CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			return "SSNCUSTOMFIELD";
		}
		
		return null;
	}
	
	public ModelAndView deleteCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		String fieldLabel = ServletRequestUtils.getStringParameter(request, "fieldLabel");
		String fieldType = ServletRequestUtils.getStringParameter(request, "fieldType");
		String fieldEntity = ServletRequestUtils.getStringParameter(request, "fieldEntity");
		
		if  ( request.getParameterValues("customfield") != null ) {
			long[] id = new long[request.getParameterValues("customfield").length];
			String []checkID = request.getParameterValues("customfield");
			for ( int i = 0 ; i < id.length ; i++ ) {
				id[i] = Long.valueOf(checkID[i]);
				CustomField field = accreditationService.loadForUpdateCustomField(id[i]);
				field.setActive(false);
				accreditationService.saveCustomField(field);
			}
		}
		
		fieldEntity = this.getDBEntityType(fieldEntity);
		fieldType = this.getDBFieldType(fieldType);
		
		List<CustomField> globalCustomFieldList = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, fieldEntity), fieldType, fieldLabel);
		List<GlobalCustomField> globalCustomFieldWrapperList = new ArrayList<GlobalCustomField>();
		for( CustomField field : globalCustomFieldList ) {
			GlobalCustomField globalCustomField = new GlobalCustomField();
			globalCustomField.setId(field.getId());
			globalCustomField.setFieldLabel(field.getFieldLabel());
			globalCustomField.setFieldType(this.getFieldType(field));
			globalCustomField.setEntity(this.getEntity(field));
			globalCustomFieldWrapperList.add(globalCustomField);
		}
		context.put("customFieldList", globalCustomFieldWrapperList);
		context.put("customFieldEntities", CUSTOMFIELD_ENTITIES);
		context.put("customFieldTypes", CUSTOMFIELD_TYPES);
		
		return new ModelAndView(defaultGlobalCustomFieldTemplate,"context",context);
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
	 * @return the defaultGlobalCustomFieldTemplate
	 */
	public String getDefaultGlobalCustomFieldTemplate() {
		return defaultGlobalCustomFieldTemplate;
	}

	/**
	 * @param defaultGlobalCustomFieldTemplate the defaultGlobalCustomFieldTemplate to set
	 */
	public void setDefaultGlobalCustomFieldTemplate(
			String defaultGlobalCustomFieldTemplate) {
		this.defaultGlobalCustomFieldTemplate = defaultGlobalCustomFieldTemplate;
	}

}