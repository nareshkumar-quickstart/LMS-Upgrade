/**
 * 
 */
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

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PROCTOR_PLAYER_AUDIT")
public class ProctorEnrollment implements SearchableKey{
	
	@Id
	private Long id;
	
	@OneToOne
    @JoinColumn(name="PROCTOR_ID")
	private Proctor proctor ;
	
	@OneToOne
    @JoinColumn(name="LEARNERENROLLMENT_ID")
	private LearnerEnrollment enrollment ;
	
	@Column(name = "TIMESTAMP")
	private Date timestamp;
	
	@Column(name = "IS_SUCCESSFUL_LOGIN_TF")
	private Boolean successfullLogin;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the proctor
	 */
	public Proctor getProctor() {
		return this.proctor;
	}
	/**
	 * @param proctor the proctor to set
	 */
	public void setProctor(Proctor proctor) {
		this.proctor = proctor;
	}

	/**
	 * @return the enrollment
	 */
	public LearnerEnrollment getEnrollment() {
		return this.enrollment;
	}
	/**
	 * @param enrollment the enrollment to set
	 */
	public void setEnrollment(LearnerEnrollment enrollment) {
		this.enrollment = enrollment;
	}
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the successfullLogin
	 */
	public  Boolean isSuccessfullLogin() {
		return successfullLogin;
	}
	/**
	 * @param successfullLogin the successfullLogin to set
	 */
	public void setSuccessfullLogin(Boolean successfullLogin) {
		this.successfullLogin = successfullLogin;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	

}
