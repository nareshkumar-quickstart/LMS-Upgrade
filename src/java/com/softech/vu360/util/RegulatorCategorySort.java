package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.RegulatorCategory;

public class RegulatorCategorySort implements Comparator<RegulatorCategory> {

	String sortBy = "categoryName";
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

	public int compare(RegulatorCategory arg0, RegulatorCategory arg1) {

		if( sortBy.equalsIgnoreCase("categoryName") ) {
			if( sortDirection == 0 )
				return arg0.getDisplayName().trim().toUpperCase().compareTo(arg1.getDisplayName().trim().toUpperCase());
			else
				return arg1.getDisplayName().trim().toUpperCase().compareTo(arg0.getDisplayName().trim().toUpperCase());
		} else if( sortBy.equalsIgnoreCase("categoryType") ) {
			if ( arg0.getCategoryType() == null )
				arg0.setCategoryType("");
			if ( arg1.getCategoryType() == null )
				arg1.setCategoryType("");
			if( sortDirection == 0 )
				return arg0.getCategoryType().trim().toUpperCase().compareTo(arg1.getCategoryType().trim().toUpperCase());
			else
				return arg1.getCategoryType().trim().toUpperCase().compareTo(arg0.getCategoryType().trim().toUpperCase());
		}
		
		return 0;
	}
	

}
