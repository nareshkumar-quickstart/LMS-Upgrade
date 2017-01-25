package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Modality;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCategoryForm;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.Accreditation.RegulatorCategoryValidator;
import com.softech.vu360.util.CustomFieldEntityType;

public class AddRegulatorCategoryWizardController extends AbstractWizardFormController {

    private static final Logger log = Logger.getLogger(AddRegulatorWizardController.class.getName());
    private AccreditationService accreditationService;
    private String cancelTemplate = null;
    private String finishTemplate = null;
    public static final String CUSTOMFIELD_ENTITY_REGULATOR = "CUSTOMFIELD_REGULATOR";

    public AddRegulatorCategoryWizardController() {
        super();
        setCommandName("regulatorCategoryForm");
        setCommandClass(RegulatorCategoryForm.class);
        setSessionForm(true);
        this.setBindOnNewForm(true);
        setPages(new String[]{
                    "accreditation/regulator/category/addCategory/addRegulatorCategoryStep1",
                    "accreditation/regulator/category/addCategory/addRegulatorCategoryStep2",
        });
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Object command = super.formBackingObject(request);
        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        logger.debug("regulator id: " + request.getAttribute("regulatorId"));
        //TODO: Can't seem to get the regulaorId from the request.
//            int regulatorId = Integer.valueOf(String.valueOf(request.getAttribute("regulatorId")));
        String regulatorId = request.getParameter("regulatorId");
        form.setRegulator(accreditationService.loadForUpdateRegulator(Long.valueOf(regulatorId)));
        logger.debug("regulator name: " + form.getRegulator().getName());
        
        return command;
    }
    
    private RegulatorCategoryForm setModalities(HttpServletRequest request,RegulatorCategoryForm form){
    	Set<Modality> modalitiesAllowed = new HashSet<Modality>();
        String[] selectedValues = request.getParameterValues("modalities");
        //Set<Modality> modalities= accreditationService.getAllModalities();
        List<Modality> modalities= accreditationService.getAllModalities();
        if(selectedValues!=null)
        for (int i = 0; i < selectedValues.length; i++) {
           for(Modality modality:modalities){
        	   if(selectedValues[i].trim().equalsIgnoreCase(modality.getName())){
        		   modalitiesAllowed.add(modality);
        	   }
           } 
        	
        }
        form.setModalitiesAllowed(modalitiesAllowed);
        return form;
    }
    
