package com.softech.vu360.lms.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LOCKEDCOURSE")
public class LockedCourse  implements SearchableKey{
	
	@Id
    @javax.persistence.TableGenerator(name = "LOCKEDCOURSE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LOCKEDCOURSE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LOCKEDCOURSE_ID")
	private Long id;
	
	@Column(name = "LOCKTYPE")
	private String locktype;
	
	@OneToOne
    @JoinColumn(name="ENROLLMENT_ID")
	private LearnerEnrollment enrollment ;
	
	@Column(name = "LEARNER_ID")
	private Long learner ;	
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "COURSE_ID")
	private Long course ;
	
	@Column(name = "COURSELOCKED")
	private Boolean courselocked;
	
	/**
	 * @return the locktype
	 */
	public String getLocktype() {
		return locktype;
	}
	/**
	 * @param locktype the locktype to set
	 */
	public void setLocktype(String locktype) {
		this.locktype = locktype;
	}
	/**
	 * @return the enrollment
	 */
	public LearnerEnrollment  getEnrollment() {
		return this.enrollment;
	}
	/**
	 * @param enrollment the enrollment to set
	 */
	public void setEnrollment(LearnerEnrollment enrollment) {
		this.enrollment = enrollment;
	}
	/**
	 * @return the learner
	 */
	public Long getLearner() {
		return this.learner ;
	}
	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Long learner) {
		this.learner = learner ;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the course
	 */
	public Long getCourse() {
		return this.course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Long course) {
		this.course = course;
	}
	/**
	 * @return the courselocked
	 */
	public  Boolean isCourselocked() {
		return courselocked;
	}
	/**
	 * @param courselocked the courselocked to set
	 */
	public void setCourselocked(Boolean courselocked) {
		this.courselocked = courselocked;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}
}
