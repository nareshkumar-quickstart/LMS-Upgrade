/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.LearnerEnrollment;

/**
 * @author syed.mahmood
 *
 */
public class ProctorCertificateCompletionSort implements Comparator<LearnerEnrollment>{
	
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
	
	
	public int compare(LearnerEnrollment arg0, LearnerEnrollment arg1) {

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
		else if( sortBy.equalsIgnoreCase("emailAddress") ) {
			
			if( sortDirection == 0 )
				return arg0.getLearner().getVu360User().getEmailAddress().trim().toUpperCase().compareTo(arg1.getLearner().getVu360User().getEmailAddress().trim().toUpperCase());
			else
				return arg1.getLearner().getVu360User().getEmailAddress().trim().toUpperCase().compareTo(arg0.getLearner().getVu360User().getEmailAddress().trim().toUpperCase());
			
		} 
		else if( sortBy.equalsIgnoreCase("courseTitle") ) {
			
			if( sortDirection == 0 )
				return arg0.getCourse().getCourseTitle().trim().toUpperCase().compareTo( arg1.getCourse().getCourseTitle().trim().toUpperCase() );
			else
				return arg1.getCourse().getCourseTitle().trim().toUpperCase().compareTo( arg0.getCourse().getCourseTitle().trim().toUpperCase() );				
		} else if( sortBy.equalsIgnoreCase("completionDate") ) {
			
			if( sortDirection == 0 )
				return arg0.getCourseStatistics().getCompletionDate().compareTo( arg1.getCourseStatistics().getCompletionDate());
			else
				return arg1.getCourseStatistics().getCompletionDate().compareTo( arg0.getCourseStatistics().getCompletionDate());				
		
		} 
		
		return 0;
	}
}
