package com.softech.vu360.lms.web.restful.request.customer.contract.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.web.restful.request.AbstractResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.AddCustomerContractRequest;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.LmsAddCustomerContractRequest;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.AddCustomerContractResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response.LmsAddCustomerContractResponse;
import com.softech.vu360.lms.web.restful.request.customer.contract.responsible.AddCustomerContractRequestResponsible;
import com.softech.vu360.lms.web.restful.request.handler.AbstractRequestHandler;
import com.softech.vu360.lms.web.restful.request.handler.RequestHandler;
import com.softech.vu360.lms.web.restful.request.responsible.AbstractRequestResponsible;
import com.softech.vu360.lms.web.restful.request.security.RestRequestResellerSecurityManager;
import com.softech.vu360.lms.web.restful.request.security.RestRequestSecurityManager;
import com.softech.vu360.lms.web.restful.request.validator.AbstractRequestValidator;
import com.softech.vu360.lms.web.restful.request.validator.RequestValidator;

@Controller
public class CustomerContractController {

	@Autowired 
	private ServletContext servletContext;
	private ApplicationContext applicationContext;
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		System.out.println("In CustomerContractController");
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if (applicationContext == null) {
			throw new BeanCreationException("CustomerContractController", "applicationContext is null");
		}
		System.out.println(applicationContext);
	}
	
	// http://localhost:8080/lms/restful/customer/contract/add
	
	@RequestMapping(value = "/customer/contract/add", method = RequestMethod.POST)
	public ModelAndView addCustomerContract(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		AbstractResponse apiResponse = null;
		//RequestDriver requestDriver = (CustomerContractRequestDriver)applicationContext.getBean("customerContractRequestDriver");
		AbstractRequestResponsible requestResponsible = (AddCustomerContractRequestResponsible)applicationContext.getBean("addCustomerContractRequestResponsible");
		AbstractRequestHandler requestHandler = (RequestHandler)applicationContext.getBean("restRequestHandler");
		AbstractRequestValidator requestValidator = (RequestValidator)applicationContext.getBean("restRequestValidator");
		RestRequestSecurityManager resellerSecurityManager = (RestRequestResellerSecurityManager)applicationContext.getBean("restRequestResellerSecurityManager");
		requestValidator.setRestRequestHandler(requestHandler);
		requestValidator.setRequestResponsible(requestResponsible);
		requestValidator.setRestRequestSecurityManager(resellerSecurityManager);
		//requestDriver.setRequestResponsible(requestResponsible);
		requestHandler.setRequestResponsible(requestResponsible);
		
		
		try {
			
			/**
			 * The Accept header is used by HTTP clients to tell the server what content types they'll accept. The server 
			 * will then send back a response, which will include a Content-Type header telling the client what the content
			 * type of the returned content actually is.
			 */
			String accept = request.getHeader("Accept");
			
			/**
			 * HTTP requests can also contain Content-Type headers. Why? Well, think about POST or PUT requests. With those
			 * request types, the client is actually sending a bunch of data to the server as part of the request, and the 
			 * Content-Type header tells the server what the data actually is (and thus determines how the server will parse
			 * it).
			 */
			String contentType = request.getHeader("Content-Type");
			
			ServletInputStream servletInputStream = request.getInputStream();
			LmsAddCustomerContractRequest lmsAddCustomerContractRequest = null;
			if (servletInputStream != null) {
				if (contentType.equalsIgnoreCase("application/json")) {
					String requestJsonString = IOUtils.toString(servletInputStream);
					JSONObject requestJsonObj = JSONObject.fromObject(requestJsonString);
					lmsAddCustomerContractRequest = (LmsAddCustomerContractRequest)JSONObject.toBean(requestJsonObj, LmsAddCustomerContractRequest.class);
				} else if (contentType.equalsIgnoreCase("application/xml")) {
					String requestXml = IOUtils.toString(servletInputStream);
				}
			}
			
			if (lmsAddCustomerContractRequest != null) {
				
				AddCustomerContractRequest addCustomerContractRequest = lmsAddCustomerContractRequest.getAddCustomerContractRequest();
				String apiKey = addCustomerContractRequest.getKey();
				Long resellerId = addCustomerContractRequest.getResellerId();
				apiResponse = requestValidator.validateRequest(lmsAddCustomerContractRequest, apiKey, resellerId);
				if (apiResponse == null) {
					apiResponse = requestHandler.handleRequest(lmsAddCustomerContractRequest);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			apiResponse = requestHandler.handleRequest(null, e);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		if (apiResponse != null) {
			LmsAddCustomerContractResponse lmsAddCustomerContractResponse = (LmsAddCustomerContractResponse)apiResponse;
			AddCustomerContractResponse addCustomerContractResponse = lmsAddCustomerContractResponse.getAddCustomerContractResponse();
			//model.put("success", "true");
			model.put("AddCustomerContractResponse", addCustomerContractResponse);
				
		} else {
			model.put("AddCustomerContractResponse", "No Valid request found");
		}
		
		/**
		 * When a view name "jsonView" is returned by controller, the XmlViewResolver will find the bean id jsonView in
		 * "spring-views.xml" file, and return the corresponds view’s URL “/WEB-INF/pages/WelcomPage.jsp” back to the 
		 * DispatcherServlet.
		 */
		return new ModelAndView("jsonView", model);
	}
	
}
