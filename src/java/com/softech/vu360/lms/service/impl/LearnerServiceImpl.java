package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.helpers.LoginSecurityHelper;
import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerGroupMember;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LearnerValidationAnswers;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.OrganizationalGroupMember;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.RegulatoryAnalyst;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.ValidationQuestion;
import com.softech.vu360.lms.model.Widget;
import com.softech.vu360.lms.repositories.AddressRepository;
import com.softech.vu360.lms.repositories.ContentOwnerRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldValueChoiceRepository;
import com.softech.vu360.lms.repositories.CreditReportingFieldValueRepository;
import com.softech.vu360.lms.repositories.DistributorGroupRepository;
import com.softech.vu360.lms.repositories.DistributorRepository;
import com.softech.vu360.lms.repositories.LMSRoleLMSFeatureRepository;
import com.softech.vu360.lms.repositories.LMSRoleRepository;
import com.softech.vu360.lms.repositories.LearnerGroupItemRepository;
import com.softech.vu360.lms.repositories.LearnerGroupMemberRepository;
import com.softech.vu360.lms.repositories.LearnerGroupRepository;
import com.softech.vu360.lms.repositories.LearnerPreferencesRepository;
import com.softech.vu360.lms.repositories.LearnerProfileRepository;
import com.softech.vu360.lms.repositories.LearnerRepository;
import com.softech.vu360.lms.repositories.LearnerValidationAnswersRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupMemberRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupRepository;
import com.softech.vu360.lms.repositories.RegistrationInvitationRepository;
import com.softech.vu360.lms.repositories.RepositorySpecificationsBuilder;
import com.softech.vu360.lms.repositories.TimeZoneRepository;
import com.softech.vu360.lms.repositories.TrainingAdministratorRepository;
import com.softech.vu360.lms.repositories.VU360UserRepository;
import com.softech.vu360.lms.repositories.WidgetRepository;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.UserWidgetService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.vo.UniqueQuestionAnswerVO;
import com.softech.vu360.lms.web.controller.manager.AssignSurveyController;
import com.softech.vu360.lms.web.controller.model.LearnerValidationQASetDTO;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.client.impl.VcsDiscussionForumClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.ProctorStatusEnum;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Ashis
 * @modifed by Fasial A. Siddiqui
 */
public class LearnerServiceImpl implements LearnerService {

	@Inject
	private LearnerValidationAnswersRepository learnerValidationAnswerRepository;
	@Inject
	private TimeZoneRepository timeZoneRepository;
	@Inject
	private RegistrationInvitationRepository registrationInvitationRepository;
	@Inject
	private OrganizationalGroupRepository organizationalGrpRepository;
	@Inject
	private LMSRoleRepository lmsRoleRepository;
	@Inject
	private LMSRoleLMSFeatureRepository lmsRoleLMSFeatureRepository;
	@Inject
	private LearnerPreferencesRepository learnerPreferenceRepository;
	@Inject
	private LearnerProfileRepository learnerProfileRepository;
	@Inject
	private LearnerGroupRepository learnerGroupRepository;
	@Inject
	private LearnerGroupItemRepository learnerGroupItemRepository;
	@Inject
	private CreditReportingFieldValueChoiceRepository crfValueChoiceRepository;
	@Inject
	private CreditReportingFieldValueRepository crfValueRepository;
	@Inject
	private CreditReportingFieldRepository crfRepository;
	@Inject
	private ContentOwnerRepository contentOwnerRepository;
	@Inject
	private LearnerRepository learnerRepository;
	@Inject
	OrganizationalGroupMemberRepository organizationalGroupMemberRepository;
	@Inject
	LearnerGroupMemberRepository learnerGroupMemberRepository;
	@Inject
	private VU360UserRepository vu360UserRepository;
	@Inject
	DistributorGroupRepository distributorGroupRepository;
	@Inject
	DistributorRepository distributorRepository;
	@Inject
	AddressRepository addressRepository;
	@Inject
	private TrainingAdministratorRepository trainingAdminstratorRepository;

	private PasswordEncoder passwordEncoder = null;
	private SaltSource saltSource = null;
	private AuthorService authorService = null;
	private AccreditationService accreditationService = null;
	private EntitlementService entitlementService = null;
	private CustomerService customerService = null;
	private UserWidgetService userWidgetService;
	private ActiveDirectoryService activeDirectoryService = null;
	private EnrollmentService enrollmentService = null;
	private VU360UserService vu360UserService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	
	@Inject
	private WidgetRepository widgetRepository;
	private boolean isProctorRole = false;

	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	public UserWidgetService getUserWidgetService() {
		return userWidgetService;
	}

	public void setUserWidgetService(UserWidgetService userWidgetService) {
		this.userWidgetService = userWidgetService;
	}

	private static final String DFC_USER_TYPE_LEARNER = "L";

	private static final Logger log = Logger.getLogger(LearnerServiceImpl.class
			.getName());

	public List<VU360User> findLearner(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId) {
		List<VU360User> results = new ArrayList<VU360User>();
		if (trainingAdmin_isManagesAllOrganizationalGroups || (managedGroups!=null && managedGroups.size() > 0)) {
			Map<Object, Object> searchResult = (Map<Object, Object>) this.getAllUsersByCriteria(
					"", null, isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, -1, -1, "", -1, firstName,
					lastName, email, -1, false);
			results = (ArrayList<VU360User>) searchResult.get("list");
		}
		log.debug((results == null) ? "Null Result" : "Data Size="
				+ results.size());
		return results;
	}

	public Learner getLearnerForB2CCustomer(Customer cust) {
		if (cust.getCustomerType().equalsIgnoreCase(Customer.B2B)) {
			throw new IllegalArgumentException(
					"provided customer is not a B2C customer!");
		}
		List<Learner> learnerList = new ArrayList<Learner>();
		Learner learner = null;
		try {
			learnerList = learnerRepository.findByCustomerId(cust.getId());
			if (!learnerList.isEmpty()) {
				learner = learnerList.get(0);
			}
		} catch (ObjectRetrievalFailureException e) {
			log.error(
					"learner not found for customer: " + cust.getCustomerCode(),
					e);
		}
		return learner;
	}

	public Long getLearnerForSelectedCustomer(Long customerId) {
		Long learnerId = 0l;
		Learner learner =learnerRepository.findFirstByCustomerIdOrderByIdAsc(customerId);
		
		if(learner != null) {
			learnerId = learner.getId();
		} 
		
		return learnerId;
	}

	public Long getLearnerForSelectDistributor(Long myCustomerId) {
		
		Long learnerId = 0l;
		Learner learner =learnerRepository.findFirstByCustomerIdOrderByIdAsc(myCustomerId);
		
		if(learner != null) {
			learnerId = learner.getId();
		} 
		
		return learnerId;
	}

	public List<VU360User> findLearner(String searchCriteria,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId) {
		Map<Object, Object> searchResult = (Map<Object, Object>) this.getAllUsersByCriteria(searchCriteria, null, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				-1, -1, "", -1, "", "", "",
				-1, false);
		List<VU360User> results = (ArrayList<VU360User>) searchResult
				.get("list");
		log.debug((results == null) ? "Null Result" : "Data Size="
				+ results.size());
		return results;
	}

	public Map<Object, Object> findAllLearnersWithCriteria(String name,
			String name2, String mail, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy,
			int sortDirection) {
		Map<Object, Object> results = new HashMap<Object, Object>();
		results = (Map<Object, Object>) this.getAllUsersByCriteria("", null,
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				-1, -1, sortBy, sortDirection, name, name2, mail,
				-1, false);
		return results;
	}

	public Map<Object, Object> findAllLearners(String searchCriteria,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection) {
		Map<Object, Object> results = new HashMap<Object, Object>();
		results = (Map<Object, Object>) this.getAllUsersByCriteria(searchCriteria,null, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				-1, -1, sortBy, sortDirection, "", "", "",
				-1, false);
		return results;
	}

	
	public Map<Object, Object> findAllLearners(String searchCriteria, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize, String sortBy, int sortDirection) {
		
		Map<Object, Object> results = new HashMap<Object, Object>();
		results = (Map<Object, Object>) this.getAllUsersByCriteria(searchCriteria, null, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				pageIndex, pageSize, sortBy, sortDirection, "", "", "",	-1, false);
		return results;
	}

	public List<VU360User> findAllSystemLearners(Collection<String> vList) {
		List<VU360User> results = null;
		if (vList.size() > 0) {
			results = vu360UserRepository.findByUsernameIn(vList);
		}

		return results;
	}

	public List<VU360User> findAllLearner(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize, String sortBy, int sortDirection, int limit) {
		Map<Object, Object> searchResult = (Map<Object, Object>) this.getAllUsersByCriteria("", null, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				pageIndex, -1, sortBy, sortDirection,
				firstName, lastName, email, -1, false);
		List<VU360User> results = (ArrayList<VU360User>) searchResult
				.get("list");
		return results;
	}

	public Map<Object, Object> findAllLearnersByCustomer(String firstName, String lastName,
			String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection, int limit) {
		Map<Object, Object> results = (Map<Object, Object>) this.getAllUsersByCriteria("", null, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				-1, -1, sortBy, sortDirection,
				firstName, lastName, email, limit, false);
		return results;
	}

	public Map<Object, Object> findLearner1(String searchCriteria,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, 
			Long customerId, Long userId, int pageIndex, int pageSize, String sortBy,
			int sortDirection) {
		Map<Object, Object> results = new HashMap<Object, Object>();
		results = (Map<Object, Object>) this.getAllUsersByCriteria(searchCriteria,null, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				pageIndex, pageSize, sortBy, sortDirection,
				"", "", "", -1, false);
		return results;
	}

	public Map<Object, Object> findLearner1(String firstName, String lastName,
			String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize,
			String sortBy, int sortDirection) {
		Map<Object, Object> results = new HashMap<Object, Object>();
		results = (Map<Object, Object>) this.getAllUsersByCriteria("", null,
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				pageIndex, pageSize, sortBy, sortDirection,
				firstName, lastName, email, -1, false);
		return results;

	}

	/**
	 * code modified by Marium Saud eliminating  
	 * boolean notLmsRole, String searchCriteria and LMSRole are commented from this method as they are not used in search where clause
	 */

