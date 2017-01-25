/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.HashSet;
import java.util.Set;

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
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@Table(name = "CREDITREPORTINGFIELD")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="FIELDTYPE")
public class CreditReportingField implements SearchableKey,Comparable<CreditReportingField> {

	private static final long serialVersionUID = -239317351448928033L;
	public static final String HORIZONTAL = "horizonatl";
	public static final String VERTICAL = "vertical";
	
	@Id
	@javax.persistence.TableGenerator(name = "CREDITREPORTINGFIELD_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMREPORTINGFIELD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDITREPORTINGFIELD_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="CONTENTOWNER_ID")
	private ContentOwner contentOwner ;
	
	@Column(name = "FIELDENCRYPTEDTF")
	private Boolean fieldEncrypted = false;
	
	@Column(name = "FIELDLABEL")
	private String fieldLabel = null;
	
	@Column(name = "FIELDREQUIREDTF")
	private Boolean fieldRequired = false;
	
	@Column(name = "FIELDTYPE" , insertable=false, updatable=false)
	//@Transient
	private String fieldType = null;
	/* Weight variable determines sort order of the reporting field*/
	
	@Transient
    private Integer weight= 0;
    
	@Column(name = "DESCRIPTION")
	private String customFieldDescription = null;
	
	@Column(name = "ALIGNMENT")
	private String alignment = HORIZONTAL;
	
	@Column(name = "active")
	private Boolean active = true;
		
	// This field is not being used by any class
	@Transient
	private Set <CourseApproval> containingCourseApprovalSet=new HashSet<CourseApproval>();
	
	@Column(name = "HOSTNAME")
	private String hostName = getHostName();
	
	
	public CreditReportingField()
	{
		setHostName(getHostName());
	}
	
	public String getHostName() {
		try
		{
			String CompleteURL = null;
			
			HttpServletRequest request = null;
			if(RequestContextHolder.getRequestAttributes() !=null )
				request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			
			CompleteURL =(request!=null && request.getRequestURL()!=null) ? request.getRequestURL().toString():null;
			
			/*
			String queryString = null;
			
			if(request.getQueryString()!=null)
				queryString = request.getQueryString();
						
			
			if (queryString != null) {		        
				CompleteURL = CompleteURL+'?'+queryString;
		    }
			*/
			
			return CompleteURL;
		}
		catch( Exception ex)
		{
			return null;			
		}
	}
	
	public void setHostName(String hostName)
	{
		try
		{
			String CompleteURL = null;
			
			HttpServletRequest request = null;
			if(RequestContextHolder.getRequestAttributes() !=null )
				request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			
			CompleteURL =(request!=null && request.getRequestURL()!=null) ? request.getRequestURL().toString():null;
			/*
			String queryString = null;
			
			if(request.getQueryString()!=null)
				queryString = request.getQueryString();
						
			
			if (queryString != null) {		        
				CompleteURL = CompleteURL+'?'+queryString;
		    }
		    */
			
			this.hostName = CompleteURL;
		}
		catch( Exception ex)
		{
			this.hostName = null;
		}
	}
	
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the id
	 */
	public Long getId() {	
		setHostName(null);
		return id;		
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {		
		this.id = id;		
		setHostName(null);
	}
	/**
	 * @return the fieldEncrypted
	 */
	public  Boolean isFieldEncrypted() {
		return fieldEncrypted;
	}
	/**
	 * @param fieldEncrypted the fieldEncrypted to set
	 */
	public void setFieldEncrypted(Boolean fieldEncrypted) {
		this.fieldEncrypted = fieldEncrypted;
	}
	/**
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}
	/**
	 * @param fieldLabel the fieldLabel to set
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	/**
	 * @return the fieldRequired
	 */
	public  Boolean isFieldRequired() {
		return fieldRequired;
	}
	/**
	 * @param fieldRequired the fieldRequired to set
	 */
	public void setFieldRequired(Boolean fieldRequired) {
		this.fieldRequired = fieldRequired;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		setHostName(null);
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	/**
	 * @return the customFieldDescription
	 */
	public String getCustomFieldDescription() {
		return customFieldDescription;
	}
	/**
	 * @param customFieldDescription the customFieldDescription to set
	 */
	public void setCustomFieldDescription(String customFieldDescription) {
		this.customFieldDescription = customFieldDescription;
	}
	/**
	 * @return the alignment
	 */
	public String getAlignment() {
		return alignment;
	}
	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}
	/**
	 * @return the active
	 */
	public  Boolean isActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	public ContentOwner getContentOwner() {
		return this.contentOwner;
	}
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}
	
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}	
		
	public Set<CourseApproval> getContainingCourseApprovalSet() {
		return containingCourseApprovalSet;
	}
	public void setContainingCourseApprovalSet(
			Set<CourseApproval> containingCourseApprovalSet) {
		this.containingCourseApprovalSet = containingCourseApprovalSet;
	}
	
	public Boolean getFieldEncrypted() {
		return fieldEncrypted;
	}

	public Boolean getFieldRequired() {
		return fieldRequired;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		ContentOwner localContentOwner=  this.contentOwner; //  (ContentOwner)contentOwner.getValue();
		result = prime * result
				+ ((localContentOwner == null || localContentOwner.getId()==null) ? 0 : localContentOwner.hashCode());
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
		CreditReportingField other = (CreditReportingField) obj;
		if (contentOwner == null) {
			if (other.contentOwner != null)
				return false;
		} else if (!contentOwner.equals(other.contentOwner))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public int compareTo(CreditReportingField field2) {
		return (this.weight<=field2.weight)?1:-1;				
	}

}
