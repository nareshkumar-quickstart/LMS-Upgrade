package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulatorCategory;

public class ApprovalRegulatorCategorySort implements Comparator<ApprovalRegulatorCategory>{

	String sortBy = "name";
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

	public int compare(ApprovalRegulatorCategory arg0, ApprovalRegulatorCategory arg1) {

		if( sortBy.equalsIgnoreCase("name") ) {

			if( sortDirection == 0 )
				return arg0.getRegulator().getName().trim().toUpperCase().compareTo(arg1.getRegulator().getName().trim().toUpperCase());
			else
				return arg1.getRegulator().getName().trim().toUpperCase().compareTo(arg0.getRegulator().getName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("alias") ) {
			if ( arg0.getRegulator().getAlias() == null )
				arg0.getRegulator().setAlias("");
			if ( arg1.getRegulator().getAlias() == null )
				arg1.getRegulator().setAlias("");
			if( sortDirection == 0 )
				return arg0.getRegulator().getAlias().trim().toUpperCase().compareTo(arg1.getRegulator().getAlias().trim().toUpperCase());
			else
				return arg1.getRegulator().getAlias().trim().toUpperCase().compareTo(arg0.getRegulator().getAlias().trim().toUpperCase());

		} /*else if( sortBy.equalsIgnoreCase("contact") ) {
			if ( arg0.getContactPersonName() == null )
				arg0.setContactPersonName("");
			if ( arg1.getContactPersonName() == null )
				arg1.setContactPersonName("");
			if( sortDirection == 0 )
				return arg0.getContactPersonName().trim().toUpperCase().compareTo(arg1.getContactPersonName().trim().toUpperCase());
			else
				return arg1.getContactPersonName().trim().toUpperCase().compareTo(arg0.getContactPersonName().trim().toUpperCase());

		}*/ else if( sortBy.equalsIgnoreCase("state") ) {
			if ( arg0.getRegulator().getAddress() != null && arg1.getRegulator().getAddress() != null ) {
				if( arg0.getRegulator().getAddress().getState() != null && arg1.getRegulator().getAddress().getState() != null ) {
					if( sortDirection == 0 )
						return arg0.getRegulator().getAddress().getState().trim().toUpperCase().compareTo(arg1.getRegulator().getAddress().getState().trim().toUpperCase());
					else
						return arg1.getRegulator().getAddress().getState().trim().toUpperCase().compareTo(arg0.getRegulator().getAddress().getState().trim().toUpperCase());
				}
			}
		}
		return 0;
	}

}