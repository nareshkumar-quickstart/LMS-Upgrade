package com.softech.vu360.lms.web.controller.accreditation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldContext;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.administrator.SearchMemberController;
import com.softech.vu360.lms.web.controller.model.accreditation.CustomFieldForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CustomFieldValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddCustomFieldWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger
			.getLogger(AddCustomFieldWizardController.class.getName());
    private AccreditationService accreditationService = null;
    private String closeCredentialTemplate = null;
    private String closeCredentialRequirementTemplate = null;
    private String closeRegulatorTemplate = null;
    private String closeApprovalTemplate = null;
    private String closeProviderTemplate = null;
    private String closeInstructorTemplate = null;
    public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
    public static final String CUSTOMFIELD_DATE = "Date";
    public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
    public static final String CUSTOMFIELD_NUMBER = "Number";
    public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
    public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
    public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
    public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
    private static final String[] CUSTOMFIELD_TYPES = {
        CUSTOMFIELD_SINGLE_LINE_OF_TEXT, CUSTOMFIELD_DATE, CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT, CUSTOMFIELD_NUMBER, CUSTOMFIELD_RADIO_BUTTON, CUSTOMFIELD_CHOOSE, CUSTOMFIELD_CHECK_BOX, CUSTOMFIELD_SOCIAL_SECURITY_NUMBER
    };

    public AddCustomFieldWizardController() {
        super();
        setCommandName("customFieldForm");
        setCommandClass(CustomFieldForm.class);
        setSessionForm(true);
        this.setBindOnNewForm(true);
        setPages(new String[]{
                    "accreditation/addCustomField/add_customfield_credentials", 
                    "accreditation/addCustomField/add_customfield_details_container", 
                    "accreditation/addCustomField/add_customfield_confirm"
                });
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @SuppressWarnings("static-access")
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Object command = super.formBackingObject(request);
        CustomFieldForm form = (CustomFieldForm) command;
        CustomField customField = new CustomField();
        form.setCustomField(customField);
        form.setFieldType(CUSTOMFIELD_SINGLE_LINE_OF_TEXT);
        if (request.getParameter("entity") != null) {
            form.setEntity(request.getParameter("entity"));
        }
        
        if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
            long credentialID = Long.parseLong(request.getParameter("credentialID"));
            Credential credential = accreditationService.loadForUpdateCredential(credentialID);
            form.setCredential(credential);
        } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
            long credentialReqID = Long.parseLong(request.getParameter("credentialRequirementId"));
            CredentialCategoryRequirement credentialCategoryRequirement = accreditationService.
                    loadForUpdateCredentialCategoryRequirement(credentialReqID);
            form.setCredentialRequirement(credentialCategoryRequirement);
            
        } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
            long regulatorID = Long.parseLong(request.getParameter("regulatorID"));
            Regulator regulator = accreditationService.loadForUpdateRegulator(regulatorID);
            form.setRegulator(regulator);
        } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
            String regCatIdStr = request.getParameter("regulatorCategoryId");
            if (StringUtils.isBlank(regCatIdStr)) {
                throw new IllegalArgumentException("Please provide regulator cateogry id.");
            }
            long regulatorCategoryID = Long.parseLong(regCatIdStr);
            RegulatorCategory regulatorCategory = accreditationService.loadForUpdateRegulatorCategory(
                    regulatorCategoryID);
            form.setRegulatorCategory(regulatorCategory);
        } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
            long providerApprovalId = Long.parseLong(request.getParameter("appID"));
            ProviderApproval providerApproval = accreditationService.loadForUpdateProviderApproval(providerApprovalId);
            form.setProviderApproval(providerApproval);
        } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
            long instructorApprovalId = Long.parseLong(request.getParameter("appID"));
            InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(instructorApprovalId);
            form.setInstructorApproval(instructorApproval);
        } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
            long courseApprovalId = Long.parseLong(request.getParameter("appID"));
            CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(courseApprovalId);
            form.setCourseApproval(courseApproval);
        } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
            long providerID = Long.parseLong(request.getParameter("providerID"));
            Provider provider = accreditationService.loadForUpdateProvider(providerID);
            form.setProvider(provider);

        } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
            long instructorID = Long.parseLong(request.getParameter("instructorID"));
            Instructor instructor = accreditationService.loadForUpdateInstructor(instructorID);
            form.setInstructor(instructor);
        } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) { // [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
            Long categoryID = Long.valueOf(request.getParameter("entityId"));
            CredentialCategory category = this.accreditationService.loadForUpdateCredentialCategory(categoryID);
            form.setCredentialCategory(category);
        }

        
        return command;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
     */
    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        CustomFieldForm form = (CustomFieldForm) command;
        if (this.getTargetPage(request, page) == 2) {
            if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)
                    || form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)
                    || form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
                if (!StringUtils.isBlank(form.getOption())) {
                    this.readOptions(form);
                }
            }
        }
        super.onBindAndValidate(request, command, errors, page);
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
     */
    @Override
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
        return super.getTargetPage(request, command, errors, currentPage);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
     */
    @Override
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        super.postProcessPage(request, command, errors, page);
    }

    private void readOptions(CustomFieldForm form) {
        String str;
        BufferedReader reader = new BufferedReader(new StringReader(form.getOption()));

        try {
            List<String> optionList = new ArrayList<String>();
            while ((str = reader.readLine()) != null) {
                if (str.length() > 0) {
                    if (!StringUtils.isBlank(str)) {
                        optionList.add(str);
                    }
                }
            }
            form.setOptionList(optionList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @SuppressWarnings("static-access")
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        CustomFieldForm form = (CustomFieldForm) command;
        Map<Object, Object> context = new HashMap<Object, Object>();
        if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
            return new ModelAndView(closeCredentialTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
            return new ModelAndView(closeCredentialRequirementTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
            return new ModelAndView(closeRegulatorTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
            context.put("target", "showProviderApprovalCustomField");
            return new ModelAndView(closeApprovalTemplate, "context", context);
        } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
            context.put("target", "showInstructorApprovalCustomField");
            return new ModelAndView(closeApprovalTemplate, "context", context);
        } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
            context.put("target", "showCourseApprovalCustomField");
            return new ModelAndView(closeApprovalTemplate, "context", context);
        } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
            return new ModelAndView(closeProviderTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
            return new ModelAndView(closeInstructorTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
            return new ModelAndView("redirect:/acc_manageCredential.do?method=showCredentialCategoryCustomField");
        } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
            return new ModelAndView("redirect:/acc_manageRegulatorCategory.do?method=listRegulatorCategoryCustomFields");
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @SuppressWarnings("static-access")
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
            throws Exception {

        CustomFieldForm form = (CustomFieldForm) command;
        CustomField customField = form.getCustomField();

       // to Avoid LazyLoading exception for all type of entity in Form. 
        form = initializeFormEntity(form);
        
//        /*Fixed to solve lazy initialization exception --*/
//        VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();//(VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // parameter type of findContentOwnerByRegulatoryAnalyst() is changed so creating second user object of type VO.
        // first user object is also necessary for further operation
        com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ContentOwner contentOwner = loggedInUserVO.getContentOwner()!=null ? accreditationService.findContentOwnerById(loggedInUserVO.getContentOwner().getId()) : accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUserVO.getRegulatoryAnalyst()) ;
        CustomFieldContext customFieldContext = new CustomFieldContext();
        customFieldContext.setContentOwner(contentOwner);
        if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
            SingleLineTextCustomFiled singleLineTextCustomFiled = new SingleLineTextCustomFiled();
            singleLineTextCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
            singleLineTextCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
            singleLineTextCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
            singleLineTextCustomFiled.setFieldRequired(customField.getFieldRequired());
            singleLineTextCustomFiled.setCustomFieldContext(customFieldContext);

            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
            	form.getRegulator().getCustomfields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(singleLineTextCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(singleLineTextCustomFiled);
            }

        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_DATE)) {
            DateTimeCustomField dateTimeCustomField = new DateTimeCustomField();
            dateTimeCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
            dateTimeCustomField.setFieldEncrypted(customField.getFieldEncrypted());
            dateTimeCustomField.setFieldLabel(customField.getFieldLabel().trim());
            dateTimeCustomField.setFieldRequired(customField.getFieldRequired());
            dateTimeCustomField.setCustomFieldContext(customFieldContext);
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(dateTimeCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(dateTimeCustomField);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(dateTimeCustomField);
            }
        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
            MultipleLineTextCustomfield multipleLineTextCustomfield = new MultipleLineTextCustomfield();
            multipleLineTextCustomfield.setCustomFieldDescription(customField.getCustomFieldDescription());
            multipleLineTextCustomfield.setFieldEncrypted(customField.getFieldEncrypted());
            multipleLineTextCustomfield.setFieldLabel(customField.getFieldLabel().trim());
            multipleLineTextCustomfield.setFieldRequired(customField.getFieldRequired());
            multipleLineTextCustomfield.setCustomFieldContext(customFieldContext);
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(multipleLineTextCustomfield);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(multipleLineTextCustomfield);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(multipleLineTextCustomfield);
            }
        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_NUMBER)) {
            NumericCusomField numericCusomField = new NumericCusomField();
            numericCusomField.setCustomFieldDescription(customField.getCustomFieldDescription());
            numericCusomField.setFieldEncrypted(customField.getFieldEncrypted());
            numericCusomField.setFieldLabel(customField.getFieldLabel().trim());
            numericCusomField.setFieldRequired(customField.getFieldRequired());
            numericCusomField.setCustomFieldContext(customFieldContext);
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(numericCusomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(numericCusomField);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(numericCusomField);
            }
        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
            SSNCustomFiled ssnCustomFiled = new SSNCustomFiled();
            ssnCustomFiled.setCustomFieldDescription(customField.getCustomFieldDescription());
            ssnCustomFiled.setFieldEncrypted(customField.getFieldEncrypted());
            ssnCustomFiled.setFieldLabel(customField.getFieldLabel().trim());
            ssnCustomFiled.setFieldRequired(customField.getFieldRequired());
            ssnCustomFiled.setCustomFieldContext(customFieldContext);
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(ssnCustomFiled);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(ssnCustomFiled);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(ssnCustomFiled);
            }
        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_RADIO_BUTTON)) {
            SingleSelectCustomField singleSelectCustomField = new SingleSelectCustomField();
            singleSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
            singleSelectCustomField.setFieldRequired(customField.getFieldRequired());
            singleSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
            singleSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
            singleSelectCustomField.setCustomFieldContext(customFieldContext);
            if (form.isAlignment()) {
                singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
            } else {
                singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
            }
            for (int i = 0; i < form.getOptionList().size(); i++) {
                CustomFieldValueChoice option = new CustomFieldValueChoice();
                option.setDisplayOrder(i);
                option.setLabel(form.getOptionList().get(i));
                option.setValue(form.getOptionList().get(i));
                option.setCustomField(singleSelectCustomField);
                accreditationService.saveOption(option);
            }
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(singleSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(singleSelectCustomField);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(singleSelectCustomField);
            }
        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHOOSE)) {
            MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
            multiSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
            multiSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
            multiSelectCustomField.setFieldRequired(customField.getFieldRequired());
            multiSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
            multiSelectCustomField.setCustomFieldContext(customFieldContext);
            multiSelectCustomField.setCheckBox(false);
            if (form.isAlignment()) {
                multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
            } else {
                multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
            }
            for (int i = 0; i < form.getOptionList().size(); i++) {
                CustomFieldValueChoice option = new CustomFieldValueChoice();
                option.setDisplayOrder(i);
                option.setLabel(form.getOptionList().get(i));
                option.setValue(form.getOptionList().get(i));
                option.setCustomField(multiSelectCustomField);
                accreditationService.saveOption(option);
            }
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(multiSelectCustomField);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(multiSelectCustomField);
            }
        } else if (form.getFieldType().equalsIgnoreCase(CUSTOMFIELD_CHECK_BOX)) {
            MultiSelectCustomField multiSelectCustomField = new MultiSelectCustomField();
            multiSelectCustomField.setCustomFieldDescription(customField.getCustomFieldDescription());
            multiSelectCustomField.setFieldEncrypted(customField.getFieldEncrypted());
            multiSelectCustomField.setFieldRequired(customField.getFieldRequired());
            multiSelectCustomField.setFieldLabel(customField.getFieldLabel().trim());
            multiSelectCustomField.setCustomFieldContext(customFieldContext);
            multiSelectCustomField.setCheckBox(true);
            if (form.isAlignment()) {
                multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
            } else {
                multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
            }
            for (int i = 0; i < form.getOptionList().size(); i++) {
                CustomFieldValueChoice option = new CustomFieldValueChoice();
                option.setDisplayOrder(i);
                option.setLabel(form.getOptionList().get(i));
                option.setValue(form.getOptionList().get(i));
                option.setCustomField(multiSelectCustomField);
                accreditationService.saveOption(option);
            }
            if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
                form.getCredential().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
                form.getCredentialRequirement().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
                form.getRegulator().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
                form.getProviderApproval().getCustomFields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
                form.getInstructorApproval().getCustomFields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
                form.getCourseApproval().getCustomFields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
                form.getProvider().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
                form.getInstructor().getCustomfields().add(multiSelectCustomField);
            } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
                form.getCredentialCategory().getCustomFields().add(multiSelectCustomField);
            }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
                form.getRegulatorCategory().getCustomFields().add(multiSelectCustomField);
            }
        }

        Map<Object, Object> context = new HashMap<Object, Object>();
        if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL)) {
            accreditationService.saveCredential(form.getCredential());
            return new ModelAndView(closeCredentialTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_REQUIREMENT)) {
            accreditationService.saveCredentialCategoryRequirement(form.getCredentialRequirement());
            return new ModelAndView(closeCredentialRequirementTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.REGULATOR)) {
            accreditationService.saveRegulator(form.getRegulator());
            return new ModelAndView(closeRegulatorTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER_APPROVAL)) {
            accreditationService.saveProviderApproval(form.getProviderApproval());
            context.put("target", "showProviderApprovalCustomField");
            return new ModelAndView(closeApprovalTemplate, "context", context);
        } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR_APPROVAL)) {
            accreditationService.saveInstructorApproval(form.getInstructorApproval());
            context.put("target", "showInstructorApprovalCustomField");
            return new ModelAndView(closeApprovalTemplate, "context", context);
        } else if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
            accreditationService.saveCourseApproval(form.getCourseApproval());
            context.put("target", "showCourseApprovalCustomField");
            return new ModelAndView(closeApprovalTemplate, "context", context);
        } else if (form.getEntity().equalsIgnoreCase(form.PROVIDER)) {
            accreditationService.saveProvider(form.getProvider());
            return new ModelAndView(closeProviderTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.INSTRUCTOR)) {
            accreditationService.saveInstructor(form.getInstructor());
            return new ModelAndView(closeInstructorTemplate);
        } else if (form.getEntity().equalsIgnoreCase(form.CREDENTIAL_CATEGORY)) {
            accreditationService.saveCredentialCategory(form.getCredentialCategory());
            return new ModelAndView("redirect:acc_manageCredential.do?method=showCredentialCategoryCustomField");
        }else if (form.getEntity().equalsIgnoreCase(form.REGULATOR_CATEGORY)) {
            accreditationService.saveRegulatorCategory(form.getRegulatorCategory());
            return new ModelAndView("redirect:acc_manageRegulatorCategory.do?method=listRegulatorCategoryCustomFields");
        }
        
        
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
     */
    @SuppressWarnings("unchecked")
    protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<Object, Object> model = new HashMap<Object, Object>();
        switch (page) {
            case 0:
                model.put("customFieldTypes", CUSTOMFIELD_TYPES);
                return model;
            case 1:
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
        CustomFieldValidator validator = (CustomFieldValidator) this.getValidator();
        CustomFieldForm form = (CustomFieldForm) command;
        errors.setNestedPath("");
        switch (page) {
            case 0:
                break;
            case 1:
                validator.validateAddCustomFieldPage(form, errors);
                break;
            case 2:
                break;
        }
        super.validatePage(command, errors, page, finish);
    }

    //Method Added By Marium Saud.
    //LMS-19278 -- Inorder to avoid 'could not initialize proxy - no Session' issue a centralized method is created that will set Entity in form.
    
    /*
     * @Kaunain - made certain changes. A coding bug that providerapproval was being fetched instead of provider.
     * static fields were being fetched from object references instead of Classname
     * **/
    private CustomFieldForm initializeFormEntity(CustomFieldForm form){
    	try{
    		if (form.getEntity().equalsIgnoreCase(CustomFieldForm.CREDENTIAL)) {
                Credential credential = accreditationService.loadForUpdateCredential(form.getCredential().getId());
                form.setCredential(credential);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.CREDENTIAL_REQUIREMENT)) {
                CredentialCategoryRequirement credentialCategoryRequirement = accreditationService.
                        loadForUpdateCredentialCategoryRequirement(form.getCredentialRequirement().getId());
                form.setCredentialRequirement(credentialCategoryRequirement);
                
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.REGULATOR)) {
                Regulator regulator = accreditationService.loadForUpdateRegulator(form.getRegulator().getId());
                form.setRegulator(regulator);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.REGULATOR_CATEGORY)) {
                String regCatIdStr = String.valueOf(form.getRegulatorCategory().getId());
                if (StringUtils.isBlank(regCatIdStr)) {
                    throw new IllegalArgumentException("Please provide regulator cateogry id.");
                }
                long regulatorCategoryID = Long.parseLong(regCatIdStr);
                RegulatorCategory regulatorCategory = accreditationService.loadForUpdateRegulatorCategory(
                        regulatorCategoryID);
                form.setRegulatorCategory(regulatorCategory);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.PROVIDER_APPROVAL)) {
                ProviderApproval providerApproval = accreditationService.loadForUpdateProviderApproval(form.getProviderApproval().getId());
                form.setProviderApproval(providerApproval);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.INSTRUCTOR_APPROVAL)) {
                InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(form.getInstructorApproval().getId());
                form.setInstructorApproval(instructorApproval);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.COURSE_APPROVAL)) {
                CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(form.getCourseApproval().getId());
                form.setCourseApproval(courseApproval);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.PROVIDER)) {
                Provider provider = accreditationService.getProviderById(form.getProvider().getId());
                form.setProvider(provider);

            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.INSTRUCTOR)) {
                Instructor instructor = accreditationService.loadForUpdateInstructor(form.getInstructor().getId());
                form.setInstructor(instructor);
            } else if (form.getEntity().equalsIgnoreCase(CustomFieldForm.CREDENTIAL_CATEGORY)) { // [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Custom Fields
                CredentialCategory category = this.accreditationService.loadForUpdateCredentialCategory(form.getCredentialCategory().getId());
                form.setCredentialCategory(category);
            }
    	}
    	
    	catch(Exception ex){
    		log.debug(ex);
    	}
    	
    	return form;
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

    /**
     * @return the closeCredentialTemplate
     */
    public String getCloseCredentialTemplate() {
        return closeCredentialTemplate;
    }

    /**
     * @param closeCredentialTemplate the closeCredentialTemplate to set
     */
    public void setCloseCredentialTemplate(String closeCredentialTemplate) {
        this.closeCredentialTemplate = closeCredentialTemplate;
    }

    /**
     * @return the closeRegulatorTemplate
     */
    public String getCloseRegulatorTemplate() {
        return closeRegulatorTemplate;
    }

    /**
     * @param closeRegulatorTemplate the closeRegulatorTemplate to set
     */
    public void setCloseRegulatorTemplate(String closeRegulatorTemplate) {
        this.closeRegulatorTemplate = closeRegulatorTemplate;
    }

    /**
     * @return the closeApprovalTemplate
     */
    public String getCloseApprovalTemplate() {
        return closeApprovalTemplate;
    }

    /**
     * @param closeApprovalTemplate the closeApprovalTemplate to set
     */
    public void setCloseApprovalTemplate(String closeApprovalTemplate) {
        this.closeApprovalTemplate = closeApprovalTemplate;
    }

    /**
     * @return the closeProviderTemplate
     */
    public String getCloseProviderTemplate() {
        return closeProviderTemplate;
    }

    /**
     * @param closeProviderTemplate the closeProviderTemplate to set
     */
    public void setCloseProviderTemplate(String closeProviderTemplate) {
        this.closeProviderTemplate = closeProviderTemplate;
    }

    /**
     * @return the closeInstructorTemplate
     */
    public String getCloseInstructorTemplate() {
        return closeInstructorTemplate;
    }

    /**
     * @param closeInstructorTemplate the closeInstructorTemplate to set
     */
    public void setCloseInstructorTemplate(String closeInstructorTemplate) {
        this.closeInstructorTemplate = closeInstructorTemplate;
    }

    public String getCloseCredentialRequirementTemplate() {
        return closeCredentialRequirementTemplate;
    }

    public void setCloseCredentialRequirementTemplate(
            String closeCredentialRequirementTemplate) {
        this.closeCredentialRequirementTemplate = closeCredentialRequirementTemplate;
    }
}