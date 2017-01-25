/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.CredentialCategory;

/**
 * @author sana.majeed
 * // [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
 */
public class CredentialCategorySort implements Comparator<CredentialCategory> {
	
	private int sortDirection = 0;
	
	public CredentialCategorySort (int sortDirection) {
		this.sortDirection = sortDirection;
	}

	@Override
	public int compare(CredentialCategory category1, CredentialCategory category2) {
		
		String first = category1.getName().trim();
		String second = category2.getName().trim();
		int result;
		
		if (this.sortDirection == 0) {
			result = first.compareToIgnoreCase(second);
		}
		else {
			result = second.compareToIgnoreCase(first);
		}
		
		return (result != 0 ? result : (category1.getCategoryType().compareToIgnoreCase(category2.getCategoryType()) ) );
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}
	
	

}
