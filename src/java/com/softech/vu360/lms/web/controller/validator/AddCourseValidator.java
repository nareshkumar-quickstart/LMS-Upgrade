/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.InstructorService;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.instructor.CourseDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Tahir Mehmood
 * 
 */
public class AddCourseValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddCourseValidator.class.getName());
	private String[] fileFormate = { ".jpeg", ".jpg", ".png", ".pdf" };
	private InstructorService instructorService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;

	/**
	 * 
	 */
	public AddCourseValidator() {
		// TODO Auto-generated constructor stub
	}

	public boolean supports(Class clazz) {
		return CourseDetails.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CourseDetails courseDetails = (CourseDetails) obj;
		// TODO call the individual page validation routines
		if (courseDetails.getCourseType().equals(
				HomeworkAssignmentCourse.COURSE_TYPE)
				|| courseDetails.getCourseType().equals(
						InstructorConnectCourse.COURSE_TYPE)) {
			
			validateCourseAdditionalDetails(courseDetails, errors);

		} else {
			validatePage1(courseDetails, errors);
			// validatePage2(courseDetails, errors);
			validatePage4(courseDetails, errors);
			validateCourseAdditionalDetails(courseDetails, errors);
		}
	}

	public void validatePage1(CourseDetails courseDetails, Errors errors) {
		log.debug("inside validatePage1");

		if (StringUtils.isBlank(courseDetails.getCourseName())) {
			errors.rejectValue("courseName",
					"error.addCourse.courseName.required");
		}
		if (StringUtils.isBlank(courseDetails.getKeywords())) {
			errors.rejectValue("keywords", "error.addCourse.keyword.required");
		}
		if(courseDetails.getTabName()!=null && courseDetails.getTabName().equalsIgnoreCase("summeryTab")){
		/*	if (StringUtils.isBlank(courseDetails.getCourseName())) {
				errors.rejectValue("courseName",
						"error.addCourse.courseName.required");
			}
			if (StringUtils.isBlank(courseDetails.getKeywords())) {
				errors.rejectValue("keywords", "error.addCourse.keyword.required");
			}*/
			
			
			if (StringUtils.isBlank(courseDetails.getVersion())) 
				errors.rejectValue("version", "error.addCourse.version.required");
	
			if (StringUtils.isBlank(courseDetails.getDescription())) 
				errors.rejectValue("description", "error.addCourse.description.required");
		}
		
		
		if(courseDetails.getTabName()!=null && courseDetails.getTabName().equalsIgnoreCase("additionalDetailsTab")){
			if (StringUtils.isBlank(courseDetails.getCurrency())) 
				errors.rejectValue("currency", "error.addCourse.currency.required");
			
			if (StringUtils.isBlank(courseDetails.getApprovalNumber())) 
				errors.rejectValue("approvalNumber", "error.addCourse.ApprovalNumber.required");
			
	    	try{
		    	if(courseDetails.getProductPrice()==null || courseDetails.getProductPrice().equals("") || Double.parseDouble(courseDetails.getProductPrice()) < 1){
		    		errors.rejectValue("productPrice", "error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
		    	}
	    	} catch (Exception sube) {
					errors.rejectValue("productPrice", "error.instructor.editCourseAdditionalDetails.ProductPrice.greaterThenZero.invalid");
			}
		}
    	
    	
		if (!StringUtils.isBlank(courseDetails.getCreditHours())) {
			if (!StringUtils.isNumeric(courseDetails.getCreditHours())) {
				errors.rejectValue("creditHours",
						"error.addCourse.credithours.numericonly");
			}
		}
		if (courseDetails.getCourseType().equals(WebLinkCourse.COURSE_TYPE)) {
			if (StringUtils.isBlank(courseDetails.getLink())) {
				errors.rejectValue("link",
						"error.instructor.addCourse.courseLink.required");
			}

		}
		//LMS-15747
	    try{
		    if (courseDetails.getDurationHours() != null && 
		    		!courseDetails.getDurationHours().equals("") &&
		    		 		Double.parseDouble(courseDetails.getDurationHours()) < 0.0 ) {
		    	
		    			errors.rejectValue("durationHours", "error.instructor.addEditCourse.durationField.dataType.errorMessage");
		    }
	    } catch(NumberFormatException exSub){
	    				errors.rejectValue("durationHours", "error.instructor.addEditCourse.durationField.dataType.errorMessage");
	    }
	    

		// if (courseDetails.getDescription().equalsIgnoreCase("<br>") ||
		// StringUtils.isBlank(courseDetails.getDescription()) ){
		// errors.rejectValue("description",
		// "error.addCourse.description.required");
		// }

	}

	public void validatePage2(CourseDetails courseDetails, Errors errors) {
		log.debug("inside SCORM PACKAGE");

		if ((courseDetails.getScormPackages() == null || courseDetails
				.getScormPackages().size() == 0)
				&& (courseDetails.getFile() == null || courseDetails.getFile()
						.isEmpty())) {
			errors.rejectValue("file",
					"error.addCourse.SCORMPackage.file.required");
		}
	}

	public void validatePage4(CourseDetails courseDetails, Errors errors) {
		log.debug("inside SYNCHRONOUS COURSE");

		if (StringUtils.isBlank(courseDetails.getCourseName())) {
			errors.rejectValue("courseName",
					"error.addCourse.courseName.required");
		}
		if (StringUtils.isBlank(courseDetails.getKeywords())) {
			errors.rejectValue("keywords", "error.addCourse.keyword.required");
		}
		if (!StringUtils.isBlank(courseDetails.getCreditHours())) {
			if (!StringUtils.isNumeric(courseDetails.getCreditHours())) {
				errors.rejectValue("creditHours",
						"error.addCourse.credithours.numericonly");
			}
		}
		// if (courseDetails.getDescription().equalsIgnoreCase("<br>") ||
		// StringUtils.isBlank(courseDetails.getDescription()) ){
		// errors.rejectValue("description",
		// "error.addCourse.description.required");
		// }

	}

	public void validateCourseAdditionalDetails(CourseDetails courseDetails,
			Errors errors) {
		log.debug("inside validateCourseAdditionalDetails");

		if (!StringUtils.isBlank(courseDetails.getMsrp())) {
			try {
				if (Double.parseDouble(courseDetails.getMsrp()) < 0.0) {
					errors.rejectValue("msrp",
							"error.instructor.editCourseAdditionalDetails.msrp.numeric");
				}
			} catch (NumberFormatException e) {
				errors.rejectValue("msrp",
						"error.instructor.editCourseAdditionalDetails.msrp.invalid");
			}
		}

		if (!StringUtils.isBlank(courseDetails.getApprovedCourseHours())) {
			try {
				if (Double.parseDouble(courseDetails.getApprovedCourseHours()) < 0.0) {
					errors.rejectValue("approvedCourseHours",
							"error.instructor.editCourseAdditionalDetails.approvedCourseHours.numeric");
				}
			} catch (NumberFormatException e) {
				errors.rejectValue("approvedCourseHours",
						"error.instructor.editCourseAdditionalDetails.approvedCourseHours.invalid");
			}
		}
		if (!StringUtils.isBlank(courseDetails.getApprovalNumber())) {
			if (!FieldsValidation.isValidAlphaNumeric(courseDetails
					.getApprovalNumber())) {
				errors.rejectValue("approvalNumber",
						"error.instructor.editCourseAdditionalDetails.approvalNumber.invalid");
			}
		}

		if (!StringUtils.isBlank(courseDetails.getProductPrice())) {
			if (FieldsValidation.isNumeric(courseDetails.getProductPrice())) {
				if (Double.valueOf(courseDetails.getProductPrice()) < 0) {
					errors.rejectValue("productPrice",
							"error.instructor.editCourseAdditionalDetails.productPrice.lessthanzero");
				}
			} else {
				errors.rejectValue("wholeSalePrice",
						"error.instructor.editCourseAdditionalDetails.productPrice.invalid");
			}
		}
		if (!StringUtils.isBlank(courseDetails.getWholeSalePrice())) {
			try {
				if (Double.parseDouble(courseDetails.getWholeSalePrice()) < 0.0) {
					errors.rejectValue("wholeSalePrice",
							"error.instructor.editCourseAdditionalDetails.wholeSalePrice.numeric");
				}
			} catch (NumberFormatException e) {
				errors.rejectValue("wholeSalePrice",
						"error.instructor.editCourseAdditionalDetails.wholeSalePrice.invalid");
			}
		}

		if (!StringUtils.isBlank(courseDetails.getTermsOfService())) {
			try {
				if (Integer.parseInt(courseDetails.getTermsOfService()) < 0.0) {
					errors.rejectValue("termsOfService",
							"error.instructor.editCourseAdditionalDetails.termofservices.numeric");
				}
			} catch (NumberFormatException e) {
				errors.rejectValue("termsOfService",
						"error.instructor.editCourseAdditionalDetails.termofservices.invalid");
			}
		}

	}

	public void validatePage5(CourseDetails courseDetails, Errors errors) {
		log.debug("inside validatePage1");

		if (StringUtils.isBlank(courseDetails.getCourseName())) {
			errors.rejectValue("courseName",
					"error.addCourse.courseName.required");
		}
		if (StringUtils.isBlank(courseDetails.getKeywords())) {
			errors.rejectValue("keywords", "error.addCourse.keyword.required");
		}
		if (!StringUtils.isBlank(courseDetails.getCreditHours())) {
			if (!StringUtils.isNumeric(courseDetails.getCreditHours())) {
				errors.rejectValue("creditHours",
						"error.addCourse.credithours.numericonly");
			}
		}
		if (StringUtils.isBlank(courseDetails.getLink())) {
			errors.rejectValue("link",
					"error.instructor.addCourse.courseLink.required");
		}
		// if (courseDetails.getDescription().equalsIgnoreCase("<br>") ||
		// StringUtils.isBlank(courseDetails.getDescription()) ){
		// errors.rejectValue("description",
		// "error.addCourse.description.required");
		// }

	}

	public void validatePage6(CourseDetails courseDetails, Errors errors,
			Brander brander) {
		log.debug("inside validatePage6");
		log.debug("Brander = " + brander);

		String fileName = null;
		
		if(courseDetails.isBlankHomeAssignementfile())
		{
			errors.rejectValue("file", "error.addCourse.file.required");
		}
		
		/*
		if (courseDetails.getFile() == null
				|| courseDetails.getFile().isEmpty()) {
			errors.rejectValue("file", "error.addCourse.file.required");
		}*/
		
		

		/*
		 * if (courseDetails.getFile().getSize() > 10000) {
		 * errors.rejectValue("file", "error.empty.file",
		 * "File size more than 10000 bytes "); }
		 */

		if (StringUtils.isBlank(courseDetails.getCourseName())) {
			errors.rejectValue("courseName",
					"error.addCourse.courseName.required");
		}
		if (StringUtils.isBlank(courseDetails.getKeywords())) {
			errors.rejectValue("keywords", "error.addCourse.keyword.required");
		}
		if (!StringUtils.isBlank(courseDetails.getCreditHours())) {
			if (!StringUtils.isNumeric(courseDetails.getCreditHours())) {
				errors.rejectValue("creditHours",
						"error.addCourse.credithours.numericonly");
			}
		}

		/*
		 * if (StringUtils.isBlank(courseDetails.getHwAssignmentInstruction()))
		 * { errors.rejectValue("hwAssignmentInstruction",
		 * "error.addCourse.hwassigninstruction.required"); }
		 */

		if (courseDetails.getGradingMethod().equals("Scored")) {
			if (StringUtils.isBlank(courseDetails.getMasterScore())) {
				errors.rejectValue("masterScore",
						"error.addCourse.masteryscore.required");
			} else {
				try {
					if (Double.parseDouble(courseDetails.getMasterScore()) > 100) {
						errors.rejectValue("masterScore",
								"error.instructor.editCourseAdditionalDetails.masterscore.valid");
					}
				} catch (NumberFormatException e) {
					errors.rejectValue("masterScore",
							"error.instructor.editCourseAdditionalDetails.masterscore.numeric");
				}

			}
		}

		if (!StringUtils.isBlank(courseDetails.getAssignmentDueDate())) {

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date expirationDate = null;
			Date date = new Date();
			try {
				expirationDate = formatter.parse(courseDetails
						.getAssignmentDueDate());
				if (!formatter.format(expirationDate).equals(
						courseDetails.getAssignmentDueDate())) {
					errors.rejectValue("assignmentDueDate",
							"error.instructor.editCourseAdditionalDetails.assignmentDueDate.date");

				} else {
					if (expirationDate.before(date)) {
						errors.rejectValue("assignmentDueDate",
								"error.instructor.editCourseAdditionalDetails.assignmentDueDate.date");

					}
				}

			} catch (ParseException e) {
				e.printStackTrace();
				errors.rejectValue("assignmentDueDate",
						"error.instructor.editCourseAdditionalDetails.assignmentDueDate.date");
			}

		}

		// if (courseDetails.getDescription().equalsIgnoreCase("<br>") ||
		// StringUtils.isBlank(courseDetails.getDescription()) ){
		// errors.rejectValue("description",
		// "error.addCourse.description.required");
		// }

	}

	public void validatePage7(CourseDetails courseDetails, Errors errors) {

		log.debug("validatePage7");
		log.debug("getInstructorType " + courseDetails.getInstructorType());
		log.debug("getMeetingId " + courseDetails.getMeetingId());
		log.debug("getMeetingPasscode " + courseDetails.getMeetingPasscode());
		log.debug("getEmailAddress " + courseDetails.getEmailAddress());

		if (courseDetails.getInstructorType().equals("WebEx")) {

			if (StringUtils.isBlank(courseDetails.getMeetingId())) {

				errors.rejectValue("meetingId",
						"error.instructor.editCourseAdditionalDetails.meetingId.empty");
			}

			if (StringUtils.isBlank(courseDetails.getMeetingPasscode())) {
				errors.rejectValue("meetingPasscode",
						"error.instructor.editCourseAdditionalDetails.meetingPasscode.empty");
			}

		}
		
		if (StringUtils.isBlank(courseDetails.getEmailAddress())) {

		} else if (!FieldsValidation.isEmailValid(courseDetails
				.getEmailAddress())) {
			errors.rejectValue("emailAddress",
					"error.instructor.editCourseAdditionalDetails.emailAddress.invalid");
		}
		

		if (StringUtils.isBlank(courseDetails.getCourseName())) {
			errors.rejectValue("courseName",
					"error.addCourse.courseName.required");
		}
		if (StringUtils.isBlank(courseDetails.getKeywords())) {
			errors.rejectValue("keywords", "error.addCourse.keyword.required");
		}
		
		if (!StringUtils.isBlank(courseDetails.getCreditHours())) {
			if (!StringUtils.isNumeric(courseDetails.getCreditHours())) {
				errors.rejectValue("creditHours",
						"error.addCourse.credithours.numericonly");
			}
		}

	}

	public void validateEditHW(CourseDetails courseDetails, Errors errors,
			Brander brander) {
		log.debug("inside validateEditHW");

		if(courseDetails.isBlankHomeAssignementfile())
		{
			errors.rejectValue("file", "error.addCourse.file.required");
		}
		
		if (courseDetails.getFile() == null
				|| courseDetails.getFile().isEmpty()) {

		} else {
			String fileName = courseDetails.getFile().getOriginalFilename();
			long fileSize = courseDetails.getFile().getSize();
			boolean fileError = false;
			if (!FileUploadUtils
					.validFileFormate(
							fileName,
							brander.getBrandElement("lms.hwassignmentUpload.extension"))) {
				fileError = true;
			}

			if (fileError) {
				errors.rejectValue("file",
						"error.instructor.addCourse.file.save.failed");
			} else {
				Long maxFileSize = Long.parseLong(brander.getBrandElement(
						"lms.hwassignmentUpload.maximumFileSize").trim());
				log.debug("Max File Size : " + maxFileSize);
				log.debug("Actual File Size : " + fileSize);

				if (fileSize > maxFileSize) {
					errors.rejectValue("",
							"lms.hwassignmentUpload.fileSizeNotice");
				}
			}

		}

		/*
		 * if (courseDetails.getFile().getSize() > 10000) {
		 * errors.rejectValue("file", "error.empty.file",
		 * "File size more than 10000 bytes "); }
		 */

		if (StringUtils.isBlank(courseDetails.getCourseName())) {
			errors.rejectValue("courseName",
					"error.addCourse.courseName.required");
		}
		if (StringUtils.isBlank(courseDetails.getKeywords())) {
			errors.rejectValue("keywords", "error.addCourse.keyword.required");
		}
		if (!StringUtils.isBlank(courseDetails.getCreditHours())) {
		    try
		    {
			BigDecimal decimal = new BigDecimal(courseDetails.getCreditHours());
			BigDecimal checkDecimal = decimal.movePointRight(1);
			if(checkDecimal.scale()>0) {
			    throw new Exception(); 
			}
		    }catch(Exception e){
			errors.rejectValue("creditHours","error.addCourse.credithours.numericonly");
		    }
		}

		/*
		 * if (StringUtils.isBlank(courseDetails.getHwAssignmentInstruction()))
		 * { errors.rejectValue("hwAssignmentInstruction",
		 * "error.addCourse.hwassigninstruction.required"); }
		 */

		if (courseDetails.getGradingMethod().equals("Scored")) {
			if (StringUtils.isBlank(courseDetails.getMasterScore())) {
				errors.rejectValue("masterScore",
						"error.addCourse.masteryscore.required");
			} else {
				try {
					if (Double.parseDouble(courseDetails.getMasterScore()) > 100) {
						errors.rejectValue("masterScore",
								"error.instructor.editCourseAdditionalDetails.masterscore.valid");
					}
				} catch (NumberFormatException e) {
					errors.rejectValue("masterScore",
							"error.instructor.editCourseAdditionalDetails.masterscore.numeric");
				}

			}
		}

		if (!StringUtils.isBlank(courseDetails.getAssignmentDueDate())) {

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date expirationDate = null;
			Date date = new Date();
			try {
				expirationDate = formatter.parse(courseDetails
						.getAssignmentDueDate());
				if (!formatter.format(expirationDate).equals(
						courseDetails.getAssignmentDueDate())) {
					errors.rejectValue("assignmentDueDate",
							"error.instructor.editCourseAdditionalDetails.assignmentDueDate.date");

				} else {
					// comments by K.O 
					/*if (expirationDate.before(date)) {
						errors.rejectValue("assignmentDueDate",
								"error.instructor.editCourseAdditionalDetails.assignmentDueDate.date");

					}*/
				}

			} catch (ParseException e) {
				e.printStackTrace();
				errors.rejectValue("assignmentDueDate",
						"error.instructor.editCourseAdditionalDetails.assignmentDueDate.date");
			}

		}

		// if (courseDetails.getDescription().equalsIgnoreCase("<br>") ||
		// StringUtils.isBlank(courseDetails.getDescription()) ){
		// errors.rejectValue("description",
		// "error.addCourse.description.required");
		// }

	}
	
	/**
	 * @param instructorService the instructorService to set
	 */
	public void setInstructorService(InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	/**
	 * @return the instructorService
	 */
	public InstructorService getInstructorService() {
		return instructorService;
	}
	
	/**
	 * @param instructorService the instructorService to set
	 */
	public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	/**
	 * @return the instructorService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	
}
