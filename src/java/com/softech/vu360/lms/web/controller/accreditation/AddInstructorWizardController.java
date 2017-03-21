package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.InstructorForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddInstructorValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 * created on 1st july 2009
 *
 */
public class AddInstructorWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddInstructorWizardController.class.getName());

	private VU360UserService vu360UserService;
	private AccreditationService accreditationService;
	private LearnerService learnerService;
	private CustomerService customerService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private SecurityAndRolesService securityAndRolesService;

	private String cancelTemplate = null;
	private String finishTemplate = null;

	public static final String CUSTOMFIELD_ENTITY_INSTRUCTOR = "CUSTOMFIELD_INSTRUCTOR";

	public AddInstructorWizardController() {
		super();
		setCommandName("instructorForm");
		setCommandClass(InstructorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/addInstructor/add_instructorStep1"
				, "accreditation/approvals/addInstructor/add_instructorStep2"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			InstructorForm form = (InstructorForm) command;
			Instructor instructor = new Instructor();
			VU360User user = new VU360User();
			Learner learner = new Learner();
			LearnerProfile profile = new LearnerProfile();
			Address add1 = new Address();
			Address add2 = new Address();
			profile.setLearnerAddress(add1);
			profile.setLearnerAddress2(add2);
			instructor.setUser(user);
			form.setInstructor(instructor);
			form.setLearner(learner);
			form.setProfile(profile);
			form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		InstructorForm form = (InstructorForm) command;
		switch(page){

		case 0:
			if (!errors.hasErrors() && form.getCustomFields().size()==0){
				List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_INSTRUCTOR), "", "");
				CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
				List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();

				for(CustomField customField : globalCustomFieldList){
					if (customField instanceof SingleSelectCustomField || 
							customField instanceof MultiSelectCustomField ){

						List<CustomFieldValueChoice> customFieldValueChoices=this.getAccreditationService().getOptionsByCustomField(customField);
						fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

					} else {
						fieldBuilder.buildCustomField(customField,0,customFieldValues);
					}
				}
				List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
				form.setCustomFields(customFieldList);
			}
			break;
		case 1:
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (!multiSelectCustomField.getCheckBox()){

						if(customField.getSelectedChoices()!=null){

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
									if(selectedChoiceIdString.equalsIgnoreCase(customFieldValueChoice.getCustomFieldValueChoiceRef().getKey())){
										customFieldValueChoice.setSelected(true);
									}
								}
							}
						}
					}
				}
			}
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	/**
	 * method used to correct the country and state labels in form bean.
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) 
	throws Exception {

		Brander b = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		InstructorForm form = (InstructorForm) command;
		List<LabelBean> countries = b.getBrandMapElements("lms.manageUser.AddLearner.Country");
		String stateBean = "";
		if( page == 0 ) {
			if( form.getEventSource().equalsIgnoreCase("true") ) {

				String country1 = form.getProfile().getLearnerAddress().getCountry();
				String country2 = form.getProfile().getLearnerAddress2().getCountry();
				String state = form.getProfile().getLearnerAddress().getState();
				String state2 = form.getProfile().getLearnerAddress2().getState();
				List<LabelBean> states = null;

				for( LabelBean bean : countries ) {
					if( country1.equalsIgnoreCase(bean.getValue()) || country1.equalsIgnoreCase(bean.getLabel()) ) {
						stateBean = bean.getValue();
						country1 = bean.getLabel();
						form.getProfile().getLearnerAddress().setCountry(country1);
						if( stateBean.isEmpty() || stateBean.equalsIgnoreCase("US") || stateBean.equalsIgnoreCase("United States") ) {
							states = b.getBrandMapElements("lms.manageUser.AddLearner.State");
						} else {
							states = b.getBrandMapElements("lms.manageUser.AddLearner."+stateBean+".State");
						}
						for( LabelBean bin : states ) {
							if( state.equalsIgnoreCase(bin.getValue()) || state.equalsIgnoreCase(bin.getLabel()) ) {
								state = bin.getLabel();
								form.getProfile().getLearnerAddress().setState(state);
								break;
							}
						} 
						break;
					}
				}
				for( LabelBean bean : countries ) {
					if( country2.equalsIgnoreCase(bean.getValue()) || country2.equalsIgnoreCase(bean.getLabel()) ) {
						stateBean = bean.getValue();
						country2 = bean.getLabel();
						form.getProfile().getLearnerAddress2().setCountry(country2);
						if( stateBean.isEmpty() || stateBean.equalsIgnoreCase("US") || stateBean.equalsIgnoreCase("United States") ) {
							states = b.getBrandMapElements("lms.manageUser.AddLearner.State");
						} else {
							states = b.getBrandMapElements("lms.manageUser.AddLearner."+stateBean+".State");
						}
						for( LabelBean bin : states ) {
							if( state2.equalsIgnoreCase(bin.getValue()) || state2.equalsIgnoreCase(bin.getLabel()) ) {
								log.debug("STATE - "+bin.getValue());
								log.debug("STATEEEE - "+bin.getLabel());
								state2 = bin.getLabel();
								form.getProfile().getLearnerAddress2().setState(state2);
								break;
							}
						} 
						break;
					}
				}
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {

		log.debug("IN processFinish");
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Customer customer = null;
		if( loggedInUser.isLMSAdministrator() ) {
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomer();
		} else {
			customer = loggedInUser.getLearner().getCustomer();
		}

		ContentOwner contentOwner = customer.getContentOwner();
		InstructorForm form = (InstructorForm) command;
		LearnerProfile profile = form.getProfile();
		Learner learner = form.getLearner();
		VU360User user = form.getInstructor().getUser();
		Instructor instructor = form.getInstructor();
		instructor.setContentOwner(contentOwner);
		// managing role...
		LMSRole lmsRole = vu360UserService.getRoleByName("LEARNER", customer);
		user.addLmsRole(lmsRole);
		
		// [12/18/2010] LMS-8205 :: Assign Instructor Role to Instructor
		lmsRole = this.securityAndRolesService.addSystemInstructorRoleIfNotExist(customer);
		user.addLmsRole(lmsRole);
		

		// setting...
		if( !StringUtils.isBlank(form.getExpirationDate()) ) {
			Date exDate = formatter.parse(form.getExpirationDate());
			user.setExpirationDate(exDate);
		}
		user.setPassWordChanged(true);
		user.setUserGUID(GUIDGeneratorUtil.generateGUID());
		learner.setVu360User(user);
		learner.setCustomer(customer);
		user.setLearner(learner);
		profile.setLearner(learner);
		learner.setLearnerProfile(profile);
		instructor.setUser(user);
		instructor.setFirstName(user.getFirstName());
		instructor.setLastName(user.getLastName());
		user.setInstructor(instructor);
		
		// LMS-8165 :: Assign learner to root org. group by default		
		OrganizationalGroup rootOrgGroup = this.orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
		List<OrganizationalGroup> orgGroupList = new ArrayList<OrganizationalGroup>();
		if (rootOrgGroup != null ) {
			orgGroupList.add( rootOrgGroup );
		}		
		
		learner = this.learnerService.addLearner(customer, orgGroupList, null, learner);

		instructor = accreditationService.getInstructorByID(instructor.getId());
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
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
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
						Object value = customFieldValue.getValue();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						/*
						 *  for Encryption...
						 */
						if( customField.getCustomFieldRef().getFieldEncrypted() ) {
							customFieldValue.setValue(value);
						}
						customFieldValues.add(customFieldValue);
					}

				} else {
					CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
					Object value = customFieldValue.getValue();
					customFieldValue.setCustomField(customField.getCustomFieldRef());
					/*
					 *  for Encryption...
					 */
					if( customField.getCustomFieldRef().getFieldEncrypted() ) {
						customFieldValue.setValue(value);
					}
					customFieldValues.add(customFieldValue);
				}
			}
			if (customFieldValues.size()>0){
				instructor.setCustomfieldValues(customFieldValues);
			}
		}
		accreditationService.saveInstructor(instructor);
		return new ModelAndView(finishTemplate);
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		AddInstructorValidator validator = (AddInstructorValidator)this.getValidator();
		InstructorForm form = (InstructorForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			if( form.getEventSource().equalsIgnoreCase("true") ) {
				validator.validateFirstPage(form, errors);
				if (form.getCustomFields().size()>0){
					validator.validateCustomFields(form.getCustomFields(), errors);
				}
			}
			break;
		case 1:
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}
	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	public String getFinishTemplate() {
		return finishTemplate;
	}
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
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

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @param orgGroupLearnerGroupService the orgGroupLearnerGroupService to set
	 */
	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	/**
	 * @return the orgGroupLearnerGroupService
	 */
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	/**
	 * @param securityAndRolesService the securityAndRolesService to set
	 */
	public void setSecurityAndRolesService(SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}

	/**
	 * @return the securityAndRolesService
	 */
	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}
	
}