/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;


/**
 * @author tapas
 *
 */
public class CustomFieldSort implements Comparator<ManageCustomField> {

	String sortBy = "fieldLabel";
	int sortDirection = 0;
	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}
	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	public int compare(ManageCustomField arg0, ManageCustomField arg1) {

		if( sortBy.equalsIgnoreCase("fieldLabel") ) {

			if( sortDirection == 0 )
				return arg0.getFieldName().trim().toUpperCase().compareTo(arg1.getFieldName().trim().toUpperCase());
			else
				return arg1.getFieldName().trim().toUpperCase().compareTo(arg0.getFieldName().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("fieldType") ) {
			if ( arg0.getFieldType() == null )
				arg0.setFieldType("");
			if ( arg1.getFieldType() == null )
				arg1.setFieldType("");
			if( sortDirection == 0 )
				return arg0.getFieldType().trim().toUpperCase().compareTo(arg1.getFieldType().trim().toUpperCase());
			else
				return arg1.getFieldType().trim().toUpperCase().compareTo(arg0.getFieldType().trim().toUpperCase());
			
		} else if( sortBy.equalsIgnoreCase("containingRegulatorCategoryCount") ) {			
			if( sortDirection == 0 )
				return arg0.getContainingRegulatorCategoryCount()>arg1.getContainingRegulatorCategoryCount()?1:0;
			else
				return arg0.getContainingRegulatorCategoryCount()>arg1.getContainingRegulatorCategoryCount()?0:1;
			
		} 
		return 0;
	}
}
