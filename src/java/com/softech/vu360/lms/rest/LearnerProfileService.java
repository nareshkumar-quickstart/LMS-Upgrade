package com.softech.vu360.lms.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softech.JWTValidation.JwtValidation;
import com.softech.JWTValidation.model.JwtPayload;
import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.VU360UserRepository;
import com.softech.vu360.lms.rest.model.FieldOption;
import com.softech.vu360.lms.rest.model.LearnerCreditReportingField;
import com.softech.vu360.lms.rest.model.LearnerProfile;
import com.softech.vu360.lms.rest.model.LearnerProfileAddress;
import com.softech.vu360.lms.rest.model.PersonalInformation;
import com.softech.vu360.lms.rest.model.Question;
import com.softech.vu360.lms.rest.model.ValidationQuestionSet;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.LmsAuthenticationService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.learner.LearnerProfileController;
import com.softech.vu360.lms.web.controller.model.LearnerProfileForm;
import com.softech.vu360.lms.web.controller.model.LearnerValidationQASetDTO;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;

/**
 * Created by muhammad.sajjad on 11/7/2016.
 */

@RestController
@RequestMapping("/learner/profile")
public class LearnerProfileService {

    @Autowired
    private LearnerService learnerService;

    @Autowired
    private VU360UserService vu360UserService;

    @Autowired
    private CustomFieldService customFieldService;

    @Autowired
    private VU360UserRepository vu360UserRepository;

    @Autowired
    private LmsAuthenticationService lmsAuthenticationService;
    
    @Autowired
    private VelocityEngine velocityEngine;

    private static final Logger logger = Logger.getLogger(LearnerProfileService.class);


