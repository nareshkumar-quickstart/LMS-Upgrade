/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PROCTOR")
public class Proctor implements SearchableKey{
	
	@Id
    @javax.persistence.TableGenerator(name = "PROCTOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "PROCTOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PROCTOR_ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VU360USER_ID")
	private VU360User user ;
	
	@Column(name = "USERNAME")
	private String username;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "PASSWORD_RESET_TIMESTAMP")
	private Date passwordResetDateTime;
	
	@Column(name = "STATUS")
	private String status;
	
	/**
	 * Records the time when the Proctor was added for the first time to a Credential.  
	 * The time is updated in this field in the case where the Proctor is retired entirely from all 
	 * credentials and comes back as a new Proctor once added to a credential.    
	 */
	@Column(name = "PROCTOR_STATUS_TIMESTAMP")
	private Date proctorStatusTimeStamp;
	
	@ManyToMany
    @JoinTable(name="CREDENTIAL_PROCTOR", joinColumns = @JoinColumn(name="PROCTOR_ID"),inverseJoinColumns = @JoinColumn(name="CREDENTIAL_ID"))
    private List<Credential> credentials = new ArrayList<Credential>();
	
	@Column(name = "FIRSTLOGINTF")
	private Boolean firstLogin = false;
	
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
	 * @return the user
	 */
	public VU360User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(VU360User user) {
		this.user = user;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passwordResetDateTime
	 */
	public Date getPasswordResetDateTime() {
		return passwordResetDateTime;
	}

	/**
	 * @param passwordResetDateTime the passwordResetDateTime to set
	 */
	public void setPasswordResetDateTime(Date passwordResetDateTime) {
		this.passwordResetDateTime = passwordResetDateTime;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the credentials
	 */
	public List<Credential> getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}

	public Date getProctorStatusTimeStamp() {
		return proctorStatusTimeStamp;
	}

	public void setProctorStatusTimeStamp(Date proctorStatusTimeStamp) {
		this.proctorStatusTimeStamp = proctorStatusTimeStamp;
	}

	public  Boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	

}
