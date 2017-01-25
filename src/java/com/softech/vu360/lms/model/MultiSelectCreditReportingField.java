/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("MULTISELECTCREDITREPORTINGFIELD")
public class MultiSelectCreditReportingField extends CreditReportingField {
	
	@Transient
	private  Boolean checkBox = false;
	public MultiSelectCreditReportingField() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the checkBox
	 */
	public  Boolean isCheckBox() {
		return checkBox;
	}
	/**
	 * @param checkBox the checkBox to set
	 */
	public void setCheckBox(Boolean checkBox) {
		this.checkBox = checkBox;
	}

}
