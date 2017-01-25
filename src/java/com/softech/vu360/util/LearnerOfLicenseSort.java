package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.LicenseOfLearner;



public class LearnerOfLicenseSort implements Comparator<LicenseOfLearner>{
	
	String sortBy = "licenseType";
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
	
	
	@Override
	public int compare(LicenseOfLearner arg0, LicenseOfLearner arg1) {
		
		if( sortBy.equalsIgnoreCase("licenseType") ) {

			if( sortDirection == 0 )
				return arg0.getIndustryCredential().getCredential().getOfficialLicenseName().trim().toUpperCase().compareTo(arg1.getIndustryCredential().getCredential().getOfficialLicenseName().trim().toUpperCase());
			else
				return arg1.getIndustryCredential().getCredential().getOfficialLicenseName().trim().toUpperCase().compareTo(arg0.getIndustryCredential().getCredential().getOfficialLicenseName().trim().toUpperCase());
			
		}
		if( sortBy.equalsIgnoreCase("licenseExpiration") ) {

			if( sortDirection == 0 )
				return arg0.getSupportingInformation().trim().toUpperCase().compareTo(arg1.getSupportingInformation().trim().toUpperCase());
			else
				return arg1.getSupportingInformation().trim().toUpperCase().compareTo(arg0.getSupportingInformation().trim().toUpperCase());
			
		}

		return 0;

	}
	
	


}
