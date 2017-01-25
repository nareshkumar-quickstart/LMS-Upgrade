/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
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
@Table(name = "CONTACT")
public class Contact implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "CONTACT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CONTACT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CONTACT_ID")
	private Long id;
	
	@Column(name = "FIRSTNAME")
    private String firstName = null;
	
	@Column(name = "MIDDLENAME")
    private String middleName= null;
	
	@Column(name = "LASTNAME")
    private String lastName = null;
	
	@Column(name = "PRIMARYPHONE")
    private String phone = null;
	
	@Column(name = "PHONEEXT")
    private String phoneExt = null;
	
	@Column(name = "EMAILADDRESS")
    private String emailAddress = null;
    
    @Column(name = "WEBSITEURL")
    private String websiteURL = null;
    
    @Column(name = "TITLE")
    private String title = null;
    
    @OneToOne
    @JoinColumn(name="REGULATOR_ID")
    private Regulator regulator;
    
    @OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="PRIMARYADDRESS_ID")
    private Address address = null;
    
    @OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="ADDRESS2_ID")
    private Address address2 ;
    
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the regulator
	 */
	public Regulator getRegulator() {
		return regulator;
	}

	/**
	 * @param regulator the regulator to set
	 */
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
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
	 * @return the address2
	 */
	public Address getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(Address address2) {
		this.address2 = address2;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPhoneExt() {
		return phoneExt;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

}
