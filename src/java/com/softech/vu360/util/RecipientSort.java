package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.MngAlert;





public class RecipientSort implements Comparator<MngAlert>{
	String sortBy = "recipientGroupName";
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

	
		if( sortBy.equalsIgnoreCase("recipientGroupName") ) {

			if( sortDirection == 0 )
				return recip1.getRecipient().getAlertRecipientGroupName().trim().toUpperCase().compareTo(recip2.getRecipient().getAlertRecipientGroupName().trim().toUpperCase());
			else
				return recip2.getRecipient().getAlertRecipientGroupName().trim().toUpperCase().compareTo(recip1.getRecipient().getAlertRecipientGroupName().trim().toUpperCase());
			
		}   else if( sortBy.equalsIgnoreCase("recipGroupType") ) {
			
			if( sortDirection == 0 )
				return recip1.getAlertRecipientGroupType().compareTo(recip2.getAlertRecipientGroupType());
			else
				return recip2.getAlertRecipientGroupType().compareTo(recip1.getAlertRecipientGroupType());
			 	
		}	return 0;
	}

	

}
