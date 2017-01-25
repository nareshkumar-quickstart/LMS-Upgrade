package com.softech.vu360.lms.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Runtime;

import com.softech.vu360.lms.exception.TemplateServiceException;

@SuppressWarnings("deprecation")
public class TemplateServiceImpl {

	private static Logger log = Logger.getLogger(TemplateServiceImpl.class);

	private final static TemplateServiceImpl singleton = new TemplateServiceImpl();

	/** Creates a new instance of TemplateService */
	private TemplateServiceImpl() {
		init();
	}

	/**
	 * Method to initialize the Velocity runtime services.
	 */
	public synchronized static void init() {
		try {
			InputStream is = TemplateServiceImpl.class
					.getResourceAsStream("/velocity.properties");
			Properties props = new Properties();
			props.load(is);
			Velocity.init(props);
		} catch(ResourceNotFoundException rnfe) {
			log.error("Exception caught: Unable to load velocity.properties.",rnfe);
		} catch (Exception ex) {
			log.error("Exception caught while initializing Velocity runtime services.",ex);
		}
	}

	/** 
	 * Returns the singleton instance of this class.
	 *
	 * @return TemplateService 
	 */
	public static TemplateServiceImpl getInstance() {
		return singleton;
	}

	/**
	 * Method to render a Template
	 *
	 * @param String URL to the template to be rendered
	 * @param VelocityContext context to be used in the rendering operation
	 * @return String the rendered template
	 * @throws TemplateServiceException
	 */
	public String renderTemplate(String templateURL, VelocityContext context)
			throws TemplateServiceException {
		String result = null;

		if (templateURL != null && !templateURL.equals("")) {

			try {
				StringWriter stringWriter = new StringWriter();
				Template template = Runtime.getTemplate(templateURL);template.setEncoding("UTF-8");
				template.merge(context, stringWriter);
				stringWriter.close();
				result = stringWriter.toString();
			} catch (ResourceNotFoundException rnfex) {
				String msg = "ResourceNotFoundException encountered merging template URL \""
						+ templateURL
						+ "\" with provided context: "
						+ rnfex.getMessage();
				throw new TemplateServiceException(msg);
			} catch (IOException ioex) {
				String msg = "IOException encountered merging template URL \""
						+ templateURL + "\" with provided context: "
						+ ioex.getMessage();
				throw new TemplateServiceException(msg);
			} catch (ParseErrorException peex) {
				String msg = "ParseErrorException encountered merging template URL \""
						+ templateURL
						+ "\" with provided context: "
						+ peex.getMessage();
				throw new TemplateServiceException(msg);
			} catch (Exception ex) {
				String msg = "Exception encountered merging template URL \""
						+ templateURL + "\" with provided context: "
						+ ex.getMessage();
				throw new TemplateServiceException(msg);
			}

		}

		return result;
	}

	/**
	 * Method to render a Template
	 *
	 * @param String URL to the template to be rendered
	 * @param HashMap attributes to be placed in the VelocityContext for rendering
	 * @return String the rendered template
	 * @throws TemplateServiceException
	 */
	public String renderTemplate(String templateURL, HashMap<Object, Object> attributes)
			throws TemplateServiceException {
		String result = null;

		if (attributes != null) {
			VelocityContext context = new VelocityContext();
			Set<Object> keySet = attributes.keySet();
			for (Iterator<Object> iter = keySet.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = attributes.get(key);
				context.put(key, value);
			}
			result = renderTemplate(templateURL, context);
		}

		return result;
	}

	/**
	 * Method to render a Template
	 *
	 * @param String URL to the template to be rendered
	 * @param HashMap attributes to be placed in the VelocityContext for rendering
	 * @return String the rendered template
	 * @throws TemplateServiceException
	 */
	public void renderTemplateToWriter(String templateURL, HashMap<Object, Object> attributes,
			Writer writer) throws TemplateServiceException {
		if (attributes != null) {
			VelocityContext context = new VelocityContext();
			Set<Object> keySet = attributes.keySet();
			for (Iterator<Object> iter = keySet.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = attributes.get(key);
				context.put(key, value);
			}
			renderTemplateToWriter(templateURL, context, writer);
		}
	}

	public void renderTemplateToWriter(String templateURL,
			VelocityContext context, Writer writer)
			throws TemplateServiceException {
		if (StringUtils.isNotBlank(templateURL)) {
			try {
				Template template = Runtime.getTemplate(templateURL);template.setEncoding("UTF-8");
				template.merge(context, writer);
			} catch (ResourceNotFoundException rnfex) {
				String msg = "ResourceNotFoundException encountered merging template URL \""
						+ templateURL
						+ "\" with provided context: "
						+ rnfex.getMessage();
				throw new TemplateServiceException(msg);
			} catch (IOException ioex) {
				String msg = "IOException encountered merging template URL \""
						+ templateURL + "\" with provided context: "
						+ ioex.getMessage();
				throw new TemplateServiceException(msg);
			} catch (ParseErrorException peex) {
				String msg = "ParseErrorException encountered merging template URL \""
						+ templateURL
						+ "\" with provided context: "
						+ peex.getMessage();
				throw new TemplateServiceException(msg);
			} catch (Exception ex) {
				String msg = "Exception encountered merging template URL \""
						+ templateURL + "\" with provided context: "
						+ ex.getMessage();
				throw new TemplateServiceException(msg);
			}
		}
	}

	/**
	 * Method to render a Template
	 *
	 * @param String URL to the template to be rendered
	 * @param String key to use when place the value in the VelocityContext
	 * @param Object value to placed in the VelocityContext for rendering
	 * @return String the rendered template
	 * @throws TemplateServiceException
	 */
	public String renderTemplate(String templateURL, String key, Object value)
			throws TemplateServiceException {
		String result = null;

		if (StringUtils.isNotBlank(key)) {
			VelocityContext context = new VelocityContext();
			context.put(key, value);
			result = renderTemplate(templateURL, context);
		}

		return result;
	}

}
