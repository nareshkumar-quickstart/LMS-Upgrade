/**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
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
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.LearnerDetailsForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.AddLearnerValidator;
import com.softech.vu360.lms.web.controller.validator.LearnerProfileValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;


public class AddLearnerController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddLearnerController.class.getName());

	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private CustomerService customerService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;
	private CustomFieldService customFieldService = null;
	private EntitlementService entitlementService = null;
	private EnrollmentService enrollmentService;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;	

	public AddLearnerController() {
		super();
		setCommandName("learnerForm");
		setCommandClass(LearnerDetailsForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/manageUser/addLearner/Step1"
				, "administrator/manageUser/addLearner/Step2"
				, "administrator/manageUser/addLearner/Step3"});
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
		
		if (loggedInUserVO.isLMSAdministrator()){
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
					.getAuthentication().getDetails()).getCurrentCustomerId();
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LMSADMINISTRATOR,customerId));
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_TRAININGMANAGER,customerId));
			roleList.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LEARNER,customerId));
		}else if (loggedInUserVO.isTrainingAdministrator()){
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
		
		if( customer.getCustomFields() != null ){
			totalCustomFieldList.addAll(customer.getCustomFields());	
		}
		
		if(reseller.getCustomFields() != null) {
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
		super.onBindOnNewForm(request, command, binder);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		// check for customer selection
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			
			if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
				if(details.getCurrentSearchType()!=AdminSearchType.CUSTOMER){
					response.sendRedirect("adm_manageLearners.do?error=customerSelection");
				}
			}
		}	
		// TODO Auto-generated method stub request, response, binder
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
			learnerForm.setOldUserName(learnerForm.getUserName());
			learnerForm.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
			break;

		case 1:
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
			TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup,orgGroupIdList);
			if(orgGroupRoot != null && orgGroupRoot.bfs()!=null){
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			for(TreeNode node:treeAsList )
				log.debug(node.toString());
			model.put("orgGroupTreeAsList", treeAsList);
			}
			
			
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
					
					//LearnerGroup learnerGroup=learnerService.getLearnerGroupById(availablelearnerGroupId);
					LearnerGroup learnerGroup=new LearnerGroup();//;=learnerService.getLearnerGroupById(selectedlearnerGroupId);
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
			
			
			
		/*	for(Long id:learnerGroupIdList){
				String learnerGroupName=learnerService.getLearnerGroupById(id).getName();
				learnerGroupNameList.add(learnerGroupName);
			}*/
			//LMSRole role = vu360UserService.getRoleByName(learnerDetailsForm.getDefaultRoleName(), customer);
			learnerDetailsForm.setRoleName(learnerDetailsForm.getDefaultRoleName());
						
			model.put("learnerGroupNameList", learnerGroupNameList);
			return model;
		
			
		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.referenceData(request, page);
	}

	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage");

		// TODO Auto-generated method stub
		LearnerDetailsForm learnerDetails = (LearnerDetailsForm)command;

		AddLearnerValidator addLearnerValidator = (AddLearnerValidator) getValidator();

		errors.setNestedPath("");
		switch (page) {

		case 0:
			// if country is changed  then do not validate
			if(! learnerDetails.getEventSource().equalsIgnoreCase("donotValidate")){
				addLearnerValidator.validatePage1(learnerDetails, errors);
				
				LearnerProfileValidator learnerProfileValidator = new LearnerProfileValidator();
				learnerProfileValidator.validateMainCustomFields(learnerDetails.getCustomFields(), errors);
			}
				

			break;
		case 1:
			//TODO
			addLearnerValidator.validatePage2(learnerDetails, errors);
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}
	//super.validatePage(command, errors, page);


	/**
	 * we can do custom processing after binding and validation
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {
		log.debug("IN onBindAndValidate");
		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, error, page);
	}

	//16.01.2009
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		// TODO Auto-generated method stub
		LearnerDetailsForm learnerDetails = (LearnerDetailsForm)command;
		
		int Page=getCurrentPage(request);
		if (Page==1){
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		//return super.processCancel(request, response, command, error);
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		//log.debug("IN processFinish");
		
		//15-01-2009
		LearnerDetailsForm learnerDetails = (LearnerDetailsForm)command;
		
		Learner newLearner = new Learner();
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		Customer customer = null;
		String learnerPassWord=null;
		if( loggedInUser.isLMSAdministrator() ) {
			customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		} else {
			customer = loggedInUser.getLearner().getCustomer();
		}
		
		VU360User newUser = new VU360User();
		newUser.setFirstName(learnerDetails.getFirstName().trim());

		newUser.setMiddleName(learnerDetails.getMiddleName().trim());
		newUser.setLastName(learnerDetails.getLastName().trim());
		newUser.setEmailAddress(learnerDetails.getEmailAddress().trim());
		newUser.setPassword(learnerDetails.getPassword().trim());
		newUser.setUsername(learnerDetails.getUserName().trim());
		learnerPassWord=newUser.getPassword();
		newUser.setAccountNonExpired(new Boolean(learnerDetails.getAccountNonExpired()));
		newUser.setNewUser(true);
		//newUser.setAcceptedEULA(new Boolean(learnerDetails.getAccountNonExpired()));
		newUser.setAccountNonLocked(new Boolean(learnerDetails.getAccountNonLocked()));
		newUser.setChangePasswordOnLogin(new Boolean(learnerDetails.getChangePasswordOnLogin()));
		newUser.setCredentialsNonExpired(new Boolean(learnerDetails.getAccountNonExpired()));
		newUser.setEnabled(new Boolean(learnerDetails.getEnabled()));
		newUser.setVissibleOnReport(new Boolean(learnerDetails.getVisibleOnReport()));
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		//Date currentTime_1 = new Date();
		// Date for 13 Nov 2001
		String dateString = learnerDetails.getExpirationDate().trim();
		log.debug("dateString " + dateString);
		// Parse the string into a Date.
		if (!dateString.isEmpty()){
			Date myDate = formatter.parse(dateString);
			newUser.setExpirationDate(myDate);
		}
		else{
			newUser.setExpirationDate(null);
		}
		
		newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
		Calendar calender=Calendar.getInstance();
		Date createdDate=calender.getTime();
		newUser.setCreatedDate(createdDate);
		
		LMSRole lmsRole = vu360UserService.getRoleByName(learnerDetails.getDefaultRoleName(), customer);
		
		List<LMSRole> systemRoles=vu360UserService.getSystemRolesByCustomer(customer);
		if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
			
			LMSAdministrator lmsAdministrator = new LMSAdministrator();
			lmsAdministrator.setGlobalAdministrator(false);
			lmsAdministrator.setVu360User(newUser);
			newUser.setLmsAdministrator(lmsAdministrator);

			TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
			trainingAdministrator.setManagesAllOrganizationalGroups(true);
			trainingAdministrator.setCustomer(customer);
			trainingAdministrator.setVu360User(newUser);

			newUser.setTrainingAdministrator(trainingAdministrator);
			newUser.getLmsRoles().addAll(systemRoles);
			newUser.addLmsRole(lmsRole);
		}else if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)){
			TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
			trainingAdministrator.setManagesAllOrganizationalGroups(true);
			trainingAdministrator.setCustomer(customer);
			trainingAdministrator.setVu360User(newUser);
			LMSRole systemRole=null;
			for(LMSRole role:systemRoles){
				
				if(role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER))
				{
					systemRole=role;
					break;
				}
			}
			newUser.getLmsRoles().add(systemRole);
			newUser.addLmsRole(lmsRole);
			newUser.setTrainingAdministrator(trainingAdministrator);

		}
		else{
			newUser.addLmsRole(lmsRole);
			
		}
			LearnerProfile newLearnerProfile = new LearnerProfile();
		newLearnerProfile.setMobilePhone(learnerDetails.getMobilePhone().trim());
		newLearnerProfile.setOfficePhone(learnerDetails.getOfficePhone().trim());
		newLearnerProfile.setOfficePhoneExtn(learnerDetails.getOfficePhoneExtn().trim());
		Address newAddress = new Address();
		newAddress.setStreetAddress(learnerDetails.getStreetAddress1().trim());
		newAddress.setStreetAddress2(learnerDetails.getStreetAddress1a().trim());
		newAddress.setCity(learnerDetails.getCity().trim());
		newAddress.setState(learnerDetails.getState().trim());
		newAddress.setZipcode(learnerDetails.getZipcode().trim());

		newAddress.setCountry(learnerDetails.getCountry().trim());

		newLearnerProfile.setLearnerAddress(newAddress);
		
		Address newAddress2 = new Address();
		newAddress2.setStreetAddress(learnerDetails.getStreetAddress2().trim());
		newAddress2.setStreetAddress2(learnerDetails.getStreetAddress2a().trim());
		newAddress2.setCity(learnerDetails.getCity2().trim());
		newAddress2.setState(learnerDetails.getState2().trim());
		newAddress2.setZipcode(learnerDetails.getZipcode2().trim());
		newAddress2.setCountry(learnerDetails.getCountry2().trim());

		newLearnerProfile.setLearnerAddress2(newAddress2);

		
		/* ################################################################# */
		//Save Custom Fields
		//Save Custom Fields
		List<CustomFieldValue> myCustomFieldValues=new ArrayList<CustomFieldValue>();
		if(learnerDetails.getCustomFields().size()>0){
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField field : learnerDetails.getCustomFields()){

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

					//this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
					myCustomFieldValues.add(customFieldValue);

				}

			}
		}
		
		//assign it to learner profile & save
		//form.getCustomFieldValueList().addAll(myCustomFieldValues);
		learnerDetails.getCustomFieldValueList().addAll( myCustomFieldValues );
		
		//assign it to learner profile 
		newLearnerProfile.setCustomFieldValues(myCustomFieldValues);
