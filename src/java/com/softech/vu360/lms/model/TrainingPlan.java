/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "TRAINIINGPLAN")
public class TrainingPlan implements Serializable {
	
	private static final long serialVersionUID = 1975011069400996251L;

	@Id
    @javax.persistence.TableGenerator(name = "TRAINIINGPLAN_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "TRAINIINGPLAN", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TRAINIINGPLAN_ID")
	private Long id;
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="TRAININGPLANCOURSE_TRAININGPLAN", joinColumns = @JoinColumn(name="TRAININGPLAN_ID"),inverseJoinColumns = @JoinColumn(name="TRAININGPLANCOURSE_ID"))
    private List<TrainingPlanCourse> courses = new ArrayList<TrainingPlanCourse>();
	
	@OneToOne
    @JoinColumn(name="CUSTOMER_ID")
	private Customer customer = null;
	
	@Column(name = "NAME")
	private String name = null;
	
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@Column(name = "STARTDATE")
	private Date startdate = null;
	
	@Column(name = "ENDDATE")
	private Date enddate = null;
	
	/*this field will hold the count of training courses that are actually entitled to the customer*/
	@Transient
	private Integer entitledCourseCount;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public List<TrainingPlanCourse> getCourses() {
		return courses;
	}
	
	// [1/27/2011] LMS-7183 :: Retired Course Functionality II
	public List<TrainingPlanCourse> getActiveCourses() {
		List<TrainingPlanCourse> tpCourses = new ArrayList<TrainingPlanCourse>();
		for (TrainingPlanCourse tpCourse : this.courses) {
			if (tpCourse.getCourse().isActive()) {
				tpCourses.add(tpCourse);
			}
		}				
		return tpCourses;
	}

	public void setCourses(List<TrainingPlanCourse> courses) {
		this.courses = courses;
	}

	public Integer getEntitledCourseCount() {
		return entitledCourseCount;
	}

	public void setEntitledCourseCount(Integer entitledCourseCount) {
		this.entitledCourseCount = entitledCourseCount;
	}
}
