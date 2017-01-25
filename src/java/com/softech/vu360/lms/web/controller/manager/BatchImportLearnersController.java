package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BatchImportLearnersService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.vo.BatchImportParam;
import com.softech.vu360.lms.vo.BatchImportParamSerialized;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.SimpleFormController;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.BatchImportAsyncTask;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * Refactored Controller for Batch Import of Learners. All logic moved to Service class.
 * 
 * @author muzammil.shaikh
 */

public class BatchImportLearnersController extends SimpleFormController {

	public BatchImportLearnersController() {
		super();
		setCommandClass(FileUploadBean.class);
		setCommandName("bean");
		setFormView("manager/userGroups/Manager-Batch-Import-Users");
		setSuccessView("manager/userGroups/Manager-Batch-Import-Users-Result-New");
	}

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private String updateFileTemplate = null;
	private CustomFieldService customFieldService = null;
	private BatchImportLearnersService batchImportLearnersService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;

	public static final int FIX_FIELD_SIZE = 17;  // plus zero index
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	public static final String spliter = ">";
	private JmsTemplate jmsTemplate;
	private Destination destination;
	

	public BatchImportLearnersService getBatchImportLearnersService() {
		return batchImportLearnersService;
	}

	public void setBatchImportLearnersService(
			BatchImportLearnersService batchImportLearnersService) {
		this.batchImportLearnersService = batchImportLearnersService;
	}

	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}

	public Object formBackingObject(HttpServletRequest request) throws ServletException
	{
		if(this.getAllCustomFields().size()>0)
			request.getSession().setAttribute("fieldheader", "1");
		else
			request.getSession().setAttribute("fieldheader", "0");	
		String file = request.getParameter("file");
		log.debug("getting file object in 'formBackingObject' method "+file);
		FileUploadBean bean = new FileUploadBean();
		// this is useless here
		bean.setFile(file);
		return bean;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	throws ServletException {
		// to actually be able to convert Multipart instance to a String
		// we have to register a custom editor
		binder.registerCustomEditor(String.class, new StringMultipartFileEditor());
		// now Spring knows how to handle multipart object and convert them
	}

	@SuppressWarnings("unchecked")
	protected ModelAndView onSubmit (
			final HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors
	) throws Exception
	{
		try{
			final FileUploadBean bean = (FileUploadBean) command;

			final StringBuilder loginURL=new StringBuilder();
			loginURL.append(request.getScheme());
			loginURL.append("://");
			loginURL.append(request.getServerName());
			if(request.getServerPort()!=80)
			{
				loginURL.append(":");
				loginURL.append(request.getServerPort());
			}
			loginURL.append(request.getContextPath());
			
			// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
			final boolean changePasswordOnLogin = !( StringUtils.isBlank(request.getParameter("changePasswordOnLogin")) || request.getParameter("changePasswordOnLogin").equalsIgnoreCase("no") );

			final boolean accLocked = !(StringUtils.isBlank(request.getParameter("accLocked")) ||
					request.getParameter("accLocked").equalsIgnoreCase("no"));
			final boolean  accVisible = !(StringUtils.isBlank(request.getParameter("accVisible")) ||
					request.getParameter("accVisible").equalsIgnoreCase("no"));
			final boolean isFirstRowHeader = StringUtils.isBlank(request.getParameter("header")) ||
			request.getParameter("header").equalsIgnoreCase("yes");
			final boolean notifyLearnerOnRegistration = StringUtils.isBlank(request.getParameter("registration")) ||
			request.getParameter("registration").equalsIgnoreCase("yes");
			final VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			final Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			final List<CustomField> customFields =  getAllCustomFields();

			String isUsingActiveMQ = (String) VU360Properties.getVU360Property("batchimport.activemq");
			
			if(isUsingActiveMQ!=null &&  isUsingActiveMQ.equals("true")){
				//ActiveMQConnectionf
				jmsTemplate.send(new MessageCreator() {
					
					@Override
					public Message createMessage(Session session) throws JMSException {
						Message messageToSend = session.createObjectMessage(new BatchImportParamSerialized(getCurrentCustomer(),  bean.getFile(),  request.getParameter("delimiter"), request.getParameter("dupRecords"), accVisible, accLocked, isFirstRowHeader, notifyLearnerOnRegistration, loginURL.toString(), loggedInUser, brander, changePasswordOnLogin, customFields, false));
						return messageToSend;
					}
				});
			}
			else{
				//CREATE new BatchImportParams and then pass to the BatchImportAsyncTask
				BatchImportParam batchImportParams = new BatchImportParam(getCurrentCustomer(),  bean.getFile(),  request.getParameter("delimiter"), request.getParameter("dupRecords"), accVisible, accLocked, isFirstRowHeader, notifyLearnerOnRegistration, loginURL.toString(), loggedInUser, velocityEngine, brander, changePasswordOnLogin);  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
				BatchImportAsyncTask task = new BatchImportAsyncTask();
				task.setBatchImportParam(batchImportParams);
				task.setBatchImportService(batchImportLearnersService);
				asyncTaskExecutorWrapper.execute(task);//task will be executed asynchronously
			}
			
			ModelAndView modelAndView = super.onSubmit(request, response, command, errors);
			//modelAndView = modelAndView.addObject("context", context);
			return modelAndView;
		}
		catch(UncategorizedJmsException ucjmse){
			errors.rejectValue("file", "ActiveMQ file uploading server connection error.", "2");
			return super.processFormSubmission(request, response, command, errors);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return super.onSubmit(request, response, command, errors);
		}
	}

	/**
	 * used for validation
	 */
	protected ModelAndView processFormSubmission(
			final HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			final BindException errors
	) throws Exception
	{
		String firstRowHeaderStr = request.getParameter("header");
		final FileUploadBean bean = (FileUploadBean) command;
		final boolean firstRowHeader = StringUtils.isBlank(firstRowHeaderStr) || firstRowHeaderStr.equalsIgnoreCase("yes");
		if(firstRowHeader)
			bean.setFileHeader(this.getAllCustomFields());

		//Added by Faisal A. Siddiqui July 21 09 for new branding
		final Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());


		String delimiter = request.getParameter("delimiter");
		Map<String, String[]> errorsMap = null;
		try {
			errorsMap = batchImportLearnersService.validateCSVData(bean.getFile(),firstRowHeader,delimiter,brander,getAllCustomFields());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (errorsMap != null && errorsMap.keySet() != null && !errorsMap.keySet().isEmpty())
		{
			String[] errorStrings;
			for (String errorKey : errorsMap.keySet())
			{
				errorStrings = errorsMap.get(errorKey);
				errors.rejectValue(errorKey, errorStrings[0], errorStrings[1]);
			}
		}	



		return super.processFormSubmission(request, response, command, errors);
	}

	public String getUpdateFileTemplate() {
		return updateFileTemplate;
	}

	public void setUpdateFileTemplate(String updateFileTemplate) {
		this.updateFileTemplate = updateFileTemplate;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	private Customer getCurrentCustomer()
	{
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer(); 
		log.debug("customer name :"+customer.getName());
		return customer; 	
	}
	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

	private List<com.softech.vu360.lms.model.CustomField> getAllCustomFields()
	{
		Customer customer = getCurrentCustomer();
		List<com.softech.vu360.lms.model.CustomField> customerFields = customer.getCustomFields();
		List<com.softech.vu360.lms.model.CustomField> distributorFields = customer.getDistributor().getCustomFields();
		List<com.softech.vu360.lms.model.CustomField> allCustomFields = new ArrayList<com.softech.vu360.lms.model.CustomField>();
		allCustomFields.addAll(customerFields);
		allCustomFields.addAll(distributorFields);
		return allCustomFields;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}