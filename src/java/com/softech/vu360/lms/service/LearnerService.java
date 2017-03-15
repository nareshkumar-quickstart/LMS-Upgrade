package com.softech.vu360.lms.service;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LearnerValidationAnswers;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.model.LearnerValidationQASetDTO;
import com.softech.vu360.util.Brander;

/**
 * @author Ashis
 * @modified Faisal A. Siddiqui
 */
public interface LearnerService {
	
	public List<VU360User> findLearner(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId);
	public VU360User updateUser(VU360User user);
	public VU360User updateUserFromBatchFile(VU360User user);
	public Learner saveLearner(Learner learner); // save learner for update
	public Learner addLearner(Learner newLearner); 
	public Learner addLearner(Customer cust,
			List<OrganizationalGroup> orgGroups,
			List<LearnerGroup> learnerGroups, Learner newLearner);
	public Learner addLearner(boolean shouldAddInAD, Customer cust,
			List<OrganizationalGroup> orgGroups,
			List<LearnerGroup> learnerGroups, Learner newLearner);
	public Learner addLearnerForDefaultCustomer(Customer customer, com.softech.vu360.lms.webservice.message.storefront.Contact sfContact, com.softech.vu360.lms.webservice.message.storefront.Address sfAddress);
	
	public Learner updateLearner(Learner learner, Customer customer, com.softech.vu360.lms.webservice.message.storefront.Contact sfContact, com.softech.vu360.lms.webservice.message.storefront.Address sfAddress);
	

	public Learner getLearnerByID(long id);
	public List<Learner> getLearnersByIDs(Long[] ids);
	public VU360User saveUser(VU360User userToSave);

	public Learner getLearnerForB2CCustomer(Customer cust);
	
	public Long getLearnerForSelectedCustomer(Long customerId);
	
	public Long getLearnerForSelectDistributor(Long myCustomerId);

	public List<VU360User> findLearner(String searchCriteria, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId);

	public void InactiveUsers(long userIdArray[]);

	public RegistrationInvitation saveRegistrationInvitation(RegistrationInvitation regInvitation);

	//public OrganizationalGroup findLearnerByOrgGroup(long id);

	public RegistrationInvitation getRegistrationInvitationByID(long id);

	public void deleteRegistrationInvitations(long regIdArray[]);

	public LearnerGroup getLearnerGroupById(Long id);

	public OrganizationalGroup getOrganizationalGroupById(Long id);
//	public OrganizationalGroup readOrganizationalGroupById(Long id);
	//public void ManageOrgAndLearnerGroup(Learner learner);

//	public void deleteLearnerFromOrgGroup(List<Learner> learner,
//			OrganizationalGroup orgGroup);

	public Map<Object, Object> findLearner1(String searchCriteria,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize, String sortBy,
			int sortDirection);

	public Map<Object, Object> findLearner1(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize,
			String sortBy, int sortDirection);

	public Map<Object, Object> findActiveLearners(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId,
			boolean accountNonExpired, boolean accountNonLocked, boolean enabled,
			int pageIndex, int pageSize,String sortBy, int sortDirection);

	
	public List<Learner> addLearnersInOrgGroup(String[] selectedLearners,Long orgGroupId);
	public List<Learner> addLearnersInOrgGroup(List<Learner> listSelectedLearner,	OrganizationalGroup OrgGroup);

	//public void addLearnerInOrgGroup(List<Learner> listSelectedLearner,	OrganizationalGroup OrgGroup);

//	public Map<Object, Object> findLearner4AssigningInstRole(String firstName, String lastName,
//			String email, VU360User loggedInUser, int pageIndex, int pageSize,
//			String sortBy, int sortDirection, int roleId);

	public List<ContentOwner> getContentOwnerList(VU360User loggedInUser);
	
	public void deleteOrgGroup(List<Learner> listSelectedLearner, OrganizationalGroup OrgGroup);

//	public OrganizationalGroup getOrganizationalGroupByName(String name, Customer customer);

	public LearnerGroup getLearnerGroupByName(String name, Customer customer);

	public void addLearnersInLearnerGroup(List<Learner> listSelectedLearner,	LearnerGroup learnerGroup);
	//public void addLearnerInLearnerGroup(List<Learner> listSelectedLearner,	LearnerGroup learnerGroup);

    public void addLearnerInLearnerGroup (Learner learner, LearnerGroup learnerGroup);

