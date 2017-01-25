package com.softech.vu360.lms.web.controller.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddCustomCoursesForm;
import com.softech.vu360.lms.web.controller.validator.AddCustomCoursesValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.GUIDGeneratorUtil;

public class AddCustomCoursesWizardController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddCustomCoursesWizardController.class.getName());
	private String closeTemplate = null;
	private String cancelTemplate = null;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	private EntitlementService entitlementService;

	public AddCustomCoursesWizardController() {
		super();
		setCommandName("addCustomCoursesForm");
		setCommandClass(AddCustomCoursesForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/customCourses/addCustomCourses"

		});
	}

	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			customer = details.getCurrentCustomer();
		}
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		WebLinkCourse webLinkCourse=new WebLinkCourse();
		webLinkCourse.setCourseGUID(GUIDGeneratorUtil.generateGUID());
		webLinkCourse.setCode(webLinkCourse.getCourseGUID());
		AddCustomCoursesForm addCustomCoursesForm = (AddCustomCoursesForm)command;
		String name=addCustomCoursesForm.getCourseName();
		webLinkCourse.setCourseTitle(addCustomCoursesForm.getCourseName().trim());
		webLinkCourse.setCredithour(addCustomCoursesForm.getHours().trim());
		webLinkCourse.setLink(addCustomCoursesForm.getLink().trim());
		webLinkCourse.setDescription(addCustomCoursesForm.getDescription().trim());
		/*
		 * Since there is no concept of publishing either on VU or storefront 
		 * therefore these type of courses should have default status published instead of Null.
		 */
		webLinkCourse.setCourseStatus(webLinkCourse.PUBLISHED);
		/*
		 * Provide default value Course Name in Keyword field from LMS application for Custom Course.  
		 */
		webLinkCourse.setKeywords(name);
		/*
		 * all courses with published status should be visible in LMS with the 
		 * values of published on VU or published on Store Front or both as 1.
		 */
		webLinkCourse.setPublishedonstorefront(true);
		webLinkCourse.setPublishOnVU(true);
		
		VU360User vu360UserModel = new VU360User();
		vu360UserModel.setId(loggedInUser.getId());
		
		courseAndCourseGroupService.SaveWeblinkCourse(webLinkCourse,customer,vu360UserModel);
		return new ModelAndView(closeTemplate);
	}

	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage");
		AddCustomCoursesForm customCoursesDetails = (AddCustomCoursesForm)command;
		AddCustomCoursesValidator addCustomerValidator = (AddCustomCoursesValidator) getValidator();

		errors.setNestedPath("");
		switch (page) {

		case 0:
			addCustomerValidator.validate(customCoursesDetails, errors);
			break;

		case 1:

			break;

		default:
			break;
		}
		errors.setNestedPath("");
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(cancelTemplate);
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}


	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}


	public String getCancelTemplate() {
		return cancelTemplate;
	}


	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
}
