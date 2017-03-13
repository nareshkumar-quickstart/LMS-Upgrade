package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SaveEnrollmentParam;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.CourseEntitlementItem;
import com.softech.vu360.lms.web.controller.model.EnrollmentCourse;
import com.softech.vu360.lms.web.controller.model.EnrollmentDetailsForm;
import com.softech.vu360.lms.web.controller.model.LearnerCourse;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.model.SurveySuggestedCourse;
import com.softech.vu360.lms.web.controller.validator.AddEnrollmentValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.ArrangeOrgGroupTree;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.EnrollLearnerAsyncTask;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;

public class ManageEnrollmentController extends AbstractWizardFormController {

    private static final Logger log = Logger.getLogger(ManageEnrollmentController.class.getName());

    private VU360UserService vu360UserService;
    private LearnerService learnerService;
    private EntitlementService entitlementService;
    private SurveyService surveyService;
    private EnrollmentService enrollmentService;
    private SynchronousClassService synchronousClassService = null;
    private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
    private CourseAndCourseGroupService courseCourseGrpService;
    private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
    private VelocityEngine velocityEngine;
    private LearnersToBeMailedService learnersToBeMailedService;
    private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
    private static final String MANAGE_USER_SORT_LASTNAME = "lastName";
    private static final String MANAGE_USER_SORT_USERNAME = "emailAddress";
    private static final String MANAGE_USER_SORT_ACCOUNTLOCKED = "accountNonLocked";
    private static final String[] SORTABLE_COLUMNS = { MANAGE_USER_SORT_FIRSTNAME, MANAGE_USER_SORT_LASTNAME, MANAGE_USER_SORT_USERNAME,
	    MANAGE_USER_SORT_ACCOUNTLOCKED };
    private static final String ENROLLMENT_METHOD_LEARNER = "Learner";
    private static final String ENROLLMENT_METHOD_ORGGROUP = "OrgGroup";
    private static final String ENROLLMENT_METHOD_LEARNERGROUP = "LearnerGroup";
    private static final String ENROLLMENT_METHOD_SURVEYRESPONSE = "SurveyResponse";
    private static final int SEARCH_COURSE_PAGE_SIZE = 10;
    // Enrollment Summary
    private static final String ENROLLMENT_DUPLICATE_IGNORE = "Ignore";
    private static final String ENROLLMENT_DUPLICATE_UPDATE = "Update";
    private static final String ENROLLMENT_ONCONFIRMATION_YES = "Yes";
    private static final String ENROLLMENT_ONCONFIRMATION_NO = "No";
    private static final String ENROLLMENT_ENROLLMENTCONFIRMATION_YES = "Yes";
    private static final String ENROLLMENT_ENROLLMENTCONFIRMATION_NO = "No";
    // added by muhammad akif : 19 June 2014
    private static boolean ACCOUNT_NON_EXPIRED = true;
    private static boolean ACCOUNT_NON_LOCKED = true;
    private static boolean ENABLED = true;

    private String closeTemplate = null;

    public ManageEnrollmentController() {
	super();
	setCommandName("enrollmentForm");
	setCommandClass(EnrollmentDetailsForm.class);
	setSessionForm(true);
	this.setBindOnNewForm(true);
	setPages(new String[] { "manager/enrollments/assignEnrollments", "manager/enrollments/enrollSelectLearners",
		"manager/enrollments/assignEnrollmentsOrgGrp", "manager/enrollments/assignEnrollmentsLearnerGrp",
		"manager/enrollments/enrollSelectCourses", "manager/enrollments/enrollSelectDates", "manager/enrollments/enrollSelectSchedule",
		"manager/enrollments/enrollConfirmation", "manager/enrollments/assignEnrollmentsByLearner",
		"manager/enrollments/assignEnrollmentsBySurveyResponse", "manager/enrollments/selectTrainingPlans" });
    }

