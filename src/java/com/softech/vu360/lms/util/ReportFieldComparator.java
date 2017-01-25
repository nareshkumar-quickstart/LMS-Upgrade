/**
 * 
 */
package com.softech.vu360.lms.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.VU360ReportField;

/**
 * @author jason
 *
 */
public class ReportFieldComparator implements Comparator<VU360ReportField>{
	
	public static final int SORTORDER = 0;
	public static final int DISPLAYORDER = 1;
	public static final int FIELDORDER = 2;	
	private int orderType = FIELDORDER;
	
	public ReportFieldComparator() {

	}
	
	public ReportFieldComparator(int orderType) {
		this.orderType = orderType;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(VU360ReportField left, VU360ReportField right) {
		
		switch ( orderType ) {
			case SORTORDER : {
				if ( left.getSortOrder() > right.getSortOrder() ) {
					return 1;
				}
				else if ( left.getSortOrder() < right.getSortOrder() ) {
					return -1;
				}
				else {
					return 0;
				}
			}			
			case DISPLAYORDER : {
				if ( left.getDisplayOrder() > right.getDisplayOrder() ) {
					return 1;
				}
				else if ( left.getDisplayOrder() < right.getDisplayOrder() ) {
					return -1;
				}
				else {
					return 0;
				}
			}			
			case FIELDORDER : {
				if ( left.getFieldOrder() > right.getFieldOrder() ) {
					return 1;
				}
				else if ( left.getFieldOrder() < right.getFieldOrder() ) {
					return -1;
				}
				else {
					return 0;
				}
			}
			default : { return 0; }
		}
	}
}