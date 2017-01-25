package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "DISTRIBUTORLMSFEATURE")
public class DistributorLMSFeature implements ILMSFeaturePermission {

	private static final long serialVersionUID = 5213234446726484716L;

	@Id
	@javax.persistence.TableGenerator(name = "DISTRIBUTORLMSFEATURE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "DISTRIBUTORLMSFEATURE", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DISTRIBUTORLMSFEATURE_ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "DISTRIBUTOR_ID")
	private Distributor distributor ;
	
	@Transient
	private Boolean isLocked=false;
		
	@Column(name = "ENABLEDTF")
	private Boolean enabled = true;
	
	@OneToOne
	@JoinColumn(name = "LMSFEATURE_ID")
	private LMSFeature lmsFeature ;

	/**
	 * @see SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	/**
	 * Return id
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set id
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Return enabled
	 * @return enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Set enabled
	 * @param enabled
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Return lmsFeature
	 * @return lmsFeature
	 * @see {@link LMSFeature}
	 */
	public LMSFeature getLmsFeature() {
		return this.lmsFeature;
	}

	/**
	 * Set lmsFeature
	 * @param lmsFeature
	 * @see {@link LMSFeature}
	 */
	public void setLmsFeature(LMSFeature lmsFeature) {
		this.lmsFeature = lmsFeature;
	}


	public  Boolean isLocked() {
		return isLocked;
	}

	public void setLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}	
	public Distributor getDistributor() {
		return this.distributor;
	}
	
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
}
