package com.softech.vu360.lms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
 
public class FtpClientHandler extends IFtpClient{
 
	private static final Logger log = Logger.getLogger(FtpClientHandler.class.getName());		

	@SuppressWarnings("unused")
	private FtpClientHandler(){}

	
	/**
	 * 
	 * @param properties
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public FtpClientHandler(Properties properties) throws FileNotFoundException, IOException {
		initi(properties);
    }
    
	/**
	 */
	@Override
	public Boolean perfromCopyTask(File filToBeCopied) throws SocketException, IOException {
		InputStream input = null;
		FTPClient ftp = null;
		boolean isUploaded = false;
        try {
        	ftp = getFtp();
        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
        	ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(getRemoteDirectory());
//			ftp.c
			
			input = new FileInputStream(filToBeCopied);
			isUploaded = ftp.storeFile(filToBeCopied.getName(), input);
			input.close();
		} catch (SocketException e) {
			e.printStackTrace();
			throw e;
		}
        catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
        finally{
        	if(input!=null){
        		try{
        			input.close();
        		}
        		catch(Exception e){}
        	}
        }
        return isUploaded;
	}

public static void main(String[] args) {
	try {
		File file1 = new File("D:/haiderAffidavit.pdf");
		IFtpClient client = new FtpClientHandler(null);
		client.connect();
		client.perfromCopyTask(file1);
		client.disconnect();
		

		//String fname = "haiderAffidavit.pdf";
		//IFtpClient client = new FtpClientHandler(null);
		//client.connect();
		//File f = client.perfromReadFileTask(fname);
		//client.disconnect();

		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
}


	@Override
	public File perfromReadFileTask(String fileName) throws SocketException, IOException {

		
		FTPClient client = getFtp();
		client.setFileType(FTP.BINARY_FILE_TYPE);
		client.enterLocalPassiveMode();
		FileOutputStream fos = null;
		File file = null;

		try {
			//
			// The remote filename to be downloaded.
			//
			if(StringUtils.isNotEmpty(fileName)){
				
				String tempDir = System.getProperty("java.io.tmpdir");
		        file = new File(tempDir +  System.getProperty("file.separator") + fileName);

				fos = new FileOutputStream(file);
				boolean success = client.retrieveFile(getRemoteDirectory() + System.getProperty("file.separator") + fileName, fos);
				if(success)
					log.info("File read successfully ==> " + getRemoteDirectory() + "/" + fileName );
				else
					log.info("File not found ==> " + getRemoteDirectory() + "/" + fileName);

			}
			
		} catch (IOException e) {
			log.debug(e);
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}

}