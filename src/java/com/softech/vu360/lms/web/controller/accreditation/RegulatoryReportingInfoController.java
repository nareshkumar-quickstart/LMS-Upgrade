package com.softech.vu360.lms.web.controller.accreditation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.ManageUserStatus;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.RegulatoryReportingService;
import com.softech.vu360.lms.vo.BatchImportParamSerialized;
import com.softech.vu360.lms.vo.RegulatoryReportingParamSerialized;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;
import com.softech.vu360.lms.web.controller.model.ImportAffidavitTemplateIdsForm;
import com.softech.vu360.lms.web.controller.model.ImportRegulatoryReportingInfoForm;
import com.softech.vu360.lms.web.controller.model.ManageUserStatusForm;
import com.softech.vu360.lms.web.controller.model.MultimodalityForm;
import com.softech.vu360.lms.web.controller.validator.EditCustomerValidator;
import com.softech.vu360.lms.web.controller.validator.Accreditation.ImportRegulatoryReportingValidator;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class RegulatoryReportingInfoController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(RegulatoryReportingInfoController.class.getName());
	public static final String CONTEXT = "context";
	private String startTemplate = null;
	private String finishTemplate = null;
	private JmsTemplate acmjmsTemplate;
	private RegulatoryReportingService regulatoryReportingService = null;
	

	private String errors = null;
	final String FILE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";

	

	@Override
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

	}

	@Override
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName)
			throws Exception {
		
	}
	
	public ModelAndView initializePage(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception {

		
		ImportRegulatoryReportingInfoForm form = (ImportRegulatoryReportingInfoForm) command;
		form.setErrorFileName(null);

		return new ModelAndView(startTemplate, CONTEXT, new HashMap());
		
	}
	
	public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		log.debug("INSIDE downloadUploadedFile()");

		Map<Object, Object> context = new HashMap<>();
		Map<String, String> model = new HashMap<>();
		String fileName = request.getParameter("downloadfilename");
		model.put("fileName", fileName);
		log.debug("fileName " + fileName);
		String fileLocation = VU360Properties.getVU360Property("document.regulatoryReporting.errorcsv.saveLocation");

		String filePath = fileLocation + File.separator +  fileName;
		if (!FileUploadUtils.downloadFile(filePath, response)) {
		    errors.rejectValue("", "error.learner.launchcourse.file.notfound");
		}
		
        return new ModelAndView(startTemplate, CONTEXT, context);
	}
	
	public ModelAndView regulatoryReportingId(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception {
	
		ModelAndView modelAndView;
		ImportRegulatoryReportingInfoForm form = (ImportRegulatoryReportingInfoForm) command;
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<Object, Object> context = new HashMap<>();
		
		
		List<Map<String,String>> lstErrors= null;
		List<Map<String,String>> lstMainList;
		List<Map<String,String>> lstDataList= null;
		Map<String,Object> mainMap;
		
		final MultipartFile file = form.getFile();
		form.setFileName(file.getOriginalFilename());
		byte[] filed=file.getBytes();
		form.setFileData(filed);
		Map<Object, Object> formContext;
		Map<Object, Object> errorMap = null;
		FileOutputStream csvwriter;
		
		mainMap = regulatoryReportingService.ReadCSVfile(file,form.getReportingMethod());
		
		for(Map.Entry m:mainMap.entrySet()){  
		    if("mainList".equals(m.getKey())){
		    	lstMainList = (List<Map<String,String>>)m.getValue();
		    }
		    if("errorList".equals(m.getKey())){
		    	lstErrors = (List<Map<String,String>>)m.getValue();
		    }
		    if("dataList".equals(m.getKey())){
		    	lstDataList = (List<Map<String,String>>)m.getValue();
		    }
		  }  
		
		if(mainMap != null && mainMap.size() > 1){
		   errorMap = this.validateForm(form,lstErrors,lstDataList);
		}
		
		
		
		if(errorMap.get("error")==null){
             
            	String isUsingActiveMQ = (String) VU360Properties.getVU360Property("batchimport.activemq");
		           if(isUsingActiveMQ!=null &&  "true".equals(isUsingActiveMQ)){
				     	acmjmsTemplate.send(new MessageCreator() {
				     		@Override
				     					public Message createMessage(Session session) throws JMSException {
				     						return session.createObjectMessage(new RegulatoryReportingParamSerialized(file,file.getOriginalFilename()));
				     					}
				     				});
				     			}
			            
			     			      return new ModelAndView(finishTemplate, "context", context);
			}
		else{
			  if(lstErrors != null && !lstErrors.isEmpty()){
	            	
				    ByteArrayOutputStream csvText = (ByteArrayOutputStream) regulatoryReportingService.getRegulatoryReportingErrorsCSV(lstDataList,form.getReportingMethod());
	            	
					String documentPath = VU360Properties.getVU360Property("document.regulatoryReporting.errorcsv.saveLocation");
					csvwriter = new FileOutputStream(documentPath+ File.separator + loggedInUserVO.getId()+"_errorCreditReport.csv");
	            	
					if(csvText!=null && csvText.size()>0 ){
						csvText.writeTo(csvwriter);
					}
					form.setErrorFileName(loggedInUserVO.getId()+"_errorCreditReport.csv");
	                context.put("status", errorMap);
	                csvwriter.close();
	                modelAndView = new ModelAndView(startTemplate, CONTEXT, context);
	                return modelAndView;
	            }
			  else{
				    context.put("status", errorMap);
	                modelAndView = new ModelAndView(startTemplate, CONTEXT, context);
	                return modelAndView;
			    }
			  }
	}
	
	
	
private Map<Object, Object> validateForm( ImportRegulatoryReportingInfoForm form, List<Map<String, String>> lstErrors, List<Map<String, String>> lstData){
		
		
		List<String> errorList = new ArrayList<>();
		Map<Object, Object> status = new HashMap<>();
			// Email
		if( form.getFile() == null  ) {
			errorList.add("error.AddDocuments.fileName");
		} else if ( form.getFile().getSize() == 0 ) {
			errorList.add("error.AddDocuments.fileName");
		} else if(form.getFile()!=null){
			 String fileName = form.getFile().getOriginalFilename().trim().replaceAll(" ", "");
			 
			 Pattern pattern;
		     Matcher matcher;
		     pattern = Pattern.compile(FILE_PATTERN);
			 matcher = pattern.matcher(fileName);
			 boolean isCSVFile =  matcher.matches();
			 
			 if(!isCSVFile)
				 errorList.add("error.AddDocuments.fileType");	
		     if(lstErrors != null && !lstErrors.isEmpty() )
		    	 errorList.add("error.accraditation.regulatoryReporting.details");

		  }
		     if(errorList != null && !errorList.isEmpty() ){
			    status.put("error", true);
			    status.put("errorCodes", errorList);
		     }
		
        return status;
	}
	
	
	
	
	public String getStartTemplate() {
		return startTemplate;
	}

	public void setStartTemplate(String startTemplate) {
		this.startTemplate = startTemplate;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public JmsTemplate getAcmjmsTemplate() {
		return acmjmsTemplate;
	}

	public void setAcmjmsTemplate(JmsTemplate acmjmsTemplate) {
		this.acmjmsTemplate = acmjmsTemplate;
	}
	
	
	public RegulatoryReportingService getRegulatoryReportingService() {
		return regulatoryReportingService;
	}

	public void setRegulatoryReportingService(RegulatoryReportingService regulatoryReportingService) {
		this.regulatoryReportingService = regulatoryReportingService;
	}
}