    @RequestMapping( method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Boolean> save(@RequestBody LearnerProfile udpForm, @RequestHeader(value = "token") String token, HttpServletRequest request, HttpServletResponse response) throws Exception{
        boolean updated = false;
        Map<String,Boolean> responseData = new HashMap<>();
        try {
            JwtPayload payload = JwtValidation.validateJWTToken(token);
            logger.debug("\nUser name is " + payload.getUser_name());
            LearnerProfileForm learnerProfileForm = new LearnerProfileForm();

            VU360User lmsVu360User = vu360UserRepository.findUserByUserName(payload.getUser_name());

            Authentication auth = lmsAuthenticationService.authenticateUser(payload.getUser_name());

            SecurityContextHolder.getContext().setAuthentication(auth);

            PersonalInformation ps = udpForm.getPersonalInformation();
            if(ps != null) {
                updateUserInformation(lmsVu360User, ps);
            }

            if(ps != null) {
                ps.setUserName(payload.getUser_name());
                if(ps.getTimeZone() != null && !ps.getTimeZone().isEmpty()) {
                    learnerProfileForm.setTimeZoneId(Long.parseLong(ps.getTimeZone().get(0).getValue()));
                }else if(lmsVu360User.getLearner().getLearnerProfile().getTimeZone() != null){
                    learnerProfileForm.setTimeZoneId(lmsVu360User.getLearner().getLearnerProfile().getTimeZone().getId());
                }
                logger.debug("\nTime zone id is " + learnerProfileForm.getTimeZoneId());
            }

            learnerProfileForm.setVu360User(lmsVu360User);

            List<ValidationQuestionSet> udpValidationQuestionList =  udpForm.getValidationQuestions();
            logger.debug("\nPreparing questions... ");
            learnerProfileForm.setLearnerValidationQuestions(getLearnerValidationQASetDTO(udpValidationQuestionList, lmsVu360User.getLearner().getId()));

            logger.debug("\nPreparing Custom Fields ... ");
            learnerProfileForm.setCustomFields(mapCustomFieldsFromUDPToLearner(lmsVu360User, udpForm));

            logger.debug("\nPreparing Reporting Fields... ");
            learnerProfileForm.setCreditReportingFields(mapCreditReportingFieldsFromUDPToLearner(lmsVu360User, udpForm, learnerProfileForm));

            LearnerProfileController learnerProfileController = new LearnerProfileController();
            learnerProfileController.setLearnerService(learnerService);
            learnerProfileController.setVelocityEngine(velocityEngine);
            learnerProfileForm.setEventSource("");
            learnerProfileController.setVu360UserService(vu360UserService);
            learnerProfileController.setCustomFieldService(customFieldService);
            logger.debug("\nUpdating user profile...");
            learnerProfileController.updateProfile(request, response, learnerProfileForm, null);
            responseData.put("status", Boolean.TRUE);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            responseData.put("status", Boolean.FALSE);
        }

        return responseData;
    }

    private List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> getCreditReportFields(VU360User vu360User, LearnerProfileForm learnerProfileForm){
        CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
        List<CreditReportingField> customFieldList=learnerService.getCreditReportingFieldsByLearnerCourseApproval(vu360User.getLearner());
        List<CreditReportingFieldValue> customFieldValueList = learnerService.getCreditReportingFieldValues(vu360User.getLearner());

        Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();

        for(CreditReportingField creditReportingField : customFieldList){
            if(!(creditReportingField instanceof StaticCreditReportingField)){
                if (creditReportingField instanceof SingleSelectCreditReportingField ||
                        creditReportingField instanceof MultiSelectCreditReportingField) {

                    List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = learnerService.getChoicesByCreditReportingField(creditReportingField);

                    if(creditReportingField instanceof MultiSelectCreditReportingField){
                        CreditReportingFieldValue creditReportingFieldValue=this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, customFieldValueList);
                        List<CreditReportingFieldValueChoice> tempCreditReportingFieldValueOptionList = new ArrayList<CreditReportingFieldValueChoice>();
                        for(CreditReportingFieldValueChoice fieldChoice: creditReportingFieldValue.getCreditReportingValueChoices()){
                            for (Iterator<CreditReportingFieldValueChoice> iterator = creditReportingFieldValueOptionList.iterator(); iterator.hasNext();) {
                                CreditReportingFieldValueChoice fieldList = iterator.next();
                                if(fieldChoice.getId() == fieldList.getId()){
                                    iterator.remove();
                                    tempCreditReportingFieldValueOptionList.add(0,fieldList);
                                }
                            }
                        }
                        for(int iCount = tempCreditReportingFieldValueOptionList.size(); iCount>0;iCount--){
                            int sortedItemsize = iCount - 1;
                            CreditReportingFieldValueChoice tempChoiceToAdd = tempCreditReportingFieldValueOptionList.get(sortedItemsize);
                            creditReportingFieldValueOptionList.add(0, tempChoiceToAdd);
                        }
                        existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(), creditReportingFieldValue.getCreditReportingValueChoices());
                    }
                    fieldBuilder.buildCreditReportingField(creditReportingField, 1, customFieldValueList,creditReportingFieldValueOptionList);
                } else {
                    fieldBuilder.buildCreditReportingField(creditReportingField, 1, customFieldValueList);
                }
            }
        }

        List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = fieldBuilder.getCreditReportingFieldList();

