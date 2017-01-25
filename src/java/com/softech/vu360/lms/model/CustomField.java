package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@Table(name = "CUSTOMFIELD")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="FIELDTYPE")
public class CustomField implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	public static final String HORIZONTAL = "horizonatl";
	public static final String VERTICAL = "vertical";
	
	@Id
	@javax.persistence.TableGenerator(name = "CUSTOMFIELD_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMFIELD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMFIELD_ID")
	private Long id;
	
	@Column(name = "FIELDENCRYPTEDTF")
	private Boolean fieldEncrypted = Boolean.FALSE;
	
	@Column(name = "FIELDLABEL")
	private String fieldLabel = null;
	
	@Column(name = "FIELDREQUIREDTF")
	private Boolean fieldRequired = false;
	
	@Column(insertable=false,updatable=false)
	private String fieldType = null;
	
	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="CUSTOMFIELDCONTXT_ID")
	private CustomFieldContext customFieldContext = null;
	
	@Column(name = "DESCRIPTION")
	private String customFieldDescription = null;
	
	@Column(name = "ALIGNMENT")
	private String alignment = HORIZONTAL;
	
	@Column(name = "ISGLOBALTF")
	private Boolean global = false;
	
	@Column(name = "active")
	private Boolean active = true;
	
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

	public  Boolean getFieldEncrypted() {
		return fieldEncrypted;
	}

	public void setFieldEncrypted(Boolean fieldEncrypted) {
		this.fieldEncrypted = fieldEncrypted;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public  Boolean getFieldRequired() {
		return fieldRequired;
	}

	public void setFieldRequired(Boolean fieldRequired) {
		this.fieldRequired = fieldRequired;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public CustomFieldContext getCustomFieldContext() {
		return customFieldContext;
	}

	public void setCustomFieldContext(CustomFieldContext customFieldContext) {
		this.customFieldContext = customFieldContext;
	}

	public String getCustomFieldDescription() {
		return customFieldDescription;
	}

	public void setCustomFieldDescription(String customFieldDescription) {
		this.customFieldDescription = customFieldDescription;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public  Boolean isGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	public  Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

    @Override
    public  boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomField other = (CustomField) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}