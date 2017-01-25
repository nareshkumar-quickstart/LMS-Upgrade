package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.CascadeType;
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
 * @author marium.saud
 *
 */
@Entity
@Table(name = "LCMSPERMISSION")
public class AuthorPermission implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "LCMSPERMISSION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LCMSPERMISSION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LCMSPERMISSION_ID")
	private Long id;
	
	@Column(name = "ENABLEDTF")
	private Integer featureEnabled = null;
	
	@OneToOne
    @JoinColumn(name="LCMS_FEATURE_ID")
	private AuthorFeature authorFeature = null;
	
	@OneToOne
    @JoinColumn(name="LCMSAUTHGROUP_ID")
	private AuthorGroup authorGroup ;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "LastUpdatedDate")
	private Date lastUpdatedDate;
	
	@Column(name = "CreateUserId")
	private Long createdUserId;
	
	@Column(name = "LastUpdateUser")
	private Long lastUpdatedUserId;
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	/**
	 * @return the createdUserId
	 */
	public Long getCreatedUserId() {
		return createdUserId;
	}
	/**
	 * @param createdUserId the createdUserId to set
	 */
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	/**
	 * @return the lastUpdatedUserId
	 */
	public Long getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	/**
	 * @param lastUpdatedUserId the lastUpdatedUserId to set
	 */
	public void setLastUpdatedUserId(Long lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
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

	public Boolean isFeatureEnabled() {
		return (featureEnabled.intValue()==1);
	}
	/**
	 * @return the featureEnabled
	 */
	public Integer getFeatureEnabled(){
		return this.featureEnabled;
	}
	/**
	 * @param featureEnabled the featureEnabled to set
	 */
	public void setFeatureEnabled(Integer featureEnabled) {
		this.featureEnabled = featureEnabled;
	}

	/**
	 * @return the authorFeature
	 */
	public AuthorFeature getAuthorFeature() {
		return authorFeature;
	}

	/**
	 * @param authorFeature the authorFeature to set
	 */
	public void setAuthorFeature(AuthorFeature authorFeature) {
		this.authorFeature = authorFeature;
	}
	/**
	 * @return the authorGroup
	 */
	public AuthorGroup getAuthorGroup() {
		return authorGroup;
	}
	/**
	 * @param authorGroup the authorGroup to set
	 */
	public void setAuthorGroup(AuthorGroup authorGroup) {
		this.authorGroup = authorGroup;
	}
	
	
	
}
