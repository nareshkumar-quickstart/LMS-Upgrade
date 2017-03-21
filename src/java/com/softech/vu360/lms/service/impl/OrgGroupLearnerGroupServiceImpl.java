package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;










import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerGroupMember;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.OrganizationalGroupMember;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.LearnerGroupItemRepository;
import com.softech.vu360.lms.repositories.LearnerGroupMemberRepository;
import com.softech.vu360.lms.repositories.LearnerGroupRepository;
import com.softech.vu360.lms.repositories.LearnerRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupMemberRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupRepository;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.util.ORMUtils;
import com.softech.vu360.util.FormUtil;

/**
 * @author Tapas Mondal
 *
 */
public class OrgGroupLearnerGroupServiceImpl implements OrgGroupLearnerGroupService {
	
	private static final Logger log = Logger.getLogger(OrgGroupLearnerGroupServiceImpl.class.getName());
	private LearnerService learnerService = null;
	
	@Autowired
	OrganizationalGroupRepository organizationalGroupRepository;
	
	@Autowired
	OrganizationalGroupMemberRepository organizationalGroupMemberRepository;
	
	@Autowired
	LearnerGroupRepository learnerGroupRepository;
	
	@Autowired
	LearnerGroupMemberRepository learnerGroupMemberRepository;
	
	@Autowired
	LearnerGroupItemRepository learnerGroupItemRepository;
	
	@Autowired
	LearnerRepository learnerRepository;
	
	public List<Learner> getLearnersByOrgGroupIds(Long orgGroupIdArray[]){
		
		List<Learner> learners = new ArrayList<Learner>();		
		learners = learnerRepository.getLearnerByOrganizationalGroups(orgGroupIdArray);
//		List<OrganizationalGroupMember> orgGroupMemberList = organizationalGroupMemberRepository.findByOrganizationalGroupIdIn(orgGroupIdArray);
//		
//		Learner learner = null;
//		Map<Long, Learner> learnerMap = new HashMap<Long, Learner>();
//		
//		if ( CollectionUtils.isNotEmpty(orgGroupMemberList) ) {
//			for ( OrganizationalGroupMember orgMember : orgGroupMemberList ) {
//				learner = learnerMap.get( orgMember.getLearner().getId() );
//				if (learner == null) {
//					learnerMap.put(orgMember.getLearner().getId(), orgMember.getLearner());
//					learners.add( orgMember.getLearner() );
//				}
//			}
//		}
		
		return learners;
	}

	public OrganizationalGroup getRootOrgGroupForCustomer(Long customerId) {
		List<OrganizationalGroup> groups = organizationalGroupRepository.findByCustomerIdAndRootOrgGroupIsNull(customerId);
		
		if ( groups.size() > 0 ) {
			return groups.get(0);
		}
		return null;
	}

	public List<Learner> getLearnersByLearnerGroupIds(Long learnerGroupIdArray[]){
		 
		List<Learner> learners = new ArrayList<Learner>();		
		List<LearnerGroupMember> lrnGroupMemberList = learnerGroupMemberRepository.findByLearnerGroupIdIn(learnerGroupIdArray);
		Map<Long, Learner> learnerMap = new HashMap<Long, Learner>();
		Learner learner = null;
		
		if ( CollectionUtils.isNotEmpty(lrnGroupMemberList) ) {			
			for ( LearnerGroupMember lrgMember : lrnGroupMemberList ) {				
				learner = learnerMap.get( lrgMember.getLearner().getId() );
				if (learner == null) {
					learnerMap.put(lrgMember.getLearner().getId(), lrgMember.getLearner());
					learners.add( lrgMember.getLearner() );
				}
			}
		}
		
		return learners;
	}
	@Transactional
	public List<LearnerGroupMember> addRemoveLearnerGroupsForLearner(Learner learner, Set<LearnerGroup> learnerGroups){
		
		learnerGroupMemberRepository.deleteByLearnerId(learner.getId());
		
		LearnerGroupMember member = null;
		List<LearnerGroupMember> addedSuccssfully = new ArrayList<LearnerGroupMember>();
		for(LearnerGroup lg:learnerGroups){
			member = new LearnerGroupMember();
			member.setLearner(learner);
			member.setLearnerGroup(lg);
			LearnerGroupMember newLearnerGroups = 
					learnerGroupMemberRepository.saveLGM(member);
			addedSuccssfully.add(newLearnerGroups);
		}
		return addedSuccssfully;
	}
	
