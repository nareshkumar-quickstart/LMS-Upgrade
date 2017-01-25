/**
 * 
 */
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


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PROVIDER")
public class Provider implements SearchableKey {
	
	@Id
    @javax.persistence.TableGenerator(name = "PROVIDER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "PROVIDER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PROVIDER_ID")
	private Long id;
	
	@Column(name = "PROVIDERNAME")
	private String name = null;
	
	@Column(name = "CONTACTNAME")
	private String contactName = null;
	
	@Column(name = "PHONE")
	private String phone = null;
	
	@Column(name = "FAX")
	private String fax = null;
	
	@Column(name = "WEBSITE")
	private String website = null;
	
	@Column(name = "EMAILADDRESS")
	private String emailAddress = null;
	
	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="ADDRESS_ID")
	private Address address = null;
	
	@OneToOne
    @JoinColumn(name="CONTENTOWNER_ID")
	private ContentOwner contentOwner = null;
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="PROVIDER_CUSTOMFIELD", joinColumns = @JoinColumn(name="PROVIDER_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
    private List<CustomField> customfields = new ArrayList<CustomField>();
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="PROVIDER_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="PROVIDER_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
    private List<CustomFieldValue>customfieldValues = new ArrayList<CustomFieldValue>();
	
	@ManyToMany
    @JoinTable(name="PROVIDER_ASSET", joinColumns = @JoinColumn(name="PROVIDER_ID"),inverseJoinColumns = @JoinColumn(name="ASSET_ID"))
    private List<Document> documents = new ArrayList<Document>();
	
	@Column(name = "active")
	private Boolean active = true;
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
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
	 * @return the addres
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param addres the addres to set
	 */
	public void setAddress(Address address) {
		this.address = address;
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
	 * @return the customfields
	 */
	public List<CustomField> getCustomfields() {
		return customfields;
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
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
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
	
	public Provider(Long id,String providerName){
		this.id = id;
		this.name = providerName;
	}

	public Provider() {
		// TODO Auto-generated constructor stub
	}

}
