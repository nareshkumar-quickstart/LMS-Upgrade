package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.CommissionProduct;


public class CommissionProductSort  implements Comparator<CommissionProduct>{
	String sortBy = "name";
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
	
	
	public int compare(CommissionProduct arg0, CommissionProduct arg1) {
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			
 		//return 0;
	}

}
