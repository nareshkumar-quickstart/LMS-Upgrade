package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author haider.ali, Muhammad Saleem
 * modified by marium.saud
 */
@Entity
@NamedStoredProcedureQuery(name = "VU360User.getOrganizational_Group_Members", procedureName = "LMS_GETORGANIZATIONALGROUP_MEMBERS", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "VU360USER_ID", type = Long.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "CUSTOMER_ID", type = Long.class) })

@Table(name = "VU360USER")
public class VU360User implements Serializable, PersistentAttributeInterceptable {

	private static final long serialVersionUID = 52260678146352048L;
	private static Logger log = Logger.getLogger(VU360User.class);

	@Transient
	private transient PersistentAttributeInterceptor interceptor;

	@Id
	@javax.persistence.TableGenerator(name = "VU360USER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "vu360user", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360USER_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "USERGUID", unique = true, nullable = false)
	private String userGUID = null;

	@Nationalized
	@Column(name = "USERNAME", unique = true, nullable = false)
	private String username = null;

	/*
     * As their is no record in PROD and as well as in DEV; let's remove this one
     * @Column(name = "DOMAIN")
        private String domain = null;
    */
	@Nationalized
	@Column(name = "EMAILADDRESS", unique = false, nullable = false)
	private String emailAddress = null;

	@Nationalized
	@Column(name = "FIRSTNAME", unique = false, nullable = false)
	private String firstName = null;

	@Nationalized
	@Column(name = "LASTNAME", unique = false, nullable = false)
	private String lastName = null;

	@Nationalized
	@Column(name = "MIDDLENAME")
	private String middleName = null;

	@Column(name = "CREATEDDATE", nullable = false)
	private Date createdDate = new Date();

	@Column(name = "LASTUPDATEDDATE", nullable = false)
	private Date lastUpdatedDate = new Date();

	@Column(name = "ACCOUNTNONEXPIREDTF")
	private Boolean  accountNonExpired = Boolean.TRUE;

	@Column(name = "ACCOUNTNONLOCKEDTF")
	private Boolean  accountNonLocked = Boolean.TRUE;

	@Column(name = "CREDENTIALSNONEXPIREDTF")
	private Boolean  credentialsNonExpired = Boolean.TRUE;

	@Column(name = "PASSWORD", unique = false, nullable = false)
	private String password = null;

	@Column(name = "ENABLEDTF")
	private Boolean  enabled = Boolean.TRUE;

	@Column(name = "NUMLOGONS")
	private Integer numLogons = 0;

	@Column(name = "LASTLOGONDATE")
	private Date lastLogonDate;

	@Column(name = "NEWUSERTF")
	private Boolean  newUser = true;

	@Column(name = "ACCEPTEDEULATF")
	private Boolean  acceptedEULA = false;

	@Column(name = "CHANGEPASSWORDONLOGINTF")
	private Boolean  changePasswordOnLogin = false;

	@Column(name = "VISIBLEONREPORTTF")
	private Boolean  vissibleOnReport = true;

	@Column(name = "EXPIRATIONDATE")
	private Date expirationDate = null;

	@Column(name = "NotifyOnLicenseExpire")
	private Boolean  notifyOnLicenseExpire = Boolean.TRUE;

	@OneToOne(mappedBy="vu360User", fetch = FetchType.LAZY)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private Learner learner;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="vu360User" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private TrainingAdministrator trainingAdministrator;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="vu360User" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private LMSAdministrator lmsAdministrator;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private Proctor proctor;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private RegulatoryAnalyst regulatoryAnalyst;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private Instructor instructor;

	@ManyToMany(cascade={CascadeType.MERGE })
	@JoinTable(name="VU360USER_ROLE", joinColumns = @JoinColumn(name="USER_ID"),inverseJoinColumns = @JoinColumn(name="ROLE_ID"))
	private Set<LMSRole> lmsRoles = new HashSet<LMSRole>();

	@Transient
	private LMSRole logInAsManagerRole = null;

	@Transient
	private  Boolean passWordChanged = Boolean.FALSE;

	@Transient
	private  Boolean isAdminMode = Boolean.FALSE;

	@Transient
	private  Boolean isProctorMode = Boolean.FALSE;

	@Transient
	private  Boolean isManagerMode = Boolean.FALSE;

	@Transient
	private  Boolean isLearnerMode = Boolean.FALSE;

	@Transient
	private  Boolean isInstructorMode = Boolean.FALSE;

	@Transient
	private  Boolean isAccreditationMode = Boolean.FALSE;

	@Column(name = "SHOWGUIDEDTOURSCREENONLOGIN")
	private Boolean showGuidedTourScreenOnLogin = Boolean.TRUE;

	/**
	 * Added By Marium Saud
	 * Fields required for Search All Users
	 *
	 */
	@Transient
	private Long roleID;

	@Transient
	private String roleName;

	@Transient
	private Long learnerID;

	@Transient
	private Integer accountNonLockedInt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append(firstName);
		sb.append(" ");
		sb.append(lastName);
		return sb.toString();
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getNumLogons() {
		return numLogons;
	}

	public void setNumLogons(Integer numLogons) {

		this.numLogons = numLogons;
	}

	public Date getLastLogonDate() {
		return lastLogonDate;
	}

	public void setLastLogonDate(Date lastLogonDate) {
		this.lastLogonDate = lastLogonDate;
	}

	public  Boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(Boolean newUser) {
		this.newUser = newUser;
	}

	public  Boolean isAcceptedEULA() {
		return acceptedEULA;
	}

	public void setAcceptedEULA(Boolean acceptedEULA) {
		this.acceptedEULA = acceptedEULA;
	}

	public  Boolean getChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}

