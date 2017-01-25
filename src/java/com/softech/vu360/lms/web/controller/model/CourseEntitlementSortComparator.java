/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author tathya
 *
 */
public class CourseEntitlementSortComparator  implements ILMSBaseInterface, Comparator<CourseEntitlementDetails>{
	private static final long serialVersionUID = 1L;
	public int compare(CourseEntitlementDetails arg0, CourseEntitlementDetails arg1) {
		if(arg0==null && arg1 == null) return 0;
		if(arg0==null && arg1 != null) return 1;
		if(arg0!=null && arg1 == null) return -1;
		/*Check course availibility*/
		
		if(arg0.getCourse()==null && arg1.getCourse() == null) return 0;
		if(arg0.getCourse()==null && arg1.getCourse() != null) return 1;
		if(arg0.getCourse()!=null && arg1.getCourse() == null) return -1;
		if(arg0.getCourse().getId().equals(arg1.getCourse().getId())){
			return arg0.getExpirationDate().compareTo(arg1.getExpirationDate());
		}else{
			return arg0.getCourse().getId().compareTo(arg1.getCourse().getId());
		}
	}
}