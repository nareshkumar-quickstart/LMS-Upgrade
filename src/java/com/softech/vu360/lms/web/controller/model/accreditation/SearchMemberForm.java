package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SearchMemberForm  implements ILMSBaseInterface{

	String allSearch = "false";
	String searchType = "";
	List<SearchMemberItem> items = new ArrayList<SearchMemberItem>();
	private static final long serialVersionUID = 1L;
	public String getAllSearch() {
		return allSearch;
	}
	public void setAllSearch(String allSearch) {
		this.allSearch = allSearch;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public List<SearchMemberItem> getItems() {
		return items;
	}
	public void setItems(List<SearchMemberItem> items) {
		this.items = items;
	}
}