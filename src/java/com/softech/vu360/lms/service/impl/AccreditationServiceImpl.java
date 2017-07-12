package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
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
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.Modality;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.RegulatoryAnalyst;
import com.softech.vu360.lms.model.RegulatoryApproval;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.ValidationQuestion;
import com.softech.vu360.lms.repositories.AffidavitRepository;
import com.softech.vu360.lms.repositories.AssetRepository;
import com.softech.vu360.lms.repositories.CertificateBookmarkAssociationRepository;
import com.softech.vu360.lms.repositories.CertificateRepository;
import com.softech.vu360.lms.repositories.ContactRepository;
import com.softech.vu360.lms.repositories.ContentOwnerRepository;
import com.softech.vu360.lms.repositories.CourseApprovalRepository;
import com.softech.vu360.lms.repositories.CourseConfigurationRepository;
import com.softech.vu360.lms.repositories.CourseConfigurationTemplateRepository;
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.CredentialCategoryRepository;
import com.softech.vu360.lms.repositories.CredentialCategoryRequirementRepository;
import com.softech.vu360.lms.repositories.CredentialRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldValueChoiceRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldValueRepository;
import com.softech.vu360.lms.repositories.CustomFieldRepository;
import com.softech.vu360.lms.repositories.CustomFieldValueChoiceRepository;
import com.softech.vu360.lms.repositories.CustomFieldValueRepository;
import com.softech.vu360.lms.repositories.DocumentRepository;
import com.softech.vu360.lms.repositories.InstructorApprovalRepository;
import com.softech.vu360.lms.repositories.InstructorRepository;
import com.softech.vu360.lms.repositories.LanguageRepository;
import com.softech.vu360.lms.repositories.LearnerCourseStatisticsRepository;
import com.softech.vu360.lms.repositories.LearningSessionRepository;
import com.softech.vu360.lms.repositories.ModalityRepository;
import com.softech.vu360.lms.repositories.ProctorRepository;
import com.softech.vu360.lms.repositories.ProviderApprovalRepository;
import com.softech.vu360.lms.repositories.ProviderRepository;
import com.softech.vu360.lms.repositories.PurchaseCertificateNumberRepository;
import com.softech.vu360.lms.repositories.RegulatorCategoryRepository;
import com.softech.vu360.lms.repositories.RegulatorRepository;
import com.softech.vu360.lms.repositories.RegulatoryAnalystRepository;
import com.softech.vu360.lms.repositories.RegulatoryApprovalRepository;
import com.softech.vu360.lms.repositories.ValidationQuestionRepository;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.CourseApprovalVO;
import com.softech.vu360.lms.vo.VU360User;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.util.CategoryRequirementTree;
import com.softech.vu360.util.CourseApprovalVOSort;
import com.softech.vu360.util.CredentialCategorySort;
import com.softech.vu360.util.CustomFieldEntityType;
import com.softech.vu360.util.CustomFieldSort;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.PdfFieldsEnum;
import com.softech.vu360.util.ProctorStatusEnum;
import com.softech.vu360.util.TreeNode;

/**
 * @author tapas
 * @modified by Faisal A. Siddiqui
 */
public class AccreditationServiceImpl implements AccreditationService {

	@Inject
	CertificateBookmarkAssociationRepository certificateBookmarkAssociationRepository;
	@Inject
	CertificateRepository certificateRepository;
	@Inject
	ProctorRepository proctorRepository;
	@Inject
	RegulatoryAnalystRepository regulatoryAnalystRepository = null;
	@Inject
	ContactRepository contactRepository;
	@Inject
	ContentOwnerRepository contentOwnerRepository;
	@Inject
	RegulatorRepository regulatorRepository;
	@Inject
	RegulatorCategoryRepository regulatorCategoryRepository;
	@Inject
	RegulatoryApprovalRepository regulatoryApprovalRepository;
	@Inject
	CredentialCategoryRequirementRepository credentialCategoryRequirementRepository;
	@Inject
	CredentialCategoryRepository credentialCategoryRepository;
	@Inject
	PurchaseCertificateNumberRepository purchaseCertificateNumberRepository;
	@Inject
	AssetRepository assetRepository;
	@Inject
	CourseApprovalRepository courseApprovalRepository;
	@Inject
	InstructorApprovalRepository instructorApprovalRepository;
	@Inject
	ProviderApprovalRepository providerApprovalRepository;
	@Inject
	CourseRepository courseRepository;
	@Inject
	CredentialRepository credentialRepository;
	@Inject
	CustomFieldRepository customFieldRepository;
	@Inject
	InstructorRepository instructorRepository;
	@Inject
	ProviderRepository providerRepository;
	@Inject
	CourseConfigurationTemplateRepository courseConfigurationTemplateRepository;
	@Inject
	AffidavitRepository affidavitRepository;
	@Inject
	private CreditReportingFieldRepository creditReportingFieldRepository;
	@Inject
	private CreditReportingFieldValueRepository creditReportingFieldValueRepository;
	@Inject
	CourseConfigurationRepository courseConfigurationRepository;
	@Inject
	ModalityRepository modalityRepository;
	@Inject
	CreditReportingFieldValueChoiceRepository creditReportingFieldValueChoiceRepository;
	@Inject
	CustomFieldValueChoiceRepository customFieldValueChoiceRepository;
	@Inject
	DocumentRepository documentRepository;
	@Inject
	LearningSessionRepository learningSessionRepository;
	@Inject
	LearnerCourseStatisticsRepository learnerCourseStatisticsRepository;
	@Inject
	CustomFieldValueRepository customFieldValueRepository;
	@Inject
	ValidationQuestionRepository validationQuestionRepository;
	@Inject
	LanguageRepository languageRepository;
	
	private static final Logger log = Logger
			.getLogger(AccreditationServiceImpl.class.getName());

	private VU360UserService vu360UserService = null;

	public Regulator getRegulatorById(long regulatorById) {
		return regulatorRepository.findOne(regulatorById);
	}

	@Transactional
	public Regulator saveRegulator(Regulator regulator) {
		List<CustomFieldValue> lst = new ArrayList<CustomFieldValue>();// regulator.getCustomfieldValues();
		CustomFieldValue customFieldValue = new CustomFieldValue();
		for (CustomFieldValue obj : regulator.getCustomfieldValues()) {
			customFieldValue = obj;
			customFieldValue.setCustomField(customFieldRepository.findOne(obj
					.getCustomField().getId()));
			lst.add(customFieldValue);
			customFieldValue = new CustomFieldValue();
		}
		regulator.setCustomfieldValues(null);
		regulator.setCustomfieldValues(lst);
		return regulatorRepository.save(regulator);
	}

	public List<Regulator> findRegulator(String name, String alias,
			String emailAddress, RegulatoryAnalyst regulatoryAnalyst) {

		List<ContentOwner> listContentOwner = null;
		if (!regulatoryAnalyst.isForAllContentOwner()) {
			listContentOwner = contentOwnerRepository
					.findByRegulatoryAnalysts(regulatoryAnalyst);
		}

		return regulatorRepository.findRegulator(name, alias, emailAddress,
				listContentOwner);

	}

	public List<Regulator> findRegulator() {
		List<Regulator> listRegulator = (List<Regulator>) regulatorRepository
				.findAll();
		return listRegulator;
	}

	public List<Regulator> findAllActiveRegulator() {
		// return accreditationDAO.findAllActiveRegulator();
		return regulatorRepository.findByActiveOrderByNameAsc(true);

	}

	public ContentOwner findContentOwnerByRegulatoryAnalyst(
			com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst) {
		if (regulatoryAnalyst.isForAllContentOwner()) {

			List<ContentOwner> contentOwners = (List<ContentOwner>) contentOwnerRepository
					.findAll();

			if (contentOwners != null && !contentOwners.isEmpty()) {
				return contentOwners.get(0);
			}
		} else {
			RegulatoryAnalyst regulatoryAnalystDB = regulatoryAnalystRepository
					.findOne(regulatoryAnalyst.getId());
			List<ContentOwner> lstCO = regulatoryAnalystDB.getContentOwners();
			if (lstCO != null && !lstCO.isEmpty()) {
				return regulatoryAnalystDB.getContentOwners().get(0);
			}
		}
		return null;
	}

	public Credential getCredentialById(long credentialId) {
		// return accreditationDAO.getCredentialById(credentialId);
		return credentialRepository.findOne(credentialId);
	}

	public Document saveDocument(Document document) {
		// /return accreditationDAO.saveDocument(document);
		return documentRepository.save(document);
	}

	public List<Contact> getContactsByRegulator(long regulatorId) {
		// return accreditationDAO.getContactsByRegulator(regulatorId);
		return contactRepository.findByRegulatorId(regulatorId);
	}

	public Contact saveContact(Contact contact) {

		// return accreditationDAO.saveContact(contact);
		return contactRepository.save(contact);
	}

	@Transactional
	public Credential saveCredential(Credential credential) {
		// return accreditationDAO.saveCredential(credential);
		return credentialRepository.save(credential);
	}

	public List<Credential> findCredential(String offLicName,
			String shortLicName,
			com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst) {

		Collection<Long> coIds = getRegulatorAnalystContentOwnerIds(regulatoryAnalyst);
		return credentialRepository.findCredentialByContentOwner(coIds,
				offLicName, shortLicName, true);

	}

	public List<Credential> findCredentialInRegulator(String credentialName,
			String credentialShortName, Long[] CredentialId) {
		return credentialRepository.findByCredentialNameAndCredentialIds(
				Arrays.asList(CredentialId), credentialName,
				credentialShortName);
	}

