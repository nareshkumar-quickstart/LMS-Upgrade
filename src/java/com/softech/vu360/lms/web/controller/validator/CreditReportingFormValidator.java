package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.TextFormatter;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.CreditReportingForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldValidationUtil;

public class CreditReportingFormValidator implements Validator {

	private static final Logger log = Logger.getLogger(CreditReportingFormValidator.class
			.getName());

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return LearnerProfile.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {

		CreditReportingForm form = (CreditReportingForm) obj;
		VU360User vu360User = form.getVu360User();

		if (form.getCreditReportingFields().size() > 0) {
			this.validateCustomFields(form.getCreditReportingFields(), errors);
		}

		if (form.getMissingCustomFields().size() > 0) {
			this.validateMainCustomFields(form.getMissingCustomFields(), errors);
		}
	}

	public void validateLearnerConnect(Object obj, Errors errors) {
		CreditReportingForm form = (CreditReportingForm) obj;
		if (StringUtils.isBlank(form.getLearnerEmailSubject())) {
			errors.rejectValue("learnerEmailSubject",
					"error.learner.learnerConnect.subject.empty");
		}
	}

	public void validateHWSubmission(Map<String, MultipartFile> filesNames,
			String assignmentDueDate, Errors errors, Brander brander) {
		log.debug("validateHWSubmission");
		log.debug("assignmentDueDate" + assignmentDueDate);

		if (!StringUtils.isBlank(assignmentDueDate)) {

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date expirationDate = null;
			Date date = new Date();
			log.debug("currentdate " + formatter.format(date));
			try {
				expirationDate = formatter.parse(assignmentDueDate);
				if (date.after(expirationDate)) {
					errors.rejectValue("assignmentDueDate",
							"error.learner.launchcourse.assignmentDueDate");

				}

			} catch (ParseException e) {
				log.debug("exception" + e.getMessage());
				errors.rejectValue("assignmentDueDate",
						"error.learner.launchcourse.assignmentDueDate");
			}

		}

		long fileSize = 0;
		List<String> fileNameList = new ArrayList<String>();
		Iterator<MultipartFile> multipartFileIterator = filesNames.values()
				.iterator();
		while (multipartFileIterator.hasNext()) {
			MultipartFile file = multipartFileIterator.next();
			if (!file.getOriginalFilename().equals("")) {
				fileSize += file.getSize();
				fileNameList.add(file.getOriginalFilename());
			}
		}

		boolean fileError = false;

		if (fileNameList.size() > 0) {
			Iterator<String> fileNameIterator = fileNameList.iterator();

			while (fileNameIterator.hasNext()) {
				String fileName = fileNameIterator.next();
				if (!FileUploadUtils.validFileFormate(fileName, brander
						.getBrandElement("lms.hwassignmentUpload.extension"))) {
					fileError = true;
				}

			}
		} else {
			fileError = true;
		}

		if (fileError) {
			errors.rejectValue("", "error.learner.launchassignmentcourse.file");
		} else {

			Long maxFileSize = Long.parseLong(brander.getBrandElement(
					"lms.hwassignmentUpload.maximumFileSize").trim());
			log.debug("Max File Size : " + maxFileSize);
			log.debug("Actual File Size : " + fileSize);

			if (fileSize > maxFileSize) {
				errors.rejectValue("", "lms.hwassignmentUpload.fileSizeNotice");
			}

		}

	}

