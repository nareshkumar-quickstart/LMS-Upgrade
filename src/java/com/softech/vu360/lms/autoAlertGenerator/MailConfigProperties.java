package com.softech.vu360.lms.autoAlertGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author jason
 *
 */
public class MailConfigProperties {

		
	private static Properties properties;
	
	// statically load the properties as it will not change from request to
	// request
	// this is the same for all threads in this JVM
	static {
		try {
			properties = new Properties();
			ClassLoader classLoader = MailConfigProperties.class
					.getClassLoader();
			InputStream resourceAsStream = classLoader.getResourceAsStream("com/softech/vu360/lms/autoAlertGenerator/mail-config.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMailProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static Properties getMailProperties() {
		return properties;
	}
}
