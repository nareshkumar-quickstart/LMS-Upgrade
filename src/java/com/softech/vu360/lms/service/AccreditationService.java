package com.softech.vu360.lms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.Modality;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.RegulatoryAnalyst;
import com.softech.vu360.lms.model.RegulatoryApproval;
import com.softech.vu360.lms.model.ValidationQuestion;
import com.softech.vu360.lms.vo.CourseApprovalVO;
import com.softech.vu360.lms.vo.VU360User;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.PdfFieldsEnum;
import com.softech.vu360.util.TreeNode;

/**
 * @author tapas
 *
 */
public interface AccreditationService {

	public Regulator getRegulatorById(long regulatorId);
	public Credential getCredentialById(long credentialId);
	public Regulator saveRegulator(Regulator regulator);
	public List<Regulator> findRegulator(String name,String alias, String emailAddress,RegulatoryAnalyst regulatoryAnalyst);
	public List<Regulator> findRegulator();
	public List<Regulator> findAllActiveRegulator();
//	public Regulator removeCredentialFromRegulator(Regulator regulator,long credentialIdarray[]);
	public List<Contact> getContactsByRegulator(long regulatorId);
	public Contact saveContact(Contact contact);
	public Credential saveCredential(Credential credential);
	public List<Credential> findCredential(String offLicName,String shortLicName,com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst);
	public ContentOwner findContentOwnerByRegulatoryAnalyst(com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst);
	public ContentOwner findContentOwnerById(Long id);
	public List<Credential> findCredentialInRegulator( String credentialName, String credentialShortName,Long[] CredentialId);
	public  List<Contact> findContactByRegulator(String firstName,String lastName, String emailAddress, String phone,long regulatorId);
	//public List<Provider> findProvidersByRegulator(long regulatorId);
	public Provider saveProvider(Provider provider);
	public List<CustomField> findGlobalsCustomFields(CustomFieldEntityType customFieldEntityType,String fieldType, String name);
	public CustomField saveGlobalCustomField(CustomField customField);
	public ProviderApproval saveProviderApproval(ProviderApproval providerApproval);
	public CourseApproval saveCourseApproval(CourseApproval courseApproval);
	public InstructorApproval saveInstructorApproval(InstructorApproval instructorApproval);
	public Instructor saveInstructor(Instructor instructor);
	public List<Course>  findCoursesByRegulatoryAnalyst(String courseName,String courseId,com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst);
	public List<Document> findDocumentInRegulator( String name, List<Document> documents);
	public CustomFieldValueChoice saveOption(CustomFieldValueChoice option);
	public CreditReportingFieldValueChoice loadForUpdateCreditReportingFieldValueChoice(long id);
	public Contact getContactById(long contactId);
	public CustomField getCustomFieldById(long customFieldId);
	public CustomField saveCustomField(CustomField customfield);
	public List<Instructor> findInstructor(String firstname, String lastname,String emailAddress,String sortBy, String sortDirection);
	public List<CustomFieldValueChoice> getOptionsByCustomField(CustomField customfield);
	public Instructor getInstructorByID(long instructorId);
    public void removeOption(CustomField customfield);	
    public List<Provider> findProviders(String name, RegulatoryAnalyst regulatoryAnalyst);
    public Provider getProviderById(long providerId);
    public Certificate saveCertificate(Certificate certificate);
    public List<Certificate> getCertficatesByName(String name);
    public Certificate getCertificateById( long id );
    public Certificate getCertificateById( long id, boolean readFromDB );
    public Affidavit saveAffidavit(Affidavit affidavit);
    public List<Affidavit> getAffidavitsByName(String name);
    public Affidavit getAffidavitById( long id );
    public Affidavit getAffidavitById( long id, boolean readFromDB );
    public Affidavit loadForUpdateAffidavit( long id );
	public List<ProviderApproval> findProviderApproval(String approvalName,String  approvalNumber,String approvalType,String regulatorName,int status);
	public List<InstructorApproval> findInstructorApproval(String approvalName,String  approvalNumber,String approvalType,String regulatorName,int status);
	public List<CourseApproval> findCourseApproval(String approvalName,String  approvalNumber,String approvalType,String regulatorName,int status);
	public List<CredentialCategory> findCredentialCategory(String categoryType,String  categoryName);
	public ArrayList<RegulatorCategory> findRegulatorCategories(String categoryName,String categoryType,long regulatorId);
	public ArrayList<RegulatorCategory> findRegulatorCategories(long regulatorId);
	public List<CreditReportingField> getAllCreditReportingFields(String fieldName,String fieldType);		
	public List<CertificateBookmarkAssociation> getAllCertificateBookmarkAssociation(String bookmarkName);
	public HashSet<RegulatorCategory> getContainingRegulatorCategories(CreditReportingField creditReportingField, String categoryType,String categoryName); 
	public ProviderApproval getProviderApprovalById( long providerApprovalId);
	public InstructorApproval getInstructorApprovalById( long instructorApprovalId);
	public CourseApproval getCourseApprovalById( long courseApprovalId);
	public Document getDocumentById(long documentId) ;
	public Document saveDocument(Document document);
	public List<Instructor> findInstructor(String firstName, String lastName, String emailAddress, VU360User user,String sortBy,String sortDirection);
	public CourseConfiguration saveCourseConfiguration(CourseConfiguration courseConfiguration);
	public CourseConfiguration getCourseConfigurationById(long id);
	public CourseConfigurationTemplate getTemplateById(long id);
	//public List<CourseConfigurationTemplate> findTemplates(String name, Date lastUpdatedDate );

