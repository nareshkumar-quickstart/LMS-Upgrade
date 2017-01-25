/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;

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
@Table(name = "TRAININGPLANCOURSE")
public class TrainingPlanCourse implements Serializable {

	private static final long serialVersionUID = 1594685748480047945L;

	@Id
    @javax.persistence.TableGenerator(name = "TRAININGPLANCOURSE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "TrainingPlanCourse", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TRAININGPLANCOURSE_ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name="TRAININGPLAN_ID")
    private TrainingPlan trainingPlan = null;
	
	@OneToOne
    @JoinColumn(name="COURSE_ID")
	private Course course = null;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TrainingPlan getTrainingPlan() {
		return trainingPlan;
	}

	public void setTrainingPlan(TrainingPlan trainingPlan) {
		this.trainingPlan = trainingPlan;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
