/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("DATETIMECREDITREPORTINGFIELD")
public class DateTimeCreditReportingField extends CreditReportingField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateTimeCreditReportingField() {
		super();
		// TODO Auto-generated constructor stub
	}

}
