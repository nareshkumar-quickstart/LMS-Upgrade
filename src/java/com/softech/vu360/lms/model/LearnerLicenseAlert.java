package com.softech.vu360.lms.model;

import java.util.Date;

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
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LICENSE_ALERT")
public class LearnerLicenseAlert  implements SearchableKey{
	


	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "LICENSE_ALERT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LICENSE_ALERT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LICENSE_ALERT_ID")
	private Long id;
	
	@Transient
	private Long learnerid;
	
	@Transient
	private Long licenseoflearnerId;

	@OneToOne
    @JoinColumn(name="LICENSE_ID")
	private LicenseOfLearner learnerlicense;
	
	@OneToOne
    @JoinColumn(name="LEARNER_ID")
	private Learner learner ;
	
	@Column(name="DAYSAFTEREVENT")
    private Integer daysAfterEvent = null;
    
	@Column(name="DAYSBEFOREEVENT")
    private Integer daysBeforeEvent = null;
    
	@Column(name="ALERTNAME")
	private String alertName = null;
	
	@Column(name="TRIGGERSINGLEDATE")
	private Date triggerSingleDate=null;
	
	@Column(name="ISDELETE")
	private Boolean isDelete = false;
	
	@Column(name="CREATEDDATE")
	private Date createdDate = null;
	
	@Column(name="UPDATEDDATE")
	private Date updatedDate = null;	
	
	public Long getLicenseoflearnerId() {
		return licenseoflearnerId;
	}

	public void setLicenseoflearnerId(Long licenseoflearnerId) {
		this.licenseoflearnerId = licenseoflearnerId;
	}

	public Long getLearnerid() {
		return learnerid;
	}

	public void setLearnerid(Long learnerid) {
		this.learnerid = learnerid;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LicenseOfLearner getLearnerlicense() {
		return learnerlicense;
	}

	public void setLearnerlicense(LicenseOfLearner learnerlicense) {
		this.learnerlicense = learnerlicense;
	}

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public Integer getDaysAfterEvent() {
		return daysAfterEvent;
	}



	
	public void setDaysAfterEvent(Integer daysAfterEvent) {
		this.daysAfterEvent = daysAfterEvent;
	}

	public Integer getDaysBeforeEvent() {
		return daysBeforeEvent;
	}

	public void setDaysBeforeEvent(Integer daysBeforeEvent) {
		this.daysBeforeEvent = daysBeforeEvent;
	}

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public Date getTriggerSingleDate() {
		return triggerSingleDate;
	}

	public void setTriggerSingleDate(Date triggerSingleDate) {
		this.triggerSingleDate = triggerSingleDate;
	}

	public Boolean isDelete() {
		return isDelete;
	}

	public void setDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
