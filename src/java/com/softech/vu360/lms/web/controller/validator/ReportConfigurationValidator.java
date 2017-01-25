package com.softech.vu360.lms.web.controller.validator;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.web.controller.model.ReportForm;
import com.softech.vu360.util.FieldsValidation;

public class ReportConfigurationValidator  implements Validator{

	public boolean supports(Class clazz) {
		return ReportForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		
	}

	public void validateSystemOwnedReport(ReportForm form, Errors errors){
		VU360Report curr_report = form.getCurrentReport();
		
		if(curr_report.getSystemOwned()){
			errors.setNestedPath("currentReport");
			errors.rejectValue("systemOwned", "error.report.editing.systemowned", "Cannot edit a system owned report");
		}
		errors.setNestedPath("");
	}
	
	
	public void validateProctorReport(ReportForm form, Errors errors){
		//VU360Report summary_report = form.getCurrentReport();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		if (StringUtils.isBlank(form.getCourseId())) {
			//errors.setNestedPath("cancelReportSummary");
			errors.rejectValue("courseId", "error.addNewSynchrounousCourse.courseID.required", "");
		}
		
		if((form.getStartDate()==null || form.getStartDate().equals("")) && (form.getEndDate()==null || form.getEndDate().equals(""))){
			errors.rejectValue("startDate", "lms.proctor.proctorReport.error.selectStartorEndDate.Message","");
			
		}else if(!form.getStartDate().equals("")){
			if(!FieldsValidation.isValidDate(form.getStartDate())){
				//errors.setNestedPath("currentReport");
				errors.rejectValue("startDate", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message","");
			}	
		}else if(!form.getEndDate().equals("")){
			if(!FieldsValidation.isValidDate(form.getEndDate())){
				//errors.setNestedPath("currentReport");
				errors.rejectValue("startDate", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message","");
			}
		}
		errors.setNestedPath("");
	}
	
	public void validateEditReport(ReportForm form, Errors errors){
		VU360Report summary_report = form.getReportSummaryEdit();
		if (StringUtils.isBlank(summary_report.getTitle())) {
			errors.setNestedPath("reportSummaryEdit");
			errors.rejectValue("title", "error.report.editing.title", "Report Name can not left blank");
		}
		if (StringUtils.isBlank(summary_report.getCategory())) {
			errors.setNestedPath("reportSummaryEdit");
			errors.rejectValue("category", "error.report.editing.category", "Category can not left blank");
		}
		if (StringUtils.isBlank(summary_report.getDescription())) {
			errors.setNestedPath("reportSummaryEdit");
			errors.rejectValue("description", "error.report.editing.description", "Description can not left blank");
		}
		errors.setNestedPath("");
	}
}
