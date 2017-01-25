/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Haider.ali
 * 
 **/
@Entity
@Table(name = "LMSRESOURCETYPE")
public class ResourceType implements SearchableKey {
	
	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "LMSRESOURCETYPE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSRESOURCETYPE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSRESOURCETYPE_ID")
	private Long id;
	
	@Column(name="RESOURCETYPENAME")
	private String name = null;

	@Column(name="DESCRIPTION")
	private String description = null;

    @OneToOne
    @JoinColumn(name="CONTENTOWNER_ID")
    private ContentOwner contentOwner ;
	
	@Column(name="ENABLEDTF")
	private  Boolean isActive =  true;
	

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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public ContentOwner getContentOwner() {
		return contentOwner;
	}


	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}


	public  Boolean isActive() {
		return isActive;
	}


	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	

}
