/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * @author raja.ali
 * 2015/11/19
 * 
 */
@Entity
@PrimaryKeyJoinColumn(name="ID") 
public abstract class MutlipleChoiceSurveyQuestion extends SurveyQuestion implements SearchableKey {

	private static final long serialVersionUID = 6794588995981616699L;
	
	public static final String HORIZONTAL = "horizontal";
	public static final String VERTICAL = "vertical";
	
	@Column(name = "ALIGNMENT")
	private String alignment = HORIZONTAL;

	public MutlipleChoiceSurveyQuestion() {
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

}
