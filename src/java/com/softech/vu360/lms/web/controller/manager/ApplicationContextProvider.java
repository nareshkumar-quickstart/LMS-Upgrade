package com.softech.vu360.lms.web.controller.manager;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;

/**
 * This class provides an application-wide access to the
 * Spring ApplicationContext! The ApplicationContext is
 * injected in a static method of the class "AppContext".
 *
 * Use AppContext.getApplicationContext() to get access
 * to all Spring Beans.
 *
 * @author Haider.ali
 */
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext pApplicationContext) throws BeansException {
        StorefrontClientWSImpl.setApplicationContext(pApplicationContext);
        applicationContext = pApplicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

} 