package com.softech.vu360.lms.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.util.VU360Properties;
public class FileUploadApacheAPI extends HttpServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws
	ServletException, IOException
	{
		
		response.setContentType("text/html");
		
		PrintWriter pw=response.getWriter();
		String fileName=null;
		long sizeInBytes=0;
		try
		{
			// Check that we have a file upload request
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart)
			{				
				FileItemFactory factory = new DiskFileItemFactory();				
				ServletFileUpload upload = new ServletFileUpload(factory);
				List  items = upload.parseRequest(request);
				Iterator iter = items.iterator();
				while (iter.hasNext())
				{					
					FileItem item = (FileItem) iter.next();
					if (!item.isFormField())
					{
						String fileLocation = VU360Properties.getVU360Property("scormPackage.tempSaveLocation");//for saving the file in temporary location
						fileName = item.getName();
						
						System.out.println("FILE BEING UPLOADED/. :::::::::::::::: "+fileName);
						sizeInBytes = item.getSize();
						File destinationFile = new File(fileLocation+File.pathSeparator+System.currentTimeMillis()+"_"+fileName.substring(fileName.lastIndexOf('\\')+1));
						//copy this file to temp location
						FileUploadUtils.copyFile(item, destinationFile);						
					}
					pw.println("<h1>File Uploaded Successfully ....... :: "+fileName+"... "+sizeInBytes+" uploaded...... </h1><br>");
				}
				
			}
			else
				pw.println("<h1>NOT a multipart content....... </h1><br>");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			pw.println("<h1>other file problem</h1><br>");
		}
	}
	
	
	
}