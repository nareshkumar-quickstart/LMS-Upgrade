package com.softech.vu360.lms.web.controller.instructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.util.CourseSort;

/**
 * @author Dyutiman
 *
 */
public class ManageCourseGroupSynchronousCoursesController extends MultiActionController implements InitializingBean{

	private static final String MANAGE_COURSE_PAGE_SEARCH_ACTION = "pageSearch";
	private static final String CHANGE_GROUP_ALL_SEARCH_ACTION = "allSearch";
	private static final String MANAGE_COURSE_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final int MANAGE_COURSES_PAGE_SIZE = 10;
	private static final String MANAGE_COURSE_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_COURSE_NEXTPAGE_DIRECTION = "next";
	private static final int MANAGE_COURSE_PAGE_SIZE =10;
	private static final String MANAGE_COURSES_UPDATECOURSE_ACTION = "update";
	private static final String MANAGE_COURSES_DELETE_COURSE_ACTION = "delete";
	private String[] courseTypes = {"All", "Discussion Forum", "SCORM Package", SynchronousCourse.COURSE_TYPE, "Weblink Course", WebinarCourse.COURSE_TYPE};
	
	private static final Logger log = Logger.getLogger(ManageCourseGroupSynchronousCoursesController.class.getName());	
	private CourseAndCourseGroupService courseAndCourseGroupService =null ;

	private String viewCourseGroupCoursesTemplate;
	private String viewCoursesToAddInCourseGroupTemplate;
	private String viewCourseGroupsRedirectTemplate;
	
	private static final String ACTION_SEARCH_NONE = "showNone";
	private static final String ACTION_SHOW_ALL = "showAll";
	private static final String ACTION_SHOW_PREV = "showPrev";
	private static final String ACTION_SHOW_NEXT = "showNext";	
	 
