/**
 * 
 */
package com.softech.vu360.lms.web.controller;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.batchImport.BatchImportConnectionManager;
import com.softech.vu360.lms.batchImport.BatchImportMessageListener;
import com.softech.vu360.util.VU360Properties;

public class BatchImportAdminController implements Controller {
	
	private static final Logger log = Logger.getLogger(BatchImportAdminController.class.getName());
	private static final String AMQ_PROCESSING_SERVER_IP=(String) VU360Properties.getVU360Property("amq.porcessor.ip");
	private String batchImportAdminTemplate = "";
	private BatchImportConnectionManager batchImportConnectionManager;
	
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String amqAllowServerActions = "false";
		String action = request.getParameter("frmAction")!=null ? request.getParameter("frmAction") : "";
		
		
		if(action.equals("Start")){
			batchImportConnectionManager.startListening();
		}
		
		if(action.equals("Stop")){
			batchImportConnectionManager.stopListening();
		}
		
		String amqServerStatus = batchImportConnectionManager.findServerStatus();
		String listenerStatus = batchImportConnectionManager.findListenerStatus();
		
		
/*		try{
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(AMQ_BROKER_URL);
			factory.setTransportListener(new ActiveMQConnectionStateMonitor());
		    factory.createConnection(AMQ_USER, AMQ_PASSWORD);
		}
		catch(JMSException jmxe){
			amqServerStatus = AMQ_SERVER_UNAVAILABLE;
			con=null;
			session=null;
			consumer = null;
			queue = null;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if(!amqServerStatus.equals(AMQ_SERVER_UNAVAILABLE)){
		
			if(this.con==null){
				ConnectionFactory connectionFactory = (CachingConnectionFactory) this.cachedConnectionFactory;
				this.con = connectionFactory.createConnection();
				this.con.start();
				
			}
			
			if(this.session==null){
				this.session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
				this.session.run();
			}
			
			if(queue==null){
				queue = session.createQueue(AMQ_QUEUE_NAME);
			}
			
			if(action.equals("Start")){
				if(this.consumer==null){
					this.consumer = session.createConsumer(queue);
					MessageListener listener = this.batchImportMessageListener;
					this.consumer.setMessageListener(listener);
					System.out.println("##- Message receiving has now been started.");
				}
			}
			else if(action.equals("Stop")){
				if(this.consumer!=null){
					this.consumer.close();
					this.consumer = null;
				}
			}
		
		}
		
		String status = "";
		if(consumer==null){
			status = "Stop";
			if(amqServerStatus.equals(AMQ_SERVER_UNAVAILABLE)){
				status += " (ActiveMQ Server connection error)";
			}
		}
		else{
			status = "Running";
		}
		*/
		
		/*
		 * Applying condition for the calling host
		 */
		if(AMQ_PROCESSING_SERVER_IP.equals(InetAddress.getLocalHost().getHostAddress())){
			amqAllowServerActions = "true";
		}

		int pendingCounter = batchImportConnectionManager.findPendingQueueMessagesCount();
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("AMQServerStatus", amqServerStatus);
		context.put("ListenerStatus", listenerStatus);
		context.put("PendingMessages", pendingCounter-BatchImportMessageListener.messagesInQueue);
		context.put("MessageInProcessing",BatchImportMessageListener.messagesInQueue);
		context.put("AllowActions", amqAllowServerActions);
		
		return new ModelAndView(batchImportAdminTemplate, "context", context);
	}
	
	
/*	private int getPendingQueueMessagesCount(){
		Queue statQueue = null;
		Session statSession = null;
		QueueBrowser browserQueue = null;
		Connection statConnection= null;
		int pendingMsgCounter=0;
		
		try{
			ConnectionFactory connectionFactory = (CachingConnectionFactory) this.cachedConnectionFactory;
			statConnection= connectionFactory.createConnection();
			statConnection.start();
			
		
			statSession = statConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			statSession.run();
		
			statQueue = statSession.createQueue(AMQ_QUEUE_NAME);
			
			browserQueue = statSession.createBrowser(statQueue);
			Enumeration e = browserQueue.getEnumeration();
			
			while(e.hasMoreElements()){
				Message m = (Message) e.nextElement();
				pendingMsgCounter++;
			}
			log.debug("~~~ Total Pending Messages:: "+pendingMsgCounter);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				browserQueue.close();
				browserQueue=null;
				
				statQueue = null;
				
				statSession.close();
				statSession=null;
				
				statConnection.close();
				statConnection=null;
			}
			catch(Exception e){
				
			}
		}
		
		return pendingMsgCounter;
	}
*/
	public String getBatchImportAdminTemplate() {
		return batchImportAdminTemplate;
	}

	public void setBatchImportAdminTemplate(String batchImportAdminTemplate) {
		this.batchImportAdminTemplate = batchImportAdminTemplate;
	}


	public BatchImportConnectionManager getBatchImportConnectionManager() {
		return batchImportConnectionManager;
	}


	public void setBatchImportConnectionManager(
			BatchImportConnectionManager batchImportConnectionManager) {
		this.batchImportConnectionManager = batchImportConnectionManager;
	}


}
