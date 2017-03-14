package com.softech.vu360.lms.web.controller.manager;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SaveSurveyParam;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AssignSurveyForm;
import com.softech.vu360.lms.web.controller.model.CustomerItemForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.model.SurveyItem;
import com.softech.vu360.lms.web.controller.validator.AssignSurveyValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.AssignSurveyAsyncTask;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;


public class AssignSurveyController extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AssignSurveyController.class.getName());

	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private SurveyService surveyService;
	private CustomerService customerService;

	private SynchronousClassService synchronousClassService=null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private CourseAndCourseGroupService courseCourseGrpService;
    private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	private VelocityEngine velocityEngine;
	private LearnersToBeMailedService learnersToBeMailedService;
	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String MANAGE_USER_SORT_LASTNAME = "lastName";
	private static final String MANAGE_USER_SORT_USERNAME = "emailAddress";
	private static final String MANAGE_USER_SORT_ACCOUNTLOCKED = "accountNonLocked";
	private static final String[] SORTABLE_COLUMNS = {MANAGE_USER_SORT_FIRSTNAME,MANAGE_USER_SORT_LASTNAME,MANAGE_USER_SORT_USERNAME,MANAGE_USER_SORT_ACCOUNTLOCKED};

	public static final String SURVEY_METHOD_LEARNER = "Learner";
	public static final String SURVEY_METHOD_ORGGROUP = "OrgGroup";
	public static final String SURVEY_METHOD_LEARNERGROUP = "LearnerGroup";
	public static final String SURVEY_METHOD_CUSTOMER = "Customer";
	public static final String SURVEY_METHOD_RESELLER = "Reseller";
	
	private static final int SEARCH_SURVEY_PAGE_SIZE = 10;
	private boolean previousClicked = false;

	private String closeTemplate = null;


	public AssignSurveyController() {
		super();
		setCommandName("enrollmentForm");
		setCommandClass(AssignSurveyForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/survey/assignSurvey/selectAssignmentType"
				, "manager/survey/assignSurvey/selectLearners"
				, "manager/survey/assignSurvey/selectOrgGroup"
				, "manager/survey/assignSurvey/selectLearnerGroup"
				, "manager/survey/assignSurvey/selectSurveys"
				, "manager/survey/assignSurvey/selectSurveyDates"
				
				, "manager/survey/assignSurvey/selectSurveyConfigurationSettings"
				, "manager/survey/assignSurvey/assignSurveySummary"
				, "manager/survey/assignSurvey/selectCustomers"});
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

		try{
			AssignSurveyForm enrollmentDetails = (AssignSurveyForm)command;
			enrollmentDetails.setCourseSearchResultsPageSize(SEARCH_SURVEY_PAGE_SIZE);
			
			VU360User logInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomerId();
/*
 * @TODO UNUSED CODE REVIEW 
 */
			List<OrganizationalGroup> LearnerOrgGroups = null;

			if( vu360UserService.hasAdministratorRole(logInUser) ) {
				LearnerOrgGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customerId);
			} else {
				if(logInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()){
					LearnerOrgGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customerId);
				} else {
					LearnerOrgGroups = logInUser.getTrainingAdministrator().getManagedGroups();
				}
			}
			List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList<LearnerGroup>();

			selectedLearnerGroupsAssociatedWithOrgGroup.addAll(orgGroupLearnerGroupService.getAllLearnerGroups(customerId,logInUser));

			List<LearnerGroupEnrollmentItem> learnerGroupList = new ArrayList <LearnerGroupEnrollmentItem>();

			for(int learnerGroupNo=0; learnerGroupNo<selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++) {

				LearnerGroup lgrp = selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo);
				LearnerGroupEnrollmentItem item = new LearnerGroupEnrollmentItem(); 
				item.setLearnerGroupId(lgrp.getId());
				item.setLearnerGroupName(lgrp.getName());
				item.setSelected(false);
				learnerGroupList.add(item);
			}
			enrollmentDetails.setLearnerGroupEnrollmentItems(learnerGroupList);
 
		}
		catch (Exception e) {
			log.error("exception", e);
		}
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
	/*
	 * TODO - delete 
	 * */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		log.debug("IN initBinder");
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
	 *  TODO TO BE REMOVED
	 */
	protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
		log.debug("IN onBindOnNewForm");
		
		super.onBindOnNewForm(request, command, binder);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 * TODO TO BE REMOVED
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		ModelAndView modelNView = super.showForm(request, response, binder);
		String view = modelNView.getViewName();
		log.debug("OUT showForm for view = "+view);
		return modelNView;
	}

	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 * TODO -  ALL LOADING DATA SHOULLD BE MOVED HRE FROM BACKING OBJECT
	 */
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		log.debug("IN referenceData");
		Map <Object, Object>model = new HashMap <Object, Object>();
		AssignSurveyForm enrollmentDetail= (AssignSurveyForm)command;
		Map <Object, Object>surveyMethodMap = new LinkedHashMap <Object, Object>();
		
		if (enrollmentDetail.getEnrollmentMethod() != null && !enrollmentDetail.getEnrollmentMethod().trim().equals(""))
			surveyMethodMap.put("selectedEnrollmentMethod", enrollmentDetail.getEnrollmentMethod());
		else
			surveyMethodMap.put("selectedEnrollmentMethod", "");
		
		switch(page){

		case 0:

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			surveyMethodMap.put(SURVEY_METHOD_LEARNER, SURVEY_METHOD_LEARNER);
			surveyMethodMap.put(SURVEY_METHOD_ORGGROUP, SURVEY_METHOD_ORGGROUP);
			surveyMethodMap.put(SURVEY_METHOD_LEARNERGROUP, SURVEY_METHOD_LEARNERGROUP);
			surveyMethodMap.put(SURVEY_METHOD_CUSTOMER, SURVEY_METHOD_CUSTOMER);
			surveyMethodMap.put(SURVEY_METHOD_RESELLER, SURVEY_METHOD_RESELLER);
			surveyMethodMap.put("pageNo_1", 0);

			model.put("surveyMethodMap", surveyMethodMap);
			return model;

		case 1: 

			surveyMethodMap.put("pageNo_2", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model;			

		case 2:

			OrganizationalGroup rootOrgGroup = null;
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomerId();
			rootOrgGroup  = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);

			String[] orgGroupList = enrollmentDetail.getGroups();
			List<Long> orgGroupIdList = new ArrayList<Long>();
			if(orgGroupList!=null && orgGroupList.length>0){
				for(String orgGroup:orgGroupList){
					Long orgGroupId = Long.parseLong(orgGroup);
					orgGroupIdList.add(orgGroupId);
				}
			}
			TreeNode orgGroupRoot  =ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup,orgGroupIdList,loggedInUser);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			for(TreeNode node:treeAsList )
				log.debug(node.toString());
			model.put("orgGroupTreeAsList", treeAsList);
			surveyMethodMap.put("pageNo_2", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model; 

		case 3:
			
			surveyMethodMap.put("pageNo_2", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model;

		case 4:
			surveyMethodMap.put("pageNo_3", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model;
			
		case 5:
			surveyMethodMap.put("pageNo_4", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model;
		case 6:
			surveyMethodMap.put("pageNo_5", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model;
			
		case 7:

			model.put("learnersAttemptedToEnroll", enrollmentDetail.getAttemptedToEnroll());
			model.put("surveysAssigned", enrollmentDetail.getSurveysAssigned() );
			
			surveyMethodMap.put("pageNo_6", 0);
			model.put("surveyMethodMap", surveyMethodMap);
			return model;			
		case 8:
			/*
			 * Reference Data for Customer selection page
			 */
			surveyMethodMap.put("pageNo_2", 0); //Same as in case of 2
			model.put("surveyMethodMap", surveyMethodMap);
			return model;
		default :
			break;
		}

		return super.referenceData(request, page);
	}

	/**
	 * we can do custom processing after binding and validation
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

		log.debug("IN onBindAndValidate");
		AssignSurveyForm form = (AssignSurveyForm)command;
		AssignSurveyValidator validator = (AssignSurveyValidator)this.getValidator();

		int targetPage = this.getTargetPage(request, command, errors, 1);
		
		//sync the learner list with the selected learner list 
		if (((page == 1 || page == 8) 
				&& !StringUtils.isBlank(form.getAction()) 
				&& form.getAction().equalsIgnoreCase("search"))
				||(targetPage==4)
				||(targetPage==0)
		){
			if (form.getEnrollmentMethod().equals(SURVEY_METHOD_LEARNER))
			{
				List<LearnerItemForm> learnerList = form.getLearners();
				Collections.sort(learnerList);
	
				List<LearnerItemForm> selectedLearnerList = form.getSelectedLearners();
				Collections.sort(selectedLearnerList);
	
				List<LearnerItemForm> mergedLearnerList = new ArrayList<LearnerItemForm>();
				int i=0, j=0;
				LearnerItemForm item, selecteditem;
				for(;i<learnerList.size()&&j<selectedLearnerList.size();){
					item = learnerList.get(i);
					selecteditem = selectedLearnerList.get(j);
					if(item.compareTo(selecteditem)<0){
						i++;
						if(item.isSelected())
							mergedLearnerList.add(item);
					}else if(item.compareTo(selecteditem)>0){
						j++;
						mergedLearnerList.add(selecteditem);
					}else{
						if(item.isSelected())
							mergedLearnerList.add(item);
						i++; j++;
					}
				}
				for(;i<learnerList.size();i++){
					item = learnerList.get(i);
					if(item.isSelected())
						mergedLearnerList.add(item);
				}
				for(;j<selectedLearnerList.size();j++){
					selecteditem = selectedLearnerList.get(j);
					mergedLearnerList.add(selecteditem);
				}
				form.setSelectedLearners(mergedLearnerList);
			}
			else if (form.getEnrollmentMethod().equals(SURVEY_METHOD_CUSTOMER)) {

				List<CustomerItemForm> customersList = form.getCustomers();
				Collections.sort(customersList);
	
				List<CustomerItemForm> selectedCustomerList = form.getSelectedCustomers();
				Collections.sort(selectedCustomerList);
	
				List<CustomerItemForm> mergedCustomerList = new ArrayList<CustomerItemForm>();
				int i=0, j=0;
				CustomerItemForm item, selecteditem;
				for(;i<customersList.size()&&j<selectedCustomerList.size();){
					item = customersList.get(i);
					selecteditem = selectedCustomerList.get(j);
					if(item.compareTo(selecteditem)<0){
						i++;
						if(item.isSelected())
							mergedCustomerList.add(item);
					}else if(item.compareTo(selecteditem)>0){
						j++;
						mergedCustomerList.add(selecteditem);
					}else{
						if(item.isSelected())
							mergedCustomerList.add(item);
						i++; j++;
					}
				}
				for(;i<customersList.size();i++){
					item = customersList.get(i);
					if(item.isSelected())
						mergedCustomerList.add(item);
				}
				for(;j<selectedCustomerList.size();j++){
					selecteditem = selectedCustomerList.get(j);
					mergedCustomerList.add(selecteditem);
				}
				form.setSelectedCustomers(mergedCustomerList);
			}
		}
		if( page == 4 ) {
			
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomer();
			
			coursesSearch(form, customer,request);
			coursesSearchMove(form);
			coursesSearchShowAll(form); // must be executed last
			//pickUptheSelectedCourses(form);
			//validator.validateFifthPage(form, errors);
			
		}
		
		if (page == 4 && (targetPage == 8 || targetPage == 1)
				|| (page == 1 || page == 8) && targetPage == 0)
			previousClicked = true;
		else 
			previousClicked = false;
		
		super.onBindAndValidate(request, command, errors, page);

	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(closeTemplate);
		//return super.processCancel(request, response, command, error);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processFinish");
		int Page=getCurrentPage(request);

		if(Page == 7){
			log.debug("getting page 7");
		}
		return new ModelAndView(closeTemplate);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		log.debug("IN getTargetPage");
		AssignSurveyForm form = (AssignSurveyForm)command;
		HttpSession session = request.getSession();

		if ( currentPage == 1 || currentPage == 8) { 

			if(request.getParameter("action")!=null
					&& request.getParameter("action").equals("simpleSearch")) {
				if (form.getEnrollmentMethod().equalsIgnoreCase(SURVEY_METHOD_CUSTOMER))
					return 8;
				if (form.getEnrollmentMethod().equalsIgnoreCase(SURVEY_METHOD_RESELLER))
					return 4;
				else
					return 1;
			}
			
			if (form.getEnrollmentMethod().equalsIgnoreCase(SURVEY_METHOD_CUSTOMER))
				session.setAttribute("pageIndex",8);
			else if (form.getEnrollmentMethod().equalsIgnoreCase(SURVEY_METHOD_RESELLER))
				session.setAttribute("pageIndex",0);
			else
				session.setAttribute("pageIndex",1);

		} else if ( currentPage == 0 ) {

			String enrollmentMethod = form.getEnrollmentMethod();

			if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_LEARNER)) {
				return 1;
			} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_ORGGROUP)) {
				return 2;
			} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_LEARNERGROUP)) {
				return 3;
			} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_CUSTOMER)) {
				return 8;
			} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_RESELLER)) {
				if (form.getEnrollmentMethod() != null && form.getEnrollmentMethod().equals(SURVEY_METHOD_RESELLER))
					session.setAttribute("pageIndex", 0);
				return 4;
			} 

		} else if ( currentPage == 5 ) {

			String enrollmentMethod = form.getEnrollmentMethod();

			if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_LEARNER)) {
				session.setAttribute("pageIndex",1);
			} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_ORGGROUP)) {
				session.setAttribute("pageIndex",2);
			} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_LEARNERGROUP)) {
				session.setAttribute("pageIndex",3);
			}

		} else if ( currentPage == 2 ) {

			session.setAttribute("pageIndex",2);

		} else if ( currentPage == 3 ) {

			session.setAttribute("pageIndex",3);

		}
		else if ( currentPage == 4) {
			if (form.getEnrollmentMethod() != null && form.getEnrollmentMethod().equals(SURVEY_METHOD_RESELLER))
				session.setAttribute("pageIndex", 0);
		}
		log.debug(" current Page ===>> " + currentPage);
		return super.getTargetPage(request, command, errors, currentPage);
	}

	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, List<Long> selectedOrgGroups){
		log.debug("IN getOrgGroupTree");
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

	public String getCloseTemplate() {
		return closeTemplate;
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		log.debug("IN postProcessPage");
		AssignSurveyForm form = (AssignSurveyForm)command;
		//CourseStatistics newCourseStatistics = new CourseStatistics();

		//Added by Faisal A. Siddiqui July 21 09 for new branding
		Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());

		if (((page == 1 || page == 8) 
				&& ((!StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))//search or paging or sort done by this action
						||(this.getTargetPage(request, command, errors, 1)==4) && errors.hasErrors()))//trying to move to next page with errors
						||(page == 4 && this.getTargetPage(request, command, errors, 1)==1)//back from next page
						||(page == 0 && this.getTargetPage(request, command, errors, 1)==1)//back from previous page
						||(page == 4 && this.getTargetPage(request, command, errors, 1)==8)
						||(page == 0 && this.getTargetPage(request, command, errors, 1)==8)
		) {

			if(!StringUtils.isBlank(form.getSearchType())){
				Map<Object,Object> results = new HashMap<Object,Object>();
				//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				int pageNo = form.getPageIndex()<0 ? 0 : form.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
				int sortColumnIndex = form.getSortColumnIndex();
				String sortBy = (sortColumnIndex>=0 && sortColumnIndex<SORTABLE_COLUMNS.length) ? SORTABLE_COLUMNS[sortColumnIndex]: MANAGE_USER_SORT_FIRSTNAME ;
				int sortDirection = form.getSortDirection()<0 ? 0:(form.getSortDirection()>1?1:form.getSortDirection());
				List<VU360User> userList=null;
				List<Customer> customerList = null;
				
				if(form.getSearchType().equalsIgnoreCase("simplesearch")) {
					//userList=learnerService.findLearner(form.getSearchKey(), loggedInUser);
					Integer totalResults = 0;
					if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (tempManagedGroups!=null && tempManagedGroups.size() > 0 ) {


							results=learnerService.findLearner1(form.getSearchKey().trim(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}

					}else {

						results=learnerService.findLearner1(form.getSearchKey().trim(), 
								loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}

					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				else if(form.getSearchType().equalsIgnoreCase("advancesearch")) {
					//userList=learnerService.findLearner(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), loggedInUser);
					Integer totalResults = 0;
					
					VU360UserAuthenticationDetails authenticationDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
					if (authenticationDetails.getCurrentSearchType() == AdminSearchType.DISTRIBUTOR ) {
						
						if (form.getEnrollmentMethod().equals(SURVEY_METHOD_LEARNER))
						{
							if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
								if (tempManagedGroups!=null && tempManagedGroups.size() > 0) {
		
		
									results = learnerService.findAllLearnersOfCustomersOfReseller(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), 
											form.getSearchEmailAddress().trim(), pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, 
											sortBy, sortDirection, form.getEnrollmentMethod());
									totalResults = (Integer)results.get("recordSize");
									userList = (List<VU360User>)results.get("list");
								}
		
							}else {	
								results = learnerService.findAllLearnersOfCustomersOfReseller(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), 
										form.getSearchEmailAddress().trim(), pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, 
										sortBy, sortDirection, form.getEnrollmentMethod());
								totalResults = (Integer)results.get("recordSize");
								userList = (List<VU360User>)results.get("list");
							}
						}
						else if (form.getEnrollmentMethod().equals(SURVEY_METHOD_CUSTOMER)){
							results = customerService.getCustomersByCurrentDistributor(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							customerList = (List<Customer>)results.get("list");
						}
					}
					else {
						if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
							if (tempManagedGroups!=null && tempManagedGroups.size() > 0 ) {
	
	
								results = learnerService.findLearner1(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
										loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
										loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
										loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
										pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
								totalResults = (Integer)results.get("recordSize");
								userList = (List<VU360User>)results.get("list");
							}
	
						}else {
	
							results = learnerService.findLearner1(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}
					}
					
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				else if(form.getSearchType().equalsIgnoreCase("allsearch")) {
					//userList = learnerService.findLearner("", loggedInUser);
					Integer totalResults = 0;
					if( !loggedInUser.isAdminMode() &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (tempManagedGroups!=null && tempManagedGroups.size() > 0) {


							results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
									loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									sortBy, sortDirection);
							userList = (List<VU360User>)results.get("list");
						}

					}else {

						results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
								loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								sortBy, sortDirection);
						userList = (List<VU360User>)results.get("list");
					}


				}

				if(userList!=null && userList.size()>0){
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					for(VU360User auser:userList){
						LearnerItemForm learnerItem = new LearnerItemForm();
						learnerItem.setSelected(false);
						learnerItem.setUser(auser);
						learnerList.add(learnerItem);
					}
					for(LearnerItemForm learnerItem: learnerList){
						for(LearnerItemForm preSelectedItem:form.getSelectedLearners()){
							if(learnerItem.compareTo(preSelectedItem)==0){
								learnerItem.setSelected(true);
								break;
							}
						}
					}
					form.setLearners(learnerList);
				}else{
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					form.setLearners(learnerList);
				}
				
				if (customerList != null && customerList.size() > 0) {
					List<CustomerItemForm> customers = new ArrayList<CustomerItemForm>();
					for(Customer customer:customerList){
						CustomerItemForm customerItem = new CustomerItemForm();
						customerItem.setCustomer(customer);
						customerItem.setSelected(false);
						customers.add(customerItem);
					}
					
					for(CustomerItemForm customerItem: customers){
						for(CustomerItemForm preSelectedItem:form.getSelectedCustomers()){
							if(customerItem.compareTo(preSelectedItem)==0){
								customerItem.setSelected(true);
								break;
							}
						}
					}
					form.setCustomers(customers);
				}else{
					List<CustomerItemForm> customers = new ArrayList<CustomerItemForm>();
					form.setCustomers(customers);
				}
			}
		}
		if ( page == 6 ){
			if(form.getPrevToDate().equalsIgnoreCase("false")){
				String url=request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+ "/login.do";
				
				saveSurveyAssignment(request,form, brander);
			}
		}

		super.postProcessPage(request, command, errors, page);
	}

     
	private void saveSurveyAssignment(HttpServletRequest request,AssignSurveyForm form, Brander brander) throws Exception {
		log.debug("IN saveSurveyAssignment");
        VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
        List<Learner> surveyLearners = getLearnersTobeEnrolled(form);
        boolean modifyAllsurvey =false ;
        boolean openSurvey =false ;  
         
        if(!surveyLearners.isEmpty()){
        	surveyLearners.get(0);
    		/**
    		 * Sultan.mubasher
    		 * LMS-7830 setting Brander object for Customer when Customer is selected form Admin mode
    		 */
    		VU360UserAuthenticationDetails adminDetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
    		if(adminDetails.getCurrentSearchType().equals(AdminSearchType.CUSTOMER)){
    			// Modified By MariumSaud : VU360User fetch again as the learner object in SurveyLearner has customized User Object created From customized Constructor from Learner.java 
    			// to improve performance inacse of Enrollment through OrgGrpEntitlement
    			VU360User vu360user = vu360UserService.getUpdatedUserById(surveyLearners.get(0).getVu360User().getId());
    			brander=VU360Branding.getInstance().getBranderByUser(request, vu360user);    		
    		}
        }
        
        
        if (form.getSurveyDateAssignment().equalsIgnoreCase("open")){
        	openSurvey = true;
        }
        else if(form.getSurveyDateAssignment().equalsIgnoreCase("all")){
        	modifyAllsurvey =true;
        }
 
        SaveSurveyParam saveSurveyParam = new SaveSurveyParam(form.getSurveyItemList(),
        		surveyLearners, modifyAllsurvey, form.getAllSurveyStartDate(),
                form.getAllSurveyEndDate() , form.isEmailOnCompletion() , openSurvey);       
        saveSurveyParam.setBrander(brander);
        saveSurveyParam.setUser(user);
        /* ***************************************************************************************************************       */
        saveSurveyParam.setNotifyLearner(form.isSendSurveyNotificationToLearners());
        saveSurveyParam.setNotifyManagerOnConfirmation(form.isEmailOnConfirmation());
        saveSurveyParam.setNotifyMeOnLearnerSurveyCompletions(form.isEmailOnCompletion() );
        saveSurveyParam.setOpenSurvey(openSurvey);
        /* **************************************************************************************************************       */
 

        saveSurveyParam.setVelocityEngine(velocityEngine);
        saveSurveyParam.setEmailService(learnersToBeMailedService);
 
        //EnrollmentVO enrollmentVO = enrollmentService.saveEnrollment(saveSurveyParam);
//      async code here
        AssignSurveyAsyncTask task = new AssignSurveyAsyncTask();
        task.setSaveSurveyParam(saveSurveyParam);
        task.setSurveyService( surveyService);
        asyncTaskExecutorWrapper.execute(task);
 
		form.setAttemptedToEnroll(surveyLearners.size());
 
		int noOfSurveysSelected = 0;
		for(SurveyItem surveyItem:form.getSurveyItemList()){
			if( surveyItem.isSelected())
				noOfSurveysSelected++;
		}
		form.setSurveysAssigned(noOfSurveysSelected);
 
	}

    @SuppressWarnings("unchecked")
    private Map createHashMapModelForEnrollUserEmail(EnrollUserEmailParam enrollUserEmailParam) {
    	log.debug("IN createHashMapModelForEnrollUserEmail");
        Map model = new HashMap();
        model.put("user", enrollUserEmailParam.getUser());
        model.put("learnersAttemptedToEnroll", enrollUserEmailParam.getLearnersToBeEnrolled().size());
        if(enrollUserEmailParam.getUniqueLearners() !=null && enrollUserEmailParam.getUniqueLearners().size()>0)
            model.put("learnersEnrolledSucssessfully", enrollUserEmailParam.getUniqueLearners().size());
        else
            model.put("learnersEnrolledSucssessfully", 0);
        if(enrollUserEmailParam.getUniqueCourses() !=null && enrollUserEmailParam.getUniqueCourses().size()>0)
            model.put("courses", enrollUserEmailParam.getUniqueCourses());
        else
            model.put("courses", 0);

        model.put("coursesAssigned", enrollUserEmailParam.getCourseNumber());
        model.put("enrollmentsCreated", enrollUserEmailParam.getEnrollmentsCreated());
        model.put("learnersNotEnrolled", enrollUserEmailParam.getLearnersFailedToEnroll());
        model.put("enrollmentsUpdated", enrollUserEmailParam.getEnrollmentsUpdated());
        model.put("brander", enrollUserEmailParam.getBrander());//to fix LMS-3214
        String support = enrollUserEmailParam.getBrander().getBrandElement("lms.email.managerenrollment.fromCommonName");
        model.put("support", support);
        return model;
    }
    // LMS-3212 joong end

	protected void validatePage(Object command, Errors errors, int page,
			boolean finish) {
		log.debug("IN validatePage");
		AssignSurveyValidator validator = (AssignSurveyValidator)this.getValidator();
		AssignSurveyForm form = (AssignSurveyForm)command;
		log.debug("Page num --- "+page);
		switch(page) {

		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			if(!form.getAction().equalsIgnoreCase("search"))
				validator.validateSecondPage(form, errors);
			break;
		case 2:
			validator.validateThirdPage(form, errors);
			break;
		case 3:
			if(!form.getLearnerGroupSearchAction().equalsIgnoreCase("search"))
				validator.validateFourthPage(form, errors);
			break;
		case 4:
			if (!previousClicked)
				validator.validateFifthPage(form, errors);
		break;
		case 5:
			validator.validateSixthPage(form, errors);
			break;
		case 8:
			if(!form.getAction().equalsIgnoreCase("search"))
				validator.validateEighthPage(form, errors);
			break;			
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		log.debug("IN onBind");
		AssignSurveyForm form = (AssignSurveyForm)command;
		if( getCurrentPage(request) == 1 ) {
			if( form.getGroups() == null ) {

			}
		}
		super.onBind(request, command, errors);
	}
	
    private List<Learner> getLearnersTobeEnrolled(AssignSurveyForm form ){
    	log.debug("IN getLearnersTobeEnrolled");
		String enrollmentMethod = form.getEnrollmentMethod();
		List<Learner> learnersToBeEnrolled = new ArrayList<Learner>();
		if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_LEARNER)) {
			if (form.getLearners()!=null && form.getLearners().size()>0){
				for(LearnerItemForm learner:form.getSelectedLearners()){
					
					learnersToBeEnrolled.add(learner.getUser().getLearner());
				}
			}
		} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_ORGGROUP)) {
			//Get Learners from selected Organizational Groups 

			String[] OrgGroupIdsArray = form.getGroups();

			List<Long> orgGroupIdList = new ArrayList<Long>();

			for( int i=0; i<OrgGroupIdsArray.length; i++ ) {
				String orgGroupID = OrgGroupIdsArray[i];
				if( StringUtils.isNotBlank(orgGroupID) ) {
					orgGroupIdList.add(Long.valueOf(OrgGroupIdsArray[i]));
				}	
			}
			Long orgGroupIdArray[] = new Long[orgGroupIdList.size()];
			orgGroupIdArray = orgGroupIdList.toArray(orgGroupIdArray);
			learnersToBeEnrolled = orgGroupLearnerGroupService.getLearnersByOrgGroupIds(orgGroupIdArray);
//			List<OrganizationalGroup> orgGroupList = orgGroupLearnerGroupService.getOrgGroupsById(orgGroupIdArray);
//			Set<Long> learneIdSet = new HashSet<Long>();
//			for(OrganizationalGroup orgGroup:orgGroupList){
//				for(Learner learner:orgGroup.getMembers()){
//					if(!learneIdSet.contains(learner.getId())){
//						learneIdSet.add(learner.getId());
//
//						learnersToBeEnrolled.add(learner);
//					}
//				}
//			}


		} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_LEARNERGROUP)) {

			List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = form.getLearnerGroupEnrollmentItems();

			if(learnerGroupEnrollmentItems!=null && learnerGroupEnrollmentItems.size()>0){
				List<Long> learnerGroupIdList = new ArrayList<Long>();

				for( LearnerGroupEnrollmentItem item:learnerGroupEnrollmentItems) {
					if (item.isSelected()== true){
						learnerGroupIdList.add(item.getLearnerGroupId());
					} 
				}
				Long learnerGroupIdArray[] = new Long[learnerGroupIdList.size()]; 
				learnerGroupIdArray = learnerGroupIdList.toArray(learnerGroupIdArray);
				learnersToBeEnrolled = orgGroupLearnerGroupService.getLearnersByLearnerGroupIds(learnerGroupIdArray);
//				List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByLearnerGroupIDs(learnerGroupIdArray);
//				Set<Long> learneIdSet = new HashSet<Long>();
//				for(LearnerGroup lrnGroup:learnerGroups){
//					for(Learner learner:lrnGroup.getLearners()){
//						if(!learneIdSet.contains(learner.getId())){
//							learneIdSet.add(learner.getId());
//
//							learnersToBeEnrolled.add(learner);
//						}
//					}
//				}
			}
		} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_RESELLER)) {
			Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
					.getAuthentication().getDetails()).getCurrentDistributor();
			List<Learner> learners = learnerService.getLearnersOfCustomersOfSelectedReseller(distributor);
			if (learners != null && learners.size() > 0) {
				learnersToBeEnrolled.addAll(learners);
			}
		} else if (enrollmentMethod.equalsIgnoreCase(SURVEY_METHOD_CUSTOMER)) {
			if (form.getSelectedCustomers() !=null && form.getSelectedCustomers().size()>0){
				List<CustomerItemForm> customerItemFormList = form.getSelectedCustomers();
				for (CustomerItemForm customerItemForm : customerItemFormList) {
					Customer customer = customerItemForm.getCustomer();
					List<Learner> learners = learnerService.getAllLearnersOfCustomer(customer);
					
					if (learners != null && learners.size() > 0) {
						learnersToBeEnrolled.addAll(learners);
					}
				}
			}
		} 
		return learnersToBeEnrolled;
	}
	
	

	
	private void coursesSearch(AssignSurveyForm form,Customer customer ,HttpServletRequest request ) {
		customer.initializeOwnerParams();
		log.debug("IN coursesSearch");
		// if course  search  is requested 
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		
		if( form.getSurveySearchType().trim().equalsIgnoreCase("advanceSearch") ){
			
			int intLimit  = 100;
			Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");
			
			if(StringUtils.isNumeric(limit.trim()))
				intLimit  = Integer.parseInt(limit);
			
			String surveyName = form.getSearchSurveyName();
			String surveyStatus = form.getSearchSurveyStatus();
			String retiredSurvey = form.getSearchSurveyRetired();
			String isEditable = request.getParameter("editable");//added for LMS-8506
			retiredSurvey = (retiredSurvey == null)? "false" : retiredSurvey.trim();//added for LMS-8506
			isEditable = (isEditable == null)? "false" : isEditable.trim();//added for LMS-8506
			//Commented as per the LMS-8506  List<Survey> surveyList = surveyService.findManualSurveys(surveyName ,surveyStatus , retiredSurvey ,customer,intLimit );
			List<Survey> surveyList=null;
			if( distributor != null && ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR) ) {
				distributor.initializeOwnerParams();
				surveyList = (List<Survey>) surveyService.getManualSurveyByName(distributor,surveyName,surveyStatus,retiredSurvey,isEditable);
			} else {
				surveyList = (List<Survey>) surveyService.getManualSurveyByName(customer,surveyName,surveyStatus,retiredSurvey,isEditable); //??? all surveys...no filters???
			}
			
			List<Survey> assignedInspections = (List<Survey>) surveyService.getAssignedInspectionSurveys(customer);
			surveyList.removeAll(assignedInspections);
			
			List<SurveyItem> surveyItemList = new ArrayList<SurveyItem>();
			for(Survey survey: surveyList){
				
				SurveyItem surveyItem =new SurveyItem();
				survey.getName();// Don't remove this line, We need to initialize survey else throw lazy loading exception in assign survey for distributor
				surveyItem.setSurvey(survey);
				surveyItem.setSelected(false);
				surveyItemList.add(surveyItem);
				
			}
 			
			form.setSurveyItemList(surveyItemList);
			if( form.getSurveyItemList().size() <= SEARCH_SURVEY_PAGE_SIZE )
				form.setCourseSearchEnd( form.getSurveyItemList().size()-1);
			else
				form.setCourseSearchEnd( (SEARCH_SURVEY_PAGE_SIZE -1));
			form.setCourseSearchStart(0);
			form.setCourseSearchPageNumber(0);
			form.setCourseSearchShowAll( "" );
		
		}
		
	}
	

	void coursesSearchShowAll(AssignSurveyForm form){
		log.debug("IN coursesSearchShowAll");
		// show all courses is requested 
		if( form.getSurveySearchType().trim().equalsIgnoreCase("showAll") ){

			form.setCourseSearchEnd( form.getSurveyItemList().size()-1);
			form.setCourseSearchStart(0);
			form.setCourseSearchShowAll( "showAll" );
			form.setCourseSearchPageNumber(0);
			//form.setCourseSearchType("");
		}
		
	}
	
	void coursesSearchMove(AssignSurveyForm form){
		log.debug("IN coursesSearchMove");
		// show all courses is requested 
		if( form.getSurveySearchType().trim().equalsIgnoreCase("move") ){
			int total = form.getSurveyItemList().size();
		
			if( form.getCourseSearchDirection().trim().equalsIgnoreCase("next")){
				int newPageNumber = form.getCourseSearchPageNumber() + 1 ;
				if( ( newPageNumber * SEARCH_SURVEY_PAGE_SIZE ) > total ){
					log.debug( newPageNumber +" greater than  "+total );
					// nochange
				}
				else{
					int lastPageLimit = (form.getCourseSearchPageNumber()+2) *  SEARCH_SURVEY_PAGE_SIZE ;
					form.setCourseSearchStart( form.getCourseSearchEnd()+1 );
					form.setCourseSearchPageNumber( form.getCourseSearchPageNumber() + 1) ;
					if( total >= lastPageLimit  ) {
						 
						form.setCourseSearchEnd( (lastPageLimit-1) );
					}
					else 
						 
						form.setCourseSearchEnd( ( total-1) );
					

					
				}
				
			}
			else if( form.getCourseSearchDirection().trim().equalsIgnoreCase("prev") ){
				int newPageNumber = form.getCourseSearchPageNumber() - 1 ;
				if( ( newPageNumber * SEARCH_SURVEY_PAGE_SIZE ) < 0 ){
					log.debug( newPageNumber +" greater than  "+total );
					// nochange
				}
				else {
					int lastPageLimit = ( form.getCourseSearchPageNumber() - 1 ) *  SEARCH_SURVEY_PAGE_SIZE ;
					
					if( newPageNumber == 0 ) {
						form.setCourseSearchStart(0 );
					}
					else if( newPageNumber > 0 ) {
						form.setCourseSearchStart(  ((newPageNumber  *  SEARCH_SURVEY_PAGE_SIZE) ) );
					}

					else{
						form.setCourseSearchStart( 0 );
					}
					
					form.setCourseSearchEnd(  ( ((newPageNumber  *  SEARCH_SURVEY_PAGE_SIZE) + ( SEARCH_SURVEY_PAGE_SIZE -1) )) );
					form.setCourseSearchPageNumber( form.getCourseSearchPageNumber() - 1 ) ; 
				}
			}

		}
		
		form.setCourseSearchShowAll( "" );
		//form.setCourseSearchType("");
	}
	
 
	
	
	/*
	 * The getters are setters
	 */

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
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

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public SurveyService getEntitlementService() {
		return surveyService;
	}

	public void setEntitlementService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public CourseAndCourseGroupService getCourseCourseGrpService() {
		return courseCourseGrpService;
	}

	public void setCourseCourseGrpService(
			CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @return the learnersToBeMailedService
	 */
	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	/**
	 * @param learnersToBeMailedService the learnersToBeMailedService to set
	 */
	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

    public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
        this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
    }

    private static class EnrollUserEmailParam {
        private final Brander brander;
        private final VU360User user;
        private final List<Learner> learnersToBeEnrolled;
        private final List<Learner> uniqueLearners;
        private final List<Course> uniqueCourses;
        private final List<Learner> learnersFailedToEnroll;
        private final int enrollmentsCreated;
        private final int courseNumber;
        private final int enrollmentsUpdated;

        public EnrollUserEmailParam(Brander brander, VU360User user, List<Learner> learnersToBeEnrolled,
                                    List<Learner> uniqueLearners, List<Course> uniqueCourses,
                                    List<Learner> learnersFailedToEnroll, int enrollmentsCreated,
                                    int courseNumber, int enrollmentsUpdated) {
            this.brander = brander;
            this.user = user;
            this.learnersToBeEnrolled = learnersToBeEnrolled;
            this.uniqueLearners = uniqueLearners;
            this.uniqueCourses = uniqueCourses;
            this.learnersFailedToEnroll = learnersFailedToEnroll;
            this.enrollmentsCreated = enrollmentsCreated;
            this.courseNumber = courseNumber;
            this.enrollmentsUpdated = enrollmentsUpdated;
        }

        public Brander getBrander() {
            return brander;
        }

        public VU360User getUser() {
            return user;
        }

        public List<Learner> getLearnersToBeEnrolled() {
            return learnersToBeEnrolled;
        }

        public List<Learner> getUniqueLearners() {
            return uniqueLearners;
        }

        public List<Course> getUniqueCourses() {
            return uniqueCourses;
        }

        public List<Learner> getLearnersFailedToEnroll() {
            return learnersFailedToEnroll;
        }

        public int getEnrollmentsCreated() {
            return enrollmentsCreated;
        }

        public int getCourseNumber() {
            return courseNumber;
        }

        public int getEnrollmentsUpdated() {
            return enrollmentsUpdated;
        }
    }

	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}