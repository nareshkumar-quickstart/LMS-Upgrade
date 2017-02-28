package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SaveEnrollmentParam;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddTrainingPlanForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.validator.AssignTrainingPlanValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.EnrollLearnerAsyncTask;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;

public class AddTrainingPlanControllerStep2 extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddTrainingPlanControllerStep2.class.getName());

	private static final String ENROLLMENT_METHOD_LEARNER = "Learner";
	private static final String ENROLLMENT_METHOD_ORGGROUP = "OrgGroup";
	private static final String ENROLLMENT_METHOD_LEARNERGROUP = "LearnerGroup";
	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String MANAGE_USER_SORT_LASTNAME = "lastName";
	private static final String MANAGE_USER_SORT_USERNAME = "emailAddress";
	private static final String MANAGE_USER_SORT_ACCOUNTLOCKED = "accountNonLocked";
	private static final String[] SORTABLE_COLUMNS = {MANAGE_USER_SORT_FIRSTNAME,MANAGE_USER_SORT_LASTNAME,MANAGE_USER_SORT_USERNAME,MANAGE_USER_SORT_ACCOUNTLOCKED};

	private LearnerService learnerService;
	private TrainingPlanService trainingPlanService;
	private EntitlementService entitlementService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private CourseAndCourseGroupService courseCourseGrpService;
	private EnrollmentService enrollmentService;
	private SynchronousClassService synchronousClassService=null;
	private VU360UserService vu360UserService = null;

	private VelocityEngine velocityEngine;
	private LearnersToBeMailedService learnersToBeMailedService;
	private String summaryTemplate = null;
	private String closeTemplate = null;
    private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
    
    // added by muhammad akif : 10-07-014
    private static boolean ACCOUNT_NON_EXPIRED = true;
    private static boolean ACCOUNT_NON_LOCKED = true;
    private static boolean ENABLED = true;

    
	public AddTrainingPlanControllerStep2() {

		super();
		setCommandName("trainingPlanForm");
		setCommandClass(AddTrainingPlanForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/trainingPlan/selectTrainingPlan"
				, "manager/trainingPlan/addTrainingPlanMethod"
				, "manager/trainingPlan/trainingPlanLearnerGrp"
				, "manager/trainingPlan/trainingPlanSelectLearners"
				, "manager/trainingPlan/trainingPlanOrgGrp"
				, "manager/trainingPlan/trainingPlanSelectDates"
				, "manager/trainingPlan/trainingPlanSelectSchedule"
				, "manager/trainingPlan/addtrainingPlanSummery"
				, "manager/trainingPlan/assignTrainingPlanResults" });
	}

	protected Object formBackingObject(HttpServletRequest request)throws Exception {

		Object command = super.formBackingObject(request);
		try {
			AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
			VU360User logInUser = VU360UserAuthenticationDetails.getCurrentUser();

			List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList <LearnerGroup>();
			@SuppressWarnings("unused")
			List <OrganizationalGroup> learnerOrgGroups = null;

			/*if( logInUser.isLMSAdministrator() ) {
				learnerOrgGroups = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
						getAuthentication().getDetails()).getCurrentCustomer().getOrganizationalGroups();
			} else {
				if( logInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
					learnerOrgGroups = logInUser.getLearner().getCustomer().getOrganizationalGroups();
				} else {
					learnerOrgGroups = logInUser.getTrainingAdministrator().getManagedGroups();
				}
			}*/
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomer();
			learnerOrgGroups = orgGroupLearnerGroupService.getOrgGroupsByCustomer(customer);

			selectedLearnerGroupsAssociatedWithOrgGroup.addAll(orgGroupLearnerGroupService.getAllLearnerGroups(customer.getId(),logInUser));
			List<LearnerGroupEnrollmentItem> learnerGroupList = new ArrayList <LearnerGroupEnrollmentItem>();

			for(int learnerGroupNo=0; learnerGroupNo < selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++ ) {

				LearnerGroup lgrp = selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo);
				LearnerGroupEnrollmentItem item = new LearnerGroupEnrollmentItem();
				item.setLearnerGroupId(lgrp.getId());
				item.setLearnerGroupName(lgrp.getName());
				item.setSelected(false);
				learnerGroupList.add(item);
			}
			addTrainingPlanForm.setLearnerGroupTrainingItems(learnerGroupList);
			
						
			

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> referenceData(HttpServletRequest request,
			Object command,	Errors errors, int page) throws Exception {

		Map<String, Object> model = new HashMap<String, Object>();
		AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		switch(page) {

		case 0:
			List<String> trPlanNames = new ArrayList<String>();
			List<TrainingPlan> trPlans = trainingPlanService.findTrainingPlanByName("", customer);
			
			for( TrainingPlan tp : trPlans ) {
				trPlanNames.add(tp.getName());
			}	
						
			model.put("trainingPlanNames", trPlans);
			return model;
			
		case 1:
			Map <Object, Object>trainingPlanMethodMap = new LinkedHashMap <Object, Object>();
			trainingPlanMethodMap.put(ENROLLMENT_METHOD_LEARNERGROUP, ENROLLMENT_METHOD_LEARNERGROUP);
			trainingPlanMethodMap.put(ENROLLMENT_METHOD_LEARNER, ENROLLMENT_METHOD_LEARNER);
			trainingPlanMethodMap.put(ENROLLMENT_METHOD_ORGGROUP, ENROLLMENT_METHOD_ORGGROUP);
			model.put("trainingPlanMethods", trainingPlanMethodMap);
			return model;
		case 2:
			break;
		case 3:
			break;
		case 4:
			OrganizationalGroup rootOrgGroup = null;
			if( vu360UserService.hasAdministratorRole(loggedInUser) ) {
				rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
			} else {
				rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(loggedInUser.getLearner().getCustomer().getId());
			}
			String[] orgGroupList = addTrainingPlanForm.getOrgGroups();
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
			return model;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

		AddTrainingPlanForm form = (AddTrainingPlanForm)command;
		HttpSession session = request.getSession();

		if ( currentPage == 1 ) {

			String trainingPlanMethod = form.getTrainingPlanMethod();
			if( form.getToFirst().equalsIgnoreCase("true") ) {
				return 0;
			} else {
				if (trainingPlanMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP)) {
					return 2;
				} else if (trainingPlanMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER)) {
					return 3;
				} else if (trainingPlanMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP)) {
					return 4;
				}
			}
		} else if ( currentPage == 3 ) { 

			if(request.getParameter("action")!=null
					&& request.getParameter("action").equals("search")) {
				return 3;
			}else if(request.getParameter("action")!=null
					&& request.getParameter("action").equals("allSearch")) {
				return 3;
			}else{
				request.setAttribute("newPage","true");
			}
			
			session.setAttribute("pageIndex",3);

		} else if ( currentPage == 5 ) {

			String enrollmentMethod = form.getTrainingPlanMethod();

			if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP)) {
				session.setAttribute("pageIndex",4);
			} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER)) {
				session.setAttribute("pageIndex",3);
			} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP)) {
				session.setAttribute("pageIndex",2);
			}

		} else if ( currentPage == 2 ) {

			session.setAttribute("pageIndex",2);

		} else if ( currentPage == 3 ) {

			session.setAttribute("pageIndex",3);

		} else if ( currentPage == 4 ) {

			session.setAttribute("pageIndex",4);

		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

		AddTrainingPlanForm form = (AddTrainingPlanForm)command;
		AssignTrainingPlanValidator validator = (AssignTrainingPlanValidator)this.getValidator();
		
		if(page==0 && this.getTargetPage(request, command, errors, page)==1){
			List<EnrollmentCourseView>courseViewList=new ArrayList<EnrollmentCourseView>();
			List<EnrollmentCourseView>tempCourseViewList=new ArrayList<EnrollmentCourseView>();
			EnrollmentCourseView courseView=null;
			
			if(!StringUtils.isBlank(form.getSelectedTrainingPlanId()) ){
				TrainingPlan tp=trainingPlanService.getTrainingPlanByID(Long.parseLong(form.getSelectedTrainingPlanId()));
				
				Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();				
			
				List<CustomerEntitlement> ceList=entitlementService.getActiveCustomerEntitlementsForCustomer(customer); 
				if (ceList != null && ceList.size() > 0) {
					tempCourseViewList=entitlementService.getCoursesForTrainingPlanByCustomer(customer, "", "", "", null, 500);
					if(tp!=null){
						for(TrainingPlanCourse tpCourse : tp.getActiveCourses()) { // [1/27/2011] LMS-7183 :: Retired Course Functionality II
							for(EnrollmentCourseView view : tempCourseViewList){
								if(view.getCourseId().longValue() == tpCourse.getCourse().getId().longValue()){
									courseView=view;
									courseView.setSelected(true);
									if(courseView.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || courseView.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE))
										courseView.setSyncClasses(synchronousClassService.getSynchronousClassByCourseId(tpCourse.getCourse().getId()));
									courseViewList.add(courseView);
									break;
								}
							}
						}
					}
				}					
			}
			
			if (!courseViewList.isEmpty()) {
				form.setEnrollmentCourseViewList(courseViewList);
			}
			
		}
		
		
		if ((page == 3 
				&& !StringUtils.isBlank(form.getAction()))
				||(this.getTargetPage(request, command, errors, 3)==5)
				||(this.getTargetPage(request, command, errors, 3)==1)
		){
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
						
			// [09/17/2010] LMS-6859 :: Validations on Synchronous Courses enrollment
 			form.setSyncCourseSelected(false);
 			form.setNonSyncCourseSelected(false);
 			for(EnrollmentCourseView enrollment : form.getEnrollmentCourseViewList()) {
				if(enrollment.getSelected()){
					if(enrollment.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || enrollment.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)){
						form.setSyncCourseSelected(true); 								
						List<SynchronousClass> syncClasses=synchronousClassService.getSynchronousClassesForEnrollment(enrollment.getCourseId(), this.getLearnersTobeEnrolled(form).size());
						enrollment.setSyncClasses(syncClasses);
						if ( syncClasses != null ) {
							boolean isClassSelected = false;							
							for( SynchronousClass syncClass : syncClasses ) {
								if (syncClass.getSelected()) {
									isClassSelected = true;
								}							
								syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));															
							}		
							
							if(!isClassSelected) {
								syncClasses.get(0).setSelected(true);									
							}
						}	
					}
					else
					{
						form.setNonSyncCourseSelected(true);
					}
				}
			}
		

		}

		if( page == 0 ){
			validator.validateFirstPage(form, errors);
		}
		if( page == 1 ) {
			validator.validateSecondPage(form, errors);
		}
		if( page == 2 ) {
			if(!form.getAction().equalsIgnoreCase("search"))
				validator.validateThirdPage(form, errors);
		}
		if( page == 3 ) {
			if(!form.getAction().equalsIgnoreCase("search"))
				validator.validateFourthPage(form, errors);
		}
		if( page == 4 ) {
			validator.validateFifthPage(form, errors);
		}
		if( page == 5 ) {
			validator.validateSixthPage(form, errors);
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	@SuppressWarnings("unchecked")
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		
			AddTrainingPlanForm form = (AddTrainingPlanForm)command;
			HttpSession session = request.getSession();
			
		if( page == 2 ) {
			
			List<LearnerGroupEnrollmentItem> lGroups = form.getLearnerGroupTrainingItems();
			
			String sortDirection = request.getParameter("sortDirection");
			if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = "0";
			
			Map<String, Object> PagerAttributeMap = new HashMap <String, Object>();
			PagerAttributeMap.put("pageIndex",pageIndex);
			session.setAttribute("showAll", showAll);
			
			/** Added by Dyutiman...
			 *  manual sorting of learner groups
			 */
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			
			if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
				// sorting against course name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < lGroups.size() ; i ++ ) {
							for( int j = 0 ; j < lGroups.size() ; j ++ ) {
								if( i != j ) {
									LearnerGroupEnrollmentItem c1 = (LearnerGroupEnrollmentItem) lGroups.get(i);
									LearnerGroupEnrollmentItem c2 = (LearnerGroupEnrollmentItem) lGroups.get(j);
									if( c1.getLearnerGroupName().toUpperCase().compareTo
											(c2.getLearnerGroupName().toUpperCase()) > 0 ) {
										LearnerGroupEnrollmentItem tempLG = lGroups.get(i);
										lGroups.set(i, lGroups.get(j));
										lGroups.set(j, tempLG);
									}
								}
							}
						}
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						for( int i = 0 ; i < lGroups.size() ; i ++ ) {
							for( int j = 0 ; j < lGroups.size() ; j ++ ) {
								if( i != j ) {
									LearnerGroupEnrollmentItem c1 = (LearnerGroupEnrollmentItem) lGroups.get(i);
									LearnerGroupEnrollmentItem c2 = (LearnerGroupEnrollmentItem) lGroups.get(j);
									if( c1.getLearnerGroupName().toUpperCase().compareTo
											(c2.getLearnerGroupName().toUpperCase()) < 0 ) {
										LearnerGroupEnrollmentItem tempLG = lGroups.get(i);
										lGroups.set(i, lGroups.get(j));
										lGroups.set(j, tempLG);
									}
								}
							}
						}
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
				}
			}
			form.setLearnerGroupTrainingItems(lGroups);
		}
		if ((page == 3 
				&& ((!StringUtils.isBlank(form.getAction()))//search or paging or sort done by this action
						||(this.getTargetPage(request, command, errors, 3)==5) && errors.hasErrors()))//trying to move to next page with errors
						||((page == 5 && this.getTargetPage(request, command, errors, 3)==3) 
								&& !form.getAction().equalsIgnoreCase("courseSearch")) //back from next page
								||(page == 1 && this.getTargetPage(request, command, errors, 3)==3)//back from previous page
		){

			if( !StringUtils.isBlank(form.getSearchType()) ){
				Map<Object,Object> results = new HashMap<Object,Object>();
				VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
				int pageNo = form.getPageIndex()<0 ? 0 : form.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
				int sortColumnIndex = form.getSortColumnIndex();
				String sortBy = (sortColumnIndex>=0 && sortColumnIndex<SORTABLE_COLUMNS.length) ? SORTABLE_COLUMNS[sortColumnIndex]: MANAGE_USER_SORT_FIRSTNAME ;
				int sortDirection = form.getSortDirection()<0 ? 0:(form.getSortDirection()>1?1:form.getSortDirection());
				List<VU360User> userList=null;
				if(form.getSearchType().equalsIgnoreCase("simplesearch")) {
					Integer totalResults = 0;
					if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
							results=learnerService.findLearner1(form.getSearchKey(), 
									vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}

					}else {
						results=learnerService.findLearner1(form.getSearchKey(), 
								vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
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
					Integer totalResults = 0;
					if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
							
							/**
							 * code modified by muhammad akif
							 * 10-07-2014
							 *  (LMS-16175) System is allowing Disabled , Locked and expired users enrollment into courses through "Enroll Users by Training Plan"
							 */
							
							//results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), loggedInUser, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);

						    results = learnerService
								    .findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
								    		vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
											loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
											loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
											ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);

						    
							totalResults = (Integer)results.get("recordSize");
							userList = (List<VU360User>)results.get("list");
						}

					}else {
						
					    results = learnerService
							    .findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress(), 
							    		vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
										loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
										loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
										ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);

						//results = learnerService.findLearner1(form.getSearchFirstName(),form.getSearchLastName(), form.getSearchEmailAddress(), loggedInUser, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
						totalResults = (Integer)results.get("recordSize");
						userList = (List<VU360User>)results.get("list");
					}
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
				else if(form.getSearchType().equalsIgnoreCase("allsearch")) {
					
					if( !vu360UserService.hasAdministratorRole(loggedInUser) &&  !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0 ) {
							results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
									vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									sortBy, sortDirection);
							userList = (List<VU360User>)results.get("list");
						}
					} else {
						results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName().trim(),form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
								vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								sortBy, sortDirection);
						userList = (List<VU360User>)results.get("list");
					}

				}
				if( userList != null && userList.size() > 0 ) {
					List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
					for(VU360User auser:userList){
						LearnerItemForm learnerItem = new LearnerItemForm();
						learnerItem.setSelected(false);
						learnerItem.setUser(auser);
						// Modified By : Marium Saud (LMS-21383) Handling done to set user object in case VU360User= null when selection of learners for Enrollment done by using showAll option 
						if(auser.getLearner().getVu360User()==null){
							auser.getLearner().setVu360User(auser);
						}
						learnerList.add(learnerItem);
					}
					for(LearnerItemForm learnerItem: learnerList){
						for(LearnerItemForm preSelectedItem : form.getSelectedLearners()){
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
			}
		}
 		if ( page == 6 ){
			if(form.getPrevToDate().equalsIgnoreCase("false")){
				String url = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+ "/login.do";
				//Added by Faisal A. Siddiqui July 21 09 for new branding
				Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				doEnrollment(form, url,brander, request);
			}
		}
 		
 		super.postProcessPage(request, command, errors, page);
	}

	@SuppressWarnings("unchecked")
	private void doEnrollment(AddTrainingPlanForm addTrainingPlanForm, String url,Brander brander, HttpServletRequest request) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		List<LearnerEnrollment> learnerEnrollments = new ArrayList<LearnerEnrollment>();

		List<Learner> selectedLearner = this.getLearnersTobeEnrolled(addTrainingPlanForm);
		String planId = addTrainingPlanForm.getSelectedTrainingPlanId();
		TrainingPlan selectedTrainingPlan = trainingPlanService.getTrainingPlanByID(Long.valueOf(planId));
		List<Learner> learnersFailedToEnroll = new ArrayList<Learner>();

		List<Integer> learnersEnrolledSucessfully = new ArrayList <Integer>();
		int enrollmentsCreated = 0;
		int enrollmentsUpdated = 0;

		List<Learner> learnersToEnrolled = new ArrayList<Learner>();
		CustomerEntitlement courseEntitled = null;
		int remainingLearnerToBeEntolledNumber = selectedLearner.size();
 
			learnersToEnrolled = selectedLearner;
					//courseEntitled = ent;
					 this.saveEnrollment(addTrainingPlanForm,selectedTrainingPlan,
							learnersToEnrolled,null,courseEntitled, brander, request);

			learnersFailedToEnroll.addAll(selectedLearner.subList(selectedLearner.size()-remainingLearnerToBeEntolledNumber,selectedLearner.size()));

			log.debug("learnersToEnrolled size -- "+learnersToEnrolled.size());
			//for( TrainingPlanCourse tpCourse : selectedTrainingPlan.getCourses()){
			//	}
		log.debug("enrollments -- "+learnerEnrollments.size());

		// assign one training plan assignment with a list of learner enrollments

		addTrainingPlanForm.setAttemptedToEnroll(selectedLearner.size());
		if(learnersEnrolledSucessfully!=null && learnersEnrolledSucessfully.size()>0)
			addTrainingPlanForm.setEnrolledSuccessfully((Integer)Collections.max(learnersEnrolledSucessfully));
		else
			addTrainingPlanForm.setEnrolledSuccessfully(0);
		addTrainingPlanForm.setEnrollmentsCreated(enrollmentsCreated);
		context.put("enrollmentsUpdated", enrollmentsUpdated);
		addTrainingPlanForm.setNumerOfLearnersFailed(learnersFailedToEnroll.size());
		addTrainingPlanForm.setLearnersFailedToEnroll(learnersFailedToEnroll);
		addTrainingPlanForm.setCoursesAssigned(selectedTrainingPlan.getActiveCourses().size()); // [1/27/2011] LMS-7183 :: Retired Course Functionality II

	}


	private void saveEnrollment(AddTrainingPlanForm addTrainingPlanForm, 
			TrainingPlan selectedTrainingPlan, List<Learner> learners, Course course, 
			CustomerEntitlement entitlement, Brander brander, HttpServletRequest request){
		
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
		
		List<Learner> learnersToBeEnrolled = getLearnersTobeEnrolled(addTrainingPlanForm);
		
		/**
		 * code modified by muhammad akif
		 * 10-07-2014
		 *  (LMS-16175) System is allowing Disabled , Locked and expired users enrollment into courses through "Enroll Users by Training Plan"
		 */
		learnersToBeEnrolled= getActiveLearners(learnersToBeEnrolled);
		
        SaveEnrollmentParam saveEnrollmentParam = new SaveEnrollmentParam(addTrainingPlanForm.getEnrollmentCourseViewList(),
                learnersToBeEnrolled, true, addTrainingPlanForm.getAllCourseStartDate(),
                addTrainingPlanForm.getAllCourseEndDate(), false);
         
        saveEnrollmentParam.setBrander(brander);
        saveEnrollmentParam.setUser(user);
        saveEnrollmentParam.setNotifyLearner(addTrainingPlanForm.isEnrollConfirmation());
        saveEnrollmentParam.setNotifyManagerOnConfirmation(addTrainingPlanForm.isOnConfirmation());
        saveEnrollmentParam.setEnableEnrollmentEmailsForNewCustomers(customer.getDistributor().getDistributorPreferences().isEnableRegistrationEmailsForNewCustomers()?customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers()?true:false:false);
        saveEnrollmentParam.setVelocityEngine(velocityEngine);
        saveEnrollmentParam.setEmailService(learnersToBeMailedService);
        saveEnrollmentParam.setTrainingPlan(selectedTrainingPlan);
		
        EnrollLearnerAsyncTask task = new EnrollLearnerAsyncTask();
        task.setSaveEnrollmentParam(saveEnrollmentParam);
        task.setEnrollmentService(enrollmentService);
        task.setTrainingPlanService(trainingPlanService);
        asyncTaskExecutorWrapper.execute(task);
		
	}
	
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
	}

	private List<Learner> getLearnersTobeEnrolled(AddTrainingPlanForm form ){

		String enrollmentMethod = form.getTrainingPlanMethod();
		List<Learner> learnersToBeEnrolled = new ArrayList<Learner>();

		if( enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER) ) {

			List<LearnerItemForm> learner = form.getSelectedLearners();
			if ( learner!=null && learner.size()>0 ){
				List<Learner> learners = new ArrayList <Learner>();

				for(int i=0;i<learner.size();i++){

					if (learner.get(i).isSelected()==true){
						learners.add(learner.get(i).getUser().getLearner());
					}
				}
				learnersToBeEnrolled = learners;
			}
		} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP) ) {
			
			String[] orgGroupIds = form.getOrgGroups();
			Long[] orgGroupIdArray = new Long[orgGroupIds.length];
			
			for (int i = 0; i < orgGroupIds.length; i++) {
				orgGroupIdArray[i] = Long.valueOf( orgGroupIds[i] );
			}			
			
			learnersToBeEnrolled = this.orgGroupLearnerGroupService.getLearnersByOrgGroupIds(orgGroupIdArray);

		} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP) ) {

			List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = form.getLearnerGroupTrainingItems();

			if(learnerGroupEnrollmentItems!=null && learnerGroupEnrollmentItems.size()>0){
				List<Long> learnerGroupIdList = new ArrayList<Long>();
				for ( LearnerGroupEnrollmentItem item : learnerGroupEnrollmentItems ) {
					if ( item.isSelected() ) {
						learnerGroupIdList.add( item.getLearnerGroupId() );
					}
				}
				Long learnerGroupIdArray[] = new Long[ learnerGroupIdList.size() ];				
				learnerGroupIdArray = learnerGroupIdList.toArray( learnerGroupIdArray );
				
				learnersToBeEnrolled = this.orgGroupLearnerGroupService.getLearnersByLearnerGroupIds(learnerGroupIdArray);
			}
		}
		return learnersToBeEnrolled;
	}

	
	/**
	 * code modified by muhammad akif
	 * 10-07-2014
	 *  (LMS-16175) System is allowing Disabled , Locked and expired users enrollment into courses through "Enroll Users by Training Plan"
	 */

    private List<Learner> getActiveLearners(List<Learner> learnersToBeEnrolled) {
	    List<Learner> tempLearnersToBeEnrolled = new ArrayList<Learner>();
	    tempLearnersToBeEnrolled = learnersToBeEnrolled.stream().filter(p -> p.getVu360User().getAccountNonExpired().equals(Boolean.TRUE)).
	    	    filter(p -> p.getVu360User().getAccountNonLocked().equals(Boolean.TRUE)).
	    	    filter(p -> p.getVu360User().getEnabled().equals(Boolean.TRUE)).collect(Collectors.toList());
	    	    /**
	    	     * Modified By Marium Saud : Stream has been used to increase performance.
	    	     * 	
	    	     * for(Learner l:learnersToBeEnrolled){
	    	    	if(l.getVu360User().getAccountNonExpired() &&
	    	    			l.getVu360User().getAccountNonLocked() &&
	    	    				l.getVu360User().getEnabled()
	    	    			){
	    	    		tempLearnersToBeEnrolled.add(l);
	    	    	}
	    	    }
	    	     */
	    
	    return tempLearnersToBeEnrolled;
    }

	
	@SuppressWarnings("unused")
	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, 
			List<Long> selectedOrgGroups ) {
		if( orgGroup != null ) {
			TreeNode node = new TreeNode(orgGroup);
			for( Long selectedId : selectedOrgGroups ) {
				if( orgGroup.getId().longValue() == selectedId.longValue() ){
					node.setSelected(true);
					break;
				}
			}
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i),selectedOrgGroups);
			}
			node.setEnabled(true);

			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public CourseAndCourseGroupService getCourseCourseGrpService() {
		return courseCourseGrpService;
	}

	public void setCourseCourseGrpService(
			CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public String getSummaryTemplate() {
		return summaryTemplate;
	}

	public void setSummaryTemplate(String summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @return the trainingPlanService
	 */
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	/**
	 * @param trainingPlanService the trainingPlanService to set
	 */
	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
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

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
}