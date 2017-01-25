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
 * @author muhammad.rehan,
 * @dated: 24 Nov, 2015
 */

@Entity
@Table(name = "INSTRUCTOR_SYNCHRONOUSCLASS")
public class InstructorSynchronousClass implements SearchableKey  {

	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "INSTRUCTOR_SYNCHRONOUSCLASS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "INSTRUCTOR_SYNCHRONOUSCLASS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "INSTRUCTOR_SYNCHRONOUSCLASS_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name="INSTRUCTORTYPE")
	private String instructorType = null;
	
	@OneToOne
	@JoinColumn(name = "SYNCHRONOUSCLASS_ID")
	private SynchronousClass synchronousClass ;
	
	@OneToOne
	@JoinColumn(name = "INSTRUCTOR_ID")
	private Instructor instructor ;
	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstructorType() {
		return instructorType;
	}
	public void setInstructorType(String instructorType) {
		this.instructorType = instructorType;
	}

	public SynchronousClass getSynchronousClass() {
		return synchronousClass;
	}

	public void setSynchronousClass(SynchronousClass synchronousClass) {
		this.synchronousClass = synchronousClass;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
}
