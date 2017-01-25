package com.softech.vu360.lms.web.controller.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerController;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;

public class FileRecordValidator  implements ILMSBaseInterface{
    private final int NO_OF_COLUMNS = 17;
    private static Logger log = Logger.getLogger(ManageLearnerController.class.getName());
    private static final long serialVersionUID = 1L;
    public boolean isValidFile(String file, String firstRowHeader, Brander brander) {
        boolean isValid = true;
        int maxUploadFileSize = Integer.parseInt(brander.getBrandElement("lms.batchUpload.maximumFileSize").trim());
        int fileSize = file.length();
        if (fileSize > maxUploadFileSize) {
            isValid = false;
        }
        return isValid;
    }

    public  boolean isValidDelimiter(String file, String firstRowHeader,String delimiter)throws IOException
    {
    	 boolean isValid = true;
    	 String header = null  ;
    	 BufferedReader is = new BufferedReader(new StringReader(file));
    	 if( StringUtils.isBlank(firstRowHeader) || firstRowHeader.equalsIgnoreCase("yes") ){
 			header = is.readLine();
 			if (!header.contains(delimiter))
 			{
 				
 				isValid=false;
 			}
    	 }
    	 return isValid;
    }
    public Map<String, Boolean> isValidRow(String[] record, Map<Integer, CustomField> fields, Map<CustomField, List<CustomFieldValueChoice>> customFieldChoicesMap, boolean hasManagerColumn, int managerColumnIndex) {
        //printChars1(line);
        int column_count = 0;
        
        //String[] record = line.split(delimiter, 1000);
        Map<String, Boolean> validRecord = new HashMap<String, Boolean>();
        validRecord.put("firstNameBlank", false);
        validRecord.put("lastNameBlank", false);
        validRecord.put("emailAddressBlank", false);
        validRecord.put("userNameBlank", false);
        validRecord.put("passwordBlank", false);
        validRecord.put("confirmPasswordBlankl", false);
        validRecord.put("invalidRowNumber", false);
        validRecord.put("invalidRecord", false);
        validRecord.put("shortPassword", false);
        log.debug("number of columns - " + record.length);
        for (int i = 0; i < record.length; i++) {
            column_count++;
            String rec = record[i].trim();
            if (column_count == 1 && rec.equalsIgnoreCase("")) {
                validRecord.put("firstNameBlank", true);
                validRecord.put("invalidRecord", true);
            }
            if (column_count == 3 && rec.equalsIgnoreCase("")) {
                validRecord.put("lastNameBlank", true);
                validRecord.put("invalidRecord", true);
            }
            if (column_count == 13 && rec.equalsIgnoreCase("")) {
                validRecord.put("emailAddressBlank", true);
                validRecord.put("invalidRecord", true);
            }
            if (column_count == 14 && rec.equalsIgnoreCase("")) {
                validRecord.put("passwordBlank", true);
                validRecord.put("invalidRecord", true);
            }
            if (column_count == 14 && rec.equalsIgnoreCase("")) {
                validRecord.put("confirmPasswordBlankl", true);
                validRecord.put("invalidRecord", true);
            } else {
                //KS - 2009-12-14 -
//				if( column_count == 14 && rec.length()<8) {
                if (column_count == 14 && !FieldsValidation.isPasswordCorrect(rec)) {
                    validRecord.put("shortPassword", true);
                    validRecord.put("invalidRecord", true);
                }
            }
            if (column_count == 17 && rec.equalsIgnoreCase("")) {
                validRecord.put("userNameBlank", true);
                validRecord.put("invalidRecord", true);
            }
        }
        boolean invalidRecord = false;
        boolean choicesAreGood = false;
        
        
        //by OWS for LMS-5182
        int columnCountToIterate=0;
//        if(hasManagerColumn)
//        	columnCountToIterate++;
        
        for (int i = NO_OF_COLUMNS; i < record.length; i++) {
            
        	if(i==managerColumnIndex)
        		continue;
        	
            CustomField field = fields.get(Integer.valueOf(i));
            if (field != null) {
                invalidRecord = false;
                if (field.getFieldRequired() && StringUtils.isBlank(record[i])) {
                    validRecord.put(field.getFieldLabel(), true);
                    validRecord.put("invalidRecord", true);
                    validRecord.put(field.getFieldLabel() + "-required", true);
                    invalidRecord = true;
                }
                if (!invalidRecord && !StringUtils.isBlank(record[i])) {
                    if (field instanceof SSNCustomFiled) {
                        if (!FieldsValidation.isSSNValid(record[i])) {
                            validRecord.put(field.getFieldLabel(), true);
                            validRecord.put("invalidRecord", true);
                            validRecord.put(field.getFieldLabel() + "-ssnError", true);
                            invalidRecord = true;
                        }
                    } else if (field instanceof DateTimeCustomField) {
                        if (!FieldsValidation.isValidDate(record[i])) {
                            validRecord.put(field.getFieldLabel(), true);
                            validRecord.put("invalidRecord", true);
                            validRecord.put(field.getFieldLabel() + "-dateError", true);
                            invalidRecord = true;
                        }
                    } else if (field instanceof NumericCusomField) {
                        if (!FieldsValidation.isNumeric(record[i])) {
                            validRecord.put(field.getFieldLabel(), true);
                            validRecord.put("invalidRecord", true);
                            validRecord.put(field.getFieldLabel() + "-numericError", true);
                            invalidRecord = true;
                        }
                    } else if (field instanceof SingleSelectCustomField) {
                        List<CustomFieldValueChoice> choices = customFieldChoicesMap.get(field);
                        boolean choiceFound = false;
                        for (CustomFieldValueChoice choice : choices) {
                            if (choice.getLabel().trim().equalsIgnoreCase(record[i].trim())) {
                                choiceFound = true;
                                break;
                            }
                        }
                        if (!choiceFound) {
                            validRecord.put("invalidRecord", true);
                            validRecord.put(field.getFieldLabel() + "-textError", true);
                            invalidRecord = true;
                        }
                    } else if (field instanceof MultiSelectCustomField) {
                        List<CustomFieldValueChoice> choices = customFieldChoicesMap.get(field);
                        String[] valueList = record[i].split(">");
                        String someValue = "";
                        for (String str : valueList) {
                            choicesAreGood = false;
                            for (CustomFieldValueChoice customFieldValueChoice : choices) {
                                if (customFieldValueChoice.getLabel().trim().equalsIgnoreCase(str.trim())) {
                                    choicesAreGood = true;
                                }
                            }
                            if (!choicesAreGood)
                                break;
                        }
                        if (!choicesAreGood) {
                            validRecord.put("invalidRecord", true);
                            validRecord.put(field.getFieldLabel() + "-multiSelectError", true);
                            invalidRecord = true;
                        }
                    }
                }
            }
        }
        return validRecord;
    }
}