/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;



/**
 * @author syed.mahmood
 *
 */
public class PurchaseCertificateNumberSort implements Comparator<PurchaseCertificateNumber>{

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
	
	public int compare(PurchaseCertificateNumber arg0, PurchaseCertificateNumber arg1) {

		if( sortBy.equalsIgnoreCase("name") ) {
			if ( arg0.getCertificateNumber() == null )
				arg0.setCertificateNumber("");
			if ( arg1.getCertificateNumber() == null )
				arg1.setCertificateNumber("");
			if( sortDirection == 0 )
				return arg0.getCertificateNumber().trim().toUpperCase().compareTo(arg1.getCertificateNumber().trim().toUpperCase());
			else
				return arg1.getCertificateNumber().trim().toUpperCase().compareTo(arg0.getCertificateNumber().trim().toUpperCase());
		}
			
		 else if( sortBy.equalsIgnoreCase("used") ) {
			if ( arg0.getUsed() == null )
				arg0.setUsed("");
			if ( arg1.getUsed() == null )
				arg1.setUsed("");
			if( sortDirection == 0 )
				return arg0.getUsed().trim().toUpperCase().compareTo(arg1.getUsed().trim().toUpperCase());
			else
				return arg1.getUsed().trim().toUpperCase().compareTo(arg0.getUsed().trim().toUpperCase());
		}
		return 0;
	}

}
