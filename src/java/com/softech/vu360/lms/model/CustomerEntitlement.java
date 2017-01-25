package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.time.DateUtils;

import com.softech.vu360.util.Brander;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@Table(name = "CUSTOMERENTITLEMENT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ENROLLMENTTYPE")
public class CustomerEntitlement implements SearchableKey//,SearchableCourses 
{
	private static final long serialVersionUID = 1L;
	public static final String COURSE_ENROLLMENTTYPE = "Course";
    public static final String COURSEGROUP_ENROLLMENTTYPE = "CourseGroup";
    
    @Id
	@javax.persistence.TableGenerator(name = "CUSTOMERENTITLEMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMERENTITLEMENT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMERENTITLEMENT_ID")
	private Long id;
    
    @Column(name = "NAME")
    private String name = null;
    
    @OneToOne
	@JoinColumn(name="CUSTOMER_ID")
    private Customer customer ;
    
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
    
    //@Column(name = "ENROLLMENTTYPE") ---- No need to map DiscriminatorColumn on attribute Level. DiscrimatorColumn will automatically map the value.
    @Transient
    private String enrollmentType = null;
    
    @Column(name = "ISSYSTEMMANAGEDTF")
    private Boolean isSystemManaged=false;

    
    @Column(name = "TRANSACTION_AMOUNT")
    private Double transactionAmount = 0.0;
    
    @Column(name = "CONTRACTCREATIONDATE")
    private Date contractCreationDate = null;
    
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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return id.toString();
    }




    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the maxNumberSeats
     */
    public Integer getMaxNumberSeats() {
        return maxNumberSeats;
    }

    /**
     * @param maxNumberSeats the maxNumberSeats to set
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
     * @param numberSeatsUsed the numberSeatsUsed to set
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
     * @param defaultTermOfServiceInDays the defaultTermOfServiceInDays to set
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
     * @param endDate the endDate to set
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
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the allowUnlimitedEnrollments
     */
    public  Boolean isAllowUnlimitedEnrollments() {
    	 if(allowUnlimitedEnrollments==null){
    		 allowUnlimitedEnrollments=Boolean.FALSE;
    	    }

        return allowUnlimitedEnrollments;
    }

    /**
     * @param allowUnlimitedEnrollments the allowUnlimitedEnrollments to set
     */
    public void setAllowUnlimitedEnrollments(Boolean allowUnlimitedEnrollments) {
        this.allowUnlimitedEnrollments = allowUnlimitedEnrollments;
    }

    /**
     * @return the allowSelfEnrollment
     */
    public  Boolean isAllowSelfEnrollment() {
    	 if(allowSelfEnrollment==null){
    		 allowSelfEnrollment=Boolean.FALSE;
    	    }
        return allowSelfEnrollment;
    }

    /**
     * @param allowSelfEnrollment the allowSelfEnrollment to set
     */
    public void setAllowSelfEnrollment(Boolean allowSelfEnrollment) {
        this.allowSelfEnrollment = allowSelfEnrollment;
    }

    public  Boolean hasAvailableSeats(Integer numSeatsRequesting) {
        // do the business logic of checking if this entitlement may be used
        Date today = new Date(System.currentTimeMillis());
        // check dates
        if (this.startDate != null) {
            if (this.startDate.after(today)) {
                return false;
            }
        }
        if (this.endDate != null) {
            if (this.endDate.before(today)) {
                return false;
            }
        }
        // check available seats
        return allowUnlimitedEnrollments ||
                ((this.maxNumberSeats - this.numberSeatsUsed) >= numSeatsRequesting);
    }

    public Date getEntitlementEndDate() {
        Date temp = endDate;
        if (temp == null) {
            temp=DateUtils.addDays(startDate, defaultTermOfServiceInDays);
		}
		return temp;
	}

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("CustomerEntitlement\n").append("ID: ").append(getId()).append("\n")
                .append("Name: ").append(getName()).append("\n")
                .append("Customer: ").append(getCustomer()).append("\n")
                .append("Key: ").append(getKey()).append("\n")
                .append("Start Date: ").append(getStartDate()).append("\n")
                .append("End Date: ").append(getEndDate()).append("\n");
        	
        return sb.toString();
    }

	public String getEnrollmentType() {
		return enrollmentType;
	}

	public void setEnrollmentType(String enrollmentType) {
		this.enrollmentType = enrollmentType;
	}
	/*
	 * done
	 */
	public Set<Course> getUniqueCourses(){
		Set<Course> courses = new HashSet<Course>();
		return courses;
	}
	protected List<CourseCustomerEntitlementItem> getEntitlementItems(){
		return new ArrayList<CourseCustomerEntitlementItem>();
	}
	protected List<CourseGroup> getCourseGroups(){
		return new ArrayList<CourseGroup>();
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerEntitlement other = (CustomerEntitlement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getContractTYPLabel(Brander brander){
		String contractType = "UNKNOWN";
		if(this instanceof CourseCustomerEntitlement)
			contractType = brander.getBrandElement("lms.customercontract.contracttype.course");
		else if(this instanceof CourseGroupCustomerEntitlement){
			contractType = brander.getBrandElement("lms.customercontract.contracttype.coursegroup");
		}
		return contractType;
	}
	
	public Integer getRemainingSeats(){
		
		if(this.isAllowUnlimitedEnrollments())
			return 0;
		else
		{
			return (this.getMaxNumberSeats() - this.getNumberSeatsUsed());
		}

	}
	/*
	 * done
	 */
	public Set<CourseGroup> getListOfCourseGroups()
	{
		Set<CourseGroup> courseGroups = new HashSet<CourseGroup>();
		return courseGroups;
	}

	public  Boolean isSystemManaged() {
		return isSystemManaged;
	}

	public void setSystemManaged(Boolean isSystemManaged) {
		this.isSystemManaged = isSystemManaged;
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

	public Date getContractCreationDate() {
		return contractCreationDate;
	}

	public void setContractCreationDate(Date contractCreationDate) {
		this.contractCreationDate = contractCreationDate;
	}

	

}