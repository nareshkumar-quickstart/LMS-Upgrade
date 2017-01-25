/**
 * 
 */
package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author sana.majeed
 *
 */
public class InstructorItemForm  implements ILMSBaseInterface, Comparable<InstructorItemForm> {
	private static final long serialVersionUID = 1L;
	private boolean isSelected = false;
	private Instructor instructor = new Instructor();
	private String instructorType = null;
	private Long id = null;

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(InstructorItemForm other) {
		return this.instructor.getId().compareTo(other.getInstructor().getId());		
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param instructor the instructor to set
	 */
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	/**
	 * @return the instructor
	 */
	public Instructor getInstructor() {
		return instructor;
	}

	/**
	 * @param instructorType the instructorType to set
	 */
	public void setInstructorType(String instructorType) {
		this.instructorType = instructorType;
	}

	/**
	 * @return the instructorType
	 */
	public String getInstructorType() {
		return instructorType;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

}
