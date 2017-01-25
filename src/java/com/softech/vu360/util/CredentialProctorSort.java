package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.CredentialProctors;


public class CredentialProctorSort implements Comparator<CredentialProctors>{
	
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
	
	
	public int compare(CredentialProctors arg0, CredentialProctors arg1) {

		if( sortBy.equalsIgnoreCase("firstName") ) {

			if( sortDirection == 0 )
				return arg0.getFirstName().trim().toUpperCase().compareTo(arg1.getFirstName().trim().toUpperCase());
			else
				return arg1.getFirstName().trim().toUpperCase().compareTo(arg0.getFirstName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("lastName") ) {

			if( sortDirection == 0 )
				return arg0.getLastName().trim().toUpperCase().compareTo(arg1.getLastName().trim().toUpperCase());
			else
				return arg1.getLastName().trim().toUpperCase().compareTo(arg0.getLastName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("userName") ) {

			if( sortDirection == 0 )
				return arg0.getUserName().trim().toUpperCase().compareTo(arg1.getUserName().trim().toUpperCase());
			else
				return arg1.getUserName().trim().toUpperCase().compareTo(arg0.getUserName().trim().toUpperCase());
			
		}
		
		
		else if( sortBy.equalsIgnoreCase("email") ) {

			if( sortDirection == 0 )
				return arg0.getEmail().trim().toUpperCase().compareTo(arg1.getEmail().trim().toUpperCase());
			else
				return arg1.getEmail().trim().toUpperCase().compareTo(arg0.getEmail().trim().toUpperCase());
			
		}
		
		else if( sortBy.equalsIgnoreCase("companyName") ) {

			if(arg0.getCompanyName()==null)
				arg0.setCompanyName("");
			if(arg1.getCompanyName()==null)
				arg1.setCompanyName("");
			
			if( sortDirection == 0 )
				return arg0.getCompanyName().trim().toUpperCase().compareTo(arg1.getCompanyName().trim().toUpperCase());
			else
				return arg1.getCompanyName().trim().toUpperCase().compareTo(arg0.getCompanyName().trim().toUpperCase());
			
		}
		
		else if( sortBy.equalsIgnoreCase("courseId") ) {
			if(arg0.getCourseId()==null)
				arg0.setCourseId("");
			if(arg1.getCourseId()==null)
				arg1.setCourseId("");
			
			if( sortDirection == 0 )
				return arg0.getCourseId().trim().toUpperCase().compareTo(arg1.getCourseId().trim().toUpperCase());
			else
				return arg1.getCourseId().trim().toUpperCase().compareTo(arg0.getCourseId().trim().toUpperCase());
			
		}
		
		else if( sortBy.equalsIgnoreCase("completionDate") ) {
			if(arg0.getCompletionDate()==null)
				arg0.setCompletionDate("");
			if(arg1.getCompletionDate()==null)
				arg1.setCompletionDate("");
			
			if( sortDirection == 0 )
				return arg0.getCompletionDate().trim().toUpperCase().compareTo(arg1.getCompletionDate().trim().toUpperCase());
			else
				return arg1.getCompletionDate().trim().toUpperCase().compareTo(arg0.getCompletionDate().trim().toUpperCase());
			
		}
		
		return 0;
	}
}
