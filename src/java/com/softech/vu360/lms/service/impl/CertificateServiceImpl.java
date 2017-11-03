package com.softech.vu360.lms.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.softech.vu360.lms.exception.NoCertificateNumberFoundException;
import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TextCreditReportingField;
import com.softech.vu360.lms.model.TextCustomField;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.util.AssetUtil;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.PdfFieldsEnum;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;


/**
 * 
 * @author haider.ali
 *
 */
public class CertificateServiceImpl implements CertificateService {
	private AccreditationService accreditationService = null;
	private EntitlementService entitlementService;
	private StatisticsService statisticsService;
	//PIR-2666
	//private BusinessObjectSequenceService businessObjectSequenceService = null;	
	//private static final String BOSEQ_OSHA_CERTIFICATE = "OSHACertificate";
	private CustomFieldService customFieldService;
	/**
	 * 
	 * @param lcs
	 * @param courseApproval
	 */
	public synchronized void assignCompletionCertificateNo(LearnerCourseStatistics lcs,CourseApproval courseApproval) throws NoCertificateNumberFoundException {

		if ( StringUtils.isBlank(lcs.getCertificateNumber()) ) {
			String certificateNumber = null;
			
			if(courseApproval!=null && courseApproval.getUsePurchasedCertificateNumbers()){
				PurchaseCertificateNumber pcn= accreditationService.getUnusedPurchaseCertificateNumber(courseApproval.getId()); //courseApproval.getNextPurchansedCertificateNumber();
				if(pcn!=null){
                    pcn= accreditationService.loadForUpdatePurchaseCertificateNumber(pcn.getId());
                    certificateNumber= pcn.getCertificateNumber();
                    pcn.setUsed(true);
                    pcn.setUsed(String.valueOf(true));
                    accreditationService.savePurchaseCertificateNumber(pcn);
				}
				else{
					throw new NoCertificateNumberFoundException("No Certificate numbers available");
				}
			}
			else if(courseApproval!=null && courseApproval.getUseCertificateNumberGenerator()){
				
				String digitChar = "#";
				
				String prefix = courseApproval.getCertificateNumberGeneratorPrefix() == null ? "" : courseApproval.getCertificateNumberGeneratorPrefix();
				StringBuffer formatString = new StringBuffer(courseApproval.getCertificateNumberGeneratorNumberFormat() == null ? "" : courseApproval.getCertificateNumberGeneratorNumberFormat());
				String number = courseApproval.getCertificateNumberGeneratorNextNumber() + "";
				
				if(formatString.equals("")){
					certificateNumber = prefix + number;
				}else{
					
					int count = 0;
					int totalDigitCount = StringUtils.countMatches(formatString.toString(), digitChar);
					
					ArrayList<String> numberArray = null;
					
					if(number.length() < totalDigitCount){
						numberArray = new ArrayList<String>(totalDigitCount);
						for(int i = number.length(); i<totalDigitCount; i++){
							numberArray.add("0");
						}
						
					}else{
						numberArray = new ArrayList<String>(number.length());
					}
					
					int index = formatString.indexOf(digitChar);
					while( index >= 0){
						formatString.replace(index, index+1, "{"+(count)+"}");
						
						if(count < number.length()) {
							numberArray.add(number.charAt(count) + "");
						}
						
			            count++;
			            
			            index = formatString.indexOf(digitChar);			            
					}
					
					while(count < number.length()){
						numberArray.add(number.charAt(count) + "");
						formatString.append("{"+(count++)+"}");
					}
					
					String formatValue = formatString.toString();
					
					if(formatValue != null) {
						formatValue = formatValue.replace("YY", new SimpleDateFormat("yy").format(lcs.getCompletionDate()));
						formatValue = formatValue.replace("MM", new SimpleDateFormat("MM").format(lcs.getCompletionDate()));
					}
					
					certificateNumber = prefix + MessageFormat.format(formatValue, numberArray.toArray());

				}
				
				courseApproval.setCertificateNumberGeneratorNextNumber(courseApproval.getCertificateNumberGeneratorNextNumber() + 1);
				accreditationService.saveCourseApproval(courseApproval);
				
			}
			//commit due to issue (PIR-2666)
			//else{
			   //certificateNumber = this.businessObjectSequenceService.getNextBusinessObjectSequence( BOSEQ_OSHA_CERTIFICATE );
			//}
			
			//save in stats
			if ( StringUtils.isNotBlank(certificateNumber) ) {				
				lcs.setCertificateNumber( certificateNumber );				 
				this.statisticsService.saveLearnerCourseStatistics(lcs);
				
			}			
		}
	}

