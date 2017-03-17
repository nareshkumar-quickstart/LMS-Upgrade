package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.lms.web.controller.model.MyCoursesCourseGroup;
import com.softech.vu360.lms.web.controller.model.MyCoursesItem;
import com.softech.vu360.lms.widget.WidgetLogic;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.IApplicationConstants;
import com.softech.vu360.util.VU360Branding;

public class CoursesWidgetLogic implements WidgetLogic {

	protected static final Logger log = Logger.getLogger(CoursesWidgetLogic.class);
	
	protected EnrollmentService enrollmentService;
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request) {
		/*
		 * At this point we are adding a pre-display courses action to check and update status 
		 * of Synchronous course for a learner automatically (TPMO-960) 
		 */
		enrollmentService.updateAutoSynchronousCourseStatus(vu360User.getLearner());
		
		log.debug("in getWidgetDataList vu360User.id=" + vu360User.getId() + " vu360User.username=" + vu360User.getUsername() + " params=" + params);
		List<WidgetData> datas = new ArrayList<WidgetData>();
		String filter = (String) ((params.get("filter")!=null) ? params.get("filter") : "all");
		
		//TODO: needs refactoring since there is already a mess in LMS so i dont have time right now
		Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());

		if(filter.compareTo("recent")==0 || filter.compareTo("enrolled")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List<CourseGroupView> sortedCourseGroups = new ArrayList<CourseGroupView>();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, brand, courseGroups, filteredMyCourses, sortedCourseGroups, filter, search, true);
			if(filter.compareTo("recent")==0 || filter.compareTo("all")==0) {
				List myCourseItems = (List) map.get("myCourseItems");
				DateUtil dateUtil = new DateUtil();
				for (Object item : myCourseItems) {
					MyCoursesItem myCoursesItem = (MyCoursesItem) item;

					if( (myCoursesItem.getEnrollmentStatus().equalsIgnoreCase(IApplicationConstants.ENROLLMENT_STATUS_ACTIVE) || myCoursesItem.getEnrollmentStatus().equalsIgnoreCase(IApplicationConstants.ENROLLMENT_STATUS_EXPIRED)) &&
							dateUtil.dateDifference(new Date(), myCoursesItem.getEnrollment().getEnrollmentEndDate()) > 0){
						continue;
					}

					WidgetData widgetData = new WidgetData();
					Map<String, Object> data = widgetData.getDataMap();
					widgetData.setId((long)datas.size());
					data.put("courseName", myCoursesItem.getCourseTitle());
					data.put("percentComplete", myCoursesItem.getCompletionPercent()+"");
					data.put("courseCompleted", myCoursesItem.getCourseCompleted()+"");
					data.put("courseID", myCoursesItem.getCourseID());
					data.put("courseCategoryType", myCoursesItem.getCourseCategoryType());
					data.put("courseIdKey", myCoursesItem.getCourseIdKey().toString());
					data.put("learnerEnrollmentID", myCoursesItem.getLearnerEnrollmentID());
					data.put("courseStatus", myCoursesItem.getCourseStatusDisplayText());
					datas.add(widgetData);
				}
			}

			if(filter.compareTo("enrolled")==0 || filter.compareTo("all")==0) {
				List myCourseGroups = (List) map.get("myCoursesCourseGroups");
				for (Object item : myCourseGroups) {
					MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
					Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
					while(iterator.hasNext()) {
						MyCoursesItem myCoursesItem = iterator.next();
						WidgetData widgetData = new WidgetData();


						//skip all expired courses.
						if("enrolled".equals(filter)){
							Date enrolEndDate = myCoursesItem.getEnrollment().getEnrollmentEndDate();
							Date currentDate = new Date();
							if( enrolEndDate.before(currentDate) &&
									(myCoursesItem.getEnrollmentStatus().equalsIgnoreCase(IApplicationConstants.ENROLLMENT_STATUS_ACTIVE)
											|| myCoursesItem.getEnrollmentStatus().equalsIgnoreCase(IApplicationConstants.ENROLLMENT_STATUS_EXPIRED)))
								continue;
						}

						Map<String, Object> data = widgetData.getDataMap();
						widgetData.setId((long)datas.size());
						data.put("courseName", myCoursesItem.getCourseTitle());
						data.put("percentComplete", myCoursesItem.getCompletionPercent()+"");
						data.put("courseCompleted", myCoursesItem.getCourseCompleted()+"");
						data.put("courseID", myCoursesItem.getCourseID());
						data.put("courseCategoryType", myCoursesItem.getCourseCategoryType());
						data.put("courseIdKey", myCoursesItem.getCourseIdKey().toString());
						data.put("learnerEnrollmentID", myCoursesItem.getLearnerEnrollmentID());
						data.put("courseStatus", myCoursesItem.getCourseStatusDisplayText());
						datas.add(widgetData);
					}

				}
			}
		}

		
		if(filter.compareTo("completedCourses")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List<CourseGroupView> sortedCourseGroups = new ArrayList<CourseGroupView>();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, brand, courseGroups, filteredMyCourses, sortedCourseGroups, "completedCourses", search, true);
			List myCourseGroups = (List) map.get("myCoursesCourseGroups");
			for (Object item : myCourseGroups) {
				MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
//				Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
				Iterator<MyCoursesItem> iterator = courseGroup.getCurrentMyCoursesItems("completedCourses").iterator();
				while(iterator.hasNext()) {
					MyCoursesItem myCoursesItem = iterator.next();
					WidgetData widgetData = new WidgetData();
					Map<String, Object> data = widgetData.getDataMap();
					widgetData.setId((long)datas.size());
					data.put("courseName", myCoursesItem.getCourseTitle());
					data.put("percentComplete", myCoursesItem.getCompletionPercent()+"");
					data.put("courseCompleted", myCoursesItem.getCourseCompleted()+"");
					data.put("courseID", myCoursesItem.getCourseID());
					data.put("courseCategoryType", myCoursesItem.getCourseCategoryType());
					data.put("courseIdKey", myCoursesItem.getCourseIdKey().toString());
					data.put("learnerEnrollmentID", myCoursesItem.getLearnerEnrollmentID());
                    data.put("courseStatus", myCoursesItem.getCourseStatusDisplayText());
					datas.add(widgetData);
				}
				
			}
		}
		
		if(filter.compareTo("expiredCourses")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List<CourseGroupView> sortedCourseGroups = new ArrayList<CourseGroupView>();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, brand, courseGroups, filteredMyCourses, sortedCourseGroups, "expiredCourses", search, true);
			List myCourseGroups = (List) map.get("myCoursesCourseGroups");
			for (Object item : myCourseGroups) {
				MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
				Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
				while(iterator.hasNext()) {
					MyCoursesItem myCoursesItem = iterator.next();
					WidgetData widgetData = new WidgetData();
					Map<String, Object> data = widgetData.getDataMap();
					widgetData.setId((long)datas.size());
					data.put("courseName", myCoursesItem.getCourseTitle());
					data.put("percentComplete", myCoursesItem.getCompletionPercent()+"");
					data.put("courseCompleted", myCoursesItem.getCourseCompleted()+"");
					data.put("courseID", myCoursesItem.getCourseID());
					data.put("courseCategoryType", myCoursesItem.getCourseCategoryType());
					data.put("courseIdKey", myCoursesItem.getCourseIdKey().toString());
					data.put("learnerEnrollmentID", myCoursesItem.getLearnerEnrollmentID());
					datas.add(widgetData);
				}
				
			}
		}
		
		if(filter.compareTo("coursesAboutToExpire")==0 || filter.compareTo("all")==0) {
			List<CourseGroupView> courseGroups = new ArrayList<CourseGroupView>();
			List<MyCoursesItem> filteredMyCourses = new ArrayList<MyCoursesItem>();
			List<CourseGroupView> sortedCourseGroups = new ArrayList<CourseGroupView>();
			String search = "Search";
			Map map = enrollmentService.displayMyCourses(vu360User, brand, courseGroups, filteredMyCourses, sortedCourseGroups, "coursesAboutToExpire", search, true);
			List myCourseGroups = (List) map.get("myCoursesCourseGroups");
			for (Object item : myCourseGroups) {
				MyCoursesCourseGroup courseGroup = (MyCoursesCourseGroup) item;
				Iterator<MyCoursesItem> iterator = courseGroup.getAllMyCoursesItems().iterator();
				while(iterator.hasNext()) {
					MyCoursesItem myCoursesItem = iterator.next();
					WidgetData widgetData = new WidgetData();
					Map<String, Object> data = widgetData.getDataMap();
					widgetData.setId((long)datas.size());
					data.put("courseName", myCoursesItem.getCourseTitle());
					data.put("percentComplete", myCoursesItem.getCompletionPercent()+"");
					data.put("courseCompleted", myCoursesItem.getCourseCompleted()+"");
					data.put("courseID", myCoursesItem.getCourseID());
					data.put("courseCategoryType", myCoursesItem.getCourseCategoryType());
					data.put("courseIdKey", myCoursesItem.getCourseIdKey().toString());
					data.put("learnerEnrollmentID", myCoursesItem.getLearnerEnrollmentID());
					datas.add(widgetData);
				}
				
			}
		}
		
		return datas;
	}

}
