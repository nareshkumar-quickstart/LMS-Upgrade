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
@DiscriminatorValue("LECTURE")
public class LectureCourseActivity extends CourseActivity implements
		SearchableKey {

}
