package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.controller.model.AddDistributorForm;
import com.softech.vu360.lms.web.controller.model.EditDistributorForm;

public interface DistributorService {
	public List<Distributor> findDistributorsByName(String name, VU360User loggedInUser, boolean isExact, int pageIndex,
			int retrieveRowCount, ResultSet resultSet, String sortBy, int sortDirection);

	public DistributorGroup addDistributorGroup(DistributorGroup distributorGroup);

	public DistributorGroup getDistributorGroupById(long distributorGroupId);

	public DistributorGroup saveDistributorGroup(DistributorGroup distributorGroup);
	public Distributor addDistributor(com.softech.vu360.lms.vo.VU360User loggedInUser, AddDistributorForm addDistributorForm);
	public Distributor addDistributorForSelfService(Long userId, AddDistributorForm addDistributorForm);

	public Map<Object, Object> findDistributorsByName(String distributorName, VU360User loggedInUser, int pageIndex,
			int pageSize, String sortBy, int sortDirection);

	public Distributor getDistributorById(long distributorId);

	public List<DistributorGroup> findDistributorGroupsByName(String name,com.softech.vu360.lms.vo.VU360User loggedInUser,boolean isExact);
	public List<Distributor> getAllowedDistributorByGroupId(String groupId,String administratorId);
	public void deleteDistributorByGroupIdAndAdministratorId(String groupId, String administratorId, String distributorIdArray);
	public void addDistributorWithGroupIdAndAdministratorId(LMSAdministratorAllowedDistributor allowedDistributor);

	public void deleteDistributorGroups(Long distributorGroupIdArray[]);

	public void assignDistributorsInDistributorGroup(Long distributorGroupId, Long distributorIdArray[]);

	public List<DistributorGroup> getDistributorGroupsBydistributorId(long Id);

	public List<Distributor> getDistributorByDistributorCode(String distributorCode);

	public Boolean isDistributCodeMappedToMoreThenOneDistributor(String distributorCode,
			Long exceptSelectedDistributor);

	public Distributor saveDistributor(Distributor distributor);
	public Distributor updateDistributor(EditDistributorForm distributor, Distributor authDistributor, com.softech.vu360.lms.vo.VU360User loggedInUser);
	
	public Distributor findDistibutorByDistributorCode(String distributorCode);

	public DistributorPreferences getDistributorPreferencesById(long distributorPreferencesId);

	public DistributorPreferences saveDistributorPreferences(DistributorPreferences distributorPreferences);

	public List<VU360User> getLearnersByCustomer(String firstName, String lastName, String email, String searchCriteria,
			Long customerId, int pageIndex, int retrieveRowCount, ResultSet resultSet, String sortBy,
			int sortDirection);

	public List<VU360User> getLearnersByDistributor(String firstName, String lastName, String email,
			String searchCriteria, Long distributorId, int pageIndex, int retrieveRowCount, ResultSet resultSet,
			String sortBy, int sortDirection);

	public List<VU360User> getAllLearners(String firstName, String lastName, String email, String searchCriteria,
			VU360User loggedInUser, int pageIndex, int retrieveRowCount, ResultSet resultSet, String sortBy,
			int sortDirection);

	public List<VU360User> getAllLearners(String firstName, String lastName, String email, String searchCriteria);

	public List<LMSAdministrator> getLMSAdministratorsByDistributorGroupId(long distributorGroupId);

	public void assignLMSAdministratorsToDistributorGroup(Long[] lmsAdministratorIdArray, Long distributorGroupId,
			String[] allowResellers);

	public List<LMSAdministrator> unassignLMSAdministratorsToDistributorGroup(Long[] lmsAdministratorIdArray,
			Long distributorGroupId);

	public List<LMSAdministrator> findLMSAdministrators(String firstName, String lastName, String email,
			VU360User loggedInUser);

	public List<DistributorLMSFeature> getDefaultEnablePermissions(Distributor distributor, String roleType);

	List<DistributorLMSFeature> getPermissions(Distributor distributor, String roleType);

	boolean updateFeaturesForDistributor(Distributor distributor, List<LMSRoleLMSFeature> lmsPermissions);

	DistributorGroup loadForUpdateDistributorGroup(long distributorGroupId);

	Distributor loadForUpdateDistributor(long distributorId);

	DistributorPreferences loadForUpdateDistributorPreferences(long distributorPreferencesId);

	Distributor updateDistributor(Distributor distributor);

	DistributorLMSFeature loadForUpdateDistributorPermission(long distributorPermissionId);

	List<Distributor> findResellersWithCustomFields(VU360User loggedInUser);

	List<String> getRestrictedFeatures();

	Address findAddressById(Long id);

	List<DistributorGroup> findDistributorGroupsByLMSAdministratorId(Long lmsAdministratorId);
}
