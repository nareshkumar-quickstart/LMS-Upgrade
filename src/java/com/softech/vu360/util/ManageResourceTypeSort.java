package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.ResourceType;


public class ManageResourceTypeSort implements Comparator<ResourceType>{
	String sortBy = "resourceTypeName";
	int sortDirection = 0;
	
	public int compare(ResourceType arg0, ResourceType arg1) {
		if( sortBy.equalsIgnoreCase("resourceTypeName") ) {
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			

		}
		return 0;
	}
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

}
