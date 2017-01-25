/**
 * 
 */
package com.softech.vu360.lms.service.impl;

// Java Imports
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.repositories.TimeZoneRepository;
import com.softech.vu360.lms.service.TimeZoneService;
import com.softech.vu360.util.Brander;
//Application Imports




/**
 * @author sana.majeed
 *
 */

public class TimeZoneServiceImpl implements TimeZoneService {
	
	@Inject
	private TimeZoneRepository timeZoneRepository;
	
	private static final Logger log = Logger.getLogger(TimeZoneServiceImpl.class.getName());

	@Override
	public List<TimeZone> getAllTimeZone() {
		return (List<TimeZone>) timeZoneRepository.findAll();
	}

	@Override
	public TimeZone getTimeZoneById(int id) {
		return timeZoneRepository.findOne(id);
	}

	public TimeZone getDefaultLernerTimeZone(Brander brand){
		String defaultTimeZoneId=brand.getBrandElement("lms.learner.default.timezone");
		Integer timeZoneId=Integer.valueOf(defaultTimeZoneId);
		TimeZone defaultTimeZone=getTimeZoneById(timeZoneId);
		return defaultTimeZone;
	}
	
	public TimeZone getTimeZoneByName(String ZoneName){
		return timeZoneRepository.findByZone(ZoneName);
	}
	

}
