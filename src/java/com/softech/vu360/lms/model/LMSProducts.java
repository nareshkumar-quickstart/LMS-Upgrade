package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LMSPRODUCTS")
public class LMSProducts   implements SearchableKey{
	public static final long WEBINAR_AND_WEBLINK_COURSES = 1001;
	public static final long INSTRUCTORS_FOR_CLASSROOM_TRAINING = 1002;
	public static final long SCORM_COURSES = 1003;
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "LMSPRODUCTS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSPRODUCTS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSPRODUCTS_ID")
	private Long id;
	
	@Column(name = "PRODUCTNAME")
	private String productName = null;
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
