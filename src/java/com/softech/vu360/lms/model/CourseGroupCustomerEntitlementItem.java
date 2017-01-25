package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 * @modifier Raja Wajahat Ali @Date 2016-01-25
 *
 */
@Entity
@Table(name = "COURSEGROUP_CUSTOMERENTITLEMENT")
@IdClass(CourseGroupCustomerEntitlementItemPK.class)
public class CourseGroupCustomerEntitlementItem  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public CourseGroupCustomerEntitlementItem() {
	}
    
	/*@Id
	@javax.persistence.TableGenerator(name = "COURSEGROUP_CUSTOMERENTITLEMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "coursegroup_customerentitlement", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSEGROUP_CUSTOMERENTITLEMENT_ID")
	@Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
	@OneToOne
	@JoinColumn(name = "COURSEGROUP_ID")
	private CourseGroup courseGroup ;
	
	@OneToOne
	@JoinColumn(name = "CUSTOMERENTITLEMENT_ID")
	private CustomerEntitlement customerEntitlement ;
	*/
	
	
	@Id
	@ManyToOne
	@JoinColumn(name = "COURSEGROUP_ID")
	private CourseGroup courseGroup ;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "CUSTOMERENTITLEMENT_ID")
	private CourseGroupCustomerEntitlement customerEntitlement ;
	
	
    /*public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}*/
	
	public CourseGroup getCourseGroup() {
		return this.courseGroup;
	}
	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
	public CourseGroupCustomerEntitlement getCourseGroupCustomerEntitlement() {
		return this.customerEntitlement;
	}
	public void setCourseGroupCustomerEntitlement(CourseGroupCustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}
	
    	
}