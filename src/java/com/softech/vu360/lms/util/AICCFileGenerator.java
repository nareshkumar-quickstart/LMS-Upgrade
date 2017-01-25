/**
 * 
 */
package com.softech.vu360.lms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.service.impl.TemplateServiceImpl;

/**
 * @author jason
 *
 */
public class AICCFileGenerator {
	
	private static Logger log = Logger.getLogger(AICCFileGenerator.class);
	
	public static void main(String[] args) {
		
		String auTemplate = "aicc/exportFiles/AUtemplate.vm";
		String crsTemplate = "aicc/exportFiles/CRStemplate.vm";
		String cstTemplate = "aicc/exportFiles/CSTtemplate.vm";
		String desTemplate = "aicc/exportFiles/DEStemplate.vm";
		String csvFilePath = null;
		String tmpDirLoc = null;
		String custCode = null;
		
		if ( args.length == 3 ) {
			csvFilePath = args[0];
			tmpDirLoc = args[1];
			custCode = args[2];
		}
		else {
			log.error("Invalid arguments, three argument are required: csvFilePath, targetLocation and customerCode");
			return;
		}
		
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		HashMap<Object, Object> context;
		List<Course> courses = getCourses(csvFilePath);
		File f = null;
		BufferedWriter bufWriter = null;
		
		log.debug("have "+ courses.size() +" courses to process");
		for (Course course : courses) {
			context = new HashMap<Object, Object>();
			context.put("courseCode", course.getBussinesskey());
			context.put("courseName", course.getName());
			context.put("courseDescription", course.getDescription());
			context.put("customerCode",custCode);
			context.put("courseGUID", course.getCourseGUID());
			context.put("lmsLaunchURL", "https://lms.360training.com/lms/aicc.do");
			try {
				
				// first figure out the base file name
				StringBuffer baseFile = new StringBuffer();
				baseFile.append(tmpDirLoc);
				baseFile.append(course.getBussinesskey());
				baseFile.append("/");
				f = new File(baseFile.toString());
				f.mkdirs();
				baseFile.append(course.getBussinesskey());
				
				// write AU file
				f = new File(baseFile.toString()+".au");
				bufWriter = new BufferedWriter(new FileWriter(f));
				tmpSvc.renderTemplateToWriter(auTemplate, context, bufWriter);
				bufWriter.flush();
				bufWriter.close();

			
				// render CRS file
				f = new File(baseFile.toString()+".crs");
				bufWriter = new BufferedWriter(new FileWriter(f));
				tmpSvc.renderTemplateToWriter(crsTemplate, context, bufWriter);
				bufWriter.flush();
				bufWriter.close();
	
				// render CST file
				f = new File(baseFile.toString()+".cst");
				bufWriter = new BufferedWriter(new FileWriter(f));
				tmpSvc.renderTemplateToWriter(cstTemplate, context, bufWriter);
				bufWriter.flush();
				bufWriter.close();
	
				// render DES file
				f = new File(baseFile.toString()+".des");
				bufWriter = new BufferedWriter(new FileWriter(f));
				tmpSvc.renderTemplateToWriter(desTemplate, context, bufWriter);
				bufWriter.flush();
				bufWriter.close();

			}
			catch (Exception ex) {
				log.error("could not write file for course:"+course.getBussinesskey()+":"+ex.getMessage(), ex);
			}
		} // end for
	}
	
	private static List<Course> getCourses(String aFilePath) {
		List<Course> courses = new ArrayList<Course>();
		
		Course course = null;
		// load the file and parse it
		try {
			
			
			
			CSVReader reader = new CSVReader(new FileReader(aFilePath));
		    String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
				course = new Course();
				course.setId(Long.valueOf(nextLine[0].trim()));
				course.setName(nextLine[1].trim());
				course.setDescription(nextLine[2].trim());
				course.setCourseGUID(nextLine[3].trim());
				course.setBussinesskey(nextLine[4].trim());
				courses.add(course);
		    }
			
/**			
			
			//create BufferedReader to read csv file
			BufferedReader br = new BufferedReader( new FileReader(aFilePath));
			String strLine = "";
			StringTokenizer st = null;
			int lineNumber = 0;
 
			//read comma separated file line by line
			while( (strLine = br.readLine()) != null) {
				lineNumber++;
 
				//break comma separated line using ","
				st = new StringTokenizer(strLine, ",");
				

			}
			
			**/
		}
		catch(Exception ex) {
			log.error("Exception while reading csv file: ", ex);			
		}
		return courses;
	}
}