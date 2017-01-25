package com.softech.vu360.lms.meetingservice.webex;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class WebExProperties {

	private static Properties properties;
	
	// statically load the properties as it will not change from request to
	// request.	This is the same for all threads in this JVM
	static {
		try {
			properties = new Properties();
			InputStream resourceAsStream = WebExProperties.class.getResourceAsStream("webex.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
			}else {
				resourceAsStream = WebExProperties.class.getClassLoader().getResourceAsStream("webex.properties");
				properties.load(resourceAsStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static String getPartnerId() {
		return properties.getProperty("webex.partner.id");
	}
	public static String getSideId() {
		return properties.getProperty("webex.site.id");
	}
	public static String getId() {
		return properties.getProperty("webex.id");
	}
	public static String getPassword() {
		return properties.getProperty("webex.password");
	}
	public static String getEmail() {
		return properties.getProperty("webex.email");
	}
	public static String getServerURL(){
		return properties.getProperty("webex.server.url");
	}
	public static Properties getVU360Properties() {
		return properties;
	}

}
