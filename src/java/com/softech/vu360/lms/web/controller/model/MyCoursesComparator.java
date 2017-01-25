package com.softech.vu360.lms.web.controller.model;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.learner.MyCoursesController;

/**
 * 
 * @author jason
 *
 */

public class MyCoursesComparator  implements ILMSBaseInterface, Comparator<MyCoursesItem>  {
	private static final long serialVersionUID = 1L;
	public static final int RECENT_VIEW = 0;
	public static final int ENROLLED_VIEW = 1;
	public static final int COURSE_CATALOG_VIEW = 2;
	
	private int view = RECENT_VIEW;
	
	public MyCoursesComparator(String viewStr) {
		if ( !StringUtils.isBlank(viewStr) ) {
			if ( viewStr.trim().equalsIgnoreCase(MyCoursesController.RECENT_MYCOURSES_VIEW)) {
				this.view = RECENT_VIEW;
			}
			else if ( viewStr.trim().equalsIgnoreCase(MyCoursesController.ENROLLED_MYCOURSES_VIEW)) {
				this.view = ENROLLED_VIEW;
			}
			else if ( viewStr.trim().equalsIgnoreCase(MyCoursesController.COURSECATALOG_MYCOURSES_VIEW)) {
				this.view = COURSE_CATALOG_VIEW;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MyCoursesItem o1, MyCoursesItem o2) {
		switch (view) {
			case MyCoursesCourseGroup.FILTER_COURSECATALOG: case MyCoursesCourseGroup.FILTER_ENROLLED: {
				String courseName1 = (String)o1.get("courseTitle");
				String courseName2 = (String)o2.get("courseTitle");
				return courseName1.compareToIgnoreCase(courseName2);
			}
			default: {
				Date lastAccessDate1 = (Date)o1.get("lastAccessDate");
				Date lastAccessDate2 = (Date)o2.get("lastAccessDate");

				if ( lastAccessDate1 == null ) {
					return -1;
				}
				else if ( lastAccessDate2 == null ) {
					return 1;
				}
				// return the opposite for desc order
				return lastAccessDate2.compareTo(lastAccessDate1);
			}
		}
	}
}