package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
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
import com.softech.vu360.lms.web.controller.model.instructor.LocationForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.LocationValidator;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.ManageLocationsSort;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Saptarshi
 */
public class ManageAndEditLocationController extends VU360BaseMultiActionController {
	
	private static final Logger log = Logger.getLogger(ManageAndEditLocationController.class.getName());
	
	private String showLocationCustomFieldTemplate;
	private AccreditationService accreditationService;
	private ResourceService resourceService = null;
	private String manageLocationTemplate = null;
	private String editLocationTemplate = null;
	private String showCustomFieldTemplate = null;
	private String redirectTemplate = null;
	HttpSession session = null;
	
	public ManageAndEditLocationController() {
		super();
	}

	public ManageAndEditLocationController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		log.debug(" IN handleNoSuch Method ");
		return searchLocation( request, response);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		/*if( command instanceof LocationForm ){
			LocationForm form = (LocationForm)command;
			if( methodName.equals("editLocation") 
					|| methodName.equals("showCustomField")){
				
				Location location=null;
				String id = request.getParameter("id");
				if( id != null ) {
					location = resourceService.loadForUpdateLocation(Long.parseLong(id));
					form.setLocation(location);
					form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
					form.setId(Long.parseLong(id));
				} else {
					//form.setLocation(resourceType);
				}
			} 
		}*/
		

		

		
		if( command instanceof LocationForm ){
			LocationForm form = (LocationForm)command;

			if( methodName.equals("editLocation")
					|| methodName.equals("showCustomField")){
				Location location=null;
				String id = request.getParameter("id");
				if( id != null ) {
					location = resourceService.loadForUpdateLocation(Long.parseLong(id));
					form.setId(Long.parseLong(id));
					form.setLocation(location);
				} else {
					location = resourceService.loadForUpdateLocation(form.getId());
					form.setLocation(location);
				}
				if (location != null){

					//if (form.getCustomFields().size()!=credential.getCustomfields().size()){
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = location.getCustomfieldValues();
					List<CustomField> credentialCustomFieldList = location.getCustomfields();
					//List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_PROVIDER), "", "");

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
					form.setCustomFields(customFieldList);
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
					
				}
				form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
			}
		}	
	
	
		
	}
	
	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		LocationValidator validator = (LocationValidator)this.getValidator();
		LocationForm form = (LocationForm)command;
		if( methodName.equals("saveLocation")) {
			validator.validateLocationPage(form, errors);
			if (form.getCustomFields().size()>0){
				validator.validateCustomFields(form.getCustomFields(), errors);
			}
		}
	}
	
	public ModelAndView searchLocation( HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//Long contentOwnerId;
			HttpSession session= request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			//Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
			getPrincipal();
			List<Location> locationList = new ArrayList<Location>();
			String locationName = null;
			String city = null;
			String state = null;
			String country = null;
			String zip = null;
			
			if (request.getParameter("showAll") !=null && request.getParameter("showAll").equalsIgnoreCase("true")  )
			{
				 locationName =(session.getAttribute("locationName") == null) ? "" : session.getAttribute("locationName").toString() ; 
				 city = (session.getAttribute("city") == null) ? "" : session.getAttribute("city").toString() ;
				 state = (session.getAttribute("state") == null) ? "" : session.getAttribute("state").toString() ;
				 country = (session.getAttribute("country") == null) ? "" : session.getAttribute("country").toString() ;
				 zip = (session.getAttribute("zip") == null) ? "" : session.getAttribute("zip").toString() ;
				
				
			}else{
				 locationName = (request.getParameter("locationName") == null) ? "" : request.getParameter("locationName");
				 city = (request.getParameter("city") == null) ? "" : request.getParameter("city");
				 state = (request.getParameter("state") == null) ? "" : request.getParameter("state");
				 country = (request.getParameter("country") == null) ? "" : request.getParameter("country");
				 zip = (request.getParameter("zip") == null) ? "" : request.getParameter("zip");
				 
				
				 session.setAttribute("locationName", locationName);
				 session.setAttribute("city", city);
				 session.setAttribute("state", state);
				 session.setAttribute("country", country);
				 session.setAttribute("zip", zip);
			}
			
			
			context.put("locationName", locationName);
			context.put("city", city);
			context.put("state", state);
			context.put("country", country);
			context.put("zip", zip);
			
			
			locationList=resourceService.findLocations(loggedInUser.getLearner().getCustomer().getContentOwner().getId(),locationName,city,state,country,zip);
		
			
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
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("locationName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("locationName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("city");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("city");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("state");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("state");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				}
			}	
			
			context.put("locationList", locationList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll", request.getParameter("showAll"));
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			context.put("deleteError", false);
			if( session.getAttribute("deleteError") != null )
				context.put("deleteError", session.getAttribute("deleteError"));
			
			session.removeAttribute("deleteError");
			return new ModelAndView(manageLocationTemplate,"context",context);
		}catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(manageLocationTemplate);
	}
	
	public ModelAndView deleteLocation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String[] selectedLocationIds = request.getParameterValues("location");
		boolean hasSchedule = false;
		if (selectedLocationIds != null) {
			long locationIdArray[] = new long[selectedLocationIds.length];
			for (int loop = 0; loop < selectedLocationIds.length; loop++) {
				String delLocationId = selectedLocationIds[loop];
				List<SynchronousClass> schedules = resourceService.findLocationAssociatedSynchronousClass(delLocationId);
				if( schedules == null || schedules.size() == 0 ) {
					locationIdArray[loop] = Long.parseLong(delLocationId);
				} else {
					hasSchedule = true;
				}
			}
			/*
			 * this will Inactive Resource location that is not associated to any schedule
			 */
			
			if( hasSchedule ) {
				session=request.getSession();
				session.setAttribute("deleteError", "error.instructor.deleteLocation.associated");
			} else {
				resourceService.deleteLocation(locationIdArray);
			}
		}
		
		return (this.searchLocation(request, response));
	}
	
	public ModelAndView editLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		LocationForm form = (LocationForm)command;
		Location location = new Location();
		String id = request.getParameter("id");
		if(id != null && id != ""){
			Long lid = Long.parseLong(id);
			location = resourceService.loadForUpdateLocation(lid);
		}
		
		form.setLocation(location);
		form.setCity(form.getLocation().getAddress().getCity());
		form.setCountry(form.getLocation().getAddress().getCountry());
		form.setState(form.getLocation().getAddress().getState());
		form.setZipcode(form.getLocation().getAddress().getZipcode());
		form.setStreetAddress(form.getLocation().getAddress().getStreetAddress());
		form.setStreetAddress2(form.getLocation().getAddress().getStreetAddress2());
		form.setDescription(form.getLocation().getDescription());
		
		return new ModelAndView(editLocationTemplate);
	}
	
	public ModelAndView saveLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		LocationForm form = (LocationForm)command;
		/*if(errors.hasErrors()){
			form.setLocation(form.getLocation());
			return new ModelAndView(// for checking the validation...
	public String eventSource = null;);
		}
		try {
			
			
			Location locationToUpdate=resourceService.loadForUpdateLocation(form.getLocation().getId());
			
			locationToUpdate.getAddress().setStreetAddress(form.getStreetAddress());
			locationToUpdate.getAddress().setStreetAddress2(form.getStreetAddress2());
			locationToUpdate.getAddress().setCity(form.getCity());
			locationToUpdate.getAddress().setCountry(form.getCountry());
			locationToUpdate.getAddress().setState(form.getState());
			locationToUpdate.getAddress().setZipcode(form.getZipcode());			
			locationToUpdate = resourceService.saveLocation(locationToUpdate);
			
		}catch (Exception e) {
			log.debug("exception: ", e);
		}*/
		
		
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

				return new ModelAndView(editLocationTemplate);
			}
			
			
			Location locationToUpdate=resourceService.loadForUpdateLocation(form.getLocation().getId());
            locationToUpdate.setName(form.getLocation().getName());
			locationToUpdate.getAddress().setStreetAddress(form.getStreetAddress());
			locationToUpdate.getAddress().setStreetAddress2(form.getStreetAddress2());
			locationToUpdate.getAddress().setCity(form.getCity());
			locationToUpdate.getAddress().setCountry(form.getCountry());
			locationToUpdate.getAddress().setState(form.getState());
			locationToUpdate.getAddress().setZipcode(form.getZipcode());
            locationToUpdate.setPhone(form.getLocation().getPhone());
			locationToUpdate.setDescription(form.getDescription());
			locationToUpdate = resourceService.saveLocation(locationToUpdate);
			

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
					locationToUpdate.setCustomfieldValues(customFieldValues);
				}
			}
			resourceService.saveLocation(locationToUpdate);

			if( form.getEventSource() != null &&  form.getEventSource().equalsIgnoreCase("false") ) {
				return new ModelAndView(editLocationTemplate);
			} else {
				return new ModelAndView(manageLocationTemplate);
			}

		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		
		return new ModelAndView(manageLocationTemplate);
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
	
	@SuppressWarnings("static-access")
	public ModelAndView showCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		LocationForm form = (LocationForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getLocation().getCustomfields() != null && form.getLocation().getCustomfields().size() > 0) {
			for (CustomField customField : form.getLocation().getCustomfields()) {
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
			String pageIndex="";
			if(request.getParameter("pageIndex")!=null){
			pageIndex = request.getParameter("pageIndex");
			}
			if( pageIndex != "0") {
			manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);
			}
			form.setManageCustomField(manageCustomFieldList);
		} else {
			form.setManageCustomField(manageCustomFieldList);
		}
		return new ModelAndView(showLocationCustomFieldTemplate, "context", context);
	}

	public ModelAndView deleteLocationCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		LocationForm form = (LocationForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		Location location = form.getLocation();
		List<CustomField> customFields = location.getCustomfields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		location.setCustomfields(customFields);
		resourceService.saveLocation(location);
		
		return showCustomField(request, response, command, errors);
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
	 * @return the manageLocationTemplate
	 */
	public String getManageLocationTemplate() {
		return manageLocationTemplate;
	}

	/**
	 * @param manageLocationTemplate the manageLocationTemplate to set
	 */
	public void setManageLocationTemplate(String manageLocationTemplate) {
		this.manageLocationTemplate = manageLocationTemplate;
	}

	/**
	 * @return the editLocationTemplate
	 */
	public String getEditLocationTemplate() {
		return editLocationTemplate;
	}

	/**
	 * @param editLocationTemplate the editLocationTemplate to set
	 */
	public void setEditLocationTemplate(String editLocationTemplate) {
		this.editLocationTemplate = editLocationTemplate;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getShowLocationCustomFieldTemplate() {
		return showLocationCustomFieldTemplate;
	}

	public void setShowLocationCustomFieldTemplate(
			String showLocationCustomFieldTemplate) {
		this.showLocationCustomFieldTemplate = showLocationCustomFieldTemplate;
	}


	public String getShowCustomFieldTemplate() {
		return showCustomFieldTemplate;
	}

	public void setShowCustomFieldTemplate(String showCustomFieldTemplate) {
		this.showCustomFieldTemplate = showCustomFieldTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}


}