/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Ashis
 * 
 */
@Entity
@Table(name = "LMSFEATURE")
public class LMSFeature implements SearchableKey {

	private static final long serialVersionUID = 5104238309355158808L;


	@Id
	@javax.persistence.TableGenerator(name = "LMSFEATURE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSFEATURE", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSFEATURE_ID")
	private Long id;
	
	
	@Column(name="FEATURECODE")
	private String featureCode = null;
	
	@Column(name="FEATUREDESC")
	private String featureDescription = null;
	
	@Column(name="FEATURENAME")
	private String featureName = null;
	
	@Column(name="FEATUREGROUP")
	private String featureGroup = null;
	
	@Column(name="LMSMODE")
	private String lmsMode = null;
	
	@Column(name="ROLETYPE")
	private String roleType = LMSRole.ROLE_LEARNER;
	
	@Column(name="DISPLAYORDER")
	private Integer displayOrder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
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
	 * @return the featureCode
	 */
	public String getFeatureCode() {
		return featureCode;
	}

	/**
	 * @param featureCode
	 *            the featureCode to set
	 */
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	/**
	 * @return the featureDescription
	 */
	public String getFeatureDescription() {
		return featureDescription;
	}

	/**
	 * @param featureDescription
	 *            the featureDescription to set
	 */
	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @param featureName
	 *            the featureName to set
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	/**
	 * @return the lmsMode
	 */
	public String getLmsMode() {
		return lmsMode;
	}

	/**
	 * @param lmsMode
	 *            the lmsMode to set
	 */
	public void setLmsMode(String lmsMode) {
		this.lmsMode = lmsMode;
	}

	/**
	 * @return the parentFeature
	 */

	/**
	 * @return the roleType
	 */
	public String getRoleType() {
		return roleType;
	}

	/**
	 * @param roleType
	 *            the roleType to set
	 */
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	/**
	 * @return the featureGroup
	 */
	public String getFeatureGroup() {
		return featureGroup;
	}

	/**
	 * @param featureGroup
	 *            the featureGroup to set
	 */
	public void setFeatureGroup(String featureGroup) {
		this.featureGroup = featureGroup;
	}
	
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}