	public ModelAndView showCoursesForCourseGroup(HttpServletRequest request,
			HttpServletResponse response){
				
		try {
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			HttpSession session = request.getSession();

			String courseGroupId = request.getParameter("courseGroupId");
			String sortDirection = request.getParameter("sortDirection");
			String sortBy = request.getParameter("sortBy");
			String direction = request.getParameter("direction");
			String searchType = request.getParameter("searchType");
			String pageIndex = request.getParameter("pageIndex");
			String prevAct = request.getParameter("prevAct");
			if( prevAct == null ) prevAct = "";

			session.setAttribute("courseGroupId",courseGroupId);
			int pageNo = 0;
			int recordShowing = 0;

			if( sortDirection == null ) {
				if( session.getAttribute("courseGroupSortDirection") == null ) {
					sortDirection = "0";
				}
				else{
					sortDirection=session.getAttribute("courseGroupSortDirection").toString();
				}
			}
			searchType = (searchType == null) ? MANAGE_COURSE_PAGE_SEARCH_ACTION : searchType;
			sortBy = (sortBy == null) ? "courseTitle" : sortBy;
			pageIndex = (pageIndex==null) ? "0" : pageIndex;
			direction = (direction == null) ? "prev" : direction;

			if( courseGroupId != null ) {

				if( session.getAttribute("prevSortBy") != null && session.getAttribute("prevAct") != null && 
						session.getAttribute("prevSortBy").toString().equalsIgnoreCase(sortBy) &&
							( session.getAttribute("prevAct").toString().equalsIgnoreCase("PrevNext") || 
								session.getAttribute("prevAct").toString().equalsIgnoreCase("All") ) )
				{
					if (sortDirection.equalsIgnoreCase("0"))
						sortDirection = "1";
					else
						sortDirection="0";
				}

				String action = request.getParameter("action");
				if (action == null) action = "default";
				
				if( action.equalsIgnoreCase("default") && session.getAttribute("courseGroupSortDirection") != null ) {
					sortDirection = session.getAttribute("courseGroupSortDirection").toString();
				}
				CourseGroup courseGroup = courseAndCourseGroupService.loadForUpdateCourseGroup(new Long(courseGroupId));
				List<Course> tempCourseList = courseGroup.getCourses();
				List<Course> courseList = courseAndCourseGroupService.getCoursesByCourseGourp(courseGroup);//get the courses attached to this coursegroup. we need to get 4(SCORM,DFC, weblink and Synch) courses.
				CourseSort courseSort = new CourseSort();				
				/*
				  for(Course course : tempCourseList){
				  if(course.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE))
						courseList.add(course);
					}
				 */
				
				courseSort.setSortBy(sortBy);
				courseSort.setSortDirection(Integer.parseInt(sortDirection));
				Collections.sort(courseList,courseSort);
				List<Course> finalCourseList=null;

				if( action != null ) {

					//for delete
					
					if(action.equalsIgnoreCase(MANAGE_COURSES_DELETE_COURSE_ACTION)){
						String[] selectedCourseValues = request.getParameterValues("courses");
						if( selectedCourseValues != null ){
							for( int i=0; i<selectedCourseValues.length; i++ ) {
								String courseId = selectedCourseValues[i];
								if( courseId != null ) {
									//List<Learner> listLearners = new ArrayList <Learner>();
									Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
									//listLearners = mngOrzGroup.getMembersbyOrgGroup(orgGroup, listVU360User);
									courseGroup.getCourses().remove(course); 
									//
									
								}	
							}	
							// update this Course Group
							courseAndCourseGroupService.updateCourseGroup(courseGroup);
							return new ModelAndView("redirect:ins_viewCourseGroupCourses.do?courseGroupId="+courseGroup.getId());
						}
					}
					//end delete
					
					
					if( searchType.equalsIgnoreCase(CHANGE_GROUP_ALL_SEARCH_ACTION) ) {

						recordShowing = courseList.size();

						if( action.equalsIgnoreCase("sort") ) {
							sortDirection= request.getParameter("sortDirection");
							session.setAttribute("courseGroupSortDirection", sortDirection);

							if( sortDirection.equalsIgnoreCase("0") )
								sortDirection = "1";
							else
								sortDirection="0";
							session.setAttribute("previousAction", "sort");
							
						}
						finalCourseList = courseList.subList(0, courseList.size());
						context.put("courseGroup",courseGroup);
						context.put("totalRecord",finalCourseList.size());
						context.put("recordShowing",recordShowing);
						context.put("pageNo",pageNo);
						context.put("listCourses",finalCourseList);
						context.put("sortDirection", sortDirection );
						context.put("sortBy", sortBy);
						context.put("direction", direction);
						context.put("searchType", searchType);
						session.setAttribute("prevSortBy", sortBy);
						session.setAttribute("prevAct", prevAct);
						
						return new ModelAndView(viewCourseGroupCoursesTemplate, "context", context);
					}
				

					if( searchType.equalsIgnoreCase(MANAGE_COURSE_SIMPLE_SEARCH_ACTION) ) {
						pageNo = 0;
						if( courseList.size() > MANAGE_COURSES_PAGE_SIZE )	
							finalCourseList = courseList.subList(0, courseList.size());

					} else {
						//pagination serach
						if(direction.equalsIgnoreCase(MANAGE_COURSE_PREVPAGE_DIRECTION)){

							if( session.getAttribute("previousAction") != null &&
									session.getAttribute("previousAction").toString().equalsIgnoreCase("sort") ) {
								session.setAttribute("previousAction", "paging");
							}
							pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
							pageNo = (pageNo <= 0)?0:pageNo-1;

						}else if(direction.equalsIgnoreCase(MANAGE_COURSE_NEXTPAGE_DIRECTION)){

							if( session.getAttribute("previousAction") != null &&
									session.getAttribute("previousAction").toString().equalsIgnoreCase("sort") ) {
								session.setAttribute("previousAction", "paging");
							}
							pageIndex = request.getParameter("pageIndex");
							pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
							if( action.equalsIgnoreCase("sort") ) {
								pageNo = (pageNo<=0)?0:pageNo-1;
							}
							pageNo = (pageNo<0)?0:pageNo+1;
						}
					}
					int totalRecord = 0;

					if( courseList != null && courseList.size() > 0 ) {
						if( courseList.size() < (pageNo+1)*MANAGE_COURSE_PAGE_SIZE ) {
							finalCourseList = courseList.subList(pageNo*MANAGE_COURSE_PAGE_SIZE ,courseList.size());
							recordShowing = courseList.size();
							
						}else if( courseList.size()< MANAGE_COURSE_PAGE_SIZE ) {
							recordShowing = courseList.size();
							finalCourseList = courseList.subList(0 ,courseList.size());

						}else{
							recordShowing = (pageNo+1)*MANAGE_COURSE_PAGE_SIZE;
							finalCourseList = courseList.subList(pageNo*MANAGE_COURSE_PAGE_SIZE ,( pageNo+1) * MANAGE_COURSE_PAGE_SIZE);

						}
						totalRecord = courseList.size();
					}

					if (searchType.equalsIgnoreCase(MANAGE_COURSE_SIMPLE_SEARCH_ACTION))
						recordShowing=totalRecord;
					if(action.equalsIgnoreCase("sort") ){
						session.setAttribute("courseGroupSortDirection", sortDirection);

						if (sortDirection.equalsIgnoreCase("0"))
							sortDirection = "1";
						else
							sortDirection="0";
						session.setAttribute("previousAction", "sort");
					}
					context.put("courseGroup",courseGroup);
					context.put("totalRecord",totalRecord);
					context.put("recordShowing",recordShowing);
					context.put("pageNo",pageNo);
					context.put("listCourses",finalCourseList);
					context.put("sortDirection", sortDirection );
					context.put("sortBy", sortBy);
					context.put("direction", direction);
					context.put("searchType", searchType);
					session.setAttribute("prevSortBy", sortBy);
					session.setAttribute("prevAct", prevAct);
				}
			}
			return new ModelAndView(viewCourseGroupCoursesTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
			return new ModelAndView(viewCourseGroupCoursesTemplate);
		}
		//return new ModelAndView(viewCourseGroupCoursesTemplate);
		
		
	}
	
	// [1/27/2011] LMS-7183 :: Retired Course Functionality II (Refactored Code)
	@SuppressWarnings("unchecked")
	public ModelAndView showCoursesToAddInCourseGroup(HttpServletRequest request, HttpServletResponse response){
		
		log.debug("IN searchSynchronousCourse");		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		int totalRecords = -1, recordShowing = 0, pageIndex = 0, pageSize = MANAGE_COURSES_PAGE_SIZE, sortDirection = 0;	
		String sortColumn = "NAME", courseType = "All";
				
		try {
			// Get request parameters
			Long courseGroupId = Long.valueOf(request.getParameter("courseGroupId") );
			String action = StringUtils.isNotBlank(request.getParameter("action")) ? request.getParameter("action") : ACTION_SEARCH_NONE;
			String courseTitle = request.getParameter("courseTitle");			
			String courseId = request.getParameter("courseId");
			courseType = StringUtils.isNotBlank(request.getParameter("courseType")) ? request.getParameter("courseType") : courseType;			
			sortColumn = StringUtils.isNotBlank(request.getParameter("sortColumn")) ? request.getParameter("sortColumn") : sortColumn;
			sortDirection = StringUtils.isNotBlank(request.getParameter("sortDirection")) ? Integer.valueOf(request.getParameter("sortDirection")) : sortDirection;
			pageIndex = StringUtils.isNotBlank(request.getParameter("pageIndex")) ? Integer.valueOf(request.getParameter("pageIndex")) : pageIndex;
			pageIndex = (pageIndex <= 0) ? 0 : pageIndex;
			
			CourseGroup courseGroup = courseAndCourseGroupService.loadForUpdateCourseGroup(courseGroupId);
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			com.softech.vu360.lms.vo.ContentOwner contentOwner = loggedInUser.getLearner().getCustomer().getContentOwner();
			
			if(action.equalsIgnoreCase(MANAGE_COURSES_UPDATECOURSE_ACTION)) { //Add Courses in Course Group
				
				String[] selectedCourseValues = request.getParameterValues("selectedCourses");
				if( selectedCourseValues != null ){
					for( int i=0; i<selectedCourseValues.length; i++ ) {
						String courseID = selectedCourseValues[i];
						if( courseID != null ) {

							Course course=courseAndCourseGroupService.getCourseById(Long.valueOf(courseID));
							if(!courseGroup.getCourses().contains(course)){
								courseGroup.addCourse(course);
							}
						}	
					}		
				}
				courseAndCourseGroupService.updateCourseGroup(courseGroup);
				return new ModelAndView("redirect:ins_viewCourseGroupCourses.do?courseGroupId="+courseGroup.getId());
			}
			
			if (action.equalsIgnoreCase(ACTION_SEARCH_NONE)) {
				request.setAttribute("newPage", true);
			}
			else {
				
				if ( action.equalsIgnoreCase(ACTION_SHOW_ALL) ) {					
					pageSize = -1;
					pageIndex = 0;
				}
				else if ( action.equalsIgnoreCase(ACTION_SHOW_PREV) ) {
					pageIndex = (pageIndex <= 0) ? 0 : (pageIndex - 1);				
				}
				else if ( action.equals(ACTION_SHOW_NEXT) ) {
					pageIndex = pageIndex < 0 ? 0 : (pageIndex + 1);			
				}
				
				Map<String,Object> resultSet = this.courseAndCourseGroupService.getCourses(contentOwner.getId(), courseTitle, courseId, courseType, "Active", sortColumn, sortDirection, pageIndex, pageSize);
				List<Course> courseList = (List<Course>) resultSet.get("list");
				totalRecords = Integer.valueOf( resultSet.get("recordSize").toString() );
				pageSize = (pageSize == -1) ? totalRecords : pageSize;
				
				if ( action.equalsIgnoreCase(ACTION_SHOW_ALL) ) {
					recordShowing = courseList.size();
				}
				else {
					recordShowing = courseList.size() < MANAGE_COURSES_PAGE_SIZE ? totalRecords : (pageIndex + 1) * MANAGE_COURSES_PAGE_SIZE;
				}
				
				context.put("courseList", courseList);
			}
			
			context.put("courseGroup", courseGroup);
			context.put("courseTitle", courseTitle);
			context.put("courseId", courseId);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		
		context.put("courseTypes", courseTypes);		
		context.put("courseType", courseType);		
		context.put("totalRecords", totalRecords);
		context.put("recordShowing", recordShowing);
		context.put("pageIndex", pageIndex);
		context.put("pageSize", pageSize);
		context.put("sortDirection", sortDirection);
		context.put("sortColumn", sortColumn);
		
		return new ModelAndView(viewCoursesToAddInCourseGroupTemplate, "context", context);	
	}
	
	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}
	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getViewCourseGroupCoursesTemplate() {
		return viewCourseGroupCoursesTemplate;
	}
	public void setViewCourseGroupCoursesTemplate(
			String viewCourseGroupCoursesTemplate) {
		this.viewCourseGroupCoursesTemplate = viewCourseGroupCoursesTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		
	}
	public String getViewCoursesToAddInCourseGroupTemplate() {
		return viewCoursesToAddInCourseGroupTemplate;
	}

	public void setViewCoursesToAddInCourseGroupTemplate(
			String viewCoursesToAddInCourseGroupTemplate) {
		this.viewCoursesToAddInCourseGroupTemplate = viewCoursesToAddInCourseGroupTemplate;
	}

	public String getViewCourseGroupsRedirectTemplate() {
		return viewCourseGroupsRedirectTemplate;
	}

	public void setViewCourseGroupsRedirectTemplate(
			String viewCourseGroupsRedirectTemplate) {
		this.viewCourseGroupsRedirectTemplate = viewCourseGroupsRedirectTemplate;
	}	
	
	
	@SuppressWarnings("unused")
	private List<Course> filterCourseList(List<Course> courseList, List<Course> courseGroupCourses){
		for(Course course : courseGroupCourses){
			if(courseList.contains(course)){
				courseList.remove(course);
			}
		}
		return courseList;
	}
		
}