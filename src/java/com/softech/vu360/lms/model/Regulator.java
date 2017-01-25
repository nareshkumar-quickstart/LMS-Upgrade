package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */

@Entity
@Table(name = "REGULATOR")
public class Regulator implements SearchableKey{

	@Id
	@javax.persistence.TableGenerator(name = "REGULATOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATOR", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATOR_ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner = null;
	
	@Column(name = "NAME")
	private String name = null;
	
	@Column(name = "ALIAS")
	private String alias = null;
	
	@Column(name = "PHONE")
	private String phone = null;
	
	@Column(name = "FAX")
	private String fax = null;
	
	@Column(name = "WEBSITE")
	private String website = null;
	
	@Column(name = "EMAILADDRESS")
	private String emailAddress = null;
	
	@Column(name = "JURISDICTIONS")
	private String jurisdiction = null;
	
	@OneToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name = "ADDRESS_ID")
	private Address address = null;
	
	@OneToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name = "ADDRESS2_ID")
	private Address  address2 ;
	
	@ManyToMany (cascade={CascadeType.MERGE}) // Added By Marium Saud -- Cascade Configure here to avoid object references an unsaved transient instance LMS-18781
	@JoinTable(name="REGULATOR_ASSET", joinColumns = @JoinColumn(name="REGULATOR_ID"),inverseJoinColumns = @JoinColumn(name="ASSET_ID"))
	private List<Document> documents = new ArrayList<Document>();
	
	@ManyToMany
	@JoinTable(name="CREDENTIAL_REGULATOR", joinColumns = @JoinColumn(name="REGULATOR_ID"),inverseJoinColumns = @JoinColumn(name="CREDENTIAL_ID"))
	private List<Credential> credentials = new ArrayList<Credential>();
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(name="REGULATOR_CUSTOMFIELD", joinColumns = @JoinColumn(name="REGULATOR_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customfields = new ArrayList<CustomField>();
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(name="REGULATOR_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="REGULATOR_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue>customfieldValues = new ArrayList<CustomFieldValue>();	
	
	@Column(name = "active")
	private  Boolean active = true;
	
	@Transient
	private String otherJurisdiction= null;


	public String getKey() {
		return id.toString();
	}


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
	 * @return the contentOwner
	 */
	public ContentOwner getContentOwner() {
		return contentOwner;
	}


	/**
	 * @param contentOwner the contentOwner to set
	 */
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}


	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}


	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}


	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}


	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}


	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}


	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}


	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}


	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}


	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	/**
	 * @return the jurisdiction
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}


	/**
	 * @param jurisdiction the jurisdiction to set
	 */
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;		
		this.otherJurisdiction=jurisdiction;		
	}


	/**
	 * @return the otherJurisdiction
	 */
	public String getOtherJurisdiction() {
		return otherJurisdiction;
	}


	/**
	 * @param otherJurisdiction the otherJurisdiction to set
	 */
	public void setOtherJurisdiction(String otherJurisdiction) {
		this.otherJurisdiction = otherJurisdiction;
	}


	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}


	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}


	/**
	 * @return the credentials
	 */
	public List<Credential> getCredentials() {
		List<Credential> activeCredentials = new ArrayList<Credential>();
		if( credentials != null ) {
			for( Credential activeCredential : credentials ) {
				if( activeCredential.isActive() )
					activeCredentials.add(activeCredential);
			}
		}
		return activeCredentials;
	}

	public void addCredential(Credential credential) {
		credentials.add(credential);
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}


	/**
	 * @return the customfields
	 */
	public List<CustomField> getCustomfields() {
		return customfields;
	}

	public void addCustomfield(CustomField customfield) {
		customfields.add(customfield);
	}

	/**
	 * @param customfields the customfields to set
	 */
	public void setCustomfields(List<CustomField> customfields) {
		this.customfields = customfields;
	}


	/**
	 * @return the customfieldValues
	 */
	public List<CustomFieldValue> getCustomfieldValues() {
		return customfieldValues;
	}

	public void addCustomFieldValue(CustomFieldValue customfieldValue) {
		customfieldValues.add(customfieldValue);
	}
	/**
	 * @param customfieldValues the customfieldValues to set
	 */
	public void setCustomfieldValues(List<CustomFieldValue> customfieldValues) {
		this.customfieldValues = customfieldValues;
	}


	/**
	 * @return the documents
	 */
	public List<Document> getDocuments() {
		/*List<Document> activeDocuments = new ArrayList<Document>();
		for( Document doc : documents){
			if( doc.isActive() )
				activeDocuments.add(doc);
		}
		return activeDocuments;*/
		return documents;
	}


	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}


	/**
	 * @return the active
	 */
	public  Boolean isActive() {
		return active;
	}


	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}


	public Address getAddress2() {
		return this.address2;
	}


	public void setAddress2(Address address2) {
		this.address2 = address2;
	}

	public Regulator(Long id,String name){
		this.id = id;
		this.name = name;
	}


	public Regulator() {
		// TODO Auto-generated constructor stub
	}
	
}