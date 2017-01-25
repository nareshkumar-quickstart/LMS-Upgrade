package com.softech.vu360.lms.web.controller.comparator;

import java.util.Comparator;

import com.softech.vu360.lms.model.Commission;

public class CommissionComparator implements
		Comparator<Commission> 
{
	private int sortDirection = 0;
	private int sortColumnIndex = 0;
	
	public CommissionComparator(int sortDirection, int sortColumnIndex){
		this.sortDirection = sortDirection;
		this.sortColumnIndex = sortColumnIndex;
	}
	
	@Override
	public int compare(Commission o1, Commission o2) {
        if(o1.getName() == null || o2.getName() == null) return 0;
		return o1.getName().compareTo(o2.getName()) >= 0 ? (this.sortDirection == 0 ? 1 : 0) : (this.sortDirection == 0 ? 0 : 1);
	}

}