	public Map<Object, Object> findActiveLearners(String firstName,
			String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId,
			boolean accountNonExpired, boolean accountNonLocked,
			boolean enabled, int pageIndex, int pageSize, String sortBy,
			int sortDirection) {

		/** Marium Saud 
		 * boolean notLmsRole, String searchCriteria and LMSRole are commented from this method as they are not used in search where clause
		 
		boolean notLmsRole = false;
		String searchCriteria = "";
		LMSRole lmsRole = null;
		*/
 
		List<VU360User> userList = new ArrayList<VU360User>();
		Map<Object, Object> serachResult = new HashMap<Object, Object>();
		RepositorySpecificationsBuilder<VU360User> specificationBuilder = new RepositorySpecificationsBuilder<VU360User>();
		try {
			if (sortBy == "" || sortBy.equalsIgnoreCase(""))
				sortDirection = -1;
			Map<?, ?> result = getManagerRestrictionUserList(isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId);
			boolean isExpressionReturn = Boolean.parseBoolean(result.get(
					"isExpressionReturn").toString());
			if (!isExpressionReturn) {
				Object[] ids = (Object[]) ((HashSet) result.get("userIdArray"))
						.toArray();
				final int max_limit = 1000;
				int buckets = ids.length % max_limit;
				int bucketSize = max_limit;
				boolean loop = buckets > 0;
				int index = 0;
				Long[] idbucket = null;
				int recordSize = 0;
				boolean isPageSizeExceeded = false;
				while (loop) {
					if (index + max_limit > ids.length) {
						bucketSize = ids.length - index;// supposed to be last
														// bucket with the
														// content less than
														// max_limit
					} else {
						bucketSize = max_limit;
					}
					idbucket = new Long[bucketSize];
					System.arraycopy(ids, index, idbucket, 0, bucketSize);
					try {
						if (pageSize != -1 && pageIndex != -1) {
							specificationBuilder.with("id", specificationBuilder.IN, idbucket, "AND");
							specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE,firstName, "AND");
							specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE, lastName,"AND");
							specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, email,"AND");

							specificationBuilder.with("accountNonExpired", specificationBuilder.EQUALS_TO,accountNonExpired, "AND");
							specificationBuilder.with("accountNonLocked",specificationBuilder.EQUALS_TO,accountNonLocked, "AND");
							specificationBuilder.with("enabled", specificationBuilder.EQUALS_TO, enabled,"AND");

							Sort sortSpec = null;
							if (sortDirection != -1) {
								sortSpec = orderBy(sortDirection, sortBy);
							}

							PageRequest pageRequest = null;

								pageRequest = new PageRequest(pageIndex, pageSize,sortSpec);

								Page<VU360User> page = vu360UserRepository.findAll(specificationBuilder.build(), pageRequest);

								recordSize = recordSize	+ ((int) (long) page.getTotalElements());

								userList = page.getContent();
						}
						else {

							List<VU360User> users = vu360UserRepository.showAll(null, 
								isLMSAdministrator, trainingAdmin_isManagesAllOrganizationalGroups, customerId, userId, 
								"", firstName, lastName, email, idbucket, false, false, accountNonExpired, accountNonLocked, enabled, sortBy, sortDirection);

							recordSize = recordSize + users.size();

							for (VU360User user : users)
								userList.add(user);
						}
					} catch (ObjectRetrievalFailureException e) {
						if (log.isDebugEnabled()) {
							log.debug("learners:" + ids);
						}
					}
					index += bucketSize;
					if (index >= ids.length || isPageSizeExceeded) {
						loop = false;
					}
				}
				serachResult.put("recordSize", recordSize);
				serachResult.put("list", userList);
			} else {
				int recordSize = 0;
				if (pageSize != -1 && pageIndex != -1) {
					specificationBuilder = (RepositorySpecificationsBuilder<VU360User>) result.get("expression");
					specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE, firstName, "AND");
					specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE, lastName, "AND");
					specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, email, "AND");

					specificationBuilder.with("accountNonExpired", specificationBuilder.EQUALS_TO,accountNonExpired, "AND");
					specificationBuilder.with("accountNonLocked",specificationBuilder.EQUALS_TO,accountNonLocked, "AND");
					specificationBuilder.with("enabled", specificationBuilder.EQUALS_TO, enabled, "AND");

					Sort sortSpec = null;
					if (sortDirection != -1) {
						sortSpec = orderBy(sortDirection, sortBy);
					}

					PageRequest pageRequest = null;
					
						pageRequest = new PageRequest(pageIndex, pageSize, sortSpec);
						
						Page<VU360User> page = vu360UserRepository.findAll(specificationBuilder.build(), pageRequest);
						
						recordSize = ((int) (long)page.getTotalElements());

						userList = page.getContent();
 
				}
				else {

					List<VU360User> users = vu360UserRepository.showAll(null, 
							isLMSAdministrator, trainingAdmin_isManagesAllOrganizationalGroups, customerId, userId, 
							"", firstName, lastName, email, null, false, false, accountNonExpired, accountNonLocked, enabled, sortBy, sortDirection);

					recordSize = users.size();

					for (VU360User user : users)
						userList.add(user);
				}
				serachResult.put("recordSize", recordSize);
				serachResult.put("list", userList);
			}
		} catch (ObjectRetrievalFailureException e) {

		}
		return serachResult;
	}

	public Map<Object, Object> findAllLearnersOfCustomersOfReseller(
			String firstName, String lastName, String email, int pageIndex,
			int pageSize, String sortBy, int sortDirection,
			String enrollmentMethod) {

		Map<Object, Object> results = new HashMap<Object, Object>();
		RepositorySpecificationsBuilder<VU360User> specificationBuilder = new RepositorySpecificationsBuilder<VU360User>();

		Long distributorId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentDistributor().getId();

		// Get all learners of the current Distributor's customers.
		specificationBuilder.with("learner_customer_distributor_id", specificationBuilder.JOIN_EQUALS, distributorId, "AND");
		
		if (enrollmentMethod != null) {
			if (enrollmentMethod
					.equals(AssignSurveyController.SURVEY_METHOD_LEARNER)) {

				specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE, firstName, "AND");
				specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE, lastName, "AND");
				specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, email, "AND");
			}
		}

		Sort sortSpec = null;
		if (sortDirection != -1) {
			sortSpec = orderBy(sortDirection, sortBy);
		}

		List<VU360User> resultList1 = new ArrayList<VU360User>();
		int recordSize = 0;
		PageRequest pageRequest = null;

		if (pageSize > 0) {
			pageRequest = new PageRequest(pageIndex, pageSize, sortSpec);

			Page<VU360User> page = vu360UserRepository.findAll(
					specificationBuilder.build(), pageRequest);

			recordSize = ((int) (long)page.getTotalElements());

			resultList1 = page.getContent();

		} else {

			List<VU360User> users = vu360UserRepository.findAll(
					specificationBuilder.build(), sortSpec);

			recordSize = users.size();

			for (VU360User user : users)
				resultList1.add(user);
		}

		log.debug("finalExpr");
		log.debug("ffffffffffffff " + resultList1.size());
		results = new HashMap<Object, Object>();
		results.put("recordSize", recordSize);
		results.put("list", resultList1);

		return results;
	}

	public List<Learner> getLearnersOfCustomersOfSelectedReseller(
			Distributor distributor) {
		List<Learner> learners = new ArrayList<Learner>();
		learners = learnerRepository.findByCustomer_Distributor_Id(distributor
				.getId());
		return learners;
	}

	public Learner loadForUpdateLearner(Long id) {
		Learner learner = null;
		try {
			learner = learnerRepository.findOne(id);
		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("user with id: " + id + "is not found");
			}
		}
		return learner;
	}

	public Learner getLearnerByID(long id) {
		return learnerRepository.findOne(id);
	}

	public List<Learner> getLearnersByIDs(Long[] ids) {
		return this.getLearnersByIds(ids);
	}

	public RegistrationInvitation loadForUpdateRegistrationInvitation(long id) {
		return registrationInvitationRepository.findOne(id);
	}

	public OrganizationalGroup loadForUpdateOrganizationalGroup(long id) {
		OrganizationalGroup org = null;
		try {
			org = organizationalGrpRepository.findOne(id);
		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("org group with id: " + id + "is not found");
			}
		}
		return org;
	}

	public LearnerGroup loadForUpdateLearnerGroup(long id) {
		return learnerGroupRepository.findOne(id);
	}

	public LMSRole loadForUpdateLMSRole(long id) {
		LMSRole lmsRole = null;
		try {

			lmsRole = lmsRoleRepository.findOne(id);

		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("role with id: " + id + "is not found");
			}
		}
		return lmsRole;
	}

	public CreditReportingFieldValue loadForUpdateCreditReportingFieldValue(
			long id) {
		return crfValueRepository.findOne(id);
	}


	public VU360User updateUser(VU360User user) {
		return vu360UserRepository.updateUserWithLoad(user);
	}

	public VU360User updateUserFromBatchFile(VU360User updatedUser) {
		String newPassword = "", encodedPassword = "";
		if (updatedUser.isPassWordChanged()) {
			newPassword = updatedUser.getPassword(); // Update in the Active Directory 
			//com.softech.vu360.lms.vo.VU360User voUser = ProxyVOHelper.setUserProxy(updatedUser);
			//Object salt = saltSource.getSalt(voUser); // salt is the guid
			//encodedPassword = passwordEncoder.encodePassword(newPassword, salt);
			
			
			com.softech.vu360.lms.vo.VU360User vu360UserVO = new com.softech.vu360.lms.vo.VU360User();
			vu360UserVO.setUserGUID(updatedUser.getUserGUID());
			vu360UserVO.setId(updatedUser.getId());
			vu360UserVO.setPassword(updatedUser.getPassword());
			
			
			encodedPassword = LoginSecurityHelper.getEncryptedPassword(vu360UserVO);
			
			updatedUser.setPassword(encodedPassword);
		}

		VU360User updatedUserForAD = vu360UserRepository.save(updatedUser);

		if (updatedUserForAD != null
				&& activeDirectoryService.isADIntegrationEnabled()) {// if
																		// success
																		// and
																		// AD is
																		// enabled
			updatedUser.setPassword(newPassword);// we require plain password in
													// AD
			activeDirectoryService.updateUser(updatedUser);// edit user to AD
			updatedUser.setPassword(encodedPassword);// reset the password to
														// encrypted
		}

		return updatedUserForAD;
	}

	@Transactional
	public Learner saveLearner(Learner learner) {
		VU360User updatedUser = learner.getVu360User();
		if (updatedUser.isPassWordChanged()) {
			String newPassword = updatedUser.getPassword();
//			/com.softech.vu360.lms.vo.VU360User voUser = ProxyVOHelper.setUserProxy(updatedUser);
			//Object salt = saltSource.getSalt(voUser); // salt is the guid
			//String encodedPassword = passwordEncoder.encodePassword(newPassword, salt);
			com.softech.vu360.lms.vo.VU360User vu360UserVO = new com.softech.vu360.lms.vo.VU360User();
			vu360UserVO.setUserGUID(updatedUser.getUserGUID());
			vu360UserVO.setId(updatedUser.getId());
			vu360UserVO.setPassword(updatedUser.getPassword());
			
			
			String encodedPassword = LoginSecurityHelper.getEncryptedPassword(vu360UserVO);
			updatedUser.setPassword(encodedPassword);
		}
		return learnerRepository.save(learner);
	}

	// used to update the profile
	@Transactional
	@Override
	public VU360User saveUser(VU360User userToSave) {
		String newPassword = "";
		String encodedPassword = "";
		boolean isNameChanged = false;
		boolean isEmailChanged = false;
		VU360User dbUser = vu360UserRepository.findOne(userToSave.getId());
		dbUser.shallowCopy(userToSave);

		LearnerProfile profile=dbUser.getLearner().getLearnerProfile();
		profile.setLearnerAddress(userToSave.getLearner().getLearnerProfile().getLearnerAddress());
		profile.setLearnerAddress2(userToSave.getLearner().getLearnerProfile().getLearnerAddress2());
		profile.setMobilePhone(userToSave.getLearner().getLearnerProfile().getMobilePhone());
		profile.setOfficePhone(userToSave.getLearner().getLearnerProfile().getOfficePhone());
		profile.setOfficePhoneExtn(userToSave.getLearner().getLearnerProfile().getOfficePhoneExtn());
		profile.setTimeZone(userToSave.getLearner().getLearnerProfile().getTimeZone());
		
		if (userToSave.isPassWordChanged()) {
			// required since we need to get the old guid...assuming the guid wont change
			newPassword = userToSave.getPassword();
			com.softech.vu360.lms.vo.VU360User voUser = ProxyVOHelper.setUserProxy(userToSave);
			Object salt = saltSource.getSalt(voUser); // salt is the guid
			encodedPassword = passwordEncoder.encodePassword(newPassword, salt);
			dbUser.setPassword(encodedPassword);
		}

		isEmailChanged = !userToSave.getEmailAddress().equalsIgnoreCase(dbUser.getEmailAddress());
		// if name (first name OR last name) is changed. Then only, audit the change
		if (!userToSave.getLastName().equalsIgnoreCase(dbUser.getLastName())
				|| !userToSave.getFirstName().equalsIgnoreCase(dbUser.getFirstName())) {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long loginUserID = loggedInUser.getId();
			vu360UserRepository.saveVU360UserAudit(dbUser, "update",loginUserID);
			isNameChanged = true;
		}

		profile=learnerProfileRepository.save(profile);
		dbUser= profile.getLearner().getVu360User();

		if (dbUser != null && activeDirectoryService.isADIntegrationEnabled() && // if success and AD is enabled
				(userToSave.isPassWordChanged() || isNameChanged || isEmailChanged)) { // AND if any of the AD field is changed
			userToSave.setPassword(newPassword);// we require plain password in AD
			activeDirectoryService.updateUser(userToSave);// edit user to AD
			userToSave.setPassword(encodedPassword);// reset the password to encrypted
		}
		return dbUser;
	}

	/*
	 * Overload method that will be used to add learner along default learner
	 * preferences If we need to set customer preference as template for learner
	 * preference then here is the method that will rescue you
	 */
	public Learner addLearner(Learner newLearner) {
		Learner savedLearner = addLearner(null, null, null, newLearner);
		return savedLearner;
	}

	public Learner addLearner(Customer cust,
			List<OrganizationalGroup> orgGroups,
			List<LearnerGroup> learnerGroups, Learner newLearner) {
		Learner savedLearner = addLearner(true, cust, orgGroups, learnerGroups,newLearner);
		return savedLearner;
	}

	@Transactional
	public Learner addLearner(boolean shouldAddUserInAD, Customer cust,
			List<OrganizationalGroup> orgGroups,
			List<LearnerGroup> learnerGroups, Learner newLearner) {

		// setup new learner and persist to database
		VU360User user = newLearner.getVu360User();
		String plainPwd = user.getPassword();
		//com.softech.vu360.lms.vo.VU360User voUser = ProxyVOHelper.setUserProxy(user);
		com.softech.vu360.lms.vo.VU360User voUser = new com.softech.vu360.lms.vo.VU360User();
		voUser.setId(user.getId());
		voUser.setPassword(user.getPassword());
		voUser.setUserGUID(user.getUserGUID());
		
		/*Object salt = saltSource.getSalt(voUser); // salt is the guid
		String encodedPassword = passwordEncoder.encodePassword(plainPwd, salt);*/
		
		String encodedPassword = LoginSecurityHelper.getEncryptedPassword(voUser);
		
		user.setPassword(encodedPassword);
		LearnerProfile profile= newLearner.getLearnerProfile();
		profile=learnerProfileRepository.save(profile);

		newLearner=profile.getLearner();
		user=newLearner.getVu360User();
		
		// associated as member to org groups, persist to database
		if (orgGroups != null) {
			for (OrganizationalGroup og : orgGroups) {
				OrganizationalGroupMember ogm = new OrganizationalGroupMember();
				ogm.setLearner(newLearner);
				ogm.setOrganizationalGroup(og);
				organizationalGroupMemberRepository.saveOGM(ogm);
			}
		}
		// associate as member to learner groups, persist to database
		if (learnerGroups != null) {
			for (LearnerGroup lg : learnerGroups) {
				LearnerGroupMember lgm=new LearnerGroupMember();
				lgm.setLearner(newLearner);
				lgm.setLearnerGroup(lg);
				learnerGroupMemberRepository.saveLGM(lgm);
			}
		}

		LearnerPreferences preferences = new LearnerPreferences();
		if (newLearner.getCustomer().getCustomerPreferences() != null) {
			preferences.updateValuesByCustomerPreferences(newLearner.getCustomer().getCustomerPreferences());
		}
		preferences.setLearner(newLearner);
		preferences = learnerPreferenceRepository.save(preferences);

		boolean dashboardFeature = lmsRoleLMSFeatureRepository.isAllowedLearnerDashboard(newLearner.getVu360User().getId());
		
		/*for (LMSRole role : newLearner.getVu360User().getLmsRoles()) {
			List<LMSRoleLMSFeature> permissions = (List<LMSRoleLMSFeature>) lmsRoleLMSFeatureRepository.findByLmsRoleId(role.getId());
			for (LMSRoleLMSFeature feature : permissions) {
				if (feature.getLmsFeature().getFeatureCode().equals("LMS-LRN-0007")) {
					dashboardFeature = true;
					break;
				}
			}
		}*/

		if (dashboardFeature) {
			Widget widget = widgetRepository.findByTitle("Courses");
			if (widget != null) {
				userWidgetService.addUserWidget(user, widget, 1);
			}
		}

		if (user != null && activeDirectoryService.isADIntegrationEnabled()) {
			// if success and AD is enabled
			user.setPassword(plainPwd);// we require plain password in AD
			activeDirectoryService.addUser(user);// add user to AD
			user.setPassword(encodedPassword);// reset the password to encrypted
		}

		return newLearner;
	}

	@Transactional
	public RegistrationInvitation saveRegistrationInvitation(
			RegistrationInvitation regInvitation) {
		return registrationInvitationRepository.save(regInvitation);
	}

	/**
	 * stored procedure is used to add registration invitation
	 */
	public Object[] addRegistrationInvitation(
			RegistrationInvitation regInvitation) {
		return registrationInvitationRepository.addRegistrationInvitation(
				regInvitation.getId(), regInvitation.getRegistrationUtilized());
	}

	@Transactional
	public void deleteRegistrationInvitations(long regIdArray[]) {

		RegistrationInvitation registrationInvitation;
		for (int i = 0; i < regIdArray.length; ++i) {
			registrationInvitation = registrationInvitationRepository
					.findOne(regIdArray[i]);
			registrationInvitationRepository.delete(registrationInvitation);
		}
	}

	public RegistrationInvitation getRegistrationInvitationByID(long id) {
		return registrationInvitationRepository.findOne(id);
	}

	

	public OrganizationalGroup getOrganizationalGroupById(Long id) {
		OrganizationalGroup org = null;
		try {
			org = organizationalGrpRepository.findOne(id);
		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("org group with id: " + id + "is not found");
			}
		}
		return org;
	}

	public LearnerGroup getLearnerGroupByName(String name, Customer customer) {
		return learnerGroupRepository.findFirstByNameIgnoreCaseAndCustomerId(name, customer.getId());
	}


	public LearnerGroup getLearnerGroupById(Long id) {
		LearnerGroup learnerGroup = null;
		try {
			learnerGroup = learnerGroupRepository.findOne(id);
		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("user with id: " + id + "is not found");
			}
		}
		return learnerGroup;
	}
	

	public List<Learner> addLearnersInOrgGroup(String[] selectedLearners,
			Long orgGroupId) {
		Long[] selectedLearnerIds = new Long[selectedLearners.length];
		for (int i = 0; i < selectedLearners.length; i++) {
			selectedLearnerIds[i] = Long.valueOf(selectedLearners[i]);
		}
		List<Learner> learners = this
				.getLearnersByIds(selectedLearnerIds);
		OrganizationalGroup orgGroup = organizationalGrpRepository.findOne(orgGroupId);
		return this.addLearnersInOrgGroup(learners, orgGroup);
	}
	

	public List<Learner> addLearnersInOrgGroup(
			List<Learner> listSelectedLearner, OrganizationalGroup orgGroup) {
		Long[] learnersArray = new Long[listSelectedLearner.size()];
		int counter = 0;
		for (Learner l : listSelectedLearner) {
			learnersArray[counter] = l.getId();
			counter += 1;
		}
		List<OrganizationalGroupMember> existingMemberships = organizationalGroupMemberRepository.findByOrganizationalGroupIdLearnerIdIn
																											(orgGroup.getId(), learnersArray);
		if (CollectionUtils.isNotEmpty(existingMemberships)) {
			for (OrganizationalGroupMember member : existingMemberships) {
				if (listSelectedLearner.contains(member.getLearner())) {
					log.warn("duplicate membership found, fail safe logic executing:"
							+ member.getLearner().getVu360User().getUsername());
					listSelectedLearner.remove(member.getLearner());
				}
			}
		}
		OrganizationalGroupMember temp = null;
		List<Learner> savedSuccessfullly = new ArrayList<Learner>();
		for (Learner learner : listSelectedLearner) {
			temp = new OrganizationalGroupMember();
			temp.setLearner(learner);
			temp.setOrganizationalGroup(orgGroup);
			savedSuccessfullly.add(organizationalGroupMemberRepository.saveOGM(temp).getLearner());
		}
		return savedSuccessfullly;
	}

	@Transactional
	public void deleteOrgGroup(List<Learner> listSelectedLearner,
			OrganizationalGroup OrgGroup) {
		organizationalGrpRepository.deleteById(OrgGroup.getId());
	}


	public void addLearnersInLearnerGroup(List<Learner> listSelectedLearner,
			LearnerGroup learnerGroup) {

		Long[] learnersArray = new Long[listSelectedLearner.size()];
		int counter = 0;
		for (Learner l : listSelectedLearner) {
			learnersArray[counter] = l.getId();
			counter+=1;
		}
		List<LearnerGroupMember> existingMemberships = learnerGroupMemberRepository
				.findByLearnerGroupIdAndLearnerIdIn(learnerGroup.getId(), learnersArray);
		if (CollectionUtils.isNotEmpty(existingMemberships)) {
			for (LearnerGroupMember member : existingMemberships) {
				if (listSelectedLearner.contains(member.getLearner())) {
					log.warn("duplicate UG membership found, fail safe logic executing:"+member.getLearner().getVu360User().getUsername());
					listSelectedLearner.remove(member.getLearner());
				}
			}
		}
		List<OrganizationalGroupMember> existingOrgGroupMembers =
	            organizationalGroupMemberRepository.findByOrganizationalGroupIdLearnerIdIn(learnerGroup.getOrganizationalGroup().getId(), learnersArray);
	        if (CollectionUtils.isNotEmpty(existingOrgGroupMembers)) {
	          for (OrganizationalGroupMember member : existingOrgGroupMembers) {
	            if (listSelectedLearner.contains(member.getLearner())) {
	              log.warn("duplicate OG membership found, fail safe logic executing:"
	                  + member.getLearner().getVu360User().getUsername());
	              listSelectedLearner.remove(member.getLearner());
	            }
	          }
	        }
			
		LearnerGroupMember learnerGroupMember = null;
		for (Learner learner : listSelectedLearner) {
			log.debug("... addLearnersInLearnerGroup start *3 learner.getId()"+ learner.getId()); 
			learnerGroupMember = new LearnerGroupMember();
			learnerGroupMember.setLearner(learner);
			learnerGroupMember.setLearnerGroup(learnerGroup);
			learnerGroupMemberRepository.saveLGM(learnerGroupMember).getLearner();
			log.debug("...addLearnersInLearnerGroup end *3");
		}
		
		OrganizationalGroupMember orgGroupMember = null;
        for (Learner learner : listSelectedLearner) {
          log.debug("... addLearnersInOrgGroup start *4 learner.getVu360User().getId()"
              + learner.getVu360User().getId());
          orgGroupMember = new OrganizationalGroupMember();
          orgGroupMember.setLearner(learner);
          orgGroupMember.setOrganizationalGroup(learnerGroup.getOrganizationalGroup());
          organizationalGroupMemberRepository.saveOGM(orgGroupMember).getLearner();
          log.debug("...addLearnersInOrgGroup end *4");
        }
    }
	

	public void addLearnerInLearnerGroup(Learner learner,
			LearnerGroup learnerGroup) {
		if (learnerGroup != null) {
			LearnerGroupMember member = new LearnerGroupMember();
	    	member.setLearner(learner);
	    	member.setLearnerGroup(learnerGroup);
	    	learnerGroupMemberRepository.save(member);
		}
	}
	

	public void deleteLearnerFromLearnerGroup(Learner learner,
			LearnerGroup learnerGroup) {
		List<LearnerGroupMember> lstLGM = learnerGroupMemberRepository.findByLearnerGroupIdAndLearnerIdIn(learnerGroup.getId(), 
																										new Long[] { learner.getId() });
		for(LearnerGroupMember LGM : lstLGM)
			learnerGroupMemberRepository.delete(LGM);
	}
	

	public LMSRole getLMSRoleById(Long id) {
		LMSRole lmsRole = null;

		try {
			lmsRole = lmsRoleRepository.findOne(id);
		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("role with id: " + id + "is not found");
			}
		}
		return lmsRole;
	}

	public List<Learner> getMembersByRole(LMSRole lmsRole) {
		List<Learner> learners = null;
		try {

			learners = learnerRepository.findByVu360User_LmsRoles_Id(lmsRole
					.getId());

		} catch (ObjectRetrievalFailureException e) {
			log.debug("Exception " + e);
			e.printStackTrace();
		}
		return learners;
	}
	
	public Long countMembersByRole(LMSRole lmsRole) {
			return learnerRepository.countByVu360User_LmsRoles_Id(lmsRole.getId());
	}
	
	public Map<String, String> countLearnerByRoles(Long [] trainingPlanIds){
		return lmsRoleRepository.countLearnerByRoles(trainingPlanIds);
	}
	
	public void InactiveUsers(long userIdArray[]) {

		for (int userId = 0; userId < userIdArray.length; userId++) {
			VU360User user = vu360UserRepository
					.findOne(userIdArray[userId]);
			user.setAccountNonLocked(false);
			vu360UserRepository.save(user);
			// TODO given below code written in 4.5.2 branch
			// userDAO.saveUser(user);
		}

	}
	
	
	// Added by partha
	@Transactional
	public LMSRole addRole(LMSRole role, Customer customer) {

		if (role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {

			if (role.isDefaultForRegistration()) {
				
				LMSRole defaultRole = lmsRoleRepository.getDefaultRole(customer);
				
				if (defaultRole != null) {
					defaultRole.setDefaultForRegistration(Boolean.FALSE);
					lmsRoleRepository.save(defaultRole);
				}
			}
		}
		return lmsRoleRepository.save(role);

	}
	

	public LMSRole getDefaultRoleForUser(Customer customer) {
		LMSRole role = null;
		role = lmsRoleRepository.getDefaultRole(customer);
		if (role == null) {
			role = new LMSRole();
			role.setOwner(customer);
			role.setRoleName("LEARNER");
			role.setDefaultForRegistration(true);
			role.setSystemCreated(true);
			role.setRoleType(LMSRole.ROLE_LEARNER);
			List<LMSRoleLMSFeature> lmsPermissions = customerService
					.getLMSPermissions(customer.getDistributor(),
							LMSRole.ROLE_LEARNER, role);
			role.setLmsPermissions(lmsPermissions);
			lmsRoleRepository.save(role);
		}
		return role;
	}

	@Transactional
	public void assignUserToRole(long userIdArray[], LMSRole lmsRole) {

		for (int i = 0; i < userIdArray.length; i++) {
			VU360User user = vu360UserRepository
					.findOne(userIdArray[i]);
			assignUserToRole(user, lmsRole);
			vu360UserRepository.saveUser(user);
		}
	}

	public void assignUserToRole(VU360User user, LMSRole lmsRole) {
		if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER))
			user.addLmsRole(lmsRole);
		else if (lmsRole.getRoleType().equalsIgnoreCase(
				LMSRole.ROLE_TRAININGMANAGER)) {
			if (!user.isTrainingAdministrator()) {
				TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
				trainingAdministrator.setCustomer(user.getLearner()
						.getCustomer());
				trainingAdministrator.setVu360User(user);
				user.setTrainingAdministrator(trainingAdministrator);
			}
			user.addLmsRole(lmsRole);
		} else if (lmsRole.getRoleType().equalsIgnoreCase(
				LMSRole.ROLE_LMSADMINISTRATOR)) {
			if (!user.isLMSAdministrator()) {
				LMSAdministrator lmsAdministrator = new LMSAdministrator();
				lmsAdministrator.setVu360User(user);
				user.setLmsAdministrator(lmsAdministrator);
			}
			user.addLmsRole(lmsRole);
		} else if (lmsRole.getRoleType().equalsIgnoreCase(
				LMSRole.ROLE_REGULATORYANALYST)) {
			RegulatoryAnalyst regulatoryAnalyst = new RegulatoryAnalyst();
			regulatoryAnalyst.setUser(user);
			user.setRegulatoryAnalyst(regulatoryAnalyst);
			regulatoryAnalyst.setForAllContentOwner(true);
			user.addLmsRole(lmsRole);
		} else if (lmsRole.getRoleType().equalsIgnoreCase(
				LMSRole.ROLE_INSTRUCTOR)) {
			Instructor instructor = new Instructor();
			instructor.setUser(user);
			user.setInstructor(instructor);
			user.addLmsRole(lmsRole);
		} else if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR))
			user.addLmsRole(lmsRole);

	}

	/**
	 * duplicate method having manageAll & org group list as extra parameters.
	 */
	@Transactional
	public void assignUserToRole(long userIdArray[], LMSRole lmsRole,
			String manageAll, List<OrganizationalGroup> orgGroupsList) {

		boolean mngAll = false;

		if (manageAll.equalsIgnoreCase("true")) {
			mngAll = true;
		}
		for (int i = 0; i < userIdArray.length; i++) {
			VU360User user = vu360UserRepository
					.findOne(userIdArray[i]);
			if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {

				user.addLmsRole(lmsRole);

			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_TRAININGMANAGER)) {

				user.addLmsRole(lmsRole);
				/*
				 * TODO should be fixed in future.
				 */
				user = vu360UserRepository.saveUser(user);
				if (!user.isTrainingAdministrator()) {

					TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
					trainingAdministrator.setCustomer(user.getLearner()
							.getCustomer());
					trainingAdministrator
							.setManagesAllOrganizationalGroups(mngAll);
					if (!mngAll) {
						trainingAdministrator.setManagedGroups(orgGroupsList);
					}
					trainingAdministrator.setVu360User(user);
					user.setTrainingAdministrator(trainingAdministrator);
				} else {
					if (!mngAll) {
						user.getTrainingAdministrator().setManagedGroups(
								orgGroupsList);
						user.getTrainingAdministrator()
								.setManagesAllOrganizationalGroups(false);
					} else {
						user.getTrainingAdministrator()
								.setManagesAllOrganizationalGroups(true);
					}
				}

			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_LMSADMINISTRATOR)) {

				if (!user.isLMSAdministrator()) {
					// No need to create manager in time of create administrator
					LMSAdministrator lmsAdministrator = new LMSAdministrator();
					lmsAdministrator.setVu360User(user);
					user.setLmsAdministrator(lmsAdministrator);
					// lmsRole.setLmsPermissions(uow.registerAllObjects(lmsRole.getLmsPermissions()));
				}
				user.addLmsRole(lmsRole);
			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_REGULATORYANALYST)) {
				RegulatoryAnalyst regulatoryAnalyst = new RegulatoryAnalyst();
				regulatoryAnalyst.setUser(user);
				user.setRegulatoryAnalyst(regulatoryAnalyst);
				// TODO: need to change when there is a option for global
				// content owner
				regulatoryAnalyst.setForAllContentOwner(true);
				user.addLmsRole(lmsRole);
			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_INSTRUCTOR)) {
				ContentOwner coCustomer = user.getLearner().getCustomer()
						.getContentOwner();

				Instructor instructor = new Instructor();
				instructor.setUser(user);
				instructor.setContentOwner(coCustomer);
				user.setInstructor(instructor);
				user.addLmsRole(lmsRole);
			}

			vu360UserRepository.saveUser(user);

		}
	}

	@Transactional
	public void assignUserToRole(Map<Long, Long> userIdContentOwnerMap,
			LMSRole lmsRole) {
		// public void assignUserToRole(long userIdArray[], LMSRole lmsRole){
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Object[] userIdArray = userIdContentOwnerMap.keySet().toArray();
		for (int i = 0; i < userIdArray.length; i++) {

			VU360User user = vu360UserRepository
					.findOne(((Long) userIdArray[i]).longValue());
			if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {

				user.addLmsRole(lmsRole);

			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_TRAININGMANAGER)) {

				if (!user.isTrainingAdministrator()) {

					TrainingAdministrator trainingAdministrator = new TrainingAdministrator();
					trainingAdministrator.setCustomer(user.getLearner()
							.getCustomer());
					trainingAdministrator.setVu360User(user);
					user.setTrainingAdministrator(trainingAdministrator);
				}
				user.addLmsRole(lmsRole);

			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_LMSADMINISTRATOR)) {

				if (!user.isLMSAdministrator()) {

					/*
					 * LMS-4266,LMS-4469 manager role is not supposed to be
					 * assigned along Admin type role. both are separate roles
					 * if(!user.isTrainingAdministrator()){
					 * 
					 * TrainingAdministrator trainingAdministrator = new
					 * TrainingAdministrator();
					 * trainingAdministrator.setCustomer
					 * (user.getLearner().getCustomer());
					 * user.setTrainingAdministrator(trainingAdministrator);
					 * List<LMSRole> systemRoles =
					 * userDAO.getSystemRolesByCustomer
					 * (user.getLearner().getCustomer()); for( LMSRole role :
					 * systemRoles ) {
					 * 
					 * if(role.getRoleType().equalsIgnoreCase(LMSRole.
					 * ROLE_TRAININGMANAGER)) { user.getLmsRoles().add(role);
					 * break; } } }
					 */
					LMSAdministrator lmsAdministrator = user.getLmsAdministrator();
					if(lmsAdministrator==null || (lmsAdministrator!=null && lmsAdministrator.getId()==null)){
						lmsAdministrator = new LMSAdministrator();
					}
					lmsAdministrator.setVu360User(user);
					user.setLmsAdministrator(lmsAdministrator);
				}
				user.addLmsRole(lmsRole);
			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_REGULATORYANALYST)) {
				
				RegulatoryAnalyst regulatoryAnalyst = user.getRegulatoryAnalyst();
				if(regulatoryAnalyst==null || (regulatoryAnalyst!=null && regulatoryAnalyst.getId()==null)){
					regulatoryAnalyst = new RegulatoryAnalyst();	
					regulatoryAnalyst.setUser(user);
					user.setRegulatoryAnalyst(regulatoryAnalyst);
				}
				ContentOwner cntOwner = authorService
						.addContentOwnerIfNotExist(user.getLearner()
								.getCustomer(), loggedInUser.getId());

				Customer newCustomer = user.getLearner().getCustomer();
				newCustomer = this.getCustomerService().loadForUpdateCustomer(
						newCustomer.getId());
				newCustomer.setContentOwner(cntOwner);
				this.getCustomerService().updateCustomer(newCustomer);

				// TODO: need to change when there is a option for global
				// content owner
				// regulatoryAnalyst.setForAllContentOwner(true);
				regulatoryAnalyst.getContentOwners().add(cntOwner);
				user.addLmsRole(lmsRole);
			} else if (lmsRole.getRoleType().equalsIgnoreCase(
					LMSRole.ROLE_INSTRUCTOR)) {
				log.debug("adding roles if ");
				user.addLmsRole(lmsRole);
				if (user.getInstructor() == null) {
					Instructor instructor = new Instructor();
					ContentOwner cntOwner = authorService
							.addContentOwnerIfNotExist(user.getLearner()
									.getCustomer(), loggedInUser.getId());
					Customer newCustomer = user.getLearner().getCustomer();
					newCustomer = this.getCustomerService()
							.loadForUpdateCustomer(newCustomer.getId());
					newCustomer.setContentOwner(cntOwner);
					this.getCustomerService().updateCustomer(newCustomer);

					// ContentOwner cntOwner =
					// authorService;//learnerDAO.getContentOwnerByID(userIdContentOwnerMap.get(userIdArray[i]).longValue());
					instructor.setContentOwner(cntOwner);
					instructor.setUser(user);
					instructor.setInstructorInfo(user); /*
														 * setting default
														 * values of newly
														 * created instructor
														 */
					user.setInstructor(instructor);
				}
			}

			vu360UserRepository.saveUser(user);
		}
	}

	@Transactional
	public void unAssignUsersFromAllRolesOfType(String userIdArray[],
			String roleType) {
		for (int i = 0; i < userIdArray.length; i++) {
			VU360User user = vu360UserRepository.findOne(Long
					.valueOf(userIdArray[i]));
			ArrayList<LMSRole> rolesToRemove = new ArrayList<LMSRole>();
			for (LMSRole role : user.getLmsRoles()) {
				if (role.getRoleType().equalsIgnoreCase(roleType)) {
					rolesToRemove.add(role);
				}
			}
			user.getLmsRoles().removeAll(rolesToRemove);
			if(roleType.equals(LMSRole.ROLE_TRAININGMANAGER) && user.getTrainingAdministrator()!=null && user.getTrainingAdministrator().getId()!=null){
				trainingAdminstratorRepository.delete(user.getTrainingAdministrator().getId());
				user.setTrainingAdministrator(null);
			}
			vu360UserRepository.saveUser(user);
		}
		
	}
	
	@Transactional
	public void unAssignUserFromRole(long userIdArray[], LMSRole lmsRole) {

		for (int i = 0; i < userIdArray.length; i++) {
			VU360User user = vu360UserRepository
					.findOne(userIdArray[i]);
			int count = 0;
			for (LMSRole role : user.getLmsRoles()) {
				if (role.getId().longValue() == lmsRole.getId().longValue()) {
					user.getLmsRoles().remove(role);
					break;
				}
				count = count + 1;
			}

			vu360UserRepository.saveUser(user);

		}

		// learnerDAO.unAssignUserFromRole(userIdArray,lmsRole);
	}

	/**
	 * Added by Saptarshi
	 */

	public LearnerGroup saveLearnerGroup(LearnerGroup learnerGroup) {
		return learnerGroupRepository.save(learnerGroup);

	}

	public LearnerGroup saveLearnerGroup2(LearnerGroup learnerGroup) {
		return learnerGroupRepository.save(learnerGroup);
	}

	@Transactional
	public LMSRole updateRole(LMSRole role, Customer customer) {

		if (role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {

			LMSRole defaultRole = lmsRoleRepository.getDefaultRole(customer);
			if (role.isDefaultForRegistration() && defaultRole!=null && (role.getId().longValue()!=defaultRole.getId().longValue())) {
				defaultRole.setDefaultForRegistration(Boolean.FALSE);
				lmsRoleRepository.save(defaultRole);
			}
		}

		return lmsRoleRepository.save(role);

	}

	public Map<Object, Object> getAllUsersInLmsRole(LMSRole lmsRole,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection) {

		Map<Object, Object> results = new HashMap<Object, Object>();
		results = (Map<Object, Object>) this.getAllUsersByCriteria("", lmsRole,
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				-1, -1, sortBy, sortDirection, "", "", "", -1,
				false);
		return results;

	}

	public Map<Object, Object> getAllUsersInLmsRole(LMSRole lmsRole,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize, String sortBy,
			int sortDirection) {
		isProctorRole = true;
		Map<Object, Object> searchResult = (Map<Object, Object>) this.getAllUsersByCriteria("", lmsRole, 
				isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId, 
				pageIndex, pageSize, sortBy,
				sortDirection, "", "", "", -1, false);
		isProctorRole = false;
		return searchResult;
	}

	// ===========================================================//
	// ===========Added By Marium Saud ==========================//
	// ========= This Function has been moved from ===============//
	// ========= TopLinkLearnerDAOImpl to Service ===============//

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getAllUsersByCriteria(String searchCriteria, LMSRole lmsRole, boolean isLMSAdministrator, 
			boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, int pageIndex,
			int pageSize, String sortBy, int sortDirection, String firstName,	String lastName, String email, int limit, boolean notLmsRole) {
		
		List<VU360User> userList = new ArrayList<VU360User>();
		Map<Object, Object> serachResult = new HashMap<Object, Object>();
		try {
			RepositorySpecificationsBuilder<VU360User> specificationBuilder = new RepositorySpecificationsBuilder<VU360User>();	
			if (sortBy == "" || sortBy.equalsIgnoreCase(""))
				sortDirection = -1;
			Map<?, ?> result = getManagerRestrictionUserList(isLMSAdministrator, isTrainingAdministrator, trainingAdministratorId, trainingAdmin_isManagesAllOrganizationalGroups, managedGroups, customerId, userId);
			boolean isExpressionReturn = Boolean.parseBoolean(result.get(
					"isExpressionReturn").toString());
			if (!isExpressionReturn) {
				Object[] ids = (Object[]) ((HashSet) result.get("userIdArray"))
						.toArray();
				final int max_limit = 1000;
				int buckets = ids.length % max_limit;
				int bucketSize = max_limit;
				boolean loop = buckets > 0;
				int index = 0;
				Long[] idbucket = null;
				int recordSize = 0;
				boolean isPageSizeExceeded = false;
				while (loop) {
					if (index + max_limit > ids.length) {
						bucketSize = ids.length - index;// supposed to be last bucket with the content less than max_limit
					} else {
						bucketSize = max_limit;
					}
					idbucket = new Long[bucketSize];
					System.arraycopy(ids, index, idbucket, 0, bucketSize);
					try {
						
						if(pageSize != -1 && pageIndex != -1){
						
							specificationBuilder.with("id", specificationBuilder.IN, idbucket, "AND");
							if (lmsRole != null && !notLmsRole) {
								specificationBuilder.with("lmsRoles", specificationBuilder.JOIN_EQUALS, lmsRole.getId(), "AND");
							}
							if (lmsRole != null && notLmsRole) {
								specificationBuilder.with("lmsRoles", specificationBuilder.JOIN_NOT_EQUALS,lmsRole.getId(), "AND");
							}
							if (isProctorRole && lmsRole != null && lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)) {
								specificationBuilder.with("proctor_status", specificationBuilder.JOIN_EQUALS, ProctorStatusEnum.Active.toString(), "AND");
							}
							specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE,firstName, "AND");
							specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE, lastName,	"AND");
							specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, email,	"AND");

							specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE, searchCriteria, "OR");
							specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE,  searchCriteria, "OR");
							specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, searchCriteria, "OR");

							Sort sortSpec = null;
							if (sortDirection != -1) {
								sortSpec = orderBy(sortDirection, sortBy);
							}

							PageRequest pageRequest = null;
							pageRequest = new PageRequest(pageIndex, pageSize,sortSpec);
							Page<VU360User> page = vu360UserRepository.findAll(specificationBuilder.build(), pageRequest);
							recordSize = recordSize	+ ((int) (long)page.getTotalElements());
							userList = page.getContent();
							 
						}
						else {
							//Inorder to optimize performance for Search All Users feature several soluitions are proposed via JPQL, Secondary Tables(Entities) and Native Query 
							// The adopted solution i-e Native Query has been accepted which minimizes the search response time from 3 min to 1 sec. 
							List<VU360User> users = vu360UserRepository.showAll(lmsRole, isLMSAdministrator, trainingAdmin_isManagesAllOrganizationalGroups, customerId, userId, searchCriteria, firstName, lastName, email, idbucket, isProctorRole, notLmsRole, null, null, null, sortBy, sortDirection);
							recordSize = recordSize + users.size();
							userList.addAll(users);
							}
					} catch (ObjectRetrievalFailureException e) {
						if (log.isDebugEnabled()) {
							log.debug("learners:" + ids);
						}
					}
					index += bucketSize;
					if (index >= ids.length || isPageSizeExceeded) {
						loop = false;
					}
				}
				serachResult.put("recordSize", recordSize);
				serachResult.put("list", userList);
			} else {
				if (result.get("expression") != null) { // LMS-16348: Condition // is added due to the	reason that if expression is null then all records are
														// being fetched. ReadAllQuery raq = new ReadAllQuery(VU360User.class);
					int recordSize = 0;
					if (pageSize != -1 && pageIndex != -1) {
						specificationBuilder = (RepositorySpecificationsBuilder<VU360User>) result.get("expression");	
						
						if (lmsRole != null && !notLmsRole) {
							specificationBuilder.with("lmsRoles", specificationBuilder.JOIN_EQUALS, lmsRole.getId(), "AND");
						}
						if (lmsRole != null && notLmsRole) {
							specificationBuilder.with("lmsRoles", specificationBuilder.JOIN_NOT_EQUALS, lmsRole.getId(), "AND");
						}
						if (isProctorRole && lmsRole != null && lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)) {
							specificationBuilder.with("proctor_status", specificationBuilder.JOIN_EQUALS, ProctorStatusEnum.Active.toString(), "AND");
						}
						specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE, firstName,"AND");
						specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE, lastName,	"AND");
						specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, email,	"AND");
						specificationBuilder.with("firstName", specificationBuilder.LIKE_IGNORE_CASE,	searchCriteria, "OR");
						specificationBuilder.with("lastName", specificationBuilder.LIKE_IGNORE_CASE, searchCriteria, "OR");
						specificationBuilder.with("emailAddress", specificationBuilder.LIKE_IGNORE_CASE, searchCriteria, "OR");

						Sort sortSpec = null;
						if (sortDirection != -1) {
							sortSpec = orderBy(sortDirection, sortBy);
						}

						PageRequest pageRequest = null;
						
						pageRequest = new PageRequest(pageIndex, pageSize,	sortSpec);
						Page<VU360User> page = vu360UserRepository.findAll(specificationBuilder.build(), pageRequest);
						recordSize = recordSize	+ ((int) (long)page.getTotalElements());
						userList = page.getContent();
						
					}
					 else {
						List<VU360User> users = vu360UserRepository.showAll(lmsRole, isLMSAdministrator, trainingAdmin_isManagesAllOrganizationalGroups, customerId, userId, searchCriteria, firstName, lastName, email, null, isProctorRole, notLmsRole, null,null,null, sortBy, sortDirection);
						recordSize = users.size();
						userList.addAll(users);
					}

					serachResult.put("recordSize", recordSize);
					serachResult.put("list", userList);
				} else {
					serachResult.put("recordSize", 0);
					serachResult.put("list", userList);
				}
			}
		} catch (ObjectRetrievalFailureException e) {

		}
		return serachResult;
	}

	private Sort orderBy(int sortDirection, String sortBy) {
		if (sortDirection == 0) {
			return new Sort(Sort.Direction.ASC, sortBy);
		} else {
			return new Sort(Sort.Direction.DESC, sortBy);
		}

	}

	private Map<Object, Object> getManagerRestrictionUserList(
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean isManagesAllOrganizationalGroups_trainingAdministrator, List<OrganizationalGroup> managedGroups, Long customerId, Long userId) {

		RepositorySpecificationsBuilder<VU360User> specificationBuilder = new RepositorySpecificationsBuilder<VU360User>();
		boolean isExpressionReturn = true;
		HashMap<Object, Object> results = new HashMap<Object, Object>();

		if (isLMSAdministrator) {
			Long currentCustomerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			specificationBuilder.with("learner_customer_id", specificationBuilder.JOIN_EQUALS, currentCustomerId, "AND");
		} else {
			if (isManagesAllOrganizationalGroups_trainingAdministrator) {
				specificationBuilder.with("learner_customer_id", specificationBuilder.JOIN_EQUALS, customerId, "AND");
			} else {
				Set<Long> userIds = new HashSet<Long>();
				//loggedInUser = vu360UserRepository.findOne(loggedInUser.getId());
				if (managedGroups != null && managedGroups.size() > 0) {

					userIds=vu360UserRepository.getOrganizational_Group_Members(userId, customerId);
					
					if (userIds == null || userIds.size() == 0) {
						Object[] objEmptyArray = new Object[1];
						objEmptyArray[0] = -1;
						results.put("userIdArray", objEmptyArray);
					} else {
						isExpressionReturn = false;
						results.put("userIdArray", userIds);
					}
				} else {
					specificationBuilder = null;
				}
			}
		}
		results.put("isExpressionReturn", isExpressionReturn);
		results.put("expression", specificationBuilder);
		return results;
	}
	
	public List<ContentOwner> getContentOwnerList(VU360User loggedInUser) {

		List<ContentOwner> contentOwnerList = new ArrayList<ContentOwner>();
		/*
		 * According to business analyst none of the of customer can be exists
		 * in the system without distributor but distributor can exists without
		 * belonging to any distributor group
		 */

		if (loggedInUser.getLmsAdministrator().isGlobalAdministrator()) {
			contentOwnerList = (List<ContentOwner>) contentOwnerRepository
					.findAll();
		} else {
			Vector<Long> distributorIds = new Vector<Long>();
			Customer loggedInCustomer = loggedInUser.getLearner().getCustomer();
			Distributor loggedInDistributor = loggedInCustomer.getDistributor();
			List<DistributorGroup> distributorGroups = distributorGroupRepository.findByDistributorsId(loggedInDistributor.getId());

			if (distributorGroups == null) {
				distributorIds.add(loggedInDistributor.getId());
			} else {
				List<Distributor> distributorList = new ArrayList<Distributor>();
				for (DistributorGroup distributorGroup : distributorGroups) {

					// LMS-14184
					List<Distributor> ls = distributorRepository.getAllowedDistributorByGroupId(
							String.valueOf(distributorGroup.getId()), 
							String.valueOf(loggedInUser.getLmsAdministrator().getId()));
					if (ls != null && ls.size() > 0)
						distributorList.addAll(ls);
				}

				for (Distributor distributor : distributorList) {
					distributorIds.add(distributor.getId());
				}

			}

			/**
			 * Changed because of [LMS-14140]
			 */
			List<Map<Object, Object>> objCol = new ArrayList<Map<Object, Object>>();
			objCol = contentOwnerRepository.getContentOwnerByDistributorIDs(distributorIds);

			for (Map map : objCol) {
				ContentOwner contentOwner = new ContentOwner();

				contentOwner.setId(Long.valueOf(map.get("ID").toString()));
				contentOwner.setName(map.get("NAME").toString());
				contentOwner.setGuid(map.get("GUID").toString());

				contentOwnerList.add(contentOwner);
			}

		}
		return contentOwnerList;

	}
	

	@Transactional
	public LearnerPreferences saveLearnerPreferences(
			LearnerPreferences learnerPreferences) {
		return learnerPreferenceRepository.save(learnerPreferences);
	}
	

	public List<RegistrationInvitation> getRegistrationInvitationByName(
			Customer customer, String invitationName, VU360User loggedinUser) {

		List<RegistrationInvitation> registrationInvitations = null;

		if (loggedinUser.isLMSAdministrator()) {
			Long customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			registrationInvitations = registrationInvitationRepository.findByCustomerIdAndInvitationNameContainingIgnoreCase(customerId, invitationName);
		} else {
			if (loggedinUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
				//Checking equals as in TopLink 2 predicates for customerID is formed with 2 different customerIds as parameter which never gives result.
				//IF purpose is to avoid duplication of where clause for different customerIds
				if(customer.getId().equals(loggedinUser.getLearner().getCustomer().getId())){
					registrationInvitations = registrationInvitationRepository
							.findByCustomerIdAndInvitationNameContainingIgnoreCase(loggedinUser.getLearner().getCustomer().getId(), invitationName);
				}
				
			} else {
				if(loggedinUser.getTrainingAdministrator().getManagedGroups() != null && !loggedinUser.getTrainingAdministrator().getManagedGroups().isEmpty()){
  					Set<OrganizationalGroup> orgSet = new HashSet<OrganizationalGroup>(loggedinUser.getTrainingAdministrator().getManagedGroups());
  				     if(orgSet!=null && !orgSet.isEmpty()){
  				    	registrationInvitations = registrationInvitationRepository
  								.findDistinctByOrgGroupsIdInAndCustomerIdAndInvitationNameContainingIgnoreCase(
  										FormUtil.getPropertyArrayFromList(new ArrayList<OrganizationalGroup>(orgSet)), customer.getId(),
  										invitationName); 
  				       }
  				     }
  				   }
		}
		return registrationInvitations;

	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.service.LearnerService#
	 * getCreditReportingFieldsByLearner(com.softech.vu360.lms.model.Learner)
	 */
	public List<CreditReportingField> getCreditReportingFieldsByLearner(
			Learner learner) {
		List<LearnerEnrollment> emrollments = this.entitlementService
				.getActiveLearnerEnrollmentsByLearner(learner, "");
		Set<CreditReportingField> creditreportingFields = new HashSet<CreditReportingField>();
		List<Course> coursesList = new ArrayList<Course>();
		for (LearnerEnrollment learnerenrollemnt : emrollments) {

			if (!coursesList.contains(learnerenrollemnt.getCourse()))
				coursesList.add(learnerenrollemnt.getCourse());

		}
		for (Course course : coursesList) {
			List<CreditReportingField> creditReportingFieldsForCourse = new ArrayList<CreditReportingField>(
					this.accreditationService
							.getCreditReportingFieldsByCourse(course));
			if (creditReportingFieldsForCourse != null
					&& !creditReportingFieldsForCourse.isEmpty()) {
				creditreportingFields.addAll(creditReportingFieldsForCourse);
			}
		}

		return new ArrayList<CreditReportingField>(creditreportingFields);
	}
	

	public List<CreditReportingField> getCreditReportingFieldsByLearnerCourseApproval(Learner learner) {
		List<LearnerEnrollment> emrollments = this.entitlementService.getLearnerEnrollmentsForReportingfields(learner);
		Set<CreditReportingField> creditreportingFields = new HashSet<CreditReportingField>();
		List<String> coursesList = new ArrayList<String>();

		// TODO for performance use the below query to fetch course guid for learner enrollment
		/**
			SELECT DISTINCT c.GUID
			FROM LEARNERENROLLMENT le
			inner join Course c ON c.id=le.course_id
			where le.id in(51,52,53,54,55);
		 * */
		for (LearnerEnrollment learnerenrollemnt : emrollments) {
			if (!coursesList.contains(learnerenrollemnt.getCourse().getCourseGUID()))
				coursesList.add(learnerenrollemnt.getCourse().getCourseGUID());
		}

		List<LearningSession> lstLearningSession = accreditationService
				.getLearningSessionForCourseApproval(coursesList,
						learner.getId());

		for (LearningSession ls : lstLearningSession) {
			CourseApproval ca = accreditationService.getCourseApprovalById(ls
					.getCourseApprovalId());

			if (ca != null) {
				for (RegulatorCategory category : ca.getRegulatorCategories())
					creditreportingFields.addAll(category.getReportingFields());
			}
		}

		return new ArrayList<CreditReportingField>(creditreportingFields);
	}
	

	public List<CreditReportingFieldValueChoice> getChoicesByCreditReportingField(
		CreditReportingField creditReportingField) {
		return crfValueChoiceRepository.findByCreditReportingFieldIdOrderByDisplayOrderAsc(creditReportingField.getId());
	}
	

	public List<CreditReportingFieldValue> getCreditReportingFieldValues(
			Learner learner) {
		/**
		 * Modfied By Marium Saud : Learner>MyCourses Performance Optimization : No need to load 'CreditReportingFieldValue' again so commented this code.
		 */
		List<CreditReportingFieldValue> reportingFieldValues = new ArrayList<CreditReportingFieldValue>();
		reportingFieldValues = crfValueRepository.findByLearnerprofile_Learner_Id(learner.getId());
		return reportingFieldValues;
	}
	

	public List<Learner> getAllLearnersOfCustomer(Customer customer) {
		List<Learner> learners = new ArrayList<Learner>();
		learners = learnerRepository.findByCustomerId(customer.getId());
		return learners;
	}

	public CreditReportingFieldValue getCreditReportingFieldValueById(long id) {
		return crfValueRepository.findOne(id);
	}

	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	
	public SaltSource getSaltSource() {
		return saltSource;
	}

	
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	
	public void setAccreditationService(
			AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	

	// Modified By Marium.Saud
	@Transactional
	public CreditReportingFieldValue saveCreditReportingfieldValue(
			CreditReportingFieldValue creditReportingfieldValue) {

		CreditReportingField crf = creditReportingfieldValue
				.getReportingCustomField();
		Long id = crf.getId();
		CreditReportingField crfOriginal = null;

		try {
			if (id.equals(new Long(1104)) || id.equals(new Long(1102))) {
				crfOriginal = crfRepository.findOne(id);
				log.info("CreditReportingField before deep merge id = " + id
						+ ", FieldLabel = " + crfOriginal.getFieldLabel());
			}
			else{
				crfOriginal = crfRepository.findOne(id);
			}
		} catch (Exception e) {
			log.error("Exception on getting CreditReportingField before deep merge ...:"
					+ e);
		}

		// due to LMS-16564, we have to set CRField it again.- fresh Copy from db
	    creditReportingfieldValue.setReportingCustomField(crfOriginal);
				
		creditReportingfieldValue = crfValueRepository
				.save(creditReportingfieldValue);

		// @TODO ONLY ON DOB AND SSN
		if (creditReportingfieldValue.getReportingCustomField() instanceof DateTimeCreditReportingField) {
			this.storeEEncryptedValue(creditReportingfieldValue);
		}
		if (creditReportingfieldValue.getReportingCustomField() instanceof SSNCreditReportingFiled) {
			this.storeEEncryptedValue(creditReportingfieldValue);
		}

		try {
			if (id.equals(new Long(1104)) || id.equals(new Long(1102))) {
				CreditReportingField dbCRF = crfRepository.findOne(id);
				log.info("CreditReportingField after deep merge id = " + id
						+ ", FieldLabel = " + dbCRF.getFieldLabel());

				if (crfOriginal != null) {
					if (!crfOriginal.getFieldLabel().equals(
							dbCRF.getFieldLabel())) {
						String userName = creditReportingfieldValue
								.getLearnerprofile().getLearner()
								.getVu360User().getUsername();
						log.info("Reporting field change for this user: "
								+ userName + " at " + new Date());
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception on getting CreditReportingField after deep merge ...:"
					+ e);
		}
		return creditReportingfieldValue;
	}
	

	// Modified By Marium Saud
	@Transactional
	private void storeEEncryptedValue(
			CreditReportingFieldValue creditReportingfieldValue) {

		try {
			if (creditReportingfieldValue.getId() != null
					&& creditReportingfieldValue.getId() > 0) {
				crfValueRepository
						.storeEncryptedValue(creditReportingfieldValue);
			}
		} catch (Exception ex) {
			log.error("Exception on executing Stored Procedure storeEEncryptedValue after deep merge ...:"
					+ ex);
		}

	}
	

	@Transactional
	public LearnerProfile updateLearnerProfile(LearnerProfile learnerProfile) {
		return learnerProfileRepository.save(learnerProfile);
	}
	

	public List<TimeZone> getTimeZoneList() {
		List<TimeZone> timeZoneList = new ArrayList<TimeZone>();
		try {
			timeZoneList = (List<TimeZone>) timeZoneRepository.findAll();
		} catch (ObjectRetrievalFailureException e) {
			log.debug("Error in getting Time Zone List", e);
		}
		return timeZoneList;
	}
	

	public TimeZone getTimeZoneById(long id) {
		TimeZone timeZone = new TimeZone();
		try {
			timeZone = timeZoneRepository.findOne(Integer.valueOf((int)id));
		} catch (ObjectRetrievalFailureException e) {
			log.debug("Error getting timezone by ID", e);
		}
		return timeZone;
	}


	public AuthorService getAuthorService() {
		return authorService;
	}

	
	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	/**
	 * Register Learner to Discussion Forum on phpBB and returned lauch URL of forum.
	 */
	public String registerLearnerToVCS(DiscussionForumCourse dfCourse,
			Learner learner) {

		// Register learner to phpBB if new learner, or update account
		// information otherwise
		boolean wsSuccess = new VcsDiscussionForumClientWSImpl()
				.registerLearnerEvent(learner, dfCourse.getCourseGUID());
		log.debug("Success: " + wsSuccess);

		// Create and Return launchURL for Discussion Forum
		return VU360Properties.getVU360Property("vcsDFC.launchURL") + "?un="
				+ learner.getVu360User().getUsername() + "&pw="
				+ learner.getVu360User().getPassword() + "&ug="
				+ learner.getVu360User().getUserGUID().replace("-", "")
				+ "&ut=" + DFC_USER_TYPE_LEARNER + "&fg="
				+ dfCourse.getCourseGUID().replace("-", "");

	}
	

	@Transactional
	public List<Learner> setupLearnerForUsers(List<String> userGuidList,
			String contentOwnerGUID) {
		ContentOwner contentOwner = authorService
				.readContentOwnerByGUID(contentOwnerGUID);

		Customer customer = null;

		customer = authorService.getCustomerByContentOwner(contentOwner);
		if (customer == null) {
			if (authorService.getDistributorByContentOwner(contentOwner) != null)
				customer = authorService.getDistributorByContentOwner(
						contentOwner).getMyCustomer();
		}

		List<VU360User> userList = vu360UserRepository
				.getListOfUsersByGUID(userGuidList);
		Learner learner = null;
		List<Learner> listOfLearnersAdded = new ArrayList<Learner>();
		for (VU360User user : userList) {
			learner = new Learner();
			learner.setVu360User(user);
			learner.setCustomer(customer);
			LearnerProfile newLearnerProfile = new LearnerProfile();
			Address newAddress1 = new Address();
			newAddress1.setStreetAddress("");
			newAddress1.setStreetAddress2("");
			newAddress1.setCity("");
			newAddress1.setState("");
			newAddress1.setZipcode("");
			newAddress1.setCountry("");
			newLearnerProfile.setLearnerAddress(newAddress1);
			Address newAddress2 = new Address();
			newAddress2.setStreetAddress("");
			newAddress2.setStreetAddress2("");
			newAddress2.setCity("");
			newAddress2.setState("");
			newAddress2.setZipcode("");
			newAddress2.setCountry("");
			newLearnerProfile.setLearnerAddress2(newAddress2);
			newLearnerProfile.setLearner(learner);
			learner.setLearnerProfile(newLearnerProfile);
			learner = learnerRepository.save(learner);
			listOfLearnersAdded.add(learner);
		}
		List<OrganizationalGroup> groups = organizationalGrpRepository.findByCustomerIdAndRootOrgGroupIsNull(customer.getId());
		OrganizationalGroup orgGroup = null;
		if ( groups.size() > 0 ) 
			orgGroup = groups.get(0);
		this.addLearnersInOrgGroup(listOfLearnersAdded, orgGroup);
		LMSRole learnerRole = this.getDefaultRoleForLearner(customer);
		this.addLearnerInRole(listOfLearnersAdded, learnerRole);
		return listOfLearnersAdded;
	}
	
	
	public void addLearnerInRole(List<Learner> learner, LMSRole lmsRole) {
		
		VU360User updatedUser = null;
		
		for(int i=0; i<learner.size(); i++) {
			
			updatedUser = vu360UserRepository.findOne(learner.get(i).getVu360User().getId());
			updatedUser.getLmsRoles().add(lmsRole);
		}
		vu360UserRepository.save(updatedUser);
	}
	

	public String getLearnerAvatarNameFromVCS(Learner learner) {

		/**
		 * Modified By Marium Saud : LMS-19051 : The webservice calling has been commented out as it's not in working condition as verified from QA.
		String avatarName = new VcsDiscussionForumClientWSImpl()
				.getUserAvatarNameEvent(learner.getVu360User().getUserGUID());

		// Set avatar name to UserGUID if no avatar name is returned; this will
		// indicate NOT to save avatar on phpBB if it contains UserGUID
		if (StringUtils.isBlank(avatarName)) {
			avatarName = learner.getVu360User().getUserGUID().replace("-", "");
		}
		 */
		
		String avatarName = learner.getVu360User().getUserGUID().replace("-", "");
		return avatarName;
	}
	

	/**
	 * Save Learner Avatar on phpBB if and only if Avatar Name is not like
	 * UserGUID
	 */
	@Transactional
	public LearnerPreferences saveLearnerPreferencesToVCS(Learner learner,
			LearnerPreferences learnerPreferences, int avatarWidth,
			int avatarHeight) {

		String userGUID = learner.getVu360User().getUserGUID().replace("-", "");
		boolean wsSuccess = true;

		// Call phpBB Web Service to save avatar information IF avatar name does
		// not contain UserGUID
		if (learnerPreferences.getAvatar().indexOf(userGUID) == -1) {
			wsSuccess = new VcsDiscussionForumClientWSImpl()
					.saveUserAvatarEvent(userGUID,
							learnerPreferences.getAvatar(), avatarWidth,
							avatarHeight);
		}

		log.info("wsSuccess for saveUserAvatarEvent :: " + wsSuccess);

		if (wsSuccess) {
			learnerPreferences = learnerPreferenceRepository
					.save(learnerPreferences);
		}

		return learnerPreferences;
	}

	@Override
	/**
	 * [9/27/2010] LMS-7219 :: Learner Mode > Login: Force User to Change Password on Next Login
	 * Return true if given password is same as user password in Database - to be used for Change Password functionality
	 */
	public boolean isCorrectPassword(com.softech.vu360.lms.vo.VU360User user, String password) {

		try{
			// If the password is encrypted
			Object salt = this.saltSource.getSalt(user);
			String encryptedPassword = this.passwordEncoder.encodePassword(
					password, salt); // Encrypt given password
	
			if(encryptedPassword.equals(user.getPassword())){
				return Boolean.TRUE;
			}
			
			// If the password is plain
			if(password.equals(user.getPassword())){
				return Boolean.TRUE;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	

	public LMSRole getDefaultRoleForLearner(Customer customer) {
		LMSRole role = lmsRoleRepository.getDefaultRole(customer);
		if (role == null)
			role = lmsRoleRepository
					.findByRoleNameAndOwner("LEARNER", customer);
		return role;
	}
	

	public void deleteLearnerFromRole(Learner learner) {
		vu360UserRepository.deleteLMSTrainingManager(learner.getVu360User());

	}
	

	public List<LearnerGroupItem> getLearnerGroupItemsByLearnerGroupId(
			Long learnerGroupId) {
		return learnerGroupItemRepository.findByLearnerGroupId(Long
				.valueOf(learnerGroupId));
	}
	

	/*public void saveLearnerGroupItems(List<LearnerGroupItem> learnerGroupItems) {
		if (!learnerGroupItems.isEmpty()) {
			for (LearnerGroupItem learnerGroupItem : learnerGroupItems) {
				saveLearnerGroupItem(learnerGroupItem);
			}
		}
	}*/
	
	
	public void saveLearnerGroupItems(Set<LearnerGroupItem> learnerGroupItems) {
		if (!learnerGroupItems.isEmpty()) {
			for (LearnerGroupItem learnerGroupItem : learnerGroupItems) {
				saveLearnerGroupItem(learnerGroupItem);
			}
		}
	}
	
	
	public void saveLearnerGroupItem(LearnerGroupItem learnerGroupItem) {
		learnerGroupItemRepository.saveLGI(learnerGroupItem);
	}
	

	/**
	 * // [9/27/2010] LMS-7219 :: Learner Mode > Login: Force User to Change
	 * Password on Next Login
	 */
	@Override
	public VU360User updateUser(VU360User vu360User, boolean notifyUserByEmail,
			String loginURL, Brander brander, VelocityEngine velocityEngine) {

		String plainPassword = vu360User.getPassword();

		// Update User profile in LMS
		VU360User updatedUser = this.saveUser(vu360User);

		// Update User profile on SF
		Customer customer = vu360User.getLearner().getCustomer();
		if (customer != null && customer.getCustomerType().equals(Customer.B2C)
				&& (CustomerUtil.isUserProfileUpdateOnSF(customer))) {
			this.updateProfileOnSF(customer, vu360User, plainPassword);
		}

		// Notify User via Email
		if (notifyUserByEmail) {
			this.NotifyUserByEmail(vu360User, plainPassword, loginURL, brander,
					velocityEngine);
		}

		return updatedUser;
	}

	private void NotifyUserByEmail(VU360User vu360User, String plainPassword,
			String loginURL, Brander brander, VelocityEngine velocityEngine) {

		try {
			Map<String, Object> model = new HashMap<String, Object>();

			VU360User user = vu360User;
			user.setPassword(plainPassword);

			model.put("loggedInUser", vu360User);
			model.put("customerName", vu360User.getLearner().getCustomer()
					.getName());
			model.put("user", user);
			model.put("url", loginURL);

			String templatePath = brander
					.getBrandElement("lms.email.resetPassWord.body");
			String fromAddress = brander
					.getBrandElement("lms.email.resetPassWord.fromAddress");
			String fromCommonName = brander
					.getBrandElement("lms.email.resetPassWord.fromCommonName");
			String subject = brander
					.getBrandElement("lms.emil.resetPassWord.subject");
			String support = brander
					.getBrandElement("lms.email.resetPassWord.fromCommonName");
			String phone = brander
					.getBrandElement("lms.footerLinks.contactus.contactPhone");
			String lmsDomain = VU360Properties.getVU360Property("lms.domain");
			model.put("lmsDomain", lmsDomain);
			model.put("support", support);
			model.put("phone", phone);
			model.put("brander", brander);
			model.put("learnerPassword", user.getPassword());

			/* START- BRANDNG EMAIL WORK */
			String templateText = brander
					.getBrandElement("lms.branding.email.passwordUpdated.templateText");
			String loginurl = lmsDomain.concat("/lms/login.do?brand=").concat(
					brander.getName());
			templateText = templateText.replaceAll("&lt;firstname&gt;",
					vu360User.getFirstName());
			templateText = templateText.replaceAll("&lt;lastname&gt;",
					vu360User.getLastName());
			templateText = templateText
					.replaceAll("&lt;loginurl&gt;", loginurl);
			templateText = templateText.replaceAll("&lt;phone&gt;", phone);
			templateText = templateText.replaceAll("&lt;support&gt;", support);
			templateText = templateText.replaceAll("&lt;customername&gt;",
					vu360User.getLearner().getCustomer().getName());
			model.put("templateText", templateText);
			/* END-BRANDING EMAIL WORK */
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, templatePath, model);
			SendMailService.sendSMTPMessage(user.getEmailAddress(),
					fromAddress, fromCommonName, subject, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void updateProfileOnSF(Customer customer, VU360User vu360User,
			String plainPassword) {
		CustomerData customerData = new CustomerData();

		customerData.setCustomerID(customer.getCustomerGUID());
		customerData.setBillingAddress(getBillingAddress(vu360User));
		customerData.setShippingAddress(getShippingAddress(vu360User));

		UpdateUserAuthenticationCredential auth = new UpdateUserAuthenticationCredential();
		auth.setUserLogonID(vu360User.getUsername());
		auth.setUserPassword(plainPassword);

		new StorefrontClientWSImpl().updateProfileOnStorefront(customerData,
				auth);
	}
	

	/**
	 * Setting the Address info
	 * 
	 * @param user
	 * @param address
	 * @return
	 */
	public com.softech.vu360.lms.webservice.message.storefront.client.Address getBillingAddress(
			VU360User user) {
		com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
		Address learnerAddress = user.getLearner().getLearnerProfile()
				.getLearnerAddress();

		msgAddress.setStreetAddress1(learnerAddress.getStreetAddress());
		msgAddress.setStreetAddress2(learnerAddress.getStreetAddress2());
		msgAddress.setCity(learnerAddress.getCity());
		msgAddress.setCountry(learnerAddress.getCountry());
		msgAddress.setState(learnerAddress.getState());
		msgAddress.setZipCode(learnerAddress.getZipcode());
		msgAddress.setFirstName(user.getFirstName());
		msgAddress.setLastName(user.getLastName());
		msgAddress.setPhone(user.getLearner().getLearnerProfile()
				.getOfficePhone());

		return msgAddress;
	}
	

	/**
	 * Setting the Address info
	 * 
	 * @param user
	 * @param address
	 * @return
	 */
	public com.softech.vu360.lms.webservice.message.storefront.client.Address getShippingAddress(
			VU360User user) {
		com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
		Address learnerAddress = user.getLearner().getLearnerProfile()
				.getLearnerAddress2();

		msgAddress.setStreetAddress1(learnerAddress.getStreetAddress());
		msgAddress.setStreetAddress2(learnerAddress.getStreetAddress2());
		msgAddress.setCity(learnerAddress.getCity());
		msgAddress.setCountry(learnerAddress.getCountry());
		msgAddress.setState(learnerAddress.getState());
		msgAddress.setZipCode(learnerAddress.getZipcode());
		msgAddress.setFirstName(user.getFirstName());
		msgAddress.setLastName(user.getLastName());
		msgAddress.setPhone(user.getLearner().getLearnerProfile()
				.getOfficePhone());
		return msgAddress;
	}
	

	public LMSRole getRoleForProctorByCustomer(Customer customer) {
		// LMSRole role = userDAO.getDefaultRole(customer);
		// if(role==null)
		LMSRole role = lmsRoleRepository.findByRoleNameAndOwner("PROCTOR",
				customer);
		return role;
	}
	

	public LearnerProfile loadForUpdateLearnerProfile(long id) {
		return learnerProfileRepository.findOne(id);
	}

	public CustomerService getCustomerService() {
		return customerService;
	}
	

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	

	public RegistrationInvitation loadForPreviewRegistrationInvitation(long id) {
		return registrationInvitationRepository.findOne(id);
	}

	public LearnerGroup loadForDisplayLearnerGroup(long id) {
		LearnerGroup learnerGroup = null;
		try {
			learnerGroup = learnerGroupRepository.findOne(id);
		} catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("user with id: " + id + "is not found");
			}
		}
		return learnerGroup;
	}
	

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public List<LearnerValidationAnswers> getLearnerUniqueQuestionsAnswers(long learnerId,long courseConfigurationId){
			List<LearnerValidationAnswers> answers = learnerValidationAnswerRepository.getLearnerUniqueValidationQuestionsAnswers(learnerId, courseConfigurationId);
			return answers;
    }
	
   public LearnerValidationAnswers getLearnerUniqueQuestionsAnswersByQuestion(long questionId){
		
		LearnerValidationAnswers questionanswer = learnerValidationAnswerRepository.findByQuestionId(questionId);

		return questionanswer;
	}

   public List<UniqueQuestionAnswerVO> getLearnerUniqueQestionNoAnswers(List<ValidationQuestion> lstValidationQuestion,long learnerId){
		UniqueQuestionAnswerVO uniqueQuestionAnswerVO = null;
		List<UniqueQuestionAnswerVO> lstuniqueQuestionAnswerVO = new ArrayList<>();
		
		if(lstValidationQuestion!=null && !lstValidationQuestion.isEmpty()){
			for( ValidationQuestion learnerValidationQuestion :lstValidationQuestion){
				uniqueQuestionAnswerVO = new UniqueQuestionAnswerVO();
				uniqueQuestionAnswerVO.setLearnerId(learnerId);
				uniqueQuestionAnswerVO.setQuestion(learnerValidationQuestion.getQuestion());
				uniqueQuestionAnswerVO.setQuestionId(learnerValidationQuestion.getId().longValue());
				uniqueQuestionAnswerVO.setQuestionType(learnerValidationQuestion.getQuestionType());
				uniqueQuestionAnswerVO.setAnswer("");
				lstuniqueQuestionAnswerVO.add(uniqueQuestionAnswerVO);
		  }
		}
		return lstuniqueQuestionAnswerVO;
	}
   
   public List<UniqueQuestionAnswerVO> getLearnerUniqueQestionAvailableAnswers(List<ValidationQuestion> lstValidationQuestion,long learnerId, long courseConfigurationId){
		UniqueQuestionAnswerVO uniqueQuestionAnswerVO = null;
		List<LearnerValidationAnswers> lstAnswers = null;
		List<UniqueQuestionAnswerVO> lstuniqueQuestionAnswerVO = new ArrayList<>();
		
		lstAnswers = getLearnerUniqueQuestionsAnswers(learnerId, courseConfigurationId);
		
		if(lstValidationQuestion!=null && !lstValidationQuestion.isEmpty()){
			if(lstAnswers!=null && !lstAnswers.isEmpty()){
			for( ValidationQuestion learnerValidationQuestion :lstValidationQuestion){
				for(LearnerValidationAnswers learnerValidationAnswers:lstAnswers){
					if(learnerValidationAnswers.getQuestionId() == learnerValidationQuestion.getId().longValue()){
						uniqueQuestionAnswerVO = new UniqueQuestionAnswerVO();
						uniqueQuestionAnswerVO.setLearnerId(learnerId);
						uniqueQuestionAnswerVO.setQuestion(learnerValidationQuestion.getQuestion());
						uniqueQuestionAnswerVO.setQuestionId(learnerValidationQuestion.getId().longValue());
						uniqueQuestionAnswerVO.setQuestionType(learnerValidationQuestion.getQuestionType());
						uniqueQuestionAnswerVO.setAnswer(learnerValidationAnswers.getAnswer());
						uniqueQuestionAnswerVO.setAnswerId(learnerValidationAnswers.getId());
						lstuniqueQuestionAnswerVO.add(uniqueQuestionAnswerVO);
			  }
			 }
			}
		  }
		}
		return lstuniqueQuestionAnswerVO;
	}
   
   public Map<Object,Object> getLearnerUniqueQuestions(long learnerId){
		
		Learner learner = getLearnerByID(learnerId);
		List<ValidationQuestion> lstValidationQuestion = null;
		List<LearnerValidationAnswers> lstAnswers = null;
		List<UniqueQuestionAnswerVO> lstuniqueQuestionAnswerVO = null;
		LinkedHashMap<Object,Object> mpUniqueQuestions = new LinkedHashMap<Object,Object>();
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		if(learner != null){
			List<LearnerEnrollment>  lstLearnerEnrollment = enrollmentService.getAllLearnerEnrollmentsByLearner(learner);
			for(LearnerEnrollment le :lstLearnerEnrollment){
				if(le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.IN_PROGRESS)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.IN_COMPLETE)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.LOCKED)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_PENDING)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.REPORTING_PENDING)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT)
				        || le.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.REPORTED)) {
				      
	                  CourseApproval courseApproval =
	                      accreditationService.getCourseApprovalByCourse(le.getCourse());
	                  if (courseApproval != null && courseApproval.getTemplate() != null) {
	                    CourseConfiguration courseCongifuration =
	                        accreditationService.getCourseConfigurationByTemplateId(courseApproval
	                            .getTemplate().getId(), true);
	                    if (courseCongifuration != null
	                        && courseCongifuration.isRequireDefineUniqueQuestionValidation()) {
	                      lstValidationQuestion =
	                          accreditationService
	                              .getUniqueValidationQuestionByCourseConfigurationId(courseCongifuration
	                                  .getId());
	                      if (lstValidationQuestion != null && !lstValidationQuestion.isEmpty()) {
	                        lstAnswers =
	                            getLearnerUniqueQuestionsAnswers(learnerId, courseCongifuration.getId());
	                        if (lstAnswers != null && !lstAnswers.isEmpty()) {
	                          lstuniqueQuestionAnswerVO =
	                              getLearnerUniqueQestionAvailableAnswers(lstValidationQuestion, learnerId,
	                                  courseCongifuration.getId());
	                        } else {
	                          lstuniqueQuestionAnswerVO =
	                              getLearnerUniqueQestionNoAnswers(lstValidationQuestion, learnerId);
	                        }
	                        mpUniqueQuestions.put("courseName_" + le.getCourse().getId(), le.getCourse()
	                            .getCourseTitle());
	                        if (lstuniqueQuestionAnswerVO != null && !lstuniqueQuestionAnswerVO.isEmpty())
	                          mpUniqueQuestions.put("questionanswerLst_" + le.getCourse().getId(),
	                              lstuniqueQuestionAnswerVO);
	                      }
	                    }
	                  }
				}
			}		
		}
		return mpUniqueQuestions;
	}
   
	public LearnerValidationQASetDTO getLearnerValidationQuestions(
			long learnerId) {
		List<LearnerValidationAnswers> answers = learnerValidationAnswerRepository
				.findByLearnerId(learnerId);

		LearnerValidationQASetDTO dto = new LearnerValidationQASetDTO();
		dto = dto.getLearnerValidationQASetDTO(answers);
		dto.setLearnerId(learnerId);
		return dto;
	}
	
	public List<LearnerValidationAnswers> getLearnerUniquesValidationQuestions(long learnerId){
		List<LearnerValidationAnswers> answers = learnerValidationAnswerRepository.getLearnerValidationQuestions(learnerId);
        return answers;
	}
	
	@Transactional
	public void saveLearnerUniquesValidationQuestions(LearnerValidationAnswers answer){
		learnerValidationAnswerRepository.save(answer);
    }
	
	public LearnerValidationAnswers loadForUpdateLearnerValidationAnswers(long answer){
		return learnerValidationAnswerRepository.findOne(answer);
	}

	@Transactional
	public LearnerValidationAnswers updateLearnerValidationAnswers(LearnerValidationAnswers lva ){
		return learnerValidationAnswerRepository.save(lva );
		
	}
	@Transactional
	public void saveLearnerValidationAnswers(LearnerValidationQASetDTO qaDTO,
			Learner learner) {
		LearnerValidationAnswers dbAnswer, formAnswer;
		List<LearnerValidationAnswers> formAnswers = qaDTO
				.getLearnerValidationAnswersList();
		List<LearnerValidationAnswers> dbAnswers = learnerValidationAnswerRepository.findByLearnerId(learner.getId());
		LearnerValidationAnswers updatedAnswer;

		if (dbAnswers.size() == 0) {// insert
			for (LearnerValidationAnswers answer : formAnswers) {
				if (answer.getQuestionId() > 0) {
					answer.setLearner(learner);
					learnerValidationAnswerRepository.save(answer);
				}
			}
		}

		else {// update
			for (int i = 0; i < 5; i++) {
				dbAnswer = dbAnswers.get(i);
				formAnswer = formAnswers.get(i);

				// only update in case if the value is change to minimize write
				// operations
				if ((!dbAnswer.getAnswer().equalsIgnoreCase(
						formAnswer.getAnswer()) || dbAnswer.getQuestionId() != formAnswer
						.getQuestionId()) && formAnswer.getQuestionId() > 0) {
					updatedAnswer = learnerValidationAnswerRepository
							.findOne(dbAnswer.getId());
					updatedAnswer.setAnswer(formAnswer.getAnswer());
					updatedAnswer.setQuestionId(formAnswer.getQuestionId());
					learnerValidationAnswerRepository.save(updatedAnswer);
				}
			}
		}
	}
	

	@Override
	public Learner addLearnerForDefaultCustomer(
			Customer customer,
			com.softech.vu360.lms.webservice.message.storefront.Contact sfContact,
			com.softech.vu360.lms.webservice.message.storefront.Address sfAddress) {
		// TODO Auto-generated method stub
		Learner newLearner = new Learner();
		VU360User newUser = new VU360User();

		newUser.setFirstName(sfContact.getFirstName());
		// UI has not provided me the middlename
		newUser.setLastName(sfContact.getLastName());
		newUser.setEmailAddress(sfContact.getEmailAddress());
		newUser.setPassword(sfContact.getAuthenticationCredential()
				.getPassword());
		newUser.setUsername(sfContact.getAuthenticationCredential()
				.getUsername());

		newUser.setAccountNonExpired(true);
		newUser.setAcceptedEULA(new Boolean(false));
		newUser.setNewUser(new Boolean(true));
		newUser.setAccountNonLocked(true);
		newUser.setChangePasswordOnLogin(false);
		newUser.setCredentialsNonExpired(true);
		newUser.setEnabled(true);
		newUser.setVissibleOnReport(true);

		newUser.setExpirationDate(null);

		String guid = GUIDGeneratorUtil.generateGUID();
		newUser.setUserGUID(guid);
		Calendar calender = Calendar.getInstance();
		Date createdDate = calender.getTime();
		newUser.setCreatedDate(createdDate);

		// everyone is a learner
		newUser.addLmsRole(getDefaultRoleForUser(customer));

		LearnerProfile newLearnerProfile = new LearnerProfile();

		Address newAddress = new Address();
		newAddress.setStreetAddress(sfContact.getBillingAddress()
				.getAddressLine1().trim());
		newAddress.setStreetAddress2(sfContact.getBillingAddress()
				.getAddressLine2().trim());
		newAddress.setCity(sfAddress.getCity());
		newAddress.setState(sfContact.getBillingAddress().getState().trim());
		newAddress.setZipcode(sfContact.getBillingAddress().getZipCode());
		newAddress.setCountry(sfAddress.getCountry());
		newLearnerProfile.setLearnerAddress(newAddress);

		Address newAddress2 = new Address();
		newAddress2.setStreetAddress(sfContact.getShippingAddress()
				.getAddressLine1().trim());
		newAddress2.setStreetAddress2(sfContact.getShippingAddress()
				.getAddressLine2().trim());
		newAddress2.setCity(sfContact.getShippingAddress().getCity().trim());
		newAddress2.setState(sfContact.getShippingAddress().getState().trim());
		newAddress2.setZipcode(sfContact.getShippingAddress().getZipCode()
				.trim());
		newAddress2.setCountry(sfContact.getShippingAddress().getCountry()
				.trim());
		newLearnerProfile.setLearnerAddress2(newAddress2);

		newLearnerProfile.setMobilePhone(sfContact.getPrimaryPhone());

		newLearner.setVu360User(newUser);
		newLearnerProfile.setLearner(newLearner);
		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);

		newUser.setLearner(newLearner);
		List<OrganizationalGroup> groups = new ArrayList<OrganizationalGroup>();

		OrganizationalGroup orgGroup = orgGroupLearnerGroupService.getRootOrganizationalGroupForUser(customer);

		//OrganizationalGroup orgGroup = ((CustomerServiceImpl) customerService)
		//		.getGroupService().getRootOrganizationalGroupForUser(customer);

		groups.add(orgGroup);
		newLearner = addLearner(false, newLearner.getCustomer(), groups, null,
				newLearner);
		return newLearner;
	}
	

	public Learner updateLearner(
			Learner learner,
			Customer customerToUpdate,
			com.softech.vu360.lms.webservice.message.storefront.Contact sfContact,
			com.softech.vu360.lms.webservice.message.storefront.Address sfAddress) {
		log.info("Updating Learner's User and Learner Profile Addresses...");
		learner = loadForUpdateLearner(learner.getId());
		VU360User user = learner.getVu360User();
		user.setPassword(sfContact.getAuthenticationCredential().getPassword());
		user.setPassWordChanged(true);
		user.setFirstName(sfContact.getFirstName());
		user.setLastName(sfContact.getLastName());
		user.setLastUpdatedDate(Calendar.getInstance().getTime());
		Address billingAddress = learner.getLearnerProfile()
				.getLearnerAddress();
		Address shippingaddress = learner.getLearnerProfile()
				.getLearnerAddress2();
		if (billingAddress != null)
			copyAddress(sfContact.getBillingAddress(), billingAddress);
		if (shippingaddress != null)
			copyAddress(sfContact.getShippingAddress(), shippingaddress);
		learner.getLearnerProfile().setLearnerAddress(billingAddress);
		learner.getLearnerProfile().setLearnerAddress2(shippingaddress);
		learner.getLearnerProfile().setMobilePhone(sfContact.getPrimaryPhone());
		learner.getLearnerProfile().setOfficePhone(
				sfContact.getAlternatePhone());
		learner.setCustomer(customerToUpdate);
		try {
			saveLearner(learner);
			log.info("Learner's User and Learner Profile Addresses are updated!");
		} catch (Exception e) {
			log.error("Error occured while saving learner " + e.getMessage(), e);
		}
		return learner;
	}
	

	private void copyAddress(
			com.softech.vu360.lms.webservice.message.storefront.Address srcAddress,
			Address destAddress) {
		destAddress.setCity(srcAddress.getCity());
		destAddress.setCountry(srcAddress.getCountry());
		// destAddress.setProvince(srcAddress.getProvince());
		destAddress.setState(srcAddress.getState());
		destAddress.setStreetAddress(srcAddress.getAddressLine1());
		destAddress.setStreetAddress2(srcAddress.getAddressLine2());
		destAddress.setStreetAddress3(srcAddress.getAddressLine3());
		destAddress.setZipcode(srcAddress.getZipCode());
	}

	// this method is not in use, can be use in future.
	@Override
	public Learner addNewLearnerGivenCustomer(Customer customer,
			Hashtable<String, Object> leanerDetailInHashmap) throws Exception {

		String firstName = (String) leanerDetailInHashmap.get("FirstName");
		String lastName = (String) leanerDetailInHashmap.get("LastName");
		String emailAddress = (String) leanerDetailInHashmap.get("EmailAddress");
		String userName = (String) leanerDetailInHashmap.get("UserName");
		String password = (String) leanerDetailInHashmap.get("Password");
		boolean isAccountExpired = false;
		boolean isAccountLocked = false;
		boolean isChangePasswordOnNextLogin = true;
		boolean isVisibleOnReport = true;

		Calendar calender = Calendar.getInstance();
		Date createdDate = calender.getTime();
		LMSRole systemRole = vu360UserService.getDefaultSystemRole(customer);

		VU360User newUser = new VU360User();
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmailAddress(emailAddress);
		newUser.setPassword(password);
		newUser.setUsername(userName);
		newUser.setAccountNonExpired(!isAccountExpired);
		newUser.setAccountNonLocked(!isAccountLocked);
		newUser.setChangePasswordOnLogin(isChangePasswordOnNextLogin);
		newUser.setVissibleOnReport(isVisibleOnReport);
		newUser.setAcceptedEULA(false);
		newUser.setNewUser(true);
		newUser.setCredentialsNonExpired(true);
		newUser.setEnabled(true);
		newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
		newUser.setCreatedDate(createdDate);
		newUser.addLmsRole(systemRole);

		newUser.setExpirationDate(DateUtil.getDateObject("12/31/2020"));

		// Learner Profile setting.
		String phone = (String) leanerDetailInHashmap.get("Phone");
		Address address1 = getEmptyAddress();
		Address alternateAddress = getEmptyAddress();

		LearnerProfile newLearnerProfile = new LearnerProfile();
		newLearnerProfile.setOfficePhone(phone);

		if (address1 != null) {
			Address newAddress = getNewAddress(leanerDetailInHashmap);
			newLearnerProfile.setLearnerAddress(newAddress);
		} else {
			address1 = getEmptyAddress();
			Address newAddress = getNewAddress(leanerDetailInHashmap);
			newLearnerProfile.setLearnerAddress(newAddress);
		}

		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress2(leanerDetailInHashmap);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			alternateAddress = getEmptyAddress();
			Address newAddress2 = getNewAddress2(leanerDetailInHashmap);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		}

		Learner newLearner = new Learner();
		newLearner.setVu360User(newUser);

		newLearnerProfile.setLearner(newLearner);

		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);

		newUser.setLearner(newLearner);

		log.debug("adding learner...");
		newLearner = addLearner(newLearner);
		log.debug("learner added.");

		return newLearner;
	}
	

	private Address getNewAddress(
			Hashtable<String, Object> leanerDetailInHashmap) {

		String streetAddress1 = (String) leanerDetailInHashmap
				.get("Address1Line1");
		String streetAddress2 = (String) leanerDetailInHashmap
				.get("Address1Line2");
		String city = (String) leanerDetailInHashmap.get("City1");
		String country = (String) leanerDetailInHashmap.get("Country1");
		String state = (String) leanerDetailInHashmap.get("State1");
		String zipCode = (String) leanerDetailInHashmap.get("Zip1");

		Address newAddress = new Address();
		newAddress.setStreetAddress(streetAddress1);
		newAddress.setStreetAddress2(streetAddress2);
		newAddress.setCity(city);
		newAddress.setState(state);
		newAddress.setZipcode(zipCode);
		newAddress.setCountry(country);

		return newAddress;

	}
	

	private Address getNewAddress2(
			Hashtable<String, Object> leanerDetailInHashmap) {

		String streetAddress1 = (String) leanerDetailInHashmap
				.get("Address2Line1");
		String streetAddress2 = (String) leanerDetailInHashmap
				.get("Address2Line2");
		String city = (String) leanerDetailInHashmap.get("City2");
		String country = (String) leanerDetailInHashmap.get("Country2");
		String state = (String) leanerDetailInHashmap.get("State2");
		String zipCode = (String) leanerDetailInHashmap.get("Zip2");

		Address newAddress = new Address();
		newAddress.setStreetAddress(streetAddress1);
		newAddress.setStreetAddress2(streetAddress2);
		newAddress.setCity(city);
		newAddress.setState(state);
		newAddress.setZipcode(zipCode);
		newAddress.setCountry(country);

		return newAddress;

	}
	

	private Address getEmptyAddress() {
		Address address = new Address();
		address.setCity("");
		address.setCountry("");
		address.setState("");
		address.setStreetAddress("");
		address.setStreetAddress2("");
		address.setStreetAddress2("");
		return address;
	}
	

	// Added By Marium Saud
	// Function moved from TopLinkLearnerDao to Service
	public List<Learner> getLearnersByIds(Long[] ids) {
		final int max_limit = 1000;
		int buckets = ids.length % max_limit;
		int bucketSize = max_limit;
		List<Learner> allLearners = new ArrayList<Learner>(ids.length);
		List<Learner> learners = null;
		boolean loop = buckets > 0;
		int index = 0;
		Long[] idbucket = null;
		while (loop) {
			if (index + max_limit > ids.length) {
				bucketSize = ids.length - index;// supposed to be last bucket
												// with the content less than
												// max_limit
			} else {
				bucketSize = max_limit;
			}
			idbucket = new Long[bucketSize];
			System.arraycopy(ids, index, idbucket, 0, bucketSize);
			try {

				learners = learnerRepository.findByIdIn(idbucket);
				allLearners.addAll(learners);
			} catch (ObjectRetrievalFailureException e) {
				if (log.isDebugEnabled()) {
					log.debug("learners:" + ids);
				}
			}

			index += bucketSize;
			if (index >= ids.length)
				loop = false;
		}
		return allLearners;
	}
	

	@Override
	public Address findAddressById(Long id) {
		return addressRepository.findOne(id);
	}
	

	@Override
	public Address updateAddress(Address address) {
		if(address!=null && address.getId()!=null){
			return addressRepository.save(address);
		}
		
		return null;
	}
	
	@Override
	public Learner getLearnerByVU360UserId(VU360User user) {
		return learnerRepository.findByVu360UserId(user.getId());
	}

	@Override
	public TimeZone getTimeZoneByLearnerProfileId(long learnerProfileId) {
		return timeZoneRepository.findByLearnerProfileId(learnerProfileId);
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	@Override
	public List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId) {
		return organizationalGrpRepository.findAllManagedGroupsByTrainingAdministratorId(trainingAdminstratorId);
	}
	
	public boolean hasAnyInProgressEnrollmentOfStandardValidationQuestions(long learnerId) {
		 return learnerRepository.hasAnyInProgressEnrollmentOfStandardValidationQuestions(learnerId);
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	
}