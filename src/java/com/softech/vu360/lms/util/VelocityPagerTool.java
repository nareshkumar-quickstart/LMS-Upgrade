package com.softech.vu360.lms.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.tools.generic.ValueParser;
import org.apache.velocity.tools.view.tools.AbstractPagerTool;

public class VelocityPagerTool extends AbstractPagerTool {

	public static final int DEFAULT_PAGE_SIZE = 10;
	private boolean showAll = false;
	private Integer totalItemsCount = null;

	/**
	 * To properly understand the implementation of org.apache.velocity.tools.view.tools.AbstractPagerTool 
	 * @see org.apache.velocity.tools.view.tools.AbstractPagerTool
	 * 1. To enable displaying all items, we MUST send the request attribute/parameters
	 *    - showAll = true : both key-values should be strings
	 * 2. To see the next page or previous page, we need to send the 
	 *    - pageIndex parameter/attribute in the request with proper value.
	 *    - The value can be found by the call to the pager.nextIndex or pager.previousIndex
	 *    - from the vm.
	 * 3. Sometimes the full list is not available to the pager for optimisation purposes.
	 *    - In this case the items list in the pager will actually be a sublist of the total list
	 *    - that need to be paginated.
	 *    - In such cases for pager to work, we need to have the total count of the list
	 *    - apart from the sublist that is set in the items attribute.
	 *    - This value can be set from the request attribute map named PagerAttributeMap for the 
	 *    - totalCount key name and the value correspondingly as strings.
	 *    
	 */
	public void setup(HttpServletRequest request) {
		//accept the pageindex and showall only from request
		ValueParser parser = new ValueParser(request.getParameterMap());
		showAll = parser.getBoolean("showAll",false);
		setIndex(parser.getInt("pageIndex", 0));
		//accept the page size and the item total count from map in request attribute...for robustness 
		Map pagerAttributeMap = (request.getAttribute("PagerAttributeMap")!=null)?(Map)request.getAttribute("PagerAttributeMap"):new HashMap();
		parser = new ValueParser(pagerAttributeMap);
		setIndex(parser.getInt("pageIndex", getIndex()));//override if there is pageIndex in the attributes
		totalItemsCount = parser.getInteger("totalCount", null); 
		setItemsPerPage(parser.getInt("pageSize", DEFAULT_PAGE_SIZE));
		if(request.getAttribute("myPageSize")!=null)
			setItemsPerPage( ((Integer) request.getAttribute("myPageSize")).intValue() );
	}


	/**
	 * @see org.apache.velocity.tools.view.tools.AbstractPagerTool#getTotal()
	 */
	public int getTotal() {
		//implies that the items list given to the pager
		//is only a part of the actual list to be paged
		return (totalItemsCount!=null && totalItemsCount.intValue()>super.getTotal())? totalItemsCount:super.getTotal();
	}


	/**
	 * @see org.apache.velocity.tools.view.tools.AbstractPagerTool#getFirstIndex()
	 */
	public int getIndex() {
		if(showAll) return 0;
		return super.getIndex();
	}
	
	public Integer getFirstIndex() {
		if(showAll) return 0;
		return super.getFirstIndex();
	}

	/**
	 * @see org.apache.velocity.tools.view.tools.AbstractPagerTool#getLastIndex()
	 */
	public Integer getLastIndex() {
		if(showAll) return (getTotal()-1);
		return super.getLastIndex();
	}

	/**
	 * @see org.apache.velocity.tools.view.tools.AbstractPagerTool#getNextIndex()
	 */
	public Integer getNextIndex() {
		if(showAll) return null;
        return super.getNextIndex();
	}
	
	/**
	 * @see org.apache.velocity.tools.view.tools.AbstractPagerTool#getPrevIndex()
	 */
	public Integer getPrevIndex() {
		if(showAll) return null;
        return super.getPrevIndex();
	}

}
