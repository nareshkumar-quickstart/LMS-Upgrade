package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "COURSEVIEWFORENROLLLEARNER")
@SqlResultSetMappings({
	@SqlResultSetMapping(
		name = "getCoursesForEnrollmentWithPagingMapping", 
		classes = @ConstructorResult(
			targetClass = EnrollmentCourseView.class, 
			columns = {
				@ColumnResult(name = "COURSENAME"), 
				@ColumnResult(name = "COURSE_ID", type = Long.class), 
				@ColumnResult(name = "COURSETYPE"),
				@ColumnResult(name = "COURSECODE"), 
				@ColumnResult(name = "TOTAL_SEATS", type = BigInteger.class), 
				@ColumnResult(name = "NUMBERSEATSUSED"),
				@ColumnResult(name = "SEATS_REMAINING", type = BigInteger.class), 
				@ColumnResult(name = "EXPIRATION_DATE"), 
				@ColumnResult(name = "CUSTOMERENTITLEMENT_ID", type = Long.class),
				@ColumnResult(name = "TOS"), 
				@ColumnResult(name = "ALLOWUNLIMITEDENROLLMENTS"),
				@ColumnResult(name = "ENTITLEMENT_ID", type = Long.class),
				@ColumnResult(name = "STARTDATE"), 
				@ColumnResult(name = "ENDDATE"), 
				@ColumnResult(name = "ENTITLEMENT_NAME"),
				@ColumnResult(name = "CUSTOMER_ID", type = Long.class), 
				@ColumnResult(name = "COURSEGROUP_ID", type = Long.class),
				@ColumnResult(name = "TOTALRECORDS")
			}
		) 
	),
	@SqlResultSetMapping(
		name = "getCoursesForEnrollmentMapping", 
		classes = @ConstructorResult(
			targetClass = EnrollmentCourseView.class, 
				columns = {
					@ColumnResult(name = "COURSENAME"), 
					@ColumnResult(name = "COURSE_ID", type = Long.class), 
					@ColumnResult(name = "COURSETYPE"),
					@ColumnResult(name = "COURSECODE"), 
					@ColumnResult(name = "TOTAL_SEATS", type = BigInteger.class), 
					@ColumnResult(name = "NUMBERSEATSUSED"),
					@ColumnResult(name = "SEATS_REMAINING", type = BigInteger.class), 
					@ColumnResult(name = "EXPIRATION_DATE"), 
					@ColumnResult(name = "CUSTOMERENTITLEMENT_ID", type = Long.class),
					@ColumnResult(name = "TOS"), 
					@ColumnResult(name = "ALLOWUNLIMITEDENROLLMENTS"),
					@ColumnResult(name = "ENTITLEMENT_ID", type = Long.class),
					@ColumnResult(name = "STARTDATE"), 
					@ColumnResult(name = "ENDDATE"), 
					@ColumnResult(name = "ENTITLEMENT_NAME"),
					@ColumnResult(name = "CUSTOMER_ID", type = Long.class), 
					@ColumnResult(name = "COURSEGROUP_ID", type = Long.class)
			}
		) 
	)
})
public class EnrollmentCourseView implements Serializable, Comparable<EnrollmentCourseView> {

	private static final long serialVersionUID = 1L;

	private String courseName;
	private Long courseId;
	private String courseType;
	private String courseCode;
	private Long totalSeats;
	private Integer seatsUsed;
	private Long seatsRemaining;
	private Date expirationDate;
	private Long customerEntitlementId;
	private Integer termsOfService;
	private Integer unlimitedEnrollments;
	private Long entitlementId;
	private Date entitlementStartDate;
	private Date entitlementEndDate;
	private String entitlementName;
	private Long customerId;
	private Long courseGroupId;
	private Long enrollmentId;
	private String businessKey;
	private String alphaNumeicCourseId;
	private  Boolean selected = false;
	private String enrollmentStartDate;
	private String enrollmentEndDate;
	private List<SynchronousClass> syncClasses;
	private Integer totalRecords;
	
	public EnrollmentCourseView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EnrollmentCourseView(String courseName, Long courseId, String courseType, String courseCode, BigInteger totalSeats,
			Integer seatsUsed, BigInteger seatsRemaining, Date expirationDate, Long customerEntitlementId,
			Integer termsOfService, Integer unlimitedEnrollments, Long entitlementId, Date entitlementStartDate,
			Date entitlementEndDate, String entitlementName, Long customerId, Long courseGroupId,
			Integer totalRecords) {
		super();
		this.courseName = courseName;
		this.courseId = courseId;
		this.courseType = courseType;
		this.courseCode = courseCode;
		if (totalSeats != null) {
			this.totalSeats = totalSeats.longValue();
		}
		
		this.seatsUsed = seatsUsed;
		if (seatsRemaining != null) {
			this.seatsRemaining = seatsRemaining.longValue();
		}
		
		this.expirationDate = expirationDate;
		this.customerEntitlementId = customerEntitlementId;
		this.termsOfService = termsOfService;
		this.unlimitedEnrollments = unlimitedEnrollments;
		this.entitlementId = entitlementId;
		this.entitlementStartDate = entitlementStartDate;
		this.entitlementEndDate = entitlementEndDate;
		this.entitlementName = entitlementName;
		this.customerId = customerId;
		this.courseGroupId = courseGroupId;
		this.totalRecords = totalRecords;
	}
	
