/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author Haider.ali
 * @date: 22-09-2015
 */

@Entity
@Table(name = "COURSECONFIGURATIONTEMPLATE")
public class CourseConfigurationTemplate implements SearchableKey {

	private static final long serialVersionUID = 1L;
	/*	
	@Id
	@javax.persistence.TableGenerator(name = "COURSECONFIGURATIONTEMPLATE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COURSECONFIGURATIONTEMPLATE", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSECONFIGURATIONTEMPLATE_ID")
	 */	
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqCourseConfigurationTemplateId")
	@GenericGenerator(name = "seqCourseConfigurationTemplateId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "COURSECONFIGURATIONTEMPLATE") })
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name="name")
	private String name = null;
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner = null;
	
	@Column(name="LASTUPDATEDDATE")
	private Date lastUpdatedDate = null;
	
	@Column(name="active")
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ContentOwner getContentOwner() {
		return contentOwner;
	}
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public  Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "CourseConfigurationTemplate [id=" + id + ", name=" + name
				+ ", contentOwner=" + contentOwner + ", lastUpdatedDate="
				+ lastUpdatedDate + ", active=" + active + "]";
	}
	public Boolean getActive() {
		return active;
	}

}