	/**
	 * added by Dyutiman
	 */
	public List<Contact> findContactByRegulator(String firstName,
			String lastName, String emailAddress, String phone, long regulatorId) {
		return contactRepository.findContactByRegulator(firstName, lastName,
				emailAddress, phone, regulatorId);
	}

	/**
	 * added by Dyutiman
	 */
	public List<Document> findDocumentInRegulator(String name,
			List<Document> documents) {

		List<Document> regDocs = new ArrayList<Document>();
		for (Document doc : documents) {
			if (StringUtils.isBlank(doc.getName())) {
				regDocs.add(doc);
			} else if (doc.getName().toUpperCase().contains(name.toUpperCase())) {
				regDocs.add(doc);
			}
		}
		return regDocs;
	}

	@Transactional
	public void removeOption(CustomField customfield) {
		List<CustomFieldValueChoice> options = getOptionsByCustomField(customfield);
		customFieldValueChoiceRepository.delete(options);
	}

	public CourseConfiguration saveCourseConfiguration(
			CourseConfiguration courseConfiguration) {
		CourseConfiguration cc = courseConfigurationRepository
				.save(courseConfiguration);
		return cc;
	}

	public Course getCourseByGUID(String courseGUID) {
		// return accreditationDAO.getCourseByGUIDId(courseGUID);
		return courseRepository.findByCourseGUID(courseGUID);
	}

	public CourseConfiguration getCourseConfigurationById(long id) {
		// return accreditationDAO.getCourseConfigurationById(id);
		return courseConfigurationRepository.findOne(id);

	}

	public Provider saveProvider(Provider provider) {
		// return accreditationDAO.saveProvider(provider);
		return providerRepository.save(provider);
	}

	public List<CustomField> findGlobalsCustomFields(
			CustomFieldEntityType customFieldEntityType, String fieldType,
			String name) {
		// return
		// accreditationDAO.findGlobalsCustomFields(customFieldEntityType,fieldType,
		// name);

		return customFieldRepository
				.findCustomFieldByCustomFieldEntityTypeAndFieldTypeAndName(
						customFieldEntityType, fieldType, name);
	}

	public CustomField saveGlobalCustomField(CustomField customField) {
		// return accreditationDAO.saveGlobalCustomField(customField);
		return customFieldRepository.save(customField);
	}

	public ProviderApproval saveProviderApproval(
			ProviderApproval providerApproval) {

		// return accreditationDAO.saveProviderApproval(providerApproval);
		return providerApprovalRepository.save(providerApproval);
	}

	public CourseApproval saveCourseApproval(CourseApproval courseApproval) {
		CourseApproval ca = courseApprovalRepository.save(courseApproval);
		return ca;
	}

	public InstructorApproval saveInstructorApproval(
			InstructorApproval instructorApproval) {
		// return accreditationDAO.saveInstructorApproval(instructorApproval);
		return instructorApprovalRepository.save(instructorApproval);

	}

	public Instructor saveInstructor(Instructor instructor) {
		return instructorRepository.save(instructor);
	}

	public List<Course> findCoursesByRegulatoryAnalyst(String courseName,
			String courseId,
			com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst) {
		Collection<Long> listContentOwnerIds = getRegulatorAnalystContentOwnerIds(regulatoryAnalyst);
		return courseRepository.findCoursesByContentOwner(listContentOwnerIds,
				courseName, Course.PUBLISHED, false, courseId);
	}

	public CustomFieldValueChoice saveOption(CustomFieldValueChoice option) {
		return customFieldValueChoiceRepository.save(option);
	}

	public Contact getContactById(long contactId) {
		return contactRepository.findOne(contactId);
	}

	public CustomField saveCustomField(CustomField customfield) {
		return customFieldRepository.save(customfield);
	}

	public List<Instructor> findInstructor(String firstName, String lastName,
			String emailAddress, String sortBy, String sortDirection) {
		return instructorRepository
				.findInstructorByfirstNamelastNameemailAddress(firstName,
						lastName, emailAddress, sortBy, sortDirection);
	}

	public List<CustomFieldValueChoice> getOptionsByCustomField(
			CustomField customfield) {
		return customFieldValueChoiceRepository
				.findByCustomFieldOrderByDisplayOrderAsc(customfield);
	}

	private Collection<Long> getRegulatorAnalystContentOwnerIds(
			com.softech.vu360.lms.vo.RegulatoryAnalyst regulatoryAnalyst) {

		Collection<Long> coIds = null;

		if (!regulatoryAnalyst.isForAllContentOwner()) {
			if (regulatoryAnalyst.getContentOwners() != null
					&& regulatoryAnalyst.getContentOwners().size() > 0) {
				// get id from collection.
				coIds = Collections2
						.transform(
								regulatoryAnalyst.getContentOwners(),
								new Function<com.softech.vu360.lms.vo.ContentOwner, Long>() {
									public Long apply(
											com.softech.vu360.lms.vo.ContentOwner arg0) {
										return Long.parseLong(arg0.getId()
												.toString());
									}
								});
			}
		}

		return coIds;
	}

	public List<Certificate> getCertficatesByName(String name) {
		return certificateRepository
				.findByNameLikeIgnoreCaseAndActiveIsTrue('%' + name + '%');
	}

	public List<ProviderApproval> findProviderApproval(String approvalName,
			String approvalNumber, String approvalType, String regulatorName,
			int status) {
		/**
		 * Code Modified By Marium Saud Status clause will only be included when
		 * status value id greater Than 0 else the clause will not be included.
		 * Create 1 more method that excludes status clause
		 */
		List<ProviderApproval> providerApprovalsList = new ArrayList<ProviderApproval>();
		if (status >= 0) {
			Boolean isActive = true;

			if (status == 0)
				isActive = false;
			if (status == 1)
				isActive = true;

			providerApprovalsList = providerApprovalRepository
					.findProviderApprovals(approvalName.toUpperCase(),
							approvalNumber.toUpperCase(), isActive);

		} else {

			providerApprovalsList = providerApprovalRepository
					.findByApprovedProviderNameLikeIgnoreCaseAndProviderApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(
							"%" + approvalName + "%", "%" + approvalNumber
									+ "%");
		}

		return providerApprovalsList;

	}

	public List<InstructorApproval> findInstructorApproval(String approvalName,
			String approvalNumber, String approvalType, String regulatorName,
			int status) {
		/**
		 * Code Modified By Marium Saud Status clause will only be included when
		 * status value id greater Than 0 else the clause will not be included.
		 * Create 1 more method that excludes status clause
		 */
		List<InstructorApproval> instructorApprovalsList = new ArrayList<InstructorApproval>();
		if (status >= 0) {
			Boolean isActive = true;

			if (status == 0)
				isActive = false;
			if (status == 1)
				isActive = true;

			instructorApprovalsList = instructorApprovalRepository
					.findInstructorApprovals(approvalName.toUpperCase(),
							approvalNumber.toUpperCase(), isActive);

		} else {

			instructorApprovalsList = instructorApprovalRepository
					.findByApprovedInstructorNameLikeIgnoreCaseAndInstructorApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(
							"%" + approvalName + "%", "%" + approvalNumber
									+ "%");
		}

		return instructorApprovalsList;

	}

	public List<CourseApproval> findCourseApproval(String approvalName,
			String approvalNumber, String approvalType, String regulatorName,
			int status) {

		/**
		 * Code Modified By Marium Saud Status clause will only be included when
		 * status value id greater Than 0 else the clause will not be included.
		 * Create 1 more method that excludes status clause
		 */
		List<CourseApproval> courseApprovalsList = new ArrayList<CourseApproval>();
		if (status >= 0) {
			Boolean isActive = Boolean.TRUE;

			if (status == 0)
				isActive = Boolean.FALSE;
			if (status == 1)
				isActive = Boolean.TRUE;

			courseApprovalsList = courseApprovalRepository.findCourseApprovals(
					approvalName.toUpperCase(), approvalNumber.toUpperCase(),
					false, isActive);

		} else {
			courseApprovalsList = courseApprovalRepository
					.findByApprovedCourseNameLikeIgnoreCaseAndCourseApprovalNumberLikeIgnoreCaseAndDeletedIsFalse(
							"%" + approvalName + "%", "%" + approvalNumber
									+ "%");
		}

		return courseApprovalsList;
	}

	public List<CredentialCategory> findCredentialCategory(String categoryName,
			String categoryType) {
		/*
		 * return accreditationDAO.findCredentialCategory(categoryType,
		 * categoryName);
		 */

		return credentialCategoryRepository.findByCategoryTypeAndName(
				categoryType, categoryName);
	}

	public List<CreditReportingField> getAllCreditReportingFields(
			String fieldName, String fieldType) {
		// return accreditationDAO.getAllCreditReportingFields(contentOwner,
		// fieldName, fieldType);
		return creditReportingFieldRepository
				.findByFieldLabelLikeIgnoreCaseAndFieldTypeLikeIgnoreCaseAndActiveTrue(
						'%' + fieldName + '%', '%' + fieldType + '%');

	}

	public List<CertificateBookmarkAssociation> getAllCertificateBookmarkAssociation(
			String bookmarkLabel) {
		// return
		// accreditationDAO.getAllCertificateBookmarkAssociation(bookmarkLabel);
		return certificateBookmarkAssociationRepository
				.findByBookmarkLabelLikeIgnoreCase('%' + bookmarkLabel + '%');
	}

