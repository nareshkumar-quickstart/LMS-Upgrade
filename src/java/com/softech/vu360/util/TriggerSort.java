package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.MngAlert;



public class TriggerSort implements Comparator<MngAlert>{
	String sortBy = "triggerName";
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
	
	
	public int compare(MngAlert recip1, MngAlert  recip2) {

	
		if( sortBy.equalsIgnoreCase("triggerName") ) {

			if( sortDirection == 0 )
				return recip1.getTrigger().getTriggerName().trim().toUpperCase().compareTo(recip2.getTrigger().getTriggerName().trim().toUpperCase());
			else
				return recip2.getTrigger().getTriggerName().trim().toUpperCase().compareTo(recip1.getTrigger().getTriggerName().trim().toUpperCase());
			
		} 		return 0;
	}
	
	


}
