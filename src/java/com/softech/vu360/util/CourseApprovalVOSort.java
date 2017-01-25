package com.softech.vu360.util;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.vo.CourseApprovalVO;

public class CourseApprovalVOSort  implements Comparator<CourseApprovalVO>{

	
	private String sortBy="holdingRegulator";
	private int sortDirection = 0;
	

	@Override
	public int compare(CourseApprovalVO o1, CourseApprovalVO o2) {
	if(sortBy.equalsIgnoreCase("holdingRegulator")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getHoldingRegulator()).toUpperCase().compareTo(o2.getHoldingRegulator().toUpperCase());
			return (StringUtils.EMPTY + o2.getHoldingRegulator()).toUpperCase().compareTo(o1.getHoldingRegulator().toUpperCase());
			
		}

		return 0;
	}	

	public String getSortBy() {
		return sortBy;
	}


	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}


	public int getSortDirection() {
		return sortDirection;
	}


	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

}