	public void validateCustomFields(
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> customFields,
			Errors errors) {

		int fieldindex = 0;
		if (customFields.size() > 0) {

			for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField tempCustomField : customFields) {

				CreditReportingField customField = tempCustomField
						.getCreditReportingFieldRef();
				CreditReportingFieldValue customFieldValue = tempCustomField
						.getCreditReportingFieldValueRef();
				// customFieldValue.getValue();
				Object originalValue = customFieldValue.getValue();

				if (customField.isFieldRequired()) {
					if (customField instanceof MultiSelectCreditReportingField) {

						if (((MultiSelectCreditReportingField) customField)
								.isCheckBox()) {
							int count = 0;
							for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice customFieldValueChoice : tempCustomField
									.getCreditReportingFieldValueChoices()) {
								if (customFieldValueChoice.isSelected()) {
									count = count + 1;
								}
							}
							if (count == 0) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
										"custom.field.required",
										"Please provide a value for the '"
												+ customField.getFieldLabel()
												+ "' field.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						} else {

							if (tempCustomField.getSelectedChoices() == null) {
								errors.rejectValue("creditReportingFields["
										+ fieldindex + "].selectedChoices",
										"custom.field.required",
										"Please provide a value for the '"
												+ customField.getFieldLabel()
												+ "' field.");
								tempCustomField.setStatus(2);
							} else if (tempCustomField.getSelectedChoices().length == 0) {
								errors.rejectValue("creditReportingFields["
										+ fieldindex + "].selectedChoices",
										"custom.field.required",
										"Please provide a value for the '"
												+ customField.getFieldLabel()
												+ "' field.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					} else {
						/*
						 * Object[] errorArgs = new Object[1]; errorArgs[0] =
						 * customField.getFieldLabel();
						 * ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						 * "customFields["
						 * +fieldindex+"].customFieldValueRef.value" ,
						 * "custom.field.required",
						 * errorArgs,customField.getFieldLabel
						 * ()+" is required");
						 */
						if (originalValue == null) {
							errors.rejectValue("creditReportingFields["
									+ fieldindex
									+ "].creditReportingFieldValueRef.value",
									"custom.field.required",
									"Please provide a value for the '"
											+ customField.getFieldLabel()
											+ "' field.");
							tempCustomField.setStatus(2);
						} else if (StringUtils
								.isBlank(originalValue.toString())) {
							errors.rejectValue("creditReportingFields["
									+ fieldindex
									+ "].creditReportingFieldValueRef.value",
									"custom.field.required",
									"Please provide a value for the '"
											+ customField.getFieldLabel()
											+ "' field.");
							tempCustomField.setStatus(2);
						} else {
							tempCustomField.setStatus(1);
						}
					}
				}
				if (customField instanceof NumericCreditReportingField) {
					if (originalValue != null) {
						if (StringUtils.isNotBlank(originalValue.toString())) {
							if (!CustomFieldValidationUtil
									.isNumeric(originalValue.toString())) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ " is an invalid number.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					}
				} else if (customField instanceof DateTimeCreditReportingField) {
					if (originalValue != null) {
						if (StringUtils.isNotBlank(originalValue.toString())) {
							if (!CustomFieldValidationUtil.isValidDate(
									originalValue.toString(), true,
									"MM/dd/yyyy")) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ " is an invalid date.");
								tempCustomField.setStatus(2);
							} else if((customField.getFieldLabel().equalsIgnoreCase("Date of Birth")||
									customField.getFieldLabel().equalsIgnoreCase("DOB")) &&
									(!CustomFieldValidationUtil.isValidBirthDate(originalValue.toString(), "MM/dd/yyyy", 10))) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
												"custom.field.required", 
										"The year you have entered is invalid, please enter a valid year of birth");
								tempCustomField.setStatus(2);
							} else
								tempCustomField.setStatus(1);
							}
					}
				} else if (customField instanceof SSNCreditReportingFiled) {
					if (originalValue != null) {
						if (StringUtils.isNotBlank(originalValue.toString())) {
							if (!CustomFieldValidationUtil
									.isSSNValid(originalValue.toString())) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ " is an invalid SSN Number.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					}
				}
				else if (customField instanceof TelephoneNumberCreditReportingField) {
					if (originalValue != null) {
						if (StringUtils.isNotBlank(originalValue.toString())) {
							if (!CustomFieldValidationUtil
									.isTelephoneNumberValid(originalValue.toString())) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ " is an invalid Telephone Number.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
								customFieldValue.setValue(TextFormatter.convertTelephoneToNumber(originalValue.toString()));
							}
						}
					}
				}
				fieldindex = fieldindex + 1;
			}
		}
	}

	/*************************/