	@Transactional
	public List<OrganizationalGroupMember> addRemoveOrgGroupsForLearner(Learner learner, Set<OrganizationalGroup> orgGroups){
		organizationalGroupMemberRepository.deleteByLearnerId(learner.getId());
		
		OrganizationalGroupMember member = null;
		List<OrganizationalGroupMember> addedSuccssfully = new ArrayList<OrganizationalGroupMember>();
		for(OrganizationalGroup og:orgGroups){
			member = new OrganizationalGroupMember();
			member.setLearner(learner);
			member.setOrganizationalGroup(og);
			addedSuccssfully.add(organizationalGroupMemberRepository.saveOGM(member));
		}
		return addedSuccssfully;
	}
	public OrganizationalGroup addOrgGroup(OrganizationalGroup organizationalGroup){
		return organizationalGroupRepository.save(organizationalGroup);
	}
	
	public LearnerGroup addLearnerGroup(LearnerGroup learnerGroup){
		return learnerGroupRepository.save(learnerGroup);
	}
	
	@Transactional
	public void deleteLearnerGroups(long learnerGroupIdArray[]){
		for (int i = 0; i < learnerGroupIdArray.length; ++i) {
			LearnerGroup LGM = learnerGroupRepository.findOne(learnerGroupIdArray[i]);
			if(LGM!=null){
				List<LearnerGroupItem> learnerGroupItems= LGM.getLearnerGroupItems();
				if(!learnerGroupItems.isEmpty()) {
					learnerGroupItemRepository.delete(learnerGroupItems);
				}
				learnerGroupRepository.delete(LGM);
			}
			}	
	}

	@Transactional
	public void deleteOrgGroups(long orgGroupIdArray[]){
		Long orgGroupIdArrayOjects[] = ArrayUtils.toObject(orgGroupIdArray);
		OrganizationalGroup og;
		List<LearnerGroup> ogLearnerGroups = null;
		for (int i = 0; i < orgGroupIdArrayOjects.length; ++i) {
			
			og = organizationalGroupRepository.findOne(orgGroupIdArrayOjects[i]);
			
			if(og!=null){
				
				ogLearnerGroups = learnerGroupRepository.findByOrganizationalGroupId(og.getId());
				for (LearnerGroup lg : ogLearnerGroups) {
					learnerGroupRepository.delete(lg.getId());
				}
				
				if(og.getParentOrgGroup()!=null){
					og.getParentOrgGroup().getChildrenOrgGroups().remove(og);
				}
				organizationalGroupRepository.deleteById(orgGroupIdArrayOjects[i]);
			}
	}
	}

	public List<OrganizationalGroup> getAllOrganizationalGroups(Long customerID){
		return organizationalGroupRepository.findByCustomerId(customerID);
	}
	
	public List<LearnerGroup> getAllLearnerGroups(Long customerId,VU360User loggedInUser){
		List<LearnerGroup> objlg = null;
		 if(!loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()){ 

			//LMS-19020 
         	List<OrganizationalGroup> ls = loggedInUser.getTrainingAdministrator().getManagedGroups();
         	//remove duplicate
         	if(ls!=null){
 	        	Set<OrganizationalGroup> gset = new HashSet<OrganizationalGroup>(ls);
 	        	ls = new ArrayList<OrganizationalGroup>(gset);
         	}
			 
			 Object[] orgGroupIdArray=FormUtil.getPropertyArrayFromList(ls);
			Long[] longArr = Arrays.copyOf(orgGroupIdArray, orgGroupIdArray.length, Long[].class);

			objlg=  learnerGroupRepository.findByCustomerIdAndOrganizationalGroupIdIn(customerId,longArr);
		 }else
			 objlg = learnerGroupRepository.findByCustomerId(customerId);
		 
		 return objlg;
	}

	public void deleteLearnersFromOrgGroup(Long learnerIdArray[],OrganizationalGroup orgGroup ){
		List<OrganizationalGroupMember> lstlgm = organizationalGroupMemberRepository.findByOrganizationalGroupIdLearnerIdIn(orgGroup.getId(), learnerIdArray);
		for (OrganizationalGroupMember lg : lstlgm) 
			organizationalGroupMemberRepository.delete(lg);
	}

