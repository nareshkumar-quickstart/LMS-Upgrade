package com.softech.vu360.lms.web.controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.MultipleLineTextCustomfield;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleLineTextCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldForm;

/**
 * Provides common utilities for Custom Field customFieldForm and Custom Fields.
 * 
 * @author abdul.aziz
 */
public class CustomFieldFormUtil {

    /**
     * Transforms the {@code CustomField} from ${@code CustomFieldForm} and also
     * update the custom fields in the database.
     * 
     * @param customFieldForm
     * @param service
     * @return
     * @throws IOException 
     */
    public static CustomField processCustomField(final CustomFieldForm customFieldForm, AccreditationService service)
            throws IOException {
        CustomField customField = null;

        if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_SINGLE_LINE_OF_TEXT)) {
            
            SingleLineTextCustomFiled singleLineTextCustomFiled = (SingleLineTextCustomFiled) customFieldForm.getCustomField();
            singleLineTextCustomFiled.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            singleLineTextCustomFiled.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            singleLineTextCustomFiled.setFieldLabel(customFieldForm.getFieldLabel().trim());
            singleLineTextCustomFiled.setFieldRequired(customFieldForm.isFieldRequired());
            service.saveCustomField(singleLineTextCustomFiled);

            customField = singleLineTextCustomFiled;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_DATE)) {
            
            DateTimeCustomField dateTimeCustomField = (DateTimeCustomField) customFieldForm.getCustomField();
            dateTimeCustomField.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            dateTimeCustomField.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            dateTimeCustomField.setFieldLabel(customFieldForm.getFieldLabel().trim());
            dateTimeCustomField.setFieldRequired(customFieldForm.isFieldRequired());
            
            service.saveCustomField(dateTimeCustomField);
            
            customField = dateTimeCustomField;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT)) {
            
            MultipleLineTextCustomfield multipleLineTextCustomfield = (MultipleLineTextCustomfield) customFieldForm.getCustomField();
            multipleLineTextCustomfield.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            multipleLineTextCustomfield.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            multipleLineTextCustomfield.setFieldLabel(customFieldForm.getFieldLabel().trim());
            multipleLineTextCustomfield.setFieldRequired(customFieldForm.isFieldRequired());
            
            service.saveCustomField(multipleLineTextCustomfield);
            
            customField = multipleLineTextCustomfield;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_NUMBER)) {
            
            NumericCusomField numericCusomField = (NumericCusomField) customFieldForm.getCustomField();
            numericCusomField.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            numericCusomField.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            numericCusomField.setFieldLabel(customFieldForm.getFieldLabel().trim());
            numericCusomField.setFieldRequired(customFieldForm.isFieldRequired());
            
            service.saveCustomField(numericCusomField);
            
            customField = numericCusomField;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_SOCIAL_SECURITY_NUMBER)) {
            
            SSNCustomFiled sSNCustomFiled = (SSNCustomFiled) customFieldForm.getCustomField();
            sSNCustomFiled.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            sSNCustomFiled.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            sSNCustomFiled.setFieldLabel(customFieldForm.getFieldLabel().trim());
            sSNCustomFiled.setFieldRequired(customFieldForm.isFieldRequired());
            
            service.saveCustomField(sSNCustomFiled);
            
            customField = sSNCustomFiled;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_RADIO_BUTTON)) {
            
            SingleSelectCustomField singleSelectCustomField = (SingleSelectCustomField) customFieldForm.getCustomField();
            singleSelectCustomField.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            singleSelectCustomField.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            singleSelectCustomField.setFieldLabel(customFieldForm.getFieldLabel().trim());
            singleSelectCustomField.setFieldRequired(customFieldForm.isFieldRequired());

            if (customFieldForm.isAlignment()) {
                singleSelectCustomField.setAlignment(singleSelectCustomField.HORIZONTAL);
            } else {
                singleSelectCustomField.setAlignment(singleSelectCustomField.VERTICAL);
            }

            readOptions(customFieldForm);
            service.removeOption(singleSelectCustomField);
            
            for (int i = 0; i < customFieldForm.getOptionList().size(); i++) {
                CustomFieldValueChoice option = new CustomFieldValueChoice();
                option.setDisplayOrder(i);
                option.setLabel(customFieldForm.getOptionList().get(i));
                option.setValue(customFieldForm.getOptionList().get(i));
                option.setCustomField(singleSelectCustomField);
                service.saveOption(option);
            }
            
            service.saveCustomField(singleSelectCustomField);
            
            customField = singleSelectCustomField;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_CHOOSE)) {
            
            MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField) customFieldForm.getCustomField();
            multiSelectCustomField.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            multiSelectCustomField.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            multiSelectCustomField.setFieldRequired(customFieldForm.isFieldRequired());
            multiSelectCustomField.setFieldLabel(customFieldForm.getFieldLabel().trim());
            
            if (customFieldForm.isAlignment()) {
                multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
            } else {
                multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
            }

            readOptions(customFieldForm);
            service.removeOption(multiSelectCustomField);
            
            for (int i = 0; i < customFieldForm.getOptionList().size(); i++) {
                CustomFieldValueChoice option = new CustomFieldValueChoice();
                option.setDisplayOrder(i);
                option.setLabel(customFieldForm.getOptionList().get(i));
                option.setValue(customFieldForm.getOptionList().get(i));
                option.setCustomField(multiSelectCustomField);
                service.saveOption(option);
            }
            
            service.saveCustomField(multiSelectCustomField);
            
            customField = multiSelectCustomField;
        } else if (customFieldForm.getFieldType().equalsIgnoreCase(customFieldForm.CUSTOMFIELD_CHECK_BOX)) {
            
            MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField) customFieldForm.getCustomField();
            multiSelectCustomField.setCustomFieldDescription(customFieldForm.getCustomFieldDescription());
            multiSelectCustomField.setFieldEncrypted(customFieldForm.isFieldEncrypted());
            multiSelectCustomField.setFieldRequired(customFieldForm.isFieldRequired());
            multiSelectCustomField.setFieldLabel(customFieldForm.getFieldLabel().trim());
            
            if (customFieldForm.isAlignment()) {
                multiSelectCustomField.setAlignment(multiSelectCustomField.HORIZONTAL);
            } else {
                multiSelectCustomField.setAlignment(multiSelectCustomField.VERTICAL);
            }

            readOptions(customFieldForm);
            service.removeOption(multiSelectCustomField);

            for (int i = 0; i < customFieldForm.getOptionList().size(); i++) {
                CustomFieldValueChoice option = new CustomFieldValueChoice();
                option.setDisplayOrder(i);
                option.setLabel(customFieldForm.getOptionList().get(i));
                option.setValue(customFieldForm.getOptionList().get(i));
                option.setCustomField(multiSelectCustomField);
                service.saveOption(option);
            }
            
            service.saveCustomField(multiSelectCustomField);
            customField = multiSelectCustomField;
        }

        return customField;
    }

    private static void readOptions(CustomFieldForm customFieldForm) throws IOException {
        String str;
        BufferedReader reader = new BufferedReader(new StringReader(customFieldForm.getOption()));

        List<String> optionList = new ArrayList<String>();
        while ((str = reader.readLine()) != null) {
            if (str.length() > 0) {
                if (!StringUtils.isBlank(str)) {
                    optionList.add(str);
                }
            }
        }

        customFieldForm.setOptionList(optionList);
    }
}
