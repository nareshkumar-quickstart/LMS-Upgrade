package com.softech.vu360.lms.web.controller.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Dyutiman
 *
 */
public class SampleBatchFileController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(SampleBatchFileController.class.getName());

	public ModelAndView fileDownload( HttpServletRequest request, HttpServletResponse response
			, Object command, BindException errors ) throws Exception {

		VU360Properties props = new VU360Properties();
		String file1 = request.getParameter("file");
		String file2 = props.getVU360Property("brands.basefolder");
		String file = file2 + file1;
		String delimiter = request.getParameter("delimiter");
		log.debug("Delimeter is - "+delimiter);
		
		try {
			String fileName = file.substring(file.lastIndexOf("/") + 1);
			String nextLine;
			String line = "";
			File f = new File(file);
			byte[] bites = null;
			List<byte[]> allBytes = new ArrayList<byte[]> ();
			
			FileInputStream in = new FileInputStream(f);
			BufferedReader is = new BufferedReader(new FileReader(f));
			
			while( ( nextLine = is.readLine() ) != null ) {
				String[] record = nextLine.split(",");
				for( int i=0; i<record.length; i++ ) {
					if( i == record.length - 1 ) {
						line = line + record[i];
					}
					else {
						line = line + record[i] + delimiter;
					}
				}
				line = line + "\n";
				bites = line.getBytes();
				allBytes.add(bites);
				line = "";
			}
			response.setContentType("application/csv");
			response.setContentLength( (int)f.length() );
			response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");

			ServletOutputStream ouputStream = response.getOutputStream();
			for( byte[] bite : allBytes ) {
				ouputStream.write(bite);
				ouputStream.flush();
			}
			in.close();
			ouputStream.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return null; 
	}
	
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub

	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub

	}
}