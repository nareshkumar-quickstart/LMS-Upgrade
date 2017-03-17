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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.softech.vu360.util.ProctorStatusEnum;

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
@NamedEntityGraphs({
	@NamedEntityGraph(name = "VU360User.allJoins", 
			attributeNodes = {
				@NamedAttributeNode(value = "learner", subgraph = "learnerGraph"),
				@NamedAttributeNode(value = "trainingAdministrator"/*, subgraph = "trainingAdministratorGraph"*/),
				@NamedAttributeNode("lmsAdministrator"),
				@NamedAttributeNode("proctor"),
				@NamedAttributeNode("regulatoryAnalyst"),
				@NamedAttributeNode("instructor"),
				@NamedAttributeNode(value = "lmsRoles", subgraph = "lmsRolesGraph")
			},
			subgraphs = {
					@NamedSubgraph(
							name = "learnerGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "customer", subgraph = "customerGraph"),
									@NamedAttributeNode(value = "learnerProfile", subgraph = "learnerProfileGraph"),
									@NamedAttributeNode(value = "preference")
							}
					),
					@NamedSubgraph(
							name = "customerGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "distributor", subgraph = "distributorGraph"),
									@NamedAttributeNode(value = "customerPreferences"),
									@NamedAttributeNode(value = "billingAddress"),
									@NamedAttributeNode(value = "shippingAddress")
							}
					),
					@NamedSubgraph(
							name = "learnerProfileGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "learnerAddress"),
									@NamedAttributeNode(value = "learnerAddress2")
							}
					),
					@NamedSubgraph(
							name = "distributorGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "distributorPreferences"),
									@NamedAttributeNode(value = "distributorAddress"),
									@NamedAttributeNode(value = "distributorAddress2")
							}
					),
					@NamedSubgraph(
							name = "lmsRolesGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "lmsPermissions", subgraph = "lmsfeatureGraph")
							}
					),
					@NamedSubgraph(
							name = "lmsfeatureGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "lmsFeature")
							}
					)
					/*,
					@NamedSubgraph(
							name = "trainingAdministratorGraph",
							attributeNodes = {
									@NamedAttributeNode(value = "managedGroups")
							}
					)*/
			}
	)
})
public class VU360User implements Serializable {

	private static final long serialVersionUID = 52260678146352048L;
	private static Logger log = Logger.getLogger(VU360User.class);

