/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "CUSTOMFIELDCONTEXT")
public class CustomFieldContext implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "CUSTOMFIELDCONTEXT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMFIELDCONTEXT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMFIELDCONTEXT_ID")
	private Long id;
	
	@Column(name="GLOBALCONTACTTF")
	private Boolean globalContact = false;
	
	@Column(name="GLOBALCOURSEAPPROVALTF")
	private Boolean globalCourseApproval = false;
	
	@Column(name="GLOBALCREDENTIALTF")
	private Boolean globalCredential = false;
	
	@Column(name="GLOBALCREDENTIALREQUIREMENTTF")
	private Boolean globalCredentialRequirement = false;
	
	@Column(name="GLOBALINSTRUCTORTF")
	private Boolean globalInstructor = false;
	
	@Column(name="GLOBALINSTRACTORAPPROVALTF")
	private Boolean globalInstructorApproval = false;
	
	@Column(name="GLOBALPROVIDERTF")
	private Boolean globalProvider = false;
	
	@Column(name="GLOBALPROVIDERAPPROVALTF")
	private Boolean globalProviderApproval = false;
	
	@Column(name="GLOBALREGULATORTF")
	private Boolean globalRegulator = false;
	
	@Column(name="GLOBALCREDENTIALCATEGORYTF")
	private Boolean globalCredentialCategory = false;
	
	@OneToOne
    @JoinColumn(name="CONTENTOWNER_ID")
	private ContentOwner contentOwner = null;

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
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

	/**
	 * @return the globalContact
	 */
	public  Boolean isGlobalContact() {
		return globalContact;
	}

	/**
	 * @param globalContact the globalContact to set
	 */
	public void setGlobalContact(Boolean globalContact) {
		this.globalContact = globalContact;
	}

	/**
	 * @return the globalCourseApproval
	 */
	public  Boolean isGlobalCourseApproval() {
		return globalCourseApproval;
	}

	/**
	 * @param globalCourseApproval the globalCourseApproval to set
	 */
	public void setGlobalCourseApproval(Boolean globalCourseApproval) {
		this.globalCourseApproval = globalCourseApproval;
	}

	/**
	 * @return the globalCredential
	 */
	public  Boolean isGlobalCredential() {
		return globalCredential;
	}

	/**
	 * @param globalCredential the globalCredential to set
	 */
	public void setGlobalCredential(Boolean globalCredential) {
		this.globalCredential = globalCredential;
	}

	/**
	 * @return the globalInstructor
	 */
	public  Boolean isGlobalInstructor() {
		return globalInstructor;
	}

	/**
	 * @param globalInstructor the globalInstructor to set
	 */
	public void setGlobalInstructor(Boolean globalInstructor) {
		this.globalInstructor = globalInstructor;
	}

	/**
	 * @return the globalInstructorApproval
	 */
	public  Boolean isGlobalInstructorApproval() {
		return globalInstructorApproval;
	}

	/**
	 * @param globalInstructorApproval the globalInstructorApproval to set
	 */
	public void setGlobalInstructorApproval(Boolean globalInstructorApproval) {
		this.globalInstructorApproval = globalInstructorApproval;
	}

	/**
	 * @return the globalProvider
	 */
	public  Boolean isGlobalProvider() {
		return globalProvider;
	}

	/**
	 * @param globalProvider the globalProvider to set
	 */
	public void setGlobalProvider(Boolean globalProvider) {
		this.globalProvider = globalProvider;
	}

	/**
	 * @return the globalProviderApproval
	 */
	public  Boolean isGlobalProviderApproval() {
		return globalProviderApproval;
	}

	/**
	 * @param globalProviderApproval the globalProviderApproval to set
	 */
	public void setGlobalProviderApproval(Boolean globalProviderApproval) {
		this.globalProviderApproval = globalProviderApproval;
	}

	/**
	 * @return the globalRegulator
	 */
	public  Boolean isGlobalRegulator() {
		return globalRegulator;
	}

	/**
	 * @param globalRegulator the globalRegulator to set
	 */
	public void setGlobalRegulator(Boolean globalRegulator) {
		this.globalRegulator = globalRegulator;
	}

	/**
	 * @return the contentOwner
	 */
	public ContentOwner getContentOwner() {
		return contentOwner;
	}

	/**
	 * @param contentOwner the contentOwner to set
	 */
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	
	/**
	 * @return the globalCredentialRequirement
	 */
	public  Boolean isGlobalCredentialRequirement() {
		return globalCredentialRequirement;
	}

	/**
	 * @param globalCredentialRequirement the globalCredentialRequirement to set
	 */
	public void setGlobalCredentialRequirement(Boolean globalCredentialRequirement) {
		this.globalCredentialRequirement = globalCredentialRequirement;
	}

	/**
	 * @param globalCredentialCategory the globalCredentialCategory to set
	 */
	public void setGlobalCredentialCategory(Boolean globalCredentialCategory) {
		this.globalCredentialCategory = globalCredentialCategory;
	}

	/**
	 * @return the globalCredentialCategory
	 */
	public  Boolean isGlobalCredentialCategory() {
		return globalCredentialCategory;
	}

}
