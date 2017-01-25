/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "GRADEBOOK")
public class Gradebook implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "GRADEBOOK_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "GRADEBOOK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "GRADEBOOK_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="SYNCHRONOUSCLASS_ID")
	private SynchronousClass synchronousClass ; 
	
	@Transient
	private String name = null;
	
	@Transient
	private Integer displayOrder = 0;  // Note : LMS:7970
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
	 * @return the synchronousClass
	 */
	public SynchronousClass getSynchronousClass() {
		return synchronousClass;
	}
	/**
	 * @param synchronousClass the synchronousClass to set
	 */
	public void setSynchronousClass(SynchronousClass synchronousClass) {
		this.synchronousClass = synchronousClass;
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
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public Integer compareTo(Gradebook arg0) {
		Integer i1 = new Integer (  displayOrder  ) ; 
        Integer i2 = new Integer (  arg0.getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}

}
