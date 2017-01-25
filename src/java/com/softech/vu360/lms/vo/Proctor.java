package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.Date;

public class Proctor implements Serializable {
	private static final long serialVersionUID = 1521767274264036958L;
	
	private Long id;
	private String username;
	private String password;
	private Date passwordResetDateTime;
	private String status;
	private Date proctorStatusTimeStamp;
	private Boolean firstLogin = Boolean.FALSE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getPasswordResetDateTime() {
		return passwordResetDateTime;
	}

	public void setPasswordResetDateTime(Date passwordResetDateTime) {
		this.passwordResetDateTime = passwordResetDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getProctorStatusTimeStamp() {
		return proctorStatusTimeStamp;
	}

	public void setProctorStatusTimeStamp(Date proctorStatusTimeStamp) {
		this.proctorStatusTimeStamp = proctorStatusTimeStamp;
	}

	public Boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

}
