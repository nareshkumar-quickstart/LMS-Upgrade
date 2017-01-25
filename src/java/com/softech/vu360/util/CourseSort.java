package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Course;

/**
 * @author Dyutiman
 */
public class CourseSort implements Comparator<Course>{

	String sortBy = "courseTitle";
	int sortDirection = 0;

	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public int getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	public int compare(Course arg0, Course arg1) {

		if( sortBy.equalsIgnoreCase("courseTitle") ) {
			if ( arg0.getCourseTitle() == null )
				arg0.setCourseTitle("");
			if ( arg1.getCourseTitle() == null )
				arg1.setCourseTitle("");
			if( sortDirection == 0 )
				return arg0.getCourseTitle().trim().toUpperCase().compareTo(arg1.getCourseTitle().trim().toUpperCase());
			else
				return arg1.getCourseTitle().trim().toUpperCase().compareTo(arg0.getCourseTitle().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("courseId") ) {
			if ( arg0.getBussinesskey() == null )
				arg0.setBussinesskey("");
			if ( arg1.getBussinesskey() == null )
				arg1.setBussinesskey("");
			if( sortDirection == 0 )
				return arg0.getBussinesskey().trim().toUpperCase().compareTo(arg1.getBussinesskey().trim().toUpperCase());
			else
				return arg1.getBussinesskey().trim().toUpperCase().compareTo(arg0.getBussinesskey().trim().toUpperCase());
		} 
		else if( sortBy.equalsIgnoreCase("courseType") ) {
			if ( arg0.getCourseType() == null )
				arg0.setCourseType("");
			if ( arg1.getCourseType() == null )
				arg1.setCourseType("");
			if( sortDirection == 0 )
				return arg0.getCourseType().trim().toUpperCase().compareTo(arg1.getCourseType().trim().toUpperCase());
			else
				return arg1.getCourseType().trim().toUpperCase().compareTo(arg0.getCourseType().trim().toUpperCase());
		} 
		return 0;
	}
}