/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.CredentialTrainingCourses;

/**
 * @author syed.mahmood
 *
 */
public class TrainingCourseByCredentialSort implements Comparator<CredentialTrainingCourses>{
	
	String sortBy = "courseTitle";
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
	
	
	public int compare(CredentialTrainingCourses arg0, CredentialTrainingCourses arg1) {

		if( sortBy.equalsIgnoreCase("courseTitle") ) {

			//courseTitle can be null, so set it to empty string to save NullPointerException 
			if(arg0.getCourseTitle()==null)
				arg0.setCourseTitle("");
			if(arg1.getCourseTitle()==null)
				arg1.setCourseTitle("");
			
			if( sortDirection == 0 )
				return arg0.getCourseTitle().trim().toUpperCase().compareTo(arg1.getCourseTitle().trim().trim().toUpperCase());
			else
				return arg1.getCourseTitle().trim().trim().toUpperCase().compareTo(arg0.getCourseTitle().trim().trim().toUpperCase());
			
		}
		else if( sortBy.equalsIgnoreCase("bussinesskey") ) {
			//Bussinesskey can be null, so set it to empty string to save NullPointerException 
			if(arg0.getBussinesskey()==null)
				arg0.setBussinesskey("");
			if(arg1.getBussinesskey()==null)
				arg1.setBussinesskey("");
			
			if( sortDirection == 0 )
				return arg0.getBussinesskey().trim().toUpperCase().compareTo(arg1.getBussinesskey().trim().toUpperCase());
			else
				return arg1.getBussinesskey().trim().toUpperCase().compareTo(arg0.getBussinesskey().trim().toUpperCase());
			
		} 
		
		
		return 0;
	}
}