	public CourseConfiguration getCourseConfigurationByTemplateId(long templateId, boolean readForcefullyFromDB);
	public CourseConfiguration getCourseConfigurationByTemplateIdWithAssociatedCertificate(long templateId);
	
	public CourseConfigurationTemplate saveCourseConfigurationTemplate(CourseConfigurationTemplate courseConfigurationTemplate);
	//public List<CreditReportingField> getCreditReportingFieldByCourseApproval(CourseApproval courseApproval);
	public CreditReportingField saveCreditReportingField(CreditReportingField creditReportingField);
	public CertificateBookmarkAssociation saveCertificateBookmarkAssociation(CertificateBookmarkAssociation cba);
	public CreditReportingFieldValueChoice saveChoice(CreditReportingFieldValueChoice option);
	public Set<CreditReportingField> getCreditReportingFieldsByCourse(Course course);
	public Set<CreditReportingField> getCreditReportingFieldsByCourseApproval(long courseApprovalId);
	public List<CourseApprovalVO> getCourseApprovalByCourse_Jurisdiction(String courseId, String varLearnerEnrollmentId);
	
	public List<Learner> getLearnerByCourseCompletion(Long[] courseIdArray,Date startdate,Date endDate);
	public List<Learner> SearchLearnerByCourseCompletion(Long[] courseIdArray, Date startdate, Date endDate,
			String firstName, String lastName, String email);
	public void removeCreditReportingField(List<CreditReportingField> reportingFields);
	public void deleteCertificateBookmarkAssociation(List<CertificateBookmarkAssociation> cbaList);
	public List<CreditReportingFieldValue> getCreditReportingFieldValue(CreditReportingField reportingfield);
	public List<CreditReportingFieldValue> getCreditReportingFieldValue(LearnerProfile learnerProfile);
	public CreditReportingFieldValue getCreditReportingFieldValue(CreditReportingField reportingfield,LearnerProfile learnerProfile); 
	public CreditReportingField getCreditReportingFieldById(long id);
	public boolean isCreditReportingFieldDuplicate(CreditReportingField field,long contentOwnerId);
	public String getValueByReportingType( CreditReportingFieldValue fieldValue );
	public List<CreditReportingFieldValueChoice> getOptionsByCreditReportingField(CreditReportingField creditReportingField);
	public void removeChoice(CreditReportingField creditReportingField);
	public CourseApproval getCourseApprovalByCourse(Course course);
	public CourseApproval getCourseApprovalByCourse(Course course,Date currentDate);
	//public CredentialCategory getCredentialCategoryByCourse(Course course);
	public RegulatoryApproval getRegulatoryApprovalById(long regulatoryApprovalId) ;
	public void saveApproval(RegulatoryApproval regulatoryApproval);
	public void deleteContacts(List<Contact> contacts);
	public boolean isUniqueCustomFieldName(String entityType, String name);
	public List<CourseConfigurationTemplate> findTemplates(String name, Date lastUpdatedDate, long contentOwnerId );
	public Regulator loadForUpdateRegulator(long regulatorId);
	public Credential loadForUpdateCredential(long credentialId);
	public Contact loadForUpdateContact(long contactId);
	public CustomField loadForUpdateCustomField(long customFieldId);
	public Instructor loadForUpdateInstructor(long instructorId);
	public Provider loadForUpdateProvider(long providerId);
	public Certificate loadForUpdateCertificate( long id );
	public ProviderApproval loadForUpdateProviderApproval( long providerApprovalId);
	public InstructorApproval loadForUpdateInstructorApproval( long instructorApprovalId);
	public CourseApproval loadForUpdateCourseApproval( long courseApprovalId);
	public Document loadForUpdateDocument(long documentId);
	public CourseConfigurationTemplate loadForUpdateTemplate(long id);
	public CourseConfiguration loadForUpdateCourseConfiguration(long id);
	public CreditReportingField loadForUpdateCreditReportingField(long id);
	public CertificateBookmarkAssociation loadForUpdateCertificateBookmarkAssociation(long id);
	public RegulatoryApproval loadForUpdateRegulatoryApproval(long regulatoryApprovalId) ;
	
	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	public CredentialCategoryRequirement loadForUpdateCredentialCategoryRequirement (Long credCatReqId );
	public List<CredentialCategoryRequirement>  getCredentialCategoryRequirementsByCredential (Long credentialId);
	public CredentialCategoryRequirement getCredentialCategoryRequirementById (Long credCatReqId );
	public CredentialCategoryRequirement saveCredentialCategoryRequirement (CredentialCategoryRequirement credentialCategoryRequirement);
	public CredentialCategory saveCredentialCategory (CredentialCategory credentialCategory);
	public CredentialCategory loadForUpdateCredentialCategory (Long credentialCategoryId);
	public CredentialCategory getCredentialCategoryById (Long credentialCategoryId);
	public List<CredentialCategory> getCredentialCategoryByCredential (Long credentialId);
	public List<CredentialCategoryRequirement> getCredentialCategoryRequirementsByCategory (Long categoryId);	
	public List<CredentialCategory> getCredentialCategoryByCredential (Long credentialId, int sortDirection);
	public List<List<TreeNode>> getCategroyRequirementTreeByCredential (Long credentialId, boolean enableNodes);
	public boolean deleteCredentialCategoryRequirements ( String[] categoryIds, String[] requirementIds );	
	public Map<Object, Object> getCustomFieldsByCategory (Long categoryId, String sortColumn, int sortDirection, int pageIndex, int pageSize);
	public boolean deleteCategoryCustomFields (Long categoryId, String[] customFieldIds);
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldsAndValues (List<CustomField> customFieldList, List<CustomFieldValue> customFieldValueList);
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldAndValuesForRendering (List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> rawCustomFieldList );
	public List<CustomFieldValue> getCustomFieldValues (List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList, boolean escapeValues);
        
