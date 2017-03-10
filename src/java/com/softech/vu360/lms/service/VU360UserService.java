/**
 * 
 */
package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.VU360UserNew;

/**
 * @author jason
 *
 */
public interface VU360UserService extends UserDetailsService {
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;
	public void deleteLMSTrainingAdministrator(TrainingAdministrator trainingAdministrator);
	public VU360User findUserByUserName(String username); 
	
	public boolean isEmailAddressInUse(String emailAddress);
	
	public VU360User getUserById(Long id);

	public VU360UserNew getSimpleUserById(Long id);
	
	public List<VU360User> getUsersByEmailAddress(String emailAddress);
	
	public VU360User getUserByUsernameAndDomain(String username, String domain);
	
	public List<VU360User> getUserByEmailFirstNameLastName(String email,String firstName, String lastName);
	
	public List<VU360User> getActiveUserByUsername(String username);
	
	public VU360User changeUserPassword(long id, VU360User updatedUser);
	
	public List<LMSRole>  getRolesByRoleType(String roleType,Long customerId);
	
	public List<LMSRole>  getAllRoles(Customer customer,com.softech.vu360.lms.vo.VU360User loggedInUser);
	
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
	public VU360UserNew updateUser(VU360UserNew updatedUser);
	public VU360User updateUser(VU360User updatedUser);
	public List<LMSRole> getSystemRolesByCustomer(Customer customer);
	public List<LMSRole> findRolesByName( String name, Customer customer, VU360User loggedInUser);
	public Map<Object,Object> findUsers(String firstName,String lastName,String email,VU360User loggedInUser,int pageIndex,int pageSize,String sortBy,int sortDirection);
	public Map<Object, Object> findAllLearners(String firstName,String lastName,String email,VU360User loggedInUser, String sortBy, int sortDirection);
	public VU360User getUpdatedUserById(Long id);
	public VU360UserNew getVU360UserNewById(Long id);
	public Map<Object,Object> searchCustomerUsers(Customer customer, String firstName, String lastName,
			String email, int pageIndex, int pageSize,
			String sortBy, int sortDirection);
	public List<VU360User> searchCustomerUsers(Customer customer, String firstName, String lastName,
			String email, String sortBy, int sortDirection);
	public  LMSRole  loadForUpdateLMSRole(long id);
	public  VU360User  loadForUpdateVU360User(Long id);
	public  VU360UserNew  loadForUpdateVU360UserNew(Long id);
	public String getValueForStaticReportingField(VU360User vu360User, String reportingField);
	public void setValueForStaticReportingField(VU360User vu360User, String reportingField,String value);
	public VU360User updateNumLogons(VU360User updatedUser);
	public VU360UserNew updateNumLogons(VU360UserNew updatedUser);
	public LMSRole getDefaultSystemRole(Customer customer) throws Exception;
	public TrainingAdministrator findTrainingAdminstratorById(Long id);
	//Added By Marium Saud
	public int countUserByEmailAddress(String emailAddress);
	public List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId);
	
	public Learner getLearnerByVU360UserId(Long id);

}
