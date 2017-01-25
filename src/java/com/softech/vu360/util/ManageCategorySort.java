package com.softech.vu360.util;
import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.ManageCategory;

public class ManageCategorySort implements Comparator<ManageCategory>{
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

	public int compare(ManageCategory arg0, ManageCategory arg1) {

		if( sortBy.equalsIgnoreCase("name") ) {
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("categoryType") ) {
						
			if ( arg0.getCategoryType() == null )
				arg0.setCategoryType("");
			if ( arg1.getCategoryType() == null )
				arg1.setCategoryType("");
									
			if( sortDirection == 0 )
				return arg0.getCategoryType().trim().toUpperCase().compareTo(arg1.getCategoryType().trim().toUpperCase());
			else
				return arg1.getCategoryType().trim().toUpperCase().compareTo(arg0.getCategoryType().trim().toUpperCase());

		} 
		return 0;
	}
}
