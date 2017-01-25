/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("FINALSCORE")
public class FinalScoreCourseActivity extends CourseActivity implements
		SearchableKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
