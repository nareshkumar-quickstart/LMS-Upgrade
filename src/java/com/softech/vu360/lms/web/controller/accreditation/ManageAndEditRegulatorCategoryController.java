package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Modality;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCreditReportingfield;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCreditReportingFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCategoryForm;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCredential;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.util.CredentialSort;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.HtmlEncoder;

public class ManageAndEditRegulatorCategoryController extends VU360BaseMultiActionController {

    private String editRegulatorCategoryTemplate = null;
    private String listRegulatorCategoriesTemplate = null;
    private String regCategoryReportingFieldTemplate = null;
    private String listCreditReportingFieldTemplate = null;
    private String listCustomFieldTemplate = null;
    private String closeTemplate = null;
    private String finishTemplate = null;
    private AccreditationService accreditationService;
    private static final Logger log = Logger.getLogger(ManageAndEditRegulatorCategoryController.class.getName());

    @Override
    protected void onBind(HttpServletRequest request, Object command,
            String methodName) throws Exception {
        if (command instanceof RegulatorCategoryForm) {
        	RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        	Long id= null;
            if (methodName.equals("editRegulatorCategory")) {
                String idStr = request.getParameter("id");
                
                if(idStr!=null && idStr.equalsIgnoreCase("null")==false && idStr.length()>0){
                   id= Long.parseLong(idStr);
                }
                RegulatorCategory regulatorCategory = accreditationService.loadForUpdateRegulatorCategory(id);                    
                form.loadRegulatorCategory(regulatorCategory);
            }
            if (methodName.equals("listRegulatorCategoryCustomFields")){                
                id= form.getId();                             
                RegulatorCategory regulatorCategory = accreditationService.loadForUpdateRegulatorCategory(id);                    
                form.loadRegulatorCategory(regulatorCategory);
            }            
            
            if( methodName.equals("editRegulatorCategory")){

				RegulatorCategory regulatorCategory = form.getCategory();
				if (regulatorCategory != null){

					//if (form.getCustomFields().size()!=credential.getCustomfields().size()){
					CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
					List<CustomFieldValue> customFieldValues = regulatorCategory.getCustomFieldValues();
					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
					List<CustomField> regulatorCustomFieldList = regulatorCategory.getCustomFields();
					//List<CustomField> globalCustomFieldList  = this.getAccreditationService().findGlobalsCustomFields(Enum.valueOf(CustomFieldEntityType.class, CUSTOMFIELD_ENTITY_REGULATOR_CATEGORY), "", "");

					/*for (CustomField globalCustomField : globalCustomFieldList){
						totalCustomFieldList.add(globalCustomField);
					}*/
					for (CustomField customField : regulatorCustomFieldList){
						totalCustomFieldList.add(customField);
					}

					Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

					for(CustomField customField:totalCustomFieldList){
						if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
							List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
							fieldBuilder.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

							if (customField instanceof MultiSelectCustomField){
								CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
								existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
							}

						}else {
							fieldBuilder.buildCustomField(customField,0,customFieldValues);
						}
					}

					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();

					for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList){
						if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){
							List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
							Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

							for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
								tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
							}

							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
								if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
									customFieldValueChoice.setSelected(true);
								}
							}
						}
					}
					form.setCustomFields(customFieldList);
				}
			}
            
        }
    }

    private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField,List<CustomFieldValue> customFieldValues){
		if (customFieldValues != null){
			for (CustomFieldValue customFieldValue : customFieldValues){
				if (customFieldValue.getCustomField()!=null){
					if (customFieldValue.getCustomField().getId().compareTo(customField.getId())==0){
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}
    
    @Override
    protected void validate(HttpServletRequest request, Object command,
            BindException errors, String methodName) throws Exception {
        getValidator().validate(command, errors);
    }

    public ModelAndView editRegulatorCategory(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        
        ModelAndView mnv = new ModelAndView(editRegulatorCategoryTemplate);
        mnv.addObject("categoryTypes", RegulatorCategory.CATEGORY_TYPES);
        
        return mnv;
    }

    @SuppressWarnings("static-access")
    public ModelAndView showRegulatorCategoryReportingFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        Map<Object, Object> context = new HashMap<Object, Object>();
        List<CreditReportingField> creditReportingFieldList = null;
        int showAllReportingFieldsForSelection = Integer.parseInt((request.getParameter("showAllReportingFields") == null) ? "0" : request.getParameter("showAllReportingFields"));

        creditReportingFieldList = form.getReportingFields();

        form = assignReportingFieldType(request, form, context, creditReportingFieldList);
        return new ModelAndView(regCategoryReportingFieldTemplate, "context", context);
    }

    @SuppressWarnings("static-access")
	public ModelAndView listRegulatorCategoryCustomFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
 
		RegulatorCategoryForm form = (RegulatorCategoryForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		form.setManageCustomFields(null);
		List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>(); 
		if (form.getCategory().getCustomFields() != null && form.getCategory().getCustomFields().size() > 0) {
			for (CustomField customField : form.getCategory().getCustomFields()) {
				ManageCustomField manageCustomField = new ManageCustomField();
				manageCustomField.setFieldName(customField.getFieldLabel());
				manageCustomField.setId(customField.getId());
				if (customField instanceof SingleLineTextCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
				} else if (customField instanceof DateTimeCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_DATE);
				} else if (customField instanceof MultipleLineTextCustomfield) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
				} else if (customField instanceof NumericCusomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_NUMBER);
				} else if (customField instanceof SSNCustomFiled) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
				} else if (customField instanceof SingleSelectCustomField) {
					manageCustomField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
				} else if (customField instanceof MultiSelectCustomField) {
					if (((MultiSelectCustomField) customField).getCheckBox())
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
					else 
						manageCustomField.setFieldType(form.CUSTOMFIELD_CHOOSE);
				} 
				manageCustomFieldList.add(manageCustomField);
			}
			manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);
			form.setManageCustomFields(manageCustomFieldList);
		}
		return new ModelAndView(listCustomFieldTemplate);
	}

    public ModelAndView deleteREgulatorCategoriesCustomFields( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		RegulatorCategoryForm form = (RegulatorCategoryForm)command;
		String[] selectedCustomFieldId = request.getParameterValues("customField");
		RegulatorCategory regCat = form.getCategory();
		List<CustomField> customFields = regCat.getCustomFields();
		for ( int i = 0 ; i < selectedCustomFieldId.length ; i++ ) {
			for ( int j = 0 ; j < customFields.size() ; j++ ) {
				if ( customFields.get(j).getId().compareTo(Long.parseLong(selectedCustomFieldId[i])) == 0 ) {
					customFields.remove(j);
					break;
				}
			}
		}
		regCat.setCustomFields(customFields);
		accreditationService.saveRegulatorCategory(regCat);

		//context.put("target", "listCustomFieldTemplate");
		return listRegulatorCategoryCustomFields(request, response, command, errors);//new ModelAndView(listCustomFieldTemplate, "context", context);
	}
    @SuppressWarnings("static-access")
    public ModelAndView showReportingFieldsForSelection(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        Map<Object, Object> context = new HashMap<Object, Object>();
        List<CreditReportingField> creditReportingFieldList = null;
        
        //@MariumSaud : ContentOwner argument is not used by the method accreditationService.getAllCreditReportingFields implementation
        //VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
        
        String fieldName = (request.getParameter("fieldName") == null) ? "" : request.getParameter("fieldName");
        String fieldType = (request.getParameter("fieldType") == null || request.getParameter("fieldType").equalsIgnoreCase("All")) ? "" : request.getParameter("fieldType");
        creditReportingFieldList = accreditationService.getAllCreditReportingFields(fieldName, fieldType);

        //Remove reporting fields that are already assigned
        creditReportingFieldList.removeAll(form.getReportingFields());

        form = this.assignReportingFieldType(request, form, context, creditReportingFieldList);
        return new ModelAndView(listCreditReportingFieldTemplate, "context", context);
    }

    /**
     * @param request
     * @param form
     * @param context
     * @param creditReportingFieldList
     * @return 
     * @throws Exception
     */
    private RegulatorCategoryForm assignReportingFieldType(HttpServletRequest request,
            RegulatorCategoryForm form, Map<Object, Object> context,
            List<CreditReportingField> creditReportingFieldList)
            throws Exception {
        List<ManageCustomField> manageReportingFieldList = new ArrayList<ManageCustomField>();
        if (creditReportingFieldList != null && creditReportingFieldList.size() > 0) {
            for (CreditReportingField reportingField : creditReportingFieldList) {
                ManageCustomField manageReportingField = new ManageCustomField();
                manageReportingField.setFieldName(reportingField.getFieldLabel());
                manageReportingField.setId(reportingField.getId());
                if (reportingField instanceof SingleLineTextCreditReportingFiled) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
                } else if (reportingField instanceof DateTimeCreditReportingField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_DATE);
                } else if (reportingField instanceof MultipleLineTextCreditReportingfield) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
                } else if (reportingField instanceof NumericCreditReportingField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_NUMBER);
                } else if (reportingField instanceof SSNCreditReportingFiled) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
                } else if (reportingField instanceof SingleSelectCreditReportingField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
                } else if (reportingField instanceof MultiSelectCreditReportingField) {
                    if (((MultiSelectCreditReportingField) reportingField).isCheckBox()) {
                        manageReportingField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
                    } else {
                        manageReportingField.setFieldType(form.CUSTOMFIELD_CHOOSE);
                    }
                } else if (reportingField instanceof StaticCreditReportingField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_STATIC);
                }
                else if (reportingField instanceof TelephoneNumberCreditReportingField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_TELEPHONE_NUMBER);
                }
                manageReportingFieldList.add(manageReportingField);
            }
            manageReportingFieldList = this.sortCustomField(request, manageReportingFieldList, context);

            form.setManageCreditReportingField(manageReportingFieldList);
        } else {
            form.setManageCreditReportingField(manageReportingFieldList);
        }
        return form;
    }
    
    private RegulatorCategoryForm assignCustomFieldType(HttpServletRequest request,
            RegulatorCategoryForm form, Map<Object, Object> context,
            List<CustomField> customFieldList)
            throws Exception {
        List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>();
        
        if (customFieldList != null && customFieldList.size() > 0) {
            for (CustomField customField : customFieldList) {
                ManageCustomField manageReportingField = new ManageCustomField();
                manageReportingField.setFieldName(customField.getFieldLabel());
                manageReportingField.setId(customField.getId());
                
                if (customField instanceof SingleLineTextCustomFiled) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
                } else if (customField instanceof DateTimeCustomField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_DATE);
                } else if (customField instanceof MultipleLineTextCustomfield) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT);
                } else if (customField instanceof NumericCusomField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_NUMBER);
                } else if (customField instanceof SSNCustomFiled) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER);
                } else if (customField instanceof SingleSelectCustomField) {
                    manageReportingField.setFieldType(form.CUSTOMFIELD_RADIO_BUTTON);
                } else if (customField instanceof MultiSelectCustomField) {
                    if (((MultiSelectCustomField) customField).getCheckBox()) {
                        manageReportingField.setFieldType(form.CUSTOMFIELD_CHECK_BOX);
                    } else {
                        manageReportingField.setFieldType(form.CUSTOMFIELD_CHOOSE);
                    }
                } 
                manageCustomFieldList.add(manageReportingField);
            }
            manageCustomFieldList = this.sortCustomField(request, manageCustomFieldList, context);

            form.setManageCustomFields(manageCustomFieldList);
        } else {
            form.setManageCustomFields(manageCustomFieldList);
        }
        return form;
    }

    /**
     * This method is for sorting the custom field.
     * @param request
     * @param manageCustomFieldList
     * @param context
     * @return List<ManageCustomField>
     * @throws Exception
     */
    private List<ManageCustomField> sortCustomField(HttpServletRequest request, List<ManageCustomField> manageCustomFieldList, Map<Object, Object> context) throws Exception {

//		session = request.getSession();
        Map<String, String> PagerAttributeMap = new HashMap<String, String>();

        String pageIndex = request.getParameter("pageIndex");
        if (pageIndex == null) {
            pageIndex = "0";
        }

        PagerAttributeMap.put("pageIndex", pageIndex);
        if (manageCustomFieldList.size() <= 10) {
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
        if (showAll == null) {
            showAll = "false";
        }
        if (showAll.isEmpty()) {
            showAll = "true";
        }
        context.put("showAll", showAll);

        // sorting against officialLicenseName name
        if (sortColumnIndex != null && sortDirection != null) {
            request.setAttribute("PagerAttributeMap", PagerAttributeMap);
            if (sortColumnIndex.equalsIgnoreCase("0")) {
                if (sortDirection.equalsIgnoreCase("0")) {

                    CustomFieldSort customFieldSort = new CustomFieldSort();
                    customFieldSort.setSortBy("fieldLabel");
                    customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
                    Collections.sort(manageCustomFieldList, customFieldSort);
                    context.put("sortDirection", 0);
                    context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
                } else {
                    CustomFieldSort customFieldSort = new CustomFieldSort();
                    customFieldSort.setSortBy("fieldLabel");
                    customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
                    Collections.sort(manageCustomFieldList, customFieldSort);
                    context.put("sortDirection", 1);
                    context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
                }
                // sorting against jurisdiction
            } else if (sortColumnIndex.equalsIgnoreCase("1")) {
                if (sortDirection.equalsIgnoreCase("0")) {

                    CustomFieldSort customFieldSort = new CustomFieldSort();
                    customFieldSort.setSortBy("fieldType");
                    customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
                    Collections.sort(manageCustomFieldList, customFieldSort);
                    context.put("sortDirection", 0);
                    context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
                } else {
                    CustomFieldSort customFieldSort = new CustomFieldSort();
                    customFieldSort.setSortBy("fieldType");
                    customFieldSort.setSortDirection(Integer.parseInt(sortDirection));
                    Collections.sort(manageCustomFieldList, customFieldSort);
                    context.put("sortDirection", 1);
                    context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
                }
            }
        }
        return manageCustomFieldList;
    }

    public ModelAndView assignReportingFieldsToRegulatorCategory(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        //RegulatorCredential category= form.getr R Category();

        Map<Object, Object> context = new HashMap<Object, Object>();

        List<CreditReportingField> reportingFields = new ArrayList<CreditReportingField>();
        String[] selectedReportingFieldId = request.getParameterValues("reportingField");
        for (int i = 0; i < selectedReportingFieldId.length; i++) {
            CreditReportingField reportingField = accreditationService.getCreditReportingFieldById(Long.parseLong(selectedReportingFieldId[i]));
            //List<CreditReportingFieldValue> reportingFieldValues = accreditationService.getCreditReportingFieldValue(reportingField);
            //if ( reportingFieldValues == null || reportingFieldValues.size() == 0 ) {
            reportingFields.add(reportingField);
            /*} else {
            errors.rejectValue("manageCreditReportingField", "error.courseApproval.reportingField.notDelete");
            return new ModelAndView(courserApprovalReportingFieldTemplate, "context", context);
            }*/

        }
        if (reportingFields != null && reportingFields.size() > 0) {
            form.getReportingFields().addAll(reportingFields);
            RegulatorCategory category = accreditationService.loadForUpdateRegulatorCategory(form.getId());
            //form.toRegulatorCategory(category);
            category.setReportingFields(form.getReportingFields());
            accreditationService.saveRegulatorCategoryCreditReporting(category);
        }
//		if (reportingFields != null && reportingFields.size() > 0)
//			accreditationService.removeCreditReportingField(reportingFields);

        //context.put("target", "showRegulatoryCategoryReportingFields");
        return showRegulatorCategoryReportingFields(request, response, command, errors);
    }

    public ModelAndView searchRegulatorCredential(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        try {
//			session = request.getSession();
            Map<Object, Object> context = new HashMap<Object, Object>();
            Map<String, String> PagerAttributeMap = new HashMap<String, String>();

            RegulatorCategoryForm form = (RegulatorCategoryForm) command;
            List<Credential> credentials = form.getRegulator().getCredentials();
            String credentialName = (request.getParameter("credentialName") == null) ? "" : request.getParameter("credentialName");
            String credentialShortName = (request.getParameter("credentialShortName") == null) ? "" : request.getParameter("credentialShortName");

            credentialName = HtmlEncoder.escapeHtmlFull(credentialName).toString();
            context.put("credentialName", credentialName);
            credentialShortName = HtmlEncoder.escapeHtmlFull(credentialShortName).toString();
            context.put("credentialShortName", credentialShortName);

            String pageIndex = request.getParameter("pageIndex");
            if (pageIndex == null) {
                pageIndex = "0";
            }

            PagerAttributeMap.put("pageIndex", pageIndex);
            if (credentials.size() <= 10) {
                PagerAttributeMap.put("pageIndex", "0");
            }
            List<RegulatorCredential> regCredentials = new ArrayList<RegulatorCredential>();
            List<Credential> searchedCredentials = null;
            if (credentials != null && credentials.size() > 0) {
                Long credentialIdArray[] = new Long[credentials.size()];
                int count = 0;
                for (Credential credential : credentials) {
                    credentialIdArray[count] = credential.getId();
                    count = count + 1;
                }
                searchedCredentials = accreditationService.findCredentialInRegulator(credentialName,
                        credentialShortName, credentialIdArray);
            } else {
                context.put("sortDirection", 0);
                context.put("sortColumnIndex", 0);
                //form.setRegCredential(null);
                return new ModelAndView(listRegulatorCategoriesTemplate, "context", context);
            }

            // for sorting...
            String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
            String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
            String showAll = request.getParameter("showAll");
            if (showAll == null) {
                showAll = "false";
            }
            if (showAll.isEmpty()) {
                showAll = "true";
            }
            context.put("showAll", showAll);

            if (sortColumnIndex != null && sortDirection != null) {

                request.setAttribute("PagerAttributeMap", PagerAttributeMap);

                // sorting against officialLicenseName name
                if (sortColumnIndex.equalsIgnoreCase("0")) {
                    if (sortDirection.equalsIgnoreCase("0")) {

                        CredentialSort credentialSort = new CredentialSort();
                        credentialSort.setSortBy("officialLicenseName");
                        credentialSort.setSortDirection(Integer.parseInt(sortDirection));
                        Collections.sort(searchedCredentials, credentialSort);
                        context.put("sortDirection", 0);
                        context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
                    } else {
                        CredentialSort credentialSort = new CredentialSort();
                        credentialSort.setSortBy("officialLicenseName");
                        credentialSort.setSortDirection(Integer.parseInt(sortDirection));
                        Collections.sort(searchedCredentials, credentialSort);
                        context.put("sortDirection", 1);
                        context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
                    }
                    // sorting against jurisdiction
                } else if (sortColumnIndex.equalsIgnoreCase("1")) {
                    if (sortDirection.equalsIgnoreCase("0")) {

                        CredentialSort credentialSort = new CredentialSort();
                        credentialSort.setSortBy("shortLicenseName");
                        credentialSort.setSortDirection(Integer.parseInt(sortDirection));
                        Collections.sort(searchedCredentials, credentialSort);
                        context.put("sortDirection", 0);
                        context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
                    } else {
                        CredentialSort credentialSort = new CredentialSort();
                        credentialSort.setSortBy("shortLicenseName");
                        credentialSort.setSortDirection(Integer.parseInt(sortDirection));
                        Collections.sort(searchedCredentials, credentialSort);
                        context.put("sortDirection", 1);
                        context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
                    }
                }
            }

            for (Credential cred : searchedCredentials) {
                RegulatorCredential regCredential = new RegulatorCredential();
                regCredential.setCredential(cred);
                regCredential.setSelected(false);
                regCredentials.add(regCredential);
            }
            //form.setRegCredential(regCredentials);
            return new ModelAndView(listRegulatorCategoriesTemplate, "context", context);

        } catch (Exception e) {
            log.debug("exception", e);
        }
        return new ModelAndView(listRegulatorCategoriesTemplate);
    }

    /**
     * Saves regulator category in the system.
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return 
     */
    public ModelAndView saveRegulatorCategory(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) {
        log.debug("Saving regulator category. --- " + command);        
        
        RegulatorCategoryForm form = (RegulatorCategoryForm) command;

        if(StringUtils.isNotBlank(form.getCategoryType())){
        	if(getAccreditationService().isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(form.getRegulator().getId(), form.getCategoryType(), form.getId())){
        		errors.rejectValue("categoryType", "error.editRegulatorCategory.categoryTypeAlreadyAssociated");
        	}
        }

        if (errors.hasErrors()) {
            return new ModelAndView(editRegulatorCategoryTemplate).addObject("categoryTypes", RegulatorCategory.CATEGORY_TYPES);
        }

        form = this.setModalities(request, form);
        RegulatorCategory editedRegulatorCategory = form.toRegulatorCategory(accreditationService.loadForUpdateRegulatorCategory(form.getId()));
        
        if( errors.hasErrors() ) {

			CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
			Map<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomField>  customFieldMap = new HashMap<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
			for(com.softech.vu360.lms.web.controller.model.customfield.CustomField tempcustomField:form.getCustomFields()){

				CustomField customField = tempcustomField.getCustomFieldRef();
				List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
				customFieldValues.add(tempcustomField.getCustomFieldValueRef());

				if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField){
					List<CustomFieldValueChoice> customFieldValueChoices=accreditationService.getOptionsByCustomField(customField);
					fieldBuilder.buildCustomField(customField,tempcustomField.getStatus(),customFieldValues,customFieldValueChoices);

					if (customField instanceof MultiSelectCustomField){
						customFieldMap.put(tempcustomField.getCustomFieldRef().getId(), tempcustomField);
					}

				}else {
					customFieldValues.get(0).setValue(HtmlEncoder.escapeHtmlFull(customFieldValues.get(0).getValue().toString()).toString());
					fieldBuilder.buildCustomField(customField,tempcustomField.getStatus(),customFieldValues);
				}
			}

			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList =fieldBuilder.getCustomFieldList();
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldList){
				if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){

					com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField = customFieldMap.get(customField.getCustomFieldRef().getId());
					if (tempCustomField != null){

						Map<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice> tempCustomFieldValueChoiceMap = new HashMap<Long,com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice>();

						for(com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice tempCustomFieldValueChoice:tempCustomField.getCustomFieldValueChoices()){
							tempCustomFieldValueChoiceMap.put(tempCustomFieldValueChoice.getCustomFieldValueChoiceRef().getId(), tempCustomFieldValueChoice);
						}

						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice:customField.getCustomFieldValueChoices()){
							if (tempCustomFieldValueChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
								customFieldValueChoice.setSelected(tempCustomFieldValueChoiceMap.get(customFieldValueChoice.getCustomFieldValueChoiceRef().getId()).isSelected());
							}
						}

					}
				}
			}
			/*
			 * Now re set the multi select option
			 * */
			form.setCategory(editedRegulatorCategory);
			form.setCustomFields(customFieldList);

			return new ModelAndView(editRegulatorCategoryTemplate);
		}

		if (form.getCustomFields().size()>0){
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField :form.getCustomFields()){

				if(customField.getCustomFieldRef() instanceof MultiSelectCustomField){

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField)customField.getCustomFieldRef();
					if (multiSelectCustomField.getCheckBox()){

						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){

							if (customFieldValueChoice.isSelected()){
								CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice.getCustomFieldValueChoiceRef();
								customFieldValueChoices.add(customFieldValueChoiceRef);
							}

						}
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);
					} else {
						List<CustomFieldValueChoice> customFieldValueChoices=new ArrayList<CustomFieldValueChoice>();
						if(customField.getSelectedChoices()!=null){
							Map<Long,CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long,CustomFieldValueChoice>();
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : customField.getCustomFieldValueChoices()){
								customFieldValueChoiceMap.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(), customFieldValueChoice.getCustomFieldValueChoiceRef());
							}

							for(String selectedChoiceIdString : customField.getSelectedChoices()){
								if(customFieldValueChoiceMap.containsKey(new Long(selectedChoiceIdString))){
									CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap.get(new Long(selectedChoiceIdString));
									customFieldValueChoices.add(customFieldValueChoiceRef);
								}
							}
						}
						CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);
						
						customFieldValues.add(customFieldValue);
					}

				} else {
					CustomFieldValue customFieldValue = customField.getCustomFieldValueRef();
					customFieldValue.setCustomField(customField.getCustomFieldRef());
					if(customFieldValue.getValue()!=null){
						customFieldValue.setValue(customFieldValue.getValue().toString());
					}
					customFieldValues.add(customFieldValue);
				}
			}
			if (customFieldValues.size()>0){
				editedRegulatorCategory.setCustomFieldValues(customFieldValues);
			}
		}
        accreditationService.saveRegulatorCategory(editedRegulatorCategory);

        return new ModelAndView(finishTemplate);
    }

    private RegulatorCategoryForm setModalities(HttpServletRequest request, RegulatorCategoryForm form) {
        Set<Modality> modalitiesAllowed = new HashSet<Modality>();
        String[] selectedValues = request.getParameterValues("modalities");
        //Set<Modality> modalities = accreditationService.getAllModalities();
        List<Modality> modalities = accreditationService.getAllModalities();
        if (selectedValues != null) {
            for (int i = 0; i < selectedValues.length; i++) {
                for (Modality modality : modalities) {
                    if (selectedValues[i].trim().equalsIgnoreCase(modality.getName())) {
                        modalitiesAllowed.add(modality);
                    }
                }

            }
        }
        form.setModalitiesAllowed(modalitiesAllowed);
        return form;
    }

    /**
     * This method delete the credit reporting field.
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView unAssignReportingFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RegulatorCategoryForm form = (RegulatorCategoryForm) command;
        //CredentialCategory credentialCategory= form.getCategory();

        Map<Object, Object> context = new HashMap<Object, Object>();
        /*if(errors.hasErrors()){
        return new ModelAndView(categoryReportingFieldTemplate, "context", context);
        }*/
        List<CreditReportingField> reportingFields = new ArrayList<CreditReportingField>();
        String[] selectedReportingFieldId = request.getParameterValues("reportingField");
        for (int i = 0; i < selectedReportingFieldId.length; i++) {
            CreditReportingField reportingField = accreditationService.getCreditReportingFieldById(Long.parseLong(selectedReportingFieldId[i]));
            reportingFields.add(reportingField);
        }

        if (reportingFields != null && reportingFields.size() > 0) {
            form.getReportingFields().removeAll(reportingFields);
            RegulatorCategory category = accreditationService.loadForUpdateRegulatorCategory(form.getId());
            category.setReportingFields(form.getReportingFields());
            accreditationService.dropRegulatorCategoryCreditReporting(category);
        }

        //context.put("target", "showCredentialCategoryReportingFields");
        return showRegulatorCategoryReportingFields(request, response, command, errors);
    }

    public String getEditRegulatorCategoryTemplate() {
        return editRegulatorCategoryTemplate;
    }

    public void setEditRegulatorCategoryTemplate(
            String editRegulatorCategoryTemplate) {
        this.editRegulatorCategoryTemplate = editRegulatorCategoryTemplate;
    }

    public String getListRegulatorCategoriesTemplate() {
        return listRegulatorCategoriesTemplate;
    }

    public void setListRegulatorCategoriesTemplate(
            String listRegulatorCategoriesTemplate) {
        this.listRegulatorCategoriesTemplate = listRegulatorCategoriesTemplate;
    }

    public String getRegCategoryReportingFieldTemplate() {
        return regCategoryReportingFieldTemplate;
    }

    public void setRegCategoryReportingFieldTemplate(
            String regCategoryReportingFieldTemplate) {
        this.regCategoryReportingFieldTemplate = regCategoryReportingFieldTemplate;
    }

    public String getListCreditReportingFieldTemplate() {
        return listCreditReportingFieldTemplate;
    }

    public void setListCreditReportingFieldTemplate(
            String listCreditReportingFieldTemplate) {
        this.listCreditReportingFieldTemplate = listCreditReportingFieldTemplate;
    }

    public String getCloseTemplate() {
        return closeTemplate;
    }

    public void setCloseTemplate(String closeTemplate) {
        this.closeTemplate = closeTemplate;
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

    /**
     * @param listCustomFieldTemplate the listCustomFieldTemplate to set
     */
    public void setListCustomFieldTemplate(String listCustomFieldTemplate) {
        this.listCustomFieldTemplate = listCustomFieldTemplate;
    }
}
