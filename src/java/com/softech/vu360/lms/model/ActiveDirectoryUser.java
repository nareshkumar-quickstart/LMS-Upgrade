package com.softech.vu360.lms.model;

import java.io.UnsupportedEncodingException;

import com.softech.vu360.util.VU360Properties;

public class ActiveDirectoryUser {
	
	private static final String baseDN = VU360Properties.getVU360Property("ad.user.basedn");
	private static final String domain = VU360Properties.getVU360Property("ad.user.domain");
	
	 private String userName;
	 private String firstName;
	 private String lastName;
	 private String middleName;
	 private String displayName;
	 private String email;
	 private String password;
	 private String userGUID;
	 private String distinguishedName;
	 private String userPrincipalName;
	 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserGUID() {
		return userGUID;
	}
	public void setUserGUID(String userGUID) {
		this.userGUID = userGUID;
	}
	
	public String getDistinguishedName() {
		distinguishedName = new StringBuffer().append("CN=").append(userName).append(",").append(baseDN).toString(); 
		return distinguishedName;
	}
	public String getUserPrincipalName() {
		userPrincipalName = new StringBuffer().append(userName).append("@").append(domain).toString();
		return userPrincipalName;
	}
	
	public  byte[] getADUnicodePassword() throws UnsupportedEncodingException{
		String adUserPassword = "\"" + password + "\"";
		byte[] newUnicodePassword = adUserPassword.getBytes("UTF-16LE");
		return newUnicodePassword;
	}
}
