package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseAlertTriggeerFilter;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.FilterCourseForm;
import com.softech.vu360.lms.web.controller.validator.AddFilterCourseValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;

public class AddFilterCourseWizardController extends AbstractWizardFormController{

	private static transient Logger log = Logger.getLogger(AddFilterWizardController.class.getName());
	private static final String COURSES = "courses";
	private EntitlementService entitlementService=null;
	private SurveyService surveyService = null;
	private String finishTemplate = null;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;

	public AddFilterCourseWizardController(){

		super();
		setCommandName("addFilterCourseForm");
		setCommandClass(FilterCourseForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageTrigger/manageFilter/addCourse/add1"
		});

	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");

		Object command = super.formBackingObject(request);

		try {
			FilterCourseForm form = (FilterCourseForm)command;

			String filterId = request.getParameter("filterId");
			form.setFilterId(Long.parseLong(filterId));

			com.softech.vu360.lms.vo.VU360User loggedInUser = null;
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder
					.getContext().getAuthentication()).getDetails();
			com.softech.vu360.lms.vo.Customer customer = null;

			if (details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
				customer = (com.softech.vu360.lms.vo.Customer) request.getSession(true)
						.getAttribute("adminSelectedCustomer");

			} else if (details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
				loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				customer = loggedInUser.getLearner().getCustomer();
			}
			List<Course> courseList = new ArrayList<Course>();

			courseList = entitlementService.getAllCoursesByEntitlement(customer.getId(), "", "");
			
			form.setCourseListFromDB(courseList);
		}
		catch (Exception e) {
			log.debug("exception", e);
		}

		return command;

	}

	protected ModelAndView processFinish(HttpServletRequest request,HttpServletResponse arg1, Object command, BindException arg3)throws Exception {

		FilterCourseForm form = (FilterCourseForm) command;

		CourseAlertTriggeerFilter orgGroupAlertFilter = (CourseAlertTriggeerFilter) surveyService
				.getAlertTriggerFilterByID(form.getFilterId());

		Set<Course> selectedOrgGroup = form
				.getCourseListFromDB().stream().filter(course -> Arrays
						.asList(form.getCourses()).contains(course.getId().toString()))
				.collect(Collectors.toSet());

		selectedOrgGroup.addAll(orgGroupAlertFilter.getCourses());
		orgGroupAlertFilter.setCourses(selectedOrgGroup.stream().collect(Collectors.toList()));
		orgGroupAlertFilter.setId(form.getFilterId());
		surveyService.addAlertTriggerFilter(orgGroupAlertFilter);

		return new ModelAndView("redirect:mgr_manageFilter.do?filterId="+form.getFilterId()+"&method=searchEditFilterPageCourse");

	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		log.debug("IN processCancel");
		FilterCourseForm form =(FilterCourseForm)command;
		return new ModelAndView("redirect:mgr_manageFilter.do?filterId="+form.getFilterId()+"&method=searchEditFilterPageCourse");

	}

	protected void onBindAndValidate(HttpServletRequest request,

			Object command, BindException errors, int page) throws Exception {

		FilterCourseForm form = (FilterCourseForm) command;
		
		String searchAction = Optional.ofNullable(request.getParameter("searchAction"))
				 .filter(s -> s != null).orElse("");
		
		if(!searchAction.isEmpty()) {
			form.setAction(searchAction);
		} else {
			form.setAction("");
		}

		super.onBindAndValidate(request, command, errors, page);

	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors,int currentPage) throws Exception {

		FilterCourseForm form =(FilterCourseForm)command;
		
		com.softech.vu360.lms.vo.Customer customerVO = null;
		com.softech.vu360.lms.vo.Distributor distributorVO = null;

		VU360User vu360UserModel = null;

		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) (SecurityContextHolder.getContext()
				.getAuthentication()).getDetails();

		if (details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
			customerVO = (com.softech.vu360.lms.vo.Customer) request.getSession(true)
					.getAttribute("adminSelectedCustomer");
			distributorVO = (com.softech.vu360.lms.vo.Distributor) request.getSession(true)
					.getAttribute("adminSelectedDistributor");
			if (customerVO != null) {
				Long learnerId = learnerService.getLearnerForSelectedCustomer(customerVO.getId());
				vu360UserModel = learnerService.getLearnerByID(learnerId.longValue()).getVu360User();
			} else if (distributorVO != null) {
				Long learnerId = learnerService.getLearnerForSelectDistributor(distributorVO.getMyCustomer().getId());
				vu360UserModel = learnerService.getLearnerByID(learnerId).getVu360User();
			} else {
				vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
			}
		} else if (details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)) {
			vu360UserModel = VU360UserAuthenticationDetails.getCurrentUser();
		}

		if (form.getFilterType().equals(COURSES)) {
			if (form.getAction().equalsIgnoreCase("search")) {
				List<Course> courseList = getSearchCriteriaCourses(vu360UserModel.getLearner().getCustomer().getId(), form.getCourseName(),
						form.getCourseType());
				form.setCourseListFromDB(courseList);
			}
		}
		
		String courseselecteditem;
		Course courseItem;
		int i=0, j = 0;

		if(currentPage==0 && this.getTargetPage(request, currentPage)==0){
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
				form.setSelectedCourseList(form.getCourseListFromDB());
			} else {
				form.setSelectedCourseList(selectedCourseList);
			}
		}
		
		super.postProcessPage(request, command, errors, currentPage);

	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		return super.referenceData(request, command, errors, page);

	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		FilterCourseForm form = (FilterCourseForm) command;
		AddFilterCourseValidator validator = (AddFilterCourseValidator) this.getValidator();
		switch (page) {
			case 0:
				if (!form.getAction().equalsIgnoreCase("Next") && !form.getAction().equalsIgnoreCase("Previous") && !form.getAction().equalsIgnoreCase("ShowAll")) {
					if (!form.getAction().equalsIgnoreCase("search")) {
						if (form.getFilterType().equalsIgnoreCase(COURSES)) {
							validator.validateAddFilterCourse(form, errors);
						}
					}
				}
			default:
				break;
		}

		super.validatePage(command, errors, page, finish);

	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {

		return super.getTargetPage(request, command, errors, currentPage);

	}
	
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

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public static String getCOURSES() {
		return COURSES;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

}