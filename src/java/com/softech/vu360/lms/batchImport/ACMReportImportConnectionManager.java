package com.softech.vu360.lms.batchImport;

import java.net.InetAddress;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.softech.vu360.util.VU360Properties;

public class ACMReportImportConnectionManager {

private static final Logger log = Logger.getLogger(ACMReportImportConnectionManager.class.getName());
	
	private RegulatoryReportingMessageListener regulatoryReportingMessageListener;
	private ActiveMQConnectionStateMonitor activeMQConnectionStateMonitor;
	private static final String AMQ_USER=(String) VU360Properties.getVU360Property("amq.connection.username");//"admin";
	private static final String AMQ_PASSWORD=(String) VU360Properties.getVU360Property("amq.connection.password");//"admin";
	private static final String AMQ_BROKER_URL=(String) VU360Properties.getVU360Property("amq.broker.url");//"tcp://localhost:61616";
	//private static final String AMQ_BROKER_URL="tcp://localhost:8161";
	//private static final String AMQ_QUEUE_NAME=(String) VU360Properties.getVU360Property("amq.queue.name");//"LMS.BatchImport";
	private static final String AMQ_QUEUE_NAME="LMS.ACMCardImport";//"LMS.BatchImport";
	private static final String AMQ_SERVER_IP=(String) VU360Properties.getVU360Property("amq.porcessor.ip");
	public static final String AMQ_SERVER_UNAVAILABLE="Server Un-available";
	public static final String AMQ_SERVER_AVAILABLE="Server Available";
	
	
	private static Connection con = null;
	private static Session session = null;
	private static javax.jms.MessageConsumer consumer = null;
	private static Queue queue = null;
	
public void startListening(){
		
		InetAddress ip;
        String hostname;
		
		try{
			ip = InetAddress.getLocalHost();
	        hostname = ip.getHostAddress();
			if(hostname.equals(AMQ_SERVER_IP) && ACMReportImportConnectionManager.consumer==null){
				ACMReportImportConnectionManager.consumer = getSession().createConsumer(getQueue());
				ACMReportImportConnectionManager.consumer.setMessageListener((MessageListener)getRegulatoryReportingMessageListener());
				log.info("##- Message receiving has now been started.");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

public void stopListening(){
	try{
		ACMReportImportConnectionManager.consumer = null;
		ACMReportImportConnectionManager.con = null;
		ACMReportImportConnectionManager.session = null;
		ACMReportImportConnectionManager.queue = null;
	}
	catch(Exception e){
		e.printStackTrace();
	}
}

private Connection getConnection(){
	if(ACMReportImportConnectionManager.con==null){
		try{
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AMQ_BROKER_URL);
			connectionFactory.setTransportListener(activeMQConnectionStateMonitor);
			ACMReportImportConnectionManager.con = connectionFactory.createQueueConnection();
			ACMReportImportConnectionManager.con.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	return ACMReportImportConnectionManager.con;
}

private Session getSession(){
	try{
		if(ACMReportImportConnectionManager.session==null){
			ACMReportImportConnectionManager.session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
			ACMReportImportConnectionManager.session.run();
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return ACMReportImportConnectionManager.session;

}

private Queue getQueue(){
	try{
		if(queue==null){
			queue = session.createQueue(AMQ_QUEUE_NAME);
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return this.queue;
}

public String findServerStatus(){
	String amqServerStatus=AMQ_SERVER_AVAILABLE;
	try{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(AMQ_BROKER_URL);
		factory.setTransportListener(getActiveMQConnectionStateMonitor());
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
		amqServerStatus = AMQ_SERVER_UNAVAILABLE;
		con=null;
		session=null;
		consumer = null;
		queue = null;
		e.printStackTrace();
	}
	
	return amqServerStatus;

}

public String findListenerStatus(){
	String amqServerStatus = findServerStatus();
	
	String status = "";
	if(consumer==null){
		status = "Stop";
		if(amqServerStatus.equals(AMQ_SERVER_UNAVAILABLE)){
			//status += " (ActiveMQ Server connection error)";
		}
	}
	else{
		status = "Running";
	}
	
	return status;

}

public int findPendingQueueMessagesCount(){
	Queue statQueue = null;
	Session statSession = null;
	QueueBrowser browserQueue = null;
	Connection statConnection= null;
	int pendingMsgCounter=0;
	
	try{
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AMQ_BROKER_URL);
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



public RegulatoryReportingMessageListener getRegulatoryReportingMessageListener() {
	return regulatoryReportingMessageListener;
}

public void setRegulatoryReportingMessageListener(
		RegulatoryReportingMessageListener regulatoryReportingMessageListener) {
	this.regulatoryReportingMessageListener = regulatoryReportingMessageListener;
}

public ActiveMQConnectionStateMonitor getActiveMQConnectionStateMonitor() {
	return activeMQConnectionStateMonitor;
}

public void setActiveMQConnectionStateMonitor(
		ActiveMQConnectionStateMonitor activeMQConnectionStateMonitor) {
	this.activeMQConnectionStateMonitor = activeMQConnectionStateMonitor;
}
	
}
