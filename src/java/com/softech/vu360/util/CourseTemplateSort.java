package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.CourseConfigurationTemplate;

/**
 * @author Dyutiman
 * created on 25-june-2009
 *
 */
public class CourseTemplateSort implements Comparator<CourseConfigurationTemplate>{

	String sortBy = "name";
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

	public int compare(CourseConfigurationTemplate arg0, CourseConfigurationTemplate arg1) {

		if( sortBy.equalsIgnoreCase("name") ) {

			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("date") ) {
			
			if( sortDirection == 0 )
				if( arg1.getLastUpdatedDate() != null && arg0.getLastUpdatedDate() != null ) {
					return arg0.getLastUpdatedDate().compareTo(arg1.getLastUpdatedDate());
				} else return 0;
			else
				if( arg1.getLastUpdatedDate() != null && arg0.getLastUpdatedDate() != null ) {
					return arg1.getLastUpdatedDate().compareTo(arg0.getLastUpdatedDate());
				} else return 0;
		} 
		return 0;
	}
	
}