package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("SSNCREDITREPORTINGFIELD")
//public class SSNCreditReportingFiled extends TextCreditReportingField
public class SSNCreditReportingFiled extends CreditReportingField {

	/**
	 * 
	 */
	public SSNCreditReportingFiled() {
		super();
	}

}
