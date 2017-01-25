package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softech.vu360.util.ObjectToStringConverter;
import com.softech.vu360.util.SecurityUtil;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud add ObjectToStringConverter.class 
 *
 */

@Entity
@Table(name = "CUSTOMFIELDVALUE")
public class CustomFieldValue implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "CUSTOMFIELDVALUE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMFIELDVALUE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMFIELDVALUE_ID")
	private Long id;
	
	@OneToOne  (cascade = { CascadeType.MERGE })
    @JoinColumn(name="CUSTOMFIELD_ID")
	private CustomField customField = null;
	
	@Column(name="VALUE")
	@Convert(converter=ObjectToStringConverter.class)
	private Object value = null;
	
	@ManyToMany
	@JoinTable(name="CUSTOMFIELDVALUECHOICE_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"),inverseJoinColumns = @JoinColumn(name="SINGLESELECTCUSTOMFIELDOPTION_ID"))
	private List<CustomFieldValueChoice> valueItems = new ArrayList<CustomFieldValueChoice>();

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
	 * @return the customField
	 */
	public CustomField getCustomField() {
		return customField;
	}

	/**
	 * @param customField the customField to set
	 */
	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		if( customField != null ) {
			if( customField.getFieldEncrypted() ) {
				try {
					SecurityUtil su = SecurityUtil.getInstance();
					return su.decrypt(value != null ? value.toString() : "");
				} catch ( Exception e ) {
					e.printStackTrace();
				}	
			}
		}
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		if( customField != null ) {
			if( customField.getFieldEncrypted() ) {
				try {
					SecurityUtil su = SecurityUtil.getInstance();
					value = su.encryptData(value.toString().getBytes());
				} catch ( Exception e ) {
					e.printStackTrace();
				}	
			}
		}
		this.value = value;
	}
	
	public String getValueForDisplay(){
		if( customField != null ) {
			if(customField instanceof SSNCustomFiled){
				if(customField.getFieldEncrypted()){
					try{
						SecurityUtil su = SecurityUtil.getInstance();
						String strValue = su.decrypt(value.toString());
						if(strValue.length()>4){
							strValue=strValue.substring(strValue.length()-4,strValue.length());
							return "*********-"+strValue;
						}
					}catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}
			if( customField.getFieldEncrypted() ) {
				try {
					SecurityUtil su = SecurityUtil.getInstance();
					return su.decrypt(value.toString());
				} catch ( Exception e ) {
					e.printStackTrace();
				}	
			}
		}
		return value.toString();
	}

	/**
	 * @return the valueItems
	 */
	public List<CustomFieldValueChoice> getValueItems() {
		return valueItems;
	}

	/**
	 * @param valueItems the valueItems to set
	 */
	public void setValueItems(List<CustomFieldValueChoice> valueItems) {
		this.valueItems = valueItems;
	}

}