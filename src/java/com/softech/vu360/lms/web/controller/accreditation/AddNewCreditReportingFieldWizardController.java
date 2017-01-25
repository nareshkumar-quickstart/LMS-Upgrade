package com.softech.vu360.lms.web.controller.accreditation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultipleLineTextCreditReportingfield;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SingleLineTextCreditReportingFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CreditReportingFieldForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCreditReportingFieldValidator;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddNewCreditReportingFieldWizardController extends AbstractWizardFormController {

	private AccreditationService accreditationService = null;

	private	String closeApprovalTemplate=null;

	
	private static final String[] REPORTINGFIELD_TYPES = {
		CreditReportingFieldForm.REPORTINGFIELD_SINGLE_LINE_OF_TEXT
		, CreditReportingFieldForm.REPORTINGFIELD_DATE
		, CreditReportingFieldForm.REPORTINGFIELD_MULTIPLE_LINES_OF_TEXT
		, CreditReportingFieldForm.REPORTINGFIELD_NUMBER
		, CreditReportingFieldForm.REPORTINGFIELD_RADIO_BUTTON
		, CreditReportingFieldForm.REPORTINGFIELD_CHOOSE
		, CreditReportingFieldForm.REPORTINGFIELD_CHECK_BOX
		, CreditReportingFieldForm.REPORTINGFIELD_SOCIAL_SECURITY_NUMBER
		, CreditReportingFieldForm.REPORTINGFIELD_STATIC_FIELD
		, CreditReportingFieldForm.REPORTINGFIELD_TELEPHONE_NUMBER_FIELD
	};

	public AddNewCreditReportingFieldWizardController() {
		super();
		setCommandName("reportingFieldForm");
		setCommandClass(CreditReportingFieldForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/CreditReportingFieldTemplate/addCreditReportingField/add_reportingfield"
				, "accreditation/CreditReportingFieldTemplate/addCreditReportingField/add_reportingfield_details_container"
				, "accreditation/CreditReportingFieldTemplate/addCreditReportingField/add_reportingfield_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		CreditReportingField creditReportingField = new CreditReportingField();
		form.setCreditReportingField(creditReportingField);
		form.setFieldType(CreditReportingFieldForm.REPORTINGFIELD_SINGLE_LINE_OF_TEXT);
		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		/*if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
			long courseApprovalId = Long.parseLong(request.getParameter("appID"));
			CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(courseApprovalId);
			form.setCourseApproval(courseApproval);
		}*/
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		if(this.getTargetPage(request, page) == 2) {
			if(form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_RADIO_BUTTON)
					||form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHOOSE)
					||form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHECK_BOX)) {
				if( !StringUtils.isBlank(form.getOption()) ) {
					this.readOptions(form);
				}
			}
		}
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		// TODO Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		super.postProcessPage(request, command, errors, page);
	}

	private void readOptions(CreditReportingFieldForm form){
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(form.getOption()));

		try {
			List<String> optionList = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						optionList.add(str);
					}
				}
			}
			form.setOptionList(optionList);
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		
			context.put("target", "showReportingFieldList");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		
		
	}

	private ContentOwner getCurrentContentOwner(){
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		return contentOwner ;
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {

		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		CreditReportingField creditReportingField = form.getCreditReportingField();
		ContentOwner contentOwner = this.getCurrentContentOwner();

		if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCreditReportingFiled singleLineTextCreditReportingFiled = new SingleLineTextCreditReportingFiled();
			singleLineTextCreditReportingFiled.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			singleLineTextCreditReportingFiled.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			singleLineTextCreditReportingFiled.setFieldLabel(creditReportingField.getFieldLabel());
			singleLineTextCreditReportingFiled.setFieldRequired(creditReportingField.isFieldRequired());
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//singleLineTextCreditReportingFiled.setCourseApproval(form.getCourseApproval());
				singleLineTextCreditReportingFiled.setContentOwner(contentOwner);
				//CourseApproval courseApproval =form.getCourseApproval();
				//Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
				//reportingFields.add(singleLineTextCreditReportingFiled);
				//courseApproval.setReportingFields(reportingFields);
				//accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(singleLineTextCreditReportingFiled);
			//}
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_DATE)) {
			DateTimeCreditReportingField dateTimeCreditReportingField =  new DateTimeCreditReportingField();
			dateTimeCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			dateTimeCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			dateTimeCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			dateTimeCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//dateTimeCreditReportingField.setCourseApproval(form.getCourseApproval());
				dateTimeCreditReportingField.setContentOwner(contentOwner);
				//CourseApproval courseApproval =form.getCourseApproval();
				//Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
				//reportingFields.add(dateTimeCreditReportingField);
				//courseApproval.setReportingFields(reportingFields);
				
			//	accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(dateTimeCreditReportingField);
			//}
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCreditReportingfield multipleLineTextCreditReportingfield = new MultipleLineTextCreditReportingfield();
			multipleLineTextCreditReportingfield.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			multipleLineTextCreditReportingfield.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			multipleLineTextCreditReportingfield.setFieldLabel(creditReportingField.getFieldLabel());
			multipleLineTextCreditReportingfield.setFieldRequired(creditReportingField.isFieldRequired());
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//multipleLineTextCreditReportingfield.setCourseApproval(form.getCourseApproval());
				
				multipleLineTextCreditReportingfield.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(multipleLineTextCreditReportingfield);
//				courseApproval.setReportingFields(reportingFields);
//				
//				
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(multipleLineTextCreditReportingfield);
			//}
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_NUMBER)) {
			NumericCreditReportingField numericCreditReportingField = new NumericCreditReportingField();
			numericCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			numericCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			numericCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			numericCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());

			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//numericCreditReportingField.setCourseApproval(form.getCourseApproval());
				numericCreditReportingField.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(numericCreditReportingField);
