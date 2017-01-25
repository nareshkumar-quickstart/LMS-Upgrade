package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;
import com.softech.vu360.util.Brander;

public interface LearnerServiceLmsApi {
	
	public Map<String, Object> getLearnersMap(List<String> userNameList, String customerCode) throws Exception;
	public void sendEmailToLearners(Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap, String loginURL, VU360User user,
			Brander brander ) throws Exception;
	
	public List<Learner> getLearnersList(List<Long> learnerGroupIdList) throws Exception;
	public Map<String, Object> getLearnersBelongToCustomer(List<Learner> learnersList, String customerCode) throws Exception;
	public Learner getNewLearner(Customer customer, User user) throws Exception;
	public Learner getUpdatedLearner(Customer customer, User user) throws Exception;
	public LearnerProfile getUpdatedLearnerProfile(User user, VU360User updateableUser);
	public boolean updateLearnerAssociationOfOrgGroups (Learner learner, List<OrganizationalGroup> organizationalGroupList) 
			throws Exception;
	

}
