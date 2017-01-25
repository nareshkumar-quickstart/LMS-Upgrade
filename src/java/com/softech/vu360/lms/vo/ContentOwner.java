package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class ContentOwner implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name = null;
	private String guid = null;
	private Long planTypeId = null;
	private Long maxAuthorCount = null;
	private Long maxCourseCount = null;
	private Long currentAuthorCount = null;
	private Long currentCourseCount = null;

	private static final String CONTENTOWNER = "CONTENTOWNER";

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

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getOwnerType() {
		return CONTENTOWNER;
	}

	public Long getPlanTypeId() {
		return planTypeId;
	}

	public void setPlanTypeId(Long planTypeId) {
		this.planTypeId = planTypeId;
	}

	public Long getMaxAuthorCount() {
		return maxAuthorCount;
	}

	public void setMaxAuthorCount(Long maxAuthorCount) {
		this.maxAuthorCount = maxAuthorCount;
	}

	public Long getMaxCourseCount() {
		return maxCourseCount;
	}

	public void setMaxCourseCount(Long maxCourseCount) {
		this.maxCourseCount = maxCourseCount;
	}

	public Long getCurrentAuthorCount() {
		return currentAuthorCount;
	}

	public void setCurrentAuthorCount(Long currentAuthorCount) {
		this.currentAuthorCount = currentAuthorCount;
	}

	public Long getCurrentCourseCount() {
		return currentCourseCount;
	}

	public void setCurrentCourseCount(Long currentCourseCount) {
		this.currentCourseCount = currentCourseCount;
	}

}
