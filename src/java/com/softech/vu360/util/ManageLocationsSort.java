package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Location;

public class ManageLocationsSort implements Comparator<Location>{

	String sortBy = "resourceName";
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

	public int compare(Location arg0, Location arg1) {

		if( sortBy.equalsIgnoreCase("locationName") ) {
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("state") ) {
			
			if ( arg0.getAddress().getState() == null )
				arg0.getAddress().setState("");
			if ( arg1.getAddress().getState() == null )
				arg1.getAddress().setState("");
			
			if( sortDirection == 0 )
				return arg0.getAddress().getState().trim().toUpperCase().compareTo(arg1.getAddress().getState().trim().toUpperCase());
			else
				return arg1.getAddress().getState().trim().toUpperCase().compareTo(arg0.getAddress().getState().trim().toUpperCase());
		}else if( sortBy.equalsIgnoreCase("city") ) {
			
			if ( arg0.getAddress().getCity() == null )
				arg0.getAddress().setCity("");
			if ( arg1.getAddress().getCity() == null )
				arg1.getAddress().setCity("");
			
			if( sortDirection == 0 )
				return arg0.getAddress().getCity().trim().toUpperCase().compareTo(arg1.getAddress().getCity().trim().toUpperCase());
			else
				return arg1.getAddress().getCity().trim().toUpperCase().compareTo(arg0.getAddress().getCity().trim().toUpperCase());
		}
		return 0;
	}
}