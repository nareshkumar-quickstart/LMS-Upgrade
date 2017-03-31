package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kaunain.wajeeh
 */
@Entity
@Table(name = "VU360USER")
public class VU360UserNew implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;
	@Column(name = "VU360AUTHPROVIDER_ID")
	private Long vu360authproviderId;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "DOMAIN")
	private String domain;
	@Column(name = "FIRSTNAME")
	private String firstname;
	@Column(name = "LASTNAME")
	private String lastname;
	@Column(name = "EMAILADDRESS")
	private String emailaddress;
	@Column(name = "USERGUID")
	private String userguid;
	@Column(name = "ACCEPTEDEULATF")
	private Integer acceptedeulatf;
	@Column(name = "ACCOUNTNONEXPIREDTF")
	private Integer accountnonexpiredtf;
	@Column(name = "ACCOUNTNONLOCKEDTF")
	private Integer accountnonlockedtf;
	@Column(name = "CHANGEPASSWORDONLOGINTF")
	private Integer changepasswordonlogintf;
	@Column(name = "CREATEDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	@Column(name = "CREDENTIALSNONEXPIREDTF")
	private Integer credentialsnonexpiredtf;
	@Column(name = "ENABLEDTF")
	private Integer enabledtf;
	@Column(name = "LASTLOGONDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastlogondate;
	@Column(name = "LASTUPDATEDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastupdateddate;
	@Column(name = "NEWUSERTF")
	private Integer newusertf;
	@Column(name = "NUMLOGONS")
	private Integer numlogons;
	@Column(name = "MIDDLENAME")
	private String middlename;
	@Column(name = "EXPIRATIONDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationdate;
	@Column(name = "VISIBLEONREPORTTF")
	private Integer visibleonreporttf;
	@Column(name = "CreateUserId")
	private BigInteger createUserId;
	@Column(name = "LastUpdateUser")
	private BigInteger lastUpdateUser;
	@Column(name = "CUSTOMERREPRESENTATIVE")
	private Boolean customerrepresentative;
	@Column(name = "SHOWGUIDEDTOURSCREENONLOGIN")
	private Integer showguidedtourscreenonlogin;
	@Column(name = "NotifyOnLicenseExpire")
	private Integer notifyOnLicenseExpire;
	@Column(name = "PROFILEIMAGEASSETID")
	private Integer profileimageassetid;

	public VU360UserNew() {
	}

	public VU360UserNew(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVu360authproviderId() {
		return vu360authproviderId;
	}

	public void setVu360authproviderId(Long vu360authproviderId) {
		this.vu360authproviderId = vu360authproviderId;
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

	public Integer getAcceptedeulatf() {
		return acceptedeulatf;
	}

	public void setAcceptedeulatf(Integer acceptedeulatf) {
		this.acceptedeulatf = acceptedeulatf;
	}

	public Integer getAccountnonexpiredtf() {
		return accountnonexpiredtf;
	}

	public void setAccountnonexpiredtf(Integer accountnonexpiredtf) {
		this.accountnonexpiredtf = accountnonexpiredtf;
	}

	public Integer getAccountnonlockedtf() {
		return accountnonlockedtf;
	}

	public void setAccountnonlockedtf(Integer accountnonlockedtf) {
		this.accountnonlockedtf = accountnonlockedtf;
	}

	public Integer getChangepasswordonlogintf() {
		return changepasswordonlogintf;
	}

	public void setChangepasswordonlogintf(Integer changepasswordonlogintf) {
		this.changepasswordonlogintf = changepasswordonlogintf;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Integer getCredentialsnonexpiredtf() {
		return credentialsnonexpiredtf;
	}

	public void setCredentialsnonexpiredtf(Integer credentialsnonexpiredtf) {
		this.credentialsnonexpiredtf = credentialsnonexpiredtf;
	}

	public Integer getEnabledtf() {
		return enabledtf;
	}

	public void setEnabledtf(Integer enabledtf) {
		this.enabledtf = enabledtf;
	}

	public Date getLastlogondate() {
		return lastlogondate;
	}

	public void setLastlogondate(Date lastlogondate) {
		this.lastlogondate = lastlogondate;
	}

	public Date getLastupdateddate() {
		return lastupdateddate;
	}

	public void setLastupdateddate(Date lastupdateddate) {
		this.lastupdateddate = lastupdateddate;
	}

	public Integer getNewusertf() {
		return newusertf;
	}

	public void setNewusertf(Integer newusertf) {
		this.newusertf = newusertf;
	}

	public Integer getNumlogons() {
		return numlogons;
	}

	public void setNumlogons(Integer numlogons) {
		this.numlogons = numlogons;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public Date getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(Date expirationdate) {
		this.expirationdate = expirationdate;
	}

	public Integer getVisibleonreporttf() {
		return visibleonreporttf;
	}

	public void setVisibleonreporttf(Integer visibleonreporttf) {
		this.visibleonreporttf = visibleonreporttf;
	}

	public BigInteger getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(BigInteger createUserId) {
		this.createUserId = createUserId;
	}

	public BigInteger getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(BigInteger lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Boolean getCustomerrepresentative() {
		return customerrepresentative;
	}

	public void setCustomerrepresentative(Boolean customerrepresentative) {
		this.customerrepresentative = customerrepresentative;
	}

	public Integer getShowguidedtourscreenonlogin() {
		return showguidedtourscreenonlogin;
	}

	public void setShowguidedtourscreenonlogin(
			Integer showguidedtourscreenonlogin) {
		this.showguidedtourscreenonlogin = showguidedtourscreenonlogin;
	}

	public Integer getNotifyOnLicenseExpire() {
		return notifyOnLicenseExpire;
	}

	public void setNotifyOnLicenseExpire(Integer notifyOnLicenseExpire) {
		this.notifyOnLicenseExpire = notifyOnLicenseExpire;
	}

	public Integer getProfileimageassetid() {
		return profileimageassetid;
	}

	public void setProfileimageassetid(Integer profileimageassetid) {
		this.profileimageassetid = profileimageassetid;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public  boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof VU360UserNew)) {
			return false;
		}
		VU360UserNew other = (VU360UserNew) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "VU360UserNew [id=" + id + ", vu360authproviderId="
				+ vu360authproviderId + ", username=" + username
				+ ", password=" + password + ", domain=" + domain
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", emailaddress=" + emailaddress + ", userguid=" + userguid
				+ ", acceptedeulatf=" + acceptedeulatf
				+ ", accountnonexpiredtf=" + accountnonexpiredtf
				+ ", accountnonlockedtf=" + accountnonlockedtf
				+ ", changepasswordonlogintf=" + changepasswordonlogintf
				+ ", createddate=" + createddate + ", credentialsnonexpiredtf="
				+ credentialsnonexpiredtf + ", enabledtf=" + enabledtf
				+ ", lastlogondate=" + lastlogondate + ", lastupdateddate="
				+ lastupdateddate + ", newusertf=" + newusertf + ", numlogons="
				+ numlogons + ", middlename=" + middlename
				+ ", expirationdate=" + expirationdate + ", visibleonreporttf="
				+ visibleonreporttf + ", createUserId=" + createUserId
				+ ", lastUpdateUser=" + lastUpdateUser
				+ ", customerrepresentative=" + customerrepresentative
				+ ", showguidedtourscreenonlogin="
				+ showguidedtourscreenonlogin + ", notifyOnLicenseExpire="
				+ notifyOnLicenseExpire + ", profileimageassetid="
				+ profileimageassetid + "]";
	}

}
