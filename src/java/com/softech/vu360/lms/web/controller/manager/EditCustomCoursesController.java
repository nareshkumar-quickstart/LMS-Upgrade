package com.softech.vu360.lms.web.controller.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;

public class EditCustomCoursesController extends MultiActionController implements
InitializingBean{

	private static final Logger log = Logger.getLogger(EditCustomCoursesController.class.getName());
	private static final String EDIT_CUSTOMCOURSE_SAVENAME_ACTION = "SaveCustomCourse";
	private String editCustomCourseTemplate = null;
	private CourseAndCourseGroupService courseAndCourseGroupService ;
	HttpSession session = null;
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	public ModelAndView editCustomCourse(HttpServletRequest request,
			HttpServletResponse response) {
		try {
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();
		String customCourseId = request.getParameter("Id");
		WebLinkCourse webLinkCourse= null;
		if(StringUtils.isNotBlank(customCourseId)){
			webLinkCourse = courseAndCourseGroupService.getWebLinkCourseByID(Long.parseLong(customCourseId));
			session.setAttribute("webLinkCourseObject", webLinkCourse);
		}
		String action = request.getParameter("action");
		if(StringUtils.isNotBlank(action)){
			if(action.equalsIgnoreCase(EDIT_CUSTOMCOURSE_SAVENAME_ACTION)){
				webLinkCourse = (WebLinkCourse)session.getAttribute("webLinkCourseObject");
				if(!validate(request,context)){
				webLinkCourse.setCourseTitle(request.getParameter("courseTitle").trim());
				webLinkCourse.setCredithour(request.getParameter("creditHour").trim());
				webLinkCourse.setLink(request.getParameter("link").trim());
				webLinkCourse.setDescription(request.getParameter("description").trim());
				courseAndCourseGroupService.SaveCourse(webLinkCourse);
				context.put("redirect", "true");
				log.debug("QQQQQQQQQQQQQQQWWWWWWWWWWWWXXXXXXX"+webLinkCourse.getCourseTitle());
				}else{
					webLinkCourse.setCourseTitle(request.getParameter("courseTitle").trim());
					webLinkCourse.setDescription(request.getParameter("description").trim());
				}
			}
		}	
		context.put("webLinkCourse", webLinkCourse);
		return new ModelAndView(editCustomCourseTemplate,"context",context);
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCustomCourseTemplate);
		
	}
	
	private boolean validate(HttpServletRequest request,Map context){
		boolean check = false;
		if(StringUtils.isBlank(request.getParameter("courseTitle"))){
			context.put("validateName","error.editCustomCourse.validateCustomCourseTitleBlank");
			check = true;
		}
		if(StringUtils.isBlank(request.getParameter("link"))){
			context.put("validateLink","error.editCustomCourse.validateCustomCourseLinkBlank");
			check = true;
		}else{
			String expression = "^(https?://(.*?)\\.(.*))$";
			CharSequence inputStr = request.getParameter("link");
			{
				//Make the comparison case-insensitive. 
				Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(inputStr);
				if(matcher.matches()){
					//do nothing
				}else{
					context.put("validateLink", "error.manager.addCustomCourses.link.invalid");
					check = true;
				}
			}
		}
		return check;
	}
	
	public String getEditCustomCourseTemplate() {
		return editCustomCourseTemplate;
	}
	public void setEditCustomCourseTemplate(String editCustomCourseTemplate) {
		this.editCustomCourseTemplate = editCustomCourseTemplate;
	}
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

}