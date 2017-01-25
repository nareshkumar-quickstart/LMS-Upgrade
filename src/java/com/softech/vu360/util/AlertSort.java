package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.MngAlert;

public class AlertSort  implements Comparator<MngAlert>{
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
	
	
	public int compare(MngAlert arg0, MngAlert arg1) {

		if( sortBy.equalsIgnoreCase("alertName") ) {

			if( sortDirection == 0 )
				return arg0.getAlert().getAlertName().trim().toUpperCase().compareTo(arg1.getAlert().getAlertName().trim().toUpperCase());
			else
				return arg1.getAlert().getAlertName().trim().toUpperCase().compareTo(arg0.getAlert().getAlertName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("recipents") ) {
			
			if( sortDirection == 0 )
				return arg0.getRecipents().compareTo(arg1.getRecipents());
			else
				return arg1.getRecipents().compareTo(arg0.getRecipents());
			
		} 		return 0;
	}
	
	


}
