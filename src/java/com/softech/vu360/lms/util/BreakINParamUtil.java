/**
 * 
 */
package com.softech.vu360.lms.util;

import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * @author marium.saud
 * This class is serve as utility to break In clause given params incase if the number of params are greater than 99.
 * Note : Avoiding SQL Exception which does not allow more than 2100 parameters in a SQL statement. (LMS-19012)
 *
 */
public class BreakINParamUtil {

	public static List<Long> breakIds(List<Long> ids, int listSize,
			int inParamLimit, int index) {

		List<Long> idsSubList = null;
		if (!CollectionUtils.isEmpty(ids)) {
			if (listSize > index + inParamLimit) {
				idsSubList = ids.subList(index,
						(index + inParamLimit));
			} else {
				idsSubList = ids.subList(index, listSize);
			}
		}
		return idsSubList;
	}

}
