package com.softech.vu360.lms.web.controller.helper;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class SortPagingAndSearch {

    private static final Logger log = Logger.getLogger(SortPagingAndSearch.class.getName());
    
    /*
     * Properties related to sorting.
     */

    public static final String SORT_ORDER_DESC = "desc";
    public static final String SORT_ORDER_ASC = "asc";

    private String sortDirection = null;
    private String sortColumnIndex = null;
    private String sortColumnName = null;

    /*
     * properties related to paging
     */

    private int pageSize;
    private int pageIndex;
    private long totalRecordCount;
    private String showAll;

    /*
     * properties related to data grid searching
     */

    private boolean doSearchData;
    private HashMap<String, String> searchValues;

    /*
	 * 
	 */
    public SortPagingAndSearch() {
	this.sortDirection = SORT_ORDER_DESC;
	this.sortColumnIndex = "0"; // default is first column
	this.sortColumnName = "";
	this.pageSize = 10; // should ideally be reset by controller or any
			    // other class which instantiate this class
	this.doSearchData = false;
	this.showAll = "false";
	this.searchValues = new HashMap<String, String>();
	this.pageIndex = 1;
    }

    public String getShowAll() {
	return showAll;
    }

    public void setShowAll(String showAll) {
	this.showAll = showAll;
	if("true".equalsIgnoreCase(this.showAll)) this.pageSize = -1;
    }

    public void setValue(String key, String val) {
	this.searchValues.put(key, val);
    }

    public String getValue(String key) {
	return this.searchValues.get(key) == null ? "" : this.searchValues.get(key);
    }

    public String getSortDirection() {
	return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
	this.sortDirection = sortDirection;
    }

    public String getSortColumnIndex() {
	return sortColumnIndex;
    }

    public void setSortColumnIndex(String sortColumnIndex) {
	this.sortColumnIndex = sortColumnIndex;
    }

    public String getSortColumnName() {
	return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
	this.sortColumnName = sortColumnName;
    }

    public int getPageSize() {
	return pageSize;
    }

    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }

    public int getPageIndex() {
	return this.pageIndex;
    }

    public int getMaxRows() {
	return this.pageIndex*this.pageSize;
    }

    public void setPageIndex(int pageIndex) {
	this.pageIndex = pageIndex;
    }

    public long getTotalRecordCount() {
	return totalRecordCount;
    }

    public void setTotalRecordCount(long totalRecordCount) {
	this.totalRecordCount = totalRecordCount;
    }

    public boolean isDoSearchData() {
	return doSearchData;
    }

    public void setDoSearchData(boolean doSearchData) {
	this.doSearchData = doSearchData;
    }

    public int getFirstResult() {
	return (this.pageIndex - 1) * this.pageSize;
    }

    public boolean getEnableShowAll() {
	
	return ( this.totalRecordCount > this.pageSize && !"true".equalsIgnoreCase(this.showAll) );
    }

    public boolean showNextIcon() {
	return this.pageSize * this.pageIndex < this.totalRecordCount && !"true".equalsIgnoreCase(this.showAll);
    }

    public boolean showPreviousIcon() {
	return this.pageIndex > 1;
    }

    public int getNextPageIndex() {
	return (this.pageIndex+1);
    }

    public int getPreviousPageIndex() {
	return (this.pageIndex-1);
    }

    public int getTotalPages() {
	return (int) Math.ceil(this.totalRecordCount / this.pageSize) <= 0 ? (int) this.totalRecordCount : (int) Math.ceil(this.totalRecordCount
		/ this.pageSize);
    }

    public int getTotalRecordsSeen() {
	
	return (int)this.pageIndex*this.pageSize <= this.totalRecordCount ? (int)this.pageIndex*this.pageSize : (int)this.totalRecordCount;   
    }
    
    /**
     * returns the sort icon based on the column index passed
     * 
     * @param columnIndex
     * @return
     */
    public String getSortDirectionIcon(String columnIndex) {

	if (null != this.sortColumnIndex && this.sortColumnIndex.equals(columnIndex)) {
	    return getSortIcon();
	}

	return "";
    }

    /**
     * Returns css class to show sort icon on the data grid
     * 
     * @return
     */
    public String getSortIcon() {
	return SORT_ORDER_ASC.equalsIgnoreCase(this.sortDirection) ? "icon_up" : "icon_down";
    }

    public String toString() {
	return "Page Index:" + this.pageIndex + "\n\r Page size :" + this.pageSize + "\n\r total record count :" + this.totalRecordCount
		+ "\n\r  sort direction :" + this.sortDirection + "\n\r  sort column index :" + this.sortColumnIndex + "\n\r  sort column name :"
		+ this.sortColumnName + "\n\r  show all :" + this.showAll + "\n\r  Do search ? :" + this.doSearchData + "\n\r  Get sort icon :"
		+ getSortDirectionIcon(this.sortColumnIndex) + "\n\r  Get first result :" + getFirstResult() + "\n\r  Get Next :" + showNextIcon()
		+ "\n\r  Get Previous :" + showPreviousIcon();
    }

    public static void main(String[] arg) {
	System.out.println((int) Math.ceil(12.1));
	System.out.println((int) Math.ceil(12.5));
	System.out.println((int) Math.ceil(12.6));
    }

}
