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

@Entity
@Table(name = "CUSTOMERLMSFEATURE")
public class CustomerLMSFeature implements ILMSFeaturePermission {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Unique identifier, probably Primary Key.
	 */
	@Id
	@javax.persistence.TableGenerator(name = "CUSTOMERLMSFEATURE", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMERLMSFEATURE", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMERLMSFEATURE")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	
	
	/**
	 * Whether this feature is enabled or disabled
	 */
	@Column(name = "ENABLEDTF")
	private Boolean enabled = true;
	
	/**
	 * {@link LMSFeature}
	 */
	@OneToOne
	@JoinColumn(name = "LMSFEATURE_ID")
	private LMSFeature lmsFeature ;

	
	@OneToOne
    @JoinColumn(name = "CUSTOMER_ID")
	private Customer customer ;

	/**
	 * This is to show the feature as disabled on UI, not mapped with any DB field.
	 */
	@Transient
	private  Boolean isLocked=false;

	
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


	public Customer getCustomer() {
		return this.customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	

}
