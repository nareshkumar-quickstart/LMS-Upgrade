package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * 
 * @author 	marium.saud
 * @author	ramiz.uddin
 *
 */
@Entity
@Table(name = "SUBSCRIPTION_COURSE")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = "SurveyCourse.INSERT_SUBSCRIPTION_COURSE", 
		procedureName = "INSERT_SUBSCRIPTION_COURSE",
	    parameters = {
		    @StoredProcedureParameter(mode = ParameterMode.IN, name = "SUBSCRIPTION_ID", type = Long.class),
		    @StoredProcedureParameter(mode = ParameterMode.IN, name = "SUBSCRIPTION_KIT_ID", type = Long.class) 
		}
	)})
public class SubscriptionCourse {
	
	@Id
    @javax.persistence.TableGenerator(name = "SUBSCRIPTION_COURSE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SUBSCRIPTION_COURSE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SUBSCRIPTION_COURSE_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="SUBSCRIPTION_ID")
	private Subscription subscription ; //No reference for set method in project
	
	@OneToOne
    @JoinColumn(name="COURSE_ID")
	private Course course ; //No reference for set method in project
	
	@OneToOne
    @JoinColumn(name="COURSEGROUP_ID")
	private CourseGroup courseGroup ;  //No reference for set method in project
	
	@Column(name = "TOS")
	private Integer tos ;
	
	public SubscriptionCourse(){
		//this.subscription = new ValueHolder();;
	}
	
	//ID bigint NOT NULL,
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	//SUBSCRIPTION_ID bigint NOT NULL,
	public Subscription getSubscription() {
		return this.subscription;
	}
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	
	//COURSE_ID int NOT NULL,
	public Course getCourse() {
		return this.course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	//	COURSEGROUP_ID int NULL,
	public CourseGroup getCourseGroup() {
		return this.courseGroup;
	}
	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
	
	public Integer getTos() {
		return tos;
	}
	public void setTos(Integer tos) {
		this.tos = tos;
	}
	
	
}
