package com.softech.vu360.lms.web.controller;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.batchImport.ACMReportImportConnectionManager;
import com.softech.vu360.lms.batchImport.BatchImportConnectionManager;
import com.softech.vu360.lms.batchImport.BatchImportMessageListener;
import com.softech.vu360.util.VU360Properties;

public class RegulatoryReportingAdminController implements Controller{

	private String regulatoryReportingAdminTemplate = "";
	private ACMReportImportConnectionManager acmReportImportConnectionManager;
	private static final String AMQ_PROCESSING_SERVER_IP=(String) VU360Properties.getVU360Property("amq.porcessor.ip");
	
	

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		
		String amqAllowServerActions = "false";
		String action = request.getParameter("frmAction")!=null ? request.getParameter("frmAction") : "";
		
		
		if(action.equals("Start")){
			acmReportImportConnectionManager.startListening();
		}
		
		if(action.equals("Stop")){
			acmReportImportConnectionManager.stopListening();
		}
		
		String amqServerStatus = acmReportImportConnectionManager.findServerStatus();
		String listenerStatus = acmReportImportConnectionManager.findListenerStatus();
		
		if(AMQ_PROCESSING_SERVER_IP.equals(InetAddress.getLocalHost().getHostAddress())){
			amqAllowServerActions = "true";
		}

		int pendingCounter = acmReportImportConnectionManager.findPendingQueueMessagesCount();
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("AMQServerStatus", amqServerStatus);
		context.put("ListenerStatus", listenerStatus);
		context.put("PendingMessages", pendingCounter-BatchImportMessageListener.messagesInQueue);
		context.put("MessageInProcessing",BatchImportMessageListener.messagesInQueue);
		context.put("AllowActions", amqAllowServerActions);
		
		return new ModelAndView(regulatoryReportingAdminTemplate, "context", context);
	}

	public String getRegulatoryReportingAdminTemplate() {
		return regulatoryReportingAdminTemplate;
	}

	public void setRegulatoryReportingAdminTemplate(String regulatoryReportingAdminTemplate) {
		this.regulatoryReportingAdminTemplate = regulatoryReportingAdminTemplate;
	}

	public ACMReportImportConnectionManager getAcmReportImportConnectionManager() {
		return acmReportImportConnectionManager;
	}

	public void setAcmReportImportConnectionManager(ACMReportImportConnectionManager acmReportImportConnectionManager) {
		this.acmReportImportConnectionManager = acmReportImportConnectionManager;
	}
}
