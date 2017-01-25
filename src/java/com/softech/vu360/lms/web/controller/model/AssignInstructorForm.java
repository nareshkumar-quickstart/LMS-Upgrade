package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


/**
 * @author Noman
 *
 */

public class AssignInstructorForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AssignInstructorForm.class.getName());
	
	private Vector<Long> instructorIds = new Vector<Long>();
	private List<Instructor> selectedInstructors = null;
	
	private List<Instructor> instructors = new ArrayList<Instructor>();
//	private List<Instructor> instructors = null;
	private String[] selectedInstructorIds = null;
	
	private String instType = null;
	
	/* this is synchronousClass id, we need this id because
	 * we are assigning instructor to this class only*/
	private Long id = null;
	
	// For sorting and pagination...
	private String action = "";
	private String pageIndex = "0";
	private String showAll = "false";
	private String pageCurrIndex;	
	private String sortColumnIndex = "0";
	private String sortDirection = "0";
	
	public Vector<Long> getInstructorIds() {
		return instructorIds;
	}
	public void setInstructorIds(Vector<Long> instructorIds) {
		this.instructorIds = instructorIds;
	}
	public String getInstType() {
		return instType;
	}
	public void setInstType(String instType) {
		this.instType = instType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Instructor> getInstructors() {
		return instructors;
	}
	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}
	public String[] getSelectedInstructorIds() {
		return selectedInstructorIds;
	}
	public void setSelectedInstructorIds(String[] selectedInstructorIds) {
		this.selectedInstructorIds = selectedInstructorIds;
	}

	public List<Instructor> getSelectedInstructors() {
		return selectedInstructors;
	}
	public void setSelectedInstructors(List<Instructor> selectedInstructors) {
		this.selectedInstructors = selectedInstructors;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	
}