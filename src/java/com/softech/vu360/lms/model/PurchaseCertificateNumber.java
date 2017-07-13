/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PURCHASECERTIFICATE")
public class PurchaseCertificateNumber  implements SearchableKey{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PURCHASECERTIFICATE_ID")
	@GenericGenerator(name = "PURCHASECERTIFICATE_ID", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "PURCHASECERTIFICATE") })
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "CERTIFICATENUMBER")
	private String certificateNumber;
	
	@Column(name = "ISUSED")
	private Boolean isUsed;
	
	@Transient
	private String used;
	
	@Column(name = "numericCertificateNumber")
	private long  numericCertificateNumber;
//	private ValueHolderInterface courseApproval ;

	@ManyToOne
	@JoinTable(name="COURSEAPPROVAL_PURCHASECERTIFICATE", joinColumns = @JoinColumn(name="PURCHASECERTIFICATENUMBERID"),inverseJoinColumns = @JoinColumn(name="COURSEAPPROVALID"))
	private CourseApproval courseApproval;
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
	 * @return the certificateNumber
	 */
	public String getCertificateNumber() {
		return certificateNumber;
	}




	/**
	 * @param certificateNumber the certificateNumber to set
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}




	/**
	 * @return the isUsed
	 */
	public  Boolean isUsed() {
		return isUsed;
	}




	/**
	 * @param isUsed the isUsed to set
	 */
	public void setUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	
	

	/**
	 * @return the used
	 */
	public String getUsed() {
		if(isUsed)
			return "Yes";
		else
			return "No";
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(String used) {
		this.used = used;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}

	public long getNumericCertificateNumber() {
		return numericCertificateNumber;
	}

	public void setNumericCertificateNumber(long numericCertificateNumber) {
		this.numericCertificateNumber = numericCertificateNumber;
	}

    public PurchaseCertificateNumber clone() {
        PurchaseCertificateNumber clone = new PurchaseCertificateNumber();
        clone.setId(this.getId());
        clone.setCertificateNumber(this.getCertificateNumber());
        clone.setNumericCertificateNumber(this.getNumericCertificateNumber());
        clone.setUsed(this.isUsed());
        clone.setUsed(this.getUsed());
        return clone;
    }

	public CourseApproval getCourseApproval() {
		return courseApproval;
	}

	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if(obj == null){
			isEqual = false;
		}
		if(this.getCertificateNumber().equals( ((PurchaseCertificateNumber)obj).getCertificateNumber()) &&
		   this.getCourseApproval().getId() == 	((PurchaseCertificateNumber)obj).getCourseApproval().getId()){
			isEqual = true;
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		int purchaseNumberHash = this.getCertificateNumber().hashCode() + this.getCourseApproval().getId().hashCode();
		return purchaseNumberHash;
	}
}
