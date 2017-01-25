package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Contact;

/**
 * @author Dyutiman
 * created on 25-june-2009
 *
 */
public class ContactSort implements Comparator<Contact>{

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
	
	public int compare(Contact arg0, Contact arg1) {

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
			
		} else if( sortBy.equalsIgnoreCase("emailAddress") ) {
			if ( arg0.getEmailAddress() == null )
				arg0.setEmailAddress("");
			if ( arg1.getEmailAddress() == null )
				arg1.setEmailAddress("");
			if( sortDirection == 0 )
				return arg0.getEmailAddress().trim().toUpperCase().compareTo(arg1.getEmailAddress().trim().toUpperCase());
			else
				return arg1.getEmailAddress().trim().toUpperCase().compareTo(arg0.getEmailAddress().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("phone") ) {
			if ( arg0.getPhone() == null )
				arg0.setPhone("");
			if ( arg1.getPhone() == null )
				arg1.setPhone("");
			if( sortDirection == 0 )
				return arg0.getPhone().trim().toUpperCase().compareTo(arg1.getPhone().trim().toUpperCase());
			else
				return arg1.getPhone().trim().toUpperCase().compareTo(arg0.getPhone().trim().toUpperCase());
		}
		return 0;
	}
	
}