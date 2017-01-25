package com.softech.vu360.lms.model;

import java.util.Date;

/**
 * 
 * @author jason
 * 
 */
public class LearnerNote implements SearchableKey {

	public static final String LEARNER_NOTE = "Learner";
	public static final String LMS_NOTE = "System";
	
	private Long id;
	private String comment = null;
	private Integer displayOrder = 0;
	private String location = null;
	private String source = LEARNER_NOTE;
	private Date timestamp = new Date();
	private Learner learner = null;
	private Course course = null;
	
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
	 * @return the searchableKey
	 */
	public String getKey() {
		return id.toString();
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}
	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}
}