    public void addLearnerToOrgGroup(Learner learner, OrganizationalGroup orgGroup ) {
        OrganizationalGroupMember member = new OrganizationalGroupMember();
    	member.setLearner(learner);
    	member.setOrganizationalGroup(orgGroup);
    	organizationalGroupMemberRepository.saveOGM(member);
    }
	public void deleteLearnersFromLearnerGroup(Long learnerIdArray[],LearnerGroup learnerGroup ){
		List<LearnerGroupMember> objlg = learnerGroupMemberRepository.findByLearnerGroupIdAndLearnerIdIn(learnerGroup.getId(), learnerIdArray);
		for (LearnerGroupMember lg : objlg) 
			learnerGroupMemberRepository.delete(lg);
	}

	public OrganizationalGroup saveOrgGroup(OrganizationalGroup orgGroup){
		orgGroup = organizationalGroupRepository.save(orgGroup);

		OrganizationalGroup root;
		if(orgGroup.getParentOrgGroup() !=null){
			root = orgGroup.getParentOrgGroup().getRootOrgGroup();
		}else{
			root = orgGroup;
		}
		orgGroup.setRootOrgGroup(root);
		orgGroup = organizationalGroupRepository.save(orgGroup);

		return orgGroup;

	}
	
	public OrganizationalGroup getRootOrganizationalGroupForUser(Customer customer){
		OrganizationalGroup orgGroup = getRootOrgGroupForCustomer(customer.getId());
		if(orgGroup == null){
			orgGroup = new OrganizationalGroup();
			orgGroup.setName(customer.getName());
			orgGroup.setCustomer(customer);
			orgGroup.setRootOrgGroup(null);
			orgGroup.setParentOrgGroup(null);
			saveOrgGroup(orgGroup);
		}
		return orgGroup;
	}
	
	public List<OrganizationalGroup>  getOrgGroupByNames(String orgGroupNames[],Customer customer){
		return organizationalGroupRepository.findByCustomerIdAndNameIn(customer.getId(), orgGroupNames);
	}
	
	public OrganizationalGroup getOrganizationalGroupById(Long id){
		return organizationalGroupRepository.findOne(id);
	}
	
	public List<OrganizationalGroup>  getOrgGroupsById(String orgGroupId[]){
		//return organizationalGroupRepository.findByIdIn(orgGroupId);
		return organizationalGroupRepository.findOrganizationGroupById(orgGroupId);
	}
	
	public LearnerGroup loadForUpdateLearnerGroup(long id){
		return learnerGroupRepository.findOne(id);
	}
	
	public  OrganizationalGroup  loadForUpdateOrganizationalGroup(long id){
		return organizationalGroupRepository.findOne(id);
	}
	
	public List<OrganizationalGroup>  getOrgGroupsByCustomer(Customer customer){
		return organizationalGroupRepository.findByCustomerId(customer.getId());
	}
	
	@Transactional
	public List<OrganizationalGroup>  getOrgGroupsByLearner(Learner learner){ // TODO verify
		List<OrganizationalGroup> orgGroups = new ArrayList<OrganizationalGroup>();
		
		List<OrganizationalGroupMember> memberships = organizationalGroupMemberRepository.findByLearnerIdOrderByOrganizationalGroupNameAsc(learner.getId());
		
		for(OrganizationalGroupMember membership:memberships){
			orgGroups.add(ORMUtils.initializeAndUnproxy(membership.getOrganizationalGroup()));
		}
		return orgGroups; 
	}
	
	public List<LearnerGroup>  getLearnerGroupsByLearner(Learner learner){
		List<LearnerGroup> learnerGroups = new ArrayList<LearnerGroup>();
		
		learnerGroups = learnerGroupRepository.findByLearnerIdOrderByLearnerGroupNameAsc(learner.getId());
		
		return learnerGroups; 
	}
	
	public void  ManageOrgAndLearnerGroup(List<OrganizationalGroup> organizationalGroups,List<LearnerGroup> learnerGroups,Learner learner){
		addRemoveOrgGroupsForLearner(learner, new HashSet<OrganizationalGroup>(organizationalGroups));
		addRemoveLearnerGroupsForLearner(learner, new HashSet<LearnerGroup>(learnerGroups));
	}
	
	public LearnerGroup saveLearnerGroup(LearnerGroup learnerGroup){
		return	learnerGroupRepository.save(learnerGroup);
	}

    public LearnerGroup getLearnerGroupByLearnerGroupId(long id)
    {
        return learnerGroupRepository.findOne(id);
    }
    
