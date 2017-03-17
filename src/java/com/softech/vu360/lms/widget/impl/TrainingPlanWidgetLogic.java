package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.lms.web.controller.model.MyCoursesCourseGroup;
import com.softech.vu360.lms.web.controller.model.MyCoursesItem;
import com.softech.vu360.lms.widget.WidgetLogic;

public class TrainingPlanWidgetLogic implements WidgetLogic {

	protected static final Logger log = Logger.getLogger(TrainingPlanWidgetLogic.class);
	private static final String recentCoursesLink = "lrn_myCourses.do?method=&search=Search&show=recent";
	private static final String enrolledCoursesLink = "lrn_myCourses.do?method=&search=Search&show=enrolled";

	protected EnrollmentService enrollmentService;
	protected CourseAndCourseGroupService courseAndCourseGroupService;

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request) {
		log.debug("in getWidgetDataList vu360User.id=" + vu360User.getId() + " vu360User.username=" + vu360User.getUsername() + " params=" + params);
		List<WidgetData> datas = new ArrayList<WidgetData>();
		String filter = (String) ((params.get("filter")!=null) ? params.get("filter") : "all");
		Map<Long, TrainingPlan> trainingPlanMap = new HashMap<Long, TrainingPlan>();
		Map<Long, List<MyCoursesItem>> trainingPlanCoursesMap = new HashMap<Long, List<MyCoursesItem>>();

		//TODO: doesnt like the following code but have to depend on the existing services to collect the data :(
		if(filter.compareTo("recent")==0 || filter.compareTo("enrolled")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List sortedCourseGroups = new ArrayList();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, null, courseGroups, filteredMyCourses, sortedCourseGroups, filter, search, true);
			if(filter.compareTo("recent")==0 || filter.compareTo("all")==0) {
				List myCourseItems = (List) map.get("myCourseItems");
				for (Object item : myCourseItems) {
					MyCoursesItem myCoursesItem = (MyCoursesItem) item;
					Long courseId = new Long(myCoursesItem.getCourseIdKey());
					Course course = courseAndCourseGroupService.getCourseById(courseId);
					TrainingPlan trainingPlan = courseAndCourseGroupService.getTrainingPlanByCourse(course, vu360User.getLearner().getCustomer());
					if(trainingPlan!=null) {
						trainingPlanMap.put(trainingPlan.getId(), trainingPlan);
						List<MyCoursesItem> courses = trainingPlanCoursesMap.get(trainingPlan.getId());
						if(courses == null) {
							courses = new ArrayList<MyCoursesItem>();
							trainingPlanCoursesMap.put(trainingPlan.getId(), courses);
						}
						courses.add(myCoursesItem);
					}
				}
			}

			if(filter.compareTo("enrolled")==0 || filter.compareTo("all")==0) {
				List myCourseGroups = (List) map.get("myCoursesCourseGroups");
				for (Object item : myCourseGroups) {
					MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
					Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
					while(iterator.hasNext()) {
						MyCoursesItem myCoursesItem = iterator.next();
						Long courseId = new Long(myCoursesItem.getCourseIdKey());
						Course course = courseAndCourseGroupService.getCourseById(courseId);
						TrainingPlan trainingPlan = courseAndCourseGroupService.getTrainingPlanByCourse(course, vu360User.getLearner().getCustomer());
						if(trainingPlan!=null) {
							trainingPlanMap.put(trainingPlan.getId(), trainingPlan);
							List<MyCoursesItem> courses = trainingPlanCoursesMap.get(trainingPlan.getId());
							if(courses == null) {
								courses = new ArrayList<MyCoursesItem>();
								trainingPlanCoursesMap.put(trainingPlan.getId(), courses);
							}
							courses.add(myCoursesItem);
						}
					}
				}
			}
		}


		if(filter.compareTo("completed")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List sortedCourseGroups = new ArrayList();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, null, courseGroups, filteredMyCourses, sortedCourseGroups, "completedCourses", search, true);
			List myCourseGroups = (List) map.get("myCoursesCourseGroups");
			for (Object item : myCourseGroups) {
				MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
				Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
				while(iterator.hasNext()) {
					MyCoursesItem myCoursesItem = iterator.next();
					Long courseId = new Long(myCoursesItem.getCourseIdKey());
					Course course = courseAndCourseGroupService.getCourseById(courseId);
					TrainingPlan trainingPlan = courseAndCourseGroupService.getTrainingPlanByCourse(course, vu360User.getLearner().getCustomer());
					if(trainingPlan!=null) {
						trainingPlanMap.put(trainingPlan.getId(), trainingPlan);
						List<MyCoursesItem> courses = trainingPlanCoursesMap.get(trainingPlan.getId());
						if(courses == null) {
							courses = new ArrayList<MyCoursesItem>();
							trainingPlanCoursesMap.put(trainingPlan.getId(), courses);
						}
						courses.add(myCoursesItem);
					}
				}
			}
		}

		if(filter.compareTo("expired")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List sortedCourseGroups = new ArrayList();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, null, courseGroups, filteredMyCourses, sortedCourseGroups, "expiredCourses", search, true);
			List myCourseGroups = (List) map.get("myCoursesCourseGroups");
			for (Object item : myCourseGroups) {
				MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
				Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
				while(iterator.hasNext()) {
					MyCoursesItem myCoursesItem = iterator.next();
					Long courseId = new Long(myCoursesItem.getCourseIdKey());
					Course course = courseAndCourseGroupService.getCourseById(courseId);
					TrainingPlan trainingPlan = courseAndCourseGroupService.getTrainingPlanByCourse(course, vu360User.getLearner().getCustomer());
					if(trainingPlan!=null) {
						trainingPlanMap.put(trainingPlan.getId(), trainingPlan);
						List<MyCoursesItem> courses = trainingPlanCoursesMap.get(trainingPlan.getId());
						if(courses == null) {
							courses = new ArrayList<MyCoursesItem>();
							trainingPlanCoursesMap.put(trainingPlan.getId(), courses);
						}
						courses.add(myCoursesItem);
					}
				}
			}
		}

		Iterator<Long> trainingPlanIterator = trainingPlanMap.keySet().iterator();
		while(trainingPlanIterator.hasNext()) {
			TrainingPlan trainingPlan = trainingPlanMap.get((Long)trainingPlanIterator.next());
			WidgetData widgetData = new WidgetData();
			Map<String, Object> data = widgetData.getDataMap();
			widgetData.setId((long)datas.size());
			data.put("trainingPlanId", trainingPlan.getId()+"");
			data.put("name", trainingPlan.getName());
			data.put("description", trainingPlan.getDescription());
			if(trainingPlan.getStartdate()!=null) {
				data.put("startDate", trainingPlan.getStartdate().getMonth()+"/"+trainingPlan.getStartdate().getDate()+"/"+(trainingPlan.getStartdate().getYear()+1900));
			}
			if(trainingPlan.getEnddate()!=null) {
				data.put("endDate", trainingPlan.getEnddate().getMonth()+"/"+trainingPlan.getEnddate().getDate()+"/"+(trainingPlan.getEnddate().getYear()+1900));
			}
			boolean courseStarted = false;
			List<MyCoursesItem> courses = (List<MyCoursesItem>)trainingPlanCoursesMap.get(trainingPlan.getId());
			for(MyCoursesItem course:courses) {
				if(!course.getCourseStatus().equalsIgnoreCase(LearnerCourseStatistics.NOT_STARTED)) {
					courseStarted = true;
					break;
				}
			}
			if(courseStarted) {
				/* If the course within Training Plan has already started then we should take the user to the “Recently Accessed Courses”
				 */

				data.put("link", recentCoursesLink);
			} else {
				/* If the course within Training Plan has not been started then we should take the user to “Enrolled Courses” */
				data.put("link", enrolledCoursesLink);
			}
			datas.add(widgetData);
		}

		return datas;
	}

}
