/**
 * 
 */
package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.service.InstructorService;
import com.softech.vu360.lms.web.controller.model.instructor.InstructorItemForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * @author sana.majeed
 * [5/3/2010] For VCS-266: Associate Instructor with Courses (specially for DFC type)
 */
public class ManageCourseInstructorController extends MultiActionController implements InitializingBean {
	
	private static final Logger log = Logger.getLogger(ManageCourseInstructorController.class.getName()) ;
	private InstructorService instructorService = null;
	private String manageCourseInstructorTemplate = null;
	private String editCourseInstructorTypeTemplate = null;
	private String closeTemplate = null;
	
	private static final int SEARCH_RESULT_PAGE_SIZE = 10;	

	private static final String MANAGE_USER_SHOW_ALL_ACTION = "showAll";
	private static final String MANAGE_USER_SHOW_NEXT_ACTION = "showNext";
	private static final String MANAGE_USER_SHOW_PREV_ACTION = "showPrev";
	private static final String MANAGE_USER_SHOW_DEFAULT_ACTION = "showNone";
	private static final String MANAGE_USER_DELETE_ACTION = "delete";
	
	private static final String[] INSTRUCTOR_TYPES = {"All", "Lead","Moderator","Teaching Assistant"};
	
	//search criteria
	private Long courseId = null;	
	private String action = MANAGE_USER_SHOW_DEFAULT_ACTION;
	
	private String searchFirstName = null;
	private String courseType = null;
	private String searchLastName = null;
	private String instructorType = null;
	private String instructorCourseType = null;	
		
	//sorting and paging items	
	private String sortColumn = "firstName";
	private int sortDirection = 0;
	private int searchResultsPageSize = 0;
	private int pageIndex = 0;
	
