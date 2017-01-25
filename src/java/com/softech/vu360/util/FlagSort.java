package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.ManageFlag;


/**
 * 
 * @author Saptarshhi
 *
 */
public class FlagSort implements Comparator<ManageFlag> {
	
	String sortBy = "flagName";
	int sortDirection = 0;
	
	public int compare(ManageFlag arg0, ManageFlag arg1) {

		if( sortBy.equalsIgnoreCase("flagName") ) {

			if( sortDirection == 0 )
				return arg0.getFlag().getFlagName().trim().toUpperCase().compareTo(arg1.getFlag().getFlagName().trim().toUpperCase());
			else
				return arg1.getFlag().getFlagName().trim().toUpperCase().compareTo(arg0.getFlag().getFlagName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("questionText") ) {
			if ( arg0.getQuestionText() == null )
				arg0.setQuestionText("");
			if ( arg1.getQuestionText() == null )
				arg1.setQuestionText("");
			if( sortDirection == 0 )
				return arg0.getQuestionText().trim().toUpperCase().compareTo(arg1.getQuestionText().trim().toUpperCase());
			else
				return arg1.getQuestionText().trim().toUpperCase().compareTo(arg0.getQuestionText().trim().toUpperCase());
			
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
