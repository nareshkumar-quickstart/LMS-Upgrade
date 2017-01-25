package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author raja.ali
 *
 */

@Entity
@Table(name = "COURSE_RESTRICTEDIP")
public class CourseRestrictedIP implements SearchableKey {

 	private static final long serialVersionUID = -3550333865404345234L;

        
   	@Id
   	@javax.persistence.TableGenerator(name = "COURSE_RESTRICTEDIP_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COURSE_RESTRICTEDIP", allocationSize = 1)
   	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSE_RESTRICTEDIP_ID")    
	private Long  id;
   	
   	@Column(name = "COURSE_NAME")
	private String  courseName;
   	
   	@Column(name = "COURSE_ID")
	private Long  courseId;
   	
   	@Column(name = "JURISDICTION_COUNTRY")
	private String  jurisdictionCountry;

   	@Column(name = "JURISDICTION_TYPE")
	private String  jurisdictionType;

   	@Column(name = "COURSE_BUISNESSKEY")
	private String  courseBusinessKey;

	@Override
	public String getKey() {
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getJurisdictionCountry() {
		return jurisdictionCountry;
	}

	public void setJurisdictionCountry(String jurisdictionCountry) {
		this.jurisdictionCountry = jurisdictionCountry;
	}

	public String getJurisdictionType() {
		return jurisdictionType;
	}

	public void setJurisdictionType(String jurisdictionType) {
		this.jurisdictionType = jurisdictionType;
	}

	public String getCourseBusinessKey() {
		return courseBusinessKey;
	}

	public void setCourseBusinessKey(String courseBusinessKey) {
		this.courseBusinessKey = courseBusinessKey;
	}

   	
   	
   	
}