/* ################################################################# */

		
		
		newLearner.setVu360User(newUser);
		newLearnerProfile.setLearner(newLearner);
		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);
		newUser.setLearner(newLearner);
		
		//16.01.2009
		//Selected Learner Groups  added.
		String[] selectedlearnerGroupList=learnerDetails.getSelectedLearnerGroups();
		List<LearnerGroup> selectedLearnersGroupList = new ArrayList<LearnerGroup>();
		if(selectedlearnerGroupList!=null && selectedlearnerGroupList.length>0){
			selectedLearnersGroupList = new ArrayList<LearnerGroup>();
			
			for(String selectedlearnerGroup:selectedlearnerGroupList){
				int index=selectedlearnerGroup.indexOf("-",0);
				
				String lgId=selectedlearnerGroup.substring(0,index);
				
				
				Long selectedlearnerGroupId = Long.parseLong(lgId);
				
				LearnerGroup learnerGroup=learnerService.getLearnerGroupById(selectedlearnerGroupId);
				selectedLearnersGroupList.add(learnerGroup);
			}
			
		}
		
		//16.01.2009
		//Selected Organizational Groups added
		
		String[] orgGroupsList = learnerDetails.getGroups();
		List<OrganizationalGroup> organizationalGroupsList = new ArrayList<OrganizationalGroup>();
		if(orgGroupsList!=null && orgGroupsList.length>0){
			
			organizationalGroupsList = new ArrayList<OrganizationalGroup>();
			
			for(String orgGroup:orgGroupsList){
				Long orgGroupId = Long.parseLong(orgGroup);
				
				OrganizationalGroup organizationalGroups=learnerService.getOrganizationalGroupById(orgGroupId);
				organizationalGroupsList.add(organizationalGroups);
				
			}
			
		}
		
		Learner addedLearner = learnerService.addLearner(newLearner.getCustomer(), organizationalGroupsList, selectedLearnersGroupList, newLearner);
		//Code for Auto-learner enrollment to courses of learner group(s) selected.
		 
		Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		
		if (!selectedLearnersGroupList.isEmpty()) {
			enrollmentService.enrollLearnerInLearnerGroupsCourses(loggedInUser, customer, addedLearner, selectedLearnersGroupList, brander);
		}
		
