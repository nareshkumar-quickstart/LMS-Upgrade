package com.softech.vu360.lms.web.controller.manager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseAlertTriggeerFilter;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertFilter;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertFilter;
import com.softech.vu360.lms.model.LegacyCourse;
import com.softech.vu360.lms.model.OrgGroupAlertFilterTrigger;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.CourseEntitlementItem;
import com.softech.vu360.lms.web.controller.model.FilterTriggerForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.validator.AddFilterValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.TreeNode;



public class AddFilterWizardController extends AbstractWizardFormController{


	private EntitlementService entitlementService=null;
	private SurveyService surveyService = null;
	private LearnerLicenseService learnerLicenseServices;
	

	private static transient Logger log = Logger.getLogger(AddFilterWizardController.class.getName());

	private String finishTemplate = null;



	private static final String LEARNERS = "learners";
	private static final String LEARNER_GROUPS = "learnergroups";
	private static final String ORGANIZATION_GROUPS = "organizationgroups";
	private static final String COURSES = "courses";

	private static final int SEARCH_COURSE_PAGE_SIZE = 10;

	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;

	public AddFilterWizardController(){

		super();
		setCommandName("addTriggerFilterForm");
		setCommandClass(FilterTriggerForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/step1"
				,"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/step2"
				,"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/mgr_addFilter_learner"
				,"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/mgr_addFilter_learnerGroup"
				,"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/mgr_addFilter_organization_Groups"
				,"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/mgr_addFilter_courses"
				,"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addFilter/mgr_addFilter_finish"
		});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		try{
			FilterTriggerForm form = (FilterTriggerForm)command;
			String triggerId = request.getParameter("triggerId");
			AlertTrigger alertTrigger = surveyService.getAlertTriggerByID(Long.parseLong(triggerId));
			if(alertTrigger.getLicenseExpiratrionDate()!=null)
				form.setLicenseExpirationdate(alertTrigger.getLicenseExpiratrionDate().toString());
			form.setTriggerId(Long.parseLong(triggerId));
			form.setCourseSearchResultsPageSize(SEARCH_COURSE_PAGE_SIZE);
			VU360User vu360UserModel = null;
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder.getContext().getAuthentication()).getDetails();
			
