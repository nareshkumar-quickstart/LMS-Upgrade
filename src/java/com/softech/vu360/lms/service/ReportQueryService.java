package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

/**
 * Simple interface for querying the database using velocity
 * templates for dynamically generating SQL and returns a 
 * list of maps representing the columns and values for each
 * row returned.
 * 
 * @author jason
 **10/31/2008
 */
public interface ReportQueryService {
	
	public List<Map<Object, Object>> queryDatabase(String templateName, Map<Object, Object> context);

}
