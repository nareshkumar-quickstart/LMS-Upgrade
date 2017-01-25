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
 * @author haider.ali,
 * @dated: 15-09-15
 *
 */

@Entity
@Table(name = "LMSRESOURCE")
public class Resource implements SearchableKey {

	private static final long serialVersionUID = -2096276996637684020L;

	@Id
	@javax.persistence.TableGenerator(name = "LMSRESOURCE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSRESOURCE", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSRESOURCE_ID")
	private Long id;

	@Column(name = "RESOURCENAME")
	private String name = null;
	
	@Column(name = "ASSETTAGNUMBER")
	private String assetTagNumber = null;
	
	@Column(name = "DESCRIPTION")
	private String description = null;

	@OneToOne
	@JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner ;
	
	@OneToOne
	@JoinColumn(name = "LMSRESOURCETYPE_ID")
	private ResourceType resourceType ;

	@ManyToMany (fetch=FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name="LMSRESOURCE_CUSTOMFIELD", joinColumns = @JoinColumn(name="LMSRESOURCE_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customfields = new ArrayList<CustomField>();
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name="LMSRESOURCE_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"),inverseJoinColumns = @JoinColumn(name="LMSRESOURCE_ID"))
	private List<CustomFieldValue> customfieldValues = new ArrayList<CustomFieldValue>();
	
	@Column(name = "ENABLEDTF")
	private  Boolean isActive = true;
	
	
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


	public String getAssetTagNumber() {
		return assetTagNumber;
	}


	public void setAssetTagNumber(String assetTagNumber) {
		this.assetTagNumber = assetTagNumber;
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


	public ResourceType getResourceType() {
		return resourceType;
	}


	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}


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


	public  Boolean isActive() {
		return isActive;
	}


	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	

}
