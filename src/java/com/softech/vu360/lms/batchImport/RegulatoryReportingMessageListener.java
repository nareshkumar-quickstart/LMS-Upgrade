package com.softech.vu360.lms.batchImport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.RegulatoryReportingService;
import com.softech.vu360.lms.vo.RegulatoryReportingParamSerialized;
import com.softech.vu360.util.VU360Properties;

import au.com.bytecode.opencsv.CSVReader;

public class RegulatoryReportingMessageListener implements MessageListener{
	
	private static final Logger log = Logger.getLogger(RegulatoryReportingMessageListener.class);
	private static final String REGULATOR_REPORTING_IMPORT_ERROR = "Regulator Reporting Error. ";
	
	private EnrollmentService enrollmentService;
	private RegulatoryReportingService regulatoryReportingService;

	@Override
	public void onMessage(Message message) {
		
		ObjectMessage msg = (ObjectMessage) message;
		try {
			RegulatoryReportingParamSerialized bp = (RegulatoryReportingParamSerialized) msg.getObject();
			
			MultipartFile file = bp.getFile();
			log.debug("FILE NAME "+ file.getOriginalFilename() +" ("+file.getSize()+"kb)");
			regulatoryReportingService.UpdateEnrollmentInfo(file);
			
			
		} catch (JMSException e) {

			log.error("Error", e);
		} 
		
	}
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public RegulatoryReportingService getRegulatoryReportingService() {
		return regulatoryReportingService;
	}

	public void setRegulatoryReportingService(RegulatoryReportingService regulatoryReportingService) {
		this.regulatoryReportingService = regulatoryReportingService;
	}


}
