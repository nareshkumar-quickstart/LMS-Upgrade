/**
 * 
 */
package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;

/**
 * @author jason
 *
 */
public interface VU360UserService extends UserDetailsService {
	
	public void deleteLMSTrainingAdministrator(TrainingAdministrator trainingAdministrator);
	public VU360User findUserByUserName(String username);
	public VU360User findByIdForBatchImport(Long userId);
	public VU360User loadUserForBatchImport(Long userId);
	public boolean isEmailAddressInUse(String emailAddress);
	
	public VU360User getUserById(Long id);

	public List<Long> findLearnerIdsByVu360UserIn(List<VU360User> vu360Users);
	
	public List<VU360User> getUsersByEmailAddress(String emailAddress);
	
	public VU360User getUserByUsernameAndDomain(String username, String domain);
	
	public List<VU360User> getUserByEmailFirstNameLastName(String email,String firstName, String lastName);
	
	public List<VU360User> getActiveUserByUsername(String username);
	
	public VU360User changeUserPassword(long id, VU360User updatedUser);
	
	public List<LMSRole>  getRolesByRoleType(String roleType,Long customerId);
	
	public List<LMSRole>  getAllRoles(Customer customer,VU360User loggedInUser);
	
	public void deleteUserRole(Long roleIdArray[]);
	
	public List<LMSFeature>  getFeaturesByRoleType(String roleType);
	
	public List<LMSRole> getLMSRolesByUserById(long id);
	
	public LMSRole  getRoleByName(String roleName,Customer customer);
	
	public LMSRole  getDefaultRole(Customer customer);
	
	public LMSRole  getOptimizedBatchImportLearnerDefaultRole(Customer customer);
	
	public int checkNoOfBefaultReg(Customer customer);
	public VU360User getUserByGUID(String userGUID);
	public List<VU360User> getUserByFirstNameAndLastName(Customer cust, String firstName, String lastName);
	
	public VU360User addUser(VU360User user);
	public VU360User updateUser(long id, VU360User updatedUser);
	public VU360User updateUser(VU360User updatedUser);
	public List<LMSRole> getSystemRolesByCustomer(Customer customer);
	public List<LMSRole> findRolesByName( String name, Customer customer, VU360User loggedInUser);
	public Map<Object,Object> findUsers(String firstName,String lastName,String email,VU360User loggedInUser,int pageIndex,int pageSize,String sortBy,int sortDirection);
	public Map<Object, Object> findAllLearners(String firstName,String lastName,String email,VU360User loggedInUser, String sortBy, int sortDirection);
	public Map<Object,Object> searchCustomerUsers(Customer customer, String firstName, String lastName,
			String email, int pageIndex, int pageSize,
			String sortBy, int sortDirection);
	public List<VU360User> searchCustomerUsers(Customer customer, String firstName, String lastName,
			String email, String sortBy, int sortDirection);
	public  LMSRole  loadForUpdateLMSRole(long id);
	public  VU360User  loadForUpdateVU360User(Long id);
	public String getValueForStaticReportingField(VU360User vu360User, String reportingField);
	public void setValueForStaticReportingField(VU360User vu360User, String reportingField,String value);
	public LMSRole getDefaultSystemRole(Customer customer) throws Exception;
	public TrainingAdministrator findTrainingAdminstratorById(Long id);
	//Added By Marium Saud
	public int countUserByEmailAddress(String emailAddress);
	public List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId);
	void checkForPermission(VU360User user);
	boolean hasLearnerRole(VU360User user);
	boolean hasProctorRole(VU360User user);
	boolean hasAdministratorRole(VU360User user);
	boolean hasTrainingAdministratorRole(VU360User user);
	boolean hasRegulatoryAnalystRole(VU360User user);
	boolean hasInstructorRole(VU360User user);
	boolean hasAccessToFeatureGroup(Long userId, String featureGroup);
	boolean hasAccessToFeatureCode(Long userId, String featureCode);
	boolean hasAccessToFeatureGroup(Long userId, Long roleId, String featureGroup);
	boolean hasAccessToFeatureCode(Long userId, Long roleId, String featureCode);
	List<String> getEnabledFeatureGroups(Long userId, Long roleId);
	List<String> getEnabledFeatureGroups(Long userId);
	public void updateNumLogons(com.softech.vu360.lms.vo.VU360User userVO);
	public LMSRole getTop1RoleByName(String roleName,Customer customer);
	public List<VU360User> findTrainingAdministratorsOfUser(Long userId);
}
