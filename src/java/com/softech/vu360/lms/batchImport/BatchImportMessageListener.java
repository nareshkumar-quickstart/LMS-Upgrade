package com.softech.vu360.lms.batchImport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.softech.vu360.lms.service.impl.OptimizedBatchImportLearnersProcessorThread;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BatchImportData;
import com.softech.vu360.lms.service.BatchImportLearnersService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.impl.OptimizedBatchImportLearnersSummary;
import com.softech.vu360.lms.vo.BatchImportParam;
import com.softech.vu360.lms.vo.BatchImportParamSerialized;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class BatchImportMessageListener implements MessageListener {
	
	private static final Logger log = Logger.getLogger(BatchImportMessageListener.class);
	private static final String BATCH_IMPORT_ERROR = "Batch Import Error. ";
	
	public static int messagesInQueue = 0;
	//private static final Object obj = 0;
	
	private VelocityEngine velocityEngine;
	private BatchImportLearnersService batchImportLearnersService;
	private CustomerService customerService;
	private VU360UserService userDetailsService;
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public BatchImportLearnersService getBatchImportLearnersService() {
		return batchImportLearnersService;
	}

	public void setBatchImportLearnersService(BatchImportLearnersService batchImportLearnersService) {
		this.batchImportLearnersService = batchImportLearnersService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public VU360UserService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(VU360UserService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage) message;
			BatchImportParamSerialized bp = (BatchImportParamSerialized) msg.getObject();
			
			messagesInQueue++;
			
			Customer customer = bp.getCustomer();
			VU360User loggedInUser = bp.getLoggedInUser();
			Brander brander = bp.getBrander();
			String csvFile = bp.getFile();
			String csvFileDelimiter = bp.getDelimiter();
			String loginUrl =  bp.getLoginURL();
			String customerGuid = null;
			boolean sendCustomEmail = bp.isSendCustomEmail();
			boolean readCsvFileContent = bp.isReadCsvFileContent();

			try {
				if (customer != null) {
					customerGuid = customer.getCustomerGUID();
				} else {
					customerGuid = bp.getCustomerGuid();
				}
				
				customer = findCustomerByCustomerGuid(customerGuid);
				
				if (loggedInUser == null) {
					String userName = bp.getUserName();
					loggedInUser = findUserByUserName(userName);
				}
				
				if (brander == null) {
					com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
					lang.setLanguage(Language.DEFAULT_LANG);
					brander = VU360Branding.getInstance().getBrander(customer.getBrandName(), new com.softech.vu360.lms.vo.Language());
				}
				
				if (StringUtils.isBlank(loginUrl)) {
					loginUrl = VU360Properties.getVU360Property("lms.loginURL");
				}
				
				String fileContent = null;
				if (readCsvFileContent) {
					String batchImportCsvFile = getBatchImportCsvFile(csvFile);
					fileContent = readFileContent(batchImportCsvFile);
				} else {
					fileContent = csvFile;
				}
				
				Map<Object, Object> context = doBatchImport(bp, customer, fileContent, csvFileDelimiter, loginUrl, loggedInUser, brander);
				if (sendCustomEmail) {
					
					String[] toEmailAddress = bp.getTo();
					if (ArrayUtils.isEmpty(toEmailAddress)) {
						String emailAddress = loggedInUser.getEmailAddress();
						toEmailAddress = new String[]{emailAddress};
					}
					
					sendCustomEmail(bp, context, brander, toEmailAddress);
				}
			} catch (Exception e) {
				log.error(e);
				if (sendCustomEmail) {
					sendErrorEmail(bp, brander, e);
				}
			}
		} catch (Exception  e) {
			log.error(e);
		} finally {
			messagesInQueue--;
		}
			
			/**
			 * 
			 
			//Customer customerBatchImport = customerService.findCustomerByCustomerGUID(bp.getCustomer().getCustomerGUID());
			
			BatchImportParam batchImportParam = new BatchImportParam(
					customerBatchImport, bp.getFile(), bp.getDelimiter(), bp.getActionOnDuplicateRecords(), bp.isAccVisibleOnReport(), bp.isAccLocked(), 
					bp.isFirstRowHeader(), bp.isNotifyLearnerOnRegistration(), bp.getLoginURL(), bp.getLoggedInUser(), this.velocityEngine, bp.getBrander(), 
					bp.isChangePasswordOnLogin());
			
			BatchImportData batchImportData = new BatchImportData(batchImportParam, bp.getCustomFields());
				
			//synchronized (obj) {
				log.debug("Starting synch block");
					try{
					
					batchImportLearnersService.importUsersFromBatchFile(batchImportData);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				log.debug("Finishing synch block");
			//}
			messagesInQueue--;
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	private Customer findCustomerByCustomerGuid(String customerGuid) throws Exception {
		
		Customer customer = null;
		
		if (StringUtils.isNotBlank(customerGuid)) {
			customer = customerService.findCustomerByCustomerGUID(customerGuid);
			if (customer == null) {
				throw new Exception(BATCH_IMPORT_ERROR + "No customer found against customer guid: " + customerGuid);
			}
		} else {
			throw new Exception(BATCH_IMPORT_ERROR + "CustomerGuid can not be empty or blank: " + customerGuid);
		}
		
		return customer;
	}
	
	private VU360User findUserByUserName(String userName) throws Exception {
		VU360User vu360User = null;
		if (StringUtils.isNotBlank(userName)) {
			vu360User = userDetailsService.findUserByUserName(userName);
			if (vu360User == null) {
				throw new Exception(BATCH_IMPORT_ERROR + "No User found against user name: " + userName);
			}
		} else {
			throw new Exception(BATCH_IMPORT_ERROR + "User name can not be empty or blank: " + userName);
		}
		
		return vu360User;
	}
	
	private String getBatchImportCsvFile(String csvFile) throws Exception {
		
		if (StringUtils.isBlank(csvFile)) {
			throw new Exception(BATCH_IMPORT_ERROR + "File name can not be empty or blank: " + csvFile);
		}
		
		String batchFileLocation = VU360Properties.getVU360Property("lms.batchfile.tempLocation");
		String batchImportCsvFile = batchFileLocation + csvFile;
		Path batchImportCsvFileDest = Paths.get(batchImportCsvFile);
		if (Files.notExists(batchImportCsvFileDest)) {
			throw new Exception(BATCH_IMPORT_ERROR + "No file found for destination: " + batchImportCsvFileDest);
		}
		
		return batchImportCsvFileDest.toString();
		
	}
	
	private String readFileContent(String batchFileLocation) throws IOException{
		
		File file = new File(batchFileLocation);
    	byte[] buffer = new byte[(int)file.length()];
    	
    	try (BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));) {
    		f.read(buffer);
    	}
    	
	    return new String(buffer);
	}
	
	private Map<Object, Object> doBatchImport(BatchImportParamSerialized bp, Customer customer, String fileContent, String csvFileDelimiter, String loginUrl, 
			VU360User loggedInUser, Brander brander) throws Exception {
		
		BatchImportParam batchImportParam = new BatchImportParam(
				customer, fileContent, csvFileDelimiter, bp.getActionOnDuplicateRecords(), bp.isAccVisibleOnReport(), bp.isAccLocked(), 
				bp.isFirstRowHeader(), bp.isNotifyLearnerOnRegistration(), loginUrl, loggedInUser, this.velocityEngine, brander, 
				bp.isChangePasswordOnLogin());
			BatchImportData batchImportData = new BatchImportData(batchImportParam, bp.getCustomFields());
			log.debug("Starting synch block");
			Map<Object, Object> context = batchImportLearnersService.importUsersFromBatchFile(batchImportData);
			log.debug("Finishing synch block");
			return context;
	}
	
	private OptimizedBatchImportLearnersSummary getBatchImportResultSummary(Map<Object, Object> context) {
		
		OptimizedBatchImportLearnersSummary batchImportResultSummary = null;
		if (!CollectionUtils.isEmpty(context)) {
			Object resultSummary = context.get("batchImportResultSummary");
			if (resultSummary != null) {
				if (resultSummary instanceof OptimizedBatchImportLearnersSummary) {
					batchImportResultSummary = (OptimizedBatchImportLearnersSummary)resultSummary;
				}	
			}
		}
		return batchImportResultSummary;
	}
	
	private void sendErrorEmail(BatchImportParamSerialized bp, Brander brander, Exception e) {
		
		String text = e.getMessage();
		String[] toEmailAddress = bp.getMonitoringEmailAddress();
		if (ArrayUtils.isEmpty(toEmailAddress)) {
			toEmailAddress = bp.getTo();
		}
		String from = bp.getFrom();
		if (StringUtils.isBlank(from)) {
			if (brander != null) {
				from = brander.getBrandElement("lms.email.batchUpload.managerMail.fromAddress");
			}
		}
		if (!ArrayUtils.isEmpty(toEmailAddress) && StringUtils.isNotBlank(from)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String toDate = formatter.format(Calendar.getInstance().getTime());
			String subject = BATCH_IMPORT_ERROR + toDate;
			log.info("Sending Batch Import error email ...");
			SendMailService.sendSMTPMessage(toEmailAddress, null, null, from, null, subject, text, null, null, null, null);
			log.info("Batch Import error email has been sent.");
		}
	}
	
	private void sendCustomEmail(BatchImportParamSerialized bp, Map<Object, Object> context, Brander brander, String[] toEmailAddress) throws Exception {
		
		OptimizedBatchImportLearnersSummary batchImportResultSummary = getBatchImportResultSummary(context);
		if (batchImportResultSummary != null) {
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("brander", brander);
			model.put("batchImportResultSummary", batchImportResultSummary);
			model.put("totalerrors", batchImportResultSummary.getBatchImportErrorsList().size());
			
			sendEmail(bp, model, toEmailAddress);
			
		}
	}
	
	private void sendEmail(BatchImportParamSerialized bp, Map<String, Object> model, String[] toEmailAddress) throws Exception {
		
		String CUSTOM_EMAIL_ERROR = "BatchImport custom email error. ";
		
		if (CollectionUtils.isEmpty(model)) {
			throw new Exception(CUSTOM_EMAIL_ERROR + "Model map can not be empty or blank. For sending email model map must contain " + Brander.class.getName());
		} else {
			Object brander = model.get("brander");
			if (brander == null || !(brander instanceof Brander)) {
				throw new Exception(CUSTOM_EMAIL_ERROR + "BatchImport custom email error. For sending email model map must contain " + Brander.class.getName() );
			}
		}
		
		Brander brander = (Brander)model.get("brander");
		
		String from = bp.getFrom();
		String[] cc = bp.getCc();
		String[] bcc = bp.getBcc();
		String subject = bp.getSubject();
		String text = bp.getText();
		
		if (StringUtils.isBlank(from)) {
			from = brander.getBrandElement("lms.email.batchUpload.managerMail.fromAddress");
		}
		
		if (StringUtils.isBlank(subject)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String toDate = formatter.format(Calendar.getInstance().getTime());
			subject = brander.getBrandElement("lms.emil.batchUpload.managerMail.subject")+ toDate;
		}
		
		if (StringUtils.isBlank(text)) {
			String batchImportTemplatePath = brander.getBrandElement("lms.email.batchUpload.managerMail.body");
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);
		}
		
		log.info("Sending Batch import summary custom email ...");
		SendMailService.sendSMTPMessage(toEmailAddress, cc, bcc, from, null, subject, text, null, null, null, null);
		log.info("Batch import summary custom email has been sent.");
	}
	
}