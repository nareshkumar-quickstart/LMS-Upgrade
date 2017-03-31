package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.accreditation.ProviderForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddProviderValidator;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ProviderSort;
import com.softech.vu360.util.VU360Branding;

/**
 * @author PG
 * 
 */
public class ManageAndEditProviderController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditInstructorController.class.getName());

	private AccreditationService accreditationService;
//	HttpSession session = null;

	private String searchProviderTemplate = null;
	private String editProviderTemplate = null;
	private String showProviderCustomFieldTemplate = null;
	private String redirectTemplate = null;
	public static final String CUSTOMFIELD_ENTITY_PROVIDER = "CUSTOMFIELD_PROVIDER";



	public ManageAndEditProviderController() {
		super();
	}

	public ManageAndEditProviderController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		request.setAttribute("newPage","true");
		return new ModelAndView(searchProviderTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		
		if( command instanceof ProviderForm ){
			ProviderForm form = (ProviderForm)command;

			if( methodName.equals("editProvider")
					|| methodName.equals("showCustomField")){
				Provider provider = null;
				String id = request.getParameter("providerId");
				if( id != null ) {
					provider = accreditationService.loadForUpdateProvider(Long.parseLong(id));
					form.setProvId(Long.parseLong(id));
					form.setProvider(provider);
				} else {
					provider = accreditationService.loadForUpdateProvider(form.getProvId());
					form.setProvider(provider);
				}
				if (provider != null){

					//if (form.getCustomFields().size()!=credential.getCustomfields().size()){
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = provider.getCustomfieldValues();
					List<CustomField> credentialCustomFieldList = provider.getCustomfields();
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_PROVIDER), "", "");

					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();

					for (CustomField globalCustomField : globalCustomFieldList){
						totalCustomFieldList.add(globalCustomField);
					}
					for (CustomField customField : credentialCustomFieldList){
						totalCustomFieldList.add(customField);
					}

					Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

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

					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();

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
					form.setCustomFields(customFieldList);
				}
				form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
			}
		}	
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

	public ModelAndView searchProvider( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			String name = (request.getParameter("providerName") == null) ? "" : request.getParameter("providerName");
			List<Provider> providers = accreditationService.findProviders(name,loggedInUser.getRegulatoryAnalyst());

			name = HtmlEncoder.escapeHtmlFull(name).toString();
			context.put("name", name);
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against regulator name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ProviderSort providerSort = new ProviderSort();
						providerSort.setSortBy("name");
						providerSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(providers,providerSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ProviderSort providerSort = new ProviderSort();
						providerSort.setSortBy("name");
						providerSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(providers,providerSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against alias
				}
			}
			context.put("providers", providers);
			return new ModelAndView(searchProviderTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchProviderTemplate);
	}

	public ModelAndView deleteProvider( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			if  ( request.getParameterValues("providers") != null ) {
				long[] id = new long[request.getParameterValues("providers").length];
				String []checkID = request.getParameterValues("providers");
				for ( int i = 0 ; i < id.length ; i++ ) {
					id[i] = Long.valueOf(checkID[i]);
					//Provider provider = accreditationService.getProviderById(id[i]);
					Provider provider = accreditationService.loadForUpdateProvider(id[i]);//LMS-15622
					provider.setActive(false);
					accreditationService.saveProvider(provider);
				}
				log.debug("TO BE DELETED ::- "+id.length);
			}
			List<Provider> providers = accreditationService.findProviders("", loggedInUser.getRegulatoryAnalyst());
			context.put("providers", providers);
			return new ModelAndView(searchProviderTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchProviderTemplate);
	}

	public ModelAndView editProvider( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			// do nothing
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editProviderTemplate);
	}

	public ModelAndView saveProvider( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		ProviderForm form = (ProviderForm)command;
		try {
			if( errors.hasErrors() ) {

				CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
				Map<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomField>  customFieldMap = new HashMap<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
				for(com.softech.vu360.lms.web.controller.model.customfield.CustomField tempcustomField:form.getCustomFields()){

					CustomField customField = tempcustomField.getCustomFieldRef();
					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
					CustomFieldValue tempCustomFieldValue = tempcustomField.getCustomFieldValueRef();
					tempCustomFieldValue.setCustomField(customField);
					customFieldValues.add(tempcustomField.getCustomFieldValueRef());

					if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField){
						List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
						fieldBuilder.buildCustomField(customField,tempcustomField.getStatus(),customFieldValues,customFieldValueChoices);

						if (customField instanceof MultiSelectCustomField){
							customFieldMap.put(tempcustomField.getCustomFieldRef().getId(), tempcustomField);
						}

					} else {
						fieldBuilder.buildCustomField(customField,tempcustomField.getStatus(),customFieldValues);
					}
				}

				List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
				for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldList){
					if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){

						com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField = customFieldMap.get(customField.getCustomFieldRef().getId());
						if (tempCustomField != null){

							Map<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice> tempCustomFieldValueChoiceMap = new HashMap<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice>();

							for(com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice tempCustomFieldValueChoice:tempCustomField.getCustomFieldValueChoices()){
								tempCustomFieldValueChoiceMap.put(tempCustomFieldValueChoice.getCustomFieldValueChoiceRef().getId(), tempCustomFieldValueChoice);
							}

							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice:customField.getCustomFieldValueChoices()){
								if (tempCustomFieldValueChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
									customFieldValueChoice.setSelected(tempCustomFieldValueChoiceMap.get(customFieldValueChoice.getCustomFieldValueChoiceRef().getId()).isSelected());
								}
							}
						}
					}
				}
				/*
				 * Now re set the multi select option
				 * */
				form.setCustomFields(customFieldList);

				return new ModelAndView(editProviderTemplate);
			}
			Provider provider = form.getProvider();

			if (form.getCustomFields().size()>0){
				List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
				for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

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
							CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
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

							CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
							customFieldValue.setCustomField(customField.getCustomFieldRef());
							customFieldValue.setValueItems(customFieldValueChoices);

							customFieldValues.add(customFieldValue);

						}

					}else {
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField.getCustomFieldRef());

						customFieldValues.add(customFieldValue);
					}
				}
				if (customFieldValues.size()>0){
					provider.setCustomfieldValues(customFieldValues);
				}
			}
			
			String countryChange = request.getParameter("countryChange");
			if(StringUtils.isEmpty(countryChange))    
					accreditationService.saveProvider(provider);

			if( form.getEventSource().equalsIgnoreCase("false") ) {
				return new ModelAndView(editProviderTemplate);
			} else {
				return new ModelAndView(searchProviderTemplate);
			}

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchProviderTemplate);
	}

	@SuppressWarnings("static-access")
	public ModelAndView showCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		ProviderForm form = (ProviderForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getProvider().getCustomfields() != null && form.getProvider().getCustomfields().size() > 0) {
			for (CustomField customField : form.getProvider().getCustomfields()) {
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
		return new ModelAndView(showProviderCustomFieldTemplate, "context", context);
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
	 * This method delete the Provider custom field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteProviderCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ProviderForm form = (ProviderForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		Provider provider = form.getProvider();
		List<CustomField> customFields = provider.getCustomfields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		provider.setCustomfields(customFields);
		accreditationService.saveProvider(provider);

		context.put("target", "showCustomField");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	protected void validate( HttpServletRequest request, Object command,
			BindException errors, String methodName ) throws Exception {
		AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		ProviderForm form = (ProviderForm)command;

		if( methodName.equals("saveProvider")) {
			if( form.getEventSource().equalsIgnoreCase("true")) {
				validator.validateFirstPage(form, errors);
				if (form.getCustomFields().size()>0){
					validator.validateCustomFields(form.getCustomFields(), errors);
				}
			}
		}
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getSearchProviderTemplate() {
		return searchProviderTemplate;
	}

	public void setSearchProviderTemplate(String searchProviderTemplate) {
		this.searchProviderTemplate = searchProviderTemplate;
	}

	public String getEditProviderTemplate() {
		return editProviderTemplate;
	}

	public void setEditProviderTemplate(String editProviderTemplate) {
		this.editProviderTemplate = editProviderTemplate;
	}

	/**
	 * @return the showProviderCustomFieldTemplate
	 */
	public String getShowProviderCustomFieldTemplate() {
		return showProviderCustomFieldTemplate;
	}

	/**
	 * @param showProviderCustomFieldTemplate the showProviderCustomFieldTemplate to set
	 */
	public void setShowProviderCustomFieldTemplate(
			String showProviderCustomFieldTemplate) {
		this.showProviderCustomFieldTemplate = showProviderCustomFieldTemplate;
	}

	/**
	 * @return the redirectTemplate
	 */
	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	/**
	 * @param redirectTemplate the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

}