/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Ashis
 *
 */
@Entity
@DiscriminatorValue("TEXTCUSTOMFIELD")
public class TextCustomField extends CustomField {

	public TextCustomField() {
		super();
		// TODO Auto-generated constructor stub
	}

}
