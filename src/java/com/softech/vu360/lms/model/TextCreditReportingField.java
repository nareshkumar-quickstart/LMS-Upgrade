/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.SecondaryTable;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("TEXTCREDITREPORTINGFIELD")
@SecondaryTable(name="CREDITREPORTINGFIELD")
public class TextCreditReportingField extends CreditReportingField {

	private static final long serialVersionUID = 7191433124808346018L;

	public TextCreditReportingField() {
		super();
		// TODO Auto-generated constructor stub
	}

}
