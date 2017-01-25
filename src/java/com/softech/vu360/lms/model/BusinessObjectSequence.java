/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author sana.majeed
 * @modified by: raja.ali on [11/17/2015]
 * The Business Object Sequence class to hold the sequence information for a particular BusinessObject
 * Implemented for: [10/27/2010] LMS-6389 :: Business Object Sequencing for OSHA Certificates
 */

@Entity
@NamedQuery(name="BusinessObjectSequence.findBusinessObjectSequenceByName",query="SELECT bos FROM BusinessObjectSequence bos WHERE UPPER(bos.name) = UPPER(:name)")
@Table(name = "BUSINESSOBJECT_SEQ")
public class BusinessObjectSequence implements Serializable{
	
	private static final long serialVersionUID = -8480401312709818407L;
	
	@Id
	@javax.persistence.TableGenerator(name = "BusinessObjectSequence_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "BusinessObjectSequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "BusinessObjectSequence_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "BUSINESSOBJECT")
	private String name;
	
	@Column(name = "BUSINESSOBJECTTABLE")
	private String table;
	
	@Column(name = "BUSINESSOBJECTTABLECOLUMN")
	private String column;
	
	@Column(name = "BUSINESSOBJECTSEQROOT")
	private String sequenceRoot;
	
	@Column(name = "NEXT_SEQ")
	private Integer nextSequence;
	
	@Column(name = "NUMBERFORMAT")
	private String numberFormat;
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}
	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}
	/**
	 * @param sequenceRoot the sequenceRoot to set
	 */
	public void setSequenceRoot(String sequenceRoot) {
		this.sequenceRoot = sequenceRoot;
	}
	/**
	 * @return the sequenceRoot
	 */
	public String getSequenceRoot() {
		return sequenceRoot;
	}
	/**
	 * @param nextSequence the nextSequence to set
	 */
	public void setNextSequence(Integer nextSequence) {
		this.nextSequence = nextSequence;
	}
	/**
	 * @return the nextSequence
	 */
	public Integer getNextSequence() {
		return nextSequence;
	}
	/**
	 * @param numberFormat the numberFormat to set
	 */
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
	/**
	 * @return the numberFormat
	 */
	public String getNumberFormat() {
		return numberFormat;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
}
