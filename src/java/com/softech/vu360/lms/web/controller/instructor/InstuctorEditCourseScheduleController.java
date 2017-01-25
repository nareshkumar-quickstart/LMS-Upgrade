package com.softech.vu360.lms.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;

public class InstuctorEditCourseScheduleController extends VU360BaseMultiActionController
{
	private String closeTemplate=null;
	private String summaryTabTemplate=null;
	private String schedulingTabTemplate=null;
	private String resourcesTabTemplate=null;
	private String instructorTabTemplate=null;
	private String addLocationTemplate=null;
	private CourseAndCourseGroupService courseAndCourseGroupService;	
	private SynchronousClassService synchronousClassService = null;
	private VelocityEngine velocityEngine;
	
//	private static final Logger log = Logger.getLogger(InstuctorEditCourseScheduleController.class.getName());
	
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ModelAndView viewScheduleSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		
		return new ModelAndView(summaryTabTemplate);
	}
	
	public ModelAndView saveUpdateScheduleSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(closeTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView viewScheduleScheduling( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(schedulingTabTemplate);
	}
	public ModelAndView saveUpdateScheduleScheduling( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(closeTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView viewScheduleResources( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(resourcesTabTemplate);
	}
	public ModelAndView saveUpdateScheduleResources( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(closeTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView viewScheduleInstructor( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(instructorTabTemplate);
	}
	public ModelAndView saveUpdateScheduleInstructor( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		
		return new ModelAndView(closeTemplate);
	}
	@SuppressWarnings("unchecked")
	public ModelAndView changeLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
	
		return new ModelAndView(addLocationTemplate);
	}
	
	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public String getSummaryTabTemplate() {
		return summaryTabTemplate;
	}

	public void setSummaryTabTemplate(String summaryTabTemplate) {
		this.summaryTabTemplate = summaryTabTemplate;
	}

	public String getSchedulingTabTemplate() {
		return schedulingTabTemplate;
	}

	public void setSchedulingTabTemplate(String schedulingTabTemplate) {
		this.schedulingTabTemplate = schedulingTabTemplate;
	}

	public String getResourcesTabTemplate() {
		return resourcesTabTemplate;
	}

	public void setResourcesTabTemplate(String resourcesTabTemplate) {
		this.resourcesTabTemplate = resourcesTabTemplate;
	}

	public String getInstructorTabTemplate() {
		return instructorTabTemplate;
	}

	public void setInstructorTabTemplate(String instructorTabTemplate) {
		this.instructorTabTemplate = instructorTabTemplate;
	}
	
	public String getAddLocationTemplate() {
		return addLocationTemplate;
	}

	public void setAddLocationTemplate(String addLocationTemplate) {
		this.addLocationTemplate = addLocationTemplate;
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
	
	

}
