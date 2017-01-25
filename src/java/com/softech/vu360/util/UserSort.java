package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Learner;

public class UserSort implements Comparator<Learner> {

	String sortBy = "firstName";
	int sortDirection = 0;
	
	public int getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	@Override
	public int compare( Learner arg0, Learner arg1 ) {

		if( sortBy.equalsIgnoreCase("firstName") ) {

			if( sortDirection == 0 )
				return arg0.getVu360User().getFirstName().toUpperCase().compareTo(arg1.getVu360User().getFirstName().toUpperCase());
			else
				return arg1.getVu360User().getFirstName().toUpperCase().compareTo(arg0.getVu360User().getFirstName().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("lastName") ) {

			if( sortDirection == 0 )
				return arg0.getVu360User().getLastName().toUpperCase().compareTo(arg1.getVu360User().getLastName().toUpperCase());
			else
				return arg1.getVu360User().getLastName().toUpperCase().compareTo(arg0.getVu360User().getLastName().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("userName") ) {

			if( sortDirection == 0 )
				return arg0.getVu360User().getUsername().toUpperCase().compareTo(arg1.getVu360User().getUsername().toUpperCase());
			else
				return arg1.getVu360User().getUsername().toUpperCase().compareTo(arg0.getVu360User().getUsername().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("accountNonLocked") ) {

			if( sortDirection == 0 )
				return new Boolean(arg0.getVu360User().getAccountNonLocked()).compareTo(new Boolean(arg1.getVu360User().getAccountNonLocked()));
			else
				return new Boolean(arg1.getVu360User().getAccountNonLocked()).compareTo(new Boolean(arg0.getVu360User().getAccountNonLocked()));

		} else if( sortBy.equalsIgnoreCase("emailAddress") ) {

			if( sortDirection == 0 )
				return arg0.getVu360User().getEmailAddress().toUpperCase().compareTo(arg1.getVu360User().getEmailAddress().toUpperCase());
			else
				return arg1.getVu360User().getEmailAddress().toUpperCase().compareTo(arg0.getVu360User().getEmailAddress().toUpperCase());
		}
		return 0;
	}

}