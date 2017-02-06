package com.softech.vu360.lms.web.controller.manager;

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

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddTrainingPlanForm;
import com.softech.vu360.lms.web.controller.model.CourseEntitlementItem;
import com.softech.vu360.lms.web.controller.model.EnrollmentCourse;
import com.softech.vu360.lms.web.controller.model.EnrollmentDetailsForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupEnrollmentItem;
import com.softech.vu360.lms.web.controller.validator.AddTrainingPlanValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Saptarshi
 *
 */
public class AddTrainingPlanWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddTrainingPlanWizardController.class.getName());

	private TrainingPlanService trainingPlanService;
	private EntitlementService entitlementService = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private CourseAndCourseGroupService courseCourseGrpService;
	private EnrollmentService enrollmentService;
		

	private static final int SEARCH_COURSE_PAGE_SIZE = 10;
	

	private String closeTemplate = null;
	private String summaryTemplate = null;

//	HttpSession session=null;

	public AddTrainingPlanWizardController() {

		super();
		setCommandName("trainingPlanForm");
		setCommandClass(AddTrainingPlanForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/trainingPlan/selectTrainingPlanName"
				, "manager/trainingPlan/trainingPlanCourses"
				, "manager/trainingPlan/addTrainingPlanResults" });
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

		AddTrainingPlanForm form = (AddTrainingPlanForm)command;
		AddTrainingPlanValidator validator = (AddTrainingPlanValidator)this.getValidator();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		
		if( page == 0 ){
			validator.validateFirstPage(form, errors);
			if(StringUtils.isEmpty(request.getParameter("action"))){
				request.setAttribute("newPage","true");
			}
		}
		if( page == 1){
			if( request.getParameter("action") != null ) {
				if( !request.getParameter("action").equals("courseSearch")){
					request.setAttribute("newPage","true");
				}
			}
			coursesSearch(form, customer,request, errors);
			coursesSearchMove(form);
			coursesSearchShowAll(form); // must be executed last
			//validator.validateSixthPage(form, errors);
			//}
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AddTrainingPlanForm form = (AddTrainingPlanForm)command;
		if( page == 5 ) {
			// do nothing
		}
		//check if the bundle has any sync course in it
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException error) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
		TrainingPlan trainingPlan = new TrainingPlan();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		List<Course> selectedCourses = new ArrayList<Course>();
		Course course = null;
		for(EnrollmentCourseView courseView : addTrainingPlanForm.getEnrollmentCourseViewList()){
			
			if(courseView.getSelected()){
				course = courseCourseGrpService.getCourseById(courseView.getCourseId());
				selectedCourses.add(course);
			}
		}

		List <TrainingPlanCourse> trainingPlanCourses = new ArrayList <TrainingPlanCourse>();
		
		trainingPlan.setName(addTrainingPlanForm.getTrainingPlanName().trim());
		if(addTrainingPlanForm.getDescription() != null){
			trainingPlan.setDescription(addTrainingPlanForm.getDescription().trim());
		}	
		//TODO set trainingPlanCourse
		trainingPlan.setCustomer(customer);		
		//trainingPlan.setCourses(trainingPlanCourses);
		trainingPlan= trainingPlanService.addTrainingPlan(trainingPlan);

		for( Course c : selectedCourses) {			
			TrainingPlanCourse tpcourse = new TrainingPlanCourse();
			tpcourse.setCourse(c);
			tpcourse.setTrainingPlan(trainingPlan);
			trainingPlanCourses.add(tpcourse);
		}
		
		if (!trainingPlanCourses.isEmpty()) {
			List<TrainingPlanCourse> trainingPlanCoursesAdded = trainingPlanService.addTrainingPlanCourses(trainingPlanCourses);
			if (!trainingPlanCoursesAdded.isEmpty()) {
				//updatedTrainingPlan.getCourses().addAll(trainingPlanCoursesAdded);
				trainingPlan.setCourses(trainingPlanCourses);
				//trainingPlanService.addTrainingPlan(trainingPlan);
			}
		}
		trainingPlan= trainingPlanService.addTrainingPlan(trainingPlan);
		context.put("name", trainingPlan.getName());
		context.put("desc", trainingPlan.getDescription());
		context.put("courses", selectedCourses);

		return new ModelAndView(closeTemplate, "context", context);
	}
	/*
	 * TODO This method requires detailed review for optimization (Faisal A. Siddiqui) 
 	*/
	protected Object formBackingObject(HttpServletRequest request)throws Exception {

		Object command = super.formBackingObject(request);
		try {
			AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
			VU360User logInUser = VU360UserAuthenticationDetails.getCurrentUser();

			List <LearnerGroup> selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList <LearnerGroup>();
			//List <OrganizationalGroup> learnerOrgGroups = null; //@Wajahat: 13-Oct-2014 against ticket LMS-16368
			List <LearnerGroupEnrollmentItem> learnerGroupList = addTrainingPlanForm.getLearnerGroupTrainingItems();

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
			Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
					getAuthentication().getDetails()).getCurrentCustomerId();
			
			
			/*
			 * @Wajahat: 13-Oct-2014 against ticket LMS-16368
			 * The below code is committed due to the retrieval of data through an iterative DB calls
			 * **************************************************************************************
			learnerOrgGroups = orgGroupLearnerGroupService.getOrgGroupsByCustomer(customer, logInUser);
			List<LearnerGroup> learnerGroups = null;
			for( int orgGroupNo=0; orgGroupNo<learnerOrgGroups.size(); orgGroupNo++ ) {
				learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(learnerOrgGroups.get(orgGroupNo).getId());
				selectedLearnerGroupsAssociatedWithOrgGroup.addAll(learnerGroups);
				//selectedLearnerGroupsAssociatedWithOrgGroup.addAll(learnerOrgGroups.get(orgGroupNo).getLearnerGroups());
			}
			*/
			
			/*
			 * @Wajahat: 13-Oct-2014 against ticket LMS-16368
			 * The below two lines of code is the replacement for the above commented code
			 */
			selectedLearnerGroupsAssociatedWithOrgGroup = new ArrayList <LearnerGroup>();
			selectedLearnerGroupsAssociatedWithOrgGroup.addAll(orgGroupLearnerGroupService.getAllLearnerGroups(customerId, logInUser));
			/*
			for(int learnerGroupNo=0; learnerGroupNo < selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++ ) {

				LearnerGroup lgrp = selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo);
				LearnerGroupEnrollmentItem item = new LearnerGroupEnrollmentItem();
				item.setLearnerGroupId(lgrp.getId());
				item.setLearnerGroupName(lgrp.getName());
				item.setSelected(false);
				learnerGroupList.add(item);
			}

			if (logInUser.isLMSAdministrator())
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			else
				customer = logInUser.getLearner().getCustomer();
			List<CustomerEntitlement> customerEntitlementList = null;//TODO remove this local variable
			List<CourseEntitlementItem> courseEntitlements =  new ArrayList<CourseEntitlementItem>();
			
			customerEntitlementList = entitlementService.getAllCustomerEntitlements(customer);
			//addTrainingPlanForm.setCustomerEntitlements(customerEntitlementList);
			List<TrainingCustomerEntitlement> trCustEntList = new ArrayList <TrainingCustomerEntitlement>();

			for(CustomerEntitlement custEnt : customerEntitlementList) {
				Date date = new Date();
				List<TrainingCourse> trCourses = new ArrayList <TrainingCourse>();
				//List<TrainingCourseGroup> trCourseGroups = new ArrayList <TrainingCourseGroup>();
				TrainingCustomerEntitlement trainingCustEnt = new TrainingCustomerEntitlement();
				trainingCustEnt.setCustomerEntitlement(custEnt);

				if( custEnt.getEndDate() != null ) {
					if( date.after(custEnt.getEndDate())) {
						trainingCustEnt.setExpired(true);
					} else {
						if(custEnt.isAllowUnlimitedEnrollments()|| (custEnt.getMaxNumberSeats()-custEnt.getNumberSeatsUsed())>0){
							trainingCustEnt.setExpired(false);
						}else{
							trainingCustEnt.setExpired(true);
						}
					}
				}
				// should be removed after code review 
				Set<Course> uniqueCourses = entitlementService.getUniqueCourses(custEnt);//custEnt.getUniqueCourses();
				for(Course course : uniqueCourses) {
					TrainingCourse trCourse = new TrainingCourse();
					trCourse.setCourse(course);
					trCourse.setSelected(false);
					trCourses.add(trCourse);
				}
				trainingCustEnt.setCourses(trCourses);
				List<TrainingCourse> selectedCourses = new ArrayList <TrainingCourse>();
				//trainingCustEnt.setCourseGroups(trCourseGroups);
				trCustEntList.add(trainingCustEnt);
			}
			
			addTrainingPlanForm.setCustomerEntitlements(trCustEntList);
			*/
			addTrainingPlanForm.setLearnerGroupTrainingItems(learnerGroupList);
			

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected Map referenceData(HttpServletRequest request,
			Object command,	Errors errors, int page) throws Exception {

		Map <Object, Object>model = new HashMap <Object, Object>();
		AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
		
		switch(page) {

		case 0:
			break;
		case 1:
			break;
		case 2:
			AddTrainingPlanValidator validator = (AddTrainingPlanValidator)this.getValidator();
			validator.validateSelectedCourses(addTrainingPlanForm, errors);
			List<Course> selectedCourses = new ArrayList<Course>();
			
//			for(TrainingCustomerEntitlement trCustEnt : addTrainingPlanForm.getCustomerEntitlements()) {
//				if(trCustEnt.getCourses() != null){
//					for(TrainingCourse trCourse : trCustEnt.getCourses()) {
//						if(trCourse.getSelected() == true) {
//							selectedCourses.add(trCourse.getCourse());
//						}
//					}
//				}
//			}
			Course course=null;
			for(EnrollmentCourseView courseView : addTrainingPlanForm.getEnrollmentCourseViewList()){
				
				if(courseView.getSelected()){
					course=new Course();
					course.setId(courseView.getCourseId());
					course.setName(courseView.getCourseName());
					course.setCourseTitle(courseView.getCourseName());
					
					selectedCourses.add(course);
				}
			}
			
			model.put("courses", selectedCourses);
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
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		errors.setNestedPath("");

		switch (page) {

		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
		super.validatePage(command, errors, page, finish);
	}
	
	
	private void coursesSearch(AddTrainingPlanForm form,Customer customer ,HttpServletRequest request, Errors errors ) {
		
		// if course  search  is requested 
		AddTrainingPlanValidator validator = (AddTrainingPlanValidator)this.getValidator();
//		session = request.getSession();
		
		boolean doValidate = true;
		if( form.getCourseSearchType() != null && ( form.getCourseSearchType().trim().equalsIgnoreCase("advanceSearch")
					|| form.getCourseSearchType().trim().equalsIgnoreCase("showAll") ) ) {
			
			int intLimit  = 100;
			doValidate = false;
			Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");
			
			if(StringUtils.isNumeric(limit.trim()))
				intLimit  = Integer.parseInt(limit);
			
			// get parameter from request and pass to service layer
			// enrollmentStartDate = formatter.parse(saveEnrollmentParam.getStartDate());
			String strEntDate=form.getSearchDateLimit();
			Date entitlementDate=null;
			if(!StringUtils.isBlank(strEntDate))
				entitlementDate = DateUtil.getDateObject(strEntDate);
			//	List<CustomerEntitlement> ce = entitlementService.searchCoursesForEnrollment( customer, form.getSearchCourseName(), form.getSearchCourseId(), form.getSearchEntitlementName(), form.getSearchDateLimit(), intLimit);
	    
			List<EnrollmentCourseView> trainingPlanCourseViewList = getUniqueCoursesList(form, intLimit, customer, entitlementDate);
			
			//for Sorting:
			Map<Object, Object> context = new HashMap<Object, Object>();
			String sortDirection = request.getParameter("sortDirection");
//			if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
//			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex="0";
			String pageIndex = request.getParameter("pageCurrIndex");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			Map PagerAttributeMap = new HashMap();
			
			/**
			 *  added by Dyutiman for sorting purpose
			 */
			if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
				// sorting against invitation name
				request.setAttribute("PagerAttributeMap", PagerAttributeMap);
				//if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < trainingPlanCourseViewList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanCourseViewList.size() ; j ++ ) {
								if( i != j ) {
									EnrollmentCourseView courseName1 =  trainingPlanCourseViewList.get(i);
									EnrollmentCourseView courseName2 =  trainingPlanCourseViewList.get(j);
									
									if(sortColumnIndex.equalsIgnoreCase("0")){
										if( courseName1.getCourseName().compareTo(courseName2.getCourseName()) > 0 ) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("1")){
										if( courseName1.getCourseId() > courseName2.getCourseId()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("2")){
										if( courseName1.getTotalSeats() > courseName2.getTotalSeats()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("3")){
										if( courseName1.getSeatsUsed() > courseName2.getSeatsUsed()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("4")){
										if( courseName1.getSeatsRemaining() > courseName2.getSeatsRemaining()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("5")){
										if( courseName1.getExpirationDate().after(courseName2.getExpirationDate())) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("6")){
										if( courseName1.getEntitlementName().compareTo(courseName2.getEntitlementName()) > 0) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									
								}
							}
						}
						context.put("showAll", showAll);
						form.setSortDirection(1);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", sortColumnIndex);
						form.setSortColumnIndex(Integer.parseInt(sortColumnIndex));
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						for( int i = 0 ; i < trainingPlanCourseViewList.size() ; i ++ ) {
							for( int j = 0 ; j < trainingPlanCourseViewList.size() ; j ++ ) {
								if( i != j ) {
									EnrollmentCourseView courseName1 =  trainingPlanCourseViewList.get(i);
									EnrollmentCourseView courseName2 =  trainingPlanCourseViewList.get(j);
									
									if(sortColumnIndex.equalsIgnoreCase("0")){
										if( courseName1.getCourseName().compareTo(courseName2.getCourseName()) < 0 ) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("1")){
										if( courseName1.getCourseId() < courseName2.getCourseId()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("2")){
										if( courseName1.getTotalSeats() < courseName2.getTotalSeats()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("3")){
										if( courseName1.getSeatsUsed() < courseName2.getSeatsUsed()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("4")){
										if( courseName1.getSeatsRemaining() < courseName2.getSeatsRemaining()) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("5")){
										if( courseName1.getExpirationDate().before(courseName2.getExpirationDate())) {
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
									if(sortColumnIndex.equalsIgnoreCase("6")){
										if( courseName1.getEntitlementName().compareTo(courseName2.getEntitlementName()) < 0) { 
											EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
											trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
											trainingPlanCourseViewList.set(j, tempMap);
										}
									}
								}
							}
						}
						context.put("sortDirection", 0);
						form.setSortDirection(0);
						context.put("sortColumnIndex", sortColumnIndex);
						form.setSortColumnIndex(Integer.parseInt(sortColumnIndex));
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
			}
			// end sorting
			
			form.setEnrollmentCourseViewList(trainingPlanCourseViewList );
			//	Collections.sort( form.getEnrollmentCourseViewList()) ;
			
	
			/*List<CustomerEntitlement> customerEntitlementList = entitlementService.getAllCustomerEntitlements(customer);
			//addTrainingPlanForm.setCustomerEntitlements(customerEntitlementList);
			List<TrainingCustomerEntitlement> trCustEntList = new ArrayList <TrainingCustomerEntitlement>();

			for(CustomerEntitlement custEnt : customerEntitlementList) {
				Date date = new Date();
				List<TrainingCourse> trCourses = new ArrayList <TrainingCourse>();
				//List<TrainingCourseGroup> trCourseGroups = new ArrayList <TrainingCourseGroup>();
				TrainingCustomerEntitlement trainingCustEnt = new TrainingCustomerEntitlement();
				trainingCustEnt.setCustomerEntitlement(custEnt);

				if( custEnt.getEndDate() != null ) {
					if( date.after(custEnt.getEndDate())) {
						trainingCustEnt.setExpired(true);
					} else {
						if(custEnt.isAllowUnlimitedEnrollments()|| (custEnt.getMaxNumberSeats()-custEnt.getNumberSeatsUsed())>0){
							trainingCustEnt.setExpired(false);
						}else{
							trainingCustEnt.setExpired(true);
						}
					}
				} 
				if(custEnt.getCourses() != null && !custEnt.getCourses().isEmpty()) {
					for(Course course : custEnt.getCourses()) {
						TrainingCourse trCourse = new TrainingCourse();
						trCourse.setCourse(course);
						trCourse.setSelected(false);
						trCourses.add(trCourse);
					}
					trainingCustEnt.setCourses(trCourses);
				} else if(custEnt.getCourseGroups() != null && !custEnt.getCourseGroups().isEmpty()) {

					List<TrainingCourse> selectedCourses = new ArrayList <TrainingCourse>();

					for(CourseGroup courseGroup : custEnt.getCourseGroups()) {
						List<Course> courses = courseGroup.getCourses();
						List<Course> uniqueCourseList = new ArrayList<Course> ();
						boolean present = false;
						for(Course course : courses) {
							if(course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED)){
								
								for(Course uniqueCourse : uniqueCourseList) {
									if( course.getId().compareTo(uniqueCourse.getId()) == 0 ) {
										present = true;
										break;
									}
								}
								if( !present ){
									uniqueCourseList.add(course);
								}
								present = false;
							}
						}
						for(Course course : uniqueCourseList) {
							TrainingCourse trCourse = new TrainingCourse();
							trCourse.setCourse(course);
							trCourse.setSelected(false);
							trCourses.add(trCourse);
						}
						selectedCourses.addAll(trCourses);
						
					}
					trainingCustEnt.setCourses(selectedCourses);
				}
				//trainingCustEnt.setCourseGroups(trCourseGroups);
				trCustEntList.add(trainingCustEnt);
			}
			
			addTrainingPlanForm.setCustomerEntitlements(trCustEntList);
			
			*/
			if( form.getEnrollmentCourseViewList().size() <= SEARCH_COURSE_PAGE_SIZE )
				form.setCourseSearchEnd( form.getEnrollmentCourseViewList().size()-1);
			else
				form.setCourseSearchEnd( (SEARCH_COURSE_PAGE_SIZE -1));
			form.setCourseSearchStart(0);
			form.setCourseSearchPageNumber(0);
			form.setCourseSearchShowAll( "" );
		
		}
		else if( form.getCourseSearchType()!= null && form.getCourseSearchType().trim().equalsIgnoreCase("move") ) {
			doValidate = false;
		}
		
		if ( doValidate ) {
			validator.validateCourses(form,errors);
		}
	}

	/**
	 * LMS-7001
	 * Jason Burns added a comment - 24/Oct/10 11:26 PM
	 * Then we should make the add screen only show unique courses as well
	 * @param form
	 * @param intLimit
	 * @param entitlements
	 * @param entitlementDate
	 * @return
	 * @author sultan.mubasher 
	 */
	private List<EnrollmentCourseView> getUniqueCoursesList(
			AddTrainingPlanForm form, int intLimit,
			Customer customer, Date entitlementDate) {

		Map<Long,EnrollmentCourseView> enrollmentCourseViewUniqueMap=new HashMap<Long, EnrollmentCourseView>();
		List<EnrollmentCourseView> trainingPlanCourseViewList = entitlementService.getCoursesForEnrollmentByCustomer(customer , 
				form.getSearchCourseName() , form.getSearchCourseId() , form.getSearchEntitlementName() ,entitlementDate, intLimit);
		
		for(EnrollmentCourseView enrollmentCourseView:trainingPlanCourseViewList) {
			if(!enrollmentCourseViewUniqueMap.containsKey(enrollmentCourseView.getCourseId()))
				enrollmentCourseViewUniqueMap.put(enrollmentCourseView.getCourseId(),enrollmentCourseView );
		}
		
		return new ArrayList<EnrollmentCourseView>(enrollmentCourseViewUniqueMap.values());
	}
	

	void coursesSearchShowAll(AddTrainingPlanForm form){
		// show all courses is requested 
		if( form.getCourseSearchType()!=null && form.getCourseSearchType().trim().equalsIgnoreCase("showAll") ){

			form.setCourseSearchEnd( form.getEnrollmentCourseViewList().size()-1);
			form.setCourseSearchStart(0);
			form.setCourseSearchShowAll( "showAll" );
			form.setCourseSearchPageNumber(0);
			//form.setCourseSearchType("");
		}
		
	}
	
	void coursesSearchMove(AddTrainingPlanForm form){
		// show all courses is requested 
		if( form.getCourseSearchType()!=null && form.getCourseSearchType().trim().equalsIgnoreCase("move") ){
			int total = form.getEnrollmentCourseViewList().size();
		
			if( form.getCourseSearchDirection().trim().equalsIgnoreCase("next")){
				int newPageNumber = form.getCourseSearchPageNumber() + 1 ;
				if( ( newPageNumber * SEARCH_COURSE_PAGE_SIZE ) > total ){
					log.debug( newPageNumber +" greater than  "+total );
					// nochange
				}
				else{
					int lastPageLimit = (form.getCourseSearchPageNumber()+2) *  SEARCH_COURSE_PAGE_SIZE ;
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
				if( ( newPageNumber * SEARCH_COURSE_PAGE_SIZE ) < 0 ){
					log.debug( newPageNumber +" greater than  "+total );
					// nochange
				}
				else {
					int lastPageLimit = ( form.getCourseSearchPageNumber() - 1 ) *  SEARCH_COURSE_PAGE_SIZE ;
					
					if( newPageNumber == 0 ) {
						form.setCourseSearchStart(0 );
					}
					else if( newPageNumber > 0 ) {
						form.setCourseSearchStart(  ((newPageNumber  *  SEARCH_COURSE_PAGE_SIZE) ) );
					}

					else{
						form.setCourseSearchStart( 0 );
					}
					
					form.setCourseSearchEnd(  ( ((newPageNumber  *  SEARCH_COURSE_PAGE_SIZE) + ( SEARCH_COURSE_PAGE_SIZE -1) )) );
					form.setCourseSearchPageNumber( form.getCourseSearchPageNumber() - 1 ) ; 
				}
			}

		}
		
		form.setCourseSearchShowAll( "" );
		//form.setCourseSearchType("");
	}
	
	void  pickUptheSelectedCourses(EnrollmentDetailsForm  form){
		
		for(CourseEntitlementItem ceItem : form.getCourseEntitlementItems()) {
			if( ceItem.getCourses() != null ){
				for(EnrollmentCourse entCourse : ceItem.getCourses() ){
					if( entCourse.getSelected()){
						addUserSelectedCourse(ceItem , entCourse.getCourse() , form) ;
					}
				}
			}
		}
		
	}
	
	void addUserSelectedCourse(CourseEntitlementItem ceItem , Course entCourse ,EnrollmentDetailsForm  form){
		// keeping in mind that it should not be repeated
		boolean entitlementFound = false ;
		boolean courseFound = false ;
		for(CourseEntitlementItem selectedCEItem :  form.getSelectedCourseEntitlementItems()){
			if( selectedCEItem.getEntitlement().getId().equals(ceItem.getEntitlement().getId()) ) {
				
				//if( selectedCEItem.getCourses() != null && )
				for(EnrollmentCourse selectedEntitlementCourse : selectedCEItem.getCourses()){
				
					if(entCourse.getId().equals( selectedEntitlementCourse.getCourse().getId() )){
						selectedCEItem.setSelected(true);
						selectedEntitlementCourse.setSelected(true);
						courseFound = true ; 
						break;
					}
				}	
				if(courseFound == false )
					log.debug(" entitlement -"+ selectedCEItem.getEntitlement().getId() + "- found but course -"+entCourse.getId()+"- not found ");
				entitlementFound = true ;
				break; // if ent id and course id matched
				
			}
		}
		
		if(form.getSelectedCourseEntitlementItems().size() == 0 || entitlementFound == false ) {
			form.getSelectedCourseEntitlementItems().add(ceItem) ;
		}
	}
	
	
	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	/**
	 * @return the orgGroupLearnerGroupService
	 */
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	/**
	 * @param orgGroupLearnerGroupService the orgGroupLearnerGroupService to set
	 */
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

}