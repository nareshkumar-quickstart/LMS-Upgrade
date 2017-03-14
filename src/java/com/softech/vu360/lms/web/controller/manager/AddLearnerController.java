package com.softech.vu360.lms.web.controller.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.LearnerDetailsForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.AddLearnerValidator;
import com.softech.vu360.lms.web.controller.validator.LearnerProfileValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;


public class AddLearnerController extends AbstractWizardFormController{
	
	private static final Logger log = Logger.getLogger(AddLearnerController.class.getName());

	private VU360UserService vu360UserService;
	private CustomFieldService customFieldService = null;
	private LearnerService learnerService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;
	private SecurityAndRolesService securityAndRolesService;
	private EntitlementService entitlementService;
	private EnrollmentService enrollmentService;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;

	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}

	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}

	public AddLearnerController() {
		super();
		setCommandName("learnerForm");
		setCommandClass(LearnerDetailsForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/addLearner/Step1"
				,"manager/userGroups/addLearner/Step2"
				,"manager/userGroups/addLearner/Step3"});
	}

	/**
	 * Step 1.
	 * We do not need to override this method now.
	 * This method basically lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * This is called first when a new request is made and then on
	 * every subsequent request. However in our case, 
	 * since the bindOnNewForm is true this 
	 * will NOT be called on subsequent requests...
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		LearnerDetailsForm learnerForm = (LearnerDetailsForm)command;
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<LMSRole> roleList = new ArrayList<LMSRole>();
		List<String> roleListNames = new ArrayList<String>();

		if (loggedInUserVO.isAdminMode()){
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
					.getAuthentication().getDetails()).getCurrentCustomerId();
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LMSADMINISTRATOR,customerId));
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_TRAININGMANAGER,customerId));
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LEARNER,customerId));
		}else if (loggedInUserVO.isManagerMode()){
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_TRAININGMANAGER,loggedInUserVO.getLearner().getCustomer().getId()));
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LEARNER,loggedInUserVO.getLearner().getCustomer().getId()));
		}
		for (LMSRole lmsRole : roleList){
			if( lmsRole.isDefaultForRegistration() == true) {
				learnerForm.setDefaultRoleName(lmsRole.getRoleName());
			}
			roleListNames.add(lmsRole.getRoleName());
		}
		learnerForm.setRoles(roleListNames);
		
		/*########################################################################################*/
		//for Custom Fields
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
				.getAuthentication().getDetails()).getCurrentCustomer();
		
		List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
		List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
		//customFieldValues = ;
		Distributor reseller = customer.getDistributor();
		CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
		customFieldValues =  learnerForm.getCustomFieldValueList() ;//vu360User.getLearner().getLearnerProfile().getCustomFieldValues() ;
		if( customer.getCustomFields()!= null) {
			totalCustomFieldList.addAll(customer.getCustomFields());
		}
			
		if( reseller.getCustomFields()!= null) {
			totalCustomFieldList.addAll(reseller.getCustomFields());
		}
		
		
	 
		
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
		
		learnerForm.setCustomFields(customFieldList2);

		/*#########################################################################################*/
		
		return command;
	}

	/**
	 * Step 2.
	 * We do not need to override this method now.
	 * This method lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * and just before the binder is initialized...
	 * We can have customized binders used here to interpret the request fields
	 * according to our requirements. It allows us to register 
	 * custom editors for certain fields.
	 * Called on the first request to this form wizard.
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		log.debug("IN initBinder");
		// Auto-generated method stub
		super.initBinder(request, binder);
	}

	/**
	 * Step 3.
	 * We do not need to override this method now.
	 * Since we have bindOnNewForm property set to true in the constructor
	 * this method will be called when the first request is processed.
	 * We can add custom implentation here later to populate the command object
	 * accordingly.
	 * Called on the first request to this form wizard.
	 */
	protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
		log.debug("IN onBindOnNewForm");
		// Auto-generated method stub
		super.onBindOnNewForm(request, command, binder);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		// Auto-generated method stub request, response, binder
		ModelAndView modelNView = super.showForm(request, response, binder);
		String view = modelNView.getViewName();
		log.debug("OUT showForm for view = "+view);
		return modelNView;
	}

	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		log.debug("IN referenceData");
		LearnerDetailsForm learnerForm = (LearnerDetailsForm)command;
		Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
				.getAuthentication().getDetails()).getCurrentCustomerId();
		Map model = new HashMap();
		switch(page){
		case 0:
			learnerForm.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));

			break;

		case 1:
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);


			//Create OrgGroups list
			LearnerDetailsForm learnerDetails = (LearnerDetailsForm)command;
			String[] orgGroupList = learnerDetails.getGroups();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if(orgGroupList!=null && orgGroupList.length>0){
				for(String orgGroup:orgGroupList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,loggedInUser);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();

			model.put("orgGroupTreeAsList", treeAsList);


			//Selected Learner Group List
			String[] selectedlearnerGroupList=learnerDetails.getSelectedLearnerGroups();

			if(selectedlearnerGroupList!=null && selectedlearnerGroupList.length>0){
				List<LearnerGroup> selectedLearnersGroupList = new ArrayList<LearnerGroup>();

				for(String selectedlearnerGroup:selectedlearnerGroupList){
					int index=selectedlearnerGroup.indexOf("-");
					String lgId=null;
					String lgName=null;
					if(index !=-1) {
						lgId=selectedlearnerGroup.substring(0,index);
						lgName=selectedlearnerGroup.substring(index+1,selectedlearnerGroup.length());
					}
					else
						selectedlearnerGroup=lgId;

					Long selectedlearnerGroupId = Long.parseLong(lgId);

					LearnerGroup learnerGroup=new LearnerGroup();//;=learnerService.getLearnerGroupById(selectedlearnerGroupId);
					learnerGroup.setId(selectedlearnerGroupId);
					learnerGroup.setName(lgName);
					selectedLearnersGroupList.add(learnerGroup);
				}
				model.put("selectedLearnersGroupList", selectedLearnersGroupList);
			}

			//Available Learner Group List
			String[] availablelearnerGroupList=learnerDetails.getAvailableLearnerGroups();

			if(availablelearnerGroupList!=null && availablelearnerGroupList.length>0){
				List<LearnerGroup> availableLearnersGroupList = new ArrayList<LearnerGroup>();

				for(String availablelearnerGroup:availablelearnerGroupList){
					int index=availablelearnerGroup.indexOf("-");
					String lgId=null;
					String lgName=null;
					if(index !=-1) {
						lgId=availablelearnerGroup.substring(0,index);
						lgName=availablelearnerGroup.substring(index+1,availablelearnerGroup.length());
					}
					else
						availablelearnerGroup=lgId;
					Long availablelearnerGroupId = Long.parseLong(lgId);

					LearnerGroup learnerGroup=new LearnerGroup();
					learnerGroup.setId(availablelearnerGroupId);
					learnerGroup.setName(lgName);
					availableLearnersGroupList.add(learnerGroup);
				}
				model.put("availableLearnersGroupList", availableLearnersGroupList);
			}

			return model;
		case 2:

			//Organization Group List Name

			LearnerDetailsForm learnerDetailsForm = (LearnerDetailsForm)command;

			String[] orgGroupsList = learnerDetailsForm.getGroups();
			String[] orgGroupNamesList = learnerDetailsForm.getGroupnames();
			List<Long> orgGroupsIdList = new ArrayList<Long>();
			List<String> orgGroupsNameList = new ArrayList<String>();
			if(orgGroupsList!=null && orgGroupsList.length>0){
				for(String orgGroup:orgGroupsList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupsIdList.add(orgGroupId);

				}
			}
			for(int count=0;count<orgGroupNamesList.length;count++) {
				orgGroupsNameList.add(orgGroupNamesList[count]);
			}

			model.put("orgGroupNameList", orgGroupsNameList);

			//Learner Group List Name

			String[] learnerGroupList=learnerDetailsForm.getSelectedLearnerGroups();

			List<Long> learnerGroupIdList = new ArrayList<Long>();
			List<String> learnerGroupNameList = new ArrayList<String>();
			if(learnerGroupList!=null && learnerGroupList.length>0){
				for(String learnerGroup:learnerGroupList){
					int index=learnerGroup.indexOf("-",0);

					String lgId=learnerGroup.substring(0,index);
					String lgName=learnerGroup.substring(index+1,learnerGroup.length());
					Long learnerGroupId = Long.parseLong(lgId);
					learnerGroupIdList.add(learnerGroupId);
					learnerGroupNameList.add(lgName);
				}
			}
			learnerDetailsForm.setRoleName(learnerDetailsForm.getDefaultRoleName());

			//KS - 2009-12-17 - LMS-3257 
			applyCountryStateLabel(learnerDetailsForm); 

			model.put("learnerGroupNameList", learnerGroupNameList);
			return model;


		default:
			break;
		}
		
		return super.referenceData(request, page);
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
	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage");
		LearnerDetailsForm learnerDetails = (LearnerDetailsForm)command;
		AddLearnerValidator addLearnerValidator = (AddLearnerValidator) getValidator();
		

		errors.setNestedPath("");
		switch (page) {

		case 0:
			if (learnerDetails.getEventSource().equalsIgnoreCase("validate")){
				addLearnerValidator.validatePage1(learnerDetails, errors);
				LearnerProfileValidator learnerProfileValidator = new LearnerProfileValidator();
				learnerProfileValidator.validateMainCustomFields(learnerDetails.getCustomFields(), errors);
			}
			break;
		case 1:
			addLearnerValidator.validatePage2(learnerDetails, errors);
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}


	/**
	 * we can do custom processing after binding and validation
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {
		log.debug("IN onBindAndValidate");
		// Auto-generated method stub
		super.onBindAndValidate(request, command, error, page);
	}

	//16.01.2009
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		
		LearnerDetailsForm learnerDetails = (LearnerDetailsForm)command;
		int Page = getCurrentPage(request);
		
		if ( Page == 1 ) {
			if	(request.getParameter("selectedLearnerGroups")==null){
				if (learnerDetails.getSelectedLearnerGroups()!=null){
					learnerDetails.setSelectedLearnerGroups(null);
				}		
			}
			if (request.getParameter("availableLearnerGroups")==null){
				if (learnerDetails.getAvailableLearnerGroups()!=null){
					learnerDetails.setAvailableLearnerGroups(null);
				}		
			}

			if (request.getParameter("groups")==null){
				if (learnerDetails.getGroups()!=null){
					learnerDetails.setGroups(null);
				}		
			}
		}
		super.onBind(request, command, errors);
	}

	//16.01.2009
	@Override
	protected int getCurrentPage(HttpServletRequest request) {
		// Auto-generated method stub
		return super.getCurrentPage(request);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		if(currentPage==1 
				&& request.getParameter("action")!=null
				&& request.getParameter("action").equals("getLearnerGroup"))
			return 1;
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		//return super.processCancel(request, response, command, error);
		return new ModelAndView(closeTemplate);
	}

	@SuppressWarnings("unchecked")
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		HttpSession session = request.getSession();
		LearnerDetailsForm userDetailsForm = (LearnerDetailsForm)command;

		VU360User newUser = new VU360User();
		newUser.setFirstName(userDetailsForm.getFirstName().trim());
		newUser.setMiddleName(userDetailsForm.getMiddleName().trim());
		newUser.setLastName(userDetailsForm.getLastName().trim());
		newUser.setEmailAddress(userDetailsForm.getEmailAddress().trim());
		newUser.setPassword(userDetailsForm.getPassword().trim());
		newUser.setUsername(userDetailsForm.getUserName().trim());
		newUser.setAccountNonExpired(new Boolean(userDetailsForm.getAccountNonExpired()));
		newUser.setAcceptedEULA(Boolean.FALSE);
		newUser.setNewUser(Boolean.TRUE);
		newUser.setAccountNonLocked(userDetailsForm.getAccountNonLocked()?Boolean.TRUE:Boolean.FALSE);
		newUser.setChangePasswordOnLogin(userDetailsForm.getChangePasswordOnLogin()?Boolean.TRUE:Boolean.FALSE);
		newUser.setCredentialsNonExpired(userDetailsForm.getAccountNonExpired()?Boolean.TRUE:Boolean.FALSE);
		newUser.setEnabled(userDetailsForm.getEnabled()?Boolean.TRUE:Boolean.FALSE);
		newUser.setVissibleOnReport(userDetailsForm.getVisibleOnReport()?Boolean.TRUE:Boolean.FALSE);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateString = userDetailsForm.getExpirationDate().trim();
		log.debug("dateString " + dateString);
		// Parse the string into a Date.
		if (!dateString.isEmpty()){
			Date myDate = formatter.parse(dateString);
			newUser.setExpirationDate(myDate);
		} else{
			newUser.setExpirationDate(null);
		}
		newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
		Calendar calender=Calendar.getInstance();
		Date createdDate=calender.getTime();
		newUser.setCreatedDate(createdDate);

		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		Customer customer = null;
		if( vu360UserService.hasAdministratorRole(loggedInUser) ) {
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomer();
		} else {
			customer = loggedInUser.getLearner().getCustomer();
		}

		LMSRole systemRole=vu360UserService.getDefaultRole(customer);
		if(systemRole == null) {
			List<LMSRole> systemRoles=vu360UserService.getSystemRolesByCustomer(customer);
			for(LMSRole role:systemRoles){
				if(role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER))
				{
					systemRole=role;
					break;
				}
			}
		}
		newUser.addLmsRole(systemRole);

		/* ################################################################# */
		List<CustomFieldValue> myCustomFieldValues=new ArrayList<CustomFieldValue>();
		if(userDetailsForm.getCustomFields().size()>0){
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField field : userDetailsForm.getCustomFields()){
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
							Map<Long,CustomFieldValueChoice> totalChoiceMap2 = new HashMap<Long,CustomFieldValueChoice>();
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : field.getCustomFieldValueChoices()){
								totalChoiceMap2.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
							}
							for(String selectedChoiceIdString : field.getSelectedChoices()){
								if(totalChoiceMap2.containsKey(new Long(selectedChoiceIdString))){
									selectedChoiceList.add(totalChoiceMap2.get(new Long(selectedChoiceIdString)));
								}
							}
						}
					}
					CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
					customFieldValue.setCustomField(field.getCustomFieldRef());
					customFieldValue.setValueItems(selectedChoiceList);
					myCustomFieldValues.add(customFieldValue);
				}else{
					CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
					customFieldValue.setCustomField(field.getCustomFieldRef());
					myCustomFieldValues.add(customFieldValue);
				}
			}
		}
		userDetailsForm.getCustomFieldValueList().addAll( myCustomFieldValues );
		
		//assign it to learner profile 
		LearnerProfile newLearnerProfile = new LearnerProfile();
		newLearnerProfile.setCustomFieldValues(myCustomFieldValues);
		
		if(StringUtils.isNotBlank(userDetailsForm.getMobilePhone()))
			newLearnerProfile.setMobilePhone(userDetailsForm.getMobilePhone().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getOfficePhone()))
			newLearnerProfile.setOfficePhone(userDetailsForm.getOfficePhone().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getOfficePhoneExtn()))
			newLearnerProfile.setOfficePhoneExtn(userDetailsForm.getOfficePhoneExtn().trim());

		Address newAddress1 = new Address();
		if(StringUtils.isNotBlank(userDetailsForm.getStreetAddress1()))
			newAddress1.setStreetAddress(userDetailsForm.getStreetAddress1().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getStreetAddress1a()))
			newAddress1.setStreetAddress2(userDetailsForm.getStreetAddress1a().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getCity()))
			newAddress1.setCity(userDetailsForm.getCity().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getState()))
			newAddress1.setState(userDetailsForm.getState().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getZipcode()))
			newAddress1.setZipcode(userDetailsForm.getZipcode().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getCountry()))
			newAddress1.setCountry(userDetailsForm.getCountry().trim());
		newLearnerProfile.setLearnerAddress(newAddress1);

		Address newAddress2 = new Address();
		if(StringUtils.isNotBlank(userDetailsForm.getStreetAddress2()))
			newAddress2.setStreetAddress(userDetailsForm.getStreetAddress2().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getStreetAddress2a()))
			newAddress2.setStreetAddress2(userDetailsForm.getStreetAddress2a().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getCity2()))
			newAddress2.setCity(userDetailsForm.getCity2().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getState2()))
			newAddress2.setState(userDetailsForm.getState2().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getZipcode2()))
			newAddress2.setZipcode(userDetailsForm.getZipcode2().trim());
		if(StringUtils.isNotBlank(userDetailsForm.getCountry2()))
			newAddress2.setCountry(userDetailsForm.getCountry2().trim());
		newLearnerProfile.setLearnerAddress2(newAddress2);
		
		/* ################################################################# */
		
		Learner newLearner = new Learner();
		newLearner.setVu360User(newUser);
		newLearnerProfile.setLearner(newLearner);
		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);

		//16.01.2009
		//Selected Learner Groups  added.
		String[] selectedlearnerGroupList=userDetailsForm.getSelectedLearnerGroups();
		List<LearnerGroup> selectedLearnersGroupList = new ArrayList<LearnerGroup>();
		if(selectedlearnerGroupList!=null && selectedlearnerGroupList.length>0){
			selectedLearnersGroupList = new ArrayList<LearnerGroup>();
			for( String selectedlearnerGroup : selectedlearnerGroupList ) {
				int index = selectedlearnerGroup.indexOf("-",0);
				String lgId = selectedlearnerGroup.substring(0,index);
				Long selectedlearnerGroupId = Long.parseLong(lgId);
				LearnerGroup learnerGroup = learnerService.getLearnerGroupById(selectedlearnerGroupId);
				selectedLearnersGroupList.add(learnerGroup);
			}
		}

		//16.01.2009
		//Selected Organizational Groups added
		String[] orgGroupsList = userDetailsForm.getGroups();
		List<OrganizationalGroup> organizationalGroupsList = new ArrayList<OrganizationalGroup>();
		if(orgGroupsList!=null && orgGroupsList.length>0){
			organizationalGroupsList = new ArrayList<OrganizationalGroup>();
			for(String orgGroup:orgGroupsList){
				Long orgGroupId = Long.parseLong(orgGroup);
				OrganizationalGroup organizationalGroups=learnerService.getOrganizationalGroupById(orgGroupId);
				organizationalGroupsList.add(organizationalGroups);
			}
		}

		String learnerPassWord=newUser.getPassword();
		Learner addedLearner = learnerService.addLearner(newLearner.getCustomer(), organizationalGroupsList, selectedLearnersGroupList, newLearner);
		// Added by Dyutiman :: LMS 4300
		Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		
		//Code for Auto-learner enrollment to courses of learner group(s) selected.
		try {
			if (!selectedLearnersGroupList.isEmpty()) {
				enrollmentService.enrollLearnerInLearnerGroupsCourses(loggedInUser, customer, addedLearner, selectedLearnersGroupList, brander);
			}

			Map model = new HashMap();

			newLearner.getVu360User().setPassword(learnerPassWord);
			if(customer.getCustomerPreferences().isEnableRegistrationEmailsForNewCustomers() && 
					customer.getDistributor().getDistributorPreferences().isEnableRegistrationEmailsForNewCustomers()){
			model.put("loggedInUser", loggedInUser);
			model.put("customerName", customer.getName());
			model.put("user", newLearner.getVu360User());
			StringBuilder loginURL=new StringBuilder();
			loginURL.append(request.getScheme());
			loginURL.append("://");
			loginURL.append(request.getServerName());
			if(request.getServerPort()!=80){
				loginURL.append(":");
				loginURL.append(request.getServerPort());
			}
			loginURL.append(request.getContextPath());

			model.put("url",loginURL.toString());

			String batchImportTemplatePath =  brander.getBrandElement("lms.email.batchUpload.body");
			String fromAddress =  brander.getBrandElement("lms.email.batchUpload.fromAddress");
			String fromCommonName =  brander.getBrandElement("lms.email.batchUpload.fromCommonName");
			String subject =  brander.getBrandElement("lms.email.batchUpload.subject");//+ customer.getName();
			String support =  brander.getBrandElement("lms.email.batchUpload.fromCommonName");
			model.put("support", support);
			String lmsDomain=VU360Properties.getVU360Property("lms.domain");
			model.put("lmsDomain",lmsDomain);
			model.put("brander", brander);
			/*START-BRANDING EMAILTEMPLATE WORK*/
			String templateText=brander.getBrandElement("lms.branding.email.accountDetails.templateText");			                            
			String loginUrl=lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());
			
			templateText=templateText.replaceAll("&lt;firstname&gt;", newLearner.getVu360User().getFirstName());			                
            templateText=templateText.replaceAll("&lt;lastname&gt;", newLearner.getVu360User().getLastName());
            templateText=templateText.replaceAll("&lt;username&gt;", newLearner.getVu360User().getUsername());
            templateText=templateText.replaceAll("&lt;password&gt;", newLearner.getVu360User().getPassword());            			                            
            templateText=templateText.replaceAll("&lt;loginurl&gt;", loginUrl);                                                
            
            model.put("templateText", templateText);
			/*END BRANDING EMAIL TEMPLATE WORK*/
			
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, batchImportTemplatePath, model);
			SendMailService.sendSMTPMessage(newLearner.getVu360User().getEmailAddress(), 
					fromAddress, 
					fromCommonName, 
					subject, 
					text);		
		}			

		} catch (Exception e ) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		session.setAttribute("createdLearnerId", newLearner.getId()); 
		return new ModelAndView(closeTemplate);
	}
	
	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	/*
	 * The getters are setters
	 */
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

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

	/**
	 * @author khurram.shehzad
	 * @param applyCountryStateLabel Set Label to country and state fields
	 */
	private void applyCountryStateLabel(LearnerDetailsForm form) {
		Brander b = form.getBrander();
		List<LabelBean> countries = b
				.getBrandMapElements("lms.manageUser.AddLearner.Country");
		String stateBean = "";
		if (form.getEventSource().equalsIgnoreCase("validate")) {

			String country1 = form.getCountry();
			String country2 = form.getCountry2();
			String state = form.getState();
			String state2 = form.getState2();
			List<LabelBean> states = null;
 
			for (LabelBean bean : countries) {
				if (country1.equalsIgnoreCase(bean.getValue())
						|| country1.equalsIgnoreCase(bean.getLabel())) {
					stateBean = bean.getValue();
					country1 = bean.getLabel();
					form.setCountryLabel(country1);
					if (stateBean.isEmpty()
							|| stateBean.equalsIgnoreCase("US")
							|| stateBean.equalsIgnoreCase("United States")) {
						states = b
								.getBrandMapElements("lms.manageUser.AddLearner.State");
					} else {
						states = b
								.getBrandMapElements("lms.manageUser.AddLearner."
										+ stateBean + ".State");
					}
					
					if (states == null) {
						form.setStateLabel(state);												
					}
					else {
						for (LabelBean bin : states) {
							if (state.equalsIgnoreCase(bin.getValue())
									|| state.equalsIgnoreCase(bin.getLabel())) {
								state = bin.getLabel();
								form.setStateLabel(state);
								break;
							}
						}
					}
					break;
				}
			}
			for (LabelBean bean : countries) {
				if (country2.equalsIgnoreCase(bean.getValue())
						|| country2.equalsIgnoreCase(bean.getLabel())) {
					stateBean = bean.getValue();
					country2 = bean.getLabel();
					form.setCountryLabel2(country2);
					if (stateBean.isEmpty()
							|| stateBean.equalsIgnoreCase("US")
							|| stateBean.equalsIgnoreCase("United States")) {
						states = b
								.getBrandMapElements("lms.manageUser.AddLearner.State");
					} else {
						states = b
								.getBrandMapElements("lms.manageUser.AddLearner."
										+ stateBean + ".State");
					}
					if (states == null) {
						form.setStateLabel2(state2);												
					}
					else {
						for (LabelBean bin : states) {
							if (state2.equalsIgnoreCase(bin.getValue())
									|| state2.equalsIgnoreCase(bin.getLabel())) {
								log.debug("STATE - " + bin.getValue());
								log.debug("STATEEEE - " + bin.getLabel());
								state2 = bin.getLabel();
								form.setStateLabel2(state2);
								break;
							}
						}
					}
					break;
				}
			}
		}
	}
	
}