        for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : creditReportingFields){
            if(field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField){
                List<CreditReportingFieldValueChoice> existingChoices = existingCreditReportingFieldValueChoiceMap.get(field.getCreditReportingFieldRef().getId());
                Map<Long,CreditReportingFieldValueChoice> existingChoicesMap = new HashMap<Long,CreditReportingFieldValueChoice>();

                for (CreditReportingFieldValueChoice choice : existingChoices){
                    existingChoicesMap.put(choice.getId(), choice);
                }
                List<String> selectedChoicesList = new ArrayList<>();
                for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice tempChoice : field.getCreditReportingFieldValueChoices()){
                    if(existingChoicesMap.containsKey(tempChoice.getCreditReportingFieldValueChoiceRef().getId())){
                        tempChoice.setSelected(true);
                        selectedChoicesList.add(tempChoice.getCreditReportingFieldValueChoiceRef().getId().toString());
                    }
                }
                String[] selectedChoices = new String[selectedChoicesList.size()];
                selectedChoicesList.toArray(selectedChoices);
                field.setSelectedChoices(selectedChoices);
            }
        }
        customFieldService.createValueRecordForStaticReportingField(vu360User, customFieldList, customFieldValueList);
        return creditReportingFields;
    }

    private CreditReportingFieldValue getCreditReportingFieldValueByCreditReportingField(CreditReportingField creditReportingField,
                                                                                         List<CreditReportingFieldValue> creditReportingFieldValues){
        if (creditReportingFieldValues != null){
            for (CreditReportingFieldValue creditReportingFieldValue : creditReportingFieldValues){
                if (creditReportingFieldValue.getReportingCustomField()!=null){
                    if (creditReportingFieldValue.getReportingCustomField().getId().compareTo(creditReportingField.getId())==0){
                        return creditReportingFieldValue;
                    }
                }
            }
        }
        return new CreditReportingFieldValue();
    }

    private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldsForUser(VU360User vu360User){
        List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();;//customer.getCustomFields();
        List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
        Customer customer = vu360User.getLearner().getCustomer();
        Distributor reseller = vu360User.getLearner().getCustomer().getDistributor();
        CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
        customFieldValues = vu360User.getLearner().getLearnerProfile().getCustomFieldValues() ;
        totalCustomFieldList.addAll(customer.getCustomFields());
        totalCustomFieldList.addAll(reseller.getCustomFields());

        Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

        for(CustomField customField:totalCustomFieldList){
            if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ){
                List<CustomFieldValueChoice> customFieldValueChoices=customFieldService.getOptionsByCustomField(customField);
                fieldBuilder2.buildCustomField(customField,1,customFieldValues,customFieldValueChoices);

                if (customField instanceof MultiSelectCustomField){
                    CustomFieldValue customFieldValue=this.getCustomFieldValueByCustomField(customField, customFieldValues);
                    existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
                }

            }else {
                fieldBuilder2.buildCustomField(customField,1,customFieldValues, null);
            }
        }

        List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList2 =fieldBuilder2.getCustomFieldList();

        for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField:customFieldList2){
            if (customField.getCustomFieldRef() instanceof MultiSelectCustomField ){
                List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
                Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

                for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
                    tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
                }
                List<String> selectedChoicesList = new ArrayList<>();
                for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
                    if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
                        customFieldValueChoice.setSelected(true);
                        selectedChoicesList.add(customFieldValueChoice.getCustomFieldValueChoiceRef().getId().toString());
                    }
                }
                String[] selectedChoices = new String[selectedChoicesList.size()];
                selectedChoicesList.toArray(selectedChoices);
                customField.setSelectedChoices(selectedChoices);
            }
        }

        return customFieldList2;
    }

    private CustomFieldValue getCustomFieldValueByCustomField(CustomField customField,List<CustomFieldValue> customFieldValues){
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

    private VU360User updateUserInformation(VU360User lmsVu360User, PersonalInformation personalInformation){

        copyProfileInfo(lmsVu360User, personalInformation);

        Address address1 =  lmsVu360User.getLearner().getLearnerProfile().getLearnerAddress();
        LearnerProfileAddress learnerProfileAddress1 = personalInformation.getAddress1();
        copyAddressFields(address1, learnerProfileAddress1);

        Address address2 =  lmsVu360User.getLearner().getLearnerProfile().getLearnerAddress2();
        LearnerProfileAddress learnerProfileAddress2 = personalInformation.getAddress2();
        copyAddressFields(address2, learnerProfileAddress2);

        return lmsVu360User;
    }

    private void copyProfileInfo(VU360User lmsVu360User, PersonalInformation personalInformation){
        System.out.println("Copying profile info ....");
        if(personalInformation != null) {
            lmsVu360User.setFirstName(!StringUtils.isEmpty(personalInformation.getFirstName()) ? personalInformation.getFirstName() : lmsVu360User.getFirstName());
            lmsVu360User.setMiddleName(!StringUtils.isEmpty(personalInformation.getMiddleName()) ? personalInformation.getMiddleName() : "");
            lmsVu360User.setLastName(!StringUtils.isEmpty(personalInformation.getLastName()) ? personalInformation.getLastName() : lmsVu360User.getLastName());
            lmsVu360User.setEmailAddress(!StringUtils.isEmpty(personalInformation.getEmailAddress()) ? personalInformation.getEmailAddress() : lmsVu360User.getEmailAddress());

            com.softech.vu360.lms.model.LearnerProfile learnerProfile = lmsVu360User.getLearner().getLearnerProfile();
            learnerProfile.setMobilePhone(!StringUtils.isEmpty(personalInformation.getPhone()) ? personalInformation.getPhone() : learnerProfile.getMobilePhone());
            learnerProfile.setOfficePhone(!StringUtils.isEmpty(personalInformation.getOfficePhone()) ? personalInformation.getOfficePhone() : learnerProfile.getOfficePhone());
            learnerProfile.setOfficePhoneExtn(!StringUtils.isEmpty(personalInformation.getOfficeExtension()) ? personalInformation.getOfficeExtension() : learnerProfile.getOfficePhoneExtn());

            lmsVu360User.setUsername(!StringUtils.isEmpty(personalInformation.getUserName()) ? personalInformation.getUserName() : lmsVu360User.getUsername());
            if (!StringUtils.isEmpty(personalInformation.getPassword())) {
                lmsVu360User.setPassword(personalInformation.getPassword());
                lmsVu360User.setPassWordChanged(Boolean.TRUE);
            }
        }
    }

    private void copyAddressFields(Address address, LearnerProfileAddress learnerProfileAddress){
        if(learnerProfileAddress != null) {
            address.setStreetAddress(!StringUtils.isEmpty(learnerProfileAddress.getStreetAddress()) ? learnerProfileAddress.getStreetAddress() : address.getStreetAddress());
            address.setCity(!StringUtils.isEmpty(learnerProfileAddress.getCity()) ? learnerProfileAddress.getCity() : address.getCity());
            address.setState(!StringUtils.isEmpty(learnerProfileAddress.getState()) ? learnerProfileAddress.getState() : address.getState());
            address.setZipcode(!StringUtils.isEmpty(learnerProfileAddress.getZipCode()) ? learnerProfileAddress.getZipCode() : address.getZipcode());
            address.setCountry(!StringUtils.isEmpty(learnerProfileAddress.getCountry()) ? learnerProfileAddress.getCountry() : address.getCountry());
        }
    }

    private LearnerValidationQASetDTO getLearnerValidationQASetDTO(List<ValidationQuestionSet> udpValidationQuestionList, Long learnerId){
        LearnerValidationQASetDTO learnerValidationQASetDTO = new LearnerValidationQASetDTO();
        if(udpValidationQuestionList != null && !udpValidationQuestionList.isEmpty()) {
            learnerValidationQASetDTO.setLearnerId(learnerId);

            learnerValidationQASetDTO.setQuestionInSet1(getSelectedQuestionId(udpValidationQuestionList.get(0)));
            learnerValidationQASetDTO.setAnswerForSet1(udpValidationQuestionList.get(0).getAnswer());

            learnerValidationQASetDTO.setQuestionInSet2(getSelectedQuestionId(udpValidationQuestionList.get(1)));
            learnerValidationQASetDTO.setAnswerForSet2(udpValidationQuestionList.get(1).getAnswer());

            learnerValidationQASetDTO.setQuestionInSet3(getSelectedQuestionId(udpValidationQuestionList.get(2)));
            learnerValidationQASetDTO.setAnswerForSet3(udpValidationQuestionList.get(2).getAnswer());

            learnerValidationQASetDTO.setQuestionInSet4(getSelectedQuestionId(udpValidationQuestionList.get(3)));
            learnerValidationQASetDTO.setAnswerForSet4(udpValidationQuestionList.get(3).getAnswer());

            learnerValidationQASetDTO.setQuestionInSet5(getSelectedQuestionId(udpValidationQuestionList.get(4)));
            learnerValidationQASetDTO.setAnswerForSet5(udpValidationQuestionList.get(4).getAnswer());
        }

        return learnerValidationQASetDTO;
    }

    private Long getSelectedQuestionId(ValidationQuestionSet validationQuestionSet){
        Long selectedQuestionId = 0L;
        for(Question question : validationQuestionSet.getQuestions()){
            if(question.getSelected()){
                selectedQuestionId = Long.parseLong(question.getValue());
                break;
            }
        }
        return selectedQuestionId;
    }


    private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> mapCustomFieldsFromUDPToLearner(VU360User vu360User, LearnerProfile udpForm) {
        List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldsFromDB = getCustomFieldsForUser(vu360User);
        if (udpForm.getCustomFields() != null && udpForm.getCustomFields().size() > 0){
            for (com.softech.vu360.lms.web.controller.model.customfield.CustomField fieldValueMap : customFieldsFromDB) {
                List<CustomFieldValueChoice> customFieldValueChoicesFromUDP = null;
                customFieldValueChoicesFromUDP = new ArrayList<>();
                for (LearnerCreditReportingField learnerCreditReportingFieldFromUDP : udpForm.getCustomFields()) {
                    if (fieldValueMap.getCustomFieldRef().getId() == Long.parseLong(learnerCreditReportingFieldFromUDP.getValue())) {
                        switch (fieldValueMap.getCustomFieldRef().getFieldType()) {
                            case "SINGLELINETEXTCUSTOMFIELD":
                            case "NUMERICCUSTOMFIELD":
                            case "DATETIMECUSTOMFIELD":
                            case "SSNCUSTOMFIELD":
                            case "MULTIPLELINETEXTCUSTOMFIELD":
                                fieldValueMap.getCustomFieldValueRef().setValue(learnerCreditReportingFieldFromUDP.getText());
                                break;
                            case "SINGLESELECTCUSTOMFIELD":
                                for (FieldOption fieldOption : learnerCreditReportingFieldFromUDP.getOptions()) {
                                    if(fieldOption.getSelected())
                                        fieldValueMap.getCustomFieldValueRef().setValue(fieldOption.getText());
                                }
                                break;
                            case "MULTISELECTCUSTOMFIELD"://
                                CustomFieldValueChoice customFieldValueChoice = null;
                                List<String> selectedChoicesList = new ArrayList<>();
                                List<com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice> newCustomFieldValueChoiceMap = new ArrayList<>();
                                ;
                                for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoiceMap : fieldValueMap.getCustomFieldValueChoices()) {
                                    customFieldValueChoice = customFieldValueChoiceMap.getCustomFieldValueChoiceRef();

                                    for (FieldOption fieldOption : learnerCreditReportingFieldFromUDP.getOptions()) {
                                        if (fieldOption.getSelected() && customFieldValueChoice.getId() == Long.parseLong(fieldOption.getValue())) {
                                            customFieldValueChoicesFromUDP.add(customFieldValueChoice);
                                            newCustomFieldValueChoiceMap.add(customFieldValueChoiceMap);
                                            selectedChoicesList.add(customFieldValueChoice.getId().toString());
                                            customFieldValueChoiceMap.setSelected(Boolean.TRUE);
                                        }
                                    }
                                }
                                String[] selectedChoices = new String[selectedChoicesList.size()];
                                fieldValueMap.setSelectedChoices(selectedChoicesList.toArray(selectedChoices));
                                fieldValueMap.setCustomFieldValueChoices(newCustomFieldValueChoiceMap);
                                fieldValueMap.getCustomFieldValueRef().setValueItems(customFieldValueChoicesFromUDP);
                                break;
                        }
                        break;
                    }
                }
            }
        }
        return customFieldsFromDB;
    }

    private List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> mapCreditReportingFieldsFromUDPToLearner(VU360User vu360User, LearnerProfile udpForm, LearnerProfileForm learnerProfileForm){
        List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFieldsFromDB = getCreditReportFields(vu360User, learnerProfileForm);
        if(udpForm.getReportingFields() != null && udpForm.getReportingFields().size() > 0) {
            for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField fieldValueMap : creditReportingFieldsFromDB) {
                List<CreditReportingFieldValueChoice> creditReportingFieldValueChoicesFromUDP = null;
                creditReportingFieldValueChoicesFromUDP = new ArrayList<>();
                for (LearnerCreditReportingField learnerCreditReportingFieldFromUDP : udpForm.getReportingFields()) {
                    if (fieldValueMap.getCreditReportingFieldRef().getId() == Long.parseLong(learnerCreditReportingFieldFromUDP.getValue())) {
                        switch (fieldValueMap.getCreditReportingFieldRef().getFieldType()) {
                            case "SINGLELINETEXTCREDITREPORTINGFIELD":
                            case "NUMERICCREDITREPORTINGFIELD":
                            case "DATETIMECREDITREPORTINGFIELD":
                            case "SSNCREDITREPORTINGFIELD":
                            case "MULTIPLELINETEXTCREDITREPORTINGFIELD":
                                fieldValueMap.getCreditReportingFieldValueRef().setValue(learnerCreditReportingFieldFromUDP.getText());
                                break;
                            case "SINGLESELECTCREDITREPORTINGFIELD":
                                for (FieldOption fieldOption : learnerCreditReportingFieldFromUDP.getOptions()) {
                                    if(fieldOption.getSelected())
                                        fieldValueMap.getCreditReportingFieldValueRef().setValue(fieldOption.getText());
                                }
                                break;

                            case "MULTISELECTCREDITREPORTINGFIELD":
                                CreditReportingFieldValueChoice creditReportingFieldValueChoice = null;
                                List<String> selectedChoicesList = new ArrayList<>();
                                List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice> newCreditReportingFieldValueChoiceMap = new ArrayList<>();
                                for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice creditReportingFieldValueChoiceMap : fieldValueMap.getCreditReportingFieldValueChoices()) {
                                    creditReportingFieldValueChoice = creditReportingFieldValueChoiceMap.getCreditReportingFieldValueChoiceRef();

                                    for (FieldOption fieldOption : learnerCreditReportingFieldFromUDP.getOptions()) {
                                        if (fieldOption.getSelected() && creditReportingFieldValueChoice.getId() == Long.parseLong(fieldOption.getValue())) {
                                            creditReportingFieldValueChoicesFromUDP.add(creditReportingFieldValueChoice);
                                            newCreditReportingFieldValueChoiceMap.add(creditReportingFieldValueChoiceMap);
                                            selectedChoicesList.add(creditReportingFieldValueChoice.getId().toString());
                                            creditReportingFieldValueChoiceMap.setSelected(Boolean.TRUE);
                                        }
                                    }
                                }
                                String[] selectedChoices = new String[selectedChoicesList.size()];
                                fieldValueMap.setSelectedChoices(selectedChoicesList.toArray(selectedChoices));
                                fieldValueMap.setCreditReportingFieldValueChoices(newCreditReportingFieldValueChoiceMap);
                                fieldValueMap.getCreditReportingFieldValueRef().setCreditReportingValueChoices(creditReportingFieldValueChoicesFromUDP);
                                break;
                        }
                        break;
                    }
                }
            }
        }
        return creditReportingFieldsFromDB;
    }
}
