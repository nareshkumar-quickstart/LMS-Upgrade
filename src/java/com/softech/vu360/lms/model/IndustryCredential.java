package com.softech.vu360.lms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "INDUSTRY_CREDENTIALS")
public class IndustryCredential  implements SearchableKey{
	
	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "INDUSTRY_CREDENTIALS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "INDUSTRY_CREDENTIALS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "INDUSTRY_CREDENTIALS_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="INDUSTRY_ID")
	private LicenseIndustry licenseindustry ;
	
	@OneToOne
    @JoinColumn(name="CREDENTIALS_ID")
	private Credential credential ;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LicenseIndustry getLicenseindustry() {
		return licenseindustry;
	}

	public void setLicenseindustry(LicenseIndustry licenseindustry) {
		this.licenseindustry = licenseindustry;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
