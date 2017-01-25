package com.softech.vu360.lms.web.controller;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.instructor.CourseDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class SCORMUploadController implements Controller{
	private static final Logger log = Logger.getLogger(SCORMUploadController.class.getName());	
	@SuppressWarnings({ "unchecked" })
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		String fileName=null;
		long sizeInBytes=0;
		try
		{
			String fileLocation = VU360Properties.getVU360Property("scormPackage.tempSaveLocation");//for saving the file in temporary location
			CourseDetails courseForm = new CourseDetails();
			String userType = request.getParameter("userType").substring(0,3);
			if(userType.equalsIgnoreCase("ins")) {
				courseForm = (CourseDetails) request.getSession().getAttribute("com.softech.vu360.lms.web.controller.instructor.AddCourseController.FORM.courseForm");
			}
			else if(userType.equalsIgnoreCase("mgr")) {
				courseForm = (CourseDetails) request.getSession().getAttribute("com.softech.vu360.lms.web.controller.manager.ManagerAddCourseController.FORM.courseForm");
			}
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart)	{	
				MultipartRequest mreq = 	(MultipartRequest) request;
				log.debug("mreq.getFileNames()" +mreq.getFileNames());
				Iterator<String> it = mreq.getFileNames();
				Vector<String> fileNames = new Vector<>(); 
				Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
				String successMessage = brander.getBrandElement("lms.instructor.addCourse.scorm.fileUploadedSuccessfully.caption");
				while(it.hasNext()) {
					MultipartFile file = mreq.getFile(it.next());
					fileName = file.getOriginalFilename();
					log.debug("FILE BEING UPLOADED : "+fileName);
					sizeInBytes = file.getSize();
					File destinationFile = new File(fileLocation+File.separator+System.currentTimeMillis()+"_"+fileName.substring(fileName.lastIndexOf('\\')+1));
					fileNames.add(FileUploadUtils.copyFile(file, destinationFile));
					DecimalFormat decimal=new DecimalFormat("0.000");
					pw.println("\n<div><font style=\"font-weight:bold;color:green\">");
					pw.println(successMessage);
					pw.println("</font><br><font style=\"font-weight:bold;\">File Name : </font> <i>"+fileName+" </i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font style=\"font-weight:bold;\"> Size : </font> <i>"+decimal.format((double)sizeInBytes/(1024.0*1024.0))+"</i>Mb </div><br>");
				}	
				
				if(courseForm.getScormPackages().isEmpty()) {
					courseForm.setScormPackages(fileNames);
				}
				else {
					courseForm.getScormPackages().addAll(fileNames);
				}
				
				if(userType.equalsIgnoreCase("ins")) {
					request.getSession().setAttribute("com.softech.vu360.lms.web.controller.instructor.AddCourseController.FORM.courseForm", courseForm);
				}
				else if(userType.equalsIgnoreCase("mgr")) {
					request.getSession().setAttribute("com.softech.vu360.lms.web.controller.manager.ManagerAddCourseController.FORM.courseForm", courseForm);
				}

			}
			else {
				pw.println("<h1>NOT a multipart content. </h1><br>");
			}
		}
		catch(Exception e) {
			log.error(e);
			pw.println("\n<h4 style=\"color:red\" >There was some internal Error while uploading file.</h1><br>");
		}
		return null;
	}
}
