package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "COURSE_CUSTOMERENTITLEMENT")
public class CourseCustomerEntitlementItem  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "COURSE_CUSTOMERENTITLEMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COURSE_CUSTOMERENTITLEMENT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSE_CUSTOMERENTITLEMENT_ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "COURSE_ID")
	private Course course;
	
	@OneToOne
	@JoinColumn(name = "COURSEGROUP_ID")
	private CourseGroup courseGroup;
	
	@OneToOne
	@JoinColumn(name = "CUSTOMERENTITLEMENT_ID")
	private CustomerEntitlement customerEntitlement;
	
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public CourseGroup getCourseGroup() {
		return courseGroup;
	}
	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
	public CustomerEntitlement getCustomerEntitlement() {
		return customerEntitlement;
	}
	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result
				+ ((courseGroup == null) ? 0 : courseGroup.hashCode());
		result = prime
				* result
				+ ((customerEntitlement == null) ? 0 : customerEntitlement
						.hashCode());
		return result;
	}
	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseCustomerEntitlementItem other = (CourseCustomerEntitlementItem) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (courseGroup == null) {
			if (other.courseGroup != null)
				return false;
		} else if (!courseGroup.equals(other.courseGroup))
			return false;
		if (customerEntitlement == null) {
			if (other.customerEntitlement != null)
				return false;
		} else if (!customerEntitlement.equals(other.customerEntitlement))
			return false;
		return true;
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
	
    	
}