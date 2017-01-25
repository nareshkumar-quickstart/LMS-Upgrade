package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.InterceptorAlertForm;

public class AlertInterceotorSort implements Comparator<InterceptorAlertForm>{
	
	String sortBy = "date";
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
	
	
	public int compare(InterceptorAlertForm arg0, InterceptorAlertForm arg1) {
		
		if( sortBy.equalsIgnoreCase("date") ) {

			if( sortDirection == 0 && arg0.getCreatedDate() != null && arg1.getCreatedDate() != null)
				return arg0.getCreatedDate().compareTo(arg1.getCreatedDate());
			else if(arg1.getCreatedDate() != null && arg0.getCreatedDate() != null)
				return arg1.getCreatedDate().compareTo(arg0.getCreatedDate());
			
		}	return 0;
	}
	
	


}
