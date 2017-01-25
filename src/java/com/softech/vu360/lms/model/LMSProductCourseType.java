package com.softech.vu360.lms.model;

import java.util.List;

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
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LMSPRODUCT_COURSETYPES")
public class LMSProductCourseType    implements SearchableKey{

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "LMSPRODUCT_COURSETYPES_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSPRODUCT_COURSETYPES", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSPRODUCT_COURSETYPES_ID")
	private Long id;
	
	@Column(name = "COURSETYPE")
	private String courseType = null;

	@OneToOne
    @JoinColumn(name="PRODUCT_ID")
	private LMSProducts lmsProduct ;
    
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public LMSProducts getLmsProduct() {
		return lmsProduct;
	}


	public void setLmsProduct(LMSProducts lmsProduct) {
		this.lmsProduct = lmsProduct;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
}
