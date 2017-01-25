package com.softech.vu360.lms.web.controller.accreditation;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.util.VU360Properties;

/**
 * @author Dyutiman
 *
 */
public class ExportCsvController implements Controller {

	String documentPath = VU360Properties.getVU360Property("document.saveLocation");

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			String type = request.getParameter("type");
			File file = null;
			if( type.equalsIgnoreCase("csv") ) {
				file = new File(documentPath+"CreditReport.csv");
			} else if( type.equalsIgnoreCase("pdf") ) {
				file = new File(documentPath+"Certificates.pdf");
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] filedata = null;

			filedata = new byte[(int)file.length()];
			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < filedata.length
					&& (numRead = fis.read(filedata, offset, filedata.length-offset)) >= 0) {
				offset += numRead;
			}
			fis.read();
			response.reset();
			if( type.equalsIgnoreCase("csv") ) {
				response.setContentType("application/csv");
				response.setContentLength((int)file.length());
				response.setHeader("Content-Disposition", "attachment; filename="+"CreditReport.csv");
			} else if( type.equalsIgnoreCase("pdf") ) {
				response.setContentType("application/pdf");
				response.setContentLength((int)file.length());
				response.setHeader("Content-Disposition", "attachment; filename="+"Certificates.pdf");
			}
			ServletOutputStream outputStream = response.getOutputStream();

			outputStream.write(filedata);
			outputStream.flush();
			outputStream.close();

		} catch (Exception e) {
			//log.debug("exception", e);
		}
		return null;
	}

}