    public void deleteLearnerFromLearnerGroup(Learner learner, LearnerGroup learnerGroup);

//    public void deleteLearnerFromLearnerGroup(List<Learner> learner, LearnerGroup learnerGroup);

	public LMSRole getLMSRoleById(Long id);

	public List<Learner> getMembersByRole(LMSRole lmsRole);
	public Long countMembersByRole(LMSRole lmsRole);
	
	public Map<String, String> countLearnerByRoles(Long [] trainingPlanIds);
	
//	public void deleteLearnerFromRole(List<Learner> learner, LMSRole lmsRole);

	public void addLearnerInRole(List<Learner> learner, LMSRole lmsRole);

	public LearnerGroup saveLearnerGroup(LearnerGroup learnerGroup);
    public LearnerGroup saveLearnerGroup2(LearnerGroup learnerGroup);
	// find all learners irrespective of current customer
//	public Map<Object,Object> findAllApplicationLearners(String searchCriteria,VU360User loggedInUser,String sortBy,int sortDirection);
	// find from all learners irrespective of current customer
	public 	 List<VU360User> findAllLearner(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize,
			String sortBy, int sortDirection ,int limit);
	
	// find from all learners irrespective of current customer
	public Map findAllLearnersByCustomer(String firstName, String lastName, String email, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection,int limit);	
	
	public Map<Object,Object> findAllLearnersWithCriteria( String fName, String lName, String eMail, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection );
	
	public Map<Object, Object> findAllLearners(String searchCriteria,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection);
	
	public Map<Object, Object> findAllLearners(String searchCriteria, 
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize, String sortBy, int sortDirection); 
	
	// searching for all learners with having the same email addresses
	public List<VU360User> findAllSystemLearners(Collection<String> vList);

	public LMSRole addRole(LMSRole role, Customer customer);

	public LMSRole updateRole(LMSRole role,Customer customer);

	public Map<Object, Object> getAllUsersInLmsRole(LMSRole lmsRole,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			int pageIndex, int pageSize, String sortBy,
			int sortDirection);

	public Map<Object, Object> getAllUsersInLmsRole(LMSRole lmsRole,
			boolean isLMSAdministrator, boolean isTrainingAdministrator, Long trainingAdministratorId,
			boolean trainingAdmin_isManagesAllOrganizationalGroups, List<OrganizationalGroup> managedGroups, Long customerId, Long userId, 
			String sortBy, int sortDirection);

//	public Map<Object, Object> getAllUsersNotInLmsRole(LMSRole lmsRole,
//			VU360User loggedInUser, String sortBy, int sortDirection);
//
//	public Map<Object, Object> getAllUsersNotInLmsRole(LMSRole lmsRole,
//			String firstName, String lastName, String email,
//			VU360User loggedInUser, int pageIndex, int pageSize, String sortBy,
//			int sortDirection);// advanch
//
//	public Map<Object, Object> getAllUsersNotInLmsRole(LMSRole lmsRole,
//			String searchCriteria, VU360User loggedInUser, int pageIndex,
//			int pageSize, String sortBy, int sortDirection);// simple

//	public Map<Object, Object> getAllMembersByRole(LMSRole lmsRole);

	public void assignUserToRole(long userIdArray[], LMSRole lmsRole, String manageAll, List<OrganizationalGroup> orgGroupsList);

	public void assignUserToRole(long userIdArray[], LMSRole lmsRole);
	public void assignUserToRole(VU360User user, LMSRole lmsRole);

//	public void assignUserToRole(long userIdArray[], LMSRole lmsRole);
	public void assignUserToRole(Map<Long, Long> userIdContentOwnerMap, LMSRole lmsRole);
	
	public void unAssignUserFromRole(long userIdArray[], LMSRole lmsRole);

//	public List<RegistrationInvitation> getAllRegistrationInvitation(Customer customer);

	public LearnerPreferences saveLearnerPreferences(LearnerPreferences learnerPreferences);
	
	public List<RegistrationInvitation> getRegistrationInvitationByName(Customer customer, String invitationName,VU360User loggedinUser);
	
//	public List<RegistrationInvitation> getRegistrationInvitationByName(Customer customer, String invitationName);
	public List<CreditReportingField> getCreditReportingFieldsByLearner(Learner learner);
	public List<CreditReportingField> getCreditReportingFieldsByLearnerCourseApproval(Learner learner);
	public CreditReportingFieldValue saveCreditReportingfieldValue(CreditReportingFieldValue creditReportingfieldValue);
	public List<CreditReportingFieldValueChoice>   getChoicesByCreditReportingField(CreditReportingField creditReportingField);
	public List<CreditReportingFieldValue> getCreditReportingFieldValues(Learner learner);
	public CreditReportingFieldValue getCreditReportingFieldValueById(long id);
	public LearnerProfile updateLearnerProfile(LearnerProfile learnerProfile);
	
