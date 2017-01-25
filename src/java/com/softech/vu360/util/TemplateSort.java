package com.softech.vu360.util;

import java.util.Comparator;
import java.util.Date;

import com.softech.vu360.lms.model.CourseConfigurationTemplate;

public class TemplateSort implements Comparator<CourseConfigurationTemplate> {

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
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
		} else if( sortBy.equalsIgnoreCase("lastUpdatedDate") ) {
			if ( arg0.getLastUpdatedDate() == null )
				arg0.setLastUpdatedDate(new Date());
			if ( arg1.getLastUpdatedDate() == null )
				arg1.setLastUpdatedDate(new Date());
			if( sortDirection == 0 )
				if( arg0.getLastUpdatedDate().before(arg1.getLastUpdatedDate()) )
					return 0;
				else
					return 1;
			else
				if( arg1.getLastUpdatedDate().after(arg0.getLastUpdatedDate()) )
					return 0;
				else
					return 1;
		}
		return 0;
	}
}