//				courseApproval.setReportingFields(reportingFields);
//								
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(numericCreditReportingField);
			//}
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCreditReportingFiled ssnCreditReportingFiled = new SSNCreditReportingFiled();
			ssnCreditReportingFiled.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			ssnCreditReportingFiled.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			ssnCreditReportingFiled.setFieldLabel(creditReportingField.getFieldLabel());
			ssnCreditReportingFiled.setFieldRequired(creditReportingField.isFieldRequired());
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//ssnCreditReportingFiled.setCourseApproval(form.getCourseApproval());
				ssnCreditReportingFiled.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(ssnCreditReportingFiled);
//				courseApproval.setReportingFields(reportingFields);
//			
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(ssnCreditReportingFiled);
			//}
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_RADIO_BUTTON)) {
			SingleSelectCreditReportingField singleSelectCreditReportingField = new SingleSelectCreditReportingField();
			singleSelectCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			singleSelectCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			singleSelectCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			singleSelectCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			if (form.isAlignment())
				singleSelectCreditReportingField.setAlignment(creditReportingField.HORIZONTAL);
			else
				singleSelectCreditReportingField.setAlignment(creditReportingField.VERTICAL);
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//singleSelectCreditReportingField.setCourseApproval(form.getCourseApproval());
				singleSelectCreditReportingField.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(singleSelectCreditReportingField);
//				courseApproval.setReportingFields(reportingFields);
//				
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(singleSelectCreditReportingField);
			//}
			
			for(int i=0;i<form.getOptionList().size();i++) {
				CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCreditReportingField(singleSelectCreditReportingField);
				accreditationService.saveChoice(option);
			}
			
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHOOSE)) {
			MultiSelectCreditReportingField multiSelectCreditReportingField = new MultiSelectCreditReportingField();
			multiSelectCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			multiSelectCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			multiSelectCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			multiSelectCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			multiSelectCreditReportingField.setCheckBox(false);
			if (form.isAlignment())
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.HORIZONTAL);
			else
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.VERTICAL);
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//multiSelectCreditReportingField.setCourseApproval(form.getCourseApproval());
				multiSelectCreditReportingField.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(multiSelectCreditReportingField);