	public OrganizationalGroup saveOrganizationalGroupFromBatchImport(OrganizationalGroup orgGroup){
		return	organizationalGroupRepository.save(orgGroup);
	}

	public OrganizationalGroup saveOrganizationalGroup(OrganizationalGroup orgGroup){
		return	organizationalGroupRepository.save(orgGroup);
	}
	
	public OrganizationalGroup saveOrganizationalGroupForCustomerProfile(OrganizationalGroup orgGroup){
		return	organizationalGroupRepository.save(orgGroup);
	}

	public List<LearnerGroup> findLearnerGroupByName(String name, Customer customer,VU360User loggedInUser) {
		Object[] orgGroupIdArray=FormUtil.getPropertyArrayFromList(loggedInUser.getTrainingAdministrator().getManagedGroups());
		Long[] longArr = Arrays.copyOf(orgGroupIdArray, orgGroupIdArray.length, Long[].class);

		if(!loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups())
			return learnerGroupRepository.findByCustomerIdAndOrganizationalGroupIdInAndNameLike(customer.getId(), longArr , name);
		else
			return learnerGroupRepository.findByCustomerIdAndNameLike(customer.getId(), name);
	}
	
	public OrganizationalGroup  getOrgGroupsByHiererchyString( String spliter, String hiererchy, Customer customer, VU360User loggedInUser){
		 try
	        {
	            log.info(" - - - - - getOrgGroupsByHiererchyString : START - - - - - ");
	            StringTokenizer orgGroupNames = new StringTokenizer(hiererchy, spliter);
	            OrganizationalGroup rootOrgGroup = this.getRootOrgGroupForCustomer(customer.getId());
	            log.info("rootOrgGroup.getName() = " + rootOrgGroup.getName());
	            OrganizationalGroup createdOrgGroup = null;
	            int i = 0;
	            int spliterCount = orgGroupNames.countTokens() - 1;
	            log.info("spliterCount = " + spliterCount);
	            OrganizationalGroup lastOrgGroup = null;

	            while( orgGroupNames.hasMoreTokens() )
	            {
	                log.info("i = " + i);
	                String orgGroupName = orgGroupNames.nextToken().trim();
	                log.info("orgGroupName = " + orgGroupName);
	                if( i == 0 )
	                {
	                    if( !orgGroupName.equals(rootOrgGroup.getName()) )
	                        return null;
	                    else
	                    {
	                        if( i == spliterCount )
	                            createdOrgGroup = rootOrgGroup;
	                        lastOrgGroup = rootOrgGroup;
	                    }
	                }
	                else
	                {
	                    String orgGroupStringArray[]= new String[1];
	                    orgGroupStringArray[0] = orgGroupName;
	                    List<OrganizationalGroup> orgGroupListByName = this.getOrgGroupByNames(orgGroupStringArray, customer);
	                    log.info("orgGroupListByName = " + orgGroupListByName);
	                    if( orgGroupListByName != null && !orgGroupListByName.isEmpty() )
	                    {
	                        boolean isMatch = false;
	                        for( OrganizationalGroup orggroup : orgGroupListByName )
	                        {
	                            if(orggroup.getParentOrgGroup().getId().longValue() == lastOrgGroup.getId().longValue())
	                            {
	                                if( i == spliterCount )
	                                    createdOrgGroup = orggroup;
	                                isMatch = true;
	                                lastOrgGroup = orggroup;
	                                break;
	                            }
	                        }
	                        log.info("isMatch = " + isMatch);
	                        if( !isMatch )
	                        {
	                            OrganizationalGroup newOrgGroup = this.createOrgGroup(customer,orgGroupName,rootOrgGroup,lastOrgGroup);
	                            if( i == spliterCount )
	                                createdOrgGroup = newOrgGroup;
	                            lastOrgGroup = newOrgGroup;
	                        }
	                    }
	                    else
	                    {
	                        log.info("loggedInUser.isLMSAdministrator() = " + loggedInUser.isLMSAdministrator());
	                        log.info("loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() = " +
	                                loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups());
	                        if( loggedInUser.isLMSAdministrator() || loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups() )
	                        {
	                            OrganizationalGroup newOrgGroup = this.createOrgGroup(customer,orgGroupName,rootOrgGroup,lastOrgGroup);
	                            if( i == spliterCount )
	                                createdOrgGroup = newOrgGroup;
	                            lastOrgGroup = newOrgGroup;
	                        }
	                        else
	                        {
	                            boolean hasPermission = false;
	                            for(OrganizationalGroup permitedOrgGroup :loggedInUser.getTrainingAdministrator().getManagedGroups())
	                            {
	                                if(permitedOrgGroup.getName().equals(lastOrgGroup.getName()))
	                                {
	                                    hasPermission = true;
	                                    break;
	                                }
	                            }
	                            log.info("hasPermission = " + hasPermission);
	                            if(hasPermission)
	                            {
	                                OrganizationalGroup newOrgGroup = this.createOrgGroup(customer,orgGroupName,rootOrgGroup,lastOrgGroup);
	                                if( i == spliterCount )
	                                    createdOrgGroup = newOrgGroup;
	                                lastOrgGroup = newOrgGroup;
	                            }
	                            else
	                                return null;
	                        }
	                    }
	                }
	                i++;
	            }
	            return createdOrgGroup;
	        }
	        finally
	        {
	            log.info(" - - - - - getOrgGroupsByHiererchyString : END - - - - - ");
	        }
	}
	
