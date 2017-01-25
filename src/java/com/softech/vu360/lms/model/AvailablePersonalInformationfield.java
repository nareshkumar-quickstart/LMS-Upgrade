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
@Table(name = "AVAILABLEPERSONALINFORMATIONFIELD")
public class AvailablePersonalInformationfield implements SearchableKey {
     
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "AVAILABLEPERSONALINFORMATIONFIELD_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AVAILABLEPERSONALINFORMATIONFIELD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AVAILABLEPERSONALINFORMATIONFIELD_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="AVAILABLEPERSONALINFORMATIONFIELDITEM_ID")
	private AvailablePersonalInformationfieldItem availablePersonalInformationfieldItem;
	
	@Column(name = "DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@Column(name = "REQUIREDTF")
	private Boolean fieldsRequired = Boolean.FALSE;
	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
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
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}


	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}


	/**
	 * @return the fieldsRequired
	 */
	public  Boolean isFieldsRequired() {
		return fieldsRequired;
	}


	/**
	 * @param fieldsRequired the fieldsRequired to set
	 */
	public void setFieldsRequired(Boolean fieldsRequired) {
		if(fieldsRequired==null){
			this.fieldsRequired=Boolean.FALSE;
		}
		else{
			this.fieldsRequired = fieldsRequired;
		}
	}


	/**
	 * @return the availablePersonalInformationfieldItem
	 */
	public AvailablePersonalInformationfieldItem getAvailablePersonalInformationfieldItem() {
		return availablePersonalInformationfieldItem;
	}


	/**
	 * @param availablePersonalInformationfieldItem the availablePersonalInformationfieldItem to set
	 */
	public void setAvailablePersonalInformationfieldItem(
			AvailablePersonalInformationfieldItem availablePersonalInformationfieldItem) {
		this.availablePersonalInformationfieldItem = availablePersonalInformationfieldItem;
	}

}