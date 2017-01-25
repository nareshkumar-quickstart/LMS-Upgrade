/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author haider.ali,
 * @dated: 19-09-15
 * @changed muhammad.rehan
 * @test muhammad.rehan
 * @dated: 24 Nov, 2015
 */

@Entity
@Table(name = "INSTRUCTOR_COURSE")
public class InstructorCourse implements SearchableKey {
	
	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "INSTRUCTOR_COURSE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "INSTRUCTOR_COURSE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "INSTRUCTOR_COURSE_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@OneToOne
    @JoinColumn(name = "COURSE_ID")
	private Course course ;
	
	@OneToOne
	@JoinColumn(name = "INSTRUCTOR_ID")
	private Instructor instructor ;
	
	@Column(name="instructorType")
	private String instructorType = null;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	public String getInstructorType() {
		return instructorType;
	}
	public void setInstructorType(String instructorType) {
		this.instructorType = instructorType;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString() {
		return "InstructorCourse [id=" + id + ", course=" + course
				+ ", instructor=" + instructor + ", instructorType="
				+ instructorType + "]";
	}

	

}