	public OrganizationalGroup createOrgGroup(Customer customer,String name,OrganizationalGroup rootOrgGroup,OrganizationalGroup parentOrgGroup){
		OrganizationalGroup newOrgGroup = new OrganizationalGroup();
		newOrgGroup.setName(name);
		newOrgGroup.setRootOrgGroup(rootOrgGroup);
		parentOrgGroup = this.learnerService.getOrganizationalGroupById(parentOrgGroup.getId());
		newOrgGroup.setParentOrgGroup(parentOrgGroup);
		newOrgGroup.setCustomer(customer);
		this.addOrgGroup(newOrgGroup);
		return newOrgGroup;
	}

    public OrganizationalGroup createOrgGroup2(Customer customer,String name,OrganizationalGroup rootOrgGroup,OrganizationalGroup parentOrgGroup){
        OrganizationalGroup newOrgGroup = new OrganizationalGroup();
        newOrgGroup.setName(name);
        newOrgGroup.setRootOrgGroup(rootOrgGroup);
        newOrgGroup.setParentOrgGroup(parentOrgGroup);
        newOrgGroup.setCustomer(customer);
        newOrgGroup = addOrgGroup(newOrgGroup);
        return newOrgGroup;
    }
	
	public void deleteLearnerGroupItems(List<LearnerGroupItem> learnerGroupItems) {
		learnerGroupItemRepository.delete(learnerGroupItems);
	}
    public OrganizationalGroupMember addMemberInOrgGroup(OrganizationalGroupMember member){
    	return organizationalGroupMemberRepository.saveOGM(member);
    }
    
	public LearnerGroupMember addMemberInLearnerGroup(LearnerGroupMember member){
    	return learnerGroupMemberRepository.save(member);
    }
	
    public void deleteMemberFromLearnerGroup(LearnerGroupMember member){
    	learnerGroupMemberRepository.delete(member);
    }

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public Map<Long,Long> getLearnerCountOfLearnerGroups(List<LearnerGroup> learnerGroups){
		Long[] ids = new Long[learnerGroups.size()];
		int counter=0;
		for(LearnerGroup lg:learnerGroups){
			ids[counter++] = lg.getId();
		}
	
		HashMap<Long,Long> learnerGroupMap = null;
		if(ids.length>0){
		List<Object[]> learnerGroup = learnerGroupRepository.getLearnerCountByLearnerGroup(ids);
		learnerGroupMap = new HashMap<Long, Long>();
		
		for (int i=0; i< learnerGroup.size(); i++){
			learnerGroupMap.put(Long.valueOf(learnerGroup.get(i)[1].toString()), Long.valueOf(learnerGroup.get(i)[0].toString()));
		}
		
		//LMS-6764 : Set learner count to zero, if not fetched from database
		if (learnerGroupMap.size() < ids.length) {
			Long tempCount = null;
			for (int i=0; i< ids.length; i++) {
				tempCount = learnerGroupMap.get( ids[i] );
				if (tempCount == null) {
					learnerGroupMap.put( Long.valueOf(ids[i].toString()) , 0L);
				}
			}
		}
		}
		return learnerGroupMap;
	}

    public void deleteMemberFromOrgGroup(OrganizationalGroupMember member){
    	organizationalGroupMemberRepository.delete(member);
    }
	
