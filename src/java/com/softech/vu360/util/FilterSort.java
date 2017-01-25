package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.MngFilter;

public class FilterSort implements Comparator<MngFilter>{
	
	String sortBy = "firstName";
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
	
	
	public int compare(MngFilter arg0, MngFilter arg1) {

		if( sortBy.equalsIgnoreCase("filterName") ) {

			if( sortDirection == 0 )
				return arg0.getFilterName().trim().toUpperCase().compareTo(arg1.getFilterName().trim().toUpperCase());
			else
				return arg1.getFilterName().trim().toUpperCase().compareTo(arg0.getFilterName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("recipents") ) {
			
			if( sortDirection == 0 )
				return arg0.getFilterType().compareTo(arg1.getFilterType());
			else
				return arg1.getFilterType().compareTo(arg0.getFilterType());
			
		} 		return 0;
	}

	
	

	

}
