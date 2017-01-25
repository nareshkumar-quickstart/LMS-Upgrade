package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Instructor;

/**
 * @author Dyutiman
 * created on 1st july, 2009
 *
 */
public class InstructorSort implements Comparator<Instructor>{

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

	public int compare(Instructor arg0, Instructor arg1) {

		if( sortBy.equalsIgnoreCase("firstName") ) {
			if ( arg0.getUser().getFirstName() == null )
				arg0.getUser().setFirstName("");
			if ( arg1.getUser().getFirstName() == null )
				arg1.getUser().setFirstName("");
			if( sortDirection == 0 )
				return arg0.getUser().getFirstName().trim().toUpperCase().compareTo(arg1.getUser().getFirstName().trim().toUpperCase());
			else
				return arg1.getUser().getFirstName().trim().toUpperCase().compareTo(arg0.getUser().getFirstName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("lastName") ) {
			if ( arg0.getUser().getLastName() == null )
				arg0.getUser().setLastName("");
			if ( arg1.getUser().getLastName() == null )
				arg1.getUser().setLastName("");
			if( sortDirection == 0 )
				return arg0.getUser().getLastName().trim().toUpperCase().compareTo(arg1.getUser().getLastName().trim().toUpperCase());
			else
				return arg1.getUser().getLastName().trim().toUpperCase().compareTo(arg0.getUser().getLastName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("phone") ) {
			if( arg0.getUser().getLearner().getLearnerProfile() != null && arg1.getUser().getLearner().getLearnerProfile() != null ) {
				if ( arg0.getUser().getLearner().getLearnerProfile().getOfficePhone() == null )
					arg0.getUser().getLearner().getLearnerProfile().setOfficePhone("");
				if ( arg1.getUser().getLearner().getLearnerProfile().getOfficePhone() == null )
					arg1.getUser().getLearner().getLearnerProfile().setOfficePhone("");
				if( sortDirection == 0 )
					return arg0.getUser().getLearner().getLearnerProfile().getOfficePhone().trim().toUpperCase().compareTo(arg1.getUser().getLearner().getLearnerProfile().getOfficePhone().trim().toUpperCase());
				else
					return arg1.getUser().getLearner().getLearnerProfile().getOfficePhone().trim().toUpperCase().compareTo(arg0.getUser().getLearner().getLearnerProfile().getOfficePhone().trim().toUpperCase());
			}
		} else if( sortBy.equalsIgnoreCase("emailAddress") ) {
			if ( arg0.getUser().getEmailAddress() == null )
				arg0.getUser().setEmailAddress("");
			if ( arg1.getUser().getEmailAddress() == null )
				arg1.getUser().setEmailAddress("");
			if( sortDirection == 0 )
				return arg0.getUser().getEmailAddress().trim().toUpperCase().compareTo(arg1.getUser().getEmailAddress().trim().toUpperCase());
			else
				return arg1.getUser().getEmailAddress().trim().toUpperCase().compareTo(arg0.getUser().getEmailAddress().trim().toUpperCase());
		} 
		return 0;
	}

}