	private int totalRecords = -1;
	private int recordShowing = 0;
	private List<InstructorItemForm> instructors = null;
		
	
	/**
	 * SearchCourseInstructor - list all the associated course instructor based on the given search criteria
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView searchCourseInstuctor (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN searchCourseInstuctor");
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		// Get request parameters
		this.courseId = new Long( request.getParameter("id") );
		this.searchFirstName = request.getParameter("searchFirstName");
		this.searchLastName = request.getParameter("searchLastName");
		this.instructorCourseType = request.getParameter("instructorCourseType");
		this.instructorType = request.getParameter("instructorType");
		this.courseType = request.getParameter("courseType");
		
		this.action = request.getParameter("action");
		this.sortColumn = request.getParameter("sortColumn");
		
		String curSortDirection = request.getParameter("sortDirection");
		this.sortDirection = (curSortDirection == null) ? 0 : Integer.parseInt( curSortDirection );
		String curPageIndex = request.getParameter("pageIndex");
		this.pageIndex = (curPageIndex == null) ? 0 : Integer.parseInt( curPageIndex );
		
		// Setting default values in case of null
		this.setDefaultValues();
		
		if ( this.action.equals(MANAGE_USER_DELETE_ACTION) ) {
			String[] associationIds = request.getParameterValues("selectedInstructors");
			
			if ( (associationIds != null) && (associationIds.length > 0) ) {
				if ( !this.deleteCourseInstructors(associationIds) ) {
					request.setAttribute("errors", "error.instructor.manageCourseInstructor.dfcCourse.delete.failed");
				}
			}
			// reset back to search action to display updated results
			this.action = "advanceSearch";
		}
		
		if( this.action.equals(MANAGE_USER_SHOW_DEFAULT_ACTION) ) {
			request.setAttribute("newPage", "true");		
		}		
		else {
			log.debug("IN searchInstructor");
			
			// Override parameters based on search type		
			if ( this.action.equals(MANAGE_USER_SHOW_ALL_ACTION) ) {
				Brander brander = VU360Branding.getInstance().getBrander(
						(String) request.getSession().getAttribute(VU360Branding.BRAND),
						new com.softech.vu360.lms.vo.Language());
				String showLimit = brander.getBrandElement("lms.resultSet.showAll.Limit").trim();
				this.searchResultsPageSize = Integer.parseInt(showLimit);
				this.pageIndex = 0;				
			}
			else if ( this.action.equals(MANAGE_USER_SHOW_PREV_ACTION) ) {
				this.pageIndex = this.pageIndex <= 0 ? 0 : (this.pageIndex - 1);				
			}
			else if ( this.action.equals(MANAGE_USER_SHOW_NEXT_ACTION) ) {
				this.pageIndex = this.pageIndex < 0 ? 0 : (this.pageIndex + 1);				
			}
			
			Map<Object, Object> result = this.instructorService.getAssociatedCourseInstructors(this.courseId, this.searchFirstName, this.searchLastName, (this.instructorType.equals("All") ? null : this.instructorType) , this.pageIndex, this.searchResultsPageSize, this.sortDirection, this.sortColumn);
			if (!result.isEmpty()) {
				
				List<InstructorCourse> courseInstructorList = (List<InstructorCourse>) result.get("courseInstructorList");
				this.instructors = this.instructorService.getInstructorItemListFromCourseInstructors(courseInstructorList);
				
				this.totalRecords = Integer.parseInt( result.get("totalRecords").toString() );
				
				if ( this.action.equals(MANAGE_USER_SHOW_ALL_ACTION) ) {
					this.recordShowing = this.instructors.size();
				}
				else {
					this.recordShowing = this.instructors.size() < SEARCH_RESULT_PAGE_SIZE ? this.totalRecords : (this.pageIndex + 1) * SEARCH_RESULT_PAGE_SIZE;
				}
			}			
		}
		
		// Send values back to the page
		context.put("courseId", this.courseId);
		context.put("action", this.action);
		context.put("courseType", this.courseType);
		context.put("courseType", this.courseType);
		
		context.put("lstIntructorTypes", INSTRUCTOR_TYPES);
		context.put("searchFirstName", this.searchFirstName);
		context.put("searchLastName", this.searchLastName);
		context.put("instructorType", this.instructorType);
		context.put("instructorCourseType", this.instructorCourseType);
		
		context.put("sortColumn", this.sortColumn);
		context.put("sortDirection", this.sortDirection);
		context.put("pageIndex", this.pageIndex);
				
		context.put("searchResultsPageSize", this.searchResultsPageSize);
		context.put("totalRecords", this.totalRecords);
		context.put("recordShowing", this.recordShowing);
		context.put("instructors", this.instructors);
					
		return new ModelAndView(manageCourseInstructorTemplate, "context", context);
	}
	
		
	public ModelAndView editCourseInstuctorType (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN editCourseInstuctorType");
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		// Get request parameters
		this.courseId = new Long( request.getParameter("id") );
		Long associationId = new Long( request.getParameter("associationId") );
		this.instructorCourseType = request.getParameter("instructorCourseType");
		
		InstructorCourse courseInstructor = this.instructorService.getCourseInstructorInfoById(associationId);
		Course course = courseInstructor.getCourse();
		if(course instanceof InstructorConnectCourse)
		{
			if (((InstructorConnectCourse) course).getInstructorType().equals("Email Online"))
			{
				this.instructorCourseType = "Email Online";
				this.courseType = course.getCourseType();
			}
		}
		
		context.put("courseId", this.courseId);
		context.put("associationId", associationId);
		context.put("courseInstructor", courseInstructor);
		context.put("instructorCourseType", this.instructorCourseType);
		context.put("courseType", this.courseType);
		
		return new ModelAndView(editCourseInstructorTypeTemplate, "context", context);		
	}
	
	public ModelAndView cancelCourseInstuctorType (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN cancelCourseInstuctorType");
		Map<Object, Object> context = new HashMap<Object, Object>();
		this.courseId = new Long( request.getParameter("id") );
		Long associationId = new Long( request.getParameter("associationId") );
		this.instructorCourseType = request.getParameter("instructorCourseType");
		
		// Get request parameters
		
		
		InstructorCourse courseInstructor = this.instructorService.getCourseInstructorInfoById(associationId);
		Course course = courseInstructor.getCourse();
		if(course instanceof InstructorConnectCourse)
		{
			if (((InstructorConnectCourse) course).getInstructorType().equals("Email Online"))
			{
				this.instructorCourseType = "Email Online";
				this.courseType = course.getCourseType();
			}
		}
		
		//this.instructorService.get
		
		// Fill out following information required for closeTemplate
		context.put("courseId", this.courseId);
		context.put("action", "advanceSearch");
		context.put("instructorCourseType", this.instructorCourseType);
		context.put("courseType", this.courseType);
		
		return new ModelAndView(closeTemplate, "context", context);		
	}
	
	public ModelAndView saveCourseInstuctorType (HttpServletRequest request, HttpServletResponse response) {
		
		log.info("IN saveCourseInstuctorType");
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		// Get request parameters
		this.courseId = new Long( request.getParameter("id") );
		Long associationId = new Long( request.getParameter("associationId") );
		this.instructorType = request.getParameter("instructorType");
		
		// Update instructor type
		this.instructorService.updateCourseInstructorRole(associationId, this.instructorType);
		
		// Fill out following information required for closeTemplate and return to search page
		context.put("courseId", this.courseId);
		context.put("action", "advanceSearch");
		
		return new ModelAndView(closeTemplate, "context", context);
	}
	
	private boolean deleteCourseInstructors (String[] associationIds) {
		
		Long courseInstructorIds[] = new Long[associationIds.length];
		String error = "";
		for (int index = 0; index < associationIds.length; index++ ) {
			courseInstructorIds[index] = new Long (associationIds[index]);
		}
		
		return this.instructorService.deleteCourseInstructors(courseInstructorIds);
	}
	
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}
	
	
	private void setDefaultValues () {		
		// Setting default values in case of null
		this.action = (this.action == null) ? MANAGE_USER_SHOW_DEFAULT_ACTION : this.action;
		this.instructorType = (this.instructorType == null) ? "All" : this.instructorType;
		this.sortColumn = (this.sortColumn == null) ? "firstName" : this.sortColumn;
		this.sortDirection = (this.sortDirection <= 0) ? 0 : this.sortDirection;
		this.searchResultsPageSize = SEARCH_RESULT_PAGE_SIZE;
		this.pageIndex = (this.pageIndex <= 0) ? 0 : this.pageIndex;
		this.totalRecords = -1;
		this.recordShowing = 0;	
		this.instructors = new ArrayList<InstructorItemForm>();
	}

		/**
	 * @param manageCourseInstructorTemplate the manageCourseInstructorTemplate to set
	 */
	public void setManageCourseInstructorTemplate(
			String manageCourseInstructorTemplate) {
		this.manageCourseInstructorTemplate = manageCourseInstructorTemplate;
	}


	/**
	 * @return the manageCourseInstructorTemplate
	 */
	public String getManageCourseInstructorTemplate() {
		return manageCourseInstructorTemplate;
	}


	/**
	 * @param instructorService the instructorService to set
	 */
	public void setInstructorService(InstructorService instructorService) {
		this.instructorService = instructorService;
	}


	/**
	 * @return the instructorService
	 */
	public InstructorService getInstructorService() {
		return instructorService;
	}


	/**
	 * @param instructors the instructors to set
	 */
	public void setInstructors(List<InstructorItemForm> instructors) {
		this.instructors = instructors;
	}


	/**
	 * @return the instructors
	 */
	public List<InstructorItemForm> getInstructors() {
		return instructors;
	}


	/**
	 * @param editCourseInstructorTypeTemplate the editCourseInstructorTypeTemplate to set
	 */
	public void setEditCourseInstructorTypeTemplate(
			String editCourseInstructorTypeTemplate) {
		this.editCourseInstructorTypeTemplate = editCourseInstructorTypeTemplate;
	}


	/**
	 * @return the editCourseInstructorTypeTemplate
	 */
	public String getEditCourseInstructorTypeTemplate() {
		return editCourseInstructorTypeTemplate;
	}


	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}


	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}
	
	
}