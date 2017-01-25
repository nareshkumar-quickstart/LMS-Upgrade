package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.SearchMemberItem;

public class RecordSort implements Comparator<SearchMemberItem> {

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
	
	public int compare(SearchMemberItem arg0, SearchMemberItem arg1) {
		
		if( sortBy.equalsIgnoreCase("name") ) {
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("type") ) {
			
			if( sortDirection == 0 )
				return arg0.getType().trim().toUpperCase().compareTo(arg1.getType().trim().toUpperCase());
			else
				return arg1.getType().trim().toUpperCase().compareTo(arg0.getType().trim().toUpperCase());
		} 
		return 0;
	}
}