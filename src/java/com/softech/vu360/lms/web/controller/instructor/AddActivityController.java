package com.softech.vu360.lms.web.controller.instructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.softech.vu360.lms.model.AssignmentCourseActivity;
import com.softech.vu360.lms.model.AttendanceCourseActivity;
import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.FinalScoreCourseActivity;
import com.softech.vu360.lms.model.GeneralGradedCourseActivity;
import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.model.instructor.AddActivityForm;

/**
 * @author Dyutiman created on 1 Apr 2010
 *
 */
@Controller
@RequestMapping("/ins_addActivity.do")
public class AddActivityController {

	private String finishTemplate = null;
	
	@Autowired
	private ResourceService resourceService;
	
	private static final Logger log = Logger.getLogger(AddActivityController.class.getName());
	private final String[] activityTypes = { CourseActivity.ASSIGNMENT_ACTIVITY, CourseActivity.ATTENDANCE_ACTIVITY }; // LMS-16530

	private static final String STEP_1 = "instructor/instructorTool/Activity/addActivity/addActivityStep1";
	private static final String STEP_2 = "instructor/instructorTool/Activity/addActivity/addActivityStep2";
	private static final String STEP_REDIRECT = "instructor/instructorTool/Activity/addActivity/finish_redirect";
	
	AddActivityForm addActivityFormCommand = new AddActivityForm();
	private static final String addActivityCommandName = "addActivityForm";
	
	
	public AddActivityController() {
		
	}

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap modelMap, HttpServletRequest request) {

		try {
			String gradebookId = request.getParameter("Id");
			this.addActivityFormCommand.setGradebookId(gradebookId);
		} catch (Exception e) {
			// log.debug("exception", e);
		}

		modelMap.put("addActivityForm", addActivityFormCommand);
		return STEP_1;

	}

	@ModelAttribute("activityTypes")
	public String[] referenceDataActivityTypes() {

		return this.activityTypes;
	}
	
	@ModelAttribute("addActivityForm")
	public AddActivityForm formBackingObject(final HttpServletRequest request) throws Exception{
	    //final AddActivityForm userProfileForm = new AddActivityForm();
		try {
			String gradebookId = request.getParameter("Id");
			addActivityFormCommand.setGradebookId(gradebookId);
		} catch ( Exception e ) {
			//log.debug("exception", e);
		}
		
	    return this.addActivityFormCommand; 
	}
	
	/*protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		AddActivityForm form = (AddActivityForm)command;
		try {
			String gradebookId = request.getParameter("Id");
			form.setGradebookId(gradebookId);
		} catch ( Exception e ) {
			//log.debug("exception", e);
		}
		return command;
	}*/
	
	

	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {

		AddActivityForm form = (AddActivityForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("grdBkId", form.getGradebookId());
		return new ModelAndView(finishTemplate, "context", context);
	}

	/**
	 * Set a form validator
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// binder.setValidator(userFormValidator);
		//System.out.println("......initBinder.......");
		
	}

	/**
	 * Maybe you want to be provided with the _page parameter (in order to map
	 * the same method for all), as you have in AbstractWizardFormController.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processPage(final HttpServletRequest request, @RequestParam("_page") final int currentPage,
			final @ModelAttribute("addActivityForm") AddActivityForm command, final BindingResult result,
			final HttpServletResponse response) {
		
		this.addActivityFormCommand = command;
		
		//System.out.println("......................processPage.......................");
		
		ModelAndView mv = new ModelAndView();
		
		int targetPage = WebUtils.getTargetPage(request, "_target", currentPage);
		
		switch (targetPage) {
		
			case 0:
				//System.out.println(".............Target Page: 0 ................");
				mv.setViewName(STEP_1);
				break;
	
			case 1:
				//System.out.println(".............Target Page: 1 ................");
				mv.setViewName(STEP_2);
				break;

			default:
				//System.out.println(".............Target Page: default ................");
				mv.setViewName(STEP_1);
				break;
		}
		
		
		
		
		
		mv.addObject("addActivityForm", command);
		
		return mv;
	}

	@RequestMapping(params = "_cancel")
	public String processCancel(final HttpServletRequest request, final HttpServletResponse response,
			final @ModelAttribute("addActivityForm") AddActivityForm command, final SessionStatus status) {
		
		//System.out.println("......................Cancel.......................");
		
		status.setComplete();

		AddActivityForm form = (AddActivityForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("grdBkId", form.getGradebookId());
		// return new ModelAndView(finishTemplate, "context", context);

		return STEP_REDIRECT;
	}

	@RequestMapping(params = "_finish")
	public ModelAndView processFinish(final HttpServletRequest request, final HttpServletResponse response,
			final SessionStatus status, final @ModelAttribute("addActivityForm") AddActivityForm command) {
		
		//System.out.println("......................Finish.......................");
		
		status.setComplete();

		AddActivityForm form = (AddActivityForm) command;
		try {
			CourseActivity activity = null;
			if (form.getType().equalsIgnoreCase(CourseActivity.FINAL_SCORE_COURSE_ACTIVITY)) {
				activity = new FinalScoreCourseActivity();
			} else if (form.getType().equalsIgnoreCase(CourseActivity.ASSIGNMENT_ACTIVITY)) {
				activity = new AssignmentCourseActivity();
			} else if (form.getType().equalsIgnoreCase(CourseActivity.GENERAL_GRADED_ACTIVITY)) {
				activity = new GeneralGradedCourseActivity();
			} else if (form.getType().equalsIgnoreCase(CourseActivity.ATTENDANCE_ACTIVITY)) {
				activity = new AttendanceCourseActivity();
			}

			activity.setActivityName(form.getName());
			activity.setDescription(form.getDesc());
			activity.setActivityScore(0);

			Gradebook gradebook = resourceService.getGradeBookById(Long.parseLong(form.getGradebookId()));
			// gradebook.setId(Long.parseLong(form.getGradebookId()));
			activity.setGradeBook(gradebook);

			List<CourseActivity> actvtyList = resourceService.findCourseActivitiesByGradeBook(gradebook.getId());
			activity.setDisplayOrder(actvtyList.size());

			log.debug("Desc - " + form.getDesc());
			resourceService.saveCourseActivity(activity);
		} catch (Exception e) {
			// do nothing
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("grdBkId", form.getGradebookId());
		// return new ModelAndView(finishTemplate, "context", context);
		return new ModelAndView(STEP_REDIRECT, "context", context);

	}

	@RequestMapping(value = "/cancel")
	@ResponseBody
	public String cancelAction() {
		return "instructor/instructorTool/Activity/addActivity/addActivityStep1";
	}

	@RequestMapping(params = "_target")
	@ResponseBody
	public String step2Action() {
		//System.out.println("......................_target.......................");
		// System.out.println("i="+i);
		return "instructor/instructorTool/Activity/addActivity/addActivityStep2";
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

}