//				courseApproval.setReportingFields(reportingFields);
//			
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(multiSelectCreditReportingField);
			//}
				
				//Collections.sort(form.getOptionList());	
			
			for(int i=0;i<form.getOptionList().size();i++) {
				CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCreditReportingField(multiSelectCreditReportingField);
				accreditationService.saveChoice(option);
			}
			
		} else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHECK_BOX)) {
			MultiSelectCreditReportingField multiSelectCreditReportingField = new MultiSelectCreditReportingField();
			multiSelectCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			multiSelectCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			multiSelectCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			multiSelectCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			multiSelectCreditReportingField.setCheckBox(true);
			if (form.isAlignment())
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.HORIZONTAL);
			else
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.VERTICAL);
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//multiSelectCreditReportingField.setCourseApproval(form.getCourseApproval());
				multiSelectCreditReportingField.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(multiSelectCreditReportingField);
//				courseApproval.setReportingFields(reportingFields);
//			
//			
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(multiSelectCreditReportingField);
			//}
			
			for(int i=0;i<form.getOptionList().size();i++) {
				CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCreditReportingField(multiSelectCreditReportingField);
				accreditationService.saveChoice(option);
			}
			
		}else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_STATIC_FIELD)) {
			StaticCreditReportingField staticCreditReportingField =  new StaticCreditReportingField();
			staticCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			staticCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			staticCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			staticCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			
			//if(form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				//StaticCreditReportingField.setCourseApproval(form.getCourseApproval());
				staticCreditReportingField.setContentOwner(contentOwner);
//				CourseApproval courseApproval =form.getCourseApproval();
//				Set<CreditReportingField> reportingFields=courseApproval.getReportingFields();
//				reportingFields.add(staticCreditReportingField);
//				courseApproval.setReportingFields(reportingFields);
//				
//				accreditationService.saveCourseApproval(courseApproval);
				accreditationService.saveCreditReportingField(staticCreditReportingField);
			//}
		}
		else if (form.getFieldType().equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_TELEPHONE_NUMBER_FIELD)) {
			TelephoneNumberCreditReportingField telephoneNumberCreditReportingField = new TelephoneNumberCreditReportingField();
			fillCreditReportingFieldValues(telephoneNumberCreditReportingField,creditReportingField,contentOwner);
		} 
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		//if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
			context.put("target", "showReportingFieldList");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		//}
		//return null;
	}
	
	
	
	
	/**
	 * The method will be used to fill data to Model object from 
	 * 
	 * @param creditReportingField
	 * @param contentOwner
	 * 
	 */
	private void fillCreditReportingFieldValues(CreditReportingField targetCreditReportingField,CreditReportingField sourceCreditReportingField,ContentOwner contentOwner){						
		targetCreditReportingField.setCustomFieldDescription(sourceCreditReportingField.getCustomFieldDescription());
		targetCreditReportingField.setFieldEncrypted(sourceCreditReportingField.isFieldEncrypted());
		targetCreditReportingField.setFieldLabel(sourceCreditReportingField.getFieldLabel());
		targetCreditReportingField.setFieldRequired(sourceCreditReportingField.isFieldRequired());
		targetCreditReportingField.setContentOwner(contentOwner);
		accreditationService.saveCreditReportingField(targetCreditReportingField);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map<Object,Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		Map<Object,Object> model = new HashMap<Object,Object>();
		switch(page){
		case 0:
			model.put("reportingFieldTypes", REPORTINGFIELD_TYPES);
			return model;
		case 1:
			if(request.getParameter("step1") != null && 
					request.getParameter("step1").equalsIgnoreCase("page1")) {
				CreditReportingFieldForm form = (CreditReportingFieldForm) command;
				CreditReportingField creditReportingField =  new CreditReportingField();
				form.setCreditReportingField(creditReportingField);
			} 
			break;
		case 2:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddCreditReportingFieldValidator validator = (AddCreditReportingFieldValidator)this.getValidator();
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			break;
		case 1:			
			
			validator.validateCreditReportingField(form, errors);
			break;
		case 2:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getcloseApprovalTemplate() {
		return closeApprovalTemplate;
	}
	
	public void setCloseApprovalTemplate(String closeApprovalTemplate) {
		this.closeApprovalTemplate = closeApprovalTemplate;
	}
}
