package com.softech.vu360.lms.web.controller.accreditation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultipleLineTextCreditReportingfield;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.RegulatoryApproval;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SingleLineTextCreditReportingFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CreditReportingFieldForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageApproval;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCategory;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCreditReportingFieldValidator;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ManageApprovalSort;
import com.softech.vu360.util.ManageCategorySort;

public class ManageCreditReportingFieldController extends VU360BaseMultiActionController  {
	private static final Logger log = Logger.getLogger(ManageCreditReportingFieldController.class.getName());
	private AccreditationService accreditationService = null;

	public static final String COURSE_APPROVAL = "Course Approval";	
	public static final String ACTIVE_YES = "Yes";
	public static final String ACTIVE_NO = "No";
	
	private String editCreditReportingFieldTemplate;
	//private String closeApprovalTemplate;
	private String listCreditReportingFieldTemplate = null;
	private String redirectTemplate = null;
	private String listCreditReportingFieldApprovalsTemplate = null;
	private String listCreditReportingFieldCategoriesTemplate= null;
	public ManageCreditReportingFieldController() {
		super();
	}

	public ManageCreditReportingFieldController(Object delegate) {
		super(delegate);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String)
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if(command instanceof CreditReportingFieldForm){
			CreditReportingFieldForm form = (CreditReportingFieldForm)command;
			if(methodName.equals("displayReportingFieldForEdit")){
				CreditReportingField creditReportingField = accreditationService.getCreditReportingFieldById(form.getReportingFieldId());
				form.setCreditReportingField(creditReportingField);
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#validate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, java.lang.String)
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		if(methodName.equals("saveReportingField")){
			AddCreditReportingFieldValidator validator = (AddCreditReportingFieldValidator)this.getValidator();
			CreditReportingFieldForm form = (CreditReportingFieldForm)command;
			if (form.getCreditReportingField() instanceof SingleSelectCreditReportingField 
					|| form.getCreditReportingField() instanceof MultiSelectCreditReportingField) {
				this.readOptions(form);
			}
			long crfId = 0;
			if(request.getParameter("crfId") !=null && request.getParameter("crfId").length() >0 )
			{
				crfId = Long.parseLong(request.getParameter("crfId"));
			}
			if(crfId >0)
				validator.validateCreditReportingField(form, errors,crfId);
			else
				validator.validateCreditReportingField(form, errors);
		}
	}

	public ModelAndView displayReportingFieldForEdit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		form.setCreditReportingField(accreditationService.loadForUpdateCreditReportingField(form.getCreditReportingField().getId()));
		CreditReportingField creditReportingField = form.getCreditReportingField();		
		if (creditReportingField instanceof SingleLineTextCreditReportingFiled) {
			SingleLineTextCreditReportingFiled singleLineTextCreditReportingFiled = (SingleLineTextCreditReportingFiled)creditReportingField;
			form.setFieldType(form.REPORTINGFIELD_SINGLE_LINE_OF_TEXT);
			form.setFieldLabel(singleLineTextCreditReportingFiled.getFieldLabel());
			form.setFieldEncrypted(singleLineTextCreditReportingFiled.isFieldEncrypted());
			form.setFieldRequired(singleLineTextCreditReportingFiled.isFieldRequired());
			form.setReportingFieldDescription(singleLineTextCreditReportingFiled.getCustomFieldDescription());
		} else if (creditReportingField instanceof DateTimeCreditReportingField) {
			DateTimeCreditReportingField dateTimeCreditReportingField = (DateTimeCreditReportingField)creditReportingField;
			form.setFieldLabel(dateTimeCreditReportingField.getFieldLabel());
			form.setFieldEncrypted(dateTimeCreditReportingField.isFieldEncrypted());
			form.setFieldRequired(dateTimeCreditReportingField.isFieldRequired());
			form.setReportingFieldDescription(dateTimeCreditReportingField.getCustomFieldDescription());
			form.setFieldType(form.REPORTINGFIELD_DATE);
		} else if (creditReportingField instanceof MultipleLineTextCreditReportingfield) {
			MultipleLineTextCreditReportingfield multipleLineTextCreditReportingfield = (MultipleLineTextCreditReportingfield)creditReportingField;
			form.setFieldLabel(multipleLineTextCreditReportingfield.getFieldLabel());
			form.setFieldEncrypted(multipleLineTextCreditReportingfield.isFieldEncrypted());
			form.setFieldRequired(multipleLineTextCreditReportingfield.isFieldRequired());
			form.setReportingFieldDescription(multipleLineTextCreditReportingfield.getCustomFieldDescription());
			form.setFieldType(form.REPORTINGFIELD_MULTIPLE_LINES_OF_TEXT);
		} else if (creditReportingField instanceof NumericCreditReportingField) {
			NumericCreditReportingField numericCreditReportingField = (NumericCreditReportingField)creditReportingField;
			form.setFieldLabel(numericCreditReportingField.getFieldLabel());
			form.setFieldEncrypted(numericCreditReportingField.isFieldEncrypted());
			form.setFieldRequired(numericCreditReportingField.isFieldRequired());
			form.setReportingFieldDescription(numericCreditReportingField.getCustomFieldDescription());
			form.setFieldType(form.REPORTINGFIELD_NUMBER);
		} else if (creditReportingField instanceof SSNCreditReportingFiled) {
			SSNCreditReportingFiled ssnCreditReportingFiled = (SSNCreditReportingFiled)creditReportingField;
			form.setFieldLabel(ssnCreditReportingFiled.getFieldLabel());
			form.setFieldEncrypted(ssnCreditReportingFiled.isFieldEncrypted());
			form.setFieldRequired(ssnCreditReportingFiled.isFieldRequired());
			form.setReportingFieldDescription(ssnCreditReportingFiled.getCustomFieldDescription());
			form.setFieldType(form.REPORTINGFIELD_SOCIAL_SECURITY_NUMBER);
		} else if (creditReportingField instanceof SingleSelectCreditReportingField) {
			SingleSelectCreditReportingField singleSelectCreditReportingField = (SingleSelectCreditReportingField)creditReportingField;
			form.setFieldLabel(singleSelectCreditReportingField.getFieldLabel());
			form.setFieldEncrypted(singleSelectCreditReportingField.isFieldEncrypted());
			form.setFieldRequired(singleSelectCreditReportingField.isFieldRequired());
			List<CreditReportingFieldValueChoice> options = accreditationService.getOptionsByCreditReportingField(singleSelectCreditReportingField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(singleSelectCreditReportingField.getAlignment())){
				if (singleSelectCreditReportingField.getAlignment().equalsIgnoreCase(singleSelectCreditReportingField.VERTICAL))
					form.setAlignment(false);
			}
			form.setReportingFieldDescription(singleSelectCreditReportingField.getCustomFieldDescription());
			form.setFieldType(form.REPORTINGFIELD_RADIO_BUTTON);
		} else if (creditReportingField instanceof MultiSelectCreditReportingField) {
			MultiSelectCreditReportingField multiSelectCreditReportingField = (MultiSelectCreditReportingField)creditReportingField;
			form.setFieldLabel(multiSelectCreditReportingField.getFieldLabel());
			form.setFieldEncrypted(multiSelectCreditReportingField.isFieldEncrypted());
			form.setFieldRequired(multiSelectCreditReportingField.isFieldRequired());
			List<CreditReportingFieldValueChoice> options = accreditationService.getOptionsByCreditReportingField(multiSelectCreditReportingField);
			form.setOptions(options);
			form.setOption(this.getOption(options));
			if(StringUtils.isNotBlank(multiSelectCreditReportingField.getAlignment())){
				if (multiSelectCreditReportingField.getAlignment().equalsIgnoreCase(multiSelectCreditReportingField.VERTICAL))
					form.setAlignment(false);
			}
			form.setReportingFieldDescription(multiSelectCreditReportingField.getCustomFieldDescription());
			if (multiSelectCreditReportingField.isCheckBox()){
				form.setFieldType(form.REPORTINGFIELD_CHECK_BOX);
			}else{
				form.setFieldType(form.REPORTINGFIELD_CHOOSE);
			}
		} else if (creditReportingField instanceof StaticCreditReportingField) {
			StaticCreditReportingField staticCreditReportingFiled = (StaticCreditReportingField)creditReportingField;
			form.setFieldType(form.REPORTINGFIELD_STATIC_FIELD);
			form.setFieldLabel(staticCreditReportingFiled.getFieldLabel());
			form.setFieldEncrypted(staticCreditReportingFiled.isFieldEncrypted());
			form.setFieldRequired(staticCreditReportingFiled.isFieldRequired());
			form.setReportingFieldDescription(staticCreditReportingFiled.getCustomFieldDescription());
		}
		else if (creditReportingField instanceof TelephoneNumberCreditReportingField) {
			TelephoneNumberCreditReportingField telephoneNumberCreditReportingField = (TelephoneNumberCreditReportingField)creditReportingField;
			form.setFieldLabel(telephoneNumberCreditReportingField.getFieldLabel());
			form.setFieldEncrypted(telephoneNumberCreditReportingField.isFieldEncrypted());
			form.setFieldRequired(telephoneNumberCreditReportingField.isFieldRequired());
			form.setReportingFieldDescription(telephoneNumberCreditReportingField.getCustomFieldDescription());
			form.setFieldType(form.REPORTINGFIELD_TELEPHONE_NUMBER_FIELD);
		} 
		return new ModelAndView(editCreditReportingFieldTemplate);
	}