	public void updateCertificateIssueDate(LearnerCourseStatistics lcs){
		
		if (lcs.getCertificateIssueDate() == null){
			lcs=statisticsService.loadForUpdateLearnerCourseStatistics(lcs.getId());
			lcs.setCertificateIssueDate( new Date() );
			this.statisticsService.saveLearnerCourseStatistics(lcs);			
		}
		
	}
	public String getCertificateURL(Long learnerEnrollmentId){
		LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);		
		LearnerCourseStatistics lcs=le.getCourseStatistics();
		return lcs.getCertificateURL()!=null&& lcs.getCertificateURL().length()>0?lcs.getCertificateURL():null;
	}
	
	public ByteArrayOutputStream generateCertificate(LearnerEnrollment le)throws URISyntaxException, DocumentException,IOException,NoCertificateNumberFoundException {
		return generateCertificate(le, true);
	}
	
	public ByteArrayOutputStream generateCertificate(LearnerEnrollment le, boolean isSelfReported)throws URISyntaxException, DocumentException,IOException,NoCertificateNumberFoundException {		
		
		Brander brander = VU360Branding.getInstance().getBranderByUser(null, le.getLearner().getVu360User());
		LearnerCourseStatistics lcs = statisticsService
				.loadForUpdateLearnerCourseStatistics(le.getCourseStatistics().getId());
		Long courseApprovalId = statisticsService.getLearnerSelectedCourseApprovalByEnrollmentId(le.getId());
		CourseApproval courseApproval = null;

		if (courseApprovalId != null && courseApprovalId.longValue() > 0) {
			courseApproval = accreditationService.loadForUpdateCourseApproval(courseApprovalId);
		}

		List<CreditReportingField> creditReportingFieldList = new ArrayList<CreditReportingField>(
				accreditationService.getCreditReportingFieldsByCourse(le.getCourse()));
		List<CreditReportingFieldValue> creditReportingFieldValues = accreditationService
				.getCreditReportingFieldValue(le.getLearner().getLearnerProfile());
		customFieldService.createValueRecordForStaticReportingField(le.getLearner().getVu360User(),
				creditReportingFieldList, creditReportingFieldValues);
		
		// String tempCertificateLocation= null;
		/* START-SAVE CERTIFICATE DETAILS */
		if (isSelfReported)
			this.assignCompletionCertificateNo(lcs, courseApproval);

		this.updateCertificateIssueDate(lcs);
		/* END-SAVE CERTIFICATE DETAILS */

		Date completionDate = lcs.getCompletionDate();
		if (le.getCourse() instanceof SynchronousCourse || le.getCourse() instanceof WebinarCourse) {
			if (completionDate == null)
				completionDate = le.getSynchronousClass().getClassEndDate();
		}

		StringBuffer filePath = new StringBuffer();
		String completionCertURI = null;
		PdfReader reader = null;

		if (courseApproval != null && courseApproval.getCertificate() != null) {

			filePath.append(AssetUtil.getAssetFilePath(courseApproval.getCertificate()));

		} else if (courseApproval != null && courseApproval.getTemplate() != null) {

			CourseConfiguration courseConfiguration = accreditationService
					.getCourseConfigurationByTemplateId(courseApproval.getTemplate().getId(), true);

			if (courseConfiguration.isCertificateEnabled() != null && courseConfiguration.isCertificateEnabled()) {

				if (courseConfiguration.getCompletionCertificate() != null) {
					filePath.append(AssetUtil.getAssetFilePath(courseConfiguration.getCompletionCertificate()));
				} else {
					// showDefaultCert = true;
				}
			}
		} else if (le.getCourse().getCourseConfigTemplate() != null) {

			CourseConfiguration courseConfiguration = accreditationService
					.getCourseConfigurationByTemplateId(le.getCourse().getCourseConfigTemplate().getId(), true);

			if (courseConfiguration.isCertificateEnabled()) {

				if (courseConfiguration.getCompletionCertificate() != null) {
					filePath.append(AssetUtil.getAssetFilePath(courseConfiguration.getCompletionCertificate()));
				} else {
					// showDefaultCert = true;
				}
			}
		} else {

		}

		InputStream certificateIO = null;

		if (filePath.length() == 0) {

			filePath.append(VU360Properties.getVU360Property("brands.basefolder"));
			completionCertURI = brander.getBrandElement("lms.completionCertificateUrl");
			filePath.append(completionCertURI);
			certificateIO = new FileInputStream(new File(filePath.toString()));

		} else {

			URL url = AssetUtil.getEncodedURL(filePath.toString());
			certificateIO = url.openStream();

		}

		if (certificateIO == null) {
			return null;
		}

		String password = VU360Properties.getVU360Property("lms.server.certificate.SecurityKey");
		reader = new PdfReader(certificateIO);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamp2 = new PdfStamper(reader, baos);

		stamp2.setEncryption(null, password.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);

		// filling in the form
		AcroFields formField = stamp2.getAcroFields();

		StringBuilder fontPath = new StringBuilder();
		fontPath.append(System.getProperty( "catalina.base" )); 
		fontPath.append(brander.getBrandElement("lms.learner.certificate.font.path"));
		BaseFont unicode =  BaseFont.createFont(fontPath.toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		formField.setFieldProperty("name", "textfont", unicode, null);
		
		List<CustomFieldValue> customFieldValues = le.getLearner().getLearnerProfile().getCustomFieldValues();
		/* POPULATE CUSTOM FIELDS DEFAULT */
		if (customFieldValues != null) {
			fillCustomFieldValues(le.getLearner(), customFieldValues, formField);
		}

		// POPULATE MANUALLY ASSOCIATED CUSTOM FIELDS
		List<CertificateBookmarkAssociation> associatedCustomFieldList = accreditationService
				.getCertBookMarkAssociations(PdfFieldsEnum.CUSTOMFIELDS);
		if (associatedCustomFieldList != null && associatedCustomFieldList.size() > 0) {
			fillManuallyMappedCustomFieldValues(le, courseApproval, associatedCustomFieldList, formField);
		}

		// List<CreditReportingFieldValue> creditReportingFieldValues=
		// accreditationService.getCreditReportingFieldValue(le.getLearner().getLearnerProfile());
		/* POPULATE REPORTING FIELDS DEFAULT */
		if (creditReportingFieldValues != null) {
			fillCreditReportingFieldValues(le.getLearner(), creditReportingFieldValues, formField);
		}

		// POPULATE MANUALLY ASSOCIATED LMS FIELDS
		List<CertificateBookmarkAssociation> associatedLMSFieldList = accreditationService
				.getCertBookMarkAssociations(PdfFieldsEnum.LMSFIELDS);
		if (associatedLMSFieldList != null && associatedLMSFieldList.size() > 0) {
			fillLMSFieldValues(lcs, le, courseApproval, associatedLMSFieldList, formField);
		}

		// POPULATE MANUALLY ASSOCIATED REPORTING FIELDS
		List<CertificateBookmarkAssociation> associatedCRFList = accreditationService
				.getCertBookMarkAssociations(PdfFieldsEnum.REPORTINGFIELDS);
		if (associatedCRFList != null && associatedCRFList.size() > 0) {
			fillManuallyMappedCreditReportingFieldValues(le, courseApproval, associatedCRFList, formField);
		}

		stamp2.setFormFlattening(true);
		stamp2.close();

		if (certificateIO != null) {
			certificateIO.close();
			reader.close();
		}
		return baos;                              
	}
	
	private void fillCreditReportingFieldValues(Learner learner,List<CreditReportingFieldValue> creditReportingFieldValues, AcroFields formField) throws IOException, DocumentException{		
		CreditReportingField field = null;
			String valueStr=null;
			for(CreditReportingFieldValue value:creditReportingFieldValues){
				field = value.getReportingCustomField();
				if(field instanceof StaticCreditReportingField){
					value= accreditationService.getCreditReportingFieldValue(field,learner.getLearnerProfile());
					valueStr= value.getValue().toString();
				}else if((field instanceof TextCreditReportingField) || (field instanceof SingleSelectCreditReportingField) || (field instanceof NumericCreditReportingField) || (field instanceof DateTimeCreditReportingField)){
					valueStr= this.extractLastFourDigitsIfSSNField(value);										
				}
				formField.setField(value.getReportingCustomField().getFieldLabel(),valueStr);
			}
	}
	
	private void fillCustomFieldValues(Learner learner,List<CustomFieldValue> customFieldValues, AcroFields formField) throws IOException, DocumentException{		
		CustomField field = null;
 		String valueStr=null;
		for(CustomFieldValue value:customFieldValues){
			field = value.getCustomField();			
			if((field instanceof TextCustomField) || (field instanceof SingleSelectCustomField) || (field instanceof NumericCusomField) || (field instanceof DateTimeCustomField)){
				valueStr= this.extractLastFourDigitsIfSSNField(value);					
				formField.setField(value.getCustomField().getFieldLabel(),valueStr);
			}
		}
 }
	
	/**
	 * fills manually mapped credit reporting fields mapped from user interface
	 * @param learner
	 * @param lstCertBookmarkAssociation
	 * @param formField
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void fillManuallyMappedCreditReportingFieldValues(LearnerEnrollment le,CourseApproval courseApproval,List<CertificateBookmarkAssociation> lstCertBookmarkAssociation, AcroFields formField) throws IOException, DocumentException{				
		String valueStr=null;
		for(CertificateBookmarkAssociation certBookmark : lstCertBookmarkAssociation){    	    		
    		 CreditReportingFieldValue value= accreditationService.getCreditReportingFieldValue(certBookmark.getReportingField(),le.getLearner().getLearnerProfile());
    		 valueStr=this.extractLastFourDigitsIfSSNField(value);    		     		     		 
    		 formField.setField(certBookmark.getBookmarkLabel(), valueStr);
    		}
	}
	
	private void fillManuallyMappedCustomFieldValues(LearnerEnrollment le,CourseApproval courseApproval,List<CertificateBookmarkAssociation> lstCertBookmarkAssociation, AcroFields formField) throws IOException, DocumentException{				
		String valueStr=null;
		for(CertificateBookmarkAssociation certBookmark : lstCertBookmarkAssociation){    	    		
    		 CustomFieldValue value= le.getLearner().getLearnerProfile().getCustomFieldValue(certBookmark.getCustomField());    		     		     		     		     		 
    		 valueStr=this.extractLastFourDigitsIfSSNField(value);    		     		     		 
    		 formField.setField(certBookmark.getBookmarkLabel(), valueStr);
    		}
	}
	
	private String extractLastFourDigitsIfSSNField(CreditReportingFieldValue value){
		String valueStr = "";
		if(value != null){
			Object oValue = value.getValue();
			if(oValue != null)
			{
				valueStr=oValue.toString();				
				if(value.getReportingCustomField() instanceof SSNCreditReportingFiled){
				   if(valueStr.length()>=4){
					   valueStr = valueStr.substring(valueStr.length()-4);
				   }
				}
			}
		}
		 return valueStr;
	}
	
	private String extractLastFourDigitsIfSSNField(CustomFieldValue value){
		String valueStr = "";
		if(value != null){
			Object oValue = value.getValue();
			if(oValue != null)
			{
				valueStr= oValue.toString();				
				if(value.getCustomField() instanceof SSNCustomFiled){
				   if(valueStr.length()>=4){
					   valueStr = valueStr.substring(valueStr.length()-4);
				   }
				}
			}
		}
		 return valueStr;
	}
	/**
	 * fills manually LMS fields mapped from user interface
	 * @param learner
	 * @param lstCertBookmarkAssociation
	 * @param formField
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void fillLMSFieldValues(LearnerCourseStatistics lcs,LearnerEnrollment le,CourseApproval courseApproval,List<CertificateBookmarkAssociation> lstCertBookmarkAssociation, AcroFields formField) throws IOException, DocumentException{				
		for(CertificateBookmarkAssociation certBookmark : lstCertBookmarkAssociation)    	
     		formField.setField(certBookmark.getBookmarkLabel(), getBookmarkValue(lcs,le,courseApproval,certBookmark.getValue()));    	
	}
	
	/**
	    * Gets bookmark values against fieldLabel mapped using Certificate association mapped screen
	    * @param value
	    * @param learner
	    * @return
	    */
		public String getBookmarkValue(LearnerCourseStatistics lcs, LearnerEnrollment le,CourseApproval courseApproval,String fieldLabel)
		{
			String value = "";
			if(le == null)
				return value;
			
			Brander brander = VU360Branding.getInstance().getBranderByUser(null, le.getLearner().getVu360User());	
			String firstName = le.getLearner().getVu360User().getFirstName();
			String middleName = le.getLearner().getVu360User().getMiddleName();
			String lastName = le.getLearner().getVu360User().getLastName();
			
			if(middleName == null || (middleName !=null && middleName.isEmpty())){				
				middleName="";
			}
			
			if(fieldLabel.equals("First Name"))
				value = firstName;
			if(fieldLabel.equals("Middle Initial"))
				value = middleName;
			if(fieldLabel.equals("Last Name"))
				value = lastName;
			if(fieldLabel.equals("Full Name"))
				value = firstName +" "+middleName+" "+lastName;
			
			if(le.getLearner().getVu360User().getLearner() != null && le.getLearner().getVu360User().getLearner().getLearnerProfile() != null && le.getLearner().getVu360User().getLearner().getLearnerProfile().getLearnerAddress() != null)
			{
				if(fieldLabel.equals("Address"))
					value = le.getLearner().getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress();
				if(fieldLabel.equals("City"))
					value = le.getLearner().getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getCity();
				if(fieldLabel.equals("State"))
					value = le.getLearner().getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getState();
				if(fieldLabel.equals("Zip Code"))
					value = le.getLearner().getVu360User().getLearner().getLearnerProfile().getLearnerAddress().getZipcode();
				if(fieldLabel.equals("Phone Number"))
					value = le.getLearner().getVu360User().getLearner().getLearnerProfile().getMobilePhone();
			}
			
			if(fieldLabel.equals("Course Completion Date"))
			{
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				Date completionDate = lcs.getCompletionDate();
				if(completionDate != null){
					value = dateFormatter.format(completionDate);
				}
			}
			/*Short fix for TABC certificate expiration, need to be changed when permanent fix is done through LMS-12992
			 * please change code sequence as per course Approval and course level precedence with respect to months */
			
			if(fieldLabel.equals("Certificate Validation")){
 				
				if(le!= null){
				String strEnrollmentId = le.getId().toString();
			    int requiredLengthAfterPadding = 12;
			    int inputStringLengh = strEnrollmentId.length();
			    int diff = requiredLengthAfterPadding - inputStringLengh;
			    if (inputStringLengh < requiredLengthAfterPadding){
			    	strEnrollmentId = new String(new char[diff]).replace("\0", "0")+ strEnrollmentId;
			    	value = strEnrollmentId;
			      }
				}
			}
				
			//LMS-20652
			if(fieldLabel.equals("SID")){
			  				
			 				if(le!= null && le.getLearner() != null){
			 				String strLearnerId = le.getLearner().getId().toString();
			 			    	value = strLearnerId;
			 			      }
			 				
			}
			
			if(fieldLabel.equals("Certificate Expiration Date"))
			{
 				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				Date certifcateExpirationDate = lcs.getCompletionDate();
				String autocomputeCertificateExpirationDate = VU360Properties.getVU360Property("lms.certificateexpirationperiod.enabled");  
				if (certifcateExpirationDate!=null){
					if(autocomputeCertificateExpirationDate.equalsIgnoreCase("TRUE")){
					  if(courseApproval != null){
						Calendar cal = Calendar.getInstance();
						Integer certificateExpirationPeriod = courseApproval.getCertificateExpirationPeriod();
						if(certificateExpirationPeriod != null){
							if(certificateExpirationPeriod > 0){
								cal.setTime(certifcateExpirationDate);
								//cal.add(Calendar.YEAR, 2);  
								cal.add(Calendar.DATE, certificateExpirationPeriod);
								certifcateExpirationDate = cal.getTime();					
								if(certifcateExpirationDate != null){
									value = dateFormatter.format(certifcateExpirationDate);
								  }
							 }
							
						}
					  }
					 else{
						Calendar cal = Calendar.getInstance();				
						cal.setTime(certifcateExpirationDate);
						cal.add(Calendar.YEAR, 2);  
						cal.add(Calendar.DATE, -1);
						certifcateExpirationDate = cal.getTime();					
						if(certifcateExpirationDate != null){
							value = dateFormatter.format(certifcateExpirationDate);
						}
					 }
					}
					else
					{
						Calendar cal = Calendar.getInstance();				
						cal.setTime(certifcateExpirationDate);
						cal.add(Calendar.YEAR, 2);  
						cal.add(Calendar.DATE, -1);
						certifcateExpirationDate = cal.getTime();					
						if(certifcateExpirationDate != null){
							value = dateFormatter.format(certifcateExpirationDate);
						}
					}
				}				
			}
			

			//3years expiration
			if(fieldLabel.equals("Certificate Expiration Date - 3 years"))
			{
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				Date certifcateExpirationDate = lcs.getCompletionDate();
				if (certifcateExpirationDate!=null){
					Calendar cal = Calendar.getInstance();				
					cal.setTime(certifcateExpirationDate);
					cal.add(Calendar.YEAR, 3);  
					cal.add(Calendar.DATE, -1);
					certifcateExpirationDate = cal.getTime();					
					if(certifcateExpirationDate != null){
						value = dateFormatter.format(certifcateExpirationDate);
					}	
				}				
			}
			
			/*Short fix for TABC certificate expiration, need to be changed when permanent fix is done through LMS-12992
			 * please change code sequence as per course Approval and course level precedence with respect to months */
			if(fieldLabel.equals("Certificate Number"))
				value = lcs.getCertificateNumber();
			if(fieldLabel.equals("Course Registration Date"))
				value = le.getEnrollmentDate().toString();
			if(fieldLabel.equals("School Name"))
				value = le.getLearner().getCustomer().getName();
			if(fieldLabel.equals("Course Name"))
				value = brander.getBrandElement("lms.completionCertificate.appendtextWithCoursename") + " " + le.getCourse().getCourseTitle();
			
			if(fieldLabel.equals("Final Exam Score")){
				if(lcs.getHighestPostTestScore()>0)
					value = Double.toString(lcs.getHighestPostTestScore());
				else
					value ="N/A";
			}
			if(fieldLabel.equals("Credit Hours")){
				//value = le.getCourse().getCredithour();
				value = String.valueOf(le.getCourse().getCeus());
				if(value == null || value.trim().length() == 0){
					value = "0.00";// set default value as appearing in LCMS.//LMS-12928
				}
			}
//			if(fieldLabel.equals("Course State"))
//				value = user.getFirstName();
			if(fieldLabel.equals("Course Status"))
//				value = lcs.getStatus();
				value = lcs.getStatusDisplayText();
			if(fieldLabel.equals("Course Start Date")) {
				if(lcs.getFirstAccessDate() == null)
					value = "";
				else
					value = lcs.getFirstAccessDate().toString();
			}
			if(fieldLabel.equals("Course Start Time")) {
				if(le.getEnrollmentDate() == null)
					value = "";
				else
					value = le.getEnrollmentStartDate().toString();
			}
			if(fieldLabel.equals("Course End Time")) {
				if(le.getEnrollmentEndDate() == null)
					value = "";
				else
					value = le.getEnrollmentEndDate().toString();
			}
				
//			if(fieldLabel.equals("SSN last 4 digits"))
//				value = user.getFirstName();
			if(courseApproval != null)
			{
				if(fieldLabel.equals("Approved Course Name"))
					value = courseApproval.getApprovedCourseName();
				if(fieldLabel.equals("Course Expiration Date"))
					value = courseApproval.getCourseApprovalEffectivelyEndsDate().toString();
				if(fieldLabel.equals("Provider Name"))
					value = courseApproval.getProvider().getName();
				if(fieldLabel.equals("Course Approval Number"))
					value = courseApproval.getCourseApprovalNumber();
				if(fieldLabel.equals("Approved Credit Hours"))
					value = courseApproval.getApprovedCreditHours();
				if(fieldLabel.equals("Instructor Name"))
					value = getInstructorApprovalByRegulatorCategory(courseApproval,fieldLabel);
				if(fieldLabel.equals("Instructor Number"))
					value = getInstructorApprovalByRegulatorCategory(courseApproval,fieldLabel);
				if(fieldLabel.equals("Course Approval Type"))
					value = courseApproval.getCourseApprovaltype();
			}
			return value;
		}
		
	public String getInstructorApprovalByRegulatorCategory(CourseApproval courseApproval, String fieldLabel)
	{
		String value = "";
		Long[] catIds = null;
		List<RegulatorCategory>  regulatorCategoryList = null;
		InstructorApproval instructorApproval = null;
		if(courseApproval != null)
		{
			 regulatorCategoryList = courseApproval.getRegulatorCategories();
			 if(regulatorCategoryList != null && regulatorCategoryList.size() > 0)
			 {
				 catIds = new Long[regulatorCategoryList.size()];
				 for(int i =0; i < regulatorCategoryList.size();i++)
				 {
					 catIds[i] = regulatorCategoryList.get(i).getId();
				 }
				 List<InstructorApproval> instructorApprovalList = accreditationService.getInstructorApprovalsByCatIds(catIds);
				 if(instructorApprovalList != null && instructorApprovalList.size() > 0)
				 {
					 instructorApproval = instructorApprovalList.get(0);
					 if(instructorApproval != null)
					 {
						 if(fieldLabel.equals("Instructor Name"))
						 {
							 if(instructorApproval.getInstructor() != null)
							 {
							 	value = instructorApproval.getInstructor().getFirstName() +" "+ instructorApproval.getInstructor().getLastName();
							 }
						 }
						 if(fieldLabel.equals("Instructor Number"))
						 {
							 value = instructorApproval.getInstructorApprovalNumber(); 
						 }
					 }
				 }
			 }
		}
		return value;
	}

	public ByteArrayInputStream fetchCertificateFromURL (String urlToFetch,LearnerEnrollment learnerEnrollment) throws ClientProtocolException, IOException
	{
		//String tempCertificateLocation= VU360Properties.getVU360Property("document.tempCertificateLocation")+"certificate_"+learnerEnrollment.getId()+".pdf";
		HttpClient httpclient = new DefaultHttpClient();
		int statusCode = -1;
		HttpResponse response;
		//File file= new File(tempCertificateLocation);
		do
		{
			HttpGet httpget = new HttpGet(urlToFetch);
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13");
			response = httpclient.execute(httpget);
			statusCode = response.getStatusLine().getStatusCode(); 
		}
		while(statusCode == HttpStatus.SC_MOVED_TEMPORARILY);
		if(response != null)
		{
			HttpEntity entity = response.getEntity();
			if (entity != null) {
			    InputStream instream = entity.getContent();			    
			    byte[] bytes = IOUtils.toByteArray(instream);
			    ByteArrayInputStream bais= new ByteArrayInputStream(bytes);
			    instream.close();
			    return bais;			    			
			}
		}
		return null;
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
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	/**
	 * @return the statisticsService
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}

	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}
	
	
}
