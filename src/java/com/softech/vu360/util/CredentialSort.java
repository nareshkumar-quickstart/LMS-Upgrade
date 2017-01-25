package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Credential;


/**
 * 
 * @author Saptarshi
 * created on 25-june-2009
 *
 */
public class CredentialSort implements Comparator<Credential> {

	String sortBy = "officialLicenseName";
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

	public int compare(Credential arg0, Credential arg1) {

		if( sortBy.equalsIgnoreCase("officialLicenseName") ) {
			if( sortDirection == 0 )
				return arg0.getOfficialLicenseName().trim().toUpperCase().compareTo(arg1.getOfficialLicenseName().trim().toUpperCase());
			else
				return arg1.getOfficialLicenseName().trim().toUpperCase().compareTo(arg0.getOfficialLicenseName().trim().toUpperCase());
		} else if( sortBy.equalsIgnoreCase("shortLicenseName") ) {
			if ( arg0.getShortLicenseName() == null )
				arg0.setShortLicenseName("");
			if ( arg1.getShortLicenseName() == null )
				arg1.setShortLicenseName("");
			if( sortDirection == 0 )
				return arg0.getShortLicenseName().trim().toUpperCase().compareTo(arg1.getShortLicenseName().trim().toUpperCase());
			else
				return arg1.getShortLicenseName().trim().toUpperCase().compareTo(arg0.getShortLicenseName().trim().toUpperCase());
		}else if( sortBy.equalsIgnoreCase("jurisdiction") ) {
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
