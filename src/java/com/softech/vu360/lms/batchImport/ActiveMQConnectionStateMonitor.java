package com.softech.vu360.lms.batchImport;

import java.io.IOException;
import java.sql.Timestamp;

import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;

public class ActiveMQConnectionStateMonitor implements TransportListener {

	private static final Logger log = Logger.getLogger(ActiveMQConnectionStateMonitor.class);
	private BatchImportConnectionManager batchImportConnectionManager;
	private static int checkingAvailability=0;

	  @Override
	  public void onCommand(Object command) {
	    log.debug("Command detected: '" + command + "'");
	  }

	  @Override
	  public void onException(IOException exception) {
	    log.error("Exception detected: '" + exception + "'");
	    try{
	    	if(checkingAvailability==0){
	    		checkingAvailability=1;
	    		batchImportConnectionManager.stopListening();

			    while(true){
			    	System.out.println(new Timestamp(System.currentTimeMillis())+"~~ Checking Availability in Monitoring class");
			    	if(batchImportConnectionManager.findServerStatus().equals(BatchImportConnectionManager.AMQ_SERVER_AVAILABLE)){
			    		batchImportConnectionManager.startListening();
			    		break;
			    	}
			    	else{
			    		Thread.sleep(50000);
			    	}

			    }
			    checkingAvailability=0;
	    	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	  }

	  @Override
	  public void transportInterupted() {
	    log.error("Transport interuption detected.");
	  }

	  @Override
	  public void transportResumed() {
	    log.info("Transport resumption detected.");
	  }

		public BatchImportConnectionManager getBatchImportConnectionManager() {
			return batchImportConnectionManager;
		}
		
		public void setBatchImportConnectionManager(
				BatchImportConnectionManager batchImportConnectionManager) {
			this.batchImportConnectionManager = batchImportConnectionManager;
		}

}
