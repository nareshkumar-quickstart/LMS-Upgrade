package com.softech.vu360.util;

import java.util.Comparator;
import java.util.Date;

import com.softech.vu360.lms.model.TrainingPlanCourseView;

/**
 * @author muzammil.shaikh
 */
public class TrainingPlanCourseViewSort implements Comparator<TrainingPlanCourseView>{

	public static final String SORTBY_COURSENAME = "courseName";
	public static final String SORTBY_ID = "businessKey";
	public static final String SORTBY_EXPIRATIONDATE = "expirationDate";
	public static final String SORTBY_ENTITLEMENTNAME = "entitlementName";
	
	String sortBy = "courseName";
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
	
	public int compare(TrainingPlanCourseView arg0, TrainingPlanCourseView arg1) {

		if( sortBy.equalsIgnoreCase(SORTBY_COURSENAME) ) {
			if ( arg0.getCourseName() == null )
				arg0.setCourseName("");
			if ( arg1.getCourseName() == null )
				arg1.setCourseName("");
			if( sortDirection == 0 )
				return arg0.getCourseName().trim().toUpperCase().compareTo(arg1.getCourseName().trim().toUpperCase());
			else
				return arg1.getCourseName().trim().toUpperCase().compareTo(arg0.getCourseName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase(SORTBY_ID) ) {
			if ( arg0.getBusinessKey() == null )
				arg0.setBusinessKey("");
			if ( arg1.getBusinessKey() == null )
				arg1.setBusinessKey("");
			if( sortDirection == 0 )
				return arg0.getBusinessKey().trim().toUpperCase().compareTo(arg1.getBusinessKey().trim().toUpperCase());
			else
				return arg1.getBusinessKey().trim().toUpperCase().compareTo(arg0.getBusinessKey().trim().toUpperCase());
		} 
		else if( sortBy.equalsIgnoreCase(SORTBY_EXPIRATIONDATE) ) {
			if ( arg0.getExpirationDate() == null )
				arg0.setExpirationDate(new Date());
			if ( arg1.getExpirationDate() == null )
				arg1.setExpirationDate(new Date());
			if( sortDirection == 0 )
				return arg0.getExpirationDate().compareTo(arg1.getExpirationDate());
			else
				return arg1.getCourseType().compareTo(arg0.getCourseType());
		} 
		else if( sortBy.equalsIgnoreCase(SORTBY_ENTITLEMENTNAME) ) {
			if ( arg0.getEntitlementName() == null )
				arg0.setEntitlementName("");
			if ( arg1.getEntitlementName() == null )
				arg1.setEntitlementName("");
			if( sortDirection == 0 )
				return arg0.getEntitlementName().compareTo(arg1.getEntitlementName());
			else
				return arg1.getEntitlementName().compareTo(arg0.getEntitlementName());
		}
		return 0;
	}
}