    /**
     * Step 1. We do not need to override this method now. This method basically
     * lets us hook in to the point before the request data is bound into the
     * form/command object This is called first when a new request is made and
     * then on every subsequent request. However in our case, since the
     * bindOnNewForm is true this will NOT be called on subsequent requests...
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		try {
		    EnrollmentDetailsForm enrollmentDetails = (EnrollmentDetailsForm) command;
		    enrollmentDetails.setCourseSearchResultsPageSize(SEARCH_COURSE_PAGE_SIZE);
		    VU360User logInUser = VU360UserAuthenticationDetails.getCurrentUser();
		    Long customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
			    .getCurrentCustomerId();
	
		    List<LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList<LearnerGroup>();
		    selectedLearnerGroupsAssociatedWithOrgGroup.addAll(orgGroupLearnerGroupService.getAllLearnerGroups(customerId, logInUser));
		    List<LearnerGroupEnrollmentItem> learnerGroupList = new ArrayList<LearnerGroupEnrollmentItem>();
	
		    for (int learnerGroupNo = 0; learnerGroupNo < selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++) {
				LearnerGroup lgrp = selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo);
				LearnerGroupEnrollmentItem item = new LearnerGroupEnrollmentItem();
				item.setLearnerGroupId(lgrp.getId());
				item.setLearnerGroupName(lgrp.getName());
				item.setSelected(false);
				learnerGroupList.add(item);
		    }
		    enrollmentDetails.setLearnerGroupEnrollmentItems(learnerGroupList);
		    List<CourseEntitlementItem> courseEntitlements = new ArrayList<CourseEntitlementItem>();
		    enrollmentDetails.setCourseEntitlementItems(courseEntitlements);
		} catch (Exception e) {
		    log.debug("exception", e);
		}
		return command;
    }

    /**
     * Step 2. We do not need to override this method now. This method lets us
     * hook in to the point before the request data is bound into the
     * form/command object and just before the binder is initialized... We can
     * have customized binders used here to interpret the request fields
     * according to our requirements. It allows us to register custom editors
     * for certain fields. Called on the first request to this form wizard.
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
	log.debug("IN initBinder");
	super.initBinder(request, binder);
    }

    /**
     * Step 3. We do not need to override this method now. Since we have
     * bindOnNewForm property set to true in the constructor this method will be
     * called when the first request is processed. We can add custom
     * implentation here later to populate the command object accordingly.
     * Called on the first request to this form wizard.
     */
    protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
	log.debug("IN onBindOnNewForm");
	super.onBindOnNewForm(request, command, binder);
    }

    /**
     * Step 4. Shows the first form view. Called on the first request to this
     * form wizard.
     */
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
	log.debug("IN showForm");
	ModelAndView modelNView = super.showForm(request, response, binder);
	String view = modelNView.getViewName();
	log.debug("OUT showOForm for view = " + view);
	return modelNView;
    }

    /**
     * Called by showForm and showPage ... get some standard data for this page
     */
    protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		log.debug("IN referenceData");
		Map<Object, Object> model = new HashMap<Object, Object>();
		EnrollmentDetailsForm enrollmentDetail = (EnrollmentDetailsForm) command;
	
		switch (page) {
	
		case 0:
		    Map<Object, Object> enrollmentMethodMap = new LinkedHashMap<Object, Object>();
		    enrollmentMethodMap.put(ENROLLMENT_METHOD_LEARNER, ENROLLMENT_METHOD_LEARNER);
		    enrollmentMethodMap.put(ENROLLMENT_METHOD_ORGGROUP, ENROLLMENT_METHOD_ORGGROUP);
		    enrollmentMethodMap.put(ENROLLMENT_METHOD_LEARNERGROUP, ENROLLMENT_METHOD_LEARNERGROUP);
		    enrollmentMethodMap.put(ENROLLMENT_METHOD_SURVEYRESPONSE, ENROLLMENT_METHOD_SURVEYRESPONSE);
		    model.put("enrollmentMethods", enrollmentMethodMap);
		    return model;
	
		case 1:
		    break;
	
		case 2:
		    OrganizationalGroup rootOrgGroup = null;
		    VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		    Long customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
			    .getCurrentCustomerId();
		    rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		    String[] orgGroupList = enrollmentDetail.getGroups();
		    List<Long> orgGroupIdList = new ArrayList<Long>();
		    if (orgGroupList != null && orgGroupList.length > 0) {
				for (String orgGroup : orgGroupList) {
				    Long orgGroupId = Long.parseLong(orgGroup);
				    orgGroupIdList.add(orgGroupId);
				}
		    }
		    TreeNode orgGroupRoot = ArrangeOrgGroupTree.getOrgGroupTree(null, rootOrgGroup, orgGroupIdList, loggedInUser);
		    List<TreeNode> treeAsList = orgGroupRoot.bfs();
		    model.put("orgGroupTreeAsList", treeAsList);
		    return model;
	
		case 3:
		    break;
	
		case 4:
		    break;
	
		case 5:
		    break;
		    
		case 6:
		    break;
	
		case 7:
		    Map<Object, Object> duplicateTypedMap = new LinkedHashMap<Object, Object>();
		    duplicateTypedMap.put(ENROLLMENT_DUPLICATE_IGNORE, ENROLLMENT_DUPLICATE_IGNORE);
		    duplicateTypedMap.put(ENROLLMENT_DUPLICATE_UPDATE, ENROLLMENT_DUPLICATE_UPDATE);
		    model.put("duplicateTypes", duplicateTypedMap);
	
		    Map<Object, Object> confirmationTypedMap = new LinkedHashMap<Object, Object>();
		    confirmationTypedMap.put(ENROLLMENT_ONCONFIRMATION_YES, ENROLLMENT_ONCONFIRMATION_YES);
		    confirmationTypedMap.put(ENROLLMENT_ONCONFIRMATION_NO, ENROLLMENT_ONCONFIRMATION_NO);
		    model.put("confirmationTypes", confirmationTypedMap);
	
		    Map<Object, Object> enrollmentConfirmationTypedMap = new LinkedHashMap<Object, Object>();
		    enrollmentConfirmationTypedMap.put(ENROLLMENT_ENROLLMENTCONFIRMATION_YES, ENROLLMENT_ENROLLMENTCONFIRMATION_YES);
		    enrollmentConfirmationTypedMap.put(ENROLLMENT_ENROLLMENTCONFIRMATION_NO, ENROLLMENT_ENROLLMENTCONFIRMATION_NO);
		    model.put("enrollmentConfirmationTypes", enrollmentConfirmationTypedMap);
	
		    return model;
	
		case 8:
		    model.put("learnersAttemptedToEnroll", enrollmentDetail.getAttemptedToEnroll());
		    model.put("learnersEnrolledSucssessfully", enrollmentDetail.getLearnersEnrolledSuccessfully());
		    model.put("coursesAssigned", enrollmentDetail.getCoursesAssigned());
		    model.put("enrollmentsCreated", enrollmentDetail.getEnrollmentsCreated());
		    model.put("learnersNotEnrolled", enrollmentDetail.getLearnersNotEnrolled());
		    model.put("enrollmentsUpdated", enrollmentDetail.getEnrollmentsUpdated());
		    return model;
	
		case 13:
		    model.put("learnersAttemptedToEnroll", enrollmentDetail.getAttemptedToEnroll());
		    model.put("learnersEnrolledSucssessfully", enrollmentDetail.getLearnersEnrolledSuccessfully());
		    model.put("coursesAssigned", enrollmentDetail.getCoursesAssigned());
		    model.put("enrollmentsCreated", enrollmentDetail.getEnrollmentsCreated());
		    model.put("learnersNotEnrolled", enrollmentDetail.getLearnersNotEnrolled());
		    model.put("enrollmentsUpdated", enrollmentDetail.getEnrollmentsUpdated());
		    return model;
		    
		default:
		    break;
		}
	
		return super.referenceData(request, page);
    }

    /**
     * we can do custom processing after binding and validation
     */
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
	EnrollmentDetailsForm form = (EnrollmentDetailsForm) command;
	// sync the learner list with the selected learner list
	if ((page == 1 && !StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))
		|| (this.getTargetPage(request, command, errors, 1) == 4) || (this.getTargetPage(request, command, errors, 1) == 0)) {

	    List<LearnerItemForm> learnerList = form.getLearners();
	    Collections.sort(learnerList);

	    List<LearnerItemForm> selectedLearnerList = form.getSelectedLearners();
	    Collections.sort(selectedLearnerList);

	    List<LearnerItemForm> mergedLearnerList = new ArrayList<LearnerItemForm>();
	    int i = 0, j = 0;
	    LearnerItemForm item, selecteditem;
	    for (; i < learnerList.size() && j < selectedLearnerList.size();) {
		item = learnerList.get(i);
		selecteditem = selectedLearnerList.get(j);
		if (item.compareTo(selecteditem) < 0) {
		    i++;
		    if (item.isSelected())
			mergedLearnerList.add(item);
		} else if (item.compareTo(selecteditem) > 0) {
		    j++;
		    mergedLearnerList.add(selecteditem);
		} else {
		    if (item.isSelected())
			mergedLearnerList.add(item);
		    i++;
		    j++;
		}
	    }
	    for (; i < learnerList.size(); i++) {
		item = learnerList.get(i);
		if (item.isSelected())
		    mergedLearnerList.add(item);
	    }
	    for (; j < selectedLearnerList.size(); j++) {
		selecteditem = selectedLearnerList.get(j);
		mergedLearnerList.add(selecteditem);
	    }
	    form.setSelectedLearners(mergedLearnerList);
	}
	if (page == 4) {
	    /*
	     * [8/25/2010] LMS-6891, LMS-6889, LMS-6857 :: Enroll Learner Class
	     * size Issue , Validations and Back Traversing on different type of
	     * courses
	     */
	    if (form.getCourseSearchStay() != null) {
			if (form.getCourseSearchStay().equalsIgnoreCase("stay")) {
			    form.setSelectedSyncCourse(false);
			    form.setSelectedNonSyncCourse(false);
			}
	    }
	    int targetPage = this.getTargetPage(request, command, errors, page);

	    if (targetPage == 4) {
			Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
				.getCurrentCustomer();
	
			coursesSearch(form, customer, request);
			coursesSearchMove(form);
			coursesSearchShowAll(form); // must be executed last
	    } else {
		// Take decision based on the target page, IF Sync. Course is
		// selected.
		form.setSelectedSyncCourse(false);
		form.setSelectedNonSyncCourse(false);
		for (EnrollmentCourseView item : form.getEnrollmentCourseViewList()) {

		    if (item.getSelected()) {
			if (item.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || item.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
				List<SynchronousClass> syncClasses=synchronousClassService.getSynchronousClassesForEnrollment(item.getCourseId(), this.getLearnersTobeEnrolled(form).size());
				item.setSyncClasses(syncClasses);
				if ( syncClasses != null ) {
					boolean isClassSelected = false;							
					for( SynchronousClass syncClass : syncClasses ) {
						if (syncClass.getSelected()) {
							isClassSelected = true;
						}							
						syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));															
					}		
					
					if(!isClassSelected) {
						item.getSyncClasses().get(0).setSelected(true);									
					}
				}	

				form.setSelectedSyncCourse(true);
			    form.setLearnersToBeEnrolled(getLearnersTobeEnrolled(form).size());
			} else if (item.getCourseType().equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
				Course course = synchronousClassService.getInstructorCourse(item.getCourseId());
				if((course instanceof InstructorConnectCourse))
				{
			      if(!((InstructorConnectCourse) course).getInstructorType().equals("Email Online"))
				   {
					List<SynchronousClass> syncClasses=synchronousClassService.getSynchronousClassesForEnrollment(item.getCourseId(), this.getLearnersTobeEnrolled(form).size());
					item.setSyncClasses(syncClasses);
					if ( syncClasses != null ) 
					{
						boolean isClassSelected = false;							
						for( SynchronousClass syncClass : syncClasses ) 
						{
							if (syncClass.getSelected()) 
							{
								isClassSelected = true;
							}							
							syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));															
						}		
						
						if(!isClassSelected) {
							item.getSyncClasses().get(0).setSelected(true);									
						}
					}	
					form.setSelectedSyncCourse(true);
				    form.setLearnersToBeEnrolled(getLearnersTobeEnrolled(form).size());
			  }
			} 
		}		
				else {
			    form.setSelectedNonSyncCourse(true);
			}
		    }
		}
	    }
	}
	super.onBindAndValidate(request, command, errors, page);

    }

    private void SelectTypeOfCourse(HttpServletRequest request, Object command, BindException errors, int page) {
	EnrollmentDetailsForm form = (EnrollmentDetailsForm) command;
	form.setSelectedSyncCourse(false);
	form.setSelectedNonSyncCourse(false);
	for (EnrollmentCourseView item : form.getEnrollmentCourseViewList()) {

	    if (item.getSelected()) {
		if (item.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)||item.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
		    form.setSelectedSyncCourse(true);
		    form.setLearnersToBeEnrolled(getLearnersTobeEnrolled(form).size());

		    List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassesForEnrollment(item.getCourseId(),
			    form.getLearnersToBeEnrolled());
		    item.setSyncClasses(syncClasses);
		    if (syncClasses != null) {

			item.setSyncClasses(syncClasses);
			boolean isClassSelected = false;
			for (SynchronousClass syncClass : syncClasses) {
			    if (syncClass.getSelected()) {
				isClassSelected = true;
			    }
			    syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));
			}

			if (!isClassSelected) {
			    syncClasses.get(0).setSelected(true);
			}
		    }
		} else if (item.getCourseType().equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
		    form.setSelectedSyncCourse(true);
		    form.setLearnersToBeEnrolled(getLearnersTobeEnrolled(form).size());

		    List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassesForEnrollment(item.getCourseId(),
			    form.getLearnersToBeEnrolled());
		    item.setSyncClasses(syncClasses);
		    if (syncClasses != null) {

			item.setSyncClasses(syncClasses);
			boolean isClassSelected = false;
			for (SynchronousClass syncClass : syncClasses) {
			    if (syncClass.getSelected()) {
				isClassSelected = true;
			    }
			    syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));
			}

			if (!isClassSelected) {
			    syncClasses.get(0).setSelected(true);
			}
		    }
		} else {
		    form.setSelectedNonSyncCourse(true);
		}
	    }
	}

    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error)
	    throws Exception {
	log.debug("IN processCancel");
	command = null;
	return new ModelAndView("redirect:mgr_viewPlanAndEnroll.do");
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error)
	    throws Exception {
	log.debug("IN processFinish");
	int Page = getCurrentPage(request);

	if (Page == 7) {
	    log.debug("getting page 7");
	}
	return new ModelAndView(closeTemplate);
    }

    /*
     * [8/25/2010] LMS-6891, LMS-6889, LMS-6857 :: Enroll Learner Class size
     * Issue, Validations and Back Traversing on different type of courses
     */
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

	EnrollmentDetailsForm form = (EnrollmentDetailsForm) command;
	HttpSession session = request.getSession();

	int targetPage = super.getTargetPage(request, command, errors, currentPage);
	try {
	    form.setPrev(request.getParameter("moveTo"));
	} catch (Exception e) {
	    // do nothing
	}
	if (currentPage == 1) {

	    if (request.getParameter("action") != null && request.getParameter("action").equals("simpleSearch")) {
		return 1;
	    }
	    session.setAttribute("pageIndex", 1);

	} else if (currentPage == 0) {

	    String enrollmentMethod = form.getEnrollmentMethod();

	    if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER)) {
		return 1;
	    } else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP)) {
		return 2;
	    } else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP)) {
		return 3;
	    } else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_SURVEYRESPONSE)) {
		return 9;
	    }

	} else if (currentPage == 5) {

	    String enrollmentMethod = form.getEnrollmentMethod();

	    if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER)) {
		session.setAttribute("pageIndex", 1);
	    } else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP)) {
		session.setAttribute("pageIndex", 2);
	    } else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP)) {
		session.setAttribute("pageIndex", 3);
	    } else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_SURVEYRESPONSE)) {
		session.setAttribute("pageIndex", 10);
	    }

	    if (targetPage > currentPage) {
		if (targetPage == 10) {
		    form.setPrev(request.getParameter("moveTo"));
		    return 10;
		} else if (form.isSelectedSyncCourse()) {
		    form.setPrev(request.getParameter("moveTo"));
		    form.setCurrentPage("6");
		    return 6;
		} else {
		    form.setPrev(request.getParameter("moveTo"));

		    return 7;
		}
	    }

	} else if (currentPage == 2) {

	    session.setAttribute("pageIndex", 2);

	} else if (currentPage == 3) {

	    session.setAttribute("pageIndex", 3);

	} else if (currentPage == 10) {
	    if (targetPage != 9) {
		if (form.isSelectedNonSyncCourse()) {
		    return 5;
		} else if (form.isSelectedSyncCourse()) {
		    return 6;
		}
		form.setCurrentPage("10");
	    }
	} else if (currentPage == 4) {
	    if (targetPage > currentPage) {
		if (form.isSelectedNonSyncCourse()) {
		    return 5;
		} else if (form.isSelectedSyncCourse()) {
		    return 6;
		}
	    }
	} else if (currentPage == 6) {
	    if (targetPage < currentPage) {
		if (form.isSelectedNonSyncCourse()) {
		    return 5;
		} else if (form.isSelectedSyncCourse()) {
		    return 4;
		}

	    }
	}

	return targetPage;
    }

    public String getCloseTemplate() {
    	return closeTemplate;
    }

    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

    	EnrollmentDetailsForm form = (EnrollmentDetailsForm) command;

 	Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(
			VU360Branding.BRAND), new Language());
 	
	List<Survey> surveyList = new ArrayList<Survey>();
	List<SuggestedTraining> suggestedTrainings = new ArrayList<SuggestedTraining>();
	List<SurveyResult> surveyResults = new ArrayList<SurveyResult>();
	List<Course> surveyCourses = new ArrayList<Course>();
	if ((page == 1 && ((!StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))
			// search or paging or sort done by this action
			
		|| (this.getTargetPage(request, command, errors, 1) == 4) && errors.hasErrors()))
		// trying to move to next page with errors
		
		|| (page == 4 && this.getTargetPage(request, command, errors, 1) == 1)
		// back from next page
		|| (page == 0 && this.getTargetPage(request, command, errors, 1) == 1)
		// back from previous page
	) {

	    if (!StringUtils.isBlank(form.getSearchType())) {
		Map<Object, Object> results = new HashMap<Object, Object>();
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		int pageNo = form.getPageIndex() < 0 ? 0 : form.getPageIndex() / VelocityPagerTool.DEFAULT_PAGE_SIZE;
		int sortColumnIndex = form.getSortColumnIndex();
		String sortBy = (sortColumnIndex >= 0 && sortColumnIndex < SORTABLE_COLUMNS.length) ? SORTABLE_COLUMNS[sortColumnIndex]
			: MANAGE_USER_SORT_FIRSTNAME;
		int sortDirection = form.getSortDirection() < 0 ? 0 : (form.getSortDirection() > 1 ? 1 : form.getSortDirection());
		List<VU360User> userList = null;
		if (form.getSearchType().equalsIgnoreCase("simplesearch")) {
		    // userList=learnerService.findLearner(form.getSearchKey(),
		    // loggedInUser);
		    Integer totalResults = 0;
		    if (!vu360UserService.hasAdministratorRole(loggedInUser) && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
				if (loggedInUser.getTrainingAdministrator().getManagedGroups().size() > 0) {
				    results = learnerService.findLearner1(form.getSearchKey().trim(), 
				    		vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
				    totalResults = (Integer) results.get("recordSize");
				    userList = (List<VU360User>) results.get("list");
				}
		    } else {
				results = learnerService.findLearner1(form.getSearchKey().trim(), 
						vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE,
					sortBy, sortDirection);
				totalResults = (Integer) results.get("recordSize");
				userList = (List<VU360User>) results.get("list");
		    }

		    // for pagination to work for a paged list
		    Map<String, String> pagerAttributeMap = new HashMap<String, String>();
		    pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
		    pagerAttributeMap.put("totalCount", totalResults.toString());
		    request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		} else if (form.getSearchType().equalsIgnoreCase("advancesearch")) {
		    // userList=learnerService.findLearner(form.getSearchFirstName(),form.getSearchLastName(),
		    // form.getSearchEmailAddress(), loggedInUser);
		    Integer totalResults = 0;
		    if (!vu360UserService.hasAdministratorRole(loggedInUser) && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
		    	TrainingAdministrator tadmin = vu360UserService.findTrainingAdminstratorById(loggedInUser.getTrainingAdministrator().getId());
		    	//if (loggedInUser.getTrainingAdministrator().getManagedGroups().size() > 0) {
			    if (tadmin.getManagedGroups()!=null && tadmin.getManagedGroups().size() > 0) {
				    results = learnerService
						    .findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
						    		vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
				    totalResults = (Integer) results.get("recordSize");
				    userList = (List<VU360User>) results.get("list");
				}
		    } else {
			    results = learnerService
					    .findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
					    		vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
								loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
								loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
								ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
				totalResults = (Integer) results.get("recordSize");
				userList = (List<VU360User>) results.get("list");
		    }
		    // for pagination to work for a paged list
		    Map<String, String> pagerAttributeMap = new HashMap<String, String>();
		    pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
		    pagerAttributeMap.put("totalCount", totalResults.toString());
		    request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}

		else if (form.getSearchType().equalsIgnoreCase("allsearch")) {
		    // userList = learnerService.findLearner("", loggedInUser);
		    if (!vu360UserService.hasAdministratorRole(loggedInUser) && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
				if (loggedInUser.getTrainingAdministrator().getManagedGroups().size() > 0) {
				    results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName().trim(), form.getSearchLastName().trim(),
					    form.getSearchEmailAddress().trim(), 
					    vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						sortBy, sortDirection);
				    userList = (List<VU360User>) results.get("list");
				}
		    } else {
				results = learnerService.findAllLearnersWithCriteria(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form
					.getSearchEmailAddress().trim(), 
					vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
					sortBy, sortDirection);
				userList = (List<VU360User>) results.get("list");
		    }
		}

		if (userList != null && userList.size() > 0) {
		    List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
		    for (VU360User auser : userList) {
				LearnerItemForm learnerItem = new LearnerItemForm();
				learnerItem.setSelected(false);
				learnerItem.setUser(auser);
				learnerList.add(learnerItem);
		    }
		    for (LearnerItemForm learnerItem : learnerList) {
				for (LearnerItemForm preSelectedItem : form.getSelectedLearners()) {
				    if (learnerItem.compareTo(preSelectedItem) == 0) {
						learnerItem.setSelected(true);
						break;
				    }
				}
		    }
		    form.setLearners(learnerList);
		} else {
		    List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
		    form.setLearners(learnerList);
		}
	    } else {
		request.setAttribute("newPage", "true");
	    }
	} else if (((page == 1)) && (!form.getAction().equalsIgnoreCase("search"))) {
	    request.setAttribute("newPage", "true");
	}
	if (((page == 9)) && form.getSearchType().equalsIgnoreCase("surveySearch")) {
	    // userList=learnerService.findLearner(form.getSearchFirstName(),form.getSearchLastName(),
	    // form.getSearchEmailAddress(), loggedInUser);
	    Integer totalResults = 0;
	    String surveyName = request.getParameter("searchSurveyName");
	    String surveyStatus = request.getParameter("status");
	    String isRetire = request.getParameter("retire");
	    String isEditable = request.getParameter("editable");
	    surveyName = (surveyName == null) ? "" : surveyName.trim();
	    surveyStatus = (surveyStatus == null || surveyStatus.equalsIgnoreCase("All")) ? "" : surveyStatus.trim();
	    isRetire = (isRetire == null) ? "false" : isRetire.trim();
	    isEditable = (isEditable == null) ? "false" : isEditable.trim();
	    Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
		    .getCurrentCustomer();
	    Distributor distributor = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
		    .getCurrentDistributor();
	    if (distributor != null
		    && ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentMode()
			    .equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
	    	surveyList = (List<Survey>) surveyService.getSurveysHavingSuggestedTraining(distributor, surveyName, surveyStatus, isRetire,
			isEditable);
	    } else {
	    	surveyList = (List<Survey>) surveyService.getSurveysHavingSuggestedTraining(customer, surveyName, surveyStatus, isRetire, isEditable); // ???
	    }
	    form.setSurveyList(surveyList);

	    // for pagination to work for a paged list
	    Map<String, String> pagerAttributeMap = new HashMap<String, String>();
	    pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
	    pagerAttributeMap.put("totalCount", totalResults.toString());
	    request.setAttribute("PagerAttributeMap", pagerAttributeMap);
	} else if (((page == 9)) && form.getSearchType().equalsIgnoreCase("allSurveySearch")) {
	    Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
		    .getCurrentCustomer();
	    Distributor distributor = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
		    .getCurrentDistributor();
	    if (distributor != null
		    && ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentMode()
			    .equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
	    	surveyList = (List<Survey>) surveyService.getSurveysHavingSuggestedTraining(distributor, "", "", "", "");
	    } else {
	    	surveyList = (List<Survey>) surveyService.getSurveysHavingSuggestedTraining(customer, "", "", "", ""); // ???
	    }
	    form.setSurveyList(surveyList);
	}

	if (page == 6) {
	    if (form.getPrevToDate().equalsIgnoreCase("false")) {
	    	saveEnrollment(form, brander);
	    }
	}
	if (((page == 9)) && this.getTargetPage(request, page) == 10 && form.getSurveys() != null) {
	    List<Survey> surveys = new ArrayList<Survey>();
	    Survey item0;
	    int j = 0, i = 0;
	    int k = 0;
	    String selecteditem0;
	    if (form.getSurveys() != null) {
			// form.setPageIndex(2);
			for (i = 0; i < form.getSurveyList().size();) {
			    item0 = form.getSurveyList().get(i);
			    for (j = 0; j < form.getSurveys().length;) {
			    	selecteditem0 = (form.getSurveys())[j];
					if (item0 != null) {
					    if (item0.getId() == (Long.parseLong(selecteditem0))) {
							// addRecipientForm.getSelectedLearnerGroupList().add(item);
							surveys.add(item0);
							break;
					    }
					}
					j++;
			    }
			    j = 0;
			    i++;
			}
	    }
	    form.setSelectedSurveys(surveys);
	    List<Long> learnerIdList = new ArrayList<Long>();
	    List<LearnerCourse> learnerCourses = new ArrayList<LearnerCourse>();
	    List<SurveySuggestedCourse> surveySuggestedCourses = new ArrayList<SurveySuggestedCourse>();
	    for (k = 0; k < form.getSelectedSurveys().size(); k++) {
		long sid = form.getSelectedSurveys().get(k).getId();

		surveyResults = surveyService.findSurveyResult(sid);
		suggestedTrainings = surveyService.getSuggestedTrainingsBySurveyID(sid);
		// if(surveyResults!=null)
		if (surveyResults != null && suggestedTrainings != null) {
		    for (i = 0; i < surveyResults.size() && i < suggestedTrainings.size(); i++) {
			learnerCourses = new ArrayList<LearnerCourse>();
			SurveySuggestedCourse surveySuggestedCourse = new SurveySuggestedCourse();
			List<SurveyResultAnswer> surveyResultAnswers = (surveyResults.get(i).getAnswers());
			boolean check = matchSurveyAnswer(suggestedTrainings, surveyResultAnswers);
				if (check) {
				    surveyCourses = surveyService.getSuggestedTrainingCoursesBySurveyId(sid);
				    Learner learner = surveyResults.get(i).getSurveyee().getLearner();
				    surveySuggestedCourse.setSurvey(form.getSelectedSurveys().get(k));
				    if (surveyCourses != null && surveyCourses.size() > 0) {
						LearnerCourse learnerCourse = null;
						learnerCourse = new LearnerCourse();
						learnerCourse.setCourses(surveyCourses);
						learnerCourse.setLearnerName((learner.getVu360User().getFirstName() + " " + learner.getVu360User().getLastName()));
						learnerCourse.setId(learner.getVu360User().getId());
						learnerIdList.add(learner.getVu360User().getId());
						learnerCourses.add(learnerCourse);
				    }
				    surveySuggestedCourse.setLearnerCourses(learnerCourses);
				    surveySuggestedCourses.add(surveySuggestedCourse);
				    check = false;
				    break;
				}
		    }
		}
	    }
	    form.setLearnerIdList(learnerIdList);
	    form.setSurveySuggestedCourses(surveySuggestedCourses);
	}
	if (page == 10) {
	    int intLimit = 100;
	    List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>();
	    List<EnrollmentCourseView> enrollmentCourseViewLists = new ArrayList<EnrollmentCourseView>();
	    String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");
	    if (StringUtils.isNumeric(limit.trim()))
		intLimit = Integer.parseInt(limit);
	    Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails())
		    .getCurrentCustomer();
	    if (form.getEnrollCourses() != null) {
			for (int i = 0; i < form.getEnrollCourses().length; i++) {
			    Course course = courseCourseGrpService.loadForUpdateCourse(Long.parseLong(form.getEnrollCourses()[i]));
			    String strEntDate = form.getSearchDateLimit();
			    Date entitlementDate = null;
			    if (!StringUtils.isBlank(strEntDate)){
			    	entitlementDate = DateUtil.getDateObject(strEntDate);
			    }
			    enrollmentCourseViewList = entitlementService.getCoursesForEnrollmentByCustomer(customer, course.getCourseTitle(), "",
					"", entitlementDate, null, intLimit);
				for (int k = 0; k < enrollmentCourseViewList.size(); k++) {
				    if (enrollmentCourseViewList.get(k) != null) {
				    	enrollmentCourseViewList.get(k).setSelected(true);
				    }
				}
				enrollmentCourseViewLists.addAll(enrollmentCourseViewList);
			}
			form.setEnrollmentCourseViewList(enrollmentCourseViewLists);
			SelectTypeOfCourse(request, command, null, page);
	    }
	}
	super.postProcessPage(request, command, errors, page);
    }

    private boolean matchSurveyAnswer(List<SuggestedTraining> suggestedTrainings, List<SurveyResultAnswer> surveyResultAnswers) {
	boolean isPresent = false;
	List<SurveyAnswerItem> surveyAnswerItemsFromSuggestedTraining = new ArrayList<SurveyAnswerItem>();
	List<SurveyAnswerItem> surveyAnswerItemsFromSurveyResultAnswer = new ArrayList<SurveyAnswerItem>();

	Label1: for (int i = 0; i < surveyResultAnswers.size(); i++) {
	    for (int z = 0; z < suggestedTrainings.size(); z++) {
		surveyAnswerItemsFromSuggestedTraining = suggestedTrainings.get(z).getResponses();
		surveyAnswerItemsFromSurveyResultAnswer = surveyResultAnswers.get(i).getSurveyAnswerItems();
		for (int k = 0; k < surveyAnswerItemsFromSuggestedTraining.size(); k++) {
		    if (surveyAnswerItemsFromSurveyResultAnswer.contains(surveyAnswerItemsFromSuggestedTraining.get(k))) {
			isPresent = true;
			break Label1;
		    } else {
			isPresent = false;
		    }
		}
	    }
	}

	return isPresent;
    }

    private void saveEnrollment(EnrollmentDetailsForm form, Brander brander) throws Exception {
		VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
		Customer customer = null;
		if (vu360UserService.hasAdministratorRole(user)) {
		    customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		} else {
		    customer = user.getLearner().getCustomer();
		}
		List<Learner> learnersToBeEnrolled = getLearnersTobeEnrolled(form);
		
		learnersToBeEnrolled= getActiveLearners(learnersToBeEnrolled);// updated by muhammad akif for (LMS-16047) 
		SaveEnrollmentParam saveEnrollmentParam = new SaveEnrollmentParam(form.getEnrollmentCourseViewList(), learnersToBeEnrolled,
			form.isModifyAllEntitlements(), form.getAllCourseStartDate(), form.getAllCourseEndDate(), form.getDuplicates());
	
		saveEnrollmentParam.setEnableEnrollmentEmailsForNewCustomers(customer.getDistributor().getDistributorPreferences()
			.isEnableRegistrationEmailsForNewCustomers() ? customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers() ? true
			: false : false);
		saveEnrollmentParam.setBrander(brander);
		saveEnrollmentParam.setUser(user);
		saveEnrollmentParam.setNotifyLearner(form.getEnrollConfirmation());
		saveEnrollmentParam.setNotifyManagerOnConfirmation(form.getOnConfirmation());
		saveEnrollmentParam.setVelocityEngine(velocityEngine);
		saveEnrollmentParam.setEmailService(learnersToBeMailedService);
		// async code here
		EnrollLearnerAsyncTask task = new EnrollLearnerAsyncTask();
		task.setSaveEnrollmentParam(saveEnrollmentParam);
		task.setEnrollmentService(enrollmentService);
		asyncTaskExecutorWrapper.execute(task);
		form.setAttemptedToEnroll(learnersToBeEnrolled.size());
		int noOfCoursesSelected = 0;
		for (EnrollmentCourseView courseView : form.getEnrollmentCourseViewList()) {
		    if (courseView.getSelected()){
		    	noOfCoursesSelected++;
		    }
		}
		form.setCoursesAssigned(noOfCoursesSelected);
    }

    // LMS-3212 joong end

    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
	AddEnrollmentValidator validator = (AddEnrollmentValidator) this.getValidator();
	EnrollmentDetailsForm form = (EnrollmentDetailsForm) command;
	errors.setNestedPath("");
	log.debug("Page num --- " + page);
	switch (page) {

	case 0:
	    validator.validateFirstPage(form, errors);
	    break;
	case 1:
	    if (!form.getAction().equalsIgnoreCase("search"))
		validator.validateSecondPage(form, errors);
	    break;
	case 2:
	    validator.validateThirdPage(form, errors);
	    break;
	case 3:
	    if (!form.getLearnerGroupSearchAction().equalsIgnoreCase("search"))
		validator.validateFourthPage(form, errors);
	    break;
	case 4:
	    validator.validateFifthPage(form, errors);
	    // [8/25/2010] LMS-6891, LMS-6889, LMS-6857 :: Enroll Learner Class
	    // size Issue, Validations and Back Traversing on different type of
	    // courses
	    if (form.isSelectedSyncCourse()) {
		validator.validateSeventhPage(form, errors);
	    }
	    break;
	case 5:
	    if (form.getPrev() != null) {
		if ((form.getPrev().equals("next"))) {
		    validator.validateSixthPage(form, errors);
		}
	    }
	    break;
	case 9:
	    if (!form.getAction().equalsIgnoreCase("searchSurvey"))
		validator.validateNinthPage(form, errors);
	    break;
	case 10:
	    validator.validateTenthPage(form, errors);
	    break;

	default:
	    break;
	}
	errors.setNestedPath("");
	// super.validatePage(command, errors, page, finish);
    }

    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
	EnrollmentDetailsForm form = (EnrollmentDetailsForm) command;
	if (getCurrentPage(request) == 1) {
	    if (form.getGroups() == null) {

	    }
	}
	super.onBind(request, command, errors);
    }

    private List<Learner> getLearnersTobeEnrolled(EnrollmentDetailsForm form) {
		String enrollmentMethod = form.getEnrollmentMethod();
		List<Learner> learnersToBeEnrolled = new ArrayList<Learner>();
		if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNER)) {
		    if (form.getLearners() != null && form.getLearners().size() > 0) {
				for (LearnerItemForm learner : form.getSelectedLearners()) {
					Learner selectedLearner = learnerService.getLearnerByVU360UserId(learner.getUser());
				    learnersToBeEnrolled.add(selectedLearner);
				}
		    }
		} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_ORGGROUP)) {
		    // Get Learners from selected Organizational Groups
		    String[] OrgGroupIdsArray = form.getGroups();
		    List<Long> orgGroupIdList = new ArrayList<Long>();
		    for (int i = 0; i < OrgGroupIdsArray.length; i++) {
				if (StringUtils.isNotBlank(OrgGroupIdsArray[i])) {
				    orgGroupIdList.add(Long.valueOf(OrgGroupIdsArray[i]));
				}
		    }
		    Long[] orgGroupIds = new Long[orgGroupIdList.size()];
		    orgGroupIds = orgGroupIdList.toArray(orgGroupIds);
		    learnersToBeEnrolled = orgGroupLearnerGroupService.getLearnersByOrgGroupIds(orgGroupIds);
		} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_LEARNERGROUP)) {
		    List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = form.getLearnerGroupEnrollmentItems();
		    if (learnerGroupEnrollmentItems != null && learnerGroupEnrollmentItems.size() > 0) {
				List<Long> learnerGroupIdList = new ArrayList<Long>();
		
				for (LearnerGroupEnrollmentItem item : learnerGroupEnrollmentItems) {
				    if (item.isSelected()) {
				    	learnerGroupIdList.add(item.getLearnerGroupId());
				    }
				}
				Long learnerGroupIdArray[] = new Long[learnerGroupIdList.size()];
				learnerGroupIdArray = learnerGroupIdList.toArray(learnerGroupIdArray);
				learnersToBeEnrolled = orgGroupLearnerGroupService.getLearnersByLearnerGroupIds(learnerGroupIdArray);
		    }
		} else if (enrollmentMethod.equalsIgnoreCase(ENROLLMENT_METHOD_SURVEYRESPONSE)) {
		    Learner learner = new Learner();
		    for (int i = 0; i < form.getLearnerIdList().size(); i++) {
				learner = learnerService.getLearnerByID(form.getLearnerIdList().get(i));
				learnersToBeEnrolled.add(learner);
		    }
		}
		return learnersToBeEnrolled;
    }
    
	/**
	 * code modified by muhammad akif
	 * 20-06-2014
	 *  (LMS-16047) Expired, Locked & Disabled users Should not be able to enroll
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

    private void coursesSearch(EnrollmentDetailsForm form, Customer customer, HttpServletRequest request) {
	// if course search is requested
	if (form.getCourseSearchType().trim().equalsIgnoreCase("advanceSearch")) {

	    int intLimit = 100;
	    Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(
				VU360Branding.BRAND), new Language());
	    String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");

	    if (StringUtils.isNumeric(limit.trim()))
		intLimit = Integer.parseInt(limit);

	    // get parameter from request and pass to service layer
	    // enrollmentStartDate =
	    // formatter.parse(saveEnrollmentParam.getStartDate());
	    String strEntDate = form.getSearchDateLimit();
	    Date entitlementDate = null;
	    if (!StringUtils.isBlank(strEntDate))
		entitlementDate = DateUtil.getDateObject(strEntDate);
	    // List<CustomerEntitlement> ce =
	    // entitlementService.searchCoursesForEnrollment( customer,
	    // form.getSearchCourseName(), form.getSearchCourseId(),
	    // form.getSearchEntitlementName(), form.getSearchDateLimit(),
	    // intLimit);

	    List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>();
	    
	    Long[] customerEntitlementIds = null;
	    
	    // @MariumSaud : LMS-22023 -- The method will return 'true' if manager Permission for 'Enforce Org. Group Enrollment Restriction' is Enabled for Customer else return 'false'
	    //                            If 'true' then Course Filteration will be applied on basis of organizational group else all courses will be open
	    boolean isEnabled = entitlementService.isEnforceOrgGroupEnrollmentRestrictionEnable(customer);
	    
	    if(isEnabled){
	    	// @MariumSaud : LMS-21702 -- Filter course on the basis of organization group as per enrollment method selected
		    customerEntitlementIds = getCustomerEntitlementIdsByOrgGrps(form,form.getEnrollmentMethod());
	    }

	   
		enrollmentCourseViewList = entitlementService.getCoursesForEnrollmentByCustomer(customer, form.getSearchCourseName(),
			form.getSearchCourseId(), form.getSearchEntitlementName(), entitlementDate, customerEntitlementIds, intLimit);

	    form.setEnrollmentCourseViewList(enrollmentCourseViewList);
	    if (form.getEnrollmentCourseViewList().size() <= SEARCH_COURSE_PAGE_SIZE)
		form.setCourseSearchEnd(form.getEnrollmentCourseViewList().size() - 1);
	    else
		form.setCourseSearchEnd((SEARCH_COURSE_PAGE_SIZE - 1));
	    form.setCourseSearchStart(0);
	    form.setCourseSearchPageNumber(0);
	    form.setCourseSearchShowAll("");

	}

    }

 // @MariumSaud : LMS-21702 : Filter course on the basis of following Enrollment Method
    // 1. User -- Only those courses contract will displays whose OrgGroupEntitlement bound with the organizationGroup of selected Learners.
    // 2. OrganizationGroup -- Only those courses contract will displays whose OrgGroupEntitlement bound with the selected organizationGroup.
    // 3. UserGroup -- Only those courses contract will displays whose OrgGroupEntitlement bound with the selected LearnerGroups organizationGroup.
    public Long[] getCustomerEntitlementIdsByOrgGrps(EnrollmentDetailsForm form, String enrollmentMethod){
    	List<Long> customerEntitlementIds= new ArrayList<Long>();
    	if(enrollmentMethod.equals(ENROLLMENT_METHOD_LEARNER)){
    		Long selectedLearnerId[]=new Long[form.getSelectedLearners().size()];
			if(!form.getSelectedLearners().isEmpty()){
				for(int i=0;i<form.getSelectedLearners().size();i++){
					selectedLearnerId[i] = form.getSelectedLearners().get(i).getUser().getLearner().getId(); 
				}
				customerEntitlementIds = entitlementService.getCustomerEntitlementForOrgGroupEntitlementsByLearnerIds(selectedLearnerId);
			}
    	}
    	else if(enrollmentMethod.equals(ENROLLMENT_METHOD_ORGGROUP)){
    		List<String> list = Arrays.asList(form.getGroups());
    		// Converting String[] to List<Long>
    		List<Long> selectedOrgGrpIds = new ArrayList<Long>();
    		for(String s : list){
    			selectedOrgGrpIds.add(Long.valueOf(s));
    		}
    		
    		customerEntitlementIds = entitlementService.getCustomerEntitlementForOrgGroupEntitlementsByOrgGrpIds(selectedOrgGrpIds);
    	}
    	else if(enrollmentMethod.equals(ENROLLMENT_METHOD_LEARNERGROUP)){
    		
    		List<Long> selectedLearnerGrpIds = new ArrayList<Long>();
    		
    	    List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = form.getLearnerGroupEnrollmentItems();
    	    if (learnerGroupEnrollmentItems != null && learnerGroupEnrollmentItems.size() > 0) {
    		for (LearnerGroupEnrollmentItem item : learnerGroupEnrollmentItems) {
    		    if (item.isSelected()) {
    		    	selectedLearnerGrpIds.add(item.getLearnerGroupId());
    		    	}
    			}
    	    }
    		
    	    customerEntitlementIds = entitlementService.getCustomerEntitlementForOrgGroupEntitlementsByLearnerGroupIds(selectedLearnerGrpIds);
    		
    	}
    	
    	if(customerEntitlementIds!=null){
    		Long[] entitlementIds = new Long[customerEntitlementIds.size()];
        	entitlementIds = customerEntitlementIds.toArray(entitlementIds);
        	return entitlementIds;
    	}
    	
    	return null;
    }
    
    void coursesSearchShowAll(EnrollmentDetailsForm form) {
	// show all courses is requested
	if (form.getCourseSearchType().trim().equalsIgnoreCase("showAll")) {

	    form.setCourseSearchEnd(form.getEnrollmentCourseViewList().size() - 1);
	    form.setCourseSearchStart(0);
	    form.setCourseSearchShowAll("showAll");
	    form.setCourseSearchPageNumber(0);
	    // form.setCourseSearchType("");
	}

    }

    void coursesSearchMove(EnrollmentDetailsForm form) {
		// show all courses is requested
		if (form.getCourseSearchType().trim().equalsIgnoreCase("move")) {
		    int total = form.getEnrollmentCourseViewList().size();
		    if (form.getCourseSearchDirection().trim().equalsIgnoreCase("next")) {
				int newPageNumber = form.getCourseSearchPageNumber() + 1;
				if ((newPageNumber * SEARCH_COURSE_PAGE_SIZE) > total) {
				    log.debug(newPageNumber + " greater than  " + total);
				    // nochange
				} else {
				    int lastPageLimit = (form.getCourseSearchPageNumber() + 2) * SEARCH_COURSE_PAGE_SIZE;
				    form.setCourseSearchStart(form.getCourseSearchEnd() + 1);
				    form.setCourseSearchPageNumber(form.getCourseSearchPageNumber() + 1);
				    if (total >= lastPageLimit) {
				    	form.setCourseSearchEnd((lastPageLimit - 1));
				    } else{
				    	form.setCourseSearchEnd((total - 1));
				    }
				}
		    } else if (form.getCourseSearchDirection().trim().equalsIgnoreCase("prev")) {
				int newPageNumber = form.getCourseSearchPageNumber() - 1;
				if ((newPageNumber * SEARCH_COURSE_PAGE_SIZE) < 0) {
				    log.debug(newPageNumber + " greater than  " + total);
				    // nochange
				} else {
				    if (newPageNumber == 0) {
				    	form.setCourseSearchStart(0);
				    } else if (newPageNumber > 0) {
				    	form.setCourseSearchStart(((newPageNumber * SEARCH_COURSE_PAGE_SIZE)));
				    } else {
				    	form.setCourseSearchStart(0);
				    }
				    form.setCourseSearchEnd((((newPageNumber * SEARCH_COURSE_PAGE_SIZE) + (SEARCH_COURSE_PAGE_SIZE - 1))));
				    form.setCourseSearchPageNumber(form.getCourseSearchPageNumber() - 1);
				}
		    }
		}
		form.setCourseSearchShowAll("");
    }

    void pickUptheSelectedCourses(EnrollmentDetailsForm form) {

	for (CourseEntitlementItem ceItem : form.getCourseEntitlementItems()) {
	    if (ceItem.getCourses() != null) {
		for (EnrollmentCourse entCourse : ceItem.getCourses()) {
		    if (entCourse.getSelected()) {
			addUserSelectedCourse(ceItem, entCourse.getCourse(), form);
		    }
		}
	    }
	}

    }

    void addUserSelectedCourse(CourseEntitlementItem ceItem, Course entCourse, EnrollmentDetailsForm form) {
	// keeping in mind that it should not be repeated
	boolean entitlementFound = false;
	boolean courseFound = false;
	for (CourseEntitlementItem selectedCEItem : form.getSelectedCourseEntitlementItems()) {
	    if (selectedCEItem.getEntitlement().getId().equals(ceItem.getEntitlement().getId())) {

		// if( selectedCEItem.getCourses() != null && )
		for (EnrollmentCourse selectedEntitlementCourse : selectedCEItem.getCourses()) {

		    if (entCourse.getId().equals(selectedEntitlementCourse.getCourse().getId())) {
			selectedCEItem.setSelected(true);
			selectedEntitlementCourse.setSelected(true);
			courseFound = true;
			break;
		    }

		}
		if (courseFound == false)
		    log.debug(" entitlement -" + selectedCEItem.getEntitlement().getId() + "- found but course -" + entCourse.getId()
			    + "- not found ");
		entitlementFound = true;
		break; // if ent id and course id matched

	    }
	}

	if (form.getSelectedCourseEntitlementItems().size() == 0 || entitlementFound == false) {
	    form.getSelectedCourseEntitlementItems().add(ceItem);
	}

    }

    /*
     * The getters are setters
     */

    public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
	return orgGroupLearnerGroupService;
    }

    public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
	this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
    }

    public EnrollmentService getEnrollmentService() {
	return enrollmentService;
    }

    public void setEnrollmentService(EnrollmentService enrollmentService) {
	this.enrollmentService = enrollmentService;
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

    public EntitlementService getEntitlementService() {
	return entitlementService;
    }

    public void setEntitlementService(EntitlementService entitlementService) {
	this.entitlementService = entitlementService;
    }

    public CourseAndCourseGroupService getCourseCourseGrpService() {
	return courseCourseGrpService;
    }

    public void setCourseCourseGrpService(CourseAndCourseGroupService courseCourseGrpService) {
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
     * @param learnersToBeMailedService
     *            the learnersToBeMailedService to set
     */
    public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
	this.learnersToBeMailedService = learnersToBeMailedService;
    }

    public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
	this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
    }

    public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
    	return asyncTaskExecutorWrapper;
    }

    public SynchronousClassService getSynchronousClassService() {
	return synchronousClassService;
    }

    public void setSynchronousClassService(SynchronousClassService synchronousClassService) {
	this.synchronousClassService = synchronousClassService;
    }

    public SurveyService getSurveyService() {
    	return surveyService;
    }

    public void setSurveyService(SurveyService surveyService) {
    	this.surveyService = surveyService;
    }
}