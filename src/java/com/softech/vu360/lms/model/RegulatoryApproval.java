package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "REGULATORYAPPROVAL")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="APPROVALTYPE")
@NamedQueries({
	@NamedQuery(name="RegulatoryApproval.getByRequirements",query="SELECT ra FROM RegulatoryApproval ra join ra.requirements r  WHERE r.id IN (?1)"),
})
public abstract class RegulatoryApproval implements SearchableKey{

	private static final long serialVersionUID = -7979658563159516546L;

	@Id
	@javax.persistence.TableGenerator(name = "REGULATORYAPPROVAL_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATORYAPPROVAL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATORYAPPROVAL_ID")
	private Long id;
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="REGULATORYAPPROVAL_CUSTOMFIELD", joinColumns = @JoinColumn(name="REGULATORYAPPROVAL_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
    private List<CustomField> customFields = new ArrayList<CustomField>();
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="REGULATORYAPPROVAL_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="REGULATORAPPROVAL_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
    private List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
	
	@ManyToMany  (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="REGULATORYAPPROVAL_ASSET", joinColumns = @JoinColumn(name="REGULATORYAPPROVAL_ID"),inverseJoinColumns = @JoinColumn(name="ASSET_ID"))
    private List<Document> documents = new ArrayList<Document>();
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="CONTENTOWNER_ID")
	private ContentOwner contentOwner = null;
	
	@ManyToMany  (cascade = { CascadeType.MERGE })
    @JoinTable(name="REGULATORYAPPROVAL_CREDENTIALCATEGORYREQUIREMENT", joinColumns = @JoinColumn(name="REGULATORYAPPROVAL_ID"),inverseJoinColumns = @JoinColumn(name="CREDENTIALCATEGORYREQUIREMENT_ID"))
    private List<CredentialCategoryRequirement> requirements = new ArrayList<CredentialCategoryRequirement>();
	
	@Column(name = "deleted")
	private Boolean deleted = false;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="REGULATORCATEGORY_ID")
	private RegulatorCategory regulatorCategory = null;
	
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
	 * @return the customFields
	 */
	public List<CustomField> getCustomFields() {
		return customFields;
	}

	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}

	/**
	 * @return the customFieldValues
	 */
	public List<CustomFieldValue> getCustomFieldValues() {
		return customFieldValues;
	}

	/**
	 * @param customFieldValues the customFieldValues to set
	 */
	public void setCustomFieldValues(List<CustomFieldValue> customFieldValues) {
		this.customFieldValues = customFieldValues;
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
	 * @return the requirements
	 */
	public List<CredentialCategoryRequirement> getRequirements() {
		return requirements;
	}

	/**
	 * @param requirements the requirements to set
	 */
	public void setRequirements(List<CredentialCategoryRequirement> requirements) {
		this.requirements = requirements;
	}

	/**
	 * @return the regulators
	 */
	/*
	public List<RegulatorCategory> getRegulatorCategories() {
		
		return regulatorCategories;
	}
	*/

	/**
	 * @param regulators the regulators to set
	 */
	/*
	public void setRegulatorCategories(List<RegulatorCategory> regulatorCategories) {
		this.regulatorCategories = regulatorCategories;
	}
	*/

	/**
	 * @return the deleted
	 */
	public  Boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public RegulatorCategory getRegulatorCategory() {
		return regulatorCategory;
	}

	public void setRegulatorCategory(RegulatorCategory regulatorCategory) {
		this.regulatorCategory = regulatorCategory;
	}

	public List<RegulatorCategory> getRegulatorCategories() {
		ArrayList list = new ArrayList<RegulatorCategory>();
		if(regulatorCategory!=null)
			list.add(regulatorCategory);
		
		return list;
	}

	public void setRegulatorCategories(List<RegulatorCategory> regulatorCategories) {
		this.regulatorCategory = (regulatorCategories == null || regulatorCategories.size() == 0)? null : regulatorCategories.get(0);
	}

	
}