	public List<TimeZone> getTimeZoneList();
	public TimeZone getTimeZoneById(long id);
	public TimeZone getTimeZoneByLearnerProfileId(long learnerProfileId);
	
	public Map<Object,Object> findAllLearnersOfCustomersOfReseller(String firstName, String lastName,
			String email, int pageIndex, int pageSize,
			String sortBy, int sortDirection, String enrollmentMethod);
		
	public List<Learner> getAllLearnersOfCustomer(Customer customer);

	public List<Learner> getLearnersOfCustomersOfSelectedReseller(Distributor distributor);
	public List<Learner> setupLearnerForUsers(List<String> userGuidList,String contentOwnerGUID);
	
	// For VCS - Discussion Forum Integration
	public String registerLearnerToVCS(DiscussionForumCourse dfCourse, Learner learner);	
	public String getLearnerAvatarNameFromVCS(Learner learner);
	public LearnerPreferences saveLearnerPreferencesToVCS(Learner learner, LearnerPreferences learnerPreferences, int avatarWidth, int avatarHeight);	
	public Learner loadForUpdateLearner(Long id);
	public RegistrationInvitation loadForUpdateRegistrationInvitation(long id);
	public  OrganizationalGroup loadForUpdateOrganizationalGroup(long id);
	public  LearnerGroup  loadForUpdateLearnerGroup(long id);
	public LMSRole   loadForUpdateLMSRole(long id);
	public  CreditReportingFieldValue  loadForUpdateCreditReportingFieldValue(long id);
//	public ContentOwner   loadForUpdateContentOwner(long id);
	//public OrganizationalGroup loadForUpdateLearnerByOrgGroup(long id);
	public void deleteLearnerFromRole(Learner learner);
	public List<LearnerGroupItem> getLearnerGroupItemsByLearnerGroupId(Long learnerGroupId);
	public void saveLearnerGroupItems(Set<LearnerGroupItem> learnerGroupItems);
	public void saveLearnerGroupItem(LearnerGroupItem learnerGroupItem);
	
	//[9/27/2010] LMS-7219 :: Learner Mode > Login: Force User to Change Password on Next Login
	public boolean isCorrectPassword ( com.softech.vu360.lms.vo.VU360User user, String password );
	public VU360User updateUser (VU360User vu360User, boolean notifyUserByEmail, String loginURL, Brander brander, VelocityEngine velocityEngine);
	public LearnerProfile loadForUpdateLearnerProfile(long id);
	public void unAssignUsersFromAllRolesOfType(String userIdArray[],String roleType);
	
	public LMSRole getRoleForProctorByCustomer(Customer customer);
	public Object[] addRegistrationInvitation(RegistrationInvitation regInvitation);
	public RegistrationInvitation loadForPreviewRegistrationInvitation(long id);
	public LearnerGroup loadForDisplayLearnerGroup(long id);
	public boolean hasAnyInProgressEnrollmentOfStandardValidationQuestions(long learnerId);
	public LearnerValidationQASetDTO getLearnerValidationQuestions(long id);
	public void saveLearnerValidationAnswers(LearnerValidationQASetDTO qaDTO, Learner learner);
	public Learner addNewLearnerGivenCustomer(Customer customer, Hashtable<String, Object> leanerDetailInHashmap)  throws Exception;
	public Map<Object,Object> getLearnerUniqueQuestions(long learnerId);
	public LearnerValidationAnswers getLearnerUniqueQuestionsAnswersByQuestion(long questionId);
	public void saveLearnerUniquesValidationQuestions(LearnerValidationAnswers answer);
	public LearnerValidationAnswers loadForUpdateLearnerValidationAnswers(long answer);
	public LearnerValidationAnswers updateLearnerValidationAnswers(LearnerValidationAnswers lva ); 
	
	public Address findAddressById(Long id);
	public Address updateAddress(Address address);
	
	public Learner getLearnerByVU360UserId(VU360User user);
	public List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId);
}