package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * created on 2nd Apr 2010
 *
 */
public class ScheduleResourceForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private SynchronousSession schedule = null;
	private SynchronousClass synClass = null;
	private List<ManageResource> resources = new ArrayList<ManageResource>();
	// For sorting and pagination...
	private String action = "";
	private String pageIndex = "0";
	private String showAll = "false";
	private String pageCurrIndex;	
	private String sortColumnIndex = "";
	private String sortDirection = "";

	public List<ManageResource> getResources() {
		return resources;
	}

	public void setResources(List<ManageResource> resources) {
		this.resources = resources;
	}

	public SynchronousSession getSchedule() {
		return schedule;
	}

	public void setSchedule(SynchronousSession schedule) {
		this.schedule = schedule;
	}

	public SynchronousClass getSynClass() {
		return synClass;
	}

	public void setSynClass(SynchronousClass synClass) {
		this.synClass = synClass;
	}

	public String getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getShowAll() {
		return showAll;
	}

	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}

	public String getPageCurrIndex() {
		return pageCurrIndex;
	}

	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}

	public String getSortColumnIndex() {
		return sortColumnIndex;
	}

	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}