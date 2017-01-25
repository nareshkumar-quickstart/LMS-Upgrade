package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author muhammad.saleem
 * updated By marium.saud
 *
 */
@Entity
@Table(name = "LEARNERENROLLMENT")
@NamedEntityGraph(name="LearnerEnrollment.findEnrollmentByLearnerAndEnrollmentStatus", 
attributeNodes={
	@NamedAttributeNode("id"),
	@NamedAttributeNode("enrollmentStatus"),
	@NamedAttributeNode("enrollmentDate"),
	@NamedAttributeNode("enrollmentStartDate"),
	@NamedAttributeNode("enrollmentEndDate"),
	@NamedAttributeNode("enrollmentRequiredCompletionDate"),
	@NamedAttributeNode("enableCertificate"),
	@NamedAttributeNode("skippedCourseApproval"),
	@NamedAttributeNode("LaunchInN3"),
	@NamedAttributeNode("marketoCompletion"),
	@NamedAttributeNode(value="courseStatistics", subgraph="courseStatisticsSubGraph"),
	@NamedAttributeNode(value="course", subgraph="courseSubGraph")
},
subgraphs={
		@NamedSubgraph(name="courseStatisticsSubGraph", attributeNodes={
	    	    @NamedAttributeNode("id"),
	    	    @NamedAttributeNode("status")
	    }),
	    @NamedSubgraph(name="courseSubGraph", attributeNodes={
	    	    @NamedAttributeNode("id"),
	    	    @NamedAttributeNode("courseStatus")
	    })
		}
)
public class LearnerEnrollment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String ACTIVE = "Active";
    public static final String EXPIRED = "Expired";
    public static final String SWAPPED = "Swapped";
    public static final String DROPPED = "Dropped";
    public static final String INACTIVE = "Inactive";
    public static final String UNLOCK = "Unlock";

    
    @Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqLearnerEnrollmentId")
	@GenericGenerator(name = "seqLearnerEnrollmentId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "LEARNERENROLLMENT") })
    private Long id;
    
    @Column(name = "ENROLLMENTSTATUS")
    private String enrollmentStatus = ACTIVE;

    @OneToOne(mappedBy="learnerEnrollment" , cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch=FetchType.LAZY) 
    private LearnerCourseStatistics courseStatistics;
    
    
    @OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="COURSE_ID")
    private Course course ;
    
    @OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNER_ID")
    private Learner learner ;
    
    @Column(name = "ENROLLMENTDATE")
    private Date enrollmentDate = null;
    
    @Column(name = "STARTDATE")
    private Date enrollmentStartDate = null;
    
    @Column(name = "ENDDATE")
    private Date enrollmentEndDate = null;
    
    @Column(name = "ENROLLMENTREQUIREDCOMPLETIONDATE")
    private Date enrollmentRequiredCompletionDate = null;
    
    @OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name = "CUSTOMERENTITLEMENT_ID")
    private CustomerEntitlement customerEntitlement ;

    @OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name = "ORGGROUPENTITLEMENT_ID")
    private OrgGroupEntitlement orgGroupEntitlement ;
    
    @OneToMany(mappedBy = "enrollment")
    private List<LockedCourse> lockedEnrollments = new ArrayList<LockedCourse>();
    
    @Column(name = "CERTIFICATETF")
    private Boolean enableCertificate;
    
    @OneToMany(mappedBy = "enrollment")
    private List<ProctorEnrollment> proctorEnrollment = new ArrayList<ProctorEnrollment>();
    
    @OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name = "SYNCHRONOUSCLASS_ID")
    private SynchronousClass synchronousClass ;
    
    @Column(name = "SKIPPED_COURSEAPPROVAL")
    private Boolean skippedCourseApproval=false;
    
    @OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name = "SUBSCRIPTION_ID")
    private Subscription subscription ;

    @Column(name = "LaunchInN3")
    private Boolean LaunchInN3 = false;
    
    @Column(name = "MARKETOCOMPLETIONTF")
    private Boolean marketoCompletion = Boolean.FALSE;

    @OneToMany(mappedBy = "learnerEnrollment")
    private List<LearnerHomeworkAssignmentSubmission> hwSubmission = new ArrayList<LearnerHomeworkAssignmentSubmission>();

    @Transient
    private LockedCourse lastLockedCourse = null; // field to hold the last
						  // LockedCourse info for this
						  // enrollment; should not be
						  // mapped to DB.
    
    public List<LearnerHomeworkAssignmentSubmission> getHwSubmission() {
	return hwSubmission;
    }

    public void setHwSubmission(List<LearnerHomeworkAssignmentSubmission> hwSubmissions) {
	this.hwSubmission = hwSubmissions;
    }

   

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

    /*
     * (non-Javadoc)
     * 
     * @see com.softech.vu360.lms.model.SearchableKey#getKey()
     */
    public String getKey() {
	return id.toString();
    }

    /**
     * @return the courseStatistics
     */
    public LearnerCourseStatistics getCourseStatistics() {
	return this.courseStatistics;
    }

    /**
     * @param courseStatistics
     *            the courseStatistics to set
     */
    public void setCourseStatistics(LearnerCourseStatistics courseStatistics) {
	this.courseStatistics = courseStatistics;
    }

    /**
     * @return the course
     */
    public Course getCourse() {
	return this.course;
    }

    /**
     * @param course
     *            the course to set
     */
    public void setCourse(Course course) {
	this.course = course;
    }

    /**
     * @return the enrollmentStatus
     */
    public String getEnrollmentStatus() {
	return enrollmentStatus;
    }

    /**
     * @param enrollmentStatus
     *            the enrollmentStatus to set
     */
    public void setEnrollmentStatus(String enrollmentStatus) {
	this.enrollmentStatus = enrollmentStatus;
    }

    /**
     * @return the learner
     */
    public Learner getLearner() {
	return this.learner;
    }

    /**
     * @param learner
     *            the learner to set
     */
    public void setLearner(Learner learner) {
	this.learner =  learner;
    }

    /**
     * @return the enrollmentDate
     */
    public Date getEnrollmentDate() {
	return enrollmentDate;
    }

    /**
     * @param enrollmentDate
     *            the enrollmentDate to set
     */
    public void setEnrollmentDate(Date enrollmentDate) {
	this.enrollmentDate = enrollmentDate;
    }

    /**
     * @return the customerEntitlement
     */
    public CustomerEntitlement getCustomerEntitlement() {
	return this.customerEntitlement;
    }

    /**
     * @param customerEntitlement
     *            the customerEntitlement to set
     */
    public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
	this.customerEntitlement = customerEntitlement;
    }

    public SynchronousClass getSynchronousClass() {
	return this.synchronousClass;
    }

    public void setSynchronousClass(SynchronousClass synchronousClass) {
	this.synchronousClass = synchronousClass;
    }

    /**
     * @return the orgGroupEntitlement
     */
    public OrgGroupEntitlement getOrgGroupEntitlement() {
	return this.orgGroupEntitlement;
    }

    /**
     * @param orgGroupEntitlement
     *            the orgGroupEntitlement to set
     */
    public void setOrgGroupEntitlement(OrgGroupEntitlement orgGroupEntitlement) {
	this.orgGroupEntitlement = orgGroupEntitlement;
    }

    /**
     * @return the enrollmentStartDate
     */
    public Date getEnrollmentStartDate() {
	return enrollmentStartDate;
    }

    /**
     * @param enrollmentStartDate
     *            the enrollmentStartDate to set
     */
    public void setEnrollmentStartDate(Date enrollmentStartDate) {
	this.enrollmentStartDate = enrollmentStartDate;
    }

    /**
     * @return the enrollmentEndDate
     */
    public Date getEnrollmentEndDate() {
	return enrollmentEndDate;
    }

    /**
     * @param enrollmentEndDate
     *            the enrollmentEndDate to set
     */
    public void setEnrollmentEndDate(Date enrollmentEndDate) {
	this.enrollmentEndDate = enrollmentEndDate;
    }

    /**
     * @return the enrollmentRequiredCompletionDate
     */
    public Date getEnrollmentRequiredCompletionDate() {
	return enrollmentRequiredCompletionDate;
    }

    /**
     * @param enrollmentRequiredCompletionDate
     *            the enrollmentRequiredCompletionDate to set
     */
    public void setEnrollmentRequiredCompletionDate(Date enrollmentRequiredCompletionDate) {
	this.enrollmentRequiredCompletionDate = enrollmentRequiredCompletionDate;
    }

    /**
     * @return the lockedEnrollments
     */
    public List<LockedCourse> getLockedEnrollments() {
	return lockedEnrollments;
    }

    /**
     * @param lockedEnrollments
     *            the lockedEnrollments to set
     */
    public void setLockedEnrollments(List<LockedCourse> lockedEnrollments) {
	this.lockedEnrollments = lockedEnrollments;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public  boolean equals(Object obj) {
	if (obj instanceof LearnerEnrollment) {
	    LearnerEnrollment other = (LearnerEnrollment) obj;
	    if (this == obj)
		return true;
	    else if (id == null && other.id == null)
		return true;
	    else if (this.id.equals(other.id))
		return true;
	}

	return false;
    }

    /**
     * @param lastLockedCourse
     *            the lastLockedCourse to set
     */
    public void setLastLockedCourse(LockedCourse lastLockedCourse) {
	this.lastLockedCourse = lastLockedCourse;
    }

    /**
     * @return the lastLockedCourse
     */
    public LockedCourse getLastLockedCourse() {
	return lastLockedCourse;
    }
    
    public LearnerEnrollment(Long id){
	this.id = id;
    }
    public LearnerEnrollment(){
	
    }

	public  Boolean isEnableCertificate() {
		return enableCertificate;
	}

	public void setEnableCertificate(Boolean enableCertificate) {
		this.enableCertificate = enableCertificate;
	}
	
	/**
	 * @return the proctorEnrollment
	 */
	public List<ProctorEnrollment> getProctorEnrollment() {
		return proctorEnrollment;
	}
	
	/**
	 * @param proctorEnrollment the proctorEnrollment to set
	 */
	public void setProctorEnrollment(List<ProctorEnrollment> proctorEnrollment) {
		this.proctorEnrollment = proctorEnrollment;
	}

	public  Boolean isLaunchInN3() {
		return LaunchInN3;
	}

	public void setLaunchInN3(Boolean launchInN3) {
		LaunchInN3 = launchInN3;
	}

    public Boolean getMarketoCompletion() {
		return marketoCompletion;
	}

	public void setMarketoCompletion(Boolean marketoCompletion) {
		this.marketoCompletion = marketoCompletion;
	}

	public Boolean getSkippedCourseApproval() {
		return skippedCourseApproval;
	}

	public void setSkippedCourseApproval(Boolean skippedCourseApproval) {
		this.skippedCourseApproval = skippedCourseApproval;
	}

	public Subscription getSubscription() {
		return this.subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public LearnerEnrollment(Date enrollmentStartDate,Long courseID, String courseGUID){
		this.enrollmentStartDate = enrollmentStartDate;
		
		Course course = new Course();
		course.setId(courseID);
		course.setCourseGUID(courseGUID);
		this.course = course;
	}
    
    
    
}
