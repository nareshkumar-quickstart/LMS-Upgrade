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
@DiscriminatorValue("TEXTCREDITREPORTINGFIELD")
//org.hibernate.boot.spi.InFlightMetadataCollector$DuplicateSecondaryTableException: Table with that name [CREDITREPORTINGFIELD] already associated with entity
//@SecondaryTable(name="CREDITREPORTINGFIELD")
public class TextCreditReportingField extends CreditReportingField {

	private static final long serialVersionUID = 7191433124808346018L;

	public TextCreditReportingField() {
		super();
	}

}
