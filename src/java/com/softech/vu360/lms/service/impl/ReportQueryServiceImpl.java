package com.softech.vu360.lms.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.service.ReportQueryService;

/**
 * ReportQueryServiceImpl - generic implementation class
 * for the reporting framework that will query the database
 * given a velocity template.
 * 
 * @author jason
 *
 */
public class ReportQueryServiceImpl implements ReportQueryService {
	
	private static final Logger log = Logger.getLogger(ReportQueryServiceImpl.class);

	@Override
	public List<Map<Object, Object>> queryDatabase(String templateName,
			Map<Object, Object> context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