			if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
				com.softech.vu360.lms.vo.Customer customerVO = (com.softech.vu360.lms.vo.Customer) request.getSession(true).getAttribute("adminSelectedCustomer");
				com.softech.vu360.lms.vo.Distributor distributorvo = (com.softech.vu360.lms.vo.Distributor) request.getSession(true).getAttribute("adminSelectedDistributor");
				if(customerVO != null) {
					Long learnerId = learnerService.getLearnerForSelectedCustomer(customerVO.getId());
					vu360UserModel = learnerService.getLearnerByID(learnerId.longValue()).getVu360User();
				}else if(distributorvo != null) {
					Long learnerId = learnerService.getLearnerForSelectDistributor(distributorvo.getMyCustomer().getId());
					vu360UserModel = learnerService.getLearnerByID(learnerId).getVu360User();
				} else {
					vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
				}
			} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
			}
			
			List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList<LearnerGroup>();
			selectedLearnerGroupsAssociatedWithOrgGroup.addAll(orgGroupLearnerGroupService.getAllLearnerGroups(vu360UserModel.getLearner().getCustomer().getId(),vu360UserModel));
			List<LearnerGroupEnrollmentItem> learnerGroupList = new ArrayList <LearnerGroupEnrollmentItem>();
			for(int learnerGroupNo=0; learnerGroupNo<selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++) {
				LearnerGroup lgrp = selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo);
				LearnerGroupEnrollmentItem item = new LearnerGroupEnrollmentItem(); 
				item.setLearnerGroupId(lgrp.getId());
				item.setLearnerGroupName(lgrp.getName());
				item.setSelected(false);
				learnerGroupList.add(item);
			}

			form.setLearnerGroupEnrollmentItems(learnerGroupList);
			List<CourseEntitlementItem> courseEntitlements =  new ArrayList<CourseEntitlementItem>();
			form.setCourseEntitlementItems(courseEntitlements);
		}

		catch (Exception e) {
			log.debug("exception", e);
		}

		return command;
	}



	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {

		FilterTriggerForm form =(FilterTriggerForm)command;
		Long ltriggerId = form.getTriggerId();
		AlertTrigger trigger = surveyService.getAlertTriggerByID(ltriggerId);
		List<LearnerGroup> learnerGroups = new ArrayList<LearnerGroup>();
		List<OrganizationalGroup> organizationalGroups = new ArrayList<OrganizationalGroup>();
		List<Course> courseList=  new ArrayList<Course>();

		surveyService.getAlertTriggerFilterByID(ltriggerId);
		if(form.getFilterType().equalsIgnoreCase(LEARNERS)){
			LearnerAlertFilter learnerAlertFilter=form.getLearnerAlertFilter();
			learnerAlertFilter.setFilterName(form.getFilterName().substring(0, Math.min(form.getFilterName().length(), 100)));
			learnerAlertFilter.setLearners(form.getLearnerss());
			learnerAlertFilter.setAlertTrigger(trigger);
			//learnerAlertRecipient.setLearners()
			surveyService.addAlertTriggerFilter(learnerAlertFilter);
		}
		if(form.getFilterType().equalsIgnoreCase(LEARNER_GROUPS)){
			LearnerGroupAlertFilter learnerGroupAlertFilter=form.getLearnerGroupAlertFilter();
			learnerGroupAlertFilter.setFilterName(form.getFilterName().substring(0, Math.min(form.getFilterName().length(), 100)));
			learnerGroupAlertFilter.setLearnerGroup(form.getSelectedLearnerGroupList());
			learnerGroupAlertFilter.setAlertTrigger(trigger);
			surveyService.addAlertTriggerFilter(learnerGroupAlertFilter);
		}

		if(form.getFilterType().equalsIgnoreCase(ORGANIZATION_GROUPS)){
			OrgGroupAlertFilterTrigger orgGroupAlertFilterTrigger=form.getOrgGroupAlertFilterTrigger();
			orgGroupAlertFilterTrigger.setFilterName(form.getFilterName().substring(0, Math.min(form.getFilterName().length(), 100)));
			orgGroupAlertFilterTrigger.setOrggroup(form.getSelectedOrgGroupList());
			orgGroupAlertFilterTrigger.setAlertTrigger(trigger);
			surveyService.addAlertTriggerFilter(orgGroupAlertFilterTrigger);

			//form.getOrgGroupAlertRecipient().setAlertRecipientGroupName(form.getRecipientGroupName());
			//form.getOrgGroupAlertRecipient().setOrganizationalGroups(organizationalGroups);
			//form.getOrgGroupAlertRecipient().setAlert(alert);
			//surveyService.addAlertRecipient(form.getOrgGroupAlertRecipient());
		}

		if(form.getFilterType().equalsIgnoreCase(COURSES)){
			CourseAlertTriggeerFilter courseAlertTriggeerFilter =form.getCourseAlertTriggeerFilter();
			courseAlertTriggeerFilter.setFilterName(form.getFilterName().substring(0, Math.min(form.getFilterName().length(), 100)));
			courseAlertTriggeerFilter.setCourses(form.getSelectedCourseList());
			courseAlertTriggeerFilter.setAlertTrigger(trigger);
			surveyService.addAlertTriggerFilter(courseAlertTriggeerFilter);
		}
		// TODO Auto-generated method stub
		return new ModelAndView(finishTemplate);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN processCancel");
		return new ModelAndView(finishTemplate);
		//return super.processCancel(request, response, command, error);
	}





	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

		// TODO Auto-generated method stub
		FilterTriggerForm form = (FilterTriggerForm) command;
		
		if(request.getParameter("searchAction") != null) {
			form.setAction(request.getParameter("searchAction"));
		} else {
			form.setAction("");
		}
		
		if(form.getFilterType().equals(ORGANIZATION_GROUPS)) {
			if(request.getParameter("orgCheckBox") != null && request.getParameter("orgCheckBox").equals("false")) {
				form.setGroups(null);
			}
		}
		super.onBindAndValidate(request, command, errors, page);
	}





	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors,int currentPage) throws Exception {

		FilterTriggerForm form =(FilterTriggerForm)command;
		Map <Object, Object>model = new HashMap <Object, Object>();
		Map <Object, Object>surveyMethodMap = new LinkedHashMap <Object, Object>();
		// TODO Auto-generated method stub
		log.debug("in GET TARGET PAGE...."+currentPage);
		/*if(currentPage==3 && this.getTargetPage(request, currentPage) == 4)
			{
				String Learner[]=request.getParameterValues("learnerGroup");

			}*/
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		if(firstName== null){
			firstName="";
		}
		if(lastName== null){
			lastName="";
		}
		if(email== null){
			email="";
		}

		VU360User vu360UserModel = null;
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder.getContext().getAuthentication()).getDetails();
		
		if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
			com.softech.vu360.lms.vo.Customer customerVO = (com.softech.vu360.lms.vo.Customer) request.getSession(true).getAttribute("adminSelectedCustomer");
			com.softech.vu360.lms.vo.Distributor distributorVO = (com.softech.vu360.lms.vo.Distributor) request.getSession(true).getAttribute("adminSelectedDistributor");
			if(customerVO != null) {
				Long learnerId = learnerService.getLearnerForSelectedCustomer(customerVO.getId());
				vu360UserModel = learnerService.getLearnerByID(learnerId.longValue()).getVu360User();
			}else if(distributorVO != null) {
				Long learnerId = learnerService.getLearnerForSelectDistributor(distributorVO.getMyCustomer().getId());
				vu360UserModel = learnerService.getLearnerByID(learnerId).getVu360User();
			} else {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
			}
		} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
			vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
		}
		
		
		if(form.getFilterType().equals(COURSES)) {
			if(form.getAction().equalsIgnoreCase("search")) {
				List<Course> courseList = getSearchCriteriaCourses(vu360UserModel.getLearner().getCustomer().getId(), form.getCourseName(), form.getCourseType());
				form.setCourseListFromDB(courseList);
				form.setCourseList(courseList);
			}
			
		} else  if(form.getFilterType().equals(LEARNERS)) {
			boolean hasAdmninistratorRole = vu360UserService.hasAdministratorRole(vu360UserModel);
			List<VU360User> users = learnerService.findLearner(firstName, lastName, email, 
					hasAdmninistratorRole, vu360UserService.hasTrainingAdministratorRole(vu360UserModel), vu360UserModel.getTrainingAdministrator().getId(), 
					vu360UserModel.getTrainingAdministrator().isManagesAllOrganizationalGroups(), vu360UserModel.getTrainingAdministrator().getManagedGroups(), 
					vu360UserModel.getLearner().getCustomer().getId(), vu360UserModel.getId());
			List<VU360User> licensedusers = new ArrayList<VU360User>();
			AlertTrigger trigger = new AlertTrigger();
			List<Map<Object, Object>> license = null;
			if(users != null)
			{
				Long customerId = null;
				
				if (hasAdmninistratorRole) {
					customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder
							.getContext().getAuthentication().getDetails())
							.getCurrentCustomerId();
				} else {
					if (vu360UserModel.getTrainingAdministrator()
							.isManagesAllOrganizationalGroups()) {
						customerId = vu360UserModel.getLearner().getCustomer().getId();
					}
				}
				
				license = learnerLicenseServices.getLicenseName(customerId,firstName,lastName,email);
				//license = learnerLicenseServices.get
				HashMap<Long, String> hashMap = new HashMap<Long, String>();
				
		        for(Map map:license)
				{
					hashMap.put(Long.parseLong(map.get("LEARNER_ID").toString()), map.get("LEARNER_ID").toString());
				}
		        
		        for(Long key : hashMap.keySet())
		        {
		        	for(VU360User user : users)
		        	{
		        		if(user.getLearner().getId() == Long.parseLong(hashMap.get(key).toString()))
		        		{
		        			licensedusers.add(user);
		        		}
		        	}
		        	
		        }
		        
			}
			if(form.getTriggerId() != null)
			{
				trigger = surveyService.getAlertTriggerByID(form.getTriggerId());
				if(trigger.getLicenseExpiratrionDate() != null)
					form.setLearnerListFromDB(licensedusers);
				else
				form.setLearnerListFromDB(users);
			}
		}
		
		
		int i=0, j=0;
		VU360User item0;
		String selecteditem0;

		List<Learner> learnerss = new ArrayList<Learner>();

		if(currentPage==2 && this.getTargetPage(request, currentPage)==6 && form.getSelectedLearner()!=null){

			if(form.getSelectedLearner()!=null)
				form.setPageIndex("2");
				for(;i<form.getLearnerListFromDB().size();){
					item0 = form.getLearnerListFromDB().get(i);
					for(;j<form.getSelectedLearner().length;){
						selecteditem0 = (form.getSelectedLearner())[j];
						if(item0 != null){
							if(item0.getId()==(Long.parseLong(selecteditem0))){
								//addRecipientForm.getSelectedLearnerGroupList().add(item);
								Learner tempLearner = item0.getLearner();
								tempLearner.setVu360User(item0);
								learnerss.add(tempLearner);

								break;
							}
						}
						j++;
					}
					j=0;
					i++;
				}			

		}
		form.setLearnerss(learnerss);
		//For doing pagination
		/*if (((currentPage == 1 || currentPage == 8) 
				&& ((!StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))){
			
		}*/
		i=0; j=0;
		LearnerGroup item;
		String selecteditem;
		List<LearnerGroup> selectedLearnerGroupList =  new ArrayList<LearnerGroup>();

		if(form.getLearnerGroup()!=null)
			for(;i<form.getLearnerGroupListFromDB().size();){
				item = form.getLearnerGroupListFromDB().get(i);
				for(;j<form.getLearnerGroup().length;){
					selecteditem = (form.getLearnerGroup())[j];
					if(item != null){
						if(item.getId()==(Long.parseLong(selecteditem))){
							//addRecipientForm.getSelectedLearnerGroupList().add(item);
							selectedLearnerGroupList.add(item);



							break;
						}
					}
					j++;
				}
				j=0;
				i++;
			}
		form.setSelectedLearnerGroupList(selectedLearnerGroupList);

		i=0;
		j=0;

		OrganizationalGroup orgitem;
		String orgselecteditem;
		List<OrganizationalGroup> selectedOrgGroupList =  new ArrayList<OrganizationalGroup>();
		if(form.getGroups()!=null)
			for(;i<form.getOrgGroupListFromDB().size();){
				orgitem = form.getOrgGroupListFromDB().get(i);
				for(;j<form.getGroups().length;){
					orgselecteditem = (form.getGroups())[j];
					if(orgitem != null){
						if(orgitem.getId()==(Long.parseLong(orgselecteditem))){
							//addRecipientForm.getSelectedOrgGroupList().add(orgitem);
							selectedOrgGroupList.add(orgitem);


							break;
						}
					}
					j++;

				}
				j=0;
				i++;
			}
		form.setSelectedOrgGroupList(selectedOrgGroupList);
		String courseselecteditem;
		Course courseItem;
		i=0;

		if(currentPage==5 && this.getTargetPage(request, currentPage)==6){
			List<Course> selectedCourseList=  new ArrayList<Course>();
			if(form.getCourses()!=null)
				for(;i<form.getCourseListFromDB().size();){
					courseItem = form.getCourseListFromDB().get(i);
					for(;j<form.getCourses().length;){
						courseselecteditem=(form.getCourses())[j];
						if(courseItem != null){
							if(courseItem.getId()==(Long.parseLong(courseselecteditem))){
								//courseItem.setCourseAlertTriggeerFilter(form.getCourseAlertTriggeerFilter());
								selectedCourseList.add(courseItem);
								break;
							}
						}
						j++;
					}
					j=0;
					i++;
				}
			if(selectedCourseList.isEmpty() || selectedCourseList.size() < 0) {
				form.setSelectedCourseList(form.getCourseList());
			} else {
				form.setSelectedCourseList(selectedCourseList);
			}
			
		}
		super.postProcessPage(request, form, errors, currentPage);

	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		log.debug("IN referenceData");
		Map<String, Object> model = new HashMap<>();
		FilterTriggerForm form =(FilterTriggerForm)command;

		switch(page){
		case 0:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			//com.softech.vu360.lms.vo.VU360User loggedInUser = null;
			VU360User vu360UserModel = null;
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder.getContext().getAuthentication()).getDetails();
			
			if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
				com.softech.vu360.lms.vo.Customer customerVO = (com.softech.vu360.lms.vo.Customer) request.getSession(true).getAttribute("adminSelectedCustomer");
				com.softech.vu360.lms.vo.Distributor distributorVO = (com.softech.vu360.lms.vo.Distributor) request.getSession(true).getAttribute("adminSelectedDistributor");
				if(customerVO != null) {
					Long learnerId = learnerService.getLearnerForSelectedCustomer(customerVO.getId());
					vu360UserModel = learnerService.getLearnerByID(learnerId.longValue()).getVu360User();
				}else if(distributorVO != null) {
					Long learnerId = learnerService.getLearnerForSelectDistributor(distributorVO.getMyCustomer().getId());
					vu360UserModel = learnerService.getLearnerByID(learnerId).getVu360User();
				} else {
					vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
				}
			} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
			}

			OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(vu360UserModel.getLearner().getCustomer().getId());
			String[] orgGroupList = form.getGroups();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if(orgGroupList!=null && orgGroupList.length>0){
				for(String orgGroup:orgGroupList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot  = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,vu360UserModel);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			
			model.put("orgGroupTreeAsList", treeAsList);
			return model;
		case 5:
			break;
		case 6:
			break;
		default:
			break;
		}
		return super.referenceData(request, command, errors, page);
		
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		AddFilterValidator validator = (AddFilterValidator)this.getValidator();
		FilterTriggerForm form =(FilterTriggerForm)command;
		switch(page){
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			break;
		case 2:
			if(!form.getAction().equalsIgnoreCase("search")) {
				if(form.getFilterType().equalsIgnoreCase(LEARNERS)) {
					validator.validateLearnerSelectPage(form, errors);
				}
			}
			break;
		case 3:
			if(form.getFilterType().equalsIgnoreCase(LEARNER_GROUPS)) {
				validator.validateLearnerGroupSelectPage(form, errors);
			}
			break;
		case 4:
			if(form.getFilterType().equalsIgnoreCase(ORGANIZATION_GROUPS)) {
				validator.validateOrgGroupSelectPage(form, errors);
			}
			break;
		case 5:
			if(!form.getAction().equalsIgnoreCase("Next") && !form.getAction().equalsIgnoreCase("Previous")){
				if(!form.getAction().equalsIgnoreCase("search")) {
					if(form.getFilterType().equalsIgnoreCase(COURSES)) {
						validator.validateCourseSelectPage(form, errors);
					}
				}
			}
			
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

		VU360User vu360UserModel = null;
		
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder.getContext().getAuthentication()).getDetails();
		
		if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
			com.softech.vu360.lms.vo.Customer customerVO = (com.softech.vu360.lms.vo.Customer) request.getSession(true).getAttribute("adminSelectedCustomer");
			com.softech.vu360.lms.vo.Distributor distributorVO = (com.softech.vu360.lms.vo.Distributor) request.getSession(true).getAttribute("adminSelectedDistributor");
			if(customerVO != null) {
				Long learnerId = learnerService.getLearnerForSelectedCustomer(customerVO.getId());
				vu360UserModel = learnerService.getLearnerByID(learnerId.longValue()).getVu360User();
			}else if(distributorVO != null) {
				Long learnerId = learnerService.getLearnerForSelectDistributor(distributorVO.getMyCustomer().getId());
				vu360UserModel = learnerService.getLearnerByID(learnerId).getVu360User();
			} else {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
			}
		} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
			vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
		}
		
		FilterTriggerForm form =(FilterTriggerForm)command;
		
		log.debug("in GET TARGET PAGE...."+currentPage);
		if(currentPage==1 && this.getTargetPage(request, currentPage) != 0){
			if (form.getFilterType().equals(LEARNERS)){
				form.setPageIndex("2");	
				String firstName=request.getParameter("firstName");
				String lastName=request.getParameter("lastName");
				String email=request.getParameter("email");
				List<VU360User> users = null;
				if(request.getParameter("search") != null){
					if(request.getParameter("search").equalsIgnoreCase("doSearch")){
						users = learnerService.findLearner(firstName, lastName, email, 
								vu360UserService.hasAdministratorRole(vu360UserModel), vu360UserService.hasTrainingAdministratorRole(vu360UserModel), vu360UserModel.getTrainingAdministrator().getId(), 
								vu360UserModel.getTrainingAdministrator().isManagesAllOrganizationalGroups(), vu360UserModel.getTrainingAdministrator().getManagedGroups(), 
								vu360UserModel.getLearner().getCustomer().getId(), vu360UserModel.getId());
						form.setLearnerListFromDB(users);
					}
				}
				else{
					form.setLearnerListFromDB(users);
				}
				return 2;
			}

			else if (form.getFilterType().equals(LEARNER_GROUPS)){
				form.setPageIndex("3");
				List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getAllLearnerGroups(vu360UserModel.getLearner().getCustomer().getId(), vu360UserModel);
				form.setLearnerGroupListFromDB(learnerGroups);
				return 3;
			}

			else if (form.getFilterType().equals(ORGANIZATION_GROUPS)){
				form.setPageIndex("4");
				List<OrganizationalGroup> orgGroup = orgGroupLearnerGroupService.getAllOrganizationalGroups(vu360UserModel.getLearner().getCustomer().getId());
				form.setOrgGroupListFromDB(orgGroup);
				return 4;
			}

			else if (form.getFilterType().equals(COURSES)){
				form.setPageIndex("5");
				Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
						getAuthentication().getDetails()).getCurrentCustomerId();
				List<Course> courseList= new ArrayList<Course>();
				List<Course> courses= new ArrayList<Course>();
				courseList= entitlementService.getAllCoursesByEntitlement(customerId);
				courses=entitlementService.getAllCoursesByEntitlement(customerId);
				for(Course selectedCourse : courses){
					if(selectedCourse instanceof LegacyCourse){
						courseList.remove(selectedCourse);
					}
				}
				form.setCourseListFromDB(courseList);
				form.setCourseList(courseList);
				return 5;
			}		
		}
		
		if (form.getFilterType().equals(COURSES)&& currentPage==5){
			
		  if(!request.getParameter("paging").equals("paging")){
			boolean flag = true;
			List<Course> courseList= new ArrayList<Course>();
			List<Course> allCourseList = form.getCourseList();
			if(!form.getAction().equals("search")) {
				
			if(form.getCourseName()=="" && form.getCourseType().trim().equalsIgnoreCase("All")){
				form.setCourseListFromDB(allCourseList);
				flag=false;
			}
				if(flag){
					for(Course selectedCourse : allCourseList){
						if(form.getCourseName()!= "" && !form.getCourseType().trim().equalsIgnoreCase("All")){
							if(selectedCourse.getCourseTitle().contains(form.getCourseName())){
								if(form.getCourseType().contains(selectedCourse.getCourseType())){
									courseList.add(selectedCourse);
									
								}
							}
						}
						else if(form.getCourseName()!= "" && selectedCourse.getCourseTitle().contains(form.getCourseName())){
					
							if(form.getCourseType()!="All" && form.getCourseType().contains(selectedCourse.getCourseType())){
								courseList.add(selectedCourse);
								
							}
							else{
								courseList.add(selectedCourse);
								//break;
							}
						}else if(form.getCourseType().trim()!="All"){
							if(form.getCourseType().contains(selectedCourse.getCourseType())){
								courseList.add(selectedCourse);
							}
						}
			}
			form.setCourseListFromDB(courseList);
			}
			}
		  }
			form.setPageIndex("5");
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}
	
	
	/*
	 * Method to search courses by name and type.
	 * LMS-14294
	 */
	public List<Course> getSearchCriteriaCourses(Long customerId, String courseName, String courseType) {
		List<Course> courseList = new ArrayList<Course>();
		courseList = entitlementService.getAllCoursesByEntitlement(customerId, courseName, courseType);
		return courseList;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public static String getLEARNERS() {
		return LEARNERS;
	}

	public static String getLEARNER_GROUPS() {
		return LEARNER_GROUPS;
	}

	public static String getORGANIZATION_GROUPS() {
		return ORGANIZATION_GROUPS;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
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

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public static String getCOURSES() {
		return COURSES;
	}

	public static int getSEARCH_COURSE_PAGE_SIZE() {
		return SEARCH_COURSE_PAGE_SIZE;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	
	public LearnerLicenseService getLearnerLicenseServices() {
		return learnerLicenseServices;
	}

	public void setLearnerLicenseServices(
			LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}

}

