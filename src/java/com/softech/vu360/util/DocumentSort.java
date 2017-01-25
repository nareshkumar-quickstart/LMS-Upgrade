package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Document;

/**
 * @author tathya
 * created on 26-june-2009
 *
 */
public class DocumentSort implements Comparator<Document>{

	String sortBy = "fileName";
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
	
	public int compare(Document arg0, Document arg1) {

		if( sortBy.equalsIgnoreCase("name") ) {
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getFileName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("description") ) {
			if ( arg0.getDescription() == null )
				arg0.setDescription("");
			if ( arg1.getDescription() == null )
				arg1.setDescription("");
			if( sortDirection == 0 )
				return arg0.getDescription().trim().toUpperCase().compareTo(arg1.getDescription().trim().toUpperCase());
			else
				return arg1.getDescription().trim().toUpperCase().compareTo(arg0.getDescription().trim().toUpperCase());
		}
		return 0;
	}
}