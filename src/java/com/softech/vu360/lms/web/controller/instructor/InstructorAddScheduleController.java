package com.softech.vu360.lms.web.controller.instructor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;

public class InstructorAddScheduleController extends
		AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddCourseController.class.getName());	
	private CourseAndCourseGroupService courseAndCourseGroupService;	
	private SynchronousClassService synchronousClassService;	
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;	

	
	public InstructorAddScheduleController() {
		super();
		setCommandName("scheduleForm");
		setCommandClass(com.softech.vu360.lms.web.controller.model.ScheduleForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageSynchronousCourse/addSchedule/inst_addSchedule_Summary",
				"instructor/manageSynchronousCourse/addSchedule/inst_addSchedule_RecurrencePattern",
				"instructor/manageSynchronousCourse/addSchedule/inst_addSchedule_Location"});
	}
	
	@Override
	protected int getTargetPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) {
		// TODO Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	@Override
	protected Map referenceData(HttpServletRequest request, int page)
			throws Exception {
		
		return super.referenceData(request, page);
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		return super.showForm(request, response, errors);
	}

	@Override
	protected void validatePage(Object command, Errors errors, int page,
			boolean finish) {
		// TODO Auto-generated method stub
		super.validatePage(command, errors, page, finish);
	}

	@Override
	protected ModelAndView processFinish(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
		
	@Override
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		return super.processCancel(request, response, command, errors);
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}


}
