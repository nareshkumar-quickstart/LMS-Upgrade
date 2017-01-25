package com.softech.vu360.lms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.softech.vu360.util.VU360Properties;


public abstract class IFtpClient {

	//private Properties props;
	private String serverAddress = null;
	private String userId = null;
	private String password = null;
	private String remoteDirectory = null;
	private String localDirectory = null;
	private FTPClient ftp = null;
	private static final Logger log = Logger.getLogger(IFtpClient.class.getName());		


	public void initi(Properties properties) throws FileNotFoundException, IOException{
		
    	if (properties==null){
  	      setServerAddress(StringUtils.trim(VU360Properties.getVU360Property("lms.ftpserver.address")));
  	      setUserId(StringUtils.trim(VU360Properties.getVU360Property("lms.ftpserver.userId").trim()));
  	      setPassword(StringUtils.trim(VU360Properties.getVU360Property("lms.ftpserver.password")));
  	      setRemoteDirectory(StringUtils.trim(VU360Properties.getVU360Property("lms.ftpserver.remoteDirectory")));
  	      setLocalDirectory(StringUtils.trim(VU360Properties.getVU360Property("lms.ftpserver.localDirectory")));
      	}else{
	      setServerAddress(StringUtils.trim(properties.getProperty("lms.ftpserver.address")));
  	      setUserId(StringUtils.trim(properties.getProperty("lms.ftpserver.userNameId")));
  	      setPassword(StringUtils.trim(properties.getProperty("lms.ftpserver.password")));
  	      setRemoteDirectory(StringUtils.trim(properties.getProperty("lms.ftpserver.remoteDirectory")));
  	      setLocalDirectory(StringUtils.trim(properties.getProperty("lms.ftpserver.localDirectory")));
      	}	
	}

		
	public Boolean connect() throws SocketException, IOException {
		ftp = new FTPClient();
		ftp.connect(serverAddress);
		log.info("FTP: reply  ==> "+ftp.getReplyCode()); 
		boolean connected = ftp.login(userId, password);
		
		if (connected) {
	         if(FTPReply.isPositiveCompletion(ftp.getReplyCode())){
	             System.out.println("FTP: Connected Success");
	         }else {
	             System.out.println("FTP: Connection Failed..");
	             ftp.disconnect();
	         }
		}else{
            System.out.println("FTP: Connection Failed");
            ftp.disconnect();
		}
		return connected;
		
		


	}

	public void disconnect() throws IOException {
		if (ftp != null) {
			ftp.logout();
			ftp.disconnect();
		}
	}
	
	public abstract Boolean perfromCopyTask(File filToBeCopied) throws SocketException, IOException ;
	public abstract File perfromReadFileTask(String fileName) throws SocketException, IOException ;


	public String getServerAddress() {
		return serverAddress;
	}


	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRemoteDirectory() {
		return remoteDirectory;
	}


	public void setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}


	public String getLocalDirectory() {
		return localDirectory;
	}


	public void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
	}


	public FTPClient getFtp() {
		return ftp;
	}


	public void setFtp(FTPClient ftp) {
		this.ftp = ftp;
	}
	
	

}