    @SuppressWarnings("unchecked")
    protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
            int page) throws Exception {

        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        switch (page) {

            case 0:
                if (!errors.hasErrors() && form.getCustomFields().isEmpty()) {
                    List<CustomField> globalCustomFieldList = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_REGULATOR), "", "");
                    CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
                    List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();

                    for (CustomField customField : globalCustomFieldList) {
                        if (customField instanceof SingleSelectCustomField
                                || customField instanceof MultiSelectCustomField) {

                            List<CustomFieldValueChoice> customFieldValueChoices = this.getAccreditationService().getOptionsByCustomField(customField);
                            fieldBuilder.buildCustomField(customField, 0, customFieldValues, customFieldValueChoices);

                        } else {
                            fieldBuilder.buildCustomField(customField, 0, customFieldValues);
                        }
                    }

                    List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList = fieldBuilder.getCustomFieldList();
                    form.setCustomFields(customFieldList);
                }
            case 1:
                for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : form.getCustomFields()) {

                    if (customField.getCustomFieldRef() instanceof MultiSelectCustomField) {

                        MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField) customField.getCustomFieldRef();
                        if (!multiSelectCustomField.getCheckBox()) {

                            if (customField.getSelectedChoices() != null) {

                                for (String selectedChoiceIdString : customField.getSelectedChoices()) {
                                    for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()) {
                                        if (selectedChoiceIdString.equalsIgnoreCase(customFieldValueChoice.getCustomFieldValueChoiceRef().getKey())) {
                                            customFieldValueChoice.setSelected(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                form= this.setModalities(request, form);
            case 2:
                break;
            default:
                break;
        }
        
        request.setAttribute("categoryTypes", RegulatorCategory.CATEGORY_TYPES);
        return super.referenceData(request, page);
    }

    protected ModelAndView processFinish(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors) throws Exception {

        log.debug("IN processFinish");
        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        
//        if (form.getCustomFields().size() > 0) {			
//			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
//			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){
//
//				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){
//
//					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
//					if (multiSelectCustomField.isCheckBox()){
//
//						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
//						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
//
//							if (customFieldValueChoice.isSelected()){
//								CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
//								customFieldValueChoices.add(customFieldValueChoiceRef);
//							}
//
//						}
//						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
//						Object value = customFieldValue.getValue();
//						customFieldValue.setCustomField(customField.getCustomFieldRef());
//						/*  @added by Dyutiman
//						 *  for Encryption...
//						 */
//						if( customField.getCustomFieldRef().isFieldEncrypted() ) {
//							customFieldValue.setValue(value);
//						}
//						customFieldValue.setValueItems(customFieldValueChoices);
//						customFieldValues.add(customFieldValue);
//						
//					} else {
//						List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
//						if(customField.getSelectedChoices() != null){
//							Map<Long,CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long,CustomFieldValueChoice>();
//							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
//								customFieldValueChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
//							}
//
//							for(String selectedChoiceIdString : customField.getSelectedChoices()){
//								if(customFieldValueChoiceMap.containsKey(new Long(selectedChoiceIdString))){
//									CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap.get(new Long(selectedChoiceIdString));
//									customFieldValueChoices.add(customFieldValueChoiceRef);
//								}
//							}
//						}
//						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
//						Object value = customFieldValue.getValue();
//						customFieldValue.setCustomField(customField.getCustomFieldRef());
//						customFieldValue.setValueItems(customFieldValueChoices);
//						/*
//						 *  for Encryption...
//						 */
//						if( customField.getCustomFieldRef().isFieldEncrypted() ) {
//							customFieldValue.setValue(value);
//						}
//						customFieldValues.add(customFieldValue);
//					}
//
//				} else {
//					CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
//					Object value = customFieldValue.getValue();
//					customFieldValue.setCustomField(customField.getCustomFieldRef());
//					/*
//					 *  for Encryption...
//					 */
//					if( customField.getCustomFieldRef().isFieldEncrypted() ) {
//						customFieldValue.setValue(value);
//					}
//					customFieldValues.add(customFieldValue);
//				}
//			}
//			if (customFieldValues.size()>0){
//				form.setCustomFieldValues(customFieldValues);
//			}
//		}
        
        log.debug("Saving regulatorCateogry");
        accreditationService.saveRegulatorCategory(form.toRegulatorCategory()); 

        return new ModelAndView(finishTemplate);
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {

        RegulatorCategoryValidator validator = (RegulatorCategoryValidator) this.getValidator();
        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        errors.setNestedPath("");
        switch (page) {

            case 0:
                validator.validate(form, errors);
                if (form.getCustomFields().size() > 0) {
                    //validator.validateCustomFields(form.getCustomFields(), errors);
                }
                
                if(StringUtils.isNotBlank(form.getCategoryType())){
                	if(getAccreditationService().isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(form.getRegulator().getId(), form.getCategoryType(), 0)){
                		errors.rejectValue("categoryType", "error.editRegulatorCategory.categoryTypeAlreadyAssociated");
                	}
                }
                /*
                if(!StringUtils.isNumeric(form.getReportingTimeframeStr())){
                	errors.rejectValue("reportingTimeframeStr", "error.editRegulatorCategory.reportintimefrmNumeric");
                	
                }
                
                if(!StringUtils.isNumeric(form.getValidationFrequencyStr())){
                	errors.rejectValue("validationFrequencyStr", "error.editRegulatorCategory.validationfrequencyNumeric");
                	
                }
                */
                //}	
                break;
            case 1:
                /*if( form.getEventSource().equalsIgnoreCase("true") ) {
                validator.validateSecondPage(form, errors);
                }*/
                break;
            case 2:
                break;
            default:
                break;
        }
        super.validatePage(command, errors, page, finish);
    }

    @Override
    protected ModelAndView processCancel(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        log.debug("IN processCancel");
        return new ModelAndView(cancelTemplate);
    }

    public String getCancelTemplate() {
        return cancelTemplate;
    }

    public void setCancelTemplate(String cancelTemplate) {
        this.cancelTemplate = cancelTemplate;
    }

    public AccreditationService getAccreditationService() {
        return accreditationService;
    }

    public void setAccreditationService(AccreditationService accreditationService) {
        this.accreditationService = accreditationService;
    }

    public String getFinishTemplate() {
        return finishTemplate;
    }

    public void setFinishTemplate(String finishTemplate) {
        this.finishTemplate = finishTemplate;
    }
}