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
@DiscriminatorValue("ATTENDANCE")
public class AttendanceCourseActivity extends CourseActivity implements
		SearchableKey {

}
