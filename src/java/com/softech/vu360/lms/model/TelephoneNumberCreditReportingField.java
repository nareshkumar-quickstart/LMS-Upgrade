package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("TELEPHONENUMBERCREDITREPORTINGFIELD")
//public class TelephoneNumberCreditReportingField extends TextCreditReportingField
public class TelephoneNumberCreditReportingField extends CreditReportingField {

	/**
	 * 
	 */
	public TelephoneNumberCreditReportingField() {
		super();
		// TODO Auto-generated constructor stub
	}

}