        /**
         * Determines if the given custom field already has values against it.
         * 
         * @see <a href="http://jira.360training.com/browse/LMS-10215">Make custom fields editable if no data found.</a>
         * @param customfield Custom field to determine if there is any data against it.
         * @return if there is any value against the custom value then true else false.
         */
        public boolean customFieldHasValue(CustomField customField);
   
   public RegulatorCategory getRegulatorCategory(long regulatorcategoryId);
   public RegulatorCategory loadForUpdateRegulatorCategory(long regulatorcategoryId);
   public void deleteRegulatorCategories(List<RegulatorCategory> categories);
   public RegulatorCategory saveRegulatorCategory(RegulatorCategory regulatorCategory);
   public ValidationQuestion saveValidationQuestion(ValidationQuestion validationQuestion);
   public RegulatorCategory saveRegulatorCategoryCreditReporting(RegulatorCategory regulatorCategory) ;
   public RegulatorCategory dropRegulatorCategoryCreditReporting(RegulatorCategory regulatorCategory);

   public Set<RegulatorCategory> getAllRegulatorCategories(long regulatorId);   
   //public HashSet<Modality> getAllModalities();
   public List<Modality> getAllModalities();
   public void unAssignCategoryFromAllCourseApprovals(RegulatorCategory regulatorCategory);
   public List<Asset> findAssets(ContentOwner co, String name, String keywords);
   public List<CourseApproval> getCourseApprovalByCourseConfigurationTemplate(long templateId);
   
