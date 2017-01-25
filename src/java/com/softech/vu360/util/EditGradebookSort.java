package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.instructor.ManageActivity;

public class EditGradebookSort implements Comparator<ManageActivity>{

	String sortBy = "firstName";
	int sortDirection = 0;

	public int compare(ManageActivity arg0, ManageActivity arg1) {
		if( sortBy.equalsIgnoreCase("firstName") ) {

			if( sortDirection == 0 )
				return arg0.getLearner().getVu360User().getFirstName().trim().toUpperCase().compareTo(arg1.getLearner().getVu360User().getFirstName().trim().toUpperCase());
			else
				return arg1.getLearner().getVu360User().getFirstName().trim().toUpperCase().compareTo(arg0.getLearner().getVu360User().getFirstName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("lastName") ) {
			
			if( sortDirection == 0 )
				return arg0.getLearner().getVu360User().getLastName().trim().toUpperCase().compareTo(arg1.getLearner().getVu360User().getLastName().trim().toUpperCase());
			else
				return arg1.getLearner().getVu360User().getLastName().trim().toUpperCase().compareTo(arg0.getLearner().getVu360User().getLastName().trim().toUpperCase());
			
		}
		return 0;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
}
