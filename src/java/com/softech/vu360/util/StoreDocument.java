package com.softech.vu360.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Dyutiman
 */
public class StoreDocument {

	String documentPath = VU360Properties.getVU360Property("document.saveLocation");
	String filepath = "";

	public void createForceDirectory(String dirName) throws Exception {

		// Creates  file
		File file = new File(documentPath+dirName);
		filepath = documentPath+dirName;
		// dir with existing file
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException ex) {
			System.out.println("Exception creating Directory="+ex);
		}
	}

	public Boolean createFile( String fileName, byte[] fileContent){

		Boolean isUpload = false;
		if( filepath.isEmpty() )
			filepath = documentPath;
		File f = new File(filepath + File.separator + fileName);
		try{
			FileOutputStream fop = new FileOutputStream(f);
			if( f.exists() ) {
				try {
					fop.write(fileContent);
					fop.flush();
					fop.close();
					isUpload=true;
				} catch( IOException ex1 ) {
					System.out.println("Exception creating file IO ="+ex1);
				}
			}
		} catch( FileNotFoundException ex ) {
			System.out.println("Exception creating file"+ex);
		}
		return isUpload;
	}

	public Boolean createFile( String fileName, MultipartFile file ) {

		Boolean isUpload = false;
		if( filepath.isEmpty() )
			filepath = documentPath;
		File f = new File(filepath + File.separator + fileName);
		// try{
		// FileOutputStream fop = new FileOutputStream(f);
		//if( f.exists() ) {
		try {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			if (file.getSize() > 0) {
				byte[] file1 = file.getBytes();
				byte[] buffer = new byte[8192];
				//file.transferTo(f);
				/*inputStream = file.getInputStream();
						//File realUpload = new File("C:/");
						outputStream = new FileOutputStream(f);
						System.out.println("====22=========");
						System.out.println(file.getOriginalFilename());
						System.out.println("=============");
						int readBytes = 0;
						byte[] buffer = new byte[8192];
						while ((readBytes = inputStream.read(buffer, 0 , 8192))!=-1){
							System.out.println("===ddd=======");
							outputStream.write(buffer, 0, readBytes);
						}
						outputStream.close();
						inputStream.close();*/
			}
		} catch( IOException ex1 ) {
			System.out.println("Exception creating file IO ="+ex1);
		}
		//}
		//}catch(FileNotFoundException ex){
		//	System.out.println("Exception creating file"+ex);
		//}
		return isUpload;
	}

	public void delete(String fileName) {
		try {
			// Construct a File object for the file to be deleted.
			File target = new File(documentPath + File.separator + fileName);
			if ( !target.exists() ) {
				System.err.println("File " + fileName+ " not present to begin with!");
				return;
			}
			// Quick, now, delete it immediately:
			if (target.delete())
				System.err.println("** Deleted " + fileName + " **");
			else
				System.err.println("Failed to delete " + fileName);
		} catch (SecurityException e) {
			System.err.println("Unable to delete " + fileName + "("	+ e.getMessage() + ")");
		}
	}
}