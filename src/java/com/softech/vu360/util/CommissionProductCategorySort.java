package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.CommissionProductCategory;


public class CommissionProductCategorySort  implements Comparator<CommissionProductCategory>{
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
	
	
	public int compare(CommissionProductCategory arg0, CommissionProductCategory arg1) {
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			
 		//return 0;
	}

}