//		LMS:3461, If customer adds more than one learner the customer type will be changed to b2b type.
		if(newLearner.getCustomer().getCustomerType().equals("b2c")){
			Learner learnerValue=learnerService.getLearnerForB2CCustomer(newLearner.getCustomer());
			if(learnerValue!=null){
				customer.setCustomerType("b2b");
				customerService.saveCustomer(customer,true);
			}
		}
		
		try{
			VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			if(adminDetails.getCurrentSearchType().equals(AdminSearchType.CUSTOMER)){
				brander=VU360Branding.getInstance().getBranderByUser(request, addedLearner.getVu360User());
			}
			
				 Map model = new HashMap();
				 
				 newLearner.getVu360User().setPassword(learnerPassWord);
			if(customer.getCustomerPreferences().isEnableRegistrationEmailsForNewCustomers() && 
							customer.getDistributor().getDistributorPreferences().isEnableRegistrationEmailsForNewCustomers()){
				
				    model.put("loggedInUser", loggedInUser);
					model.put("customerName", customer.getName());
					model.put("user", newLearner.getVu360User());
					String  loginURL = VU360Properties.getVU360Property("lms.loginURL");
					 
				//	model.put("url", request.getScheme()+"://"+request.getServerName()
				//			+":"+request.getServerPort()+request.getContextPath()+"/login.do");
					 model.put("url",loginURL+"login.do");
					 
					//commented by Faisal A. Siddiqui July 21 09
					 //Brander brander= VU360Branding.getInstance().getBrander("default", new Language());					 
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
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return new ModelAndView(closeTemplate);
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

	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, List<Long> selectedOrgGroups){
		if(orgGroup!=null){
			TreeNode node = new TreeNode(orgGroup);
			for(Long selectedId:selectedOrgGroups){
				if(orgGroup.getId().longValue()==selectedId.longValue()){
					node.setSelected(true);
					break;
				}
			}
			
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i),selectedOrgGroups);
			}
			node.setEnabled(true);
			
			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
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

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @return the customFieldService
	 */
	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	/**
	 * @param customFieldService the customFieldService to set
	 */
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
}