	public List<LearnerGroup> getLearnerGroupsByOrgGroup(Long orgGroupId){
		return learnerGroupRepository.findByOrganizationalGroupId(orgGroupId);
	}
	
	public List<Learner> getLearnersByLearnerGroupId(Long learnerGroupId){
		return learnerRepository.findByLearnerGroupId(learnerGroupId);
	}

	public List<Learner> getLearnersByOrgGroupId(Long orgGroupId){
		List<Learner> learners = new ArrayList<Learner>();
		List<OrganizationalGroupMember> members = organizationalGroupMemberRepository.findByOrganizationalGroupId(orgGroupId); 
		for(OrganizationalGroupMember member:members){
			learners.add(member.getLearner());
		}
		return learners;
	}
	
	public Map<Long,Long> getLearnerCountOfOrgGroups(Long customerID){
		// Modified By Marium Saud : The method param changed to CustomerID from List<OrganizationalGroup> inorder to avoid unnecessarily queries for childrenOrgGroup that is marked as 'EAGER'
		// Now the count for Learner Against OrgGrp retreived by passing CustomerID
		List<Object[]> learnerGroup = learnerGroupRepository.getLearnerCountByOrganizationalGroup(customerID);
		HashMap<Long,Long> learnerGroupMap = new HashMap<Long, Long>();
		
		for (int i=0; i< learnerGroup.size(); i++){
			learnerGroupMap.put(Long.valueOf(learnerGroup.get(i)[1].toString()), Long.valueOf(learnerGroup.get(i)[0].toString()));
		}

		return learnerGroupMap;
	}

	public OrganizationalGroup createOrganiationalGroupForDefaultCustomer(Customer customer){
		OrganizationalGroup orgGroup = new OrganizationalGroup();
		orgGroup.setName(customer.getName());
		orgGroup.setCustomer(customer);
		orgGroup.setRootOrgGroup(null);
		orgGroup.setParentOrgGroup(null);
		List<OrganizationalGroup> organizationalGroup = new ArrayList <OrganizationalGroup>();
		organizationalGroup.add(orgGroup);
		return saveOrgGroup(orgGroup);
	}
	
	//Added By Marium Saud: New method created when Enrollments are done , because for each learner enrollment,  Organizational Group
    // will be fetched along with its all children (as List<OrganizationalGroup> childrenOrgGroups) because 
	// in OrganizationalGroup.java the relation is marked as Eager (LMS-21545,LMS-21541)
	@Transactional
	public List<OrganizationalGroup>  getOrgGrpsForLearner(Learner learner){ // TODO verify
		List<OrganizationalGroup> orgGroups = new ArrayList<OrganizationalGroup>();
		
		orgGroups = organizationalGroupRepository.findByLearnerIdOrderByOrgGrpNameAsc(learner.getId());
		
		return orgGroups; 
	}

	//Added By MariumSaud : New method created that is similar to orgGroupLearnerGroupService.getLearnersByLearnerGroupId(learnerGroup.getId());
	// Reason : While fetching Learner , bulk queries are being executed for VU360User which is causing Proxy Error
	@Override
	public List<Learner> getAllLearnersByLearnerGroupId(Long learnerGroupId) {
		return learnerRepository.findLearnerByLearnerGroupID(learnerGroupId);
	}

	@Override
	public List<OrganizationalGroup> getOrgGroupsByLearners(Long[] learnerIds) {
		List<OrganizationalGroup> orgGroups = new ArrayList<OrganizationalGroup>();
		List<OrganizationalGroupMember> memberships = new ArrayList<OrganizationalGroupMember>();
		try {
			memberships = organizationalGroupMemberRepository.findDistinctByLearnerIdIn(learnerIds);
		}catch(ObjectNotFoundException e){
			if (log.isDebugEnabled()) {
				log.debug("Learners :"+learnerIds);
			}
		}
		for(OrganizationalGroupMember membership:memberships){
			orgGroups.add(membership.getOrganizationalGroup());
		}
		return orgGroups; 
	}

	@Override
	public List<LearnerGroup> getLearnerGroupsByLearnerGroupIDs(Long[] learnerGroupId) {
		return learnerGroupRepository.findByIdIn(learnerGroupId);
	}

	@Override
	@Transactional
	public OrganizationalGroup getOrgGroupById(Long orgGroupId) {
		return organizationalGroupRepository.findOrganizationGroupById(orgGroupId);
	}
}