/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "Location")
public class Location implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "LOCATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LOCATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LOCATION_ID")
	private Long id;
	
	@Column(name="LOCATIONNAME")
	private String name = null;
	
	@Column(name="PHONE")
	private String phone = null;
	
	@Column(name="DESCRIPTION")
	private String description = null;
	
	@OneToOne
    @JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner ;
	
	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "ADDRESS_ID")
	private Address address ;
	
	@Column(name="ENABLEDTF")
	private Boolean isActive = true;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="LOCATION_CUSTOMFIELD", joinColumns = @JoinColumn(name="LOCATION_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customfields = new ArrayList<CustomField>();

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="LOCATION_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="LOCATION_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue>customfieldValues = new ArrayList<CustomFieldValue>();
	
	public List<CustomField> getCustomfields() {
		return customfields;
	}
	public void setCustomfields(List<CustomField> customfields) {
		this.customfields = customfields;
	}
	public List<CustomFieldValue> getCustomfieldValues() {
		return customfieldValues;
	}
	public void setCustomfieldValues(List<CustomFieldValue> customfieldValues) {
		this.customfieldValues = customfieldValues;
	}
	@Override
	public String getKey() {
		return id.toString();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public  Boolean isActive() {
		return isActive;
	}
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ContentOwner getContentowner() {
		return contentOwner;
	}
	public void setContentowner(ContentOwner contentowner) {
		this.contentOwner = contentowner;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	


	
}
