package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CreditReportForm;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Dyutiman
 */
public class AddCreditReportValidator implements Validator {

	private AccreditationService accreditationService;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return CreditReportForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CreditReportForm form = (CreditReportForm)obj;
		validateCoursePage(form, errors);
		validateTimeFramePage(form, errors);
		validateSelectionPage(form, errors);
		validateStudentPage(form, errors);
		validateOptionPage(form, errors);
	}

	public void validateCoursePage( CreditReportForm form, Errors errors ) {
		boolean sel = false;
		for( CourseItem item : form.getCourses() ) {
			if( item.getSelected() ) {
				sel = true;
				break;
			}
		}
		if( !sel ) {
			errors.rejectValue("courses", "error.custEntitlement.selectedCourses.required");
		}
	}

	public void validateTimeFramePage( CreditReportForm form, Errors errors ) {
		/*if( form.getSelectedLearners() == 0 ) {
			errors.rejectValue("selectedLearners", "error.addCreditReport.noStudent");
		}*/
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;

		if( form.getFromDate().isEmpty() ) {
			errors.rejectValue("fromDate", "error.addCreditReport.fromDate");
		} else {
			try {
				sDate = formatter.parse(form.getFromDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if( form.getToDate().isEmpty() ) {
			errors.rejectValue("toDate", "error.addCreditReport.toDate");
		} else {
			try {
				eDate = formatter.parse(form.getToDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("toDate", "error.addCreditReport.invalidDate.crossed","");
			}
		}
	}

	public void validateSelectionPage(CreditReportForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		List<CourseItem> items = form.getCourses();
		int i = 0;
		for( CourseItem item : items ) {
			if( item.getSelected() )
				i ++;
		}
		Long[] courseIdArray = new Long[i];
		i = 0;
		for( CourseItem item : items ) {
			if( item.getSelected() ) {
				courseIdArray[i] = item.getCourse().getId();
				i ++;
			}	
		}
		List<Learner> searchedLearners = new ArrayList <Learner>();
		try {
			searchedLearners = accreditationService.SearchLearnerByCourseCompletion(courseIdArray, 
					formatter.parse(form.getFromDateWithTime()), formatter.parse(form.getToDateWithTime()), 
					"", "", "" );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if( searchedLearners.size() == 0 ) {
			errors.rejectValue("learners", "error.addCreditReport.learner.required");
		}
	}

	public void validateStudentPage( CreditReportForm form, Errors errors ) {
		boolean sel = false;
		for( LearnerItemForm item : form.getLearners() ) {
			if( item.isSelected() ) {
				sel = true;
				break;
			}
		}
		if( !sel ) {
			errors.rejectValue("learners", "error.addCreditReport.student.required");
		}
	}

	public void validateOptionPage( CreditReportForm form, Errors errors ) {

		String documentPath = VU360Properties.getVU360Property("document.saveLocation");

		if( ( form.getExportCsv().isEmpty() || form.getExportCsv().equalsIgnoreCase("false") ) && 
				( form.getGenCertificate().isEmpty() || form.getGenCertificate().equalsIgnoreCase("false") ) &&
				( form.getMarkComplete().isEmpty() || form.getMarkComplete().equalsIgnoreCase("flase") ) ) {
			errors.rejectValue("exportCsv", "error.addCreditReport.option.required");
		}
		if( form.getGenCertificate().equalsIgnoreCase("true") ) {
			List<CourseItem> items = form.getCourses();
			List<Course> selectedCourses = new ArrayList <Course>();
			for( CourseItem item : items ) {
				if( item.getSelected() )
					selectedCourses.add(item.getCourse());
			}
			//TODO validation need to be approved from jason what to do if no course approval 
			// defined for the selected course with proper certificate physically available on disk
			for( Course c : selectedCourses ) {
				CourseApproval app = accreditationService.getCourseApprovalByCourse(c);
				if(app==null)continue;
				Certificate cert = app.getCertificate();
				File f = new File(documentPath + cert.getFileName());
				if( ! f.exists() ) {
					errors.rejectValue("genCertificate", "error.addCreditReport.certificate.required");
					break;
				}
			}
		}
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
}