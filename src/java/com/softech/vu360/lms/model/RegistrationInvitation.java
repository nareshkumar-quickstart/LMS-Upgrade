/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * @author raja.ali
 * Date: 2015/11/18
 *
 */

@Entity
//This stored procedure has two input parameter REG_ID and REG_UTILIZED.
@NamedStoredProcedureQuery(name = "RegistrationInvitation.addNewRegistrationInvitation", procedureName = "ADD_REGISTRATION_INVITATION", parameters = {
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "REG_ID", type = Long.class),
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "REG_UTILIZED", type = Integer.class) })
@Table(name = "REGISTRATIONINVITATION")
public class RegistrationInvitation implements Comparable<RegistrationInvitation>, SearchableKey {
	
	private static final long serialVersionUID = 642214745123131052L;
	
	
	@Id
	@javax.persistence.TableGenerator(name = "REGISTRATIONINVITATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGISTRATIONINVITATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGISTRATIONINVITATION_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "INVITATIONNAME")
	private String invitationName = null;
	
	@Column(name = "PASSCODE")
	private String passcode = null;
	
	@Column(name = "ISUNLIMITED")
	private Boolean isUnlimited = true;
	
	@Column(name = "MAXIMUMREGISTRATION")
	private Integer maximumRegistration = 0;
	
	@Column(name = "MESSAGE")
	private String invitationMessage = null;
	
	@ManyToMany
    @JoinTable(name="REGISTRATIONINVITATION_ORGANIZATIONALGROUP", joinColumns = @JoinColumn(name="REGISTRATIONINVITATION_ID"),inverseJoinColumns = @JoinColumn(name="ORGANIZATIONALGROUP_ID"))
    private List<OrganizationalGroup> orgGroups = new ArrayList<OrganizationalGroup>();
	
	@ManyToMany
    @JoinTable(name="REGISTRATIONINVITATION_LEARNERGROUP", joinColumns = @JoinColumn(name="REGISTRATIONINVITATION_ID"),inverseJoinColumns = @JoinColumn(name="LEARNERGROUP_ID"))
    private List<LearnerGroup> learnerGroups = new ArrayList<LearnerGroup>();
	
	@OneToOne
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer = null;
	
	@Column(name = "NUMBERUTILIZED")
	private Integer registrationUtilized = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public  Boolean hasUnutilizedInvitation() {
		if (isUnlimited) {
			return true;
		} else {
			return (maximumRegistration - registrationUtilized) > 0 ? true
					: false;

		}

	}

	public String getKey() {
		return id.toString();
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

	/**
	 * @return the invitationName
	 */
	public String getInvitationName() {
		return invitationName;
	}

	/**
	 * @param invitationName
	 *            the invitationName to set
	 */
	public void setInvitationName(String invitationName) {
		this.invitationName = invitationName;
	}

	/**
	 * @return the passcode
	 */
	public String getPasscode() {
		return passcode;
	}

	/**
	 * @param passcode
	 *            the passcode to set
	 */
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	/**
	 * @return the isUnlimited
	 */
	public Boolean getIsUnlimited() {
		return isUnlimited;
	}

	/**
	 * @param isUnlimited
	 *            the isUnlimited to set
	 */
	public void setIsUnlimited(Boolean isUnlimited) {
		this.isUnlimited = isUnlimited;
	}

	/**
	 * @return the maximumRegistration
	 */
	public Integer getMaximumRegistration() {
		return maximumRegistration;
	}

	/**
	 * @param maximumRegistration
	 *            the maximumRegistration to set
	 */
	public void setMaximumRegistration(Integer maximumRegistration) {
		this.maximumRegistration = maximumRegistration;
	}

	/**
	 * @return the invitationMessage
	 */
	public String getInvitationMessage() {
		return invitationMessage;
	}

	/**
	 * @param invitationMessage
	 *            the invitationMessage to set
	 */
	public void setInvitationMessage(String invitationMessage) {
		this.invitationMessage = invitationMessage;
	}

	/**
	 * @return the orgGroups
	 */
	public List<OrganizationalGroup> getOrgGroups() {
		return orgGroups;
	}

	public void addOrganizationalGroup(OrganizationalGroup orgGroup) {
		orgGroups.add(orgGroup);
	}

	/**
	 * @param orgGroups
	 *            the orgGroups to set
	 */
	public void setOrgGroups(List<OrganizationalGroup> orgGroups) {
		this.orgGroups = orgGroups;
	}

	/**
	 * @return the learnerGroups
	 */
	public List<LearnerGroup> getLearnerGroups() {
		return learnerGroups;
	}

	/**
	 * @param learnerGroups
	 *            the learnerGroups to set
	 */
	public void setLearnerGroups(List<LearnerGroup> learnerGroups) {
		this.learnerGroups = learnerGroups;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the registrationUtilized
	 */
	public Integer getRegistrationUtilized() {
		return registrationUtilized;
	}

	/**
	 * @param registrationUtilized
	 *            the registrationUtilized to set
	 */
	public void setRegistrationUtilized(Integer registrationUtilized) {
		this.registrationUtilized = registrationUtilized;
	}

	public int compareTo(RegistrationInvitation arg0) {
		return invitationName.compareToIgnoreCase(arg0.getInvitationName());
	}

}
