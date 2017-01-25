package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.SurveyFlagAndSurveyResult;

public class AprovalSort implements Comparator<SurveyFlagAndSurveyResult>{
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
	
	
	public int compare(SurveyFlagAndSurveyResult arg0, SurveyFlagAndSurveyResult arg1) {

		if( sortBy.equalsIgnoreCase("firstName") ) {

			if( sortDirection == 0 )
				return arg0.getSurveyResult().getSurveyee().getFirstName().trim().toUpperCase().compareTo(arg1.getSurveyResult().getSurveyee().getFirstName().trim().toUpperCase());
			else
				return arg1.getSurveyResult().getSurveyee().getFirstName().trim().toUpperCase().compareTo(arg0.getSurveyResult().getSurveyee().getFirstName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("lastName") ) {
			
			if( sortDirection == 0 )
				return arg0.getSurveyResult().getSurveyee().getLastName().trim().toUpperCase().compareTo(arg1.getSurveyResult().getSurveyee().getLastName().trim().toUpperCase());
			else
				return arg1.getSurveyResult().getSurveyee().getLastName().trim().toUpperCase().compareTo(arg0.getSurveyResult().getSurveyee().getLastName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("userName") ) {
			if ( arg0.getSurveyResult().getSurveyee().getUsername() == null )
				arg0.getSurveyResult().getSurveyee().setUsername("");
			if ( arg1.getSurveyResult().getSurveyee().getUsername() == null )
				arg1.getSurveyResult().getSurveyee().setUsername("");
			if( sortDirection == 0 )
				return arg0.getSurveyResult().getSurveyee().getUsername().trim().toUpperCase().compareTo(arg1.getSurveyResult().getSurveyee().getUsername().trim().toUpperCase());
			else
				return arg1.getSurveyResult().getSurveyee().getUsername().trim().toUpperCase().compareTo(arg0.getSurveyResult().getSurveyee().getUsername().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("surveyName") ) {
			if ( arg0.getSurveyResult().getSurvey().getName() == null )
				arg0.getSurveyResult().getSurvey().setName("");
			if ( arg1.getSurveyResult().getSurvey().getName() == null )
				arg1.getSurveyResult().getSurvey().setName("");
			if( sortDirection == 0 )
				return arg0.getSurveyResult().getSurvey().getName().trim().toUpperCase().compareTo(arg1.getSurveyResult().getSurvey().getName().trim().toUpperCase());
			else
				return arg1.getSurveyResult().getSurvey().getName().trim().toUpperCase().compareTo(arg0.getSurveyResult().getSurvey().getName().trim().toUpperCase());
		}
		else if( sortBy.equalsIgnoreCase("status") ) {
			if ( arg0.getSurveyResult().getSurvey().getStatus() == null )
				arg0.getSurveyResult().getSurvey().setStatus("");
			if ( arg1.getSurveyResult().getSurvey().getStatus() == null )
				arg1.getSurveyResult().getSurvey().setStatus("");
			if( sortDirection == 0 )
				return arg0.getSurveyResult().getSurvey().getStatus().trim().toUpperCase().compareTo(arg1.getSurveyResult().getSurvey().getStatus().trim().toUpperCase());
			else
				return arg1.getSurveyResult().getSurvey().getStatus().trim().toUpperCase().compareTo(arg0.getSurveyResult().getSurvey().getStatus().trim().toUpperCase());
		}
		return 0;
	}
	
	
	
}



