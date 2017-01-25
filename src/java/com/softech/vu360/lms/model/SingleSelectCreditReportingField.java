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
@DiscriminatorValue("SINGLESELECTCREDITREPORTINGFIELD")
public class SingleSelectCreditReportingField extends CreditReportingField {
	
	public SingleSelectCreditReportingField() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
