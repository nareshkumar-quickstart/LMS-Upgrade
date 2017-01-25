package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("MULTIPLELINETEXTCREDITREPORTINGFIELD")

//public class MultipleLineTextCreditReportingfield extends TextCreditReportingField {
public class MultipleLineTextCreditReportingfield extends CreditReportingField {

	/**
	 * 
	 */
	public MultipleLineTextCreditReportingfield() {
		super();
	}

}
