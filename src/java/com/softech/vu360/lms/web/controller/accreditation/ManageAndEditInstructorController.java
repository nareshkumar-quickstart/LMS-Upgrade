package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ORMUtils;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.helper.InstructorSearchEnum;
import com.softech.vu360.lms.web.controller.model.accreditation.CustomFieldForm;
import com.softech.vu360.lms.web.controller.model.accreditation.InstructorForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditInstructorValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.InstructorSort;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 * created on 1st july 2009
 *
 */
public class ManageAndEditInstructorController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditInstructorController.class.getName());

	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private AccreditationService accreditationService;
//	HttpSession session = null;

	private String searchInstructorTemplate = null;
	private String editInstructorSummaryTemplate = null;
	private String redirectTemplate = null;


	public static final String CUSTOMFIELD_ENTITY_INSTRUCTOR = "CUSTOMFIELD_INSTRUCTOR";

	private String showInstructorCustomFieldTemplate = null;


	public ManageAndEditInstructorController() {
		super();
	}

	public ManageAndEditInstructorController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		request.setAttribute("newPage","true");

		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView(searchInstructorTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if( command instanceof InstructorForm ){
			InstructorForm form = (InstructorForm)command;
			if( methodName.equals("editInstructorSummary") 
					|| methodName.equals("showCustomField") ) {
				Instructor instructor = null;
				String id = request.getParameter("id");
				if( id != null ) {
					instructor = accreditationService.loadForUpdateInstructor(Long.parseLong(id));
					form.setInstructorId(Long.parseLong(id));
					form.setInstructor(instructor);
				} else {
					instructor = accreditationService.loadForUpdateInstructor(form.getInstructorId());;
					form.setInstructor(instructor);
				}
				if( instructor.getUser().getExpirationDate() != null ) {
					form.setExpirationDate(formatter.format(instructor.getUser().getExpirationDate()));
				} else {
					form.setExpirationDate("");
				}
				form.setLearner(instructor.getUser().getLearner());
				form.setProfile(instructor.getUser().getLearner().getLearnerProfile());

				if (instructor != null){

					//if (form.getCustomFields().size()!=credential.getCustomfields().size()){
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = instructor.getCustomfieldValues();
					List<CustomField> credentialCustomFieldList = instructor.getCustomfields();
					List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_INSTRUCTOR), "", "");

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

	public ModelAndView searchInstructor( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String firstName = (request.getParameter("firstName") == null) ? "" : request.getParameter("firstName");
			String lastName = (request.getParameter("lastName") == null) ? "" : request.getParameter("lastName");
			String eMailAdd = (request.getParameter("eMailAdd") == null) ? "" : request.getParameter("eMailAdd");
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			
			
			List<Instructor> instructors =new ArrayList<Instructor>();
			context.put("eMailAdd", eMailAdd);
			firstName = HtmlEncoder.escapeHtmlFull(firstName).toString();
			lastName = HtmlEncoder.escapeHtmlFull(lastName).toString();
			context.put("firstName", firstName);
			context.put("lastName", lastName);
			
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			
			String sortBy = null;
			
			if(sortColumnIndex != null && sortDirection != null){
				
				sortBy = InstructorSearchEnum.INSTRUCTOR.getSortBy(sortColumnIndex);
				
				context.put("sortDirection", sortDirection);
				context.put("sortColumnIndex", sortColumnIndex);
				
			}
			
			if(loggedInUser.getRegulatoryAnalyst()==null)
				instructors = accreditationService.findInstructor(firstName, lastName, eMailAdd, sortBy, sortDirection);
			else
				instructors = accreditationService.findInstructor(firstName, lastName, eMailAdd, loggedInUser, sortBy, sortDirection);
			
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);
			context.put("instructors", instructors);
			return new ModelAndView(searchInstructorTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchInstructorTemplate);
	}

	public ModelAndView deleteInstructor( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();

			if  ( request.getParameterValues("instructors") != null ) {
				long[] id = new long[request.getParameterValues("instructors").length];
				String []checkID = request.getParameterValues("instructors");
				for ( int i = 0 ; i < id.length ; i++ ) {
					id[i] = Long.valueOf(checkID[i]);
					Instructor instructor = accreditationService.getInstructorByID(id[i]);
					instructor.setActive(false);
					accreditationService.saveInstructor(instructor);
				}
				log.debug("TO BE DELETED ::- "+id.length);
			}
			List<Instructor> instructors = accreditationService.findInstructor("", "", "","","");
			context.put("instructors", instructors);
			return new ModelAndView(searchInstructorTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchInstructorTemplate);
	}


	public ModelAndView editInstructorSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			// do nothing
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editInstructorSummaryTemplate);
	}

	public ModelAndView saveInstructorSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		InstructorForm form = (InstructorForm)command;
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
				/*
				 * Now re set the multi select option
				 * */
				form.setCustomFields(customFieldList);

				return new ModelAndView(editInstructorSummaryTemplate);
			}
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Customer customer = null;
			if( loggedInUser.isLMSAdministrator() ) {
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
						getAuthentication().getDetails()).getCurrentCustomer();
			} else {
				customer = loggedInUser.getLearner().getCustomer();
			}

			Instructor instructor = form.getInstructor();
			LearnerProfile profile = form.getProfile();
			Learner learner = form.getLearner();
			VU360User user = form.getInstructor().getUser();
			
			// managing role...
			LMSRole lmsRole = vu360UserService.getRoleByName("LEARNER", customer);
			
			// loading user's existing LMSRoles to resolve No Session Lazy Initialization Exception for LMSRole in VU360User
			VU360User dbUser  = vu360UserService.getUserById(user.getId());
			dbUser.getLmsRoles().add(lmsRole);
			user.setLmsRoles(dbUser.getLmsRoles());

			// setting...
			instructor.setUser(user);
			instructor.setFirstName(user.getFirstName());
			instructor.setLastName(user.getLastName());
			user.setInstructor(instructor);
			user.setPassWordChanged(true);
			learner.setVu360User(user);
			profile.setLearner(learner);
			learnerService.updateLearnerProfile(profile);

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
					instructor.setCustomfieldValues(customFieldValues);
				}
			}
			accreditationService.saveInstructor(instructor);

			if( form.getEventSource().equalsIgnoreCase("false") ) {
				return new ModelAndView(editInstructorSummaryTemplate);
			} else {
				return new ModelAndView(searchInstructorTemplate);
			}

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchInstructorTemplate);
	}

	public ModelAndView showCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		InstructorForm form = (InstructorForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getInstructor().getCustomfields() != null && form.getInstructor().getCustomfields().size() > 0) {
			for (CustomField customField : form.getInstructor().getCustomfields()) {
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
		return new ModelAndView(showInstructorCustomFieldTemplate, "context", context);
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
	 * This method delete the Instructor custom field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteInstructorCustomField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		InstructorForm form = (InstructorForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		Instructor instructor = form.getInstructor();
		List<CustomField> customFields = instructor.getCustomfields();
		for (int i=0 ; i < selectedCustomFieldId.length ;i++) {
			for (int j=0 ; j < customFields.size(); j++) {
				if (customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0) {
					customFields.remove(j);
					break;
				}
			}
		}
		instructor.setCustomfields(customFields);
		accreditationService.saveInstructor(instructor);

		context.put("target", "showCustomField");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		InstructorForm form = (InstructorForm)command; 
		EditInstructorValidator validator = (EditInstructorValidator)this.getValidator();

		if( methodName.equals("saveInstructorSummary") ) {
			if( form.getEventSource().equalsIgnoreCase("true")) {
				validator.validateSummaryPage(form, errors);
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
	public String getSearchInstructorTemplate() {
		return searchInstructorTemplate;
	}
	public void setSearchInstructorTemplate(String searchInstructorTemplate) {
		this.searchInstructorTemplate = searchInstructorTemplate;
	}
	public String getEditInstructorSummaryTemplate() {
		return editInstructorSummaryTemplate;
	}
	public void setEditInstructorSummaryTemplate(String editInstructorSummaryTemplate) {
		this.editInstructorSummaryTemplate = editInstructorSummaryTemplate;
	}
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	public LearnerService getLearnerService() {
		return learnerService;
	}
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the showInstructorCustomFieldTemplate
	 */
	public String getShowInstructorCustomFieldTemplate() {
		return showInstructorCustomFieldTemplate;
	}

	/**
	 * @param showInstructorCustomFieldTemplate the showInstructorCustomFieldTemplate to set
	 */
	public void setShowInstructorCustomFieldTemplate(
			String showInstructorCustomFieldTemplate) {
		this.showInstructorCustomFieldTemplate = showInstructorCustomFieldTemplate;
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