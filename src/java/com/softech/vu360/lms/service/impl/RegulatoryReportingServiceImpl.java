package com.softech.vu360.lms.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;


import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.RegulatoryReportingService;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class RegulatoryReportingServiceImpl implements RegulatoryReportingService{
	
	private EnrollmentService enrollmentService = null;
	private LearnerService learnerService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private String fileErrors = "";
	public static final String STUDENTID = "Student ID";
	public static final String ENROLLMENTID = "Enrollment ID";
	public static final String COURSEID = "Course ID";
	public static final String DATEREPORTED = "Date Reported";
	public static final String ERRORS = "Errors";
	char csv_separator = ',';
	
	private static final Logger log = Logger.getLogger(RegulatoryReportingServiceImpl.class.getName());

	

	@Override
public Map<String,Object> ReadCSVfile(MultipartFile file, String reportingMethod){
		
		List<Map<String,String>> errorList = new ArrayList<>();
		List<Map<String,String>> mainList = new ArrayList<>();
		List<Map<String,String>> dataList = new ArrayList<>();
		String [] nextLine;
		String delimiter = ",";
		String[] strSplittedIds;
		int totTempID,addedtempID;
		List<Course> lstCourse = new ArrayList<>();
		String bussinessKey = null;
    	
        CSVReader reader;
		try {
			reader = new CSVReader(new InputStreamReader(file.getInputStream()), '\t', '\'', 1);
		
		
		while ((nextLine = reader.readNext()) != null) {
			if(StringUtils.isNotEmpty(nextLine[0])){
				strSplittedIds = nextLine[0].split(delimiter);
    			
	            if(strSplittedIds!=null && strSplittedIds.length==4 && (strSplittedIds[0]!=null && strSplittedIds[0].length()>0) && (strSplittedIds[1]!=null && strSplittedIds[1].length()>0) && (strSplittedIds[2]!=null && strSplittedIds[2].length()>0) && (strSplittedIds[3]!=null && strSplittedIds[3].length()>0)){
	            	
	            	List<Long> lstEnrollmentIds = new ArrayList<>();
	            	Learner learner = learnerService.getLearnerByID(Long.parseLong(strSplittedIds[0]));
	            	if(learner != null){
	            		
	            	}
	            	else{
	            		fileErrors = fileErrors == null ? "Student ID "+ strSplittedIds[0] +" cannot be found," : fileErrors + "Student ID "+ strSplittedIds[0] +" cannot be found,";
	            	}
	            	
	            	lstEnrollmentIds.add(Long.parseLong(strSplittedIds[1]));
	            	List<LearnerEnrollment> lsLearnerEnrollments = enrollmentService.getLearnerEnrollmentByEnrollmentIds(lstEnrollmentIds);
	            	if(lsLearnerEnrollments!=null && !lsLearnerEnrollments.isEmpty()){
	            		for(LearnerEnrollment le : lsLearnerEnrollments){
	            			bussinessKey = le.getCourse().getBussinesskey();
	            		}
	            	}
	            	else{
	            		fileErrors = fileErrors == null ? "Enrollment ID "+ strSplittedIds[1] +" cannot be found against the given Student ID," : fileErrors + "Enrollment ID "+ strSplittedIds[1] +" cannot be found against the given Student ID,";
	            	}
	            	
	            	lstCourse = courseAndCourseGroupService.getCourseByBusinessKey(strSplittedIds[2]);
	            	
	            	if(lstCourse!=null && !lstCourse.isEmpty()){
	            		for(Course c:lstCourse){
		            	 if(bussinessKey != null && bussinessKey.length()>0){	
	            			if(bussinessKey.equals(c.getBussinesskey())){
		            			
		            		}
		            		else{ 
		            			fileErrors = fileErrors == null ? "Course ID cannot be found against the given Student ID.," : fileErrors + "Course ID cannot be found against the given Student ID.,";
		            		}
		            	 }
		              }
	            		
	            	}else{
	            		fileErrors = fileErrors == null ? "Course ID cannot be found against the given Student ID.," : fileErrors + "Course ID cannot be found against the given Student ID.,";
	            	}
	            	
	            	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	            	Date mailedDate = dateFormat.parse(strSplittedIds[3]);
	            	
	            	if(isAfterToday(mailedDate)){
	            		if("certificateMailingDate".equals(reportingMethod))
	            			fileErrors = fileErrors == null ? "Mailing Date cannot be a future date," : fileErrors + "Mailing Date cannot be a future date,";
	            		else if("courseReportingDate".equals(reportingMethod))
	            			fileErrors = fileErrors == null ? "Reporting Date cannot be a future date," : fileErrors + "Reporting Date cannot be a future date,";
	            	}
	            	
	            	
	            	if(!StringUtils.isEmpty(fileErrors)){
	            	
	            		Map<String,String> mMap = new HashMap<>();
	            		mMap.put(STUDENTID, strSplittedIds[0]);
		            	mMap.put(ENROLLMENTID, strSplittedIds[1]);
		            	mMap.put(COURSEID, strSplittedIds[2]);
		            	mMap.put(DATEREPORTED, strSplittedIds[3]);
		            	mMap.put(ERRORS, removeLastChar(fileErrors.toString()));
		            	
		            	dataList.add(mMap);
		            	errorList.add(mMap);
		            	fileErrors = null;
		            	mMap = null;
		            }
	            	else{
	            		Map<String,String> mainMap = new HashMap<>();
	            		mainMap.put(STUDENTID, strSplittedIds[0]);
	            		mainMap.put(ENROLLMENTID, strSplittedIds[1]);
	            		mainMap.put(COURSEID, strSplittedIds[2]);
	            		mainMap.put(DATEREPORTED, strSplittedIds[3]);
	            		mainMap.put(ERRORS, "");
	            		
	            		dataList.add(mainMap);
	            		mainList.add(mainMap);
	            	}
	            	
	            }
	            else{
	            	
	            	Map<String,String> mMap = new HashMap<>();
	            	String studentId = "";
	            	String enrollmentId = "";
	            	String courseId = "";
	            	String dateReported ;
	            	
	            	if(strSplittedIds.length == 4){
	            		studentId = strSplittedIds[0];
	            		enrollmentId = strSplittedIds[1];
	            		courseId = strSplittedIds[2];
	            		if(strSplittedIds[3] != null)
	            			dateReported = strSplittedIds[3];
	            		else
	            			dateReported = "";
	            	}
	            	else{
	            		if(strSplittedIds.length == 1){
	            			studentId = strSplittedIds[0];
	    	            	enrollmentId = "";
	    	            	courseId = "";
	    	            	
	            		}
	            		if(strSplittedIds.length == 2){
	            			studentId = strSplittedIds[0];
	    	            	enrollmentId = strSplittedIds[1];
	    	            	courseId = "";
	    	            	
	            		}
	            		if(strSplittedIds.length == 3){
	            			studentId = strSplittedIds[0];
	    	            	enrollmentId = strSplittedIds[1];
	    	            	courseId = strSplittedIds[2];
	    	            	
	            		}
	            		dateReported = "";
	            	}
	            	
	            	if(strSplittedIds!=null && strSplittedIds.length > 0){
		            	mMap.put(STUDENTID, studentId != null ? studentId : "");
		            	mMap.put(ENROLLMENTID, enrollmentId != null ? enrollmentId : "");
		            	mMap.put(COURSEID, courseId != null ? courseId : "");
		            	mMap.put(DATEREPORTED, dateReported);
		            	mMap.put(ERRORS, "Please provide data in all the columns.");
	            	}
	            	else{
	            		mMap.put(STUDENTID, "");
		            	mMap.put(ENROLLMENTID, "");
		            	mMap.put(COURSEID, "");
		            	mMap.put(DATEREPORTED, "");
		            	mMap.put(ERRORS, "Please provide data in all the columns.");
	            	}
	            	dataList.add(mMap);
	            	errorList.add(mMap);
	            	fileErrors = null;
	            	mMap = null;
	            	
	            }
			}
		}
	
	 } catch (IOException e) {
		 log.error(ERRORS, e);
	 } catch (ParseException e) {
		 log.error(ERRORS, e);
	}
		
		Map<String,Object> map = new HashMap<>();
		map.put("mainList",mainList);
		map.put("errorList",errorList);
		map.put("dataList",dataList);
		
		return map;
		
	}

	@Override
public ByteArrayOutputStream getRegulatoryReportingErrorsCSV(List<Map<String,String>> errorList) {

		
		
		String[] values;
		values = new String[5];
		
		StringBuilder csvHeading = new StringBuilder();
		
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		
		try{
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayStream), csv_separator);
		
		csvHeading.append("Student ID" + csv_separator + "Enrollment ID" + csv_separator + "Course ID" + csv_separator + "Date emailed" + csv_separator + "Errors" + "\r\n");

		String[] headingEntries = csvHeading.toString().split(Character.toString(csv_separator) + "");
		writer.writeNext(headingEntries);
		
		
		for (Map<String,String> errorMap : errorList) {
			for(Map.Entry m:errorMap.entrySet()){  
				   
				    if(m.getKey().equals(STUDENTID))
				    	values[0] = m.getValue().toString();
				    if(m.getKey().equals(ENROLLMENTID))
				    	values[1] = m.getValue().toString();
				    if(m.getKey().equals(COURSEID))
				    	values[2] = m.getValue().toString();
				    if(m.getKey().equals(DATEREPORTED))
				    	values[3] = m.getValue().toString();
				    if(m.getKey().equals(ERRORS))
				    	values[4] = m.getValue().toString();
				  }  
			
			writer.writeNext(values);

		}

		writer.close();

		
		return byteArrayStream;
		}catch (IOException e) {
			log.error(ERRORS, e);
		 }
		
		return null;
	}
	
	boolean isAfterToday(Date d) {
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        return d.after(today);
    }
	
	public boolean UpdateEnrollmentInfo(MultipartFile file)
	{
		CSVReader reader;
		try {
			reader = new CSVReader(new InputStreamReader(file.getInputStream()), '\t', '\'', 0);
		
		String [] nextLine;
		String delimiter = ",";
		String reportingMethod = "";
		String[] strSplittedIds;
		List<Long> lstEnrollmentIds = new ArrayList<>();
		 List<LearnerEnrollment> lstLearnerEnrollment;
		 LearnerEnrollment le;
		 LearnerEnrollment learnerEnrollment;

		
		while ((nextLine = reader.readNext()) != null) {
			if(StringUtils.isNotEmpty(nextLine[0])){
				strSplittedIds = nextLine[0].split(delimiter);
    			
	            if(strSplittedIds!=null && strSplittedIds.length==4 && (strSplittedIds[0]!=null && strSplittedIds[0].length() > 0) && (strSplittedIds[1]!=null && strSplittedIds[1].length() > 0) && (strSplittedIds[2]!=null && strSplittedIds[2].length() > 0) && (strSplittedIds[3]!=null && strSplittedIds[3].length() > 0)){
	            	
	            	if(StringUtils.containsIgnoreCase(strSplittedIds[3], "Date Mailed")){
	            	  reportingMethod = "cardMailingDate";
	              }
	              else if(StringUtils.containsIgnoreCase(strSplittedIds[3], "Date Reported")){
	            	  reportingMethod = "certificateReportingDate";
	              }
	              else{	
	            	learnerEnrollment = enrollmentService.loadForUpdateLearnerEnrollment(Long.valueOf(strSplittedIds[1]));
	            	
	            	if(learnerEnrollment != null){
	            		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		            	Date mailedDate = dateFormat.parse(strSplittedIds[3]);
	            		 if("cardMailingDate".equals(reportingMethod)){
	 		            	learnerEnrollment.setCardMailingDate(mailedDate);
		            		learnerEnrollment.setCardMailingStatus("Mailed");	            			 
	            		 }
	            		 if("certificateReportingDate".equals(reportingMethod)){
		 		            	learnerEnrollment.setCourseReportingDate(mailedDate);
		 		            	learnerEnrollment.setCourseReportingStatus("Reported");

		            		 }

	            		enrollmentService.updateEnrollment(learnerEnrollment);
	            	}
	            	

	            	lstEnrollmentIds.add(Long.parseLong(strSplittedIds[1]));
	            	
	            }
	          }
			}
		}
		} catch (IOException e) {
			log.error("Error", e);
		}  // 1 means skip first 
        catch (ParseException e) {
        	log.error("Error", e);
		}
		return false;
	}
	
	private String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}
}
