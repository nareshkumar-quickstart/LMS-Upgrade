package com.softech.vu360.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author jason
 *
 */
public class VU360Properties {

		
	private static Properties properties;
	
	// statically load the properties as it will not change from request to
	// request
	// this is the same for all threads in this JVM
	static {
		try {
			properties = new Properties();
			ClassLoader classLoader = VU360Properties.class
					.getClassLoader();
			InputStream resourceAsStream = classLoader.getResourceAsStream("vu360-lms.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
			}
			
			/*
			File file = new File("D:/development/lmsWorkspace/VU360-LMS4.23.1-DocuSign2/src/config/dev/vu360-lms.properties");
			properties = new Properties();
	        FileInputStream fis = new FileInputStream(file);
	        properties.load(fis);    
	        fis.close();
	        */
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getVU360Property(String key) {
		return properties.getProperty(key);
	}
	
	public static Properties getVU360Properties() {
		return properties;
	}
}
