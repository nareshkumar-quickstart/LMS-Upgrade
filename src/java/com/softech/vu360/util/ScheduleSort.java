package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.SynchronousClass;

/**
 * @author Adeel
 * created on 1st Feb, 2010
 *
 */
public class ScheduleSort implements Comparator<SynchronousClass>{
	
	
	public int compare(SynchronousClass arg0, SynchronousClass arg1) {
		
		return arg1.getClassStartDate().compareTo(arg0.getClassStartDate()); // desc
	}

}