	/**
	 * This method returns the set of Course Approvals currently using the
	 * credit reporting field supplied as argument
	 * 
	 * @param creditReportingField
	 * @return
	 */
	// public Set<CourseApproval>
	// findSetOfContainingApprovals(CreditReportingField
	// creditReportingField,String approvalName, String approvalNumber){
	//
	// Set <CourseApproval> approvalSet= new HashSet<CourseApproval>();
	// List <CourseApproval> allApprovalList=
	// accreditationDAO.findCourseApproval(approvalName, approvalNumber, "","",
	// null);
	//
	// for(CourseApproval approval: allApprovalList)
	// if(approval.getCreditReportingFieldList().contains(creditReportingField))
	// approvalSet.add(approval);
	//
	// return approvalSet;
	// }

	public HashSet<RegulatorCategory> getContainingRegulatorCategories(
			CreditReportingField creditReportingField, String categoryType,
			String categoryName) {

		HashSet<RegulatorCategory> credCategorySet = new HashSet<RegulatorCategory>();
		// List<RegulatorCategory> allCategoryList =
		// accreditationDAO.findRegulatorCategory(categoryType,
		// categoryName,-1);
		List<RegulatorCategory> allCategoryList = regulatorCategoryRepository
				.findByCriteria(categoryType, categoryName, new Long(-1));

		for (RegulatorCategory category : allCategoryList)
			if (category.getReportingFields().contains(creditReportingField))
				credCategorySet.add(category);

		return credCategorySet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.softech.vu360.lms.service.AccreditationService#getCustomFieldById
	 * (long)
	 */
	public CustomField getCustomFieldById(long customFieldId) {
		// return accreditationDAO.getCustomFieldById(customFieldId);
		return customFieldRepository.findOne(customFieldId);
	}

	public Document getDocumentById(long documentId) {
		// return accreditationDAO.getDocumentById(documentId);
		return documentRepository.findOne(documentId);
	}

	public Instructor getInstructorByID(long instructorId) {

		// return accreditationDAO.getInstructorByID(instructorId);
		return instructorRepository.findOne(instructorId);
	}

	public List<Provider> findProviders(String name,
			RegulatoryAnalyst regulatoryAnalyst) {

		List<ContentOwner> listContentOwner = null;
		if (!regulatoryAnalyst.isForAllContentOwner()) {
			listContentOwner = contentOwnerRepository
					.findByRegulatoryAnalysts(regulatoryAnalyst);

		}

		return providerRepository.findProviders(name, listContentOwner);
	}

	public Provider getProviderById(long providerId) {
		// return accreditationDAO.getProviderById(providerId);
		return providerRepository.findOne(providerId);

	}

	public Certificate saveCertificate(Certificate certificate) {
		// return accreditationDAO.saveCertificate(certificate);
		return certificateRepository.save(certificate);
	}

	public ProviderApproval getProviderApprovalById(long providerApprovalId) {
		// return accreditationDAO.getProviderApprovalById(providerApprovalId);
		return providerApprovalRepository.findOne(providerApprovalId);
	}

	public InstructorApproval getInstructorApprovalById(
			long instructorApprovalId) {
		// return
		// accreditationDAO.getInstructorApprovalById(instructorApprovalId);
		return instructorApprovalRepository.findOne(instructorApprovalId);
	}

	@Override
	public ProviderApproval getProviderApprovalDeleteFalseById(Long providerApprovalId) {
		return providerApprovalRepository.findByIdAndDeletedFalse(providerApprovalId);
	}
	
	public InstructorApproval getInstructorApprovalDeleteFalseById(Long instructorApprovalId){
		return instructorApprovalRepository.findByIdAndDeletedFalse(instructorApprovalId);
	}
	
	public CourseApproval getCourseApprovalById(long courseApprovalId) {
		// return accreditationDAO.getCourseApprovalById(courseApprovalId);
		return courseApprovalRepository
				.findByIdAndDeletedFalse(courseApprovalId);
	}

	public Certificate getCertificateById(long id) {
		// return accreditationDAO.getCertificateById(id);
		return certificateRepository.findOne(id);
	}

	public Certificate getCertificateById(long id, boolean readFromDB) {
		// return accreditationDAO.getCertificateById(id, readFromDB);
		return certificateRepository.findOne(id);
	}

	public Affidavit getAffidavitById(long id) {
		// return accreditationDAO.getAffidavitById(id);
		return affidavitRepository.findOne(id);
	}

	public Affidavit getAffidavitById(long id, boolean readFromDB) {
		// return accreditationDAO.getAffidavitById(id, readFromDB);

		return affidavitRepository.findOne(id);
	}

	public List<Affidavit> getAffidavitsByName(String name) {
		return affidavitRepository
				.findByNameLikeIgnoreCaseAndActiveIsTrue('%' + name + '%');

	}

	public Affidavit loadForUpdateAffidavit(long id) {
		// return accreditationDAO.loadForUpdateAffidavit(id);
		return affidavitRepository.findOne(id);
	}

	public Affidavit saveAffidavit(Affidavit affidavit) {
		// return accreditationDAO.saveAffidavit(affidavit);
		return affidavitRepository.save(affidavit);
	}

	public List<Instructor> findInstructor(String firstName, String lastName,
			String emailAddress, VU360User user, String sortBy,
			String sortDirection) {
		// return accreditationDAO.findInstructor(name, , phone, user);

		return instructorRepository.findInstructor(firstName, lastName,
				emailAddress, user, sortBy, sortDirection);
	}

	public CourseConfigurationTemplate getTemplateById(long id) {
		// return accreditationDAO.getTemplateById(id);
		return courseConfigurationTemplateRepository.findOne(id);
	}

	public List<CourseConfigurationTemplate> findTemplates(String name,
			Date lastUpdatedDate) {
		// return accreditationDAO.findTemplates(name, lastUpdatedDate);

		return courseConfigurationTemplateRepository
				.findByNameAndLastUpdateDate(name, lastUpdatedDate);
	}

	public List<CourseConfigurationTemplate> findTemplates(String name,
			Date lastUpdatedDate, long contentOwnerId) {
		// return accreditationDAO.findTemplates(name, lastUpdatedDate,
		// contentOwnerId);
		return courseConfigurationTemplateRepository
				.findByNameAndLastUpdateDateAndContentOwnerId(name,
						lastUpdatedDate, contentOwnerId);
	}

	public CourseConfiguration getCourseConfigurationByTemplateId(
			long templateId, boolean readForcefullyFromDB) {
		// CourseConfiguration courseconfiguration = null;
		// courseconfiguration =
		// accreditationDAO.getCourseConfigurationByTemplateId(templateId,
		// readForcefullyFromDB);
		List<CourseConfiguration> courseconfiguration = courseConfigurationRepository
				.findByCourseConfigTemplateId(templateId);
		if (courseconfiguration != null && courseconfiguration.size() > 0) {
			return courseconfiguration.get(0);
		}
		return null;
	}

	public CourseConfiguration getCourseConfigurationByTemplateIdWithAssociatedCertificate(
			long templateId) {
		// CourseConfiguration courseconfiguration = null;
		// courseconfiguration =
		// accreditationDAO.getCourseConfigurationByTemplateIdWithAssociatedCertificate(templateId);

		List<CourseConfiguration> courseconfiguration = courseConfigurationRepository
				.findByCourseConfigTemplateId(templateId);
		if (courseconfiguration != null && courseconfiguration.size() > 0) {
			return courseconfiguration.get(0);
		}
		return null;
	}

	public CourseConfigurationTemplate saveCourseConfigurationTemplate(
			CourseConfigurationTemplate courseConfigurationTemplate) {
		// return
		// accreditationDAO.saveCourseConfigurationTemplate(courseConfigurationTemplate);
		return courseConfigurationTemplateRepository
				.save(courseConfigurationTemplate);
	}

	// public List<CreditReportingField>
	// getCreditReportingFieldByCourseApproval(CourseApproval courseApproval){
	// return courseApproval.getCreditReportingFieldList();//return
	// accreditationDAO.getCreditReportingFieldByCourseApproval(courseApproval);
	// }

	public CreditReportingField saveCreditReportingField(
			CreditReportingField creditReportingField) {
		// return
		// accreditationDAO.saveCreditReportingField(creditReportingField);
		return creditReportingFieldRepository.save(creditReportingField);
	}

	public CertificateBookmarkAssociation saveCertificateBookmarkAssociation(
			CertificateBookmarkAssociation cba) {
		// return accreditationDAO.saveCertificateBookmarkAssociation(cba);
		return certificateBookmarkAssociationRepository.save(cba);
	}

	@Transactional
	public CreditReportingFieldValueChoice saveChoice(
			CreditReportingFieldValueChoice option) {
		// return accreditationDAO.saveChoice(option);
		return creditReportingFieldValueChoiceRepository.save(option);
	}

	// public List<CreditReportingField> getCreditReportingFieldsByCourse(Course
	// course){
	// Date now = new Date(System.currentTimeMillis());
	// List<CreditReportingField> reportingFields = new
	// ArrayList<CreditReportingField>();
	// CourseApproval approval = this.getCourseApprovalByCourse(course, now);
	// if(approval!=null)
	// reportingFields.addAll(approval.getCreditReportingFieldList());
	//
	// return reportingFields;
	// }

	@Override
	public Set<CreditReportingField> getCreditReportingFieldsByCourse(
			Course course) {
		HashSet<CreditReportingField> reportingFields = new HashSet<CreditReportingField>();

		for (CourseApproval courseApproval : this
				.getCourseApprovalListByCourse(course))
			for (RegulatorCategory category : courseApproval
					.getRegulatorCategories())
				reportingFields.addAll(category.getReportingFields());

		return reportingFields;
	}

	@Override
	public List<CourseApprovalVO> getCourseApprovalByCourse_Jurisdiction(
			String courseId, String varLearnerEnrollmentId) {

		List<CourseApprovalVO> courseConfigList = new ArrayList<CourseApprovalVO>();

		// List<Map<Object, Object>>
		// mp_Jurisdiction=accreditationDAO.getCourseApprovalByCourse(courseId,
		// varLearnerEnrollmentId);
		List<Map<Object, Object>> mp_Jurisdiction = courseApprovalRepository
				.getCourseApprovalByCourse(courseId, varLearnerEnrollmentId);

		Map<String, CourseApprovalVO> distinctApprovalIds = new HashMap<String, CourseApprovalVO>();

		if (mp_Jurisdiction != null && !mp_Jurisdiction.isEmpty()) {

			for (Map<Object, Object> map : mp_Jurisdiction) {
				CourseApprovalVO courseApprovalVO = new CourseApprovalVO();
				courseApprovalVO.setCourseApprovalId(map
						.get("CourseApprovalID").toString());
				courseApprovalVO.setHoldingRegulator(map
						.get("HoldingRegulator").toString());
				courseApprovalVO.setApprovedCredithours(map.get(
						"APPROVEDCREDITHOURS").toString());
				courseApprovalVO
						.setCreditType(map.get("CREDITTYPE").toString());
				// courseApprovalVO.setCredentialName(map.get("CREDENTIALNAME").toString());
				distinctApprovalIds.put(map.get("CourseApprovalID").toString(),
						courseApprovalVO);
			}

			// update credit type in distinct list.
			courseConfigList = new ArrayList<CourseApprovalVO>(
					distinctApprovalIds.values());
			// comments out because of LMS-16197
			/*
			 * for (int i = 0; i < courseConfigList.size(); i++) {
			 * CourseApprovalVO c = courseConfigList.get(i); String creditType =
			 * StringUtils.EMPTY; //String credentialName = StringUtils.EMPTY;
			 * String creditHours = StringUtils.EMPTY; for(Map<Object, Object>
			 * map : mp_Jurisdiction){
			 * if(Integer.parseInt(c.getCourseApprovalId()) ==
			 * Integer.parseInt(map.get("CourseApprovalID").toString())){
			 * creditType = creditType + map.get("CREDITTYPE").toString() +
			 * "<br>"; //credentialName = credentialName +
			 * map.get("CREDENTIALNAME").toString() + "<br>"; creditHours =
			 * creditHours + map.get("APPROVEDCREDITHOURS").toString() + "<br>";
			 * } } creditType=StringUtils.strip(creditType, "<br>");
			 * c.setCreditType(creditType);
			 * 
			 * //c.setCredentialName(credentialName=StringUtils.strip(credentialName
			 * , "<br>"));
			 * c.setApprovedCredithours(StringUtils.strip(creditHours, "<br>"));
			 * }
			 */
		}

		CourseApprovalVOSort compartor = new CourseApprovalVOSort();
		compartor.setSortBy("holdingRegulator");
		compartor.setSortDirection(0);// ascending order
		Collections.sort(courseConfigList, compartor);

		return courseConfigList;
	}

	@Override
	public Set<CreditReportingField> getCreditReportingFieldsByCourseApproval(
			long courseApprovalId) {
		HashSet<CreditReportingField> reportingFields = new HashSet<CreditReportingField>();

		CourseApproval courseApproval = this
				.getCourseApprovalById(courseApprovalId);

		if (courseApproval != null) {
			for (RegulatorCategory category : courseApproval
					.getRegulatorCategories())
				reportingFields.addAll(category.getReportingFields());
		}
		return reportingFields;
	}

	public List<Learner> getLearnerByCourseCompletion(Long[] courseIdArray,
			Date startdate, Date endDate) {
		List<Learner> learners = new ArrayList<Learner>();
		List<LearnerCourseStatistics> learnerCourseStatisticsList = this
				.getCourseStatisticsList(courseIdArray, startdate, endDate);
		for (LearnerCourseStatistics learnercourseStatistics : learnerCourseStatisticsList) {
			boolean isDuplicate = false;
			for (Learner oldLearner : learners) {
				if (learnercourseStatistics.getLearnerEnrollment().getLearner()
						.getId().longValue() == oldLearner.getId().longValue()) {
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate)
				learners.add(learnercourseStatistics.getLearnerEnrollment()
						.getLearner());
		}
		return learners;
	}

	private List<LearnerCourseStatistics> getCourseStatisticsList(
			Long[] courseIdArray, Date startdate, Date endDate) {
		List<LearnerCourseStatistics> learnerCourseStatisticsList = new ArrayList<LearnerCourseStatistics>();
		int chunkLimit = 2050;
		if (courseIdArray.length < chunkLimit) {
			// learnerCourseStatisticsList.addAll(accreditationDAO.getLearnerCourseStatistics(courseIdArray,
			// startdate, endDate));
			learnerCourseStatisticsList
					.addAll(learnerCourseStatisticsRepository
							.findByLearnerEnrollmentCourseIdInAndCompletedTrueAndCompletionDateBetweenAndLearnerEnrollmentLearnerCustomerDistributorSelfReportingFalse(
									Arrays.asList(courseIdArray), startdate,
									endDate));
		} else {
			boolean complete = false;
			int to = 0, endIndex = courseIdArray.length - 1;
			while (!complete) {
				if ((to + chunkLimit) < endIndex) {
					to += chunkLimit;
				} else {
					to = endIndex;
					complete = true;
				}
				// learnerCourseStatisticsList.addAll(accreditationDAO.getLearnerCourseStatistics(Arrays.copyOfRange(courseIdArray,
				// from, to), startdate, endDate));
				learnerCourseStatisticsList
						.addAll(learnerCourseStatisticsRepository
								.findByLearnerEnrollmentCourseIdInAndCompletedTrueAndCompletionDateBetweenAndLearnerEnrollmentLearnerCustomerDistributorSelfReportingFalse(
										Arrays.asList(courseIdArray),
										startdate, endDate));
			}
		}
		return learnerCourseStatisticsList;
	}

	public List<Learner> SearchLearnerByCourseCompletion(Long[] courseIdArray,
			Date startdate, Date endDate, String firstName, String lastName,
			String email) {

		List<Learner> searchedLearners = new ArrayList<Learner>();
		List<Learner> learnersByCompletion = this.getLearnerByCourseCompletion(
				courseIdArray, startdate, endDate);
		boolean searchFilter = false;

		// Modified By Marium Saud : Fix for Existing Issue LMS-19669
		// Learner List is filtered on based of provided criteria after pulling
		// records from DB. Applied Java 8 Stream to filter retreived records.
		if (!StringUtils.isBlank(firstName) || !StringUtils.isBlank(lastName)
				|| !StringUtils.isBlank(email)) {
			searchFilter = true;

		}
		if (searchFilter) {
			if (!learnersByCompletion.isEmpty()) {
				List<Learner> filterLearner = learnersByCompletion
						.stream()
						.filter(p -> p.getVu360User().getFirstName()
								.contains(firstName)
								&& p.getVu360User().getLastName()
										.contains(lastName)
								&& p.getVu360User().getEmailAddress()
										.contains(email))
						.collect(Collectors.<Learner> toList());
				searchedLearners.addAll(filterLearner);
			}

		} else {
			searchedLearners.addAll(learnersByCompletion);
		}
		return searchedLearners;
	}

	public void removeCreditReportingField(
			List<CreditReportingField> reportingFields) {
		// accreditationDAO.removeCreditReportingField(reportingFields);
		creditReportingFieldRepository.delete(reportingFields);
	}

	public void deleteCertificateBookmarkAssociation(
			List<CertificateBookmarkAssociation> cbaList) {
		// accreditationDAO.deleteCertificateBookmarkAssociation(cbaList);
		certificateBookmarkAssociationRepository.delete(cbaList);
	}

	public List<CreditReportingFieldValue> getCreditReportingFieldValue(
			LearnerProfile learnerProfile) {
		// return
		// accreditationDAO.getCreditReportingFieldValue(learnerProfile.getId());
		return creditReportingFieldValueRepository
				.findByLearnerprofileId(learnerProfile.getId());
	}

	public List<CreditReportingFieldValue> getCreditReportingFieldValue(
			CreditReportingField reportingfield) {
		// return accreditationDAO.getCreditReportingFieldValue(reportingfield);
		return creditReportingFieldValueRepository
				.findByReportingCustomFieldId(reportingfield.getId());

	}

	public CreditReportingFieldValue getCreditReportingFieldValue(
			CreditReportingField reportingfield, LearnerProfile learnerProfile) {

		if (reportingfield instanceof StaticCreditReportingField) {
			CreditReportingFieldValue staticcrfv = new CreditReportingFieldValue();
			staticcrfv.setReportingCustomField(reportingfield);
			String str = vu360UserService.getValueForStaticReportingField(
					learnerProfile.getLearner().getVu360User(),
					reportingfield.getFieldLabel());
			staticcrfv.setValue(str == null ? "" : str);
			return staticcrfv;
		}
		// return
		// accreditationDAO.getCreditReportingFieldValue(reportingfield,learnerProfile.getId());
		List<CreditReportingFieldValue> creditReportingFieldValueList = creditReportingFieldValueRepository
				.findByReportingCustomFieldIdAndLearnerprofileId(
						reportingfield.getId(), learnerProfile.getId());
		return creditReportingFieldValueList != null
				&& creditReportingFieldValueList.size() > 0 ? creditReportingFieldValueList
				.get(0) : null;
	}

	public String getValueByReportingType(CreditReportingFieldValue fieldValue) {
		String value = "";
		if (fieldValue.getReportingCustomField() instanceof SingleSelectCreditReportingField
				|| fieldValue.getReportingCustomField() instanceof MultiSelectCreditReportingField) {
			List<CreditReportingFieldValueChoice> choises = fieldValue
					.getCreditReportingValueChoices();
			// value = "\"";
			for (CreditReportingFieldValueChoice c : choises)
				value = value + "," + c.getValue();
			// value = value + "\"";
		} else {
			value = fieldValue.getValue().toString();
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.service.AccreditationService#
	 * getCreditReportingFieldById(long)
	 */
	public CreditReportingField getCreditReportingFieldById(long id) {
		// return accreditationDAO.getCreditReportingFieldById(id);
		return creditReportingFieldRepository.findOne(id);
	}

	public boolean isCreditReportingFieldDuplicate(CreditReportingField field,
			long contentOwnerId) {
		// List<CreditReportingField> fields=
		// accreditationDAO.getCreditReportingFieldByLabel(field.getFieldLabel(),contentOwnerId);
		List<CreditReportingField> fields = creditReportingFieldRepository
				.findByFieldLabelAndContentOwnerIdAndActiveTrue(
						field.getFieldLabel(), contentOwnerId);

		boolean isDuplicate = false;
		if (field.getId() != null) {// This if condition is to ensure system
									// does not stop a reporting field from
									// editing itself.

			for (CreditReportingField crf : fields) {
				if (field.getFieldLabel().equals(crf.getFieldLabel())
						&& crf.getId() != field.getId()) {
					isDuplicate = true;
				}
			}
		}

		if (field.getId() == null && fields != null && fields.size() > 0) {// This
																			// if
																			// condition
																			// is
																			// for
																			// the
																			// case
																			// of
																			// Add
																			// new
																			// credit
																			// reporting
																			// field.
			isDuplicate = true;
		}
		return isDuplicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.service.AccreditationService#
	 * getOptionsByCreditReportingField
	 * (com.softech.vu360.lms.model.CreditReportingField)
	 */
	public List<CreditReportingFieldValueChoice> getOptionsByCreditReportingField(
			CreditReportingField creditReportingField) {
		// return
		// accreditationDAO.getOptionsByCreditReportingField(creditReportingField);
		return creditReportingFieldValueChoiceRepository
				.findByCreditReportingFieldIdOrderByDisplayOrderAsc(creditReportingField
						.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.softech.vu360.lms.service.AccreditationService#removeChoice(com.softech
	 * .vu360.lms.model.CreditReportingField)
	 */
	public void removeChoice(CreditReportingField creditReportingField) {
		List<CreditReportingFieldValueChoice> options = getOptionsByCreditReportingField(creditReportingField);
		/*
		 * for (CreditReportingFieldValueChoice optionToRemove : options) {
		 * accreditationDAO.removeChoice(optionToRemove); }
		 */

		creditReportingFieldValueChoiceRepository.delete(options);

	}

	public CourseApproval getCourseApprovalByCourse(Course course) {
		// List<CourseApproval> courseApprovals =
		// accreditationDAO.getCourseApprovvalByCourse(course);
		List<CourseApproval> courseApprovals = courseApprovalRepository
				.findByCourseAndDeletedFalseAndActiveTrue(course);

		if (courseApprovals != null && !courseApprovals.isEmpty()) {
			return courseApprovals.get(0);
		}
		return null;
	}

	public List<CourseApproval> getCourseApprovalListByCourse(Course course) {
		// List<CourseApproval> courseApprovals =
		// accreditationDAO.getCourseApprovvalByCourse(course);
		List<CourseApproval> courseApprovals = courseApprovalRepository
				.findByCourseAndDeletedFalseAndActiveTrue(course);
		if (courseApprovals == null)
			return new ArrayList<CourseApproval>();

		return courseApprovals;

	}

	public CourseApproval getCourseApprovalByCourse(Course course,
			Date compareDate) {
		// List<CourseApproval> courseApprovals =
		// accreditationDAO.getCourseApprovvalByCourse(course);
		List<CourseApproval> courseApprovals = courseApprovalRepository
				.findByCourseAndDeletedFalseAndActiveTrue(course);

		CourseApproval approval = null;
		for (CourseApproval courseApproval : courseApprovals) {
			if ((courseApproval.getCourseApprovalEffectivelyStartDate()
					.compareTo(compareDate) <= 0
					&& courseApproval.getCourseApprovalEffectivelyEndsDate()
							.compareTo(compareDate) >= 0 && !courseApproval
						.isDeleted())) {
				approval = courseApproval;
				break;
			}
		}
		return approval;
	}

	public void deleteContacts(List<Contact> contacts) {
		contactRepository.delete(contacts);
	}

	public RegulatoryApproval getRegulatoryApprovalById(
			long regulatoryApprovalId) {
		return regulatoryApprovalRepository.findOne(regulatoryApprovalId);

	}

	@Transactional
	public void saveApproval(RegulatoryApproval regulatoryApproval) {
		regulatoryApprovalRepository.save(regulatoryApproval);
	}

	public boolean isUniqueCustomFieldName(String entityType, String name) {
		return customFieldRepository.isUniqueCustomFieldName(entityType, name);
	}

	public Regulator loadForUpdateRegulator(long regulatorId) {
		return regulatorRepository.findOne(regulatorId);
	}

	public Credential loadForUpdateCredential(long credentialId) {
		return credentialRepository.findOne(credentialId);
	}

	public Contact loadForUpdateContact(long contactId) {
		return contactRepository.findOne(contactId);
	}

	public CustomField loadForUpdateCustomField(long customFieldId) {
		return customFieldRepository.findOne(customFieldId);
	}

	public Instructor loadForUpdateInstructor(long instructorId) {
		return instructorRepository.findOne(instructorId);
	}

	public Provider loadForUpdateProvider(long providerId) {
		return providerRepository.findOne(providerId);
	}

	public Certificate loadForUpdateCertificate(long id) {
		return certificateRepository.findOne(id);
	}

	public ProviderApproval loadForUpdateProviderApproval(
			long providerApprovalId) {
		return providerApprovalRepository.findOne(providerApprovalId);
	}

	public InstructorApproval loadForUpdateInstructorApproval(
			long instructorApprovalId) {
		return instructorApprovalRepository.findOne(instructorApprovalId);
	}

	public CourseApproval loadForUpdateCourseApproval(long courseApprovalId) {
		return courseApprovalRepository.findOne(courseApprovalId);
	}

	public Document loadForUpdateDocument(long documentId) {
		return documentRepository.findOne(documentId);
	}

	public CourseConfigurationTemplate loadForUpdateTemplate(long id) {
		return courseConfigurationTemplateRepository.findOne(id);
	}

	public CourseConfiguration loadForUpdateCourseConfiguration(long id) {
		return courseConfigurationRepository.findOne(id);
	}

	public ValidationQuestion loadForUpdateValidationQuestion(long id) {
		return validationQuestionRepository.findOne(id);
	}
	
	public CreditReportingField loadForUpdateCreditReportingField(long id) {
		return creditReportingFieldRepository.findOne(id);
	}

	public CreditReportingFieldValueChoice loadForUpdateCreditReportingFieldValueChoice(
			long id) {
		return creditReportingFieldValueChoiceRepository.findOne(id);
	}

	public CertificateBookmarkAssociation loadForUpdateCertificateBookmarkAssociation(
			long id) {
		return certificateBookmarkAssociationRepository.findOne(id);

	}

	public RegulatoryApproval loadForUpdateRegulatoryApproval(
			long regulatoryApprovalId) {
		return regulatoryApprovalRepository.findOne(regulatoryApprovalId);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public CredentialCategoryRequirement loadForUpdateCredentialCategoryRequirement(
			Long credCatReqId) {
		return credentialCategoryRequirementRepository.findOne(credCatReqId);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public List<CredentialCategoryRequirement> getCredentialCategoryRequirementsByCredential(
			Long credentialId) {
		return credentialCategoryRequirementRepository
				.findByCredentialCategoryCredentialId(credentialId);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public CredentialCategoryRequirement getCredentialCategoryRequirementById(
			Long credCatReqId) {
		return credentialCategoryRequirementRepository.findOne(credCatReqId);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public CredentialCategoryRequirement saveCredentialCategoryRequirement(
			CredentialCategoryRequirement credentialCategoryRequirement) {
		return credentialCategoryRequirementRepository
				.save(credentialCategoryRequirement);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public CredentialCategory saveCredentialCategory(
			CredentialCategory credentialCategory) {
		return this.credentialCategoryRepository.save(credentialCategory);

	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public CredentialCategory loadForUpdateCredentialCategory(
			Long credentialCategoryId) {
		return credentialCategoryRepository.findOne(credentialCategoryId);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public CredentialCategory getCredentialCategoryById(
			Long credentialCategoryId) {
		return credentialCategoryRepository.findOne(credentialCategoryId);
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public List<CredentialCategory> getCredentialCategoryByCredential(
			Long credentialId) {
		return credentialCategoryRepository.findByCredentialId(credentialId);

	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public List<CredentialCategory> getCredentialCategoryByCredential(
			Long credentialId, int sortDirection) {

		List<CredentialCategory> categoryList = new ArrayList<CredentialCategory>();
		categoryList = credentialCategoryRepository
				.findByCredentialId(credentialId);

		CredentialCategorySort categorySort = new CredentialCategorySort(
				sortDirection);
		Collections.sort(categoryList, categorySort);

		return categoryList;
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	public List<CredentialCategoryRequirement> getCredentialCategoryRequirementsByCategory(
			Long categoryId) {
		return credentialCategoryRequirementRepository
				.findByCredentialCategoryId(categoryId);
	}

	/**
	 * // [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential >
	 * Category > Requirement Convert give list of CredentialCategoryRequirement
	 * to Tree with complete hierarchy
	 */
	@Override
	public List<List<TreeNode>> getCategroyRequirementTreeByCredential(
			Long credentialId, boolean enableNodes) {

		List<CredentialCategory> categoryList = credentialCategoryRepository
				.findByCredentialId(credentialId);

		if (categoryList.isEmpty()) {
			return null;
		}

		CategoryRequirementTree crTree = new CategoryRequirementTree(
				enableNodes);
		for (CredentialCategory category : categoryList) {

			List<CredentialCategoryRequirement> requirementList = credentialCategoryRequirementRepository
					.findByCredentialCategoryId(category.getId());

			if (requirementList.isEmpty()) {
				crTree.addRootNode(category);
			} else {
				for (CredentialCategoryRequirement requirement : requirementList) {
					crTree.addChildNode(requirement);
				}
			}
		}

		return crTree.getTreeList();
	}

	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category
	// > Requirement
	@Transactional
	@Override
	public boolean deleteCredentialCategoryRequirements(String[] categoryIds,
			String[] requirementIds) {

		Long[] selectedCategoryIds = FormUtil.getStringAsLong(categoryIds);
		Long[] selectedRequirementIds = FormUtil
				.getStringAsLong(requirementIds);
		try {
			if (selectedRequirementIds.length > 0) {

				// Remove CredentialCategoryRequirement association from
				List<RegulatoryApproval> approvals = regulatoryApprovalRepository
						.findByRequirements(selectedRequirementIds);

				if (!approvals.isEmpty()) {
					for (RegulatoryApproval approval : approvals) {
						List<CredentialCategoryRequirement> requirementList = approval
								.getRequirements();
						for (Long requirementId : selectedRequirementIds) {
							for (CredentialCategoryRequirement requirement : requirementList) {
								if (requirement.getId().equals(requirementId)) {
									requirementList.remove(requirement);
									break;
								}
							}
						}
						RegulatoryApproval updatedApproval = regulatoryApprovalRepository
								.findOne(approval.getId());
						updatedApproval.setRequirements(requirementList);
						regulatoryApprovalRepository.save(updatedApproval);

					}
				}

				// Delete CredentialCategoryRequirements
				credentialCategoryRequirementRepository
						.delete(credentialCategoryRequirementRepository
								.findAll(Arrays.asList(selectedRequirementIds)));
			}

			// Delete CredentialCategory
			if (selectedCategoryIds.length > 0) {
				credentialCategoryRepository
						.delete(credentialCategoryRepository.findAll(Arrays
								.asList(selectedCategoryIds)));
			}
		} catch (Exception e) {
			log.debug(e);
			return false;
		}
		return true;
	}

	// [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential >
	// Category > Custom Fields
	@Override
	public Map<Object, Object> getCustomFieldsByCategory(Long categoryId,
			String sortColumn, int sortDirection, int pageIndex, int pageSize) {

		CredentialCategory category = credentialCategoryRepository
				.findOne(categoryId);
		List<ManageCustomField> finalList = new ArrayList<ManageCustomField>();
		int totalRecords = -1;

		if (category.getCustomFields() != null
				&& category.getCustomFields().size() > 0) {
			List<ManageCustomField> manageCustomFieldList = new ArrayList<ManageCustomField>();

			// Create ManageCustomFieldList
			for (CustomField customField : category.getCustomFields()) {
				ManageCustomField manageCustomField = new ManageCustomField();
				manageCustomField.copy(customField);
				manageCustomFieldList.add(manageCustomField);
			}

			totalRecords = manageCustomFieldList.size();

			// Sort the list
			CustomFieldSort sorter = new CustomFieldSort();
			sorter.setSortBy(sortColumn);
			sorter.setSortDirection(sortDirection);
			Collections.sort(manageCustomFieldList, sorter);

			// Apply Paging
			int startIndex = (pageSize < 0) ? -1 : (pageIndex * pageSize);
			int endIndex = (pageSize < 0) ? -1 : (startIndex + pageSize);

			if (startIndex == -1 && endIndex == -1) { // Show All
				finalList = manageCustomFieldList;
			} else { // Show Paginated
				endIndex = (manageCustomFieldList.size() < endIndex) ? manageCustomFieldList
						.size() : endIndex;
				finalList = manageCustomFieldList.subList(startIndex, endIndex);
			}
		}

		Map<Object, Object> serachResult = new HashMap<Object, Object>();
		serachResult.put("totalRecords", totalRecords);
		serachResult.put("customFieldList", finalList);
		return serachResult;
	}

	// [1/14/2011] LMS-8314 :: Regulatory Module Phase II - Credential >
	// Category > Custom Fields
	/**
	 * NOTE: Only delete Custom Field association with CredentialCategory as
	 * this is a safety mechanism so that we do not inadvertently delete data
	 * from the system - By Jason Burns
	 */
	public boolean deleteCategoryCustomFields(Long categoryId,
			String[] customFieldIds) {

		try {
			Long[] selCustomFieldIds = FormUtil.getStringAsLong(customFieldIds);
			if (selCustomFieldIds.length > 0) {

				// CredentialCategory category =
				// this.accreditationDAO.loadForUpdateCredentialCategory(categoryId);
				CredentialCategory category = credentialCategoryRepository
						.findOne(categoryId);
				if (category.getCustomFields() != null
						&& category.getCustomFields().size() > 0) {
					List<CustomField> customFields = category.getCustomFields();

					for (int index = 0; index < selCustomFieldIds.length; index++) {
						for (CustomField field : customFields) {
							if (field.getId().equals(selCustomFieldIds[index])) {
								customFields.remove(field);
								break;
							}
						}
					}
					category.setCustomFields(customFields);
					this.credentialCategoryRepository.save(category);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// [1/27/2011] LMS-8725 :: Add Category, show Global Custom Fields
	// (Refactored Code from Controller)
	@Override
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldsAndValues(
			List<CustomField> customFieldList,
			List<CustomFieldValue> customFieldValueList) {

		Map<Long, List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long, List<CustomFieldValueChoice>>();
		CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();

		for (CustomField customField : customFieldList) {
			if (customField instanceof SingleSelectCustomField
					|| customField instanceof MultiSelectCustomField) {
				List<CustomFieldValueChoice> customFieldValueChoices = this
						.getOptionsByCustomField(customField);
				fieldBuilder.buildCustomField(customField, 0,
						customFieldValueList, customFieldValueChoices);

				if (customField instanceof MultiSelectCustomField) {
					CustomFieldValue customFieldValue = this
							.getCustomFieldValueByCustomField(customField,
									customFieldValueList);
					existingCustomFieldValueChoiceMap.put(customField.getId(),
							customFieldValue.getValueItems());
				}

			} else {
				fieldBuilder.buildCustomField(customField, 0,
						customFieldValueList);
			}
		}

		List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldResultingList = fieldBuilder
				.getCustomFieldList();

		for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldResultingList) {
			if (customField.getCustomFieldRef() instanceof MultiSelectCustomField) {
				List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap
						.get(customField.getCustomFieldRef().getId());
				Map<Long, CustomFieldValueChoice> tempChoiceMap = new HashMap<Long, CustomFieldValueChoice>();

				for (CustomFieldValueChoice customFieldValueChoice : existingCustomFieldValueChoiceList) {
					tempChoiceMap.put(customFieldValueChoice.getId(),
							customFieldValueChoice);
				}

				for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField
						.getCustomFieldValueChoices()) {
					if (tempChoiceMap.containsKey(customFieldValueChoice
							.getCustomFieldValueChoiceRef().getId())) {
						customFieldValueChoice.setSelected(true);
					}
				}
			}
		}

		return customFieldResultingList;
	}

	// [1/27/2011] LMS-8725 :: Add Category, show Global Custom Fields
	// (Refactored Code from Controller)
	private CustomFieldValue getCustomFieldValueByCustomField(
			com.softech.vu360.lms.model.CustomField customField,
			List<CustomFieldValue> customFieldValues) {
		if (customFieldValues != null) {
			for (CustomFieldValue customFieldValue : customFieldValues) {
				if (customFieldValue.getCustomField() != null) {
					if (customFieldValue.getCustomField().getId()
							.compareTo(customField.getId()) == 0) {
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}

	// [1/27/2011] LMS-8725 :: Add Category, show Global Custom Fields
	// (Refactored Code from Controller)
	@Override
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFieldAndValuesForRendering(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> rawCustomFieldList) {
		CustomFieldBuilder fieldBuilder = new CustomFieldBuilder();
		Map<Long, com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldMap = new HashMap<Long, com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
		for (com.softech.vu360.lms.web.controller.model.customfield.CustomField tempcustomField : rawCustomFieldList) {

			CustomField customField = tempcustomField.getCustomFieldRef();
			List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
			CustomFieldValue tempCustomFieldValue = tempcustomField
					.getCustomFieldValueRef();
			tempCustomFieldValue.setCustomField(customField);
			customFieldValues.add(tempcustomField.getCustomFieldValueRef());

			if (customField instanceof SingleSelectCustomField
					|| customField instanceof MultiSelectCustomField) {
				List<CustomFieldValueChoice> customFieldValueChoices = this
						.getOptionsByCustomField(customField);
				fieldBuilder.buildCustomField(customField,
						tempcustomField.getStatus(), customFieldValues,
						customFieldValueChoices);

				if (customField instanceof MultiSelectCustomField) {
					customFieldMap.put(tempcustomField.getCustomFieldRef()
							.getId(), tempcustomField);
				}

			} else {
				customFieldValues.get(0).setValue(
						HtmlEncoder.escapeHtmlFull(customFieldValues.get(0)
								.getValue().toString()));
				fieldBuilder.buildCustomField(customField,
						tempcustomField.getStatus(), customFieldValues);
			}
		}

		List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList = fieldBuilder
				.getCustomFieldList();
		for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldList) {
			if (customField.getCustomFieldRef() instanceof MultiSelectCustomField) {

				com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField = customFieldMap
						.get(customField.getCustomFieldRef().getId());
				if (tempCustomField != null) {

					Map<Long, com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice> tempCustomFieldValueChoiceMap = new HashMap<Long, com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice>();

					for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice tempCustomFieldValueChoice : tempCustomField
							.getCustomFieldValueChoices()) {
						tempCustomFieldValueChoiceMap
								.put(tempCustomFieldValueChoice
										.getCustomFieldValueChoiceRef().getId(),
										tempCustomFieldValueChoice);
					}

					for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField
							.getCustomFieldValueChoices()) {
						if (tempCustomFieldValueChoiceMap
								.containsKey(customFieldValueChoice
										.getCustomFieldValueChoiceRef().getId())) {
							customFieldValueChoice
									.setSelected(tempCustomFieldValueChoiceMap
											.get(customFieldValueChoice
													.getCustomFieldValueChoiceRef()
													.getId()).isSelected());
						}
					}

				}
			}
		}

		return customFieldList;
	}

	// [1/27/2011] LMS-8725 :: Add Category, show Global Custom Fields
	// (Refactored Code from Controller)
	public List<CustomFieldValue> getCustomFieldValues(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList,
			boolean escapeValues) {

		List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
		if (customFieldList.size() > 0) {
			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldList) {
				if (customField.getCustomFieldRef() instanceof MultiSelectCustomField) {

					MultiSelectCustomField multiSelectCustomField = (MultiSelectCustomField) customField
							.getCustomFieldRef();
					if (multiSelectCustomField.getCheckBox()) {

						List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
						for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField
								.getCustomFieldValueChoices()) {

							if (customFieldValueChoice.isSelected()) {
								CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoice
										.getCustomFieldValueChoiceRef();
								customFieldValueChoices
										.add(customFieldValueChoiceRef);
							}

						}
						CustomFieldValue customFieldValue = customField
								.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField
								.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);
					} else {
						List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
						if (customField.getSelectedChoices() != null) {
							Map<Long, CustomFieldValueChoice> customFieldValueChoiceMap = new HashMap<Long, CustomFieldValueChoice>();
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField
									.getCustomFieldValueChoices()) {
								customFieldValueChoiceMap
										.put(customFieldValueChoice
												.getCustomFieldValueChoiceRef()
												.getId(),
												customFieldValueChoice
														.getCustomFieldValueChoiceRef());
							}

							for (String selectedChoiceIdString : customField
									.getSelectedChoices()) {
								if (customFieldValueChoiceMap
										.containsKey(new Long(
												selectedChoiceIdString))) {
									CustomFieldValueChoice customFieldValueChoiceRef = customFieldValueChoiceMap
											.get(new Long(
													selectedChoiceIdString));
									customFieldValueChoices
											.add(customFieldValueChoiceRef);
								}
							}
						}

						CustomFieldValue customFieldValue = customField
								.getCustomFieldValueRef();
						customFieldValue.setCustomField(customField
								.getCustomFieldRef());
						customFieldValue.setValueItems(customFieldValueChoices);

						customFieldValues.add(customFieldValue);

					}

				} else {
					CustomFieldValue customFieldValue = customField
							.getCustomFieldValueRef();
					customFieldValue.setCustomField(customField
							.getCustomFieldRef());
					if (customFieldValue.getValue() != null) {
						if (escapeValues) {
							customFieldValue.setValue(HtmlEncoder
									.escapeHtmlFull(
											customFieldValue.getValue()
													.toString()).toString());
						} else {
							customFieldValue.setValue(customFieldValue
									.getValue().toString());
						}
					}

					customFieldValues.add(customFieldValue);
				}
			}
		}
		return customFieldValues;
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public boolean customFieldHasValue(CustomField customField) {

		Long s = customFieldValueRepository.countByCustomField(customField);
		if (s != null && s > 0) {
			return true;
		}
		return false;

	}

	@Override
	public Set<RegulatorCategory> getAllRegulatorCategories(long regulatorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegulatorCategory loadForUpdateRegulatorCategory(
			long regulatorcategoryId) {
		return regulatorCategoryRepository.findOne(regulatorcategoryId);
	}

	@Override
	public RegulatorCategory getRegulatorCategory(long regulatorcategoryId) {
		return regulatorCategoryRepository.findOne(regulatorcategoryId);
	}

	@Override
	public RegulatorCategory saveRegulatorCategory(
			RegulatorCategory regulatorCategory) {
		return regulatorCategoryRepository.save(regulatorCategory);

	}

	@Override
	public RegulatorCategory saveRegulatorCategoryCreditReporting(
			RegulatorCategory regulatorCategory) {
		return regulatorCategoryRepository.save(regulatorCategory);
	}

	@Override
	public RegulatorCategory dropRegulatorCategoryCreditReporting(
			RegulatorCategory regulatorCategory) {
		return regulatorCategoryRepository.save(regulatorCategory);
	}

	@Override
	@Transactional
	public void deleteRegulatorCategories(List<RegulatorCategory> categories) {
		try {
			regulatorCategoryRepository.delete(categories);
		} catch (Exception e) {
		}
	}

	@Override
	public ArrayList<RegulatorCategory> findRegulatorCategories(
			String categoryName, String categoryType, long regulatorId) {
		return (ArrayList<RegulatorCategory>) regulatorCategoryRepository
				.findByCriteria(categoryType, categoryName, (Long) regulatorId);
	}

	@Override
	public ArrayList<RegulatorCategory> findRegulatorCategories(long regulatorId) {
		if (regulatorId > 0) {
			return (ArrayList<RegulatorCategory>) regulatorCategoryRepository
					.findByRegulator((Long) regulatorId);
		} else {
			return (ArrayList<RegulatorCategory>) regulatorCategoryRepository
					.findAllByOrderByDisplayNameAsc();
		}
	}

	@Override
	public List<Modality> getAllModalities() {
		return (List<Modality>) modalityRepository.findAll();
	}

	public void unAssignCategoryFromAllCourseApprovals(
			RegulatorCategory regulatorCategory) {
		List<CourseApproval> courseApprovalList = this.findCourseApproval("",
				"", "", "", -1);
		for (CourseApproval approval : courseApprovalList) {
			CourseApproval dbApproval = this
					.loadForUpdateCourseApproval(approval.getId());
			RegulatorCategory dbRegCategory = dbApproval.getRegulatorCategory();
			if (dbRegCategory != null) {
				if (dbRegCategory.getId().longValue() == regulatorCategory
						.getId().longValue()) {
					dbApproval.setRegulatorCategory(null);
					saveApproval(dbApproval);
				}
			}
		}
	}

	public List<Asset> findAssets(ContentOwner co, String name, String keywords) {
		/*
		 * TODO :Need to create Classes for each @DiscriminatorValue of Asset
		 */
		return assetRepository.findAssetByContentownerAndNameAndKeywords(
				co.getId(), name, keywords);
	}

	public List<CourseApproval> getCourseApprovalByCourseConfigurationTemplate(
			long ccTemplateId) {
		return courseApprovalRepository
				.findByTemplateIdAndDeletedFalse(ccTemplateId);
	}

	public PurchaseCertificateNumber savePurchaseCertificateNumber(
			PurchaseCertificateNumber purchaseCertificateNumber) {
		PurchaseCertificateNumber newPurchaseCertificateNumber = this
				.loadForUpdatePurchaseCertificateNumber(purchaseCertificateNumber
						.getId());
		newPurchaseCertificateNumber = purchaseCertificateNumber.clone();
		newPurchaseCertificateNumber = purchaseCertificateNumberRepository
				.save(newPurchaseCertificateNumber);

		return newPurchaseCertificateNumber;
	}

	public PurchaseCertificateNumber loadForUpdatePurchaseCertificateNumber(
			long id) {
		return purchaseCertificateNumberRepository.findOne(id);
	}

	public PurchaseCertificateNumber addPurchaseCertificateNumber(
			PurchaseCertificateNumber purchaseCertificateNumber) {
		return purchaseCertificateNumberRepository
				.save(purchaseCertificateNumber);
	}

	public void deletePurchaseCertificateNumbers(
			List<PurchaseCertificateNumber> certificateNumbers) {
		purchaseCertificateNumberRepository.delete(certificateNumbers);
	}

	public List<CertificateBookmarkAssociation> getCertBookMarkAssociations(
			PdfFieldsEnum type) {
		return certificateBookmarkAssociationRepository.findByFieldsType(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.service.AccreditationService#
	 * getRegulatoryApprovalByRegulatoryCategory(java.lang.Long)
	 */
	@Override
	public List<InstructorApproval> getInstructorApprovalsByCatIds(
			Long[] regCatId) {

		List<InstructorApproval> instructorApprovalList = null;
		if (regCatId != null) {
			instructorApprovalList = instructorApprovalRepository
					.findByRegulatorCategoryIdIn(Arrays.asList(regCatId));

		}
		return instructorApprovalList;
	}

	public List<CertificateBookmarkAssociation> getBookMarkAssociationsByReportingField(
			Long[] reportingFields) {
		return certificateBookmarkAssociationRepository
				.findByreportingFieldIdIn(reportingFields);
	}

	public boolean isCourseAlreadyAssociatedWithCourseApproval(String courseId) {
		boolean isAlreadyAssociated = false;
		if (courseId != null) {
			List<CourseApproval> courseApprovalList = courseApprovalRepository
					.findByCourseIdAndDeletedFalseAndActiveTrue(new Long(
							courseId));

			{
				if (courseApprovalList != null && courseApprovalList.size() > 0) {
					isAlreadyAssociated = true;
				}
			}
		}
		return isAlreadyAssociated;
	}

	public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(
			long courseId, long regulatorCategory, Date startDate, Date endDate) {
		return courseApprovalRepository
				.isCourseAlreadyAssociatedWithRegulatorAuthority(courseId,
						regulatorCategory, startDate, endDate);
	}

	public boolean isCourseAlreadyAssociatedWithRegulatorAuthority(
			long courseId, long regulatorCategory, Date startDate,
			Date endDate, long excludeCourseApprovalId) {
		return courseApprovalRepository
				.isCourseAlreadyAssociatedWithRegulatorAuthority(courseId,
						regulatorCategory, startDate, endDate,
						excludeCourseApprovalId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.service.AccreditationService#
	 * isAffidavitLinkedWithCourseApproval(long[])
	 */
	@Override
	public boolean isAffidavitLinkedWithCourseApproval(Long[] certificateIds) {
		return courseApprovalRepository
				.isAffidavitLinkedWithCourseApproval(Arrays
						.asList(certificateIds));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.service.AccreditationService#
	 * isCertificateLinkedWithCourseApproval(long[])
	 */
	@Override
	public boolean isCertificateLinkedWithCourseApproval(Long[] certificateIds) {
		return courseApprovalRepository
				.isCertificateLinkedWithCourseApproval(Arrays
						.asList(certificateIds));
	}

	public boolean isCourseApprovalLinkedWithLearnerEnrollment(
			Long courseApprovalId) {
		return learningSessionRepository
				.isCourseApprovalLinkedWithLearnerEnrollment(courseApprovalId);
	}

	public boolean isCourseApprovalLinkedWithLearnerEnrollment(
			Long[] courseApprovalIds) {
		return learningSessionRepository
				.isCourseApprovalLinkedWithLearnerEnrollment(Arrays
						.asList(courseApprovalIds));
	}

	@Transactional
	public boolean deleteRegulatoryApproval(RegulatoryApproval approval) {
		try {
			regulatoryApprovalRepository.delete(approval);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public List<Certificate> getCertificatesWhereAssetVersionIsEmpty(
			Integer fromCertificateId, Integer toCertificateId) {
		return certificateRepository.getCertificatesWhereAssetVersionIsEmpty(
				new Long(fromCertificateId), new Long(toCertificateId));
	}

	@Override
	public boolean isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(
			long regulatorId, String regulatorCategoryType,
			long excludeCategoryId) {
		return regulatorCategoryRepository
				.isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(
						regulatorId, regulatorCategoryType, excludeCategoryId);
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public List<Proctor> getAllProctorsByCredential(Long credentialId) {
		return proctorRepository.findByCredentialsIdAndStatusNot(credentialId,
				ProctorStatusEnum.Expired.toString());
	}

	public boolean isCourseApprovalSelected(Long enrollmentId) {
		List<LearningSession> listLearningSession = learningSessionRepository
				.findByEnrollmentIdAndCourseApprovalIdGreaterThan(enrollmentId,
						0L);
		if (listLearningSession != null && listLearningSession.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCourseApprovalSelected(Long enrollmentId, Long learnerId,
			String courseCode) {
		List<LearningSession> listLearningSession = learningSessionRepository
				.findByEnrollmentIdAndCourseApprovalIdGreaterThanAndLearnerIdAndCourseId(
						enrollmentId, 0L, learnerId, courseCode);
		if (listLearningSession != null && listLearningSession.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public Long getCourseApprovalSelected(Long enrollmentId) {
		List<LearningSession> listLearningSession = learningSessionRepository
				.findByEnrollmentIdAndCourseApprovalIdGreaterThan(enrollmentId,
						0L);
		if (listLearningSession != null && listLearningSession.size() > 0) {
			return listLearningSession.get(0).getCourseApprovalId();
		} else {
			return null;
		}
	}

	public Long getCourseApprovalSelected(Long enrollmentId, Long learnerId,
			String courseCode) {
		List<LearningSession> listLearningSession = learningSessionRepository
				.findByEnrollmentIdAndCourseApprovalIdGreaterThanAndLearnerIdAndCourseId(
						enrollmentId, 0L, learnerId, courseCode);
		if (listLearningSession != null && listLearningSession.size() > 0) {
			return listLearningSession.get(0).getCourseApprovalId();
		} else {
			return null;
		}
	}

	public List<LearningSession> getLearningSessionForCourseApproval(List<String> courseIds, Long learnerId) {
		List<LearningSession> listAllLearningSession = new ArrayList<LearningSession>();
		if (!courseIds.isEmpty()) {
			int CHUNK_LIMIT = 2050;
			int listSize = courseIds.size();
			if (courseIds.size() < CHUNK_LIMIT) {
				listAllLearningSession = learningSessionRepository.findByLearnerIdAndCourseApprovalIdGreaterThanAndCourseIdIn(learnerId, 0L, courseIds);
			} else {
				for (int i = 0; i < listSize; i += CHUNK_LIMIT) {
					List<String> courseIdsSubList = breakCourseIds(courseIds,listSize, CHUNK_LIMIT, i);
					if (!CollectionUtils.isEmpty(courseIdsSubList)) {
						List<LearningSession> learningSession = learningSessionRepository.findByLearnerIdAndCourseApprovalIdGreaterThanAndCourseIdIn(learnerId, 0L, courseIdsSubList);
						if (learningSession != null) {
							listAllLearningSession.addAll(learningSession);
						}
					}
				}
			}

		}
		return listAllLearningSession;
	}

	public List<LearningSession> getLearningSessionByEnrollmentId(
			Long enrollmentId) {
		return learningSessionRepository.findByEnrollmentId(enrollmentId);
	}

	public int getNumberOfUnusedPurchaseCertificateNumbers(long courseApprovalId) {
		return courseApprovalRepository
				.getNumberOfUnusedPurchaseCertificateNumbers(courseApprovalId);
	}

	public PurchaseCertificateNumber getUnusedPurchaseCertificateNumber(
			long courseApprovalId) {
		return purchaseCertificateNumberRepository
				.getUnusedPurchaseCertificateNumber(courseApprovalId);
	}

	@Override
	public List<CourseApproval> getCourseApprovalsByDateRange(Date date,
			Date after7DayDate) throws Exception {
		return courseApprovalRepository
				.findByCourseApprovalEffectivelyEndsDateBetweenAndDeletedFalseAndActiveTrue(
						date, after7DayDate);
	}

	@Override
	public void removeChoices(List<CreditReportingFieldValueChoice> options) {
		for (CreditReportingFieldValueChoice optionToRemove : options) {
			creditReportingFieldValueChoiceRepository.delete(optionToRemove);
		}
	}

	@Override
	public Document loadDocumentByName(String documentName) {
		return documentRepository.findFirstByNameOrderByIdDesc(documentName);
	}

	// New optimized method to reduce the number of queries executing when
	// performing search for Accreditation
	@Override
	public List<Regulator> searchRegulator(String name,
			RegulatoryAnalyst regulatoryAnalyst) {
		List<ContentOwner> listContentOwner = null;
		if (!regulatoryAnalyst.isForAllContentOwner()) {
			listContentOwner = contentOwnerRepository
					.findByRegulatoryAnalysts(regulatoryAnalyst);
		}

		return regulatorRepository.searchRegulator(name, listContentOwner);
	}

	// New optimized method to reduce the number of queries executing when
	// performing search for Accreditation
	@Override
	public List<Provider> searchProviders(String name,
			RegulatoryAnalyst regulatoryAnalyst) {

		List<ContentOwner> listContentOwner = null;
		if (!regulatoryAnalyst.isForAllContentOwner()) {
			listContentOwner = contentOwnerRepository
					.findByRegulatoryAnalysts(regulatoryAnalyst);

		}

		return providerRepository.searchProviders(name, listContentOwner);
	}

	// New optimized method to reduce the number of queries executing when
	// performing search for Accreditation
	@Override
	public List<Instructor> searchInstructor(String firstName) {
		return instructorRepository.searchInstructorByFirstName(firstName);
	}
	
	private List<String> breakCourseIds(List<String> courseIds, int listSize, int inParamLimit, int index) {
		
		List<String> courseIdsSubList = null;
		if (!CollectionUtils.isEmpty(courseIds)) {
			if (listSize  > index + inParamLimit) {
		        courseIdsSubList = courseIds.subList(index, (index + inParamLimit));
		    } else {
		        courseIdsSubList = courseIds.subList(index, listSize);
		    }
		}
        return courseIdsSubList;
	}

	@Override
	@Transactional
	public ValidationQuestion saveValidationQuestion(ValidationQuestion validationQuestion) {
		Language language = languageRepository.findByLanguage(validationQuestion.getCreatedBy().getVu360User().getLanguage().getLanguage());
		validationQuestion.setLanguage(language);
		return validationQuestionRepository.save(validationQuestion);
	}

	@Override
	public List<ValidationQuestion> getUniqueValidationQuestionByCourseConfigurationId(long id) {
		return validationQuestionRepository.findByCourseConfigurationId(id);
	}

	@Override
	@Transactional
	public void deleteValidationQuestion(List<Long> lsValidationQuestionIds) {
		validationQuestionRepository.deleteByIdIn(lsValidationQuestionIds);
		
	}

	@Override
	public PurchaseCertificateNumber checkForPurchaseNumberAssociation(CourseApproval courseApproval, String certificateNumber) {
		return purchaseCertificateNumberRepository.findOneByCourseApprovalAndCertificateNumber(courseApproval, certificateNumber);
	}
}