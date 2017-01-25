package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * LMS Feature for LMS Role
 * @author sm.humayun
 * @since 4.13 {LMS-8108}
 */

@Entity
@Table(name = "LMSROLELMSFEATURE")
public class LMSRoleLMSFeature implements ILMSFeaturePermission {	

	private static final long serialVersionUID = -2516709445055290221L;

	@Id
	@javax.persistence.TableGenerator(name = "LMSROLELMSFEATURE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSROLELMSFEATURE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSROLELMSFEATURE_ID")
	private Long id;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "LMSROLE_ID")
	private LMSRole lmsRole = new LMSRole();
	
	/**
	 * Whether this feature is enabled or disabled
	 */
	@Column(name="ENABLEDTF")
	private Boolean enabled = Boolean.TRUE;

	@OneToOne
	@JoinColumn(name = "LMSFEATURE_ID")
	private LMSFeature lmsFeature = null;
	

	/**
	 * This is to show the feature as disabled on UI, not mapped with any DB field.
	 */
	@Transient
	private  Boolean isLocked=false;
	
	public LMSRole getLmsRole() {
		return lmsRole;
	}
	
	public void setLmsRole(LMSRole lmsRole) {
		this.lmsRole = lmsRole;
	}

	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public LMSFeature getLmsFeature() {
		return lmsFeature;
	}

	public void setLmsFeature(LMSFeature lmsFeature) {
		this.lmsFeature = lmsFeature;
	}

	public  Boolean isLocked() {
		return isLocked;
	}

	public void setLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
}