	public ModelAndView saveReportingField(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		if(errors.hasErrors()){
			Map<Object, Object> context = new HashMap<Object, Object>();
			
			long crfId = 0;
			if(request.getParameter("crfId") !=null && request.getParameter("crfId").length() >0 )
			{
				crfId = Long.parseLong(request.getParameter("crfId"));		
			}
			
			if(crfId > 0)
				context.put("prevCrfId", crfId);
						
			return new ModelAndView(editCreditReportingFieldTemplate, "context", context);
		}
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		CreditReportingField creditReportingField = form.getCreditReportingField();
		if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_SINGLE_LINE_OF_TEXT)) {
			SingleLineTextCreditReportingFiled singleLineTextCreditReportingFiled = (SingleLineTextCreditReportingFiled)creditReportingField;
			singleLineTextCreditReportingFiled.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			singleLineTextCreditReportingFiled.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			singleLineTextCreditReportingFiled.setFieldLabel(creditReportingField.getFieldLabel());
			singleLineTextCreditReportingFiled.setFieldRequired(creditReportingField.isFieldRequired());
			accreditationService.saveCreditReportingField(singleLineTextCreditReportingFiled);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_DATE)) {
			DateTimeCreditReportingField dateTimeCreditReportingField =  (DateTimeCreditReportingField)creditReportingField;
			dateTimeCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			dateTimeCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			dateTimeCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			dateTimeCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			accreditationService.saveCreditReportingField(dateTimeCreditReportingField);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_MULTIPLE_LINES_OF_TEXT)) {
			MultipleLineTextCreditReportingfield multipleLineTextCreditReportingfield = (MultipleLineTextCreditReportingfield)creditReportingField;
			multipleLineTextCreditReportingfield.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			multipleLineTextCreditReportingfield.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			multipleLineTextCreditReportingfield.setFieldLabel(creditReportingField.getFieldLabel());
			multipleLineTextCreditReportingfield.setFieldRequired(creditReportingField.isFieldRequired());
			accreditationService.saveCreditReportingField(multipleLineTextCreditReportingfield);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_NUMBER)) {
			NumericCreditReportingField numericCreditReportingField = (NumericCreditReportingField)creditReportingField;
			numericCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			numericCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			numericCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			numericCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			accreditationService.saveCreditReportingField(numericCreditReportingField);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_SOCIAL_SECURITY_NUMBER)) {
			SSNCreditReportingFiled ssnCreditReportingFiled = (SSNCreditReportingFiled)creditReportingField;
			ssnCreditReportingFiled.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			ssnCreditReportingFiled.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			ssnCreditReportingFiled.setFieldLabel(creditReportingField.getFieldLabel());
			ssnCreditReportingFiled.setFieldRequired(creditReportingField.isFieldRequired());
			accreditationService.saveCreditReportingField(ssnCreditReportingFiled);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_RADIO_BUTTON)) {
			SingleSelectCreditReportingField singleSelectCreditReportingField = (SingleSelectCreditReportingField)creditReportingField;
			singleSelectCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			singleSelectCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			singleSelectCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			singleSelectCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			if (form.isAlignment())
				singleSelectCreditReportingField.setAlignment(singleSelectCreditReportingField.HORIZONTAL);
			else
				singleSelectCreditReportingField.setAlignment(singleSelectCreditReportingField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeChoice(singleSelectCreditReportingField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCreditReportingField(singleSelectCreditReportingField);
				accreditationService.saveChoice(option);
			}
			accreditationService.saveCreditReportingField(singleSelectCreditReportingField);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_CHOOSE)) {
			int displayOrder = 0;
			MultiSelectCreditReportingField multiSelectCreditReportingField = (MultiSelectCreditReportingField)creditReportingField;
			multiSelectCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			multiSelectCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			multiSelectCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			multiSelectCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			if (form.isAlignment())
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.HORIZONTAL);
			else
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.VERTICAL);
			this.readOptions(form);
			List<CreditReportingFieldValueChoice> optionsInDB = accreditationService.getOptionsByCreditReportingField(multiSelectCreditReportingField);
			Map operationValuesMap = createOperationListForMultipleChoiceOptions(form.getOptionList(), optionsInDB, multiSelectCreditReportingField);
			List<CreditReportingFieldValueChoice> deleteList = (ArrayList<CreditReportingFieldValueChoice>)operationValuesMap.get("DELETE_LIST");;
			List<CreditReportingFieldValueChoice> saveList = (ArrayList<CreditReportingFieldValueChoice>)operationValuesMap.get("SAVE_LIST");;
			List<CreditReportingFieldValueChoice> updateList = (ArrayList<CreditReportingFieldValueChoice>)operationValuesMap.get("UPDATE_LIST");;
			List<CreditReportingFieldValueChoice> sortedList = (ArrayList<CreditReportingFieldValueChoice>)operationValuesMap.get("SORTED_LIST");;
			List<CreditReportingFieldValueChoice> updatedsortedList = new ArrayList<CreditReportingFieldValueChoice>();
			List<CreditReportingFieldValueChoice> newupdatedsortedList = new ArrayList<CreditReportingFieldValueChoice>();
			
			//Collections.sort(sortedList, new ReportingFieldComparable());
			
			for(int iCounter = 0;iCounter<sortedList.size();iCounter++){
				CreditReportingFieldValueChoice sortedChoice = sortedList.get(iCounter);
				for (CreditReportingFieldValueChoice savedcreditReportingFieldValueChoice : saveList) {
					if((savedcreditReportingFieldValueChoice.getLabel().equals(sortedChoice.getLabel())) && (savedcreditReportingFieldValueChoice.getValue().equals(sortedChoice.getValue()))) {
						savedcreditReportingFieldValueChoice.setDisplayOrder(sortedChoice.getDisplayOrder());
						sortedList.remove(iCounter);
					}
				}
			}
			
			
			accreditationService.removeChoices(deleteList);//Delete choices
			for (CreditReportingFieldValueChoice creditReportingFieldValueChoice : sortedList) {//Update the choices
				
				accreditationService.saveChoice(creditReportingFieldValueChoice);
			}
			for (CreditReportingFieldValueChoice creditReportingFieldValueChoice : saveList) {//Save new choices
				accreditationService.saveChoice(creditReportingFieldValueChoice);
			}

			accreditationService.saveCreditReportingField(multiSelectCreditReportingField);
		} else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_CHECK_BOX)) {
			MultiSelectCreditReportingField multiSelectCreditReportingField = (MultiSelectCreditReportingField)creditReportingField;
			multiSelectCreditReportingField.setCustomFieldDescription(creditReportingField.getCustomFieldDescription());
			multiSelectCreditReportingField.setFieldEncrypted(creditReportingField.isFieldEncrypted());
			multiSelectCreditReportingField.setFieldRequired(creditReportingField.isFieldRequired());
			multiSelectCreditReportingField.setFieldLabel(creditReportingField.getFieldLabel());
			if (form.isAlignment())
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.HORIZONTAL);
			else
				multiSelectCreditReportingField.setAlignment(multiSelectCreditReportingField.VERTICAL);
			this.readOptions(form);
			accreditationService.removeChoice(multiSelectCreditReportingField);
			for(int i=0;i<form.getOptionList().size();i++) {
				CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
				option.setDisplayOrder(i);
				option.setLabel(form.getOptionList().get(i));
				option.setValue(form.getOptionList().get(i));
				option.setCreditReportingField(multiSelectCreditReportingField);
				accreditationService.saveChoice(option);
			}
			accreditationService.saveCreditReportingField(multiSelectCreditReportingField);
		}
		else if (form.getFieldType().equalsIgnoreCase(form.REPORTINGFIELD_TELEPHONE_NUMBER_FIELD)) {
			TelephoneNumberCreditReportingField telephoneNumberCreditReportingField = (TelephoneNumberCreditReportingField)creditReportingField;
			fillCreditReportingFieldValues(telephoneNumberCreditReportingField,creditReportingField);
			
		} 
		Map<Object, Object> context = new HashMap<Object, Object>();
		//if (StringUtils.isNotBlank(form.getEntity())){
			//if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				context.put("target", "showReportingFieldList");
				return new ModelAndView(redirectTemplate, "context", context);
			//} 
		//}
		//return null;
	}
	
	private void fillCreditReportingFieldValues(CreditReportingField targetCreditReportingField,CreditReportingField sourceCreditReportingField){
		targetCreditReportingField.setCustomFieldDescription(sourceCreditReportingField.getCustomFieldDescription());
		targetCreditReportingField.setFieldEncrypted(sourceCreditReportingField.isFieldEncrypted());
		targetCreditReportingField.setFieldLabel(sourceCreditReportingField.getFieldLabel());
		targetCreditReportingField.setFieldRequired(sourceCreditReportingField.isFieldRequired());
		accreditationService.saveCreditReportingField(targetCreditReportingField);
	}
	
	private List<CreditReportingFieldValueChoice> updateNextItems (List<CreditReportingFieldValueChoice> sortedList,int counter){
        int newCount = 0;
        List<CreditReportingFieldValueChoice> newUpdateItems = new ArrayList<CreditReportingFieldValueChoice>();
		  for(int iCounter = 0;iCounter<sortedList.size();iCounter++){
			 CreditReportingFieldValueChoice sortedChoice = sortedList.get(iCounter);
			 if(sortedChoice.getDisplayOrder() >= counter){
				if(sortedChoice.getId() != null)
				{
				 sortedChoice = accreditationService.loadForUpdateCreditReportingFieldValueChoice(sortedChoice.getId()); 
				 sortedChoice.setDisplayOrder(iCounter +1);
			     newUpdateItems.add(sortedChoice);
				} 
			  }
			}
		  return newUpdateItems;
}
	
	/**
	 * This method delete the Course Approval credit reporting field.
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteReportingField( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		if(errors.hasErrors()){
			return new ModelAndView(listCreditReportingFieldTemplate, "context", context);
		}
		List<CreditReportingField> reportingFieldsToDelete = new ArrayList<CreditReportingField>();		
		String[] selectedReportingFieldId = request.getParameterValues("reportingField");
		Long[] selectedReportingFieldIds = null;
		
		if(selectedReportingFieldId!=null && selectedReportingFieldId.length>0){
			selectedReportingFieldIds = new Long[selectedReportingFieldId.length];
			for (int i = 0; i < selectedReportingFieldId.length; i++) {
				selectedReportingFieldIds[i] = new Long(selectedReportingFieldId[0]);
			}
		}
		
		List<CreditReportingField> reportingFieldList = new ArrayList<CreditReportingField>();
		for (int i=0 ; i < selectedReportingFieldId.length ;i++) {
			CreditReportingField reportingField = accreditationService.loadForUpdateCreditReportingField(Long.parseLong(selectedReportingFieldId[i]));
			reportingFieldList.add(reportingField);
			/*count of approvals to which this field is associated*/
			int usageCount=accreditationService.getContainingRegulatorCategories(reportingField,"","").size();			
			if (usageCount > 0  ){				
				errors.rejectValue("manageCreditReportingField", "error.courseApproval.reportingField.deletionDenied");
				return new ModelAndView(listCreditReportingFieldTemplate, "context", context);
			}
		}
		
		/*count of bookmark to which this field is associated*/
		int bookmarkUsageCount=accreditationService.getBookMarkAssociationsByReportingField(selectedReportingFieldIds).size();			
		if (bookmarkUsageCount > 0  ){				
			errors.rejectValue("manageCreditReportingField", "error.courseApproval.reportingField.deletionDeniedBookmarkAssociation");
			return new ModelAndView(listCreditReportingFieldTemplate, "context", context);
		}
		
		for (CreditReportingField reportingField:reportingFieldList) {			
			/*count of approvals to which this field is associated*/			
			List<CreditReportingFieldValue> reportingFieldValues = accreditationService.getCreditReportingFieldValue(reportingField);
			if ( reportingFieldValues != null && reportingFieldValues.size() > 0 ) {
				reportingField.setActive(false);
				accreditationService.saveCreditReportingField(reportingField);								
			}
			else{ 
			reportingFieldsToDelete.add(reportingField);
			}

		}
		
		
		if (reportingFieldsToDelete != null && reportingFieldsToDelete.size() > 0)
			accreditationService.removeCreditReportingField(reportingFieldsToDelete);

		context.put("target", "showReportingFieldList");
		return new ModelAndView(redirectTemplate, "context", context);
	}
	public ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		//if (StringUtils.isNotBlank(form.getEntity())){

			//if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
				context.put("target", "showReportingFieldList");
				return new ModelAndView(redirectTemplate, "context", context);
			//}

		//} 
		//return null;
	}

	private String getOption(List<CreditReportingFieldValueChoice> optionList) {
		String optionString="";
		//Collections.sort(optionList);
		for (CreditReportingFieldValueChoice option : optionList) {
			optionString = optionString + option.getLabel() + "\n";
		}
		return optionString;
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
	
	/**
	 * This method shows the CreditReporting Fields for Course Approval 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public ModelAndView showReportingFieldList( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		
		CreditReportingFieldForm form = (CreditReportingFieldForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		//@MariumSaud : ContentOwner argument is not used by the method accreditationService.getAllCreditReportingFields implementation
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		String fieldName = (request.getParameter("fieldName") == null) ? "" : request.getParameter("fieldName");
		String fieldType = (request.getParameter("fieldType") == null || request.getParameter("fieldType").equalsIgnoreCase("All")) ? "" : request.getParameter("fieldType");
		context.put("fieldName", fieldName);
		context.put("fieldType", fieldType);
		List<CreditReportingField> creditReportingFieldList = accreditationService.getAllCreditReportingFields(fieldName.trim(),fieldType); 
		//List<CreditReportingField> creditReportingFieldList = accreditationService.getCreditReportingFieldByCourseApproval(form.getCourseApproval()); 
		List<ManageCustomField> manageReportingFieldList = new ArrayList<ManageCustomField>(); 
		if (creditReportingFieldList != null && creditReportingFieldList.size() > 0) {
			for (CreditReportingField reportingField : creditReportingFieldList) {
				ManageCustomField manageReportingField = new ManageCustomField();
				manageReportingField.setFieldName(reportingField.getFieldLabel());
				manageReportingField.setId(reportingField.getId());
				if (reportingField instanceof SingleLineTextCreditReportingFiled) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_SINGLE_LINE_OF_TEXT);
				} else if (reportingField instanceof DateTimeCreditReportingField) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_DATE);
				} else if (reportingField instanceof MultipleLineTextCreditReportingfield) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_MULTIPLE_LINES_OF_TEXT);
				} else if (reportingField instanceof NumericCreditReportingField) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_NUMBER);
				} else if (reportingField instanceof SSNCreditReportingFiled) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_SOCIAL_SECURITY_NUMBER);
				} else if (reportingField instanceof SingleSelectCreditReportingField) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_RADIO_BUTTON);
				} else if (reportingField instanceof MultiSelectCreditReportingField) {
					if (((MultiSelectCreditReportingField) reportingField).isCheckBox())
						manageReportingField.setFieldType(form.REPORTINGFIELD_CHECK_BOX);
					else 
						manageReportingField.setFieldType(form.REPORTINGFIELD_CHOOSE);
				}else if (reportingField instanceof StaticCreditReportingField) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_STATIC_FIELD);
				}
				else if (reportingField instanceof TelephoneNumberCreditReportingField) {
					manageReportingField.setFieldType(form.REPORTINGFIELD_TELEPHONE_NUMBER_FIELD);
				} 
				//manageReportingField.setContainingRegulatorCategoryCount(accreditationService.getContainingRegulatorCategories(reportingField,"","").size());
				//manageReportingField.setInUseByLearners(accreditationService.getCreditReportingFieldValue(reportingField).size()>0);
				manageReportingFieldList.add(manageReportingField);
				
			}
			manageReportingFieldList = this.sortCustomField(request, manageReportingFieldList, context);
			
			form.setManageCreditReportingField(manageReportingFieldList);
		} else {
			form.setManageCreditReportingField(manageReportingFieldList);
		}
		return new ModelAndView(listCreditReportingFieldTemplate, "context", context);
	}
	/**
	 * This method is for sorting the custom field.
	 * @param request
	 * @param manageCustomFieldList
	 * @param context
	 * @return List<ManageCustomField>
	 * @throws Exception
	 */
	private List<ManageCustomField> sortCustomField( HttpServletRequest request, List<ManageCustomField> manageCustomFieldList, Map<Object, Object> context ) throws Exception {
		
//		session = request.getSession();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();

		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( manageCustomFieldList.size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}

		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null ) {
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortColumnIndex") == null ) {
//			context.put("sortColumnIndex", "");
//		}
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null ) {
//			sortDirection = session.getAttribute("sortDirection").toString();
//		} else if (sortColumnIndex == null && session.getAttribute("sortDirection") == null ) {
//			context.put("sortDirection", "");
//		}
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);

		// sorting against officialLicenseName name
		if( sortColumnIndex != null && sortDirection != null ) {
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldLabel");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldLabel");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against jurisdiction
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldType");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("fieldType");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("containingRegulatorCategoryCount");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("containingRegulatorCategoryCount");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}else if( sortColumnIndex.equalsIgnoreCase("3") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("inUseByLearners");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					CustomFieldSort customFieldSort = new CustomFieldSort();
					customFieldSort.setSortBy("inUseByLearners");
					customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(manageCustomFieldList,customFieldSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		return manageCustomFieldList;
	}
	

	/**
	 * For display manage approval page
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView displayApprovals(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView(listCreditReportingFieldApprovalsTemplate);
	}


	/**
	 * For approval search
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showReportingFieldApproval( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			
			List<ManageApproval> approvalList = new ArrayList<ManageApproval>();
			String appName = (request.getParameter("appName") == null) ? "" : request.getParameter("appName");
			String appNumber = (request.getParameter("appNumber") == null) ? "" : request.getParameter("appNumber");			
			String regulator = (request.getParameter("regulator") == null) ? "" : request.getParameter("regulator");
			int addNewApproval = Integer.parseInt((request.getParameter("addNewApproval") == null) ? "0" : request.getParameter("addNewApproval"));
			String  creditReportingFieldIdStr = request.getParameter("reportingFieldId");
			context.put("reportingFieldId", creditReportingFieldIdStr);
			
			appName = HtmlEncoder.escapeHtmlFull(appName).toString();
			context.put("appName", appName);
			appNumber = HtmlEncoder.escapeHtmlFull(appNumber).toString();
			context.put("appNumber", appNumber);
			context.put("appType", COURSE_APPROVAL);
			regulator = HtmlEncoder.escapeHtmlFull(regulator).toString();
			context.put("regulator", regulator);
				
			 	CreditReportingField creditReportingField =accreditationService.getCreditReportingFieldById(Long.valueOf(creditReportingFieldIdStr));
				//List<CourseApproval> approvals = accreditationService.findCourseApproval(appName, appNumber, appType, regulator,loggedInUser.getRegulatoryAnalyst());
			 	Set<CourseApproval> approvals= null;
			 	Set<CourseApproval> containingApprovals = null;//accreditationService.getContainingCredentialCategories(creditReportingField,appName,appNumber);
			 	approvals= containingApprovals;
			 	
			 	if(addNewApproval > 0){
			 		context.put("addNewApproval", addNewApproval);
					List<CourseApproval> allApprovalsList =  accreditationService.findCourseApproval(appName,appNumber, "", "", -1);
					allApprovalsList.removeAll(containingApprovals);
					approvals= new HashSet<CourseApproval>(allApprovalsList);
				}								
				
				for (CourseApproval cApproval : approvals) {
					ManageApproval manageApproval = new ManageApproval();
					manageApproval.setId(cApproval.getId());
					manageApproval.setApprovalName(cApproval.getApprovedCourseName());
					manageApproval.setApprovalNumber(cApproval.getCourseApprovalNumber());
					manageApproval.setApprovalType(COURSE_APPROVAL);
					if(cApproval.getRegulatorCategories() != null && cApproval.getRegulatorCategories().size() >0)
						manageApproval.setRegulatorName(cApproval.getRegulatorCategories().get(0).getDisplayName()+ " -- " + cApproval.getRegulatorCategories().size());
					if (cApproval.getActive())
						manageApproval.setActive(ACTIVE_YES);
					else
						manageApproval.setActive(ACTIVE_NO);
					approvalList.add(manageApproval);
				}

			 
			
			//============================For Sorting============================
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
//			if( pageIndex == null ) pageIndex = session.getAttribute("pageCurrIndex").toString();

			if( sortColumnIndex != null && sortDirection != null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("regulatorName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 3);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("regulatorName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 3);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("4") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("active");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 4);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("active");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 4);
					}
				}
			}	
			context.put("approvals", approvalList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll", request.getParameter("showAll"));
//			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			
			//========================================================================
			
			return new ModelAndView(listCreditReportingFieldApprovalsTemplate, "context",context);

		} catch (Exception e) {
			log.debug("COUGHT EXCEPTION  "+e);
			e.printStackTrace();
		}
		return new ModelAndView(listCreditReportingFieldApprovalsTemplate);
	}

	/**
	 * For approval search
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView showReportingFieldRegulatorCategories( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		
			List<ManageCategory> categoryList = new ArrayList<ManageCategory>();
			String catgoryName = (request.getParameter("categoryName") == null) ? "" : request.getParameter("categoryName");
			String catgoryType = (request.getParameter("categoryType") == null) ? "" : request.getParameter("categoryType");			
			//String regulator = (request.getParameter("regulator") == null) ? "" : request.getParameter("regulator");
			int addNewCategory = Integer.parseInt((request.getParameter("addNewCategory") == null) ? "0" : request.getParameter("addNewCategory"));
			String  creditReportingFieldIdStr = request.getParameter("reportingFieldId");
			context.put("reportingFieldId", creditReportingFieldIdStr);
			
			catgoryName = HtmlEncoder.escapeHtmlFull(catgoryName).toString();
			context.put("categoryName", catgoryName);
			catgoryType = HtmlEncoder.escapeHtmlFull(catgoryType).toString();
			context.put("catgoryType", catgoryType);
			//context.put("appType", COURSE_APPROVAL);
			//regulator = HtmlEncoder.escapeHtmlFull(regulator).toString();
			//context.put("regulator", regulator);
				
			 	CreditReportingField creditReportingField =accreditationService.getCreditReportingFieldById(Long.valueOf(creditReportingFieldIdStr));
				//List<CourseApproval> approvals = accreditationService.findCourseApproval(appName, appNumber, appType, regulator,loggedInUser.getRegulatoryAnalyst());
			 	Set<RegulatorCategory> categories= null;
			 	Set<RegulatorCategory> containingCategories = accreditationService.getContainingRegulatorCategories(creditReportingField,catgoryName,catgoryType);
			 	categories= containingCategories;
			 	
			 	if(addNewCategory > 0){
			 		context.put("addNewCategory", addNewCategory);
					List<RegulatorCategory> allCategoryList =  accreditationService.findRegulatorCategories(catgoryName,catgoryType,-1);
					allCategoryList.removeAll(containingCategories);
					categories= new HashSet<RegulatorCategory>(allCategoryList);
				}								
				
				for (RegulatorCategory category : categories) {
					ManageCategory manageCategory = new ManageCategory();
					manageCategory.setId(category.getId());
					manageCategory.setCategoryType(category.getCategoryType());
					manageCategory.setName(category.getDisplayName());															
					categoryList.add(manageCategory);
				}

			 
			
			//============================For Sorting============================
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
//			if( pageIndex == null ) pageIndex = session.getAttribute("pageCurrIndex").toString();

			if( sortColumnIndex != null && sortDirection != null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageCategorySort sort = new ManageCategorySort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(categoryList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageCategorySort sort = new ManageCategorySort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						//Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageCategorySort sort = new ManageCategorySort();
						sort.setSortBy("categoryType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(categoryList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageCategorySort sort = new ManageCategorySort();
						sort.setSortBy("categoryType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(categoryList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} /*else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("approvalType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("regulatorName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 3);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("regulatorName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 3);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("4") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("active");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 4);
					} else {
						ManageApprovalSort sort = new ManageApprovalSort();
						sort.setSortBy("active");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(approvalList,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 4);
					}
				}*/
			}	
			context.put("categories", categoryList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll", request.getParameter("showAll"));
//			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			
			//========================================================================
			
			return new ModelAndView(listCreditReportingFieldCategoriesTemplate, "context",context);

		} catch (Exception e) {
			log.debug("COUGHT EXCEPTION  "+e);
			e.printStackTrace();
		}
		return new ModelAndView(listCreditReportingFieldCategoriesTemplate);
	}
	/**
	 * This method dis-associates the reporting field with selected approvals
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView deleteReportingFieldRegulatorCategories(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		int  creditReportingFieldId = Integer.parseInt(request.getParameter("reportingFieldId"));
		if  ( request.getParameterValues("regulatorCategory") != null ) {			
			CreditReportingField creditReportingField =accreditationService.getCreditReportingFieldById(creditReportingFieldId);
			String []checkID = request.getParameterValues("regulatorCategory");
			for (int i=0;i<checkID.length;i++) {								
				RegulatorCategory category= accreditationService.loadForUpdateRegulatorCategory(Long.valueOf(checkID[i]));
				category.getReportingFields().remove(creditReportingField);				
				accreditationService.saveRegulatorCategory(category);
			}
		}
		context.put("reportingFieldId", creditReportingFieldId);
		context.put("target", "showReportingFieldRegulatorCategories");
		return new ModelAndView(redirectTemplate, "context", context);
	}
	
	/**
	 * This method associates the reporting field with selected approvals
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView saveReportingFieldRegulatorCategories( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		int  creditReportingFieldId = Integer.parseInt(request.getParameter("reportingFieldId"));
		if  ( request.getParameterValues("regulatorCategory") != null ) {			
			CreditReportingField creditReportingField =accreditationService.getCreditReportingFieldById(creditReportingFieldId);
			String []checkID = request.getParameterValues("regulatorCategory");
			for (int i=0;i<checkID.length;i++) {								
				RegulatorCategory category= accreditationService.loadForUpdateRegulatorCategory(Long.valueOf(checkID[i]));
				category.getReportingFields().add(creditReportingField);				
				accreditationService.saveRegulatorCategory(category);
			}
		}
		context.put("reportingFieldId", creditReportingFieldId);
		context.put("target", "showReportingFieldRegulatorCategories");
		return new ModelAndView(redirectTemplate, "context", context);
	}
	
	private Map<String, List<CreditReportingFieldValueChoice>> createOperationListForMultipleChoiceOptions(List<String> newOptionsList, List<CreditReportingFieldValueChoice> existingOptionsList, MultiSelectCreditReportingField msCrRptField){
		
		List<CreditReportingFieldValueChoice> deleteList = new ArrayList<CreditReportingFieldValueChoice>();
		List<CreditReportingFieldValueChoice> saveList = new ArrayList<CreditReportingFieldValueChoice>();
		List<CreditReportingFieldValueChoice> updateList = new ArrayList<CreditReportingFieldValueChoice>();
		List<CreditReportingFieldValueChoice> sortedList = new ArrayList<CreditReportingFieldValueChoice>();
		
		Map<String, List<CreditReportingFieldValueChoice>> resMap = new HashMap<String, List<CreditReportingFieldValueChoice>>();
		
		if(existingOptionsList!=null && newOptionsList!=null){
			
			//To fill list with new options added or altered
			for (int i = 0; i < newOptionsList.size(); i++) {
				String optionValueLabel = (String)newOptionsList.get(i);
				
				boolean isExist = false;
				
				for (Iterator iterator = existingOptionsList.iterator(); existingOptionsList!=null && iterator.hasNext();) {
					CreditReportingFieldValueChoice creditReportingFieldValueChoice = (CreditReportingFieldValueChoice) iterator.next();
					
					if(optionValueLabel.equals(creditReportingFieldValueChoice.getLabel()) && optionValueLabel.equals(creditReportingFieldValueChoice.getValue())){
						CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
						creditReportingFieldValueChoice = accreditationService.loadForUpdateCreditReportingFieldValueChoice(creditReportingFieldValueChoice.getId());
						creditReportingFieldValueChoice.setDisplayOrder(i);
						sortedList.add(creditReportingFieldValueChoice);
						updateList.add(option);
						isExist = true;
						break;
					}
				}
				
				if(!isExist){//value is new and will be added
					CreditReportingFieldValueChoice option = new CreditReportingFieldValueChoice();
					option.setDisplayOrder(i);
					option.setLabel(optionValueLabel);
					option.setValue(optionValueLabel);
					option.setCreditReportingField(msCrRptField);
					
					saveList.add(option);
					
					sortedList.add(option);
				}
			}
			
			//To fill list with options removed from list
			for (Iterator iterator = existingOptionsList.iterator(); existingOptionsList!=null && iterator.hasNext();) {
				CreditReportingFieldValueChoice creditReportingFieldValueChoice = (CreditReportingFieldValueChoice) iterator.next();
				
				boolean isExist = false;
				for (int i = 0; i < newOptionsList.size(); i++) {
					String optionValueLabel = (String)newOptionsList.get(i);
					
					if(optionValueLabel.equals(creditReportingFieldValueChoice.getLabel()) && optionValueLabel.equals(creditReportingFieldValueChoice.getValue())){
						isExist = true;
						break;
					}
				}
				
				if(!isExist){
					deleteList.add(creditReportingFieldValueChoice);
				}
				
				
			}
			
		}
		
		//Collections.sort(saveList, new ReportingFieldComparable());
		
		for(int iCounter=0;iCounter<saveList.size();iCounter++){
			CreditReportingFieldValueChoice creditReportingFieldValueChoice = saveList.get(iCounter);
			if((sortedList != null) && (sortedList.size() == saveList.size()))
			creditReportingFieldValueChoice.setDisplayOrder(iCounter);
			
		}
		
		resMap.put("DELETE_LIST", deleteList);
		resMap.put("SAVE_LIST", saveList);
		resMap.put("UPDATE_LIST", updateList);
		resMap.put("SORTED_LIST", sortedList);
		
		return resMap;
	}

	public class ReportingFieldComparable implements Comparator<CreditReportingFieldValueChoice>{
		 
	    @Override
	    public int compare(CreditReportingFieldValueChoice o1, CreditReportingFieldValueChoice o2) {
	    	 String s1 = o1.getValue().trim().toUpperCase();
	    	    String s2 = o2.getValue().trim().toUpperCase();
	    	    return s1.compareTo(s2);
	    }
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

	
	public String getEditCreditReportingFieldTemplate() {
		return editCreditReportingFieldTemplate;
	}

	
	public void setEditCreditReportingFieldTemplate(
			String defaultReportingFieldTemplate) {
		this.editCreditReportingFieldTemplate = defaultReportingFieldTemplate;
	}

	
	public String getListCreditReportingFieldTemplate() {
		return listCreditReportingFieldTemplate;
	}

	public void setListCreditReportingFieldTemplate(
			String listCreditReportingFieldTemplate) {
		this.listCreditReportingFieldTemplate = listCreditReportingFieldTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getListCreditReportingFieldApprovalsTemplate() {
		return listCreditReportingFieldApprovalsTemplate;
	}

	public void setListCreditReportingFieldApprovalsTemplate(
			String listCreditReportingFieldApprovalsTemplate) {
		this.listCreditReportingFieldApprovalsTemplate = listCreditReportingFieldApprovalsTemplate;
	}

	public String getListCreditReportingFieldCategoriesTemplate() {
		return listCreditReportingFieldCategoriesTemplate;
	}

	public void setListCreditReportingFieldCategoriesTemplate(
			String listCreditReportingFieldCategoriesTemplate) {
		this.listCreditReportingFieldCategoriesTemplate = listCreditReportingFieldCategoriesTemplate;
	}
	
}
