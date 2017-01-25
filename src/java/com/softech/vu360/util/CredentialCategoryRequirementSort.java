package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.SelectedRequirement;

public class CredentialCategoryRequirementSort implements Comparator<SelectedRequirement> {

	
	private String sortBy = "requirementName";
	private int sortDirection = 0;
	
	public int compare(SelectedRequirement arg0, SelectedRequirement arg1) {
		if( sortBy.equalsIgnoreCase("requirementName") ) {
			if ( arg0.getRequirement().getName() == null )
				arg0.getRequirement().setName("");
			if ( arg1.getRequirement().getName() == null )
				arg1.getRequirement().setName("");
			if( sortDirection == 0 )
				return arg0.getRequirement().getName().trim().toUpperCase().compareTo(arg1.getRequirement().getName().trim().toUpperCase());
			else
				return arg1.getRequirement().getName().trim().toUpperCase().compareTo(arg0.getRequirement().getName().trim().toUpperCase());
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
