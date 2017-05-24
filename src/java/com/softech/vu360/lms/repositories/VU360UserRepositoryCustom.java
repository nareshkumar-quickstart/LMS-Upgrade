package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.VU360UserAudit;

public interface VU360UserRepositoryCustom {

	public List<VU360User> getUserByFirstNameAndLastName(Customer cust,String firstName, String lastName);

	public List<VU360User> getActiveUserByUsername(String username);

	public List<VU360User> findUserByEmailAddress(String emailAddress);

	public VU360User findUserByUserName(String username);

	public VU360User findByIdForBatchImport(Long userId);
	public VU360User loadUserForBatchImport(Long userId);

	public VU360User saveUser(VU360User user);

	public VU360User updateUser(VU360User updatedUser);

	public List<VU360User> getUserByIds(String[] idl);

	public List<VU360User> getUserByEmailFirstNameLastName(String email,String firstName, String lastName);

	public List<VU360User> getListOfUsersByGUID(List<String> guids);

	public VU360User getUserByGUID(String userGUID);

	public Map<Object, Object> findUsers(String firstName, String lastName, String email,
			VU360User loggedInUser, int pageIndex, int pageSize, String sortBy,	int sortDirection);

	public Map<Object, Object> findAllLearners(String firstName,
			String lastName, String email, VU360User loggedInUser,
			String sortBy, int sortDirection);

	public VU360User deleteLMSAdministrator(VU360User user);

	public void deleteLMSTrainingAdministrator(
			TrainingAdministrator trainingAdministrator);

	public VU360User deleteLMSTrainingManager(VU360User user);

	public Map<Object, Object> searchCustomerUsers(Customer customer,
			String firstName, String lastName, String email, int pageIndex,
			int pageSize, String sortBy, int sortDirection);

	public List<VU360User> searchCustomerUsers(Customer customer,
			String firstName, String lastName, String email, String sortBy,
			int sortDirection);

	public VU360UserAudit saveVU360UserAudit(VU360User user, String operation,
			Long modifingUser);
	
	
	public List<VU360User> getAllLearners(String firstName,String lastName,String email,String searchCriteria, VU360User loggedInUser);
	public List<VU360User> findBylearnerIn(Collection<Long> ids);
	public Set<Long> getOrganizational_Group_Members(Long userId,Long customerId);
	
	public List<VU360User> getLearnersByCustomer(String firstName,String lastName,String email,String searchCriteria,Long customerId);
	public List<VU360User> getLearnersByDistributor(String firstName,String lastName,String email,String searchCriteria, Long distributorId);
	public List<VU360User> showAll(LMSRole lmsRole, 
			boolean isLMSAdministrator, boolean trainingAdmin_isManagesAllOrganizationalGroups, Long customerId, Long userId, 
			String searchCriteria, String firstName,String lastName, String email, Long[] idbucket, Boolean isProctorRole, Boolean notLmsRole, Boolean accountNonExpired, Boolean accountNonLocked,
		Boolean enabled, String sortBy, int sortDirection);

	public List<Long> findLearnerIdsByVu360UserIn(List<VU360User> users);

	public void updateNumLogons(com.softech.vu360.lms.vo.VU360User userVO);


	public List<VU360User> findByUsernameInForBatchImport(Collection<String> vList);

	public VU360User saveUserForBatchImport(VU360User user);

	public VU360User loadForUpdateVU360User(Long userId);
}
