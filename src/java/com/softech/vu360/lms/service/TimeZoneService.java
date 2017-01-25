/**
 * 
 */
package com.softech.vu360.lms.service;

// Application Imports
// Java Imports
import java.util.List;

import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.util.Brander;

/**
 * @author sana.majeed
 * Date: Jan. 06, 2010
 */
public interface TimeZoneService {
	
	public List<TimeZone> getAllTimeZone ();
	public TimeZone getTimeZoneById (int id);
	public TimeZone getDefaultLernerTimeZone(Brander brand);
	public TimeZone getTimeZoneByName(String name);
}



