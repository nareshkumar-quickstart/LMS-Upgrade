package com.softech.vu360.lms.webservice.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BatchImportData;
import com.softech.vu360.lms.service.BatchImportLearnersService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.webservice.LMSIntegrationWS;
import com.softech.vu360.lms.webservice.message.integration.BatchImportLearnerRequest;
import com.softech.vu360.lms.webservice.message.integration.BatchImportLearnerResponse;
import com.softech.vu360.lms.webservice.message.integration.TransactionResultType;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;



/**
 * LMSIntegrationWSImpl defines the set of interfaces 
 * to control the interactions and business logic
 * of the integration between Storefront & LMS.
 * 
 * @author Faisal A. Siddiqui
 */
@Endpoint
public class LMSIntegrationWSImpl implements LMSIntegrationWS {
	

	private static final Logger log = Logger.getLogger(LMSIntegrationWSImpl.class.getName());
	
	private static final String BATCHFILE_IMPORT_EVENT = "BatchImportLearnerRequest";
	
	private static final String MESSAGES_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/integrationInterface";
	private BatchImportLearnersService batchImportLearnersService = null;
	private CustomerService customerService = null;
	private VU360UserService userDetailsService = null;
	private VelocityEngine velocityEngine = null;
	
	
	@PayloadRoot(localPart = BATCHFILE_IMPORT_EVENT, namespace = MESSAGES_NAMESPACE)
	public BatchImportLearnerResponse importBatchFileEvent(BatchImportLearnerRequest batchImportLearnerRequest) {
		BatchImportLearnerResponse response = new BatchImportLearnerResponse();
		String fileName = batchImportLearnerRequest.getFileLocation();
		String batchFileLocation = VU360Properties.getVU360Property("lms.batchfile.tempLocation")+fileName;
		String loginURL =VU360Properties.getVU360Property("lms.loginURL");
		String customerGUID = batchImportLearnerRequest.getCustomerGUID();
		Customer customer = customerService.findCustomerByCustomerGUID(customerGUID);
		VU360User loggedInUser = userDetailsService.findUserByUserName(batchImportLearnerRequest.getUserName());
		String action = batchImportLearnerRequest.getActionOnDuplicateRecords().get(0).value();
		boolean changePasswordOnLogin = false;	// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
		
		try{
			String fileContent = readFileContent(batchFileLocation);
			/*
			 * the below called method need brand, which we usually take from request. Please update. The batchimportservice has been
			 * changed by Humayun in 4.5.4. We need to update Here.
			 * Tahir Mehmood - merging 4.6.0 to trunk. 06-20-2010
			 * I am setting the brander to the customer brand, and a velocity engine and custom fields... Please update it to correct value. this was just for the time being so that I can test
			 */
			com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
			lang.setLanguage(Language.DEFAULT_LANG);
			Brander brander = VU360Branding.getInstance().getBrander(customer.getBrandName(), lang);
                        
                        BatchImportData batchImportData = new BatchImportData(
                            customer, 
                            fileContent, ",", 
                            action, 
                            batchImportLearnerRequest.isVisibleOnReport(), 
                            batchImportLearnerRequest.isAccountLocked(), 
                            true, 
                            batchImportLearnerRequest.isNotifyLearnerOnRegistration(),
                            loginURL,loggedInUser,
                            velocityEngine, 
                            customer.getCustomFields(), 
                            brander, changePasswordOnLogin);
                        
			batchImportLearnersService.importUsersFromBatchFile(batchImportData);	// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
                        
			response.getTransactionResult().add(TransactionResultType.SUCCESS);
			response.setTransactionResultMessage("Import Successfully");
		}catch(IOException ioe){
			log.error("Error in Reading file["+fileName+"] =>"+ioe.getMessage());
			response.getTransactionResult().add(TransactionResultType.FAILURE);
			response.setTransactionResultMessage("Import Failed due to error in reading file["+ioe.getMessage()+"]");
		}catch(Exception e){
			log.error("error in importing file["+fileName+"] =>"+e.getMessage());
			response.getTransactionResult().add(TransactionResultType.FAILURE);
			response.setTransactionResultMessage("Import Failed file["+e.getMessage()+"]");
			log.error(e.getStackTrace());

		}
		//response.setEventDate(DateUtil.getCurrentDateTimeForXML());
		response.setEventDate(DateUtil.getXMLGregorianCalendarNow());
		response.setTransactionGUID(batchImportLearnerRequest.getTransactionGUID());
		return response;
	}
	private String readFileContent(String batchFileLocation) throws IOException{
		File file = new File(batchFileLocation);
    	byte[] buffer = new byte[(int)file.length()];
    	BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
    	f.read(buffer);
	    return new String(buffer);
	}

	public BatchImportLearnersService getBatchImportLearnersService() {
		return batchImportLearnersService;
	}


	public void setBatchImportLearnersService(
			BatchImportLearnersService batchImportLearnersService) {
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

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	
	
}
