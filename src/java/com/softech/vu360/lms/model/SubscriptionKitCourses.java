package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "SUBSCRIPTION_KIT_COURSES")
public class SubscriptionKitCourses  implements SearchableKey{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	@Column(name = "COURSE_GUID")
	private String courseGUID = null;
	
	@Column(name = "COURSEGROUP_GUID")
	private String courseGroupGUID = null;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUBSCRIPTION_KIT_ID")
	SubscriptionKit subscriptionKit = new SubscriptionKit();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCourseGUID() {
		return courseGUID;
	}
	
	public void setCourseGUID(String courseGUID) {
		this.courseGUID = courseGUID;
	}
	
	public String getCourseGroupGUID() {
		return courseGroupGUID;
	}
	
	public void setCourseGroupGUID(String courseGroupGUID) {
		this.courseGroupGUID = courseGroupGUID;
	}
	
	public SubscriptionKit getSubscriptionKit() {
		return subscriptionKit;
	}

	public void setSubscriptionKit(SubscriptionKit subscriptionKit) {
		this.subscriptionKit = subscriptionKit;
	}

	@Override
	public String getKey() {
		return id.toString();
	}
}
