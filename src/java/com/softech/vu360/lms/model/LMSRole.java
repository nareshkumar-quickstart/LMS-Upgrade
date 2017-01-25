package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;

/**
 * @author Raja Wajahat Ali
 * @Date 2016-02-02
 * 
 */
@Entity
@Table(name = "LMSROLE")

public class LMSRole implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	// ROLE TYPES
	public static final String ROLE_LEARNER = "ROLE_LEARNER";
	public static final String ROLE_TRAININGMANAGER = "ROLE_TRAININGADMINISTRATOR";
	public static final String ROLE_LMSADMINISTRATOR = "ROLE_LMSADMINISTRATOR";
	public static final String ROLE_REGULATORYANALYST = "ROLE_REGULATORYANALYST";
	public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
	public static final String ROLE_PROCTOR = "ROLE_PROCTOR";
	
	private static Logger log = Logger.getLogger(LMSRole.class);	
	
	@Id
    @javax.persistence.TableGenerator(name = "LMSROLE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSROLE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSROLE_ID")
	private Long id;
	
	@Column(name="ROLENAME")
	private String roleName;
	
	@Column(name="ROLE_TYPE")
	private String roleType;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer owner;
	
	@OneToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY, mappedBy="lmsRole", orphanRemoval=true)
	private List<LMSRoleLMSFeature> lmsPermissions = new ArrayList<LMSRoleLMSFeature>();
	
	@Column(name="DEFAULTFORREGISTRATIONTF")
	private Boolean isDefaultForRegistration = Boolean.FALSE;
	
	/* there is no other way to distinguish system generated role from custom role.
	 * System generated role will not be editable.
	 * In Manager creation a system owner Learner role needs to be assigned and no way to 
	 * get that learner role.
	 */
	
	@Column(name="SYSTEMCREATEDTF")
	private Boolean isSystemCreated = Boolean.FALSE;
	
	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public List<LMSRoleLMSFeature> getLmsPermissions() {
		return lmsPermissions;
	}

	public void setLmsPermissions(List<LMSRoleLMSFeature> lmsPermissions) {
		this.lmsPermissions = lmsPermissions;
	}

	public  Boolean isDefaultForRegistration() {
	    if(isDefaultForRegistration==null){
	        isDefaultForRegistration=Boolean.FALSE;
	    }
		return isDefaultForRegistration;
	}

	public void setDefaultForRegistration(Boolean isDefaultForRegistration) {
		this.isDefaultForRegistration = isDefaultForRegistration;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}

	public  Boolean isSystemCreated() {
		return isSystemCreated;
	}

	public void setSystemCreated(Boolean isSystemCreated) {
		this.isSystemCreated = isSystemCreated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		try {
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + (isDefaultForRegistration!=null && isDefaultForRegistration.booleanValue() ? 1231 : 1237);
			result = prime * result + (isSystemCreated!=null && isSystemCreated.booleanValue() ? 1231 : 1237);
			result = prime
					* result
					+ ((lmsPermissions == null) ? 0 : lmsPermissions.hashCode());
			result = prime * result + ((owner == null) ? 0 : owner.hashCode());
			result = prime * result
					+ ((roleName == null) ? 0 : roleName.hashCode());
			result = prime * result
					+ ((roleType == null) ? 0 : roleType.hashCode());
		} catch (Exception e) {
			log.debug("Exception in hashCode in LMSRole.");
		}
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
		LMSRole other = (LMSRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDefaultForRegistration != other.isDefaultForRegistration)
			return false;
		if (isSystemCreated != other.isSystemCreated)
			return false;
		if (lmsPermissions == null) {
			if (other.lmsPermissions != null)
				return false;
		} else if (!lmsPermissions.equals(other.lmsPermissions))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (roleType == null) {
			if (other.roleType != null)
				return false;
		} else if (!roleType.equals(other.roleType))
			return false;
		return true;
	}

	/**
	 * Return string representation of given <code>roleType</code>
	 * @param roleType
	 * @return string representation of given <code>roleType</code>
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public static String getRoleType (Integer roleType) 
	{
		switch(roleType)
		{
			case 1 : return ROLE_TRAININGMANAGER;
			case 2 : return ROLE_INSTRUCTOR;
			case 3 : return ROLE_REGULATORYANALYST;
			case 4 : return ROLE_LMSADMINISTRATOR;
			case 5 : return ROLE_PROCTOR;
			default: return ROLE_LEARNER;
		}
	}
}