	@Id
	@javax.persistence.TableGenerator(name = "VU360USER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "vu360user", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360USER_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "USERGUID", unique = true, nullable = false)
	private String userGUID = null;

	@Column(name = "USERNAME", unique = true, nullable = false)
	private String username = null;

	@Column(name = "DOMAIN")
	private String domain = null;

	@Column(name = "EMAILADDRESS", unique = false, nullable = false)
	private String emailAddress = null;

	@Column(name = "FIRSTNAME", unique = false, nullable = false)
	private String firstName = null;

	@Column(name = "LASTNAME", unique = false, nullable = false)
	private String lastName = null;

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
	
	@OneToOne(mappedBy="vu360User") 
	private Learner learner;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="vu360User" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TrainingAdministrator trainingAdministrator;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="vu360User" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private LMSAdministrator lmsAdministrator;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Proctor proctor;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user" , cascade = { CascadeType.PERSIST, CascadeType.MERGE }) 
	private RegulatoryAnalyst regulatoryAnalyst;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user" , cascade = { CascadeType.PERSIST, CascadeType.MERGE }) 
	private Instructor instructor;

	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.MERGE })
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public TrainingAdministrator getTrainingAdministrator() {
		return trainingAdministrator;
	}

	public void setTrainingAdministrator(TrainingAdministrator trainingAdministrator) {
		this.trainingAdministrator = trainingAdministrator;
	}

	public LMSAdministrator getLmsAdministrator() {
		return lmsAdministrator;
	}

	public void setLmsAdministrator(LMSAdministrator lmsAdministrator) {
		this.lmsAdministrator = lmsAdministrator;
	}

	public Proctor getProctor() {
		return proctor;
	}

	public void setProctor(Proctor proctor) {
		this.proctor = proctor;
	}

	public  Boolean isTrainingAdministrator() {
		if (this.getTrainingAdministrator() == null) {
			return false;
		} else {
			 Boolean isManager = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(
						LMSRole.ROLE_TRAININGMANAGER)
						&& atLeastOnePermssionEnable(role)) {
					isManager = true;
					break;
				}
			}
			return isManager;
		}

	}

	public  Boolean isLMSAdministrator() {
		if (this.getLmsAdministrator() == null) {
			return false;
		} else {
			 Boolean isAdmin = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(
						LMSRole.ROLE_LMSADMINISTRATOR)
						&& atLeastOnePermssionEnable(role)) {
					isAdmin = true;
					break;
				}
			}
			return isAdmin;
		}
	}

	public  Boolean isProctor() {
		if (this.getProctor() == null) {
			return false;
		} else {
			 Boolean isProctor = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)
						&& atLeastOnePermssionEnable(role)
						&& !(this.getProctor().getStatus()
								.equals(ProctorStatusEnum.Expired.toString()))) {
					isProctor = true;
					break;
				}
			}
			return isProctor;
		}
	}

	public  Boolean isInLearnerRole() {
		if (this.getLearner() == null) {
			return false;
		} else {

			 Boolean isLearner = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)
						&& atLeastOnePermssionEnable(role)) {
					isLearner = true;
					break;
				}
			}
			return isLearner;

		}
	}

	public  Boolean atLeastOnePermssionEnable(LMSRole role) {
		 Boolean result = false;
		for (LMSRoleLMSFeature permission : role.getLmsPermissions()) {
			if (permission.getEnabled()) {
				result = true;
				break;
			}
		}
		return result;

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
		return lmsRoles;
	}

	public void addLmsRole(LMSRole lmsRole) {
		lmsRoles.add(lmsRole);
	}

	public void setLmsRoles(Set<LMSRole> lmsRoles) {
		this.lmsRoles = lmsRoles;

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
		return this.regulatoryAnalyst;
	}

	public void setRegulatoryAnalyst(RegulatoryAnalyst regulatoryAnalyst) {
		this.regulatoryAnalyst = regulatoryAnalyst;
	}

	public  Boolean isRegulatoryAnalyst() {
		if (this.regulatoryAnalyst == null) {
			return false;
		} else {
			 Boolean isRegulator = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(
						LMSRole.ROLE_REGULATORYANALYST)
						&& atLeastOnePermssionEnable(role)) {
					isRegulator = true;
					break;
				}
			}
			return isRegulator;
		}
	}

	public Instructor getInstructor() {
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public  Boolean isInstructor() {
		if (this.instructor == null) {
			return false;
		} else {
			 Boolean isInstructor = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType()
						.equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)
						&& atLeastOnePermssionEnable(role)) {
					isInstructor = true;
					break;
				}
			}
			return isInstructor;
		}
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
        this.setDomain(user.getDomain());
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
		this.setDomain(user.getDomain());
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

	public void checkForPermission() {
		/*
		 * if the user has no roles, it will show the error message
		 * "Invalid username or password" as if user had typed in the wrong
		 * password.
		 */
		if (this.getLmsRoles().size() == 0) {
			throw new UsernameNotFoundException(
					"FOUND NO ROLES ASSIGNED TO USER");
		} else {
			 Boolean hasPermission = false;
			for (LMSRole role : this.getLmsRoles()) {
				if (this.atLeastOnePermssionEnable(role)) {
					hasPermission = true;
					break;
				}
			}
			if (!hasPermission) {
				throw new UsernameNotFoundException(
						"FOUND NO PERMISSIONS ASSIGNED TO USER");
			}
		}
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

}
