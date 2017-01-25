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
@Table(name = "LICENSETYPE_LEARNER")
public class LearnerLicenseType  implements SearchableKey {

	
	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "LICENSETYPE_LEARNER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LICENSETYPE_LEARNER", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LICENSETYPE_LEARNER_ID")
	private Long Id;
	
	@OneToOne
    @JoinColumn(name="LICENSETYPE_ID")
	private LicenseType licenseType ;
	
	@OneToOne
    @JoinColumn(name="LEARNER_ID")
	private Learner learner ;

	
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
