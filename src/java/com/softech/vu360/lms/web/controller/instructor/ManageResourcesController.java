package com.softech.vu360.lms.web.controller.instructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.model.instructor.AddResourceForm;
import com.softech.vu360.lms.web.controller.model.instructor.CustomFieldForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.AddResourceValidator;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ManageResourcesSort;
import com.softech.vu360.util.VU360Branding;

public class ManageResourcesController  extends VU360BaseMultiActionController {

	private ResourceService resourceService;
	private AccreditationService accreditationService = null;

	private String searchResourceTemplate = null;
	private String editResourceTemplate = null;
	private String showResourceCustomFieldTemplate = null;
	private String finishTemplate = null;
	private String displayCustomFieldTemplate = null;
	private	String closeResourceTemplate = null;

	public static final String CUSTOMFIELD_ENTITY_RESOURCE = "CUSTOMFIELD_RESOURCE";

	HttpSession session = null;

	private static final Logger log = Logger.getLogger(ManageResourcesController.class.getName());

	public ManageResourcesController() {
		super();
	}

	public ManageResourcesController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		log.debug(" IN handleNoSuch Method ");
		Map<Object, Object> context = new HashMap<Object, Object>();
		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long contentOwnerId = 0l;
			if( loggedInUser.getLearner().getCustomer().getContentOwner().getId() != null ) {
				contentOwnerId = loggedInUser.getLearner().getCustomer().getContentOwner().getId();
			}
			List<ResourceType> resourseTypes = resourceService.getAllResourceTypes(contentOwnerId);
			List<String> rTypes = new ArrayList<String>();
			if ( resourseTypes != null ) {
				for( ResourceType r : resourseTypes ) {
					rTypes.add(r.getName());
				}
			}
			context.put("rTypes", rTypes);
		} catch ( Exception e) {
			log.debug(e);
		}
		return searchResources(request,response,null,null);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		log.debug(" IN onBind Method ");
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
		getPrincipal();
		if( command instanceof  AddResourceForm ){
			AddResourceForm form = (AddResourceForm)command;

			if( methodName.equals("editResource") 
					|| methodName.equals("showResourceCustomField")) {
				Resource resource = null;
				List<ResourceType> resourceTypes=null;
				String id = request.getParameter("id");
				if( id != null ) {
					resource = resourceService.loadForUpdateResource(Long.parseLong(id));
					resourceTypes = resourceService.getAllResourceTypes(loggedInUser.getLearner().getCustomer().getContentOwner().getId());
					form.setResource(resource);
					form.setResourceTypes(resourceTypes);
					form.setResourceTypeId(resource.getResourceType().getId());
					form.setId(Long.parseLong(id));
				} else {
					resource = resourceService.getResourceById(form.getId());
					form.setResource(resource);
				}

				if (resource != null){

					//if (form.getCustomFields().size()!=credential.getCustomfields().size()){
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = resource.getCustomfieldValues();
					List<CustomField> credentialCustomFieldList = resource.getCustomfields();
					//List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_RESOURCE), "", "");

					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();

					/*for (CustomField globalCustomField : globalCustomFieldList){
						totalCustomFieldList.add(globalCustomField);
					}*/
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
			if(methodName.equals("displayCustomFieldForEdit")){
				CustomField customField = accreditationService.getCustomFieldById(form.getCustomFieldId());
				form.setCustomField(customField);
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

	public ModelAndView searchResources(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception{
		try {
			HttpSession session= request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long contentOwnerId = 0l;
			if( loggedInUser.getLearner().getCustomer().getContentOwner().getId() != null ) {
				contentOwnerId = loggedInUser.getLearner().getCustomer().getContentOwner().getId();
			}
			List<Resource> resourceList = new ArrayList<Resource>();
			List<ResourceType> resourseTypes = resourceService.getAllResourceTypes(contentOwnerId);
			List<String> rTypes = new ArrayList<String>();
			if(resourseTypes!=null){
				for( ResourceType r : resourseTypes ) {
					rTypes.add(r.getName());
				}
			}
			String resourceName = "";
			String tagNumber = "";
			String resourceType = "";
			if( request.getParameter("showAll") != null && request.getParameter("showAll").equalsIgnoreCase("true") )
			{
				resourceName = (session.getAttribute("resourceName") == null) ? "" : session.getAttribute("resourceName").toString();
				tagNumber = (session.getAttribute("tagNumber")== null) ? "" : session.getAttribute("tagNumber").toString();
				resourceType = (session.getAttribute("resourceType")== null) ? "All" : session.getAttribute("resourceType").toString(); 
			} else {
				resourceName = (request.getParameter("resourceName") == null) ? "" : request.getParameter("resourceName");
				tagNumber = (request.getParameter("tagNumber") == null) ? "" : request.getParameter("tagNumber");
				if( request.getParameter("sorting") != null && request.getParameter("sorting").equalsIgnoreCase("true") ) {
					resourceType = (session.getAttribute("resourceType")== null) ? "All" : session.getAttribute("resourceType").toString();
				} else {
					resourceType = (request.getParameter("resourceType") == null) ? "All" : request.getParameter("resourceType");
				}
				session.setAttribute("resourceName", resourceName);
				session.setAttribute("tagNumber", tagNumber);
				session.setAttribute("resourceType", resourceType);
			}
			//appName = HtmlEncoder.escapeHtmlFull(resourceName).toString();
			resourceName = HtmlEncoder.escapeHtmlFull(resourceName).toString();
			tagNumber = HtmlEncoder.escapeHtmlFull(tagNumber).toString();
			resourceType = HtmlEncoder.escapeHtmlFull(resourceType).toString();
			if(resourseTypes!=null){
				for( ResourceType r : resourseTypes ) {
					if( r.getName().equalsIgnoreCase(resourceType) ) {
						resourceType = r.getId().toString();
						break;
					}
				}
			}
			context.put("resourceName", resourceName);
			context.put("tagNumber", tagNumber);
			context.put("resourceType", resourceType);
			context.put("rTypes", rTypes);

			if(resourseTypes!=null && resourceType.equalsIgnoreCase("All") && !resourseTypes.isEmpty() ) {
				resourceList = resourceService.findResources(loggedInUser.getLearner().getCustomer().getContentOwner().getId(),resourceName,tagNumber,resourseTypes.get(0).getId().toString());
				for( int i = 1 ; i < resourseTypes.size() ; i ++ ) {
					resourceList.addAll(resourceService.findResources(loggedInUser.getLearner().getCustomer().getContentOwner().getId(),resourceName,tagNumber,resourseTypes.get(i).getId().toString()));
				}
			}
			if( !resourceType.equalsIgnoreCase("All") ) {
				resourceList = resourceService.findResources(loggedInUser.getLearner().getCustomer().getContentOwner().getId(),resourceName,tagNumber,resourceType);
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
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				}else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("tagNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("tagNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				} 
			}	
			context.put("resourceList", resourceList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll",  (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			context.put("deleteError", false);
			if( session.getAttribute("deleteError") != null )
				context.put("deleteError", session.getAttribute("deleteError"));	
			
			session.removeAttribute("deleteError");
			return new ModelAndView(searchResourceTemplate,"context",context);

		}catch( Exception e ){
			// do nothing
		}
		return new ModelAndView(searchResourceTemplate);
	}

	public ModelAndView editResource(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception{

		return new ModelAndView(editResourceTemplate);
	}

	public ModelAndView saveResource(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors ) throws Exception{

		AddResourceForm form = (AddResourceForm)command;
		try {
			log.debug("In saveResource .............");
			if(errors.hasErrors()){

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

					}else {
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
				// Now re set the multi select option
				form.setCustomFields(customFieldList);

				return new ModelAndView(editResourceTemplate);
			}
			Resource resource = form.getResource();
			log.debug("form.getAssetTagNumber() ............." +  resource.getAssetTagNumber());

			for(ResourceType resourceType: form.getResourceTypes()){
				if(resourceType.getId().longValue() ==  form.getResourceTypeId().longValue()){
					resource.setResourceType(resourceType);
					break;
				}
			}

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
					resource.setCustomfieldValues(customFieldValues);
				}
			}
			resource = resourceService.saveResource(resource);

		} catch (Exception e) {
			//log.debug("exception", e);
		}
		return new ModelAndView(finishTemplate);
	}

	public ModelAndView deleteResources( HttpServletRequest request,HttpServletResponse response,Object command,BindException errors ) throws Exception {

		String[] selectedResourceIds = request.getParameterValues("selectedResources");

		if( selectedResourceIds != null ) {
			long resourceIdArray[] = new long[selectedResourceIds.length];
			boolean hasSchedule = false;
			for( int loop = 0 ; loop < selectedResourceIds.length ; loop++ ) {
				String delResourceId = selectedResourceIds[loop];
				if( delResourceId != null ) {
					List<SynchronousClass> schedules = resourceService.findResourceAssociatedSynchronousClass(delResourceId);
					if( schedules == null || schedules.size() == 0 ) {
						resourceIdArray[loop] = Long.parseLong(delResourceId);
					} else {
						hasSchedule = true;
					}
				}
			}
			if( hasSchedule ) {
				session=request.getSession();
				session.setAttribute("deleteError", "error.instructor.deleteResource.associated");
			} else {
				resourceService.deleteResource(resourceIdArray);
			}
		}
		return (this.searchResources(request, response, command, errors));
	}


	public ModelAndView showResourceCustomField(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors ) throws Exception{
		Map<Object, Object> context = new HashMap<Object, Object>();
		try {
			AddResourceForm form = (AddResourceForm)command;
			List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
			if (form.getResource().getCustomfields() != null && form.getResource().getCustomfields().size() > 0) {
				for (CustomField customField : form.getResource().getCustomfields()) {
					ManageCustomField manageCustomField = new ManageCustomField();
					manageCustomField.setFieldName(customField.getFieldLabel());
					manageCustomField.setId(customField.getId());
					if (customField instanceof SingleLineTextCustomFiled) {
						manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
					} else if (customField instanceof DateTimeCustomField) {
						manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_DATE);
					} else if (customField instanceof MultipleLineTextCustomfield) {
						manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
					} else if (customField instanceof NumericCusomField) {
						manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_NUMBER);
					} else if (customField instanceof SSNCustomFiled) {
						manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
					} else if (customField instanceof SingleSelectCustomField) {
						manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_RADIO_BUTTON);
					} else if (customField instanceof MultiSelectCustomField) {
						if (((MultiSelectCustomField) customField).getCheckBox())
							manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_CHECK_BOX);
						else 
							manageCustomField.setFieldType(CustomFieldForm.CUSTOMFIELD_CHOOSE);
					}
					manageCustomFieldList.add(manageCustomField);
				}
				manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);

				form.setManageCustomField(manageCustomFieldList);
			} else {
				form.setManageCustomField(manageCustomFieldList);
			}
		} catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(showResourceCustomFieldTemplate, "context", context);
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

		session = request.getSession();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();

		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( manageCustomFieldList.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}

		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null ) {
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		} else if (sortColumnIndex == null && session.getAttribute("sortColumnIndex") == null ) {
			context.put("sortColumnIndex", "");
		}
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null ) {
			sortDirection = session.getAttribute("sortDirection").toString();
		} else if (sortColumnIndex == null && session.getAttribute("sortDirection") == null ) {
			context.put("sortDirection", "");
		}
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
		return manageCustomFieldList;
	}

	@SuppressWarnings("static-access")
	public ModelAndView displayCustomFieldForEdit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		AddResourceForm form = (AddResourceForm)command;
		CustomField customField = form.getCustomField();
		if (customField instanceof SingleLineTextCustomFiled) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled)form.getCustomField();
			form.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
			form.setFieldLabel(singleLineTextCustomFiled.getFieldLabel());
			form.setFieldEncrypted(singleLineTextCustomFiled.getFieldEncrypted());
			form.setFieldRequired(singleLineTextCustomFiled.getFieldRequired());
			form.setCustomFieldDescription(singleLineTextCustomFiled.getCustomFieldDescription());
		} else if (customField instanceof DateTimeCustomField) {
			DateTimeCustomField dateTimeCustomField = (DateTimeCustomField)form.getCustomField();
			form.setFieldLabel(dateTimeCustomField.getFieldLabel());
			form.setFieldEncrypted(dateTimeCustomField.getFieldEncrypted());
			form.setFieldRequired(dateTimeCustomField.getFieldRequired());
			form.setCustomFieldDescription(dateTimeCustomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_DATE);
		} else if (customField instanceof MultipleLineTextCustomfield) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield)form.getCustomField();
			form.setFieldLabel(multipleLineTextCustomfield.getFieldLabel());
			form.setFieldEncrypted(multipleLineTextCustomfield.getFieldEncrypted());
			form.setFieldRequired(multipleLineTextCustomfield.getFieldRequired());
			form.setCustomFieldDescription(multipleLineTextCustomfield.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
		} else if (customField instanceof NumericCusomField) {
			NumericCusomField numericCusomField = (NumericCusomField)form.getCustomField();
			form.setFieldLabel(numericCusomField.getFieldLabel());
			form.setFieldEncrypted(numericCusomField.getFieldEncrypted());
			form.setFieldRequired(numericCusomField.getFieldRequired());
			form.setCustomFieldDescription(numericCusomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_NUMBER);
		} else if (customField instanceof SSNCustomFiled) {
			SSNCustomFiled sSNCustomFiled = (SSNCustomFiled)form.getCustomField();
			form.setFieldLabel(sSNCustomFiled.getFieldLabel());
			form.setFieldEncrypted(sSNCustomFiled.getFieldEncrypted());
			form.setFieldRequired(sSNCustomFiled.getFieldRequired());
			form.setCustomFieldDescription(sSNCustomFiled.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
		} else if (customField instanceof SingleSelectCustomField) {
			SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField)form.getCustomField();
			form.setFieldLabel(singleSelectCustomField.getFieldLabel());
			form.setFieldEncrypted(singleSelectCustomField.getFieldEncrypted());
			form.setFieldRequired(singleSelectCustomField.getFieldRequired());
			List<CustomFieldValueChoice> options = accreditationService.getOptionsByCustomField(singleSelectCustomField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(singleSelectCustomField.getAlignment())){
				if (singleSelectCustomField.getAlignment().equalsIgnoreCase(singleSelectCustomField.VERTICAL))
					form.setAlignment(false);
			}
			form.setCustomFieldDescription(singleSelectCustomField.getCustomFieldDescription());
			form.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
		} else if (customField instanceof MultiSelectCustomField) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)form.getCustomField();
			form.setFieldLabel(multiSelectCustomField.getFieldLabel());
			form.setFieldEncrypted(multiSelectCustomField.getFieldEncrypted());
			form.setFieldRequired(multiSelectCustomField.getFieldRequired());
			List<CustomFieldValueChoice> options = accreditationService.getOptionsByCustomField(multiSelectCustomField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(multiSelectCustomField.getAlignment())){
				if (multiSelectCustomField.getAlignment().equalsIgnoreCase(multiSelectCustomField.VERTICAL))
					form.setAlignment(false);
			}
			form.setCustomFieldDescription(multiSelectCustomField.getCustomFieldDescription());
			if (multiSelectCustomField.getCheckBox()){
				form.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
			}else{
				form.setFieldType(form.CUSTOMFIELD_CHOOSE);
			}
		} 
		return new ModelAndView(displayCustomFieldTemplate);
	}

	@SuppressWarnings("static-access")
	public ModelAndView saveCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		if(errors.hasErrors()){
			return new ModelAndView(displayCustomFieldTemplate);
		}
		AddResourceForm form = (AddResourceForm)command;
		CustomField customField = form.getCustomField();
		if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled)customField;
			singleLineTextCustomFiled.setCustomFieldDescription(form.getCustomFieldDescription());
			singleLineTextCustomFiled.setFieldEncrypted(form.isFieldEncrypted());
			singleLineTextCustomFiled.setFieldLabel(form.getFieldLabel().trim());
			singleLineTextCustomFiled.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(singleLineTextCustomFiled);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_DATE)) {
			DateTimeCustomField dateTimeCustomField =  (DateTimeCustomField)form.getCustomField();
			dateTimeCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			dateTimeCustomField.setFieldEncrypted(form.isFieldEncrypted());
			dateTimeCustomField.setFieldLabel(form.getFieldLabel().trim());
			dateTimeCustomField.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(dateTimeCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield)customField;
			multipleLineTextCustomfield.setCustomFieldDescription(form.getCustomFieldDescription());
			multipleLineTextCustomfield.setFieldEncrypted(form.isFieldEncrypted());
			multipleLineTextCustomfield.setFieldLabel(form.getFieldLabel().trim());
			multipleLineTextCustomfield.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(multipleLineTextCustomfield);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_NUMBER)) {
			NumericCusomField numericCusomField = (NumericCusomField)form.getCustomField();
			numericCusomField.setCustomFieldDescription(form.getCustomFieldDescription());
			numericCusomField.setFieldEncrypted(form.isFieldEncrypted());
			numericCusomField.setFieldLabel(form.getFieldLabel().trim());
			numericCusomField.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(numericCusomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCustomFiled sSNCustomFiled = (SSNCustomFiled)customField;
			sSNCustomFiled.setCustomFieldDescription(form.getCustomFieldDescription());
			sSNCustomFiled.setFieldEncrypted(form.isFieldEncrypted());
			sSNCustomFiled.setFieldLabel(form.getFieldLabel().trim());
			sSNCustomFiled.setFieldRequired(form.isFieldRequired());
			accreditationService.saveCustomField(sSNCustomFiled);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_RADIO_BUTTON)) {
			SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField)customField;
			singleSelectCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			singleSelectCustomField.setFieldEncrypted(form.isFieldEncrypted());
			singleSelectCustomField.setFieldLabel(form.getFieldLabel().trim());
			singleSelectCustomField.setFieldRequired(form.isFieldRequired());
			if (form.isAlignment())
				singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
			else
				singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeOption(singleSelectCustomField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(singleSelectCustomField);
				accreditationService.saveOption(option);
			}
			accreditationService.saveCustomField(singleSelectCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_CHOOSE)) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			multiSelectCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(form.isFieldEncrypted());
			multiSelectCustomField.setFieldRequired(form.isFieldRequired());
			multiSelectCustomField.setFieldLabel(form.getFieldLabel().trim());
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeOption(multiSelectCustomField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				accreditationService.saveOption(option);
			}
			accreditationService.saveCustomField(multiSelectCustomField);
		} else if (form.getFieldType().equalsIgnoreCase(form.CUSTOMFIELD_CHECK_BOX)) {
			MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField;
			multiSelectCustomField.setCustomFieldDescription(form.getCustomFieldDescription());
			multiSelectCustomField.setFieldEncrypted(form.isFieldEncrypted());
			multiSelectCustomField.setFieldRequired(form.isFieldRequired());
			multiSelectCustomField.setFieldLabel(form.getFieldLabel().trim());
			if (form.isAlignment())
				multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
			else
				multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeOption(multiSelectCustomField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CustomFieldValueChoice option = new CustomFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCustomField(multiSelectCustomField);
				accreditationService.saveOption(option);
			}
			accreditationService.saveCustomField(multiSelectCustomField);
		}
		if (StringUtils.isNotBlank(form.getEntity())){

			if (form.getEntity().equalsIgnoreCase(form.RESOURCE)) {
				return new ModelAndView(closeResourceTemplate);
			} 
		}
		return null;
	}

	public ModelAndView cancelEditCustomField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		return new ModelAndView(closeResourceTemplate);
	}

	/**
	 * This method delete the Resource custom field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteResourceCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		AddResourceForm form = (AddResourceForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		Resource resource = form.getResource();
		List<CustomField> customFields = resource.getCustomfields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		resource.setCustomfields(customFields);
		resourceService.saveResource(resource);

		context.put("target", "showResourceCustomField");
		return new ModelAndView(finishTemplate, "context", context);
	}


	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		AddResourceValidator validator = (AddResourceValidator)this.getValidator();
		AddResourceForm form = (AddResourceForm)command;


		if( methodName.equals("saveResource")) {
			validator.validateEditPage(form, errors);
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
		}

		if(methodName.equals("saveCustomField")){
			form.getCustomField().setFieldLabel(form.getFieldLabel());
			if (form.getCustomField() instanceof SingleSelectCustomField || form.getCustomField() instanceof MultiSelectCustomField) {
				this.readOptions(form);
			}
			validator.validateAddCustomFieldPage(form, errors);
		}
	}

	private String getOption(List<CustomFieldValueChoice> optionList) {
		String optionString="";
		//Collections.sort(optionList);
		for (CustomFieldValueChoice option : optionList) {
			optionString = optionString + option.getLabel() + "\n";
		}
		return optionString;
	}

	private void readOptions(AddResourceForm form){
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(form.getOption()));

		try {
			List<String> optionList = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						optionList.add(str);
					}
				}
			}
			form.setOptionList(optionList);
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}

	public ResourceService getResourceService() {
		return resourceService;
	}
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	public String getSearchResourceTemplate() {
		return searchResourceTemplate;
	}
	public void setSearchResourceTemplate(String searchResourceTemplate) {
		this.searchResourceTemplate = searchResourceTemplate;
	}

	public String getEditResourceTemplate() {
		return editResourceTemplate;
	}

	public void setEditResourceTemplate(String editResourceTemplate) {
		this.editResourceTemplate = editResourceTemplate;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	/**
	 * @return the showResourceCustomFieldTemplate
	 */
	public String getShowResourceCustomFieldTemplate() {
		return showResourceCustomFieldTemplate;
	}

	/**
	 * @param showResourceCustomFieldTemplate the showResourceCustomFieldTemplate to set
	 */
	public void setShowResourceCustomFieldTemplate(
			String showResourceCustomFieldTemplate) {
		this.showResourceCustomFieldTemplate = showResourceCustomFieldTemplate;
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
	 * @return the displayCustomFieldTemplate
	 */
	public String getDisplayCustomFieldTemplate() {
		return displayCustomFieldTemplate;
	}

	/**
	 * @param displayCustomFieldTemplate the displayCustomFieldTemplate to set
	 */
	public void setDisplayCustomFieldTemplate(String displayCustomFieldTemplate) {
		this.displayCustomFieldTemplate = displayCustomFieldTemplate;
	}

	/**
	 * @return the closeResourceTemplate
	 */
	public String getCloseResourceTemplate() {
		return closeResourceTemplate;
	}

	/**
	 * @param closeResourceTemplate the closeResourceTemplate to set
	 */
	public void setCloseResourceTemplate(String closeResourceTemplate) {
		this.closeResourceTemplate = closeResourceTemplate;
	}

}