	public void validateMainCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields,
			Errors errors) {
		int fieldindex = 0;
		if (customFields.size() > 0) {

			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField : customFields) {

				CustomField customField = tempCustomField.getCustomFieldRef();
				CustomFieldValue customFieldValue = tempCustomField
						.getCustomFieldValueRef();

				if (customField.getFieldRequired()) {
					if (customField instanceof MultiSelectCustomField) {

						if (((MultiSelectCustomField) customField).getCheckBox()) {
							int count = 0;
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : tempCustomField
									.getCustomFieldValueChoices()) {
								if (customFieldValueChoice.isSelected()) {
									count = count + 1;
								}
							}
							if (count == 0) {
								errors.rejectValue("missingCustomFields["
										+ fieldindex
										+ "].customFieldValueRef.value",
										"custom.field.required",
										"Please provide a value for the '"
												+ customField.getFieldLabel()
												+ "' field.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						} else {
							if (tempCustomField.getSelectedChoices() == null) {
								errors.rejectValue("missingCustomFields["
										+ fieldindex + "].selectedChoices",
										"custom.field.required",
										"Please provide a value for the '"
												+ customField.getFieldLabel()
												+ "' field.");
								tempCustomField.setStatus(2);
							} else if (tempCustomField.getSelectedChoices().length == 0) {
								errors.rejectValue("missingCustomFields["
										+ fieldindex + "].selectedChoices",
										"custom.field.required",
										"Please provide a value for the '"
												+ customField.getFieldLabel()
												+ "' field.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					} else {
						/*
						 * Object[] errorArgs = new Object[1]; errorArgs[0] =
						 * customField.getFieldLabel();
						 * ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						 * "customFields["
						 * +fieldindex+"].customFieldValueRef.value" ,
						 * "custom.field.required",
						 * errorArgs,customField.getFieldLabel
						 * ()+" is required");
						 */
						if (customFieldValue.getValue() == null) {
							errors.rejectValue("missingCustomFields["
									+ fieldindex
									+ "].customFieldValueRef.value",
									"custom.field.required",
									"Please provide a value for the '"
											+ customField.getFieldLabel()
											+ "' field.");
							tempCustomField.setStatus(2);
						} else if (StringUtils.isBlank(customFieldValue
								.getValue().toString())) {
							errors.rejectValue("missingCustomFields["
									+ fieldindex
									+ "].customFieldValueRef.value",
									"custom.field.required",
									"Please provide a value for the '"
											+ customField.getFieldLabel()
											+ "' field.");
							tempCustomField.setStatus(2);
						} else {
							tempCustomField.setStatus(1);
						}
					}
				}
				if (customField instanceof NumericCusomField) {
					if (customFieldValue.getValue() != null) {
						if (StringUtils.isNotBlank(customFieldValue.getValue()
								.toString())) {
							if (!CustomFieldValidationUtil
									.isNumeric(customFieldValue.getValue()
											.toString())) {
								errors.rejectValue("missingCustomFields["
										+ fieldindex
										+ "].customFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ " is an invalid number.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					}
				} else if (customField instanceof DateTimeCustomField) {
					if (customFieldValue.getValue() != null) {
						if (StringUtils.isNotBlank(customFieldValue.getValue()
								.toString())) {
							if (!CustomFieldValidationUtil.isValidDate(
									customFieldValue.getValue().toString(),
									true, "MM/dd/yyyy")) {
								errors.rejectValue("missingCustomFields["
										+ fieldindex
										+ "].customFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ "' is an invalid date.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					}
				} else if (customField instanceof SSNCustomFiled) {
					if (customFieldValue.getValue() != null) {
						if (StringUtils.isNotBlank(customFieldValue.getValue()
								.toString())) {
							if (!CustomFieldValidationUtil
									.isSSNValid(customFieldValue.getValue()
											.toString())) {
								errors.rejectValue("missingCustomFields["
										+ fieldindex
										+ "].customFieldValueRef.value",
										"custom.field.required", "'"
												+ customField.getFieldLabel()
												+ "' is an invalid SSN Number.");
								tempCustomField.setStatus(2);
							} else {
								tempCustomField.setStatus(1);
							}
						}
					}
				}
				fieldindex = fieldindex + 1;
			}
		}
	}

}