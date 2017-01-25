package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
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
@Table(name = "HOMEWORKASSIGNMENT_ASSET")
public class HomeWorkAssignmentAsset   implements SearchableKey{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "HOMEWORKASSIGNMENT_ASSET_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "HOMEWORKASSIGNMENT_ASSET", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "HOMEWORKASSIGNMENT_ASSET_ID")
	private Long id;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="COURSE_ID")
	private Course homeWorkAssignmentCourse;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="ASSET_ID")
	private Asset asset ;
	
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
	public Asset getAsset() {
		return this.asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public Course getHomeWorkAssignmentCourse() {
		return this.homeWorkAssignmentCourse;
	}
	public void setHomeWorkAssignmentCourse(Course homeWorkAssignmentCourse) {
		this.homeWorkAssignmentCourse = homeWorkAssignmentCourse;
	}

}
