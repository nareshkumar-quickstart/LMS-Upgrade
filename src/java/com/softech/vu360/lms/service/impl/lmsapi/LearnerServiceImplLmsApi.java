package com.softech.vu360.lms.service.impl.lmsapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.LearnerServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.VU360UserServiceLmsApi;
import com.softech.vu360.lms.util.LmsApiEmailAsyncTask;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LearnersToBeMailedService;

public class LearnerServiceImplLmsApi implements LearnerServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(LearnerServiceImplLmsApi.class.getName());
	
	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private LearnersToBeMailedService learnersToBeMailedService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private VU360UserServiceLmsApi vu360UserServiceLmsApi;
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}

	public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}
	
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	
	public VU360UserServiceLmsApi getVu360UserServiceLmsApi() {
		return vu360UserServiceLmsApi;
	}

	public void setVu360UserServiceLmsApi(
			VU360UserServiceLmsApi vu360UserServiceLmsApi) {
		this.vu360UserServiceLmsApi = vu360UserServiceLmsApi;
	}

	public Map<String, Object> getLearnersMap(List<String> userNameList, String customerCode) throws Exception {
		
		log.debug("getValidLearners() start");
		Map<String, Object> learnersMap = new HashMap<String, Object>();
		List<Learner> validLearnersList = new ArrayList<Learner>();
		Map<String, String> invalidLearnersMap = new LinkedHashMap<String, String>();
		
		for (String userName : userNameList) {
			try {
				Learner learner = getValidLearner(userName, customerCode);
				validLearnersList.add(learner);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				invalidLearnersMap.put(userName, errorMessage);
			}
		}
		
		if (!validLearnersList.isEmpty()) {
			learnersMap.put("validLearnersList", validLearnersList);
		}
		
		if (!invalidLearnersMap.isEmpty()) {
			learnersMap.put("invalidLearnersMap", invalidLearnersMap);
		}
		
		return learnersMap;
		
	}

	private Learner getValidLearner(String userName, String customerCode) throws Exception {
		
		log.debug("getValidLearner() start");
		
		String errorMessage = null;
		
		if (StringUtils.isEmpty(userName) && StringUtils.isBlank(userName)) {
			errorMessage =  "UserId can not be empty or blank" ;
			throwException(errorMessage);
		}
				
		VU360User vu360User = vu360UserService.findUserByUserName(userName);
		if (vu360User == null) {
			errorMessage =  userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);
		}
				
		Learner vu360Learner = vu360User.getLearner();
		String learnerCustomerCode = vu360Learner.getCustomer().getCustomerCode();
				
		// Check whether customer has these learners or not
		if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
			errorMessage = "UserId: " + userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);			
		}	 
		
		log.debug("getValidLearner() end");
		
	    return vu360Learner;		
			
	}
	
	public List<Learner> getLearnersList(List<Long> learnerGroupIdList) throws Exception {
		
		Long learnerGroupIdArray[] = new Long[learnerGroupIdList.size()];				
		learnerGroupIdArray = learnerGroupIdList.toArray( learnerGroupIdArray);
		List<Learner> learnersToBeEnrolledList = orgGroupLearnerGroupService.getLearnersByLearnerGroupIds(learnerGroupIdArray);
		if (learnersToBeEnrolledList == null || learnersToBeEnrolledList.isEmpty()) {
			return null;
		}
		
		return learnersToBeEnrolledList;
		
	}
	
	public Map<String, Object> getLearnersBelongToCustomer(List<Learner> learnersList, String customerCode) throws Exception {
		
		Map<String, Object> learnersMap = new LinkedHashMap<String, Object>();
		
		for (Learner learner : learnersList) {
			String learnerCustomerCode = learner.getCustomer().getCustomerCode();
			
			// Check whether customer has these learners or not
			if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
				String userName = learner.getVu360User().getUsername();
				String errorMessage = userName + " not found";
				learnersMap.put(userName, errorMessage);
				learnersList.remove(learner);
			}
		} //end of for()
		
		if (!learnersList.isEmpty()) {
			learnersMap.put("learnersList", learnersList);
		}
		
		return learnersMap;
	}
	
	public Learner getNewLearner(Customer customer, User user) throws Exception {
		
		Learner newLearner = createNewUser(customer, user);;
		return newLearner;
		
	}
	
	private Learner createNewUser(Customer customer, User user) throws Exception {
		
		VU360User newUser = vu360UserServiceLmsApi.getNewUser(customer, user);
		LearnerProfile newLearnerProfile = getNewLearnerProfile(user);
		Learner newLearner = new Learner();
		newLearner.setVu360User(newUser);
		
		newLearnerProfile.setLearner(newLearner);
		
		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);
		
		newUser.setLearner(newLearner);
		
		log.debug("adding learner...");
		newLearner = learnerService.addLearner(newLearner);
		log.debug("learner added.");
		
		return newLearner;
		
	} //end of createNewUser()
	
	private LearnerProfile getNewLearnerProfile(User user) {
		
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		//List<CustomFieldValue> myCustomFieldValues = new ArrayList<CustomFieldValue>();
		
		LearnerProfile newLearnerProfile = new LearnerProfile();
		newLearnerProfile.setMobilePhone(mobilePhone);
		newLearnerProfile.setOfficePhone(phone);
		newLearnerProfile.setOfficePhoneExtn(extension);
		newLearnerProfile.setCustomFieldValues(null);
		
		if (address1 != null) {
			Address newAddress = getNewAddress(address1);
			newLearnerProfile.setLearnerAddress(newAddress);
		} else {
			address1 = getEmptyAddress();
			Address newAddress = getNewAddress(address1);
			newLearnerProfile.setLearnerAddress(newAddress);
		}
		
		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress(alternateAddress);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			alternateAddress = getEmptyAddress();
			Address newAddress2 = getNewAddress(alternateAddress);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		}
		
		return newLearnerProfile;
	}
	
	private Address getNewAddress(com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address) {
		
		String streetAddress1 = address.getStreetAddress1();
		String streetAddress2 = address.getStreetAddress2();
		String city = address.getCity();
		String country = address.getCountry();
		String state = address.getState();
		String zipCode = address.getZipCode();
		
		Address newAddress = new Address();
		newAddress.setStreetAddress(streetAddress1);
		newAddress.setStreetAddress2(streetAddress2);
		newAddress.setCity(city);
		newAddress.setState(state);
		newAddress.setZipcode(zipCode);
		newAddress.setCountry(country);
		
		return newAddress;
		
	}
	
	private com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address getEmptyAddress() {
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address = new com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address();
		address.setCity("");
		address.setCountry("");
		address.setState("");
		address.setStreetAddress1("");
		address.setStreetAddress2("");
		address.setZipCode("");
		return address;
	}
	
	public Learner getUpdatedLearner(Customer customer, User user) throws Exception {
		
		String userName = user.getUserName();
		Learner updatedLearner = null;
		VU360User existingUser = vu360UserService.findUserByUserName(userName);
		if (existingUser == null) {
			String errorMessage = "No existing user found for: " + userName;
			throwException(errorMessage);
		} 
		
		updatedLearner = vu360UserServiceLmsApi.updateUser(customer, existingUser, user);
		return updatedLearner;
		
	}
	
	public LearnerProfile getUpdatedLearnerProfile(User user, VU360User updateableUser) {
		
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		
		LearnerProfile updatedLearnerProfile = updateableUser.getLearner().getLearnerProfile();
		updatedLearnerProfile = learnerService.loadForUpdateLearnerProfile(updatedLearnerProfile.getId());
		updatedLearnerProfile.setMobilePhone(mobilePhone);
		updatedLearnerProfile.setOfficePhone(phone);
		updatedLearnerProfile.setOfficePhoneExtn(extension);
		updatedLearnerProfile.setCustomFieldValues(null);
		
		if (address1 != null) {
			Address newAddress = getNewAddress(address1);
			updatedLearnerProfile.setLearnerAddress(newAddress);
		} else {
			address1 = getEmptyAddress();
			Address newAddress = getNewAddress(address1);
			updatedLearnerProfile.setLearnerAddress(newAddress);
		}
		
		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress(alternateAddress);
			updatedLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			alternateAddress = getEmptyAddress();
			Address newAddress2 = getNewAddress(alternateAddress);
			updatedLearnerProfile.setLearnerAddress2(newAddress2);
		}
		
		return updatedLearnerProfile;
	}
	
	public boolean updateLearnerAssociationOfOrgGroups (Learner learner, List<OrganizationalGroup> organizationalGroupList) 
			throws Exception {
		
		if (organizationalGroupList == null || organizationalGroupList.isEmpty()) {
			String errorMessage = "Organizational group list is null or empty. No organizationalGroup is found to associate to learner";
			throw new Exception(errorMessage);
		}
		
		Set<OrganizationalGroup> setOfOrgGroups = new HashSet<OrganizationalGroup>();
		for (OrganizationalGroup organizationalGroup : organizationalGroupList) {
			setOfOrgGroups.add(organizationalGroup);
		}
		
        updateAssociationOfOrgGroups(learner, setOfOrgGroups);
        return true;
		
	}
	
	private void updateAssociationOfOrgGroups(Learner learner, Set<OrganizationalGroup> setOfOrgGroups) {

        log.debug("updating associations of Org groups...");
        orgGroupLearnerGroupService.addRemoveOrgGroupsForLearner(learner, setOfOrgGroups);
        
    }
	
	public void sendEmailToLearners(Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap, String loginURL, VU360User user,
			Brander brander ) throws Exception {
		
		LmsApiEmailAsyncTask emailTask = new LmsApiEmailAsyncTask(brander, loginURL, user, learnerEnrollmentEmailMap, learnersToBeMailedService);
		asyncTaskExecutorWrapper.execute(emailTask);
	}

	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

}
