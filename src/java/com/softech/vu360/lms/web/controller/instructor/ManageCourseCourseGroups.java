/**
 * 
 */
package com.softech.vu360.lms.web.controller.instructor;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.util.TreeNode;

/**
 * @author sana.majeed
 *
 */
public class ManageCourseCourseGroups extends MultiActionController {
	
	private static final Logger log = Logger.getLogger( ManageCourseCourseGroups.class.getName() );
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	
	private String manageCourseCourseGroupsTemplate = null;
	private String searchCourseGroupsForCourseTemplate = null;
	
	private static final String MANAGE_USER_REMOVE_ACTION = "remove";
	private static final String MANAGE_USER_SEARCH_ACTION = "search";
	
	// Search Criteria
	private Long courseId = null;
	private String instructorType = null;	
	
	public ModelAndView manageCourseCourseGroups (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN manageCourseCourseGroups");
		HashMap<Object, Object> context = new HashMap<Object, Object>();
		
		// Get request parameters
		this.courseId = new Long( request.getParameter("id") );
		String action = request.getParameter("action");
		
		if ( StringUtils.isNotBlank(action) && action.equals(MANAGE_USER_REMOVE_ACTION) ) {
			
			String[] selectedIds = request.getParameterValues("courseGroups");
			if ( (selectedIds != null) && (selectedIds.length > 0) ) {
				if ( ! this.removeCourseGroupRelationship(selectedIds) ) {
					request.setAttribute("errors", "error.instructor.manageCourse.courseGroup.delete.failed");					
				}
			}
			action = null;
		}
		
		// Get Course Type
		Course course = this.courseAndCourseGroupService.getCourseById( this.courseId );		
		
		 if(course instanceof InstructorConnectCourse)
		    {
		    	InstructorConnectCourse dbcourse = (InstructorConnectCourse) course;
		    	instructorType = dbcourse.getInstructorType();
		    }
		
		// Get Course Groups for this course
		List<CourseGroup> courseGroupList = this.courseAndCourseGroupService.getCourseGroupsForCourse( course );
		
		// Create Tree for display
		List<List<TreeNode>> courseGroupTreeList = this.courseAndCourseGroupService.getCourseGroupTreeList( courseGroupList, false );
		
		// Send values back to the page
		context.put("courseId", this.courseId);
		context.put("courseType", course.getCourseType() );
		context.put("courseIsRetired", course.isRetired());  // [1/27/2011] LMS-7183 :: Retired Course Functionality II
		context.put("action", action );
		context.put("courseGroupTreeList", courseGroupTreeList);
		context.put("instructorType", instructorType);
		
		return new ModelAndView(this.manageCourseCourseGroupsTemplate, "context", context);
	}	
	
	private boolean removeCourseGroupRelationship (String[] cgIds) {
		
		Long courseGroupIds[] = new Long[cgIds.length];		
		for (int index = 0; index < cgIds.length; index++ ) {
			courseGroupIds[index] = new Long (cgIds[index]);
		}		
		
		return this.courseAndCourseGroupService.removeCourseCourseGroupRelationship(this.courseId, courseGroupIds);
	}
	
	public ModelAndView searchCourseGroupsForCourse (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN searchCourseGroupsForCourse");
		HashMap<Object, Object> context = new HashMap<Object, Object>();
		
		// Get request parameters
		this.courseId = new Long( request.getParameter("id") );		
		String action = request.getParameter("action");
		String searchGroupName = request.getParameter("searchGroupName");
		String searchKeyword = request.getParameter("searchKeyword");
		
		if ( StringUtils.isNotBlank(action) && action.equals(MANAGE_USER_SEARCH_ACTION) ) {
			
			// [LMS-7106] Get Content Owner			
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			com.softech.vu360.lms.vo.ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
			if (contentOwner == null) {
				contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
			}
			
			// Get Course Groups for current Content Owner
			List<CourseGroup> courseGroupList = this.getCourseAndCourseGroupService().getCourseGroupsByContentOwner(contentOwner.getId(), searchGroupName, searchKeyword);
			
			// Create Tree for display
			List<List<TreeNode>> courseGroupTreeList = this.courseAndCourseGroupService.getCourseGroupTreeList( courseGroupList, false );
			context.put("courseGroupTreeList", courseGroupTreeList);			
		}
		
		// Send values back to the page
		context.put("courseId", this.courseId);		
		context.put("action", action);
		context.put("searchGroupName", (searchGroupName == null) ? "" : searchGroupName);
		context.put("searchKeyword", (searchKeyword == null) ? "" : searchKeyword);
		
		return new ModelAndView(this.searchCourseGroupsForCourseTemplate, "context", context);
	}
	
	
	public ModelAndView addCourseGroupsForCourse (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN addCourseGroupsForCourse");
		HashMap<Object, Object> context = new HashMap<Object, Object>();
		
		// Get request parameters
		this.courseId = new Long( request.getParameter("id") );
		context.put("courseId", this.courseId);
		
		String[] selectedIds = request.getParameterValues("courseGroups");
		
		if ( (selectedIds != null) && (selectedIds.length > 0) ) {
			if ( ! this.addCourseGroupRelationship(selectedIds) ) {
				request.setAttribute("errors", "error.instructor.manageCourse.addCourseGroup.add.failed");
				
				return new ModelAndView(this.searchCourseGroupsForCourseTemplate, "context", context);
			}
		}
		
		return new ModelAndView("redirect:/ins_manageCourseCourseGroups.do?id=" + this.courseId);
	}
	
	private boolean addCourseGroupRelationship (String[] cgIds) {
		
		Long courseGroupIds[] = new Long[cgIds.length];		
		for (int index = 0; index < cgIds.length; index++ ) {
			courseGroupIds[index] = new Long (cgIds[index]);
		}		
		
		return this.courseAndCourseGroupService.addCourseCourseGroupRelationship(this.courseId, courseGroupIds);
	}
	
	
	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}
	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}


	/**
	 * @param manageCourseCourseGroupsTemplate the manageCourseCourseGroupsTemplate to set
	 */
	public void setManageCourseCourseGroupsTemplate(
			String manageCourseCourseGroupsTemplate) {
		this.manageCourseCourseGroupsTemplate = manageCourseCourseGroupsTemplate;
	}


	/**
	 * @return the manageCourseCourseGroupsTemplate
	 */
	public String getManageCourseCourseGroupsTemplate() {
		return manageCourseCourseGroupsTemplate;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}


	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @param searchCourseGroupsForCourseTemplate the searchCourseGroupsForCourseTemplate to set
	 */
	public void setSearchCourseGroupsForCourseTemplate(
			String searchCourseGroupsForCourseTemplate) {
		this.searchCourseGroupsForCourseTemplate = searchCourseGroupsForCourseTemplate;
	}

	/**
	 * @return the searchCourseGroupsForCourseTemplate
	 */
	public String getSearchCourseGroupsForCourseTemplate() {
		return searchCourseGroupsForCourseTemplate;
	}

}
