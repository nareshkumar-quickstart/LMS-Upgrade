/**
 * 
 */
package com.softech.vu360.lms.web.controller.instructor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.CourseForm;
import com.softech.vu360.lms.web.controller.validator.AddSynchronosCourseValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;

/**
 * @author Noman
 *
 */


public class AddSynchronousCourseController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddSynchronousCourseController.class.getName());
	
	private CourseAndCourseGroupService courseAndCourseGroupService;
	
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;
	

	public AddSynchronousCourseController() {
		super();
		setCommandName("courseForm");
		setCommandClass(CourseForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageSynchronousCourse/addCourse/Step1",
				"instructor/manageSynchronousCourse/addCourse/Step2"});
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
		log.debug("IN formBackingObject in addSynchrounous course ");
		Object command = super.formBackingObject(request);
		CourseForm courseForm = (CourseForm)command;

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
		CourseForm courseForm = (CourseForm)command;
		Map model = new HashMap();
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
		CourseForm courseForm = (CourseForm)command;

		AddSynchronosCourseValidator addSynCourseValidator = (AddSynchronosCourseValidator) getValidator();

		errors.setNestedPath("");
		switch (page) {

		case 0:
			addSynCourseValidator.validatePage1(courseForm, errors);
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
		CourseForm courseForm = (CourseForm)command;
		
		int Page=getCurrentPage(request);
		if (Page==1){
			if	(request.getParameter("selectedLearnerGroups")==null){
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
		CourseForm courseForm = (CourseForm)command;
		
		Course newCourse = new SynchronousCourse();
		newCourse.setCourseTitle(courseForm.getCourseName().trim());
		//TODO setName added from branch 4.5.2
		newCourse.setName(courseForm.getCourseName().trim());
		newCourse.setDescription(courseForm.getDescription().trim());
		newCourse.setKeywords(courseForm.getCourseName().trim());
		newCourse.setCourseStatus(Course.PUBLISHED);
		newCourse.setCourseTitle(courseForm.getCourseName().trim());

		newCourse.setCourseId(courseForm.getCourseID().trim());
		// FAS: missing functionality
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();		
		newCourse.setContentOwner(loggedInUser.getContentOwner());
		
		courseAndCourseGroupService.addCourse(newCourse);
		
		return new ModelAndView(closeTemplate);
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

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

}
