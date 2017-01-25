/**
 * 
 */
package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


/**
 * @author sana.majeed
 *
 */
public class CourseInstructorForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long courseId = null;
	private String courseType = null;
	
	//search criteria
	private String action = "showNone";
	
	private String searchFirstName = null;
	private String searchLastName = null;
	private String instructorType = null;
	private String assignmentType = "all"; 
	
	//sorting and paging items	
	private String sortColumn = "firstName";
	private int sortDirection = 0;
	private int searchResultsPageSize = 0;
	private int pageIndex = 0;
	
	private int totalRecords = -1;
	private int recordShowing = 0;
	
	private List<InstructorItemForm> instructors = new ArrayList<InstructorItemForm>();
	private List<InstructorItemForm> selectedInstructors = new ArrayList<InstructorItemForm>();

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * @param searchFirstName the searchFirstName to set
	 */
	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}

	/**
	 * @return the searchFirstName
	 */
	public String getSearchFirstName() {
		return searchFirstName;
	}

	/**
	 * @param searchLastName the searchLastName to set
	 */
	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}

	/**
	 * @return the searchLastName
	 */
	public String getSearchLastName() {
		return searchLastName;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param searchResultsPageSize the searchResultsPageSize to set
	 */
	public void setSearchResultsPageSize(int searchResultsPageSize) {
		this.searchResultsPageSize = searchResultsPageSize;
	}

	/**
	 * @return the searchResultsPageSize
	 */
	public int getSearchResultsPageSize() {
		return searchResultsPageSize;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	
	/**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * @return the totalRecords
	 */
	public int getTotalRecords() {
		return totalRecords;
	}

	/**
	 * @param recordShowing the recordShowing to set
	 */
	public void setRecordShowing(int recordShowing) {
		this.recordShowing = recordShowing;
	}

	/**
	 * @return the recordShowing
	 */
	public int getRecordShowing() {
		return recordShowing;
	}

	/**
	 * @param sortColumn the sortColumn to set
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	/**
	 * @return the sortColumn
	 */
	public String getSortColumn() {
		return sortColumn;
	}

	/**
	 * @param instructors the instructors to set
	 */
	public void setInstructors(List<InstructorItemForm> instructors) {
		this.instructors = instructors;
	}

	/**
	 * @return the instructors
	 */
	public List<InstructorItemForm> getInstructors() {
		return instructors;
	}

	/**
	 * @param selectedInstructors the selectedInstructors to set
	 */
	public void setSelectedInstructors(List<InstructorItemForm> selectedInstructors) {
		this.selectedInstructors = selectedInstructors;
	}

	/**
	 * @return the selectedInstructors
	 */
	public List<InstructorItemForm> getSelectedInstructors() {
		return selectedInstructors;
	}

	/**
	 * @param instructorType the instructorType to set
	 */
	public void setInstructorType(String instructorType) {
		this.instructorType = instructorType;
	}

	/**
	 * @return the instructorType
	 */
	public String getInstructorType() {
		return instructorType;
	}


	/**
	 * @param assignmentType the assignmentType to set
	 */
	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	/**
	 * @return the assignmentType
	 */
	public String getAssignmentType() {
		return assignmentType;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	
	
}
