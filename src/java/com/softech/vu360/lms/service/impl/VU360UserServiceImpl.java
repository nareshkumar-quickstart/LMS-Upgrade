package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LearnerProfileStaticField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.LMSFeatureRepository;
import com.softech.vu360.lms.repositories.LMSRoleRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupRepository;
import com.softech.vu360.lms.repositories.TrainingAdministratorRepository;
import com.softech.vu360.lms.repositories.VU360UserRepository;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author jason
 *
 */
public class VU360UserServiceImpl implements VU360UserService {
	
	private static final Logger log = Logger.getLogger(VU360UserServiceImpl.class.getName());
	
	@Inject
	private VU360UserRepository vu360UserRepository;
	@Inject
	private LMSRoleRepository lmsRoleRepository;
	@Inject
	private LMSFeatureRepository lmsFeatureRepository;
	@Inject
	private TrainingAdministratorRepository trainingAdminstratorRepository;
	@Inject
	private OrganizationalGroupRepository organizationalGroupRepository;
	
	private PasswordEncoder passwordEncoder = null;
	private SaltSource saltSource = null;
	private ActiveDirectoryService activeDirectoryService = null;	
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}
	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		log.debug("called authentication:"+username);
		VU360User user=getUserByUsernameAndDomain(username, null);
		if(user==null)
			throw new UsernameNotFoundException("No user detail found from DB Bad credentials");
		user.setLmsRoles(user.getLmsRoles());
				
		log.debug("isEnabled:"+user.getEnabled());
		log.debug("isCredentialNonExpired:"+user.isCredentialsNonExpired());
		log.debug("isAccountNonLocked:"+user.getAccountNonLocked());
		return ProxyVOHelper.setUserProxy(user);
	}
	
	@Override
	public void deleteLMSTrainingAdministrator(TrainingAdministrator trainingAdministrator){
		vu360UserRepository.deleteLMSTrainingAdministrator(trainingAdministrator);
	}
	
	@Override
	public VU360User getUserByUsernameAndDomain(String username,String domain) {
		List<VU360User> results = vu360UserRepository.findUserByUsernameAndDomain(username , domain);
		if ( !results.isEmpty() ) {
			VU360User user = results.get(0);
			checkForPermission(user);
			return user;
		}
		
		throw new UsernameNotFoundException("FOUND NO SUCH USER NAME");
	}
	
	@Override
	public List<VU360User> getUserByEmailFirstNameLastName(String email,String firstName, String lastName){
		List<VU360User> results = vu360UserRepository.getUserByEmailFirstNameLastName(email, firstName , lastName);
		List<VU360User> vU360UserList = new ArrayList<VU360User>();
		
		for(VU360User vU360User:results) {
			if(vU360User.getLmsRoles().isEmpty()) {
				log.debug("Username :"+vU360User.getName()+" Roles not found");
				continue;
			}
			
			boolean hasPermission = false;
			for( LMSRole role : vU360User.getLmsRoles() ) {
				if(vu360UserRepository.hasAtLeastOnePermssionOfRoleEnabled(vU360User.getId(), role.getId()) ) {
					hasPermission = true;
					break;
				}
			}
			if( !hasPermission ) {
				log.debug("Username : "+vU360User.getName()+" Don not have any permission");
			}
			vU360UserList.add(vU360User);
		}
		return vU360UserList;
	}
	
	@Override
	public List<VU360User> getActiveUserByUsername(String username){
		List<VU360User> results = vu360UserRepository.getActiveUserByUsername(username);
		List<VU360User> vU360UserList = new ArrayList<VU360User>();
		
		for(VU360User vU360User:results) {
			if(vU360User.getLmsRoles().isEmpty()) {
				log.debug("Username :"+vU360User.getName()+" Roles not found");
				continue;
			}
			
			boolean hasPermission = false;
			for( LMSRole role : vU360User.getLmsRoles() ) {
				if(vu360UserRepository.hasAtLeastOnePermssionOfRoleEnabled(vU360User.getId(), role.getId())) {
					hasPermission = true;
					break;
				}
			}
			if( !hasPermission ) {
				log.debug("Username : "+vU360User.getName()+" Don not have any permission");
			}
			vU360UserList.add(vU360User);
		}
		return vU360UserList;
	}
	
	@Override
	public  LMSRole  loadForUpdateLMSRole(long id){
		return lmsRoleRepository.findOne(id);
	}
	
	@Override
	public  VU360User  loadForUpdateVU360User(Long id){
		return vu360UserRepository.findOne(id);
	}

	@Override
	public List<VU360User> getUserByFirstNameAndLastName(Customer cust, String firstName, String lastName) {
		return vu360UserRepository.getUserByFirstNameAndLastName(cust, firstName, lastName);
	}
	
	@Override
	public VU360User findUserByUserName(String username){
		return vu360UserRepository.findUserByUserName(username);
	}
	
	@Override
	public VU360User getUserById(Long id) {
		return vu360UserRepository.getUserById(id);
	}
	
	@Override
	public List<VU360User> getUsersByEmailAddress(String emailAddress) {
		return vu360UserRepository.findUserByEmailAddress(emailAddress);
	}
	
	@Override
	public boolean isEmailAddressInUse(String emailAddress) {
		List<VU360User> results = vu360UserRepository.findUserByEmailAddress(emailAddress);
		if ( results != null && results.size() > 0 ) {
			return true;
		}
		return false;
	}
	
	public VU360User changeUserPassword(long id, VU360User updatedUser) { 
		String newPassword = updatedUser.getPassword();
		//VU360User currUser = this.getUserById(id);
		com.softech.vu360.lms.vo.VU360User voUser = ProxyVOHelper.setUserProxy(updatedUser);
		Object salt = saltSource.getSalt(voUser);
		String encodedPassword = passwordEncoder.encodePassword(newPassword, salt);
		//log.debug("Given Password = "+newPassword+ "| Encoded Password = "+encodedPassword);
		//currUser.setPassword(updatedUser.getPassword());
		updatedUser.setPassword(encodedPassword);
		//LMS-9318
		updatedUser.setChangePasswordOnLogin(true);
		
		VU360User updatedUser2 = vu360UserRepository.updateUser(updatedUser);
		
		if (updatedUser != null && activeDirectoryService.isADIntegrationEnabled()){//if success and AD is enabled
			updatedUser.setPassWordChanged(true);
			updatedUser.setPassword(newPassword);// we require plain password in AD
			activeDirectoryService.updateUser(updatedUser);//edit user to AD
			updatedUser.setPassword(encodedPassword);// reset the password to encrypted
		}	
		
		return updatedUser2;
		//return vu360UserRepository.saveUser(currUser);
	}
	
	public VU360User updateUser(long id, VU360User updatedUser) {
		return vu360UserRepository.saveUser(updatedUser);
	}
	
	public VU360User updateUser(VU360User updatedUser) {
		return vu360UserRepository.updateUser(updatedUser);
	}
	
	public VU360User updateNumLogons(VU360User updatedUser){
		return vu360UserRepository.updateUser(updatedUser);
	}
	
	public void deleteUserRole(Long roleIdArray[]){
		List<LMSRole> roles=lmsRoleRepository.findByIdIn(roleIdArray);
		lmsRoleRepository.delete(roles);
		
	}
	
	public List<LMSRole> getLMSRolesByUserById(long id){
		return lmsRoleRepository.getLMSRolesByUserById(id);
	}
	
	public LMSRole  getDefaultRole(Customer customer){
		return lmsRoleRepository.getDefaultRole(customer);
		
	}
	
	public LMSRole getOptimizedBatchImportLearnerDefaultRole(Customer customer){
		return lmsRoleRepository.getOptimizedBatchImportLearnerDefaultRole(customer);
		
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
	
	public List<LMSRole>  getRolesByRoleType(String roleType,Long customerId){
		return lmsRoleRepository.findByRoleTypeAndOwnerId(roleType,customerId);
	}
	
	public List<LMSRole>  getAllRoles(Customer customer,VU360User loggedInUser){
		return lmsRoleRepository.getAllRoles(customer,loggedInUser);
	}
	
	public List<LMSFeature>  getFeaturesByRoleType(String roleType){
		return lmsFeatureRepository.findByRoleType(roleType);
	}
	
	public LMSRole  getRoleByName(String roleName,Customer customer){
		return lmsRoleRepository.findByRoleNameAndOwner(roleName, customer);
	}
	
	public int checkNoOfBefaultReg(Customer customer){
		return 0;//vu360UserRepository.checkNoOfBefaultReg(customer);
	}
	
	/*
	 * This service is responsible for adding following entities 
	 * VU360 User
	 * LMSAdministrator (optional)
	 * TrainingAdministrator (optional)
	 * Learner (optional)
	 */
	public VU360User addUser(VU360User user) {
		VU360User addedUser = vu360UserRepository.saveUser(user);
		if (addedUser!=null && activeDirectoryService.isADIntegrationEnabled()){//if success and AD is enabled
			activeDirectoryService.addUser(user);//add user to AD
		}			
		return addedUser;
	}
	
	public VU360User getUserByGUID(String userGUID) {
		VU360User myUser=null;
		try{
			myUser = vu360UserRepository.findFirstByUserGUID(userGUID);
	}
		catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("user with userGUID: " + userGUID + "is not found");
			}
		}
		return myUser;
	}
	
	
	public List<LMSRole> getSystemRolesByCustomer(Customer customer){
		return lmsRoleRepository.getSystemRolesByCustomer(customer);
	}
	
   public List<LMSRole> findRolesByName(String name, Customer customer, VU360User loggedInUser) {
        return lmsRoleRepository.findRolesByName(name, customer, loggedInUser);
    }

	@SuppressWarnings("unchecked")
	public Map<Object,Object> findUsers(String firstName,String lastName,String email,VU360User loggedInUser,
			int pageIndex,int pageSize,String sortBy,int sortDirection) {
		
		Map<Object,Object> results = new HashMap<Object,Object>();
		results = vu360UserRepository.findUsers(firstName,lastName,email,loggedInUser, pageIndex, pageSize, sortBy, sortDirection);
		return results;
	}
 
	public Map<Object,Object> findAllLearners(String firstName,String lastName,String email,VU360User loggedInUser,String sortBy,int sortDirection){
		Map<Object,Object> results = new HashMap<Object,Object>();
		results = vu360UserRepository.findAllLearners(firstName,lastName,email,loggedInUser,  sortBy, sortDirection);
		return results;
	}	
	
	public List<VU360User> searchCustomerUsers(Customer customer, String firstName, String lastName,
			String email, String sortBy, int sortDirection) {
		return vu360UserRepository.searchCustomerUsers(customer, firstName, lastName, email, sortBy, sortDirection);
	}
	
	public Map<Object,Object> searchCustomerUsers(Customer customer, String firstName, String lastName,
			String email, int pageIndex, int pageSize,
			String sortBy, int sortDirection) {
		Map<Object,Object> results = new HashMap<Object,Object>();
		
		results = vu360UserRepository.searchCustomerUsers(customer, firstName, lastName, email, pageIndex, pageSize, sortBy, sortDirection);
		
		return results;
	}
	
	public String getValueForStaticReportingField(VU360User vu360User, String reportingField){
		String returnValue= null;
		if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.FIRST_NAME.toString())){
			returnValue= vu360User.getFirstName();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.MIDDLE_NAME.toString())){
			returnValue= vu360User.getMiddleName();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.LAST_NAME.toString())){
			returnValue= vu360User.getLastName();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.EMAIL.toString())){
			returnValue= vu360User.getEmailAddress();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.PHONE.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getMobilePhone();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.OFFICE_PHONE.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getOfficePhone();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.OFFICE_EXTN.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getOfficePhoneExtn();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_1_LINE_1.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_1_LINE_2.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress2();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.CITY.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCity();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.STATE.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress().getState();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ZIP_CODE.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress().getZipcode();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.COUNTRY.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCountry();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_2_LINE_1.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getStreetAddress();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_2_LINE_2.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getStreetAddress2();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.CITY_2.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCity();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.STATE_2.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getState();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ZIP_CODE_2.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getZipcode();
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.COUNTRY_2.toString())){
			returnValue= vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCountry();			
		}			
		if(returnValue!=null && returnValue.trim().length()== 0 ){
			returnValue= null;
	    }
	
	 return returnValue; 
	
	}
	
	public void setValueForStaticReportingField(VU360User vu360User, String reportingField,String value){
		
		if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.FIRST_NAME.toString())){
			 vu360User.setFirstName(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.MIDDLE_NAME.toString())){
			 vu360User.setMiddleName(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.LAST_NAME.toString())){
			 vu360User.setLastName(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.EMAIL.toString())){
			 vu360User.setEmailAddress(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.PHONE.toString())){
			vu360User.getLearner().getLearnerProfile().setMobilePhone(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.OFFICE_PHONE.toString())){
			vu360User.getLearner().getLearnerProfile().setOfficePhone(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.OFFICE_EXTN.toString())){
			vu360User.getLearner().getLearnerProfile().setOfficePhoneExtn(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_1_LINE_1.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_1_LINE_2.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setStreetAddress2(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.CITY.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCity(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.STATE.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setState(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ZIP_CODE.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setZipcode(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.COUNTRY.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress().setCountry(value);			
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_2_LINE_1.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_2_LINE_2.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setStreetAddress2(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.CITY_2.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCity(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.STATE_2.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setState(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.ZIP_CODE_2.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setZipcode(value);
		}else if(reportingField.trim().equalsIgnoreCase(LearnerProfileStaticField.COUNTRY_2.toString())){
			vu360User.getLearner().getLearnerProfile().getLearnerAddress2().setCountry(value);			
		}		
		
	}
	
	/**
	 * Called when it is required for new/re-initialize authentication details
	 * @param {@link javax.servlet.http.HttpServletRequest}
	 * @return {@link VU360UserAuthenticationDetails}
	 * @author muhammad.javed
	 */
		
	public LMSRole getDefaultSystemRole(Customer customer) throws Exception {
		
		LMSRole systemRole = getDefaultRole(customer);
		if(systemRole == null) {
			List<LMSRole> systemRoles = getSystemRolesByCustomer(customer);
			for(LMSRole role:systemRoles){

				if(role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER))
				{
					systemRole=role;
					break;
				}
			}
		}
		return systemRole;
	}
	/*@Override
	public Object buildDetails(Object context) {
		HttpServletRequest request = (HttpServletRequest) context;
		//return new VU360UserAuthenticationDetails(request);
		return null;
	}*/
	@Override
	public TrainingAdministrator findTrainingAdminstratorById(Long id) {
		return trainingAdminstratorRepository.findOne(id);
	}
	
	/** Added By Marium Saud
	 * Method added for Validating Email Address field while adding Instructor .
	 * New Method is added because previously all users for the given email Address has been fetched that results in number of queries which cause 'Server Error'.
	 * Inorder toi avoid number of queries execution the validation is now performed by count Spring Data Marker method.
	 */
	
	@Override
	public int countUserByEmailAddress(String emailAddress) {
		return vu360UserRepository.countByEmailAddress(emailAddress);
	}
	
	@Override
	public List<OrganizationalGroup> findAllManagedGroupsByTrainingAdministratorId(Long trainingAdminstratorId) {
		return organizationalGroupRepository.findAllManagedGroupsByTrainingAdministratorId(trainingAdminstratorId);
	}

	@Override
	public void checkForPermission(VU360User user) {
		/*
		 * if the user has no roles, it will show the error message
		 * "Invalid username or password" as if user had typed in the wrong
		 * password.
		 */
		if (!vu360UserRepository.hasAnyRoleAssigned(user.getId())) {
			throw new UsernameNotFoundException(
					"FOUND NO ROLES ASSIGNED TO USER");
		} else {
			if (!vu360UserRepository.hasAtLeastOnePermssionOfAnyRoleEnabled(user.getId())) {
				throw new UsernameNotFoundException(
						"FOUND NO PERMISSIONS ASSIGNED TO USER");
			}
		}
	}
	
	@Override
	public boolean hasLearnerRole(VU360User user) {
		return vu360UserRepository.hasLearnerRole(user.getId());
	}
	
	@Override
	public boolean hasProctorRole(VU360User user) {
		return vu360UserRepository.hasProctorRole(user.getId());
	}
	
	@Override
	public boolean hasAdministratorRole(VU360User user) {
		return vu360UserRepository.hasAdministratorRole(user.getId());
	}
	
	@Override
	public boolean hasTrainingAdministratorRole(VU360User user) {
		return vu360UserRepository.hasTrainingAdministratorRole(user.getId());
	}
	
	@Override
	public boolean hasRegulatoryAnalystRole(VU360User user) {
		return vu360UserRepository.hasRegulatoryAnalystRole(user.getId());
	}
	
	@Override
	public boolean hasInstructorRole(VU360User user) {
		return vu360UserRepository.hasInstructorRole(user.getId());
	}
	
	@Override
	public boolean hasAccessToFeatureGroup(Long userId, String featureGroup) {
		return lmsFeatureRepository.hasAccessToFeatureGroup(userId, featureGroup);
	}
	
	@Override
	public boolean hasAccessToFeatureCode(Long userId, String featureCode) {
		return lmsFeatureRepository.hasAccessToFeatureCode(userId, featureCode);
	}
	
	@Override
	public boolean hasAccessToFeatureGroup(Long userId, Long roleId, String featureGroup) {
		return lmsFeatureRepository.hasAccessToFeatureGroup(userId, featureGroup);
	}
	
	@Override
	public List<String> getEnabledFeatureGroups(Long userId, Long roleId) {
		return lmsFeatureRepository.getEnabledFeatureGroups(userId, roleId);
	}
	
	@Override
	public List<String> getEnabledFeatureGroups(Long userId) {
		return lmsFeatureRepository.getEnabledFeatureGroups(userId);
	}
	
	@Override
	public boolean hasAccessToFeatureCode(Long userId, Long roleId, String featureCode) {
		return lmsFeatureRepository.hasAccessToFeatureCode(userId, featureCode);
	}

}