	public void setChangePasswordOnLogin(Boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		if(accountNonLocked==null){
			this.accountNonLocked = Boolean.TRUE;
		}else{
			this.accountNonLocked = accountNonLocked;
		}
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Learner getLearner() {
		if(interceptor != null){
			this.learner = (Learner) this.interceptor.readObject(this, "learner", this.learner);
		}
		return learner;
	}

	public void setLearner(Learner learner) {
		Learner learnerVal = learner;
		if(interceptor != null){
			learnerVal = (Learner)this.interceptor.writeObject(this, "learner", this.learner, learner);
		}
		this.learner = learnerVal;
	}

	public TrainingAdministrator getTrainingAdministrator() {
		if(this.interceptor != null){
			this.trainingAdministrator = (TrainingAdministrator) this.interceptor.readObject(this, "trainingAdministrator", trainingAdministrator);
		}
		return this.trainingAdministrator;
	}

	public void setTrainingAdministrator(TrainingAdministrator trainingAdministrator) {
		TrainingAdministrator trainingAdmin = trainingAdministrator;
		if(this.interceptor != null){
			trainingAdmin = (TrainingAdministrator) this.interceptor.writeObject(this, "trainingAdministrator", this.trainingAdministrator, trainingAdministrator);
		}
		this.trainingAdministrator = trainingAdmin;
	}

	public LMSAdministrator getLmsAdministrator() {
		if(this.interceptor != null){
			this.lmsAdministrator = (LMSAdministrator) this.interceptor.readObject(this, "lmsAdministrator", lmsAdministrator);
		}
		return this.lmsAdministrator;
	}

	public void setLmsAdministrator(LMSAdministrator lmsAdministrator) {
		LMSAdministrator lmsAdmin = lmsAdministrator;
		if(this.interceptor != null) {
			lmsAdmin = (LMSAdministrator) this.interceptor.writeObject(this, "lmsAdministrator", this.lmsAdministrator, lmsAdministrator);
		}
		this.lmsAdministrator = lmsAdmin;
	}

	public Proctor getProctor() {
		if(this.interceptor != null){
			this.proctor = (Proctor) this.interceptor.readObject(this, "proctor", proctor);
		}
		return this.proctor;
	}

	public void setProctor(Proctor proctor) {
		Proctor lmsProctor = proctor;
		if(this.interceptor != null) {
			lmsProctor = (Proctor) this.interceptor.writeObject(this, "proctor", this.proctor, proctor);
		}
		this.proctor = lmsProctor;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public  Boolean getAccountNonExpired() {
		if(accountNonExpired==null){
			accountNonExpired=Boolean.TRUE;
		}
		if (accountNonExpired) {
			if (expirationDate != null && expirationDate.before(new Date())) {
				log.debug("User Account[" + getName() + "] is expired.");
				accountNonExpired = Boolean.FALSE;
			} else {
				log.debug(" No Issue....:)");
			}

		}

		return accountNonExpired;
	}

	public  Boolean getAccountNonLocked() {
		if(accountNonLocked==null){
			accountNonLocked=Boolean.TRUE;
		}
		return accountNonLocked;
	}

	public  Boolean isCredentialsNonExpired() {
		if(credentialsNonExpired==null){
			credentialsNonExpired=Boolean.TRUE;
		}
		return credentialsNonExpired;
	}

	public  Boolean getEnabled() {
		if(enabled==null){
			enabled=Boolean.TRUE;
		}
		return enabled;
	}

	public String getUserGUID() {
		return userGUID;
	}

	public void setUserGUID(String userGUID) {
		this.userGUID = userGUID;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>(
				this.lmsRoles.size());
		for (LMSRole role : lmsRoles) {
			/*
			 * What should be the roletype
			 */
			GrantedAuthority grant = new SimpleGrantedAuthority(
					role.getRoleType());

			authorityList.add(grant);
		}
		return authorityList;
	}

	public Set<LMSRole> getLmsRoles() {
		if(this.interceptor != null){
			this.lmsRoles = (Set<LMSRole>) this.interceptor.readObject(this, "lmsRoles", lmsRoles);
		}
		return this.lmsRoles;
	}

	public void addLmsRole(LMSRole lmsRole) {
		lmsRoles.add(lmsRole);
	}

	public void setLmsRoles(Set<LMSRole> lmsRoles) {
		Set<LMSRole> lmsRolesList = lmsRoles;
		if(this.interceptor != null){
			lmsRolesList = (Set<LMSRole>) this.interceptor.writeObject(this, "lmsRoles", this.lmsRoles, lmsRoles);
		}
		this.lmsRoles = lmsRolesList;

		this.isAdminMode = false;
		this.isProctorMode = false;
		this.isManagerMode = false;
		this.isLearnerMode = false;
		this.isInstructorMode = false;
		this.isAccreditationMode = false;

		for (LMSRole role : lmsRoles) {
			String roleType = role.getRoleType();
			log.debug("roleType : " + roleType);
			if (LMSRole.ROLE_LMSADMINISTRATOR.equalsIgnoreCase(roleType))
				setAdminMode(true);
			else if (LMSRole.ROLE_TRAININGMANAGER.equalsIgnoreCase(roleType))
				setManagerMode(true);
			else if (LMSRole.ROLE_LEARNER.equalsIgnoreCase(roleType))
				setLearnerMode(true);
			else if (LMSRole.ROLE_INSTRUCTOR.equalsIgnoreCase(roleType))
				setInstructorMode(true);
			else if (LMSRole.ROLE_REGULATORYANALYST.equalsIgnoreCase(roleType))
				setAccreditationMode(true);
			else if (LMSRole.ROLE_PROCTOR.equalsIgnoreCase(roleType))
				setProctorMode(true);
		}
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public  Boolean getVissibleOnReport() {
		return vissibleOnReport;
	}

	public void setVissibleOnReport(Boolean vissibleOnReport) {
		this.vissibleOnReport = vissibleOnReport;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public  Boolean isPassWordChanged() {
		return passWordChanged;
	}

	public void setPassWordChanged(Boolean passWordChanged) {
		this.passWordChanged = passWordChanged;
	}

	public Language getLanguage() {
		return new Language();
	}

	public RegulatoryAnalyst getRegulatoryAnalyst() {
		if(this.interceptor != null){
			this.regulatoryAnalyst = (RegulatoryAnalyst) this.interceptor.readObject(this, "regulatoryAnalyst", regulatoryAnalyst);
		}
		return this.regulatoryAnalyst;
	}

	public void setRegulatoryAnalyst(RegulatoryAnalyst regulatoryAnalyst) {
		RegulatoryAnalyst lmsRegulatoryAnalyst = regulatoryAnalyst;
		if(this.interceptor != null) {
			lmsRegulatoryAnalyst = (RegulatoryAnalyst) this.interceptor.writeObject(this, "regulatoryAnalyst", this.regulatoryAnalyst, regulatoryAnalyst);
		}
		this.regulatoryAnalyst = lmsRegulatoryAnalyst;
	}

	public Instructor getInstructor() {
		if(this.interceptor != null){
			this.instructor = (Instructor) this.interceptor.readObject(this, "instructor", instructor);
		}
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		Instructor lmsInstructor = instructor;
		if(this.interceptor != null) {
			lmsInstructor = (Instructor) this.interceptor.writeObject(this, "instructor", this.instructor, instructor);
		}
		this.instructor = lmsInstructor;
	}

	public void updateDeepValues(VU360User user)
	{
		this.setAcceptedEULA(user.isAcceptedEULA());
		this.setNewUser(user.isNewUser());
		this.setAccountNonExpired(user.getAccountNonExpired());
		this.setAccountNonLocked(user.getAccountNonLocked());
		this.setChangePasswordOnLogin(user.getChangePasswordOnLogin());
		this.setCreatedDate(user.getCreatedDate());
		this.setCredentialsNonExpired(user.isCredentialsNonExpired());
		this.setEmailAddress(user.getEmailAddress());
		this.setEnabled(user.getEnabled());
		this.setExpirationDate(user.getExpirationDate());
		this.setLastLogonDate(user.getLastLogonDate());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setLastUpdatedDate(user.getLastUpdatedDate());
		this.setMiddleName(user.getMiddleName());
		this.setNumLogons(user.getNumLogons());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		this.setVissibleOnReport(user.getVissibleOnReport());
		this.setUserGUID(user.getUserGUID());
		this.setShowGuidedTourScreenOnLogin(user.getShowGuidedTourScreenOnLogin());
		this.getLearner().getLearnerProfile().setTimeZone(user.getLearner().getLearnerProfile().getTimeZone());

		LearnerProfile lp=this.getLearner().getLearnerProfile();
		LearnerProfile lp2=user.getLearner().getLearnerProfile();
		lp.setMobilePhone(lp2.getMobilePhone());
		lp.setOfficePhone(lp2.getOfficePhone());
		lp.setOfficePhoneExtn(lp2.getOfficePhoneExtn());


		if(lp.getLearnerAddress()==null || lp2.getLearnerAddress()==null)
		{
			lp.setLearnerAddress(lp2.getLearnerAddress());

		}
		else
		{
			lp.getLearnerAddress().setCity(lp2.getLearnerAddress().getCity());
			lp.getLearnerAddress().setCountry(lp2.getLearnerAddress().getCountry());
			lp.getLearnerAddress().setProvince(lp2.getLearnerAddress().getProvince());
			lp.getLearnerAddress().setState(lp2.getLearnerAddress().getState());
			lp.getLearnerAddress().setStreetAddress(lp2.getLearnerAddress().getStreetAddress());
			lp.getLearnerAddress().setStreetAddress2(lp2.getLearnerAddress().getStreetAddress2());
			lp.getLearnerAddress().setStreetAddress3(lp2.getLearnerAddress().getStreetAddress3());
			lp.getLearnerAddress().setZipcode(lp2.getLearnerAddress().getZipcode());
		}

		if(lp.getLearnerAddress2()==null || lp2.getLearnerAddress2()==null)
		{
			lp.setLearnerAddress2(lp2.getLearnerAddress2());

		}
		else
		{
			lp.getLearnerAddress2().setCity(lp2.getLearnerAddress2().getCity());
			lp.getLearnerAddress2().setCountry(lp2.getLearnerAddress2().getCountry());
			lp.getLearnerAddress2().setProvince(lp2.getLearnerAddress2().getProvince());
			lp.getLearnerAddress2().setState(lp2.getLearnerAddress2().getState());
			lp.getLearnerAddress2().setStreetAddress(lp2.getLearnerAddress2().getStreetAddress());
			lp.getLearnerAddress2().setStreetAddress2(lp2.getLearnerAddress2().getStreetAddress2());
			lp.getLearnerAddress2().setStreetAddress3(lp2.getLearnerAddress2().getStreetAddress3());
			lp.getLearnerAddress2().setZipcode(lp2.getLearnerAddress2().getZipcode());
		}

		// cascade update instructor related entities
		Instructor i=user.getInstructor();
		Instructor i2=this.getInstructor();
		if(i==null || (i!=null && i2==null))//it means instructor should be set by new user value
		{
			this.setInstructor(i);
		}
		else if (i!=null && i2!=null) //it means instructor was null now instructor is assigned to user
		{
			i2.setActive(i.isActive());
			i2.setApprovedCourses(i.getApprovedCourses());
			i2.setAreaOfExpertise(i.getAreaOfExpertise());
			i2.setCustomfields(i.getCustomfields());
			i2.setCustomfieldValues(i.getCustomfieldValues());
			i2.setUser(user);
			user.setInstructor(i2);
		}

		// cascade update role entities
		RegulatoryAnalyst ra=user.getRegulatoryAnalyst();
		RegulatoryAnalyst ra2=this.getRegulatoryAnalyst();
		if(ra==null || (ra!=null && ra2==null))
		{
			this.setRegulatoryAnalyst(ra);
		}
		else if (ra!=null && ra2!=null)
		{
			ra2.setContentOwners(ra.getContentOwners());
			ra2.setForAllContentOwner(ra.isForAllContentOwner());
			ra2.setUser(user);
			user.setRegulatoryAnalyst(ra2);
		}
		//cascade LMSAdministrator change
		LMSAdministrator admin2=this.getLmsAdministrator();
		LMSAdministrator admin=user.getLmsAdministrator();
		if(admin==null || (admin!=null && admin2==null))
		{
			this.setLmsAdministrator(admin);
		}
		else if (admin!=null && admin2!=null)
		{
			admin2.setGlobalAdministrator(admin.isGlobalAdministrator());
			admin2.setDistributorGroups(admin.getDistributorGroups());
			admin2.setVu360User(user);
			user.setLmsAdministrator(admin2);
		}
		//cascade training administrator
		TrainingAdministrator trainingAdmin2=this.getTrainingAdministrator();
		TrainingAdministrator trainingAdmin=user.getTrainingAdministrator();
		if(trainingAdmin==null || (trainingAdmin!=null && trainingAdmin2==null))
		{
			this.setTrainingAdministrator(trainingAdmin);
		}
		else if (trainingAdmin!=null && trainingAdmin2!=null)
		{
			trainingAdmin2.setCustomer(trainingAdmin.getCustomer());
			trainingAdmin2.setManagedGroups(trainingAdmin.getManagedGroups());
			trainingAdmin2.setVu360User(user);
			trainingAdmin2.setManagesAllOrganizationalGroups(trainingAdmin.isManagesAllOrganizationalGroups());
			user.setTrainingAdministrator(trainingAdmin2);
		}
		//cascade lms roles
		this.setLmsRoles(user.getLmsRoles());
		this.setNotifyOnLicenseExpire(user.getNotifyOnLicenseExpire());
	}

	public void shallowCopy(VU360User user) {
		this.setAcceptedEULA(user.isAcceptedEULA());
		this.setNewUser(user.isNewUser());
		this.setAccountNonExpired(user.getAccountNonExpired());
		this.setAccountNonLocked(user.getAccountNonLocked());
		this.setChangePasswordOnLogin(user.getChangePasswordOnLogin());
		this.setCreatedDate(user.getCreatedDate());
		this.setCredentialsNonExpired(user.isCredentialsNonExpired());
		this.setEmailAddress(user.getEmailAddress());
		this.setEnabled(user.getEnabled());
		this.setExpirationDate(user.getExpirationDate());
		this.setLastLogonDate(user.getLastLogonDate());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setLastUpdatedDate(user.getLastUpdatedDate());
		this.setMiddleName(user.getMiddleName());
		this.setNumLogons(user.getNumLogons());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		this.setVissibleOnReport(user.getVissibleOnReport());
		this.setUserGUID(user.getUserGUID());
		this.setShowGuidedTourScreenOnLogin(user.getShowGuidedTourScreenOnLogin());
//		this.setLmsRoles(user.getLmsRoles());
	}

	// [2/24/2011] LMS-9120 :: Centralized the method to fetch the User's
	// Content Owner
	public ContentOwner getContentOwner() {

		ContentOwner contentOwner = null;
		try {
			contentOwner = this.getLearner().getCustomer().getContentOwner();
			if (contentOwner == null) {
				contentOwner = this.getLearner().getCustomer().getDistributor()
						.getContentOwner();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contentOwner;
	}

	public  Boolean isAdminMode() {
		return isAdminMode;
	}

	public void setAdminMode(Boolean isAdminMode) {
		this.isAdminMode = isAdminMode;
	}

	public  Boolean isProctorMode() {
		return isProctorMode;
	}

	public void setProctorMode(Boolean isProctorMode) {
		this.isProctorMode = isProctorMode;
	}

	public  Boolean isManagerMode() {
		return isManagerMode;
	}

	public  Boolean isInstructorMode() {
		return isInstructorMode;
	}

	public void setManagerMode(Boolean isManagerMode) {
		this.isManagerMode = isManagerMode;
	}

	public  Boolean isLearnerMode() {
		return this.isLearnerMode;
	}

	public void setLearnerMode(Boolean isLearnerMode) {
		this.isLearnerMode = isLearnerMode;
	}

	public void setInstructorMode(Boolean isInstructorMode) {
		this.isInstructorMode = isInstructorMode;
	}

	public  Boolean isAccreditationMode() {
		return isAccreditationMode;
	}

	public void setAccreditationMode(Boolean isAccreditationMode) {
		this.isAccreditationMode = isAccreditationMode;
	}

	public LMSRole getLogInAsManagerRole() {
		return logInAsManagerRole;
	}

	public void setLogInAsManagerRole(LMSRole logInAsManagerRole) {
		this.logInAsManagerRole = logInAsManagerRole;
	}

	public  Boolean getShowGuidedTourScreenOnLogin() {
		if(showGuidedTourScreenOnLogin==null){
			showGuidedTourScreenOnLogin = Boolean.TRUE;
		}

		return showGuidedTourScreenOnLogin;
	}

	public void setShowGuidedTourScreenOnLogin(
			Boolean showGuidedTourScreenOnLogin) {

		if(showGuidedTourScreenOnLogin==null){
			this.showGuidedTourScreenOnLogin = Boolean.TRUE;
		}
		else{
			this.showGuidedTourScreenOnLogin = showGuidedTourScreenOnLogin;
		}
	}

	public Learner getLearnerProxy() {
		return learner;
	}

	public  Boolean getNotifyOnLicenseExpire() {
		if(notifyOnLicenseExpire == null)
			notifyOnLicenseExpire = Boolean.TRUE;

		return notifyOnLicenseExpire;
	}

	public void setNotifyOnLicenseExpire(Boolean notifyOnLicenseExpire) {
		if(notifyOnLicenseExpire==null)
			notifyOnLicenseExpire = Boolean.TRUE;
		else
			this.notifyOnLicenseExpire = notifyOnLicenseExpire;
	}

	public Long getRoleID() {
		return roleID;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getLearnerID() {
		return learnerID;
	}

	public void setLearnerID(Long learnerID) {
		this.learnerID = learnerID;
	}

	public Integer getAccountNonLockedInt() {
		return accountNonLockedInt;
	}

	public void setAccountNonLockedInt(Integer accountNonLockedInt) {
		this.accountNonLockedInt = accountNonLockedInt;
	}

	public VU360User(Long id, String firstName, String lastName,
					 String userName, String emailAddress, Integer accountNonLockedInt,
					 Long learnerID,Long lmsAdministratorID, Long trainingAdministratorID,String roleName,Long roleID) {
		super();
		this.id = id;
		this.username = userName;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountNonLocked = accountNonLockedInt==0?Boolean.FALSE:Boolean.TRUE;
		Learner l = new Learner();
		l.setId(learnerID);
		this.learner = l;

		if(lmsAdministratorID!=0) {
			LMSAdministrator lmsAdmin= new LMSAdministrator();
			lmsAdmin.setId(lmsAdministratorID);
			this.lmsAdministrator=lmsAdmin;
		}

		if(trainingAdministratorID!=0){
			TrainingAdministrator trainingAdmin=new TrainingAdministrator();
			trainingAdmin.setId(trainingAdministratorID);
			this.trainingAdministrator=trainingAdmin;
		}

		if(roleID!=0){
			LMSRole role=new LMSRole();
			role.setId(roleID);
			role.setRoleName(roleName);
			this.getLmsRoles().add(role);
		}

	}

	public VU360User() {

	}

	@Override
	public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
		return interceptor;
	}

	@Override
	public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor persistentAttributeInterceptor) {
		this.interceptor = persistentAttributeInterceptor;
	}
}
