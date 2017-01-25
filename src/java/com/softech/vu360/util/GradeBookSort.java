package com.softech.vu360.util;

import java.util.Comparator;
import java.util.Date;

import com.softech.vu360.lms.model.Gradebook;

/**
 * 
 * @author Saptarshi
 *
 */
public class GradeBookSort implements Comparator<Gradebook>{

	String sortBy = "className";
	int sortDirection = 0;

	public int compare(Gradebook arg0, Gradebook arg1) {
		if( sortBy.equalsIgnoreCase("className") ) {
			if ( arg0.getSynchronousClass().getSectionName() == null )
				arg0.getSynchronousClass().setSectionName("");
			if ( arg1.getSynchronousClass().getSectionName() == null )
				arg1.getSynchronousClass().setSectionName("");
			if( sortDirection == 0 )
				return arg0.getSynchronousClass().getSectionName().trim().toUpperCase().compareTo(arg1.getSynchronousClass().getSectionName().trim().toUpperCase());
			else
				return arg1.getSynchronousClass().getSectionName().trim().toUpperCase().compareTo(arg0.getSynchronousClass().getSectionName().trim().toUpperCase());
		} else if( sortBy.equalsIgnoreCase("startDate") ) {
			if ( arg0.getSynchronousClass().getClassStartDate() == null )
				arg0.getSynchronousClass().setClassStartDate(new Date());
			if ( arg1.getSynchronousClass().getClassStartDate() == null )
				arg1.getSynchronousClass().setClassStartDate(new Date());
			if( sortDirection == 0 )
				if( arg1.getSynchronousClass().getClassStartDate() != null && arg0.getSynchronousClass().getClassStartDate() != null ) {
					return arg0.getSynchronousClass().getClassStartDate().compareTo(arg1.getSynchronousClass().getClassStartDate());
				} else return 0;
			else
				if( arg1.getSynchronousClass().getClassStartDate() != null && arg0.getSynchronousClass().getClassStartDate() != null ) {
					return arg1.getSynchronousClass().getClassStartDate().compareTo(arg0.getSynchronousClass().getClassStartDate());
				} else return 0;
		} else if( sortBy.equalsIgnoreCase("endDate") ) {
			if ( arg0.getSynchronousClass().getClassEndDate() == null )
				arg0.getSynchronousClass().setClassEndDate(new Date());
			if ( arg1.getSynchronousClass().getClassEndDate() == null )
				arg1.getSynchronousClass().setClassEndDate(new Date());
			if( sortDirection == 0 )
				if( arg1.getSynchronousClass().getClassEndDate() != null && arg0.getSynchronousClass().getClassEndDate() != null ) {
					return arg0.getSynchronousClass().getClassEndDate().compareTo(arg1.getSynchronousClass().getClassEndDate());
				} else return 0;
			else
				if( arg1.getSynchronousClass().getClassEndDate() != null && arg0.getSynchronousClass().getClassEndDate() != null ) {
					return arg1.getSynchronousClass().getClassEndDate().compareTo(arg0.getSynchronousClass().getClassEndDate());
				} else return 0;
		}
		return 0;
	}
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
}
