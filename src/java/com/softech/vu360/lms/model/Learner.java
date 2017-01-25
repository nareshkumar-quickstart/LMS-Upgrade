package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "LEARNER")
@NamedStoredProcedureQueries({ @NamedStoredProcedureQuery(name = "Learner.getFilteredRecipientsByAlert", procedureName = "GET_FILTER_RECIPIENTS_BY_ALERT", resultClasses = VU360User.class, parameters = { @StoredProcedureParameter(mode = ParameterMode.IN, name = "alert_id", type = Long.class) }) })
@NamedEntityGraph(name = "Learner.getLearnerFromLearnerGroupMemberComposition", attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode(value = "vu360User", subgraph = "vu360UserSubGraph") }, subgraphs = { @NamedSubgraph(name = "vu360UserSubGraph", attributeNodes = {
		@NamedAttributeNode("id"), @NamedAttributeNode("firstName"),
		@NamedAttributeNode("lastName"), @NamedAttributeNode("emailAddress") }) })
public class Learner implements SearchableKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * @Id
	 * 
	 * @javax.persistence.TableGenerator(name = "LEARNER_ID", table =
	 * "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID",
	 * pkColumnValue = "LEARNER", allocationSize = 1)
	 * 
	 * @GeneratedValue(strategy = GenerationType.TABLE, generator =
	 * "LEARNER_ID") private Long id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqLearnerId")
	@GenericGenerator(name = "seqLearnerId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
			@Parameter(name = "table_name", value = "VU360_SEQ"),
			@Parameter(name = "value_column_name", value = "NEXT_ID"),
			@Parameter(name = "segment_column_name", value = "TABLE_NAME"),
			@Parameter(name = "segment_value", value = "LEARNER") })
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinColumn(name = "VU360USER_ID")
	private VU360User vu360User;

	@OneToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;

	@OneToOne(mappedBy = "learner")
	private LearnerProfile learnerProfile;

	@OneToOne(mappedBy = "learner")
	private LearnerPreferences preference;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "LEARNER_LEARNERGROUP", joinColumns = @JoinColumn(name = "LEARNER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "LEARNERGROUP_ID", referencedColumnName = "ID"))
	private List<LearnerGroup> learnerGroup = new ArrayList<LearnerGroup>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the vu360User
	 */
	public VU360User getVu360User() {
		return this.vu360User;
	}

	/**
	 * @param vu360User
	 *            the vu360User to set
	 */
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return this.customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the learnerProfile
	 */
	public LearnerProfile getLearnerProfile() {
		return this.learnerProfile;
	}

	/**
	 * @param learnerProfile
	 *            the learnerProfile to set
	 */
	public void setLearnerProfile(LearnerProfile learnerProfile) {
		this.learnerProfile = learnerProfile;
	}

	/**
	 * @return the preference
	 */
	public LearnerPreferences getPreference() {
		return this.preference;
	}

	/**
	 * @param preference
	 *            the preference to set
	 */
	public void setPreference(LearnerPreferences preference) {
		this.preference = preference;
	}

	/**
	 * @return the learnerGroup
	 */
	public List<LearnerGroup> getLearnerGroup() {
		return learnerGroup;
	}

	/**
	 * @param learnerGroup
	 *            the learnerGroup to set
	 */
	public void setLearnerGroup(List<LearnerGroup> learnerGroup) {
		this.learnerGroup = learnerGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((learnerProfile == null) ? 0 : learnerProfile.hashCode());
		result = prime * result
				+ ((preference == null) ? 0 : preference.hashCode());
		result = prime * result
				+ ((vu360User == null) ? 0 : vu360User.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Learner other = (Learner) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (learnerProfile == null) {
			if (other.learnerProfile != null)
				return false;
		} else if (!learnerProfile.equals(other.learnerProfile))
			return false;
		if (preference == null) {
			if (other.preference != null)
				return false;
		} else if (!preference.equals(other.preference))
			return false;
		if (vu360User == null) {
			if (other.vu360User != null)
				return false;
		} else if (!vu360User.equals(other.vu360User))
			return false;
		return true;
	}

	// Overloded Constructor to find Learner By oprganizatinal Group Member
	// Composite Model Bean
	public Learner(Long learnerId, Long userID, Boolean accountNonExpired,
			Boolean accountNonLocked, Boolean enabled, Date expirationDate,
			String userFirstName, String userLastName, String userEmailAddress,
			String customerName, String customerType, String distributorName) {
		this.id = learnerId;

		VU360User user = new VU360User();

		if (accountNonExpired == null) {
			accountNonExpired = Boolean.TRUE;
		}
		if (accountNonExpired) {
			if (expirationDate != null && expirationDate.before(new Date())) {
				accountNonExpired = Boolean.FALSE;
			}
		}

		if (accountNonLocked == null) {
			accountNonLocked = Boolean.TRUE;
		}
		if (enabled == null) {
			enabled = Boolean.TRUE;
		}
		user.setId(userID);
		user.setAccountNonExpired(accountNonExpired);
		user.setAccountNonLocked(accountNonLocked);
		user.setEnabled(enabled);
		user.setFirstName(userFirstName);
		user.setLastName(userLastName);
		user.setEmailAddress(userEmailAddress);

		Customer customer = new Customer();
		customer.setName(customerName);
		customer.setCustomerType(customerType);
		Distributor distributor = new Distributor();
		distributor.setName(distributorName);
		customer.setDistributor(distributor);

		this.vu360User = user;
		this.customer = customer;
	}

	// Overloded Constructor to find Learner By Learner Group Member
	// Composite Model Bean
	public Learner(Long learnerId, Long userID, Boolean accountNonLocked, String userName, String userFirstName, String userLastName, String userEmailAddress,Long lmsAdministratorId,Long trainingAdministratorId) {
		this.id = learnerId;

		VU360User user = new VU360User();

		if (accountNonLocked == null) {
			accountNonLocked = Boolean.TRUE;
		}
		
		user.setId(userID);
		user.setAccountNonLocked(accountNonLocked);
		user.setFirstName(userFirstName);
		user.setLastName(userLastName);
		user.setEmailAddress(userEmailAddress);
		user.setUsername(userName);
		
		if(lmsAdministratorId != null){
			LMSAdministrator lmsAdministrator = new LMSAdministrator();
			lmsAdministrator.setId(lmsAdministratorId);
			user.setLmsAdministrator(lmsAdministrator);
		}
		
		if(trainingAdministratorId != null){
			TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
			trainingAdministrator.setId(trainingAdministratorId);
			user.setTrainingAdministrator(trainingAdministrator);
		}

		this.vu360User = user;
	}

	public Learner() {
		// TODO Auto-generated constructor stub
	}

}
