package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("SINGLELINETEXTCREDITREPORTINGFIELD")
//public class SingleLineTextCreditReportingFiled extends TextCreditReportingField
public class SingleLineTextCreditReportingFiled extends CreditReportingField {

	/**
	 * 
	 */
	public SingleLineTextCreditReportingFiled() {
		super();
	}
   
}
