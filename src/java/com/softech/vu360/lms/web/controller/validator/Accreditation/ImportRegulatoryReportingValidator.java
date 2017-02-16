package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.RegulatoryReportingService;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;
import com.softech.vu360.lms.web.controller.model.ImportRegulatoryReportingInfoForm;
import com.softech.vu360.lms.web.controller.model.accreditation.RequirementForm;

public class ImportRegulatoryReportingValidator  implements Validator{
	
	private RegulatoryReportingService regulatoryReportingService = null;
	Map<String,Object> mainMap = new  HashMap<String,Object>();
	List<Map<String,String>> errorList = new ArrayList<Map<String, String>>();
	
	@Override
	public boolean supports(Class clazz) {
		return ImportRegulatoryReportingInfoForm.class.isAssignableFrom(clazz);
		
	}


	@Override
	public void validate(Object obj, Errors errors) {
		
		ImportRegulatoryReportingInfoForm form = (ImportRegulatoryReportingInfoForm)obj;
		validatePage(form,errors);
		
	}

	private void validatePage(ImportRegulatoryReportingInfoForm form, Errors errors) {
		
		errors.setNestedPath("");
		if( form.getFile() == null  ) {
			errors.rejectValue("file", "error.AddDocuments.fileName");
		} else if ( form.getFile().getSize() == 0 ) {
			errors.rejectValue("file", "error.AddDocuments.fileName");
		} else if(form.getFile()!=null){
			 String fileName = form.getFile().getOriginalFilename().trim().replaceAll(" ", "");
			 String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";
			 Pattern pattern;
		     Matcher matcher;
		     pattern = Pattern.compile(IMAGE_PATTERN);
			 matcher = pattern.matcher(fileName);
			 boolean isCSVFile =  matcher.matches();
			 
			 if(!isCSVFile)
				 errors.rejectValue("file", "error.AddDocuments.fileType");
			 
		if (form.getFile()!=null && form.getFile().getSize() > 0) {
			mainMap = regulatoryReportingService.ReadCSVfile(form.getFile(),"");
			for(Map.Entry m:mainMap.entrySet()){  
			    if(m.getKey().equals("errorList"))
			    	errorList = (List<Map<String,String>>)m.getValue();;
			  }  
		}
		if(errorList != null && errorList.size() > 0){
			errors.rejectValue("file", "error.AddDocuments.fileName");
		}
	  }
	}
	
	public RegulatoryReportingService getRegulatoryReportingService() {
		return regulatoryReportingService;
	}

	public void setRegulatoryReportingService(RegulatoryReportingService regulatoryReportingService) {
		this.regulatoryReportingService = regulatoryReportingService;
	}

	

}
