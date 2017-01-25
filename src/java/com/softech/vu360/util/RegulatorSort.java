package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Regulator;

/**
 * @author Dyutiman
 * created on 25-june-2009
 *
 */
public class RegulatorSort implements Comparator<Regulator>{

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

	public int compare(Regulator arg0, Regulator arg1) {

		if( sortBy.equalsIgnoreCase("name") ) {

			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("alias") ) {
			if ( arg0.getAlias() == null )
				arg0.setAlias("");
			if ( arg1.getAlias() == null )
				arg1.setAlias("");
			if( sortDirection == 0 )
				return arg0.getAlias().trim().toUpperCase().compareTo(arg1.getAlias().trim().toUpperCase());
			else
				return arg1.getAlias().trim().toUpperCase().compareTo(arg0.getAlias().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("emailAddress") ) {
			if ( arg0.getEmailAddress() == null )
				arg0.setEmailAddress("");
			if ( arg1.getEmailAddress() == null )
				arg1.setEmailAddress("");
			if( sortDirection == 0 )
				return arg0.getEmailAddress().trim().toUpperCase().compareTo(arg1.getEmailAddress().trim().toUpperCase());
			else
				return arg1.getEmailAddress().trim().toUpperCase().compareTo(arg0.getEmailAddress().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("jurisdiction") ) {
			if ( arg0.getJurisdiction() == null )
				arg0.setJurisdiction("");
			if ( arg1.getJurisdiction() == null )
				arg1.setJurisdiction("");
			if( sortDirection == 0 )
				return arg0.getJurisdiction().trim().toUpperCase().compareTo(arg1.getJurisdiction().trim().toUpperCase());
			else
				return arg1.getJurisdiction().trim().toUpperCase().compareTo(arg0.getJurisdiction().trim().toUpperCase());
		}
		return 0;
	}
	
}