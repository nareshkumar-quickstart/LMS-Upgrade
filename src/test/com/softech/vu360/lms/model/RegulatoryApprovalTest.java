package com.softech.vu360.lms.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 * The test class contains Test CRUD operations for 'RegulatoryApproval' Entity which is a base class (Inheritance of type 'Joined') extended by following classes:
 * CourseApproval.java
 * InstructorApproval.java
 * ProviderApproval.java
 * 
 * Moreover the test class also includes test CRUD operations for the classes that are used in relation with RegulatoryApproval and its extended classes.
 * These are 
 * PurchaseCertificateNumber.java
 * Modality.java
 * CourseConfugiration.java
 * CourseApprovalAudit.java
 * RegulatorCategory.java
 * Regulator.java
 * 
 */
@Transactional
public class RegulatoryApprovalTest extends TestBaseDAO<RegulatoryApproval> {

	//@Test
	public void CourseApproval_should_save_with_CustomField_CustomFieldValue_PurchaseCertificateNumber() throws Exception {
		
		CourseApproval courseApproval=new CourseApproval();

		//setting contentowner
		ContentOwner contentOwner = (ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		RegulatorCategory regulatorCategory = (RegulatorCategory)crudFindById(RegulatorCategory.class, new Long(5));
		courseApproval.setRegulatorCategory(regulatorCategory);
		// setting course...
	    Course cou = (Course)crudFindById(Course.class, new Long(2));
		courseApproval.setCourse(cou);
		//setting course group
		List<CourseGroup> courseGroups =new ArrayList<CourseGroup>();
		CourseGroup objCG = (CourseGroup)crudFindById(CourseGroup.class, new Long(3));
		courseGroups.add(objCG);
		courseApproval.setCourseGroups(courseGroups);
		// setting requirements...
		List<CredentialCategoryRequirement> requirements = new ArrayList <CredentialCategoryRequirement>();
		CredentialCategoryRequirement credentialCatgeory=(CredentialCategoryRequirement)crudFindById(CredentialCategoryRequirement.class, new Long(102));
		requirements.add(credentialCatgeory);
		courseApproval.setRequirements(requirements);
		// setting template
		CourseConfigurationTemplate temp = (CourseConfigurationTemplate)crudFindById(CourseConfigurationTemplate.class, new Long(1));
		courseApproval.setTemplate(temp);
		// setting certificate...
		Certificate cer = (Certificate)crudFindById(Certificate.class, new Long(5374));
		courseApproval.setCertificate(cer);
		// setting affidavit
		Affidavit affidavit = (Affidavit)crudFindById(Affidavit.class, new Long(97343));
		courseApproval.setAffidavit(affidavit);
		// setting provider...
		Provider pro = (Provider)crudFindById(Provider.class, new Long(2));
		if( contentOwner != null ) {
		pro.setContentOwner(contentOwner);
		}
		courseApproval.setProvider(pro);
		//Setting customfield of type 'SingleLineTextCustomFiled'
		List<CustomField> customFieldList=new ArrayList<CustomField>();
		SingleLineTextCustomFiled customField_1=new SingleLineTextCustomFiled();
		customField_1.setFieldLabel("Recommendation");
		customFieldList.add(customField_1);
		courseApproval.setCustomFields(customFieldList);
		//Setting customfieldvalue
		List<CustomFieldValue> fieldValue=new ArrayList<CustomFieldValue>();
		CustomFieldValue customFieldValue_1=new CustomFieldValue();
		customFieldValue_1.setValue("Yes");
		customFieldValue_1.setCustomField(customField_1);
		fieldValue.add(customFieldValue_1);
		courseApproval.setCustomFieldValues(fieldValue);
		//setting PURCHASECERTIFICATE
		List<PurchaseCertificateNumber> purchaseList=new ArrayList<PurchaseCertificateNumber>();
		PurchaseCertificateNumber purchaseCertificateNum=(PurchaseCertificateNumber)crudFindById(PurchaseCertificateNumber.class, new Long(305));
		purchaseList.add(purchaseCertificateNum);
		//setting documents
		List<Document> docList=new ArrayList<Document>();
		Document doc=(Document)crudFindById(Document.class, new Long(16));
		docList.add(doc);
		courseApproval.setDocuments(docList);
		
		courseApproval.setPurchaseCertificateNumbers(purchaseList);
		courseApproval.setApprovedCourseName("Test_COurseApproval");
		courseApproval.setApprovedCreditHours("9");
		courseApproval.setUsePurchasedCertificateNumbers(false);
		courseApproval.setCertificateNumberGeneratorNextNumber(new Long(10));
		
		save(courseApproval);

	}

	//@Test
	public void CourseApproval_should_update() throws Exception {

		CourseApproval course= (CourseApproval)crudFindById(CourseApproval.class, new Long(13805));
		course.setCourseGroups(null);
		course.setApprovedCourseName("Test_Approved_CourseName_Updated");
		update(course);
		
	}
	
	//@Test
	public void PurchaseCertificateNumber_should_save(){
		
		PurchaseCertificateNumber numbner=new PurchaseCertificateNumber();
		numbner.setCertificateNumber("111");
		numbner.setNumericCertificateNumber(new Long(111));
		numbner.setUsed(true);
		crudSave(PurchaseCertificateNumber.class, numbner);
		
	}
	
	//@Test
	@SuppressWarnings("unchecked")
	public Set<Modality> Modality_should_readAllModality(){
		
		
		return (Set<Modality>) getAll("Modality", Modality.class);
		
	}
	
	//@Test
	public void CourseConfiguration_should_save_with_AssessmentConfiguration(){
		
		CourseConfiguration config = new CourseConfiguration();
		config.setShowStandardIntroduction(true);
		config.setShowContent(true);
		config.setShowEndOfCourseScene(true);
		config.setAllowUserToReviewCourseAfterCompletion(true);
		config.setIdleTimeAmount(0);
		config.setPlayerCourseFlow("Test_COurse_Flow");
		config.setEnforceTimedOutlineAllScenes(true);
		config.setMustAttemptPostAssessment(true);
		config.setMustDemonstratePostAssessmentMastery(true);
		config.setMustDemonstratePreAssessmentMastery(true);
		config.setMustDemonstrateQuizMastery(true);
		config.setMustCompleteAnySurveys(true);
		config.setMustViewEverySceneInTheCourse(true);
		config.setNumberOfCoursesLaunch(1);
		config.setMinutesAfterFirstCourseLaunch(5);
		config.setDaysOfRegistraion(3);
		config.setEnableIdentityValidation(true);
		config.setValidationTimeBetweenQuestion(3);
		config.setAllowSecondsToAnswerEachQuestion(5);
		config.setValidationNoMissedQuestionsAllowed(3);
		config.setNumberOfValidationQuestions(7);
		config.setPlayerStrictlyEnforcePolicyToBeUsed(true);
		config.setValidationStrictlyEnforcePolicyToBeUsed(true);
		config.setMustCompleteSpecialQuestionnaire(true);
		config.setSpecialQuestionnaireSpecified(true);
		// setting template
		CourseConfigurationTemplate temp = (CourseConfigurationTemplate)crudFindById(CourseConfigurationTemplate.class, new Long(1));
		config.setCourseConfigTemplate(temp);
		
		// setting certificate...
		Certificate certificate = (Certificate) crudFindById(Certificate.class, new Long(5374));
		config.setCompletionCertificate(certificate);
		
		List<AssessmentConfiguration> list=new ArrayList<AssessmentConfiguration>();
		AssessmentConfiguration assesmentConfig=new AssessmentConfiguration();
		assesmentConfig.setAssessmentType("Quiz");
		assesmentConfig.setAssessmentEnabled(true);
		assesmentConfig.setCourseConfiguration(config);
		list.add(assesmentConfig);
		config.setAssessmentConfigurations(list);
		crudSave(CourseConfiguration.class, config);
		
	}
	
	//@Test
	public void CourseApprovalAudit_should_save() throws Exception{
		
		CourseApproval courseAudit=(CourseApproval)crudFindById(CourseApproval.class, new Long(13753));
		crudSave(CourseApprovalAudit.class, courseAudit);
	}
	
	//@Test
	public void InstructorApproval_should_save_with_CustomField_CustomFieldValue() throws Exception{
		InstructorApproval instructorApproval = new InstructorApproval();
		RegulatorCategory regulatorCategory = (RegulatorCategory)crudFindById(RegulatorCategory.class, new Long(5));
		instructorApproval.setRegulatorCategory(regulatorCategory);
		
		Instructor instructor= (Instructor)crudFindById(Instructor.class, new Long(2));
		instructorApproval.setInstructor(instructor);
		
		// setting provider...
		Provider provider = (Provider)crudFindById(Provider.class, new Long(2));
		instructorApproval.setProvider(provider);
		
		// setting course...
	    Course course = (Course)crudFindById(Course.class, new Long(2));
	    instructorApproval.setCourse(course);
	    
	  //Setting customfield of type 'SingleLineTextCustomFiled'
	  		List<CustomField> customFieldList=new ArrayList<CustomField>();
	  		SingleLineTextCustomFiled customField_1=new SingleLineTextCustomFiled();
	  		customField_1.setFieldLabel("Recommendation_For_InstructorApproval");
	  		customFieldList.add(customField_1);
	  		instructorApproval.setCustomFields(customFieldList);
	  		//Setting customfieldvalue
	  		List<CustomFieldValue> fieldValue=new ArrayList<CustomFieldValue>();
	  		CustomFieldValue customFieldValue_1=new CustomFieldValue();
	  		customFieldValue_1.setValue("Yes");
	  		customFieldValue_1.setCustomField(customField_1);
	  		fieldValue.add(customFieldValue_1);
	  		instructorApproval.setCustomFieldValues(fieldValue);
	  		ContentOwner contentOwner = (ContentOwner)crudFindById(ContentOwner.class, new Long(1));
	  		instructorApproval.setContentOwner(contentOwner);
			instructorApproval.setCourse(null);
	  		crudSave(InstructorApproval.class, instructor);
	}
	
		//@Test
		public void InstructorApproval_should_update() throws Exception {

			InstructorApproval instructorApproval=(InstructorApproval)crudFindById(InstructorApproval.class, new Long(13808));
			ContentOwner contentOwner = (ContentOwner)crudFindById(ContentOwner.class, new Long(1));
			instructorApproval.setContentOwner(contentOwner);
			instructorApproval.setCourse(null);
			instructorApproval.setApprovedInstructorName("Test_Approved_InstructorName_Updated");
			crudSave(InstructorApproval.class, instructorApproval);
			
		}
		
		//@Test
		public void ProviderApproval_should_save_with_CustomField_CustomFieldValue() throws Exception {
			ProviderApproval providerApproval = new ProviderApproval();
			RegulatorCategory regulatorCategory = (RegulatorCategory)crudFindById(RegulatorCategory.class, new Long(5));
			providerApproval.setRegulatorCategory(regulatorCategory);
			
			// setting provider...
			Provider provider = (Provider)crudFindById(Provider.class, new Long(2));
			providerApproval.setProvider(provider);
;
		    
		  //Setting customfield of type 'SingleLineTextCustomFiled'
		  		List<CustomField> customFieldList=new ArrayList<CustomField>();
		  		SingleLineTextCustomFiled customField_1=new SingleLineTextCustomFiled();
		  		customField_1.setFieldLabel("Recommendation_For_ProviderApproval");
		  		customFieldList.add(customField_1);
		  		providerApproval.setCustomFields(customFieldList);
		  		//Setting customfieldvalue
		  		List<CustomFieldValue> fieldValue=new ArrayList<CustomFieldValue>();
		  		CustomFieldValue customFieldValue_1=new CustomFieldValue();
		  		customFieldValue_1.setValue("Yes");
		  		customFieldValue_1.setCustomField(customField_1);
		  		fieldValue.add(customFieldValue_1);
		  		providerApproval.setCustomFieldValues(fieldValue);
		  		ContentOwner contentOwner = (ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		  		providerApproval.setContentOwner(contentOwner);
				crudSave(ProviderApproval.class, providerApproval);
		}
		
		//@Test
		public void ProviderApproval_should_save() throws Exception {

			ProviderApproval updateProvider= (ProviderApproval)crudFindById(ProviderApproval.class, new Long(13809));
			updateProvider.setApprovedProviderName("Test_Approved_ProviderName_Updated");
			crudSave(ProviderApproval.class, updateProvider);
			
		}
		
		@Test
		public void RegulatorCategory_should_save() throws Exception{
			
			RegulatorCategory regulatorCategory=new RegulatorCategory();
			Regulator regulator=(Regulator)crudFindById(Regulator.class, new Long(2));
			regulatorCategory.setRegulator(regulator);
			regulatorCategory.setDisplayName("Test_RegulatorCategory");
			regulatorCategory.setModalitiesAllowed(this.Modality_should_readAllModality());
			crudSave(RegulatorCategory.class, regulatorCategory);
			
		}

}
