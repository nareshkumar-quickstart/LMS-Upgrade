package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerGroupMember;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.OrganizationalGroupMember;
import com.softech.vu360.lms.model.VU360User;

/**
 * OrgGroupLearnerGroupService defines the set of interfaces 
 * to control the interactions and business logic
 * of all groups (OrganizationalGroup and LearnerGroups)
 * within the LMS.
 * 
 * @author jason
 *
 */
public interface OrgGroupLearnerGroupService {
	public List<Learner> getLearnersByOrgGroupIds(Long orgGroupIdArray[]);
	public LearnerGroup addLearnerGroup(LearnerGroup learnerGroup);
	public void deleteLearnerGroups(long learnerGroupIdArray[]);
	public void deleteOrgGroups(long orgGroupIdArray[]);
	public List<LearnerGroup> getAllLearnerGroups(Long customerId,VU360User loggedInUser);
	public List<OrganizationalGroup> getAllOrganizationalGroups(Long customerID);
	public void deleteLearnersFromOrgGroup(Long learnerIdArray[],OrganizationalGroup orgGroup );
    public void addLearnerToOrgGroup(Learner learner, OrganizationalGroup orgGroup );
	public void deleteLearnersFromLearnerGroup(Long learnerIdArray[],LearnerGroup learnerGroup );
	public OrganizationalGroup addOrgGroup(OrganizationalGroup organizationalGroup);
	public List<OrganizationalGroupMember> addRemoveOrgGroupsForLearner(Learner learner, Set<OrganizationalGroup> orgGroups);
	public List<LearnerGroupMember> addRemoveLearnerGroupsForLearner(Learner learner, Set<LearnerGroup> learnerGroups);
	public List<Learner> getLearnersByLearnerGroupIds(Long learnerGroupIdArray[]);
	public OrganizationalGroup saveOrgGroup(OrganizationalGroup orgGroup);
	public List<OrganizationalGroup>  getOrgGroupByNames(String orgGroupNames[],Customer customer);
	public OrganizationalGroup getOrganizationalGroupById(Long id);
	public List<OrganizationalGroup>  getOrgGroupsById(String orgGroupId[]);
	public List<OrganizationalGroup>  getOrgGroupsByCustomer(Customer customer);
	public List<OrganizationalGroup>  getOrgGroupsByLearner(Learner learner);
	public List<LearnerGroup>  getLearnerGroupsByLearner(Learner learner);
	public void  ManageOrgAndLearnerGroup(List<OrganizationalGroup> organizationalGroups,List<LearnerGroup> learnerGroups,Learner learner);
	public LearnerGroup saveLearnerGroup(LearnerGroup learnerGroup);
	public OrganizationalGroup saveOrganizationalGroup(OrganizationalGroup orgGroup);
	public OrganizationalGroup saveOrganizationalGroupFromBatchImport(OrganizationalGroup orgGroup);
	public OrganizationalGroup getRootOrgGroupForCustomer(Long customerId);
    public LearnerGroup getLearnerGroupByLearnerGroupId(long id);
	public List<LearnerGroup>  getLearnerGroupsByLearnerGroupIDs(String learnerGroupId[]);
	public List<LearnerGroup> findLearnerGroupByName( String name, Customer customer,VU360User loggedInUser);
	public OrganizationalGroup getOrgGroupsByHiererchyString(String spliter,String hiererchy,Customer customer,VU360User loggedInUser);
	public OrganizationalGroup saveOrganizationalGroupForCustomerProfile(OrganizationalGroup orgGroup);

    OrganizationalGroup createOrgGroup(Customer customer,String name,OrganizationalGroup rootOrgGroup,OrganizationalGroup parentOrgGroup);
    OrganizationalGroup createOrgGroup2(Customer customer,String name,OrganizationalGroup rootOrgGroup,OrganizationalGroup parentOrgGroup);
	public LearnerGroup   loadForUpdateLearnerGroup(long id);
	public  OrganizationalGroup  loadForUpdateOrganizationalGroup(long id);
	public void deleteLearnerGroupItems(List<LearnerGroupItem> learnerGroupItems);
	public List<Learner> getLearnersByLearnerGroupId(Long learnerGroupId);
	public List<Learner> getLearnersByOrgGroupId(Long orgGroupId);
	public List<LearnerGroup> getLearnerGroupsByOrgGroup(Long orgGroupId);
    public OrganizationalGroupMember addMemberInOrgGroup(OrganizationalGroupMember member);
    public void deleteMemberFromOrgGroup(OrganizationalGroupMember member);
    public LearnerGroupMember addMemberInLearnerGroup(LearnerGroupMember member);
    public void deleteMemberFromLearnerGroup(LearnerGroupMember member);
	public Map<Long,Long> getLearnerCountOfLearnerGroups(List<LearnerGroup> learnerGroups);
	public Map<Long,Long> getLearnerCountOfOrgGroups(Long customerID);
	public OrganizationalGroup getRootOrganizationalGroupForUser(Customer customer);	
	
	public List<OrganizationalGroup>  getOrgGrpsForLearner(Learner learner);
	public List<Learner> getAllLearnersByLearnerGroupId(Long id);
	public List<OrganizationalGroup>  getOrgGroupsByLearners(Long learnerIds[]);
	public List<LearnerGroup>  getLearnerGroupsByLearnerGroupIDs(Long learnerGroupId[]);
}