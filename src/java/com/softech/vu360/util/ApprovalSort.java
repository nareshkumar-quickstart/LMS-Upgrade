package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.SurveyResult;

public class ApprovalSort implements Comparator<SurveyResult>{
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
	
	
	public int compare(SurveyResult arg0, SurveyResult arg1) {

		if( sortBy.equalsIgnoreCase("firstName") ) {

			if( sortDirection == 0 )
				return arg0.getSurveyee().getFirstName().trim().toUpperCase().compareTo(arg1.getSurveyee().getFirstName().trim().toUpperCase());
			else
				return arg1.getSurveyee().getFirstName().trim().toUpperCase().compareTo(arg0.getSurveyee().getFirstName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("lastName") ) {
			
			if( sortDirection == 0 )
				return arg0.getSurveyee().getLastName().trim().toUpperCase().compareTo(arg1.getSurveyee().getLastName().trim().toUpperCase());
			else
				return arg1.getSurveyee().getLastName().trim().toUpperCase().compareTo(arg0.getSurveyee().getLastName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("userName") ) {
			if ( arg0.getSurveyee().getUsername() == null )
				arg0.getSurveyee().setUsername("");
			if ( arg1.getSurveyee().getUsername() == null )
				arg1.getSurveyee().setUsername("");
			if( sortDirection == 0 )
				return arg0.getSurveyee().getUsername().trim().toUpperCase().compareTo(arg1.getSurveyee().getUsername().trim().toUpperCase());
			else
				return arg1.getSurveyee().getUsername().trim().toUpperCase().compareTo(arg0.getSurveyee().getUsername().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("surveyName") ) {
			if ( arg0.getSurvey().getName() == null )
				arg0.getSurvey().setName("");
			if ( arg1.getSurvey().getName() == null )
				arg1.getSurvey().setName("");
			if( sortDirection == 0 )
				return arg0.getSurvey().getName().trim().toUpperCase().compareTo(arg1.getSurvey().getName().trim().toUpperCase());
			else
				return arg1.getSurvey().getName().trim().toUpperCase().compareTo(arg0.getSurvey().getName().trim().toUpperCase());
		}
		else if( sortBy.equalsIgnoreCase("status") ) {
			if ( arg0.getSurvey().getStatus() == null )
				arg0.getSurvey().setStatus("");
			if ( arg1.getSurvey().getStatus() == null )
				arg1.getSurvey().setStatus("");
			if( sortDirection == 0 )
				return arg0.getSurvey().getStatus().trim().toUpperCase().compareTo(arg1.getSurvey().getStatus().trim().toUpperCase());
			else
				return arg1.getSurvey().getStatus().trim().toUpperCase().compareTo(arg0.getSurvey().getStatus().trim().toUpperCase());
		}
		return 0;
	}
	
	
	
}



