package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "DISTRIBUTORENTITLEMENT")
public class DistributorEntitlement implements SearchableKey, Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "DISTRIBUTORENTITLEMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "DISTRIBUTORENTITLEMENT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DISTRIBUTORENTITLEMENT_ID")
	private Long id;
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="COURSEGROUP_DISTRIBUTORENTITLEMENT", joinColumns = @JoinColumn(name="DISTRIBUTORENTITLEMENT_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="COURSEGROUP_ID",referencedColumnName="ID"))
    private List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DISTRIBUTOR_ID")
	private Distributor distributor ;
	
	@Column(name = "NAME")
	private String name = null;
	
	@Column(name = "SEATS")
	private Integer maxNumberSeats = 0;
	
	@Column(name = "NUMBERSEATSUSED")
	private Integer numberSeatsUsed = 0;
	
	@Column(name = "NUMBERDAYS")
	private Integer defaultTermOfServiceInDays = 0;
	
	@Column(name = "ENDDATE")
	private Date endDate = null;
	
	@Column(name = "STARTDATE")
	private Date startDate = null;
	
	@Column(name = "ALLOWUNLIMITEDENROLLMENTS")
	private Boolean allowUnlimitedEnrollments = false;
	
	@Column(name = "ALLOWSELFENROLLMENTTF")
	private Boolean allowSelfEnrollment = false;
	
	@Column(name = "TRANSACTION_AMOUNT")
	private Double transactionAmount = 0.0;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the CourseGroups
	 */
	public List<CourseGroup> getCourseGroups() {
		return courseGroups;
	}

	/**
	 * @param courseGroup
	 *            the course to add to the courses set
	 */
	public void addCourseGroup(CourseGroup courseGroup) {
		courseGroups.add(courseGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the distributor
	 */
	public Distributor getDistributor() {
		return this.distributor;
	}

	/**
	 * @param distributor
	 *            the distributor to set
	 */
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param courseGroups
	 *            the courseGroups to set
	 */
	public void setCourseGroups(List<CourseGroup> courseGroups) {
		this.courseGroups = courseGroups;
	}

	/**
	 * @return the maxNumberSeats
	 */
	public Integer getMaxNumberSeats() {
		return maxNumberSeats;
	}

	/**
	 * @param maxNumberSeats
	 *            the maxNumberSeats to set
	 */
	public void setMaxNumberSeats(Integer maxNumberSeats) {
		this.maxNumberSeats = maxNumberSeats;
	}

	/**
	 * @return the numberSeatsUsed
	 */
	public Integer getNumberSeatsUsed() {
		return numberSeatsUsed;
	}

	/**
	 * @param numberSeatsUsed
	 *            the numberSeatsUsed to set
	 */
	public void setNumberSeatsUsed(Integer numberSeatsUsed) {
		this.numberSeatsUsed = numberSeatsUsed;
	}

	/**
	 * @return the defaultTermOfServiceInDays
	 */
	public Integer getDefaultTermOfServiceInDays() {
		return defaultTermOfServiceInDays;
	}

	/**
	 * @param defaultTermOfServiceInDays
	 *            the defaultTermOfServiceInDays to set
	 */
	public void setDefaultTermOfServiceInDays(Integer defaultTermOfServiceInDays) {
		this.defaultTermOfServiceInDays = defaultTermOfServiceInDays;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the allowUnlimitedEnrollments
	 */
	public  Boolean isAllowUnlimitedEnrollments() {
		return allowUnlimitedEnrollments;
	}

	/**
	 * @param allowUnlimitedEnrollments
	 *            the allowUnlimitedEnrollments to set
	 */
	public void setAllowUnlimitedEnrollments(Boolean allowUnlimitedEnrollments) {
		this.allowUnlimitedEnrollments = allowUnlimitedEnrollments;
	}

	/**
	 * @return the allowSelfEnrollment
	 */
	public  Boolean isAllowSelfEnrollment() {
		return allowSelfEnrollment;
	}

	/**
	 * @param allowSelfEnrollment
	 *            the allowSelfEnrollment to set
	 */
	public void setAllowSelfEnrollment(Boolean allowSelfEnrollment) {
		this.allowSelfEnrollment = allowSelfEnrollment;
	}

	/**
	 * @return the transactionAmount
	 */
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	/**
	 * @param transactionAmount the transactionAmount to set
	 */
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

}