	public EnrollmentCourseView(String courseName, Long courseId, String courseType, String courseCode, BigInteger totalSeats,
			Integer seatsUsed, BigInteger seatsRemaining, Date expirationDate, Long customerEntitlementId,
			Integer termsOfService, Integer unlimitedEnrollments, Long entitlementId, Date entitlementStartDate,
			Date entitlementEndDate, String entitlementName, Long customerId, Long courseGroupId) {
		super();
		this.courseName = courseName;
		this.courseId = courseId;
		this.courseType = courseType;
		this.courseCode = courseCode;
		if (totalSeats != null) {
			this.totalSeats = totalSeats.longValue();
		}
		
		this.seatsUsed = seatsUsed;
		if (seatsRemaining != null) {
			this.seatsRemaining = seatsRemaining.longValue();
		}
		
		this.expirationDate = expirationDate;
		this.customerEntitlementId = customerEntitlementId;
		this.termsOfService = termsOfService;
		this.unlimitedEnrollments = unlimitedEnrollments;
		this.entitlementId = entitlementId;
		this.entitlementStartDate = entitlementStartDate;
		this.entitlementEndDate = entitlementEndDate;
		this.entitlementName = entitlementName;
		this.customerId = customerId;
		this.courseGroupId = courseGroupId;
	}

	// [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses
	// irrespective of contract and enrollments availability
	public int compareTo(EnrollmentCourseView enrollmentCourseView) {
		return this.getEnrollmentCourseId().compareToIgnoreCase(enrollmentCourseView.getEnrollmentCourseId());
	}

	/**
	 * // [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing
	 * courses irrespective of contract and enrollments availability This method
	 * is introduced to uniquely identify each course and contract combination
	 * because one course could be in multiple contracts and vice-versa
	 * 
	 * @return the enrollmentCourseId
	 */
	@Transient
	public String getEnrollmentCourseId() {
		return (this.entitlementId + "-" + this.courseId);
	}

	@Nationalized
	@Column(name = "COURSENAME")
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Id
	@Column(name = "COURSE_ID")
	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	@Column(name = "TOTAL_SEATS")
	public Long getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Long totalSeats) {
		this.totalSeats = totalSeats;
	}

	@Column(name = "NUMBERSEATSUSED")
	public Integer getSeatsUsed() {
		return seatsUsed;
	}

	public void setSeatsUsed(Integer seatsUsed) {
		this.seatsUsed = seatsUsed;
	}

	@Column(name = "SEATS_REMAINING")
	public Long getSeatsRemaining() {
		return seatsRemaining;
	}

	public void setSeatsRemaining(Long seatsRemaining) {
		this.seatsRemaining = seatsRemaining;
	}

	@Column(name = "EXPIRATION_DATE")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Column(name = "CUSTOMERENTITLEMENT_ID")
	public Long getCustomerEntitlementId() {
		return customerEntitlementId;
	}

	public void setCustomerEntitlementId(Long customerEntitlementId) {
		this.customerEntitlementId = customerEntitlementId;
	}

	@Column(name = "TOS")
	public Integer getTermsOfService() {
		return termsOfService;
	}

	public void setTermsOfService(Integer termsOfService) {
		this.termsOfService = termsOfService;
	}

	@Column(name = "ALLOWUNLIMITEDENROLLMENTS")
	public Integer getUnlimitedEnrollments() {
		return unlimitedEnrollments;
	}

	public void setUnlimitedEnrollments(Integer unlimitedEnrollments) {
		this.unlimitedEnrollments = unlimitedEnrollments;
	}

	@Column(name = "ENTITLEMENT_ID")
	public Long getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Long entitlementId) {
		this.entitlementId = entitlementId;
	}

	@Column(name = "STARTDATE")
	public Date getEntitlementStartDate() {
		return entitlementStartDate;
	}

	public void setEntitlementStartDate(Date entitlementStartDate) {
		this.entitlementStartDate = entitlementStartDate;
	}

	@Column(name = "ENDDATE")
	public Date getEntitlementEndDate() {
		return entitlementEndDate;
	}

	public void setEntitlementEndDate(Date entitlementEndDate) {
		this.entitlementEndDate = entitlementEndDate;
	}

	@Column(name = "ENTITLEMENT_NAME")
	public String getEntitlementName() {
		return entitlementName;
	}

	public void setEntitlementName(String entitlementName) {
		this.entitlementName = entitlementName;
	}

	@Column(name = "CUSTOMER_ID")
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Column(name = "COURSEGROUP_ID")
	public Long getCourseGroupId() {
		return courseGroupId;
	}

	public void setCourseGroupId(Long courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public Long getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	@Transient
	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	@Transient
	public String getAlphaNumeicCourseId() {
		return alphaNumeicCourseId;
	}

	public void setAlphaNumeicCourseId(String alphaNumeicCourseId) {
		this.alphaNumeicCourseId = alphaNumeicCourseId;
	}

	public  Boolean isSelected() {
		return selected;
	}

	public  Boolean getSelected() {
		return selected;
	}
	
	public void setSelected( Boolean selected) {
		this.selected = selected;
	}

	public String getEnrollmentStartDate() {
		return enrollmentStartDate;
	}

	public void setEnrollmentStartDate(String enrollmentStartDate) {
		this.enrollmentStartDate = enrollmentStartDate;
	}

	public String getEnrollmentEndDate() {
		return enrollmentEndDate;
	}

	public void setEnrollmentEndDate(String enrollmentEndDate) {
		this.enrollmentEndDate = enrollmentEndDate;
	}

	@Transient
	public List<SynchronousClass> getSyncClasses() {
		return syncClasses;
	}

	public void setSyncClasses(List<SynchronousClass> syncClasses) {
		this.syncClasses = syncClasses;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}