   public PurchaseCertificateNumber savePurchaseCertificateNumber(PurchaseCertificateNumber purchaseCertificateNumber);
   public PurchaseCertificateNumber loadForUpdatePurchaseCertificateNumber(long id);
   
   public PurchaseCertificateNumber addPurchaseCertificateNumber(PurchaseCertificateNumber purchaseCertificateNumber);
   public void deletePurchaseCertificateNumbers(
		List<PurchaseCertificateNumber> certificateNumbers);
   
   public List<CertificateBookmarkAssociation> getCertBookMarkAssociations(PdfFieldsEnum type);
   
   public List<InstructorApproval> getInstructorApprovalsByCatIds(Long[] regCatId);
   //public List<CertificateBookmarkAssociation> getBookMarkAssociationsByReportingField(String[] selectedReportingFieldId);
   public List<CertificateBookmarkAssociation> getBookMarkAssociationsByReportingField(Long[] selectedReportingFieldId);
   
   /**
    * Use to check whether the course is already associated with other course approvals or not
    * @param courseId
    * @return
    */
   public boolean isCourseAlreadyAssociatedWithCourseApproval(String courseId);
   public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(long courseId, long regulatorCategory, Date startDate, Date endDate);
   public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(long courseId, long regulatorCategory, Date startDate, Date endDate, long excludeCourseApprovalId);
   public boolean isCertificateLinkedWithCourseApproval(Long[] certificateIds);
   public boolean isAffidavitLinkedWithCourseApproval(Long[] certificateIds);
   public boolean isCourseApprovalLinkedWithLearnerEnrollment(Long courseApprovalId);
   public boolean isCourseApprovalLinkedWithLearnerEnrollment(Long[] courseApprovalIds);
   public boolean deleteRegulatoryApproval(RegulatoryApproval approval);
   public boolean isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(long regulatorId, String regulatorCategoryType, long excludeCategoryId);
   
   public List<Certificate> getCertificatesWhereAssetVersionIsEmpty(Integer fromCertificateId, Integer toCertificateId);   
   
   public Course getCourseByGUID(String courseGUID);
   
   public List<CourseApproval> getCourseApprovalListByCourse(Course course);
   
   public List<Proctor> getAllProctorsByCredential(Long credentialId);
   public boolean isCourseApprovalSelected(Long enrollmentId);
   public boolean isCourseApprovalSelected(Long enrollmentId, Long learnerId, String courseCode);
   public Long getCourseApprovalSelected(Long enrollmentId); 
   public Long getCourseApprovalSelected(Long enrollmentId, Long learnerId, String courseCode);
   
   public List<LearningSession> getLearningSessionForCourseApproval(List<String> courseIds, Long learnerId);
   public List<LearningSession> getLearningSessionByEnrollmentId(Long enrollmentId);
   public int getNumberOfUnusedPurchaseCertificateNumbers(long courseApprovalId);
   public PurchaseCertificateNumber getUnusedPurchaseCertificateNumber(long courseApprovalId);
   
   public List<CourseApproval> getCourseApprovalsByDateRange(Date date, Date after7DayDate) throws Exception;
   public void removeChoices(List<CreditReportingFieldValueChoice> options);
   public ValidationQuestion loadForUpdateValidationQuestion(long id);
   public List<ValidationQuestion> getUniqueValidationQuestionByCourseConfigurationId(long id);
   public void deleteValidationQuestion(List<Long> lstUniqueQuestionIds);
   public Document loadDocumentByName(String documentName);
   
   //New optimized method to reduce the number of queries executing when performing search for Accreditation
   public List<Regulator> searchRegulator(String name,RegulatoryAnalyst regulatoryAnalyst);
   public List<Provider> searchProviders(String name, RegulatoryAnalyst regulatoryAnalyst);
   public List<Instructor> searchInstructor(String firstName);
   public InstructorApproval getInstructorApprovalDeleteFalseById(Long instructorApprovalID);
   public ProviderApproval getProviderApprovalDeleteFalseById(Long providerApprovalID);
}