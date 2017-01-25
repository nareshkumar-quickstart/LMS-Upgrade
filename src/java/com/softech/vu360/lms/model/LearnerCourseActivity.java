/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LEARNERCOURSEACTIVITY")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="ACTIVITYNAME")
public class LearnerCourseActivity implements SearchableKey {

	@Id
    @javax.persistence.TableGenerator(name = "LEARNERCOURSEACTIVITY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERCOURSEACTIVITY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERCOURSEACTIVITY_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="LEARNERCOURSESTATISTICS_ID")
	private LearnerCourseStatistics learnerCourseStatistics ;
	
	@OneToOne
    @JoinColumn(name="COURSEACTIVITY_ID")
	private CourseActivity courseActivity ;
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the learnerCourseStatistics
	 */
	public LearnerCourseStatistics getLearnerCourseStatistics() {
		return this.learnerCourseStatistics;
	}

	/**
	 * @param learnerCourseStatistics the learnerCourseStatistics to set
	 */
	public void setLearnerCourseStatistics(
			LearnerCourseStatistics learnerCourseStatistics) {
		this.learnerCourseStatistics = learnerCourseStatistics ;
	}

	/**
	 * @return the courseActivity
	 */
	public CourseActivity getCourseActivity() {
		return this.courseActivity;
	}

	/**
	 * @param courseActivity the courseActivity to set
	 */
	public void setCourseActivity(CourseActivity courseActivity) {
		this.courseActivity = courseActivity;
	}

}
